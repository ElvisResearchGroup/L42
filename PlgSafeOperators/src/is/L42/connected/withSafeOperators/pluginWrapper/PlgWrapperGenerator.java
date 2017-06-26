package is.L42.connected.withSafeOperators.pluginWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Optional;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.Expression;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.From;
import facade.L42;
import facade.Parser;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.UnresolvedOverloading;
import facade.L42.ExecutionStage;
import facade.PData;
import platformSpecific.fakeInternet.OnLineCode;
import platformSpecific.fakeInternet.PluginWithPart;
import platformSpecific.fakeInternet.PluginWithPart.PlgInfo;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import tools.Assertions;

public class PlgWrapperGenerator {
  private static List<ClassB.Member> templateWrapper=((ClassB)parseAndDesugar(
    " {class method\n"+
    " mut This0 #from(Library binaryRepr)\n"+
    " read method\n"+
    " Library #binaryRepr()\n"+
    " class method\n"+
    " Void #exceptionIf(Library binaryRepr) exception This\n"+
    "   use This check instanceof(_this:binaryRepr)\n"+
    "   exception This.#from(binaryRepr:binaryRepr)\n"+
    "}")).getMs();
 
  private static MCall templateUsingExc=(MCall) ((ClassB)parseAndDesugar(
    " {class method This m()\n"+
    " This.#from(binaryRepr:(\n"+
    "   res=use This0 check m(_:This.#binaryRepr())\n"+
    "     error This.#pluginUnresponsive(binaryRepr:void)\n"+
    "   catch error Library x (\n"+
    "     This.#exceptionIf(binaryRepr:x)\n"+
    "     error x\n"+
    "     )\n"+
    "   res\n"+
    "   ))\n"+
    "}\n"+
    "")).getMs().get(0).getInner();
  
private static ExpCore parseAndDesugar(String s) {
  Expression code1=Parser.parse("PlgWrapperGenerator",s);
  Expression code2=Desugar.of(code1);
  return code2.accept(new InjectionOnCore());
  }


//-----------------------------------------------------
  public static ClassB main(PData data,ClassB l) throws UnresolvedOverloading, ClassUnfit, MethodUnfit{
    return plgComplete(data.p.evilPush(l));
    }
  public static ClassB plgComplete(Program p) throws UnresolvedOverloading, ClassUnfit, MethodUnfit{//p contains the l to modify at top
    return plgComplete(Collections.emptyList(),p,p.top());
    }
  public static ClassB plgComplete(List<Ast.C>cs,Program p,ClassB l) throws UnresolvedOverloading, ClassUnfit, MethodUnfit{
    //p.top() is topL
    List<Member> ms=new ArrayList<>();
    for(Member m: l.getMs()){
      if(!(m instanceof NestedClass)){
        ms.add(m);
        continue;
        }
      NestedClass nc=(NestedClass)m;
      List<Ast.C>csc=new ArrayList<>(cs);
      csc.add(nc.getName());
      ms.add(nc.withInner(
        plgComplete(csc,p,(ClassB)nc.getInner()))
        );
      }
    return plgComplete1(cs,p,l.withMs(ms));
    //forall nested c in l
    // l=l[with c=plgComplete(cs:c, p,l.c)
    //return plgComplete1(cs,p,l)
    }
  public static int argLessPData(Method jm){
    Class<?>[]ps=jm.getParameterTypes();
    if(ps.length==0){return 0;}
    if(ps[0].equals(PData.class)){return ps.length-1;}
    return ps.length;
    //TODO:?assert others are not PData?
    }
  public static int argLessPData(Constructor<?> jc){
    Class<?>[]ps=jc.getParameterTypes();
    if(ps.length==0){return 0;}
    if(ps[0].equals(PData.class)){return ps.length-1;}
    return ps.length;
    //TODO:?assert others are not PData?
    }
  public static ClassB plgComplete1(List<Ast.C>cs,Program p,ClassB l) throws UnresolvedOverloading, ClassUnfit, MethodUnfit{
    PluginWithPart pwp = OnLineCode._isPluginWithPart(l.getDoc1());
    if(pwp==null){return l;}
    PlgInfo plgInfo=new PlgInfo(l.getDoc1());
    if(!hasPluginUnresponsive(l)){
      throw new RefactorErrors.ClassUnfit().msg(
        "Class "+Path.outer(0,cs) +" does not contain method #pluginUnresponsive(binaryRepr)");
      }
    Class<?> c=pwp.pointed;
    Method[] jms=c.getMethods();
    Constructor<?>[] jcs=c.getDeclaredConstructors();
    List<Member>msResult=new ArrayList<>();
    templateWrapper.forEach(m->msResult.add(m.withP(l.getP())));
    Path pTop=Path.outer(0, cs);
    for(Member m:l.getMs()){
      if (!(m instanceof MethodWithType)){
        msResult.add(m);
        continue;
        }
      MethodWithType mwt=(MethodWithType)m;
      if (mwt.get_inner().isPresent()){
        msResult.add(mwt);
        continue;
        }
      addMwt(p, plgInfo, jms,jcs, msResult, pTop, mwt);
    }
    return l.withMs(msResult);
    }


private static void addMwt(Program p, PlgInfo plgInfo, Method[] jms, Constructor<?>[] jcs, List<Member> msResult, Path pTop, MethodWithType mwt) throws UnresolvedOverloading, ClassUnfit, MethodUnfit {
//checks
  try{
    isOkAsReturn(p,pTop,mwt.getMt().getReturnType());
    for(Type ti:mwt.getMt().getExceptions()){
      isOkAsException(p,pTop,ti.getPath());
      }
    for(Type ti:mwt.getMt().getTs()){
      isOkAsParameter(p,pTop,ti);
      }//TODO: we may want to cache those tests if performance is needed
    }
  catch(ClassUnfit| MethodUnfit e){
    e.setMessage("While examining Class "+pTop+" method "+mwt.getMs()+":\n"+e.getMessage());
    throw e;
    }

  //add to msResult
  //TODO: add behaviour if mwt have special comment to define specific ms for use
  String name=mwt.getMs().nameToS();
  if(name.startsWith("#")){name=name.substring(1);}
  UsingInfo ui;
  if (!name.equals("new") && !name.equals("apply")) ui= usingMethod(plgInfo, jms, mwt, name);
  else ui=usingConstructor(plgInfo, jcs, mwt, name);
  MethodWithType tu=updateTemplateUsing(ui,mwt);
  msResult.add(tu);
}


private static UsingInfo usingMethod(PlgInfo plgInfo, Method[] jms, MethodWithType mwt, String name) throws UnresolvedOverloading {
  boolean stat=mwt.getMt().getMdf()==Mdf.Class;
  List<Method>jms0=new ArrayList<>();
  for (Method mi:jms){
    if (!mi.getName().equals(name)){continue;}
    if (argLessPData(mi)!=mwt.getMs().getNames().size()){continue;}
    if(stat!=Modifier.isStatic(mi.getModifiers())){continue;}
    jms0.add(mi);
    }
  if (jms0.size()!=1){
    String nonS=stat?"":"non ";
    throw new RefactorErrors.UnresolvedOverloading().msg(
      "The referred java class "+plgInfo.plgClass.getName()+
      " does not have exactly one "+nonS+"static method for "+name+" with right number of arguments.\n"+
      "List of candidate methods:"+jms0);
    }
  assert jms0.size()==1:
    jms0.size();
  Method jm=jms0.get(0);
  UsingInfo ui=new UsingInfo(plgInfo,jm);
  return ui;
  }

private static UsingInfo usingConstructor(PlgInfo plgInfo, Constructor<?>[] jcs, MethodWithType mwt, String name) throws UnresolvedOverloading {
  List<Constructor<?>>jcs0=new ArrayList<>();
  for (Constructor<?> mi:jcs){
    if (argLessPData(mi)!=mwt.getMs().getNames().size()){continue;}
    jcs0.add(mi);
    }
  if (jcs0.size()!=1){
  throw new RefactorErrors.UnresolvedOverloading().msg(
    "The referred java class "+plgInfo.plgClass.getName()+
    " does not have exactly one constructor with right number of arguments.\n"+
    "List of candidate constructors:"+jcs0);
  }
  assert jcs0.size()==1:
    jcs0.size();
  Constructor<?> jc=jcs0.get(0);
  UsingInfo ui=new UsingInfo(plgInfo,jc);
  return ui;
  }

  private static MethodWithType updateTemplateUsing(UsingInfo ui, MethodWithType mwt) {
    MCall e=templateUsingExc;
    MethodType mt=mwt.getMt();
    ExpCore.Block b=(Block) e.getEs().get(0);
    if (mwt.getMt().getExceptions().isEmpty()){b=b.withOns(Collections.emptyList());}
    ExpCore.Using u=(Using) b.getDecs().get(0).getInner();
    ExpCore.MCall p0=(MCall) u.getEs().get(0);//parameter expressions
    
    //e#mcall.inner<-mwt.retType.path1
    e=e.withInner(ExpCore.EPath.wrap(mt.getReturnType().getPath()));
    //u=u.withS(ui.usingMs);
    List<ExpCore> ues=new ArrayList<>();
    if(mt.getMdf()!=Mdf.Class){
      ues.add(p0.withInner(new ExpCore.X(mwt.getP(),"this")));
      }
    {int i=-1;for(String x: mwt.getMs().getNames()){i++;
      ExpCore pi=new ExpCore.X(mwt.getP(),x);
      boolean needAddBinaryRepr=true;
      Type ti=mwt.getMt().getTs().get(i);
      if(ti.equals(Type.immLibrary)){
        needAddBinaryRepr=false;
        }
      if(ti.getMdf()==Mdf.Class){
        needAddBinaryRepr=false;
        }
      if(needAddBinaryRepr){
        pi=p0.withInner(pi);  
        }
      ues.add(pi);
      }}
    u=new Using(u.getPath(),ui.usingMs,u.getDoc(),ues,u.getInner());
    String errorS=
      "plugin string: "+ui.plgInfo.plgString+"\n"+
      "plugin part: "+ui.plgInfo.plgName +"\n"+
      "method name: "+mwt.getMs() +"\n"+
      "java method: "+ui.plgInfo.plgClass.getName()+"."+ui.usingMs +"\n";
    //u.inner#mcall.es(0)<-EncodingHelper.wrapStringU()
    List<ExpCore> errorEs=Collections.singletonList(EncodingHelper.wrapStringU(errorS));
    Signal tmpS=(Signal)u.getInner();
    MCall tmpMc=((MCall)tmpS.getInner()).withEs(errorEs);
    u=u.withInner(tmpS.withInner(tmpMc));
    b=b.withDecs(Collections.singletonList(b.getDecs().get(0).withInner(u)));
    //--
    if (!mwt.getMt().getExceptions().isEmpty()){
      On on=b.getOns().get(0);
      Dec k0 = ((Block)on.getInner()).getDecs().get(0);
      List<Dec> ks=new ArrayList<>();
      {int i=-1;for(Type ti:mt.getExceptions()){i++;
        MCall mci=((MCall)k0.getInner()).withInner(ExpCore.EPath.wrap(ti.getPath()));
        ks.add(k0.withInner(mci).withX(k0.getX()+i));
        }}
      on=on.withInner(((Block)on.getInner()).withDecs(ks));
      b=b.withOns(Collections.singletonList(on));
      //k0=b.k(0).inner#block.decs(0)
      //k0 add more on need
      //ki.inner#mcall.inner<-Pi
      }
    boolean wrapRes=!mt.getReturnType().equals(Type.classAny) &&
            !mt.getReturnType().equals(Type.immLibrary);    
    if (ui.isVoid){b=b.withDecs(Collections.singletonList(b.getDecs().get(0).withT(Optional.of(Type.immVoid))));}
    if(!ui.isVoid && wrapRes){
      e=e.withEs(Collections.singletonList(b));
      mwt=mwt.withInner(e);
      }
    else{
      mwt=mwt.withInner(b);
      }
    return mwt;
    }


public static boolean hasPluginUnresponsive(ClassB l){
    //class method T #pluginUnresponsive(Library binaryRepr)
    MethodSelector ms=MethodSelector.of("#pluginUnresponsive",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(Type.immLibrary)){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().getMdf().equals(Mdf.Immutable)){return false;}
    return true;//no need to check return type, since is just thrown as error
    }
  public static boolean hasBinaryRepr(ClassB l){
    //read method Library #binaryRepr()
    MethodSelector ms=MethodSelector.of("#binaryRepr",Collections.emptyList());
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Readable)){return false;}
    if(!mt.getTs().isEmpty()){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().equals(Type.immLibrary)){return false;}
    return true;
    }
  public static boolean hasFrom(ClassB l){
    //   class method mut This0 #from(Library binaryRepr)
    MethodSelector ms=MethodSelector.of("#from",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(Type.immLibrary)){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().equals(Type.mutThis0)){return false;}
    return true;
    }

  public static boolean hasExceptionIf(ClassB l){
    //class method Void #exceptionIf(Library binaryRepr) exception This
    MethodSelector ms= MethodSelector.of("#exceptionIf",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(Type.immLibrary)){return false;}    
    if(mt.getExceptions().size()!=1){return false;}
    if(!mt.getExceptions().get(0).equals(Path.outer(0))){return false;}
    if(!mt.getReturnType().equals(Type.immVoid)){return false;}
    return true;//no need to check return type, since is just thrown as error
    }
  private static Path _pathForOutside(int dept,Path pi){
    if(pi.isPrimitive()){return null;}
    if(pi.outerNumber()<=dept){return null;}
    return pi.setNewOuter(pi.outerNumber()-dept);
    }
  private static void checkForInside(ClassB lTop,Path csTop,Path originalPath) throws ClassUnfit, MethodUnfit{
    if(originalPath.isPrimitive()){
      throw new RefactorErrors.MethodUnfit().msg(
        "Method signature not supported:\n parameters can not be Void/Any/fwd.\n"
        + "returns can not be Any/fwd\n"
        + "exceptions can not be Any/Void/Library");
      }
    Path cs=From.fromP(originalPath,csTop);
    assert cs.outerNumber()==0;
    List<Ast.C> cBar = cs.getCBar();
    ClassB lPointed=lTop.getClassB(cBar);
    Doc d=lPointed.getDoc1();
    if(d._getParameterForPlugin()==null ||d._getParameterForPluginPart()==null){
    throw new RefactorErrors.ClassUnfit().msg(
      "Class "+Path.outer(0,cBar)+" does not have @pluginPart annotation");
      }
    }
  private static void isOkAsParameter(Program p, Path csTop, Type ti) throws ClassUnfit, MethodUnfit {
    Path pi=ti.getPath();
    if(ti.getMdf()==Mdf.Class){return;}//class parameters are ok, and we just omit the .#binaryRepr() call
    if (pi.equals(Path.Library())){return;}//Libraries are ok and we just omit the .#binaryRepr() call
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.top(),csTop,pi); return;}
    ClassB l=p.extractClassB(op);//TODO: since p.top is topL, is it ok this extraction?
    boolean hasIt=hasBinaryRepr(l);
    boolean phOk=Functions.isComplete(ti);
    if(!hasIt){throw new RefactorErrors.ClassUnfit().msg(
      "Class "+op+" has no #binaryRepr() method");
      }if(!phOk){throw new RefactorErrors.MethodUnfit().msg(
      "Fwd types not allowed.");
      }
    }


  private static void isOkAsException(Program p, Path csTop, Path pi) throws ClassUnfit, MethodUnfit {
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.top(),csTop,pi);return;}
    ClassB l;try{ l=p.extractClassB(op);}
    catch(PathMetaOrNonExistant pne){
      if(pne.isMeta()){
        throw new RefactorErrors.ClassUnfit().msg(
            "Class "+op+" is still not available");
        }
      throw new RefactorErrors.ClassUnfit().msg(
            "Class "+op+" is missing/mispelled");
    }
    if(!hasExceptionIf(l)){throw new RefactorErrors.ClassUnfit().msg(
      "Class "+op+" has no method #exceptionIf(binaryRepr)");
      }
    }


  private static void isOkAsReturn(Program p, Path csTop, Type ti) throws ClassUnfit, MethodUnfit {
    Path pi=ti.getPath();
    if (pi.equals(Path.Void())){return;}
    if (pi.equals(Path.Library())){return;}//We will need to generate a simpler returning expression
    if (pi.equals(Path.Any()) && ti.getMdf()==Mdf.Class){return;}
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.top(),csTop,pi); return;}
    ClassB l=p.extractClassB(op);
    boolean hasIt=hasFrom(l);
    boolean phOk=Functions.isComplete(ti);
    if (ti.getMdf()==Mdf.Class && !ti.equals(Type.classAny)){
      throw new RefactorErrors.MethodUnfit().msg("Return type can be 'class' only if is exactly 'class any'");
      }
    if(!hasIt){throw new RefactorErrors.ClassUnfit().msg(
      "Class "+op+" has no method #from(binaryRepr)");
      }
    if(!phOk){//TODO: why this limitation?
      throw new RefactorErrors.MethodUnfit().msg("Return type can not be fwd");
      }
    }   
 
}


/*
invalidMethodType
  where: Path, ms
  what: invalid ret/par/exc type
  why: no *primitive par/ret/exc void
       method *123 not present in external ref
       plg part not internal ref
       no class/fwd par/ret
 
  no plgunresponsive
    where: Path, ms

overloading
  where: Path, ms
  in JavaClass selector/num have x variations/ is not present
  
  
  LocationIssue
    Any Operator cause //this will store some of the parameters of the operation
    Library originalInput
    internal/ext
    Path /ClassAny
    opt ms
    opt sublocation (0 ret 1..n pars -1..-k exceptions, this??)
    size code
    issue string
    LocationIssue next?

    
General exceptions for Refactor
  SelectorUnfit
  PathUnfit
  MethodClash //2 methods not ok together
  MethodUnfit //1 method not ok shape
  ClassClash
  ClassUnfit
  
  privacycoupuled
  incoherentRedirectMapping
  cicular interface implements induced
  overloading  
    
  error may talk about extern
  have field internalLocation to talk about why pointing out
    
*/
//TODO: if a class is internal and is not plugin with part, is it ok if it has the right methods anyway??
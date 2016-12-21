package is.L42.connected.withSafeOperators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.NormType;
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
import ast.ExpCore.Using;
import ast.Expression;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;
import coreVisitors.From;
import facade.L42;
import facade.Parser;
import facade.L42.ExecutionStage;
import platformSpecific.fakeInternet.OnLineCode;
import platformSpecific.fakeInternet.PluginWithPart;
import platformSpecific.fakeInternet.PluginWithPart.PlgInfo;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import tools.Assertions;

public class PlgWrapperGenerator {
  List<ClassB.Member> templateWrapper=((ClassB)parseAndDesugar(
    " {class method\n"+
    " mut This0 #from(Library binaryRepr)\n"+
    " read method\n"+
    " Library #binaryRepr()\n"+
    " class method\n"+
    " Void #exceptionIf(Library binaryRepr) exception This\n"+
    "   use This check _%%%(binaryRepr:binaryRepr)\n"+
    "   error This.#pluginUnresponsive(binaryRepr:binaryRepr)\n}"+
    "")).getMs();
  MCall templateUsingExc=(MCall) ((ClassB)parseAndDesugar(
    " {class method This m()\n"+
    " This.#from(binaryRepr:(\n"+
    "   Library res=use This0 check m(_:This.binaryRepr())\n"+
    "     error This.#pluginUnresponsive(binaryRepr:void)\n"+
    "   catch error Library x (\n"+
    "     This.#exceptionIf(binaryRepr:x)\n"+
    "     error x\n"+
    "     )\n"+
    "   res\n"+
    "   ))\n"+
    "}\n"+
    "")).getMs().get(0).getInner();
  
  MCall templateUsing=(MCall) ((ClassB)parseAndDesugar(
  " {class method This m()\n"+
  " This.#from(binaryRepr:(\n"+
  "   Library res=use This0 check m(_:This.binaryRepr())\n"+
  "     error This.#pluginUnresponsive(binaryRepr:void)\n"+
  "   res\n"+
  "   ))\n"+
  " }\n"+
  "")).getMs().get(0).getInner();
       
private ExpCore parseAndDesugar(String s) {
  Expression code1=Parser.parse("PlgWrapperGenerator",s);
  Expression code2=Desugar.of(code1);
  return code2.accept(new InjectionOnCore());
  }


//-----------------------------------------------------
  public ClassB plgComplete(Program p){//p contains the l to modify at top
    return plgComplete(Collections.emptyList(),p,p.topCb());
    }
  public ClassB plgComplete(List<String>cs,Program p,ClassB l){
    //p.top() is topL
    List<Member> ms=new ArrayList<>();
    for(Member m: l.getMs()){
      if(!(m instanceof NestedClass)){
        ms.add(m);
        continue;
        }
      NestedClass nc=(NestedClass)m;
      List<String>csc=new ArrayList<>(cs);
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
    //TODO: if(ps[0]==Resouces.PData.class){return ps.length-1;}
    return ps.length;
    //TODO:?assert others are not PData?
    }
  public ClassB plgComplete1(List<String>cs,Program p,ClassB l){
    PluginWithPart pwp = OnLineCode._isPluginWithPart(l.getDoc1());
    PlgInfo plgInfo=new PlgInfo(l.getDoc1());
    if(pwp==null){return l;}
    if(!hasPluginUnresponsive(l)){throw Assertions.codeNotReachable("Add error for no #pluginUnresponsive(binaryRepr)");}//TODO:
    Class<?> c=pwp.pointed;
    Method[] jms=c.getMethods();
    List<Member>msResult=new ArrayList<>(templateWrapper);            
    Path pTop=Path.outer(0, cs);
    for(Member m:l.getMs()){
      if (!(m instanceof MethodWithType)){
        msResult.add(m);
        continue;
        }
      MethodWithType mwt=(MethodWithType)m;
      if (mwt.get_inner().isPresent()){continue;}
      addMwt(p, plgInfo, jms, msResult, pTop, mwt);
    }
    return l.withMs(msResult);
    }


private void addMwt(Program p, PlgInfo plgInfo, Method[] jms, List<Member> msResult, Path pTop, MethodWithType mwt) {
//checks
  isOkAsReturn(p,pTop,mwt.getMt().getReturnType());
  for(Path pi:mwt.getMt().getExceptions()){
    isOkAsException(p,pTop,pi);
    }
  for(Type ti:mwt.getMt().getTs()){
    isOkAsParameter(p,pTop,ti);
    }
  //TODO: we may want to cache those tests if performance is needed
  //TODO: in the tests, check that no Any,Library,Void
  //add to msResult
  //TODO: add behaviour if mwt have special comment to define specific ms for use
  List<Method>jms0=new ArrayList<>();
  String name=mwt.getMs().getName();
  if(name.startsWith("#")){name=name.substring(1);}
  for (Method mi:jms){
    if (!mi.getName().equals(name)){continue;}
    if (argLessPData(mi)!=mwt.getMs().getNames().size()){continue;}
    jms0.add(mi);
    }
  //TODO: add errors for jms0.size()!=1;
  assert jms0.size()==1;
  Method jm=jms0.get(0);
  UsingInfo ui=new UsingInfo(plgInfo,jm);
  MethodWithType tu=updateTemplateUsing(ui,jm,mwt);
  msResult.add(tu);
}

  private MethodWithType updateTemplateUsing(UsingInfo ui, Method jm, MethodWithType mwt) {
    MCall e=templateUsing;
    if (!mwt.getMt().getExceptions().isEmpty()){e=templateUsingExc;}
    MethodType mt=mwt.getMt();
    ExpCore.Block b=(Block) e.getEs().get(0);
    ExpCore.Using u=(Using) b.getDecs().get(0).getInner();
    ExpCore.MCall p0=(MCall) u.getEs().get(0);//parameter expressions
    
    //e#mcall.inner<-mwt.retType.path
    e=e.withInner(mt.getReturnType().getNT().getPath());
    u=u.withS(ui.usingMs);
    List<ExpCore> ues=new ArrayList<>();
    if(!mt.getMdf().equals(Mdf.Class)){
      ues.add(p0.withInner(new ExpCore.X("this")));
      }
    for(String x: mwt.getMs().getNames()){
      ues.add(p0.withInner(new ExpCore.X(x)));
      }
    u=u.withEs(ues);
    String errorS=
      "plugin string: "+ui.plgInfo.plgString+"\n"+
      "plugin part: "+ui.plgInfo.plgName +"\n"+
      "method name: "+mwt.getMs() +"\n"+
      "java method: "+ui.getClass().getCanonicalName()+"."+ui.usingMs +"\n";
    //u.inner#mcall.es(0)<-EncodingHelper.wrapStringU()
    List<ExpCore> errorEs=Collections.singletonList(EncodingHelper.wrapStringU(errorS));
    u=u.withInner(((MCall)u.getInner()).withEs(errorEs));
    b=b.withDecs(Collections.singletonList(b.getDecs().get(0).withInner(u)));
    //--
    if (!mwt.getMt().getExceptions().isEmpty()){
      On on=b.getOns().get(0);
      Dec k0 = ((Block)on.getInner()).getDecs().get(0);
      List<Dec> ks=new ArrayList<>();
      for(Path pi:mt.getExceptions()){
        MCall mci=((MCall)k0.getInner()).withInner(pi);
        ks.add(k0.withInner(mci));
        }
      on=on.withInner(((Block)on.getInner()).withDecs(ks));
      b=b.withOns(Collections.singletonList(on));
      //k0=b.k(0).inner#block.decs(0)
      //k0 add more on need
      //ki.inner#mcall.inner<-Pi
      }
    e=e.withEs(Collections.singletonList(b));
    mwt=mwt.withInner(e);
    return mwt;
    }


public static boolean hasPluginUnresponsive(ClassB l){
    //class method T #pluginUnresponsive(Library binaryRepr)
    MethodSelector ms=new MethodSelector("#pluginUnresponsive",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(NormType.immLibrary)){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().getNT().getMdf().equals(Mdf.Immutable)){return false;}
    return true;//no need to check return type, since is just thrown as error
    }
  public static boolean hasBinaryRepr(ClassB l){
    //read method Library #binaryRepr()
    MethodSelector ms=new MethodSelector("#binaryRepr",Collections.emptyList());
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Readable)){return false;}
    if(!mt.getTs().isEmpty()){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().equals(NormType.immLibrary)){return false;}
    return true;
    }
  public static boolean hasFrom(ClassB l){
    //   class method mut This0 #from(Library binaryRepr)
    MethodSelector ms=new MethodSelector("#from",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(NormType.immLibrary)){return false;}    
    if(!mt.getExceptions().isEmpty()){return false;}
    if(!mt.getReturnType().equals(NormType.mutThis0)){return false;}
    return true;
    }

  public static boolean hasExceptionIf(ClassB l){
    //class method Void #exceptionIf(Library binaryRepr) exception This
    MethodSelector ms=new MethodSelector("#exceptionIf",Collections.singletonList("binaryRepr"));
    MethodWithType mwt=(MethodWithType)l._getMember(ms);
    if(mwt==null){return false;}//must be an mwt since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getTs().get(0).equals(NormType.immLibrary)){return false;}    
    if(mt.getExceptions().size()!=1){return false;}
    if(!mt.getExceptions().get(0).equals(Path.outer(0))){return false;}
    if(!mt.getReturnType().equals(NormType.immVoid)){return false;}
    return true;//no need to check return type, since is just thrown as error
    }
  private static Path _pathForOutside(int dept,Path pi){
    if(pi.getN()<=dept){return null;}
    return pi.setNewOuter(pi.getN()-dept);
    }
  private static void checkForInside(ClassB lTop,Path csTop,Path originalPath){
    Path cs=From.fromP(originalPath,csTop);
    assert cs.getN()==0;
    NestedClass li=lTop.getNested(cs.getCBar());
    Doc d=((ClassB)li.getE()).getDoc1();
    if(d._getParameterForPlugin()==null ||d._getParameterForPluginPart()==null){
    throw Assertions.codeNotReachable("Add error for internal references need to be plugins too");//TODO:
      }
    }
  private static void isOkAsParameter(Program p, Path csTop, Type ti) {
    Path pi=ti.getNT().getPath();
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.topCb(),csTop,pi); return;}
    ClassB l=p.extractCb(op);//TODO: since p.top is topL, is it ok this extraction?
    boolean hasIt=hasBinaryRepr(l);
    boolean mdfOk=!ti.getNT().getMdf().equals(Mdf.class);
    if(!hasIt){throw Assertions.codeNotReachable("Add error for no #binaryRepr");}//TODO:
    if(!mdfOk){throw Assertions.codeNotReachable("Add error for no class parameters allowed");}//TODO:
    }


  private static void isOkAsException(Program p, Path csTop, Path pi) {
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.topCb(),csTop,pi);return;}
    ClassB l=p.extractCb(op);
    if(!hasExceptionIf(l)){throw Assertions.codeNotReachable("Add error for no #exceptionIf(binaryRepr) for exception...");}//TODO:
    }


  private static void isOkAsReturn(Program p, Path csTop, Type ti) {
    Path pi=ti.getNT().getPath();
    Path op=_pathForOutside(csTop.getCBar().size(),pi);
    if(op==null){checkForInside(p.topCb(),csTop,pi); return;}
    ClassB l=p.extractCb(op);
    boolean hasIt=hasFrom(l);
    boolean mdfOk=!ti.getNT().getMdf().equals(Mdf.class);
    if(!hasIt){throw Assertions.codeNotReachable("Add error for no #from(binaryRepr)");}//TODO:
    if(!mdfOk){throw Assertions.codeNotReachable("Add error for no class result allowed");}//TODO:
    }   
 
}

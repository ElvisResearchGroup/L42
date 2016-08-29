package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import sugarVisitors.CloneVisitor;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import tools.Match;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.HistoricType;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Expression.ClassReuse;
import ast.Expression;
import ast.Util.CachedStage;
import ast.Util.InvalidMwtAsState;
import ast.Util.PathMwt;
import coreVisitors.Dec;
import coreVisitors.FreeVariables;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import coreVisitors.IsValue;
import coreVisitors.ReplaceCtx;
import facade.Configuration;

public class Functions {

public static ClassB.NestedClass encapsulateIn(List<String> cBar,ClassB elem,Doc doc) {
    //Notice: encapsulation do not do the from. It must be done
    //on the call side as in "redirectDefinition"
    assert !cBar.isEmpty();
    List<String> cBar2 = cBar.subList(1,cBar.size());
    if(cBar2.isEmpty()){return new ClassB.NestedClass(doc,cBar.get(0),elem,null);}
    List<Member> ms=new ArrayList<>();
    ms.add(encapsulateIn(cBar2,elem,doc));
    ClassB cb= new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Position.noInfo,new CachedStage());
    return new ClassB.NestedClass(Doc.empty(),cBar.get(0),cb,null);
  }
public static Path add1Outer(Path p) {
    if( p.isPrimitive()){return p;}
    return p.setNewOuter(p.outerNumber()+1);
  }
public static Path classOf(Program p,ExpCore ctxVal,ExpCore val){
  assert IsValue.of(p,val)|
         new IsValue(p).validRightValue(val):ToFormattedText.of(val);
  Path res=Match.<Path>of(val)
      .add(Path.class,t->t)
      .add(_void.class,t->Path.Void())
      .add(ClassB.class,t->Path.Library())
      .add(Block.class,t->{
        ExpCore t2=t.withInner(new ExpCore.WalkBy());
        t2=ReplaceCtx.of(ctxVal,t2);
        return classOf(p,t2,t.getInner());
        })
      .add(X.class,t->{
        ExpCore e=Dec.of(ctxVal, t.getInner(), false);
        if(e instanceof Block){return classOf(p,ctxVal,e);}
        assert e instanceof MCall:ToFormattedText.of(ctxVal)+" ["+ToFormattedText.of(val)+"]";
        return (Path)((MCall)e).getInner();
        })
      .add(MCall.class,t->{
        assert t.getInner() instanceof Path;
        return (Path)t.getInner();
        })
      .end();
  return res;

}
public static boolean isSubtype(Program p,NormType t1,NormType t2){
  return isSubtype(t1.getMdf(),t2.getMdf())
      && isSubtype(p,t1.getPath(),t2.getPath())
      && isSubtype(t1.getPh(),t2.getPh());

}
private static boolean isSubtype(Ph ph, Ph ph2) {
  if(ph==ph2){return true;}
  if(ph==Ph.None){return true;}
  if(ph2==Ph.Ph){return true;}
  return false;
}
public static boolean isSubtype(Program p, Path path1, Path path2) {
  if(path2.equals(Path.Any())){return true;}
  if(path1.equals(path2)){return true;}
  if(path1.isPrimitive()){return false;}
  Path path2N=Norm.of(p,path2);
  Path path1N=Norm.of(p,path1);
  if(path1N.equals(path2N)){return true;}

  ClassB cp1=p.extractCb(path1);
  for(Path pathi:cp1.getStage().getInheritedPaths()){
    Path pathiFrom=From.fromP(pathi, path1);
    Path pathiN=Norm.of(p,pathiFrom);//or not norm?
    if(pathiN.equals(path2N)){return true;}
  }
  return false;
}
public static boolean isSubtype(Mdf mdf1, Mdf mdf2) {
  if(mdf1==mdf2){return true;}
  if(mdf1==Mdf.Class || mdf2==Mdf.Class){return false;}
  if(mdf2==Mdf.Readable){return true;}
  if(mdf1==Mdf.Capsule){return true;}
  //mdf1 not class, capsule
  //mdf2 not class, readable
  if(mdf1==Mdf.Mutable && mdf2==Mdf.Lent){return true;}
  return false;
}
public static NormType sharedAndLentToReadable(NormType that){
  Mdf mdf=that.getMdf();
  if(mdf==Mdf.Mutable){mdf=Mdf.Readable;}
  if(mdf==Mdf.Lent){mdf=Mdf.Readable;}
  return new NormType(mdf,that.getPath(),that.getPh());
  }
public static Block garbage(Block e, int n) {
  //try to see witch of the dvs in 0..i can be trashed
  HashSet<String> needX=neededX(e,n);
  HashSet<Integer> needKeep=new HashSet<Integer>();
  while(iterateAddNeeded(e, n, needX, needKeep));
  List<ast.ExpCore.Block.Dec> decs1 = e.getDecs();
  List<ast.ExpCore.Block.Dec> decs2 = new ArrayList<>();
  for(int i=0;i<e.getDecs().size();i++){
    if(i<n&&!needKeep.contains(i)){continue;}
    decs2.add(decs1.get(i));
    }
  return new Block(e.getDoc(),decs2,e.getInner(),e.getOns(),e.getP());
  }
private static boolean iterateAddNeeded(Block e, int n, HashSet<String> needX, HashSet<Integer> needKeep) {
  int size=needKeep.size();
  for(int i: needKeep){
    ExpCore ei=e.getDecs().get(i).getInner();
    needX.addAll(FreeVariables.of(ei));
    }
  for(int i=0;i<n;i++){
    if(!needX.contains(e.getDecs().get(i).getX())){continue;}
    needKeep.add(i);
  }
  assert size<=needKeep.size();
  return size!=needKeep.size();
}
private static HashSet<String>  neededX(Block e,int n){
  HashSet<String> neededX=new HashSet<String>();
  for(int i=n;i<e.getDecs().size();i++){
    ExpCore ei=e.getDecs().get(i).getInner();
    neededX.addAll(FreeVariables.of(ei));
    }
  for(int i=n;i<e.getDecs().size();i++){
    neededX.remove(e.getDecs().get(i).getX());
    }
  neededX.addAll(FreeVariables.of(e.getInner()));
  return neededX;
  }
public static String freshName(String pathR, Set<String> usedNames) {
  while(Character.isDigit(pathR.charAt(pathR.length()-1))){
    pathR=pathR.substring(0,pathR.length()-1);
  }
  if(!usedNames.contains(pathR) && !keywords.contains(pathR)){
    usedNames.add(pathR);
    return pathR;
    }
  int i=0;
  while(true){
    String res=pathR+i;
    if(usedNames.contains(res)){i++;continue;}
    usedNames.add(res);
    return res;
  }
}

public static Path freshPathName(Path pathR, Set<String> usedNames) {
  String p=pathR.getRowData().get(pathR.getRowData().size()-1);
  p=freshName(p,usedNames);
  return pathR.popC().pushC(p);
}

public static String freshName(Path pathR, Set<String> usedNames) {
  String p=pathR.getRowData().get(pathR.getRowData().size()-1);
  p=p.substring(0,1).toLowerCase() +p.substring(1);
  return freshName(p,usedNames);
}
public static final HashSet<String>keywords=new HashSet<String>();
static{
  //keywords.add("Void");
  //keywords.add("Any");
  //keywords.add("Library");
  keywords.add("void");
  keywords.add("case");
  keywords.add("class");
  keywords.add("mut");
  keywords.add("read");
  keywords.add("lent");
  keywords.add("capsule");
  keywords.add("if");
  keywords.add("else");
  keywords.add("while");
  keywords.add("loop");
  keywords.add("with");
  keywords.add("on");
  keywords.add("in");
  keywords.add("catch");
  keywords.add("var");
  keywords.add("default");
  keywords.add("interface");
  keywords.add("method");
  keywords.add("use");
  keywords.add("check");
}
public static List<Block.Dec> replace(List<Block.Dec> dvs,MCall mc){
  String x=((ExpCore.X)mc.getInner()).getInner();
  List<Block.Dec> result=new ArrayList<>();
  for(Block.Dec dv: dvs){
    if(!dv.getX().equals(x)){result.add(dv);continue;}
    MCall inner=(MCall)dv.getInner();
    String fieldName=mc.getS().getName();
    int mIndex=inner.getS().getNames().indexOf(fieldName);
    assert mIndex!=-1 :fieldName+" / "+ToFormattedText.of(inner);
    List<ExpCore> es2 = new ArrayList<>(inner.getEs());
    ExpCore parameterAtom = mc.getEs().get(0);
    es2.set(mIndex,parameterAtom);
    result.add(dv.withInner(inner.withEs(es2)));
  }
  return result;
}
public static Member firstIncomplete(ClassB cb){
  for(Member m: cb.getMs()){
    if(IsCompiled.of(m)){continue;}
    return m;
  }
  throw Assertions.codeNotReachable();
}

public static List<Path> remove1OuterAndPrimitives(Collection<Path> paths){
  List<Path> result=new ArrayList<>();
  for(Path p:paths){
    if(p.isPrimitive()){continue;}
    int n=p.outerNumber();
    if(n>0){result.add(p.setNewOuter(n-1));}
  }
  return result;
}

public static Path classOf(Program p, ExpCore ctxVal,List<ast.ExpCore.Block.Dec> decs, ExpCore inner) {
  Position pos=null;if(inner instanceof Ast.HasPos){pos=((Ast.HasPos)inner).getP();}
  Block b=new Block(Doc.empty(),decs,new WalkBy(),Collections.emptyList(),pos);
  ctxVal=ReplaceCtx.of(ctxVal, b);
  return classOf(p,ctxVal,inner);
}
public static NormType toPartial(NormType that) {
  if(that.getPh()==Ph.Ph){return that;}
  return new NormType(that.getMdf(),that.getPath(),Ph.Partial);
  }


public static NormType toPh(NormType that){
  return new NormType(that.getMdf(),that.getPath(),Ph.Ph);
  }
public static HashMap<String, NormType> complete(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){
    if(varEnv.get(s).getPh()==Ph.None){result.put(s,varEnv.get(s));}
    }
  return result;
}
public static HashMap<String, NormType> nonComplete(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){
    if(varEnv.get(s).getPh()!=Ph.None){result.put(s,varEnv.get(s));}
    }
  return result;
}

public static HashMap<String, NormType> toPh(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){result.put(s,toPh(varEnv.get(s)));}
  return result;
}
public static HashMap<String, NormType> toPartial(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){result.put(s,toPartial(varEnv.get(s)));}
  return result;
}
public static NormType sharedToLent(NormType nt) {
  if(nt.getMdf()!=Mdf.Mutable){return nt;}
  return nt.withMdf(Mdf.Lent);
}
public static boolean isSuperTypeOfMut(Mdf mdf){
  return mdf==Mdf.Mutable
      ||mdf==Mdf.Lent
      ||mdf==Mdf.Readable;
  }
public static boolean isInterface(Program p, Path path) {
  if (path.equals(Path.Any())){return true;}
  if(path.isPrimitive()){return false;}
  return p.extractCb(path).isInterface();//in typing, this is guaranteed to be there
}
public static boolean checkCore(Expression result) {
    result.accept(new CloneVisitor(){
      public Expression visit(Path p){assert p.isCore() || p.isPrimitive():p;return p;}
      protected <T extends Expression>T lift(T e){//exists just to breakpoint
        try{return super.lift(e);}
        catch(AssertionError err){
          throw err;
          //throw new AssertionError(ToFormattedText.of(e),err);
          }
      }});
  return true;
}

public static NormType forceNormType(Program p,ExpCore inner, Type preciseTOpt) {
  assert preciseTOpt!=null;
  if (preciseTOpt instanceof Ast.HistoricType){
    return Norm.resolve(p,(HistoricType) preciseTOpt);
    //throw new ErrorMessage.UnresolvedType((Ast.HistoricType)preciseTOpt,inner);
    }
  NormType preciseT=(NormType)preciseTOpt;
  return preciseT;
}
public static List<InvalidMwtAsState> isAbstract(Program p, ClassB ct) {
  List<InvalidMwtAsState> details = coherent(p,ct);
  if(!details.isEmpty()){
	  return details;
	  }
  details=new ArrayList<>();
  for(Member m:ct.getMs()){
    if(!(m instanceof NestedClass)){continue;}
    NestedClass nc=(NestedClass)m;
    assert nc.getInner() instanceof ClassB;
    isAbstract(p.addAtTop(ct),(ClassB)nc.getInner()).stream()
        .map(s->new InvalidMwtAsState(nc.getName()+"."+s.getReason(),s.getMwt())).forEach(details::add);
  }
  return details;
}

public static List<InvalidMwtAsState> coherent(Program p, ClassB ct) {
  Program p1=p.addAtTop(ct);//was designed to give 1 error, now must give full list
  if( ct.isInterface()){ return Collections.emptyList();}
  List<MethodWithType> mwtsNI= collectNotImplementedMethods(ct);
  if(!mwtsNI.isEmpty()){
    return mwtsNI.stream().map(m->new InvalidMwtAsState("Method from interface not implemented",m)).collect(Collectors.toList());
  }
  List<MethodWithType> mwts= collectAbstractMethods(p1,ct);
  if(mwts.isEmpty()){return Collections.emptyList();}
  List<MethodWithType> typeMethods=new ArrayList<>();
  for(MethodWithType mwt:mwts){if(mwt.getMt().getMdf()==Ast.Mdf.Class){typeMethods.add(mwt);}}
  /*if(typeMethods.size()>1){
    return mwts.stream().map(m->new InvalidMwtAsState("More than one constructor candidate",m)).collect(Collectors.toList());
    }*/

  if(typeMethods.size()==0){
    return mwts.stream().map(m->new InvalidMwtAsState("No constructor candidate",m)).collect(Collectors.toList());
    }
  MethodWithType mutK=null;
  MethodWithType lentK=null;
  MethodWithType readK=null;
  MethodWithType immK=null;
  List<InvalidMwtAsState> result=new ArrayList<>();
  for(MethodWithType constr: typeMethods){
    mwts.remove(constr);
    NormType retType=(NormType)constr.getMt().getReturnType();
    if(!retType.getPath().equals(Path.outer(0))){
    return Collections.singletonList(new InvalidMwtAsState(" return path must be This",constr));
    }
    if(retType.getPh()!=Ph.None){
      return Collections.singletonList(new InvalidMwtAsState(" return type fwd invalid for constructor",constr));
    }
    if(retType.getMdf()==Mdf.Mutable){ mutK=setIfFree(mutK,constr,result);}
    if(retType.getMdf()==Mdf.Lent){ lentK=setIfFree(lentK,constr,result);}
    if(retType.getMdf()==Mdf.Readable){ readK=setIfFree(readK,constr,result);}
    if(retType.getMdf()==Mdf.Immutable){ immK=setIfFree(immK,constr,result);}
    }
  //we know typeMethods is not empty
  MethodWithType oneK = mutK!=null?mutK:(lentK!=null?lentK:(readK!=null?readK:immK));
  assert oneK!=null;
  List<Path> paths = oneK.getMt().getTs().stream().map(t->((NormType)t).getPath()).collect(Collectors.toList());
  kFieldEquals(oneK,paths,mutK,result);
  kFieldEquals(oneK,paths,lentK,result);
  kFieldEquals(oneK,paths,readK,result);
  kFieldEquals(oneK,paths,immK,result);
  kNotHave(mutK,result,Mdf.Capsule,Mdf.Lent,Mdf.Readable);
  kNotHave(lentK,result,Mdf.Capsule,Mdf.Mutable);
  kNotHave(readK,result,Mdf.Capsule,Mdf.Mutable,Mdf.Lent);
  kNotHave(immK,result,Mdf.Capsule,Mdf.Mutable,Mdf.Lent,Mdf.Readable);
  for(  MethodWithType mwt:mwts){//for all the other h
    String name=mwt.getMs().getName();
    if(name.startsWith("#")){name=name.substring(1);}
    //select satisfying parameter
    NormType nt=selectCorrespondingFieldType(oneK,  name);
    if(nt==null){
      result.add(new InvalidMwtAsState(" Abstract method not a field of the constructor",mwt));
      }
    else if(!coherent(p1, nt,mwt,oneK)){
      result.add(new InvalidMwtAsState(" Abstract method has invalid shape to be part of the state",mwt));
      }
    }
  return result;
  }
  private static void kNotHave(MethodWithType constr, List<InvalidMwtAsState> result, Mdf... mdfs) {
    if(constr==null){return;}
    for(Type t:constr.getMt().getTs()){
      NormType nt=(NormType)t;
      if(Arrays.asList(mdfs).contains(nt.getMdf())){
        result.add(new InvalidMwtAsState(" constructor has invalid mdf for its fields ",constr));
      }
    }
  }

private static MethodWithType setIfFree(MethodWithType oldK, MethodWithType constr,List<InvalidMwtAsState> result) {
  if (oldK==null){return constr;}
  result.add(new InvalidMwtAsState(" More than one constructor with the same return type:",oldK));
  result.add(new InvalidMwtAsState(" More than one constructor with the same return type:",constr));
  return oldK;
}
private static Path mapHelp(MethodWithType oneK,  MethodWithType otherK, List<InvalidMwtAsState> result,Type t){
  NormType nt=(NormType)t;
  //can not be required fwd, we need invariants to replace constructos if needed, should make ts more flexible?
  //if(nt.getPh()!=Ph.Ph){result.add(new InvalidMwtAsState(" constructor parameters must all be fwd ",otherK ));}
  return nt.getPath();
}
private static void kFieldEquals(MethodWithType oneK, List<Path> paths, MethodWithType otherK, List<InvalidMwtAsState> result) {
  if (otherK==null){return;}
  if (!oneK.getMs().getNames().equals(otherK.getMs().getNames())){
    result.add(new InvalidMwtAsState(" Different number or names of parameters for two candidate constructors: ",oneK));
    result.add(new InvalidMwtAsState(" Different number or names of parameters for two candidate constructors: ",otherK));
  }
  List<Path> otherP = otherK.getMt().getTs().stream().map(t->mapHelp(oneK,otherK,result,t)).collect(Collectors.toList());

  if(!paths.equals(otherP)){
    result.add(new InvalidMwtAsState(" Different number or paths of parameters for two candidate constructors: ",oneK));
    result.add(new InvalidMwtAsState(" Different number or paths of parameters for two candidate constructors: ",otherK));
  }
}

/**null if no corresponding field is found*/
private static NormType selectCorrespondingFieldType(MethodWithType constr, String name) {
  int i=-1;for(String ni:constr.getMs().getNames()){i+=1;
    if(!ni.equals(name)){continue;}
    NormType nt=(NormType)constr.getMt().getTs().get(i);
    return nt;
    }
  return null;
}
public static boolean getterOkMdf(Mdf k,Mdf mnt,Mdf expected){
  switch (mnt){
    case Class:return Mdf.Class==expected;
    case Immutable:
      if (k==Mdf.Immutable){return Mdf.Readable==expected || Mdf.Immutable==expected;}
      return Mdf.Immutable==expected;
    case Mutable:
    case Lent:
    case Readable:return Mdf.Readable==expected;
    default: throw Assertions.codeNotReachable();
  }
}
public static boolean exposerSetterOkMdf(Mdf k,Mdf mnt,Mdf expected){
  if( k==Mdf.Immutable){return false;}
  if( k==Mdf.Readable){return false;}
  switch (mnt){
    case Class:
    case Immutable:
    case Mutable:
    case Readable: return mnt==expected;
    case Lent:if(k==Mdf.Lent){return Mdf.Lent==expected || Mdf.Mutable==expected;}
    default: throw Assertions.codeNotReachable();
  }
}
public static boolean coherent(Program p, NormType nt, MethodWithType mwt,MethodWithType oneK) {
  if(!checkVoidAndThatOk(p,mwt)){return false;}
  //itid=imm->imm,type->type
  //getter: read to itid,*->read/itid,*->read/tid, imm->(read/imm)
  //exposer mut to allid/lent->(lent,mut),allid/ NO/NO
  //setter is mut from allid/lent->(lent,mut),allid/NO/NO
  Mdf kMdf=((NormType)oneK.getMt().getReturnType()).getMdf();
  Mdf expected=((NormType)mwt.getMt().getReturnType()).getMdf();
  if(mwt.getMt().getMdf()==Mdf.Readable){
    return getterOkMdf(kMdf,nt.getMdf(),expected);
  }
  if(!mwt.getMt().getTs().isEmpty()){
    expected=((NormType)mwt.getMt().getTs().get(0)).getMdf();
    }
  return exposerSetterOkMdf(kMdf,nt.getMdf(),expected);

  /*//group1
  if(mdf==Mdf.Class || mdf==Mdf.Immutable || mdf==Mdf.Readable){
  //case a
    if(mwt.getMt().getTs().isEmpty()){ return okSubtypeGet(p, mdf, path, mwt); }
    //case b
    if(mwt.getMt().getMdf()==Mdf.Mutable || mwt.getMt().getMdf()==Mdf.Lent){
      return okSubtypeSet(p, mdf, path, mwt);
    }
  }
 //group2
  if(mdf==Mdf.Mutable || mdf==Mdf.Lent){
    if(mwt.getMt().getMdf()==Mdf.Mutable){
      //case c
      if(mwt.getMt().getTs().isEmpty()){return okSubtypeGet(p, mdf, path, mwt);}
      //case e
      else {return okSubtypeSet(p, mdf, path, mwt);}
    }else   if(mwt.getMt().getMdf()==Mdf.Lent && !mwt.getMt().getTs().isEmpty()){
        //case f
      return okSubtypeSet(p,Mdf.Capsule,path,mwt);
    } else if(mwt.getMt().getMdf()!=Mdf.Class && mwt.getMt().getMdf()!=Mdf.Mutable){
    //case d
      return okSubtypeGet(p,mwt.getMt().getMdf(),path,mwt);
    }
  }
  //group3
  assert mwt.getMt().getMdf()!=Mdf.Class;//from call conditions
  if(mdf!=Mdf.Capsule){return false;}
  if(mwt.getMt().getTs().isEmpty()){
    //g
    return okSubtypeGet(p,mwt.getMt().getMdf(),path,mwt);
  }
  if(mwt.getMt().getMdf()==Mdf.Mutable){
    //h
    return okSubtypeSet(p,Mdf.Mutable,path,mwt);
  }
  return false;*/
}
/*
private static boolean okSubtypeGet(Program p, Mdf mdf, Path path, MethodWithType mwt) {
  return isSubtype(p, new NormType(mdf,path,Ph.None),Norm.of(p,mwt.getMt().getReturnType()));
}
private static boolean okSubtypeSet(Program p, Mdf mdf, Path path, MethodWithType mwt) {
  return isSubtype(p, Norm.of(p,mwt.getMt().getTs().get(0)),new NormType(mdf,path,Ph.None));
}*/
private static boolean checkVoidAndThatOk(Program p,MethodWithType mwt) {
  if(mwt.getMt().getTs().size()>1){return false;}
  if(mwt.getMt().getTs().isEmpty()){return true;}
  if(!Norm.of(p,mwt.getMt().getReturnType()).equals(NormType.immVoid)){return false;}
  if(!mwt.getMs().getNames().get(0).equals("that")){return false;}
  return true;
  }

private static List<MethodWithType> collectNotImplementedMethods(ClassB cb) {
  List<MethodWithType> mwts=new ArrayList<>();
  for(PathMwt inhM:cb.getStage().getInherited()){
    MethodSelector si = inhM.getMwt().getMs();
    if( Program.getIfInDom(cb.getMs(),si).isPresent()){continue;}
    mwts.add(inhM.getMwt());
  }
  return mwts;
}

private static List<MethodWithType> collectAbstractMethods(Program p,ClassB cb) {
  List<MethodWithType> mwts=new ArrayList<>();
  for(Member m:cb.getMs()){
    if(!(m instanceof MethodWithType)){continue;}
    MethodWithType mwt=(MethodWithType)m;
    if(mwt.get_inner().isPresent()){continue;}
    mwts.add(Norm.of(p,mwt,false));
    }
  return mwts;
}
public static Set<MethodSelector> originalMethOf(Program p, ClassB cb) {
  return originalMethOf(p,cb.getSupertypes(),cb.getMs());
}
public static Set<MethodSelector> originalMethOf(Program p, List<Path> paths,List<Member> ms0) {
  Set<MethodSelector> result=new HashSet<>();
  for(Member m:ms0){
    m.match(
        nc->false,
        mi->{throw Assertions.codeNotReachable();},
        mt->result.add(mt.getMs()));
    }
  retainOnlyOriginalMethOf(p,paths,result);
  return result;
}
private static void retainOnlyOriginalMethOf(Program p, List<Path> paths,Set<MethodSelector> ms0) {
   for(Path pi:paths){
    for(Member mi:p.extractCb(pi).getMs()){
      mi.match(
          nc->false,
          mim->{throw Assertions.codeNotReachable();},
          mt->ms0.remove(mt.getMs()));
      }
  }
}
//with less remove only less, with plus remove plus and less, with other remove always
@SuppressWarnings("unchecked") public static <T extends ExpCore> T clearCache(T e,Ast.Stage level){
  return (T)e.accept(new coreVisitors.CloneVisitor(){
    @Override public ExpCore visit(ExpCore.ClassB cb){
      cb=((ExpCore.ClassB)super.visit(cb));
      if(level!=Stage.Plus && level!=Stage.Less){
        return cb.withStage(new CachedStage());
      }
      if(cb.getStage().getStage()==Stage.Less){
        return  cb.withStage(new CachedStage());
        }
      if(level==Stage.Plus && cb.getStage().getStage()==Stage.Plus ){
        return  cb.withStage(new CachedStage());
        }
      return cb;
    }
  });
}

public static <T > boolean verifyMinimalCache(T e){
  if(!(e instanceof ExpCore)){return true;}
  ((ExpCore)e).accept(new coreVisitors.CloneVisitor(){
    @Override public ExpCore visit(ExpCore.ClassB cb){
      cb=((ExpCore.ClassB)super.visit(cb));
      if(!cb.getStage().isInheritedComputed()){
        throw new AssertionError(cb);
      }
      return  cb;
      }
  });
  return true;
}

@SuppressWarnings("unchecked")
public static <T > T setMinimalCache(Program p,T e){
  if(!(e instanceof ExpCore)){return e;}
  return (T)((ExpCore)e).accept(new coreVisitors.CloneVisitor(){
    @Override public ExpCore visit(ExpCore.ClassB cb){
      //cb=((ExpCore.ClassB)super.visit(cb)); we need to stop at the first layer
      Configuration.typeSystem.computeStage(p, cb);
      return  cb;
      }
  });
}

}
package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import sugarVisitors.CloneVisitor;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import tools.Match;
import ast.Ast;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.HistoricType;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Expression.ClassReuse;
import ast.Expression;

import ast.Util.InvalidMwtAsState;
import ast.Util.PathMwt;
import coreVisitors.Dec;
import coreVisitors.FreeVariables;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import coreVisitors.IsValue;
import coreVisitors.ReplaceCtx;
import facade.Configuration;
import facade.L42;
import programReduction.Program;

public class Functions {

public static Optional<Member> getIfInDom(List<ExpCore.ClassB.Member> map, Ast.C elem){
for(ExpCore.ClassB.Member m: map){
  if(m.match(nc->nc.getName().equals(elem), mi->false, mt->false)){return Optional.of(m);}
  }
return Optional.empty();
}
public static Optional<Member> getIfInDom(List<ExpCore.ClassB.Member> map, ast.Ast.MethodSelector elem){
//remember: the are no docs to make method selectors different.
for(ExpCore.ClassB.Member m: map){
  if(m.match(nc->false,mi->mi.getS().equals(elem) ,mt->mt.getMs().equals(elem))){return Optional.of(m);}
  }
return Optional.empty();
}
public static Optional<Member> getIfInDom(List<ExpCore.ClassB.Member> map, ExpCore.ClassB.Member elem){
return elem.match(nc->getIfInDom(map,nc.getName()), mi->getIfInDom(map,mi.getS()),mt->getIfInDom(map,mt.getMs()));
}
public static void removeIfInDom(List<Member> ms,MethodSelector sel){
for(Member memi:ms){
  boolean res=memi.match(
      nc->false,
      mi->{if(mi.getS().equals(sel)){ms.remove(mi);return true;}return false;},
      mt->{if(mt.getMs().equals(sel)){ms.remove(mt);return true;}return false;});
  if(res){break;}
}
}
public static void removeIfInDom(List<Member> ms,String sel){
for(Member memi:ms){
  boolean res=memi.match(
      nc->{if(nc.getName().equals(sel)){ms.remove(nc);return true;}return false;},
      mi->false,
      mt->false);
  if(res){break;}
}
}
public static void replaceIfInDom(List<Member> ms,Member m){
Object matchRes=m.match(
    nc->{for(Member mi:ms){if (!(mi instanceof NestedClass)){continue;}
      if (!nc.getName().equals(((NestedClass)mi).getName())){continue;}
      return ms.set(ms.indexOf(mi), m);//swap to keep order
      }return null;},
    mImpl->{for(Member mi:ms){if (!(mi instanceof MethodImplemented)){continue;}
      if (!mImpl.getS().equals(((MethodImplemented)mi).getS())){continue;}
      return ms.set(ms.indexOf(mi), m);//swap to keep order
      }return null;},
    mt->{for(Member mi:ms){if (!(mi instanceof MethodWithType)){continue;}
      if (!mt.getMs().equals(((MethodWithType)mi).getMs())){continue;}
      return ms.set(ms.indexOf(mi), m);//swap to keep order
      }return null;});
if(matchRes==null){ms.add(m);}
}

public static ClassB.NestedClass encapsulateIn(List<Ast.C> cBar,ClassB elem,Doc doc) {
    //Notice: encapsulation do not do the from. It must be done
    //on the call side as in "redirectDefinition"
    assert !cBar.isEmpty();
    List<Ast.C> cBar2 = cBar.subList(1,cBar.size());
    if(cBar2.isEmpty()){return new ClassB.NestedClass(doc,cBar.get(0),elem,null);}
    List<Member> ms=new ArrayList<>();
    ms.add(encapsulateIn(cBar2,elem,doc));
    ClassB cb= ClassB.membersClass(ms,Position.noInfo);
    return new ClassB.NestedClass(Doc.empty(),cBar.get(0),cb,null);
  }
public static Path originDecOf(Program p,MethodSelector ms,ClassB cb/*normalized*/){
  assert cb.getPhase().subtypeEq(Phase.Norm);
  for(Path pi:cb.getSuperPaths()){
    ClassB cbi=p.extractClassB(pi);
    MethodWithType m = (MethodWithType) cbi._getMember(ms);
    if(m!=null && !m.getMt().isRefine()){return pi;}
    }
  throw Assertions.codeNotReachable();
  }
 
public static Path add1Outer(Path p) {
    if( p.isPrimitive()){return p;}
    return p.setNewOuter(p.outerNumber()+1);
  }
/*
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
      && isSubtype(p,t1.getPath(),t2.getPath());

}
public static boolean isSubtype(Program p, Path path1, Path path2) {
  if(!path1.isPrimitive()){p.extractCb(path1);}
  if(!path2.isPrimitive()){p.extractCb(path2);}
  if(path2.equals(Path.Any())){return true;}
  if(path1.equals(path2)){return true;}
  if(path1.isPrimitive()){return false;}
  Path path2N=Norm.of(p,path2);
  Path path1N=Norm.of(p,path1);
  if(path1N.equals(path2N)){return true;}

  ClassB cp1=p.extractCb(path1);
  //for(Path pathi:cp1.getStage().getInheritedPaths()){
  for(Type ti:cp1.getSupertypes()){
    Path pathiFrom=From.fromP(ti.getNT().getPath(), path1);
    Path pathiN=Norm.of(p,pathiFrom);//or not norm?
    if(pathiN.equals(path2N)){return true;}
  }
  return false;
}*/
public static boolean isSubtype(Mdf mdf1, Mdf m) {
  if(mdf1==m){return true;}
  switch(mdf1){
    case Class:        return false;
    case Capsule:      return m!=Mdf.Class;
    case Immutable:    return m==Mdf.Readable || m==Mdf.ImmutablePFwd || m==Mdf.ImmutableFwd;//imm<=read,fwd%Imm //,fwdImm
    case Mutable:      return m==Mdf.Lent || m==Mdf.MutablePFwd ||m==Mdf.Readable|| m==Mdf.MutableFwd;//mut<=lent,fwd%Mut //,read,fwdMut
    case Lent:         return m==Mdf.Readable;//lent<=read
    case MutablePFwd:  return m==Mdf.MutableFwd;//fwd%Mut<=fwdMut
    case ImmutablePFwd:return m==Mdf.ImmutableFwd;//fwd%Imm<=fwdImm
    default: return false;
    }
  }
public static NormType sharedAndLentToReadable(NormType that){
  Mdf mdf=that.getMdf();
  if(mdf==Mdf.Mutable){mdf=Mdf.Readable;}
  if(mdf==Mdf.Lent){mdf=Mdf.Readable;}
  return that.withMdf(mdf);
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
  String p=null;
  if(pathR.isPrimitive()){p=freshName(pathR.toString(),usedNames);}
  else if(pathR.getCBar().isEmpty()){p=freshName("This",usedNames);}
  else{
    Ast.C last=pathR.getCBar().get(pathR.getCBar().size()-1);
    if (last.isUnique()){return pathR.popC().pushC(last.withUniqueNum(L42.freshPrivate()));}
    p=last.toString();
    p=freshName(p,usedNames);
    }
  return pathR.popC().pushC(C.of(p));
  }

public static String freshName(Path pathR, Set<String> usedNames) {
  String p=null;
  if(pathR.isPrimitive()){p=freshName(pathR.toString(),usedNames);}
  else if(pathR.getCBar().isEmpty()){p=freshName("This",usedNames);}
  else{
    p=pathR.getCBar().get(pathR.getCBar().size()-1).toString();
    p=freshName(p,usedNames);
    }
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
    String fieldName=mc.getS().nameToS();
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
/*
public static Path classOf(Program p, ExpCore ctxVal,List<ast.ExpCore.Block.Dec> decs, ExpCore inner) {
  Position pos=null;if(inner instanceof Ast.HasPos){pos=((Ast.HasPos)inner).getP();}
  Block b=new Block(Doc.empty(),decs,new WalkBy(),Collections.emptyList(),pos);
  ctxVal=ReplaceCtx.of(ctxVal, b);
  return classOf(p,ctxVal,inner);
}
*/
public static NormType toPartial(NormType that) {
  switch (that.getMdf()){
    case Capsule:       return that;
    case Class:         return that;
    case Immutable:     return that.withMdf(Mdf.ImmutablePFwd);
    case ImmutableFwd:  return that;
    case ImmutablePFwd: return that;
    case Lent:          return that;
    case Mutable:       return that.withMdf(Mdf.MutablePFwd);
    case MutableFwd:    return that;
    case MutablePFwd:   return that;
    case Readable:      return that;  
    }
  throw Assertions.codeNotReachable();
  }

public static boolean isComplete(NormType that){
  switch (that.getMdf()){
    case Capsule:       return true;
    case Class:         return true;
    case Immutable:     return true;
    case ImmutableFwd:  return false;
    case ImmutablePFwd: return false;
    case Lent:          return true;
    case Mutable:       return true;
    case MutableFwd:    return false;
    case MutablePFwd:   return false;
    case Readable:      return true; 
    }
  throw Assertions.codeNotReachable();
  }

public static NormType toPh(NormType that){
  switch (that.getMdf()){
    case Capsule:       return that;
    case Class:         return that;
    case Immutable:     return that.withMdf(Mdf.ImmutableFwd);
    case ImmutableFwd:  return that;
    case ImmutablePFwd: return that.withMdf(Mdf.ImmutableFwd);
    case Lent:          return that;
    case Mutable:       return that.withMdf(Mdf.MutableFwd);
    case MutableFwd:    return that;
    case MutablePFwd:   return that.withMdf(Mdf.MutableFwd);
    case Readable:      return that;  
    }
  throw Assertions.codeNotReachable();
  }
public static NormType toComplete(NormType that){
  switch (that.getMdf()){
    case Capsule:       return that;
    case Class:         return that;
    case Immutable:     return that;
    case ImmutableFwd:  return that.withMdf(Mdf.Immutable);
    case ImmutablePFwd: return that.withMdf(Mdf.Immutable);
    case Lent:          return that;
    case Mutable:       return that;
    case MutableFwd:    return that.withMdf(Mdf.Mutable);
    case MutablePFwd:   return that.withMdf(Mdf.Mutable);
    case Readable:      return that;  
    }
  throw Assertions.codeNotReachable();
  }

public static HashMap<String, NormType> complete(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){
    if(isComplete(varEnv.get(s))){result.put(s,varEnv.get(s));}
    }
  return result;
}
public static HashMap<String, NormType> nonComplete(HashMap<String, NormType> varEnv) {
  HashMap<String, NormType> result= new HashMap<String, NormType>();
  for(String s: varEnv.keySet()){
    if(!isComplete(varEnv.get(s))){result.put(s,varEnv.get(s));}
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
  return p.extractClassB(path).isInterface();//in typing, this is guaranteed to be there
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

}
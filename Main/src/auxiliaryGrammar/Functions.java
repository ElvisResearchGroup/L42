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
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.Type;
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
    if(cBar2.isEmpty()){return new ClassB.NestedClass(doc,cBar.get(0),elem,elem.getP());}
    List<Member> ms=new ArrayList<>();
    ms.add(encapsulateIn(cBar2,elem,doc));
    ClassB cb= ClassB.membersClass(ms,elem.getP(),elem.getPhase());
    return new ClassB.NestedClass(Doc.empty(),cBar.get(0),cb,elem.getP());
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

/**exceptions of a method that is conceptually a subtype
 * of both a methods containing exceptions a AND one containing exceptions b 
 */
public static List<Type> excRes(Program p, List<Type>a,List<Type>b){
  List<Type> res = excFilter(p,a,b);
  res.addAll(excFilter(p,b,a));
  return res;
  }
private static List<Type> excFilter(Program p, List<Type>mayStay,List<Type>other){
  List<Type>res=new ArrayList<>();
  //res={ti in mayStay | exists tj in other s.t. p|-ti<=tj }
  for(Type ti:mayStay){
    for(Type tj:other){
      if(p.subtypeEq(ti.getPath(),tj.getPath())){
        res.add(ti);
        }
      }
    }
  return res;
  }



public static Type sharedAndLentToReadable(Type that){
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
public static Type toPartial(Type that) {
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

public static boolean isComplete(Type that){
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

public static Type toPh(Type that){
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

public static HashMap<String, Type> complete(HashMap<String, Type> varEnv) {
  HashMap<String, Type> result= new HashMap<String, Type>();
  for(String s: varEnv.keySet()){
    if(isComplete(varEnv.get(s))){result.put(s,varEnv.get(s));}
    }
  return result;
}
public static HashMap<String, Type> nonComplete(HashMap<String, Type> varEnv) {
  HashMap<String, Type> result= new HashMap<String, Type>();
  for(String s: varEnv.keySet()){
    if(!isComplete(varEnv.get(s))){result.put(s,varEnv.get(s));}
    }
  return result;
}

public static HashMap<String, Type> toPh(HashMap<String, Type> varEnv) {
  HashMap<String, Type> result= new HashMap<String, Type>();
  for(String s: varEnv.keySet()){result.put(s,toPh(varEnv.get(s)));}
  return result;
}
public static HashMap<String, Type> toPartial(HashMap<String, Type> varEnv) {
  HashMap<String, Type> result= new HashMap<String, Type>();
  for(String s: varEnv.keySet()){result.put(s,toPartial(varEnv.get(s)));}
  return result;
}
public static Type sharedToLent(Type nt) {
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
      @Override protected Path liftP(Path p){assert p.isCore() || p.isPrimitive():p;return p;}
      protected <T extends Expression>T lift(T e){//exists just to breakpoint
        try{return super.lift(e);}
        catch(AssertionError err){
          throw err;
          //throw new AssertionError(ToFormattedText.of(e),err);
          }
      }});
  return true;
}


public static <T> List<T> push(List<T> that, T elem){
List<T> res=new ArrayList<T>(that);
res.add(elem);
return res;
}
public static NestedClass _findAndRemove(List<NestedClass> ns,Ast.C that){
for(int i=0;i<ns.size();i++){
  NestedClass ni=ns.get(i);
  if(!ni.getName().equals(that)){continue;}
  ns.remove(i);
  return ni;
  }
return null;
}
public static MethodWithType _findAndRemove(List<MethodWithType> mwts,Ast.MethodSelector that){
for(int i=0;i<mwts.size();i++){
MethodWithType mwti=mwts.get(i);
if(!mwti.getMs().equals(that)){continue;}
mwts.remove(i);
return mwti;
}
return null;
}

}
package typeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import tools.Assertions;
import tools.Map;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import ast.Ast;
import ast.Ast.MethodSelector;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.NestedClass;
import ast.InternalError;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import auxiliaryGrammar.UsedPathsPlus;

public class TypeExtraction {

  public static ClassB etFull(Program p, ClassB cb) {
    try{while(true){ //Java deadcode detection sucks, if erased in compilation
      cb=etDispatch(p,cb);
    }}
    catch(InternalError.ETDeepNotApplicable ignored){return cb;}
  }
  public static ClassB etDispatch(Program p,ClassB classB){
    ClassB result=null;
    if(IsCompiled.of(classB)){
      if(classB.getStage()==Stage.None){result= etSub(p,classB);}
      if(classB.getStage()==Stage.Star){result=etStage(p,classB);}
      }
    if(result!=null){return result;}
    result=etDeep(p,classB);
    assert !result.equals(classB);
    return result;
    }
  private static ClassB etStage(Program p, ClassB classB) {
    assert classB.getStage()==Stage.Star;
    //classB.getSupertypes().size()==1;
    //collect es
    List<ClassB> es;
    try{es=collectEs(p, classB);}
    catch(ast.InternalError.InterfaceNotFullyProcessed notReadyYet){return null;}
    List<ClassB> esNone=new ArrayList<>();
    for(ClassB cb:es){
      if(cb==null){continue;}
      if(cb.getStage()==Stage.None && IsCompiled.of(cb)){esNone.add(cb);}
    }
    if(!esNone.isEmpty()){return null;}//esNone exists solely to simpler testing,
    Stage stage=stage(p,classB,es);
    assert stage!=Stage.Star;
    assert stage!=Stage.None;
    if(stage==null){return null;}
    return classB.withStage(stage);
  }
  private static List<ClassB> collectEs(Program p, ClassB classB) {
    List<Path> usedPlus = new UsedPathsPlus().of(classB);
    //usedPlus=Functions.remove1OuterAndPrimitives(usedPlus);
    //usedPlus=Map.of( pi->From.fromP(pi, Path.outer(1)),usedPlus);
    Program p1=p.addAtTop(classB);
    List<ClassB> es=new ArrayList<>();//null represents a meta expression
    for(Path pi:usedPlus){
      if(!pi.isCore()){continue;}
      try{es.add(p1.extract(pi));}
      catch(ErrorMessage.ProgramExtractOnMetaExpression meta){es.add(null);}
      catch(ErrorMessage.ProgramExtractOnWalkBy walk){ }
      catch(ErrorMessage.PathNonExistant incomplete){
        //TODO: booh what here? not throw error to allows more flexible composition
        //operators on incomplete code
      }
      }
    collectInnerClasses(classB,es);
    return es;
  }
  static ClassB etSub(Program p, ClassB classB) {
    ClassB inh=inherited(p,classB);//checks no bad diamonds
    if(inh==null){return null;}
    List<Path> sup=new ArrayList<>(classB.getSupertypes());
    sup.addAll(inh.getSupertypes());
    List<Member> members=composeUnion(inh.getMs(),classB.getMs());
    for(Member m:members){
      if (m instanceof MethodImplemented){
        throw new ErrorMessage.InvalidMethodImplemented(inh.getMs(),(MethodImplemented)m,classB,p.getInnerData());
        }
      }

    ClassB result=new ClassB(classB.getDoc1(),classB.getDoc2(),classB.isInterface(),sup,members,Stage.Star);
    return result;
  }
  static ClassB etDeep(Program p,ClassB classB){
    for(Member m:classB.getMs()){
      try{
        if(!(m instanceof NestedClass)){continue;}
        NestedClass nc=(NestedClass)m;
          if(!(nc.getInner() instanceof ClassB)){continue;}
          ClassB inner=(ClassB)nc.getInner();
          ClassB outer=classB;
          Program p2=p.addAtTop(outer);
          return outer.withMember(nc.withInner(etDispatch(p2,inner)));
      }
      catch(InternalError.ETDeepNotApplicable ignored){}
    }
    throw new InternalError.ETDeepNotApplicable();
  }


  /**return null for impossible to compute*/
  static Ast.Stage stage(Program p,ClassB cb,List<ClassB>es/*can have nulls*/){
    assert IsCompiled.of(cb);
    for(ClassB cbi: es){
      if(cbi==null){return Stage.Less;}
      if(cbi.getStage()==Stage.Less){return Stage.Less;}
      if(!IsCompiled.of(cbi)){return Stage.Less;}
    }
    if(Functions.isAbstract(p,cb)){return Stage.Plus;}
    for(ClassB cbi: es){
      assert cbi!=null;
      if(cbi.getStage()==Stage.Plus){return Stage.Plus;}
    }
    return null;//can not be computed
   }
  /**return null for impossible to compute*/
  static ClassB inherited(Program p,ClassB cb){
    assert IsCompiled.of(cb);
    List<Path> paths=new ArrayList<>();
    List<Member> members=new ArrayList<>();
    List<Set<Ast.MethodSelector>> original=new ArrayList<>();
    Program p2=p.addAtTop(cb);
    List<Path> supers = cb.getSupertypes();
    supers=Map.of(pi->Norm.of(p2,pi),supers);
    for(Path pi:new LinkedHashSet<>(supers)){
      try{
        boolean failFast=inheritedSinglePath(p, cb, paths, members, original,p2, pi);
        if(failFast){return null;}
      }
      catch( ErrorMessage.ProgramExtractOnMetaExpression ignored){return null;}
    }
    checkOriginals(p, cb, original);
    return new ClassB(Doc.empty(),Doc.empty(),false,paths,members,Ast.Stage.Star);
  }
  private static void checkOriginals(Program p, ClassB cb, List<Set<Ast.MethodSelector>> original) {
    //if there is an element repeated in originals
    Set<MethodSelector> collapsed=new HashSet<>();
    for(Set<MethodSelector> o:original){
      for(MethodSelector ms:o){
        boolean wasThere=!collapsed.add(ms);
        if(wasThere){
          throw new ErrorMessage.TwoDifferentImplementedInterfacesDeclareMethod(ms,cb,p.getInnerData());
          }
        }
      }
  }
  private static boolean inheritedSinglePath(Program p, ClassB cb, List<Path> paths, List<Member> members, List<Set<Ast.MethodSelector>> original,Program p2, Path pi) {
    ClassB cbi=p2.extract(pi);
    if(!cbi.isInterface()){
      throw new ErrorMessage.MalformedSubtypeDeclaration(cb,cbi,pi,p.getInnerData());
      }
    if(cbi.getStage()==Stage.None){return true;}
    List<Path> pathsi = Map.of(pii->From.fromP(pii,pi), cbi.getSupertypes());
    paths.addAll(pathsi);
    original.add(Functions.originalMethOf(p2,pathsi,cbi.getMs()));
    for(Member _mii:cbi.getMs()){
      if(!(_mii instanceof ClassB.MethodWithType)){continue;}
      ClassB.MethodWithType mii=(ClassB.MethodWithType)_mii;
      assert !mii.getInner().isPresent();
      Optional<Member> isThere = Program.getIfInDom(members, mii);
      if(!isThere.isPresent()){
        members.add(From.from(mii,pi));
        }
      else{
        if(!From.from(mii,pi).equals(isThere.get())){//if equal, is checked later
          throw new ErrorMessage.TwoDifferentImplementedInterfacesDeclareMethod(mii.getMs(),cb,p.getInnerData());
        }
      }
    }
    return false;
  }

  //static boolean isCt(ClassB cb);

  //--------------------
  //--------------------
  public static List<ExpCore.ClassB.Member> composeComma(List<ExpCore.ClassB.Member> a,List<ExpCore.ClassB.Member> b){
    List<ExpCore.ClassB.Member> result=new  ArrayList<ExpCore.ClassB.Member>(b);
    for(ExpCore.ClassB.Member m:a){
      if(Program.getIfInDom(b,m).isPresent()){throw new ast.ErrorMessage.ImpossibleToCompose(a,b);}
      result.add(m);
    }
    return result;
  }
  public static List<ExpCore.ClassB.Member> composeUnion(List<ExpCore.ClassB.Member> a,List<ExpCore.ClassB.Member> b){
    List<ExpCore.ClassB.Member> result=new  ArrayList<ExpCore.ClassB.Member>();
    for(ExpCore.ClassB.Member m:a){
      assert m instanceof ExpCore.ClassB.MethodWithType;
      ExpCore.ClassB.MethodWithType mt=(ExpCore.ClassB.MethodWithType)m;
      Optional<Member> other = Program.getIfInDom(b,m);
      if(!other.isPresent()){
        result.add(m);
        continue;
        }
      other.get().match(nc->{throw Assertions.codeNotReachable();},
          mi->{return result.add(mt.withInner(Optional.of(mi.getInner())).withDoc(mt.getDoc().sum(mi.getDoc())));},
          mt2->{
            if (mt2.equals(mt)){return true;}//ecco perche avevo la sorgente, per il diamante, sono uguali, con sorgente solo per diamante!
            throw new ast.ErrorMessage.MalformedUnionOfMembers(mt, mt2);
            }
          );
    }
    for(ExpCore.ClassB.Member m:b){
      Optional<Member> other = Program.getIfInDom(result,m);
      if(other.isPresent()){continue;}
      result.add(m);
      }
    return result;
  }


  /*public static List<ClassB> collectInnerClasses(ClassB cb) {
    List<ClassB>result=new ArrayList<>();
    collectInnerClasses(cb,result);
    return result;
    }*/
    public static void collectInnerClasses(ClassB cb,List<ClassB>result) {
      result.add(cb);
      for(Member m:cb.getMs()){
        if(!(m instanceof NestedClass)){continue;}
        NestedClass nc=(NestedClass)m;
        if (!(nc.getInner() instanceof ClassB)){result.add(null);}
        ClassB cbi=(ClassB)nc.getInner();
        collectInnerClasses(cbi,result);
        }
    }
  /*private static Stage worstStage(Stage s0, Stage s1) {
    if(s0==s1){return s0;}
    if(s0==Stage.Less || s1==Stage.Less){return Stage.Less;}
    if(s0==Stage.Star ){return s1;}
    assert s1==Stage.Star:s1;
    return s0;
  }*/

}

package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import tools.Assertions;
import coreVisitors.Exists;
import coreVisitors.From;
import facade.Configuration;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.Signal;
import ast.ExpCore._void;
import ast.Expression;
import ast.Util;
import ast.Util.InfoAboutMs;
import ast.Util.PathMwt;
import ast.ExpCore.ClassB.*;
import ast.Ast.*;

public class Program {
  private final Program next;
  private final ClassB cb;//cb is like the source, with no walkby
 // private final ClassB ct;//ct is the annotated one, with collected interface types and walkby
 // //nullable cb or ct, not both
  private Program(Program next,ClassB cb/*,ClassB ct*/){
   // assert this.getClass()!=Program.class || (next!=null  && (cb!=null || ct!=null));
    this.next=next;this.cb=cb;//this.ct=ct;
    }
  /*public ClassB getCt(int num){
    //assert this.classB!=null;
    assert !this.isEmpty():
      "empty program reached";
    if(num==0){return this.ct;}
    return this.next.getCt(num-1);
    }*/
  public ClassB getCb(int num){
    //assert this.classB!=null;
    assert !this.isEmpty():
      "empty program reached";
    if(num==0){return this.cb;}
    return this.next.getCb(num-1);
    }

  private static final Program regularEmpty=new Program(null,null){ };
  private static final Program executableStarEmpty=new Program(null,null){};

  public static Program empty(){return regularEmpty;}
  public boolean isExecutableStar(){
    if (this.isEmpty()){return this==executableStarEmpty;}
    return this.pop().isExecutableStar();
  }
  public Program getExecutableStar(){
    assert this!=executableStarEmpty;//may be not needed
    if (this==regularEmpty){return executableStarEmpty;}
    return new Program(this.pop().getExecutableStar(),this.topCb()/*,this.topCt()*/);
  }
  public Program removeExecutableStar() {
    assert this!=regularEmpty;//may be not needed
    if (this==executableStarEmpty){return regularEmpty;}
    return new Program(this.pop().removeExecutableStar(),this.topCb());
  }

  public Stage getStage(){
    if(cb==null){Assertions.codeNotReachable();}
    return cb.getStage().getStage();
    //if(ct==null){Assertions.codeNotReachable();}
    //return ct.getStage().getStage();
    }
  public Stage getStage(Path p){return this.extractCb(p).getStage().getStage();};
  //public void __addAtTop(ClassB cb){this.inner.add(0,cb);}
  public void recomputeStage(){
    if(this.isEmpty()){return;}
    this.pop().recomputeStage();
    Configuration.typeSystem.computeStage(this.pop(),this.topCb());
  }
  public boolean canBeAdded(ClassB cb){
    if(this.isEmpty()){return true;}
    assert !cb.equals(this.topCb());
    if(cb.getMs().size()!=this.topCb().getMs().size()){return true;}
    assert cb.getMs().isEmpty() ||
      cb.getMs().get(0) !=this.topCb().getMs().get(0);
    //this can actually happen assert !cb.getMs().get(0).equals(this.topCb().getMs().get(0));
    return true;
  }
  public Program addAtTop(ClassB cb){
    assert canBeAdded(cb);
    return new Program(this,cb);
    }
  public Program pop(){assert !this.isEmpty();return this.next;}
  public Program pop(int n){
    assert n>=0;
    if(n==0){return this;}
    return this.pop().pop(n-1);
    }
  public boolean isEmpty(){return this.next==null;}

  public ClassB topCb(){
    assert this.cb!=null;
    return this.cb;
    }
  /*public ClassB topCt(){
    assert this.ct!=null;
    return this.ct;
    }*/

  public boolean executablePlus(){
    assert !this.isEmpty();
    //return this.ct.getStage().getStage()!=Stage.Less;
    return this.cb.getStage().getStage()!=Stage.Less;
  }

  public boolean executablePlus(Path p){
    return this.extractCb(p).getStage().getStage()!=Stage.Less;
  }
  public boolean executable(Path p){
    return this.extractCb(p).getStage().getStage()==Stage.None;
  }

/*
    public Program collapse(int n){
    if(n==0){return this;}
    return this.collapseOne().collapse(n-1);
  }
  public Program collapseOne(){
    ClassB cb=this.top();
    Program result=this.next;
    ClassB cbNext=replaceWalkByWith(result.top(),cb);
    result=result.next.addAtTop(cbNext);
    return result;
  }*/
  public Program navigateInTo(String c){
    assert !this.isEmpty();
    Optional<Member> mOpt=getIfInDom(this.topCb().getMs(),c);
    if(!mOpt.isPresent()){
      throw new ErrorMessage.PathNonExistant(Arrays.asList(c),this.topCb(),null);
    }
    Member m=mOpt.get();
    Program result=this;
    return result.addAtTop((ClassB)((NestedClass)m).getInner());
  }
  public Program navigateInTo(List<String> paths){
    if(paths.isEmpty()){return this;}
    return this.navigateInTo(paths.get(0)).navigateInTo(paths.subList(1,paths.size()));
    }

  public ClassB extractCb(Path path){
    ClassB cb=this.getCb(path.outerNumber());
    cb = extractCBar(path.getCBar(), cb);
    assert cb!=null;
    return cb;
  }
  /*public ClassB extractCt(Path path){
    ClassB ct=this.getCt(path.outerNumber());
    if(ct==null){return null;}
    ct = extractCBar(path.getCBar(), ct);
    //if(ct==null){return null;}
    return ct;
  }*/
  private static final Doc[] _trashCommentRef=new Doc[]{Doc.empty()};
  private static final Boolean[] _trashIsPrivateRef=new Boolean[]{false};
  public static ClassB extractCBar(List<String> list, ClassB cb) {
    return extractCBar(list, cb,_trashCommentRef,_trashIsPrivateRef);
  }
  public static ClassB extractCBar(List<String> list, ClassB cb,Doc[] commentRef) {
    return extractCBar(list, cb,commentRef,_trashIsPrivateRef);
  }
  public static ClassB extractCBar(List<String> list, ClassB cb,Boolean[] isPrivateRef) {
    return extractCBar(list, cb,_trashCommentRef,isPrivateRef);
  }
  public static ClassB extractCBar(List<String> list, ClassB cb,Doc[] commentRef,Boolean[]isPrivateRef) {
    assert cb!=null;
    for(String s:list){
      Optional<Member> optNc = Program.getIfInDom(cb.getMs(),s);
      if(!optNc.isPresent()){
        throw new ErrorMessage.PathNonExistant(list,cb,null);
        }
      NestedClass nc=(NestedClass)optNc.get();
      ExpCore ec=nc.getInner();
      commentRef[0]=nc.getDoc();
      isPrivateRef[0]|=nc.getDoc().isPrivate();
      if(ec instanceof ExpCore.WalkBy){
        throw new ErrorMessage.ProgramExtractOnWalkBy(null,new ArrayList<>(Arrays.asList(cb)));
        }
      if(ec instanceof ClassB){cb=(ClassB)nc.getInner();}
      else {
        throw new ErrorMessage.ProgramExtractOnMetaExpression(new Path(list),Collections.singletonList(cb));
        }
    }
    return cb;
  }
  public MethodWithType method(Path path,MethodSelector ms,ExpCore.MCall forError,boolean isOnlyType){
    if(path.isPrimitive()){
      throw new ErrorMessage.MethodNotPresent(path,ms,forError,this.getInnerData(),forError.getP());
      }
    ClassB classB=extractCb(path);
    if(classB==null){classB=extractCb(path);}
//    path=Path.parse("This0.C.C");
    //classB=(ClassB)From.from(classB,path);
    Optional<Member> result = getIfInDom(classB.getMs(),ms);
    if(!result.isPresent()){
      for(PathMwt mt:classB.getStage().getInherited()){
        if(!mt.getMwt().getMs().equals(ms)){continue;}
        return From.from(mt.getMwt(), path);
        }
      throw new ErrorMessage.MethodNotPresent(path,ms,forError,this.getInnerData(),forError.getP());
      }
    MethodWithType mwt=Program.extractMwt(result.get(),classB);
    mwt=From.from(mwt, path);
    //mwt=Norm.of(this,mwt,isOnlyType);
    return mwt;
  }

  public static Optional<NestedClass> findWalkBy(ClassB classB) {
    for(Member m:classB.getMs()){
      if(m.match(nc->nc.getInner() instanceof ExpCore.WalkBy, c2->false, c3->false)){
        return Optional.of(((NestedClass)m));
      }
    }
    return Optional.empty();
  }
  public static Optional<Member> getIfInDom(List<ExpCore.ClassB.Member> map, String elem){
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


  public List<ExpCore.ClassB> getInnerData() {
    List<ExpCore.ClassB> result=new ArrayList<ExpCore.ClassB>();
    Program p=this;
    while(p.next!=null){result.add(p.cb);p=p.next;}
    return result;
  }
  public static ClassB replaceWalkByWith(ClassB cb, ExpCore newExp) {
    ClassB ct=cb;
    Optional<NestedClass> opt = findWalkBy(ct);
    if(opt.isPresent()){
      ct=ct.withMember(opt.get().withBody(newExp));
    }
    return ct;
  }
  public boolean isNotClassB(Path path) {
    assert !path.isPrimitive():"method isNotClassB is not defined over primitive paths";
    try{//like extract but no normalize
      ClassB cb=this.getCb(path.outerNumber());
      cb = extractCBar(path.getCBar(), cb);
      }
    catch(ErrorMessage.ProgramExtractOnMetaExpression found){return true;}
    catch(ErrorMessage.ProgramExtractOnWalkBy found){return true;}
    return false;
  }




  /*
  public boolean checkFullyNormalized(){
    if(this.isEmpty()){return true;}
    checkFullyNormalized(this.topCb());
    return this.pop().checkFullyNormalized();
  }


  private static boolean checkFullyNormalized(ClassB cb) {
    for(Member m:cb.getMs()){
      m.match(
        nc->checkFullyNormalized((ClassB)nc.getInner()),
        mi->{throw Assertions.codeNotReachable();},
        mt->checkFullyNormalized(mt)
        );
      }
    return true;
  }
  private static boolean checkFullyNormalized(MethodWithType mt) {
    checkFullyNormalized(mt.getMt());
    if(!mt.getInner().isPresent()){return true;}
    ExpCore e=mt.getInner().get();
    return Exists.of(e, s->{
      if(!(s instanceof ExpCore.Block)){return false;}
      ExpCore.Block b=(ExpCore.Block)s;
      for( Dec d:b.getDecs()){
        assert d.getT() instanceof NormType;
      }
      if(!b.get_catch().isPresent()){return false;}
      for(ExpCore.Block.On on:b.get_catch().get().getOns()){
        assert on.getT()instanceof NormType;
      }
      return false;
    });
  }
  private static void checkFullyNormalized(MethodType mt) {
    assert mt.getReturnType() instanceof NormType;
    for(Type t:mt.getTs()){
      assert t instanceof NormType;
    }
  }*/
  public void exePlusOk(HashMap<String,NormType> varEnv){
    if(this.getStage()==Stage.Less){return;}
    for(NormType nt:varEnv.values()){
      if(nt.getPath().isPrimitive()){continue;}
      if(this.getStage(nt.getPath())!=Stage.Less){continue;}
      throw new ErrorMessage.PathNonStar(nt.getPath(),varEnv,null);
    }
  }
  public boolean checkComplete(){
    if(this.isEmpty()){return true;}
    if(this.topCb().getStage().getStage()!=Stage.Star){return false;}
    return this.pop().checkComplete();
  }
  public static Program getExtendedProgram(Program p,List<ClassB>extension){
    for(ClassB cb:extension){
      if(cb!=null){
        Configuration.typeSystem.computeStage(p, cb);
        p=p.addAtTop(cb);
        }
      else{return p;}
    }
    return p;
  }
  private static void accumulateAllSupertypes(ClassB forErr,List<Path> ps,Program p,Path pi){
    if(ps.contains(pi)){return;}
    ps.add(pi);
    if(pi.isPrimitive()){return;}
    ClassB cbi=p.extractCb(pi);
    if (!cbi.isInterface()){
      throw new ErrorMessage.MalformedSubtypeDeclaration(forErr,cbi, pi, p.getInnerData());
    }
    for(Path pj:cbi.getSupertypes()){
      pj=From.fromP(pj,pi);
      accumulateAllSupertypes(cbi,ps, p, pj);
    }
  }
  public static List<Path> removeDuplicates(List<Path>paths, List<String> explored){
    List<Path> result=new ArrayList<>();
    for(Path pi:paths){
      pi=From.normalizeShort(pi, explored);
      if (!result.contains(pi)){result.add(pi);}
    }
    return result;
  }
  /**cb must be frommed so that is like it is the top of p,*/
  //can return "duplicated" with different ways to refer to the same path, use  remove duplicates
  //to remove those doubles.
  public static List<Path> getAllSupertypes(Program p,ClassB cb){
    List<Path> result=new ArrayList<>();
    for(Path pi:cb.getSupertypes()){
      accumulateAllSupertypes(cb,result, p, pi);
      }
    return result;
  }
  public static InfoAboutMs getMT(Program p,MethodSelector ms, ClassB cb){
    assert p.getCb(0)==cb;
    List<Path> ps = getAllSupertypes(p,cb);
    for(Path pi:ps){
      ClassB candidate=p.extractCb(pi);
      Optional<Member> opt = Program.getIfInDom(candidate.getMs(),ms);
      if(opt.isPresent()){
        Ast.MethodType mt=From.from(((MethodWithType)opt.get()),pi).getMt();
        return new InfoAboutMs(ps, pi, mt);
        }
    }
    throw Assertions.codeNotReachable();
    }
  public Optional<MethodWithType> extractMwt(MethodSelector sel,ClassB cb){
    Optional<Member> res1 = Program.getIfInDom(cb.getMs(),sel);
    if(res1.isPresent()){return Optional.of(extractMwt(res1.get(),cb));}
    assert cb.getStage()!=null;
    Configuration.typeSystem.computeInherited(this, cb);
    for(PathMwt mt:cb.getStage().getInherited()){
      if(!mt.getMwt().getMs().equals(sel)){continue;}
      return Optional.of(mt.getMwt());
      }
    return Optional.empty();
  }
  public static MethodWithType extractMwt(Member m,ClassB cb){
    return m.match(nc->{throw Assertions.codeNotReachable();},
        mi->{
          assert cb.getStage().isInheritedComputed();
          for(PathMwt mt:cb.getStage().getInherited()){
            if(!mt.getMwt().getMs().equals(mi.getS())){continue;}
            return mt.getMwt().withInner(Optional.of(mi.getInner()));
          }
          throw Assertions.codeNotReachable();
        },
        mt->mt);
  }
}

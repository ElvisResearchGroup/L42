package is.L42.connected.withSafeOperators;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import tools.Assertions;
import tools.Map;
import coreVisitors.CloneVisitor;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.FromInClass;
import facade.Configuration;
import facade.L42;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.C;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.MethodSelector;
import ast.Ast.Stage;

import ast.Util.PathMx;
import ast.Util.PathMxMx;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.Util.PathPath;
import auxiliaryGrammar.Functions;
import programReduction.Program;

public class Rename {
  public static ClassB renameClass(Program p,ClassB cb,List<Ast.C> src,List<Ast.C> dest){
    Errors42.checkExistsPathMethod(cb, src, Optional.empty());
    if(src.equals(dest)){return cb;}
    if(src.isEmpty()){//push is asked
      return Push.pushMany(cb, dest);
    }
    if(dest.isEmpty() && src.size()==1){//pop is asked
      boolean rightSize=cb.getMs().size()==1;
      if(rightSize && ExtractInfo.isBox(cb,cb,Collections.emptyList())){
        return Pop.pop(cb);
        }//otherwise, proceed with encoding
    }
    cb=ClassOperations.normalizePaths(cb);
    if(!ExtractInfo.isPrefix(src, dest)){ return ClassOperations.normalizePaths(directRename(p,cb,src,dest));}
    src=new ArrayList<>(src);
    dest=new ArrayList<>(dest);
    C result=C.of("Result");
    src.add(0,result);
    dest.add(0,result);
    cb=Push.pushOne(cb,result);
    List<Ast.C> tmp = Collections.singletonList(C.of("Tmp"));
    cb=directRename(p,cb,src,tmp);
    if(!L42.trustPluginsAndFinalProgram) {
      newTypeSystem.TypeSystem.instance().topTypeLib(Phase.Typed, p.evilPush(cb));
      }
    cb=directRename(p,cb,tmp,dest);
    cb=Pop.directPop(cb);
    return cb;
  }
  public static ClassB renameClassStrict(Program p,ClassB cb,List<Ast.C> src,List<Ast.C> dest){
  /*
  errors:
  src is prefix of dest
  dest is prefix of src
  src does not exists
  dest+src is wrong
  */
    Errors42.checkExistsPathMethod(cb, src, Optional.empty());
    if(ExtractInfo.isPrefix(src,dest)){throw Errors42.errorPrefix(src,dest);}
    return ClassOperations.normalizePaths(directRename(p, cb, src, dest));
  }
  private static ClassB directRename(Program p, ClassB cb, List<Ast.C> src, List<Ast.C> dest) {
    CollectedLocatorsMap clm=CollectedLocatorsMap.from(Path.outer(0,src), Path.outer(0,dest));
    //rename srcC in destC in top
    ClassB renamedCb=(ClassB)new RenameAlsoDefinition(cb,clm,p).visit(cb);
    //clearCb=remove srcC from top
    ClassB clearCb=renamedCb.onNestedNavigateToPathAndDo(src,nc->Optional.empty());
    //newCB=take srcC from top, and adjust paths to dest
    ClassB newCb=redirectDefinition(src,dest,renamedCb);
    newCb=ClassOperations.normalizePaths(newCb);
    //optionally sum renamed srcC in destC
    return _Sum.normalizedTopSum(p, clearCb, newCb);
    /*
      L0[DirectRename src->dest]p =L //with src=C._, dest=C'._ and C!=C'
        L1=L0[PathRename src->dest]
        L2=L1\src
        L3=L1(src)
        L4=L3[from This(dest.size()-1).src.removeLast()][push dest]
        L=L2++p L4
     
     */
  }
  public static ClassB renameMethod(Program p,ClassB cb,List<Ast.C> path,MethodSelector src,MethodSelector dest){
      Member mem=Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      assert mem instanceof MethodWithType;
      Errors42.checkCompatibleMs(path,(MethodWithType)mem,dest);
      CollectedLocatorsMap maps=CollectedLocatorsMap.from(Path.outer(0,path),(MethodWithType) mem,dest);
      RenameAlsoDefinition ren=new RenameAlsoDefinition(cb, maps,p);
     return (ClassB) ren.visit(cb);
    }
  static class UserForMethodResult{List<PathMx> asClient;List<MethodSelector>asThis;}
  public static UserForMethodResult userForMethod(Program p,ClassB cb,List<Ast.C> path,MethodSelector src,boolean checkMethExists ){
    if(checkMethExists){
      Member mem=Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      assert mem instanceof MethodWithType;
      }
    Member mem=new ExpCore.ClassB.MethodImplemented(Doc.empty(),src,new ExpCore._void(),Position.noInfo);
    CollectedLocatorsMap maps=CollectedLocatorsMap.from(Path.outer(0,path), mem,src);
    HashSet<PathMx> result1=new HashSet<>();
    HashSet<MethodSelector> result2=new HashSet<>();
    MethodPathCloneVisitor ren=new RenameUsage(cb, maps,p){
      public Ast.Type liftT(Ast.Type t){return t;}
      @Override protected MethodSelector liftMs(MethodSelector ms){return ms;}
      @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){return ms;}
      public ExpCore visit(MCall s) {
        List<Ast.C> localPath = this.getLocator().getClassNamesPath();
        if(!localPath.equals(path)){return super.visit(s);}
        if(s.getInner().equals(Path.outer(0)) || s.getInner().equals(new ExpCore.X(Position.noInfo,"this"))){
            result2.add(s.getS());
            return s.withInner(s.getInner().accept(this)).withEs(Map.of(e->e.accept(this), s.getEs()));
            }
        return super.visit(s);
        }
      @Override public MethodSelector visitMS(MethodSelector original, Path src) {
        MethodSelector toCollect=this.mSToReplaceOrNull(original, src);
        if(toCollect==null){return original;}
        Member m=this.getLocator().getLastMember();
        assert !(m instanceof NestedClass):
          "";
        MethodSelector msUser=m.match(nc->{throw Assertions.codeNotReachable();},
            mi->mi.getS(), mt->mt.getMs());
        Path pathUser=Path.outer(0,this.getLocator().getClassNamesPath());
        result1.add(new PathMx(pathUser,msUser));
        return original;
      }
    };
   ren.visit(cb);
   return new UserForMethodResult(){{asClient=new ArrayList<>(result1);asThis=new ArrayList<>(result2);}};
  }


  private static ClassB redirectDefinition(List<Ast.C>src,List<Ast.C>dest, ClassB lprime) {
    assert !src.isEmpty();
    assert !dest.isEmpty();
    NestedClass nsCb=lprime.getNested(src);
    Path toFrom=Path.outer(dest.size()-1,src.subList(0,src.size()-1));
    ClassB cb=(ClassB) FromInClass.of((ClassB) nsCb.getInner(), toFrom);
    List<Member>ms=new ArrayList<>();
    ms.add(Functions.encapsulateIn(dest, cb,nsCb.getDoc()));
    return ClassB.membersClass(ms,cb.getP(),lprime.getPhase());
  }

  //TODO: replace with same mechanism of private normalization when is completed
  static ClassB renameUsage(List<PathPath> mapPath, ClassB cb) {
    return (ClassB)cb.accept(new coreVisitors.CloneWithPath(){
     @Override protected Path liftP(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<Ast.C> path = this.getLocator().getClassNamesPath();
        if(s.outerNumber()>path.size()){return s;}
        List<Ast.C> unexploredPath=path.subList(0,path.size()-s.outerNumber());//in usedPath similar thing.
        if(unexploredPath.contains(null)){return s;}//we are in a class literal in a method and we look inside
        if(s.outerNumber()>path.size()){return s;}
        List<Ast.C>topView=ClassOperations.toTop(path,s);
        for(PathPath pp:mapPath){
          List<Ast.C>src=pp.getPath1().getCBar();
          if(topView.size()<src.size()){continue;}
          if(topView.equals(src)){
            if(pp.getPath2().isPrimitive()){
              return pp.getPath2();
              }
            if(pp.getPath2().outerNumber()==0){
              return ClassOperations.normalizePath(path,path.size(),pp.getPath2().getCBar());
              }
            return pp.getPath2().setNewOuter(pp.getPath2().outerNumber()+path.size());
            }
          List<Ast.C>trimmedTop=topView.subList(0, src.size());
          if(trimmedTop.equals(src)){
            List<Ast.C>elongatedDest=new ArrayList<>(pp.getPath2().getCBar());
            elongatedDest.addAll(topView.subList(src.size(),topView.size()));
            if(pp.getPath2().outerNumber()==0){
              return ClassOperations.normalizePath(path,path.size(),elongatedDest);
              }
            else{
              return Path.outer(pp.getPath2().outerNumber()+path.size(),elongatedDest);
            }}}
        return s;
        }
       });
}
  }
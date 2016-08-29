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
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.MethodSelector;
import ast.Ast.Stage;
import ast.Util.CachedStage;
import ast.Util.PathMx;
import ast.Util.PathMxMx;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathPath;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class Rename {
  public static ClassB renameClass(Program p,ClassB cb,List<String> src,List<String> dest){
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
    cb=NormalizePrivates.normalize(p, cb);
    cb=ClassOperations.normalizePaths(cb);
    if(!ExtractInfo.isPrefix(src, dest)){ return ClassOperations.normalizePaths(directRename(p,cb,src,dest));}
    src=new ArrayList<>(src);
    dest=new ArrayList<>(dest);
    src.add(0,"Result");
    dest.add(0,"Result");
    cb=Push.pushOne(cb,"Result");
    List<String> tmp = Collections.singletonList("Tmp");
    cb=directRename(p,cb,src,tmp);
    if(!L42.trustPluginsAndFinalProgram) {Configuration.typeSystem.checkCt(p, cb);}
    cb=directRename(p,cb,tmp,dest);
    cb=Pop.directPop(cb);
    return cb;
  }
  public static ClassB renameClassStrict(Program p,ClassB cb,List<String> src,List<String> dest){
  /*
  errors:
  src is prefix of dest
  dest is prefix of src
  src does not exists
  dest+src is wrong
  */
    Errors42.checkExistsPathMethod(cb, src, Optional.empty());
    if(ExtractInfo.isPrefix(src,dest)){throw Errors42.errorPrefix(src,dest);}
    cb=NormalizePrivates.normalize(p, cb);
    return ClassOperations.normalizePaths(directRename(p, cb, src, dest));
  }
  private static ClassB directRename(Program p, ClassB cb, List<String> src, List<String> dest) {
    CollectedLocatorsMap clm=CollectedLocatorsMap.from(Path.outer(0,src), Path.outer(0,dest));
    ClassB renamedCb=(ClassB)new RenameAlsoDefinition(cb,clm,p).visit(cb);
    ClassB clearCb=ClassOperations.onNestedNavigateToPathAndDo(renamedCb,src,nc->Optional.empty());
    ClassB newCb=redirectDefinition(src,dest,renamedCb);
    newCb=ClassOperations.normalizePaths(newCb);
    return Sum.normalizedTopSum(p, clearCb, newCb);
  }
  public static ClassB renameMethod(Program p,ClassB cb,List<String> path,MethodSelector src,MethodSelector dest){
      Member mem=Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      assert mem instanceof MethodWithType;
      Errors42.checkCompatibleMs(path,(MethodWithType)mem,dest);
      cb=NormalizePrivates.normalize(p, cb);
      CollectedLocatorsMap maps=CollectedLocatorsMap.from(Path.outer(0,path),(MethodWithType) mem,dest);
      RenameAlsoDefinition ren=new RenameAlsoDefinition(cb, maps,p);
     return (ClassB) ren.visit(cb);
    }
  static class UserForMethodResult{List<PathMx> asClient;List<MethodSelector>asThis;}
  public static UserForMethodResult userForMethod(Program p,ClassB cb,List<String> path,MethodSelector src,boolean checkMethExists ){
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
        List<String> localPath = this.getLocator().getClassNamesPath();
        if(!localPath.equals(path)){return super.visit(s);}
        if(s.getInner().equals(Path.outer(0)) || s.getInner().equals(new ExpCore.X("this"))){
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


  private static ClassB redirectDefinition(List<String>src,List<String>dest, ClassB lprime) {
    assert !src.isEmpty();
    assert !dest.isEmpty();
    Doc[] docCb=new Doc[]{Doc.empty()};
    ClassB cb=Program.extractCBar(src, lprime,docCb);
    Path toFrom=Path.outer(dest.size()-1,src.subList(0,src.size()-1));
    cb=(ClassB)FromInClass.of(cb, toFrom);
    List<Member>ms=new ArrayList<>();
    ms.add(Functions.encapsulateIn(dest, cb,docCb[0]));
    return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Position.noInfo,new CachedStage());
  }

  //TODO: replace with same mechanism of private normalization when is completed
  static ClassB renameUsage(List<PathPath> mapPath, ClassB cb) {
    return (ClassB)cb.accept(new coreVisitors.CloneWithPath(){
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String> path = this.getLocator().getClassNamesPath();
        if(s.outerNumber()>path.size()){return s;}
        List<String> unexploredPath=path.subList(0,path.size()-s.outerNumber());//in usedPath similar thing.
        if(unexploredPath.contains(null)){return s;}//we are in a class literal in a method and we look inside
        if(s.outerNumber()>path.size()){return s;}
        List<String>topView=ClassOperations.toTop(path,s);
        for(PathPath pp:mapPath){
          List<String>src=pp.getPath1().getCBar();
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
          List<String>trimmedTop=topView.subList(0, src.size());
          if(trimmedTop.equals(src)){
            List<String>elongatedDest=new ArrayList<>(pp.getPath2().getCBar());
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
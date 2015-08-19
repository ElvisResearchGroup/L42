package is.L42.connected.withSafeOperators;

import introspection.IntrospectionAdapt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tools.Map;
import coreVisitors.CloneVisitor;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.FromInClass;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.MethodSelector;
import ast.Ast.Stage;
import ast.Util.PathMxMx;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.Util.PathPath;
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
    cb=ClassOperations.normalizePrivates(p,cb);
    if(!ExtractInfo.isPrefix(src, dest)){ return ClassOperations.normalizePaths(directRename(p,cb,src,dest));}
    src=new ArrayList<>(src);
    dest=new ArrayList<>(dest);
    src.add(0,"Result");
    dest.add(0,"Result");
    cb=Push.pushOne(cb,"Result");
    List<String> tmp = Collections.singletonList("Tmp");
    cb=directRename(p,cb,src,tmp);
    cb=directRename(p,cb,tmp,dest);
    cb=Pop.directPop(cb);
    return ClassOperations.normalizePaths(cb);
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
    cb=ClassOperations.normalizePrivates(p,cb);
    return ClassOperations.normalizePaths(directRename(p, cb, src, dest));
  }
  private static ClassB directRename(Program p, ClassB cb, List<String> src, List<String> dest) {
    //ClassB renamedCb=renameUsage(Collections.singletonList(new PathPath(Path.outer(0,src),Path.outer(0,dest))),cb);//cb, renamedCb are normalized
    ClassB renamedCb=RenameMembers.of(Path.outer(0,src),Path.outer(0,dest),cb);
    //cb, renamedCb are normalized   
    ClassB clearCb=ClassOperations.onNestedNavigateToPathAndDo(renamedCb,src,nc->Optional.empty());
    ClassB newCb=redirectDefinition(src,dest,renamedCb);
    newCb=ClassOperations.normalizePaths(newCb);
    return Sum.normalizedTopSum(p, clearCb, newCb);
  }
  public static ClassB renameMethod(Program p,ClassB cb,List<String> path,MethodSelector src,MethodSelector dest){
    /*
    errors:
    path.src does not exists
    dest+src is wrong
    */
      Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      cb=ClassOperations.normalizePrivates(p,cb);
      PathMxMx pmx=new PathMxMx(Path.outer(0,path),src,dest);
      return IntrospectionAdapt.applyMapMx(p, cb, Collections.singletonList(pmx));
      //TODO: this still calls the old sum... to fix eventually
      //test it so that it fail for it!
    }

  private static ClassB redirectDefinition(List<String>src,List<String>dest, ClassB lprime) {
    assert !src.isEmpty();
    assert !dest.isEmpty();
    Doc[] docCb=new Doc[]{Doc.empty()};
    ClassB cb=Program.extractCBar(src, lprime,docCb);
    Path toFrom=Path.outer(dest.size()-1,src.subList(0,src.size()-1));
    cb=(ClassB)FromInClass.of(cb, toFrom);
    List<Member>ms=new ArrayList<>();
    ms.add(IntrospectionAdapt.encapsulateIn(dest, cb,docCb[0]));
    return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
  }
  /*
  static ClassB renameUsage(List<PathPath> mapPath, ClassB cb) {
    return (ClassB)cb.accept(new coreVisitors.CloneWithPath(){
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String> path = getClassNamesPath();
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
}*/
  }
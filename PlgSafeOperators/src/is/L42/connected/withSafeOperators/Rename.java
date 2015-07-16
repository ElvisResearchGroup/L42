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
    if(src.equals(Path.outer(0))){//push is asked
      return Push.pushMany(cb, dest);
    }
    cb=ClassOperations.normalizePrivates(cb);
    if(!ExtractInfo.isPrefix(src, dest)){ return directRename(p,cb,src,dest);}
    //encoding: push result, rename tmp, rename dest, pop
    return null;//TODO: finish, but normalization issues have precendence here
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
    cb=ClassOperations.normalizePrivates(cb);
    cb=ClassOperations.normalizePaths(cb,Collections.emptyList());//TODO: for perfomance could be merged with renameUsage later
    return directRename(p, cb, src, dest);
  }
  private static ClassB directRename(Program p, ClassB cb, List<String> src, List<String> dest) {
    ClassB renamedCb=renameUsage(src,dest,cb);//cb, renamedCb are normalized
    ClassB clearCb=ClassOperations.onNestedNavigateToPathAndDo(renamedCb,src,nc->Optional.empty());
    ClassB newCb=redirectDefinition(src,dest,renamedCb);
    newCb=ClassOperations.normalizePaths(newCb,Collections.emptyList());
    return Sum.normalizedTopSum(p, clearCb, newCb);
  }
  public static ClassB renameMethod(Program p,ClassB cb,List<String> path,MethodSelector src,MethodSelector dest){
    /*
    errors:
    path.src does not exists
    dest+src is wrong
    */
      Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      cb=ClassOperations.normalizePrivates(cb);
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
    Path toFrom=Path.outer(src.size()-1,dest.subList(0,dest.size()-1));
    cb=(ClassB)FromInClass.of(cb, toFrom);
    List<Member>ms=new ArrayList<>();
    ms.add(IntrospectionAdapt.encapsulateIn(dest, cb,docCb[0]));
    return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
  }
  static ClassB renameUsage(List<String>src,List<String>dest, ClassB cb) {
    return (ClassB)cb.accept(new coreVisitors.CloneWithPath(){
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        if(s.outerNumber()>getPath().size()){return s;}
        List<String>topView=ClassOperations.toTop(getPath(),s);
        if(topView.equals(src)){return ClassOperations.normalizePath(getPath(),0,dest);}
        return s;
        }
       });
}}
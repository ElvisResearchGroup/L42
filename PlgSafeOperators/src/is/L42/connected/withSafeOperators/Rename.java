package is.L42.connected.withSafeOperators;

import introspection.IntrospectionAdapt;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast.Path;
import ast.Ast.MethodSelector;
import ast.Util.PathMxMx;
import ast.ExpCore.*;
import ast.Util.PathPath;
import auxiliaryGrammar.Program;

public class Rename {
  static ClassB renameClass(Program p,ClassB cb,List<String> src,List<String> dest){
  /*
  errors:
  src is prefix of dest
  dest is prefix of src
  src does not exists
  dest+src is wrong
  */
    ExtractInfo.checkExistsPathMethod(cb, src, Optional.empty());
    ExtractInfo.checkPrefix(src,dest);
    cb=Sum.normalize(cb);
    PathPath pp=new PathPath(Path.outer(0,src),Path.outer(0,dest));
    return IntrospectionAdapt.applyMapPath(p, cb, Collections.singletonList(pp));
    //TODO: this still calls the old sum... to fix eventually
    //test it so that it fail for it!
  }
  static ClassB renameMethod(Program p,ClassB cb,List<String> path,MethodSelector src,MethodSelector dest){
    /*
    errors:
    path.src does not exists
    dest+src is wrong
    */
      ExtractInfo.checkExistsPathMethod(cb, path, Optional.of(src));
      cb=Sum.normalize(cb);
      PathMxMx pmx=new PathMxMx(Path.outer(0,path),src,dest);
      return IntrospectionAdapt.applyMapMx(p, cb, Collections.singletonList(pmx));
      //TODO: this still calls the old sum... to fix eventually
      //test it so that it fail for it!
    }
}

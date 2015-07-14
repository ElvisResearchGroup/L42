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
  static ClassB fullRenameClass(Program p,ClassB cb,List<String> src,List<String> dest){
    ExtractInfo.checkExistsPathMethod(cb, src, Optional.empty());
    if(src.equals(dest)){return cb;}
    if(src.equals(Path.outer(0))){//push is asked
      return Push.pushMany(cb, dest);
    }
    cb=Sum.normalize(cb);
    if(!ExtractInfo.isPrefix(src, dest)){ return directRename(p,cb,src,dest);}
    //encoding: push result, rename tmp, rename dest, pop
    return null;//TODO: finish, but normalization issues have precendence here
  }
  static ClassB renameClass(Program p,ClassB cb,List<String> src,List<String> dest){
  /*
  errors:
  src is prefix of dest
  dest is prefix of src
  src does not exists
  dest+src is wrong
  */
    ExtractInfo.checkExistsPathMethod(cb, src, Optional.empty());
    if(ExtractInfo.isPrefix(src,dest)){throw ExtractInfo.errorPrefix(src,dest);}
    cb=Sum.normalize(cb);
    return directRename(p, cb, src, dest);
    //TODO: this still calls the old sum... to fix eventually
    //test it so that it fail for it!
  }
  private static ClassB directRename(Program p, ClassB cb, List<String> src, List<String> dest) {
    PathPath pp=new PathPath(Path.outer(0,src),Path.outer(0,dest));
    return IntrospectionAdapt.applyMapPath(p, cb, Collections.singletonList(pp));
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

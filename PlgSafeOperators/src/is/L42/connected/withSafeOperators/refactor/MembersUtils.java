package is.L42.connected.withSafeOperators.refactor;

import java.util.List;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.C;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;

public class MembersUtils{
  public static ExpCore renameParNames(List<String> ns1, List<String> ns2, ExpCore body) {
    assert ns1.size()==ns2.size();
    java.util.Map<String, String> toRename=tools.Map.list2map(ns1,ns2);
    body=coreVisitors.RenameVars.of(body, toRename);
    return body;
    }
public static boolean isPrivate(MethodSelector ms){
  return ms.isUnique();
  }
public static boolean isPrivate(Path p){return isPrivate(p.getCBar());}
public static boolean isPrivate(List<Ast.C> p){
  for(Ast.C c:p){
    if(c.isUnique()){return true;}
    }
  return false;
  }
public static boolean isPathDefined(ClassB cb, List<Ast.C> path){
  try{cb.getClassB(path);return true;}
  catch(ast.ErrorMessage.PathMetaOrNonExistant unused){return false;}
  }
/**null for not even the path is defined*/
public static Boolean _isPathMsDefined(ClassB cb, List<Ast.C> path,MethodSelector ms){
  try{return cb.getClassB(path)._getMember(ms)!=null;}
  catch(ast.ErrorMessage.PathMetaOrNonExistant unused){return null;}
  }
}
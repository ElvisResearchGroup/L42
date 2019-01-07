package is.L42.connected.withSafeOperators.refactor;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.PathAux;
import auxiliaryGrammar.Functions;
import facade.L42;
public class PathAdapter {

public static
String name(List<Ast.C> that, int index){
  return that.get(index).toString();
  }
public static 
List<Ast.C> concat(List<Ast.C> that,List<Ast.C> and){
  List<Ast.C> res=new ArrayList<>(that);
  res.addAll(and);
  return res;
  }
public static 
boolean isUnique(List<Ast.C> that){
  return MembersUtils.isPrivate(that);
  }
public static 
List<Ast.C> fresh(List<Ast.C> that){
  return Functions.freshPathName(that,L42.usedNames);
  }
public static 
List<Ast.C> rawFromS(String that){
  return PathAux.parseValidCs(that);
  }
public static
String toS(List<Ast.C>that){
  return PathAux.as42Path(that);
  }
}

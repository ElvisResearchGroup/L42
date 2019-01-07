package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util;
import ast.Util.*;
import auxiliaryGrammar.Locator;
import tools.Map;
import ast.Ast.MethodSelector;

public class CollectedLocatorsMap{
final Set<String> pedexes=new HashSet<>();
final Set<Integer> families=new HashSet<>();
final List<Locator>selectors=new ArrayList<>();
final List<Locator>nesteds=new ArrayList<>();
boolean normalized=true;
void notNormalized(){
  this.normalized=false;
}
public String toString(){
  return""+pedexes+"\n"+selectors+"\n"+nesteds+"\n"+normalized;
  }
public static  CollectedLocatorsMap from(Path src,Path dest){
  Locator nl = pathPathToLocator(src,dest);
  CollectedLocatorsMap maps=new CollectedLocatorsMap();
  maps.nesteds.add(nl);
  return maps;
}
public static  CollectedLocatorsMap from(Path src,Member m,MethodSelector ms2){
  Locator result=new Locator();
  result.addCsAndMember(src.getCBar(),m);
  result.setAnnotation(ms2);
  CollectedLocatorsMap maps=new CollectedLocatorsMap();
  maps.selectors.add(result);
  return maps;
}

private static Locator pathPathToLocator(Path src, Path dest) {
  Locator result=new Locator();
  result.addCs(src.getCBar());
  assert src.outerNumber()==0;
  result.setAnnotation(dest);
  return result;
}
}
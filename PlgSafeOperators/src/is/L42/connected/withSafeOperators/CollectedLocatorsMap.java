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
import privateMangling.PrivateHelper;
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
public void computeNewNames(){
  HashMap<Locator,String> map=new HashMap<>();
  for(Locator mL:selectors){computeNewName(map,mL);}
  for(Locator nL:nesteds){nL.setAnnotation(NormalizePrivates.freshName(nL.getLastName()));}
}
private void computeNewName(HashMap<Locator, String> map, Locator mL) {
  Member m=mL.getLastMember();
  assert m instanceof MethodWithType;
  MethodWithType mwt=(MethodWithType)m;
  if( mwt.get_inner().isPresent()){
    mL.setAnnotation(mwt.getMs().withName(NormalizePrivates.freshName(mwt.getMs().getName())));
    return;
  }
  //private and abstract, it must be state!
  Locator locator=mL.copy();
  locator.toFormerNodeLocator();
  String s=map.get(locator);
  if(s==null){
    s="__"+NormalizePrivates.countPrivates++ +"_"+PrivateHelper.countFamilies;//may be turn in method?
    map.put(locator,s);
  }
  String newPedex=s;
  List<String> names = (mwt.getMt().getMdf()!=Mdf.Class)?mwt.getMs().getNames():Map.of(si->NormalizePrivates.freshName(si,newPedex),mwt.getMs().getNames());
  ast.Ast.MethodSelector ms=new ast.Ast.MethodSelector(
      NormalizePrivates.freshName(mwt.getMs().getName(),newPedex),names);
  mL.setAnnotation(ms);
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
public static CollectedLocatorsMap from(List<PathPath> pp){
  CollectedLocatorsMap maps=new CollectedLocatorsMap();
  for(PathPath ppi:pp){
    Locator nl = pathPathToLocator(ppi.getPath1(),ppi.getPath2());
    maps.nesteds.add(nl);
  }
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
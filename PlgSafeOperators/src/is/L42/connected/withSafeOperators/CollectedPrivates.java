package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Mdf;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util;
import ast.Util.*;
import auxiliaryGrammar.Locator;
import tools.Map;

public class CollectedPrivates{
final Set<String> pedexes=new HashSet<>();
final List<Locator>privateSelectors=new ArrayList<>();
final List<Locator>privatePaths=new ArrayList<>();
boolean normalized=true;
public String toString(){
  return""+pedexes+"\n"+privateSelectors+"\n"+privatePaths+"\n"+normalized;
  }
public void computeNewNames(){
  HashMap<Locator,String> map=new HashMap<>();
  for(Locator mL:privateSelectors){computeNewName(map,mL);}
  for(Locator nL:privatePaths){nL.setAnnotation(NormalizePrivates.freshName(nL.getLastName()));}
}
private void computeNewName(HashMap<Locator, String> map, Locator mL) {
  Member m=mL.getLastMember();
  assert m instanceof MethodWithType;
  MethodWithType mwt=(MethodWithType)m;
  if( mwt.getInner().isPresent()){
    mL.setAnnotation(mwt.getMs().withName(NormalizePrivates.freshName(mwt.getMs().getName())));
    return;
  }
  //private and abstract, it must be state!
  Locator locator=mL.copy();
  locator.toFormerNodeLocator();
  String s=map.get(locator);
  if(s==null){
    s="__"+NormalizePrivates.countPrivates++ +"_"+NormalizePrivates.countFamilies;//may be turn in method?
    map.put(locator,s);
  }
  String newPedex=s;
  List<String> names = (mwt.getMt().getMdf()!=Mdf.Type)?mwt.getMs().getNames():Map.of(si->NormalizePrivates.freshName(si,newPedex),mwt.getMs().getNames());
  ast.Ast.MethodSelector ms=new ast.Ast.MethodSelector(
      NormalizePrivates.freshName(mwt.getMs().getName(),newPedex),names);
  mL.setAnnotation(ms);
  }
}
package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ast.Ast.Mdf;
import ast.Util.MethodLocator;
import ast.Util.NestedLocator;
import ast.Util.Locator;
import tools.Map;

public class CollectedPrivates{
final Set<String> pedexes=new HashSet<>();
final List<MethodLocator>privateSelectors=new ArrayList<>();
final List<NestedLocator>privatePaths=new ArrayList<>();
boolean normalized=true;
public String toString(){
  return""+pedexes+"\n"+privateSelectors+"\n"+privatePaths+"\n"+normalized;
  }
public void computeNewNames(){
  HashMap<Locator,String> map=new HashMap<>();
  for(MethodLocator mL:privateSelectors){computeNewName(map,mL);}
  for(NestedLocator nL:privatePaths){nL.setNewName(NormalizePrivates.freshName(nL.getThat()));}
}
private void computeNewName(HashMap<Locator, String> map, MethodLocator mL) {
  if(mL.getThat().getInner().isPresent()){
    mL.setNewName(mL.getThat().getMs().withName(NormalizePrivates.freshName(mL.getThat().getMs().getName())));
    return;
  }
  //it must be state!
  Locator stardizedLocator=new Locator.ImplLocator(mL.getMTail(),mL.getMPos(),mL.getMOuters());
  String s=map.get(stardizedLocator);
  if(s==null){
    s="__"+NormalizePrivates.countPrivates++ +"_"+NormalizePrivates.countFamilies;//may be turn in method?
    map.put(stardizedLocator,s);
  }
  String newPedex=s;
  List<String> names = (mL.getThat().getMt().getMdf()!=Mdf.Type)?mL.getThat().getMs().getNames():Map.of(si->NormalizePrivates.freshName(si,newPedex),mL.getThat().getMs().getNames());
  ast.Ast.MethodSelector ms=new ast.Ast.MethodSelector(
      NormalizePrivates.freshName(mL.getThat().getMs().getName(),newPedex),names);
  mL.setNewName(ms);
  }
}
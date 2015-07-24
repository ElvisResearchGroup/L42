package introspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Program;

public class ConsistentRenaming{
  public static List<PathMxMx> makeMapMxConsistent(ast.ExpCore.ClassB ct, List<PathMxMx> mapMx) {
    List<PathMxMx> result=new ArrayList<>(mapMx);
    HashMap<Path, List<PathMxMx>> divided = divideForPath(mapMx);
    for(Path pi:divided.keySet()){
      addConsistentsForSinglePath(Program.extractCBar(pi.getCBar(),ct), result, divided, pi);
    }
    return result;
  }


  private static void addConsistentsForSinglePath(ast.ExpCore.ClassB cti, List<PathMxMx> result, HashMap<Path, List<PathMxMx>> divided, Path pi){
    HashMap<String, String> consistentNames = collectConsistentRenaming(divided.get(pi),  cti);
    //here I have collected all consistent Names mapping
    for(Member m:cti.getMs()){//for all members, if need adaptation, add them to result.
      if(!(m instanceof MethodWithType)){continue;}
      MethodWithType mwt=(MethodWithType)m;
      MethodSelector adapted=applyConsistentNames(consistentNames,mwt);
      if(mwt.getMs().equals(adapted)){continue;}
      if(isInRenames(mwt,divided.get(pi))){continue;}
      result.add(new PathMxMx(pi,mwt.getMs(),adapted));
    }
  }
 
  
  private static HashMap<String, String> collectConsistentRenaming(List<PathMxMx> divided,  ClassB cti) {
    HashMap<String,String> consistentNames=new HashMap<>();
    for(PathMxMx pmxi:divided){
      //for all renames, collect whose touch consistent names a->b
     addConsistentNames(pmxi,(MethodWithType)Program.getIfInDom(cti.getMs(),pmxi.getMs1()).get(),consistentNames);
      }
    return consistentNames;
  }
  private static HashMap<Path, List<PathMxMx>> divideForPath(List<PathMxMx> mapMx) {
    HashMap<Path,List<PathMxMx>> divided=new HashMap<>();
    for(PathMxMx pi:mapMx){
      if(!divided.containsKey(pi.getPath())){divided.put(pi.getPath(), new ArrayList<>());}
      divided.get(pi.getPath()).add(pi);
    }
    return divided;
  }
  private static boolean isInRenames(MethodWithType mwt, List<PathMxMx> list) {
    for(PathMxMx pmxi:list){
      if(mwt.getMs().equals(pmxi.getMs1())){return true;}
    }
    return false;
  }
  private static MethodSelector applyConsistentNames(HashMap<String, String> consistentNames, MethodWithType mwt) {
    String name=mwt.getMs().getName();
    name = computeConsistentlyRenamedName(consistentNames, mwt, name);
    List<String>names=new ArrayList<>();
    {int i=-1;for(String ni:mwt.getMs().getNames()){i+=1;
    if(isAnnotatedConsistent(mwt.getMt().getTDocs().get(i))){
      if(consistentNames.containsKey(ni)){
        ni=consistentNames.get(ni);
        assert ni!=null;
      }
    }
    names.add(ni);  
    }}
    return new MethodSelector(name,names);
  }


  private static String computeConsistentlyRenamedName(HashMap<String, String> consistentNames, MethodWithType mwt, final String name) {
    if(!isAnnotatedConsistent(mwt.getDoc())){return name;}
    boolean isHash=name.startsWith("#");
    String newName=name;
    if(isHash){newName=name.substring(1);}
    if(!consistentNames.containsKey(newName)){return name;}
    newName=consistentNames.get(newName);
    assert newName!=null;
    if(isHash){ return "#"+newName;}
    return newName;
  }
  private static void addConsistentNames(PathMxMx pmxi, MethodWithType mwt, HashMap<String, String> consistentNames) {
    assert pmxi.getMs1().equals(mwt.getMs());
    if(isAnnotatedConsistent(mwt.getDoc())){
      addConsistentName(consistentNames, pmxi.getMs1().getName(),pmxi.getMs2().getName());
    }
    {int i=-1;for(String ni:mwt.getMs().getNames()){i+=1;
      if(isAnnotatedConsistent(mwt.getMt().getTDocs().get(i))){
        addConsistentName(consistentNames, ni,pmxi.getMs2().getNames().get(i));  
      }
    }}
  }
  private static boolean isAnnotatedConsistent(Doc doc) {
    return doc.getAnnotations().contains("consistent");
  }


  private static void addConsistentName(HashMap<String, String> consistentNames, String name1, String name2) {
    if(name1.startsWith("#")){name1=name1.substring(1);}
    if(name2.startsWith("#")){name2=name2.substring(1);}
    if(!name1.equals(name2)){
      if(!consistentNames.containsKey(name1)){
        consistentNames.put(name1,name2);
        }
      else{throw Assertions.codeNotReachable("put a real exception here");}
      }
  }
}
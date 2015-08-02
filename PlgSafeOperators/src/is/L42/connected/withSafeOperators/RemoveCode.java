package is.L42.connected.withSafeOperators;
import static auxiliaryGrammar.EncodingHelper.ensureExtractClassB;
import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import coreVisitors.CloneVisitor;
import coreVisitors.From;
import platformSpecific.fakeInternet.ActionType;
import ast.ErrorMessage;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Program;
import auxiliaryGrammar.UsedPaths;
public class RemoveCode {
  public static ClassB removeUnreachableCode(ClassB originalCb){
    ClassB newCb=removeAllPrivates(originalCb);
    ClassB oldCb=newCb;
    Set<List<String>> justAdded = paths(newCb);
    do{
      oldCb=newCb;
      //for all nested in newCb
      newCb=collectDepNested(justAdded,originalCb,newCb,newCb,Collections.emptyList());
      //take all dependencies
      //add them from originalCb to newCb in newCb
    }while(!newCb.equals(oldCb));
    return newCb;
    //colect all private/nonprivate names
    //for each name, collect used paths
    //do fix point: bring public any private used by public.
    //remove all names still private
    //be careful, some names may have to be removed, but
    //their nested may need to stay
    //for getting only the public:
    //create a copy where you delete all the privates
    //then, with fixpoint:
    //for all the classes that are in newL, if Cs is used, add Cs from oldL
  }
  private static Set<List<String>>  paths(ClassB cb) {
    Set<List<String>> result=new HashSet<>();
    collectPaths(result,Collections.emptyList(),cb);
    return result;
  }
  private static void collectPaths(Set<List<String>> accumulator,List<String> path,ClassB cb) {
    accumulator.add(path);
    for(Member m:cb.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      List<String> pathi=new ArrayList<>(path);
      pathi.add(nc.getName());
      collectPaths(accumulator,pathi,(ClassB)nc.getInner());
    }
  }
  private static ClassB collectDepNested(Set<List<String>> justAdded,ClassB originalCb,ClassB accumulator,ClassB depSource,List<String> origin){
    List<List<String>> dep=collectDep(depSource,origin);
    for(List<String> pi:dep){
      if(justAdded.contains(pi)){continue;}
      justAdded.add(pi);
      accumulator=addDep(accumulator,pi,originalCb);
    }
    assert dep!=null:"to break";
    for(Member m:depSource.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      ClassB cbi=(ClassB)nc.getInner();
      List<String> newOrigin=new ArrayList<>(origin);
      newOrigin.add(nc.getName());
      accumulator=collectDepNested(justAdded,originalCb, accumulator,cbi,newOrigin);
      }
    return accumulator;
    }
  private static ClassB addDep(ClassB accumulator, List<String> path, ClassB originalCb) {
    if(path.isEmpty()){
      return mergeNestedHolderWithDep(accumulator, originalCb);
      }
    String firstName=path.get(0);
    //either fistName does not exist in accumulator, and we call removeAllButPath
    //or we have to continue recursivelly.
    Optional<Member> optM = Program.getIfInDom(accumulator.getMs(), firstName);
    NestedClass originalNc =(NestedClass) Program.getIfInDom(originalCb.getMs(), firstName).get();
    ClassB newInner;
    if(!optM.isPresent()){
      newInner=removeAllButPath(path.subList(1, path.size()),(ClassB)originalNc.getInner());
      }
    else{
      NestedClass accumulatorNc =(NestedClass)optM.get();
      newInner=addDep((ClassB)accumulatorNc.getInner(),path.subList(1, path.size()),(ClassB)originalNc.getInner());
    }
    NestedClass nc=originalNc.withInner(newInner);
    List<Member> ms = new ArrayList<>(accumulator.getMs());
    Program.replaceIfInDom(ms,nc);
    return accumulator.withMs(ms);
  }
  private static ClassB mergeNestedHolderWithDep(ClassB accumulator, ClassB originalCb) {
    assert !accumulator.isInterface();
    assert accumulator.getSupertypes().isEmpty();
    assert accumulator.getDoc1().isEmpty();
    assert accumulator.getDoc2().isEmpty();
    List<Member> ms = new ArrayList<>();
    for(Member m:accumulator.getMs()){
      assert m instanceof NestedClass:
        m;
      ms.add(m);
    }
    for(Member m:originalCb.getMs()){
      if(m instanceof NestedClass){continue;}
      ms.add(m);
      }
    return originalCb.withMs(ms);
    }
  private static ClassB removeAllButPath(List<String> path, ClassB originalCb) {
    if(path.isEmpty()){
      List<Member> ms = new ArrayList<>();
      for(Member m:originalCb.getMs()){
        if(m instanceof NestedClass){continue;}
        ms.add(m);
        }
      return originalCb.withMs(ms);
      }
    String firstName=path.get(0);
    List<Member> ms = new ArrayList<>();
    for(Member m:originalCb.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      if(!nc.getName().equals(firstName)){continue;}
      ClassB newInner=removeAllButPath(path.subList(1, path.size()),(ClassB)nc.getInner());
      ms.add(nc.withInner(newInner));
      }
    return originalCb.withMs(ms);
  }

  private static List<List<String>> collectDep(ClassB depSource, List<String> origin) {
    List<Path> dep = new UsedPaths().of(depSource);
    List<List<String>>result=new ArrayList<>();
    for(Path pi:new HashSet<>(dep)){
      Path piF=From.fromP(pi, Path.outer(0, origin));
      if(piF.outerNumber()==0){result.add(piF.getCBar());}
      }
    return result;
  }

  private static ClassB removeAllPrivates(ClassB cb) {
    return (ClassB)cb.accept(new CloneVisitor(){
      public List<Member> liftMembers(List<Member> ms1) {
        List<Member>ms2=new ArrayList<>();
        for(Member m:ms1){
          if(!(m instanceof NestedClass)){ms2.add(m);continue;}
          NestedClass ns=(NestedClass)m;
          if(!ns.getDoc().isPrivate()){ms2.add(m);continue;}
          //otherwise, not add
          }
        return super.liftMembers(ms2);
        }
      });
  }


}

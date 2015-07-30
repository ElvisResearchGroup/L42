package is.L42.connected.withSafeOperators;
import static auxiliaryGrammar.EncodingHelper.ensureExtractClassB;
import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
    do{
      oldCb=newCb;
      //for all nested in newCb
      newCb=collectDepNested(originalCb,newCb,newCb,Collections.emptyList());
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
  private static ClassB collectDepNested(ClassB originalCb,ClassB accumulator,ClassB depSource,List<String> origin){
    List<List<String>> dep=collectDep(depSource,origin);
    for(List<String> pi:dep){
      accumulator=addDep(accumulator,pi,originalCb);
    }
    assert dep!=null:"to break";
    for(Member m:depSource.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      ClassB cbi=(ClassB)nc.getInner();
      List<String> newOrigin=new ArrayList<>(origin);
      newOrigin.add(nc.getName());
      accumulator=collectDepNested(originalCb, accumulator,cbi,newOrigin);
      }
    return accumulator;
    }
  private static ClassB addDep(ClassB accumulator, List<String> pi, ClassB originalCb) {
    Doc[] commentRef=new Doc[]{Doc.empty()};
    ClassB cb = extractClassBNoNesteds(pi, originalCb,commentRef);
    Doc comment=commentRef[0];
    assert cb!=null;
    try{
      ClassB cbAcc = extractClassBNoNesteds(pi, accumulator,commentRef);
      if(cb.equals(cbAcc)){return accumulator;}
      }
    catch(ErrorMessage.PathNonExistant e){}
    ClassB innerC=cb;
    if(!pi.isEmpty()){
      Member inner=IntrospectionAdapt.encapsulateIn(pi,cb,comment);
      innerC=new ClassB(Doc.empty(),Doc.empty(),true,Collections.emptyList(),Collections.singletonList(inner),Stage.None);
      }
    return IntrospectionSum.sum(accumulator, innerC, Path.outer(0));
    }

  private static ClassB extractClassBNoNesteds(List<String> pi, ClassB originalCb,Doc[] commentRef) {
    ClassB cb=Program.extractCBar(pi, originalCb,commentRef);
    List<Member> ms2 = new ArrayList<>();
    for(Member m:cb.getMs()){
      if(m instanceof NestedClass){continue;}
      ms2.add(m);
      }
    cb=cb.withMs(ms2);
    return cb;
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

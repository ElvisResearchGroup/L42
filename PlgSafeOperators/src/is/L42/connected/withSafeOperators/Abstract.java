package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import platformSpecific.javaTranslation.Resources;
import ast.ErrorMessage;
import ast.ExpCore.*;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Program;
public class Abstract {
  public static ClassB toAbstract(ClassB cb, List<String> path){
    Errors42.checkExistsPathMethod(cb, path, Optional.empty());
    //check privacy coupled
    ClassB cbClear=ClassOperations.onClassNavigateToPathAndDo(cb, path, cbi->clear(cbi));
    Errors42.checkPrivacyCoupuled(cb,cbClear, path);
    return cbClear;
  }

  private static ClassB clear(ClassB cb) {
    //note: it would be wrong to remove implementation of private interfaces:
    //otherwise a use could rely on methods (the interface-implemented ones) that do not
    //exists any more.
    //we choose to not remove private trown exceptions just for coherence.
    //it is empty, cut it down!
    List<Member> newMs=new ArrayList<>();
    for(Member m:cb.getMs()){
      m.match(
          nc->{
            if(nc.getDoc().isPrivate()){return null;}
            newMs.add(nc.withInner(clear((ClassB)nc.getInner())));return null;
            },
          mi->{return null;},//just implementation
          mt->{
            if(mt.getDoc().isPrivate()){return null;}
            newMs.add(mt.withInner(Optional.empty()));return null;}
      );}
    //create new class
    return cb.withMs(newMs);
  }

  public static ClassB toAbstract(ClassB cb, List<String> path,MethodSelector sel){
    Errors42.checkExistsPathMethod(cb, path, Optional.of(sel));
    if(path.isEmpty()){return auxToAbstract(cb,sel);}
    return ClassOperations.onClassNavigateToPathAndDo(cb,path,cbi->auxToAbstract(cbi,sel));
  }
  private static ClassB auxToAbstract(ClassB cb,MethodSelector sel) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    Member m=Program.getIfInDom(newMs, sel).get();
    //make m abstract
    if(m instanceof MethodWithType){
      MethodWithType mwt=(MethodWithType)m;
      mwt=mwt.withInner(Optional.empty());
      Program.replaceIfInDom(newMs,mwt);
      }
    else{//it is method implemented
      Program.removeIfInDom(newMs, sel);
      }
    //create new class
    return cb.withMs(newMs);
  }

}

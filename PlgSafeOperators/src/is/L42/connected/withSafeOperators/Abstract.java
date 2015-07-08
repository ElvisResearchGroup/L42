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
    ExtractInfo.checkExistsPathMethod(cb, path, Optional.empty());
    //check privacy coupled
    ClassB cbClear=clear(cb,path);
    ExtractInfo.checkPrivacyCoupuled(cb,cbClear, path);
    return cbClear;
  }

  private static ClassB clear(ClassB cb, List<String> path) {
    //TODO:if there is an implemented private interface, remove it (need top level?)
    //public method return private type implementing public inteface?
    //public method takes private interface but was used outside providing public implementing nested?
    //what happens for exceptions?
    if(!path.isEmpty()){
      List<Member> newMs=new ArrayList<>(cb.getMs());
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, path.get(0)).get();
      nc=nc.withInner(clear((ClassB)nc.getInner(),path.subList(1,path.size())));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
    //it is empty, cut it down!
    List<Member> newMs=new ArrayList<>();
    for(Member m:cb.getMs()){
      m.match(
          nc->{ return null;},
          mi->{return null;},//ok, since there are no private interfaces implemented
          mt->{return null;}
      );}
    //create new class
    return cb.withMs(newMs);
  }

  public static ClassB toAbstract(ClassB cb, List<String> path,MethodSelector ms){
    ExtractInfo.checkExistsPathMethod(cb, path, Optional.of(ms));
    return auxToAbstract(cb,path,ms);
  }

  private static ClassB auxToAbstract(ClassB cb, List<String> path, MethodSelector sel) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    if(!path.isEmpty()){
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, path.get(0)).get();
      nc=nc.withInner(auxToAbstract((ClassB)nc.getInner(),path.subList(1,path.size()),sel));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
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

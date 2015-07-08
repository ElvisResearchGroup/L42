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
    ExtractInfo.checkPrivacyCoupuled(cb,path);
    return auxToAbstract(cb,path);
  }

  private static ClassB auxToAbstract(ClassB cb, List<String> path) {
    // TODO Auto-generated method stub
    return null;
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

package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Program;

public class LiftValue {
  static ExpCore.ClassB liftValue(ExpCore val,Ast.MethodSelector sel, ExpCore.ClassB context){
    List<Member> ms = new ArrayList<>(context.getMs());
    MethodType mt=new MethodType(Doc.empty(),Mdf.Class,
      Collections.emptyList(),Collections.emptyList(),new Ast.NormType(Mdf.Immutable,Path.Any(),Ph.None),Collections.emptyList()
      );
    //need to insert a "cast", or to return Any, and then fwd to a cast
    //lib={ T:{} class method Any val()  class method T cast() {with val=this.val() (on T return val) error ....}
    //lib:=redirect T ->... <<lib
    //lib:=liftValue  val
    Optional<Member> optMt = Program.getIfInDom(ms,sel);
    ExpCore.ClassB.MethodWithType mwt=new ExpCore.ClassB.MethodWithType(Doc.empty(),sel,mt,Optional.of(val),context.getP());
    if(!optMt.isPresent()){
      ms.add(mwt);
      return context.withMs(ms);
      }
    if(!(optMt.get() instanceof MethodWithType)){throw Errors42.errorMethodClash(Collections.emptyList(),optMt.get(),mwt,true,Collections.emptyList(),true,true,false);}
   Errors42.checkMethodClash(Collections.emptyList(),(MethodWithType) optMt.get(),mwt,false);
   Program.replaceIfInDom(ms,mwt);
   return context.withMs(ms);
    }
}

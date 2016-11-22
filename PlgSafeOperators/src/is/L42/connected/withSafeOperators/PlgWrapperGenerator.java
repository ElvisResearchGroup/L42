package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.List;

import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Expression;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.Parser;
import facade.L42.ExecutionStage;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class PlgWrapperGenerator {
  ExpCore templateWrapper=parseAndDesugar(
    " {class method\n"+
    " mut This0 #from(Library binaryRepr)\n"+
    " read method\n"+
    " Library #binaryRepr()\n"+
    " class method\n"+
    " Void #exceptionIf(Library binaryRepr) exception This\n"+
    "   use This check _%%%(binaryRepr:binaryRepr)\n"+
    "   error This.pluginUnresponsive(binaryRepr)\n}"+
    "");
  ExpCore templateUsingExc=parseAndDesugar(
    " {class method This m()\n"+
    " This.from(binaryRepr:(\n"+
    "   Library res=use This0 check m(_:This.binaryRepr())\n"+
    "     error This.from(\n"+
    "       binaryRepr:{/**/}\n"+
    "   catch error Library x (\n"+
    "     This.exceptionIf(binaryRepr:x)\n"+
    "     error x\n"+
    "     )\n"+
    "   res\n"+
    "   ))\n"+
    "}\n"+
    "");
  
  ExpCore templateUsing=parseAndDesugar(
  " {class method This m()\n"+
  " This.from(binaryRepr:(\n"+
  "   Library res=use This0 check m()\n"+
  "     error This.from(\n"+
  "       binaryRepr:{/**/}\n"+
  "   res\n"+
  "   ))\n"+
  " }\n"+
  "");
       
private ExpCore parseAndDesugar(String s) {
  Expression code1=Parser.parse("PlgWrapperGenerator",s);
  Expression code2=Desugar.of(code1);
  return code2.accept(new InjectionOnCore());
  }


//-----------------------------------------------------

  public ClassB plgComplete(List<String>cs,Program p,ClassB l){
    List<Member> ms=new ArrayList<>();
    for(Member m: l.getMs()){
      if(!(m instanceof NestedClass)){
        ms.add(m);
        continue;
        }
      NestedClass nc=(NestedClass)m;
      List<String>csc=new ArrayList<>(cs);
      csc.add(nc.getName());
      ms.add(nc.withInner(
        plgComplete(csc,p,(ClassB)nc.getInner()))
        );
      }
    return plgComplete1(cs,p,l.withMs(ms));
    //forall nested c in l
    // l=l[with c=plgComplete(cs:c, p,l.c)
    //return plgComplete1(cs,p,l)
    }
  public ClassB plgComplete1(List<String>cs,Program p,ClassB l){
    return null;//TODO:
    }
  


}

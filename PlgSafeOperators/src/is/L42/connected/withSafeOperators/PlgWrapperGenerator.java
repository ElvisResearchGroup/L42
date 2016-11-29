package is.L42.connected.withSafeOperators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Expression;
import auxiliaryGrammar.Program;
import facade.L42;
import facade.Parser;
import facade.L42.ExecutionStage;
import platformSpecific.fakeInternet.OnLineCode;
import platformSpecific.fakeInternet.PluginWithPart;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import tools.Assertions;

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
  public ClassB plgComplete(Program p){
    return plgComplete(Collections.emptyList(),p,p.topCb());
    }
  public ClassB plgComplete(List<String>cs,Program p,ClassB l){
    //p.top() is topL
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
  public static boolean hasPluginUnresponsive(Program p,ClassB l){
    Member m=l._getMember(new MethodSelector("#pluginUnresponsive",Collections.singletonList("binaryRepr")));
    if(m==null){return false;}
    MethodWithType mwt=(MethodWithType)m;//since normalized
    MethodType mt=mwt.getMt();
    if(!mt.getMdf().equals(Mdf.Class)){return false;}
    if(!mt.getReturnType().getNT().getMdf().equals(Mdf.Immutable)){return false;}
    if(!mt.getTs().get(0).equals(NormType.immLibrary)){return false;}    
    return true;//not care what is the result, since is just thrown as error
    }
  public ClassB plgComplete1(List<String>cs,Program p,ClassB l){
    PluginWithPart pwp = OnLineCode._isPluginWithPart(l.getDoc1());
    if(pwp==null){return l;}
    if(!hasPluginUnresponsive(p, l)){throw Assertions.codeNotReachable();}//TODO:
    Map<String,Entry<Method,Ast.MethodSelector>> ms;
    return null;//TODO:
    /*plgComplete1(lTop,dept,p,l){
    
    c=locateClass(l)
    Map<String,List<Method,mh42>> ms=..
    List<Constructor,mh42> cs=...
    m42s=new List
    forall e,mh in ms,cs:
      ui=UsingInfo(e)
      tu=templateUsing
      tu.updateAllStuff(ui,mh)
      m42s.add(tu)
      foral T in mh,
        check either lTop(T) is pluginWithPart
        or p(T/dept) has right method
        //may be faster to cache?
      }
    return templateWrapper U m42s*/
    }
  
   
  


}

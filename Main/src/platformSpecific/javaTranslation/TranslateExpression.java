package platformSpecific.javaTranslation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import platformSpecific.fakeInternet.PluginType;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

/*class A{ A m(A a){return a;}
  A mm(){
    A a=new A();
    a.m(B.block(()->{A a1=new A(); return a1.m(a1);}));
    return B.block(()->{throw Assertions.codeNotReachable();});
  }
}
class B{public static <T> T block(java.util.function.Supplier<T> p){return p.get();}}
*/
public class TranslateExpression implements coreVisitors.Visitor<Void>{
  public static void of(ExpCore expCore,StringBuilder res) {
    TranslateExpression tr=new TranslateExpression(res);
    if(expCore instanceof Block){
      tr.produceNestedBlock((Block)expCore," return ");
      return;}
    res.append("{return ");
    expCore.accept(tr);
    res.append(";}");
  }
  private static Set<String> labels=new HashSet<String>();
  StringBuilder res;
  Set<String> undeclared=new HashSet<String>();
  TranslateExpression(StringBuilder res){this.res=res;}

  @Override
  public Void visit(Path s) {
    if(s.isPrimitive()){
      if(s.equals(Path.Any())){res.append("platformSpecific.javaTranslation.Resources.Any.type");}
      if(s.equals(Path.Library())){res.append("platformSpecific.javaTranslation.Resources.Library.type");}
      if(s.equals(Path.Void())){res.append("platformSpecific.javaTranslation.Resources.Void.type");}
      return null;
    }
    res.append(Translator.nameOf(s)+".type");
    return null;
  }

  @Override
  public Void visit(X s) {
    if(s.getInner().equals("this")){res.append("this");return null;}
    if(undeclared.contains(s.getInner())){res.append("PH"+s.getInner());}
    else{res.append("P"+s.getInner());}
    return null;
  }

  @Override
  public Void visit(_void s) {
    res.append("platformSpecific.javaTranslation.Resources.Void.instance");
    return null;
  }

  @Override
  public Void visit(WalkBy s) {
    throw Assertions.codeNotReachable();
  }

  @Override
  public Void visit(Using s) {
    PluginType pt=platformSpecific.fakeInternet.OnLineCode.plugin(Resources.getP(),s);
    String plgName=pt.getClass().getName();
    String plF="L"+Functions.freshName("pl",TranslateExpression.labels);
    String xsF="L"+Functions.freshName("xs",TranslateExpression.labels);
    //String k=Resources.submitRes(pt);
    res.append("platformSpecific.javaTranslation.Resources.plgExecutor(");
    res.append("\""+s.getS().getName()+"\",");
    res.append("platformSpecific.javaTranslation.Resources.getP(), ");
    res.append("new "+plgName+"(), ");
    res.append("("+plF+","+xsF+")->"+plF+".");
    //MsumInt32£xn1£xn2(xsF[0],xsF[1]),
    res.append(Translator.nameOf(s.getS().getName(),s.getS().getNames()));
    res.append("(");
    StringBuilders.formatSequence(res,
        IntStream.range(0, s.getEs().size()).iterator(),
        ", ",
        i->res.append(xsF+"["+i+"]"));
    //conclE,e1,e2);
    res.append("), ()->(");
    s.getInner().accept(this);
    res.append(")");
    for(ExpCore ei:s.getEs()){
      res.append(", ");
      ei.accept(this);
      }
    res.append(")");
    //res.append("block(()->{\ntry{\nreturn ");
    //res.append("(("+pt.getClass().getName()+")platformSpecific.javaTranslation.Resources.getRes(\""+k+"\")).");
    //res.append(Translator.nameOf(s.getName(),s.getXs()));
    //res.append("(");
    //StringBuilders.formatSequence(res,s.getEs().iterator(),
    //  ", ", ei->ei.accept(this));
    //res.append(");\n}\ncatch(ast.ErrorMessage.PluginActionUndefined Unused"+k);
    //res.append("){\nreturn ");
    //s.getInner().accept(this);
    //res.append(";\n}\n})");
    return null;
    }

  @Override
  public Void visit(Signal s) {
    res.append("platformSpecific.javaTranslation.Resources.block(()->{");
    produceThrow(s);
    res.append("})");
    return null;
  }
  public Void produceThrow(Signal s){
    res.append("throw new platformSpecific.javaTranslation.Resources.");
    res.append(s.getKind().name());
    res.append("(");
    s.getInner().accept(this);
    res.append(");\n");
    return null;
  }
  
  @Override
  public Void visit(MCall s) {
    res.append("(");
    s.getReceiver().accept(this);
    res.append(")."+Translator.nameOf(s.getS().getName(),s.getS().getNames())+"(");
    StringBuilders.formatSequence(res,s.getEs().iterator(),
      ", ", ei->ei.accept(this));
    res.append(")");
    return null;
  }

  @Override
  public Void visit(Block s) {
    res.append("platformSpecific.javaTranslation.Resources.block(()->");
    produceNestedBlock(s,"return ");
    res.append(")");
    return null;
  }
  public void produceNestedBlock(Block s,String asReturn){
    String kVar=" L"+Functions.freshName("lab",TranslateExpression.labels);
    res.append("{"+kVar+":{");
    Set<String> domPhs=declareVarsAndPh(s.getDecs());
    if(s.get_catch().isPresent()){
      res.append("try{\n");
      initializeVars(domPhs,s.getDecs(),kVar);  
      res.append("\n}");
      getCatches(s.get_catch().get(),asReturn,kVar);
    }
    else{initializeVars(domPhs,s.getDecs(),kVar);}
    //if(s.getInner()instanceof Block){
    //  produceNestedBlock((Block)s.getInner(),asReturn);
    //}else{//This change the semantic... :?
    if(s.getInner()instanceof Signal){
      if(asReturn.contains("="))res.append("if(true)");
      produceThrow((Signal)s.getInner());
    }else{
      res.append(asReturn);
      s.getInner().accept(this);   
      res.append(";");
      }
    res.append("}}");
  }

  private void getCatches(Catch c,String asReturn,String kVar) {
    assert !c.getOns().isEmpty();
    res.append("catch(platformSpecific.javaTranslation.Resources."+c.getKind().name()+" K"+c.getX());
    res.append("){\n");
    for(On on:c.getOns()){getCatch(c,on,asReturn,kVar);}
    res.append("{}/*ensure termination*/throw K"+c.getX());
    res.append(";\n}\n");
  }

  private void getCatch(Catch c, On on,String asReturn,String kVar) {
    String tn=Translator.nameOf(on.getT());
    res.append("if(K"+c.getX()+".unbox instanceof "+tn+"){\n");
    res.append("  "+tn+" P"+c.getX()+"=("+tn+")K"+c.getX()+".unbox;\n");
    res.append(asReturn);
    on.getInner().accept(this);
    res.append(";");
    if(asReturn.contains("=")){ res.append("break "+kVar+";");  }
    res.append("\n  }\nelse ");
  }

  
  private void initializeVars(Set<String> domPhs, List<Dec> decs,String kVar) {
    //insertComment(domPhs);
    this.undeclared.addAll(domPhs);
    for(Dec d:decs){//declare all vars;
      /*if(*/initializeSingleVar(d);/*){return true;}*/
      undeclared.remove(d.getX());
      if(domPhs.contains(d.getX())){
        res.append("PH"+d.getX()+".commit(P"+d.getX()+");\n");
        }
      }
    //return false;
    }

  public void initializeSingleVar(Dec d) {
    if(d.getE() instanceof ExpCore.Signal){
      res.append("if(true)");
      produceThrow((ExpCore.Signal)d.getE());
      return;
      }
    if(d.getE() instanceof ExpCore.Block && d.getNT().getPath().equals(Path.Void())){
      //speeds up compilation time
      /*boolean isThrow=*/produceNestedBlock((Block)d.getE(),"P"+d.getX()+"=");
      res.append("\n");
      //if(isThrow){return true;}
      }
    else{
      res.append("P"+d.getX()+"=");
      d.getE().accept(this);
      res.append(";\n");
      }
  }

  public void insertComment(Object domPhs) {
    res.append("//");
    res.append(domPhs.toString());
    res.append("\n");
  }

  public Set<String> declareVarsAndPh(List<Dec> decs) {
    Set<String> unDeclared=new HashSet<>();
    for(Dec d:decs){//declare all vars;
      res.append(Translator.nameOf(d.getT()));
      res.append(" P"+d.getX()+";\n");
      unDeclared.add(d.getX());
    }
    Set<String> domPhs=new HashSet<>();
    for(Dec d:decs){
      Set<String> fvei = coreVisitors.FreeVariables.of(d.getE());
      fvei.retainAll(unDeclared);
      if(!fvei.isEmpty()){domPhs.addAll(fvei);}
      unDeclared.remove(d.getX());
    }
    assert unDeclared.isEmpty();
    for(Dec d:decs){
      if(domPhs.contains(d.getX())){//declare placeholder
        res.append(Translator.nameOf(d.getT())+".Ph");
        res.append(" PH"+d.getX()+"=new "+Translator.nameOf(d.getT())+".Ph();\n");
        }
      }
    return domPhs;
  }

  @Override
  public Void visit(ClassB s) {
    String k=Resources.submitRes(s);
    res.append("platformSpecific.javaTranslation.Resources.getRes(\""+k+"\")");
    return null;
  }

  @Override
  public Void visit(Loop s) {
    res.append("platformSpecific.javaTranslation.Resources.block(()->{while(true){\n");
    res.append("platformSpecific.javaTranslation.Resources.unused=");
    s.getInner().accept(this);
    res.append(";\n}})\n");
    return null;
  }
  
}

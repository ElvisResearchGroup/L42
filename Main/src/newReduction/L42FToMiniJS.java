package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.SignalKind;
import ast.Ast;
import ast.L42F;
import ast.L42F.Block;
import ast.L42F.Body;
import ast.L42F.BreakLoop;
import ast.L42F.Call;
import ast.L42F.Cast;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.If;
import ast.L42F.K;
import ast.L42F.Kind;
import ast.L42F.Loop;
import ast.L42F.Null;
import ast.L42F.SimpleKind;
import ast.L42F.Throw;
import ast.L42F.Unreachable;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;
import ast.MiniJ;
import ast.MiniJ.*;
import auxiliaryGrammar.Functions;
import facade.L42;
import l42FVisitors.JVisitor;
import l42FVisitors.ToFormattedText;
import l42FVisitors.Visitor;
import tools.Assertions;
import tools.StringBuilders;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import platformSpecific.javaTranslation.Resources;

public class L42FToMiniJS extends ToFormattedText implements Visitor<Void>{
  public L42FToMiniJS(ClassTable ct){
    assert ct!=null;
    this.ct = ct;
    }
  ClassTable ct;
  String x0=null;
  String label=null;
  boolean emptyC(){return x0==null && label==null;}
  boolean wrapE(){//return true if we need semicolon after
    nl();
    if(emptyC()){c("return ");return true;}
    if(x0!=null){c(x0+"="); return true;}
    return false;
    }
  public static StringBuilder forBody(ClassTable ct, L42F.E body){
    body=body.accept(new OptimizeBlock());//TODO: not here?
    var res=new L42FToMiniJS(ct);
    body.accept(res);
    if(res.result.substring(0, 1).equals("{")) {return res.result;}
    res.result.insert(0, "{ ");
    res.result.append("}");
    return res.result;
    }
  Void liftWith(String x0,String label,L42F.E inner){
    String oldX0=this.x0;
    String oldLabel=this.label;
    this.x0=x0;
    this.label=label;
    try{ inner.accept(this);}
    finally{this.x0=oldX0;this.label=oldLabel;}
    return null;
  }

  @Override
  public Void visit(BreakLoop s) {
    if(label==null){throw Assertions.codeNotReachable();}
    return c("break "+label+";");
    }

  @Override
  public Void visit(X s) {
    boolean sc=wrapE();
    c(L42FToJavaString.liftX(s.getInner()));
    return semiColon(sc);
    }

  @Override
  public Void visit(Cn s) {
    boolean classAny = isClassAny(s);
    boolean sc=wrapE();
    if(classAny){
      c("generated.Resource.£COf("+s.getInner()+")");
      return semiColon(sc);
      }
    String name=ct.l42ClassName(s.getInner());
    c(name+".Instance()");
    return semiColon(sc);
    }
  private boolean isClassAny(Cn s) {
    if(L42F.Cn.cnAny.equals(s)){return true;}
    if(L42F.Cn.cnFwd.getInner()>=s.getInner()){return false;}
    ast.L42F.CD cd=ct.get(s.getInner()).cd;
    Kind k = cd.getKind();
    boolean classAny=false;
    if(k==null || k==SimpleKind.Interface){classAny=true;}
    return classAny;
    }

  @Override
  public Void visit(_void s) {
    String name=Resources.Void.class.getCanonicalName();
    boolean sc=wrapE();
    c(name+".Instance()");
    return semiColon(sc);
    }

  @Override
  public Void visit(Null s) {
    boolean sc=wrapE();
    c("null");
    return semiColon(sc);
    }

  @Override
  public Void visit(Call s) {
    boolean sc=wrapE();
    c(ct.l42ClassName(s.getCn()));
    c(".");
    String name="£C"+L42FToJavaString.liftMs(s.getMs());
    if(name.startsWith("£CLoadLib_")) {
      int cn=Integer.parseInt(name.substring("£CLoadLib_".length()));
      c("£CLoadLib("+cn+")");
      return semiColon(sc);
    }
    /*if(name.startsWith("£COf_")) {
      int cn=Integer.parseInt(name.substring("£COf_".length()));
      c(s.getCn()+".£COf("+cn+")");
      return semiColon(sc);
    }*/
    c(name);
    c("(");
    StringBuilders.formatSequence(result,s.getXs().iterator(),
        ", ",x->c(L42FToJavaString.liftX(x)));
    c(")");
    return semiColon(sc);
    }

@Override
  public Void visit(Use s) {
    boolean sc=wrapE();
    UsingInfo ui=s.getUi();
    List<String> es=tools.Map.of(L42FToJavaString::liftX, s.getXs());
    StringBuilder resOld=result;
    result=new StringBuilder();
    String e;
    try{c("{");
    liftWith(null,null,s.getInner());
    c("}");
    e=result.toString();
    }finally{result=resOld;}
    PluginType pt=platformSpecific.fakeInternet.OnLineCode.plugin(s.getDoc());
    c(pt.executableJ(ui, e, es, L42.usedNames));
    return semiColon(sc);
    }
  @Override
  public Void visit(Throw s) {
    if(s.getKind()!=null){
      return c("throw new "+tOf(s.getKind())+"("+L42FToJavaString.liftX(s.getX())+");");
      }
    return c("throw "+s.getX()+";");
    }

  @Override
  public Void visit(Loop s) {
    String label0=Functions.freshName("label",L42.usedNames);
    c(label0+":while(true)");
    return liftWith(null,label0,s.getInner());
    }
  @Override
  public Void visit(Cast s) {
    String cn = ct.className(s.getT().getCn());
    boolean sc=wrapE();
    c("("+cn+")"+L42FToJavaString.liftX(s.getX()));
    return semiColon(sc);
    }
  @Override
  public Void visit(Update s) {
    c(L42FToJavaString.liftX(s.getX1())+"="+L42FToJavaString.liftX(s.getX2()));
    return c(";");    
    }
  @Override
  public Void visit(If s) {
    c("if("+L42FToJavaString.liftX(s.getCondition())+")");
    s.getThen().accept(this);
    c(" else ");
    return s.get_else().accept(this);
    }

  @Override
  public Void visit(Block s) {//
    String label0=null;
    if(!s.getKs().isEmpty()){label0=Functions.freshName("label",L42.usedNames);}
    if(label0!=null){c("{"+label0+":");}
    c("{");
    //for(S si:s.getSs()){si.accept(this);}
    //handle ds
    for(D di:s.getDs()){liftDsTX(di);}
    //handle ks
    if(s.getKs().isEmpty()){
      for(D di:s.getDs()){liftDsXE(di);}
      }
    else{
      c("try{");
      for(D di:s.getDs()){liftDsXE(di);}
      c("}");
      liftKs(label0,s.getKs());
      }
    //handle e
    if(!(s.getE() instanceof Unreachable)){
      s.getE().accept(this);
      if(!emptyC() && x0==null && result.charAt(result.length()-1)!=';' && result.charAt(result.length()-1)!='}'){c(";");}//would not get it from wrapE()
      }
    if(label0!=null){c("}");}
    return c("}");
    }
  private String tOf(Ast.SignalKind kind){
    switch(kind){
      case Return: return Resources.Return.class.getCanonicalName();
      case Exception: return Resources.Exception.class.getCanonicalName();
      case Error: return Resources.Error.class.getCanonicalName();
      }
    throw Assertions.codeNotReachable();
    }
  private Void semiColon(boolean insert) {
    if(insert) {c(";");}
    return null;
    }
  private Void liftKs(String label0,List<K> ks) {
    List<K> errs=new ArrayList<>();
    List<K> excs=new ArrayList<>();
    List<K> rets=new ArrayList<>();
    for(K k:ks) {
      if (k.getKind()==SignalKind.Error) {errs.add(k);}
      if (k.getKind()==SignalKind.Exception) {excs.add(k);}
      if (k.getKind()==SignalKind.Return) {rets.add(k);}
      }
    liftKs(label0,SignalKind.Error,errs);
    liftKs(label0,SignalKind.Exception,excs);
    liftKs(label0,SignalKind.Return,rets);
    return null;
    }
  private Void liftKs(String label0,SignalKind kind,List<K> ks) {
    if(ks.isEmpty()){return null;}
    String catchX=Functions.freshName("catchX",L42.usedNames);
    c("catch ("+tOf(kind)+" "+catchX+"){");
    boolean isTerminating=true;
    for(K ki:ks){
      isTerminating=isTerminating&& ki.getE().accept(new Terminating());
      String cn = ct.boxedClassName(ki.getT().getCn());
      boolean positive=Cn.cnLibrary.getInner()!=ki.getT().getCn();
      if(!positive) {
        cn=NotLibrary.class.getCanonicalName();
      }
      String xi=catchX+".inner()";
      String test=xi+" instanceof "+cn;
      if (!positive) { test="!("+test+")";}
      c("if("+test+"){");
      if (positive) {
        c(cn+" "+L42FToJavaString.liftX(ki.getX())+"=("+cn+")"+xi+"; ");
        }
      else {c("Object "+L42FToJavaString.liftX(ki.getX())+"="+xi+"; ");}
      ki.getE().accept(this);
      if(!emptyC() && x0==null && result.charAt(result.length()-1)!=';' && result.charAt(result.length()-1)!='}'){c(";");}//would not get it from wrapE()
      c("} else ");
      }
    c("throw "+catchX+";");
    if(!emptyC() &&!isTerminating){
      c("break "+label0+";");
      }
    return c("}");
    }
  private Void liftDsXE(D di) {
    nl();
    liftWith(L42FToJavaString.liftX(di.getX()), label, di.getE());
    return null;
  }

  private Void liftDsTX(D di) {
    nl();
    c(ct.className(di.getT().getCn()));
    sp();
    c(L42FToJavaString.liftX(di.getX()));
    return c(";");
  }
@Override
public Void visit(Unreachable s) { throw Assertions.codeNotReachable();}

}

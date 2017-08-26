package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.SignalKind;
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
import ast.L42F.Loop;
import ast.L42F.Null;
import ast.L42F.Throw;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;
import ast.MiniJ;
import ast.MiniJ.*;
import auxiliaryGrammar.Functions;
import facade.L42;
import l42FVisitors.JVisitor;
import l42FVisitors.Visitor;
import tools.Assertions;

import platformSpecific.javaTranslation.Resources;

public class L42FToMiniJS implements Visitor<MiniJ.S>{
  public L42FToMiniJS(ClassTable ct){this.ct = ct;}
  ClassTable ct;
  String x0=null;
  String label=null;
  boolean emptyC(){return x0==null && label==null;}
  MiniJ.S wrapE(MiniJ.E e){
    if(emptyC()){return new MiniJ.Return(e);}
    if(x0!=null){return new MiniJ.VarAss(x0, e);}
    return e;
    }
  MiniJ.S liftWith(String x0,String label,L42F.E inner){
    String oldX0=this.x0;
    String oldLabel=this.label;
    this.x0=x0;
    this.label=label;
    try{return inner.accept(this);}
    finally{this.x0=oldX0;this.label=oldLabel;}
  }
  
  @Override
  public MiniJ.S visit(BreakLoop s) {
    if(label==null){throw Assertions.codeNotReachable();}
    return new MiniJ.Break(label);
    }
  
  @Override
  public MiniJ.S visit(X s) {
    return wrapE(new MiniJ.X(s.getInner()));
    }

  @Override
  public MiniJ.S visit(Cn s) {
    String name=ct.l42ClassName(s.getInner());
    E e=new MiniJ.MCall(name, "Instance", Collections.emptyList());
    return wrapE(e);
    }

  @Override
  public MiniJ.S visit(_void s) {
    String name=Resources.Void.class.getName();
    E e=new MiniJ.MCall(name, "Instance", Collections.emptyList());
    return wrapE(e);
    }

  @Override
  public MiniJ.S visit(Null s) {
    MiniJ.E e=new MiniJ.Null();
    return wrapE(e);
    }
  
  @Override
  public MiniJ.S visit(Call s) {
    E e=new MiniJ.MCall(ct.l42ClassName(s.getCn()),L42FToMiniJ.liftMs(s.getMs()),s.getXs());
    return wrapE(e);
    }

@Override
  public MiniJ.S visit(Use s) {
    S inner=liftWith(null,null,s.getInner());
    E e=new MiniJ.UseCall(ct.l42ClassName(s.getCn()),
            L42FToMiniJ.liftMs(s.getMs()),
            s.getXs(),inner);
    return wrapE(e);
    }
  @Override
  public MiniJ.S visit(Throw s) {
    String kindJName="platformSpecific.javaTranslation.Resources."+s.getKind().name();
    return new MiniJ.Throw(kindJName, s.getX());
    }

  @Override
  public MiniJ.S visit(Loop s) {
    String label0=Functions.freshName("label",L42.usedNames);
    S inner=liftWith(null,label0,s.getInner());
    return new MiniJ.WhileTrue(label0,inner);
    }
  @Override
  public MiniJ.S visit(Cast s) {
    String cn = ct.className(s.getT().getCn());
    return wrapE(new MiniJ.Cast(cn,s.getX()));
    }
  @Override
  public MiniJ.S visit(Update s) {
    return new MiniJ.VarAss(s.getX1(), new MiniJ.X(s.getX2()));
    }
  @Override
  public MiniJ.S visit(If s) {
    S then=s.getThen().accept(this);
    S _else=s.get_else().accept(this);
    return new MiniJ.If(s.getCondition(), then, _else);
  }

  @Override
  public MiniJ.S visit(Block s) {
    List<S>ds=new ArrayList<>();
    for(D di:s.getDs()){ds.add(liftDsTX(di));}
    if(s.getKs().isEmpty()){
      for(D di:s.getDs()){ds.add(liftDsXE(di));}
      }
    else{
      String label0=Functions.freshName("label",L42.usedNames); 
      List<S>dsTry=new ArrayList<>();
      if(s.getKs().isEmpty()){
        for(D di:s.getDs()){dsTry.add(liftDsXE(di));}
        }
      List<MiniJ.K> ks=liftKs(label0,s.getKs());
      Try t=new Try(new B(null,dsTry),ks);
      ds.add(t);
      }
    ds.add(s.getE().accept(this));
    return new B(label,ds);
    }
  private List<MiniJ.K> liftKs(String label0,List<K> ks) {
    List<K> errs=new ArrayList<>();
    List<K> excs=new ArrayList<>();
    List<K> rets=new ArrayList<>();
    for(K k:ks) {
      if (k.getKind()==SignalKind.Error) {errs.add(k);}
      if (k.getKind()==SignalKind.Exception) {excs.add(k);}
      if (k.getKind()==SignalKind.Return) {rets.add(k);}
      }
    List<MiniJ.K> tot=new ArrayList<>();
    liftKs(label0,SignalKind.Error,errs,tot);
    liftKs(label0,SignalKind.Exception,excs,tot);
    liftKs(label0,SignalKind.Return,rets,tot);
    return tot;

    }
  private void liftKs(String label0,SignalKind kind,List<K> ks,List<MiniJ.K> acc) {
    if(ks.isEmpty()){return;} 
    String catchX=Functions.freshName("catchX",L42.usedNames);
    S s=new MiniJ.Throw(null, catchX);
    Collections.reverse(ks);
    for(K ki:ks){
      String cn = ct.boxedClassName(ki.getT().getCn());
      S then = ki.getE().accept(this);
      s=new MiniJ.IfTypeCase(catchX, ki.getX(), cn, then,s);
      }
    List<S>ss=new ArrayList<>();
    ss.add(s);
    if(!emptyC()){
      ss.add(new MiniJ.Break(label0));
      }
    B b=new B(null,ss);
    MiniJ.K res=new MiniJ.K(kind, catchX, b);
    acc.add(res);    
    }

  private S liftDsXE(D di) {
    return liftWith(di.getX(), label, di.getE());
  }

  private S liftDsTX(D di) {
    return new MiniJ.VarDec(ct.className(di.getT().getCn()),di.getX());
  }
}

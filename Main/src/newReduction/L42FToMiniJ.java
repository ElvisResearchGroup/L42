package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.MethodSelector;
import ast.L42F;
import ast.MiniJ;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.M;
import ast.L42F.SimpleBody;
import ast.MiniJ.S;
import ast.MiniJ.RawJ;
import l42FVisitors.BodyVisitor;

import platformSpecific.javaTranslation.Resources;
import tools.Assertions;

public class L42FToMiniJ {
  public static MiniJ.CD of(ClassTable ct,L42F.CD cd){
    String name=cd.l42ClassName();
    boolean interf=cd.getKind()==L42F.SimpleKind.Interface;
    List<String> cs=tools.Map.of(i->ct.get(i).cd.l42ClassName(),cd.getCns());
    List<MiniJ.M>ms=new ArrayList<>();
    for(L42F.M m:cd.getMs()){
    String retT=ct.className(m.getReturnType().getCn());
    List<String> ts=tools.Map.of(tx->ct.className(tx.getT().getCn()),m.getTxs());
    List<String> xs=tools.Map.of(tx->tx.getX(),m.getTxs());
    MiniJ.M res=new MiniJ.M(retT, liftMs(m.getSelector()), ts, xs,null);
    MiniJ.S body=m.getBody().accept(new VB(ct,name,cd,m,res));
    ms.add(res.withBody(body));
    }
  return new MiniJ.CD(interf, name, cs, ms);
  }

  private static class VB implements BodyVisitor<MiniJ.S>{
    public VB(ClassTable ct,String cn,CD cd, M m, MiniJ.M mj) {
      this.ct=ct;
      this.cn=cn;
      this.cd = cd;
      this.m = m;
      this.mj=mj;
      }
    ClassTable ct;
    String cn;
    L42F.CD cd;
    L42F.M m;
    MiniJ.M mj;
  
    @Override
    public S visitEmpty(SimpleBody s) {
      return new RawJ(";");
      }

    @Override
    public S visitSetter(SimpleBody s) {
      String x=mj.getName();
      String t=mj.getRetT();
      String t1=mj.getTs().get(1);
      String x1=mj.getXs().get(1);
      StringBuilder sb=new StringBuilder();
      sb.append("{£this.£"+x+"=that; return "+Resources.Void.class.getName()+".instance();}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£"+x+"("+t1+" "+x1+"){return "+cn+"."+x+"(this,that);}");
        }
      return new RawJ(sb.toString());
      }

    @Override
    public S visitGetter(SimpleBody s) {
      String x=mj.getName();
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{return £this.£"+x+";}");
      sb.append(t+" "+x+";");
      sb.append("public static java.util.function.BiConsumer<Object,Object> FieldAssFor£"+
        x+"=(f,o)->{(("+cn+")o).£"+x+"=("+t+")f;}"
        );
      if(this.m.isRefine()){
        sb.append("public "+t+ "£"+x+"(){return this."+x+"();}");
        }
      return new RawJ(sb.toString());
      }

    @Override
    public S visitNew(SimpleBody s) {
      return factory(false);
      }
    @Override
    public S visitNewWithFwd(SimpleBody s) {
      return factory(true);
      }
    public S factory(boolean fwd){
      String x=mj.getName();
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{"+cn+" res=new "+cn+"();");
      for(String xi:mj.getXs()){
        sb.append("res."+xi+"="+xi+";");
        if(fwd){sb.append("Fwd.addIfFwd("+xi+","+cn+".FieldAssFor_"+xi+");");}        
        }
      sb.append("return res;}");
      return new RawJ(sb.toString());
      }

    @Override
    public S visitNewFwd(SimpleBody s) {
      StringBuilder sb=new StringBuilder();
      sb.append("{return new _Fwd();}\n");
      sb.append("private static class _Fwd extends "+cn+"CN implements Fwd{\n");
      sb.append("private java.util.List<Object> os=new java.util.ArrayList<>();\n");
      sb.append("private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n");
      sb.append("public java.util.List<Object> os(){return os;}\n");
      sb.append("public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}");
      return new RawJ(sb.toString());
      }

    @Override
    public S visitNativeIntSum(SimpleBody s) {
      throw Assertions.codeNotReachable();
      }

    @Override
    public S visitE(E s) {
      S res=s.accept(new L42FToMiniJS(ct));
      if(res instanceof MiniJ.B){return res;}
      if(res instanceof MiniJ.RawJ){return res;}
      MiniJ.B b=new MiniJ.B(null,Collections.singletonList(res));
      return b;
      }
    }

  public static String liftMs(MethodSelector ms) {
    String res=ms.nameToS();
    for(String xi:ms.getNames()){res+="£X"+xi;}
    return res;
    }
  }

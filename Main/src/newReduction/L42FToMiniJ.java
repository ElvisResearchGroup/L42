package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ast.Ast.MethodSelector;
import ast.L42F;
import ast.MiniJ;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.Kind;
import ast.L42F.M;
import ast.L42F.SimpleBody;
import ast.L42F.SimpleKind;
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
      MiniJ.M res = methodHeader(ct, m);
      MiniJ.S body=m.getBody().accept(new VB(ct,name,cd,m,res));
      ms.add(res.withBody(body));
      if(!(m.getBody() instanceof E)){continue;}
      if(!m.isRefine()){continue;}
      //remove static,add£M to name, remove £Xthis, rembemberThis
      int pSize=m.getTxs().size();
      boolean haveThis=pSize!=0 && m.getTxs().get(0).getX().equals("this");
      MiniJ.M delegator=res.withStatic(false).withName("£M"+res.getName());
      if(haveThis){
        delegator=delegator
          .withTs(res.getTs().subList(1,pSize))
          .withXs(res.getXs().subList(1,pSize));
        }
      delegator=delegator.withBody(new MiniJ.B(null,Collections.singletonList(new MiniJ.Return(
        new MiniJ.MCall(name, res.getName(),tools.Map.of(tx->tx.getX(),m.getTxs())))
        )));
      ms.add(delegator);
      }
    return new MiniJ.CD(interf, name, cs, ms);
    }
  private static MiniJ.M methodHeader(ClassTable ct, L42F.M m) {
    assert m.getBody()!= SimpleBody.Empty ||m.getTxs().get(0).getX().equals("this");
    String retT=ct.className(m.getReturnType().getCn());
    List<String> ts=tools.Map.of(tx->ct.className(tx.getT().getCn()),m.getTxs());
    List<String> xs=tools.Map.of(tx->liftX(tx.getX()),m.getTxs());
    MiniJ.M res=new MiniJ.M(true,retT, liftMs(m.getSelector()), ts, xs,null);
    return res;
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
      StringBuilder r=new StringBuilder();
      r.append("{return £Xthis.£M"+mj.getName()+"(");
      Iterator<String> it = mj.getXs().iterator();
      it.next();
      tools.StringBuilders.formatSequence(r,it,", ",x->r.append(x));
      r.append(");}\n");
      r.append("default "+mj.getRetT()+" £M"+mj.getName()+"(");
      Iterator<String> itt=mj.getTs().iterator();
      Iterator<String> itx=mj.getXs().iterator();
      itt.next();
      itx.next();
      tools.StringBuilders.formatSequence(r,itt,itx,", ",(t,x)->r.append(t+" "+x));
      r.append("){throw new Error(\"Interface method invocation\");}");
      return new RawJ(r.toString());
      }

    @Override
    public S visitSetter(SimpleBody s) {
      String x=mj.getName();
      String f=x;if(f.startsWith("£H")){f=f.substring(2);}
      String t=mj.getRetT();
      String t1=mj.getTs().get(1);
      String x1=mj.getXs().get(1);
      StringBuilder sb=new StringBuilder();
      sb.append("{£Xthis.£X"+f+"=that; return "+Resources.Void.class.getCanonicalName()+".instance();}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x+"("+t1+" "+x1+"){return "+cn+"."+x+"(this,that);}");
        }
      return new RawJ(sb.toString());
      }

    @Override
    public S visitGetter(SimpleBody s) {
      String x=mj.getName();
      String f=x;if(f.startsWith("£H")){f=f.substring(2);}
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{return £Xthis.£X"+f+";}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x+"(){return "+cn+"."+x+"(this);}");
        }
      return new RawJ(sb.toString());
      }

    @Override
    public S visitNew(SimpleBody s) {
      return new RawJ(factory(false).toString());
      }
    @Override
    public S visitNewWithFwd(SimpleBody s) {
      StringBuilder sb=factory(true);
      {int i=-1;for(String xi:mj.getXs()){i+=1;
        String ti=mj.getTs().get(i);
        sb.append(ti+" £X"+xi+";");
        sb.append("public static java.util.function.BiConsumer<Object,Object> FieldAssFor£X"+xi+"=(f,o)->{(("+cn+")o).£X"+xi+"=("+ti+")f;};");
        }}
      return new RawJ(sb.toString());
      }

    public StringBuilder factory(boolean fwd){
      String x=mj.getName();
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{"+cn+" Res=new "+cn+"();");
      for(String xi:mj.getXs()){
        sb.append("Res.£X"+xi+"="+xi+";");
        if(fwd){sb.append(Fwd.class.getCanonicalName()+".AddIfFwd("+xi+",Res,"+cn+".FieldAssFor£X"+xi+");");}
        }
      sb.append("return Res;}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x+"(){return "+cn+"."+x+"(this");
        for(String xi:mj.getXs()){sb.append(", "+xi);}
        sb.append(");}");
        }
      return sb;
      }

    @Override
    public S visitNewFwd(SimpleBody s) {
      boolean interf=cd.getKind()==SimpleKind.Interface;
      StringBuilder sb=new StringBuilder();
      sb.append("{return new _Fwd();}\n");
      if(interf){sb.append("public static class _Fwd  implements "+cn+", "+Fwd.class.getName()+"{\n");}
      else {sb.append("public static class _Fwd extends "+cn+" implements "+Fwd.class.getName()+"{\n");}
      sb.append("private java.util.List<Object> os=new java.util.ArrayList<>();\n");
      sb.append("private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();\n");
      sb.append("public java.util.List<Object> os(){return os;}\n");
      sb.append("public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}\n");
      sb.append("public static final "+cn+" Instance=new _Fwd();\n");
      sb.append("public static "+cn+" Instance(){return Instance; }");
      return new RawJ(sb.toString());
      }

    @Override
    public S visitNativeIntSum(SimpleBody s) {
      throw Assertions.codeNotReachable();
      }

    @Override
    public S visitE(E s) {
      return L42FToMiniJS.forBody(ct, s);
      }
    }
  public static String liftX(String x) {
    if(x.equals("this")){return "£Xthis";}
    return x;
    }
  public static String liftMs(MethodSelector ms) {
    String res=ms.nameToS();
    for(String xi:ms.getNames()){res+="£X"+xi;}
    return res.replace("#", "£H");
    }
  }

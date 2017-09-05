package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB.MethodWithType;
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
import tools.StringBuilders;

public class L42FToMiniJ {
  public static MiniJ.CD of(ClassTable ct,L42F.CD cd){
    String name=cd.l42ClassName();
    boolean interf=cd.getKind()==L42F.SimpleKind.Interface;
    List<String> cs=new ArrayList<>();
    if(cd.getMs().stream().anyMatch(m->m.getBody()==SimpleBody.NewWithFwd)){
      cs.add(Resources.Revertable.class.getCanonicalName());
    }
    cs.addAll(tools.Map.of(i->ct.get(i).cd.l42ClassName(),cd.getCns()));
    List<MiniJ.M>ms=new ArrayList<>();
    for(L42F.M m:cd.getMs()){
      MiniJ.M res = methodHeader(ct, m);
      MiniJ.S body=m.getBody().accept(new VB(ct,name,cd,m,res));
      ms.add(res.withBody(body));
      if(!(m.getBody() instanceof E)){continue;}
      if(!m.isRefine()){continue;}
      int pSize=m.getTxs().size();
      boolean haveThis=pSize!=0 && m.getTxs().get(0).getX().equals("this");
      assert res.getName().startsWith("£C");
      MiniJ.M delegator=res.withStatic(false).withName("£M"+res.getName().substring(2));
      if(haveThis){
        delegator=delegator
          .withTs(res.getTs().subList(1,pSize))
          .withXs(res.getXs().subList(1,pSize));
        }
      delegator=delegator.withBody(new MiniJ.B(null,Collections.singletonList(new MiniJ.Return(
        new MiniJ.MCall(name, res.getName(),tools.Map.of(tx->tx.getX().equals("this")?"this":L42FToMiniJ.liftX(tx.getX()),m.getTxs())))
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
      assert mj.getName().startsWith("£C");
      r.append("{return £Xthis.£M"+mj.getName().substring(2)+"(");
      Iterator<String> it = mj.getXs().iterator();
      it.next();
      tools.StringBuilders.formatSequence(r,it,", ",x->r.append(x));
      r.append(");}\n");
      r.append("default "+mj.getRetT()+" £M"+mj.getName().substring(2)+"(");
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
      assert x.startsWith("£C");
      String f = fieldNameFromMethName(x);
      String t=mj.getRetT();
      String t1=mj.getTs().get(1);
      String x1=mj.getXs().get(1);
      StringBuilder sb=new StringBuilder();
      sb.append("{£Xthis."+f+"=£Xthat; return "+Resources.Void.class.getCanonicalName()+".Instance();}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x.substring(2)+"("+t1+" "+x1+"){return "+cn+"."+x+"(this,£Xthat);}");
        }
      return new RawJ(sb.toString());
      }

    private String fieldNameFromMethName(String x) {
      String f=x;
      if(f.startsWith("£C£H")){f=f.substring(4);}
      else {
        assert f.startsWith("£C");
        f=f.substring(2);
        }
      int i=f.indexOf("£");
      if(i!=-1){f=f.substring(0, i);}
      return "£X"+f;
    }

    @Override
    public S visitGetter(SimpleBody s) {
      String x=mj.getName();
      assert x.startsWith("£C");
      String f=fieldNameFromMethName(x);
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{return £Xthis."+f+";}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x.substring(2)+"(){return "+cn+"."+x+"(this);}");
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
        if(m.getSelector().isUnique()) {
          xi=xi+"_$_"+m.getSelector().getUniqueNum();
          }
        sb.append(ti+" "+xi+";");
        sb.append("public static java.util.function.BiConsumer<Object,Object> FieldAssFor£X"+xi+"=(f,o)->{(("+cn+")o)."+xi+"=("+ti+")f;};");
        }}
      appendReverter(sb);
      return new RawJ(sb.toString());
      }
    private void appendReverter(StringBuilder res) {
      String uniqueNum=m.getSelector().isUnique()?
        "_$_"+m.getSelector().getUniqueNum():"";
      res.append("public ast.ExpCore revert(){\n");
      String receiver="Instance().revert()";
      StringBuilder es=new StringBuilder();
      es.append("java.util.Arrays.asList(");
      StringBuilders.formatSequence(es,mj.getXs().iterator(),
        ", ",n->es.append(
            "platformSpecific.javaTranslation.Resources.Revertable.doRevert(this."+n+uniqueNum+")"
            ));
      es.append(")");
      res.append(
        "return new ast.ExpCore.MCall("
        +receiver+","+m.getSelector().toSrcEquivalent()
        +",ast.Ast.Doc.empty(),"+es+",null,null,null);\n");
      res.append("}\n");
    }

    private StringBuilder factory(boolean fwd){
      String x=mj.getName();
      assert x.startsWith("£C");
      String t=mj.getRetT();
      StringBuilder sb=new StringBuilder();
      sb.append("{"+cn+" Res=new "+cn+"();");
      for(String xi:mj.getXs()){
        String fi=xi;
        if(m.getSelector().isUnique()) {
          fi=fi+"_$_"+m.getSelector().getUniqueNum();
          }
        sb.append("Res."+fi+"="+xi+";");
        if(fwd){sb.append(Fwd.class.getCanonicalName()+".£CAddIfFwd("+xi+",Res,"+cn+".FieldAssFor£X"+fi+");");}
        }
      sb.append("return Res;}");
      if(this.m.isRefine()){
        sb.append("public "+t+ "£M"+x.substring(2)+"(){return "+cn+"."+x+"(this");
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
      sb.append("public ast.ExpCore revert(){return generated.Resource.£CPathOf("+cd.getCn()+");}\n");
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
    //if(x.equals("this")){return "£Xthis";}
    return "£X"+x.replace("%", "£P");
    }
  public static String liftMs(MethodSelector ms) {
    String res=ms.nameToS();
    for(String xi:ms.getNames()){res+=liftX(xi);}
    return "£C"+res.replace("#", "£H").replace("%", "£P");
    }
  }

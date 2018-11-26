package newReduction;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB.MethodWithType;
import ast.L42F;
import ast.L42F.Body;
import ast.L42F.CD;
import ast.L42F.E;
import ast.L42F.Kind;
import ast.L42F.M;
import ast.L42F.SimpleBody;
import ast.L42F.SimpleKind;
import ast.L42F.T;
import ast.L42F.Unreachable;
import l42FVisitors.BodyVisitor;
import l42FVisitors.ToFormattedText;
import platformSpecific.javaTranslation.Resources;
import tools.Assertions;
import tools.StringBuilders;

public class L42FToJavaString extends ToFormattedText{
  ClassTable ct;
  String cn;
  CD cd;
  Map<String, Integer> fs;
  public L42FToJavaString(ClassTable ct, CD cd){
    this.ct=ct;
    this.cd=cd;
    this.cn=cd.l42ClassName();
    this.fs = ct.fields(cd.getMs());//note: fnames contains unique number already
    }
  public  StringBuilder compute(){
    boolean interf=cd.getKind()==L42F.SimpleKind.Interface;
    String kind=interf?"interface":"class";
    c("public "+kind+" "+cd.l42ClassName());
    if(!interf) {c(" implements ");}
    else {c(" extends ");}
    c(NotLibrary.class.getCanonicalName());
    if(cd.getMs().stream().anyMatch(m->m.getBody()==SimpleBody.NewWithFwd)){
      c(", "+Resources.Revertable.class.getCanonicalName());
      }
    cd.getCns().stream()
      .map(i->ct.get(i).cd.l42ClassName())
      .distinct()
      .forEach(ci->c(", "+ci));
    c("{");
    indent();
    if(!interf) {appendReverter(fs);}
    for(L42F.M m:cd.getMs()){
      methodHeader(false, m);
      indent();nl();
      if (m.getBody() instanceof Unreachable) {
        c("{throw new Error(\"inhabited instance method called\");}");
      }
      else {m.getBody().accept(new VB(m));}
      if(m.getBody().equals(SimpleBody.Empty)|| m.getBody().equals(SimpleBody.NewFwd)){continue;}
      //Note:delegators are handled in a more consistend way in source code w.r.t. formalization,
      //where they are repeated by the pattern matching.
      if(!m.isRefine()){continue;}
      methodHeader(true, m);
      c("{return "+cd.l42ClassName()+".£C"+liftMs(m.getSelector())+"(");
      StringBuilders.formatSequence(result,m.getTxs().iterator(),
              ", ",tx->c(tx.getX().equals("this")?"this":L42FToJavaString.liftX(tx.getX())));
      c(");}");
      }
    for(var txi:fs.entrySet()) {
      String ti=ct.className(txi.getValue());
      String fi=liftX(txi.getKey());//already contains unique number
      nl();c(ti+" "+fi+";");
      nl();c("public static java.util.function.BiConsumer<Object,Object> FieldAssFor"+fi
        +"=(f,o)->{(("+cn+")o)."+fi+"=("+ti+")f;} ;");
      }
    deIndent();
    nl();
    c("}");
    return result;
    }
  private void methodHeader(boolean delegator, L42F.M m) {
    assert m.getBody()!= SimpleBody.Empty ||m.getTxs().get(0).getX().equals("this");
    String retT=liftT(m.getReturnType());
    nl();
    if(!delegator){c("static ");}
    String name=(delegator?"£M":"£C")+liftMs(m.getSelector());
    assert name.length()>2:
      m.getSelector();
    c("public "+retT+" "+name+" (");
    var it=m.getTxs().iterator();
    if(delegator && !m.getTxs().isEmpty() &&
      m.getTxs().get(0).getX().equals("this")) {it.next();}
    tools.StringBuilders.formatSequence(result,it,
      ", ",
      tx->{
        c(liftT(tx.getT()));
        sp();
        c(liftX(tx.getX()));
        });
    c(")");
    }

  private class VB implements BodyVisitor<Void>{
    public VB(M m) {this.m = m;}
    L42F.M m;
    @Override
    public Void visitEmpty(SimpleBody s) {
      String name="£M"+liftMs(m.getSelector());
      c("{return £Xthis."+name+"(");
      {
        var it = m.getTxs().iterator();
        it.next();//this
        tools.StringBuilders.formatSequence(result,it,", ",tx->c(liftX(tx.getX())));
        }
      c(");}\n");
      c("default "+liftT(m.getReturnType())+" "+name+"(");
      {
        var it=m.getTxs().iterator();
        it.next();//this
        tools.StringBuilders.formatSequence(result,it,", ",
          tx->c(liftT(tx.getT())+" "+liftX(tx.getX())));
        }
      return c("){throw new Error(\"Interface method invocation\");}");
      }

    @Override
    public Void visitSetter(SimpleBody s) {
      String x="£C"+liftMs(m.getSelector());
      String f = fieldNameFromMethName(x);
      c("{");
      if(fs.containsKey(f)) {c("£Xthis.£X"+f+"=£Xthat;");}
      c("return "+Resources.Void.class.getCanonicalName()+".Instance();}");
      return null;
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
      return f;
    }

    @Override
    public Void visitGetter(SimpleBody s) {
      String x="£C"+liftMs(m.getSelector());
      String f=fieldNameFromMethName(x);
      String t=liftT(m.getReturnType());
      String cast="";
      Integer csI=fs.get(f);
      if(csI==null) {c("{throw new Error();}");}
      else{
        boolean toCast=!csI.equals(m.getReturnType().getCn());
        var e=ct._get(csI);
        toCast=toCast && 
          (e==null ||!e.cd.getCns().contains(m.getReturnType().getCn()));   
        if(toCast) {cast="("+t+")";}
        c("{return "+cast+"£Xthis.£X"+f+";}");
        }
      return null;
      }

    @Override
    public Void visitNew(SimpleBody s) {
      return factory(false);
      }
    @Override
    public Void visitNewWithFwd(SimpleBody s) {
      return factory(true);
      }
    private Void factory(boolean fwd){
      c("{"+cn+" Res=new "+cn+"();");
      for(String xi:fs.keySet()) {
        String vari=liftX(xi);
        String fi=liftX(xi);
        if(m.getSelector().isUnique()) {
          vari=vari.substring(0, vari.lastIndexOf("_$_"));
          }
        c("Res."+fi+"="+vari+";");
        if(fwd){c(Fwd.class.getCanonicalName()+".£CAddIfFwd("+vari+",Res,"+cn+".FieldAssFor"+fi+");");}
        }
      c("return Res;}");
      return null;
      }

    @Override
    public Void visitNewFwd(SimpleBody s) {
      boolean interf=cd.getKind()==SimpleKind.Interface;
      c("{return new _Fwd();}\n");
      if(interf){ c("public static class _Fwd  implements "+cn+", "+Fwd.class.getName()+"{");}
      else { c("public static class _Fwd extends "+cn+" implements "+Fwd.class.getName()+"{");}
      indent();nl();
      c("public ast.ExpCore revert(){return generated.Resource.£CPathOf("+cd.getCn()+");}");nl();
      c("private java.util.List<Object> os=new java.util.ArrayList<>();");nl();
      c("private java.util.List<java.util.function.BiConsumer<Object,Object>> fs=new java.util.ArrayList<>();");nl();
      c("public java.util.List<Object> os(){return os;}");nl();
      c("public java.util.List<java.util.function.BiConsumer<Object,Object>> fs(){return fs;}}");
      deIndent();nl();
      c("public static final "+cn+" Instance=new _Fwd();");nl();
      c("public static "+cn+" Instance(){return Instance; }");nl();
      return null;
      }

    @Override
    public Void visitNativeIntSum(SimpleBody s) {
      throw Assertions.codeNotReachable();
      }

    @Override
    public Void visitE(E s) {
       result.append(L42FToMiniJS.forBody(ct, s));
       return null;
      }
    }
  public static String liftX(String x) {
    //if(x.equals("this")){return "£Xthis";}
    return "£X"+x.replace("%", "£P");
    }
  public static String liftMs(MethodSelector ms) {
    String res=ms.nameToS();
    for(String xi:ms.getNames()){res+=liftX(xi);}
    return res.replace("#", "£H").replace("%", "£P");
    }
  public String liftT(T t){
    return ct.className(t.getCn());
  }
  private void appendReverter(Map<String, Integer> fs) {
    nl();c("public ast.ExpCore revert(){return null;}");
    /*indent();nl();
    c("return new ast.ExpCore.MCall(Instance().revert(),"
          +"\"revertNotExists\""
          +",ast.Ast.Doc.empty(),");
    c("java.util.Arrays.asList(");
    StringBuilders.formatSequence(result,fs.keySet().iterator(),
      ", ",f->c(
        "platformSpecific.javaTranslation.Resources.Revertable.doRevert(this."
        +f+")"
        ));
    c(")");
    c(",null,null,null);\n");
    c("}\n");*/
    }
  }
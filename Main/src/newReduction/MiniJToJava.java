package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.ExpCore;
import ast.MiniJ;
import ast.L42F.Block;
import ast.L42F.BreakLoop;
import ast.L42F.Call;
import ast.L42F.Cast;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.If;
import ast.L42F.K;
import ast.L42F.Loop;
import ast.L42F.Null;
import ast.L42F.SimpleKind;
import ast.L42F.Throw;
import ast.L42F.Update;
import ast.L42F.Use;
import ast.L42F.X;
import ast.L42F._void;
import ast.MiniJ.B;
import ast.MiniJ.Break;
import ast.MiniJ.CD;
import ast.MiniJ.IfTypeCase;
import ast.MiniJ.M;
import ast.MiniJ.MCall;
import ast.MiniJ.RawJ;
import ast.MiniJ.Return;
import ast.MiniJ.S;
import ast.MiniJ.Try;
import ast.MiniJ.UseCall;
import ast.MiniJ.VarAss;
import ast.MiniJ.VarDec;
import ast.MiniJ.WhileTrue;
import auxiliaryGrammar.Functions;
import facade.L42;
import l42FVisitors.JVisitor;
import l42FVisitors.ToFormattedText;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import platformSpecific.javaTranslation.Resources;
import tools.Assertions;

public class MiniJToJava extends ToFormattedText implements JVisitor<Void>{
  public static String of(ClassTable ct){
    List<Integer> ks=new ArrayList<>(ct.keySet());
    Collections.sort(ks);
    String res="";
    for(Integer i:ks){
      ast.L42F.CD cd = ct.get(i).cd;
      CD jCdi=null;
      if (cd.getKind()!=null){
        jCdi=L42FToMiniJ.of(ct, cd);
        }
      if(jCdi==null) {continue;}//must be a lib stub

      res+=MiniJToJava.of(jCdi)+"\n";
      }
    return res;
    }

  public static String of(MiniJ.CD cd){
    MiniJToJava v=new MiniJToJava();
    v.visit(cd);
    return v.result.toString();
    }
  public Void visit(MiniJ.CD cd){
    String kind=cd.isInterface()?"interface":"class";
    c("public "+kind+" "+cd.getCn());
    if(!cd.getCns().isEmpty()){
      if(!cd.isInterface()) {c(" implements ");}
      else {c(" extends ");}
      tools.StringBuilders.formatSequence(result,cd.getCns().iterator(),
              ", ",cni->c(cni));
      }
    c("{");
    indent();
    for(M mi: cd.getMs()){
      nl();
      if(mi.isStatic()) {c("static ");}
      c("public "+mi.getRetT()+" "+mi.getName()+"(");
      tools.StringBuilders.formatSequence(result, mi.getTs().iterator(), mi.getXs().iterator(),
        ", ",(t,x)->c(t+" "+x));
      c(")");
      mi.getBody().accept(this);
      }
    nl();
    c("}");
    deIndent();
    return null;
    }

    @Override
    public Void visit(B s) {
      if(s.getLabel()!=null){c("{"+s.getLabel()+":{");}
      else{c("{");}
      for(S si:s.getSs()){si.accept(this);}
      if(s.getLabel()!=null){c("}");}
      return c("}");
      }

    @Override
    public Void visit(Break s) {
      return c("break "+s.getLabel()+";");
      }

    @Override
    public Void visit(ast.MiniJ.If s) {
      c("if("+s.getCond()+")");
      s.getThen().accept(this);
      c(" else ");
      s.get_else().accept(this);
      return null;
      }

    @Override
    public Void visit(IfTypeCase s) {
      //if(x0.inner() instanceof cn){cn x1=(cn)x0.inner(); [then]} else [_else]
      String xi=s.getX0()+".inner()";
      c("if("+xi+" instanceof "+s.getCn()+"){");
      c(s.getCn()+" "+s.getX1()+"=("+s.getCn()+")"+xi+"; ");
      s.getThen().accept(this);
      c("} else ");
      s.get_else().accept(this);
      return null;
      }

    @Override
    public Void visit(MCall s) {
      if(s.getMName().startsWith("£CLoadLib_")) {
        int cn=Integer.parseInt(s.getMName().substring("£CLoadLib_".length()));
        return c(s.getCn()+".£CLoadLib("+cn+");");
        }
      if(s.getMName().startsWith("£COf_")) {
        int cn=Integer.parseInt(s.getMName().substring("£COf_".length()));
        return c(s.getCn()+".£COf("+cn+");");
        }
      c(s.getCn()+"."+s.getMName()+"(");
      tools.StringBuilders.formatSequence(result, s.getXs().iterator(),", ", x->c(x));
      return c(");");
      }

    @Override
    public Void visit(Return s) {
      c("return ");
      s.getE().accept(this);
      return null;
      }

    @Override
    public Void visit(ast.MiniJ.Throw s) {
      if(s.getK()!=null){
        c("throw new "+tOf(s.getK())+"("+s.getX()+");");
        }
      else {c("throw "+s.getX()+";");}
      return null;
      }

    @Override
    public Void visit(Try s) {
      c("try");
      s.getB().accept(this);
      for(ast.MiniJ.K k:s.getKs()){
        liftK(k);
        }
      return null;
      }
    private String tOf(Ast.SignalKind kind){
      switch(kind){
        case Return: return Resources.Return.class.getCanonicalName();
        case Exception: return Resources.Exception.class.getCanonicalName();
        case Error: return Resources.Error.class.getCanonicalName();
        }
      throw Assertions.codeNotReachable();
      }

    private void liftK(ast.MiniJ.K k) {
      c("catch ("+tOf(k.getT())+" "+k.getX()+")");
      k.getB().accept(this);
    }

    @Override
    public Void visit(UseCall s) {
      UsingInfo ui=s.getUi();
      List<String> es=s.getXs();
      StringBuilder resOld=result;
      result=new StringBuilder();
      result.append("{");
      s.getInner().accept(this);
      result.append("}");
      String e=result.toString();
      result=resOld;
      PluginType pt=platformSpecific.fakeInternet.OnLineCode.plugin(s.getDoc());
      c(pt.executableJ(ui, e, es, L42.usedNames));
      c(";");
      return null;
      }

    @Override
    public Void visit(VarAss s) {
      c(s.getX()+"=");
      s.getE().accept(this);
      return null;
      }

    @Override
    public Void visit(VarDec s) {
      return c(s.getCn()+" "+s.getX()+";");
      }

    @Override
    public Void visit(WhileTrue s) {
      c(s.getLabel()+":while(true)");
      s.getS().accept(this);
      return null;
      }

    @Override
    public Void visit(ast.MiniJ.X s) {
      return c(s.getInner()+";");
      }

    @Override
    public Void visit(RawJ s) {
      return c(s.getInner());
      }

    @Override
    public Void visit(ast.MiniJ.Null s) {
      return c("null;");
      }

    @Override
    public Void visit(ast.MiniJ.Cast s) {
      return c("("+s.getCn()+")"+s.getX()+";");
      }


}

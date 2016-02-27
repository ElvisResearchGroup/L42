package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.Op;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Type;
import ast.Ast.NormType;
import ast.Ast.HistoricType;
import ast.Ast;
import ast.Ast.VarDec;
import ast.Expression;
import ast.Expression.*;
public class GuessType implements Visitor<Type> {
  HashMap<String,Type> varEnv;
  public GuessType(HashMap<String, Type> varEnv) {
    this.varEnv=varEnv;
  }
  public static Type of(Expression e,HashMap<String,Type> varEnv){
    Type t=e.accept(new GuessType(varEnv));
    return t;
  }
  public Type visit(Signal s)  {return NormType.immVoid;}
  public Type visit(If s) {return NormType.immVoid;}
  public Type visit(While s) {return NormType.immVoid;}
  public Type visit(With s)  {return NormType.immVoid;}
  public Type visit(UseSquare s)  {return NormType.immVoid;}
  public Type visit(_void s) {return NormType.immVoid;}
  public Type visit(Loop s) {return NormType.immVoid;}

  public Type visit(DocE s) {return s.getInner().accept(this);}
  public Type visit(Using s) {return s.getInner().accept(this); }

  public Type visit(X s) {
    assert (this.varEnv.containsKey(s.getInner())):s;
    assert this.varEnv.get(s.getInner())!=null;
    return this.varEnv.get(s.getInner());
    }
  public Type visit(Path s) { return new NormType(Mdf.Type,s,Ph.None); }

  public Type visit(RoundBlock s) {
    HashMap<String, Type> tmpVarEnv = this.varEnv;
    this.varEnv=new HashMap<String, Type>(this.varEnv);
    for(BlockContent cnt:s.getContents()){
      for(VarDec vd:cnt.getDecs()){
        if(vd instanceof Ast.VarDecXE){
          Ast.VarDecXE vdx=(Ast.VarDecXE) vd;
          if(vdx.getT().isPresent()){
            varEnv.put(vdx.getX(),vdx.getT().get());
          }
          else{
            varEnv.put(vdx.getX(),vdx.getInner().accept(this));
          }
        }
      }
    }
    Type result=s.getInner().accept(this);
    this.varEnv=tmpVarEnv;
    return result;
  }

  public static HistoricType concatHistoricType(Type t,MethodSelector ms ){
    if(t instanceof NormType){
      NormType nt = (NormType)t;
      List<Ast.MethodSelectorX> selectors=Collections.singletonList(new Ast.MethodSelectorX(ms,""));
      return new HistoricType(nt.getPath(), selectors,false);
    }
    HistoricType ht=(HistoricType)t;
    List<Ast.MethodSelectorX> selectors = new ArrayList<>(ht.getSelectors());
    selectors.add(new Ast.MethodSelectorX(ms,""));
    return ht.withSelectors(selectors);
  }
  public Type visit(MCall s) {
    Type t=s.getReceiver().accept(this);
    assert t!=null:s;
    return concatHistoricType(t,getMS(s));
    }

  MethodSelector getMS(MCall mc){
    List<String> xs=new ArrayList<String>();
    if(mc.getPs().getE().isPresent()){xs.add("that");}
    xs.addAll(mc.getPs().getXs());
    return new MethodSelector(mc.getName(),xs);
  }


  public Type visit(UnOp s) { return visit(Desugar.visit1Step(s));}
  public Type visit(FCall s) { return visit(Desugar.visit1Step(s));}
  public Type visit(SquareCall s) { return visit(Desugar.visit1Step(s));}
  public Type visit(Literal s) { return GuessType.concatHistoricType(s.getReceiver().accept(this),Desugar.literalGuessedSelector());}
  public Type visit(BinOp s) {
    if(s.getOp().kind==Ast.OpKind.EqOp){return NormType.immVoid;}
    return visit(Desugar.visit1Step(s));
    }
  public Type visit(SquareWithCall s) {
    return visit(new MCall(s.getReceiver(),"#apply",Doc.empty(),Desugar.getPs(),s.getP()));
  }
  public Type visit(DotDotDot s)  {throw Assertions.codeNotReachable();}
  public Type visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Type visit(CurlyBlock s) {
    throw new ast.ErrorMessage.NotWellFormedMsk(s,s,"Can not infer the type of a { ... return ... } block.");
    }

  public Type visit(ClassReuse s){return new NormType(Mdf.Immutable,Path.Library(),Ph.None);}
  public Type visit(ClassB s) {return new NormType(Mdf.Immutable,Path.Library(),Ph.None);}
  @Override public Type visit(ContextId s) {
    throw new ast.ErrorMessage.NotWellFormedMsk(s,s,"Can not infer the type of a hashId (can this error happens?)");
  }

}

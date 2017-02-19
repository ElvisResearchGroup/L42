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
  public Type visit(Using s) {
    //WE CAN NOT DO SOMETHING LIKE THAT,
    //SINCE PROGRAM NOT AVAILABLE DURING DESUGARING
    //List<NormType> ts = OnLineCode.pluginType(p, s);
    return s.getInner().accept(this); }

  public Type visit(X s) {
    assert (this.varEnv.containsKey(s.getInner())):
      s;
    assert this.varEnv.get(s.getInner())!=null;
    return this.varEnv.get(s.getInner());
    }
  public Type visit(Path s) { return new NormType(Mdf.Class,s,Ph.None,Doc.empty()); }

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
      return new HistoricType(nt.getPath(), selectors,false,Doc.empty());
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
    return MethodSelector.of(mc.getName(),xs);
  }


  public Type visit(UnOp s) { return visit(Desugar.visit1Step(s));}
  public Type visit(FCall s) { return visit(Desugar.visit1Step(s));}
  public Type visit(SquareCall s) { return GuessType.concatHistoricType(s.getReceiver().accept(this),Desugar.squareGuessedSelector());}
  public Type visit(Literal s) { return GuessType.concatHistoricType(s.getReceiver().accept(this),Desugar.literalGuessedSelector());}
  public Type visit(BinOp s) {
    Op op=s.getOp();
    if(op.kind==Ast.OpKind.EqOp){return NormType.immVoid;}
    if(op.negated){
      BinOp s2=s.withOp(op.nonNegatedVersion());
      return visit(new UnOp(s.getP(),Op.Bang,s2));
      }
    if(!op.normalized){
      BinOp op2=new BinOp(s.getP(),s.getRight(),op.normalizedVersion(),s.getLeft());
      return visit(op2);
      }
    assert !op.negated && op.normalized :op; 
    MCall mc=Desugar.getMCall(s.getP(),s.getLeft(),Desugar.desugarName(op.inner),Desugar.getPs(s.getRight()));
    return visit(mc);
    }
  public Type visit(SquareWithCall s) {
    { return GuessType.concatHistoricType(s.getReceiver().accept(this),Desugar.squareGuessedSelector());}
  }
  public Type visit(DotDotDot s)  {throw Assertions.codeNotReachable();}
  public Type visit(WalkBy s) {throw Assertions.codeNotReachable();}
  public Type visit(CurlyBlock s) {
    throw new ast.ErrorMessage.NotWellFormedMsk(s,s,"Can not infer the type of a { ... return ... } block.");
    }

  public Type visit(ClassReuse s){return NormType.immLibrary;}
  public Type visit(ClassB s) {return NormType.immLibrary;}
  @Override public Type visit(ContextId s) {
    throw new ast.ErrorMessage.NotWellFormedMsk(s,s,"Can not infer the type of a hashId (can this error happens?)");
  }

}

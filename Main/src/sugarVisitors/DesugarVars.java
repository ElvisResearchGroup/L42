package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.ErrorMessage;
import ast.Expression;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.With.On;
import ast.Expression.BlockContent;
import ast.Ast.VarDec;
import ast.Ast.VarDecCE;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.ErrorMessage.NotWellFormedMsk;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
import ast.Expression.ContextId;
import ast.Expression.CurlyBlock;
import ast.Expression.DocE;
import ast.Expression.DotDotDot;
import ast.Expression.FCall;
import ast.Expression.If;
import ast.Expression.Literal;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.Signal;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.UnOp;
import ast.Expression.UseSquare;
import ast.Expression.Using;
import ast.Expression.WalkBy;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Expression.ClassB.MethodImplemented;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import auxiliaryGrammar.Functions;
import facade.L42;

public class DesugarVars extends CloneVisitor{
  Set<String> usedVars=new HashSet<String>();


  public static boolean assertVarsRemoved(Expression _e){
    _e.accept(new CloneVisitor(){
      //Ok liftVarDec since With uses liftVarDecXE
      @Override protected ast.Ast.VarDec liftVarDec(ast.Ast.VarDec d) {
        return d.match(
            vdxe->{
              if(vdxe.isVar()){
                throw new AssertionError("Variable local binding found:"+vdxe.toString());
              }
              return this.liftVarDecXE(vdxe);
            },
            this::liftVarDecE,
            this::liftVarDecCE);
      }
    });
    return true;
  }

  public static Expression of(Set<String> usedVars,Expression e){
    DesugarVars d=new DesugarVars();
    d.usedVars=usedVars;
    Expression result= e.accept(d);
    return result;
  }
  private RoundBlock blockContentSepare(RoundBlock s) {
    if(s.getContents().size()<=1){return s;}
    List<Expression.BlockContent> ctxTop = new ArrayList<>(s.getContents().subList(0,1));
    List<Expression.BlockContent> ctxPop = new ArrayList<>(s.getContents().subList(1, s.getContents().size()));
    RoundBlock next=blockContentSepare(s.withContents(ctxPop));
    return s.withContents(ctxTop).withInner(next);
  }
  public Expression visit(CurlyBlock s) {
    Expression inner;
    if(s.getContents().size()==1 &&
       s.getContents().get(0).get_catch().isEmpty() &&
       s.getContents().get(0).getDecs().size()==1&&
       s.getContents().get(0).getDecs().get(0) instanceof VarDecE
        ){
      inner=((VarDecE)s.getContents().get(0).getDecs().get(0)).getInner();
    }
    else{ inner=new RoundBlock(s.getP(),s.getDoc(),Expression._void.instance,s.getContents());}
    inner=inner.accept(this);
    List<VarDec> vd = Collections.singletonList((VarDec)new VarDecE(inner));
    BlockContent o=new BlockContent(vd,Collections.emptyList());
    return new CurlyBlock(s.getP(),Doc.empty(),Collections.singletonList(o));
  }
  public Expression visit(RoundBlock s) {
    s=blockContentSepare(s);
    s=blockVarClass(s);
    Expression result= super.visit(s);
    return result;
    }
public RoundBlock blockVarClass(RoundBlock s) {
  RoundBlock res=_blockVarClass(s);
  if(res.equals(s)){return res;}
  return blockVarClass(res);
 }
private int firstVar(List<VarDec> varDecs){
  {int i=-1;
  for(VarDec _dec:varDecs){i+=1;
    if(!(_dec instanceof VarDecXE)){continue;}
    VarDecXE dec=(VarDecXE)_dec;
    //assert dec.getT().isPresent();
    if(!dec.isVar()){continue;}
    return i;
    }}
  return -1;
  }
private VarDecXE getDecForVar(String cName,VarDecXE varDec) {
  NormType nt=new NormType(Mdf.Mutable,Path.outer(0).pushC(cName),Ph.None,Doc.empty());
  Position pos = Desugar.getPosition(varDec.getInner());
  MCall right=new MCall(nt.getPath(),"#apply",Doc.empty(), Desugar.getPs("inner",new X(varDec.getX())),pos);
  String nameZ=Functions.freshName(nt.getPath(), usedVars);
  //usedVars.add(nameZ);
  return new VarDecXE(false,Optional.of(nt),nameZ,right);
}
 private RoundBlock _blockVarClass(RoundBlock s) {
   if(s.getContents().isEmpty()){return s;}
   List<VarDec> varDecs = new ArrayList<VarDec>(s.getContents().get(0).getDecs());
   int pos=firstVar(varDecs);
   if(pos==-1){return s;}
   VarDecXE varDec=(VarDecXE)varDecs.get(pos);
   varDecs.set(pos,varDec.withVar(false));
   VarDecCE ce=getClassBForVar(varDec);
   VarDecXE d=getDecForVar(ce.getInner().getName(),varDec);
   X x=new X(varDec.getX());
   X z=new X(d.getX());
   int d3First=findD2(x,pos,varDecs);
   RoundBlock fake = getFakeBlock(x,z,s, varDecs, d3First);
   List<VarDec> trueDecs=new ArrayList<VarDec>();
   trueDecs.add(ce);
   if(d3First!=-1){trueDecs.addAll(varDecs.subList(0, d3First));}
   else{trueDecs.addAll(varDecs);}
   trueDecs.add(d);
   trueDecs.addAll(fake.getContents().get(0).getDecs());
   List<Expression.BlockContent> trueContent=new ArrayList<>();
   trueContent.add(new Expression.BlockContent(trueDecs,fake.getContents().get(0).get_catch()));
   return fake.withContents(trueContent);
   }
 private int findD2(X x,int pos, List<VarDec> decs) {
   for(VarDec _dec:decs.subList(pos+1,decs.size())){
     pos+=1;
     Expression[] inner={null};
     _dec.match(xe->inner[0]=xe.getInner(), e->inner[0]=e.getInner(), ce->null);
     if(inner[0]==null){continue;}
     boolean isAssigned=Exists.of(inner[0],e->{
       if(!(e instanceof BinOp)){return false;}
       BinOp bo=(BinOp)e;
       if(bo.getOp().kind!=Ast.OpKind.EqOp){return false;}
       return bo.getLeft().equals(x);
     });
     if (isAssigned){return pos;}
   }
   return -1;
 }
 private RoundBlock getFakeBlock(X x, X z,RoundBlock s, List<VarDec> varDecs,  int d3First) {
   Position pos=Desugar.getPosition(s);
   List<Expression.BlockContent> fakeContent=new ArrayList<>();
   List<VarDec> fakeDecs=new ArrayList<VarDec>();
   if(d3First!=-1){//d3 not empty
     fakeDecs.addAll(varDecs.subList(d3First, varDecs.size()));
   }
   fakeContent.add(new Expression.BlockContent(fakeDecs,s.getContents().get(0).get_catch()));
   RoundBlock fake=s.withContents(fakeContent);
   fake=(RoundBlock) XEqOpInZEqOp.of(x, z, fake);
   fake=(RoundBlock) XInE.of(x,Desugar.getMCall(pos, z,"#inner", Desugar.getPs()),fake);
   return fake;
 }
 private VarDecCE getClassBForVar(VarDecXE varDec) {
   List<FieldDec> fs=new ArrayList<FieldDec>();
   fs.add(new FieldDec(true,_computeTypeForClassBForVar(varDec),"inner",Doc.empty()));
   ClassB cb=new ClassB(Doc.empty(),new Ast.ConcreteHeader(Mdf.Mutable, "",fs,Position.noInfo),Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Position.noInfo,Stage.None);
   String nameC=Functions.freshName("Var"+varDec.getX(), L42.usedNames);
   //usedCnames.add(nameC);
   return new VarDecCE(new NestedClass(Doc.getPrivate(),nameC,cb,null));
 }
 public Type _computeTypeForClassBForVar(VarDecXE varDec) {
   assert varDec.getT().isPresent(): " it is now required by the stricted syntax";
   Type t=varDec.getT().get();
   t=t.match(
     nt->nt.withPath(computeTypeForClassBForVar(nt.getPath())),
     h -> h.withPath(computeTypeForClassBForVar(h.getPath()))
     );
   return t;
 }
 private Path computeTypeForClassBForVar(Path path) {
   if (path.isPrimitive()){return path;}
   if (!path.isCore()){return path;}
   return path.setNewOuter(path.outerNumber()+1);
   }

}


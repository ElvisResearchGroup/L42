package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.NormType;
import ast.Ast;
import ast.ExpCore;
import ast.Redex;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Redex.Garbage;
import ast.Ast.*;
import auxiliaryGrammar.Functions;
import programReduction.Program;

public class IsValue extends TestShapeVisitor{
  Program p;
  public IsValue(Program p){this.p=p;}
  public static boolean of(Program p, ExpCore e){return e.accept(new IsValue(p));}
  //atoms
  public static boolean isAtom(ExpCore e){
    return e instanceof Ast.Atom;
  }

  public static Redex.Garbage nestedGarbage(ExpCore e){
    if(!(e instanceof Block)){return null;}
    return nestedGarbage((Block)e);
  }


  public static Redex.Garbage nestedGarbage(Block b) {
    //b is a right value!
    Block b2=Functions.garbage(b,b.getDecs().size());
    if(!b2.equals(b)){return new Redex.Garbage(b2);}
    //else, try nested!
    {int i=-1;for(Dec di:b.getDecs()){i+=1;
      if (!(di.getInner() instanceof Block)){continue;}
      Block bi=(Block) di.getInner();
      Redex.Garbage ngi=nestedGarbage(bi);
      if(ngi==null){continue;}
      List<Block.Dec> ds=new ArrayList<Block.Dec>(b.getDecs());
      ds.set(i,di.withInner(ngi.getThatLessGarbage()));
      return new Redex.Garbage(b.withDecs(ds));
    }}
    if(!(b.getInner() instanceof Block)){return null;}
    Block bIn=(Block)b.getInner();
    Redex.Garbage ngIn=nestedGarbage(bIn);
    if(ngIn==null){return null;}
    return new Redex.Garbage(b.withInner(ngIn.getThatLessGarbage()));

  }


  public Boolean visit(ExpCore.EPath s) {return true;}
  public Boolean visit(X s) {return true;}
  public Boolean visit(_void s)  {return true;}
  public Boolean visit(ClassB s)  {return true;}
  public Boolean visit(Block s)  {
    if(!s.getOns().isEmpty()){return false;}
    int dvsn=s.getDecs().size();
    for( int i=0;i<dvsn;i++){
      if(!validDv(s.getDecs().get(i))){return false;}
    }
    return s.getInner().accept(this);
    }
  public boolean validDv(Block.Dec dv) {
    NormType nt=dv.getT().get().getNT();
    if(nt.getMdf()==Mdf.Capsule){return false;}
    if(!Functions.isComplete(nt)){return false;}
    if(Functions.isInterface(p,nt.getPath())){return false;}
    if(validRightValue(dv.getInner())){return true;}
    if(nt.getMdf()==Mdf.Immutable && dv.getInner() instanceof Block){
      return dv.getInner().accept(this);
    }
    return false;
  }
  public boolean validDecForPh(Dec dv) {
    NormType nt=dv.getT().get().getNT();
    if(validRightValue(dv.getInner())){return true;}
    if(nt.getMdf()==Mdf.Immutable && dv.getInner() instanceof Block){
      return dv.getInner().accept(this);
    }
    return false;
  }
  public boolean validRightValue(ExpCore ec)  {
    if(!(ec instanceof MCall))return false;
    MCall s=(MCall)ec;
    if(!(s.getInner() instanceof EPath)){return false;}
    MethodSelector ms=s.getS();
    MethodWithType mwt=(MethodWithType) p.extractClassB(((EPath)s.getInner()).getInner())._getMember(ms);
    if(mwt.get_inner().isPresent()){return false;}
    if(mwt.getMt().getMdf()!=ast.Ast.Mdf.Class){return false;}
    for(ExpCore ei:s.getEs()){
      if(!isAtom(ei)){return false;}
      }
    return true;
    }
  /*public static boolean isResult(Program p, ExpCore inner) {
    if (!(inner instanceof Signal)){return false;}
    return IsValue.of(p,((Signal)inner).getInner());
  }*/

}

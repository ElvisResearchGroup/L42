package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.Ast;
import ast.ExpCore;
import ast.Redex;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.*;
import ast.Redex.Garbage;
import ast.Ast.*;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class IsValue extends TestShapeVisitor{
  Program p;
  public IsValue(Program p){this.p=p;}
  public static boolean of(Program p, ExpCore e){return e.accept(new IsValue(p));}
  //atoms
  public static boolean isAtom(ExpCore e){
    return e instanceof Path ||e instanceof X
        || e instanceof _void||e instanceof ClassB;
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
      if (!(di.getE() instanceof Block)){continue;}
      Block bi=(Block) di.getE();
      Redex.Garbage ngi=nestedGarbage(bi);
      if(ngi==null){continue;}
      List<Block.Dec> ds=new ArrayList<Block.Dec>(b.getDecs());
      ds.set(i,di.withE(ngi.getThatLessGarbage()));
      return new Redex.Garbage(b.withDecs(ds));
    }}
    if(!(b.getInner() instanceof Block)){return null;}
    Block bIn=(Block)b.getInner();
    Redex.Garbage ngIn=nestedGarbage(bIn);
    if(ngIn==null){return null;}
    return new Redex.Garbage(b.withInner(ngIn.getThatLessGarbage()));    
    
  }
  
  
  public Boolean visit(Path s) {return true;}
  public Boolean visit(X s) {return true;}
  public Boolean visit(_void s)  {return true;}
  public Boolean visit(ClassB s)  {return true;}
  public Boolean visit(Block s)  {
    if(s.get_catch().isPresent()){return false;}
    int dvsn=s.getDecs().size();
    for( int i=0;i<dvsn;i++){
      if(!validDvs(s.getDecs().get(i))){return false;}
    }
    return s.getInner().accept(this);
    }
  public boolean validDvs(Block.Dec dvs) {
    NormType nt=dvs.getNT();
    if(nt.getMdf()==Mdf.Capsule){return false;}
    if(nt.getPh()!=Ph.None){return false;}
    if(Functions.isInterface(p,nt.getPath())){return false;}
    if(validRightValue(dvs.getE())){return true;}
    if(nt.getMdf()==Mdf.Immutable && dvs.getE() instanceof Block){
      return dvs.getE().accept(this);
    }
    return false;
  }
  public boolean validDecForPh(Dec dvs) {
    NormType nt=dvs.getNT();
    if(validRightValue(dvs.getE())){return true;}
    if(nt.getMdf()==Mdf.Immutable && dvs.getE() instanceof Block){
      return dvs.getE().accept(this);
    }
    return false;
  }
  public boolean validRightValue(ExpCore ec)  {
    if(!(ec instanceof MCall))return false;
    MCall s=(MCall)ec;
    if(!(s.getReceiver() instanceof Path)){return false;}
    MethodSelector ms=new MethodSelector(s.getName(),s.getXs());
    MethodWithType mwt=p.method((Path)s.getReceiver(),ms,true);
    if(mwt.getInner().isPresent()){return false;}
    if(mwt.getMt().getMdf()!=ast.Ast.Mdf.Type){return false;}
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

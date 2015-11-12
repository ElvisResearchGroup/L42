package sugarVisitors;

import ast.Expression;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.Position;
import ast.Expression.ClassB.Member;;

public class CollapsePositions extends CloneVisitor{
  Position p=Position.noInfo;
  public static Position of(Expression e){
    CollapsePositions cp=new CollapsePositions();
    e.accept(cp);
    return cp.p;
  }
  
  protected Header liftH(Header h){
    accumulatePos(h);
    return super.liftH(h);
  } 
  protected FieldDec liftF(FieldDec f){
    accumulatePos(f);
    return super.liftF(f);
  } 
  protected Member liftM(Member m){
    accumulatePos(m);
    return super.liftM(m);
  } 
  protected <T extends Expression>T lift(T e){
   accumulatePos(e);
    return super.lift(e);
  }
  private void accumulatePos(Object o){
    if(o instanceof Expression.HasPos){
      Expression.HasPos hp=(Expression.HasPos)o;
      p=accumulatePos(p,hp.getP());
    }
  }
  public static Position accumulatePos(Position p1, Position p2) {
    if(p2==null){return p1;}
    String file=p1.getFile();
    if(file==null){file=p2.getFile();}
    //assert file!=null;
    if(p1.getFile()==null || p2.getFile()==null||p1.getFile().equals(p2.getFile())){
      return new Position(file,
        Integer.min(p1.getLine1(),p2.getLine1()),
        Integer.min(p1.getPos1(),p2.getPos1()),
        Integer.max(p1.getLine2(),p2.getLine2()),
        Integer.max(p1.getPos2(),p2.getPos2())
        );
      }
    //if(genericity(p1)>genericity(p2)){return p2;}//mess up with file names?
    return p1;
    }
  private static int genericity(Position p) {
    return (p.getLine2()-p.getLine1())*10000
    +
    (p.getPos2()-p.getPos1());
  }

}

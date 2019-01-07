package sugarVisitors;

import ast.Expression;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.Position;
import ast.ExpCore;
import ast.Expression.ClassB.Member;
import coreVisitors.InjectionOnSugar;;

public class CollapsePositions extends CloneVisitor{
  Position p=Position.noInfo;
  public static Position of(ExpCore e){
    return of(e.accept(new InjectionOnSugar()));
  }
  public static Position of(Expression e){
    CollapsePositions cp=new CollapsePositions();
    cp.lift(e);
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
  public static Position accumulateSinglePos(Position p){
    if(p.get_next()==null){return p;}
    Position p2=accumulateSinglePos(p.get_next());
    assert p2.get_next()==null;
    Position res= accumulatePos(p.with_next(null),p2);
    assert res.get_next()==null;
    return res;
    }
  public static Position accumulatePos(Position p1, Position p2) {
    if(p2==null){return p1;}
    if(p1==null){return p2;}
    p1=accumulateSinglePos(p1);
    p2=accumulateSinglePos(p2);
    String file=p1.getFile();
    if(file==null){file=p2.getFile();}
    //assert file!=null;
    if(p1.getFile()==null || p2.getFile()==null||p1.getFile().equals(p2.getFile())){
      return new Position(file,
        Integer.min(p1.getLine1(),p2.getLine1()),
        Integer.min(p1.getPos1(),p2.getPos1()),
        Integer.max(p1.getLine2(),p2.getLine2()),
        Integer.max(p1.getPos2(),p2.getPos2()),
        null);
      }
    return p1;
    }
  }

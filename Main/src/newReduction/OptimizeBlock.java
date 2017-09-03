package newReduction;

import java.util.ArrayList;
import java.util.List;

import ast.L42F.*;
import l42FVisitors.CloneVisitor;
import l42FVisitors.Visitor;
/**
 
remove ds and e if a ds before is "terminating"
remove unised var //later
remove Void var and replace occurences with void //later

case cut
[(ds ks e[T])]=(ds1..dsj (dsj+1..dsn)[with e=unreachable] [ks] unreachable [T])
  d1..dn=[ds]
  forall i in 1..j-1 !terminating(dsi)
  terminating(dsj)
case noCut
[(ds ks e[T])]=(ds1..dsn [ks] [e] [T])
  d1..dn=[ds]
  forall i in 1..n !terminating(dsi)

 -------
 terminating(T x=e)=terminating(e)
 
 terminating(e)
 holds when:
   terminating(loop e)
      terminating(unreachable)
   terminating(throw[TX1,TX2] x)
   terminating(breakLoop)
   terminating((dxs kxs e [TX]))
     if terminating(kxs.es,e) 
   terminating(if x then e1 else e2)
     if terminating(e1,e2) 
    
  */
public class OptimizeBlock extends CloneVisitor{
  @Override
  public E visit(Block s) {
    List<D> newDs =new ArrayList<>();
    Terminating t=new Terminating();
    boolean terminating=false;
    for(D di:s.getDs()){
      di=liftD(di);
      newDs.add(di);
      if(di.getE().accept(t)){terminating=true;break;}
      }
    List<K> newKs = tools.Map.of(this::liftK,s.getKs());
    E e=new Unreachable();
    if(!terminating){e=s.getE().accept(this);}
    return new Block(newDs,newKs,e,liftT(s.getType()));
    }
  }


class Terminating implements Visitor<Boolean>{

    @Override
    public Boolean visit(Block s) {
      for(K ki:s.getKs()){
        if(!ki.getE().accept(this)){return false;}
      }  
    return s.getE().accept(this);
    }

    @Override
    public Boolean visit(X s) {return false;}
    @Override
    public Boolean visit(Cn s) {return false;}
    @Override
    public Boolean visit(_void s) {return false;}

    @Override
    public Boolean visit(Null s) {return false;}

    @Override
    public Boolean visit(BreakLoop s) {return true;}

    @Override
    public Boolean visit(Throw s) {return true;}
    @Override
    public Boolean visit(Loop s) {return true;}

    @Override
    public Boolean visit(Call s) {return false;}

    @Override
    public Boolean visit(Use s) {return false;}

    @Override
    public Boolean visit(If s) {
      if(!s.getThen().accept(this)){return false;}
      return s.get_else().accept(this);
    }

    @Override
    public Boolean visit(Update s) {return false;}

    @Override
    public Boolean visit(Cast s) {return false;}

    @Override
    public Boolean visit(Unreachable s) {return true;}
    }
package is.L42.visitors;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;

import java.util.HashMap;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Core.MWT;
import is.L42.generated.Full;

public abstract class Accumulate<T> extends PropagatorCollectorVisitor{
  public T of(Visitable<?> e){
    e.accept(this);
    return result;
    }
  protected T result=empty();
  public T acc(){return this.result;}
  public T empty(){return null;}//can be overrided, but is often left to null
  public static abstract class SkipL<T> extends Accumulate<T>{
    @Override public void visitL(Full.L l){}
    @Override public void visitL(CoreL l){}
    }
  public static abstract class WithG<T> extends SkipL<T>{
    private final HashMap<X,Core.T> g=new HashMap<>();
    @Override public void visitMWT(MWT mwt){
      assert g.isEmpty();
      visitDocs(mwt.docs());
      visitMH(mwt.mh());
      var _e0=mwt._e();
      if(_e0!=null){
        var names=mwt.key().xs();
        var ts=mwt.mh().pars();
        assert names.size()==ts.size();
        for(var i:range(names)){commitG(names.get(i),ts.get(i));}
        commitG(X.thisX,P.coreThis0.withMdf(mwt.mh().mdf()));
        visitE(_e0);
        assert g.size()==names.size()+1;
        g.clear();    
        }
      }
    public Core.T g(Core.XP xP){
      if(xP instanceof Core.PCastT){return ((Core.PCastT)xP).t();}
      if(xP instanceof Core.EX){return g(((Core.EX)xP).x());}
      throw bug();
      }
    public Core.T g(X x){
      Core.T res=g.get(x);
      assert res!=null:x;
      return res;
      }
    public void commitG(X x,Core.T t){
      assert !g.containsKey(x);
      g.put(x, t);
      }
    public void removeG(X x){
      assert g.containsKey(x);
      g.remove(x);
      }
    public void visitBlockDs(Core.Block block){
      visitDs(block.ds());
      }
    @Override public void visitBlock(Core.Block block){
      for(var d:block.ds()){commitG(d.x(),d.t());}
      visitBlockDs(block);
      visitE(block.e());
      for(var d:block.ds()){removeG(d.x());}
      for(var k:block.ks()){
        commitG(k.x(),k.t());
        visitK(k);  
        removeG(k.x());
        }
      }
    }
  }
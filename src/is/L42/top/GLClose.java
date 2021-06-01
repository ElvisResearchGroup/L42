package is.L42.top;

import static is.L42.tools.General.bug;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import is.L42.common.CTz;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.PropagatorCollectorVisitor;

class GLClose extends G{
  LayerL layer;
  GLClose(LayerL layer,State state){this.layer=layer;this.state=state;}
  public LayerL layer(){return layer;}
  public R _open(G gc,R rc){throw bug();}
  public R _close(G gc,R rc){
    LayerL l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GLClose)gc).layer;}
    boolean eq=l2!=null &&
      layer.p().equals(l2.p()) &&
      layer.ms().equals(l2.ms())&& 
      layer.e1n().equals(l2.e1n()) &&
      layer.ctz().equals(l2.ctz()) &&
      state.equals(gc.state);
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    CTz ctz=new CTz(layer.ctz());
    assert !eq || layer.ctz().equals(l2.ctz()); 
    Core.L res=eq?(Core.L)rc._obj:s2.topClose(layer.p(),layer.ms(),layer.e1n(),ctz);
    LayerE l=layer.layerE();
    Half.E newE=set(l.e(),res);
    l=l.layerL().push(newE,ctz.releaseMap());
    Full.L newL=_get(newE);
    if(newL!=null){return new R(new GLOpen(l,s2),res);}
    return new R(new GEClose(l,s2),res);
    }
  public boolean needOpen(){return false;}
  static Full.L _get(Half.E e){
    return new Accumulate<Full.L>(){
      @Override public void visitL(Full.L l){if(result==null){result=l;}}
      @Override public void visitL(Core.L l){return;}
      }.of(e.visitable());
    }
  static Half.E set(Half.E e,Core.L coreL){
    return new CloneVisitor(){
      boolean updated=false;
      @Override public LL visitL(Full.L l){
        if(updated){return l;}
        updated=true;
        return coreL;
        }
      @Override public Core.L visitL(Core.L l){return l;}
      }.visitE(e);
    }
  }
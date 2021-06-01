package is.L42.top;

import static is.L42.tools.General.bug;

import is.L42.common.CTz;
import is.L42.generated.Full;
import is.L42.generated.Half;

class GEOpen extends G{
  LayerL layer;
  GEOpen(LayerL layer,State state){this.layer=layer;this.state=state;}
  public LayerL layer(){return layer;}
  public R _open(G gc,R rc){
    LayerL l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GEOpen)gc).layer;}
    var old=l2==null?null:l2.ncs().get(l2.index());
    var current=layer.ncs().get(layer.index());
    boolean eq=l2!=null
      && old.equals(current)
      && l2.p().equals(layer.p())
      && state.equals(gc.state);
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    CTz ctz=eq?null:new CTz(layer.ctz());
    Half.E e=eq?(Half.E)rc._obj:s2.topNCiOpen(layer.p(),layer.index(),layer.ncs(),ctz);
    Full.L l=GLClose._get(e);
    //TODO: the meta e should not touch the ctzMap anyway, right?
    //var ctzMap=eq?((LayerE)rc._g.layer()).ctz():ctz.releaseMap();
    var ctzMap=layer.ctz();
    LayerE newLayer=layer.push(e,ctzMap);
    if(l==null){return new R(new GEClose(newLayer,s2),e);}
    return new R(new GLOpen(newLayer,s2),e);
    }
  public R _close(G gc,R rc){throw bug();}
  public boolean needOpen(){return true;}
  }
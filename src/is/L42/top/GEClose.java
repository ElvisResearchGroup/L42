package is.L42.top;

import static is.L42.tools.General.bug;

import java.util.List;
import java.util.Map;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.ST;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.visitors.Accumulate;

class GEClose extends G{
  LayerE layer;
  GEClose(LayerE layer,State state){this.layer=layer;this.state=state;}
  public LayerE layer(){return layer;}
  public R _open(G gc,R rc){throw bug();}
  public R _close(G gc,R rc){
    LayerE l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GEClose)gc).layer;}
    LayerL oldPopL=l2==null?null:l2.layerL();
    LayerL popL=layer.layerL();
    var currentP=popL.p();
    var oldP=l2==null?null:oldPopL.p();
    var currentE=layer.e();
    var oldE=l2==null?null:l2.e();
    boolean hasHashDollar=hasHashDollar(currentE);
    boolean eq=l2!=null;
    int index=layer.layerL().index();
    eq = eq && index==l2.layerL().index();
    if(eq){
      Full.L.NC nc=layer.layerL().ncs().get(index);
      Full.L.NC nc2=l2.layerL().ncs().get(index);
      eq= oldE.equals(currentE)
        && nc.key().equals(nc2.key())
        && nc.docs().equals(nc2.docs())
        && !hasHashDollar 
        && oldP.equals(currentP)
        && state.equals(gc.state);
      }
    if(eq && rc.isErr()){return rc;}
    if(!eq){Resources.loader.loadByteCodeFromCache(state.allByteCode,state.allLibs);}
    State s2=(eq?rc._g.state:state).copy();
    assert GLClose._get(layer.e())==null;
    //It seams like we need to do the new CTz that internally copies the data
    Program p=eq?(Program)rc._obj:s2.topNCiClose(currentP,popL.index(),popL.ncs(),currentE,new CTz(layer.ctz()));
    popL=popL.layerE().push(p, popL.index()+1,popL.ncs(),popL.ms(),popL.e1n(),normalize(p,popL.ctz()));
    if(popL.index()<popL.ncs().size()){return new R(new GEOpen(popL,s2),p);}
    return new R(new GLClose(popL,s2),p);
    }
  private Map<ST, List<ST>> normalize(Program p, Map<ST, List<ST>> ctz) {
    var res=new CTz(p,ctz);
    return res.releaseMap();
    }
  public boolean needOpen(){return false;}
  private boolean hasHashDollar(Half.E e){
    return new Accumulate.SkipL<Boolean>(){
      @Override public Boolean empty(){return false;}
      @Override public void visitMCall(Half.MCall s){
        super.visitMCall(s);
        if(s.s().m().startsWith("#$")){
          this.result=true;}
        }
      }.of(e.visitable());
    }
  }
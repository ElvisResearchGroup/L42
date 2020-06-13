package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.typeFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.ST;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.PropagatorCollectorVisitor;

interface LayerE{
  Half.E e();
  Map<ST,List<ST>> ctz();
  LayerL layerL();
  default LayerL push(Program p,int index,List<Full.L.NC> ncs,
    List<Full.L.M> ms,List<Half.E> e1n,Map<ST,List<ST>> ctz){
    var self=this;
    return new LayerL(){
      @Override public String toString(){return "L["+p+", "+index+", "+ncs+", "+ms+", "+e1n+", "+self+"]";}
      @Override public int hashCode(){
        return 31*(31+p.hashCode())+ncs.hashCode();
        }//purposely only exploring those two fields, to balance efficiency
      @Override public boolean equals(Object o){
        if(this==o){return true;}
        if(o==null){return false;}
        if(this.getClass()!=o.getClass()){return false;}
        var l=(LayerL)o;
        if(!p.equals(l.p())){return false;}
        if(index!=l.index()){return false;}
        if(!ncs.equals(l.ncs())){return false;}
        if(!ms.equals(l.ms())){return false;}
        if(!e1n.equals(l.e1n())){return false;}
        return true;   
        }
      boolean pOk(Program p){
        if(p.top instanceof Full.L){
          return false;}
        if(p.pTails.isEmpty()){return true;}
        return pOk(p.pop());
        }
      boolean deptOk(Program p, LayerL l){
        assert l!=LayerL.empty;
        var layerEnd=l.layerE().layerL()==LayerL.empty;
        var pEnd=p.pTails.isEmpty();
        if(layerEnd==pEnd && layerEnd){return true;}
        if(layerEnd!=pEnd){
          return false;}
        return deptOk(p.pop(),l.layerE().layerL());
        }
      {
        assert deptOk(p,this);
        assert pOk(p);
        }
      public int index(){return index;}
      public Program p(){return p;}
      public List<Full.L.NC> ncs(){return ncs;}
      public List<Full.L.M> ms(){return ms;}
      public List<Half.E> e1n(){return e1n;}
      public Map<ST,List<ST>> ctz(){return ctz;}
      public LayerE layerE(){return self;}
      };
    }
  }
interface LayerL{
  int index();
  Program p();
  List<Full.L.NC> ncs();
  List<Full.L.M> ms();
  List<Half.E> e1n();
  Map<ST,List<ST>> ctz();
  LayerE layerE();
  static final LayerL empty=new LayerL(){
    public int index(){throw bug();}
    public Program p(){throw bug();}
    public List<Full.L.NC> ncs(){throw bug();}
    public List<Full.L.M> ms(){throw bug();}
    public List<Half.E> e1n(){throw bug();}
    public LayerE layerE(){throw bug();}
    public Map<ST,List<ST>> ctz(){throw bug();}
    @Override public int hashCode(){return 1;}
    @Override public boolean equals(Object o){return this==o;}
    @Override public String toString(){return "EmptyL";}
    };
  default LayerE push(Half.E e,Map<ST,List<ST>>ctz){
    var self=this;
    return new LayerE(){
      public Half.E e(){return e;}
      public Map<ST,List<ST>> ctz(){return ctz;}
      public LayerL layerL(){return self;}
      @Override public String toString(){return "E["+e+", "+ctz+", "+self+"]";}
      @Override public int hashCode(){return 31*(31+e.hashCode())+ctz.hashCode();}//purposely not exploring nested layers
      @Override public boolean equals(Object o){
        if(this==o){return true;}
        if(o==null){return false;}
        if(this.getClass()!=o.getClass()){return false;}
        var l=(LayerE)o;
        return self.equals(l.layerL()) 
          && e.equals(l.e()) && ctz.equals(l.ctz());   
        }
      };
    }
  }
/*
layerE::= he layerL
layerL::= empty| p;i;mwts,ncs,e1n layerE
start with 
  layerE fullL empty
GLOpen->           GEOpen,          GLClose
layerE      p;0;mwts,ncs,e1n layerE   p;0;mwts,ncs,e1n  layerE
GLClose->          GEClose,         GLOpen
layerL' he layerL  he* layerL       he* layerL
GEOpen->           GLOpen,          GEClose
layerL             he layerL        he layerL
GEClose->          GLClose,         GEOpen
he p;i;.. layerE   p*;i+1;.. layerE    p*;i+1;.. layerE
*/

class GLOpen extends G{
  LayerE layer;
  GLOpen(LayerE layer,State state){this.layer=layer;this.state=state;}
  public LayerE layer(){return layer;}
  private Program currentP(LayerL l,Full.L fullL){
    if(l==LayerL.empty){return Program.flat(fullL);}
    if(l.ncs().isEmpty()){return l.p().push(fullL);}//only for tests
    C c=l.ncs().get(l.index()).key();
    return l.p().push(c,fullL);
    }
  public R _open(G gc,R rc){
    LayerE l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GLOpen)gc).layer;}
    Program pC=l2==null?null:currentP(l2.layerL(),GLClose._get(l2.e()));
    Full.L original=GLClose._get(layer.e());
    assert original!=null;
    Program p=currentP(layer.layerL(),original);
    assert p.top.isFullL();
    boolean eq=l2!=null && p.equals(pC) && !((Full.L)p.top).reuseUrl().contains("#$");
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    assert !eq || state.equals(gc.state):
      "";
    assert !eq || s2.uniqueId==0 
      || l2.layerL()==LayerL.empty 
      || l2.ctz().equals(layer.ctz());
    var ncs=typeFilter(original.ms().stream(),Full.L.NC.class);
    var ms=L(original.ms().stream().filter(m->!(m instanceof Full.L.NC)));
    var e1n=new ArrayList<Half.E>();
    var releasedMap=new AtomicReference<Map<ST,List<ST>>>();
    if(!eq){State.loader.loadByteCodeFromCache(state.allByteCode,state.allLibs);}
    Program p2=eq?getP(rc):s2.topOpen(p,e1n,state.uniqueId==0?Collections.emptyMap():layer.ctz(),releasedMap);
    if(eq){e1n.addAll(getE1n(rc));}
    var ctz=eq?((LayerL)rc._g.layer()).ctz():releasedMap.get();
    LayerL l=layer.push(p2,0,ncs,ms,e1n,ctz);
    if(ncs.isEmpty()){return new R(new GLClose(l,s2),new Object[]{p2,e1n});}
    return new R(new GEOpen(l,s2),new Object[]{p2,e1n});
    }
  private Program getP(R r){
    var os=(Object[])r._obj;
    return (Program)os[0];
  }
  @SuppressWarnings("unchecked")
  private ArrayList<Half.E> getE1n(R r){
    var os=(Object[])r._obj;
    return (ArrayList<Half.E>)os[1];
  }
  public R _close(G gc,R rc){throw bug();}
  public boolean needOpen(){return true;}
  }
class GLClose extends G{
  LayerL layer;
  GLClose(LayerL layer,State state){this.layer=layer;this.state=state;}
  public LayerL layer(){return layer;}
  public R _open(G gc,R rc){throw bug();}
  public R _close(G gc,R rc){
    LayerL l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GLClose)gc).layer;}
    boolean eq=l2!=null && layer.p().equals(l2.p()) && layer.ms().equals(l2.ms())&&layer.e1n().equals(l2.e1n()) && layer.ctz().equals(l2.ctz());
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    CTz ctz=new CTz(layer.ctz());
    assert !eq || (state.equals(gc.state) && layer.ctz().equals(l2.ctz())):
      state.equals(gc.state)+" "+layer.ctz().equals(l2.ctz()); 
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
class GEOpen extends G{
  LayerL layer;
  GEOpen(LayerL layer,State state){this.layer=layer;this.state=state;}
  public LayerL layer(){return layer;}
  public R _open(G gc,R rc){
    LayerL l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GEOpen)gc).layer;}
    var old=l2==null?null:l2.ncs().get(l2.index());
    var current=layer.ncs().get(layer.index());
    boolean eq=l2!=null && old.equals(current) && l2.p().equals(layer.p());
    assert !eq || state.equals(gc.state):
      "";
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    CTz ctz=eq?null:new CTz(layer.ctz());
    Half.E e=eq?(Half.E)rc._obj:s2.topNCiOpen(layer.p(),layer.index(),layer.ncs(),ctz);
    Full.L l=GLClose._get(e);
    var ctzMap=eq?((LayerE)rc._g.layer()).ctz():ctz.releaseMap();
    LayerE newLayer=layer.push(e,ctzMap);
    if(l==null){return new R(new GEClose(newLayer,s2),e);}
    return new R(new GLOpen(newLayer,s2),e);
    }
  public R _close(G gc,R rc){throw bug();}
  public boolean needOpen(){return true;}
  }
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
    boolean eq=l2!=null && oldE.equals(currentE)&& !hasHashDollar &&oldP.equals(currentP);
    assert !eq || state.equals(gc.state);
    //TODO: can the p ever be different?
    if(eq && rc.isErr()){return rc;}
    if(!eq){State.loader.loadByteCodeFromCache(state.allByteCode,state.allLibs);}
    State s2=(eq?rc._g.state:state).copy();
    assert GLClose._get(layer.e())==null;
    CTz ctz=new CTz(layer.ctz());//TODO: is it the case that topNCi close do not modify CTz? in that case, can we avoid the copy?
    Program p=eq?(Program)rc._obj:s2.topNCiClose(currentP,popL.index(),popL.ncs(),currentE,ctz);
    popL=popL.layerE().push(p, popL.index()+1,popL.ncs(),popL.ms(),popL.e1n(),popL.ctz());
    if(popL.index()<popL.ncs().size()){return new R(new GEOpen(popL,s2),p);}
    return new R(new GLClose(popL,s2),p);
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
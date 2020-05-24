package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.typeFilter;
import java.util.List;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LL;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;

interface LayerE{
  Half.E e();
  LayerL layerL();
  default LayerL push(int index,List<Full.L.NC> ncs,
    List<Full.L.MWT> ms,List<Half.E> e1n){
    var self=this;
    return new LayerL(){
      public int index(){return index;}
      public List<Full.L.NC> ncs(){return ncs;}
      public List<Full.L.MWT> ms(){return ms;}
      public List<Half.E> e1n(){return e1n;}
      public LayerE layerE(){return self;}
      };
    }
  }
interface LayerL{
  int index();
  List<Full.L.NC> ncs();
  List<Full.L.MWT> ms();
  List<Half.E> e1n();
  LayerE layerE();
  static final LayerL empty=new LayerL(){
    public int index(){throw bug();}
    public List<Full.L.NC> ncs(){throw bug();}
    public List<Full.L.MWT> ms(){throw bug();}
    public List<Half.E> e1n(){throw bug();}
    public LayerE layerE(){throw bug();}
    };
  default LayerE push(Half.E e){
    var self=this;
    return new LayerE(){
      public Half.E e(){return e;}
      public LayerL layerL(){return self;}
      };
    }
  }
/*
layerE::= he layerL
layerL::= empty| i;mwts,ncs,e1n layerE
start with 
  layerE fullL empty
GLOpen->           GEOpen,          GLClose
layerE      0;mwts,ncs,e1n layerE   0;mwts,ncs,e1n  layerE
GLClose->          GEClose,         GLOpen
layerL' he layerL  he* layerL       he* layerL
GEOpen->           GLOpen,          GEClose
layerL             he layerL        he layerL
GEClose->          GLClose,         GEOpen
he i;.. layerE     i+1;.. layerE    i+1;.. layerE
*/
class GLOpen implements G{
  LayerE layer; State state;
  GLOpen(LayerE layer,State state){this.layer=layer;this.state=state;}
  public G _open(){
    State s2=state;
    List<Full.L.NC> ncs=L();
    List<Full.L.MWT> ms=L();
    List<Half.E>e1n=L();
    if(state.p.top instanceof Full.L){
      s2=state.copy();
      Full.L original=(Full.L)state.p.top;
      ncs=typeFilter(original.ms().stream(),Full.L.NC.class);
      ms=typeFilter(original.ms().stream(),Full.L.MWT.class);
      e1n=s2.topOpen((Full.L)s2.p.top);
      }
    LayerL l=layer.push(0, ncs,ms,e1n);
    if(ncs.isEmpty()){return new GLClose(l,s2);}
    return new GEOpen(l,s2);
    }
  public G _close(){throw bug();}
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return true;}
  public Program out(){return this.state.p;}
  }
class GLClose implements G{
  LayerL layer; State state;
  GLClose(LayerL layer,State state){this.layer=layer;this.state=state;}
  public G _open(){throw bug();}
  public G _close(){
    LayerE l=layer.layerE();
    State s2=state.copy();
    Program res=s2.topClose(layer.ms(),layer.e1n());
    assert res!=null;
    Half.E newE=set(l.e(),res.topCore());//TODO: may return core?
    Full.L newL=_get(newE);
    LayerE newLayer=l.layerL().push(newE);
    if(newL!=null){
      s2.p=s2.p.update(newL);
      return new GLOpen(newLayer,s2);
      }
    s2.p=s2.p.pop();
    return new GEClose(newLayer,s2);
    }
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return false;}
  public Program out(){return this.state.p;}
    static Full.L _get(Half.E e){
    return new Accumulate<Full.L>(){
      @Override public void visitL(Full.L l){if(result!=null){result=l;}}
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
class GEOpen implements G{
  LayerL layer; State state;
  GEOpen(LayerL layer,State state){this.layer=layer;this.state=state;}
  public G _open(){
    State s2=state.copy();
    Half.E e=s2.topNCiOpen(layer.index(),layer.ncs());
    Full.L l=GLClose._get(e);
    LayerE newLayer=layer.push(e);
    if(l==null){return new GEClose(newLayer,s2);}
    return new GLOpen(newLayer,s2);
    }
  public G _close(){throw bug();}
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return true;}
  public Program out(){return this.state.p;}
  }
class GEClose implements G{
  LayerE layer; State state;
  GEClose(LayerE layer,State state){this.layer=layer;this.state=state;}
  public G _open(){throw bug();}
  public G _close(){
    State s2=state.copy();
    LayerL popL=layer.layerL();
    s2.topNCiClose(popL.index(),popL.ncs(),layer.e());
    if(popL.index()<popL.ncs().size()){
      popL=popL.layerE().push(popL.index()+1,popL.ncs(),popL.ms(),popL.e1n());
      return new GEOpen(popL,s2);
      }
    return new GLClose(popL,s2);
    }
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return false;}
  public Program out(){return this.state.p;}
  }
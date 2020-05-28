package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.typeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.L42.common.CTz;
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

interface LayerE{
  Half.E e();
  Map<ST,List<ST>> ctz();
  LayerL layerL();
  default LayerL push(Program p,int index,List<Full.L.NC> ncs,
    List<Full.L.M> ms,List<Half.E> e1n){
    var self=this;
    return new LayerL(){
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
  LayerE layerE();
  static final LayerL empty=new LayerL(){
    public int index(){throw bug();}
    public Program p(){throw bug();}
    public List<Full.L.NC> ncs(){throw bug();}
    public List<Full.L.M> ms(){throw bug();}
    public List<Half.E> e1n(){throw bug();}
    public LayerE layerE(){throw bug();}
    @Override public int hashCode(){return 1;}
    @Override public boolean equals(Object o){return this==o;}
    };
  default LayerE push(Half.E e,Map<ST,List<ST>>ctz){
    var self=this;
    return new LayerE(){
      public Half.E e(){return e;}
      public Map<ST,List<ST>> ctz(){return ctz;}
      public LayerL layerL(){return self;}
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
  public G _open(){
    Full.L original=GLClose._get(layer.e());
    assert original!=null;
    Program p=currentP(layer.layerL(),original);
    State s2=state.copy();
    CTz frommedCTz=s2.uniqueId==0?new CTz()://if we are not at the start
      layer.layerL().p().push(p.pTails.c(),Program.emptyL)
      .from(layer.ctz(),P.pThis1);      
    var ncs=typeFilter(original.ms().stream(),Full.L.NC.class);
    var ms=L(original.ms().stream().filter(m->!(m instanceof Full.L.NC)));
    var e1n=new ArrayList<Half.E>();
    Program p2=s2.topOpen(p,e1n,frommedCTz);
    LayerL l=layer.push(p2,0,ncs,ms,e1n);
    if(ncs.isEmpty()){return new GLClose(l,s2);}
    return new GEOpen(l,s2);
    }
  public G _close(){throw bug();}
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return true;}
  public Program out(){return this.layer.layerL().p();}
  }
class GLClose extends G{
  LayerL layer;
  GLClose(LayerL layer,State state){this.layer=layer;this.state=state;}
  public LayerL layer(){return layer;}
  public G _open(){throw bug();}
  public G _close(){
    State s2=state.copy();
    CTz ctz=new CTz(layer.layerE().ctz());
    Core.L res=s2.topClose(layer.p(),layer.ms(),layer.e1n(),ctz);
    LayerE l=layer.layerE();
    Half.E newE=set(l.e(),res);
    l=l.layerL().push(newE,ctz.releaseMap());
    Full.L newL=_get(newE);
    if(newL!=null){return new GLOpen(l,s2);}
    return new GEClose(l,s2);
    }
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return false;}
  public Program out(){return layer.p();}
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
  public G _open(){
    State s2=state.copy();
    CTz ctz=new CTz(layer.layerE().ctz());
    Half.E e=s2.topNCiOpen(layer.p(),layer.index(),layer.ncs(),ctz);
    Full.L l=GLClose._get(e);
    LayerE newLayer=layer.push(e,ctz.releaseMap());
    if(l==null){return new GEClose(newLayer,s2);}
    return new GLOpen(newLayer,s2);
    }
  public G _close(){throw bug();}
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return true;}
  public Program out(){return layer.p();}
  }
class GEClose extends G{
  LayerE layer;
  GEClose(LayerE layer,State state){this.layer=layer;this.state=state;}
  public LayerE layer(){return layer;}
  public G _open(){throw bug();}
  public G _close(){
    State s2=state.copy();
    LayerL popL=layer.layerL();
    assert GLClose._get(layer.e())==null;
    CTz ctz=new CTz(layer.ctz());
    Program p=s2.topNCiClose(popL.p(),popL.index(),popL.ncs(),layer.e(),ctz);
    popL=popL.layerE().push(p, popL.index()+1,popL.ncs(),popL.ms(),popL.e1n());
    if(popL.index()<popL.ncs().size()){return new GEOpen(popL,s2);}
    return new GLClose(popL,s2);
    }
  public boolean middleAndCloseCached(Cache c){throw bug();}
  public boolean needOpen(){return false;}
  public Program out(){return Program.flat((Core.L)layer.e());}
  }
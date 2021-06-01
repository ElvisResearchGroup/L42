package is.L42.top;

import static is.L42.tools.General.bug;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.ST;
import is.L42.generated.Full.L.M;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Half.E;
import is.L42.top.LayerL.EmptyLayerL;

interface LayerL extends Serializable{
  int index();
  Program p();
  List<Full.L.NC> ncs();
  List<Full.L.M> ms();
  List<Half.E> e1n();
  Map<ST,List<ST>> ctz();
  LayerE layerE();
  //To avoid serialization from making many instances of it
  static enum EmptyLayerL implements LayerL{Empty;
    public int index(){throw bug();}
    public Program p(){throw bug();}
    public List<Full.L.NC> ncs(){throw bug();}
    public List<Full.L.M> ms(){throw bug();}
    public List<Half.E> e1n(){throw bug();}
    public LayerE layerE(){throw bug();}
    public Map<ST,List<ST>> ctz(){throw bug();}
    @Override public String toString(){return "EmptyL";}
    };
  static LayerL empty(){return EmptyLayerL.Empty;}
  default LayerE push(Half.E e,Map<ST,List<ST>>ctz){
    //I'm not sure which is the right p    assert this==empty() || new CTz(ctz).coherent(this.p());
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
package is.L42.top;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.ST;
import is.L42.generated.Full.L.M;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Half.E;

interface LayerE extends Serializable{
  Half.E e();
  Map<ST,List<ST>> ctz();
  LayerL layerL();
  default LayerL push(Program p,int index,List<Full.L.NC> ncs,
    List<Full.L.M> ms,List<Half.E> e1n,Map<ST,List<ST>> ctz){
    assert new CTz(ctz).coherent(p);
    var self=this;
    //assert ms.stream().noneMatch(m->m instanceof Full.L.F && m._e()!=null);
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
        assert l!=LayerL.empty();
        var layerEnd=l.layerE().layerL()==LayerL.empty();
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
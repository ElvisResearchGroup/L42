package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.typeFilter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.ST;
import is.L42.generated.Half.E;
import is.L42.platformSpecific.javaTranslation.Resources;

class GLOpen extends G{
  LayerE layer;
  GLOpen(LayerE layer,State state){this.layer=layer;this.state=state;}
  public LayerE layer(){return layer;}
  private Program currentP(LayerL l,Full.L fullL){
    if(l==LayerL.empty()){return Program.flat(fullL);}
    if(l.ncs().isEmpty()){return l.p().push(fullL);}//only for tests
    C c=l.ncs().get(l.index()).key();
    return l.p().push(c,fullL);
    }
  private LL noNC(LL ll) {
    if(!ll.isFullL()) {return ll;}
    var l=(Full.L)ll;
    return l.withMs(L(l.ms().stream().filter(m->!(m instanceof Full.L.NC))));
    }
  public R _open(G gc,R rc){
    //TestCachingCases.timeNow("GLOpen1");
    LayerE l2=null;
    if(gc!=null && gc.getClass()==this.getClass()){l2=((GLOpen)gc).layer;}
    Program pC=l2==null?null:currentP(l2.layerL(),GLClose._get(l2.e()));
    Full.L original=GLClose._get(layer.e());
    assert original!=null;
    Program p=currentP(layer.layerL(),original);
    assert p.top.isFullL();
    //TestCachingCases.timeNow("GLOpen2");
    boolean eq=l2!=null &&
      !((Full.L)p.top).reuseUrl().contains("#$") &&
      state.equals(gc.state)&&
      l2.ctz().equals(layer.ctz());//ctz need to be tested, can change when errors are fixed
    if(eq){//check the header is the same
      Program pNoNC=p.update(noNC(p.top),false);
      Program pCNoNC=pC.update(noNC(pC.top),false);
      eq=pNoNC.equals(pCNoNC);
      }
    if(eq && rc.isErr() && rc._err instanceof EndError.InvalidHeader){
      eq=false;
      }
    if(eq && !rc.isErr() && p.top.isFullL()){//check novel ncs names do not clash with cached ones
      var ncs=((LayerL)rc._g.layer()).p().topCore().ncs();//all and only the nesteds from the cached reuse 
      var mwts=((LayerL)rc._g.layer()).p().topCore().mwts();//same for mwts from reuse
      for(var mi:((Full.L)p.top).ms()){
        if(LDom._elem(((Full.L)pC.top).ms(),mi.key())!=null){continue;}
        var dupC=ncs.stream().anyMatch(ncj->ncj.key().equals(mi.key()));
        if(dupC){eq=false;break;}
        var dupM=mwts.stream().anyMatch(mwtj->mwtj.key().equals(mi.key()));
        if(dupM){eq=false;break;}
        }
      }
    //TestCachingCases.timeNow("GLOpen3 "+eq);
    if(eq && rc.isErr()){return rc;}
    State s2=(eq?rc._g.state:state).copy();
    assert !eq || s2.uniqueId==0
      || l2.layerL()==LayerL.empty()
      || l2.ctz().equals(layer.ctz()):
        eq+" "+s2.uniqueId+" "+
        l2.layerL()==LayerL.empty()+" "+
        l2.ctz().equals(layer.ctz());
    var ncs=typeFilter(original.ms().stream(),Full.L.NC.class);
    var ms=L(original.ms().stream().filter(m->!(m instanceof Full.L.NC)));
    //TestCachingCases.timeNow("GLOpen4");
    if(!eq){Resources.loader.loadByteCodeFromCache(state.allByteCode,state.allLibs);}
    //TestCachingCases.timeNow("GLOpen5");
    Program p2;
    Map<ST,List<ST>> ctz;
    List<Half.E> e1n;
    //TestCachingCases.timeNow("GLOpen6");
    if(eq){
      p2=((LayerL)rc._g.layer()).p();;
      ctz=((LayerL)rc._g.layer()).ctz();
      e1n=((LayerL)rc._g.layer()).e1n();
      }
    else{
      State.TopOpenOut out=s2.topOpen(p,state.uniqueId==0?Collections.emptyMap():layer.ctz());
      p2=out.p;
      ctz=out.releasedMap;
      e1n=out.e1n;
      }
    //TestCachingCases.timeNow("GLOpen7 "+p2.dept()+" "+(!p2.pTails.isEmpty() && p2.pTails.hasC()?p2.pTails.c():""));
    LayerL l=layer.push(p2,0,ncs,ms,e1n,ctz);
    if(ncs.isEmpty()){return new R(new GLClose(l,s2),null);}
    return new R(new GEOpen(l,s2),null);
    }
  public R _close(G gc,R rc){throw bug();}
  public boolean needOpen(){return true;}
  }
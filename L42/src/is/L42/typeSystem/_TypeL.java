package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;

import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.MH;
import is.L42.generated.P;
import is.L42.generated.Core.L.MWT;

public class _TypeL {
  public static void type(Program p){
    for(MWT mwt:p.topCore().mwts()){type(p,mwt);}
    for(NC nc:p.topCore().ncs()){
      Program p0=p.push(nc.key(),nc.l());
      type(p0);
      if (nc.key().hasUniqueNum()){coherent(p0);}
      }
    }
  public static void type(Program p, MWT mwt){
    MH mh=mwt.mh();
    List<Core.T> ts=p.topCore().ts();
    List<MH> mhs=L(ts,(c,ti)->{
      var pi=ti.p().toNCs();
      var l=p._ofCore(pi);
      assert l.isInterface();
      var mwts=l.mwts();
      var mi=_elem(mwts,mwt.key());
      if(mi==null){return;}
      assert mi._e()==null;
      c.add(p.from(mi.mh(),pi));
      });
    /*
    forall MH in MHs:
      p|-MWT.T<= MH.T //method returns a type which is not a sybtype of its ancestor "name"
      MWT.G=MH.G //invalid type w.r.t. implemented paramerer xi
      forall Pi in MWT.exceptions.Ps exists Pj in MH.exceptions.Ps such that p |- Pi <= Pj
      //or error: leaked exception P is not the subtype of a declared exception
      //or  method declares an exception (P) which is not a subtype of implemented exceptions
    */
    if(mwt._e()==null){return;}
    List<P> exceptions=L(mh.exceptions().stream().map(t->t.p()));
    _Q q=new _Q(true,p,G.of(mh),L(),exceptions,mh.t().p());
    mwt._e().visitable().accept(q);
    //Q |= e :_ <=fwd% MWT.T.mdf//TODO: add the mdf TS
    }
  public static void coherent(Program p){}
}

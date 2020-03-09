package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.EX;
import is.L42.generated.Core.L;
import is.L42.generated.Core.T;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.typeSystem.Coherence;

public class Wither {
  MetaError err;
  Pos pos;
  public Core.L wither(Program p,List<C> cs,Function<L42£LazyMsg,L42Any>wrap,String immK){
    err=new MetaError(wrap);
    if(cs.isEmpty()){return wither(p,wrap,immK);}
    var pIn=p.navigate(cs);
    var l=wither(pIn,wrap,immK);
    pIn=pIn.update(l,false);
    return pIn._ofCore(P.of(cs.size(),L()));
    }
  public Core.L wither(Program p,Function<L42£LazyMsg,L42Any>wrap,String immK){
    var l=p.topCore();
    if(l.info().close()){err.throwErr(l,"Class is already close");}
    try{S.parse(immK+"()");}
    catch(EndError ee){err.throwErr(l,"invalid provided constructor name: "+immK);}
    List<MWT> candidateKs=L(l.mwts().stream().filter(m->m._e()==null &&!m.key().hasUniqueNum() && m.key().m().equals(immK)));
    if(candidateKs.isEmpty()){err.throwErr(l,"provided constructor name: "+immK+" is not present in the classs");}
    if(candidateKs.size()!=1){err.throwErr(l,"provided constructor name "+immK+" is ambiguos:"+candidateKs);}
    MWT k=candidateKs.get(0);
    pos=k.poss().get(0);
    List<MWT> newMWT=L(c->{
      //may have to transform them while inserting them, to match the multi-withers
      c.addAll(l.mwts());
      for(int i:range(k.key().xs())){witherX(i,k,c);}
      });
    return l.withMwts(newMWT);
    }
  public void witherX(int i, MWT k,ArrayList<MWT>c){
    X x=k.key().xs().get(i);
    T t=k.mh().pars().get(i);
    S s=new S("with",L(x),-1);
    if(_elem(c,s)!=null){throw todo();}//do sum
    MH mh=new MH(Mdf.Immutable,L(),P.coreThis0,s,L(t),L());
    List<Core.E>es=L(k.key().xs(),(ci,xi)->ci.add(kPar(xi,x)));
    Core.E body=Utils.ThisCall(pos, k.key(), es);
    MWT mwt=new MWT(k.poss(),L(),mh,"",body);
    c.add(mwt);
    }
  public Core.E kPar(X xi,X x){
    if(xi.equals(x)){return new Core.EX(pos,x);}
    return Utils.thisCall(pos, new S(xi.inner(), L(),-1),L());
    }
  }
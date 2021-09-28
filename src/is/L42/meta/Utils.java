package is.L42.meta;

import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.tools.General;

public class Utils {
  public static Core.PCastT This0=new Core.PCastT(null,P.pThis0,P.coreThis0.withMdf(Mdf.Class));
  public static Core.EX this0=new Core.EX(null,X.thisX);
  public static Core.MCall ThisCall(Pos pos,S s,List<Core.E>es){
    return new Core.MCall(pos,This0.withPos(pos), s, es);
    }
  public static Core.MCall thisCall(Pos pos,S s,List<Core.E>es){
    return new Core.MCall(pos,this0.withPos(pos), s, es);
    }
  public static boolean match(Program p,MetaError err,String target,MWT forErr,Doc d){
    if(d._pathSel()==null){return false;}
    CoreL ld=p._ofCore(d._pathSel().p());
    if(ld==null){
      err.throwErr(forErr,"annotation "+d._pathSel().p()+" not existent");}
    for(var di:ld.docs()){
      if(di.texts().size()!=1){return false;}
      if(di.texts().get(0).equals(target)){return true;}
      }
    return false;
    }
  public static boolean match(Program p,MetaError err,String target,MWT m){
    for(var d:m.docs()){if(match(p,err,target,m,d)){return true;}}
    return false;
    }
  public static boolean equalT(T t1, T t2){
    return t1.p().equals(t2.p()) && t1.mdf().equals(t2.mdf());
    }
  public static MWT accDoc(MWT a,MWT b){
    var totDoc=mergeU(a.docs(),b.docs());
    var mhDoc=mergeU(a.mh().docs(),b.mh().docs());
    var tDoc=mergeU(a.mh().t().docs(),b.mh().t().docs());
    General.Consumer3<ArrayList<T>,T,T> merger=(c,ta,tb)->c.add(ta.withDocs(mergeU(ta.docs(),tb.docs())));
    List<T> ts=L(a.mh().pars(),b.mh().pars(),merger);
    List<T> excs=L(a.mh().exceptions(),b.mh().exceptions(),merger);
    MH mh=new MH(a.mh().mdf(),mhDoc,a.mh().t().withDocs(tDoc),a.key(),ts,excs);
    List<Pos> pos=mergeU(a.poss(),b.poss());
    return a.withPoss(pos).withDocs(totDoc).withMh(mh);
    }
  public static boolean equalMH(MH mh1, MH mh2) {
    if(!equalT(mh1.t(),mh2.t()) || mh1.mdf()!=mh2.mdf() 
      ||mh1.pars().size()!=mh2.pars().size()
      ||mh1.exceptions().size()!=mh2.exceptions().size()
      ){return false;}
    for(int i:range(mh1.pars())){
      if(!equalT(mh1.pars().get(i),mh2.pars().get(i))){return false;}
      }
    for(int i:range(mh1.exceptions())){
      if(!equalT(mh1.exceptions().get(i),mh2.exceptions().get(i))){return false;}
      }
    return true;
    }
  }
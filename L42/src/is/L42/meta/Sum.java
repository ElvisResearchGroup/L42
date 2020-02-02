package is.L42.meta;

import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.function.Function;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.T;
import is.L42.generated.Core.Doc;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.General;
import is.L42.top.Top;

public class Sum {
  private static final Program emptyP=Program.flat(Program.emptyL);
  public static Core.L compose(Program p,C c,Core.L l1, Core.L l2){
    Plus plus=new Plus(p,c,l1,l2,L());
    Core.L l3=plus.plus(l1, l2);
    List<List<C>> growingInterfaces=L(c0->l1.visitInnerLNoPrivate((li,csi)->{
      var lj=l2._cs(csi);
      if(lj==null){return;}
      if(!li.isInterface() && !lj.isInterface()){return;}//line not needed but boost efficiency
      if(!moreThen(csi,l1,l2,li,lj) && !moreThen(csi,l2,l1,lj,li)){return;}
      c0.add(csi);
      })); 
    Core.L[] l={l3};
    l3.visitInnerLNoPrivate((li,csi)->{
      List<P> p0n=L(li.ts(),(c0,ti)->{
        if(!ti.p().isNCs()){return;}
        P pi=emptyP.from(ti.p().toNCs(),csi);
        if(!growingInterfaces.contains(pi)){return;}
        c0.add(pi);
        });
      l[0]=plus.squareAdd(csi, p0n);
      });
    for(var cs:allHiddenSupertypes(l[0])){
      if(!growingInterfaces.contains(cs)){continue;}
      throw todo();
      }
    return l[0];
    }
  public static List<List<C>> allProp(Core.L l,Function<Info,List<P.NCs>> f){return L(c->{
    l.visitInnerLNoPrivate((li,csi)->{
      for(var w:f.apply(li.info())){
        var pi=emptyP.from(w,csi).toNCs();
        assert pi.n()==0;
        c.add(pi.cs());
        }
      });
    });}
  public static List<List<C>> allWatched(Core.L l){return allProp(l,i->i.watched());}
  public static List<List<C>> allRequiredCoherent(Core.L l){return allProp(l,i->i.coherentDep());}
  public static List<List<C>> allHiddenSupertypes(Core.L l){return allProp(l,i->i.hiddenSupertypes());}
  public static boolean moreThen(List<C> cs,Core.L l1,Core.L l2,Core.L li,Core.L lj){return false;}
  public static boolean implemented(Program p,Core.L l,List<C> cs){return false;}
}

class Plus{
  public Plus(Program pOut,C c, Core.L topLeft, Core.L topRight, List<C> cs) {
    this.pOut=pOut;
    this.c=c;
    this.topLeft=topLeft;
    this.topRight=topRight;
    this.cs=cs;
    }
  Program pOut;
  C c;
  Core.L topLeft;
  Core.L topRight;
  List<C> cs;
  Plus addC(C c){return new Plus(pOut,c,topLeft,topRight,pushL(cs, c));}
  
  Core.L plus(Core.L l1,Core.L l2){
    boolean isInterface3=plusInterface(l1.isInterface(),l2.isInterface());
    List<T> ts3=mergeU(l1.ts(),l2.ts());
    //ldoms
    //List<MWT> mwts=sumMWTs(a.mwts(),b.mwts());
    //List<NC> ncs=sumNCs(a.ncs(),b.ncs());
    Info info=Top.sumInfo(l1.info(),l2.info());
    //but if one header is made interface the watched and the coherentDep from that side are discarded
    List<Pos> pos=mergeU(l1.poss(),l2.poss());
    List<Doc> docs=mergeU(l2.docs(),l1.docs());
    return null;
    //return new Core.L(pos, interf, ts, mwts, ncs, info, docs);
    }
  Core.L.NC plus(Core.L.NC nc1,Core.L.NC nc2){return null;}
  IMWT plus(IMWT imwt1,IMWT imwt2){return null;}
  List<Core.L.NC> plusNCs(List<Core.L.NC> nc1,List<Core.L.NC> nc2){return null;}
  List<IMWT> plusIMWTs(List<IMWT> imwt1,List<IMWT> imwt2){return null;}
  boolean plusInterface(boolean interface1,boolean interface2){return false;}
  Core.L squareAdd(List<C> cs, List<P> pz){return null;}
  }
class IMWT{
  final boolean isInterface;
  final Core.L.MWT mwt;
  IMWT(boolean isInterface,Core.L.MWT mwt){this.isInterface=isInterface;this.mwt=mwt;}
  public static List<IMWT> of(boolean isInterface,List<Core.L.MWT> mwts){
    return L(mwts,(c,m)->c.add(new IMWT(isInterface,m)));
    }
  }
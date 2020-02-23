package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.MH;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.tools.General;
import is.L42.top.Top;

public class Sum {
  static final Program emptyP=Program.flat(Program.emptyL);
  MetaError errC;
  MetaError errM;
  LinkedHashMap<List<C>,LinkedHashSet<List<C>>> map=new LinkedHashMap<>();
  List<List<C>> allWatchedRight;
  List<List<C>> allRequiredCoherentRight;
  List<List<C>> allHiddenSupertypesRight;
  List<List<C>> allWatchedLeft;
  List<List<C>> allRequiredCoherentLeft;
  List<List<C>> allHiddenSupertypesLeft;
  Core.L topLeft;
  Core.L topRight;

  public Core.L compose(Core.L l1, Core.L l2,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    errC=new MetaError(wrapC);
    errM=new MetaError(wrapM);
    topLeft=l1;
    topRight=l2;
    allHiddenSupertypesLeft=_allHiddenSupertypes(l1);
    allHiddenSupertypesRight=_allHiddenSupertypes(l2);
    singleMap(topLeft,topRight);
    transitiveMap();
    Plus plus=new Plus(L());
    Core.L l=plus.plus(l1, l2);
    for(var cs:allHiddenSupertypesLeft){growHiddenError(l1,l2,cs);}
    for(var cs:allHiddenSupertypesRight){growHiddenError(l2,l1,cs);}
    return l;
    }
  private static void growHiddenError(Core.L l1, Core.L l2, List<C> cs) {
    var l2cs=l2._cs(cs);
    if(l2cs==null){return;}
    if(moreThen(l2cs,l1.cs(cs))){todo();}//use err.throwErr
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
  private static List<List<C>> _allWatched(Core.L l){return allProp(l,i->i.watched());}
  private static List<List<C>> _allRequiredCoherent(Core.L l){return allProp(l,i->i.coherentDep());}
  private static List<List<C>> _allHiddenSupertypes(Core.L l){return allProp(l,i->i.hiddenSupertypes());}
  public static boolean moreThen(Core.L l1,Core.L l2){throw todo();}
  public static boolean implemented(Core.L l,List<C> cs){
    boolean[]wasIn={false};
    l.visitInnerLNoPrivate((li,csi)->{//could short circut to be faster
      for(T ti:li.ts()){
        var _cs=_publicCsOfP(ti.p(), csi);
        if(cs.equals(_cs)){
          wasIn[0]=true;}
        }
      for(var pi:li.info().hiddenSupertypes()){
        P.NCs pj=Sum.emptyP.from(pi,csi);
        if(pj.n()!=0){continue;}
        assert pj.cs().stream().noneMatch(c->c.hasUniqueNum());
        if(cs.equals(pj.cs())){
          wasIn[0]=true;}
        }
      });
    return wasIn[0];
    }
  public void singleMap(Core.L l1,Core.L l2){
    l1.visitInnerLNoPrivate((li,csi)->singleMapOne(li,l1,l2,csi));
    l2.visitInnerLNoPrivate((li,csi)->singleMapOne(li,l2,l1,csi));
    }
  void transitiveMap(){for(var e:map.values()){fixPoint(e);}}
  private void fixPoint(LinkedHashSet<List<C>> e) {
    int size=e.size();
    for(var cs:new ArrayList<>(e)){
      var mapped=map.get(cs);
      if(mapped!=null){e.addAll(mapped);}
      }
    if(size!=e.size()){fixPoint(e);}
    }
  public static List<C> _publicCsOfP(P p,List<C> cs){
    if(!p.isNCs()){return null;}//not the place to give error for implements Void
    P.NCs pi=Sum.emptyP.from(p.toNCs(),cs);
    if(pi.n()!=0){return null;}
    if(pi.cs().stream().anyMatch(c->c.hasUniqueNum())){return null;}
    return pi.cs();
    }
  public void singleMapOne(Core.L lInner,Core.L lTopThis,Core.L lTopOther,List<C>cs){
    if(lTopOther._cs(cs)!=null){return;}
    assert !map.containsKey(cs);
    LinkedHashSet<List<C>> res=new LinkedHashSet<>();
    for(T t:lInner.ts()){
      var cst=_publicCsOfP(t.p(),cs);
      if(cst==null){continue;}
      Core.L liCs=lTopThis._cs(cst);
      if(liCs==null){continue;}
      Core.L ljCs=lTopOther._cs(cst);
      if(ljCs==null){continue;}
      if(Sum.moreThen(ljCs,liCs)){res.add(cst);}
      }
    if(!res.isEmpty()){map.put(cs, res);}
    }  
  static boolean loseSafe(Core.L l,List<C> cs,Core.L lCs,S s,MH mh){
    //lCs=l(cs)
    if(!lCs.info().refined().contains(s)){return false;}
    for(T t:lCs.ts()){
      var cst=_publicCsOfP(t.p(),cs);
      if(cst==null){continue;}
      var l1=l._cs(cst);
      if(l1==null){continue;}
      var e=_elem(l1.mwts(),s);
      if(e==null){continue;}
      if(Utils.equalMH(e.mh(),mh)){return true;}
      }
    return false;
    }    
  class Plus{
    public Plus(Plus other, C c) {this.cs=pushL(other.cs, c);}
    public Plus(List<C> cs) {this.cs=cs;}
    List<C> cs;
    Plus addC(C c){return new Plus(this,c);}
    Core.L plus(Core.L l1,Core.L l2){
      boolean isInterface3=plusInterface(l1.isInterface(),l2.isInterface());
      List<T> ts3=mergeU(l1.ts(),l2.ts());
      var mwts1=l1.mwts();
      var mwts2=l2.mwts();
      var ncs1=l1.ncs();
      var ncs2=l2.ncs();
      if(l1.isInterface()!=l2.isInterface()){
        if(l1.isInterface()){
          mwts2=L(mwts2.stream().filter(m->!m.key().hasUniqueNum()));
          ncs2=L(ncs2.stream().filter(m->!m.key().hasUniqueNum()));
          }
        else{
          mwts1=L(mwts1.stream().filter(m->!m.key().hasUniqueNum()));
          ncs1=L(ncs1.stream().filter(m->!m.key().hasUniqueNum()));        
          }
        }
      boolean i1=Sum.implemented(topLeft,cs);
      boolean i2=Sum.implemented(topRight,cs);
      ArrayList<IMWT> imwts=new ArrayList<>();
      for(var m:mwts1){imwts.add(new IMWT(i1,m));}
      for(var m:mwts2){imwts.add(new IMWT(i2,m));}
      var mapped=map.get(cs);
      if(mapped!=null){for(var csi:mapped){
        var left=topLeft._cs(csi);
        if(left!=null){for(var m:left.mwts()){imwts.add(new IMWT(false,m));}}
        var right=topRight._cs(csi);
        if(right!=null){for(var m:right.mwts()){imwts.add(new IMWT(false,m));}}
        }}
      List<MWT>mwts3=plusIMWTs(imwts,l1,l2);
      List<NC> ncs3=plusNCs(ncs1, ncs2);
      List<Doc> doc3=mergeU(l1.docs(),l2.docs());
      Info info3=Top.sumInfo(l1.info(),l2.info());
      //TODO: but if one header is made interface the watched and the coherentDep from that side are discarded
      List<Pos> pos=mergeU(l1.poss(),l2.poss());
      return new Core.L(pos, isInterface3, ts3, mwts3, ncs3, info3, doc3);
      }
    void addAllFromCsi(Core.L top,List<C> csi,ArrayList<IMWT> imwts){
       var in=top._cs(csi);
       if(in==null){return;}
       for(var m:in.mwts()){
         MH mh=m.mh().withDocs(L());
         mh.withT(mh.t().withDocs(L()));
         mh.withPars(L(mh.pars(),t->t.withDocs(L())));
         mh.withExceptions(L(mh.exceptions(),t->t.withDocs(L())));
         m=m.withDocs(L()).withMh(mh);
         imwts.add(new IMWT(false,m));
         }
       }
    NC plus(NC nc1,NC nc2){
      assert nc1.key().equals(nc2.key());
      assert !nc1.key().hasUniqueNum();
      return new NC(
        mergeU(nc1.poss(),nc2.poss()),
        mergeU(nc1.docs(),nc2.docs()),
        nc1.key(),
        this.addC(nc1.key()).plus(nc1.l(),nc2.l())
        );
      }
    IMWT plus(IMWT imwt1,IMWT imwt2,Core.L l1,Core.L l2){
      boolean eqMH=Utils.equalMH(imwt1.mwt.mh(),imwt2.mwt.mh());
      boolean abs1=imwt1.mwt._e()==null;
      boolean abs2=imwt2.mwt._e()==null;
      boolean oneInterf=imwt1.isInterface || imwt2.isInterface;
      if(eqMH && abs1 && abs2){return new IMWT(oneInterf,accDoc(imwt1.mwt,imwt2.mwt));} 
      if(!abs1 && !abs2){throw todo();}//sum conflicting impl
      if(eqMH && /*abs1 &&*/ abs2){return new IMWT(oneInterf,accDoc(imwt1.mwt,imwt2.mwt));} 
      if(eqMH && abs1){return new IMWT(oneInterf,accDoc(imwt1.mwt,imwt2.mwt).with_e(imwt2.mwt._e()));} 
      boolean loseSafeLeftiIs1=loseSafe(topLeft,cs,l1,imwt1.mwt.key(),imwt2.mwt.mh());
      boolean loseSafeRightiIs1=loseSafe(topRight,cs,l2,imwt1.mwt.key(),imwt2.mwt.mh());
      boolean loseSafeLeftiIs2=loseSafe(topLeft,cs,l1,imwt2.mwt.key(),imwt1.mwt.mh());
      boolean loseSafeRightiIs2=loseSafe(topRight,cs,l2,imwt2.mwt.key(),imwt1.mwt.mh());
      if(!abs1){return loseSafeUniqueRes(imwt1,imwt2,
        loseSafeLeftiIs1 || loseSafeRightiIs1,!loseSafeLeftiIs2 &&!loseSafeRightiIs2);}//i=1
      if(!abs2){return loseSafeUniqueRes(imwt2,imwt1,
        loseSafeLeftiIs2 || loseSafeRightiIs2,!loseSafeLeftiIs1 &&!loseSafeRightiIs1);}//i=2
      assert abs1 && abs2;
      if(imwt1.isInterface && imwt2.isInterface){throw todo();}
      if(imwt1.isInterface){return loseSafeUniqueRes(imwt1,imwt2,
        loseSafeLeftiIs1 || loseSafeRightiIs1,!loseSafeLeftiIs2 &&!loseSafeRightiIs2);}//i=1
      if(imwt2.isInterface){return loseSafeUniqueRes(imwt2,imwt1,
        loseSafeLeftiIs2 || loseSafeRightiIs2,!loseSafeLeftiIs1 &&!loseSafeRightiIs1);}//i=2
      boolean iIs1=loseSafeUnique(imwt1,imwt2.mwt.mh(),
        loseSafeLeftiIs1 || loseSafeRightiIs1,!loseSafeLeftiIs2 &&!loseSafeRightiIs2);
      boolean iIs2=loseSafeUnique(imwt2,imwt1.mwt.mh(),
        loseSafeLeftiIs2 || loseSafeRightiIs2,!loseSafeLeftiIs1 &&!loseSafeRightiIs1);
      assert !(iIs1 && iIs2);
      if(iIs1){return new IMWT(oneInterf,accDoc(imwt1.mwt,imwt2.mwt));}
      if(iIs2){return new IMWT(oneInterf,accDoc(imwt2.mwt,imwt1.mwt));}
      assert !iIs1 && !iIs2;
      if(loseSafeLeftiIs1 || loseSafeRightiIs1 || loseSafeLeftiIs2 || loseSafeRightiIs2){
        throw todo();//inconsistent refinement between the sum arguments mwt1,mwt2
        }
      throw todo();//no pre accepted refinement available
      }
    IMWT loseSafeUniqueRes(IMWT imwt,IMWT imwtLose,boolean canWin,boolean otherCanNot){
      MH mh=imwtLose.mwt.mh();
      var ok=loseSafeUnique(imwt,mh,canWin,otherCanNot);
      if(!ok){throw todo();}
      return new IMWT(imwt.isInterface||imwtLose.isInterface,accDoc(imwt.mwt,imwtLose.mwt));
      }
    boolean loseSafeUnique(IMWT imwt,MH mh,boolean canWin,boolean otherCanNot){
      if(!canWin){return false;}
      if(imwt.mwt._e()!=null || imwt.isInterface){return true;}
      return otherCanNot;
      }
    MWT accDoc(MWT a,MWT b){
      var totDoc=mergeU(a.docs(),b.docs());
      var mhDoc=mergeU(a.mh().docs(),b.mh().docs());
      var tDoc=merge(a.mh().t().docs(),b.mh().t().docs());
      General.Consumer3<ArrayList<T>,T,T> merger=(c,ta,tb)->c.add(ta.withDocs(mergeU(ta.docs(),tb.docs())));
      List<T> ts=L(a.mh().pars(),b.mh().pars(),merger);
      List<T> excs=L(a.mh().exceptions(),b.mh().exceptions(),merger);
      MH mh=new MH(a.mh().mdf(),mhDoc,a.mh().t().withDocs(tDoc),a.key(),ts,excs);
      return a.withDocs(totDoc).withMh(mh);
      }
    List<NC> plusNCs(List<NC> a,List<NC> b){
      return L(c->{
        for(var mi:a){
          var other=_elem(b,mi.key());
          if(other==null){c.add(mi);}
          else{c.add(plus(mi,other));}
          }
        for(var mi:b){
          var other=_elem(a,mi.key());
          if(other==null){c.add(mi);}
          }
        });    
      }
    List<MWT> plusIMWTs(ArrayList<IMWT> that,Core.L l1,Core.L l2){return L(c->plusIMWTs(c,that,l1,l2));}
    void plusIMWTs(ArrayList<MWT> c,ArrayList<IMWT> that,Core.L l1,Core.L l2){
      //would be more efficient to start from the end and accumulate the sum in the center...
      //but would mess up the ordering
      while(!that.isEmpty()){
        IMWT current=that.get(0);
        S key=current.mwt.key();
        int index=1;
        IMWT inIndex=null;
        for(;index<that.size();index+=1){
          inIndex=that.get(index);
          if(inIndex.mwt.key().equals(key)){break;}
          }//index is now the position of the equal key
        if(index!=that.size()){
          that.set(0,plus(current,inIndex,l1,l2));
          that.remove(index);
          continue;
          }
        that.remove(0);
        c.add(current.mwt);
        }
      }
    boolean plusInterface(boolean interface1,boolean interface2){
      if(interface1==interface2){
        var leftClose=topLeft.cs(cs).info().close();
        var rightClose=topRight.cs(cs).info().close();
        if(!leftClose || !rightClose){return interface1;}
        throw todo();
        }
      if(!interface1 && interface2){
        throw todo();
        }
      throw todo();
      }
    }
  }  
class IMWT{
  final boolean isInterface;
  final Core.L.MWT mwt;
  IMWT(boolean isInterface,Core.L.MWT mwt){this.isInterface=isInterface;this.mwt=mwt;}
  public static List<IMWT> of(boolean isInterface,List<Core.L.MWT> mwts){
    return L(mwts,(c,m)->c.add(new IMWT(isInterface,m)));
    }
  }
//TODO: extract examples for testing from below
    //todo: can we be sure to chose one of the two now?
    //in case of ill formed a,b can be unclear: 
    //{I1={method A m()} I2={[I1] method B m()} I3={[I2,I1] method A m()} }
    // +
    //{                   I2={ method A m()}    I3={method B m()} }
    //=???
    //{I1={method A m()} I2={[I1] method B m()} I3={[I2,I1] method ?? m()} }
    /*
    
    In a program where A,B are still to be resolved with metaprogramming, what should the following sum produce?
{I1={interface method Any m()} I2={interface [I1] method B m()} I3={[I2] method A m()} } //first library
 +
{I0={interface method A m()}     I2={interface method A m()}        I3={[I0] method B m()} }//second library
=
{I1={interface method A m()} I2={interface [I1] method B m()} I3={[I2,I1] method ?? m()} }Should the ?? be A or B? both are "ok" decisions...in particular, if the first library is to be correct, then A,B will resolve so that A>
    
What happens in this example? is m() coming from two different interfaces now?

    { A={interface method Void m()} C={[A] method Void m()}}
    +
    { B={interface method Void m()} C={[B] method Void m()}}
    We need to add to L(Cs).Ts also map(Cs) AND
    add the new Info.refined and
    check that the new refined shape is still valid
    This is also a new point where commutative/associative may fail,
    since if I add { I={interface method Void m()} A={[I]} B={[I]}}
    before the sum, than there would be no problem
    
    */
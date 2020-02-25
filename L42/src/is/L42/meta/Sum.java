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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;

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
    l2=normalizePrivates(l2,otherNs(l1));
    topLeft=l1;
    topRight=l2;
    allHiddenSupertypesLeft=_allHiddenSupertypes(l1);
    allHiddenSupertypesRight=_allHiddenSupertypes(l2);
    allWatchedLeft=_allWatched(l1);
    allWatchedRight=_allWatched(l2);
    allRequiredCoherentLeft=_allRequiredCoherent(l1);
    allRequiredCoherentRight=_allRequiredCoherent(l2);
    singleMap(topLeft,topRight);
    transitiveMap();
    for(var cs:allHiddenSupertypesLeft){growHiddenError(l1,l2,cs);}
    for(var cs:allHiddenSupertypesRight){growHiddenError(l2,l1,cs);}
    Plus plus=new Plus(L());
    Core.L l=plus.plus(l1, l2);
    wellFormedRefine(l);
    return l;
    }
  private static HashSet<Integer> otherNs(Core.L other){
    return new Accumulate<HashSet<Integer>>(){
      @Override public HashSet<Integer>empty(){return new HashSet<>();}
      @Override public void visitMWT(MWT mwt){
        super.visitMWT(mwt);
        if(mwt.key().hasUniqueNum()){acc().add(mwt.key().uniqueNum());}
        }
      @Override public void visitNC(NC nc){
        super.visitNC(nc);
        if(nc.key().hasUniqueNum()){acc().add(nc.key().uniqueNum());}
        }
      }.of(other);
    }
  private static Core.L normalizePrivates(Core.L l,HashSet<Integer>otherNs) {
    HashMap<Integer,Integer>next=new HashMap<>();
    for(int i:otherNs){
      int nextI=i+1;
      while(otherNs.contains(nextI)){nextI+=1;}
      next.put(i, nextI);
      }
    return l.accept(new CloneVisitor(){
      @Override public C visitC(C c){
        if(!c.hasUniqueNum()){return c;}
        Integer n=next.get(c.uniqueNum());
        if(n==null){return c;}
        return c.withUniqueNum(n);
        }
      @Override public S visitS(S s){
        if(!s.hasUniqueNum()){return s;}
        Integer n=next.get(s.uniqueNum());
        if(n==null){return s;}
        return s.withUniqueNum(n);
        }
      });
    }
  private void wellFormedRefine(Core.L l){
    l.visitInnerLNoPrivate((li,csi)->{
      if(li.ts().size()<=1){return;}
      HashMap<S,P>nonRefined=new HashMap<>();
      for(T t:li.ts()){
        var csj=_publicCsOfP(t.p(), csi);
        if(csj==null){continue;}
        var lj=l.cs(csj);
        for(var m:lj.mwts()){
          if(lj.info().refined().contains(m.key())){continue;}
          var wrong=nonRefined.get(m.key());
          if(wrong==null){nonRefined.put(m.key(),t.p());continue;}
          errC.throwErr(csi,li,"No unique source for "+m.key()+"; it originates from both "+t.p()+" and "+wrong);
          }
        }
      });
    }
  private void growHiddenError(Core.L l1, Core.L l2, List<C> cs) {
    var l2cs=l2._cs(cs);
    if(l2cs==null){return;}
    var l1cs=l1.cs(cs);
    if(moreThen(l2cs,l1cs)){
      errC.throwErr(cs,l1cs,"This interface is privately implemented "
        +" but the summed version is larger: "+errC.intro(l2cs,false).stripTrailing());
      }
    }
  
  public static List<List<C>> allProp(Core.L l,Function<Info,List<P.NCs>> f){return L(c->{
    l.visitInnerLNoPrivate((li,csi)->{
      for(var w:f.apply(li.info())){
        var cs=_publicCsOfP(w, csi);
        if(cs!=null){c.add(cs);}
        }
      });
    });}
  private static List<List<C>> _allWatched(Core.L l){return allProp(l,i->i.watched());}
  private static List<List<C>> _allRequiredCoherent(Core.L l){return allProp(l,i->{
    var all=new ArrayList<>(i.coherentDep());
    all.addAll(i.metaCoherentDep());
    return all;
    });}
  private static List<List<C>> _allHiddenSupertypes(Core.L l){return allProp(l,i->i.hiddenSupertypes());}
  public static boolean moreThen(Core.L l1,Core.L l2){
    if(!l2.isInterface()){return false;}
    for(T t1:l1.ts()){
      if(l2.ts().stream().noneMatch(t2->t2.p().equals(t1.p()))){return true;}
      }
    for(MWT m1:l1.mwts()){
      if(l2.mwts().stream().noneMatch(m2->m2.key().equals(m1.key()))){return true;}
      }
    return false;
    }
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
    //NO if(lTopOther._cs(cs)!=null){return;}
    LinkedHashSet<List<C>> res=map.get(cs);
    if(res==null){
      res=new LinkedHashSet<>();
      map.put(cs, res);
      }
    for(T t:lInner.ts()){
      var cst=_publicCsOfP(t.p(),cs);
      if(cst==null){continue;}
      Core.L liCs=lTopThis._cs(cst);
      if(liCs==null){continue;}
      Core.L ljCs=lTopOther._cs(cst);
      if(ljCs==null){continue;}
      if(Sum.moreThen(ljCs,liCs)){res.add(cst);}//we could cache growing interfaces
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
      boolean isInterface3=plusInterface(l1.isInterface(),l2.isInterface(),l1,l2);
      ArrayList<T> ts3=new ArrayList<>(l1.ts());
      plusEqualTs(ts3,l2.ts());
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
      Info info1=l1.info();
      Info info2=l2.info();
      if(isInterface3&&!l1.isInterface()){info1=info1.withWatched(L()).withCoherentDep(L());}
      if(isInterface3&&!l2.isInterface()){info2=info2.withWatched(L()).withCoherentDep(L());}
      Info info3=Top.sumInfo(info1,info2);
      ArrayList<P.NCs> typeDep=new ArrayList<>(info3.typeDep());
      var mapped=map.get(cs);
      if(mapped!=null){for(var csi:mapped){
        var left=topLeft._cs(csi);
        if(left!=null){
          for(var m:left.mwts()){imwts.add(new IMWT(false,m));}
          plusEqualTs(ts3,left.ts());
          paths(typeDep,left);
          }
        var right=topRight._cs(csi);
        if(right!=null){
          for(var m:right.mwts()){imwts.add(new IMWT(false,m));}
          plusEqualTs(ts3,right.ts());
          paths(typeDep,right);
          }
        }}
      info3=info3.withTypeDep(typeDep);
      List<MWT>mwts3=plusIMWTs(imwts,l1,l2);
      List<NC> ncs3=plusNCs(ncs1, ncs2);
      List<Doc> doc3=mergeU(l1.docs(),l2.docs());
      List<Pos> pos=mergeU(l1.poss(),l2.poss());
      return new Core.L(pos, isInterface3, L(ts3.stream()), mwts3, ncs3, info3, doc3);
      }
    private void plusEqualTs(ArrayList<T> ts3, List<T> ts){
      for(T t:ts){if(ts3.stream().noneMatch(t3->t3.p().equals(t.p()))){ts3.add(t);}}
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
      if(!abs1 && !abs2){errM.throwErr(imwt1.mwt,"Conflicting implementation: the method is implemented on both side of the sum");}
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
      if(imwt1.isInterface && imwt2.isInterface){
        errM.throwErr(imwt1.mwt,"Both versions of this method are implemented, but the other have a different header:\n"+errM.intro(imwt2.mwt,false).stripTrailing());
        }
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
        errM.throwErr(imwt1.mwt,"The other method have a different signature:\n"
          +errM.intro(imwt2.mwt,false)+"But there is ambiguous refinement between those two signatures");
        }
      throw errM.throwErr(imwt1.mwt,"The other method have a different signature:\n"
        +errM.intro(imwt2.mwt,false)+"But there is no local refinement between those two signatures");
      }
    IMWT loseSafeUniqueRes(IMWT imwt,IMWT imwtLose,boolean canWin,boolean otherCanNot){
      MH mh=imwtLose.mwt.mh();
      var ok=loseSafeUnique(imwt,mh,canWin,otherCanNot);
      if(!ok){
        errM.throwErr(imwt.mwt,"The other method have a different signature:\n"
        +errM.intro(imwtLose.mwt,false)+"But there is no local refinement between those two signatures");
        }
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
          if(other==null){
            c.add(mi.withL(addC(mi.key()).plusOnlyMap(topLeft,mi.l())));}
          else{c.add(plus(mi,other));}
          }
        for(var mi:b){
          var other=_elem(a,mi.key());
          if(other==null){c.add(mi.withL(addC(mi.key()).plusOnlyMap(topRight,mi.l())));}
          }
        });    
      }
    private Core.L plusOnlyMap(Core.L top, Core.L l) {
      var mapped=map.get(cs);
      if(mapped==null){return l;}
      ArrayList<T> ts=new ArrayList<>(l.ts());
      boolean i=Sum.implemented(top,cs);
      ArrayList<IMWT> imwts=new ArrayList<>();
      for(var m:l.mwts()){imwts.add(new IMWT(i,m));}
      ArrayList<P.NCs> typeDep=new ArrayList<>(l.info().typeDep());
      for(var csi:mapped){
        var left=topLeft._cs(csi);
        var right=topRight._cs(csi);
        if(left!=null){
          plusEqualTs(ts,left.ts());
          for(var m:left.mwts()){imwts.add(new IMWT(false,m));}
          paths(typeDep,left);
          }
        if(right!=null){
          plusEqualTs(ts,right.ts());
          for(var m:right.mwts()){imwts.add(new IMWT(false,m));}
          paths(typeDep,right);
          }
        }
      List<MWT>mwts=plusIMWTs(imwts,l,l);
      Info info=l.info().withTypeDep(L(typeDep.stream()));
      return l.withTs(ts).withMwts(mwts).withInfo(info);
      }
    void paths(ArrayList<P.NCs> c,Core.L l){
      assert l.isInterface(); 
      l.withNcs(L()).accept(new Accumulate<Void>() {        
        @Override public Void empty(){return null;}
        @Override public void visitP(P p){
          if(!p.isNCs()){return;}
          if(!c.contains(p)){c.add(p.toNCs());}
          }
        @Override public void visitMWT(MWT m){
          if(!m.key().hasUniqueNum()){super.visitMWT(m);}
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
    boolean plusInterface(boolean interface1,boolean interface2,Core.L topLeftCs,Core.L topRightCs){
      if(interface1==interface2){
        var leftClose=topLeftCs.info().close();
        var rightClose=topRightCs.info().close();
        if(!leftClose || !rightClose){return interface1;}
        errC.throwErr(cs,topLeftCs,"The two nested classes are both closed, thus can not be composed.");
        }
      if(!interface1 && interface2){
        return differentInterfaces(allRequiredCoherentLeft,allWatchedLeft,topLeftCs);  
        }
      assert interface1 && !interface2;
      return differentInterfaces(allRequiredCoherentRight,allWatchedRight,topRightCs);
      }
    boolean differentInterfaces(List<List<C>> coherents,List<List<C>> watcheds, Core.L topCs){
      boolean required=coherents.contains(cs);
      if(required){
        errC.throwErr(cs, topCs,"The nested class can not be turned into an interface, since it is used with 'class' modifier (is required coherent)");
        }
      boolean watched=watcheds.contains(cs);        
      if(watched){
        errC.throwErr(cs, topCs,"The nested class can not be turned into an interface; since its privates are used by other code (is watched)");
        }
      boolean absPublic=topCs.mwts().stream().allMatch(m->m._e()==null||m.key().hasUniqueNum());
      if(!absPublic){
        errC.throwErr(cs, topCs,"The nested class can not be turned into an interface; some public methods are implemented");
        }
      return true;
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
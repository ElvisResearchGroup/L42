package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.todo;

import java.util.List;
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
  public static boolean implemented(Core.L l,List<C> cs){return false;}
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
    List<IMWT>imwts1=L(mwts1,(c,m)->c.add(new IMWT(i1,m)));
    List<IMWT>imwts2=L(mwts1,(c,m)->c.add(new IMWT(i2,m)));
    List<IMWT>imwts3=this.plusIMWTs(imwts1, imwts2);
    List<MWT>mwts3=L(imwts3,(c,m)->c.add(m.mwt));
    List<NC> ncs3=this.plusNCs(ncs1, ncs2);
    List<Doc> doc3=mergeU(l1.docs(),l2.docs());
    Info info3=Top.sumInfo(l1.info(),l2.info());
    //TODO: but if one header is made interface the watched and the coherentDep from that side are discarded
    List<Pos> pos=mergeU(l1.poss(),l2.poss());
    return new Core.L(pos, isInterface3, ts3, mwts3, ncs3, info3, doc3);
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
  IMWT plus(IMWT imwt1,IMWT imwt2){
    return null;
    }
  //if null, both win, error if both loses
  IMWT _loser(IMWT imwt1,IMWT imwt2){
    var a=imwt1.mwt.mh();
    var b=imwt2.mwt.mh();
    assert a.pars().size()==b.pars().size();
    for(var i:range(a.pars())){
      var ai=a.pars().get(i);
      var bi=b.pars().get(i);
      if(typeEq(ai,bi)){continue;}
      throw todo();
      }
    //For now, we require exceptions to be equal too.
    /*TODO: issues with exceptions: what to chose for
    [A,Any] vs [Any]?
    is  [A,Any] even allowed? should we normalize subtypes away when their arise?
    is [B,B] allowed? note it can come from a [A,B] when we rename A=>B....
    */
    if(a.exceptions().size()!=b.exceptions().size()){throw todo();}
    for(var i:range(a.exceptions())){
      var ai=a.exceptions().get(i);
      var bi=b.exceptions().get(i);
      if(ai.p().equals(bi.p())){continue;}
      throw todo();
      }
    //if same ret and same exceptions, both win
    if(typeEq(a.t(),b.t())){return null;}
    //else, find subtype ret 
    return null;   
    //and check exceptions subtype follows ret subtype
    }
  private boolean typeEq(T a, T b){return a.p().equals(b.p()) && a.mdf()==b.mdf();}
  private boolean typeSub(T subT, T superT){
    /*
     
      A->B  C->D  E
      A  B->C  D->E
      m
      
      Cs1;Pz1 .. Csn;Pzn = {Cs;collect(L1,L2,Cs) | Cs in dom(L1)U dom(L2),
        Ts=L1(Cs).Ts,L2(Cs).Ts\L1(Cs).Ts //just one side if undefined on the other side
        Cs has no unique ns and
        L1(Cs).Ts.Ps[from This0.Cs;{}] intersect Pz
        or 
        L2(Cs).Ts.Ps[from This0.Cs;{}] intersect Pz
        }
      
      
      {Exp={interface} Lit={Exp} method Lit foo()=e}
      +
      {Exp={interface[HasToS] method HasToS foo()}}
     
      for all public Cs, compute the L.Ts and put in map Cs->Ts
      then compute L3 using map, both for subtype and to save in the headers
      then do the rest of the late sum?
      Cs->Ts,mwts where L1><L2 gives the Ts and mwts that are not in the other
     
      what about Ts that do not exists?
      if super is out/sub in and super not exists, then NOPE //since in is normalized, all implemented interface exists
      if sub is out and sub not exists, then OK //the in could not be typed
      if super is in and super not exists, then NOPE //since in is normalized, all implemented interface exists
      if sub is in and sub not exists, then OK //the in could not be typed
      
      --- simpler? if subT or superT do not exists as CORE.L, then NOPE
      *both exists from now on:
      if both points outside Cs use normal p:
      min(a.n,b.n)>=size(C.Cs) => pTop.subtype(a-size(C.Cs),b-size(C.Cs))      
      if(superT.n>=size(C.Cs)){
        //subT mut be IN
        if(p(superT-size(C.Cs)) not interface) NOPE
        Ts=topLeft(C.Cs/subT).Ts U topRight(C.Cs/subT).Ts
        Ts'=fixpoint on Ts using topLeft,Right
        return superT in Ts'
        }
      if(subT.n>=size(C.Cs)){//superT must be in
        return NOPE// subT could not have been normalized, since superT is still meta
        //return C.Cs/superT in p(subT-size(C.Cs)).Ts[from subT-size(C.Cs); pOut]
        //subT.Ts is already collected since is an out CORE.L
        }
      else both inside Cs ...
    */
    return false;
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
  List<IMWT> plusIMWTs(List<IMWT> a,List<IMWT> b){
    return L(c->{
      for(var mi:a){
        var other=b.stream()
          .filter(mj->mj.mwt.key().equals(mi.mwt.key()))
          .reduce(toOneOr(()->bug()));
        if(other.isEmpty()){c.add(mi);}
        else{c.add(plus(mi,other.get()));}
        }
      for(var mi:b){
        var other=a.stream()
          .filter(mj->mj.mwt.key().equals(mi.mwt.key()))
          .reduce(toOneOr(()->bug()));
        if(other.isEmpty()){c.add(mi);}
        }
      });    
    }
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
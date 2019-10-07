package is.L42.common;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.typeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import is.L42.constraints.ToHalf;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.Psi;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.ST.STOp;
import is.L42.generated.Y;
/*

_______
#define p.solve(ST)=ST' 
* p.solve(T)=T
* p.solve(T.s)=p(T.P).mwts(s).T
* p.solve(T.s.i)=p(T.P).mwts(s).parsi
* p.solve(ST.s.i?)=p.solve(T.s.i?)
    T=p.solve(ST)
* p.solve(ST.s.i?)=ST'.s.i?
    ST'=p.solve(ST)
    ST' not of form T
* p.solve(ST.s.i?)=T.s.i?
    T=p.solve(ST)
    p(T.P).mwts(s) undefined
    or p(T.P).mwts(s).parsi undefined
NOTE: ST may contains Ps that are not in the domain of p.
should also p.minimize(P) and from(P;p) be resilient and do nothing? 

* CT well formedness: ST<=STz, ST not of form Core.T
 
* in any moment, CTz and p so that forall ST in dom or cod CTz, p.solve(ST)=ST

_______
#define CTz <+p STz<=STz' = CTz'    CTz <+p ST<=STz = CTz'
*CTz<+p STz<=STz' = CTz<+p ST1<=p.solve(STz')..<+p STn<=p.solve(STz')
   ST1..STn={ST| ST in p.solve(STz), ST not of form Core.T}

* CTz,ST<=STz <+p ST <=STz' = CTz,ST<=STz U STz'
    CTz <+p ST <=STz' = CTz,ST<= STz'
    CTz(ST) undefined
_______
#define CTz.allSTz(ST) = STz    CTz.allSubSTz(ST) = STz    CTz.allTz(p,ST) = Tz
* ST in CTz.allSTz(p,ST)
* ST' in CTz.allSTz(p,ST) 
    ST1..STn = CTz(ST)
    ST' in CTz.allSTz(p,STi)
* ST' in CTz.allSTz(p,ST.s.i?)
    ST" in CTz.allSTz(ST)
    ST' in  CTz.allSTz(p.sort(ST".s.i))
 
 Tz.allTz(p,ST)={T | ST in CTz.allST(p,ST)}

  
I(STz)=chooseGeneralT(Tz) //assert p.sort(STz)=STz
  Tz={chooseSpecificT(I.CTz.allTz(ST)) | ST in STz }
  
//what to do when the program expands?
p1=p.update(p.top with extra C=L)

p1.update(CTz): replace all ST with p1.solve(ST), if ST in dom and now is T, remove it.
 
from CTz? assert if you can not solve before from, you can not solve after  
  
inside topNC
  we use I(Half.e) to get a core.e and is the only point where
  we can get CTzs from inner FULL.Ls
  //FALSE: those new CTzs should be irrelevant for the whole duraction of topNC
  in the end of topNC, (thus for every C=e processed)
  we can update the CTz with p"
  
  
  */
public class CTz {
  private final Map<ST,List<ST>> inner=new HashMap<>();
  @Override public String toString(){
    String res=inner.toString();
    res=res.substring(1,res.length()-1);
    return res.replace("imm ","");
    }
  public boolean coherent(){
    for(var st:inner.keySet()){
      assert !(st instanceof T):st;
      }
    return true;
    }
  public boolean coherent(Program p){
    for(var e:inner.entrySet()){
      assert !(e.getKey() instanceof T):e.getKey();
      assert solve(p,e.getKey())==e.getKey():solve(p,e.getKey())+" "+e.getKey();
      for(var st:e.getValue()){
        assert solve(p,st)==st:solve(p,st)+" "+st;  
        }
      }
    return true;
    }

  public static ST solve(Program p,ST st){//can be moved in Program if it works TODO:
    if(st instanceof T){return st;}
    if(st instanceof ST.STMeth){return solve(p,(ST.STMeth)st);}
    if(st instanceof ST.STOp){return solve(p,(ST.STOp)st);}
    throw bug();
    }
  public static ST solve(Program p,ST.STMeth stsi){
    ST st=solve(p,stsi.st());
    if(!(st instanceof T) ||!((T)st).p().isNCs()){return stsi.withSt(st);}
    P.NCs p0=((T)st).p().toNCs();
    var mwt= _elem(p._ofCore(p0).mwts(),stsi.s());
    if(mwt==null){return stsi.withSt(st);}
    if(stsi.i()==-1){return p.from(mwt.mh().t(),p0);}
    if(stsi.i()>=mwt.mh().s().xs().size()){return stsi.withSt(st);}
    return p.from(mwt.mh().pars().get(stsi.i()),p0);
    }
  public static ST solve(Program p,ST.STOp st){  throw bug();    }
  
  public void plusAcc(Program p,List<ST> stz,List<ST>stz1){
    stz1=L(stz1,st->solve(p,st));
    for(ST st:stz){
      st=solve(p,st);
      if(st instanceof T){continue;}
      plusAcc(p,st,stz1);
      }
    }
  public void plusAcc(Program p,ST st,List<ST>stz1){
    var data=inner.get(st);
    if(data==null){
      inner.put(st,stz1);
      return;
      }
    inner.put(st,mergeU(data,stz1));    
    }    
  public List<ST> allSTz(ST st){
    return L(c->{
      c.add(st);
      var data=inner.get(st);
      if(data!=null){
        for(ST sti:data){c.addAll(allSTz(sti));}
        }
      c.addAll(allSubSTz(st));
      });//remove dups, visit?
      
     // T.s1 <= T.s1.s2
    }
  public List<ST> allSubSTz(ST st){
    return L(c->{
      
      });
    }
//CTz.allSTz(ST) = ST U CTz.allSTz(ST1) U..U CTz.allSTz(STn) U CTz.allSubSTz(ST) 
//  ST1..STn = CTz(ST)
//CTz.allSubSTz(T) = {}
//CTz.allSubSTz(ST.s.i?) = ST1.s.i?..STn.s.i?   
//  ST1..STn = allSTz(ST)
  
  /*
  public boolean coherent(){
    for(var e:inner.entrySet()){
      ST st=e.getKey();
      List<ST> stz=e.getValue();
      assert stz.contains(st):this;
      for(ST st1:stz){
        assert stz.containsAll(of(st1)):this;
        }
      }
      return true;
    }
  public Half.E _add(Program p, Core.MH mh, Full.E _e){
    if(_e==null){return null;}
    Y y=new Y(p,GX.of(mh),L(mh.t()),null,L(mh.t()));
    var res= new ToHalf(y,this).compute(_e);
    this.plusAccCopy(p, res.resSTz,L(mh.t()));
    assert res.retSTz.isEmpty();//may be not?
    return res.e;
    }
  public void plusAccCopy(Program p,List<ST> stz,List<ST>stz1){
    plusAcc(p,new ArrayList<>(stz),new ArrayList<>(stz1));    
    }
  public void plusAcc(Program p,ArrayList<ST> stz,ArrayList<ST>stz1){
    assert coherent();
    while(!stz.isEmpty()){
      minimize(p,stz);//TODO: check if this point of minimization follows the formalism
      minimize(p,stz1);
      var st=stz.get(0);
      stz.remove(0);
      plusAcc(p,st,stz1);
      }
    assert coherent(): this;
    }
  void plusAcc(Program p,ST st,List<ST>stz){
    assert st==minimize(p, st): st+" "+minimize(p, st);
    assert stz==minimizeFW(p, stz);
    ArrayList<ST> alreadyMapped=inner.get(st);
    ArrayList<ST>stz2=new ArrayList<>(this.of(stz));
    if(!stz2.contains(st)){stz2.add(st);}
    if(alreadyMapped!=null){
      stz2.addAll(alreadyMapped);
      inner.remove(st);
      }
    for(var stzi:inner.values()){
       if(!stzi.contains(st)){continue;}
       for(ST stj: stz2){if(!stzi.contains(stj)){stzi.add(stj);}}
       minimize(p,stzi);
       }
    inner.put(st,stz2);
    }
  public Set<ST> dom(){return inner.keySet();}
  public List<ST> of(ST st){
    var res=inner.get(st);
    if(res==null){return L(st);}
    assert res.contains(st);
    return L(inner.get(st).stream());
    }
  public List<ST> of(List<ST>stz){return L(stz,(c,sti)->c.addAll(of(sti)));}
  public List<ST> minimizeFW(Program p,List<ST>stz){return L(stz,st->minimize(p,st));}
  void minimize(Program p,ArrayList<ST>stz){
    ArrayList<T>tz=new ArrayList<>();
    for(int i=0;i<stz.size();){
      ST st=minimize(p,stz.get(i));
      stz.set(i,null);
      if(st instanceof T){tz.add((T)st);stz.remove(i);}
      else if(stz.contains(st)){stz.remove(i);}
      else{stz.set(i,st);i+=1;}
      }
    while(!tz.isEmpty()){
      T t=tz.get(tz.size()-1);
      tz.remove(tz.size()-1);
      //boolean noSub=tz.stream().noneMatch(ti->p.isSubtype(ti, t,null));
      //if(noSub){stz.add(t);}//TODO: what we gain from removing subtypes?
      stz.add(t);
      }
    }
  ST minimize(Program p,ST st){
    if(st instanceof T){return st;}
    if(st instanceof ST.STMeth){return minimize(p,(ST.STMeth)st);}
    if(st instanceof ST.STOp){return minimize(p,(ST.STOp)st);}
    throw bug();
    }
  ST minimize(Program p,ST.STMeth stsi){
    List<T> ts;
    ST st=minimize(p,stsi.st());
    if(stsi.i()==-1){
      ts=L(of(st),(c,sti)->{
        if (!(sti instanceof T)){return;}
        T ti=(T)sti;
        if(!ti.p().isNCs()){return;}
        P.NCs pi=ti.p().toNCs();
        MWT mwti=_elem(p.ofCore(pi).mwts(),stsi.s());
        if(mwti==null){return;}
        T t1=p.from(mwti.mh().t(),pi);
        boolean tI=p.ofCore(pi).isInterface();
        boolean t1I=p.ofCore(t1.p()).isInterface();
        boolean tEqSt=ti.equals(st);
        if(tI || t1I || tEqSt){c.add(t1);}
        });
      }
    else{
      ts=L(of(st),(c,sti)->{
        if (!(sti instanceof T)){return;}
        T ti=(T)sti;
        if(!ti.p().isNCs()){return;}
        P.NCs pi=ti.p().toNCs();
        MWT mwti=_elem(p.ofCore(pi).mwts(),stsi.s());
        if(mwti==null){return;}
        assert stsi.i()!=0;
        //T t1=p.from(mwti.mh().parsWithThis().get(st.i()),pi);//if assertion above is false
        T t1=p.from(mwti.mh().pars().get(stsi.i()-1),pi);
        c.add(t1);
        });
    }
    if(ts.isEmpty()){return stsi.withSt(st);}
    assert ts.stream().distinct().count()==1;
    return ts.get(0);
    }
  List<List<T>> tzsToTsz(List<List<T>> tzs){
    assert !tzs.isEmpty();
    if(tzs.size()==1){
      return L(tzs.get(0),(c,tz)->c.add(L(tz)));
      }
    var inductive=tzsToTsz(popL(tzs));
    var tz0=tzs.get(0);
    return L(tz0,(c,ti)->{for(var tz:inductive){c.add(pushL(ti,tz));}});
    }
  ST minimize(Program p,ST.STOp st){
    List<List<ST>> minStzi=L(st.stzs(),sti->minimizeFW(p,sti));
    List<List<T>> tzs=L(minStzi,(c,stzi)->c.add(typeFilter(of(stzi),T.class)));
    List<List<T>> tsz=tzsToTsz(tzs);
    Set<Psi> options=new HashSet<>();
    for(var ts:tsz){
      options.addAll(opOptions(p,st.op(),ts));
      }
    if(options.isEmpty()){return st.withStzs(minStzi);}
    if(options.size()>1){//on default, just do as isEmpty()
      return handleManyOptions(options,st.withStzs(minStzi));
      }
    assert options.size()==1;
    Psi psi=options.iterator().next(); 
    return p.from(_elem(p.ofCore(psi.p()).mwts(),psi.s()).mh().t(),psi.p());
    }

  //override it for testing
  public ST handleManyOptions(Set<Psi> options, STOp st) {return st;}
  public List<Psi> opOptions(Program p, Op op, List<T>ts){
    return L(c->{for(int i:range(ts)){
      List<P> p11n=L(range(ts),(cj,j)->{if(j!=i){cj.add(ts.get(j).p());}});
      String sName = NameMangling.methName(op,i);
      P tmp=ts.get(i).p();
      if(!tmp.isNCs()){continue;}
      P.NCs tip=tmp.toNCs();
      List<MWT> mwts=p.ofCore(tip).mwts();
      List<MH> mhs=L(mwts.stream()
        .map(m->m.mh())
        .filter(m->
          m.s().m().equals(sName) 
          && !m.s().hasUniqueNum()
          && m.s().xs().size()==ts.size()-1
          ));
      for(MH mh:mhs){
        List<P>p1n=L(mh.pars(),(ci,ti)->ci.add(p.from(ti.p(),tip)));
        assert p1n.size()==p11n.size(): p1n+" "+p11n;
        boolean acceptablePaths=IntStream.range(0,p1n.size())
          .allMatch(j->p.isSubtype(p11n.get(j),p1n.get(j),null));
        if(acceptablePaths){c.add(new Psi(tip,mh.s(),i));}
        }
      }});
    }
  */
  }
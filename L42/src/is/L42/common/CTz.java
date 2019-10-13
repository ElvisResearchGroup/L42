package is.L42.common;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

import is.L42.constraints.FreshNames;
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
import is.L42.generated.ST.STMeth;
import is.L42.generated.ST.STOp;
import is.L42.generated.Y;
import is.L42.tools.InductiveSet;
/*
    
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
  public static List<ST> solve(Program p,List<ST> stz){return L(stz,sti->solve(p,sti));}
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
    var pOfP0=p._ofCore(p0);
    if(pOfP0==null){return stsi.withSt(st);}
    var mwt= _elem(pOfP0.mwts(),stsi.s());
    if(mwt==null){return stsi.withSt(st);}
    if(stsi.i()==-1){return p.from(mwt.mh().t(),p0);}
    if(stsi.i()>mwt.mh().s().xs().size()){return stsi.withSt(st);}
    if(stsi.i()==0){return new Core.T(mwt.mh().mdf(),mwt.mh().docs(),p0);}
    return p.from(mwt.mh().pars().get(stsi.i()-1),p0);
    }
  static List<List<T>> tzsToTsz(List<List<T>> tzs){
    assert !tzs.isEmpty();
    if(tzs.size()==1){
      return L(tzs.get(0),(c,tz)->c.add(L(tz)));
      }
    var inductive=tzsToTsz(popL(tzs));
    var tz0=tzs.get(0);
    return L(tz0,(c,ti)->{for(var tz:inductive){c.add(pushL(ti,tz));}});
    }
  static public void opOptionsAcc(Program p,Op op, List<T>ts, int i,List<Psi>acc){
    List<P> p11n=L(range(ts),(cj,j)->{
      if(j!=i){cj.add(ts.get(j).p());}
      });
    String sName = NameMangling.methName(op,i);
    P tmp=ts.get(i).p();
    if(!tmp.isNCs()){return;}
    P.NCs tip=tmp.toNCs();
    List<MWT> mwts=p._ofCore(tip).mwts();
    List<MH> mhs=L(mwts.stream()
      .map(m->m.mh())
      .filter(m->
        m.s().m().equals(sName) && !m.s().hasUniqueNum() && m.s().xs().size()==ts.size()-1
        ));
    for(MH mh:mhs){
      List<P>p1n=L(mh.pars(),(ci,ti)->ci.add(p.from(ti.p(),tip)));
      assert p1n.size()==p11n.size(): p1n+" "+p11n;
      boolean acceptablePaths=IntStream.range(0,p1n.size())
       .allMatch(j->p.isSubtype(p11n.get(j),p1n.get(j),null));
      if(acceptablePaths){acc.add(new Psi(tip,mh.s(),i));}
      }  
    }
  static public List<Psi> opOptions(Program p, Op op, List<T>ts){
    assert ts.stream().noneMatch(t->t==null):ts;
    return L(c->{for(int i:range(ts)){opOptionsAcc(p,op,ts,i,c);}});
    }
  public static ST solve(Program p,ST.STOp st){
    List<List<ST>> minStzi=L(st.stzs(),stzi->solve(p,stzi));
    List<List<T>> tzs=L(minStzi,(c,stzi)->c.add(typeFilter(stzi.stream(),T.class)));
    List<List<T>> tsz=tzsToTsz(tzs);
    Set<Psi> options=new HashSet<>();
    for(var ts:tsz){
      options.addAll(opOptions(p,st.op(),ts));
      }
    if(options.size()!=1){return st.withStzs(minStzi);}
    assert options.size()==1;
    Psi psi=options.iterator().next();
    return p.from(_elem(p._ofCore(psi.p()).mwts(),psi.s()).mh().t(),psi.p());
    }  
  public void plusAcc(Program p,List<ST> stz,List<ST>stz1){
    stz1=solve(p,stz1);
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
    
    
  public InductiveSet<ST,ST> allSTz(Program p){
    return new AllSTzRule(this,p);
    }
  private static class AllSTzRule extends InductiveSet<ST,ST>{
    CTz ctz;Program p;AllSTzRule(CTz ctz,Program p){this.ctz=ctz;this.p=p;}
    @Override public void rule(ST st, Consumer<ST> s){
      s.accept(st);//* ST in CTz.allSTz(p,ST)
      transitive(st, s);
      if(st instanceof ST.STMeth){onSTMeth((ST.STMeth)st,s);}
      if(st instanceof ST.STOp){onSTOp((ST.STOp)st,s);}
      }
    private void transitive(ST st, Consumer<ST> s) {
      var st1n=ctz.inner.get(st);//    ST1..STn = CTz(ST)
      if(st1n==null){return;}
      for(ST sti:st1n){// ST' in CTz.allSTz(p,STi)
        install(sti, st1->s.accept(st1));//* ST' in CTz.allSTz(p,ST)
        }
      }
    private void onSTMeth(ST.STMeth stsi, Consumer<ST> s) {
      ST st=stsi.st();
      install(st,st2->{
        ST st3=solve(p,stsi.withSt(st2));
        install(st3,st1->s.accept(st1));
        });
      }
    private void onSTOp(ST.STOp stop, Consumer<ST> s) {
      step(stop,s,L());
      //need to accumulate the elements from the stop.stzs() 
      }
    private void step(ST.STOp stop, Consumer<ST> s,List<ST> pre) {
      var stz1n=stop.stzs();
      int n=stz1n.size();
      int size=pre.size();
      if(size==n){
        ST stSolved=solve(p,stop.withStzs(L(pre.stream().map(e->L(e)))));
        install(stSolved, st1->s.accept(st1));
        return;}
      List<ST> optionsSize=stz1n.get(size);
      for(ST sti:optionsSize){
        install(sti,stzSize->step(stop,s,pushL(pre,stzSize)));
        }
      }      
    }
  public Half.E _add(FreshNames fresh,Program p, Core.MH mh, Full.E _e){
    if(_e==null){return null;}
    Y y=new Y(p,GX.of(mh),L(mh.t()),null,L(mh.t()));
    var res= new ToHalf(y,this,fresh).compute(_e);
    this.plusAcc(p, res.resSTz,L(mh.t()));
    assert res.retSTz.isEmpty();//may be not?
    return res.e;
    }
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
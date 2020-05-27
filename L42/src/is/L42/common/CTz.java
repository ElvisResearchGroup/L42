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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
from CTz? assert if you can not solve before from, you can not solve after  

  */
public class CTz{
  private Map<ST,List<ST>> inner=new HashMap<>();
  public Set<Map.Entry<ST,List<ST>>> entries(){return Collections.unmodifiableSet(inner.entrySet());}
  public CTz(Map<ST,List<ST>> innerImm){this.inner.putAll(innerImm);}
  public CTz(){}
  public Map<ST,List<ST>> releaseMap(){
    var res=Collections.unmodifiableMap(inner);
    this.inner=null;//release the only pointer to inner
    return res;
    }
  /*public CTz copy(){
    CTz ctz=new CTz();
    ctz.inner.putAll(inner);
    return ctz;
    }*/
  @Override public String toString(){
    String res=inner.toString();
    res=res.substring(1,res.length()-1);
    return res.replace("imm ","");
    }
  /*public boolean coherent(){//below is just richer
    for(var st:inner.keySet()){
      assert !(st instanceof T):st;
      }
    return true;
    }*/
  public boolean coherent(Program p){
  //Issue: when we p.push(C,L) and L is inside halfE, we may have
  //"solve" doing invalid things... can it happens?
  //TODO: Change formalism so that ctz frommed for each L and trash the accumulated infos.
  //then, when an nc is committed, we explore the coreL to add the info again into the ctz
    for(var e:inner.entrySet()){
      assert !(e.getKey() instanceof T):e.getKey();
      assert p.solve(e.getKey())==e.getKey():p.solve(e.getKey())+" "+e.getKey();
      for(var st:e.getValue()){
        assert p.solve(st)==st:p.solve(st)+" "+st;  
        }
      }
    return true;
    }
  public void plusAcc(Program p,List<ST> stz,List<ST>stz1){
    stz1=p.solve(stz1);
    for(ST st:stz){
      st=p.solve(st);
      if(st instanceof T){continue;}
      plusAcc(p,st,stz1);
      }
    }
  public void plusAcc(Program p,ST st,List<ST>stz1){
    assert coherent(p);
    var data=inner.get(st);
    if(data==null){
      inner.put(st,stz1);
      assert coherent(p);
      return;
      }
    inner.put(st,mergeU(data,stz1));
    assert coherent(p);    
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
        if(st2.equals(stsi)){return;}//to avoid growing a.foo().foo()...foo()
        ST st3=p.solve(stsi.withSt(st2));
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
        ST stSolved=p.solve(stop.withStzs(L(pre.stream().map(e->L(e)))));
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
  }
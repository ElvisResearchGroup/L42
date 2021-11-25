package is.L42.common;
import static is.L42.tools.General.L;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.pushL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import is.L42.constraints.FreshNames;
import is.L42.constraints.ToHalf;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.ST;
import is.L42.generated.Y;
import is.L42.tools.InductiveSet;
import is.L42.visitors.Accumulate;
/*
    
//what to do when the program expands?
from CTz? assert if you can not solve before from, you can not solve after  

  */
public class CTz{
  public boolean equals(Object o){
    return o instanceof CTz && this.inner.equals(((CTz)o).inner);
    }
  public int hashCode() {return inner.hashCode();}
  private Map<ST,List<ST>> inner=new HashMap<>();
  public Set<Map.Entry<ST,List<ST>>> entries(){return Collections.unmodifiableSet(inner.entrySet());}
  public CTz(Map<ST,List<ST>> innerImm){
    this.inner.putAll(innerImm);
    assert coherent(null);  
    }
  public CTz(Program p,Map<ST,List<ST>> innerImm){
    for(var e:innerImm.entrySet()){
      var k=p.solve(e.getKey());
      if(k instanceof T){continue;}
      var v=L(e.getValue().stream().map(p::solve));
      this.inner.put(k, v);
      }
    assert coherent(p);
    }
  public CTz(){}
  public Map<ST,List<ST>> releaseMap(){
    var res=Collections.unmodifiableMap(inner);
    this.inner=null;//release the only pointer to inner
    return res;
    }
  @Override public String toString(){
    String res=inner.toString();
    res=res.substring(1,res.length()-1);
    return res.replace("imm ","");
    }
  public boolean coherent(Program p){
    var visitor=new Accumulate<Void>() {
      @Override public void visitDoc(Doc d){assert false:
        "";} 
      };
    inner.entrySet().forEach(e->{
      visitor.visitST(e.getKey());
      visitor.visitSTz(e.getValue());
      });
    if(p==null){ return true; }
    for(var e:inner.entrySet()){
      assert !(e.getKey() instanceof T):e.getKey();
      assert p.solve(e.getKey())==e.getKey():
        p.solve(e.getKey())+" "+e.getKey();
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
    assert coherent(p);
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
    //DISCUSSION:
    //We are preventing all types of form nested ST.STMeth to avoid circularities in the inference.
    //I can not write a test where a nested ST.STMeth type is required to pass.
    private void onSTMeth(ST.STMeth stsi, Consumer<ST> s) {
      ST st=stsi.st();
      install(st,st2->{
        if(st2 instanceof ST.STMeth){return;}//to avoid growing a.foo().foo()...foo()
        //exmaple weaker condition; too weak, can still loop: if(st2.equals(stsi)){return;}
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
    Half.XP self=new Core.EX(_e.pos(),X.thisX);
    Y y=new Y(p,GX.of(mh),L(mh.t()),self,L(mh.t()));
    var res= new ToHalf(y,this,fresh).compute(_e);    
    this.plusAcc(p, res.resSTz,L(mh.t()));
    assert res.retSTz.isEmpty();//may be not?
    return res.e;
    }
  }
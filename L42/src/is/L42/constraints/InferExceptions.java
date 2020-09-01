package is.L42.constraints;

import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.Block;
import is.L42.generated.Core.E;
import is.L42.generated.Core.K;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MCall;
import is.L42.visitors.Accumulate;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.ThrowKind;

public class InferExceptions {
  Program p;public InferExceptions(Program p){this.p=p;}
  public Program inferExceptions(List<S> sz){
    if(sz.isEmpty()){return p;}
    List<MWT> current=L(sz,(c,s)->c.add(current(s)));
    while(true){
      List<MWT> next=L(current,this::inferExceptions);
      assert next.size()==sz.size();
      assert current.size()==sz.size();
      if(allEq(current,next)){return p;}
      var l=p.topCore();
      List<MWT> mwts=L(l.mwts(),(c,m)->{
        var _m=LDom._elem(next,m.key());
        c.add(_m==null?m:_m);
        });
      var info=l.info();
      var td=new ArrayList<>(info.typeDep());
      for(var m:mwts){for(var t:m.mh().exceptions()){
        if(t.p().isNCs() && !td.contains(t.p())){td.add(t.p().toNCs());}
        }}
      if(td.size()>info.typeDep().size()){info=info.withTypeDep(L(td.stream()));}
      p=p.update(l.withMwts(mwts).withInfo(info),false);
      current=next;
      }
    }
  boolean allEq(List<MWT>c,List<MWT>n){
    return IntStream.range(0, c.size()).allMatch(i->c.get(i)==n.get(i));
    } 

  MWT current(S s){
    var m=LDom._elem(p.topCore().mwts(),s);
    assert m!=null;
    return m;
    }
  MWT inferExceptions(MWT m){
    var all=merge(leaks(p,m),m.mh().exceptions());
    List<T> some=L(all,(t,c)->minimizeSub(t, c));
    if(some.equals(m.mh().exceptions())){return m;}
    return m.withMh(m.mh().withExceptions(some));
    }
  private void minimizeSub(ArrayList<T> res, T t) {
    var already=res.stream().anyMatch(tSuper->p._isSubtype(t, tSuper)==Boolean.TRUE);
    if(already){return;}
    for(int i=0;i<res.size();){
      T ri=res.get(i);
      if(p._isSubtype(ri,t)==Boolean.TRUE){res.remove(i);}
      else{i++;}
      }
    res.add(t);
    }
  List<T> leaks(Program p,MWT mwt){
    return new Leak().of(mwt);
    }
  class Leak extends Accumulate.WithG<List<T>>{
    public List<T> empty(){return L();}
    void addToAcc(List<T> ts){result=merge(ts,acc());}
    List<T> swapAcc(List<T>ts){
      var tmp=result;
      result=ts;
      return tmp;
      }
    void commitAcc(List<T>acc){result=acc;}
    public void visitMCall(MCall m){
      super.visitMCall(m);
      T t=g(m.xP());
      if(t==null){return;}
      var l=p._ofCore(t.p());
      var mwt=LDom._elem(l.mwts(),m.s());
      if(mwt==null){return;}
      var exc=mwt.mh().exceptions();
      exc=L(exc,e->p.from(e,t.p().toNCs()));
      addToAcc(exc);
      }
    public boolean filtered(Block b,T t){
      for(K k:b.ks()){
        if(k.thr()!=ThrowKind.Exception){continue;}
        var res=p._isSubtype(t.p(),k.t().p());
        if(res!=null && res){return false;}
        }
      return true;
      }
    public void visitBlockDs(Block b){
      var oldAcc=this.swapAcc(L());
      super.visitBlockDs(b);
      var novel=acc().stream().filter(t->filtered(b,t));
      commitAcc(L(Stream.concat(novel,oldAcc.stream())));
      }
    }
  }
package newTypeSystem;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

class TIn{
Phase phase;
Program p;
Map<String,NormType>g=Collections.emptyMap();//could be two arrays for efficiency
ExpCore e;
NormType expected;
TIn(Phase phase,Program p,ExpCore e,NormType expected){
  this.phase=phase;this.p=p;
  this.e=e;this.expected=expected;
  }
@Override
public int hashCode() {
  final int prime = 31;
  int result = 1;
  result = prime * result + e.hashCode();
  result = prime * result + expected.hashCode();
  result = prime * result + g.hashCode();
  result = prime * result + p.hashCode();
  result = prime * result + phase.hashCode();
  return result;
  }
@Override
public boolean equals(Object obj) {
  if (this == obj){ return true;}
  assert obj!=null;
  assert getClass() == obj.getClass();
  TIn other = (TIn) obj;
  if (phase != other.phase){ return false;}
  if (!e.equals(other.e)){ return false; }
  if (!expected.equals(other.expected)){ return false;}
  if (!g.equals(other.g)){return false;}
  if (p != other.p){return false;}//simplifing comparing ps
  return true;
  }
public TIn addG(String x, NormType t){
  TIn res=gClean();
  res.g.putAll(this.g);
  assert !res.g.containsKey(x);
  res.g.put(x,t);
  return res;
  }
public TIn addGds(List<ExpCore.Block.Dec> ds){
  TIn res=gClean();
  res.g.putAll(this.g);
  for(ExpCore.Block.Dec d : ds){
    assert !res.g.containsKey(d.getX());
    res.g.put(d.getX(),programReduction.Norm.resolve(this.p,d.getT()));
    }
  return res;
  }
public TIn freshGFromMt(MethodWithType mwt){
MethodType mt=mwt.getMt();
assert mwt.get_inner().isPresent();
TIn res=gClean();
res.e=mwt.getInner();
res.expected=TypeManipulation.fwdP(mt.getReturnType().getNT());
g.put("this",new NormType(mt.getMdf(),Path.outer(0),Doc.empty()));
{int i=-1;for(String x:mwt.getMs().getNames()){i+=1;
  NormType ntx=mt.getTs().get(i).getNT();
  res.g.put(x,ntx);
  }}
return res;
}

public NormType g(String x){
  NormType res=this.g.get(x);
  assert res!=null;
  return res;
  }
public Set<String> gDom(){return g.keySet();}
public TIn gClean(){return new TIn(phase,p,e,expected);}
//onlyMutOrImm(G)={x:G(x) | G(x) only mut or imm}
public TIn onlyMutOrImm(){
  TIn res=gClean();
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    if (ti.getMdf()==Mdf.Mutable || ti.getMdf()==Mdf.Immutable){
      res.g.put(xi,ti);
      }
    }
  return res;
  }
public TIn toRead(){//toRead(G)(x)=toRead(G(x)) //thus undefined where toRead undefined
  TIn res=gClean();
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toRead(ti);
    if(ti==null){continue;}
    res.g.put(xi,ti);
    }
  return res;
  } 
public TIn toLent(){//toLent(G)(x)=toLent(G(x)) //thus undefined where toLent undefined
  TIn res=gClean();
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toRead(ti);
    if(ti==null){continue;}
    res.g.put(xi,ti);
    }
  return res;
  }
public TIn gKs(List<ExpCore.Block.On>ks){     
//G[ks]
//  G[]=G
//  G[k ks]=toRead(G) with k.throw=error and not catchRethrow(k)
//  otherwise G[k ks] = G[ks]
for( On k:ks){
  if(k.getKind()!=SignalKind.Error){continue;}
  if(TypeManipulation.catchRethrow(k)){continue;}
  return this.toRead();
  }
return this;
}
public TIn withE(ExpCore newE,NormType newExpected){
  return new TIn(phase,p,newE,newExpected);
  }
public TIn withP(Program newP){
  return new TIn(phase,newP,e,Path.Library().toImmNT());
  }

boolean isCoherent(){
  return true;
  }
}



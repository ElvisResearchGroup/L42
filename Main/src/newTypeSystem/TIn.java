package newTypeSystem;

import java.util.Collections;
import java.util.HashMap;
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
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

class TIn{
final Phase phase;
final Program p;
final Map<String,NormType>g;//=Collections.emptyMap();//could be two arrays for efficiency
final ExpCore e;
final NormType expected;
public static TIn top(Program p,ExpCore e){
  return new TIn(Phase.Coherent,p,e,Path.Library().toImmNT(),Collections.emptyMap());
  }
private TIn(Phase phase,Program p,ExpCore e,NormType expected,Map<String,NormType>g ){
  this.phase=phase;this.p=p;
  this.e=e;this.expected=expected;
  this.g=g;
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
  assert !g.containsKey(x);
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  newG.put(x,t);
  return this.withG(newG);
  }
public TIn addGds(List<ExpCore.Block.Dec> ds){
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(ExpCore.Block.Dec d : ds){
    assert !g.containsKey(d.getX());
    newG.put(d.getX(),programReduction.Norm.resolve(this.p,d.getT()));
    }
  return this.withG(newG);
  }
public TIn removeGXs(Set<String> set) {
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(String x : set){
    newG.remove(x);
    }
  return this.withG(newG);
  }
public TIn removeGDs(List<Block.Dec> ds) {
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(Dec di : ds){
    newG.remove(di.getX());
  }
  return this.withG(newG);
}

public TIn freshGFromMt(MethodWithType mwt){
  MethodType mt=mwt.getMt();
  assert mwt.get_inner().isPresent();
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  newG.put("this",new NormType(mt.getMdf(),Path.outer(0),Doc.empty()));
  {int i=-1;for(String x:mwt.getMs().getNames()){i+=1;
    NormType ntx=mt.getTs().get(i).getNT();
    newG.put(x,ntx);
    }}
  return new TIn(this.phase,this.p,mwt.getInner(),TypeManipulation.fwdP(mt.getReturnType().getNT()),newG);
  }

public NormType g(String x){
  NormType res=this.g.get(x);
  assert res!=null:
    x;
  return res;
  }
public Set<String> gDom(){return g.keySet();}
//public TIn gClean(){return new TIn(phase,p,e,expected);}
//onlyMutOrImm(G)={x:G(x) | G(x) only mut or imm}
public TIn toRead(){//toRead(G)(x)=toRead(G(x)) //thus undefined where toRead undefined
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toRead(ti);
    if(ti==null){continue;}
    newG.put(xi,ti);
    }
  return this.withG(newG);
  } 
public TIn toLent(){//toLent(G)(x)=toLent(G(x)) //thus undefined where toLent undefined
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toRead(ti);
    if(ti==null){continue;}
    newG.put(xi,ti);
    }
  return this.withG(newG);
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
  return new TIn(this.phase,this.p,newE,newExpected,this.g);
  }
public TIn withP(Program newP){
  return new TIn(this.phase,newP,this.e,Path.Library().toImmNT(),this.g);
  }
private TIn withG(Map<String,NormType>g){
  return new TIn(this.phase,this.p,this.e,this.expected,g);
}

boolean isCoherent(){
  return true;
  }
@Override public String toString(){
  return this.phase+";p;"+this.g+"|-"+sugarVisitors.ToFormattedText.of(this.e)+":"+this.expected;
  }
}



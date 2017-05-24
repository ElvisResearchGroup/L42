package newTypeSystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

abstract class AG<This extends AG<This>>{
  AG(Map<String,NormType>g){this.g=g;}
  //TODO: should not be public
  abstract public This withG(Map<String,NormType>g);//{return new This();}
  abstract This self();//{return this;}
  final Map<String,NormType>g;//=Collections.emptyMap();//could be two arrays for efficiency
  public This addG(String x, NormType t){
    assert !g.containsKey(x);
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    newG.put(x,t);
    return this.withG(newG);
    }
  public This removeG(String x){
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    newG.remove(x);
    return this.withG(newG);
    }
  public This addGds(Program p,List<ExpCore.Block.Dec> ds){
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    for(ExpCore.Block.Dec d : ds){
      assert !g.containsKey(d.getX());
      newG.put(d.getX(),programReduction.Norm.resolve(p,d.getT().get()));
      }
    return this.withG(newG);
    }
  public This addGG(AG<?> in){
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    assert newG.keySet().stream().noneMatch(k->in.g.containsKey(k));
    newG.putAll(in.g);
    return this.withG(newG);
    }
  public This removeGXs(Set<String> set) {
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    for(String x : set){
      newG.remove(x);
      }
    return this.withG(newG);
    }
  public This removeGDs(List<Block.Dec> ds) {
    Map<String,NormType>newG=new HashMap<String,NormType>(g);
    for(Dec di : ds){
      newG.remove(di.getX());
    }
    return this.withG(newG);
  }
 public NormType g(String x){
    NormType res=this.g.get(x);
    assert res!=null:
      x;
    return res;
    }
 public NormType _g(String x){
   return this.g.get(x);
   }

 public Set<String> gDom(){return g.keySet();}

  //onlyMutOrImm(G)={x:G(x) | G(x) only mut or imm}
  public This toRead(){//toRead(G)(x)=toRead(G(x)) //thus undefined where toRead undefined
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
public This toLent(){//toLent(G)(x)=toLent(G(x)) //thus undefined where toLent undefined
  Map<String,NormType>newG=new HashMap<String,NormType>(g);
  for(String xi:gDom()){
    NormType ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toLent(ti);
    if(ti==null){continue;}
    newG.put(xi,ti);
    }
  return this.withG(newG);
  }
public This gKs(List<ExpCore.Block.On>ks){     
//G[ks]
//  G[]=G
//  G[k ks]=toRead(G) with k.throw=error and not catchRethrow(k)
//  otherwise G[k ks] = G[ks]
  for( On k:ks){
    if(k.getKind()!=SignalKind.Error){continue;}
    if(TypeManipulation.catchRethrow(k)){continue;}
    return this.toRead();
    }
  return this.self();
  }
}
class G extends AG<G>{
  private G(Map<String,NormType>g){super(g);}
  @Override public G withG(Map<String,NormType>g) {return new G(g);}
  @Override G self() {return this;}
  static final G instance=new G(Collections.emptyMap());
  public String toString(){return g.toString();}
  }
public class TIn extends AG<TIn>{
@Override public TIn withG(Map<String,NormType>g) {return new TIn(this.phase,this.p,this.e,this.expected,g);}
@Override TIn self() {return this;}

final Phase phase;
final Program p;
final ExpCore e;
final NormType expected;
public static TIn top(Phase phase,Program p,ExpCore e){
  return new TIn(phase,p,e,Path.Library().toImmNT(),Collections.emptyMap());
  }
private TIn(Phase phase,Program p,ExpCore e,NormType expected,Map<String,NormType>g ){
  super(g);
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
public TIn withE(ExpCore newE,NormType newExpected){
  return new TIn(this.phase,this.p,newE,newExpected,this.g);
  }
public TIn withP(Program newP){
  return new TIn(this.phase,newP,this.e,Path.Library().toImmNT(),this.g);
  }

boolean isCoherent(){
  return true;
  }
@Override public String toString(){

  String resE=sugarVisitors.ToFormattedText.of(this.e);
  resE=resE.replace("\n", " ");
  if(resE.length()>50){resE=resE.substring(0,40)+"[..]"+resE.substring(resE.length()-5,resE.length());}
  return this.phase+";p;"+this.g+"|-"+resE+":"+this.expected;
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
  return new TIn(Phase.Typed,this.p,mwt.getInner(),TypeManipulation.fwdP(mt.getReturnType().getNT()),newG);
  }

}



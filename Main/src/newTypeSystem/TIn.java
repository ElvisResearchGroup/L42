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
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import programReduction.Program;

abstract class AG<This extends AG<This>>implements G{
  AG(Map<String,Map.Entry<Boolean, Type>>g){this.g=g;}
  
  @Override public G add(Program p, List<Dec> ds) {
  Map<String, Type> varEnv2=new HashMap<>();
  for(Entry<String, Entry<Boolean, Type>> e:g.entrySet()){
    varEnv2.put(e.getKey(),e.getValue().getValue());
    }
  for(Dec d:ds){
    if( d.get_t()!=null){varEnv2.put(d.getX(),d.get_t());}
    }
  return G.of(varEnv2);
  }
  //TODO: should not be public
  abstract public This withG(Map<String,Map.Entry<Boolean, Type>>g);//{return new This();}
  abstract This self();//{return this;}
  final Map<String,Map.Entry<Boolean, Type>>g;//=Collections.emptyMap();//could be two arrays for efficiency
  public static Map.Entry<Boolean, Type> p(Boolean b,Type t){//p for pair
    return new java.util.AbstractMap.SimpleEntry<>(b,t);
    }
  public This addTx(String x,Type t){return addG(x,false,t);}
  public This addG(String x,boolean var, Type t){
    assert !g.containsKey(x);
    assert !var || !TypeManipulation.fwd_or_fwdP_in(t.getMdf());
    assert !var || t.getMdf()!=Mdf.Capsule;
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    newG.put(x,p(var,t));
    return this.withG(newG);
    }
  public This removeG(String x){
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    newG.remove(x);
    return this.withG(newG);
    }
  public This addGds(Program p,List<ExpCore.Block.Dec> ds){
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    for(ExpCore.Block.Dec d : ds){
      assert !g.containsKey(d.getX());
      Type nt = d.getT().get();
      assert !d.isVar() || !TypeManipulation.fwd_or_fwdP_in(nt.getMdf());
      assert !d.isVar() || nt.getMdf()!=Mdf.Capsule;
      newG.put(d.getX(),p(d.isVar(),nt));
      }
    return this.withG(newG);
    }
  public This addGG(AG<?> in){
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    assert newG.keySet().stream().noneMatch(k->in.g.containsKey(k));
    newG.putAll(in.g);
    return this.withG(newG);
    }
  public This removeGXs(Set<String> set) {
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    for(String x : set){
      newG.remove(x);
      }
    return this.withG(newG);
    }
  public This removeGDs(List<Block.Dec> ds) {
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    for(Dec di : ds){
      newG.remove(di.getX());
    }
    return this.withG(newG);
  }
 public Type g(String x){
    Type res=this.g.get(x).getValue();
    assert res!=null:
      x;
    return res;
    }
 public boolean gVar(String x){
   return this.g.get(x).getKey();
   }
 public Type _g(String x){
   Entry<Boolean, Type> tmp = this.g.get(x);
   if(tmp==null){return null;}
   return tmp.getValue();
   }

 public Set<String> dom(){return g.keySet();}

  //onlyMutOrImm(G)={x:G(x) | G(x) only mut or imm}
  public This toRead(){//toRead(G)(x)=toRead(G(x)) //thus undefined where toRead undefined
    Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
    for(String xi:dom()){
      Type ti=g(xi);
      assert ti!=null;
      ti=TypeManipulation._toRead(ti);
      if(ti==null){continue;}
      newG.put(xi,p(false,ti));
      }
    return this.withG(newG);
    } 
public This toLent(){//toLent(G)(x)=toLent(G(x)) //thus undefined where toLent undefined
  Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>(g);
  for(String xi:dom()){
    Type ti=g(xi);
    assert ti!=null;
    ti=TypeManipulation._toLent(ti);
    if(ti==null){continue;}
    if(ti.getMdf()==Mdf.Immutable){
      newG.put(xi,p(gVar(xi),ti));
      }
    else{newG.put(xi,p(false,ti));}
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
class TInG extends AG<TInG>{
  private TInG(Map<String,Map.Entry<Boolean, Type>>g){super(g);}
  @Override public TInG withG(Map<String,Map.Entry<Boolean, Type>>g) {return new TInG(g);}
  @Override TInG self() {return this;}
  static final TInG instance=new TInG(Collections.emptyMap());
  public String toString(){return g.toString();}
  }
public class TIn extends AG<TIn>{
@Override public TIn withG(Map<String,Map.Entry<Boolean, Type>>g) {return new TIn(this.phase,this.p,this.e,this.expected,g);}
@Override TIn self() {return this;}

final Phase phase;
public final Program p;
final ExpCore e;
final Type expected;
public static TIn top(Phase phase,Program p,ExpCore e){
  return new TIn(phase,p,e,Path.Library().toImmNT(),Collections.emptyMap());
  }
private TIn(Phase phase,Program p,ExpCore e,Type expected,Map<String,Map.Entry<Boolean, Type>>g ){
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
public TIn withE(ExpCore newE,Type newExpected){
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
public static TIn freshGFromMt(Program p,MethodWithType mwt){
  MethodType mt=mwt.getMt();
  assert mwt.get_inner()!=null;
  Map<String,Map.Entry<Boolean, Type>>newG=new HashMap<>();
  newG.put("this",p(false,new Type(mt.getMdf(),Path.outer(0),Doc.empty())));
  {int i=-1;for(String x:mwt.getMs().getNames()){i+=1;
    Type ntx=mt.getTs().get(i);
    newG.put(x,p(false,ntx));
    }}
  return new TIn(Phase.Typed,p,mwt.getInner(),TypeManipulation.fwdP(mt.getReturnType()),newG);
  }
}



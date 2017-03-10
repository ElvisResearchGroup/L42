package newTypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import ast.ExpCore;
import ast.Ast;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;

class TErr implements TOut,TOutM,TOutDs,TOutKs,TOutK{
@Override public boolean isOk() { return false;}
@Override public TErr toError() {return this;}
public TErr(TIn in, String msg, NormType _computed, ErrorKind kind) {
  this.in = in; this.msg = msg; this._computed = _computed;this.kind=kind;
  }
TIn in;
String msg;
NormType _computed;
ErrorKind kind;
public TErr enrich(TIn in2) {
  return this;//TODO: design some general error context enreaching
  }  
}


//-----------------TOut
interface TOut{
  boolean isOk();
  default TOk toOk() {throw new Error();}
  default TErr toError() {throw new Error();}
  }

class TOk implements TOut{
  @Override public boolean isOk() { return true;}
  @Override public TOk toOk() {return this;}
  TIn in;
  ExpCore annotated;
  NormType computed;
  List<NormType>returns=Collections.emptyList();
  List<Path>exceptions=Collections.emptyList();
  public TOk(TIn in, ExpCore annotated, NormType computed){
    this.in=in;this.annotated=annotated;this.computed=computed;
    }
  public TOk withAC(ExpCore annotated,NormType computed){
    TOk res=new TOk(this.in,annotated,computed);
    res.returns=this.returns;
    res.exceptions=this.exceptions;
    return res;
    }
  private <T> List<T> union(List<T> l1,List<T>l2){
    //optimized when most lists are empty
    if(l1.isEmpty() && l2.isEmpty()){return Collections.emptyList();}
    if(l1.isEmpty()){return l2;}
    if(l2.isEmpty()){return l1;}
    List<T>res=new ArrayList<>(l1);
    res.addAll(l2);
    return res;
  }
  public TOk tsUnion(TOk that){
    //Tr1 U Tr2
    //  Ts1;Ps1 U Ts2;Ps2 =  Ts1,Ts2; Ps1,Ps2  
    TOk res=new TOk(this.in,this.annotated,this.computed);
    assert this.returns!=null;
    assert that.returns!=null;
    if(this.returns.isEmpty() && that.returns.isEmpty())
    res.returns=union(this.returns,that.returns);
    res.exceptions=union(this.exceptions,that.exceptions);
    return res;
    }
  public TOk returnsAdd(NormType t){
    TOk res=new TOk(this.in,this.annotated,this.computed);
    res.returns=new ArrayList<>(returns);
    res.returns.add(t);
    return res;
    }
  public TOk exceptionsAdd(Path p){
    TOk res=new TOk(this.in,this.annotated,this.computed);
    res.exceptions=new ArrayList<>(exceptions);
    res.exceptions.add(p);
    return res;
  }
  public TOk trCapture(On k){
    //Tr.capture(k1..kn)= Tr.capture(p,k1)...capture(p,kn)
    //Tr.capture(catch error P x e)=Tr
    //(Ts;Ps).capture(catch exception P x e)=Ts;{P'| P' in Ps, not p|-P'<=P}
    //(Ts;Ps).capture(catch return P x e)={T| T in Ts, not p|-T.P<=P};Ps
    if(k.getKind()==SignalKind.Error){return this;}
    TOk result=new TOk(in,annotated,computed);
    if(k.getKind()==SignalKind.Exception){
      result.exceptions=new ArrayList<>();
      for(Path pi: exceptions){
        if(null!=TypeSystem.subtype(this.in.p,pi,k.getT().getNT().getPath())){
          result.exceptions.add(pi);
          }
        }
      }
    //otherwise, is return
    result.returns=new ArrayList<>();
    for(NormType ti: returns){
      if(null!=TypeSystem.subtype(this.in.p,ti.getPath(),k.getT().getNT().getPath())){
        result.returns.add(ti);
        }
      }
    return result;
    }
  boolean isCoherent(){
    assert in!=null;
    assert annotated!=null;
    assert computed!=null;
    return true;
    }
  }


//----------------------TOutM
interface TOutM{
  boolean isOk();
  default TOkM toOkM() {throw new Error();}
  default TErr toError() {throw new Error();}
  }

class TOkM implements TOutM{
  public TOkM(Member inner) {this.inner = inner;}
  ExpCore.ClassB.Member inner;  
  @Override public boolean isOk() { return true;}
  @Override public TOkM toOkM() {return this;}
  }

//----------------------TOutDs
interface TOutDs{
boolean isOk();
default TOkDs toOkDs() {throw new Error();}
default TErr toError() {throw new Error();}
}

class TOkDs implements TOutDs{
  public TOkDs(TOk trAcc, List<Dec> ds, TIn g) {
    this.trAcc = trAcc;
    this.ds = ds;
    this.g = g;
    }
  TOk trAcc;
  List<ExpCore.Block.Dec> ds;
  TIn g;
  @Override public boolean isOk() { return true;}
  @Override public TOkDs toOkDs() {return this;}
  }

//----------------------TOutKs
interface TOutKs{
boolean isOk();
default TOkKs toOkKs() {throw new Error();}
default TErr toError() {throw new Error();}
}

class TOkKs implements TOutKs{
  public TOkKs(TOk trAcc,TOk trCaptured, List<On> ks, List<NormType> ts) {
    this.trAcc = trAcc;
    this.trCaptured=trCaptured;
    this.ks = ks;
    this.ts = ts;
    }
  TOk trAcc;
  TOk trCaptured;
  List<ExpCore.Block.On> ks;
  List<NormType> ts;
  @Override public boolean isOk() { return true;}
  @Override public TOkKs toOkKs() {return this;}
}
//----------------------TOutK
interface TOutK{
boolean isOk();
default TOkK toOkK() {throw new Error();}
default TErr toError() {throw new Error();}
}

class TOkK implements TOutK{
public TOkK(TOk tr, On k, NormType t) {
  this.tr = tr;
  this.k = k;
  this.t = t;
  }
TOk tr;
On k;
NormType t;
@Override public boolean isOk() { return true;}
@Override public TOkK toOkK() {return this;}
}

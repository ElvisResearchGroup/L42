package newTypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import ast.ExpCore;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.SignalKind;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;

class TOk implements TOut{
  public boolean isOk() { return true;}
  public TOk toOk() {return this;}
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
  public TOk tsCapture(List<ExpCore.Block.On> ks){
    //Tr.capture(p,k1..kn)= Tr.capture(p,k1)...capture(p,kn)
    //Tr.capture(p,catch error P x e)=Tr
    //(Ts;Ps).capture(p,catch exception P x e)=Ts;{P'| P' in Ps, not p|-P'<=P}
    //(Ts;Ps).capture(p,catch return P x e)={T| T in Ts, not p|-T.P<=P};Ps
    Stream<NormType> ret = this.returns.stream();
    Stream<Path> exc = this.exceptions.stream();
    for(On k:ks){
      if(k.getKind()==SignalKind.Error){continue;}
      if(k.getKind()==SignalKind.Exception){
        exc=exc.filter(pi->!TypeSystem.subtype(this.in.p,pi,k.getT().getNT().getPath()));
        }
      //otherwise, is return
      ret=ret.filter(ti->!TypeSystem.subtype(this.in.p,ti.getPath(),k.getT().getNT().getPath()));
      }
    TOk result=new TOk(in,annotated,computed);
    exc.forEach(result.exceptions::add);
    ret.forEach(result.returns::add);
    return result;
    }
  boolean isCoherent(){
    assert in!=null;
    assert annotated!=null;
    assert computed!=null;
    return true;
    }
  }
class TOkM implements TOutM{
  public TOkM(Member inner) {this.inner = inner;}
  ExpCore.ClassB.Member inner;  
  public boolean isOk() { return true;}
  public TOkM toOkM() {return this;}
  }
class TErr implements TOut,TOutM{
  public boolean isOk() { return false;}
  public TErr toError() {return this;}
  public TErr(TIn in, String msg, NormType _computed) {
    this.in = in; this.msg = msg; this._computed = _computed;
    }
  TIn in;
  String msg;
  NormType _computed;
  public TErr enrich(TIn in2) {
    return this;//TODO: design some general error context enreaching
    }  
  }

interface TOut{
  boolean isOk();
  default TOk toOk() {throw new Error();}
  default TErr toError() {throw new Error();}
  }
interface TOutM{
  boolean isOk();
  default TOkM toOkM() {throw new Error();}
  default TErr toError() {throw new Error();}
  }

package is.L42.nativeCode;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.List;

import is.L42.generated.Mdf;

class Signature{
  Mdf methMdf;
  Mdf retMdf;
  TrustedT retT;
  List<Mdf>parMdfs;
  List<TrustedT>parTs;
  List<TrustedT>exceptions;
  Signature(Mdf methMdf,Mdf retMdf,TrustedT retT,List<Mdf>parMdfs,List<TrustedT>parTs,List<TrustedT>exceptions){
    this.methMdf=methMdf;
    this.retMdf=retMdf;
    this.retT=retT;
    this.parMdfs=parMdfs;
    this.parTs=parTs;
    this.exceptions=exceptions;
    }
  public String toString(){
    String pars="";
    for(int i:range(parMdfs)){
      pars+=" "+parMdfs.get(i)+" "+parTs.get(i);
      }
    return methMdf.inner+" method "+retMdf.inner+" "+retT+"("+pars+")";
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT){
    return new Signature(methMdf,retMdf,retT,L(),L(),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T){
    return new Signature(methMdf,retMdf,retT,L(p1Mdf),L(p1T),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T,Mdf p2Mdf,TrustedT p2T){
    return new Signature(methMdf,retMdf,retT,List.of(p1Mdf,p2Mdf),List.of(p1T,p2T),L());
    }
  public static Signature sig(Mdf methMdf,Mdf retMdf,TrustedT retT,Mdf p1Mdf,TrustedT p1T,Mdf p2Mdf,TrustedT p2T,Mdf p3Mdf,TrustedT p3T){
    return new Signature(methMdf,retMdf,retT,List.of(p1Mdf,p2Mdf,p3Mdf),List.of(p1T,p2T,p3T),L());
    }

  public static Signature sigI(TrustedT retT){
    return new Signature(Mdf.Immutable,Mdf.Immutable,retT,L(),L(),L());
    }
  public static Signature sigI(TrustedT retT,TrustedT ...pTs){
    var pts=List.of(pTs);
    List<Mdf> mdfs=L(pts,(c,e)->c.add(Mdf.Immutable));
    return new Signature(Mdf.Immutable,Mdf.Immutable,retT,mdfs,pts,L());
    }
  }

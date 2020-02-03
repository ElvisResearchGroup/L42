package is.L42.nativeCode;

import static is.L42.tools.General.range;

import java.util.Map;

import static is.L42.nativeCode.TT.*;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.translationToJava.J;
import is.L42.translationToJava.NativeDispatch;

interface TrustedT{}
enum TT implements TrustedT{Lib,Void,Any,Gen1,Gen2,Gen3,Gen4,This}
class OpUtils{
  static String vectorGet(boolean mut){ 
    return vectorExc2("""
      var tmp=%1$s.get(%2$s*2+3);
      if(tmp==is.L42.nativeCode.TrustedOp.Flags."""+(mut?"MutElem":"ImmElem")+"""
        ){return %1$s.get(%2$s*2+2);}
      throw new L42Error(%Gen"""+(mut?"4":"3")+"""
      .wrap(new L42LazyMsg(
        "#val called, but the element in position "+%1$s+" was inserted as immutable"
        )));
    """);}
    static String vectorOp(String op,boolean mut){
      return vectorExc2("%1$s."+op+"(%2$s*2+2,%3$s);%1$s."+op+"(%2$s*2+3,is.L42.nativeCode.TrustedOp.Flags."+(mut?"MutElem":"ImmElem")+");return L42Void.instance;");
      }
    static String vectorOpRemove(){
      return vectorExc2("%1$s.remove(%2$s*2+3);%1$s.remove(%2$s*2+2);return L42Void.instance;");
      }
    static String vectorReadGet(){
      return vectorExc2("return %s.get(%s*2+2);");
      }
    static private final String vectorExc2(String body){
      return "try{"+body+"}"+
        "catch(ArrayIndexOutOfBoundsException oob){throw new L42Error(%Gen2.wrap(new L42LazyMsg(oob.getMessage())));}";
      }
    static private String vectorCache(J j){
      P pGen=j.p().topCore().info().nativePar().get(0);
      if(pGen==P.pAny){return "null";}
      if(pGen==P.pVoid){return "L42Void.myCache";}
      if(pGen==P.pLibrary){return "L42Library.myCache";}
      Program pOfGen=j.p().navigate(pGen.toNCs());
      var l=pOfGen.topCore();
      String genT=j.typeNameStr(pOfGen);
      if(l.isInterface()){return "null";}
      if(l.info().nativeKind().isEmpty()){return genT+".myCache";}
      String wrapperT=J.classNameStr(pOfGen);
      return wrapperT+".myCache.rawFieldCache(0)";
      }
    static Generator vectorKs(){return (type,mwt,j)->{
      Signature sig0=null;//sig(Class,Mutable,This,Immutable,Int);
      if(type && typingUse(j.p(),mwt,sig0)){j.c("");return;}//TODO: here and in use: why j.c("")??
      String s=OpUtils.makeVector(j,"%2$s");
      s=s.formatted(NativeDispatch.xs(mwt).toArray());
      j.c(s);
      };}
    static public String makeVector(J j,String size){
      return "var res=new "+j.typeNameStr(j.p())+"("+size+"+2); "+
        "var res0=(ArrayList)res; "+
        "res0.add("+vectorCache(j)+");res0.add(null); return res;";
      }
    static void checkParCount(Program p,MWT mwt,int expected){
      if(mwt.key().xs().size()==expected){return;}
      throw new EndError.TypeError(mwt._e().poss(),Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),expected));
      }
    static Map<TrustedKind,Generator> append(String s){
      String pattern="%s.append(\""+s+"\");return L42Void.instance;";
      var u=use(pattern,Signature.sig(Mdf.Mutable,Mdf.Immutable,Void));
      return Map.of(TrustedKind.StringBuilder,u);
      }
    static boolean typingUse(Program p, MWT mwt,Signature s){
      assert s.parMdfs.size()==s.parTs.size();
      if(s.parTs.size()!=mwt.mh().pars().size()){
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),s.parMdfs.size()));
        }
      checkMdf(mwt,mwt.mh().mdf(),s.methMdf);
      var t=mwt.mh().t();
      checkSingle(p,mwt,t.p(),t.mdf(),s.retT,s.retMdf);
      for(int i:range(mwt.mh().pars().size())){
        var pi=mwt.mh().pars().get(i).p();
        var mdfi=mwt.mh().pars().get(i).mdf();
        var tti=s.parTs.get(i);
        var tmdfi=s.parMdfs.get(i);
        checkSingle(p,mwt,pi,mdfi,tti,tmdfi);
        }
      //TODO: check exceptions
      return true;
      }
    private static void checkGen(int i,Program p,MWT mwt,P pi){
      assert p.topCore().info().nativePar().size()>i;
      var pari=p.topCore().info().nativePar().get(i);
      if(pi.equals(pari)){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,pari));            
      }
    private static void checkMdf(MWT mwt,Mdf mdfi,Mdf tmdfi){
      if(mdfi!=tmdfi){
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),mdfi,tmdfi));
        }
      }
    private static void checkSingle(Program p,MWT mwt,P pi,Mdf mdfi,TrustedT tti,Mdf tmdfi){
      checkMdf(mwt,mdfi,tmdfi);
      if(tti instanceof TrustedKind){
        var ki=(TrustedKind)tti;
        var li=p._ofCore(pi);
        assert li!=null: pi+" "+p.pop().topCore();
        var kind=li.info().nativeKind();
        if(!kind.isEmpty() && ki==TrustedKind._fromString(kind)){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,ki));            
        }
    if(tti==Lib){
      if(pi==P.pLibrary){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Library"));            
      }
    if(tti==Void){
      if(pi==P.pVoid){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Void"));            
      }
    if(tti==Any){
      if(pi==P.pAny){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"Any"));            
      }
    if(tti==Gen1){checkGen(0,p,mwt,pi);}
    if(tti==Gen2){checkGen(1,p,mwt,pi);}
    if(tti==Gen3){checkGen(2,p,mwt,pi);}
    if(tti==Gen4){checkGen(3,p,mwt,pi);}
    if(tti==This){
      if(pi.equals(P.pThis0)){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        Err.nativeParameterInvalidKind(mwt.nativeUrl(),mwt.key(),pi,"This"));            
      }
    }
  @SuppressWarnings("removal")//String.formatted is "preview feature" so triggers warnings
  static Generator use(String s0,Signature sig){
    return (type,mwt,j)->{
      Program p=j.p();
      if(type && typingUse(p,mwt,sig)){j.c("");return;}
      String s=s0;
      if(s.contains("%This")){
        String thisS=j.typeNameStr(p);
        s=s.replace("%This",thisS);
        }
      for(int i:range(p.topCore().info().nativePar())){
        P geni=p.topCore().info().nativePar().get(i);
        if(s.contains("%Gen"+(i+1)+".")){
          String geniS=J.classNameStr(p.navigate(geni.toNCs()));
          s=s.replace("%Gen"+(i+1)+".",geniS+".");
          }
        if(s.contains("%Gen"+(i+1))){
          s=s.replace("%Gen"+(i+1),j.typeNameStr(geni));
          }
        }           
      j.c(s.formatted(NativeDispatch.xs(mwt).toArray()));
      };
    }
  }

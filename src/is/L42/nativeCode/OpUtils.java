package is.L42.nativeCode;

import static is.L42.nativeCode.TT.*;
import static is.L42.nativeCode.TrustedKind.TrustedIO;
import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core.MWT;
import is.L42.generated.Mdf;
import is.L42.translationToJava.J;
import is.L42.translationToJava.NativeDispatch;

enum TT implements TrustedT{Lib,Void,Any,Gen1,Gen2,Gen3,Gen4,This}
class OpUtils{
  static String listGet(boolean mut,boolean self){
    //Note: on different Java versions multiline strings are composed differently!
    //the "non indented" .wrap is needed, otherwise we would patter match
    // ""Gen3."" on some systems and ""Gen3  ."" on some others
    String tName="ArrayList";
    if(self){tName="L42£SelfList";}
    String flagName="";
    if(self){flagName="Flag";}
    return listExc2("Object tmp=(("+tName+")%1$s).get"+flagName+"(%2$s*2+2);\n"+"""
      if(tmp==is.L42.nativeCode.Flags."""+(mut?"MutElem":"ImmElem")+"""
        ){return %1$s.get(%2$s*2+1);}
      throw new L42Error(%Gen"""+(mut?"4":"3")+"""
    .wrap(new L42£LazyMsg(
        "#val called, but the element in position "+%2$s+" was inserted as"""+(mut?" immutable\"":" mutable\"")+"""
        )));
    """);}
    static String listOp(String op,boolean mut,boolean self){
      String tName="ArrayList";
      if(self) {tName="L42£SelfList";}
      return listExc2(//ok also for SelfList since add/set take object
        "%1$s."
        +op+"(%2$s*2+1,%3$s);(("+tName+")%1$s)."
        +op+"(%2$s*2+2,is.L42.nativeCode.Flags."+(mut?"MutElem":"ImmElem")
        +");return L42£Void.instance;"
        );
      }
    static String listOpIsMut(){
      return """
        var self=%s;
        int index=%s;
        if(index<0 || index>=self.size()){ return false; }
        return self.get(index*2+2)==((Object)is.L42.nativeCode.Flags.MutElem);
        """;
      }
    static String listOpRemove(){
      return listExc2("%1$s.remove(%2$s*2+2);%1$s.remove(%2$s*2+1);return L42£Void.instance;");
      }
    static String listReadGet(){
      return listExc2("return %s.get(%s*2+1);");
      }
    private static final String listExc2(String body){
      return "try{"+body+"}"+
        "catch(IndexOutOfBoundsException oob){"+
        "throw new L42Error(%Gen2.wrap(new L42£LazyMsg("+
        "(%1$s.size()==1)?\"Attempting to access an element from an empty list\":("+
        "\"For a list of size \"+(%1$s.size()/2)+\" the provided index: \"+%2$s+\" is not in the acceptable range 0..\"+((%1$s.size()/2)-1)"+
        "))));"+
        "}";
      }
    static final Function<Program,String> mapOutOfBound(String body,String bodyOpt){
      return mapChoice(mapOutOfBound(body), mapOutOfBound(bodyOpt));
      }
    static final String mapOutOfBound(String body){
      return "try{"+body+"}"+
        "catch(ArrayIndexOutOfBoundsException oob){throw new L42Error(%Gen4.wrap(new L42£LazyMsg(oob.getMessage())));}";
      }
    static final String setOutOfBound(String body){
      return "try{"+body+"}"+
        "catch(ArrayIndexOutOfBoundsException oob){throw new L42Error(%Gen2.wrap(new L42£LazyMsg(oob.getMessage())));}";
      }
    static final Map<TrustedKind,Generator>type(String s,Signature sig){
      return Map.of(TrustedKind.Type,use("return "+s+";",sig));
      }
    static final Map<TrustedKind,Generator>nested(String s,Signature sig){
      return Map.of(TrustedKind.Nested,use("return "+s+";",sig));
      }
    static final Map<TrustedKind,Generator>nested(String s,Signature sig,int num){
      return Map.of(TrustedKind.Nested,use(exc("return "+s+";",num,"IndexOutOfBoundsException"),sig));
      }
    static final Map<TrustedKind,Generator>doc(String s,Signature sig){
      return Map.of(TrustedKind.Doc,use("return "+s+";",sig));
      }
    static final Map<TrustedKind,Generator>doc(String s,Signature sig,int num){
      return Map.of(TrustedKind.Doc,use(exc("return "+s+";",num,"IndexOutOfBoundsException"),sig));
      }
    static final Map<TrustedKind,Generator>method(String s,Signature sig){
      return Map.of(TrustedKind.Method,use("return "+s+";",sig));
      }
    static final Map<TrustedKind,Generator>method(String s,Signature sig,int num){
      return Map.of(TrustedKind.Method,use(exc("return "+s+";",num,"IndexOutOfBoundsException"),sig));
      }
    static final Map<TrustedKind,Generator>trustedIO(String s,Signature sig){
      return Map.of(TrustedIO,use(s,sig));
      }
    static final String exc(String body,int num,String exc){
      return "try{"+body+"}"+
        "catch("+exc+" o_O){throw new L42Error(%Gen"+num+".wrap(new L42£LazyMsg(o_O.getMessage())));}";
      }
    static final Map<TrustedKind,Generator> useToS(TrustedKind ... kinds){
      var gen=use("return ((Object)%s).toString();",Signature.sig(Mdf.Readable,Mdf.Immutable,TrustedKind.String));
      var res=new HashMap<TrustedKind,Generator>();
      res.put(TrustedKind.String,use("return %s;",Signature.sig(Mdf.Readable,Mdf.Immutable,TrustedKind.String)));
      for(var tk:kinds){res.put(tk,gen);}
      return Collections.unmodifiableMap(res);
      }
    @SafeVarargs static final Map<TrustedKind,Generator> all(Map<TrustedKind,Generator> ... maps){
      var res=new HashMap<TrustedKind,Generator>();
      for(var m:maps){res.putAll(m);}
      return Collections.unmodifiableMap(res);
      }
    static public String genCache(J j,int i){
      P pGen=j.p().topCore().info().nativePar().get(i);
      if(pGen==P.pAny){return "null";}
      if(pGen==P.pVoid){return "L42£Void.myCache";}
      if(pGen==P.pLibrary){return "L42£Library.myCache";}
      Program pOfGen=j.p().navigate(pGen.toNCs());
      var l=pOfGen.topCore();
      String genT=j.typeNameStr(pOfGen);
      if(l.isInterface()){return "null";}
      if(l.info().nativeKind().isEmpty()){return genT+".myCache";}
      String wrapperT=J.classNameStr(pOfGen);
      return wrapperT+".myCache.rawFieldCache(null,0)";
      }
    static Generator listKs(boolean self){return new Generator(){
      public void check(boolean allowAbs,MWT mwt,J j){
        Signature sig0=Signature.sig(Mdf.Class,Mdf.Mutable,This,Mdf.Immutable,TrustedKind.Int);
        typingUse(j.p(),mwt,sig0,allowAbs);
        }
      public void generate(MWT mwt,J j){
        if(self){ j.c("return new L42£SelfList();"); return;}
        String s=OpUtils.makeList(j,"%2$s");
        s=s.formatted(NativeDispatch.xs(mwt).toArray());
        j.c(s);
        }
      };}
    static public String makeList(J j,String size){
      String typeName=j.typeNameStr(j.p());
      return "var res=new "+typeName+"("+size+"+1); "+
        "var res0=(ArrayList)res; "+
        "res0.add(null); return res;";
      }
    static void checkParCount(Program p,MWT mwt,int expected){
      if(mwt.key().xs().size()==expected){return;}
      throw new EndError.TypeError(mwt._e().poss(),ErrMsg.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),expected));
      }
    static Map<TrustedKind,Generator> append(String s){
      String pattern="%s.append(\""+s+"\");return L42£Void.instance;";
      var u=use(pattern,Signature.sig(Mdf.Mutable,Mdf.Immutable,Void));
      return Map.of(TrustedKind.StringBuilder,u);
      }
    public static void typingUse(Program p, MWT mwt,Signature sig,boolean allowAbs){
      assert sig!=null;
      assert sig.parMdfs.size()==sig.parTs.size();
      if(sig.parTs.size()!=mwt.mh().pars().size()){
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterCountInvalid(mwt.nativeUrl(),mwt.key(),sig.parMdfs.size()));
        }
      checkMdf(mwt,sig,mwt.mh().mdf(),sig.methMdf);
      var t=mwt.mh().t();
      checkSingle(p,mwt,sig,t.p(),t.mdf(),sig.retT,sig.retMdf,allowAbs);
      for(int i:range(mwt.mh().pars().size())){
        var pi=mwt.mh().pars().get(i).p();
        var mdfi=mwt.mh().pars().get(i).mdf();
        var tti=sig.parTs.get(i);
        var tmdfi=sig.parMdfs.get(i);
        checkSingle(p,mwt,sig,pi,mdfi,tti,tmdfi,allowAbs);
        }
      }
    private static void checkGen(int i,Program p,MWT mwt,Signature sig,P pi){
      assert p.topCore().info().nativePar().size()>i;
      var pari=p.topCore().info().nativePar().get(i);
      if(pi.equals(pari)){return;}
      throw new EndError.TypeError(mwt._e().poss(),
        ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,pi,pari));            
      }
    private static void checkMdf(MWT mwt,Signature sig,Mdf mdfi,Mdf tmdfi){
      if(mdfi!=tmdfi){
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,mdfi,tmdfi));
        }
      }
    public static boolean allAbs(CoreL l){
      var noNative=l.info().nativeKind().isEmpty();
      var allAbs=l.mwts().stream().allMatch(mi->mi._e()==null);
      var hasClass=l.mwts().stream().anyMatch(mi->mi.mh().mdf().isClass());
      return noNative && allAbs && hasClass;
      }
    private static void checkSingle(Program p,MWT mwt,Signature sig,P pi,Mdf mdfi,TrustedT tti,Mdf tmdfi,boolean allowAbs){
      checkMdf(mwt,sig,mdfi,tmdfi);
      if(tti instanceof TrustedKind){
        var ki=(TrustedKind)tti;
        var li=p._ofCore(pi);
        assert li!=null:
          pi+" "+p.pop().topCore();
        var kind=li.info().nativeKind();
        var progi=p.navigate(pi.toNCs());
        if(!kind.isEmpty() && ki==TrustedKind._fromString(kind,progi)){return;}
        if(allowAbs && kind.isEmpty() && allAbs(progi.topCore())){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,pi,ki));            
        }
      if(tti==Lib){
        if(pi==P.pLibrary){return;}
        throw new EndError.TypeError(mwt._e().poss(),
         ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,pi,"Library"));            
        }
      if(tti==Void){
        if(pi==P.pVoid){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,pi,"Void"));            
        }
      if(tti==Any){
        if(pi==P.pAny){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),mwt.mh(),sig,pi,"Any"));            
        }
      if(tti==Gen1){checkGen(0,p,mwt,sig,pi);}
      if(tti==Gen2){checkGen(1,p,mwt,sig,pi);}
      if(tti==Gen3){checkGen(2,p,mwt,sig,pi);}
      if(tti==Gen4){checkGen(3,p,mwt,sig,pi);}
      if(tti==This){
        if(pi.equals(P.pThis0)){return;}
        throw new EndError.TypeError(mwt._e().poss(),
          ErrMsg.nativeParameterInvalidKind(!mwt.nativeUrl().isEmpty(),mwt.nativeUrl(),sig,mwt.key(),pi,"This"));            
        }
      }
  static Generator use(String s0,Signature sig){
    return use(p->s0,sig);
    }
  static String preOptK="""
    boolean bad=%2$s instanceof L42Fwd &&
      !(((Object)%2$s) instanceof L42NoFields<?>);
    if(!bad){return
    """;
  static String postOptK="""
    ;}
    throw new L42Error(%Gen2.wrap(new L42£LazyMsg(\"Optional value can not be initialized with a fwd\")));
    """; 
  static Function<Program,String> optChoice(String opt,String optOpt){
    return p->{
      if(!TrustedKind.isOptOpt(p,false)){return opt;}
      return optOpt;
      }; 
    }
  static Function<Program,String> mapChoice(String opt,String optOpt){
    return p->{
      var i=p.topCore().info();
      assert i.nativeKind().contains("Map");
      if(i.nativePar().size()<3){return opt;}//a better error will happen in other places
      var pOpt=i.nativePar().get(2);
      if(!pOpt.isNCs()){return opt;}//a better error will happen in other places
      if(!TrustedKind.isOptOpt(p.navigate(pOpt.toNCs()),false)){return opt;}
      return optOpt;
      }; 
    }
  static Generator use(Function<Program,String> s0,Signature sig){ return new Generator() {
    @Override public void check(boolean allowAbs,MWT mwt,J j){
      typingUse(j.p(),mwt,sig,allowAbs);
      }
    @Override public void generate(MWT mwt,J j){
      Program p=j.p();
      String s=s0.apply(p);
      s = replaceGen(j,p,s,P.pThis0,"%This");
      for(int i:range(p.topCore().info().nativePar())){
        P geni=p.topCore().info().nativePar().get(i);
        s = replaceGen(j, p, s, geni,"%Gen"+(i+1));
        }           
      j.c(s.formatted(NativeDispatch.xs(mwt).toArray()));
      }
    };}
  private static String replaceGen(J j,Program p,String s,P geni,String genName) {
    if(s.contains(genName+".")){
      String geniS=J.classNameStr(p.navigate(geni.toNCs()));
      s=s.replace(genName+".",geniS+".");
      }
    if(s.contains(genName+"::")){
      String geniS=J.classNameStr(p.navigate(geni.toNCs()));
      s=s.replace(genName+"::",geniS+"::");
      }
    if(s.contains(genName)){
      s=s.replace(genName,j.typeNameStr(geni));
      } 
    return s; 
    }
  }

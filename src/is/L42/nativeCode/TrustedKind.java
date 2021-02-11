package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.function.Function;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.General;
import is.L42.translationToJava.J;
import is.L42.typeSystem.ProgramTypeSystem;

public enum TrustedKind implements TrustedT{
  AnyKind(""){public String factory(J j,MWT mwt){throw bug();}
    @Override public boolean typePluginK(Program p,MH mh){throw bug();}
    public String defaultVal(){throw bug();}
    },
  AnyNativeKind(""){public String factory(J j,MWT mwt){throw bug();}
    @Override public boolean typePluginK(Program p,MH mh){throw bug();}
    public String defaultVal(){throw bug();}
    },
  Bool("boolean"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return false;";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    public String defaultVal(){return "false";}
    },
  Int("int"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return 0;";
    }
    public String defaultVal(){return "0";}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Long("long"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return 0;";
    }
    public String defaultVal(){return "0";}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Double("double"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return 0;";
    }
    public String defaultVal(){return "0";}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  String("String"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return \"\";";
    }
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  StringBuilder("StringBuilder"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new StringBuilder();";
    }
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    },
  TrustedIO("L42£TrustedIO"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£TrustedIO.instance;";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  BigRational("L42£BigRational"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£BigRational.ZERO;";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Meta("L42£Meta"){
    public String factory(J j,MWT mwt){
      assert mwt.key().xs().isEmpty();
      return "return new L42£Meta();";
      }
    @Override public int genExceptionNumber(){return 5;}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  LazyMessage("L42£LazyMsg"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42£LazyMsg();";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  NonDeterministicError("L42£NonDeterministicError"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42£NonDeterministicError();";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Vector("ArrayList"){@Override public String factory(J j,MWT mwt){
      assert mwt.key().xs().isEmpty();
      assert j.p().topCore().info().nativePar().size()==4;
      return OpUtils.makeVector(j,"0");      
      }
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 3;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public TrustedKind specialize(Program p,List<P>nativePars){
        if(!nativePars.get(0).equals(P.pThis0)){return this;}
        return SelfVector;
        }
    },
  SelfVector("L42£SelfVector"){@Override public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==4;
    return "return new L42£SelfVector();";      
    }
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 3;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public String typeNameStr(Program p,J j){
      return "L42£SelfVector";
      }
    },
  HIMap("L42£ImmMap"){@Override public String factory(J j,MWT mwt){return mapFactory(j,mwt);}
    @Override public int genericNumber(){return 3;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public void checkNativePars(Program p,boolean noWarning){super.checkNativePars(p,noWarning);mapCheckNativePars(p,noWarning);}
    @Override public String typeNameStr(Program p,J j){
      return typeNameStr(p,j,i->i==1?null:p.topCore().info().nativePar().get(i));
      }
    },
  HMMap("L42£MutMap"){@Override public String factory(J j,MWT mwt){return mapFactory(j,mwt);}
    @Override public int genericNumber(){return 3;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public void checkNativePars(Program p,boolean noWarning){super.checkNativePars(p,noWarning);mapCheckNativePars(p,noWarning);}
    @Override public String typeNameStr(Program p,J j){
      return typeNameStr(p,j,i->i==1?null:p.topCore().info().nativePar().get(i));
      }
    },
  HSet("L42£Set"){@Override public String factory(J j,MWT mwt){return setFactory(j,mwt);}
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public TrustedKind specialize(Program p,List<P>nativePars){
      if(!nativePars.get(0).equals(P.pThis0)){return this;}
      throw todo();//TODO:
      /*For circular sets and maps, and for any kind of nativeKind where
       * the containts are violated, we should delegate to a special
       * "broken" nativeKind, that just implements any method delegating
       * to the native expression.
       */
      }
    },
/*  SelfHSet("L42£SelfSet"){@Override public String factory(J j,MWT mwt){return setFactory(j,mwt);}
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public String typeNameStr(Program p,J j){
      return "L42£SelfSet";
      }
    },*/
  Opt("Opt"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==2;
    return "return null;";
    }
    @Override public boolean typePluginK(Program p,MH mh){return mutTypePluginK(p,mh);}
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public String typeNameStr(Program p,J j){
      var info=p.topCore().info();
      P gen1=info.nativePar().get(0);
      return optTypeNameString(p, gen1, j);
      }
    },
  Name("L42£Name"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£Name.parse(\"This\");";
    }
    @Override public int genExceptionNumber(){return 1;}
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Nested("L42£Nested"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£Nested.instanceVoid;";
    }
    @Override public int genExceptionNumber(){return 3;}//InvalidName, OutOfBound,OptNotPresent
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);} 
    },
  Type("L42£Type"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£Type.instance;";
    }
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Doc("L42£Doc"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£Doc.instance;";
    }
    @Override public int genExceptionNumber(){return 2;}//OutOfBound,OptNotPresent
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Method("L42£Method"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return L42£Method.instance;";
    }
    @Override public int genExceptionNumber(){return 1;}//OutOfBound
    @Override public boolean typePluginK(Program p,MH mh){return immTypePluginK(p,mh);}
    },
  Limit("Void"){public String factory(J j,MWT mwt){
    assert false;
    throw bug();
    }
    @Override public boolean typePluginK(Program p,MH mh){throw bug();}
    };
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public int genericNumber(){return 0;}
  public int genExceptionNumber(){return 0;}
  public abstract String factory(J j,MWT mwt);
  public abstract boolean typePluginK(Program p,MH mh);
  public String defaultVal(){return "null";}
  public static TrustedKind _fromString(String s,Program p) {
    var res=_rawFromString(s);
    if(res==null){return null;}
    return res.specialize(p,p.topCore().info().nativePar());
    }
  public TrustedKind specialize(Program p,List<P>nativePars){return this;}
  public static TrustedKind _rawFromString(String s){
    if(s.isEmpty()){return AnyKind;}
    for (TrustedKind t : TrustedKind.values()) {
      if (t.name().equals(s))return t;
      }
    return null;
    }
  public String typeNameStr(Program p,J j){
    var info=p.topCore().info();
    return typeNameStr(p,j,i->info.nativePar().get(i));
    }
  public String typeNameStr(Program p,J j,Function<Integer,P>generics){
    String res=inner;
    if(this.genericNumber()==0){return res;}
    res+="<";
    for(var i:range(this.genericNumber())){
      P pi=generics.apply(i);
      if(pi==null){continue;}
      if(!pi.isNCs()){res+=J.primitivePToString(pi);}
      else{res+=J.boxed(j.typeNameStr(p.navigate(pi.toNCs())));}
      res+=", ";
      }
    res=res.substring(0,res.length()-2)+">";
    return res;
    }
  private static boolean immTypePluginK(Program p,MH mh){return mdfTypePluginK(p,mh,Mdf.Immutable);}
  private static boolean mutTypePluginK(Program p,MH mh){return mdfTypePluginK(p,mh,Mdf.Mutable);}
  private static boolean mdfTypePluginK(Program p,MH mh,Mdf mdf){
    return mh.key().xs().isEmpty() && mdf==mh.t().mdf();
    /*if(!mh.key().xs().isEmpty()){
      throw new EndError.TypeError(poss,
        Err.nativeParameterCountInvalid(p.topCore().info().nativeKind(),mh,0));
      }
    if(mh.t().mdf()==mdf){return;}
    throw new EndError.TypeError(poss,
      Err.nativeParameterInvalidKind(p.topCore().info().nativeKind(),mh,mdf,mh.t(),mdf));
    */}
  public void checkNativePars(Program p,boolean noWarning){
    var l=p.topCore();
    var info=l.info();
    int base=genericNumber();
    for(int i:range(base,base+genExceptionNumber())){
      P pi=info.nativePar().get(i);
      if(!info.coherentDep().contains(pi)){
        throw new EndError.TypeError(l.poss(),
          ErrMsg.nativeExceptionNotCoherentDep(info.nativeKind(),pi));
        }
      var li=p.of(pi,l.poss());
      var lm=TrustedKind.LazyMessage.name();
      if(li.isFullL() || !((Core.L)li).info().nativeKind().equals(lm)){
        String msg=ErrMsg.nativeExceptionNotLazyMessage(info.nativeKind(),pi);
        errWarning(new EndError.TypeError(l.poss(),msg),noWarning);
        }
      }
    }
  public static boolean isOptOpt(Program p){
    var l=p.topCore();
    if(l.info().nativePar().size()!=2){return false;}//a better error will raise in other places
    P path=l.info().nativePar().get(0);
    return isOpt(p,path);
    }
  public static boolean isOpt(Program p,P path){
    var lO=p._ofCore(path);
    assert lO!=null;
    return lO.info().nativeKind().equals(TrustedKind.Opt.name());
    }
  //-------
  String mapFactory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==4;
    String typeName=j.typeNameStr(j.p());
    return "return new "+typeName+"();";
    }
  String setFactory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==2;
    String typeName=j.typeNameStr(j.p());
    return "return new "+typeName+"();";
    }
  String optTypeNameString(Program p,P gen1, J j) {
    if(!gen1.isNCs()){return J.primitivePToString(gen1);}
    var pLocal=p.navigate(gen1.toNCs());
    var nk=pLocal.topCore().info().nativeKind();
    var tk=TrustedKind._fromString(nk,p);
    if(tk==null){return j.typeNameStr(pLocal);}
    assert isOptOpt(p)==(tk==Opt);
    if(tk==Opt){return J.classNameStr(pLocal);}
    var boxed=J.boxed(tk.inner);
    if(boxed!=tk.inner) {return boxed;}
    return j.typeNameStr(pLocal);
    }
  void mapCheckNativePars(Program p,boolean noWarning){
    var l=p.topCore();
    var info=l.info();
    P pV=info.nativePar().get(1);
    P pO=info.nativePar().get(2);
    var lV=p._ofCore(pV);
    var lO=p._ofCore(pO);
    assert lV!=null;
    assert lO!=null;
    var isOpt=isOpt(p,pO);
    String msg=ErrMsg.nativeParameterViolatedConstraint(info.nativeKind(),pO,"to be Opt("+pV+")");
    if(!isOpt){errWarning(new EndError.TypeError(l.poss(),msg),noWarning);return;}
    if(lO.info().nativePar().isEmpty()){return;}
    //Opt will give a better error independently
    var pP=p.from(lO.info().nativePar().get(0),pO.toNCs());
    if(pP.equals(pV)){return;}
    errWarning(new EndError.TypeError(l.poss(),msg),noWarning);
    }
  void errWarning(EndError.TypeError err,boolean noWarning){
    if(noWarning){throw err;}
    ProgramTypeSystem.warningOnErr(()->{throw err;});
    }
  }
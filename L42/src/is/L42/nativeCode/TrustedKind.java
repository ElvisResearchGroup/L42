package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.tools.General;
import is.L42.translationToJava.J;

public enum TrustedKind implements TrustedT{
  AnyKind(""){public String factory(J j,MWT mwt){throw bug();}
    public String defaultVal(){throw bug();}
    },    
  Bool("boolean"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return false;";
    }
    public String defaultVal(){return "false";}
    },
  Int("int"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return 0;";
    }
    public String defaultVal(){return "0";}
    },
  String("String"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return \"\";";
    }
    @Override public int genExceptionNumber(){return 1;}
    },
  StringBuilder("StringBuilder"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new StringBuilder();";
    }},
  TrustedIO("L42£TrustedIO"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42£TrustedIO();";
    }},
  Meta("L42£Meta"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42£Meta();";
    }},  
  LazyMessage("L42£LazyMsg"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42£LazyMsg();";
    }},
  /*TODO:  well formed nativeKind need to be
   -known,
   -have the right amount of nativeGens
   -some of those gens need to be NCs//TODO
   -some of those gens need to point to other nativeKind//TODO
   -constructor have no args and return imm//TODO
  */ 
  Vector("ArrayList"){@Override public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==4;
    return OpUtils.makeVector(j,"0");      
    }
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 3;}
    },
  //TODO: how Opt work on int/float/String?
  Opt("Opt"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==2;
    return "return null;";
    }
    @Override public int genericNumber(){return 1;}
    @Override public int genExceptionNumber(){return 1;}
    @Override public String typeNameStr(Program p,J j){
      var info=p.topCore().info();
      P gen1=info.nativePar().get(0);
      if(!gen1.isNCs()){return J.primitivePToString(gen1);}
      return j.typeNameStr(p.navigate(gen1.toNCs()));
      }
    },
  Limit("Void"){public String factory(J j,MWT mwt){
    assert false;
    throw bug();
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public int genericNumber(){return 0;}
  public int genExceptionNumber(){return 0;}
  public abstract String factory(J j,MWT mwt);
  public String defaultVal(){return "null";}
  public static TrustedKind _fromString(String s) {
    if(s.isEmpty()){return AnyKind;}
    for (TrustedKind t : TrustedKind.values()) {
      if (t.name().equals(s))return t;
      }
    return null;
    }
  public String typeNameStr(Program p,J j){
    String res=inner;
    var info=p.topCore().info();
    if(this.genericNumber()==0){return res;}
    res+="<";
    for(var i:range(this.genericNumber())){
      P pi=info.nativePar().get(i);
      if(!pi.isNCs()){res+=J.primitivePToString(pi);}
      else{res+=j.typeNameStr(p.navigate(pi.toNCs()));}
      res+=", ";
      }
    res=res.substring(0,res.length()-2)+">";
    return res;
    }
  }
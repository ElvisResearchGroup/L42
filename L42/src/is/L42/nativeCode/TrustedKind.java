package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.P;
import is.L42.translationToJava.J;

public enum TrustedKind implements TrustedT{
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
    }},
  StringBuilder("StringBuilder"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new StringBuilder();";
    }},
  TrustedIO("L42TrustedIO"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new L42TrustedIO();";
    }},
  Meta("Meta"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    return "return new Meta();";
    }},  
  Vector("ArrayList"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==1;
    return "return new "+j.typeNameStr(j.p())+"();";
    }
    public int genericNumber(){return 1;}},
  Opt("Opt"){public String factory(J j,MWT mwt){
    assert mwt.key().xs().isEmpty();
    assert j.p().topCore().info().nativePar().size()==1;
    return "return null;";
    }
    public int genericNumber(){return 1;}
    public String typeNameStr(Program p,J j){
      var info=p.topCore().info();
      return j.typeNameStr(info.nativePar().get(0));
      }
    },
  Limit("Void"){public String factory(J j,MWT mwt){
    assert false;
    throw bug();
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public int genericNumber(){return 0;}
  public abstract String factory(J j,MWT mwt);
  public String defaultVal(){return "null";}
  public static TrustedKind _fromString(String s) {
    for (TrustedKind t : TrustedKind.values()) {
      if (t.name().equals(s))return t;
      }
    return null;
    }
  public String typeNameStr(Program p,J j){
    String res=inner;
    var info=p.topCore().info();
    if(info.nativePar().isEmpty()){return res;}
    res+="<";
    for(P pi:info.nativePar()){
      res+=j.typeNameStr(pi);
      res+=", ";
      }
    res=res.substring(0,res.length()-2)+">";
    return res;
    }
  }
package is.L42.nativeCode;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.L.MWT;
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
    }},  
  Limit("Void"){public String factory(J j,MWT mwt){
    assert false;
    throw bug();
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public abstract String factory(J j,MWT mwt);
  public String defaultVal(){return "null";}
  public static TrustedKind fromString(String s) {
   for (TrustedKind t : TrustedKind.values()) {
    if (t.name().equals(s))return t;
    }
   assert false:
    s;
   throw todo();
  }
 }
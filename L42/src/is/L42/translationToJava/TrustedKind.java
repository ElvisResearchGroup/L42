package is.L42.translationToJava;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.todo;

import java.util.List;

enum TrustedKind {
  Bool("boolean"){public String factory(List<String> xs){
    assert xs.size()==1;//just this
    return "return false;";
    }
    public String defaultVal(){return "false";}
    },
  Int("int"){public String factory(List<String> xs){
    assert xs.size()==1;//just this
    return "return 0;";
    }
    public String defaultVal(){return "0";}
    },
  String("String"){public String factory(List<String> xs){
    assert xs.size()==1;
    return "return \"\";";
    }},
  StringBuilder("StringBuilder"){public String factory(List<String> xs){
    assert xs.size()==1;
    return "return new StringBuilder();";
    }},

  Limit("Void"){public String factory(List<String> xs){
    assert false;
    throw bug();
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public abstract String factory(List<String> xs);
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
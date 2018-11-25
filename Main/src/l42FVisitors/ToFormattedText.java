package l42FVisitors;

import newReduction.ClassTable;

public abstract class ToFormattedText{
  protected StringBuilder result=new StringBuilder();
  protected String currentIndent="";
  protected Void nl(){result.append("\n");result.append(currentIndent);return null;}
  protected Void indent(){currentIndent+="  ";return null;}
  protected Void deIndent(){currentIndent=currentIndent.substring(2);return null;}
  protected Void c(String s){
    assert !s.startsWith(",") || ( result.charAt(result.length()-1)!='('):
      s;
    assert !s.startsWith(";") || ( result.charAt(result.length()-1)!=';'&&  result.charAt(result.length()-1)!='}'):
      s;
//    assert !s.startsWith("}") ||  ";}\n/ ".contains(result.subSequence(result.length()-1,result.length())):
//      s;
    result.append(s);
    return null;
    }
  protected Void sp(){result.append(" ");return null;}
}

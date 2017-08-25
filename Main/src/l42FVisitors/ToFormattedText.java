package l42FVisitors;

import newReduction.ClassTable;

public abstract class ToFormattedText{
  protected StringBuilder result=new StringBuilder();
  protected String currentIndent="";
  protected Void nl(){result.append("\n");result.append(currentIndent);return null;}
  protected Void indent(){currentIndent+="  ";return null;}
  protected Void deIndent(){currentIndent=currentIndent.substring(2);return null;}
  protected Void c(String s){result.append(s);return null;}
  protected Void sp(){result.append(" ");return null;}
}

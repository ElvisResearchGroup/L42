package newReduction;

import l42FVisitors.Visitor;

public abstract class L42FStringer implements Visitor<Void>{
public L42FStringer(ClassTable ct) {this.ct = ct;}
  ClassTable ct;
  StringBuilder result=new StringBuilder();
  protected String currentIndent="";
  protected Void nl(){result.append("\n");result.append(currentIndent);return null;}
  protected Void indent(){currentIndent+="  ";return null;}
  protected Void deIndent(){currentIndent=currentIndent.substring(2);return null;}
  protected Void c(String s){result.append(s);return null;}
  protected Void sp(){result.append(" ");return null;}
}

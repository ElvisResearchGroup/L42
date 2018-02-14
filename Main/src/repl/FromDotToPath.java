package repl;

import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.MethodSelector;
import ast.ErrorMessage;
import ast.PathAux;
import facade.Parser;

public class FromDotToPath {
  int thisNum=-1;
  StringBuffer pathString=new StringBuffer();
  List<Ast.C> cs;
  public FromDotToPath(String text, int row, int col) {
    String[] lines=text.split("\\r?\\n");
    String lineBefore=lines[row].substring(0, col);

    String reverseLineBefore=reverse(lineBefore);
    String reverseAndReplaced=swapParenthesis(reverseLineBefore);

    parse(reverseAndReplaced);
    cs=PathAux.parseValidCs(pathString.substring(0, pathString.length()-1).toString());
    }

  private void parse(String input) {
    String[] tokens=input.split("\\.");
    for(String token : tokens) {
      if(!parseToken(token)) {break;}
    }
  }

  private boolean parseToken(String token) {
    if(token.startsWith("(")) { parseMCall(token); return true; }
    StringBuffer tb=new StringBuffer();
    for(int i:token.codePoints().boxed().collect(Collectors.toList())) {
      char c=toChar(i);
      if(PathAux.isValidPathChar(c)|| c=='#') {tb.appendCodePoint(i);}
      else {break;}
    }
    boolean unskipped=token.length()==tb.length();
    token=tb.reverse().toString();
    if(token.isEmpty()) {return false;}//and stop processing the for
    char c=toChar(token.codePointAt(0));
    boolean up=PathAux.isValidPathStart(c);
    boolean down=MethodSelector.checkX(""+c,true);
    boolean out=PathAux.isValidOuter(token);
    //assert boolToInt(up) + boolToInt(up) + boolToInt(up) == 1;
    if(out) { parseThis(token); return unskipped; }
    if(up) { parsePath(token); return unskipped; }
    if(down) { parseX(token); return unskipped; }
    throw new IllegalArgumentException();
  }

  private int boolToInt(boolean up) {
    return up ? 1 : 0;
  }

  private void parseMCall(String token) {
    String noFirstChar=token.substring(1, token.length());
    ErrorMessage.UnclosedParenthesis err= Parser._checkForBalancedParenthesis(noFirstChar);
    if(err==null) {throw new IllegalArgumentException();}
    System.out.println(noFirstChar);
    System.out.println(err.getPos());
  }

  private void parseThis(String token) {
    if(thisNum!=-1){throw new IllegalArgumentException();}
    thisNum=PathAux.getThisn(token);
  }
  private void parsePath(String token) {
    StringBuffer b=new StringBuffer(token);
    //for(int i:token.codePoints().boxed().collect(Collectors.toList())) {
    //  char c=toChar(i);
    //  if(PathAux.isValidPathChar(c)) {b.appendCodePoint(i);}
    //  else {break;}
    //}
    //b=b.reverse();
    if(!PathAux.isValidClassName(b.toString())) {throw new IllegalArgumentException();}
    b.append(".");
    pathString=b.append(pathString);


  }

  private void parseX(String token) {
    // TODO Auto-generated method stub
    throw new IllegalArgumentException();
  }

  private char toChar(int codePoint) {
    char[]cs=Character.toChars(codePoint);
    if(cs.length!=1) {throw new IllegalArgumentException();}
    return cs[0];
  }

  private String reverse(String input) {
    return new StringBuilder(input).reverse().toString();
  }

  private static String swapParenthesis(String input) {
    StringBuffer b=new StringBuffer();
    input.codePoints().forEachOrdered(i->{
      if(i=='{') {b.append("}");}
      else if(i=='}'){b.append("{");}
      else if(i=='['){b.append("]");}
      else if(i==']'){b.append("[");}
      else if(i=='('){b.append(")");}
      else if(i==')'){b.append("(");}
      else {b.appendCodePoint(i);}
    });
    return b.toString();
  }

}

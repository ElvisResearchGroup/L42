package repl;

import java.util.ArrayList;
import java.util.Arrays;
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
  List<Ast.MethodSelector> ms=new ArrayList<>();

  public FromDotToPath(String text, int row, int col) {
    String[] lines=text.split("\\r?\\n");
    String lineBefore=lines[row].substring(0, col);

    String reverseLineBefore=reverse(lineBefore);
    String reverseAndReplaced=swapParenthesis(reverseLineBefore);

    parse(reverseAndReplaced);
    if(pathString.length()==0) {throw new IllegalArgumentException();}
    cs=PathAux.parseValidCs(pathString.substring(0, pathString.length()-1).toString());
    }

  private void parse(String input) {
    String[] tokens=input.split("\\.");
    for(String token : tokens) {
      if(!parseToken(token)) {break;}
    }
  }

  private boolean parseToken(String token) {
    if(token.startsWith("(")) {
      parseMCall(token);
      return true;
    }
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
    boolean down=MethodSelector.checkX(""+c,true);
    boolean out=PathAux.isValidOuter(token);
    boolean up=!out && PathAux.isValidPathStart(c);
    assert boolToInt(up) + boolToInt(down) + boolToInt(out) == 1;
    if(out) { parseThis(token); return unskipped; }
    if(up) { parsePath(token); return unskipped; }
    if(down) { parseX(token); return unskipped; }
    throw new IllegalArgumentException();
  }

  private int boolToInt(boolean bool) {
    return bool ? 1 : 0;
  }

  private void parseMCall(String token) {
    String noFirstChar=token.substring(1, token.length());
    String methRev=noFirstChar.substring(skipArgs(noFirstChar), noFirstChar.length());
    String methName=reverse(methRev);
    String mArgs=noFirstChar.substring(0, noFirstChar.length()-(methName.length()+1));
    ParseMArg mArg=new ParseMArg(mArgs);

    boolean isM=MethodSelector.checkX(methName,true);
    if(!isM) {throw new IllegalArgumentException();}
    String args=mArg.xs.toString().replace(" ", "");
    args=args.substring(1, args.length()-1);
    ms.add(0, Ast.MethodSelector.parse(methName+"("+args+")"));

    System.out.println(noFirstChar);

  }

  static int skipArgs(String noFirstChar) {
    try{
      Parser.checkForBalancedParenthesis(noFirstChar);
    } catch(ErrorMessage.UnclosedParenthesis e) {
      System.out.println(e.getClass().getSimpleName());
      System.out.println(e.getPos());
    } catch(ErrorMessage.UnclosedStringLiteral e) {
      System.out.println(e.getClass().getSimpleName());
      System.out.println(e.getPos());
    } catch(ErrorMessage.UnopenedParenthesis e) {
      System.out.println(e.getClass().getSimpleName());
      System.out.println(e.getPos());
      return e.getPos();
    } catch(ErrorMessage.ParenthesisMismatchRange e) {
      System.out.println(e.getClass().getSimpleName());
      System.out.println("Pos1: "+e.getPos1()+" Pos2: "+e.getPos2());
    }
    throw new IllegalArgumentException();
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

  static char toChar(int codePoint) {
    char[]cs=Character.toChars(codePoint);
    if(cs.length!=1) {throw new IllegalArgumentException();}
    return cs[0];
  }

  static String reverse(String input) {
    return new StringBuilder(input).reverse().toString();
  }

  static String swapParenthesis(String input) {
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

package repl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ast.Ast.MethodSelector;
import java.util.stream.Collectors;


public class ParseMArg {

  private static enum InState {
    MultiLineComment,StringLiteral,Out,StartX,InX,PostX
    }

  List<String> xs=new ArrayList<>();
  InState current=InState.Out;
  StringBuffer b=new StringBuffer();
  char former=' ';
  int pos=0;
  boolean usefulDone=false;

  public ParseMArg(String mArg) {
    for(int i:mArg.codePoints().boxed().collect(Collectors.toList())) {
      char c=FromDotToPath.toChar(i);
      switchState(i,c);
      former=c;
      pos++;
    }
    switch(current) {
      case Out:
        if(usefulDone) {xs.add(0, "that");}
      case PostX:
        break;
      case InX:
        xs.add(0, b.toString());
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private void switchState(int i,char c) {
    switch(current) {
    case MultiLineComment:
      if(c=='/' && former=='*') {out();}
      return;

    case StringLiteral:
      if(c=='"') {out();}
      return;

    case Out: switch(c) {
      case '"': stringLiteral(); return;

      case '*': if(former=='/') {multiLineComment();} return;

      case ':': startX();

      case ' ': case ',': case '/': return;

      default: usefulDone=true;
      }
      return;

    case InX:
      if(MethodSelector.checkX(""+c, true)) {b.appendCodePoint(i);}
      else {
        postX();
        xs.add(0, b.toString());
        b=new StringBuffer();
        usefulDone=false;
      }
      return;
    case PostX: switch(c) {
      case '"': stringLiteral(); return;

      case '*': if(former=='/') {multiLineComment();} return;

      case ':': throw new IllegalArgumentException();

      case ' ': case ',': return;

      default: out(); usefulDone=true;
      }
      return;

    case StartX:
      if(MethodSelector.checkX(""+c, true)) {
        inX();
        b.appendCodePoint(i);
        return;}
      switch(c) {
        case ',': case ' ': return;

        default: throw new IllegalArgumentException();
      }
    }
  }
  private void multiLineComment() {
    current=InState.MultiLineComment;
  }
  private void stringLiteral() {
    current=InState.StringLiteral;
    usefulDone=true;
  }
  private void out() {
    current=InState.Out;
  }
  private void inX() {
    current=InState.InX;
    usefulDone=true;
  }
  private void startX() {
    current=InState.StartX;
    usefulDone=true;
  }
  private void postX() {
    current=InState.PostX;
    usefulDone=true;
  }
}


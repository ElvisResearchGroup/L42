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

  public ParseMArg(String mArg) {
    for(int i:mArg.codePoints().boxed().collect(Collectors.toList())) {
      char c=FromDotToPath.toChar(i);
      switchState(i,c);
      former=c;
      pos++;
    }
    switch(current) {
      case Out:
        xs.add("that");
      case PostX:
        break;
      case InX:
        xs.add(b.toString());
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  private void switchState(int i,char c) {
    switch(current) {
    case MultiLineComment:
      if(c=='/' && former=='*') {current=InState.Out;}
      break;

    case StringLiteral:
      if(c=='"') {current=InState.Out;}
      break;

    case Out: switch(c) {
      case '"': current=InState.StringLiteral; break;

      case '*': if(former=='/') {current=InState.MultiLineComment;} break;

      case ':': current=InState.StartX;
      }
      break;

    case InX:
      if(MethodSelector.checkX(""+c, true)) {b.appendCodePoint(i);}
      else {
        current=InState.PostX;
        xs.add(b.toString());
        b=new StringBuffer();
      }
      break;
    case PostX: switch(c) {
      case '"': current=InState.StringLiteral; break;

      case '*': if(former=='/') {current=InState.MultiLineComment;} break;

      case ':': throw new IllegalArgumentException();

      case ' ': case ',': break;

      default: current=InState.Out;
      }
      break;

    case StartX:
      if(MethodSelector.checkX(""+c, true)) {
        current=InState.InX;
        b.appendCodePoint(i); }
      else {
        switch(c) {
        case ',': case ' ': break;

        default: throw new IllegalArgumentException();
      }}
    }
  }
}


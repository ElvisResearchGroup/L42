package is.L42.common;

import static is.L42.tools.General.L;

import java.net.URI;
import java.util.List;
import java.util.Stack;

import is.L42.common.Balance.State;
import is.L42.generated.Pos;

public class Balance{
  private static Pos pos(URI fileName,int line,int col){
    return new Pos(fileName,line,col);
    }
  URI fileName;Balance(URI fileName){this.fileName=fileName;}
  int currentLine=1;
  int currentPos=0;
  public static enum State{None,CommSL,CommML,StrSL,StrMLText,StrMLPadding,Round,Square,Curly}
  Stack<PosP>s=new Stack<>(); {push(State.None);}
  void nextPos(){currentPos+=1;}
  boolean fails(State... fs){
    PosP cs=s.peek();
    for(State si:fs){if(si==cs.state){return true;}}
    return false;
    }
  void failPadding(String token){
    if(fails(State.StrMLPadding)){
      throw new EndError.NotWellFormed(
        L(pos(fileName,this.currentLine,this.currentPos)),
        "Invalid character in multiline string indentation: \'"+token+"\', only spacing and \'|\' expected");
      }
    }
  boolean popIf(State ...ps){
    PosP cs=s.peek();
    for(State si:ps){
      if(si==cs.state){s.pop();return true;}
      }
    return false;
    }
  void swapIf(State ns,State ...ss){
    PosP cs=s.peek();
    for(State si:ss){
      if(si==cs.state){s.pop();push(ns);return;}
      }
    }
  boolean pushIf(State pushed,State ... ps){
    PosP cs=s.peek();
    for(State si:ps){
      if(si==cs.state){push(pushed);return true;}
      }
    return false;
    }
  void push(State state){
    s.push(new PosP(this.currentLine,this.currentPos,state));
    }
  private void parMismatch(State s2) {
    String token2=s2==State.Round?")":s.peek().state==State.Square?"]":"}";
    if(s.peek().state==State.None){
      throw new EndError.NotWellFormed(L(pos(fileName,currentLine,currentPos)),"Unopened parenthesis: "+token2);
      }
    String token1=s.peek().state==State.Round?"(":s.peek().state==State.Square?"[":"{";
    throw new EndError.NotWellFormed(
      List.of(
        pos(fileName,s.peek().line,s.peek().pos),
        pos(fileName,currentLine,currentPos)
        ),"Parenthesis mismatch range:\n\'"+token1+"\' in line "+s.peek().line
        +"\n is closed by \'"+token2+"\' in line "+currentLine
        );
    }
  void newLine(){
    currentLine+=1;
    currentPos=0;
    //fail in StrSL
    if(fails(State.StrSL)){throw new EndError.NotWellFormed(
      L(pos(fileName,currentLine-1,currentPos)),
      "Unclosed string literal"
      );}
      //swap StrMLPadding if State.StrMLText
    swapIf(State.StrMLPadding,State.StrMLText);
    //pop inCommSL
    popIf(State.CommSL);
    }
  void multilineStart(){
    swapIf(State.StrMLText,State.StrMLPadding);
    }
  void doubleQuote(){// "
    //pop in StrSL,StrMLPadding
    if(popIf(State.StrSL,State.StrMLPadding)){return;}
    //push State.StrSL if State.Round,State.Square,State.Curly
    pushIf(State.StrSL,State.None,State.Round,State.Square,State.Curly);
    }
  void doubleQuoteNL(){// was " now is """%*
    //pop in StrSL,StrMLPadding
    if(popIf(State.StrSL,State.StrMLPadding)){return;}
    //push State.StrSL if State.Round,State.Square,State.Curly
    pushIf(State.StrMLPadding,State.None,State.Round,State.Square,State.Curly);
    }
  void barBar(){// //
    //fail in StrMLPadding
    failPadding("//");
    //push CommSL in Round,Square,Curly
    pushIf(State.CommSL,State.None,State.Round,State.Square,State.Curly);
    }
  void barStarO(){// /*
    //fail in StrMLPadding
    failPadding("/*");
    //push CommML in Round,Square,Curly
    pushIf(State.CommML,State.None,State.Round,State.Square,State.Curly);
    }
  void barStarC(){// */
    //fail in StrMLPadding, Round,Square,Curly
    failPadding("*/");
    //pop in CommML
    popIf(State.CommML);
    }
  void oRound(){// (
    //fail in StrMLPadding
    failPadding("(");
    //push Round in Round,Square,Curly
    pushIf(State.Round,State.None,State.Round,State.Square,State.Curly);
    }
  void cRound(){// )
    //fail in StrMLPadding
    failPadding(")");
    //pop in Round
    if(popIf(State.Round)){return;}
    if(fails(State.None,State.Square,State.Curly)){parMismatch(State.Round);}
    }
  void oSquare(){// [
    //fail in StrMLPadding
    failPadding("(");
    //push Square in Round,Square,Curly
    pushIf(State.Square,State.None,State.Round,State.Square,State.Curly);
    }
  void cSquare(){// ]
    //fail in StrMLPadding
    failPadding("[");
    //pop in Square
    if(popIf(State.Square)){return;}
    if(fails(State.None,State.Round,State.Curly)){parMismatch(State.Square);}
    }
  void oCurly(){// {
    //fail in StrMLPadding
    failPadding("{");
    //push Curly in Round,Square,Curly
    pushIf(State.Curly,State.None,State.Round,State.Square,State.Curly);
    }
  void cCurly(){// }
    //fail in StrMLPadding
    failPadding("(");
    //pop in Curly
    if(popIf(State.Curly)){return;}
    if(fails(State.None,State.Round,State.Square)){parMismatch(State.Curly);}
    }
  void space(){//all ok
    }
  void comma(){//all ok
    }
  void nonSpacing(String c){
    failPadding(c);
    }
  public static int isMultilineOpening(char[] cs,int i,State s){
    assert cs[i]=='\"';//next is either new line or a bunch of % followed by newline
    if(s!=State.None && s!=State.Round && s!=State.Square &&s!=State.Curly){return 0;}
    char waiting='%';
    i+=1;
    while(i<cs.length){
      if(cs[i]=='\n'){return i;}
      if(cs[i]==waiting){i++;continue;}
      waiting=' ';
      if(cs[i]==waiting){i++;continue;}
      return 0;
      }
    return 0;
    }
  public static void checkForBalancedParenthesis(URI fileName,String s){
    int i0=s.indexOf("{");
    int in=s.lastIndexOf("}");
    if(i0==0 && in==s.length()-1){s=s.substring(1,in);}
    Balance d=new Balance(fileName);
    char[] cs=s.toCharArray();
    for(int i=0;i<cs.length;i++){
      char c=cs[i];
      char cp1=' ';
      d.nextPos();
      if(i+1<cs.length){cp1=cs[i+1];}
      switch (c){
        case '\n': d.newLine();
        break;case '/':
          if(cp1=='/'){i++;d.barBar();}
          else if(cp1=='*'){i++;d.barStarO();}
          else d.nonSpacing("/");
        break;case '{':d.oCurly();
        break;case '[':d.oSquare();
        break;case '(':d.oRound();
        break;case '}':d.cCurly();
        break;case ']':d.cSquare();
        break;case ')':d.cRound();
        break;case '\"':
          int newI=isMultilineOpening(cs,i,d.s.peek().state);
          if(newI!=0){i=newI;d.currentLine+=1;d.doubleQuoteNL();}
          else d.doubleQuote();
      break;case '|':d.multilineStart();
      break;case '*':
        if(cp1=='/'){i++;d.barStarC();}
        else d.nonSpacing("*");
      break;case ' ':d.space();
      break;case ',':d.comma();
      break;default: d.nonSpacing(c+"");
      }
    }
  if(d.s.peek().state!=State.None){
    State f=d.s.peek().state;
    String token=f==State.Round?"(":f==State.Square?"[":f==State.Curly?"{":f==State.CommML?"/*":"string literal";
    throw new EndError.NotWellFormed(L(pos(fileName,d.s.peek().line,d.s.peek().pos)),"Unclosed parenthesis: "+token);
    }
  }
}
class PosP{
  PosP(int line,int pos,State state){this.line=line;this.pos=pos;this.state=state;}
  final int line;
  final int pos;
  final State state;
  }
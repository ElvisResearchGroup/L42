package is.L42.visitors;

import is.L42.common.ErrorReporting;
import is.L42.common.Parse;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Full;
import is.L42.generated.Full.E;
import is.L42.generated.Full.EString;
import is.L42.generated.Pos;
import static is.L42.tools.General.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringInterpolation {
  StringInterpolation(String fileName, EVoid eVoid, Pos pos, StringBuilder errors){  
    this.fileName=fileName;
    this.eVoid=eVoid;
    this.pos=pos;
    this.errors=errors;
    }
  List<StringBuilder>ss=new ArrayList<>(Arrays.asList(new StringBuilder()));
  List<StringBuilder>es=new ArrayList<>();
  void appendChar(char c){
    ss.get(ss.size()-1).append(c);
    extraPos(c);
    }
  void appendExp(char c){
    es.get(es.size()-1).append(c);
    extraPos(c);
    }
  void extraPos(char c){
    if(c=='\n'){extraPos.append(c);}
    else{extraPos.append(' ');}
    }
  StringBuffer extraPos=new StringBuffer();
  void moreExp(){es.add(new StringBuilder());}
  void moreChar(){ss.add(new StringBuilder());}
  int openR=0;
  int openS=0;
  int openC=0;
  void open(char c){
    if (c=='('){openR+=1;}
    if (c=='['){openS+=1;}
    if (c=='{'){openC+=1;}
    }
  void close(char c){
    if (c==')'){openR-=1;}
    if (c==']'){openS-=1;}
    if (c=='}'){openC-=1;}
    }
  boolean isTerminal(char former,char c){
    if(openR<0 || openS<0 || openC<0){
      return true;
      }
    if(openR==0 && openS==0 && openC==0){
      if(former!=')' && former!=']'){
        return terminals.indexOf(c)!=-1;
        }
      return c!='.' && c!='(' && c!='[';
      }
    return false;
  }
  private final String terminals=" , \n !~\"%&/=?'^*-@><;:";
  String symbol="%";
  private static enum Mode{
    Str{public int run(StringInterpolation self,String s,int i){
      char c=s.charAt(i);
      if(!s.startsWith(self.symbol,i)){
        self.appendChar(c);
        return i;
        }
      i+=self.symbol.length()-1;
      char next=s.charAt(Math.min(i+1,s.length()-1));
      self.moreExp();
      if(next=='('){self.mode=Mode.Round;}
      else{self.mode=Mode.Expr;}
      return i;
      }},
    Expr{public int run(StringInterpolation self,String s,int i){
      char c=s.charAt(i);
      char former=s.charAt(i-1);
      self.open(c);
      if(!self.isTerminal(former,c)){
        self.appendExp(c);
        self.close(c);
        return i;
        }
      self.modeToStr();
      self.close(c);
      return i-1;
      }},
    Round{public int run(StringInterpolation self,String s,int i){
      char c=s.charAt(i);
      char former=' ';
      self.open(c);
      self.close(c);
      if(!self.isTerminal(former,c) && self.openR!=0){
        self.appendExp(c);
        return i;
        }
      self.modeToStr();
      if(self.openR!=0){return i-1;}
      self.appendExp(c);
      return i;
      }};
    abstract int run(StringInterpolation self,String s,int i);
    };
  Mode mode=Mode.Str;
  boolean topPar=false;
  final String fileName;
  final EVoid eVoid;
  final Pos pos;
  final StringBuilder errors;
  void modeToStr(){
    openR=0;
    openS=0;
    openC=0;
    mode=Mode.Str;
    moreChar();
    }
  public EString supParse(String s) {
    for(int i=0;i<s.length();i++){
      i=mode.run(this, s, i);
      }
    if(es.size()==ss.size()){moreChar();}
    assert es.size()+1==ss.size(): es+" "+ss;
    List<Full.E> ees=L(c->{
      c.add(eVoid);
      for(var buf:es){parseInterpolatedE(c, buf);}
      });
    return new Full.EString(pos, ees,L(ss,(c,si)->c.add(si.toString())));
    }
  private void parseInterpolatedE(List<E> c, StringBuilder buf) {
    var b0=FullL42Visitor.fixPos(pos);
    b0.append(extraPos);
    b0.append(buf);
    var res=Parse.e(fileName,b0.toString());
    if(res.hasErr()){
      String mini=ErrorReporting.trimExpression(buf.toString());
      String msg="Error: ill formed string interpolation:"+mini+"\n";
      if(!res.errorsTokenizer.isEmpty()){msg+=res.errorsTokenizer+"\n";}
      if(!res.errorsParser.isEmpty()){msg+=res.errorsParser+"\n";}
      if(!res.errorsVisitor.isEmpty()){msg+=res.errorsVisitor+"\n";}
      this.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      c.add(eVoid);
      return;
      }
    c.add(res.res); 
    }
  }
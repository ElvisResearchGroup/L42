package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.S;
import is.L42.generated.X;

public class NameMangling {
  private static final S hashApply=new S("#apply",L(),-1);
  public static S hashApply(){return hashApply;}
  public static S keyOf(Op _op,int n,S s){
    if(_op==null && !s.m().isEmpty()){return s;}
    if(_op==null && s.m().isEmpty()){return hashApply.withXs(s.xs());}
    return s.withM(methName(_op,n));
    }
  public static String methName(Op op,int n){
    if(n==-1){n=0;}
    return "#"+op.name().toLowerCase()+n;
    }
  public static S methName(String hint,Mdf _mdf){
    if(_mdf==null){return S.parse("#"+hint+"#default()");}
    return S.parse("#"+hint+"#"+_mdf.inner+"()");
    }

  public static S charName(int c){
    if(Character.isLowerCase(c)){return new S("#l"+Character.toString(c),L(),-1);}
    if(Character.isUpperCase(c)){return new S("#u"+Character.toString(c),L(),-1);}
    if(Character.isDigit(c)){return new S("#d"+Character.toString(c),L(),-1);}
    return new S("#s"+desugarSymbol(c),L(),-1);
    }
  public static String desugarSymbol(int c){
    return switch(c){
      case '+'->"plus";
      case '-'->"less";
      case '~'->"tilde";
      case '!'->"bang";
      case '&'->"and";
      case '|'->"or";
      case '<'->"left";
      case '>'->"right";
      case '='->"equal";
      case '*'->"times";
      case '/'->"divide";
      case '('->"oRound";
      case ')'->"cRound";
      case '['->"oSquare";
      case ']'->"cSquare";
      case '{'->"oCurly";
      case '}'->"cCurly";
      case '\"'->"dQuote";
      case '\''->"sQuote";
      case '`'->"hQuote";
      case '?'->"qMark";
      case '^'->"hat";
      case ','->"comma";
      case ';'->"semicolon";
      case ':'->"colon";
      case '.'->"dot";
      case '_'->"underscore";
      case '#'->"hash";
      case '@'->"at";
      case '$'->"dollar";
      case '%'->"%";
      case '\\'->"backSlash";
      case ' '->"space";
      case '\n'->"newLine";
      default->{
        assert false:Character.toString(c);
        throw bug();
        }
      };
    }

  public static S shortCircuit(Op op){
    return new S("#shortCircut#"+op.name().toLowerCase(),L(),-1);
    }
  public static S shortResult(Op op){
    return new S("#shortResult#"+op.name().toLowerCase(),L(),-1);
    }
  public static S shortProcess(Op op){
    return new S("#shortProcess#"+op.name().toLowerCase(),L(),-1);
    }
  public static S methNameTrim(X x) {
    assert x!=null;
    String s=x.inner();
    String base=s.replaceAll("\\d*$","");//remove all trailing digits
    return new S(base,L(),-1);
    }

}

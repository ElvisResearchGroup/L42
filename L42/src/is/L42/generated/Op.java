package is.L42.generated;
import static is.L42.tools.General.*;

public enum Op {
  Tilde("~", OpKind.Unary),//
  Bang("!", OpKind.Unary),//

  AndAnd("&&", OpKind.BoolOp),//
  OrOr("||", OpKind.BoolOp),//
  MinusGT("->",OpKind.BoolOp),//
  EqualEqual("==", OpKind.RelationalOp),//
  LT("<", OpKind.RelationalOp),//
  GT(">",OpKind.RelationalOp),//
  GTEqual(">=", OpKind.RelationalOp),//
  LTEqual("<=", OpKind.RelationalOp),//
  EqualGT("=>", OpKind.RelationalOp),//
  In("in", OpKind.RelationalOp),//
  BangEqual("!=",OpKind.RelationalOp),//

  Plus("+",OpKind.DataLeftOp),//
  Minus("-",OpKind.DataLeftOp),//
  Times("*",OpKind.DataLeftOp),//
  Divide("/",OpKind.DataLeftOp),//
  GTGT(">>",OpKind.DataLeftOp),//

  Hat("^",OpKind.DataRightOp),//
  Colon(":",OpKind.DataRightOp),//
  LTLT("<<", OpKind.DataRightOp),//
  PlusPlus("++",OpKind.DataRightOp),//
  MinusMinus("--",OpKind.DataRightOp),//
  TimesTimes("**",OpKind.DataRightOp),//

  ColonEqual(":=",OpKind.OpUpdate),//
  HatEqual("^=",OpKind.OpUpdate),//
  LTLTEqual("<<=",OpKind.OpUpdate),//
  PlusEqual("+=",OpKind.OpUpdate),//
  MinusEqual("-=",OpKind.OpUpdate),//
  TimesEqual("*=",OpKind.OpUpdate),//
  DivideEqual("/=",OpKind.OpUpdate),//
  PlusPlusEqual("++=",OpKind.OpUpdate),//
  MinusMinusEqual("--=",OpKind.OpUpdate),//
  TimesTimesEqual("**=",OpKind.OpUpdate),//
  GTGTEqual(">>=",OpKind.OpUpdate);//

  public final String inner;
  public final OpKind kind;
  public static enum OpKind {
    Unary(false,null),
    BoolOp(true,false),
    RelationalOp(false,null),
    DataLeftOp(false,true),
    DataRightOp(false,false),
    OpUpdate(false,null);
    public final boolean shortCircuted;
    public final Boolean leftAssociative;
    public boolean isShortCircuted(){return shortCircuted;}
    public boolean isLeftAssociative(){return leftAssociative==Boolean.TRUE;}
    public boolean isRightAssociative(){return leftAssociative==Boolean.FALSE;}
    public boolean isUnassociative(){return leftAssociative==null;}
    OpKind(boolean shortCircuted,Boolean leftAssociative) {
      this.shortCircuted = shortCircuted;
      this.leftAssociative = leftAssociative;
      }
    }
  Op(String inner, OpKind kind) {
   this.inner = inner;
   this.kind = kind;
  }

  public static Op fromString(String s) {
   for (Op op : Op.values()) {
     if (op.inner.equals(s))return op;
     }
   throw bug();
  }
  public Op nonEqOpVersion(){
    if(this.kind!=OpKind.OpUpdate){return this;}
    assert this!=Op.ColonEqual;
    return Op.fromString(inner.substring(0,inner.length()-1));
    }
 }

package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;


 public static enum Op {
  Tilde("~", OpKind.Unary),//
  Bang("!", OpKind.Unary),//

  EqualGT("=>", OpKind.BoolOp),//
  AndAnd("&&", OpKind.BoolOp),//
  OrOr("||", OpKind.BoolOp),//
  EqualEqual("==", OpKind.RelationalOp),//
  LT("<", OpKind.RelationalOp),//
  GT(">",OpKind.RelationalOp),//
  GTEqual(">=", OpKind.RelationalOp),//
  LTEqual("<=", OpKind.RelationalOp),//
  In("in", OpKind.RelationalOp),//
  BangEqual("!=",OpKind.RelationalOp),//

  Plus("+",OpKind.DataLeftOp),//
  Minus("-",OpKind.DataLeftOp),//
  Times("*",OpKind.DataLeftOp),//
  Divide("/",OpKind.DataLeftOp),//
  GTGT(">>",OpKind.DataLeftOp),//
  MinusGT("->",OpKind.DataLeftOp),//

  Hat("^",OpKind.DataRightOp),//
  Colon(":",OpKind.DataRightOp),//
  LTMinus("<-",OpKind.DataRightOp),//
  LTLT("<<", OpKind.DataRightOp),//
  PlusPlus("++",OpKind.DataRightOp),//
  MinusMinus("--",OpKind.DataRightOp),//
  TimesTimes("**",OpKind.DataRightOp),//

  ColonEqual(":=",OpKind.OpUpdate),//
  HatEqual("^=",OpKind.OpUpdate),//
  LTMinusEqual("<-=",OpKind.OpUpdate),//
  LTLTEqual("<<=",OpKind.OpUpdate),//
  PlusEqual("+=",OpKind.OpUpdate),//
  MinusEqual("-=",OpKind.OpUpdate),//
  TimesEqual("*=",OpKind.OpUpdate),//
  DivideEqual("/=",OpKind.OpUpdate),//
  PlusPlusEqual("++=",OpKind.OpUpdate),//
  MinusMinusEqual("--=",OpKind.OpUpdate),//
  TimesTimesEqual("**=",OpKind.OpUpdate),//
  GTGTEqual(">>=",OpKind.OpUpdate),//
  MinusGTEqual("->=",OpKind.OpUpdate);//

  public final String inner;
  public final OpKind kind;
  public final boolean shortCircuted;// false for <<,<, <=,<<=,><> etc
  public final Boolean leftAssociative;// false for ++ << >> ** ==
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
      this.shorCircuted = shorCircuted;
      this.leftAssociative = leftAssociative;
      }
    }
  Op(String inner, OpKind kind, Boolean leftAssociative) {
   this.inner = inner;
   this.kind = kind;
   this.shorCircuted = shorCircuted;
   this.leftAssociative = leftAssociative;
  }

  public static Op fromString(String s) {
   for (Op op : Op.values()) {
     if (op.inner.equals(s))return op;
     }
   throw bug();
  }
  public Op nonEqOpVersion(){
    if(this.kind!=OpKind.EqOp){return this;}
    assert this!=Op.ColonEqual;
    return Op.fromString(inner.substring(0,inner.length()-1));
    }
 }

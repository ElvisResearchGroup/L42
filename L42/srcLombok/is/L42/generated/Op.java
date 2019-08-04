package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;


 public static enum Op {
  Tilde("~", OpKind.Unary, false, null),//
  Bang("!", OpKind.Unary, false, null),//
  
  EqualGT("=>", OpKind.BoolOp, true,false),//
  AndAnd("&&", OpKind.BoolOp, true,false),//
  OrOr("||", OpKind.BoolOp, true,false),//

   OP2 ::= == | < | > | >= | <= | in | != // unassiociative
  EqualEqual("==", OpKind.RelationalOp, false, null),//
  BangEqual("!=",OpKind.RelationalOp, false, null),//
  LT("<", OpKind.RelationalOp, false, null),//
  GT(">",OpKind.RelationalOp, false, null),//
  GTEqual(">=", OpKind.RelationalOp, false, null),//
  LTEqual("<=", OpKind.RelationalOp, false, null),//
//HERE: I should decide; is 'in' an operator? is !> a thing?
// should I allow both | and ||?

  LTLT("<<", OpKind.RelationalOp, false, false,false),//
  GTGT(">>",OpKind.RelationalOp, true, false,false),//
  LTLTEqual("<<=",OpKind.RelationalOp,false,true,false),//
  GTGTEqual(">>=",OpKind.RelationalOp,true,true,false),//

  OP1 ::= + | - | * | / | >> | -> // left associative   
  OP0 ::= ^ | : | <- | << | ++ | -- | **   // right associative

  And("&", OpKind.BoolOp, true,true,false),//
  Or("|", OpKind.BoolOp, true, true,false),//



  Plus("+", OpKind.DataOp, true, true,false),//
  Minus("-", OpKind.DataOp, true,true,false),//
  Times("*", OpKind.DataOp, true, true,false),//
  Divide("/", OpKind.DataOp,true, true,false),//
  PlusPlus("++",OpKind.DataOp, true, false,false),//
  MinusMinus("--",OpKind.DataOp, true, false,false),//
  TimesTimes("**",OpKind.DataOp, true,false,false),//
  BabelFishL("<><",OpKind.DataOp, true,false,false),//
  BabelFishR("><>",OpKind.DataOp, false,false,false),//

  PlusEqual("+=", OpKind.EqOp,true, true,false),//
  MinusEqual("-=",OpKind.EqOp, true,true,false),//
  TimesEqual("*=",OpKind.EqOp,true,true,false),//
  DivideEqual("/=",OpKind.EqOp,true,true,false),//
  AndEqual("&=",OpKind.EqOp,true,true,false),//
  OrEqual("|=",OpKind.EqOp,true,true,false),//
  PlusPlusEqual("++=",OpKind.EqOp,true,true,false),//
  MinusMinusEqual("--=",OpKind.EqOp,true,true,false),//
  TimesTimesEqual("**=",OpKind.EqOp,true,true,false),//
  ColonEqual(":=",OpKind.EqOp,true,true,false),//
  BabelFishLEqual("<><=",OpKind.EqOp, true,false,false),//
  BabelFishREqual("><>=",OpKind.EqOp, true,false,false);//

  public final String inner;
  public final OpKind kind;
  public final boolean shortCircuted;// false for <<,<, <=,<<=,><> etc
  public final Boolean leftAssociative;// false for ++ << >> ** ==
  public static enum OpKind {Unary, BoolOp, RelationalOp, DataOp, EqOp}
  Op(String inner, OpKind kind, boolean shorCircuted, Boolean leftAssociative) {
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

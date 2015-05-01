package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ast.Ast.HistoricType;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;

@SuppressWarnings("serial") public abstract class ErrorMessage extends RuntimeException {
  public abstract int getErrCode();
  @Override public String getMessage() {
    return super.getMessage();//+this.toString();
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UserLevelError extends ErrorMessage {
    static public enum Kind {
      WellFormedness, TypeError, MetaError, Unclassified
    }
    public int getErrCode() {
      return internal.getErrCode();
    }
    Kind kind;
    Ast.Position p;
    ErrorMessage internal;
    String errorTxt;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class FinalResult extends ErrorMessage {
    public int getErrCode() {
      return result;
    }
    int result;
    ClassB topLevelProgram;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedFinalResult extends ErrorMessage {
    public int getErrCode() {
      return 500;
    }
    ClassB finalRes;
    String reason;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ImpossibleToCompose extends ErrorMessage {
    public int getErrCode() {
      return 1001;
    }
    List<ClassB.Member> inherited;
    List<ClassB.Member> alreadyOffered;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedUnionOfMembers extends ErrorMessage {
    public int getErrCode() {
      return 1002;
    }
    ClassB.MethodWithType mFromInterface;
    ClassB.MethodWithType mFromClass;
  }
  
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class  TwoDifferentImplementedInterfacesDeclareMethod extends ErrorMessage {
    public int getErrCode() {
      return 1002;
    }
    MethodSelector mFromInterface;
    ClassB cb;
    List<ClassB> p;
  }
  
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidMethodImplemented extends ErrorMessage {
    public int getErrCode() {
      return 1002;
    }
    List<ClassB.Member> mFromInterface;
    ClassB.MethodImplemented mFromClass;
    ClassB cb;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedSubtypeDeclaration extends ErrorMessage {
    public int getErrCode() {
      return 1003;
    }
    ClassB implementer;
    ClassB notInterfaceImplemented;
    Path implemented;
    List<ClassB> p;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ProgramExtractOnWalkBy extends ErrorMessage {
    public int getErrCode() {
      return 1005;
    }
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ProgramExtractOnMetaExpression extends ErrorMessage {
    public int getErrCode() {
      return 1006;
    }
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CtxExtractImpossible extends ErrorMessage {
    public int getErrCode() {
      return 1008;
    }
    ExpCore e;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ExpectedImmutableVar extends ErrorMessage {
    public int getErrCode() {
      return 1009;
    }
    ExpCore ctxVal;
    String x;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IllegalAttemptedModification extends ErrorMessage {
    public int getErrCode() {
      return 1010;
    }
    ExpCore ctxVal;
    Block.Dec dec;
    ExpCore.MCall mc;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NormalForm extends ErrorMessage {
    public int getErrCode() {
      return 1011;
    }
    ExpCore e;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParsingError extends ErrorMessage {
    //others should extend it? but some have a range position!
    public int getErrCode() {
      return 1012;
    }
    int line;
    int pos;
    String token;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class DotDotDotCanNotBeResolved extends ErrorMessage {
    public int getErrCode() {
      return 1013;
    }
    String reason;
    Expression.ClassB.NestedClass hasDotDotDot;
    java.nio.file.Path path;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidCharacter extends ErrorMessage {
    public int getErrCode() {
      return 1013;
    }
    String file;
    String message;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedStringLiteral extends ErrorMessage {
    public int getErrCode() {
      return 1013;
    }
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedParenthesis extends ErrorMessage {
    public int getErrCode() {
      return 1013;
    }
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnopenedParenthesis extends ErrorMessage {
    public int getErrCode() {
      return 1014;
    }
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParenthesisMismatchRange extends ErrorMessage {
    public int getErrCode() {
      return 1015;
    }
    String file;
    int line1;
    int pos1;
    String token1;
    int line2;
    int pos2;
    String token2;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LibraryRefersToIncompleteClasses extends TypeError {
    public int getErrCode() {
      return 1016;
    }
    List<ClassB> p;
    ClassB cb;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathNonExistant extends TypeError {
    public int getErrCode() {
      return 1016;
    }
    List<String> listOfNodeNames;
    ClassB cb;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathNonStar extends TypeError {
    public int getErrCode() {
      return 1016;
    }
    Path p;
    HashMap<String, Ast.NormType> varEnv;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableDeclaredMultipleTimes extends ErrorMessage {
    public int getErrCode() {
      return 3001;
    }
    String x;
  }

  //when a plugin exists but chose to not act
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginActionUndefined extends ErrorMessage {
    int wait;
    //-1 to act at the end, -2...-n to never act again; otherwise
    // some time in milliseconds to try acting again.
    public int getErrCode() {
      return 3002;
    }
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NotWellFormed extends ErrorMessage {
    public int getErrCode() {
      return 3003;
    }
    Expression e;
    Expression ctx;
    String reason;
  }

  //Type system
  //@ToString(callSuper=false, includeFieldNames=true)
  public static abstract class TypeError extends ErrorMessage {
    public final List<facade.ErrorFormatter.Reporter> envs = new ArrayList<>();
    @Override public String getMessage() {
      return this.envs.toString();
    }
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NotOkToStar extends TypeError {
    public int getErrCode() {
      return 3003;
    }
    ClassB ct;
    ExpCore.ClassB.MethodWithType ctor;
    String reason;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginMethodUndefined extends TypeError {
    public int getErrCode() {
      return 3002;
    }
    List<String> validMethods;
    ExpCore.Using using;
    List<ClassB> p;
  }
  /*@Value @EqualsAndHashCode(callSuper=false)@ToString(callSuper=true, includeFieldNames=true)
  public static class PluginNotResolvedByPath extends TypeError{
    public int getErrCode(){return 3002;}
    ExpCore.Using using;List<ClassB> p;
  }*/

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NormImpossible extends ErrorMessage {
    public int getErrCode() {
      return 2014;
    }
    Type notNorm;
    Throwable cause;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LentShuldBeMutable extends ErrorMessage {
    public int getErrCode() {
      return 2001;
    }
    ExpCore.X var;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableSealed extends ErrorMessage {
    public int getErrCode() {
      return 2002;
    }
    ExpCore.X var;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidTypeForThrowe extends TypeError {
    public int getErrCode() {
      return 2003;
    }
    ExpCore.Signal e;
    Ast.NormType computedType;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CapsuleUsedMoreThenOne extends TypeError {
    public int getErrCode() {
      return 2004;
    }
    List<ExpCore> es;
    String varName;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnresolvedType extends TypeError {
    public int getErrCode() {
      return 2005;
    }
    HistoricType t;
    ExpCore e;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IncompleteClassIsRequired extends TypeError {
    public int getErrCode() {
      return 2005;
    }
    String reason;
    ExpCore e;
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ExceptionThrownNotCaptured extends TypeError {
    public int getErrCode() {
      return 2006;
    }
    ExpCore e;
    Path path1;
    HashSet<Path> exceptions2;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForMultiCatch extends TypeError {
    public int getErrCode() {
      return 2007;
    }
    ExpCore.Block.Catch k;
    HashSet<Type> options;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForCatchAndBlock extends TypeError {
    public int getErrCode() {
      return 2008;
    }
    ExpCore.Block e;
    Type te;
    Type tk;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathsNotSubtype extends TypeError {
    public int getErrCode() {
      return 2009;
    }
    Type tActual;
    Type tExpected;
    ExpCore e;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class TypesNotSubtype extends TypeError {
    public int getErrCode() {
      return 2010;
    }
    Type tActual;
    Type tExpected;
    ExpCore e;
    Throwable promotionAttemptedBut;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MethodNotPresent extends TypeError {
    public int getErrCode() {
      return 2011;
    }
    Path path;
    MethodSelector ms;
    List<ClassB> p;
  }

  //not used right now, may be is not useful
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnlockImpossible extends TypeError {
    public int getErrCode() {
      return 2012;
    }
    Throwable unlockAttemptedBut;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidURL extends TypeError {
    public int getErrCode() {
      return 2013;
    }
    String url;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PromotionImpossible extends TypeError {
    public int getErrCode() {
      return 2014;
    }
    Type pFrom;
    Type pTo;
    Throwable sealCause;
    ExpCore inner;
  }
}
package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.HistoricType;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.Util.CachedStage;
import ast.Util.PathMwt;

@SuppressWarnings("serial") public abstract class ErrorMessage extends RuntimeException {
  @Override public String getMessage() {
    return super.getMessage();//+this.toString();
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UserLevelError extends ErrorMessage {
    static public enum Kind {
      WellFormedness, TypeError, MetaError, Unclassified
    }
    Kind kind;
    Ast.Position p;
    ErrorMessage internal;
    String errorTxt;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class FinalResult extends ErrorMessage {
    int result;
    ClassB topLevelProgram;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedFinalResult extends ErrorMessage {
    ClassB finalRes;
    String reason;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ImpossibleToCompose extends ErrorMessage {
    List<ClassB.Member> inherited;
    List<ClassB.Member> alreadyOffered;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IncoherentMwts extends ErrorMessage {

  List<PathMwt> incoherent;}
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedUnionOfMembers extends ErrorMessage {
    ClassB.MethodWithType mFromInterface;
    ClassB.MethodWithType mFromClass;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class  TwoDifferentImplementedInterfacesDeclareMethod extends ErrorMessage {
    MethodSelector mFromInterface;
    ClassB cb;
    List<ClassB> p;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidMethodImplemented extends ErrorMessage {
    List<ClassB.Member> mFromInterface;
    ClassB.MethodImplemented mFromClass;
    ClassB cb;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedSubtypeDeclaration extends ErrorMessage {
    ClassB implementer;
    ClassB notInterfaceImplemented;
    Path implemented;
    List<ClassB> p;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ProgramExtractOnWalkBy extends ErrorMessage {
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ProgramExtractOnMetaExpression extends ErrorMessage {
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CtxExtractImpossible extends ErrorMessage {
    ExpCore e;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ExpectedImmutableVar extends ErrorMessage {
    ExpCore ctxVal;
    String x;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IllegalAttemptedModification extends ErrorMessage {  
    ExpCore ctxVal;
    Block.Dec dec;
    ExpCore.MCall mc;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NormalForm extends ErrorMessage {   
    ExpCore e;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParsingError extends ErrorMessage {
    //others should extend it? but some have a range position!
    int line;
    int pos;
    String token;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class DotDotDotCanNotBeResolved extends ErrorMessage {
    String reason;
    //java.nio.file.Path orignFileName;
    Expression.ClassB.NestedClass hasDotDotDot;
    java.nio.file.Path path;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidCharacter extends ErrorMessage {
    String file;
    String message;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedStringLiteral extends ErrorMessage {
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedParenthesis extends ErrorMessage {
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnopenedParenthesis extends ErrorMessage {
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParenthesisMismatchRange extends ErrorMessage {
    String file;
    int line1;
    int pos1;
    String token1;
    int line2;
    int pos2;
    String token2;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LibraryRefersToIncompleteClasses extends TypeError {
    List<ClassB> p;
    ClassB cb;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathNonExistant extends TypeError {
    List<String> listOfNodeNames;
    ClassB cb;
    Ast.Position pos;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathNonStar extends TypeError {
    Path p;
    HashMap<String, Ast.NormType> varEnv;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableDeclaredMultipleTimes extends ErrorMessage {
    String x;
  }

  //when a plugin exists but chose to not act
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginActionUndefined extends ErrorMessage {
    int wait;
    //-1 to act at the end, -2...-n to never act again; otherwise
    // some time in milliseconds to try acting again.
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NotWellFormed extends ErrorMessage {
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
    ClassB ct;
    ExpCore.ClassB.MethodWithType ctor;
    String reason;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginMethodUndefined extends TypeError {
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
    Type notNorm;
    Throwable cause;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LentShuldBeMutable extends ErrorMessage {
    ExpCore.X var;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableSealed extends ErrorMessage {
    ExpCore.X var;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidTypeForThrowe extends TypeError {
    ExpCore.Signal e;
    Ast.NormType computedType;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CapsuleUsedMoreThenOne extends TypeError {
    List<ExpCore> es;
    String varName;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnresolvedType extends TypeError {
    HistoricType t;
    ExpCore e;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IncompleteClassIsRequired extends TypeError {
    String reason;
    ExpCore e;
    Path path;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ExceptionThrownNotCaptured extends TypeError {
    ExpCore e;
    Path path1;
    HashSet<Path> exceptions2;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForMultiCatch extends TypeError {
    ExpCore.Block.Catch k;
    HashSet<Type> options;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForCatchAndBlock extends TypeError {
    ExpCore.Block e;
    Type te;
    Type tk;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathsNotSubtype extends TypeError {
    Type tActual;
    Type tExpected;
    ExpCore e;
    List<ClassB> p;
    CachedStage cachedInfo;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class TypesNotSubtype extends TypeError {
    Type tActual;
    Type tExpected;
    ExpCore e;
    Throwable promotionAttemptedBut;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MethodNotPresent extends TypeError {
    Path path;
    MethodSelector ms;
    ExpCore.MCall call;
    Ast.Position pos;
    List<ClassB> p;
  }

  //not used right now, may be is not useful
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnlockImpossible extends TypeError {
    Throwable unlockAttemptedBut;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidURL extends TypeError {
    String url;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PromotionImpossible extends TypeError {
    Type pFrom;
    Type pTo;
    Throwable sealCause;
    ExpCore inner;
  }
}
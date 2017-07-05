package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import sugarVisitors.CollapsePositions;
import ast.Ast;
import ast.Ast.Position;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.Util.PathMwt;
import coreVisitors.InjectionOnSugar;

@SuppressWarnings("serial") public abstract class ErrorMessage extends RuntimeException {
  public static interface PosImprove{
    Position getPos();
    PosImprove withPos(Position val);
    default PosImprove improvePos(Position val){
      if(this.getPos()!=null){val=CollapsePositions.accumulatePos(this.getPos(),val);}
      return this.withPos(val);
      }
    static ErrorMessage improve(ErrorMessage err,Position val){
       if(! (err instanceof ErrorMessage.PosImprove)){return err;}
       return (ErrorMessage)((ErrorMessage.PosImprove)err).improvePos(val);

      }
    }
  @Override public String getMessage() {
    return super.getMessage();//+this.toString();
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UserLevelError extends ErrorMessage implements PosImprove {
    static public enum Kind {
    ParsingError,WellFormedness, TypeError, MetaError, Unclassified
    }
    Kind kind;
    Ast.Position pos;
    ErrorMessage internal;
    String errorTxt;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class FinalResult extends ErrorMessage {
    int result;
    ClassB topLevelProgram;
    public int getErrCode() {
          return result;
         }
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MalformedFinalResult extends ErrorMessage {
    ClassB finalRes;
    String reason;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ImpossibleToCompose extends ErrorMessage {
    List<ClassB.Member> inherited;
    List<ClassB.Member> alreadyOffered;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IncoherentMwts extends ErrorMessage implements PosImprove{
  ast.Ast.MethodSelector guilty;
  List<Ast.C> exploredPath;
  List<PathMwt> incoherent;
  Ast.Position pos;}
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
  @Value  @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParsingError extends ErrorMessage {
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

  //*****************************
  public static abstract class PreParserError extends ErrorMessage {  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidCharacter extends PreParserError {
    String file;
    String message;
  }
    @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidCharacterOutOfString extends PreParserError {
    String file;
    int line;
    int pos;
    String token;
  }
    @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidCharacterInMultilineStringIndentation extends PreParserError {
    String file;
    int line;
    int pos;
    String token;
  }


  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedStringLiteral extends PreParserError {
    String file;
    int line;
    int pos;
    String token;
  }

  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnclosedParenthesis extends PreParserError {
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnopenedParenthesis extends PreParserError {
    String file;
    int line;
    int pos;
    String token;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ParenthesisMismatchRange extends PreParserError {
    String file;
    int line1;
    int pos1;
    String token1;
    int line2;
    int pos2;
    String token2;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LibraryRefersToIncompleteClasses extends TypeError {
    List<ClassB> p;
    ClassB cb;
    Position pos;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathMetaOrNonExistant extends TypeError implements PosImprove{
    boolean isMeta;
    List<Ast.C> listOfNodeNames;
    ClassB cb;
    Position pos;
    Position wherePathWasWritten;
    }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathNonStar extends TypeError {
    Path path;
    HashMap<String, Ast.Type> varEnv;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableDeclaredMultipleTimes extends ErrorMessage implements ErrorMessage.PosImprove{
    String x;
    Ast.Position pos;
  }

  //when a plugin exists but chose to not act
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginActionUndefined extends ErrorMessage {
    int wait;
    //-1 to act at the end, -2...-n to never act again; otherwise
    // some time in milliseconds to try acting again.
  }

 public abstract static class NotWellFormed extends ErrorMessage {
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class VariableUsedNotInScope extends NotWellFormed {
    Expression.X e;
    Expression ctx;
    String reason;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true)
  public static class NotWellFormedMsk extends NotWellFormed {
    //msk for misk, this class will be multiplied on need when is needed to distinguis the various kinds
    Expression e;
    Expression ctx;
    String reason;
  }
  //Type system
  //@ToString(callSuper=false, includeFieldNames=true)
  @Wither
  public static abstract class TypeError extends ErrorMessage implements ErrorMessage.PosImprove {
    public final List<facade.ErrorFormatter.Reporter> envs = new ArrayList<>();
    @Override public String getMessage() {
      return this.envs.toString();
    }
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NotOkToStar extends TypeError {
    ClassB ct;
    ExpCore.ClassB.MethodWithType ctor;
    String reason;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PluginMethodUndefined extends TypeError {
    List<String> validMethods;
    ExpCore.Using using;
    List<ClassB> p;
    Position pos;
  }


  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NormImpossible extends ErrorMessage {
    Type notNorm;
    Throwable cause;
    List<ClassB> p;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CircularImplements extends ErrorMessage {
  List<Ast.Path> visited;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NonInterfaceImplements extends ErrorMessage {
  Ast.Path implementer;
  Ast.Path implemented;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class NotExaclyOneMethodOrigin extends ErrorMessage {
  Ast.Path guilty;
  Ast.MethodSelector selector;
  List<ExpCore.ClassB.Member>allMs;
  }
  /*@Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class HistoricTypeNoTarget extends ErrorMessage {
  Ast.HistoricType guilty;
  List<ExpCore.ClassB.MethodWithType>allMs;
  }*/
  /*@Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class HistoricTypeCircularDefinition extends ErrorMessage {
  Ast.HistoricType guilty;
  }*/
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class LentShuldBeMutable extends ErrorMessage {
    ExpCore.X var;
  }
  @Value @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class VariableSealed extends ErrorMessage {
    ExpCore.X var;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidTypeForThrowe extends TypeError {
    ExpCore.Signal e;
    Ast.Type computedType;
    Position pos;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class CapsuleUsedMoreThenOne extends TypeError {
    List<ExpCore> es;
    String varName;
    Position pos;
  }
  /*@Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnresolvedType extends TypeError {
    HistoricType t;
    ExpCore e;
    Position pos;
  }*/
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class IncompleteClassIsRequired extends TypeError {
    String reason;
    ExpCore e;
    Path path;
    List<ClassB> p;
    Position pos;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ExceptionThrownNotCaptured extends TypeError {
    ExpCore e;
    Path path1;
    HashSet<Path> exceptions2;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForMultiCatch extends TypeError {
    List<ExpCore.Block.On> k;
    HashSet<Type> options;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class ConfusedResultingTypeForCatchAndBlock extends TypeError {
    ExpCore.Block e;
    Type te;
    Type tk;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PathsNotSubtype extends TypeError {
    Type tActual;
    Type tExpected;
    ExpCore e;
    List<ClassB> p;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class TypesNotSubtype extends TypeError {
    Type tActual;
    Type tExpected;
    ExpCore e;
    Throwable promotionAttemptedBut;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class MethodNotPresent extends TypeError implements PosImprove{
    Path path;
    MethodSelector ms;
    ExpCore.MCall call;
    Position pos;
  }

  //not used right now, may be is not useful
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class UnlockImpossible extends TypeError {
    Throwable unlockAttemptedBut;
    Position pos;
  }

  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class InvalidURL extends TypeError {
    String url;
    Position pos;
  }
  @Value @Wither @EqualsAndHashCode(callSuper = false) @ToString(callSuper = true, includeFieldNames = true) public static class PromotionImpossible extends TypeError {
    Type pFrom;
    Type pTo;
    Throwable sealCause;
    ExpCore inner;
    Position pos;
  }
}
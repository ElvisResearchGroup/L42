package is.L42.connected.withSafeOperators;

import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;

import tools.Assertions;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Error;
import sugarVisitors.ToFormattedText;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ErrorMessage.PathNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.X;
import ast.Util.PathMx;
import auxiliaryGrammar.Program;

public class Errors42 {

  //"SourceUnfit", caused by redirect
  public static Error errorSourceUnfit(List<String> current,Path destExternalPath,ExtractInfo.ClassKind kindSrc,ExtractInfo.ClassKind kindDest,List<Member>unexpected,boolean headerOk,List<Path>unexpectedInterfaces,boolean isPrivate){
      return Resources.Error.multiPartStringError("SourceUnfit",
          "SrcPath",""+Path.outer(0,current), //the path of the class that can not be redirected
          "DestExternalPath",Doc.factory(destExternalPath), //the path of the class that can not be redirected
          "PrivatePath",""+isPrivate,//the path can not be redirected since is private
          "SrcKind",kindSrc.name(),//the kind of the class at path
          "DestKind",kindDest.name(),
          //"IncompatibleClassKind",""+!headerOk,//if the path can not be redirected because of their respective kinds. This information would make no sense if I can get the kind for dest!
          "UnexpectedMethods",""+ExtractInfo.showMembers(unexpected),//methods that are not present in dest (or present but with different declared vs Interface implemented status)
          "UnexpectedImplementednterfaces",""+unexpectedInterfaces//interfaces implemented in path but not in dest
          //TODO: I would like to give also the destination path, but by being an external path I'm troubled.
          //should I use a @Path in a doc?
          //also, what happens if destination is private, caused by example by a private type of a public method??
          //also if the original destination is private?
           );
  }
  //"ClassClash" caused by sum, renameClass, renameClassStrict, renameClass
  static Error errorClassClash(List<String> current,  List<Path> confl,ExtractInfo.ClassKind kindA,ExtractInfo.ClassKind kindB) {
    return Resources.Error.multiPartStringError("ClassClash",
       "Path",""+Path.outer(0,current),//the path of the clash, in the rename is the path of the destination clash
       "LeftKind",kindA.name(),//kind of left and right classes
       "RightKind",kindB.name(),//this allows to infer if the class kinds was compatible
       "ConflictingImplementedInterfaces",""+confl//the list of interface that define methods with same name
       //TODO:test how this is checked, what order we get what exactly?
        );
  }
  //"MethodClash" caused by sum, renameMethod, renameClassStrict, renameClass
  static Error errorMethodClash(List<String> pathForError, Member mta, Member mtb, boolean exc, List<Integer> pars, boolean retType, boolean thisMdf) {
      return Resources.Error.multiPartStringError("MethodClash",
       "Path",""+Path.outer(0,pathForError),//the path of the clash (that own  the method), in the rename is the path of the destination clash
      "Left",sugarVisitors.ToFormattedText.of(mta),//implementation dependend print of the left and right methods
      "Right",sugarVisitors.ToFormattedText.of(mtb),
      "LeftKind",ExtractInfo.memberKind(mta),//kind of the left/right methods
      "RightKind",ExtractInfo.memberKind(mtb),
      "DifferentParameters",""+ pars,//number of parameters with different types
      "DifferentReturnType",""+ !retType,//if the return types are different
      "DifferentThisMdf",""+ !thisMdf,//if the modifier for "this" is different
      "IncompatibleException",""+!exc);//if they have an incompatible exception list
    }

  //"InvalidOnTopLevel", caused by redirect and addDocumentationOnNestedClass
  static Error errorInvalidOnTopLevel() {
    return Resources.Error.multiPartStringError("InvalidOnTopLevel");
  }
  //"TargetUnavailable", caused by most operations referring to paths and methods
  static enum TargetUnavailable{PrivatePath,PrivateMethod,InexistentPath,InexistentMethod}
  static void checkExistsPathMethod(ClassB cb, List<String> path,Optional<MethodSelector>ms){
    try{
      Boolean[] isPrivateRef=new Boolean[]{false};
      ClassB cbi=Program.extractCBar(path, cb,isPrivateRef);
      Boolean[] isPrivateMeth=new Boolean[]{false};
      boolean absentMeth=false;
      if(ms.isPresent()){
        Optional<Member> meth=Program.getIfInDom(cbi.getMs(),ms.get());
        absentMeth=!meth.isPresent();
        if(meth.isPresent()){
          meth.get().match(
            nc->{throw Assertions.codeNotReachable();},
            mi->{return null;},
            mt->{if(mt.getDoc().isPrivate()){isPrivateMeth[0]=true;}return null;}
            );
          }
        }
      TargetUnavailable kind=null;
      if(absentMeth){kind=TargetUnavailable.InexistentMethod;}
      if(isPrivateMeth[0]){kind=TargetUnavailable.PrivateMethod;}
      if(isPrivateRef[0]){kind=TargetUnavailable.PrivatePath;}
      if(kind==null){return;}
      throw Resources.Error.multiPartStringError("TargetUnavailable",
          "Path",""+Path.outer(0,path),
          "Selector",""+((ms.isPresent())?ms.get():""),
          "InvalidKind",""+kind.name());
      }
    catch(ast.ErrorMessage.PathNonExistant e){
      throw Resources.Error.multiPartStringError("TargetUnavailable",
          "Path",""+Path.outer(0,path),
          "Selector",""+((ms.isPresent())?ms.get():""),
          "InvalidKind",""+TargetUnavailable.InexistentPath);
    }
  }

  //"PrivacyCoupuled", caused by removeImplementation
  static Error errorPrivacyCoupuled(List<Path> coupuledPaths, List<PathMx> ordered) {
    return Resources.Error.multiPartStringError("PrivacyCoupuled",
       "CoupuledPath",""+coupuledPaths,//private paths that are still used
      "CoupuledMethods",""+ordered);//private methods that are still used
  }

  //"PathClash", caused by renameClassStrict, if two paths are prefix of one other
  public static Resources.Error errorPrefix(List<String> a, List<String> b) {
  boolean aIsLonger=a.size()>b.size();
  List<String> shorter=aIsLonger?b:a;
  List<String> longer=aIsLonger?a:b;
  return Resources.Error.multiPartStringError("PathClash",
     "Prefix",""+shorter,
     "Clashing",""+longer);}

  //"AmbiguousPop", caused by pop if there is more then one nested class
  static Error errorAmbiguousPop(ClassB cb) {
    return Resources.Error.multiPartStringError("AmbiguousPop",
        "numberOfNestedClasses",""+cb.getMs().size());
  }
  //"NotBox", caused by pop if the top level is not of box kind
  static Error errorNotBox(ClassB cb, List<MethodSelector> meth, Set<Path> used,ExtractInfo.ClassKind kind) {
    return Resources.Error.multiPartStringError("NotBox",
        "UsedBy",""+used,
        "Supertypes",""+cb.getSupertypes(),
        "ContainsMethods",""+meth,
        "ActualKind",""+kind.name());
  }
  public static void checkMethodClash(List<String>pathForError,MethodWithType mta, MethodWithType mtb){
    boolean implClash=mta.getInner().isPresent() && mtb.getInner().isPresent();
    boolean exc=ExtractInfo.isExceptionOk(mta,mtb);
    List<Integer> pars=ExtractInfo.isParTypeOk(mta,mtb);
    boolean retType=mta.getMt().getReturnType().equals(mtb.getMt().getReturnType());
    boolean thisMdf=mta.getMt().getMdf().equals(mtb.getMt().getMdf());
    if(!implClash && exc && pars.isEmpty() && retType && thisMdf){return;}
    if(mta.getInner().isPresent()){mta=mta.withInner(Optional.of(new ExpCore.X("implementation")));}
    if(mtb.getInner().isPresent()){mtb=mtb.withInner(Optional.of(new ExpCore.X("implementation")));}
    throw errorMethodClash(pathForError, mta, mtb, exc, pars, retType, thisMdf);
  }


}

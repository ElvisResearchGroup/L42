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
import ast.Util.PathPath;
import ast.Util.PathSPath;
import auxiliaryGrammar.Program;

public class Errors42 {

  //"SourceUnfit", caused by redirect
  public static Error errorSourceUnfit(List<String> current,Path destExternalPath,ExtractInfo.ClassKind kindSrc,ExtractInfo.ClassKind kindDest,List<Member>unexpected,boolean headerOk,List<Path>unexpectedInterfaces){
      return Resources.Error.multiPartStringError("SourceUnfit",
          "SrcPath",formatPathIn(current), //the path of the class that can not be redirected
          "DestExternalPath",formatPathOut(destExternalPath), //the path of the class that can not be redirected
         // "PrivatePath",""+isPrivate,//the path can not be redirected since is private
          "SrcKind",kindSrc.name(),//the kind of the class at path
          "DestKind",kindDest.name(),
          //"IncompatibleClassKind",""+!headerOk,//if the path can not be redirected because of their respective kinds. This information would make no sense if I can get the kind for dest!
          "UnexpectedMembers",""+ExtractInfo.showMembers(unexpected),//methods that are not present in dest (or present but with different declared vs Interface implemented status)
          "UnexpectedImplementedInterfaces",ExtractInfo.showPaths(unexpectedInterfaces)//sort of interfaces implemented in path but not in dest, more complex for ambiguities
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
       "Path",formatPathIn(pathForError),//the path of the clash (that own  the method), in the rename is the path of the destination clash
      "Left",sugarVisitors.ToFormattedText.of(mta).replace("\n","").trim(),//implementation dependend print of the left and right methods
      "Right",sugarVisitors.ToFormattedText.of(mtb).replace("\n","").trim(),
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
  //"MemberUnavailable", caused by most operations referring to paths and methods
  static enum MemberUnavailable{PrivatePath,PrivateMethod,NonExistentPath,NonExistentMethod}
  static Member checkExistsPathMethod(ClassB cb, List<String> path,Optional<MethodSelector>ms){
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
          if(!isPrivateMeth[0]){return meth.get();}
          }
        }
      MemberUnavailable kind=null;
      if(absentMeth){kind=MemberUnavailable.NonExistentMethod;}
      if(isPrivateMeth[0]){kind=MemberUnavailable.PrivateMethod;}
      if(isPrivateRef[0]){kind=MemberUnavailable.PrivatePath;}
      if(kind==null){return null;}
      throw Resources.Error.multiPartStringError("MemberUnavailable",
          "Path",formatPathIn(path),
          "Selector",""+((ms.isPresent())?ms.get():""),
          "InvalidKind",""+kind.name());
      }
    catch(ast.ErrorMessage.PathNonExistant e){
      throw Resources.Error.multiPartStringError("MemberUnavailable",
          "Path",formatPathIn(path),
          "Selector",""+((ms.isPresent())?ms.get():""),
          "InvalidKind",""+MemberUnavailable.NonExistentPath);
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
 /* static void checkCoherentMapping(List<PathPath> setVisited) {
    // setVisited is a set of individual redirected classes,
    // created by walking the sub-tree under each cascade redirect.
    // getPath1() is the path in the library before redirecting.
    // getPath2() is the proposed path in the redirected library.
    // We will allow many paths to be redirected into a single new path,
    // but not vice-versa. 
    for(PathPath p1:setVisited){
      for(PathPath p2:setVisited){
        if(p1.equals(p2)){continue;}
        if(p1.getPath1().equals(p2.getPath1())){
          throw errorIncoherentRedirectMapping(setVisited, p1.getPath1(),p1.getPath2(),p2.getPath2());
          }
      }
    }
    return;
  }*/
  static Error errorIncoherentRedirectMapping(List<PathPath>verified,List<PathSPath>ambiguities,Path incoSrc,List<Path> _incoDest) {
    Doc src=Doc.empty();
    Doc dest=Doc.empty();
    //Doc ambig=Doc.empty();
    for(PathPath v:verified){
      src=src.sum(formatPathIn(v.getPath1().getCBar()));
      dest=dest.sum(formatPathOut(v.getPath2()));
    }
    for(PathSPath a:ambiguities){
      //if(a.getPaths().size()!=1){
      //  ambig=ambig.sum(formatPathIn(a.getPath().getCBar()));
      //  ambig=ambig.sum(Doc.factory("@"+a.getPaths().size()));
      //  }
      Doc srci=formatPathIn(a.getPath().getCBar());
      for(Path pij:a.getPaths()){
        src=src.sum(srci);
        dest=dest.sum(formatPathOut(pij));
      }
    }
    Doc incoDest=Doc.empty();
    for(Path pi:_incoDest){incoDest=incoDest.sum(formatPathOut(pi));}
    return Resources.Error.multiPartStringError("IncoherentRedirectMapping",
        "Src",src.formatNewLinesAsList(),
        "Dest",dest.formatNewLinesAsList(),
        //"Ambiguities",ambig.formatNewLinesAsList(),
        "IncoherentSrc",incoSrc==null?Doc.empty():formatPathIn(incoSrc.getCBar()),
        "IncoherentDest",incoDest.formatNewLinesAsList()
        );
  }
  static Doc formatPathIn(List<String> path){
    if(path.isEmpty()){return Doc.factory(Path.outer(0));}
    return Doc.factory("@::"+String.join("::", path));
  }
  static Doc formatPathOut(Path path){
    if(path.isPrimitive()){return Doc.factory(path);}
    assert path.outerNumber()>0;
    return Doc.factory(Path.outer(path.outerNumber()+1,path.getCBar()));
  }
  static Doc formatPath(Path path){
    if(path.isPrimitive()){return Doc.factory(path);}
    if(path.outerNumber()==0){return formatPathIn(path.getCBar());}
    return formatPathOut(path);
  }
}

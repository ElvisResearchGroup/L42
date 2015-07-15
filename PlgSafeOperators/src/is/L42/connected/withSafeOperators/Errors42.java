package is.L42.connected.withSafeOperators;

import introspection.FindUsage;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Error;
import sugarVisitors.ToFormattedText;
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

  public static Error errorSourceUnfit(List<String> current,ExtractInfo.ClassKind kind,List<Member>unexpected,boolean headerOk,List<Path>unexpectedInterfaces,boolean isPrivate){
      return Resources.Error.multiPartStringError("SourceUnfit",
          "Path",""+Path.outer(0,current),
          "Kind",kind.name(),
          "UnexpectedMethods",""+ExtractInfo.showMembers(unexpected),
          "IncompatibleHeaders",""+!headerOk,
          "UnexpectedImplementednterfaces",""+unexpectedInterfaces,
          "PrivatePath",""+isPrivate
           );
  }

  static Error errorClassClash(List<String> current,  List<Path> confl,ExtractInfo.ClassKind kindA,ExtractInfo.ClassKind kindB) {
    return Resources.Error.multiPartStringError("ClassClash",
       "Path",""+Path.outer(0,current),
       "LeftKind",kindA.name(),
       "RightKind",kindB.name(),
       "ConflictingImplementedInterfaces",""+confl
        );
  }

  static Error errorMehtodClash(List<String> pathForError, Member mta, Member mtb, boolean exc, List<Integer> pars, boolean retType, boolean thisMdf) {
      return Resources.Error.multiPartStringError("MethodClash",
       "Path",""+Path.outer(0,pathForError),
      "Left",sugarVisitors.ToFormattedText.of(mta),
      "Right",sugarVisitors.ToFormattedText.of(mtb),
      "LeftKind",ExtractInfo.memberKind(mta),
      "RightKind",ExtractInfo.memberKind(mtb),
  //deducible    "IncompatibleHeader",""+(!exc||!pars.isEmpty() || !retType || !thisMdf),
      "DifferentParameters",""+ pars,
      "DifferentReturnType",""+ !retType,
      "DifferentThisMdf",""+ !thisMdf,
      "IncompatibleException",""+!exc);
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
    throw errorMehtodClash(pathForError, mta, mtb, exc, pars, retType, thisMdf);
  }

  static Error errorInvalidOnTopLevel() {
    return Resources.Error.multiPartStringError("InvalidOnTopLevel");
  }

  static void checkExistsPathMethod(ClassB cb, List<String> path,Optional<MethodSelector>ms){
    try{
      ClassB cbi=Program.extractCBar(path, cb);
      if(!ms.isPresent()){return;}
      Optional<Member> meth=Program.getIfInDom(cbi.getMs(),ms.get());
      if(meth.isPresent()){return;}
      throw Resources.Error.multiPartStringError("InexistentMethod",
          "Path",""+Path.outer(0,path),
          "Selector",""+ms);
      }
    catch(ast.ErrorMessage.PathNonExistant e){
      throw Resources.Error.multiPartStringError("InexistentPath",
          "Path",""+Path.outer(0,path));
    }
  }

  public static Resources.Error errorPrefix(List<String> a, List<String> b) {
  boolean aIsLonger=a.size()>b.size();
  List<String> shorter=aIsLonger?b:a;
  List<String> longer=aIsLonger?a:b;
  return Resources.Error.multiPartStringError("PathClash",
     "Prefix",""+shorter,
     "Clashing",""+longer);}

  public static void checkPrivacyCoupuled(ClassB cbFull,ClassB cbClear, List<String> path) {
  //start from a already cleared out of private states
  //check if all private nested classes are USED using IsUsed on cbClear
  //this also verify that no private nested classes are used as
  //type in public methods of public classes.
  //collect all PublicPath.privateMethod
  //use main->introspection.FindUsage
  List<Path>prPath=ExtractInfo.collectPrivatePathsAndSubpaths(cbFull,path);
  List<PathMx>prMeth=ExtractInfo.collectPrivateMethodsOfPublicPaths(cbFull,path);
  List<Path>coupuledPaths=new ArrayList<>();
  for(Path pi:prPath){
    Set<Path> used = ExtractInfo.IsUsed.of(cbClear,pi);
    if(used.isEmpty()){continue;}
    coupuledPaths.add(pi);
  }
  Set<PathMx> usedPrMeth = FindUsage.of(Program.empty(),prMeth, cbClear);
  if(coupuledPaths.isEmpty() && usedPrMeth.isEmpty()){return;}
  List<PathMx> ordered=new ArrayList<>(usedPrMeth);
  Collections.sort(ordered,(px1,px2)->px1.toString().compareTo(px2.toString()));
  throw Resources.Error.multiPartStringError("PrivacyCoupuled",
     "CoupuledPath",""+coupuledPaths,
    "CoupuledMethods",""+ordered);
  }

}

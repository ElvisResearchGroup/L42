package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore.*;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMwt;
import ast.Util.PathMx;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import tools.Assertions;
import tools.Map;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
public class Abstract {
  public static ClassB toAbstract(ClassB cb, List<Ast.C> path){
    Errors42.checkExistsPathMethod(cb, path, Optional.empty());
    //check privacy coupled
    ClassB cbClear=cb.onClassNavigateToPathAndDo( path, cbi->clear(cbi));
    Abstract.checkPrivacyCoupuled(cb,cbClear, path);
    return cbClear;
  }

  private static ClassB clear(ClassB cb) {
    //note: it would be wrong to remove implementation of private interfaces:
    //otherwise a use could rely on methods (the interface-implemented ones) that do not
    //exists any more.
    //we choose to not remove private trown exceptions just for coherence.
    //it is empty, cut it down!
    List<Member> newMs=new ArrayList<>();
    for(Member m:cb.getMs()){
      m.match(
          nc->{
            if(nc.getName().isUnique()){return null;}
            newMs.add(nc.withInner(clear((ClassB)nc.getInner())));return null;
            },
          mi->{return null;},//just implementation
          mt->{
            if(mt.getMs().isUnique()){return null;}
            newMs.add(mt.with_inner(Optional.empty()));return null;}
      );}
    //create new class
    return cb.withMs(newMs);
  }


  static void checkPrivacyCoupuled(ClassB cbFull,ClassB cbClear, List<Ast.C> path) {
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
  List<PathMx> ordered=new ArrayList<>();
  try{
    Set<PathMx> usedPrMeth =findUsage(prMeth,cbClear); //FindUsage.of(Program.empty(),prMeth, cbClear);
    if(coupuledPaths.isEmpty() && usedPrMeth.isEmpty()){return;}
    ordered.addAll(usedPrMeth);
    }
  catch(PathMetaOrNonExistant pne){
    assert !coupuledPaths.isEmpty();
    }
  Collections.sort(ordered,(px1,px2)->px1.toString().compareTo(px2.toString()));
  throw Errors42.errorPrivacyCoupuled(coupuledPaths, ordered);
  }

  private static class NotFound extends Error{ static NotFound nf=new NotFound();}
  private static Set<PathMx> findUsage(List<PathMx> prMeth, ClassB cbClear) {
    Set<PathMx> result=new HashSet<>();
    for(PathMx pmx:prMeth){
      assert pmx.getPath().outerNumber()==0;
      UserForMethodResult res= userForMethod(Resources.getP()/*wasEmpty*/, cbClear,pmx.getPath().getCBar(),pmx.getMs(),false);
      result.addAll(res.asClient);
      res.asThis.stream().map(e->new PathMx(Path.outer(0),e)).forEach(result::add);
      }
    return result;
  }
  static class UserForMethodResult{List<PathMx> asClient;List<MethodSelector>asThis;}
  public static UserForMethodResult userForMethod(Program p,ClassB cb,List<Ast.C> path,MethodSelector src,boolean checkMethExists ){
    if(checkMethExists){
      Member mem=Errors42.checkExistsPathMethod(cb, path, Optional.of(src));
      assert mem instanceof MethodWithType;
      }
    Member mem=new ExpCore.ClassB.MethodImplemented(Doc.empty(),src,new ExpCore._void(),Position.noInfo);
    CollectedLocatorsMap maps=CollectedLocatorsMap.from(Path.outer(0,path), mem,src);
    HashSet<PathMx> result1=new HashSet<>();
    HashSet<MethodSelector> result2=new HashSet<>();
    MethodPathCloneVisitor ren=new RenameUsage(cb, maps,p){
      public Ast.Type liftT(Ast.Type t){return t;}
      @Override protected MethodSelector liftMs(MethodSelector ms){return ms;}
      @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){return ms;}
      public ExpCore visit(MCall s) {
        List<Ast.C> localPath = this.getLocator().getClassNamesPath();
        if(!localPath.equals(path)){return super.visit(s);}
        if(s.getInner().equals(Path.outer(0)) || s.getInner().equals(new ExpCore.X(Position.noInfo,"this"))){
            result2.add(s.getS());
            return s.withInner(s.getInner().accept(this)).withEs(Map.of(e->e.accept(this), s.getEs()));
            }
        return super.visit(s);
        }
      @Override public MethodSelector visitMS(MethodSelector original, Path src) {
        MethodSelector toCollect=this.mSToReplaceOrNull(original, src);
        if(toCollect==null){return original;}
        Member m=this.getLocator().getLastMember();
        assert !(m instanceof NestedClass):
          "";
        MethodSelector msUser=m.match(nc->{throw Assertions.codeNotReachable();},
            mi->mi.getS(), mt->mt.getMs());
        Path pathUser=Path.outer(0,this.getLocator().getClassNamesPath());
        result1.add(new PathMx(pathUser,msUser));
        return original;
      }
    };
   ren.visit(cb);
   return new UserForMethodResult(){{asClient=new ArrayList<>(result1);asThis=new ArrayList<>(result2);}};
  }


}

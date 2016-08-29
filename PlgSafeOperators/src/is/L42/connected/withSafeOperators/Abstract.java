package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import ast.ErrorMessage;
import ast.ErrorMessage.PathNonExistant;
import ast.ExpCore.*;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMwt;
import ast.Util.PathMx;
import auxiliaryGrammar.Program;
import is.L42.connected.withSafeOperators.ExtractInfo.IsUsed;
import is.L42.connected.withSafeOperators.Rename.UserForMethodResult;
public class Abstract {
  public static ClassB toAbstract(ClassB cb, List<String> path){
    Errors42.checkExistsPathMethod(cb, path, Optional.empty());
    //check privacy coupled
    ClassB cbClear=ClassOperations.onClassNavigateToPathAndDo(cb, path, cbi->clear(cbi));
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
            if(nc.getDoc().isPrivate()){return null;}
            newMs.add(nc.withInner(clear((ClassB)nc.getInner())));return null;
            },
          mi->{return null;},//just implementation
          mt->{
            if(mt.getDoc().isPrivate()){return null;}
            newMs.add(mt.with_inner(Optional.empty()));return null;}
      );}
    //create new class
    return cb.withMs(newMs);
  }

  public static ClassB toAbstract(Program p,ClassB cb, List<String> path,MethodSelector sel,MethodSelector newSel){
    Errors42.checkExistsPathMethod(cb, path, Optional.of(sel));
    if(path.isEmpty()){return auxToAbstract(p,cb,path,sel,newSel);}
    return ClassOperations.onClassNavigateToPathAndDo(cb,path,cbi->auxToAbstract(p,cbi,path,sel,newSel));
  }
  private static ClassB auxToAbstract(Program p,ClassB cb,List<String> pathForError,MethodSelector sel,MethodSelector newSel) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    Member m=Program.getIfInDom(newMs, sel).get();
    //make m abstract
    if(m instanceof MethodWithType){
      MethodWithType mwt=(MethodWithType)m;
      mwt=mwt.with_inner(Optional.empty());
      Program.replaceIfInDom(newMs,mwt);
      }
    else{//it is method implemented
      Program.removeIfInDom(newMs, sel);
      }
    //create new class
    if(newSel==null){ return cb.withMs(newMs);  }
    MethodWithType mwt1 = p.extractMwt(sel, cb).get();
    if(newSel!=null){Errors42.checkCompatibleMs(pathForError, mwt1, newSel);}
    Optional<MethodWithType> mwt2 = p.extractMwt(newSel, cb);
    mwt1=mwt1.withMs(newSel).withDoc(Doc.empty());
    if(mwt2.isPresent()){
       throw Errors42.errorMethodClash(pathForError, mwt1,mwt2.get(), false, Collections.emptyList(), false,false,false); 
       }   
    newMs.add(mwt1);
    return cb.withMs(newMs);  
  }

  static void checkPrivacyCoupuled(ClassB cbFull,ClassB cbClear, List<String> path) {
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
  catch(PathNonExistant pne){
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
      UserForMethodResult res= Rename.userForMethod(Program.empty(), cbClear,pmx.getPath().getCBar(),pmx.getMs(),false);
      result.addAll(res.asClient);
      res.asThis.stream().map(e->new PathMx(Path.outer(0),e)).forEach(result::add);
      }
    return result;
  }
}

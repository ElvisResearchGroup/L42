package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.C;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.PathAux;
import coreVisitors.CloneVisitorWithProgram;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.ClassOperations;
import is.L42.connected.withSafeOperators.CollectedLocatorsMap;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.Pop;
import is.L42.connected.withSafeOperators.Push;
import is.L42.connected.withSafeOperators.location.Location;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathNotFound;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import platformSpecific.javaTranslation.Resources;
import programReduction.Program;
import tools.Assertions;
interface RenameSpec{
/**<pre>#define
Pop: remove the last layer (may need to check for no one use the last layer)
  As in: {C:{..}}[pop]={..}[from This0.C]
  defined only if
  -there is only one nested, and no other methods
  -no interface, no implements
  -no reference to This0
</pre>*/void pop();

/**<pre>#define
Push: L[push Cs]
  push all in a nested of its own
  As in: {..}[push C]={C:{..}[from This1??]}
</pre>*/void push();

/**<pre>#define
DirectRename
L0[DirectRename src->dest]p =L //with src!=dest._ and dest!= src._
  L1=L0[PathRename src->dest]
  L2=L1\src //similar to L[src=undefined]
  L3=L1(src)[from This(dest.size()-1).src.removeLast()]
  L4=L3[push dest]
  L=L2++p L4
</pre>*/void directRename();

/**<pre>#define
Rename:  L[rename src -> dest]
  if with src!=dest._ and dest!= src._
    then L[rename src -> dest]=L[directRename src -> dest]
  else
    L[rename src -> dest]=
      L[push Fresh0]
      [directRename Fresh0.src->Fresh1]
      [directRename Fresh1->Fresh0.dest]
      [pop Fresh0]
</pre>*/void rename();
}

public class Rename {
  /**{@link RenameSpec#pop}*/
  public static boolean poppable(ClassB l){
    boolean rightSize=l.getMs().size()==1;
    boolean ok=ExtractInfo.checkBox(l,l,Collections.emptyList(),true);
    return rightSize && ok;
    }
  /**{@link RenameSpec#pop}*/
  public ClassB directPop(ClassB l){
    return Pop.directPop(l);
    }
  /**{@link RenameSpec#directRename} */
  public static ClassB directRename(Program p, ClassB l, List<Ast.C> src, List<Ast.C> dest) throws MethodClash, SubtleSubtypeViolation, ClassClash {
    //rename srcC in destC in top
    ClassB renamedFullL=(ClassB)new PathRename(p,src,dest).visit(l);
    //clearCb=remove srcC from top
    ClassB noSrcL=renamedFullL.onNestedNavigateToPathAndDo(src,nc->Optional.empty());
    //newCB=take srcC from top, and adjust paths to dest
    ClassB onlyDestL=is.L42.connected.withSafeOperators.Rename.redirectDefinition(src,dest,renamedFullL);
    //optionally sum renamed srcC in destC
    
    ClassB res= new Compose(l,l).composeRefreshed(p, noSrcL, onlyDestL);
    //TODO: it can be much more efficient in many cases, if we can check if the sum in not doing any heavy lifting, as in
    //renaming on a non existent target
    //TODO:try catch methodclash and replace right method location with the "right one" 
    //additional step?ClassOperations.normalizePaths(res);
    //assert !res.toString().contains("This$"):
    //  res.toString();
    return res;
  }
/**{@link RenameSpec#rename}*/   
public static ClassB renameClass(PData p,ClassB cb,String src,String dest) throws MethodClash, SubtleSubtypeViolation, ClassClash, PathNotFound{
  return renameClassAux(p.p,cb,PathAux.parseValidCs(src),PathAux.parseValidCs(dest));
  }
/**{@link RenameSpec#rename}*/   
public static ClassB renameClassAux(Program p,ClassB cb,List<Ast.C> src,List<Ast.C> dest) throws MethodClash, SubtleSubtypeViolation, ClassClash, PathNotFound{
  if(!MembersUtils.isPathDefined(cb, src)){
    throw new RefactorErrors.PathNotFound(Location.as42Path(src));
    }
  if(src.equals(dest)){return cb;}
  if(src.isEmpty()){//push is asked
    return Push.pushMany(cb, dest);
  }
  if(dest.isEmpty() && src.size()==1){//pop is asked
    if(poppable(cb)){
      return Pop.directPop(cb);
      }//otherwise, proceed with encoding
    }
  if(!ExtractInfo.isPrefix(src, dest)){
    return directRename(p,cb,src,dest);
    }
  src=new ArrayList<>(src);
  dest=new ArrayList<>(dest);
  C result=C.of("Result");
  src.add(0,result);
  dest.add(0,result);
  cb=Push.pushOne(cb,result);
  List<Ast.C> tmp = Collections.singletonList(C.of("Tmp"));
  cb=directRename(p,cb,src,tmp);
  cb=directRename(p,cb,tmp,dest);
  cb=Pop.directPop(cb);
  return cb;
}
  
}


class PathRename extends CloneVisitorWithProgram{
    List<Ast.C> src;
    List<Ast.C> dest;
    public PathRename(Program p,List<Ast.C> src,List<Ast.C> dest) {
    super(p);
    this.src=src;
    this.dest=dest;
    }
  @Override public Path liftP(Path that){
    if(that.isPrimitive()){return that;}
    if(that.getCBar().isEmpty()){return that;}
    Path srcHere=Path.outer(levels,src);
    if(that.toString().contains("$_RefTo") &&
            srcHere.toString().contains("$_RefTo") ){
    System.out.println(that);
    }

    List<Ast.C> tail=p._equivSubPath(srcHere,that);
    if(tail==null){return that;}
    if(tail.isEmpty()){return Path.outer(levels,dest);}
    List<Ast.C> newCs=new ArrayList<>(dest);
    newCs.addAll(tail);
    Path destHere=Path.outer(levels,newCs);
    return destHere;
    }    
}

class MembersUtils{
static boolean isPrivate(MethodSelector ms){
  return ms.getUniqueNum()!=-1;
  }
static boolean isPrivate(Path p){
  for(Ast.C c:p.getCBar()){
    if(c.getUniqueNum()!=-1){return true;}
    }
  return false;
  }
static boolean isPathDefined(ClassB cb, List<Ast.C> path){
  try{cb.getClassB(path);return true;}
  catch(ast.ErrorMessage.PathMetaOrNonExistant unused){return false;}
  }
/**null for not even the path is defined*/
static Boolean _isPathMsDefined(ClassB cb, List<Ast.C> path,MethodSelector ms){
  try{return cb.getClassB(path)._getMember(ms)!=null;}
  catch(ast.ErrorMessage.PathMetaOrNonExistant unused){return null;}
  }
}
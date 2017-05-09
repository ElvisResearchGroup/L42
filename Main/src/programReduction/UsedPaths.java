package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import ast.Ast.Position;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ErrorMessage.IncompleteClassIsRequired;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import coreVisitors.FindPathUsage;
import coreVisitors.IsCompiled;
import coreVisitors.PropagatorVisitor;
import sugarVisitors.CollapsePositions;
import tools.Assertions;

class PathsPaths{
  public PathsPaths(Paths left, Paths right) {
    this.left = left;this.right = right;
    }
  final Paths left; final Paths right;}

public class UsedPaths {

static PathsPaths usedPathsECatchErrors(Program p, ExpCore e){
  try{return usedPathsE(p,e);}
  catch(PathMetaOrNonExistant pne){
    if (pne.getWherePathWasWritten()==null){
      assert pne.getListOfNodeNames()!=null;
      Position pos=FindPathUsage._locate(p,e,Path.outer(1,pne.getListOfNodeNames()));
      pne=pne.withWherePathWasWritten(pos);
      }
    throw pne.withPos(CollapsePositions.of(e));
    }
  catch(IncompleteClassIsRequired icir){
    throw icir.withE(e).withPos(CollapsePositions.of(e));
    }
  }
  static PathsPaths usedPathsE(Program p, ExpCore e){
//- usedPathsE(p,eC)= <reorganize(Ps); usedPathsFix(p,paths, empty)>
//assert that the result includes paths in usedPathsFix(p,paths, empty)  
//Ps,Ps'={P|P inside eC}//arbitrary split of the set; heuristic will apply in the implementation.
    List<Ast.Path>  psBoth=CollectPaths0.of(e);//collect all paths
    List<Ast.Path>  ps1;
    try{ps1=collectNotAnyPaths(p,e);}
    catch(ErrorMessage.PathMetaOrNonExistant pne){
      throw Assertions.codeNotReachable();
      }
//L1..Ln={L| L inside eC}//in path not prime// not repeat check stage
    List<ClassB> l1n = CollectClassBs0.of(e);
//paths'=usedPathsFix(p,reorganize(Ps'), empty,Coherent)
    Paths paths1 = Paths.reorganize(ps1);
    paths1 = usedPathsFix(p,paths1,Collections.emptyList(),Phase.Coherent);
    assert paths1.checkAllDefined(p);
//paths0=reorganize(Ps,Ps') U paths' U (deepImplements(L1)U..U deepImplements(Ln)).pop()
//paths=usedPathsFix(p,paths0, empty,Typed)
    Paths paths0= Paths.reorganize(psBoth).union(paths1);
    Paths acc=Paths.empty();
    for(ClassB li:l1n){
      acc=acc.union(deepImplements(li));
      }
    paths0=paths0.union(acc.pop());
    Paths paths=usedPathsFix(p,paths0,Collections.emptyList(),Phase.Typed);
    assert paths.checkAllDefined(p);
    PathsPaths result= new PathsPaths(paths.setMinus(paths1),paths1);
    System.out.println("UsedPaths:\npaths:"+result.left+"\npaths':"+result.right+"\n-----------------\n");
    return result;
    }

  static private Paths usedPathsFix(Program p, Paths paths, List<List<Ast.C>> css,Phase phase0) {
//- usedPathsFix(p,empty,empty) =empty   
    if (paths.isEmpty()){
      assert css.isEmpty();
      return Paths.empty();
      }
    
//- usedPathsFix(p,paths,Css)= usedPathsFix(p.pop(),paths.pop(),empty).push(Css)
//paths.top()\Css==empty
    List<List<Ast.C>> topLessCss = new ArrayList<>(paths.top());
    topLessCss.removeAll(css);
    if(topLessCss.isEmpty()){
      Paths popPaths=paths.pop();
      if(popPaths.isEmpty()){
        return Paths.empty().push(css);
        }
      assert !(p instanceof FlatProgram):
        popPaths;      
      return usedPathsFix(p.pop(),popPaths,Collections.emptyList(),phase0).push(css);
      }
//-usedPathsFix(p,paths,Css)= usedPathsFix(p, paths U paths0,minimize(paths0.top() U Css)) // U on paths does minimize() internally
//paths.top()\Css!=empty
//paths0=usedPathsL(p.top(),paths.top()\Css)
    Paths paths0=usedPathsL(p,p.top(),topLessCss,phase0);
    List<List<Ast.C>> css1=new ArrayList<>(paths.top());
    css1.addAll(css);
    return usedPathsFix(p,paths.union(paths0),Paths.minimize(css1),phase0);
    }

//- usedPathsL(L, Cs1..Csn)=usedInnerL(L(Cs1),Cs1) U ... U usedInnerL(L(Csn),Csn)
  static private Paths usedPathsL(Program pForError,ClassB l, List<List<Ast.C>> css,Phase phase0) {
    Paths result=Paths.empty();
    for(List<Ast.C> csi : css){
      assert !csi.isEmpty();
      ClassB li;try{li=l.getClassB(csi);}
      catch(ErrorMessage.PathMetaOrNonExistant pne){
        /*Position p=FindPathUsage._locate(pForError,l,Path.outer(1,
          csi));*/
        throw pne.withListOfNodeNames(csi).withCb(l);//.withWherePathWasWritten(p);
        }
      ClassB liTop=li;
      if (csi.size()!=0){liTop=l.getClassB(Collections.singletonList(csi.get(0)));}
      assert IsCompiled.of(liTop);
      //checked after for newPaths: when the offending value is produced, so we have more context for error message
      //  throw new ErrorMessage.PathMetaOrNonExistant(true, Collections.singletonList(csi.get(0)), l, null,null);
      
      Paths newPaths=usedInnerL(li,csi,phase0);
      try{newPaths.checkAllDefined(pForError);}
      catch(ErrorMessage.PathMetaOrNonExistant pne){
        Position p=FindPathUsage._locate(pForError,li,Path.outer(csi.size()+1/*TODO we need to fix this crap for real*/,
                pne.getListOfNodeNames()));
        throw pne.withWherePathWasWritten(p);
        }
      result=result.union(newPaths);
      }
    return result;
    }
  
//- usedInnerL(LC,Cs)=paths.prefix(Cs)
  static private Paths usedInnerL(ClassB lc, List<Ast.C> cs,Phase phase0) {
  //LC={_ implements Ps, M1..Mn}//in implementation, error if not compiled
  if(!IsCompiled.of(lc)){
    throw new ErrorMessage.IncompleteClassIsRequired(
            "library not compiled yet is required to be typed",
            null,Path.outer(0, cs), Collections.emptyList(),Ast.Position.noInfo
            );
  }
  Paths paths=Paths.reorganize(lc.getSuperPaths());
  for(ClassB.Member mi: lc.getMs()){
    paths=paths.union(usedInnerM(mi,phase0));
    }
  //paths=reorganize(Ps) U usedInnerM(M1) U... U usedInnerM(Mn))
  return paths.prefix(cs);}
 
  static private Paths usedInnerM(Member m,Phase phase0) {  
    if(m instanceof NestedClass){
      NestedClass nc=(NestedClass)m;
      return usedInnerL((ClassB)nc.getInner(),Collections.emptyList(),phase0).pop();
      }
    List<Path> result1;
    List<ClassB> l1n = Collections.emptyList();
    if(m instanceof MethodWithType){
      MethodWithType mwt=(MethodWithType)m;
      result1=CollectPaths0.of(mwt);
      if(phase0==Phase.Typed && mwt.get_inner().isPresent()){
        l1n = CollectClassBs0.of(m.getInner());
        }
      }
    else{
      assert m instanceof MethodImplemented;
      result1=CollectPaths0.of(m.getInner());
      }
    Paths result2=Paths.reorganize(result1);
    Paths acc=Paths.empty();
    for(ClassB li: l1n){acc=acc.union(usedInnerL(li,Collections.emptyList(),phase0));}
    return result2.union(acc.pop());
    }
  
  static private Paths deepImplements(ClassB l){
    Paths res=Paths.reorganize(l.getSuperPaths());
    Paths acc=Paths.empty();
    for(Member mi:l.getMs()){
      ExpCore e;try{e=mi.getInner();}
      catch(NoSuchElementException nsee){continue;}
      for(ClassB cbij:CollectClassBs0.of(e)){
        acc=acc.union(deepImplements(cbij));
        }
      }
    return res.union(acc.pop());
    }
  
  static private List<Ast.Path> collectNotAnyPaths(Program p,ExpCore e) {
    class HeuristicForNotAnyPathsSplit extends PropagatorVisitor{
    public Void visit(ClassB s) {return null;} 
      protected List<Path> paths=new ArrayList<Path>();
      private void add(Path p){this.paths.add(p);}
    //non determinism heuristic:
    //**if P.m(_) inside e, P not Any
      public Void visit(MCall s) {
        Path p=justPath(s.getInner());
        if (p!=null){add(p);}
        return super.visit(s);
        }
    //**if ( _ T x=P _ _) inside e and T!=class Any, P not Any.
    //**if (mdf P x=_ _) inside e, P not Any  
      protected void liftDec(Block.Dec s) {
        Path pt=s.getT().match(nt->nt.getPath(), hType->hType.getPath());
        if(!pt.isPrimitive()){add(pt);}
        Path p=justPath(s.getInner());
        if (p!=null){add(p);}
        super.liftDec(s);
        }
      private Path justPath(ExpCore e){
        if(e instanceof ExpCore.EPath){
          if(!((ExpCore.EPath)e).getInner().isPrimitive()){
            return ((ExpCore.EPath)e).getInner();
            }
          }
        if(e instanceof ExpCore.Block){return justPath(((ExpCore.Block)e).getInner());}
        return null;
        }
    //**if p(Pi).Cache=Typed, Pi is not Any
      @Override protected void liftP(Path s) { 
        if(s.isPrimitive()){return;}
        try{if(p.extractClassB(s).getPhase()==Phase.Typed){
          super.liftP(s); return;
          }}
        catch(ErrorMessage.PathMetaOrNonExistant pne){/*we do not rise this error while computing the heuristic*/}
        }
      
      //**if using P _ _ inside e, P not Any
      public Void visit(ExpCore.Using s) {
        if (!s.getPath().isPrimitive()){add(s.getPath());}
        return super.visit(s);
        }
      //**if catch T inside e, T.P not Any
      protected void liftO(ExpCore.Block.On on){
        Path pOn=on.getT().match(
                normType->normType.getPath(),
                hType->hType.getPath());
        if (!pOn.isPrimitive()){add(pOn);}
        super.liftO(on);
        }
      List<Path> result(ExpCore e){
        e.accept(this);
        return this.paths;
        }
      }
    return new HeuristicForNotAnyPathsSplit().result(e);
    }
}
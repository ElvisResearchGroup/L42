package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

import ast.Ast;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ErrorMessage.IncompleteClassIsRequired;
import ast.ErrorMessage.PathNonExistant;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
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
  catch(PathNonExistant pne){
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
    List<Ast.Path>  ps=CollectPaths0.of(e);//collect all paths
    List<Ast.Path>  ps1;
    try{ps1=collectNotAnyPaths(p,e);}
    catch(ErrorMessage.PathNonExistant pne){
      throw Assertions.codeNotReachable();
      }
    ps.removeAll(ps1);
    Paths paths= Paths.reorganize(ps);
    List<ClassB> l1n = CollectClassBs0.of(e);
//    paths=usedPathsFix(p,reorganize(Ps) 
//            U (deepImplements(L1)U..U deepImplements(Ln)).pop(), empty)
    Paths acc=Paths.empty();
    for(ClassB li:l1n){
      acc=acc.union(deepImplements(li));
      }
    paths=paths.union(acc.pop());
    paths=usedPathsFix(p,paths,Collections.emptyList());
    paths.checkAllDefined(p);
    
    Paths paths1 = Paths.reorganize(ps1);
    paths1 = usedPathsFix(p,paths1,Collections.emptyList());
    paths1.checkAllDefined(p);
    paths=paths.setMinus(paths1);
    PathsPaths result= new PathsPaths(paths,paths1);
    System.out.println("UsedPaths:\npaths:"+result.left+"\npaths':"+result.right+"\n-----------------\n");
    return result;
    }

  static private Paths usedPathsFix(Program p, Paths paths, List<List<Ast.C>> css) {
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
      return usedPathsFix(p.pop(),popPaths,Collections.emptyList()).push(css);
      }
//-usedPathsFix(p,paths,Css)= usedPathsFix(p, paths U paths0,minimize(paths0.top() U Css)) // U on paths does minimize() internally
//paths.top()\Css!=empty
//paths0=usedPathsL(p.top(),paths.top()\Css)
    Paths paths0=usedPathsL(p.top(),topLessCss);
    List<List<Ast.C>> css1=new ArrayList<>(paths.top());
    css1.addAll(css);
    return usedPathsFix(p,paths.union(paths0),Paths.minimize(css1));
    }

//- usedPathsL(L, Cs1..Csn)=usedInnerL(L(Cs1),Cs1) U ... U usedInnerL(L(Csn),Csn)
  static private Paths usedPathsL(ClassB l, List<List<Ast.C>> css) {
    Paths result=Paths.empty();
    for(List<Ast.C> csi : css){
      ClassB li;try{li=l.getClassB(csi);}
      catch(ErrorMessage.PathNonExistant pne){
        throw pne.withListOfNodeNames(csi).withCb(l);
        }
      result=result.union(usedInnerL(li,csi));
      }
    return result;
    }
  
//- usedInnerL(LC,Cs)=paths.prefix(Cs)
  static private Paths usedInnerL(ClassB lc, List<Ast.C> cs) {
  //LC={_ implements Ps, M1..Mn}//in implementation, error if not compiled
  if(!IsCompiled.of(lc)){
    throw new ErrorMessage.IncompleteClassIsRequired(
            "library not compiled yet is required to be typed",
            null,Path.outer(0, cs), Collections.emptyList(),Ast.Position.noInfo
            );
  }
  Paths paths=Paths.reorganize(lc.getSuperPaths());
  for(ClassB.Member mi: lc.getMs()){
    paths=paths.union(usedInnerM(mi));
    }
  //paths=reorganize(Ps) U usedInnerM(M1) U... U usedInnerM(Mn))
  return paths.prefix(cs);}
  
//- usedInnerM(M)= reorganize({P| P inside M}) U (usedInnerL(L1,empty) U...U usedInnerL(Ln,empty)).pop()
  static private Paths usedInnerM(Member m) {
//L1..Ln={L| L inside M}
    List<Path> result1;
    List<ClassB> l1n = Collections.emptyList();
    if(m instanceof MethodWithType){
      MethodWithType mwt = (MethodWithType)m;
      result1 = CollectPaths0.of(mwt);
      if(mwt.get_inner().isPresent()){
        l1n = CollectClassBs0.of(m.getInner());
        }
      }
    else {
      result1=CollectPaths0.of(m.getInner());
      l1n = CollectClassBs0.of(m.getInner());
      }
    Paths result2=Paths.reorganize(result1);
    Paths result3=Paths.empty();
    for(ClassB li: l1n){result3=result3.union(usedInnerL(li,Collections.emptyList()));}
    return result2.union(result3.pop());
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
    class HeuristicForNotAnyPathsSplit extends CollectPaths0{
    //non determinism heuristic:
    //**if P.m(_) inside e, P not Any
      public Void visit(MCall s) {
        Path p=justPath(s.getInner());
        if (p!=null){this.paths.add(p);}
        return super.visit(s);
        }
    //**if ( _ T x=P _ _) inside e and T!=class Any, P not Any.
    //**if (mdf P x=_ _) inside e, P not Any  
      protected void liftDec(Block.Dec s) {
        Path pt=s.getT().match(nt->nt.getPath(), hType->hType.getPath());
        if(!pt.isPrimitive()){this.paths.add(pt);}
        Path p=justPath(s.getInner());
        if (p!=null){this.paths.add(p);}
        super.liftDec(s);
        }
      private Path justPath(ExpCore e){
        if(e instanceof Path){
          if(!((Path)e).isPrimitive()){
            return (Path)e;
            }
          }
        if(e instanceof ExpCore.Block){return justPath(((ExpCore.Block)e).getInner());}
        return null;
        }
    //**if p(Pi).Cache=Typed, Pi is not Any
      public Void visit(Path s) { 
        if(s.isPrimitive()){return null;}
        try{if(p.extractClassB(s).getPhase()==Phase.Typed){
          return super.visit(s);
          }}
        catch(ErrorMessage.PathNonExistant pne){/*we do not rise this error while computing the heuristic*/}
        return null;
        }
      
      //**if using P _ _ inside e, P not Any
      public Void visit(ExpCore.Using s) {
        if (!s.getPath().isPrimitive()){
          this.paths.add(s.getPath());
          }
        return super.visit(s);
        }
      //**if catch T inside e, T.P not Any
      protected void liftO(ExpCore.Block.On on){
        Path pOn=on.getT().match(
                normType->normType.getPath(),
                hType->hType.getPath());
        if (!pOn.isPrimitive()){
          this.paths.add(pOn);
          }
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
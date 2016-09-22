package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import coreVisitors.IsCompiled;
import coreVisitors.PropagatorVisitor;

class PathsPaths{
  public PathsPaths(Paths left, Paths right) {
    this.left = left;this.right = right;
    }
  final Paths left; final Paths right;}

public class UsedPaths {

  PathsPaths usedPathsE(Program p, ExpCore e){
//- usedPathsE(p,eC)= <reorganize(Ps); usedPathsFix(p,paths, empty)>
//assert that the result includes paths in usedPathsFix(p,paths, empty)  
//Ps,Ps'={P|P inside eC}//arbitrary split of the set; heuristic will apply in the implementation.
    List<Ast.Path>  ps1=CollectPaths0.of(e);//collect all paths
    List<Ast.Path>  ps=collectNotAnyPaths(p,e);
    ps1.removeAll(ps);
//paths= reorganize(Ps')
    Paths paths = Paths.reorganize(ps1);
    return new PathsPaths(Paths.reorganize(ps),usedPathsFix(p,paths,Collections.emptyList()));        
    }

  private Paths usedPathsFix(Program p, Paths paths, List<List<String>> css) {
//- usedPathsFix(p,empty,empty) =empty   
    if (paths.isEmpty()){assert css.isEmpty(); return Paths.empty();}
    
//- usedPathsFix(p,paths,Css)= usedPathsFix(p.pop(),paths.pop(),empty).push(Css)
//paths.top()\Css==empty
    List<List<String>> topLessCss = new ArrayList<>(paths.top());
    topLessCss.removeAll(css);
    if(topLessCss.isEmpty()){
      return usedPathsFix(p.pop(),paths.pop(),Collections.emptyList()).push(css);
      }
//-usedPathsFix(p,paths,Css)= usedPathsFix(p, paths U paths0,minimize(paths0.top() U Css)) // U on paths does minimize() internally
//paths.top()\Css!=empty
//paths0=usedPathsL(p.top(),paths.top()\Css)
    Paths paths0=usedPathsL(p.top(),topLessCss);
    List<List<String>> newCss=new ArrayList<>(paths0.top());
    newCss.addAll(css);
    return usedPathsFix(p,paths.union(paths0),Paths.minimize(newCss));
    }

//- usedPathsL(L, Cs1..Csn)=usedInnerL(L(Cs1),Cs1) U ... U usedInnerL(L(Csn),Csn)
  private Paths usedPathsL(ClassB l, List<List<String>> css) {
    Paths result=Paths.empty();
    for(List<String> csi : css){result=result.union(usedInnerL(l.getClassB(csi),csi));}
    return result;
    }
  
//- usedInnerL(LC,Cs)=paths.prefix(Cs)
  private Paths usedInnerL(ClassB lc, List<String> cs) {
  //LC={_ implements Ps, M1..Mn}//in implementation, error if not compiled
  assert IsCompiled.of(lc);//TODO: should it be a compilation error?
  Paths paths=Paths.reorganize(lc.getSupertypes());
  for(ClassB.Member mi: lc.getMs()){
    paths=paths.union(usedInnerM(mi));
    }
  //paths=reorganize(Ps) U usedInnerM(M1) U... U usedInnerM(Mn))
  return null;}
  
//- usedInnerM(M)= reorganize({P| P inside M}) U (usedInnerL(L1,empty) U...U usedInnerL(Ln,empty)).pop()
  private Paths usedInnerM(Member m) {
//L1..Ln={L| L inside M}
    List<Path> result1;
    if(m instanceof MethodWithType){
      result1 = CollectPaths0.of((MethodWithType)m);
      }
    else result1=CollectPaths0.of(m.getInner());
    List<ClassB> l1n = CollectClassBs0.of(m.getInner());
    Paths result2=Paths.reorganize(result1);
    for(ClassB li: l1n){result2.union(usedInnerL(li,Collections.emptyList()));}
    return result2;
    }
  private List<Ast.Path> collectNotAnyPaths(Program p,ExpCore e) {
    class C extends CollectPaths0{
    //non determinism heuristic:
    //**if P.m(_) inside e, P not Any
      public Void visit(MCall s) {
        Path p=justPath(s.getInner());
        if (p!=null){this.paths.add(p);}
        return super.visit(s);
        }
    //**if ( _ T x=P _ _) inside e and T!=class Any, P not Any.
      protected void liftDec(Block.Dec s) {
        Path p=justPath(s.getInner());
        if (p!=null){this.paths.add(p);}
        super.liftDec(s);
        }
      private Path justPath(ExpCore e){
        if(e instanceof Path){return (Path)e;}
        if(e instanceof ExpCore.Block){return justPath(((ExpCore.Block)e).getInner());}
        return null;
        }
    //**if p(Pi).Cache=Typed, Pi is not Any
      public Void visit(Path s) { 
        //TODO: update with the new caching when available
        if(p.extractClassB(s).getStage().isVerified()){
          return super.visit(s);
          }
        return null;
        }
      List<Path> result(ExpCore e){
        e.accept(this);
        return this.paths;
        }
      }
    return new C().result(e);
    }
}
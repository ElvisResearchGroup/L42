package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.Ast.Type;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import coreVisitors.IsCompiled;

class PathsPaths{
  public PathsPaths(Paths left, Paths right) {
    this.left = left;this.right = right;
    }
  final Paths left; final Paths right;}

public class UsedPaths {
  private List<Ast.Path> collectAllPaths(Ast.MethodType mt) {
    List<Ast.Path>result=new ArrayList<>();
    result.add(mt.getReturnType().getNT().getPath());
    for(Type t:mt.getTs()){result.add(t.getNT().getPath());}
    result.addAll(mt.getExceptions());
    return result;
    }
  private List<Ast.Path> collectAllPaths(ExpCore e) {
    //TODO Auto-generated method stub
    return null;
    }
  private List<Ast.Path> collectNotAnyPaths(ExpCore e) {
    //TODO Auto-generated method stub
    return null;
  }

  PathsPaths usedPathsE(Program p, ExpCore e){
//- usedPathsE(p,eC)= <reorganize(Ps); usedPathsFix(p,paths, empty)>
//assert that the result includes paths in usedPathsFix(p,paths, empty)  
//Ps,Ps'={P|P inside eC}//arbitrary split of the set; heuristic will apply in the implementation.
    List<Ast.Path>  ps1=collectAllPaths(e);
    List<Ast.Path>  ps=collectNotAnyPaths(e);
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
    List<Ast.Path>result=collectAllPaths(m.getInner());
    if(m instanceof MethodWithType){
      result.addAll(collectAllPaths(((MethodWithType)m).getMt()));
      }
    m.getInner().accept(new MAKEACOLLECTVISITOR?)
  return null;
  }
}

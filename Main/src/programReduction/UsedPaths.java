package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;

class PathsPaths{
  public PathsPaths(Paths left, Paths right) {
    this.left = left;this.right = right;
    }
  final Paths left; final Paths right;}

public class UsedPaths {
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
    for(List<String> cs : css){result=result.union(l.)}
    oldProgram.extractCBar vs new methods in classB:
    classB assume compiled class, extractCBar manage errors
    make both better and both in classB
    May be a observeClass, observeNested, observeListNesteds?
    return result;
    }
   


//- usedInnerL(LC,Cs)=prefix(Cs,reorganize(Ps) U usedInnerM(M1) U... U usedInnerM(Mn))
//where LC={_ implements Ps, M1..Mn}//in implementation, error if not compiled

//- usedInnerM(M)= reorganize({P| P inside M}) U (usedInnerL(L1,empty) U...U usedInnerL(Ln,empty)).pop()
//L1..Ln={L| L inside M}

}

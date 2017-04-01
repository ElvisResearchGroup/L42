package programReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.NormType;
import ast.Ast.Stage;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.CloneVisitor;

public class MultiTypeSystem {

public static Program typeProgram(Paths paths0,Paths paths1, Program p){
  if(paths0.isEmpty() && paths1.isEmpty()){return p;}
  if(p instanceof FlatProgram){
    assert paths0.pop().isEmpty() && paths1.pop().isEmpty(); 
    return p.updateTop(typeLibrary(paths0.top(),paths1.top(),p));
  }
  Program p0=typeProgram(paths0.pop(),paths1.pop(),p.pop());
  Program p1=p.growFellow(p0);
  ClassB l=typeLibrary(paths0.top(),paths1.top(),p1);
  return p1.updateTop(l);
  }

private static ClassB typeLibrary(List<List<Ast.C>> current0,List<List<Ast.C>> current1, Program p) {
  ClassB result=p.top();
  for(List<Ast.C> csi : current0){
    Program pi=p.navigate(csi);
    assert pi.top().getPhase()!=Phase.None:
    "";
    ClassB li=typeSingle(Phase.Typed,pi);
    result=result.onClassNavigateToPathAndDo(csi, oldLi->li);
    }
  for(List<Ast.C> csi : current1){
    Program pi=p.navigate(csi);
    assert pi.top().getPhase()!=Phase.None:
    "";
    ClassB li=typeSingle(Phase.Coherent,pi);
    result=result.onClassNavigateToPathAndDo(csi, oldLi->li);
    }
  return result;
  }

private static ClassB typeSingle(Phase phase,Program p) {
 return newTypeSystem.TypeSystem.instance().topTypeLib(phase,p);
 }

public static ExpCore toAny(Paths paths, ExpCore e) {
  return e.accept(new CloneVisitor(){
    public ExpCore visit(ClassB s) {return s;}
    public ExpCore visit(ExpCore.EPath s) {
      if(paths.contains(s.getInner())){
        return s.withInner(Ast.Path.Any());
        }
      return s;
      } 
    });
}

public static ExpCore typeMetaExp(Program p, ExpCore e) {
  return newTypeSystem.TypeSystem.instance().topTypeExp(p,e);
  }
}

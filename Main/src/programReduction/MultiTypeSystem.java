package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Type;
import ast.ErrorMessage;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Stage;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import auxiliaryGrammar.Functions;
import coreVisitors.CloneVisitor;
import facade.L42;
import newTypeSystem.FormattedError;
import newTypeSystem.GuessTypeCore;

public class MultiTypeSystem {

public static Program typeProgram(Paths paths0,Paths paths1, Program p){
  if(paths0.isEmpty() && paths1.isEmpty()){return p;}
  if(p instanceof FlatProgram){
    assert paths0.pop().isEmpty() && paths1.pop().isEmpty(); 
    ClassB l=pL(paths0.top(),paths1.top(),p);
    return p.updateTop(l);
  }
  Program p0=typeProgram(paths0.pop(),paths1.pop(),p.pop());
  Program p1=p.growFellow(p0);
  ClassB l=pL(paths0.top(),paths1.top(),p1);
  return p1.updateTop(l);
  }

private static ClassB pL(List<List<Ast.C>> current0,List<List<Ast.C>> current1, Program p) {
  ClassB result=p.top();
  for(List<Ast.C> csi : current0){
    if(current1.contains(csi)){continue;}
    Program pi=p.navigate(csi);
    assert pi.top().getPhase()!=Phase.None:
    "";
    ClassB li=typeSingle(Phase.Typed,pi);
    result=result.onClassNavigateToPathAndDo(csi, oldLi->li);
    }
  p=p.updateTop(result);
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
      if(s.getInner().isPrimitive()){return s;}
      if(!paths.containsPrefixFor(s.getInner())){
        return s.withInner(Ast.Path.Any());
        }
      return s;
      } 
    });
}


public static ExpCore typeAndAdapt(ExpCore ec,Program p,Paths paths){
  ExpCore eAny=MultiTypeSystem.toAny(paths,ec);
  @SuppressWarnings("unused")
  ExpCore errorIfFails = newTypeSystem.TypeSystem.instance()._topTypeExp(p,eAny,Type.immAny,true);
  try{
    ExpCore e1=new ExpCore.MCall(ec,new MethodSelector("#toLibrary",-1,Collections.emptyList()),
      Doc.empty(),Collections.emptyList(), Position.noInfo,null,null);
    ExpCore res = newTypeSystem.TypeSystem.instance()._topTypeExp(p,e1,Type.immLibrary,false);
    if(res!=null){return res;}
    }
  catch(ErrorMessage|AssertionError em){}
  //TODO: the Assertion error above is because we may create a non guessable expression.
  //When we implement proper well formedness, we can check for that!
  try{
    ExpCore.Block.Dec d=new ExpCore.Block.Dec(
          false,Type.immVoid,Functions.freshName("unused", L42.usedNames),ec);
    ExpCore e1=new ExpCore.Block(
      Doc.empty(),Collections.singletonList(d),ClassB.docClass(Doc.empty()),
      Collections.emptyList(), Position.noInfo, null);
    ExpCore res = newTypeSystem.TypeSystem.instance()._topTypeExp(p,e1,Type.immLibrary,false);
    if(res!=null){return res;}
    }
  catch(FormattedError|ErrorMessage t){}
  return newTypeSystem.TypeSystem.instance()._topTypeExp(p,ec,Type.immLibrary,true);
  }

}

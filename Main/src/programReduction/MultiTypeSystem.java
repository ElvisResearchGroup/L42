package programReduction;

import java.util.ArrayList;
import java.util.List;

import ast.Ast.NormType;
import ast.Ast.Stage;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.CloneVisitor;

public class MultiTypeSystem {

public static Program typeProgram(Paths paths, Program p){
//(pEmpty)---------------------------------
//empty |- p :p
  if(paths.isEmpty()){return p;}
//      Css|-p:L
//(pNoPop)--------------------------------  p.pop() undefined
//      Css  |- p :L,empty
  if(p instanceof FlatProgram){
    return p.updateTop(typeLibrary(paths.current,p));
  }
//       paths |- p.pop():p0
//       Css|-p1:L
//(pPop)----------------------------------  p1=p.growFellow(p0)
//       Css paths |- p : p1.update(L)
  Program p0=typeProgram(paths.pop(),p.pop());
  Program p1=p.growFellow(p0);
  ClassB l=typeLibrary(paths.current,p1);
  return p1.updateTop(l);
  }

private static ClassB typeLibrary(List<List<String>> current, Program p) {
//forall Csi : pi|-pi.top(): Li //if Li.Phase=Typed, the check will be just asserted
//(pL) ---------------------------------------   pi=p.navigate(Csi)
//Cs1..Csn|-p:p.top()[Cs1=L1,.. Csn=Ln]    
  ClassB result=p.top();
  for(List<String> csi : current){
    Program pi=p.navigate(csi);
    ClassB li=typeSingle(pi,pi.top());
    result=result.onClassNavigateToPathAndDo(csi, oldLi->li);
  }
  return result;
  }

private static ClassB typeSingle(Program p, ClassB l) {
  auxiliaryGrammar.Program pOld = p.oldRepr();
  //pOld=pOld.getExecutableStar();
  pOld.recomputeStage();
  l=(ClassB) l.accept(new coreVisitors.CloneVisitor(){
  public ExpCore visit(ClassB s) {
    s=(ClassB) super.visit(s);
    s.getStage().setStage(Stage.Star);
    return s;
  }});
  facade.Configuration.typeSystem.checkCt(pOld.pop(),l);
  l=(ClassB) l.accept(new coreVisitors.CloneVisitor(){
    public ExpCore visit(ClassB s) {
      s=s.withPhase(Phase.Typed);
      s=(ClassB) super.visit(s);
      s.getStage().setVerified(true);//TODO: remove when there is new TS
      s.getStage().setStage(Stage.Star);
      return s;
    }});
  return l;//TODO: replace with new TS soonish
  }

public static ExpCore toAny(Paths paths, ExpCore e) {
  return e.accept(new CloneVisitor(){
    public ExpCore visit(ClassB s) {return s;}
    public ExpCore visit(Ast.Path s) {
      if(paths.contains(s)){
        return Ast.Path.Any();
        }
      return s;
      } 
    });
}

public static void typeMetaExp(Program p, ExpCore e) {
  auxiliaryGrammar.Program pOld = p.oldRepr();
  pOld=pOld.getExecutableStar();
  pOld.recomputeStage();
  facade.Configuration.typeSystem.checkMetaExpr(pOld,e);
  //TODO: replace with new TS soonish
  }
}

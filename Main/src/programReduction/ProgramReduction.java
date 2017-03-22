package programReduction;

import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;

import java.util.Collections;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.C;
import ast.ExpCore.ClassB;
import coreVisitors.IsCompiled;

public class ProgramReduction {
  public static ClassB allSteps(ClassB l){
    Program top=new FlatProgram(l);
    while(!IsCompiled.of(top.top())){
      top=step(top);
      }
    return top.top();
    }
  static Program step(Program p){
//precondition: at top level we have a L not of form LC
    assert ! IsCompiled.of(p.top());
    CtxL top=CtxL._split(p.top());
    assert top!=null;
    Member m=top.originalCtxM();
    assert !IsCompiled.of(m);
    ExpCore hole=top.originalHole();
    if (hole instanceof ClassB){
      assert !IsCompiled.of(hole);
      return enter(p,top,m);
      }
    ClassB.NestedClass nc=(NestedClass) m;
    return top(p,nc);
    }


  static private Program top(Program p,NestedClass nc) {
    ExpCore ec=nc.getInner();
    assert IsCompiled.of(ec);
    assert !(ec instanceof ClassB);
    //ExpCore ec1=new Norm().norm(p, ec);//TODO:May disappear with new TS?
    PathsPaths pair = UsedPaths.usedPathsE(p, ec);
    Paths paths=pair.left;
    Paths paths1=pair.right;
    Program p0=Norm.multiNorm(p,paths.union(paths1));
    Program p1=MultiTypeSystem.typeProgram(paths,paths1, p0);
    ExpCore annEc1=MultiTypeSystem.typeMetaExp(p1,MultiTypeSystem.toAny(paths,ec));
    ClassB res=reduceE(p1,annEc1,C.of("NameDebug_"+nc.getName()));
    res=privateMangling.RefreshUniqueNames.refresh(res);
    ClassB top=p1.top();
    assert top.getNested(Collections.singletonList(nc.getName()))!=null;//would actually fail if not there
    top=top.withMember(nc.withE(res));
    return p1.updateTop(top);
    }
  static private ClassB reduceE(Program p, ExpCore e,Ast.C nameDebug) {
    ExpCore res=facade.Configuration.reduction.metaExp(p.reprAsPData(), e,nameDebug);
    if(res instanceof ClassB){return (ClassB)res;}
    throw new ast.ErrorMessage.MalformedFinalResult(p.top(),"error is:\n\n"+sugarVisitors.ToFormattedText.of(res));
    //TODO: add error manager/printer
    }
  
/*
        p.push(ctxL, L)==>+ p'      p.top()={interface? implements Ps, MCs  M Ms}
(enter)-------------------------    M not of form MC
          p ==> p'.pop()            M.e=ctxC[L], L not of form LC
                                    ctxL={interface? implements Ps, MCs  M[with e=ctxC] Ms}
                                    p'.top() of form LC
*/
  static private Program enter(Program p, CtxL ctxL, Member m) {
    Program p1=p.push(ctxL,(ClassB)ctxL.originalHole());
    assert !IsCompiled.of(p1.top());
    while(!IsCompiled.of(p1.top())){p1=step(p1);}
    return p1.pop(); 
    }
  }

package programReduction;

import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import caching.Loader;
import caching.Phase1CacheValue;

import java.nio.file.Path;
import java.util.Collections;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.C;
import ast.ExpCore.ClassB;
import coreVisitors.IsCompiled;
import facade.L42;
import newReduction.ClassTable;
import profiling.Timer;

public class ProgramReduction {
  public ProgramReduction(Path path,boolean saveVCache) {
    this.loader=new Loader(path);
    this.saveVCache=saveVCache;
    }
  Loader loader;
  boolean saveVCache;
  public ClassB allSteps(Program top){
    while(!IsCompiled.of(top.top())){
      top=step(top);
      }
    return top.top();
    }
  private Program step(Program p){
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


  private Program top(Program p,NestedClass nc) {
    ExpCore ec=nc.getInner();
    assert IsCompiled.of(ec);
    assert !(ec instanceof ClassB);
    System.out.println("Top running on nc:"+nc.getName());
    Paths paths=UsedPaths.tryTypedPaths(p, ec);
    Paths paths1=UsedPaths.tryCoherentPaths(p, ec);
    Program p0=Norm.multiNorm(p,paths);
    Program p1=MultiTypeSystem.typeProgram(paths,paths1, p0);
    @SuppressWarnings("unused")
    ExpCore justToTest=MultiTypeSystem.typeMetaExp(p1,MultiTypeSystem.toAny(paths1,ec));
    ExpCore annEc1=MultiTypeSystem.typeMetaExp(p1,ec);
    saveFirstTimeCache(p1);
    ClassB res=loader.execute(p1, paths, annEc1);
    //ClassB res=reduceE(p1,annEc1,C.of("NameDebug_"+nc.getName()));
    res=privateMangling.RefreshUniqueNames.refresh(res);
    ClassB top=p1.top();
    assert top.getNested(Collections.singletonList(nc.getName()))!=null;//would actually fail if not there
    top=top.withMember(nc.withE(res));
    return p1.updateTop(top);
    }
  private void saveFirstTimeCache(Program p) {
    if(!saveVCache){return;}
    Timer.deactivate("RunningWithoutParsedCache");
    saveVCache=false;
    if(!L42.cacheK.hasFileName()){return;}
    try{while(true){p=p.pop();}}
    catch(Program.EmptyProgram ep){}  
    ClassB topCb=p.top();
    Phase1CacheValue cv=new Phase1CacheValue(L42.usedNames,topCb,p.getFreshId());
    Path vPath = L42.root.resolve(L42.cacheK.firstSourceName()+".V42");
    Path kPath = L42.root.resolve(L42.cacheK.firstSourceName()+".K42");
    cv.saveOnFile(vPath);
    L42.cacheK.saveOnFile(kPath);
    }
  private ClassB reduceE(Program p, ExpCore e,Ast.C nameDebug) {
    ExpCore res=facade.Configuration.reduction.metaExp(p.reprAsPData(), e,nameDebug);
    if(res instanceof ClassB){return (ClassB)res;}
    throw new ast.ErrorMessage.MalformedFinalResult(p.top(),
            "See message above");//"error is:\n\n"+sugarVisitors.ToFormattedText.of(res));
    }

/*
        p.push(ctxL, L)==>+ p'      p.top()={interface? implements Ps, MCs  M Ms}
(enter)-------------------------    M not of form MC
          p ==> p'.pop()            M.e=ctxC[L], L not of form LC
                                    ctxL={interface? implements Ps, MCs  M[with e=ctxC] Ms}
                                    p'.top() of form LC
*/
  private Program enter(Program p, CtxL ctxL, Member m) {
    Program p1=p.push(ctxL,(ClassB)ctxL.originalHole());
    assert !IsCompiled.of(p1.top());
    while(!IsCompiled.of(p1.top())){p1=step(p1);}
    return p1.pop();
    }
  }

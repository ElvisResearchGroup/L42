package programReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import auxiliaryGrammar.Functions;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.CollectClassBs0;
import coreVisitors.CollectPaths0;
import coreVisitors.FindPathUsage;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import coreVisitors.PropagatorVisitor;
import facade.L42;
import sugarVisitors.CollapsePositions;
import tools.Assertions;

public class UsedPaths {

static boolean alive(Program p,Path P){
  if(P.isPrimitive()){return true;}
  return alive(p.get(P.outerNumber()),P.getCBar());
  }
static boolean alive(ClassB l,List<Ast.C>cs){
  if(cs.isEmpty()){return true;}
  ExpCore e;try{e=l.getNested(Collections.singletonList(cs.get(0))).getInner();}
  catch(ErrorMessage.PathMetaOrNonExistant ne){return false;}
  return aliveE(e,cs.subList(1,cs.size()));
  }

//to support the sugar for L doc = (doc L)
static boolean aliveE(ExpCore e,List<Ast.C>cs){
  if(e instanceof ClassB){return alive((ClassB)e,cs);}
  if(!(e instanceof Block)){return true;}
  Block b=(Block)e;
  if(!b.getDecs().isEmpty()){return true;}
  return aliveE(b.getE(),cs);
  }

static Paths tryCoherentPaths(Program p, ExpCore ec){
  try{
    List<Path> ps1 = collectNotAnyPaths(p,ec);
    return reachablePaths(p,Paths.reorganize(ps1),Collections.emptyList(),false);
    }
  catch(ErrorMessage.PathMetaOrNonExistant pne){
    throw Assertions.codeNotReachable();
    }
  }
static Paths tryTypedPaths(Program p, ExpCore ec){
 /*
  *   catch(PathMetaOrNonExistant pne){
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
  */
  Paths paths0=Paths.reorganize(CollectPaths0.of(ec));
  List<ClassB> l1n = CollectClassBs0.of(ec);
    {
    Paths acc=Paths.empty();
    for(ClassB li:l1n){
      acc=acc.union(stronglyDeepImplements(li));
      }
    paths0=paths0.union(acc.pop());
    }
  List<Ast.MethodType>mhs=new ArrayList<>();
  for(ClassB li:l1n){
    recursiveAux(li,mhs,Collections.emptyList());
    }
  List<Ast.Path> ps0=new ArrayList<>();
  for(Ast.MethodType mhi : mhs){
    addExternalPath(ps0,mhi.getReturnType().getPath());
    for(Type ti: mhi.getTs()){
      addExternalPath(ps0,ti.getPath());
      }
    for(Type ti: mhi.getExceptions()){
      addExternalPath(ps0,ti.getPath());
      }
    }
  Paths paths1=Paths.empty();
  for(Ast.Path pi:ps0){
    Paths paths1i=null;
    try{//ps1 here is implicit as the set of successful pi
      paths1i=reachablePaths(p,Paths.reorganize(Collections.singletonList(pi)),Collections.emptyList(),true);
      }
    catch(PathMetaOrNonExistant|IncompleteClassIsRequired t){}
    if(paths1i!=null){paths1=paths1.union(paths1i);}
    }
  Paths res=reachablePaths(p,paths0,Collections.emptyList(),true).union(paths1);
  assert res.checkAllDefined(p);
  return res;
  }

private static void addExternalPath(List<Path> ps0, Path path) {
  if(!path.isCore()){return;}
  if(path.outerNumber()==0){return;}
  ps0.add(path.setNewOuter(path.outerNumber()-1));
  return;
  }

static void recursiveAux(ClassB l,List<Ast.MethodType>mhs,List<Ast.C>cs){
  for(MethodWithType mi:l.mwts()){
    mhs.add(From.from(mi.getMt(), Path.outer(0, cs)));
    }
  for(NestedClass ni:l.ns()){
    recursiveAux((ClassB)ni.getE(),mhs,Functions.push(cs,ni.getName()));
    }
  }


  static private Paths reachablePaths(Program p, Paths paths, List<List<Ast.C>> css,boolean forTyped) {
    if (paths.isEmpty()){
      assert css.isEmpty();
      return Paths.empty();
      }
    List<List<Ast.C>> css1=new ArrayList<>(paths.top());
    css1.addAll(css);
    css1=Paths.minimize(css1);
    //is Css'!=Css ?
    if(css1.size()!=css.size() || !css1.containsAll(css)){
      Paths paths0= Paths.empty();
      for(List<Ast.C> csi : css1){
        //assert csi.size()!=0;//may be
        try{
          if(csi.size()==0 ||
              !IsCompiled.of(p.top().getClassB(Collections.singletonList(csi.get(0))))){
            throw new ErrorMessage.IncompleteClassIsRequired(
              "library not compiled yet is required to be typed",
               null,Path.outer(0, csi), Collections.emptyList(),
               Ast.Position.noInfo
               );
            }
          }
        catch(ErrorMessage.PathMetaOrNonExistant pne){
          throw pne.withListOfNodeNames(csi).withCb(p.top());}// .withWherePathWasWritten(p);
        Paths pathsi;try {pathsi=reachableFromL(p.top().getClassB(csi),forTyped);}
        catch(ErrorMessage.PathMetaOrNonExistant pne){
          throw pne.withListOfNodeNames(csi).withCb(p.top());}
        pathsi=pathsi.prefix(csi);
        paths0=paths0.union(pathsi);
        }
      return reachablePaths(p, paths.union(paths0),css1,forTyped);
      }
    if(paths.pop().isEmpty()){
      return Paths.empty().push(css);
      }
     Paths res = reachablePaths(p.pop(),paths.pop(),Collections.emptyList(),forTyped);
     return res.push(css);
    }

private static void checkPathMetaOrNonExistant(Program pForError, List<Ast.C> csi, ClassB li, Paths newPaths) {
  //check no thisns
  {int i=0;for(Paths psi=newPaths;!psi.isEmpty();i++,psi=psi.pop()){
    if(!psi.current.contains(Collections.emptyList())){continue;}
    Position p=FindPathUsage._locate(pForError,li,Path.outer(i));
    throw new ErrorMessage.ThisnRequiredToType(i,li,li.getP(),p);
  }}
  assert !newPaths.containsPrefixFor(Path.outer(0));
  //check Cs
  try{newPaths.checkAllDefined(pForError);}
  catch(ErrorMessage.PathMetaOrNonExistant pne){
    Position p=FindPathUsage._locate(pForError,li,Path.outer(csi.size()+1/*TODO we need to fix this crap for real*/,
            pne.getListOfNodeNames()));
    throw pne.withWherePathWasWritten(p);
    }
}

static private Paths reachableFromL(ClassB l,boolean forTyped){
  Paths paths=Paths.reorganize(l.getSuperPaths());
  for(ClassB.Member mi: l.getMs()){
    paths=paths.union(reachableFromM(mi,forTyped));
    }
  return paths;
  }

  static private Paths reachableFromM(Member m,boolean forTyped) {
    if(m instanceof NestedClass){
      NestedClass nc=(NestedClass)m;
      return reachableFromL((ClassB)nc.getInner(),forTyped).pop();
      }
    List<Path> result1;
    if(m instanceof MethodWithType){
      result1=CollectPaths0.of((MethodWithType)m);
      }
    else{assert m instanceof MethodImplemented;
      result1=CollectPaths0.of(m.getInner());
      }
    Paths result2=Paths.reorganize(result1);
    if(!forTyped){return result2;}
    ExpCore e=null;
    if(m instanceof MethodWithType){e=((MethodWithType)m).get_inner();}
    else {e=m.getInner();}
    if(e==null){return result2;}
    List<ClassB> l1n = Collections.emptyList();
    l1n = CollectClassBs0.of(e);
    Paths acc=Paths.empty();
    for(ClassB li: l1n){acc=acc.union(reachableFromL(li,true));}
    return result2.union(acc.pop());
    }

  static private Paths stronglyDeepImplements(ClassB l0){
    Paths res=Paths.reorganize(l0.getSuperPaths());
    Paths acc=Paths.empty();
    for(Member mi:l0.getMs()){
      if (mi instanceof MethodWithType &&
         ((MethodWithType)mi).get_inner()==null){continue;}
      ExpCore e=mi.getInner();
      for(ClassB cbij:CollectClassBs0.of(e)){
        acc=acc.union(stronglyDeepImplements(cbij));
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
    protected void liftDec(Block.Dec s) {
        if(s.getT().isPresent()){
          Path pt=s.getT().get().getPath();
          if(!pt.isPrimitive()){add(pt);}
          }
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
        Path pOn=on.getT().getPath();
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
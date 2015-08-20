package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.NestedLocator;
import ast.Util.PathPath;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.GuessTypeCore;

public class RenameMembers extends coreVisitors.CloneWithPath{
  CollectedPrivates maps;//ClassB start;
  public RenameMembers(CollectedPrivates maps) {
    super();
    this.maps = maps;
  }
  public static  ClassB of(CollectedPrivates maps,ClassB cb){
    return (ClassB)cb.accept(new RenameMembers(maps));
  }
  public static  ClassB of(Path src,Path dest,ClassB cb){
    NestedLocator nl = pathPathToLocator(src,dest);
    CollectedPrivates maps=new CollectedPrivates();
    maps.privatePaths.add(nl);
    return of(maps,cb);
  }
  public static  ClassB of(List<PathPath> pp,ClassB cb){
    CollectedPrivates maps=new CollectedPrivates();
    for(PathPath ppi:pp){
      NestedLocator nl = pathPathToLocator(ppi.getPath1(),ppi.getPath2());
      maps.privatePaths.add(nl);
    }
    return of(maps,cb);
  }
  private static NestedLocator pathPathToLocator(Path src, Path dest) {
    List<String> cs = src.getCBar();
    List<Member> mTail=new ArrayList<>();
    List<Integer> mPos=new ArrayList<>();
    List<ClassB> mOuters=new ArrayList<>();
    addCs(cs.subList(0, cs.size()-1), mTail, mPos,mOuters);
    NestedLocator nl=new NestedLocator(mTail, mPos,mOuters, cs.get(cs.size()-1));
    nl.setNewPath(dest);
    return nl;
  }
  private static void addCs(List<String> cs, List<Member> mTail, List<Integer> mPos,List<ClassB> mOuters) {
    for(String s:cs){
      mTail.add(new ClassB.NestedClass(ast.Ast.Doc.empty(),s,new ExpCore.WalkBy(), null));
      mPos.add(1);
      }
  }  
  
  
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String>cs=s.getCBar();
        if(cs.isEmpty()){return s;}//no need to rename outers
        List<Member> current = new ArrayList<>(this.getAstNodesPath());
        List<Integer> currentIndexes = new ArrayList<>(this.getAstIndexesPath());
        boolean canCut=cutUpTo(s.outerNumber(),current,currentIndexes);
        if(!canCut){return s;}
        int whereImSize=current.size();
        addCs(s.getCBar(),current,currentIndexes);
        for(NestedLocator nl:maps.privatePaths){
          if(whereImSize>nl.getMPos().size()){continue;}
          //situation: rename: s1 c1->path   current path locator is:  whereIm c cs
          //check #whereIm<=#s1 and  whereIm c cs =s1 _ 
          assert isFullyPositive(nl.getMPos()):nl.getMPos();
          boolean compatible= compatible(current, currentIndexes, nl);
          if(!compatible){continue;}
          int extraCs=(current.size()-nl.getMPos().size())-1;//the class name in nl.that
          Path pi=getDestPath(this.getClassNamesPath().size(),nl,s,extraCs);//TODO:can be made more efficient without creating the listPaths
          return pi;
          }
        return s;
        }
      private boolean cutUpTo(int outerNumber, List<Member> current, List<Integer> currentIndexes) {
        int size=current.size();
        assert size==currentIndexes.size();
        if(size==0 && outerNumber>0){return false;}
        if(size==0){return true;}        
        if(currentIndexes.get(size-1)<=0){
          current.remove(size-1);
          currentIndexes.remove(size-1);
          return cutUpTo(outerNumber,current,currentIndexes);
        }
        if(outerNumber==0){return true;}
        current.remove(size-1);
        currentIndexes.remove(size-1);
        return cutUpTo(outerNumber-1,current,currentIndexes);
        }
      private boolean isFullyPositive(List<Integer>indexes){
        for(int i:indexes){if(i<=0)return false;}
        return true;
      }
      private Path getDestPath(int myDept,NestedLocator nl, Path s, int extraCs) {
        assert extraCs>=0:extraCs;
        Path result=nl.getNewPath();
        List<String>cs=s.getCBar();
        if(result==null){
          assert nl.getNewName()!=null;
          List<String>newCs=new ArrayList<>(cs);
          newCs.set(cs.size()-1-extraCs, nl.getNewName());
          return Path.outer(s.outerNumber(),newCs);
          }
        List<String> path =cs.subList(cs.size()-extraCs,cs.size());
        for(String si:path){
          result=result.pushC(si);
        }
        return result.setNewOuter(result.outerNumber()+myDept);
      }
      private static boolean compatible(List<Member> current,  List<Integer> currentIndexes, NestedLocator nl) {
        int size=nl.getMPos().size();
        assert currentIndexes.size()==current.size();
        assert size==nl.getMTail().size();
        if(current.size()<size+1){return false;}//the extra name in nl.that
        for(int i=0;i<size;i++){
          int indexC=currentIndexes.get(i);
          int indexPos=nl.getMPos().get(i);
          if(indexC>0 && indexPos>0 && indexC!=indexPos){return false;}
          Member ci = current.get(i);
          Member nli = nl.getMTail().get(i);
          if(ci==nli){continue;}
          if(ci.getClass()!=nli.getClass()){return false;}
          if(!(ci instanceof ClassB.NestedClass)){return false;}
          String nci=((ClassB.NestedClass)ci).getName();
          String nnli=((ClassB.NestedClass)nli).getName();
          if(!nci.equals(nnli)){return false;}
        }
        return ((ClassB.NestedClass)current.get(size)).getName().equals(nl.getThat());
      }
    }

/*

abstract class MethodPathCloneVisitor extends RenameMembers {
  public MethodPathCloneVisitor(CollectedPrivates maps) {  super(maps);  }
  private HashMap<String, NormType> varEnv=new HashMap<>();
 
  public abstract MethodSelector visitMS(MethodSelector original,Path src);
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    HashMap<String, NormType> aux =this.varEnv;
    this.varEnv=new HashMap<>();
    try{return super.visit(nc);}
    finally{this.varEnv=aux;}
    }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    HashMap<String, NormType> aux =this.varEnv;
    this.varEnv=getVarEnvOf(mi.getS());
    try{return super.visit(mi);}
    finally{this.varEnv=aux;}
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    HashMap<String, NormType> aux =this.varEnv;
    this.varEnv=getVarEnvOf(mt.getMs());
    try{return super.visit(mt);}
    finally{this.varEnv=aux;}
    }
  private HashMap<String, NormType> getVarEnvOf(MethodSelector s) {
    Optional<Member> mOpt = Program.getIfInDom(p.topCt().getMs(),s);
    assert mOpt.isPresent();
    assert mOpt.get() instanceof MethodWithType:
      mOpt.get().getClass();
    MethodWithType m=(MethodWithType)mOpt.get();
    HashMap<String, NormType> result=new HashMap<>();
    {int i=-1;for(String n:s.getNames()){i+=1;
      NormType nt=Norm.of(p,m.getMt().getTs().get(i));
      result.put(n,nt);
    }}
    result.put("this",new NormType(m.getMt().getMdf(),Path.outer(0),Ph.None));
    return result;
  }
  public ExpCore visit(Block s) {
    HashMap<String, NormType> aux = new HashMap<>(this.varEnv);
    try{
      for(Dec d:s.getDecs()){
        this.varEnv.put(d.getX(),
            //Functions.forceNormType(s,d.getT())
            Norm.of(p, d.getT())
            );
        }
      List<Dec> newDecs = liftDecs(s.getDecs());
      Optional<Catch> kOpt = Optional.empty();
      if(s.get_catch().isPresent()){
        Catch k = s.get_catch().get();
        List<On> newOns=new ArrayList<>();
        for(On on:k.getOns()){
          //NormType nti=Functions.forceNormType(s,on.getT());
          NormType nti=Norm.of(p,on.getT());
          this.varEnv.put(k.getX(),nti);
          newOns.add(liftO(on));
          }
        this.varEnv.remove(k.getX());
        kOpt=Optional.of(k.withOns(newOns));
        }
      return new Block(s.getDoc(),newDecs,lift(s.getInner()),kOpt,s.getP());
      }
    finally{this.varEnv=aux;}
    }
  public Ast.Type liftT(Ast.Type t){
      if(!(t instanceof Ast.HistoricType)){return super.liftT(t);}
      Ast.HistoricType ht=(Ast.HistoricType)t;
      Path last=ht.getPath();
      List<MethodSelectorX>sels=new ArrayList<>();
      for(MethodSelectorX sel:ht.getSelectors()){
        MethodSelector ms2=visitMS(sel.getMs(),last);
        if(ms2.equals(sel.getMs())){sels.add(sel);}
        else{sels.add(new MethodSelectorX(ms2,sel.getX()));}
        Ast.HistoricType hti=new Ast.HistoricType(last,Collections.singletonList(sel),false);
        NormType nt=Norm.of(p,hti);
        last=nt.getPath();
        }
      Ast.HistoricType ht2=ht.withSelectors(sels);
      return ht2;
      }


  public ExpCore visit(MCall s) {
    MethodSelector ms=s.getS();
    Path guessed=GuessTypeCore.of(p,varEnv,s.getReceiver());
    if(guessed==null){return super.visit(s);}
    guessed=Norm.of(p, guessed);
    MethodSelector ms2=visitMS(ms,guessed);
    if(ms2.equals(ms)){return super.visit(s);}
    s=new MCall(s.getReceiver(),ms2,s.getDoc(),s.getEs(),s.getP());
    return super.visit(s);
    }
}
*/
package introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import tools.Map;
import ast.Ast;
import ast.Ast.MethodSelectorX;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;

abstract class MethodPathCloneVisitor extends CloneVisitorWithProgram {
  private HashMap<String, NormType> varEnv=new HashMap<>();
  MethodPathCloneVisitor(Program p) {
    super(p);
  }
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
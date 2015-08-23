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
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.Block.Catch;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.From;
import coreVisitors.GuessTypeCore;
import tools.Assertions;
import tools.Map;

abstract public class MethodPathCloneVisitor extends RenameMembers {
  public HashMap<String, Type> varEnv=new HashMap<>();
  public Program _p;
  public MethodPathCloneVisitor(Program p,CollectedPrivates maps) {  super(maps); this._p=p; }
  public abstract MethodSelector visitMS(MethodSelector original,Path src);
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    HashMap<String, Ast.Type> aux =this.varEnv;
    this.varEnv=new HashMap<>();
    try{return super.visit(nc);}
    finally{this.varEnv=aux;}
    }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    HashMap<String, Ast.Type> aux =this.varEnv;
    this.varEnv=getVarEnvOf(mi.getS(),this.getLocator().getLastCb());
    try{return super.visit(mi);}
    finally{this.varEnv=aux;}
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    HashMap<String, Ast.Type> aux =this.varEnv;
    this.varEnv=getVarEnvOf(mt.getMs(),this.getLocator().getLastCb());
    try{return super.visit(mt);}
    finally{this.varEnv=aux;}
    }
  private Ast.MethodType getMt(MethodSelector s,ClassB cb){
    Optional<Member> mOpt = Program.getIfInDom(cb.getMs(),s);
    assert mOpt.isPresent();
    if(mOpt.get() instanceof MethodWithType){return ((MethodWithType)mOpt.get()).getMt();}
    assert mOpt.get() instanceof MethodImplemented;
    return Program.getMT(_p, s, cb);
  }
  private HashMap<String, Ast.Type> getVarEnvOf(MethodSelector s,ClassB cb) {
    Ast.MethodType mt=getMt(s,cb);
    HashMap<String, Ast.Type> result=new HashMap<>();
    {int i=-1;for(String n:s.getNames()){i+=1;
      //NormType nt=Norm.of(p,mt.getTs().get(i));
      result.put(n,mt.getTs().get(i));
    }}
    result.put("this",new NormType(mt.getMdf(),Path.outer(0),Ph.None));
    return result;
  }
  public ExpCore visit(Block s) {
    HashMap<String, Ast.Type> aux = new HashMap<>(this.varEnv);
    try{
      for(Dec d:s.getDecs()){
        this.varEnv.put(d.getX(),d.getT()
            //Functions.forceNormType(s,d.getT())
            //Norm.of(p, d.getT())
            );
        }
      List<Dec> newDecs = liftDecs(s.getDecs());
      Optional<Catch> kOpt = Optional.empty();
      if(s.get_catch().isPresent()){
        Catch k = s.get_catch().get();
        List<On> newOns=new ArrayList<>();
        for(On on:k.getOns()){
          //NormType nti=Functions.forceNormType(s,on.getT());
          //NormType nti=Norm.of(p,on.getT());
          this.varEnv.put(k.getX(),on.getT());
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
        NormType nt=Norm.of(//this norm have to stay
           Program.getExtendedProgram(_p,this.getLocator().getCbs()),hti);
        last=nt.getPath();
        }
      Ast.HistoricType ht2=ht.withSelectors(sels);
      return ht2;
      }


  public ExpCore visit(MCall s) {
    Program ep=Program.getExtendedProgram(_p,this.getLocator().getCbs());
    MethodSelector ms=s.getS();
    Path guessed=GuessTypeCore.of( ep, varEnv,s.getReceiver());
    if(guessed==null){return super.visit(s);}
    guessed=Norm.of(ep, guessed);
    MethodSelector ms2=visitMS(ms,guessed);
    if(ms2.equals(ms)){return super.visit(s);}
    s=new MCall(s.getReceiver(),ms2,s.getDoc(),s.getEs(),s.getP());
    return super.visit(s);
    }
 
}
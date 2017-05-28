package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.Ast;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage.NormImpossible;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import auxiliaryGrammar.Functions;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.Block.On;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import programReduction.Norm;
import programReduction.Program;
import coreVisitors.From;
import facade.Configuration;
import newTypeSystem.TIn;
import tools.Assertions;
import tools.Map;

abstract public class MethodPathCloneVisitor extends RenameMembers {
  public HashMap<String, NormType> varEnv=new HashMap<>();
  public final ClassB visitStart;
  public final Program p;
  public MethodPathCloneVisitor(ClassB visitStart,CollectedLocatorsMap maps,Program p) { 
    super(maps);
    this.visitStart=visitStart;
    this.p=p.evilPush(visitStart);
    }
  ClassB getLastCb(){
    if(this.getLocator().size()==0){return visitStart;}
    return this.getLocator().getLastCb();
  }
  public abstract MethodSelector visitMS(MethodSelector original,Path src);
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    HashMap<String, Ast.NormType> aux =this.varEnv;
    this.varEnv=new HashMap<>();
    try{return super.visit(nc);}
    finally{this.varEnv=aux;}
    }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    HashMap<String, Ast.NormType> aux =this.varEnv;
    Program ep=p;for(ClassB cbi:this.getLocator().getCbs()){ep=ep.evilPush(cbi);}
    this.varEnv=getVarEnvOf(ep,mi.getS(),this.getLastCb());
    try{ return super.visit(mi); }
    finally{this.varEnv=aux;}
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    HashMap<String, Ast.NormType> aux =this.varEnv;
    Program ep=p;for(ClassB cbi:this.getLocator().getCbs()){ep=ep.evilPush(cbi);}
    this.varEnv=getVarEnvOf(ep,mt.getMs(),this.getLastCb());
    try{ return super.visit(mt);}
    finally{this.varEnv=aux;}
    }
  private Ast.MethodType getMt(Program p,MethodSelector s,ClassB cb){
	assert p.top()==cb;
    Optional<Member> mOpt = Functions.getIfInDom(cb.getMs(),s);
    assert mOpt.isPresent();
    if(mOpt.get() instanceof MethodWithType){return ((MethodWithType)mOpt.get()).getMt();}
    assert mOpt.get() instanceof MethodImplemented;
    MethodWithType mwt=(MethodWithType) cb._getMember(s);
    return mwt.getMt();
  }
  private HashMap<String, Ast.NormType> getVarEnvOf(Program p,MethodSelector s,ClassB cb) {
	assert p.top()==cb;
    Ast.MethodType mt=getMt(p,s,cb);
    HashMap<String,Ast.NormType> result=new HashMap<>();
    {int i=-1;for(String n:s.getNames()){i+=1;
      //NormType nt=Norm.of(p,mt.getTs().get(i));
      result.put(n,mt.getTs().get(i).getNT());
    }}
    result.put("this",new NormType(mt.getMdf(),Path.outer(0),Doc.empty()));
    return result;
  }
  public ExpCore visit(Block s) {
    HashMap<String, Ast.NormType> aux = new HashMap<>(this.varEnv);
    try{
      for(Dec d:s.getDecs()){
        //TODO: next check will disappear when we erase skeletal types
        if(!(d.getT().get() instanceof NormType)){
          this.varEnv.put(d.getX(),d.getT().get().getNT());
          continue;
          }
        this.varEnv.put(d.getX(),d.getT().get().getNT());
        }
      List<Dec> newDecs = liftDecs(s.getDecs());
      List<On> newOns=new ArrayList<>();
      for(On on:s.getOns()){
        this.varEnv.put(on.getX(),on.getT().getNT());
        newOns.add(liftO(on));
        this.varEnv.remove(on.getX());
        }
      return new Block(s.getDoc(),newDecs,lift(s.getInner()),newOns,s.getP());
      }
    finally{this.varEnv=aux;}
    }


  public ExpCore visit(MCall s) {
    Program ep=p;for(ClassB cbi:this.getLocator().getCbs()){
      if(cbi!=null){ep=ep.evilPush(cbi);}
      }
    MethodSelector ms=s.getS();
    Path guessed=null;
    try{
      TIn in=TIn.top(Phase.Typed, ep, s.getInner());
      in=in.withG(
        varEnv.entrySet().stream().collect(
          Collectors.toMap(
            me->me.getKey(),
            me->new java.util.AbstractMap.SimpleEntry<>(false,me.getValue())
            )));
      guessed=newTypeSystem.GuessTypeCore._of(in,s.getInner()).getPath();
      assert guessed!=null;
      }
    catch(NormImpossible ignored){return super.visit(s);}
    MethodSelector ms2=visitMS(ms,guessed);
    if(ms2.equals(ms)){return super.visit(s);}
    s=new MCall(s.getInner(),ms2,s.getDoc(),s.getEs(),s.getP());
    return super.visit(s);
    }
    @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){return visitMS(ms,Path.outer(0));}

}
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
import ast.Ast.Type;
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
import newTypeSystem.GuessTypeCore;
import newTypeSystem.StaticDispatch;
import newTypeSystem.TIn;
import tools.Assertions;
import tools.Map;

abstract public class MethodPathCloneVisitor extends RenameMembers {
  public HashMap<String, Type> varEnv=new HashMap<>();
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
    HashMap<String, Ast.Type> aux =this.varEnv;
    this.varEnv=new HashMap<>();
    try{return super.visit(nc);}
    finally{this.varEnv=aux;}
    }
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    HashMap<String, Ast.Type> aux =this.varEnv;
    Program ep=p;for(ClassB cbi:this.getLocator().getCbs()){ep=ep.evilPush(cbi);}
    this.varEnv=getVarEnvOf(ep,mi.getS(),this.getLastCb());
    try{ return super.visit(mi); }
    finally{this.varEnv=aux;}
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    HashMap<String, Ast.Type> aux =this.varEnv;
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
  private HashMap<String, Ast.Type> getVarEnvOf(Program p,MethodSelector s,ClassB cb) {
	assert p.top()==cb;
    Ast.MethodType mt=getMt(p,s,cb);
    HashMap<String,Ast.Type> result=new HashMap<>();
    {int i=-1;for(String n:s.getNames()){i+=1;
      //NormType nt=Norm.of(p,mt.getTs().get(i));
      result.put(n,mt.getTs().get(i));
    }}
    result.put("this",new Type(mt.getMdf(),Path.outer(0),Doc.empty()));
    return result;
  }
  public ExpCore visit(Block s) {
    HashMap<String, Ast.Type> aux = new HashMap<>(this.varEnv);
    try{
      for(Dec d:s.getDecs()){
        if(d.getT().isPresent()){
          this.varEnv.put(d.getX(),d.getT().get());
          }
        //TODO:??? GuessTypeCore._of(this.varEnv,d.getInner());
        }
      List<Dec> newDecs = liftDecs(s.getDecs());
      List<On> newOns=new ArrayList<>();
      for(On on:s.getOns()){
        this.varEnv.put(on.getX(),on.getT());
        newOns.add(liftO(on));
        this.varEnv.remove(on.getX());
        }
      return new Block(s.getDoc(),newDecs,lift(s.getInner()),newOns,
        s.getP(),liftTNull(s.getTypeOut()));
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
      TIn in=TIn.top(Phase.Typed, ep, s.getInner(),true,Type.immLibrary);
      in=in.withG(
        varEnv.entrySet().stream().collect(
          Collectors.toMap(
            me->me.getKey(),
            me->new java.util.AbstractMap.SimpleEntry<>(false,me.getValue())
            )));
      ExpCore _e=StaticDispatch.of(in.p,in,s.getInner(),false);
      if(_e==null){return super.visit(s);}
      Type tGuessed=newTypeSystem.GuessTypeCore._of(in.p,in,_e,false);
      if(tGuessed==null){return super.visit(s);}
      guessed=tGuessed.getPath();
      assert guessed!=null;
      }
    catch(NormImpossible ignored){return super.visit(s);}
    MethodSelector ms2=visitMS(ms,guessed);
    if(ms2.equals(ms)){return super.visit(s);}
    s=new MCall(s.getInner(),ms2,s.getDoc(),s.getEs(),s.getP(),
      liftTNull(s.getTypeRec()),liftTNull(s.getTypeOut()));
    return super.visit(s);
    }
    @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){return visitMS(ms,Path.outer(0));}

}
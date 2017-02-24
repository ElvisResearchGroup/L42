package coreVisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ast.Ast;
import ast.ExpCore;
import ast.Ast.*;
import ast.ErrorMessage;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.Loop;
import ast.Redex;
import ast.Redex.Garbage;
import ast.ExpCore.*;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;
//no, non e' un visitor, e deve ritornare un Optional Redex!
public class IsRedex{
  //Program p;
  //IsRedex(Program p){this.p=p;}
  public static Redex of(Program p,ExpCore e){
    if( e instanceof Using){return visit(p,(Using)e);}
    if( e instanceof MCall){return visit(p,(MCall)e);}
    if( e instanceof Block){return visit(p,(Block)e);}
    if( e instanceof ClassB){return visit(p,(ClassB)e);}
    if( e instanceof Loop){return visit(p,(Loop)e);}
    return Redex.invalid();
  }
  
  private static Redex visit(Program p, Loop e) {
    return new Redex.LoopR(e);
  }

  public static Redex visit(Program p,Using s)  {
    for(ExpCore e:s.getEs()){
      if(!IsValue.of(p, e)){return Redex.invalid();}
      }
    //after the first approximation//TODO: really slow
    List<ExpCore> newEs = esNestedGarbage(s.getEs());
    if(newEs!=null){return new Garbage(s.withEs(newEs));}
    try{
      ExpCore newE=platformSpecific.fakeInternet.OnLineCode.pluginAction(p, s);
      return new Redex.Using(s,newE);
      }
    catch(ErrorMessage.PluginActionUndefined pau){
      ExpCore inner=s.getInner();
      //if(IsValue.of(p,inner))//TODO: boh, should just return the expression, since is small step?
      {return new Redex.UsingOut(s);}
      //ExpCore throw_ = ExtractThrow.of(p,inner);
      //if(throw_ instanceof ExpCore.Signal){return new Redex.UsingOut(s);}
      //return Redex.invalid();
    }
  }
  public static Redex visit(Program p,ClassB s)  {
    if(IsCompiled.of(s)){return Redex.invalid();}
    return new Redex.Meta(s);
    }
    
  public static Redex visit(Program p,MCall s)  {
    if(!IsValue.of(p,s.getInner())){return Redex.invalid();}
    for(ExpCore ei:s.getEs()){
      if(!IsValue.of(p,ei)){return Redex.invalid();}
      }
    //after the first approximation//TODO: it is really slow, can we use this idea to redo the whole ctxExtract?, note, it is only needed for garbage
    Garbage g=IsValue.nestedGarbage(s.getInner());
    if (g!=null){return new Garbage(s.withInner(g.getThatLessGarbage()));}
    List<ExpCore> newEs = esNestedGarbage(s.getEs());
    if(newEs!=null){return new Garbage(s.withEs(newEs));}
    return new Redex.MethCall(s);
    /*MethodWithType mwt=p.method(path, ms);
    if(!IsValue.isAtom(s.getReceiver())){
      return Optional.of(new Redex.PrimCallRec(s));
      }
    for(int i=0;i<s.getEs().size();i++){
      if(!IsValue.isAtom(s.getEs().get(i))){
        return Optional.of(new Redex.PrimCallArg(s,i));
        }}
    return Optional.of(new Redex.);*/
    }

  public static List<ExpCore> esNestedGarbage(List<ExpCore> es) {
    {int i=-1;for(ExpCore ei:es){i+=1;
      Garbage gi=IsValue.nestedGarbage(ei);
      if(gi==null){continue;}
      List<ExpCore> newEs = new ArrayList<>(es);
      newEs.set(i,gi.getThatLessGarbage());
      return newEs;
      }}
    return null;
  }
  public static Redex visit(Program p,Block s)  {
    for(int i=0;i<s.getDecs().size();i++){
      ExpCore ei=s.getDecs().get(i).getInner();
      NormType ti=s.getDecs().get(i).getT().getNT();
      //Here I was reasoning on missing nested garbage
      //Redex ri=redexDec(p, s, i, ei, ti);
      //if(!(ri instanceof Redex.NoRedex)){return ri;}
      if(new IsValue(p).validDv(s.getDecs().get(i))){continue;}
      //otherwise, i is the first non dv
      //return ri;
      return redexDec(p, s, i, ei, ti);
    }//end of for members
    Block s2=Functions.garbage(s, s.getDecs().size());
    if(!s2.equals(s)){return new Redex.Garbage(s2);}
    if(!s.getOns().isEmpty()){
      return new Redex.NoThrowRemoveOn(s);
    }
      
    return Redex.invalid();
  }

  private static Redex redexDec(Program p, Block s, int i, ExpCore ei, NormType ti) {
    Block s2=Functions.garbage(s, i);
    if(!s2.equals(s)){return new Redex.Garbage(s2);}
    if(new IsValue(p).validDecForPh(s.getDecs().get(i))){
      if(!Functions.isComplete(ti) || Functions.isInterface(p,ti.getPath())){return new Redex.Ph(s,i);}
      if(ti.getMdf()==Mdf.Capsule){return new Redex.Subst(s,i);}
    }
    if(!IsValue.of(p,ei)){
      //no or throw
      if(s.getOns().isEmpty()){return Redex.invalid();}
      ExpCore throw_ = ExtractThrow.of(p,ei);
      if(throw_ instanceof WalkBy){return Redex.invalid();}
      return new Redex.CaptureOrNot(s, i,(Signal)throw_);

    }
    if(IsValue.isAtom(ei)){return new Redex.Subst(s, i);}
    if(ei instanceof Block &&(
       ti.getMdf()==Mdf.Mutable
     ||ti.getMdf()==Mdf.Lent
     ||ti.getMdf()==Mdf.Readable)){
      return new Redex.BlockElim(s, i);
    }
    return Redex.invalid();
  }
}
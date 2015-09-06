package typeSystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.SignalKind;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.On;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;

public class ThrowEnv {
//fgfdhgfh  //TODO:put res private and wrap accesses to check consistency
  private HashSet<Ast.NormType> res=new HashSet<Ast.NormType>();
  public Set<Ast.NormType> res(){return Collections.unmodifiableSet(res);}
  public void resAddAll(Set<Ast.NormType> s){
    res.addAll(s);
    assert isConsistent();
    }
  public void resClear(){res.clear();}
  public HashSet<Ast.Path> exceptions=new HashSet<Ast.Path>();
  public Ast.Mdf mdfOfRes(){
    assert isConsistent();
    if(res.isEmpty()){return Mdf.Immutable;}//TODO: not as bad as it look, the error is found later
    return res.iterator().next().getMdf();
  }
  private boolean inIsConsistent=false;
  public boolean isConsistent(){
    if(inIsConsistent){return true;}
    inIsConsistent=true;try{
    if(res.isEmpty()){return true;}
    Ast.Mdf m=mdfOfRes();
    for(Ast.NormType t:res){
      if(m!=t.getMdf()){
        assert false:res;
        return false;
        }
    }
    return true;
    }finally{inIsConsistent=false;}
  }
  public String toString(){return this.exceptions.toString()+";"+this.res.toString();}
  /*public ThrowEnv accumulate(ExpCore.Block.Catch k){
    ThrowEnv result=this;
    for(On on:k.getOns()){
      result=result.accumulate(k.getKind(),Functions.forceNormType(on.getInner(),on.getT()));
    }
    return result;
  }  
  public ThrowEnv accumulate(SignalKind kind, NormType type){
    ThrowEnv result=new ThrowEnv();
    result.exceptions.addAll(this.exceptions);
    if(kind==SignalKind.Exception){ result.exceptions.add(type.getPath()); }
    else{
     result.res.addAll(ThrowEnv.accResult(this.res,Arrays.asList(on))); 
      }
    trAlt.resAddAll(throwEnv2.res());
    return this;
  }
  public ThrowEnv accumulate(Optional<ExpCore.Block.Catch> k){
    if(k.isPresent()){return accumulate(k.get());}
    return this;
    }*/
  static Set<NormType> accResult(Program p,Set<NormType> res,List<On> ons) {
    assert TypecheckBlock.sameMdf(res);
    Mdf current=null;
    if(res.size()!=0){current=res.iterator().next().getMdf();}
    HashSet<NormType> results=new HashSet<NormType>();
    for(Block.On on:ons){
      NormType nt = Functions.forceNormType(p,on.getInner(),on.getT());
      if(results.isEmpty()){
        results.add(nt);
        current=nt.getMdf();
        continue;
        }
      if(current==nt.getMdf()){
        results.add(nt);
        continue;
        }
      if(Functions.isSubtype(current, nt.getMdf())){
        current=nt.getMdf();
        HashSet<NormType> results2=new HashSet<NormType>();
        for(NormType nti:results){
          results2.add(nti.withMdf(current));
          }
        results=results2;
        continue;
        }
      //not subtype
      results.clear();
      current=nt.getMdf();
      results.add(nt);
      }
    return results;
  }
}

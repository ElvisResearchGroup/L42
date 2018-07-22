package coreVisitors;

import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import tools.Assertions;

public class TestShapePairVisitor implements Visitor<Boolean>{
  ExpCore other;
  public TestShapePairVisitor(ExpCore other){this.other=other;}
  public Boolean lift(ExpCore e,ExpCore other){
    ExpCore oldOther=this.other;
    this.other=other;
    try{return e.accept(this);}
    finally{this.other=oldOther;}
    }
  public Boolean visit(ExpCore.EPath s) {return other instanceof ExpCore.EPath;}
  public Boolean visit(X s) {return other instanceof X;}
  public Boolean visit(_void s)  {return other instanceof _void;}
  public Boolean visit(ClassB s)  {return other instanceof ClassB;}
  public Boolean visit(WalkBy s)  {throw Assertions.codeNotReachable();}
  public Boolean visit(Signal s)  {
    if(!(other instanceof Signal)){return false;}
    return lift(s.getInner(),((Signal)other).getInner());
    }
  public Boolean visit(Loop s)  {
    if(!(other instanceof Loop)){return false;}
    return lift(s.getInner(),((Loop)other).getInner());
    }
  public Boolean visit(UpdateVar s) {
    if(!(other instanceof UpdateVar)){return false;}
    return lift(s.getInner(),((UpdateVar)other).getInner());
    }

  public Boolean visit(MCall s)  {
    if(!(this.other instanceof MCall)){return false;}
    MCall other=(MCall)this.other;
    boolean innerRes= lift(s.getInner(),other.getInner());
    if(!innerRes){return false;}
    if(s.getEs().size()!=other.getEs().size()){return false;}
    for(int i=0;i<s.getEs().size();i++){
      if(!lift(s.getEs().get(i),other.getEs().get(i))){return false;}
      }
    return true;
    }
  public Boolean visit(ExpCore.OperationDispatch s)  {
    if(!(this.other instanceof ExpCore.OperationDispatch)){return false;}
    ExpCore.OperationDispatch other=(ExpCore.OperationDispatch)this.other;
    if(s.getEs().size()!=other.getEs().size()){return false;}
    for(int i=0;i<s.getEs().size();i++){
      if(!lift(s.getEs().get(i),other.getEs().get(i))){return false;}
      }
    return true;
    }  
  public Boolean visit(Using s)  {
    if(!(this.other instanceof Using)){return false;}
    Using other=(Using)this.other;
    boolean innerRes= lift(s.getInner(),other.getInner());
    if(!innerRes){return false;}
    if(s.getEs().size()!=other.getEs().size()){return false;}
    for(int i=0;i<s.getEs().size();i++){
      if(!lift(s.getEs().get(i),other.getEs().get(i))){return false;}
      }
    return true;
    }
  public Boolean visit(Block s)  {
    if(!(this.other instanceof Block)){return false;}
    Block other=(Block)this.other;
    boolean innerRes= lift(s.getInner(),other.getInner());
    if(!innerRes){return false;}
    if(s.getDecs().size()!=other.getDecs().size()){return false;}
    for(int i=0;i<s.getDecs().size();i++){
      if(!lift(s.getDecs().get(i).getInner(),other.getDecs().get(i).getInner())){return false;}
      }
    if(s.getOns().size()!=other.getOns().size()){return false;}
    for(int i=0;i<s.getOns().size();i++){
      if(!lift(s.getOns().get(i).getInner(),other.getOns().get(i).getInner())){return false;}
      }
    return true;
    }
  }
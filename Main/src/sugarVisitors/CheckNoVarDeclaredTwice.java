package sugarVisitors;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ast.ErrorMessage;
import ast.Expression;
import ast.Expression.ClassB;
import ast.Expression.ClassB.Member;

//TODO: also check no varDeclaredTwice over desugared terms!

public class CheckNoVarDeclaredTwice{
  public static boolean of(Expression e){
    of(e,Collections.emptyList());
    return true;
  }
  public static void of(Expression e,List<String>names){
    Local cdv=new Local();
    cdv.xs.addAll(names);
    cdv.xs.add("this");
    e.accept(cdv);
  }
  private static class Local extends CloneVisitor {
    HashSet<String> xs=new HashSet<String>();
    
    @Override
    protected <T extends Expression>T lift(T e){
      try{  return super.lift(e); }
      catch(ErrorMessage.VariableDeclaredMultipleTimes err){
        if(e instanceof ast.Ast.HasPos){
          throw err.withPos(((ast.Ast.HasPos)e).getP());
        } else throw err;
      }
      
    }
    //Note: this also work for with is and es!   
    @Override
    protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
      if(xs.contains(d.getX())){
        throw new ErrorMessage.VariableDeclaredMultipleTimes(d.getX(),null);
        }
      assert d.getX().length()>0;
      xs.add(d.getX());
      try{return super.liftVarDecXE(d);}
      finally{xs.remove(d.getX());}
    }
    @Override 
    protected ast.Ast.Catch liftK(ast.Ast.Catch k){
      if(xs.contains(k.getX())){
        throw new ErrorMessage.VariableDeclaredMultipleTimes(k.getX(),null);
        }
      if(k.getX().length()>0){xs.add(k.getX());}
      try{return super.liftK(k);}
      finally{xs.remove(k.getX());}
      } 
    
    public Expression visit(ClassB s) {
      for(Member m:s.getMs()){
        m.match(
          nc->{CheckNoVarDeclaredTwice.of(nc.getInner(),Collections.emptyList());return null;},
          mi->{CheckNoVarDeclaredTwice.of(mi.getInner(),mi.getS().getNames());return null;},
          mt->{if(mt.getInner().isPresent()){CheckNoVarDeclaredTwice.of(mt.getInner().get(),mt.getMs().getNames());};return null;}
          );
      }
      return s;
    }  
  }
}

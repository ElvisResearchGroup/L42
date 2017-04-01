package sugarVisitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ast.Ast;
import ast.Expression;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.Expression.ClassB;
import ast.Expression.ClassReuse;
import ast.Expression.ClassB.Member;
import ast.Expression.ClassB.NestedClass;

public class DesugarPaths extends CloneVisitor{
  public static Expression of(Expression e){
    DesugarPaths d=new DesugarPaths();
    Expression result= e.accept(d);
    return result;
  }
  ArrayList<ClassB> p=new ArrayList<ClassB>();
  public Expression visit(ClassReuse s) {
    ClassB res=s.getInner();//lift(s.getInner());
    ArrayList<ClassB> oldP = this.p;
    this.p=new ArrayList<ClassB>();
    try{res=(ClassB) res.accept(this);}
    finally{this.p=oldP;}
    return s.withInner(res);
    }
  public Expression visit(ClassB s) {
    try{
        p.add(0,s);
        s=(ClassB)super.visit(s);
        return s;
        }
    finally{p.remove(0); }
    }
  public Expression visit(Expression.EPath s) {
    if(s.isCore()|| s.isPrimitive()){return s;}
    List<Ast.C> rd =s.getInner().sugarNames();
    assert !rd.isEmpty();
    Ast.C key=rd.get(0);
    int index = searchForScope(key);
    if(index==-1){index=0;}//TODO: is what we want? not to the nearest reuse?
    return s.withInner(Path.outer(index,rd));
    }

  private int searchForScope(Ast.C key) {
    assert !key.toString().equals("This");
    int index=-1;
    for(ClassB cb:p){
      index+=1;
      //if (cb.getH())
      for(Member m:cb.getMs()){
        if(!(m instanceof NestedClass)){continue;}
        NestedClass nc=(NestedClass)m;
        if(!nc.getName().equals(key)){continue;}
        return index;
      }
    }
    return index;
  }
}

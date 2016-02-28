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
  public Expression visit(Path s) {
    if(s.isCore()|| s.isPrimitive()){return s;}
    List<String> rd = new ArrayList<String>(s.getRowData());
    String key=rd.get(0);
    if(key.equals("This")){
      rd.set(0, "This0");
      return new Path(rd);
      }
    int index = searchForScope(key);
    if (index==-1){index=0;}//to simplify testing
    rd.add(0,"This"+index);
    return new Path(rd);
    }

  private int searchForScope(String key) {
    assert !key.equals("This");
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

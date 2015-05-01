package sugarVisitors;

import java.util.HashSet;
import tools.Map;
import ast.Ast;
import ast.Ast.ConcreteHeader;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.MethodSelector;
import ast.Expression.ClassB.*;
import ast.Expression;

public class CollectDeclaredClassNamesAndMethodNames extends CloneVisitor{
  HashSet<String> xs=new HashSet<String>();
  public static HashSet<String> of(Expression e){
    CollectDeclaredClassNamesAndMethodNames cdv=new CollectDeclaredClassNamesAndMethodNames();
    e.accept(cdv);
    return cdv.xs;
  }
  public NestedClass visit(NestedClass nc){
    xs.add(nc.getName());
    return super.visit(nc);
    }
  protected MethodSelector liftMs(MethodSelector ms) {
    xs.add(ms.getName());
    for(String n:ms.getNames()){xs.add(n);}
    return super.liftMs(ms);
  }
  protected FieldDec liftF(FieldDec f) {
    xs.add(f.getName());
    //NO, just the first component? xs.add(f.getName()+"(that)");
    xs.add("#"+f.getName());
    return super.liftF(f);
  }
  protected Header liftH(Header h) {
    if(h instanceof Ast.ConcreteHeader){  xs.add( ((Ast.ConcreteHeader)h).getName());}
    return super.liftH(h);
    }
}

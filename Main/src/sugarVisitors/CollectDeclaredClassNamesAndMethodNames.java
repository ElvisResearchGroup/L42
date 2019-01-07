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
  private void xsAdd(String x){
    assert x!=null;
    assert x!="";
    xs.add(x);
    }
  public static HashSet<String> of(Expression e){
    CollectDeclaredClassNamesAndMethodNames cdv=new CollectDeclaredClassNamesAndMethodNames();
    e.accept(cdv);
    assert !cdv.xs.contains("");
    assert !cdv.xs.contains(null);
    return cdv.xs;
  }
  public NestedClass visit(NestedClass nc){
    xsAdd(nc.getName().toString());
    return super.visit(nc);
    }
  protected MethodSelector liftMs(MethodSelector ms) {
    xsAdd(Desugar.desugarName(ms));
    for(String n:ms.getNames()){xsAdd(n);}
    return super.liftMs(ms);
  }
  protected FieldDec liftF(FieldDec f) {
    xsAdd(f.getName());
    //NO, just the first component? xs.add(f.getName()+"(that)");
    xsAdd("#"+f.getName());
    return super.liftF(f);
  }
  protected Header liftH(Header h) {
    if(h instanceof Ast.ConcreteHeader){  xsAdd( ((Ast.ConcreteHeader)h).getName());}
    return super.liftH(h);
    }
}

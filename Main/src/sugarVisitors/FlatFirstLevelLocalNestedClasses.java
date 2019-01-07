package sugarVisitors;

import java.util.ArrayList;
import java.util.List;

import tools.Map;
import ast.Expression;
import ast.Ast.Doc;
import ast.Ast.VarDec;
import ast.Expression.ClassB.*;
import ast.Expression.ClassB;

public class FlatFirstLevelLocalNestedClasses extends CloneVisitor{
  List<NestedClass> collected=new ArrayList<>();
  public static ClassB of(ClassB s,CloneVisitor step) {
    FlatFirstLevelLocalNestedClasses v=new FlatFirstLevelLocalNestedClasses();
    ClassB result=v.start(s);//to manage super
    List<Member> ms2=new ArrayList<>();
    for(NestedClass nc:v.collected){
      ms2.add(nc.withInner(nc.getInner().accept(step))/*.withDoc(Doc.factory("@private").sum(nc.getDoc()))*/);
    }
    ms2.addAll(result.getMs());
    return result.withMs(ms2);
  }
  private ClassB start(ClassB s) {
    return (ClassB)super.visit(s);
  }
  public Expression visit(ClassB s) {
    return s;
  }
  protected List<ast.Ast.VarDec> liftVarDecs(List<ast.Ast.VarDec> ds) {
    List<ast.Ast.VarDec> result=new ArrayList<>();
    for( VarDec d:ds){
      if(!(d instanceof ast.Ast.VarDecCE)){
        result.add(d);
        continue;
      }
      NestedClass nc=((ast.Ast.VarDecCE)d).getInner();
      collected.add(nc);
    }
    return super.liftVarDecs(result);
  }

}

package privateMangling;

import ast.ExpCore;

import java.util.List;

import ast.Ast;
import ast.Ast.C;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import coreVisitors.CloneVisitor;
import tools.Map;

public class PrivateHelper {
  public static int countFamilies=0;
  static String updateSingleName(String x){
    int isPr=x.lastIndexOf("__");
    if(isPr==-1){return x;}
    int pos=x.lastIndexOf("_");
    assert pos!=-1;
    assert isPr+2<pos && isPr!=-1;
    x=x.substring(0,pos+1)+countFamilies;
    return x;
  }
  public static ClassB updatePrivateFamilies(ClassB cb){
    countFamilies+=1;
    return (ClassB)cb.accept(new CloneVisitor(){
      @Override protected MethodSelector liftMs(MethodSelector ms){
        return ms.withName(updateSingleName(ms.getName()))
            .withNames(Map.of(PrivateHelper::updateSingleName, ms.getNames()));
        }
      @Override protected MethodSelector liftMsInMetDec(MethodSelector ms){
        return ms.withName(updateSingleName(ms.getName()))
            .withNames(Map.of(PrivateHelper::updateSingleName, ms.getNames()));
        }
      public ClassB.NestedClass visit(ClassB.NestedClass nc){
        return super.visit(nc.withName(C.of(updateSingleName(nc.getName().toString()))));
        }
      public ExpCore visit(Path s) {
        if(s.isPrimitive()){return super.visit(s);}
        List<Ast.C> cs = s.getCBar();
        cs=Map.of(ci->C.of(PrivateHelper.updateSingleName(ci.toString())),cs);
        return super.visit(Path.outer(s.outerNumber(),cs));
        }
    });
  }
}

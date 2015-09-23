package is.L42.connected.withSafeOperators;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import coreVisitors.CloneWithPath;
import coreVisitors.CollectPrivateNames;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Program;

public class ClassOperations {

  public static ClassB onNestedNavigateToPathAndDo(ClassB cb,List<String>cs,Function<NestedClass,Optional<NestedClass>>op){
    assert !cs.isEmpty();
    assert cb!=null;
    List<Member> newMs=new ArrayList<>(cb.getMs());
    if(cs.size()>1){
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, cs.get(0)).get();
      nc=nc.withInner(onNestedNavigateToPathAndDo((ClassB)nc.getInner(),cs.subList(1,cs.size()),op));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
    assert cs.size()==1;
    String nName=cs.get(0);
    NestedClass nc=(NestedClass)Program.getIfInDom(newMs, nName).get();
    Optional<NestedClass> optNc = op.apply(nc);
    if(optNc.isPresent()){
      Program.replaceIfInDom(newMs, optNc.get());
    }
    else{newMs.remove(nc);}
    return cb.withMs(newMs);
  }

  public static ClassB onClassNavigateToPathAndDo(ClassB cb,List<String>cs,Function<ClassB,ClassB>op){
    if(cs.isEmpty()){return op.apply(cb);}
    List<Member> newMs=new ArrayList<>(cb.getMs());
    if(cs.size()>1){
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, cs.get(0)).get();
      nc=nc.withInner(onClassNavigateToPathAndDo((ClassB)nc.getInner(),cs.subList(1,cs.size()),op));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
    assert cs.size()==1;
    String nName=cs.get(0);
    NestedClass nc=(NestedClass)Program.getIfInDom(newMs, nName).get();
    ClassB newCb = op.apply((ClassB)nc.getInner());
    Program.replaceIfInDom(newMs, nc.withInner(newCb));
    return cb.withMs(newMs);
  }

  static ClassB normalizePaths(ClassB cb){
    return (ClassB)cb.accept(new CloneWithPath(){
      public ast.ExpCore visit(ast.Ast.Path s) {
        if(s.isPrimitive()){return s;}
        assert s.isCore();
        List<String> path = this.getLocator().getClassNamesPath();
        if(s.outerNumber()>path.size()){return s;}
        return normalizePath(path,s.outerNumber(),s.getCBar());
      }});}
  static List<String>toTop(List<String>path,Path s){
    assert !s.isPrimitive();
    assert s.outerNumber()<=path.size(): s+" "+path;
    List<String>result=new ArrayList<>();
    result.addAll(path.subList(0,path.size()-s.outerNumber()));
    result.addAll(s.getCBar());
    return result;
  }
  static Path normalizePath(List<String>whereWeAre,int outerN,List<String>cs){
    assert cs!=null;
    List<String> whereWeAreLoc=whereWeAre.subList(whereWeAre.size()-outerN, whereWeAre.size());
    int i=0;
    while(true){
      if(i>=cs.size()){break;}
      if(i>=whereWeAreLoc.size()){break;}
      assert cs.get(i)!=null:
        cs;
      if(!cs.get(i).equals(whereWeAreLoc.get(i))){break;}
      i+=1;
    }
    return Path.outer(outerN-i,cs.subList(i,cs.size()));
    }
}

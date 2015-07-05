package is.L42.connected.withSafeOperators;

import introspection.ConsistentRenaming;
import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coreVisitors.CollectPrivateNames;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.*;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Program;

public class Sum {
  static ClassB sum(ClassB a,ClassB b){
    List<ClassB.Member> ms=new ArrayList<>();
    ms.add(new ClassB.NestedClass(Doc.empty(),"A",a));
    ms.add(new ClassB.NestedClass(Doc.empty(),"B",b));
    ClassB ab=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    ab=normalize(ab);
    a=(ClassB)((ClassB.NestedClass)ab.getMs().get(0)).getInner();
    b=(ClassB)((ClassB.NestedClass)ab.getMs().get(1)).getInner();
    return IntrospectionSum.sum(a, b, Path.outer(0));
  }
  static ClassB normalize(ClassB cb){
    //collect private names
    CollectPrivateNames cpn=CollectPrivateNames.of(cb);
    //rename all
    Program emptyP=Program.empty();
    List<PathMxMx> mapMx = ConsistentRenaming.makeMapMxConsistent(cb,cpn.mapMx);
    cb=IntrospectionAdapt.applyMapMx(emptyP,cb,mapMx);
    cb=IntrospectionAdapt.applyMapPath(emptyP,cb,cpn.mapPath);
    return cb;
  }
}

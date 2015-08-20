package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import coreVisitors.FromInClass;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;

class Push {
  //never wrong
  //assumes s well formed class name
  static ClassB pushOne(ClassB in,String s){
    Path p=Path.outer(1);
    ClassB cb=(ClassB)FromInClass.of(in, p);
    List<Member> ms=Collections.singletonList(new ClassB.NestedClass(Doc.empty(),s,cb,null));
    return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms);
  }
  //could be more efficient if directly implemented
  static ClassB pushMany(ClassB in,List<String>cs){
    assert !cs.isEmpty();
    cs=new ArrayList<>(cs);
    Collections.reverse(cs);
    for(String s:cs){
      in=pushOne(in,s);
    }
    return in;
  }
 }

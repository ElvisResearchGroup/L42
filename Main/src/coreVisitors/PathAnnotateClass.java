package coreVisitors;

import ast.Ast.Doc;
import ast.ExpCore;
import ast.ExpCore.ClassB;

public class PathAnnotateClass extends CloneWithPath{
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    String comment=computeComment();
    //comment+=nc.getName();
    Doc doc=Doc.factory(comment).sum(nc.getDoc());
    return super.visit(nc.withDoc(doc));
  }
  private String computeComment() {
    int len=this.path.size();
    assert len==this.pathNums.size();
    StringBuffer comment=new StringBuffer();
    comment.append("Outer0::");
    for(int i=0;i<len;i++){
      this.path.get(i).match(
          nc->comment.append(nc.getName()),
          mi->comment.append(mi.getS().toString()), 
          mt->comment.append(mt.getMs().toString())
          );
      comment.append("["+this.pathNums.get(i)+"]");
      }
    return comment.toString();
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    String comment=computeComment();
    //comment+=mt.getMs();
    Doc doc=Doc.factory(comment).sum(mt.getDoc());
    return super.visit(mt.withDoc(doc));
  }  
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    String comment=computeComment();
    //comment+=mi.getS();
    Doc doc=Doc.factory(comment).sum(mi.getDoc());
    return super.visit(mi.withDoc(doc));
  }
  public ExpCore visit(ClassB cb){
    String comment=computeComment();
    Doc doc=Doc.factory(comment).sum(cb.getDoc1());
    return super.visit(cb.withDoc1(doc));
  }
    
}

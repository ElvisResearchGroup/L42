package coreVisitors;

import ast.Ast.Doc;

import java.util.List;

import ast.ExpCore;
import ast.ExpCore.ClassB;

public class PathAnnotateClass extends CloneWithPath{
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    String comment=computeComment(this.getAstNodesPath(),this.getAstIndexesPath());
    //comment+=nc.getName();
    Doc doc=Doc.factory(comment).sum(nc.getDoc());
    return super.visit(nc.withDoc(doc));
  }
  public static String computeComment(List<ClassB.Member> astNodesPath, List<Integer> astIndexesPath) {
    int len=astNodesPath.size();
    assert len==astIndexesPath.size();
    StringBuffer comment=new StringBuffer();
    comment.append("Outer0::");
    for(int i=0;i<len;i++){
      astNodesPath.get(i).match(
          nc->comment.append(nc.getName()),
          mi->comment.append(mi.getS().toString()), 
          mt->comment.append(mt.getMs().toString())
          );
      comment.append("["+astIndexesPath.get(i)+"]");
      }
    return comment.toString();
  }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    String comment=computeComment(this.getAstNodesPath(),this.getAstIndexesPath());
    //comment+=mt.getMs();
    Doc doc=Doc.factory(comment).sum(mt.getDoc());
    return super.visit(mt.withDoc(doc));
  }  
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    String comment=computeComment(this.getAstNodesPath(),this.getAstIndexesPath());
    //comment+=mi.getS();
    Doc doc=Doc.factory(comment).sum(mi.getDoc());
    return super.visit(mi.withDoc(doc));
  }
  public ExpCore visit(ClassB cb){
    String comment=computeComment(this.getAstNodesPath(),this.getAstIndexesPath());
    Doc doc=Doc.factory(comment).sum(cb.getDoc1());
    return super.visit(cb.withDoc1(doc));
  }
    
}

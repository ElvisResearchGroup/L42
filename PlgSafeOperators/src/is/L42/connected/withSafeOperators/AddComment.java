package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Program;
public class AddComment {
  public static ClassB addCommentMethod (ClassB cb, List<String> cs,MethodSelector sel,Doc doc){
    ExtractInfo.checkExistsPathMethod(cb, cs, Optional.of(sel));
    return auxAddCommentMethod(cb,cs,sel,doc);
  }

  private static ClassB auxAddCommentMethod(ClassB cb, List<String> cs, MethodSelector sel,Doc doc) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    if(!cs.isEmpty()){
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, cs.get(0)).get();
      nc=nc.withInner(auxAddCommentMethod((ClassB)nc.getInner(),cs.subList(1,cs.size()),sel,doc));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
    Member m=Program.getIfInDom(newMs, sel).get();
    //add comment
    m.match(nc->{throw Assertions.codeNotReachable();},
      mi->{
        mi=mi.withDoc(mi.getDoc().sum(doc));
        Program.replaceIfInDom(newMs,mi);
        return null;
        },
      mt->{
        mt=mt.withDoc(mt.getDoc().sum(doc));
        Program.replaceIfInDom(newMs,mt);
        return null;
      });
    //create new class
    return cb.withMs(newMs);
  }

  public static ClassB addComment (ClassB cb, List<String> cs,Doc doc){
    if(cs.isEmpty()){throw ExtractInfo.errorInvalidOnTopLevel();}
    ExtractInfo.checkExistsPathMethod(cb, cs, Optional.empty());
    return auxAddComment(cb,cs,doc);
  }

  private static ClassB auxAddComment(ClassB cb, List<String> cs,Doc doc) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    if(cs.size()>1){
      NestedClass nc=(NestedClass)Program.getIfInDom(newMs, cs.get(0)).get();
      nc=nc.withInner(auxAddComment((ClassB)nc.getInner(),cs.subList(1,cs.size()),doc));
      Program.replaceIfInDom(newMs, nc);
      return cb.withMs(newMs);
    }
    assert cs.size()==1;
    String cName=cs.get(0);
    NestedClass nc=(NestedClass)Program.getIfInDom(newMs, cName).get();
    nc=nc.withDoc(nc.getDoc().sum(doc));
    Program.replaceIfInDom(newMs,nc);
    return cb.withMs(newMs);
  }

}

package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Program;
public class AddComment {

  public static ClassB addCommentMethod (ClassB cb, List<String> cs,MethodSelector sel,Doc doc){
    Errors42.checkExistsPathMethod(cb, cs, Optional.of(sel));
    if(cs.isEmpty()){return auxAddComment(cb,sel,doc);}
    return ClassOperations.onClassNavigateToPathAndDo(cb,cs,cbi->auxAddComment(cbi,sel,doc));
  }

  private static ClassB auxAddComment(ClassB cb, MethodSelector sel, Doc doc) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
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
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(cb, cs, Optional.empty());
    return ClassOperations.onNestedNavigateToPathAndDo(cb, cs, nc->Optional.of(nc.withDoc(nc.getDoc().sum(doc))));
  }

}

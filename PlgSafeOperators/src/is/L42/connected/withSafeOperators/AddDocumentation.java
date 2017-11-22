package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import tools.Assertions;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import platformSpecific.javaTranslation.Resources;
public class AddDocumentation {

  public static ClassB addDocumentationOnMethod (Program p,ClassB cb, List<Ast.C> cs,MethodSelector sel,Doc doc){
    Errors42.checkExistsPathMethod(cb, cs, Optional.of(sel));
    if(cs.isEmpty()){cb=auxAddDocOnMethod(p,cb,sel,doc);}
    else{
      Program p1=p.evilPush(cb);
      if(cs.size()>1){p1=p1.navigate(cs.subList(0, cs.size()-1));}
      Program p2=p1;
       cb= cb.onClassNavigateToPathAndDo(cs,cbi->auxAddDocOnMethod(p2,cbi,sel,doc));
      }
    return cb;
  }

  private static ClassB auxAddDocOnMethod(Program p,ClassB cb, MethodSelector sel, Doc doc) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    Member m=Functions.getIfInDom(newMs, sel).get();
    //add comment
    m.match(
      nc->{throw Assertions.codeNotReachable();},
      mi->{throw Assertions.codeNotReachable();},
      mt-> {
        mt=mt.withDoc(mt.getDoc().sum(doc));
        Functions.replaceIfInDom(newMs,mt);
        return null;
      }
      );
    //create new class
    return cb.withMs(newMs);
  }


  public static ClassB addDocumentationOnNestedClass (Program p,ClassB cb, List<Ast.C> cs,Doc doc){
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(cb, cs, Optional.empty());
    cb= cb.onNestedNavigateToPathAndDo( cs, nc->Optional.of(nc.withDoc(nc.getDoc().sum(doc))));
    return cb;
  }

}

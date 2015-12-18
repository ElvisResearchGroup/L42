package is.L42.connected.withSafeOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import tools.Assertions;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.InvalidMwtAsState;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;
import platformSpecific.javaTranslation.Resources;
public class AddDocumentation {

  public static ClassB addDocumentationOnMethod (Program p,ClassB cb, List<String> cs,MethodSelector sel,Doc doc){
    Errors42.checkExistsPathMethod(cb, cs, Optional.of(sel));
    if(cs.isEmpty()){cb=auxAddDocOnMethod(p,cb,sel,doc);}
    else{
      p=p.addAtTop(cb);
      if(cs.size()>1){p=p.navigateInTo(cs.subList(0, cs.size()-1));}
      Program p1=p;
      cb= ClassOperations.onClassNavigateToPathAndDo(cb,cs,cbi->auxAddDocOnMethod(p1,cbi,sel,doc));
      }
    if(doc.isPrivate()){
      cb.getStage().setPrivateNormalized(false);
      cb=NormalizePrivates.normalize(p, cb);
      }
    return cb;
  }

  private static ClassB auxAddDocOnMethod(Program p,ClassB cb, MethodSelector sel, Doc doc) {
    List<Member> newMs=new ArrayList<>(cb.getMs());
    boolean docPrivate=doc.isPrivate();
    Member m=Program.getIfInDom(newMs, sel).get();
    //add comment
    m.match(nc->{throw Assertions.codeNotReachable();},
      mi->{
        if (docPrivate){throw Errors42.errorInvalidOnMember(doc);}
        mi=mi.withDoc(mi.getDoc().sum(doc));
        Program.replaceIfInDom(newMs,mi);
        return null;
        },
      mt-> makeMwtPrivate(p,cb, doc, newMs, docPrivate, mt)
      );
    //create new class
    return cb.withMs(newMs);
  }

  private static Void makeMwtPrivate(Program p,ClassB cb, Doc doc, List<Member> newMs, boolean docPrivate, MethodWithType mwt) {
    if (mwt.getInner().isPresent() || !docPrivate){
      mwt=mwt.withDoc(mwt.getDoc().sum(doc));
      Program.replaceIfInDom(newMs,mwt);
      return null;
      }
    boolean prState=ExtractInfo.hasPrivateState(cb);
    if(prState){throw Errors42.errorInvalidOnMember(doc);}
    if(mwt.getMt().getMdf()!=Mdf.Type){throw Errors42.errorInvalidOnMember(doc);}
    //is an abstract type method in a non private state class.
    //discover all potential getters/setters
    List<MethodWithType> nonTypeM = 
        cb.getMs().stream()
        .filter(m->m instanceof MethodWithType).map(m->(MethodWithType)m)
        .filter(m->m.getMt().getMdf()!=Mdf.Type)
        .collect(Collectors.toCollection(ArrayList::new));
    nonTypeM.add(mwt);//the chosen constructor
    List<InvalidMwtAsState> nonWelcome = Functions.coherent(p, cb.withMs(new ArrayList<>(nonTypeM)));
    for(InvalidMwtAsState e: nonWelcome){
      if(e.getMwt().equals(mwt)){throw Errors42.errorInvalidOnMember(doc);}
      nonTypeM.remove(e.getMwt());
    }
    for(MethodWithType mwti: nonTypeM){
      mwti=mwti.withDoc(mwti.getDoc().sum(doc));
      Program.replaceIfInDom(newMs,mwti);
    }
    return null;
  }

  public static ClassB addDocumentationOnNestedClass (Program p,ClassB cb, List<String> cs,Doc doc){
    if(cs.isEmpty()){throw Errors42.errorInvalidOnTopLevel();}
    Errors42.checkExistsPathMethod(cb, cs, Optional.empty());
    if(doc.isPrivate()){cb.getStage().setPrivateNormalized(false);}
    cb= ClassOperations.onNestedNavigateToPathAndDo(cb, cs, nc->Optional.of(nc.withDoc(nc.getDoc().sum(doc))));
    if(doc.isPrivate()){
      cb.getStage().setPrivateNormalized(false);
      cb=NormalizePrivates.normalize(p, cb);
      }
    return cb;
  }

}

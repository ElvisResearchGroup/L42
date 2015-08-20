package introspection;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import platformSpecific.javaTranslation.Resources;
import facade.Configuration;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.Header;
import ast.Ast.Mdf;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;
public class IntrospectionSum {
  private static Resources.Error fail(String msg){
    throw new Resources.Error(EncodingHelper.wrapStringU(msg));
    }
  public static ClassB sum(ClassB l1,ClassB l2,Path current){
    boolean isInterface=isNewHeaderInterface(l1,l2,current);
    assert l1.getStage().getStage()==Stage.None;
    assert l2.getStage().getStage()==Stage.None;
    List<Path> superT = new ArrayList<Path> (l1.getSupertypes());
    superT.addAll(l2.getSupertypes());
    List<Member> ms1 = new ArrayList<Member>(l1.getMs());
    for(Member m2:l2.getMs()){
      Optional<Member> m1Opt = Program.getIfInDom(ms1, m2);
      if(!m1Opt.isPresent()){ms1.add(m2);continue;}
      Member m1=m1Opt.get();
      doubleSimmetricalMatch(ms1, m1, m2,current);
    }
    Doc doc1 = l1.getDoc1().sum(l2.getDoc1());
    Doc doc2=l1.getDoc2().sum(l2.getDoc2());
    //if(h instanceof Ast.ConcreteHeader){purge(ms,(Ast.ConcreteHeader)h);}
    return new ClassB(doc1,doc2,isInterface,superT,ms1);
  }
/*private static void purge(List<Member> ms, ConcreteHeader h) {
    List<Member> ms2 = Configuration.typeExtraction.cfType(h);
    for(Member m2:ms2){
      Optional<Member> m1Opt = Program.getIfInDom(ms, m2);
      if(!m1Opt.isPresent()){continue;}
      Member m1=m1Opt.get();
      if(m1 instanceof MethodImplemented){fail("Implemented method: "+((MethodImplemented)m1).getS()+" overlaps with field method");}
      MethodWithType mwt1=(MethodWithType)m1;
      MethodWithType mwt2=(MethodWithType)m2;
      if(mwt1.getInner().isPresent()){fail("Implementation of method: "+mwt1.getMs()+" overlaps with field method");}
      if(!mwt1.getMt().equals(mwt2.getMt())){fail("Discordant type for method: "+mwt1.getMs()+" "+ToFormattedText.of(mwt1)+" -- "+ToFormattedText.of(mwt2));}
      ms.remove(mwt1);
    }
  }*/

  private static boolean isNewHeaderInterface(ClassB c1,ClassB c2,Path current) {
    if(c1.isInterface() &&c2.isInterface()){return true;}
    if(!c1.isInterface() && !c2.isInterface()){return false;}
    //it can be trait if interace is never implemented
    if(implementedInterfaces==null){return false;}//to ease testing
    if (!implementedInterfaces.contains(current)){ return false;}
    throw fail("InvalidSumPerformed, interface can not become concrete since is internaly implemented");
    }
  private static List<Path> implementedInterfaces=null;//TODO: compute at the very start
  public static void doubleSimmetricalMatch(List<Member> ms, Member m1,  Member m2,Path current) {
    m1.match(//match on the pre existing one
      nc1->{
        NestedClass nc2=(NestedClass)m2;
        Path innerCurrent=current.pushC(nc1.getName());
        ClassB newInner=sum((ClassB)nc1.getInner(),(ClassB)nc2.getInner(),innerCurrent);
        Doc doc=nc1.getDoc().sum(nc2.getDoc());
        Program.replaceIfInDom(ms, nc1.withInner(newInner).withDoc(doc));
        return null;
      },
      mi1->{
        if(m2 instanceof MethodImplemented){fail("InvalidSumPerformed");}
        MethodWithType mwt2=(MethodWithType)m2;
        if(mwt2.getInner().isPresent()){fail("InvalidSumPerformed");}
        Doc doc=mi1.getDoc().sum(mwt2.getDoc());
        Program.replaceIfInDom(ms, mi1.withDoc(doc));
        return null;
      },
      mt1->m2.match(//match on the new one
        nc2->{throw Assertions.codeNotReachable();},
        mi2->{
          if(mt1.getInner().isPresent()){fail("InvalidSumPerformed");}
          Doc doc=mt1.getDoc().sum(mi2.getDoc());
          Program.replaceIfInDom(ms, mi2.withDoc(doc));
          return null;
          },
        mt2->mtMt(ms, mt1, mt2)));
  }
  private static Object mtMt(List<Member> ms, MethodWithType mt1, MethodWithType mt2) {
    assert mt1.getMs().equals(mt2.getMs());
    if(mt1.getInner().isPresent() && mt2.getInner().isPresent()){fail("InvalidSumPerformed");}
    Doc docE=mt1.getMt().getDocExceptions().sum(mt2.getMt().getDocExceptions());
    List<Doc> tDocs=new ArrayList<>(mt1.getMt().getTDocs());
    List<Type> ts=mt1.getMt().getTs();
    if(!ts.equals(mt2.getMt().getTs())){fail("InvalidSumPerformed");}
    {int i=-1;for(Doc d2: mt2.getMt().getTDocs()){i+=1;
      tDocs.set(i,tDocs.get(i).sum(d2));}}
    List<Path> exceptions=mt1.getMt().getExceptions();
    if(!exceptions.equals(mt2.getMt().getExceptions())){fail("InvalidSumPerformed");}
    Mdf mdf=mt1.getMt().getMdf();
    if(!mdf.equals(mt2.getMt().getMdf())){fail("InvalidSumPerformed");}
    Type retType=mt1.getMt().getReturnType();
    if(!retType.equals(mt2.getMt().getReturnType())){fail("InvalidSumPerformed");}
    MethodType mt=new MethodType(docE,mdf,ts,tDocs,retType,exceptions);
    Optional<ExpCore>inner=mt1.getInner();
    if(!inner.isPresent()){inner=mt2.getInner();}
    MethodWithType mwt=new MethodWithType(mt1.getDoc().sum(mt2.getDoc()),
        mt1.getMs(), mt, inner,null);
    Program.replaceIfInDom(ms, mwt);
    return null;
  }
}

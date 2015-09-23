package is.L42.connected.withSafeOperators;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import facade.Configuration;
import ast.Ast.Doc;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.*;
import auxiliaryGrammar.Program;

public class Sum {
  static ClassB sum(Program p, ClassB a, ClassB b) {
    a=ClassOperations.normalizePaths(a);
    b=ClassOperations.normalizePaths(b);
    a =NormalizePrivates.normalize(p, a);
    b = NormalizePrivates.normalize(p, b);
    int famSizeA=a.getStage().getFamilies().size();
    int famSizeB=b.getStage().getFamilies().size();
    if(famSizeA>famSizeB){
      a=NormalizePrivates.refreshFamilies(b.getStage().getFamilies(), a);
    }
    else{
      b=NormalizePrivates.refreshFamilies(a.getStage().getFamilies(), b);
    }
    return normalizedTopSum(p, a, b);
  }

  public static ClassB normalizedTopSum(Program p, ClassB topA, ClassB topB) {
    return normalizedSum(p,topA, topB,topA, topB, Collections.emptyList());
    }

  static ClassB normalizedSum(Program p, ClassB topA, ClassB topB,ClassB a, ClassB b, List<String> current) {
    List<Member> ms = new ArrayList<>();
    doubleSimetricalMatch(p,topA,topB,a, b, ms, current);
    List<Path> superT = new ArrayList<Path>(a.getSupertypes());
    superT.addAll(b.getSupertypes());
    Doc doc1 = a.getDoc1().sum(b.getDoc1());
    Doc doc2 = a.getDoc2().sum(b.getDoc2());
    ExtractInfo.checkClassClash(p, current, topA, topB, a, b);
    boolean isInterface =a.isInterface() || b.isInterface();
    return new ClassB(doc1, doc2, isInterface, superT, ms);
    }
  private static void doubleSimetricalMatch(Program p, ClassB topA, ClassB topB,ClassB a, ClassB b, List<Member> ms, List<String> current) {
    for (Member m : a.getMs()) {//add from a+b
      Optional<Member> oms = Program.getIfInDom(b.getMs(), m);
      if (!oms.isPresent()) {
        ms.add(m);
        }
      else {
        doubleSimmetricalMatch(p, topA, topB, ms, current, m, oms.get());
        }
      }
    for (Member m : b.getMs()) {//add the rest
      if (!Program.getIfInDom(ms, m).isPresent()) {
        ms.add(m);
        }
      }
    }

  public static void doubleSimmetricalMatch(Program p, ClassB topA, ClassB topB, List<Member> ms, List<String> current, Member m, Member oms) {
    m.match(nc -> matchNC(p,topA,topB,nc, ms, (NestedClass) oms, current), mi -> matchMi(current, mi, ms, oms), mt -> matchMt(current, mt, ms, oms));
  }

  private static Void matchNC(Program p, ClassB topA, ClassB topB,NestedClass nca, List<Member> ms, NestedClass ncb, List<String> current) {
    List<String> innerCurrent = new ArrayList<>(current);
    innerCurrent.add(nca.getName());
    ClassB newInner = normalizedSum(p,topA,topB,(ClassB) nca.getInner(), (ClassB) ncb.getInner(), innerCurrent);
    Doc doc = nca.getDoc().sum(ncb.getDoc());
    ms.add(nca.withInner(newInner).withDoc(doc));
    return null;
    }


  private static Void matchMt(List<String> pathForError, MethodWithType mwta, List<Member> ms, Member mb) {
    if (mb instanceof MethodImplemented) { throw Errors42.errorMethodClash(pathForError, mwta, mb, false, Collections.emptyList(), false, false); }
    MethodWithType mwtb = (MethodWithType) mb;
    Errors42.checkMethodClash(pathForError, mwta, mwtb);
    ms.add(Sum.sumMethod(mwta, mwtb));
    return null;
  }

  private static Void matchMi(List<String> pathForError, MethodImplemented mia, List<Member> ms, Member mb) {
    throw Errors42.errorMethodClash(pathForError, mia, mb, false, Collections.emptyList(), false, false);
  }

  static MethodWithType sumMethod(MethodWithType ma, MethodWithType mb) {
    Set<Path> pa = new HashSet<>(ma.getMt().getExceptions());
    Set<Path> pb = new HashSet<>(mb.getMt().getExceptions());
    Set<Path> pc = new HashSet<>(pa);
    pc.retainAll(pb);
    Doc doc = ma.getDoc().sum(mb.getDoc());
    //tDocs=TDocs[with a in ma.mt().tDocs(), b in mb.mt().tDocs() ( a+b )]
    List<Doc> tDocs = new ArrayList<>();
    for (int i = 0; i < ma.getMt().getTDocs().size(); i += 1) {
      tDocs.add(ma.getMt().getTDocs().get(i).sum(mb.getMt().getTDocs().get(i)));
    }
    Doc docExc = ma.getMt().getDocExceptions().sum(mb.getMt().getDocExceptions());
    MethodType mt = ma.getMt().withDocExceptions(docExc).withTDocs(tDocs);
    ArrayList<Path> opc = new ArrayList<>(pc);
    Collections.sort(opc, (p1, p2) -> p1.toString().compareTo(p2.toString()));
    mt = mt.withExceptions(opc);
    MethodWithType mwt = ma.withMt(mt).withDoc(doc);
    assert !ma.getInner().isPresent() || !mb.getInner().isPresent();
    if (mb.getInner().isPresent()) {
      mwt = mwt.withInner(mb.getInner());
    }
    return mwt;
  }
}

package is.L42.connected.withSafeOperators;

import introspection.IntrospectionSum;

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
    List<ClassB.Member> ms = new ArrayList<>();
    ms.add(new ClassB.NestedClass(Doc.empty(), "A", a));//TODO: no, we do not add 1 outer to external paths
    ms.add(new ClassB.NestedClass(Doc.empty(), "B", b));
    //ClassB ab = new ClassB(Doc.empty(), Doc.empty(), false, Collections.emptyList(), ms, Stage.None);
    //ab = ClassOperations.normalizePrivates(p,ab);
    //ab = ClassOperations.normalizePaths(ab,Collections.emptyList());
    //a = (ClassB) ((ClassB.NestedClass) ab.getMs().get(0)).getInner();
    //b = (ClassB) ((ClassB.NestedClass) ab.getMs().get(1)).getInner();
    a = ClassOperations.normalizePrivates(p,a);
    b = ClassOperations.normalizePrivates(p,b);
    a=ClassOperations.normalizePaths(a,Collections.emptyList());
    b=ClassOperations.normalizePaths(b,Collections.emptyList());
    return normalizedTopSum(p, a, b);
  }

  public static ClassB normalizedTopSum(Program p, ClassB topA, ClassB topB) {
    ClassB typeA = Configuration.typeSystem.typeExtraction(p, topA);
    ClassB typeB = Configuration.typeSystem.typeExtraction(p, topB);
    return new Object() {
      ClassB normalizedSum(ClassB a, ClassB b, List<String> current) {
        List<Member> ms = new ArrayList<>();
        doubleSimetricalMatch(a, b, ms, current);
        List<Path> superT = new ArrayList<Path>(a.getSupertypes());
        superT.addAll(b.getSupertypes());
        Doc doc1 = a.getDoc1().sum(b.getDoc1());
        Doc doc2 = a.getDoc2().sum(b.getDoc2());
        ExtractInfo.checkClassClash(p, current, topA, topB, typeA, typeB, a, b);
        boolean isInterface =a.isInterface() || b.isInterface(); 
        //*sum of class with non compatible interfaces (same method, different signature)
        //doh, this requires the program!!
        //*sum of two classes with private state
        //*sum class/interface invalid
        return new ClassB(doc1, doc2, isInterface, superT, ms, Stage.None);
      }

      private void doubleSimetricalMatch(ClassB a, ClassB b, List<Member> ms, List<String> current) {
        for (Member m : a.getMs()) {//add from a+b
          Optional<Member> oms = Program.getIfInDom(b.getMs(), m);
          if (!oms.isPresent()) {
            ms.add(m);
          } else {
            m.match(nc -> matchNC(nc, ms, (NestedClass) oms.get(), current), mi -> matchMi(current, mi, ms, oms.get()), mt -> matchMt(current, mt, ms, oms.get()));
          }
        }
        for (Member m : b.getMs()) {//add the rest
          if (!Program.getIfInDom(ms, m).isPresent()) {
            ms.add(m);
          }
        }
      }

      private Void matchNC(NestedClass nca, List<Member> ms, NestedClass ncb, List<String> current) {
        List<String> innerCurrent = new ArrayList<>(current);
        innerCurrent.add(nca.getName());
        ClassB newInner = normalizedSum((ClassB) nca.getInner(), (ClassB) ncb.getInner(), innerCurrent);
        Doc doc = nca.getDoc().sum(ncb.getDoc());
        ms.add(nca.withInner(newInner).withDoc(doc));
        return null;
      }
    }.normalizedSum(topA, topB, Collections.emptyList());
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

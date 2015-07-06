package is.L42.connected.withSafeOperators;

import introspection.ConsistentRenaming;
import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import platformSpecific.javaTranslation.Resources;
import coreVisitors.CollectPrivateNames;
import ast.Ast.Doc;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.*;
import ast.Util.PathMxMx;
import auxiliaryGrammar.Program;

public class Sum {
  static ClassB sum(ClassB a,ClassB b){
    List<ClassB.Member> ms=new ArrayList<>();
    ms.add(new ClassB.NestedClass(Doc.empty(),"A",a));
    ms.add(new ClassB.NestedClass(Doc.empty(),"B",b));
    ClassB ab=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    ab=normalize(ab);
    a=(ClassB)((ClassB.NestedClass)ab.getMs().get(0)).getInner();
    b=(ClassB)((ClassB.NestedClass)ab.getMs().get(1)).getInner();
    //return IntrospectionSum.sum(a, b, Path.outer(0));
    return normalizedSum(a,b,Path.outer(0));
  }
  private static ClassB normalizedSum(ClassB a, ClassB b,Path path) {
    List<Member> ms=new ArrayList<>();
    doubleSimetricalMatch(a, b, ms,path);
    List<Path> superT = new ArrayList<Path> (a.getSupertypes());
    superT.addAll(b.getSupertypes());
    Doc doc1 = a.getDoc1().sum(b.getDoc1());
    Doc doc2=a.getDoc2().sum(b.getDoc2());
    boolean isInterface=false;//TODO:

    return new ClassB(doc1,doc2,isInterface,superT,ms,Stage.None);
  }
  private static void doubleSimetricalMatch(ClassB a, ClassB b, List<Member> ms,Path current) {
    new Object(){{
      for(Member m:a.getMs()){//add from a+b
        Optional<Member> oms = Program.getIfInDom(b.getMs(),m);
        if(!oms.isPresent()){ms.add(m);}
        else {m.match(nc->matchNC(nc,ms,(NestedClass)oms.get()), mi->matchMi(mi,ms,oms.get()), mt->matchMt(mt,ms,oms.get()));}
        }
      for(Member m:b.getMs()){//add the rest
        if(!Program.getIfInDom(ms,m).isPresent()){ms.add(m);}
        }
      }
      private Void matchMt(MethodWithType mwta, List<Member> ms, Member mb) {
        if(mb instanceof MethodImplemented){throw ExtractInfo.clashImpl(mb,mwta);}
        MethodWithType mwtb=(MethodWithType)mb;
        ExtractInfo.checkMethodClash(mwta,mwtb);
        return Sum.sumMethod(mwta,mwtb);
        }
      private Void matchMi(MethodImplemented mia, List<Member> ms, Member mb) {
        throw ExtractInfo.clashImpl(mia,mb);
        }

    private Void matchNC(NestedClass nca, List<Member> ms, NestedClass ncb) {
      Path innerCurrent=current.pushC(nca.getName());
      ClassB newInner=normalizedSum((ClassB)nca.getInner(),(ClassB)ncb.getInner(),innerCurrent);
      Doc doc=nca.getDoc().sum(ncb.getDoc());
      ms.add(nca.withInner(newInner).withDoc(doc));
      return null;
      }};
    }
  protected static Void sumMethod(MethodWithType ma, MethodWithType mb) {
    Set<Path> pa=new HashSet<>(ma.getMt().getExceptions());
    Set<Path> pb=new HashSet<>(mb.getMt().getExceptions());
    Set<Path> pc=new HashSet<>(pa);
    pc.retainAll(pb);
    Doc doc=ma.getDoc().sum(mb.getDoc());
    Doc tDocs=ma.getMt().getTDocs();
    mb.getMt().getTDocs()
    ma.getMt().getDocExceptions()
    return null;
  }
  static ClassB normalize(ClassB cb){
    //collect private names
    CollectPrivateNames cpn=CollectPrivateNames.of(cb);
    //rename all
    Program emptyP=Program.empty();
    List<PathMxMx> mapMx = ConsistentRenaming.makeMapMxConsistent(cb,cpn.mapMx);
    cb=IntrospectionAdapt.applyMapMx(emptyP,cb,mapMx);
    cb=IntrospectionAdapt.applyMapPath(emptyP,cb,cpn.mapPath);
    return cb;
  }
}

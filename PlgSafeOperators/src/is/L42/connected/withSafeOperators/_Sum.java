package is.L42.connected.withSafeOperators;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import platformSpecific.javaTranslation.Resources;
import privateMangling.RefreshUniqueNames;
import sugarVisitors.CloneVisitor;
import sugarVisitors.CollapsePositions;
import tools.Map;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;

import auxiliaryGrammar.Functions;
import ast.ExpCore.*;
import programReduction.Program;
import coreVisitors.From;

public class _Sum {
  /*static ClassB sum(Program p, ClassB a, ClassB b) {
    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    //System.out.println("___________________________________________________");
    //System.out.println(a);
    //System.out.println(b);
    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    a=ClassOperations.normalizePaths(a);
    b=ClassOperations.normalizePaths(b);
    b =(ClassB) RefreshUniqueNames.refresh(b);
    return normalizedTopSum(p, a, b);
  }*/

  public static ClassB normalizedTopSum(Program p, ClassB topA, ClassB topB) {
     ClassB result=normalizedSum(p,topA, topB,topA, topB, Collections.emptyList());
     //Sum.interfaceClash(p,result);
     return result;
    }
/*
  private static void interfaceClash(Program p, ClassB candidate) {
   try{
     Configuration.typeSystem.computeStage(p,candidate);
   }catch(ErrorMessage.IncoherentMwts i){
     List<Path>ps=new ArrayList<>();
     List<Member>mems=new ArrayList<>();
     Path ph=Path.outer(0,i.getExploredPath());
     for(PathMwt e:i.getIncoherent()){
       ps.add(From.fromP(e.getOriginal(),ph));
     }
     assert !ps.isEmpty();
     Member  notInterf=null;
     Program p1=p.addAtTop(candidate);
     for(Path pi:ps){
       ClassB cb=p1.extractCb(pi);
       System.out.println(pi);
       System.out.println(i);
       System.out.println(cb.getStage());
       System.out.println(candidate.getStage());
       Optional<Member> currentOpt=Program.getIfInDom(cb.getMs(),i.getGuilty());
       Member current=currentOpt.get();

       if(cb.isInterface()){
         mems.add(current);
       }
       else notInterf=current;
     }
     if(notInterf==null){
       throw Errors42.errorClassClash(i.getExploredPath(), ps);
     }
     Member mb=mems.get(0);
     throw Errors42.errorMethodClash(i.getExploredPath(), notInterf,mb, true,Collections.emptyList(),true,true,true);

   }
  }
*/
  static ClassB normalizedSum(Program p, ClassB topA, ClassB topB,ClassB a, ClassB b, List<Ast.C> current) {
    List<Member> ms = doubleSimetricalMatch(p,topA,topB,a, b,  current);
    List<ast.Ast.Type> superT = new ArrayList<>(a.getSupertypes());
    superT.addAll(b.getSupertypes());
    superT=Collections.unmodifiableList(superT);
    Doc doc1 = a.getDoc1().sum(b.getDoc1());
    //Sum.checkClassClash(p, current, topA, topB, a, b);
    boolean isInterface =a.isInterface() || b.isInterface();
    Phase accPhase = a.getPhase().acc(b.getPhase());
    ExpCore.ClassB res= new ClassB(doc1, isInterface, superT,
            ms,a.getP().sum(b.getP()),
            accPhase,accPhase==Phase.None?0:1);
    res=(ClassB) res.accept(new coreVisitors.CloneVisitor(){
      public ExpCore visit(ClassB s){
        if (s.getPhase()==Phase.None){return super.visit(s);}
        return super.visit(s.withPhase(accPhase));
      }
      });//TODO: remove after new reduction introduced
    return res;
    }
  /*
  private static void checkMethodClashInterfaceAbstract(List<String> pathForError,List<PathMwt> aInh, List<PathMwt> bInh, List<Member> ms) {
    for(Member m:ms){
      if (!(m instanceof MethodWithType)){continue;}
      MethodWithType mwt=(MethodWithType)m;
      for(PathMwt a: bInh){
        if(a.getMwt().getMs().equals(mwt.getMs())){
          throw Errors42.errorMethodClash(pathForError, mwt,a.getMwt(), false, Collections.emptyList(), false,false,true);
        }
      }
      for(PathMwt b: bInh){
        if(b.getMwt().getMs().equals(mwt.getMs())){}
      }
    }
  }
*/
  private static List<Member> doubleSimetricalMatch(Program p, ClassB topA, ClassB topB,ClassB a, ClassB b, List<Ast.C> current) {
    List<Member> ms=new ArrayList<>();
    for (Member m : a.getMs()) {//add from a+b
      Optional<Member> oms = Functions.getIfInDom(b.getMs(), m);
      if (!oms.isPresent()) {
        ms.add(m);
        }
      else {
        doubleSimmetricalMatch(p, topA, topB, ms, current, m, oms.get());
        }
      }
    for (Member m : b.getMs()) {//add the rest
      if (!Functions.getIfInDom(ms, m).isPresent()) {
        ms.add(m);
        }
      }
    return ms;
    }

  public static void doubleSimmetricalMatch(Program p, ClassB topA, ClassB topB, List<Member> ms, List<Ast.C> current, Member m, Member oms) {
    m.match(
        nc -> matchNC(p,topA,topB,nc, ms, (NestedClass) oms, current),
        mi -> matchMi(current, mi, ms, oms),
        mt -> matchMt(current, mt, ms, oms));
  }

  private static Void matchNC(Program p, ClassB topA, ClassB topB,NestedClass nca, List<Member> ms, NestedClass ncb, List<Ast.C> current) {
    List<Ast.C> innerCurrent = new ArrayList<>(current);
    innerCurrent.add(nca.getName());
    ClassB newInner = normalizedSum(p,topA,topB,(ClassB) nca.getInner(), (ClassB) ncb.getInner(), innerCurrent);
    Doc doc = nca.getDoc().sum(ncb.getDoc());
    ms.add(nca.withInner(newInner).withDoc(doc));
    return null;
    }


  private static Void matchMt(List<Ast.C> pathForError, MethodWithType mwta, List<Member> ms, Member mb) {
    if (mb instanceof MethodImplemented) { throw Errors42.errorMethodClash(pathForError, mwta, mb, false, Collections.emptyList(), false, false,false); }
    MethodWithType mwtb = (MethodWithType) mb;
    Errors42.checkMethodClash(pathForError, mwta, mwtb,false);
    ms.add(_Sum.sumMethod(mwta, mwtb));
    return null;
  }

  private static Void matchMi(List<Ast.C> pathForError, MethodImplemented mia, List<Member> ms, Member mb) {
    throw Errors42.errorMethodClash(pathForError, mia, mb, false, Collections.emptyList(), false, false,false);
  }

  static MethodWithType sumMethod(MethodWithType ma, MethodWithType mb) {
    Set<Path> pa = new HashSet<Path>(Map.of(t->t.getPath(),ma.getMt().getExceptions()));
    Set<Path> pb = new HashSet<Path>(Map.of(t->t.getPath(),mb.getMt().getExceptions()));
    Set<Path> pc = new HashSet<>(pa);
    pc.retainAll(pb);
    Doc doc = ma.getDoc().sum(mb.getDoc());
    MethodType mt = ma.getMt();
    List<Ast.Type> opc =pc.stream().map(pi->(Ast.Type)pi.toImmNT()).collect(Collectors.toList());
    Collections.sort(opc, (p1, p2) -> p1.toString().compareTo(p2.toString()));
    mt = mt.withExceptions(opc);
    MethodWithType mwt = ma.withMt(mt).withDoc(doc);
    //now mwt has min exceptions and summed docs
    assert ma.get_inner()==null || mb.get_inner()==null;
    if (mb.get_inner()!=null) {
      mwt = mwt.withInner(mb.getInner());
    }
    return mwt;
  }

  public static void checkClassClash(
      Program p,List<Ast.C>current,
      ClassB topA,ClassB topB,
      ClassB currentA,ClassB currentB){

   //*sum of two classes with private state
   //*sum class/interface invalid
   boolean privateA=ExtractInfo.hasPrivateState(currentA);
   boolean privateB=ExtractInfo.hasPrivateState(currentB);
   boolean twoPrivateState=privateA &&privateB;
   boolean isAllOk= !twoPrivateState && currentA.isInterface()==currentB.isInterface();
   if (isAllOk){return;}
   ExtractInfo.ClassKind kindA=ExtractInfo.classKind(topA,current,currentA,null,privateA,null);
   ExtractInfo.ClassKind kindB=ExtractInfo.classKind(topB,current,currentB,null,privateB,null);
   boolean isClassInterfaceSumOk=currentA.isInterface()==currentB.isInterface();
   if(!isClassInterfaceSumOk){
     isClassInterfaceSumOk=kindA==ExtractInfo.ClassKind.FreeTemplate||kindB==ExtractInfo.ClassKind.FreeTemplate;
     }
   isAllOk= !twoPrivateState && isClassInterfaceSumOk;
   if (isAllOk){return;}
   throw Errors42.errorClassClash(current, Collections.emptyList());
  }
/*
  public static List<Path>
    conflictingImplementedInterfaces(
        Program p,List<String>current,ClassB cba,ClassB cbb){
      List<Path> cc=new ArrayList<>();
      for(PathMwt a: cba.getStage().getInherited()){
        for(PathMwt b: cbb.getStage().getInherited()){
          if(a.getMwt().getMs().equals(b.getMwt().getMs())){
            if(a.getOriginal().equals(b.getOriginal())){continue;}
            cc.add(a.getOriginal());
            cc.add(b.getOriginal());
            }
      } }
     return cc;
  }*/
}

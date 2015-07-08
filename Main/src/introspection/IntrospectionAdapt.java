package introspection;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import sugarVisitors.Desugar;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import coreVisitors.CloneVisitor;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import coreVisitors.FromInClass;
import coreVisitors.RemoveMethod;
import coreVisitors.RetainOnlyAndRenameAs;
import coreVisitors.Visitor;
import facade.Configuration;
import ast.Ast;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.TraitHeader;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.X;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util.PathMxMx;
import ast.Util.PathPath;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class IntrospectionAdapt {
  public static List<ast.Util.PathMxMx> mapMx(ClassB l){
    Path candidate=Path.outer(0);
    List<ast.Util.PathMxMx> result=new ArrayList<>();
    collectMapPathMxMx(candidate,l,result);
    return result;
  }
  private static void collectMapPathMxMx(Path candidate, ClassB l, List<PathMxMx> list) {
    for(Member m:l.getMs()){
      m.match(nc->{collectMapPathMxMxNc(candidate,nc,list);return null;},
          mi->null,
          mt->collectSinglePathMxMx(list,candidate,mt)
          );
    }
  }
  private static Void collectSinglePathMxMx(List<PathMxMx> list,Path p,MethodWithType mt) {
    if(!mt.getInner().isPresent()){return null;}
    ExpCore e=mt.getInner().get();
    if(!(e instanceof MCall)){return null;}
    MCall mc=(MCall)e;
    if(!mc.getReceiver().equals(new X("this"))){return null;}
    if(mc.getXs().size()!=mt.getMs().getNames().size()){return null;}
    MethodSelector ms=new MethodSelector(mc.getName(),mc.getXs());
    list.add(new PathMxMx(p,mt.getMs(),ms));
    return null;
  }
  private static void collectMapPathMxMxNc(Path candidate, NestedClass nc, List<PathMxMx> list) {
    assert nc.getInner() instanceof ClassB;
    candidate=candidate.pushC(nc.getName());
    collectMapPathMxMx(candidate,(ClassB)nc.getInner(),list);
    }
  public static List<ast.Util.PathPath> mapPath(ClassB l){
    Path candidate=Path.outer(0);
    List<ast.Util.PathPath> result=new ArrayList<ast.Util.PathPath>();
    collectMapPath(candidate,l,result);
    return result;
  }
  public static void collectMapPath(Path candidate,ClassB l,List<ast.Util.PathPath> list){
    if(l.getDoc1().getPaths().size()==1){
      collectMapPathH(candidate,l.getDoc1().getPaths().get(0),list);
    }
    for(Member m:l.getMs()){
      m.match(nc->{collectMapPathNc(candidate,nc,list);return null;},
          mi->null, mt->null);
    }
  }
  private static void collectMapPathNc(Path candidate, NestedClass nc,List<PathPath> list) {
    assert nc.getInner() instanceof ClassB;
    candidate=candidate.pushC(nc.getName());
    collectMapPath(candidate,(ClassB)nc.getInner(),list);
  }
  private static void collectMapPathH(Path candidate,Path pointed,List<PathPath> list) {
    if(pointed.isPrimitive()){
      assert From.fromP(pointed,candidate).equals(pointed);
      }
      list.add(new PathPath(candidate, From.fromP(pointed,candidate)));
  }
  public static ClassB adapt(Program p,ClassB l1, ClassB l2) {
    //System.out.println("used Adapteee is:"+ToFormattedText.of(l1));
    //System.out.println("used Adapter is:"+ToFormattedText.of(l2));
    List<PathMxMx> mapMx = mapMx(l2);
    List<PathPath> mapPath = mapPath(l2);
    l1=applyMapMx(p,l1,mapMx);
    l1=applyMapPath(p,l1,mapPath);
    //System.out.println("adapted result is:\n"+ToFormattedText.of(l1));
    return l1;
  }

//TODO: using a different order of operations wrt pg 7
  public static ClassB applyMapPath(Program p,ClassB l, List<PathPath> mapPath) {
    List<ClassB> results=new ArrayList<ClassB>();
    ClassB lprime=l;
    Program pPrime=removeTopLevelWalkBy(p);
    lprime=renameUsage(pPrime,mapPath,lprime);
    ClassB l0=lprime;
    for(PathPath pp:mapPath){
      l0=remove(pp.getPath1(),l0);
    }
    results.add(l0);
    for(PathPath pp:mapPath){
      redirectDefinition(pp,lprime,results);
    }
    return accumulate(results,Path.outer(0));

    /*
    List<ClassB> results=new ArrayList<ClassB>();
    ClassB l0=l;
    for(PathPath pp:mapPath){
      l0=remove(pp.getPath1(),l0);
    }
    results.add(l0);
    for(PathPath pp:mapPath){
      redirectDefinition(pp,l,results);
    }
   l0=accumulate(results,Path.outer(0));

    Program pPrime=removeTopLevelWalkBy(p);
    for(PathPath pp:mapPath){
      l0=renameUsage(pPrime,pp,l0);//TODO: this do rename usages as a sequence, is there any point where we would need
      //consistent "instantanius" multiple rename usage?
    }
    return l0;
    */
  }
  public static Program removeTopLevelWalkBy(Program p) {
    if(p.isEmpty()){return p;}
    Program pPrime=p.dupHead();
    Optional<NestedClass> wb = Program.findWalkBy(p.top());
    if(wb.isPresent()){
      ClassB newTop=p.top().withMember(wb.get().withBody(new Signal(SignalKind.Error,new _void())));
      pPrime.updateTop(newTop);
    }
    return pPrime;
  }
  private static void redirectDefinition(PathPath pp, ClassB lprime,List<ClassB> results) {
    assert pp.getPath1().isCore();
    assert pp.getPath1().outerNumber()==0;
    //assert !pp.getPath1().isPrimitive();implied from above  }
    if(pp.getPath2().isPrimitive()){return;}
    assert pp.getPath2().isCore();
    if(pp.getPath2().outerNumber()>0){return;}
    List<String> cBar1 = pp.getPath1().getCBar();
    //if(cBar1.isEmpty()){return;}
    //assert !cBar1.isEmpty();
    List<String> cBar2 = pp.getPath2().getCBar();
    Path toFrom = computeSquareTo(cBar1, cBar2);
    Doc[] docCb=new Doc[]{Doc.empty()};
    ClassB cb=Program.extractCBar(cBar1, lprime,docCb);
    //from works so that an internal path stay the same,
    //and an external path change to another external path.
    //for into:Name"Outer0" we need external paths to become internal

    if(toFrom.outerNumber()>0 && toFrom.getCBar().size()>0){
      toFrom=toFrom.setNewOuter(toFrom.outerNumber()-1);
      toFrom=toFrom.popC();
      cb=(ClassB)FromInClass.of(cb, toFrom);
    }
    else {
        if(toFrom.outerNumber()>0 /*&&toFrom.getCBar().size()==0*/){
          assert toFrom.getCBar().size()==0;
          cb=(ClassB)FromInClass.of(cb, toFrom);
        }
      else {
        assert toFrom.outerNumber()==0;
        //remove outerN where N is toFrom.getCBar().size()
        int _n=toFrom.getCBar().size();
        if(_n!=0){//TODO: test case _n==0
          cb=(ClassB)cb.accept(new CloneVisitor(){
            int n=_n-1;
            public ExpCore visit(ClassB cb){
              int oldN=n;n+=1;
              try{return super.visit(cb);}
              finally{n=oldN;}
              }
            public ExpCore visit(Path p){
              if(p.isPrimitive()){return p;}
              if(p.outerNumber()<n){return p;}//T ODO: is it right?
              p=p.setNewOuter(p.outerNumber()-_n);//_n used on purpose
              return p;
            }
          });
        }
    }}

    if(cBar2.isEmpty()){results.add(cb);return;}
    List<Member>ms=new ArrayList<>();
    ms.add(encapsulateIn(cBar2, cb,docCb[0]));
    results.add(new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None));
    return;
  }
  //C1:{ ...{ .. .Cn:{} }}
  public static ClassB.NestedClass encapsulateIn(List<String> cBar,ClassB elem,Doc doc) {
    //Notice: encapsulation do not do the from. It must be done
    //on the call side as in "redirectDefinition"
    assert !cBar.isEmpty();
    List<String> cBar2 = cBar.subList(1,cBar.size());
    if(cBar2.isEmpty()){return new ClassB.NestedClass(doc,cBar.get(0),elem);}
    List<Member> ms=new ArrayList<>();
    ms.add(encapsulateIn(cBar2,elem,doc));
    ClassB cb= new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    return new ClassB.NestedClass(Doc.empty(),cBar.get(0),cb);
  }
  private static Path computeSquareTo(List<String> cBar1, List<String> cBar2) {
    if(cBar2.isEmpty()){return Path.outer(0,cBar1);}
    if(cBar1.isEmpty()){return Path.outer(cBar2.size(),cBar1);}//otherwise
    if(!cBar1.get(0).equals(cBar2.get(0))){return Path.outer(cBar2.size(),cBar1);}//otherwise
    return computeSquareTo(cBar1.subList(1, cBar1.size()), cBar2.subList(1, cBar2.size()));
    //Path p=recComputeSquareTo(cBar1, cBar2.subList(0,cBar2.size()-1));
    //List<String> cBar = p.getCBar();
    //p=Path.outer(p.outerNumber(),cBar.subList(0, cBar.size()-1));
    //return p;
  }
/*private static Path recComputeSquareTo(List<String> cBar1, List<String> cBar2) {
    if(cBar2.isEmpty()){return Path.outer(0,cBar1);}
    assert !cBar1.isEmpty();
    String firstCBar1=cBar1.get(0);
    String firstCBar2=cBar2.get(0);
    if(firstCBar1.equals(firstCBar2)){
      return recComputeSquareTo(cBar1.subList(1,cBar1.size()),cBar2.subList(1,cBar2.size()));
      }
    return Path.outer(cBar2.size(),cBar1);
  }*/
  private static ClassB remove(Path path1, ClassB l) {
    if(path1.equals(Path.outer(0))){
      return new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),Stage.None);
      }
    return (ClassB)l.accept(new CloneVisitor(){
      List<String> cs=path1.getCBar();
      public List<Member> liftMembers(List<Member> s) {
        List<Member> result=new ArrayList<Member>();
        for(Member m:s){m.match(
          nc->manageNC(nc,result),
          mi->result.add(liftM(m)),
          mt->result.add(liftM(m))
          );}
        return result;
        }
      private boolean manageNC(NestedClass nc, List<Member> result) {
        assert !cs.isEmpty();
        String top=cs.get(0);
        if(!top.equals(nc.getName())){return result.add(nc);}//out of path
        if(cs.size()==1){return true;}
        List<String> csLocal=cs;
        cs=cs.subList(1,cs.size());
        try{return result.add(this.visit(nc));}
        finally{cs=csLocal;}
      }
    });
  }
  private static Path superPathAdapt(Path shortP1,Path shortP2,Path longP){
    if(shortP2.isPrimitive()){return shortP2;}
    List<String> scs=shortP2.getCBar();
    List<String> lcs=longP.getCBar();
    List<String> res=new ArrayList<>(scs);
    res.addAll(lcs.subList(shortP1.getCBar().size(),lcs.size()));
    return Path.outer(shortP2.getN(),res);
  }
  private static boolean  isSuperPath(Path shortP,Path longP){
    if(shortP.getN()!=longP.getN()){return false;}
    List<String> scs=shortP.getCBar();
    List<String> lcs=longP.getCBar();
    if ( scs.size()>=lcs.size()){return false;}
    {int i=-1;for(String sci:scs){i+=1;String lci=lcs.get(i);
      if(!sci.equals(lci)){return false;}
    }}
    return  true;
    }
  private static ClassB renameUsage(Program _p, List<PathPath> _map, ClassB l) {
    return new CloneVisitorWithProgram(_p){
      List<PathPath> map=removePathInsideSubPath(_map);
      public ExpCore visit(Path s) {
        Path p0n=Norm.of(this.p,s);
        for(PathPath pp:map){
          Path p1n=Norm.of(this.p,pp.getPath1());
          Path p2n=Norm.of(this.p,pp.getPath2());
          if(p0n.equals(p1n)){
              return p2n;
              }
          }
        for(PathPath pp:map){//we need two for, to be sure to use exact match if possible
          Path p1n=Norm.of(this.p,pp.getPath1());
          Path p2n=Norm.of(this.p,pp.getPath2());
          if(isSuperPath(p1n,p0n)){
            return superPathAdapt(p1n,p2n,p0n);
            }
          }
        return s;
        }
      public ExpCore visit(ClassB s) {
        List<PathPath> oldMap=map;
        map=Map.of(pp->new PathPath(add1Outer(pp.getPath1()),add1Outer(pp.getPath2())), map);
        try{return super.visit(s);}
        finally{map=oldMap;}
        }
    }.startVisit(l);
  }
  private static List<PathPath> removePathInsideSubPath(List<PathPath> map) {
    List<PathPath> result=new ArrayList<PathPath>();
    for(PathPath pp:map){
        if(isPathInsideSubPath(pp)){continue;}
        result.add(pp);
    }
    return result;
}
private static boolean isPathInsideSubPath(PathPath pp) {
  assert pp.getPath1().isCore();
  assert pp.getPath1().outerNumber()==0;
  if(pp.getPath2().isPrimitive()){return false;}
  assert pp.getPath2().isCore():pp.getPath2();
  if(pp.getPath2().outerNumber()!=0){return false;}
  return 0==Collections.indexOfSubList(pp.getPath2().getRowData(),pp.getPath1().getRowData());
 }
static Path add1Outer(Path p) {
    if( p.isPrimitive()){return p;}
    return p.setNewOuter(p.outerNumber()+1);
  }
  public static ClassB applyMapMx(Program p,ClassB l, List<PathMxMx> mapMx) {
    List<ClassB> results=new ArrayList<ClassB>();
    ClassB lprime=l;
    Program pPrime=removeTopLevelWalkBy(p);
    lprime=RenameUsage.of(pPrime,mapMx,lprime);
    ClassB l0=lprime;
    for(PathMxMx pp:mapMx){
      l0=RemoveMethod.of(l0, pp.getPath(), pp.getMs1(),pp.getMs2());
    }
    results.add(l0);
    for(PathMxMx pp:mapMx){
      results.add(RetainOnlyAndRenameAs.of(lprime, pp.getPath(), pp.getMs1(),pp.getMs2()));
    }
    return accumulate(results,Path.outer(0));
  }
  /*private static ClassB renameUsage(Program _p, PathMxMx _pMx, ClassB l) {
    return new RenameUsage(_p,_pMx).startVisit(l);
  }*/

  private static ClassB accumulate(List<ClassB> results,Path current) {
    ClassB res=results.get(0);
    for(ClassB cbi:results.subList(1, results.size())){
      res=IntrospectionSum.sum(res,cbi,current);
    }
    return res;
  }

  public static Path extractPath(String s1) {
    if(s1.equals("Outer0")){return Path.outer(0);}
    List<String> unchecked = Arrays.asList(s1.split("::"));
    List<String> rowData=new ArrayList<String>();
    for(String s:unchecked){
      if(!checkC(s)){return null;}
      rowData.add(s);
      }
    Path p= new Path(rowData);
    if(p.isPrimitive()){
      return null;
      }
    if(p.isCore()){return null;}
    return p;
  }
  private static boolean checkC(String s) {
    if(s.isEmpty()){return false;}
    for(char c:s.toCharArray()){
      if(c=='%'){continue;}
      if(c=='_'){continue;}
      if(c>='A' && c<='Z'){continue;}
      if(c>='a' && c<='z'){continue;}
      if(c>='0' && c<='9'){continue;}
      }
    char c=s.charAt(0);
    return c=='%' || (c>='A' && c<='Z');
  }


  //{ %o_0%:{ method Void m(Void x1 ... Void xn)
  //method Void #o_0#(Void _1...Void _n) (this.lowercase(x1:_1.. xn:_n)}}
  public static ExpCore buildMethodNameAdapter(MethodSelector s) {
    List<Member> ms=new ArrayList<>();
    ms.add(new ClassB.NestedClass(Doc.empty(),"%o_0%",buildMethodNameAdapterInner(s)));
    ClassB outer=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    return outer;
  }
  private static ExpCore buildMethodNameAdapterInner(MethodSelector s) {
    List<String> xs1=new ArrayList<>();
    List<ExpCore> es1=new ArrayList<>();
    List<Doc> tsDocs=new ArrayList<>();
    List<Type> ts=new ArrayList<>();
    Type tVoid=new Ast.NormType(Mdf.Immutable,Path.Void(),Ph.None);
    {int i=-1;for(String si:s.getNames()){i+=1;
      xs1.add("_"+i);
      tsDocs.add(Doc.empty());
      ts.add(tVoid);
      es1.add(new X("_"+i));
      }}
    MethodSelector s1=new MethodSelector("#o_0#",xs1);
    MethodType mt1=new MethodType(Doc.empty(),Mdf.Immutable,ts,tsDocs,tVoid,Collections.emptyList());
    MethodSelector s2=s;
    MethodType mt2=new MethodType(Doc.empty(),Mdf.Immutable,ts,tsDocs,tVoid,Collections.emptyList());
    ExpCore e1=new MCall(null,new X("this"),s.getName(),Doc.empty(),s.getNames(),es1);
    List<Member> ms=new ArrayList<>();
    ms.add(new MethodWithType(Doc.empty(),s1,mt1,Optional.of(e1)));
    ms.add(new MethodWithType(Doc.empty(),s2,mt2,Optional.empty()));

    ClassB inner=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);

    return inner;
  }
  //%path-> adapter: C1::...::Cn  turn in {  %o_0%: {(Outer1::C1::Cn that)}   C1:{ ...{ .. .Cn:{} }}}
  //%path-> adapter: Outern::C1::...::Cn  turn in {  %o_0%: {(Outer1::C1::Cn that)}   C1:{ ...{ .. .Cn:{} }}}
  public static ExpCore buildPathAdapterOut(Path path) {
    List<Member> ms=new ArrayList<>();
    ms.add(buildPathAdapterInner(pathExternToInner(path)));
    ClassB outer=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    return outer;
  }
  public static ExpCore buildPathAdapterIn(Path path) {
    List<Member> ms=new ArrayList<>();
    assert !path.isPrimitive();
    if(!path.equals(Path.outer(0))){
      assert !path.isCore();
      ms.add(IntrospectionAdapt.encapsulateIn(path.getRowData(),
        new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),Stage.None),
        Doc.empty()
        ));
      }
    ms.add(buildPathAdapterInner(reprToInner(path)));
    ClassB outer=new ClassB(Doc.empty(),Doc.empty(),false,Collections.emptyList(),ms,Stage.None);
    return outer;
  }
  private static Path reprToInner(Path path){
    assert !path.isPrimitive();
    List<String> row=new ArrayList<>();
    row.add("Outer1");
    if(!path.equals(Path.outer(0))){
      assert !path.isCore();
      row.addAll(path.getRowData());
      }
    return new Path(row);
  }
  private static Path pathExternToInner(Path path){
    if(path.isPrimitive()){return path;}
    assert path.isCore();
    return path.setNewOuter(path.outerNumber()+2);
  }
  //%o_0%:{(Outer1::C1::Cn that)}
  private static Member buildPathAdapterInner(Path path) {
    //throw Assertions.codeNotReachable();
    ClassB cb= new ClassB(new Doc("%s",Collections.singletonList(path)),Doc.empty(),false,Collections.emptyList(),Collections.emptyList(),Stage.None);
    return new ClassB.NestedClass(Doc.empty(),"%o_0%",cb);
  }
  /*public static ast.ExpCore.ClassB sealConstructors(Program emptyP, ast.ExpCore.ClassB classB, List<Path> mapConstructors) {
    // TODO Auto-generated method stub
    return null;
  }*/
  /**Expands a map to include also the implemented versions of methods*/
  public static List<PathMxMx> expandMapMx(Program p, ast.ExpCore.ClassB classB, List<PathMxMx> mapMx) {
    //compute ct
    //for all methods m in path.ms->ms' that are of an interface in classB
    List<PathMxMx> mapMxOfInterface=new ArrayList<>();
    for(PathMxMx px : mapMx){
      ClassB cbi=Program.extractCBar(px.getPath().getCBar(),classB);
      if(cbi.isInterface()){mapMxOfInterface.add(px);}
    }
    if(mapMxOfInterface.isEmpty()){return mapMx;}
    ClassB ct=Configuration.typeSystem.typeExtraction(p,classB);
    Visitor v=new CloneVisitor(){

    };
    //find all paths pathi that implement path internally (hard?)
    //NO!!! sob, it can be that there is a class internal of a method body...
    //add pathi.ms->ms'  to the map
    throw Assertions.codeNotReachable();
  }
}

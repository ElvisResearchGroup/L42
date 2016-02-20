package coreVisitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import ast.ExpCore;
import ast.Ast.*;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.Functions;

 class _CollectPathsStar extends CloneVisitor{
  Set<Path> paths=new HashSet<Path>();
  //Path toFrom=null;
  public static Set<Path> of(ClassB e){
    _CollectPathsStar cp=new _CollectPathsStar();
    e.accept(cp);
    return cp.paths;
  }
  public ExpCore visit(Path s) {//invoked by visit classB header/super and local vardec
    if(!s.isPrimitive()){paths.add(s);}
    return s;
    }
  /*public ExpCore visit(ClassB cb){
    Path tmp=toFrom;
    Set<Path> tmpP=paths;
    if(tmp!=null){toFrom=toFrom.setNewOuter(toFrom.outerNumber()+1);}
    else{paths=new HashSet<Path>();}
    ExpCore e=super.visit(cb);
    if(tmp!=null){toFrom=tmp;}
    else{
      paths=Functions.remove1Outer(paths);
      tmpP.addAll(paths);     
    }
    toFrom=tmp;
    paths=tmpP;
    return e;
    }*/
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    //assert nc.getInner() instanceof ClassB;
    paths.addAll(CollectPaths0.of(nc.getInner()));
    List<ClassB> cb0=CollectClassBs0.of(nc.getInner());
    List<Path> pathsStar=new ArrayList<>();
    for(ClassB cb:cb0){
      pathsStar.addAll(_CollectPathsStar.of(cb));
    }
    Path toFrom=Path.parse("Outer0."+nc.getName());
    pathsStar=Map.of(p->From.fromP(p,toFrom), pathsStar);
    paths.addAll(pathsStar);
    return nc;
    }
/*  protected Header liftH(Header h) {
    if(!(h instanceof ConcreteHeader)){return super.liftH(h);}
    ConcreteHeader ch=(ConcreteHeader)h;
    for( FieldDec f:ch.getFs()){
      assert f.getT() instanceof NormType;
      NormType nt=(NormType)f.getT();
      paths.add(nt.getPath());
      }
    return super.liftH(h);
  }*/  
  public ClassB.MethodImplemented visit(ClassB.MethodImplemented mi){
    collectPathsFromEandCBs(mi.getInner());
    //throw Assertions.codeNotReachable(ToFormattedText.of(mi));
    //it can happen, depends on ordering of type extraction
    return mi;
    }
  public ClassB.MethodWithType visit(ClassB.MethodWithType mt){
    liftMT(mt.getMt());//to collect paths of mtype
    paths.addAll(CollectPaths0.of(mt));
    if(!mt.getInner().isPresent()){return mt;}
    collectPathsFromEandCBs(mt.getInner().get());
    return mt;
    }
  public void collectPathsFromEandCBs(ExpCore inner) {
    List<ClassB> cb0=CollectClassBs0.of(inner);
    List<Path> pathsStar=new ArrayList<>();
    for(ClassB cb:cb0){
      pathsStar.addAll(_CollectPathsStar.of(cb));
    }
    paths.addAll(Functions.remove1OuterAndPrimitives(pathsStar));
  }  
}

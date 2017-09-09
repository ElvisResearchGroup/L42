package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ast.Ast.C;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.L42F;
import ast.L42F.CD;
import ast.L42F.Cn;
import ast.MiniJ;
import coreVisitors.From;
import l42FVisitors.PropagatorVisitor;
import platformSpecific.javaTranslation.Resources;
import programReduction.Paths;
import programReduction.Program;
import tools.Assertions;

public class ClassTable {
  private Map<Integer,Element> map;
  public ClassTable(Map<Integer, Element> map) {
    this.map = map;
    }
  public boolean isCoherent(){
    List<Integer>invalid=new ArrayList<>();
    for(int i:map.keySet()){
      CD cdi = get(i).cd;
      if(cdi.getKind()==null){continue;}
      new PropagatorVisitor(){
        protected void liftCn(int cn) {
          if(Cn.cnFwd.getInner()>=cn){return;}
          if(!map.containsKey(cn)){
            invalid.add(cn);
            }
          }
        }.visit(cdi);
      }
    assert invalid.isEmpty():invalid;
    return true;
    }
  public String dbgNameOf(int index){
    if(index==Cn.cnAny.getInner()){return "Any";}
    if(index==Cn.cnVoid.getInner()){return "Void";}
    if(index==Cn.cnLibrary.getInner()){return "Library";}
    if(index==Cn.cnResource.getInner()){return "generated.Resource";}
    if(index==Cn.cnFwd.getInner()){return Fwd.class.getCanonicalName();}
    return get(index).cd.dbgName();
    }
  public String className(int index){//as in int
    if(index==Cn.cnAny.getInner()){return "Object";}
    if(index==Cn.cnVoid.getInner()){return Resources.Void.class.getCanonicalName();}
    if(index==Cn.cnLibrary.getInner()){return "Object";}
    if(index==Cn.cnResource.getInner()){return "generated.Resource";}
    if(index==Cn.cnFwd.getInner()){return Fwd.class.getCanonicalName();}
    return get(index).cd.className();
    }
  public String boxedClassName(int index){//as in Integer
    if(index==Cn.cnAny.getInner()){return "Object";}
    if(index==Cn.cnVoid.getInner()){return Resources.Void.class.getCanonicalName();}
    if(index==Cn.cnLibrary.getInner()){return "Object";}
    if(index==Cn.cnResource.getInner()){return "generated.Resource";}
    if(index==Cn.cnFwd.getInner()){return Fwd.class.getCanonicalName();}
    return get(index).cd.boxedClassName();
    }
  public String l42ClassName(int index){//as in MyNat (that can be optimized as an int but has more static methods)
    if(index==Cn.cnAny.getInner()){return Resources.Any.class.getCanonicalName();}
    if(index==Cn.cnVoid.getInner()){return Resources.Void.class.getCanonicalName();}
    if(index==Cn.cnLibrary.getInner()){return Resources.Library.class.getCanonicalName();}
    if(index==Cn.cnResource.getInner()){return "generated.Resource";}
    if(index==Cn.cnFwd.getInner()){return Fwd.class.getCanonicalName();}
    return get(index).cd.l42ClassName();
    }

  public Element _get(int index) {
    return map.get(index);
    }
  public Element get(int index) {
    Element res=_get(index);
    assert res!=null:""+index+this.isCoherent();
    return res;
    }
  public Set<Integer> keySet(){return map.keySet();}
  public String toString() {return L42FToString.visitCT(this);}
  public String toJString() {return MiniJToJava.of(this);}
  public String toDepJString() {
    StringBuilder res=new StringBuilder();
    map.values().stream().sorted((e1,e2)->e1.cd.getCn()-e2.cd.getCn())
      .filter(e->e.deps!=null)
      .map(e->e.cd.getCn()+"->"+e.deps.stream().sorted().collect(Collectors.toList()))
      .forEachOrdered(s->res.append(s+"\n"));
    return res.toString();
    }
  public static final ClassTable empty=new ClassTable(Collections.emptyMap());
  public static class Element{
    Element(Program p, CD cd) {this.p=p; this.cd = cd;}
    Program p;
    L42F.CD cd;
    Set<Integer>deps;
    public ExpCore.ClassB cachedSrc=null;
    void reCache(Program pView) {
      Path fromming = fromming(p.pop(),pView);
      cachedSrc=(ClassB) From.from(p.top(),fromming);
      }
    public Path fromming(Program p0,Program pView) {
      List<Object> pViewWay=pView.exploredWay();
      int pViewSize=pViewWay.size();
      pViewWay=cutWay(pViewWay);
      List<Object> p0WayAll=p0.exploredWay();
      List<Object> p0WayCut=cutWay(p0WayAll);
      int i=0;
      int min=Math.min(p0WayCut.size(),pViewWay.size());
      for(;i<min;i+=1) {
        if(!p0WayCut.get(i).equals(pViewWay.get(i))) {break;}
        }
      int n=pViewSize-i;
      assert n>=0;
      List<C> cs=new ArrayList<>();
      for(int j=i;j<p0WayAll.size();j+=1) {
        Object ci=p0WayAll.get(j);
        assert ci!=null;
        assert ci instanceof C;
        cs.add((C)ci);
        }
      Path fromming=Path.outer(n,cs);
    return fromming;
    }
    private static List<Object> cutWay(List<Object> way) {
      int cut=way.indexOf(null);
      if(cut==-1) {cut=way.size();}
      way=way.subList(0, cut);
      return way;
    }
    Element copy() {//we intentionally do not copy the old cache.
      Element res=new Element(p,cd);
      res.deps=deps;
      return res;
      }
    }
  public ClassTable growWith(Program p, Paths paths){
    List<Program>ps=programsOf(p,paths);
    ClassTable res=this;
    for(Program pi:ps){
      List<String> names = Loader.computeDbgNames(pi);
      res=res.plus(names,pi);
      }
    assert res.isCoherent();
    return res;
    }

  /*public ClassTable computeJavaForNulls() {
    ClassTable res = copy();
    for(Element e:res.map.values()){
      if (e.jCd==null && e.cd.getKind()!=null){
        e.jCd=L42FToMiniJ.of(res, e.cd);
        }
      }
    return res;
    }*/

  public ClassTable computeDeps() {
    ClassTable res = copy();
    for(Element e:res.map.values()){
      if (e.deps==null && e.cd.getKind()!=null){
        e.deps=CTDeps.of(res, e.cd);
        }
      }
    return res;
    }

  private ClassTable copy() {
    Map<Integer,Element> newMap=map.entrySet().stream()
      .collect(Collectors.toMap(Map.Entry::getKey,
        e -> e.getValue().copy()));
    ClassTable res=new ClassTable(newMap);
    return res;
    }

  private List<Program> programsOf(Program p, Paths paths) {
    if(paths.isEmpty()){return Collections.emptyList();}
    List<Program>res=new ArrayList<>();
    for(List<C> cs :paths.top()){
      Program pi=p.navigate(cs);
      res.add(pi);
      }
    Paths ppaths=paths.pop();
    if(ppaths.isEmpty()){return res;}
    res.addAll(programsOf(p.pop(),paths.pop()));
    return res;
    }
  public ClassTable plus(List<String> names,Program p){
    List<Element> res1 = NtoF.libToCDs(this,names,p);
    ClassTable res2=this;
    for(Element e:res1){res2=res2.plus(e);}
    return res2;
    }
  protected ClassTable plus(Element e) {
  HashMap<Integer,Element> newMap=new HashMap<>(map);
  newMap.put(e.cd.getCn(), e);
  return new ClassTable(newMap);
  }
public Set<Set<Integer>> listOfDeps() {
  Set<Set<Integer>> l=new HashSet<>();
  for(Element e:map.values()){
    Set<Integer> s=new HashSet<>();
    if(e.cd.getKind()==null){continue;}
    for(int i:e.deps){
      s.add(i);
      }
    l.add(s);
    }
  return l;
  }
}

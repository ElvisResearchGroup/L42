package newReduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.Ast.C;
import ast.Ast.Path;
import ast.L42F;
import ast.L42F.CD;
import ast.L42F.Cn;
import ast.MiniJ;
import programReduction.Paths;
import programReduction.Program;
import tools.Assertions;

public class ClassTable {
  private Map<Integer,Element> map;
  public ClassTable(Map<Integer, Element> map) {
    this.map = map;
    }
  public String dbgNameOf(int index){
    if(index==Cn.cnAny.getInner()){return "Any";}
    if(index==Cn.cnVoid.getInner()){return "Void";}
    if(index==Cn.cnLibrary.getInner()){return "Library";}
    if(index==Cn.cnResource.getInner()){return "Resource";}
    return get(index).cd.dbgName();
    }
  public String className(int index){
    if(index==Cn.cnAny.getInner()){return "Object";}
    if(index==Cn.cnVoid.getInner()){return "platformSpecific.javaTranslation.Resources.Void";}
    if(index==Cn.cnLibrary.getInner()){return "Object";}
    if(index==Cn.cnResource.getInner()){return "Resource";}
    return get(index).cd.className();
    }
  public String boxedClassName(int index){
    if(index==Cn.cnAny.getInner()){return "Object";}
    if(index==Cn.cnVoid.getInner()){return "platformSpecific.javaTranslation.Resources.Void";}
    if(index==Cn.cnLibrary.getInner()){return "Object";}
    if(index==Cn.cnResource.getInner()){return "Resource";}
    return get(index).cd.boxedClassName();
    }
  public String l42ClassName(int index){
    if(index==Cn.cnAny.getInner()){throw Assertions.codeNotReachable();}
    if(index==Cn.cnVoid.getInner()){throw Assertions.codeNotReachable();}
    if(index==Cn.cnLibrary.getInner()){throw Assertions.codeNotReachable();}
    if(index==Cn.cnResource.getInner()){return "Resource";}
    return get(index).cd.l42ClassName();
    }

  public Element get(int index) {
    Element res=map.get(index);
    assert res!=null:index;
    return res;
    }
  public Set<Integer> keySet(){return map.keySet();}
  public String toString() {return L42FToString.visitCT(this);}
  public String toJString() {return MiniJToJava.of(this);}
  public static final ClassTable empty=new ClassTable(Collections.emptyMap());
  static class Element{
    Element(CD cd) {this.cd = cd;}
    L42F.CD cd;
    MiniJ.CD jCd;
    }
  public static class NamesP{
    NamesP(List<String>names, Program p){this.names=names;this.p=p;}
    List<String>names;Program p;
    }
  public ClassTable growWith(List<String>names,Program p, Paths paths){
    List<NamesP>ps=programsOf(names,p,paths);
    ClassTable res=this;
    for(NamesP pi:ps){res=res.plus(pi.names,pi.p);}
    for(Element e:res.map.values()){
      if (e.jCd==null && e.cd.getKind()!=null){e.jCd=L42FToMiniJ.of(res, e.cd);}
      }
    return res;
    }
  private List<NamesP> programsOf(List<String>names,Program p, Paths paths) {
    if(paths.isEmpty()){return Collections.emptyList();}
    List<NamesP>res=new ArrayList<>();
    for(List<C> cs :paths.top()){
      Program pi=p.navigate(cs);
      res.add(new NamesP(names,pi));
      }
    Paths ppaths=paths.pop();
    if(ppaths.isEmpty()){return res;}
    List<String>namesPop=names.subList(0, names.size()-1);
    res.addAll(programsOf(namesPop,p.pop(),paths.pop()));
    return res;
    }
  public ClassTable plus(List<String> names,Program p){
    List<CD> res1 = NtoF.libToCDs(this,names,p);
    ClassTable res2=this;
    for(CD cd:res1){res2=res2.plus(cd);}
    return res2;
    }
  private ClassTable plus(CD cd) {
  assert !map.containsKey(cd.getCn()):"avoided before with 'avoidRepeat'?";
  if(map.containsKey(cd.getCn())){return this;}
  HashMap<Integer,Element> newMap=new HashMap<>(map);
  newMap.put(cd.getCn(), new Element(cd));
  return new ClassTable(newMap);
  }
}

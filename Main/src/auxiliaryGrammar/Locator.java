package auxiliaryGrammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import lombok.Data;
import lombok.NonNull;

public class Locator {
  public static enum Kind {
    Node, Nested, Method
  };
  
  final List<ClassB.Member> ms = new ArrayList<>();
  final List<Integer> indexes = new ArrayList<>();
  final List<ClassB> cbs = new ArrayList<>();
  Object annotation = null;
  public void setAnnotation(Object o){annotation=o;}
  public Object getAnnotation(){return annotation;}
  public List<ClassB> getCbs(){
    return this.cbs;
  }
  public Member getLastMember(){
    return this.ms.get(this.ms.size()-1);
  }
  public ClassB getLastCb(){
    return this.cbs.get(this.cbs.size()-1);
  }
  public String getLastName(){
    Member m=this.ms.get(this.ms.size()-1);
    return m.match(nc->nc.getName(), mi->mi.getS().getName(), mt->mt.getMs().getName());
  }
  public Kind kind() {
    int size = this.size();
    if (cbs.get(size - 1) != null) { return Kind.Node; }
    if (ms.get(size - 1) instanceof ClassB.NestedClass) { return Kind.Nested; }
    return Kind.Method;
  }
  
  public int size() {
    int size = ms.size();
    assert size == indexes.size();
    assert size == cbs.size();
    assert size==0 ||!cbs.subList(0, size - 1).contains(null) : cbs;
    return size;
  }
  
  public Locator copy() {
    Locator result = new Locator();
    result.ms.addAll(this.ms);
    result.indexes.addAll(this.indexes);
    result.cbs.addAll(this.cbs);
    return result;
  }
  
  public boolean moveInPath(Path path) {
    if (!this.cutUpTo(path.outerNumber())) { return false; }
    this.addCs(path.getCBar());
    return true;
  }
  public void addCsAndMember(List<String> cs,Member m) {
    auxAddCs(cs);
    this.cbs.add(null);
    this.indexes.add(0);
    this.ms.add(m);
    }
  public void addCs(List<String> cs) {
    auxAddCs(cs);
    assert !cbs.contains(null) : cbs;
    this.cbs.set(this.cbs.size() - 1, null);
    this.indexes.set(this.cbs.size()-1, 0);
  }
  private void auxAddCs(List<String> cs) {
    int size=this.size();
    if(size>0 && this.cbs.get(size-1)==null){
      assert this.indexes.get(size-1)==0;
      this.indexes.set(size-1,1);
      this.cbs.set(size-1,LocatorSupport.dumbCb);
    }
    for (String s : cs) {
      this.ms.add(new ClassB.NestedClass(ast.Ast.Doc.empty(), s, new ast.ExpCore.WalkBy(), null));
      this.indexes.add(1);
      this.cbs.add(LocatorSupport.dumbCb);
    }
  }
  public void toFormerNodeLocator(){
    int size=this.size();
    if(this.cbs.get(size-1)!=null){return;}
    assert this.cbs.get(size-1)==null;
    this.cbs.remove(size-1);
    this.indexes.remove(size-1);
    this.ms.remove(size-1);
  }
  public boolean cutUpTo(int outerNumber) {
    assert outerNumber >= 0;
    if (outerNumber == 0) { return true; }
    int size = this.size();
    if (size == 0) { return false; }
    int cutIndex = size - 1;
    if (this.cbs.get(cutIndex) == null) {//kind ==node
      cutIndex -= 1;
    }
    cutIndex -= outerNumber-1;
    if (cutIndex < 0) { return false; }
    this.ms.subList(cutIndex, size).clear();
    this.indexes.subList(cutIndex, size).clear();
    this.cbs.subList(cutIndex, size).clear();
    return true;
  }
  
  static class LocatorSupport {
    private static final ClassB dumbCb = new ClassB(Doc.empty(), Doc.empty(), false, Collections.emptyList(), Collections.emptyList());
  }
  
  public boolean prefixOf(Locator nl) {
    int size = nl.size();
    if (this.size() < size) { return false; }
    for (int i = 0; i < size; i++) {
      int indexC = this.indexes.get(i);
      int indexPos = nl.indexes.get(i);
      assert i!=size-1 || indexPos==0: ""+i+" "+size+" "+indexPos;
      if (i!=size-1 && indexC != indexPos) { return false; }
      Member ci = this.ms.get(i);
      Member nli = nl.ms.get(i);
      if (ci == nli) {
        continue;
      }
      if (ci.getClass() != nli.getClass()) { return false; }
      if (!(ci instanceof ClassB.NestedClass)) { return false; }
      String nci = ((ClassB.NestedClass) ci).getName();
      String nnli = ((ClassB.NestedClass) nli).getName();
      if (!nci.equals(nnli)) { return false; }
      //is ok to not check classBs?
    }
    return true;
  }
  
  public String toStringNoAnnotation() {
    return coreVisitors.PathAnnotateClass.computeComment(this.ms, this.indexes);
  }
  public String toString() {
    return toStringNoAnnotation()+ annotation;
  }
  public boolean equals(Object _that){
    if(this.getClass()!=_that.getClass()){return false;}
    Locator that=(Locator)_that;
    return this.toStringNoAnnotation().equals(that.toStringNoAnnotation());
  }
  public int hashCode(){return this.toString().hashCode();}
  public List<String> getClassNamesPath(){
    int size=this.size();
    //assert path.get(path.size()-1)==null;
    List<String> sPath=new ArrayList<>();
    for(Member m:this.ms){
      m.match(nc->sPath.add(nc.getName()), mi->sPath.add(null), mt->sPath.add(null));
    }
    if(size>0 && cbs.get(size-1)==null){
      sPath.remove(sPath.size()-1);
    }
    return sPath;
    }
  public void pushMember(ClassB.Member m) {
    assert !cbs.contains(null) : cbs;
    ms.add(m);
    indexes.add(0);
    cbs.add(null);
  }
  public void enterClassB(ClassB cb){
    int size=this.size();
    if(size==0){return;}
    assert this.cbs.get(size-1)==null;
    this.cbs.set(size-1,cb);
    this.indexes.set(size-1,this.indexes.get(size-1)+1);
    assert !cbs.contains(null) : cbs;
  }
  public void exitClassB(){
    int size=this.size();
    if(size==0){return;}
    assert !cbs.contains(null) : cbs;
    //assert this.cbs.get(size-1)!=null;
    this.cbs.set(size-1,null);
  }
}
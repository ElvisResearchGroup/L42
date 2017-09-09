package ast;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import ast.Expression;
import ast.Ast.*;
import ast.ExpCore.Block;
import ast.ExpCore.ClassB;
import ast.ExpCore.MCall;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import coreVisitors.PropagatorVisitor;

public interface ExpCore extends Serializable{
  <T> T accept(coreVisitors.Visitor<T> v);
  default String toS(){return sugarVisitors.ToFormattedText.of(this);}
  @Value @EqualsAndHashCode(exclude = {"typeRec","typeOut","p"}) @ToString(exclude = {"typeRec","typeOut","p"})   @Wither
  public static class MCall implements ExpCore,HasPos<MCall>, WithInner<MCall>{
    ExpCore inner;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    Position p;
    Type typeRec;
    Type typeOut;
    public MCall withEsi(int i,ExpCore ei){
      List<ExpCore> es2=new ArrayList<>(es);
      es2.set(i,ei);
      return this.withEs(es2);
      }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") @Wither public static class UpdateVar implements ExpCore,HasPos<UpdateVar>, WithInner<UpdateVar>{
  ExpCore inner;
  String var;
  Doc doc;
  Position p;
  @Override public <T> T accept(coreVisitors.Visitor<T> v) {
    return v.visit(this);
  }
}


  @Value @Wither @EqualsAndHashCode(exclude = "p")
  public static class X implements ExpCore, Ast.Atom, HasPos<X> {
    Position p;
    String inner;
    public String toString() {
      return this.inner;
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @EqualsAndHashCode(exclude = {"typeOut","p"}) @ToString(exclude = {"typeOut","p"}) @Wither public static class Block implements ExpCore,HasPos<Block>,WithInner<Block> {
    Doc doc;
    List<Dec> decs;
    ExpCore inner;
    List<On> ons;
    Position p;
    Type typeOut;
    public ExpCore getE(){return inner;}
    public Block withE(ExpCore e){return this.withInner(e);}
    public Block withDeci(int i,Dec di){
      List<Dec> decs2=new ArrayList<>(decs);
      decs2.set(i,di);
      return this.withDecs(decs2);
      }
    public Block withDeci(int i,On oi){
    List<On> ons2=new ArrayList<>(ons);
    ons2.set(i,oi);
    return this.withOns(ons2);
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
    @Value @Wither public static class Dec implements WithInner<Dec>, Serializable{
      boolean isVar;
      Type _t;
      String x;
      ExpCore inner;
      public Optional<Type> getT(){return Optional.ofNullable(_t);}
    }
    public List<String> domDecs() {
      List<String> dom = new java.util.ArrayList<String>();
      for (Dec d : this.decs) {
        dom.add(d.x);
      }
      return dom;
    }

    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class On implements HasPos<On>, WithInner<On>, Serializable{
      SignalKind kind;
      String x;
      Type t;
      ExpCore inner;
      Position p;
      public ExpCore getE(){return inner;}
      public On withE(ExpCore e){return this.withInner(e);}

    }
  }


  @Value @Wither @EqualsAndHashCode(exclude = {"p","phase","uniqueId"})  /*@ToString(exclude ="p")*/ public static class ClassB implements ExpCore, Ast.Atom,HasPos<ClassB> {

    public ClassB(Doc doc1, boolean isInterface, List<Type> supertypes, List<Member> ms,Position p, Phase phase, int uniqueId) {
      this.doc1 = doc1;
      this.isInterface = isInterface;
      this.supertypes = supertypes;
      this.ms = ms;
      this.p=p;
      this.phase=phase;
      this.uniqueId=uniqueId;
      assert isConsistent();
      }//lombock fails me here :-(
    Doc doc1;
    boolean isInterface;
    List<Ast.Type> supertypes;
    List<Member> ms;
    Position p;
    Phase phase;
    int uniqueId;
    // In the future, we may remove members and add mwts and ns, now we add delegation constructors/getters
    public ClassB(Doc doc1, boolean isInterface, List<Type> supertypes, List<ClassB.MethodWithType> mwts, List<ClassB.NestedClass> ns,Position p, Phase phase, int uniqueId) {
      this(doc1,isInterface,supertypes,java.util.stream.Stream.concat(mwts.stream(),ns.stream()).collect(Collectors.toList()),p,phase,uniqueId);
      }
    public List<ClassB.MethodWithType> mwts(){
      return ms.stream().filter(e->e instanceof ClassB.MethodWithType)
        .map(e->(ClassB.MethodWithType)e)
        .collect(Collectors.toList());
      }
    public List<ClassB.NestedClass> ns(){
      return ms.stream().filter(e->e instanceof ClassB.NestedClass)
        .map(e->(ClassB.NestedClass)e)
        .collect(Collectors.toList());
      }
    public String toString() {return sugarVisitors.ToFormattedText.of(this);}
    public boolean isConsistent() { return _Aux.isConsistent(this);}
    public ClassB withMember(Member m) {return _Aux.withMember(this, m);}
    public Member _getMember(ast.Ast.MethodSelector ms) {return _Aux._getMember(this, ms);}
    public ClassB onClassNavigateToPathAndDo(List<Ast.C>cs,Function<ClassB,ClassB>op){return _Aux.onClassNavigateToPathAndDo(this, cs, op);}
    public ClassB onNestedNavigateToPathAndDo(List<Ast.C>cs,Function<ClassB.NestedClass,Optional<ClassB.NestedClass>>op){return _Aux.onNestedNavigateToPathAndDo(this, cs, op);}
    public ExpCore.ClassB.NestedClass getNested(List<Ast.C>cs){return _Aux.getNested(this, cs);}
    public List<ExpCore.ClassB.NestedClass> getNestedList(List<Ast.C>cs){return _Aux.getNestedList(this, cs);}
    public ExpCore.ClassB getClassB(List<Ast.C>cs){return _Aux.getClassB(this, cs);}

    public static ExpCore.ClassB docClass(Doc d){return new ClassB(d,false,Collections.emptyList(),Collections.emptyList(),Position.noInfo,Phase.Typed,-2);}

    public static ExpCore.ClassB membersClass(List<Member> ms,Position pos,Phase phase){return new ClassB(Doc.empty(),false,Collections.emptyList(),ms,pos,phase,-2);}

    public List<Path> getSuperPaths(){
      return this.getSupertypes().stream()
        .map(t->t.getPath())
        .collect(Collectors.toList());
      }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {return v.visit(this);}
    public static enum Phase{None,Norm,Typed,Coherent;
    public Phase acc(Phase that){
      if(this.subtypeEq(that)){return that;}
      return this;
      }
    public boolean subtypeEq(Phase that){
      return this.ordinal()>=that.ordinal();
      }
    }
    public interface Member extends Serializable, HasPos<Member>, WithInner<Member> {
      <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt);
      }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p")public static class NestedClass implements Member {
      @NonNull Doc doc;
      @NonNull Ast.C name;
      @NonNull ExpCore inner;
      Position p;
      public ExpCore getE(){return inner;}
      public NestedClass withE(ExpCore e) {return this.withInner(e);}
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return nc.apply(this);
        }
      }
    @Value @Wither @EqualsAndHashCode(exclude = {"p"/*,"mt"*/}) @ToString(exclude = {"p"/*,"mt"*/})public static class MethodImplemented implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector s;
      @NonNull ExpCore inner;
      Position p;
      public ExpCore getE(){return inner;}
      public MethodImplemented withE(ExpCore e) {return this.withInner(e);}
      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mi.apply(this);
        }
      }
    @Value @Wither @EqualsAndHashCode(exclude = "p") @ToString(exclude = "p") public static class MethodWithType implements Member {
      @NonNull Doc doc;
      @NonNull MethodSelector ms;
      @NonNull MethodType mt;
      ExpCore _inner;
      Position p;
      public ExpCore getInner(){ assert _inner!=null; return _inner;}//and boom if there is not
      public MethodWithType withInner(ExpCore e) {return this.with_inner(e);}

      public <T> T match(Function<NestedClass, T> nc, Function<MethodImplemented, T> mi, Function<MethodWithType, T> mt) {
        return mt.apply(this);
        }
      }
    }

  @Value public static class _void implements ExpCore, Ast.Atom {
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
      }
    }
  @Value @Wither @EqualsAndHashCode(exclude = "p")
  public static class EPath implements ExpCore,HasPos<EPath>,Atom{
    Position p;
    Ast.Path inner;
    public String toString(){return this.getInner().toString();}
    public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
      }
  public static EPath wrap(Ast.Path p){
    return new EPath(Position.noInfo,p);
    }
  }

  @Value public static class WalkBy implements ExpCore {
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
      }
    }

  @Value @Wither public static class Using implements ExpCore, WithInner<Using> {
    Path path;
    MethodSelector s;
    Doc doc;
    List<ExpCore> es;
    ExpCore inner;
    public Using(Path path,MethodSelector s,Doc doc,List<ExpCore> es,ExpCore inner){
      this.path=path;this.s=s;this.doc=doc;this.es=es;this.inner=inner;
      assert s.getNames().size()==es.size();
      }
    public Using withEsi(int i,ExpCore ei){
    List<ExpCore> es2=new ArrayList<>(es);
    es2.set(i,ei);
    return this.withEs(es2);
    }
    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither
  @EqualsAndHashCode(exclude = {"typeOut","typeIn"}) @ToString(exclude = {"typeOut","typeIn"})
  public static class Signal implements ExpCore, WithInner<Signal>{
    SignalKind kind;
    ExpCore inner;
    Type typeOut;
    Type typeIn;

    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }

  @Value @Wither public static class Loop implements ExpCore, WithInner<Loop> {
    ExpCore inner;

    @Override public <T> T accept(coreVisitors.Visitor<T> v) {
      return v.visit(this);
    }
  }
  interface WithInner<T>{
    ExpCore getInner(); T withInner(ExpCore e);
    }

  }
class _Aux{
  static ClassB wrapCast(ExpCore e){
    try{return (ClassB)e;}
    catch(ClassCastException cce){
      throw new ErrorMessage.PathMetaOrNonExistant(true,null,null,null,null);
      }
    }
  static void checkIndex(int index){
    if (index==-1){
      throw new ErrorMessage.PathMetaOrNonExistant(false,null,null,null,null);
      }
    }
  static ClassB onNestedNavigateToPathAndDo(ClassB cb,List<Ast.C>cs,Function<NestedClass,Optional<NestedClass>>op){
    assert !cs.isEmpty();
    assert cb!=null;
    List<Member> newMs=new ArrayList<>(cb.getMs());
    Ast.C nName=cs.get(0);
    int index=getIndex(newMs, nName);
    checkIndex(index);
    NestedClass nc=(NestedClass)newMs.get(index);
    if(cs.size()>1){
      nc=nc.withInner(onNestedNavigateToPathAndDo(wrapCast(nc.getInner()),cs.subList(1,cs.size()),op));
      newMs.set(index, nc);
      return cb.withMs(newMs);
      }
    assert cs.size()==1;
    Optional<NestedClass> optNc = op.apply(nc);
    if(optNc.isPresent()){
      newMs.set(index,optNc.get());
      }
    else{newMs.remove(index);}
    return cb.withMs(newMs);
    }

  static ClassB onClassNavigateToPathAndDo(ClassB cb,List<Ast.C>cs,Function<ClassB,ClassB>op){
    if(cs.isEmpty()){return op.apply(cb);}
    List<Member> newMs=new ArrayList<>(cb.getMs());
    Ast.C nName=cs.get(0);
    int index=getIndex(newMs, nName);
    checkIndex(index);
    NestedClass nc=(NestedClass)newMs.get(index);
    if(cs.size()>1){
      nc=nc.withInner(onClassNavigateToPathAndDo(wrapCast(nc.getInner()),cs.subList(1,cs.size()),op));
      newMs.set(index, nc);
      return cb.withMs(newMs);
      }
    assert cs.size()==1;
    ClassB newCb = op.apply(wrapCast(nc.getInner()));
    newMs.set(index, nc.withInner(newCb));
    return cb.withMs(newMs);
    }

  static int getIndex(List<ExpCore.ClassB.Member> map, ast.Ast.MethodSelector elem){
    int i=-1;for(ExpCore.ClassB.Member m: map){i++;
    if(m.match(nc->false,mi->mi.getS().equals(elem) ,mt->mt.getMs().equals(elem))){return i;}
      }
    return -1;
    }

  static int getIndex(List<ExpCore.ClassB.Member> map, Ast.C elem){
    int i=-1;for(ExpCore.ClassB.Member m: map){i++;
      if(m.match(nc->nc.getName().equals(elem), mi->false, mt->false)){return i;}
      }
    return -1;
    }

  static int getIndex(List<ExpCore.ClassB.Member> map, ExpCore.ClassB.Member elem){
    return elem.match(nc->getIndex(map,nc.getName()), mi->getIndex(map,mi.getS()),mt->getIndex(map,mt.getMs()));
    }

  static ExpCore.ClassB.NestedClass getNested(ExpCore.ClassB cb, List<Ast.C>cs){
    assert !cs.isEmpty();
    Ast.C nName=cs.get(0);
    int index=getIndex(cb.getMs(), nName);
    checkIndex(index);
    NestedClass nc=(NestedClass)cb.getMs().get(index);
    if(cs.size()==1){return nc;}
    return getNested(wrapCast(nc.getInner()),cs.subList(1, cs.size()));
    }

  static List<ExpCore.ClassB.NestedClass> getNestedList(ExpCore.ClassB cb, List<Ast.C>cs){
    assert !cs.isEmpty();
    List<ExpCore.ClassB.NestedClass> result=new ArrayList<>();
    getNestedList(cb,cs,result);
    return result;
    }

  static void getNestedList(ExpCore.ClassB cb, List<Ast.C>cs,List<ExpCore.ClassB.NestedClass> result){
  Ast.C nName=cs.get(0);
    int index=getIndex(cb.getMs(), nName);
    checkIndex(index);
    NestedClass nc=(NestedClass)cb.getMs().get(index);
    result.add(nc);
    if(cs.size()!=1){
      getNestedList(wrapCast(nc.getInner()),cs.subList(1, cs.size()),result);
      }
    }

  static ExpCore.ClassB getClassB(ExpCore.ClassB cb, List<Ast.C>cs){
    if(cs.isEmpty()){return cb;}
    return wrapCast(getNested(cb,cs).getInner());
    }

  static ClassB withMember(ClassB cb,Member m) {
    assert cb.isConsistent();
    List<Member> newMs = new java.util.ArrayList<>(cb.getMs());
    int index=_Aux.getIndex(newMs,m);
    if(index==-1){
      if(m instanceof NestedClass){newMs.add(m);}
      else {newMs.add(0,m);}
      }
    else {newMs.set(index, m);}
    ClassB result = cb.withMs(newMs);
    return result;
    }
  static ClassB.Member _getMember(ClassB cb,ast.Ast.MethodSelector ms) {
    assert cb.isConsistent();
    int index=_Aux.getIndex(cb.getMs(),ms);
    if(index==-1){return null;}
    return cb.getMs().get(index);
    }

  static boolean isConsistent(ClassB cb) {
    assert cb.getUniqueId()!=0;
    assert cb.getUniqueId()==-2 ||cb.getUniqueId()>0 ||cb.getPhase()==Phase.None;
    int countWalkBy = 0;
    HashSet<String> keys = new HashSet<String>();
    for (Member m : cb.getMs()) {
      if (m instanceof MethodWithType) {
        MethodWithType mwt = (MethodWithType) m;
        String key = mwt.getMs().toString();
        assert !keys.contains(key);
        keys.add(key);
        assert mwt.getMt().isRefine() || cb.getP().containsAll(mwt.getP());
        //assert mwt.getMt().getTDocs().size() == mwt.getMt().getTs().size();
        }
      if (m instanceof NestedClass) {
        NestedClass nc = (NestedClass) m;
        String key = nc.getName().toString();
        assert !keys.contains(key);
        keys.add(key);
        if (nc.getInner() instanceof ExpCore.WalkBy) {
          countWalkBy += 1;
          }
        assert cb.getP().containsAll(nc.getP());
        }
      if (m instanceof MethodImplemented) {
        MethodImplemented mi = (MethodImplemented) m;
        String key = mi.getS().toString();
        assert !keys.contains(key);
        keys.add(key);
        }
      }
    //TODO: re enable with new TS/reduction
    //assert  (cb.getPhase()==ast.ExpCore.ClassB.Phase.None && cb.getUniqueId().isEmpty())
    //     || ((cb.getPhase()!=ast.ExpCore.ClassB.Phase.None && !cb.getUniqueId().isEmpty()) );
    assert countWalkBy <= 1 : cb;

    if(cb.getPhase()==Phase.Typed ||cb.getPhase()==Phase.Coherent) {
      int[]find= {0};
      class T extends PropagatorVisitor{
        protected void liftDec(Block.Dec f) {
          if(!f.getT().isPresent()) {find[0]++;}
          super.liftDec(f);
        }
      };
      cb.accept(new T());
      assert find[0]==0;
      }
    return true;
    }
  }
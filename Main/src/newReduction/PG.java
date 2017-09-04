package newReduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.L42F;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.Ast.Type;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.EPath;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import ast.L42F.Call;
import ast.L42F.Cn;
import ast.L42F.D;
import ast.L42F.E;
import ast.L42F.K;
import ast.L42F.M;
import ast.L42F.T;
import ast.L42F.TX;
import auxiliaryGrammar.Functions;
import coreVisitors.FreeVariables;
import coreVisitors.From;
import coreVisitors.Visitor;
import facade.L42;
import l42FVisitors.CloneVisitor;
import newTypeSystem.GuessTypeCore;
import newTypeSystem.GuessTypeCore.G;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import newTypeSystem.TypeManipulation;
import programReduction.Program;
import tools.Assertions;

class PG implements Visitor<E>{
  Program p;
  G gamma;
  List<Program> ps;
  PG(Program p,G gamma,List<Program> ps){this.p=p;this.gamma=gamma;this.ps=ps;}
  public static M header(boolean isInterface,Program p,MethodWithType mwt){
    MethodType mt=mwt.getMt();

    List<TX>ts=new ArrayList<>();
    if(mt.getMdf()!=Mdf.Class || isInterface){
      T t = new T(mt.getMdf(),PG.liftP(p,Path.outer(0)));
      ts.add(new TX(t,"this"));
      }
    {int i=-1;for(String n : mwt.getMs().getNames()){i+=1;
      Ast.Type t=mt.getTs().get(i);
      ts.add(new TX(PG.liftT(p,t),n));
    }}
    return new M(mt.isRefine(),PG.liftT(p,mt.getReturnType()),mwt.getMs(),ts,L42F.SimpleBody.Empty);
  }

  public static E body(Program p, MethodWithType mwt,List<Program> ps) {
  PG pg=new PG(p,G.of(GuessTypeCore.mapForMwt(mwt)),ps);
  E res=mwt.getInner().accept(pg);
  return res;
  }

  public static int liftP(Program p,Ast.Path path){
    if(path.isCore()) {
      ClassB cb=p.extractClassB(path);
      return cb.getUniqueId();
      }
    if(path==Path.Any()) {return Cn.cnAny.getInner();}
    if(path==Path.Void()) {return Cn.cnVoid.getInner();}
    if(path==Path.Library()) {return Cn.cnLibrary.getInner();}
    throw Assertions.codeNotReachable();
    }
  public static T liftT(Program p,Ast.Type t){
    return new T(t.getMdf(),liftP(p,t.getPath()));
    }
  public static ExpCore.Block blockX(Ast.Type t,String x, ExpCore e,ExpCore e0,Type expectedT){
    Dec d=new Dec(false,Optional.of(t),x,e);
    return new ExpCore.Block(Doc.empty(),Collections.singletonList(d), e0,Collections.emptyList(), Position.noInfo,expectedT);
    }

@Override
public E visit(EPath s) {
  return new L42F.Cn(PG.liftP(p,s.getInner()));
  }
@Override
public E visit(X s) {
  return new L42F.X(s.getInner());
}
@Override
public E visit(_void s) {
  return new L42F._void();
}
@Override
public E visit(WalkBy s) {
  throw Assertions.codeNotReachable();
}
@Override
public E visit(Signal s) {
  if(s.getInner() instanceof X){
    String x = ((X)s.getInner()).getInner();
    T t = liftT(p,s.getTypeOut());
    return new L42F.Throw(s.getKind(), x,t);
    }
  String x = Functions.freshName("throwX",L42.usedNames);
  Signal sx = s.withInner(new X(Position.noInfo,x));
  return blockX(s.getTypeIn(),x, s.getInner(),sx,s.getTypeOut()).accept(this);
  }

@Override
public E visit(ClassB s) {
  MethodSelector ms=new MethodSelector("LoadLib_"+s.getUniqueId(),-1,Collections.emptyList());
  Call call=new Call(Cn.cnResource.getInner(),ms,Collections.emptyList());
  ps.add(p.evilPush(s));
  return call;
  }
@Override
public E visit(Loop s) {
  return new L42F.Loop(s.getInner().accept(this));
  }
@Override
public E visit(UpdateVar s) {
  if(s.getInner() instanceof X){
    String x = ((X)s.getInner()).getInner();
    return new L42F.Update(s.getVar(),x);
    }
  String x = Functions.freshName("updateX",L42.usedNames);
  UpdateVar sx = s.withInner(new X(Position.noInfo,x));
  return blockX(gamma._g(s.getVar()),x, s.getInner(),sx,Type.immVoid).accept(this);
  }

@Override
public E visit(MCall s) {
if(!(s.getInner() instanceof X)) {return visitReceiver(s);}
int i=0;
for(ExpCore ei:s.getEs()) {
  if (ei instanceof X) {i+=1;}
  else{break;}
}
//i is the first non X
if(i!=s.getEs().size()) {return visitParameter(i, s);}
return visitBase(s);
}
private E visitBase(MCall s) {
  ClassB cb=p.extractClassB(s.getTypeRec().getPath());
  MethodWithType mwt=(MethodWithType)cb._getMember(s.getS());
  boolean isInterface=cb.isInterface();
  boolean isClass=mwt.getMt().getMdf()==Mdf.Class;
  boolean isAbs=!mwt.get_inner().isPresent();
  List<String> ps=new ArrayList<>();
  MethodSelector ms=mwt.getMs();
  if(isInterface || !isClass) {
    ps.add(((X)s.getInner()).getInner());
    }
  if(isClass && !isInterface && isAbs && !TypeManipulation.fwd_or_fwdP_in(mwt.getMt().getTs())) {
    ms=msOptimizedNew(ms);
    }
  for(ExpCore ei:s.getEs()) {ps.add(((X)ei).getInner());}
  return new Call(cb.getUniqueId(),ms,ps);
  }
private E visitReceiver(MCall s) {
  String x = Functions.freshName("receiverX",L42.usedNames);
  MCall sx = s.withInner(new X(Position.noInfo,x));
  return blockX(s.getTypeRec(),x, s.getInner(),sx,s.getTypeOut()).accept(this);
  }
private E visitParameter(int i,MCall s) {
  String x = Functions.freshName("parX",L42.usedNames);
  MCall sx = s.withEsi(i,new X(Position.noInfo,x));
  ClassB cb=p.extractClassB(s.getTypeRec().getPath());
  MethodWithType mwt=(MethodWithType)cb._getMember(s.getS());
  Type ti=mwt.getMt().getTs().get(i);
  ti=From.fromT(ti, s.getTypeRec().getPath());
  return blockX(ti,x, s.getEs().get(i),sx,s.getTypeOut()).accept(this);
  }
private E visitParameter(int i,List<Type> lt,Using s) {
  String x = Functions.freshName("parX",L42.usedNames);
  Using sx = s.withEsi(i,new X(Position.noInfo,x));
  Type ti=lt.get(i+1);
  return blockX(ti,x, s.getEs().get(i),sx,lt.get(0)).accept(this);
  }
private E visitBase(Using s) {
  List<String> ps=new ArrayList<>();
  for(ExpCore ei:s.getEs()) {ps.add(((X)ei).getInner());}
  E e=s.getInner().accept(this);
  Doc doc=p.extractClassB(s.getPath()).getDoc1();
  UsingInfo ui=new UsingInfo(p, s);
  return new L42F.Use(
          doc,ui,
          s.getS(),ps,e);
  }

@Override
public E visit(Using s) {
  List<Type> lt;try{lt = platformSpecific.fakeInternet.OnLineCode.pluginType(p, s);}
  catch(UsingInfo.NonExistantMethod nem){throw new Error(nem);}
  int i=0;
  for(ExpCore ei:s.getEs()) {
    if (ei instanceof X) {i+=1;}
    else{break;}
    }
  //i is the first non X
  if(i!=s.getEs().size()) {
    return visitParameter(i,lt, s);
    }
  return visitBase(s);
  }
@Override
public E visit(Block s) {
  PG pg=this.plusDs(s.getDecs());
  List<D>ds=tools.Map.of(pg::visitD,s.getDecs());
  List<K>ks=tools.Map.of(pg::visitK,s.getOns());
  E e=s.getInner().accept(pg);
  Set<String> fv = FreeVariables.of(s.getDecs());
  return new L42F.Block(fwdFix(fv,ds), ks, e, PG.liftT(p,s.getTypeOut()));
}
static MethodSelector msOptimizedNew(MethodSelector ms) {
  return ms.withName("New_"+ms.getName());
}
private List<D> fwdFix(Set<String> fv,List<D>ds){
  if(ds.stream().allMatch(d->!fv.contains(d.getX()))){
    return ds;
    }
  //a block actually using placeholders
  Map<String,String> map=new HashMap<>();
  Collections.reverse(ds);
  List<D> dPrimes=new ArrayList<>();//e'i..e'n
  Set<String>usedAsFwd=new HashSet<>();
  for(D d:ds) {//from the bottom
    //compute all xiPrime and eiPrime
    L42.usedNames.add(d.getX());
    map.put(d.getX(),Functions.freshName(d.getX(), L42.usedNames));
    dPrimes.add(d.withE(ePrimei(d.getE(),map,usedAsFwd)));
    }
  Collections.reverse(ds);
  Collections.reverse(dPrimes);
  //all fwdGen
  Stream<D> acc=ds.stream().flatMap(d->fwdGen(d,map.get(d.getX()),usedAsFwd));
  acc=Stream.concat(acc,dPrimes.stream().flatMap(d->fixedXi(d,map.get(d.getX()),usedAsFwd)));
      //all normal dec+optional resourceFix
  return acc.collect(Collectors.toList());
  }
private Stream<D> fwdGen(D d, String xPrime, Set<String> xs){
  if(!xs.contains(xPrime)){return Stream.of();}
  Call e=new Call(d.getT().getCn(),new MethodSelector("NewFwd",-1,Collections.emptyList()),Collections.emptyList());
  return Stream.of(new D(false,d.getT(),xPrime,e));
  }
private PG plusDs(List<ExpCore.Block.Dec>ds){
  return new PG(p,this.gamma.addGuessing(p, ds),ps);
  }
private D visitD(ExpCore.Block.Dec d){
  return new D(d.isVar(),PG.liftT(p,d.getT().get()),d.getX(),d.getInner().accept(this));
  }
private K visitK(ExpCore.Block.On k){
  PG pg=new PG(p,gamma.addTx(k.getX(),k.getT()),ps);
  return new K(k.getKind(),PG.liftT(p,k.getT()),k.getX(),k.getInner().accept(pg));
  }
private E ePrimei(E ei,Map<String,String>map,Set<String>usedAsFwd) {
  //ex'i=exi[xi..xn=x'i..x'n]
  return ei.accept(new CloneVisitor() {
    protected String liftX(String s) {
      String mapped=map.get(s);
      if(mapped==null) {return s;}
      usedAsFwd.add(mapped);
      return mapped;
      }
    });
  }
private Stream<D> fixedXi(D di,String xPrimei,Set<String>xs){
  if(!xs.contains(xPrimei)) {return Stream.of(di);}
  E fixE=new Call(Cn.cnFwd.getInner(),new MethodSelector("Fix", -1, Collections.emptyList()),Arrays.asList(xPrimei,di.getX()));
  D dPrime=new D(false,T.immVoid,Functions.freshName("unused", L42.usedNames),fixE);
  return Stream.of(di,dPrime);
}
}
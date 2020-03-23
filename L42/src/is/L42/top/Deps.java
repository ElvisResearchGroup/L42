package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.uniqueWrap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.ThrowKind;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.P.NCs;
import is.L42.generated.S;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;
import is.L42.visitors.PropagatorCollectorVisitor;


public class Deps{
  ArrayList<P.NCs> typePs=new ArrayList<>();
  ArrayList<P.NCs> cohePs=new ArrayList<>();
  ArrayList<P.NCs> metaCohePs=new ArrayList<>();
  ArrayList<P.NCs> watched=new ArrayList<>();
  ArrayList<PathSel> usedMethods=new ArrayList<>();
  ArrayList<P.NCs> hiddenSupertypes=new ArrayList<>();
  public static P.NCs _publicRoot(P.NCs pi){
    var cs=pi.cs();
    var csCut=L(cs.stream().takeWhile(c->!c.hasUniqueNum()));
    if(cs.size()==csCut.size()){return null;}
    return pi.withCs(csCut);
    }
  void addP(P p){
    if(!p.isNCs()){return;}
    var pi=p.toNCs();
    typePs.add(pi);
    pi=_publicRoot(pi);
    if(pi==null){return;}
    typePs.add(pi);
    watched.add(pi);
    }
  public Info toInfo(){
    for(var pi:typePs){
      var cs=pi.cs();
      var csCut=L(cs.stream().takeWhile(c->!c.hasUniqueNum()));
      if(cs.size()==csCut.size()){continue;}
      watched.add(pi.withCs(csCut));
      }
    watched.removeAll(L(P.pThis0));
    return new Info(false,
      /*typeDep*/ uniqueWrap(typePs),
      /*coherentDep*/ uniqueWrap(cohePs),
      /*metaCoherentDep*/ uniqueWrap(metaCohePs),
      /*watched*/ uniqueWrap(watched),
      /*usedMethods*/ uniqueWrap(usedMethods),
      /*hiddenSupertypes*/ uniqueWrap(hiddenSupertypes),
      L(), false, "", L(), -1);    
    }
  private final PropagatorCollectorVisitor base=new PropagatorCollectorVisitor(){
    @Override public void visitP(P p){addP(p);}
    };
  public Deps collectDocs(List<Doc> docs){
    base.visitDocs(docs);
    return this;
    }
  public Deps collectTs(List<T> ts){
    base.visitTs(ts);
    return this;
    }    
  public Deps collectDeps(Program p0, List<MWT> mwts){
    var deps=new DepsV(p0);
    //TODO: we had addPublicRoots(cohePs); but I think it was wrong, it would limit the sum interface+class if the class have a non watched private nested class (that would disapper otherwise...)
    for(var m:mwts){deps.of(m);}
    return this;
    }
  public Deps collectDepsNCs(Program p0, List<NC> ncs){
    var deps=new DepsV(p0);
    for(var m:ncs){
      deps.visitDocs(m.docs());
      if(m.key().hasUniqueNum()){deps.of(m.l());}      
      }    
    return this;
    }
  public Deps collectDepsE(Program p0,Core.E e) {
    var deps=new DepsV(p0);
    deps.of(e.visitable());
    return this;
    }
  public static void collectRefined(Program p, ArrayList<S> refined) {
    for(T t:p.topCore().ts()){
      LL ll=p.of(t.p(),p.topCore().poss());
      if(ll.isFullL()){
        for(var m:((Full.L)ll).ms()){
          //if(m.key().hasUniqueNum()){continue;}
          if(m.key() instanceof S){refined.add((S)m.key());} 
          }
        }
      else{
        for(var m:((Core.L)ll).mwts()){
          //if(m.key().hasUniqueNum()){continue;}
          refined.add(m.key());
          }
        }
      }
    }
  public static void skipAct(P.NCs pi,List<C> cs,L l,Consumer<P.NCs>act){
    var tmp=_skipThis0(pi,cs,l);
    if(tmp!=null){act.accept(tmp);}
    }
  private static P.NCs _skipThis0(P.NCs pi,List<C> cs,L l){
    if(pi.n()<=cs.size()){
      var cs0=cs.subList(0, cs.size()-pi.n());
      cs0=merge(cs0,pi.cs());
      Program.flat(l).of(P.of(0,cs0),l.poss());//propagate errors is path is not existent
      return null;
      }
    return pi.withN(pi.n()-(cs.size()+1));
    }
  public class DepsV extends Accumulate.WithG<ArrayList<P.NCs>>{
    Program p0;DepsV(Program p0){this.p0=p0;}
    public ArrayList<P.NCs> empty(){return typePs;}
    @Override public void visitL(Full.L l){throw bug();}
    @Override public void visitL(Core.L l){
      l.visitInnerLNoPrivate((li,csi)->this.innerVisitL(l,li,csi));
      }
    private void innerVisitL(L l,L li,List<C> csi){
      for(var p:li.info().typeDep()){skipAct(p, csi, l,typePs::add);}
      for(var p:li.info().coherentDep()){skipAct(p, csi, l,metaCohePs::add);}
      for(var p:li.info().metaCoherentDep()){skipAct(p, csi, l,metaCohePs::add);}
      for(var p:li.info().watched()){skipAct(p, csi, l,watched::add);}
      for(var pathSel:li.info().usedMethods()){
        var p=pathSel.p().toNCs();
        skipAct(p, csi, l,pi->usedMethods.add(pathSel.withP(pi)));
        }
      for(var p:li.info().hiddenSupertypes()){skipAct(p, csi, l,hiddenSupertypes::add);}
      for(var t:li.ts()){skipAct(t.p().toNCs(), csi, l,hiddenSupertypes::add);}
      }
    @Override public void visitP(P p){addP(p);}
    @Override public void visitMCall(Core.MCall mc){
      super.visitMCall(mc);
      var t=g(mc.xP());
      if(!t.p().isNCs()){return;}
      var pi=t.p().toNCs();
      if(pi.hasUniqueNum()){return;}
      if(pi.equals(P.pThis0)){return;}
      if(mc.s().hasUniqueNum()){watched.add(pi);return;}
      usedMethods.add(new PathSel(pi, mc.s(),null));
      }
    @Override public void visitPCastT(Core.PCastT p){
      super.visitPCastT(p);
      if(p.t().p()==P.pAny){return;}
      if(p.p().isNCs()){cohePs.add(p.p().toNCs());}
      }
    @Override public void visitK(Core.K k){
      super.visitK(k);
      if(k.thr()!=ThrowKind.Return){return;}
      if(!k.t().mdf().isClass()){return;}
      if(k.t().p().isNCs()){cohePs.add(k.t().p().toNCs());}
      }
    }
  }
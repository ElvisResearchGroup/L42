package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.uniqueWrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import is.L42.common.EndError;
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
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitorWithProgram;
import is.L42.visitors.PropagatorCollectorVisitor;


public class Deps{
  ArrayList<P.NCs> typePs=new ArrayList<>();
  ArrayList<P.NCs> cohePs=new ArrayList<>();
  ArrayList<P.NCs> metaCohePs=new ArrayList<>();
  ArrayList<P.NCs> watched=new ArrayList<>();
  ArrayList<PathSel> usedMethods=new ArrayList<>();
  ArrayList<P.NCs> hiddenSupertypes=new ArrayList<>();
  public boolean isEmpty(){return typePs.isEmpty();}
  public static P.NCs _origin(Program p0,P.NCs path,S s){
    try{return p0.from(SortHeader.origin(p0.navigate(path),s,L()),path);}
    catch(LL.NotInDom | EndError ee){return null;}//can be more efficient rewriting the above to avoid the exception.
    //we need the null because when adding usedMethods for methods that are not declared, we need to "guess" that they are not refined...
    }
  public static P.NCs _publicRoot(Program p0,P.NCs pi){
    var cs=pi.cs();
    var csCut=L(cs.stream().takeWhile(c->!c.hasUniqueNum()));
    if(cs.size()==csCut.size()){return null;}
    pi=pi.withCs(csCut);
    return pi;//TODO: it is really unclear if I should do the following instead :-(
    /*if(!csCut.isEmpty()){return pi;}
    var p1=p0.pop(pi.n());
    int added=0;
    while(p1.inPrivate()){
      p1=p1.pop();
      added+=1;      
      }
    return pi.withN(pi.n()+added);*/
    }
  void addP(Program p0,P p){
    if(!p.isNCs()){return;}
    var pi=p.toNCs();
    typePs.add(pi);
    pi=_publicRoot(p0,pi);
    if(pi==null){return;}
    typePs.add(pi);
    addWatched(p0, pi);
    }
  void addUsedMethods(Program p0,P.NCs pi,S si){
    if(pi.hasUniqueNum()){return;}
    if(!pi.cs().isEmpty()){usedMethods.add(new PathSel(pi,si,null));}
    if(pi.n()==0){return;}//This0.emptyCs==This0
    var p1=p0.pop(pi.n());
    if(p1.inPrivate()){return;}
    usedMethods.add(new PathSel(pi,si,null));
    }
  void addWatched(Program p0,P.NCs pi){
    if(!pi.cs().isEmpty()){watched.add(pi);}
    if(pi.n()==0){return;}
    var p1=p0.pop(pi.n());
    if(!p1.inPrivate()){watched.add(pi);}
    }
  void addHiddenSupertype(Program p0,P.NCs pi){
    if(!pi.cs().isEmpty()){hiddenSupertypes.add(pi);}
    var p1=p0.pop(pi.n());
    if(!p1.inPrivate()){hiddenSupertypes.add(pi);}
    }
  public Info toInfo(boolean typed){
    var uWatched=uniqueWrap(watched);
    return new Info(typed,
      /*typeDep*/ uniqueWrap(typePs),
      /*coherentDep*/ uniqueWrap(cohePs),
      /*metaCoherentDep*/ uniqueWrap(metaCohePs),
      /*watched*/ uWatched,
      /*usedMethods*/ uniqueUsedMethods(usedMethods,uWatched),
      /*hiddenSupertypes*/ uniqueWrap(hiddenSupertypes),
      L(), false, "", L(), -1);    
    }
  private static List<PathSel> uniqueUsedMethods(ArrayList<PathSel> l, List<P.NCs> watched){
    ArrayList<PathSel> res=new ArrayList<>();
    for(var t:l){
      if(!watched.contains(t.p()) && !res.contains(t)){res.add(t);}
      }
    return Collections.unmodifiableList(res);
    }
  public Deps collectDocs(Program p0,List<Doc> docs){
    new DepsV(p0).visitDocs(docs);
    return this;
    }
  public Deps collectTs(Program p0,List<T> ts){
    new DepsV(p0).visitTs(ts);
    return this;
    }
  public static List<String>whiteListedNatives=List.of(
      "trusted:lazyCache",
      "trusted:eagerCache",
      "trusted:invalidateCache",
      "trusted:readNowCache"
      );
  public Deps collectDeps(Program p0, List<MWT> mwts){
    var deps=new DepsV(p0);
    //TODO: we had addPublicRoots(cohePs); but I think it was wrong, it would limit the sum interface+class if the class have a non watched private nested class (that would disapper otherwise...)
    for(var m:mwts){
      deps.of(m);
      if(m.nativeUrl().isEmpty()){continue;}
      if(whiteListedNatives.contains(m.nativeUrl())){continue;}
      P p=m.mh().t().p();
      if(p.isNCs()){cohePs.add(p.toNCs());}
      }
    return this;
    }
  public Deps collectDepsNCs(Program p0, List<NC> ncs){
    var deps=new DepsV(p0);
    for(var m:ncs){
      deps.visitDocs(m.docs());
      if(!m.key().hasUniqueNum()){continue;}
      deps.lastC=m.key();
      deps.of(m.l());
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
          if(m.key() instanceof S){refined.add((S)m.key());} 
          }
        }
      else{for(var m:((Core.L)ll).mwts()){refined.add(m.key());}}
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
      Program.flat(l).of(P.of(0,cs0),l.poss());//propagate errors if path is not existent
      return null;
      }
    return pi.withN(pi.n()-(cs.size()+1));
    }
  public class DepsV extends Accumulate.WithG<ArrayList<P.NCs>>{
    Program p0;
    C lastC=null;
    DepsV(Program p0){this.p0=p0;}
    public ArrayList<P.NCs> empty(){return typePs;}
    @Override public void visitL(Full.L l){throw bug();}
    @Override public void visitL(Core.L l){
      l.visitInnerLNoPrivate((li,csi)->this.innerVisitL(l,li,csi));
      }
    private void innerVisitL(L l,L li,List<C> csi){
      Program pcsi=(lastC==null?p0.push(l):p0.push(lastC,l)).navigate(csi);
      try{innerVisitL(pcsi,l,li,csi);}
      catch(EndError.PathNotExistent pne){
        new CloneVisitorWithProgram(pcsi){
          @Override public Info visitInfo(Info info){return info;}
          @Override public P visitP(P path){
            this.p().of(path,this.poss);//propagate path not existent error with good position
            return path;
            }
          }.visitL(li);
        throw pne;
        }
      }
    private void innerVisitL(Program pcsi,L l,L li,List<C> csi){
      for(var p:li.info().typeDep()){skipAct(p, csi, l,typePs::add);}
      for(var p:li.info().coherentDep()){skipAct(p, csi, l,metaCohePs::add);}
      for(var p:li.info().metaCoherentDep()){skipAct(p, csi, l,metaCohePs::add);}
      for(var p:li.info().watched()){skipAct(p, csi, l,w->addWatched(pcsi, w));}
      for(var pathSel:li.info().usedMethods()){
        var p=pathSel.p().toNCs();
        skipAct(p, csi, l,pi->{
          if(!pi.equals(P.pThis0)){usedMethods.add(pathSel.withP(pi));}
          });
        }
      for(var p:li.info().hiddenSupertypes()){skipAct(p, csi, l,w->addHiddenSupertype(pcsi,w));}
      for(var t:li.ts()){
        if(!t.p().hasUniqueNum()){skipAct(t.p().toNCs(), csi, l,w->addHiddenSupertype(pcsi,w));}
        }
      }
    @Override public void visitP(P p){addP(p0,p);}
    @Override public void visitMCall(Core.MCall mc){
      super.visitMCall(mc);
      var t=g(mc.xP());
      if(!t.p().isNCs()){return;}
      var pi=t.p().toNCs();
      if(pi.equals(P.pThis0)||pi.hasUniqueNum()){return;}//mc.s() can be public if is implemented
      //it is irrelevant to watch or not interface methods, since interfaces can not be made abstract anyway
      if(mc.s().hasUniqueNum()){addWatched(p0,pi);return;}
      addUsedMethods(p0,pi, mc.s());
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
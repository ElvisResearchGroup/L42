package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.generated.Mdf.*;
import static is.L42.generated.ThrowKind.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.L42.generated.Core.*;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
import is.L42.visitors.FV;
import is.L42.visitors.PropagatorCollectorVisitor;
import is.L42.visitors.UndefinedCollectorVisitor;

public class MdfTypeSystem extends UndefinedCollectorVisitor{
  public MdfTypeSystem(Program p, G g, Set<Mdf> mdfs,  Mdf expected) {
    this.p = p;
    this.g = g;
    this.mdfs=mdfs;
    this.expected=expected;
  }
  boolean isDeep;
  Program p;
  G g;
  Set<Mdf> mdfs;
  Mdf expected;
  void visitExpecting(E e,Mdf newExpected){
    Mdf oldE=expected;
    expected=newExpected;
    visitE(e);
    expected=oldE;
    }
  void errIf(boolean cond,E e,String msg){
    if(cond){
    throw new EndError.TypeError(e.poss(),msg);}
    }
  void mustSubMdf(Mdf m1,Mdf m2,List<Pos> poss){
    if(!Program.isSubtype(m1, m2)){
      throw new EndError.TypeError(poss,Err.subTypeExpected(m1,m2));
      }
    }  
  @Override public void visitEVoid(EVoid e){
    mustSubMdf(Immutable, expected,e.poss());
    }
  @Override public void visitPCastT(PCastT e){
    mustSubMdf(Class, expected,e.poss());
    }
  @Override public void visitL(L e){
    mustSubMdf(Immutable, expected,e.poss());
    }
  @Override public void visitEX(EX e){
    mustSubMdf(g.of(e.x()).mdf(), expected,e.poss());
    }
  @Override public void visitLoop(Loop e){
    mustSubMdf(Immutable, expected,e.poss());
    visitExpecting(e.e(),Immutable);
    }
  @Override public void visitThrow(Throw e){
    if(e.thr()!=Return){visitExpecting(e.e(),Immutable);return;}
    var general=TypeManipulation._mostGeneralMdf(mdfs);
    errIf(general==null,e,Err.invalidSetOfMdfs(mdfs));
    visitExpecting(e.e(),TypeManipulation.fwdOf(general));
    }
  @Override public void visitMCall(MCall e){
    P p0=TypeManipulation.guess(g,e.xP());
    var meths=AlternativeMethodTypes.types(p,p0.toNCs(),e.s());
    meths=L(meths.stream().filter(m->Program.isSubtype(m.mdf(),expected)));
    //TODO: use the "canAlsoBe" to further filter on the set of methods,
    //this can also be used to give better error messages line "class method called on non class"
    errIf(meths.isEmpty(),e,Err.methCallResultIncompatibleWithExpected(e.s(),expected));
    List<E> es=L(c->{c.add(e.xP());c.addAll(e.es());});
    var oldG=g;
    var oldExpected=expected;
    var oldMdfs=mdfs;
    HashMap<String,HashSet<Mdf>> wrongParameters=new HashMap<>();
    String currentX=null;
    Mdf currentMdf=null;
    EndError.TypeError lastErr=null;
    for(var m:meths){
      try{
        g=oldG;
        mdfs=oldMdfs;
        for(int i:range(es)){
          expected=m.mdfs().get(i);
          currentX=i==0?"receiver":e.s().xs().get(i-1).inner();
          currentMdf=expected;
          visitE(es.get(i));
          }
        expected=oldExpected;
        return;     
        }
      catch(EndError.TypeError toSave){
        lastErr=toSave;
        var res=wrongParameters.putIfAbsent(currentX,new HashSet<>(L(currentMdf)));
        if(res!=null){res.add(currentMdf);}        
        }
      }
    if(lastErr.msgPart().startsWith("Incompatible modifiers for method call")){throw lastErr;}
    errIf(true,e,Err.methCallNoCompatibleMdfParametersSignature(e.s(),wrongParameters));
    }
  @Override public void visitOpUpdate(OpUpdate e){
    mustSubMdf(Immutable,expected,e.poss());
    T t=g.of(e.x());
    visitExpecting(e.e(),t.mdf());
    assert g.isVar(e.x());//TODO: where is varriness of G used?
    }
  @Override public void visitBlock(Block e){
    var hope=expected.isIn(Capsule,Immutable,ImmutableFwd,ImmutablePFwd);
    if(e.ds().isEmpty()){hope=false;}
    if(!hope){visitBlockDirect(e);return;}
    var oldG=g;
    var oldExpected=expected;
    var oldMdfs=mdfs;
    try{visitBlockDirect(e);}
    catch(EndError.TypeError te){
      var lentG=oldG.toLent();
      e.accept(new PropagatorCollectorVisitor(){
        @Override public void visitL(Core.L l){}
        @Override public void visitEX(EX x){
          if(oldG._of(x.x())==null){return;}
          if(lentG._of(x.x())==null){throw te;}
          }
        @Override public void visitOpUpdate(Core.OpUpdate u){
          if(oldG._of(u.x())==null){return;}
          if(!lentG.isVar(u.x())){throw te;}
          super.visitOpUpdate(u);
          }
        });
      g=lentG;
      expected=Mutable;
      mdfs=oldMdfs;
      try{visitBlockDirect(e);}
      catch(EndError.TypeError te2){throw te;}
      finally{g=oldG;expected=oldExpected;}      
      }
    }
  private void visitBlockDirect(Block e){
    var mdfs2=new HashSet<Mdf>(mdfs);
    for(K k:e.ks()){typeK(k,mdfs2);}
    G g1=g;
    if(e.ks().stream().anyMatch(k->k.thr()==Error)){g1=g.toRead();}
    var oldMdfs=mdfs;
    mdfs=mdfs2;
    G oldG=g;
    G g0=typeDs(g1,e.ds(),mdfs2);
    g=oldG.plusEqMdf(g0);
    mdfs=oldMdfs;
    visitE(e.e());
    g=oldG;
    }
  private G typeDs(G g0,List<D>allDs, HashSet<Mdf> mdfs) {
    //avoiding recursion, it would go in stack overflow for long generated
    //ds when expanding long string literals
    if(allDs.isEmpty()){return g0;}
    var mutDs=new ArrayList<>(allDs);
    while(!mutDs.isEmpty()){
      g0=typeDsLoops(g0,mutDs,mdfs);
      }
    return g0;
  }
  private G typeDsLoops(G g0,ArrayList<D>mutDs, HashSet<Mdf> mdfs){
    assert !mutDs.isEmpty();
    var fvs=new ArrayList<X>();
    int split=splitDs(mutDs,fvs);
    var away=mutDs.subList(0, split+1);
    var txe=L(away.stream());
    int size=mutDs.size();
    away.clear();
    assert size==split+1+mutDs.size():size+" "+split+" "+mutDs.size();
    G g1=g0.plusEqFwdOnlyMutOrImm(txe);
    for(var d:txe){
      g=g1;
      var mdf=TypeManipulation.fwdPOf(d.t().mdf());
      visitExpecting(d.e(),mdf);
      }
    if(TypeManipulation.fwd_or_fwdP_inMdfs(mdfs)){
      List<D> errs=L(txe.stream().filter(di->fvs.contains(di.x())));
      if(!errs.isEmpty()){//TODO: errIf may not be the best abstraction...
        errIf(true,errs.get(0).e(),Err.mayLeakUnresolvedFwd(errs.get(0).x()));
        }
      assert txe.size()==1;//TODO: if this is true we can optimize above, avoiding streams
      }
    G g2;
    boolean fwdInFreeMdfs=fvs.stream().anyMatch(xi->{
      var ti=g0._of(xi);
      if(ti==null){return false;}
      return TypeManipulation.fwd_or_fwdP_in(ti.mdf());
      });
    if(fwdInFreeMdfs){g2=g0.plusEqFwdP(txe);}
    else{g2=g0.plusEq(txe);}
    return g2; 
    }
  private int splitDs(List<D> ds,ArrayList<X> xs){//cut will be from 0 to i included
    if (ds.isEmpty()){return 0;}
    int n=ds.size()-1;
    for(int i=0;i<n;i+=1){//i+1 always available in ds
      xs.addAll(FV.of(ds.get(i).e().visitable()));
      if(xsNotInDomi(xs,ds,i+1)){return i;}
      }
    return n;
    }
  private boolean xsNotInDomi(List<X> xs,List<D> ds,int ip1){
    for(int i=ip1;i<ds.size();i+=1){
      if(xs.contains(ds.get(i).x())){return false;}
      }
    return true;
    }
  private void typeK(K k, HashSet<Mdf> mdfs1) {
    var t=k.t();
    var oldG=g;
    g=g.plusEq(k.x(),k.t());
    visitE(k.e());
    g=oldG;
    if(k.thr()==ThrowKind.Return){mdfs1.add(t.mdf());}
    }
  }
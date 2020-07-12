package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.L;
import is.L42.generated.Core.T;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import static is.L42.tools.General.*;
import is.L42.typeSystem.Coherence;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;
    /*
     if a method m annotated @Cache.Calls is called
     using an expression e with no free variables
     generate "class method m_num::0()=native{trusted:lazyCache} e"
     in the same library, and replace the call with
     "This.m_num::0()"
     Recursivelly do the same task on all the nested libraries (also in method bodies)
     */
public class CacheCall{
  public static Core.L of(Program p,Function<L42£LazyMsg,L42Any>wrap){
    return of(p,new MetaError(wrap));
    }
  static Core.L of(Program p,MetaError err){
    var l=p.topCore();
    List<MWT>mwts=L(newMwts->{
      for(int i:range(l.mwts())){newMwts.add(handleMWT(p,newMwts, i, err));}
      });
    List<NC>ncs=L(l.ncs(),(newNCs,nci)->{
      var pi=p.push(nci.key(),nci.l());
      var li=of(pi,err);
      newNCs.add(nci.withL(li));
      });
    return l.withMwts(mwts).withNcs(ncs);
    }
  public static Core.L.MWT handleMWT(Program p,ArrayList<MWT> mwts,int index,MetaError err){
    var mwt=p.topCore().mwts().get(index);
    if(mwt._e()==null) {return mwt;}
    var name=mwt.key().m()+"_"+index;
    G g=G.of(mwt.mh());
    var visitor=new CacheCallCloneVisitor(p, g, mwts, name, err);
    return mwt.with_e(visitor.visitE(mwt._e()));
    }
  }
class CacheCallCloneVisitor extends CloneVisitorWithProgram.WithG{
  ArrayList<MWT> newMWTs;
  HashSet<String> fv=new HashSet<>();
  MetaError err;
  String name;
  int num=0;
  public CacheCallCloneVisitor(Program p, G g,ArrayList<MWT> mwts,String name,MetaError err){
    super(p, g);
    this.name=name;//unique names in format methName_methIndex
    this.err=err;//index would be sufficient, but the methName helps for debugging
    this.newMWTs=mwts;
    }
  //handling FV
  private HashSet<String> store(){//reusing(but duplicating) logic from FV
    var acc=fv;//needed to avoid quadratic complexity
    fv=new HashSet<>();
    return acc;
    }
  private void acc(HashSet<String> acc){
    acc.addAll(fv);
    fv=acc;
    }
  @Override public Core.EX visitEX(Core.EX x){
    fv.add(x.x().inner());return x;
    }
  @Override public Core.K visitK(Core.K k){
    var acc=store();
    k=super.visitK(k);
    fv.remove(k.x().inner());
    acc(acc);
    return k;
    }
  @Override public Core.Block visitBlock(Core.Block b){
    var acc=store();//correct because of well formedness FULL/CORE block: FV(Ks.es) disjoint bindings(Ds.DXs)
    var res=super.visitBlock(b);
    acc(acc);
    return res;
    }
  //handling nested coreL
  @Override public Core.L visitL(Core.L l){
    return CacheCall.of(p().push(l),err);
    }
  //handling mcall
  @Override public Core.MCall visitMCall(Core.MCall m){
    m=super.visitMCall(m);//also compute fv
    if(!fv.isEmpty()){return m;}
    var t=g._of(m.xP());
    if(t==null){return m;}
    var l=p()._ofCore(t.p());
    if(l==null){return m;}
    var mwt=_elem(l.mwts(),m.s());
    if(mwt==null){return m;}
    var ncs=t.p().toNCs();
    if(!Utils.match(p().navigate(ncs), err,"callCache", mwt)){return m;}
    if(!mwt.mh().t().mdf().isImm()){err.throwErr(mwt,"Call Cache methods must return an imm result");}
    //TODO: extend it to allow class result?
    if(!mwt.mh().exceptions().isEmpty()){err.throwErr(mwt,"Call Cache methods can not throw exceptions");}
    //TODO: now will generate all the subcached too. Should we recognize the issue, use the original m (before super)
    //and discard the intermediates using a store/acc like for fv?
    S sel=new S(name+"_"+num,L(),0);
    num+=1;
    var resT=p().from(mwt.mh().t().withDocs(L()),ncs);    
    var mh=new Core.MH(Mdf.Class,L(),resT,sel,L(),L());
    var newMwt=new Core.L.MWT(m.poss(),L(), mh,"trusted:lazyCache",m);
    this.newMWTs.add(newMwt);
    return Utils.ThisCall(m.pos(), sel,L());
    }
  }
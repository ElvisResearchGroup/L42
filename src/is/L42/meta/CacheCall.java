package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Mdf;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;
    /*
     if a method m annotated @Cache.Calls is called
     using an expression e with no free variables
     generate "class method m_num::0()=native{trusted:lazyCache} e"
     in the same library, and replace the call with
     "This.m_num::0()".
     //??If the class is not closed, generate a private closed nested class instead.
     Private methods annotated lazyCache whose body is not a block are not explored.
     Recursively do the same task on all the nested libraries (also in method bodies)
     */
public class CacheCall{
  public static Core.L of(Program p,Function<L42£LazyMsg,L42Any>wrap){
    return of(p,new MetaError(wrap));
    }
  static Core.PCastT _head(Core.L l){
    if(l.info().close() ||l.isInterface()) {return null;}
    Resources.allBusyUpTo+=1;
    while(Resources.usedUniqueNs.contains(Resources.allBusyUpTo)){
      Resources.allBusyUpTo+=1;
      }
    C c=C.of("CacheCallPrivate",Resources.allBusyUpTo);
    P.NCs nc=P.NCs.of(0,List.of(c));
    return new Core.PCastT(l.pos(), nc, new Core.T(Mdf.Class,L(), nc));
    } 
  static List<Core.L.NC> addHead(Core.PCastT _head,ArrayList<MWT>mwts,List<Core.L.NC>ncs,Info info){
    if(_head==null || mwts.isEmpty()){return ncs;}
    info=info.accept(CacheCallCloneVisitor.addThis1).withClose(true);
    var innerL=new Core.L(mwts.get(0).poss(),false,L(),mwts,L(), info,L());
    var nc=new Core.L.NC(mwts.get(0).poss(), L(), _head.p().toNCs().cs().get(0),innerL);
    return pushL(ncs,nc);    
    }
  static Core.L of(Program p,MetaError err){
    var l=p.topCore();
    Core.PCastT _head=_head(l);
    ArrayList<MWT>nestedMwts=new ArrayList<>();
    List<MWT>mwts=L(newMwts->{
      for(int i:range(l.mwts())){newMwts.add(handleMWT(_head,p,_head==null?newMwts:nestedMwts, i, err));}
      });
    boolean mustConsistentThis0=mwts.size()!=l.mwts().size();
    List<NC>ncs=L(l.ncs(),(newNCs,nci)->{
      var pi=p.push(nci.key(),nci.l());
      var li=of(pi,err);
      newNCs.add(nci.withL(li));
      });
    var i=l.info();
    if(mustConsistentThis0){      
      var td=i.typeDep();
      var cd=i.coherentDep();
      if(!td.contains(P.pThis0)){i=i.withTypeDep(pushL(td,P.pThis0));}
      if(!cd.contains(P.pThis0)){i=i.withCoherentDep(pushL(cd,P.pThis0));}
      }
    var res=l.withMwts(mwts).withNcs(addHead(_head,nestedMwts,ncs,i));
    if(_head!=null && !nestedMwts.isEmpty()){
      var headP=_head.p().toNCs();
      var td=i.typeDep();
      var cd=i.coherentDep();
      if(!td.contains(headP)){i=i.withTypeDep(pushL(td,headP));}
      if(!cd.contains(headP)){i=i.withCoherentDep(pushL(cd,headP));}      
      }
    return res.withInfo(i);
    }
  public static Core.L.MWT handleMWT(Core.PCastT _head, Program p,ArrayList<MWT> mwts,int index,MetaError err){
    var mwt=p.topCore().mwts().get(index);
    if(mwt._e()==null) {return mwt;}
    var skip=!(mwt._e() instanceof Core.Block) && Utils.match(p, err,"callCache", mwt);
    if(skip){return mwt;}
    var name=mwt.key().m()+"_"+index;
    G g=G.of(mwt.mh());
    var visitor=new CacheCallCloneVisitor(_head,p, g, mwts, name, err);
    return mwt.with_e(visitor.visitE(mwt._e()));
    }
  }
class CacheCallCloneVisitor extends CloneVisitorWithProgram.WithG{
  Core.PCastT _head;
  ArrayList<MWT> newMWTs;
  HashSet<String> fv=new HashSet<>();
  MetaError err;
  String name;
  int num=0;
  public CacheCallCloneVisitor(Core.PCastT _head, Program p, G g,ArrayList<MWT> mwts,String name,MetaError err){
    super(p, g);
    this._head=_head;
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
  static final CloneVisitor addThis1=new CloneVisitor(){
    @Override public P visitP(P p){
      if(!p.isNCs()) {return p;}
      return p.toNCs().withN(p.toNCs().n()+1);
      }
    };
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
    //TODO: now it will generate all the subcached too. This is good for Core.L insite the e, but could be
    //ineffective otherwise.
    //Should we recognize the issue, use the original m (before super)
    //and discard the intermediates using a store/acc like for fv?
    S sel=new S(name+"_"+num,L(),0);
    num+=1;
    var resT=p().from(mwt.mh().t().withDocs(L()),ncs);    
    var mh=new Core.MH(Mdf.Class,L(),resT,sel,L(),L());
    var newMwt=new Core.L.MWT(m.poss(),L(), mh,"trusted:lazyCache",m);
    var res = Utils.ThisCall(m.pos(), sel,L());
    if(_head!=null) {
      res=res.withXP(_head);
      newMwt=newMwt.accept(addThis1);
    }
    this.newMWTs.add(newMwt);
    return res;
    }
  }
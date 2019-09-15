package is.L42.top;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.G;
import is.L42.common.GX;
import is.L42.common.Program;
import is.L42.common.TypeManipulation;
import is.L42.generated.C;
import is.L42.generated.CT;
import is.L42.generated.Core;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.ER;
import is.L42.generated.Full;
import is.L42.generated.Full.L.NC;
import is.L42.generated.Half;
import is.L42.generated.I;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.PR;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.ST;
import is.L42.generated.Y;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.*;

public class Top {
  public PR top(PR pr)throws EndError {
    SortHeader sorted=new SortHeader(pr.p());//progagates the right header errors
    Core.L coreL=sorted.l;
    List<Full.L.NC> ncs=sorted.ncs;
    Program pl=pr.p().update(coreL);
    List<MH> mh1n=L(coreL.mwts(),(c,m)->c.add(m.mh()));
    Program p0=pr.p().update(updateInfo(pl,L(mh1n,(c,m)->c.add(sorted.mwtOf(m, null)))));
    ArrayList<CT> ctz0=new ArrayList<>(pr.ctz());
    ArrayList<Half.E> e1n=new ArrayList<>();
    for(MH mhi:mh1n){
      ctzAdd(ctz0,p0,mhi,sorted._eOf(mhi.s()),e1n);
      }
    PR pr1=topNC(ctz0,p0,ncs);//propagate exceptions
    List<CT> ctz1=pr1.ctz();
    Program p1=pr1.p();
    assert p1.top instanceof Core.L;
    List<Core.E> coreE1n=L(e1n,(c,_ei)->{
      ER eri=infer(new I(null,p1,new G(),p1.minimizeCTz(ctz1)),_ei);
      c.add(eri._e());
      });//and propagate errors out
    List<MWT> mwt1n=L(mh1n,coreE1n,(c,mhi,_ei)->c.add(sorted.mwtOf(mhi,_ei)));
    Core.L l=updateInfo(p1,mwt1n);
    Program p2=flagTyped(p1.update(l));//propagate illTyped
    return new PR(ctz1,p2);
    }
  private Program flagTyped(Program p) throws EndError {
    return p; 
    }
  private Core.L updateInfo(Program p, List<MWT>mwts) {
    //p(This0) = {interface? Ts MWTs0 MWTs1 NCs Info0 Docs}
    Core.L l=(Core.L)p.top;
    List<MWT> mwts0=L(l.mwts(),(c,m)->{
      var newM=_elem(mwts,m.key());
      if(newM==null){c.add(m);return;}
      assert newM.with_e(null).equals(m);
      });
    assert mwts0.size()+mwts.size()==l.mwts().size();
    ArrayList<P> typePs=new ArrayList<>();
    ArrayList<P> cohePs=new ArrayList<>();
    collectDeps(p,mwts,typePs,cohePs,true);
    Info info=new Info(false,L(typePs.stream()),L(cohePs.stream()),L(),L(),L(),L(),false); 
    return new Core.L(l.poss(), l.isInterface(), l.ts(), merge(mwts0,mwts), l.ncs(),sumInfo(l.info(),info),l.docs());
    }
  private Info sumInfo(Info info1, Info info2) {
    return new Info(info1.isTyped() && info2.isTyped(),
      mergeU(info1.typeDep(),info2.typeDep()),
      mergeU(info1.coherentDep(),info2.coherentDep()),
      mergeU(info1.friends(),info2.friends()),
      mergeU(info1.usedMethods(),info2.usedMethods()),
      mergeU(info1.privateSupertypes(),info2.privateSupertypes()),
      mergeU(info1.refined(),info2.refined()),
      info1.declaresClassMethods() || info2.declaresClassMethods()
      );
    }
  private Core.L updateInfo(Program p1, Core.L.NC nc) {
    List<P>dep=collectDeptDocs(nc.docs());
    Core.L l=(Core.L)p1.top;
    var info=l.info();
    info=info.withTypeDep(mergeU(info.typeDep(),dep));
    l=l.withNcs(pushL(l.ncs(),nc)).withInfo(info);
    return l;
    }
  private List<P> collectDeptDocs(List<Doc> docs) {
    // TODO Auto-generated method stub
    return null; 
    }
  private ER infer(I i, is.L42.generated.Half.E _ei) throws EndError{
    return null; //TODO: inject to core
    }
  private static final Half.T halfLib=new Half.T(null,L((ST)P.coreLibrary));
  private PR topNC(List<CT> ctz, Program p, List<NC> ncs)  throws EndError{
    if(ncs.isEmpty()){return new PR(ctz,p);}
    C c0=ncs.get(0).key();
    Full.E fe=ncs.get(0).e();
    List<Full.Doc> docs=ncs.get(0).docs();
    List<Pos> poss=ncs.get(0).poss();
    ncs=popL(ncs);
    Y y=new Y(p,new GX(),halfLib.stz(),null,halfLib,true);
    HalfQuadruple hq=new HalfQuadruple(y,fe);
    Half.E he=hq.e;
    I i=new I(c0,p,new G(),p.minimizeCTz(ctz));
    ER er=infer(i,he); //propagates errors
    List<CT> ctz1=er.ctz();
    Core.E ce=er._e();
    assert ce!=null;
    Core.T t=wellTyped(p,ce);//propagate errors
    Core.E ce0=adapt(ce,t);
    coherent(p,ce0); //propagate errors
    ER er1=reduce(p,ce0);//propagate errors
    Core.L l=(Core.L)er1._e();
    assert l!=null;
    Core.L.NC nc=new Core.L.NC(poss, TypeManipulation.toCoreDocs(docs), c0, l);
    Program p1 = p.update(updateInfo(p,nc));
    Program p2=flagTyped(p1);//propagate errors    
    PR res=topNC(ctz1,p2,ncs);
    return res; 
    }
  private ER reduce(Program p, is.L42.generated.Core.E ce0)throws EndError  {//TODO: must wrap exceptions and java exceptions
    var res=new Core.L(L(), false, L(),L(),L(),Core.L.Info.empty,L()); 
    return new ER(L(),res);
    }
  private void coherent(Program p, is.L42.generated.Core.E ce0)throws EndError {
    }
  private Core.E adapt(Core.E ce, T t) {
    return ce;
    }
  private T wellTyped(Program p, is.L42.generated.Core.E ce)  throws EndError{
    return P.coreLibrary; 
    }
  private void ctzAdd(ArrayList<CT> ctz, Program p, MH mh, Full.E _e, ArrayList<Half.E> es) {
    if(_e==null){es.add(null);return;}
//    * CTz0.add(p; MH e; Half.e) = CTz0 U CTz U STs<=MH.T
//    Y = Y[p=I.p;GX=G^MH;onSlash=MH.T;onSlashX=empty;expectedT=MH.T;onPath=class]
//    Y!e = Half.e; STs; empty; CTz
    }
  static void collectDeps(Program p0, List<MWT> mwts2, ArrayList<P> typePs, ArrayList<P> cohePs,boolean justBodies) {
   // TODO Auto-generated method stub
   }
  }
class HalfQuadruple{
  public final Half.E e;
  public HalfQuadruple(Y y, Full.E fe) { 
    if(fe instanceof Full.L){
      this.e=(Full.L)fe;
      }
    else{
      this.e=new Full.L(fe.pos(),false,"",false,L(),L(),L());
      }
    }
  }
class SortHeader{
  public final Core.L l;
  public final List<Full.L.NC> ncs;
  public final List<Full.L.M> notNC;
  public SortHeader(Program p) throws EndError{
    Full.L l=(Full.L)p.top;
    if(!l.reuseUrl().isEmpty()){throw todo();}
    List<Full.L.NC> ncs=L(l.ms(),(c,m)->{
      if(m instanceof Full.L.NC){c.add((Full.L.NC)m);}
      });
    var notNC=L(l.ms().stream().filter(m->!(m instanceof Full.L.NC)));
    Program p0=p.update(l.withMs(notNC));
    var cts=TypeManipulation.toCoreTs(l.ts());
    List<Core.T> ts1=L(c->{
      c.addAll(cts);
      for(var ti:p0.collect(cts,l.poss())){
        if(!cts.contains(ti)){c.add(ti);}
        }
      });
    for(var ti:ts1){
      if(cts.contains(ti)){continue;}
      assert ti.p().isNCs();
      List<C> cs=ti.p().toNCs().cs();
      for(C ci:cs){
        if(ci.hasUniqueNum()){
          throw new Program.InvalidImplements(l.poss(),Err.sealedInterface(ti,ts1));
          }
        }
      }
    var mh1n=p0.methods(P.coreThis0.p(),l.poss());//propagate err
    List<Core.L.MWT> mwts=L(mh1n,(c,mh)->{
      List<Core.Doc> docs=L();
      var mwti=(Full.L.MWT)_elem(l.ms(),mh.key());
      if(mwti!=null){docs=TypeManipulation.toCoreDocs(mwti.docs());}
      var res=new Core.L.MWT(mwti.poss(), docs, mh,"",null);
      c.add(res);
      });
    ArrayList<P> typePs=new ArrayList<>();
    ArrayList<P> cohePs=new ArrayList<>();
    Top.collectDeps(p0,mwts,typePs,cohePs,false);
    boolean classMeth=mh1n.stream().anyMatch(m->m.mdf().isClass());
    Info info=new Info(false,L(typePs.stream()),L(cohePs.stream()),L(),L(),L(),L(),classMeth); 
    var docs=TypeManipulation.toCoreDocs(l.docs());
    this.l=new Core.L(l.poss(),l.isInterface(), ts1, mwts, L(), info, docs);
    this.ncs=ncs;
    this.notNC=notNC;
    }
  public MWT mwtOf(MH mh, Core.E _ei) {
    Full.L.M res=_elem(notNC,mh.s()); 
    List<Core.Doc> docs=L();
    List<Pos>poss=this.l.poss();
    String url="";
    if(res!=null){
      docs=TypeManipulation.toCoreDocs(res.docs());
      poss=res.poss();
      if(res instanceof Full.L.MWT){url=((Full.L.MWT)res).nativeUrl();}
      }
    else{
      Core.L.MWT coreM=_elem(this.l.mwts(),mh.s());
      if(coreM!=null){
        docs=coreM.docs();
        poss=coreM.poss();
        url=coreM.nativeUrl();
        }
      }
    assert url.isEmpty() || _ei!=null;
    return new Core.L.MWT(poss, docs, mh,url, _ei);
    }
  public Full.E _eOf(S s) {
    Full.L.M res=_elem(notNC,s); 
    return res._e(); 
    }
  }
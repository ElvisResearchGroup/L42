package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Op;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.Core.EVoid;

public class InjectionToCore {
  public String fileName;
  public StringBuilder errors;
  public EVoid eVoid;

  public InjectionToCore(StringBuilder errors, EVoid eVoid) {
    this.errors = errors;
    this.eVoid = eVoid;
  }
  private Core.L makeErr(Pos pos,Core.L.Info info){
    if(info==null){return null;}
    String err=info.toString();
    var errRes=new Core.L(pos,false, L(),L(),L(),info,L());
    err=err.substring(0,Math.min(6, err.length()));
    err="line " + pos.line() + ":" + pos.column() 
      + " Error: Extraneus token "+err;
    errors.append(err);
    return errRes;
    }
  public Core.L inject(Full.L res,Core.L.Info info) {
    if(res.isDots() || !res.reuseUrl().isEmpty()){
      return makeErr(res.pos(),info);
      }
    long cut=res.ms().stream()
      .takeWhile(m->!(m instanceof Full.L.NC))
      .count();
    var mwts=res.ms().stream().limit(cut)
      .filter(m->m instanceof Full.L.MWT)
      .map(m->(Full.L.MWT)m)
      .collect(Collectors.toList());
    var ncs=res.ms().stream().skip(cut)
      .filter(m->m instanceof Full.L.NC)
      .map(m->(Full.L.NC)m)
      .collect(Collectors.toList());
    if(mwts.size()+ncs.size()!=res.ms().size()){
      return makeErr(res.pos(),info);
      }
    List<Core.T>cts=injectTs(res.ts());
    List<Core.L.MWT>cmwts=L(mwts,(c,mi)->c.add(inject(mi)));
    List<Core.L.NC>cncs=L(ncs,(c,ni)->c.add(inject(ni)));
    List<Core.Doc>cdocs=injectDocs(res.docs());
    if(cdocs==null || cmwts==null || cncs.contains(null) || cdocs.contains(null)){
      return makeErr(res.pos(),info);
      }      
    return new Core.L(res.pos(),res.isInterface(), cts, cmwts, cncs, info, cdocs); 
    }
  public Core.T inject(Full.T t) {
    List<Core.Doc>docs=injectDocs(t.docs());
    if(docs==null){return null;}
    if(t.csP()==null || t.csP()._p()==null){return null;}
    return new Core.T(inject(t._mdf()), docs, t.csP()._p());    
    }
  public Core.L.MWT inject(Full.L.MWT mwt) {
    var docs=injectDocs(mwt.docs());
    var mh=inject(mwt.mh());
    if(docs==null || mh==null){return null;}
    if(mwt._e()==null){return new Core.L.MWT(mwt.pos(), docs, mh, mwt.nativeUrl(),null);}
    Core.E[]e={null};
    mwt._e().visitable().accept(new CollectorVisitor(){
      @Override public void visitEX(Core.EX x){e[0]=x;}
      @Override public void visitEVoid(Core.EVoid eVoid){e[0]=eVoid;}
      @Override public void visitL(Core.L l){e[0]=l;}
      @Override public void visitL(Full.L l){e[0]=inject(l,null);}

      @Override public void visitCast(Full.Cast cast){
        if(!(cast.e() instanceof Full.CsP)){e[0]=null;return;}
        P p=inject((Full.CsP)cast.e());
        var t=inject(cast.t());
        if(p==null || t==null){e[0]=null;return;}
        e[0]=new Core.PCastT(cast.pos(), p, t);
        }
        
      @Override public void visitCall(Full.Call call){
        if(call.isSquare() || call._s()==null ||call._s().m().isEmpty()) {e[0]=null;return;}
        if(call.pars().get(0)._that()!=null){e[0]=null;return;}
        this.visitE(call.e());
        var r=e[0];
        if(r==null || !(r instanceof Core.XP)){e[0]=null;return;}
        List<Core.E> es=L(call.pars().get(0).es(),(c,ei)->{visitE(ei);c.add(e[0]);});
        if(es.contains(null)){e[0]=null;return;}
        e[0]=new Core.MCall(call.pos(),(Core.XP)r, call._s(), es); 
        }
      @Override public void visitBlock(Full.Block block){
        //visitDs(block.ds());
        //visitKs(block.ks());
        //visitE(block.e());
        }
      @Override public void visitLoop(Full.Loop loop){
        visitE(loop.e());
        if(e[0]==null){return;}
        e[0]=new Core.Loop(loop.pos(),e[0]);
        }
      @Override public void visitThrow(Full.Throw thr){
        visitE(thr.e());
        if(e[0]==null){return;}
        e[0]=new Core.Throw(thr.pos(),thr.thr(),e[0]);
        }
      @Override public void visitOpUpdate(Full.OpUpdate opUpdate){
        if(opUpdate.op()!=Op.ColonEqual){e[0]=null;return;}
        visitE(opUpdate.e());
        if(e[0]==null){return;}
        e[0]=new Core.OpUpdate(opUpdate.pos(),opUpdate.x(),e[0]);
        }
      });
    if(e[0]==null){return null;}
    return new Core.L.MWT(mwt.pos(), docs, mh, mwt.nativeUrl(),e[0]);
    }
  public Mdf inject(Mdf mdf) {return mdf==null?Mdf.Immutable:mdf;}
    
  public Core.MH inject(Full.MH mh) {
    var docs=injectDocs(mh.docs());
    var pars=injectTs(mh.pars());
    var exceptions=injectTs(mh.exceptions());
    var t=inject(mh.t());
    if(mh.s().m().isEmpty() || mh._op()!=null || docs==null
      || pars==null || exceptions==null){return null;}
    return new Core.MH(inject(mh._mdf()), docs, t, mh.s(), pars, exceptions);
    }

  public Core.L.NC inject(Full.L.NC nc) {
    var docs=injectDocs(nc.docs());
    Core.L l=null;
    if (nc.e() instanceof Core.L){l=(Core.L)nc.e();}
    if (nc.e() instanceof Full.L){l=inject((Full.L)nc.e(),null);}
    if(docs==null || l==null){return null;}
    return new Core.L.NC(nc.pos(), docs, nc.key(),l);
    }
  public P inject(Full.CsP csP) {return csP._p();}

  public Core.Doc inject(Full.Doc d) {
    Core.PathSel ps=null;
    if(d._pathSel()!=null){
      ps=inject(d._pathSel());
      if(ps==null){return null;}
      }
    var docs=injectDocs(d.docs());
    if(docs==null){return null;}
    return new Core.Doc(ps,d.texts(),docs);
    }
  public Core.PathSel inject(Full.PathSel p) {
    P cp=inject(p._csP());
    if(cp==null){return null;}
    if(p._s()!=null && p._s().m().isEmpty()){return null;}
    return new Core.PathSel(cp, p._s(),p._x());
    }
  public List<Core.Doc> injectDocs(List<Full.Doc> docs) {
    List<Core.Doc>cdocs=L(docs,(c,di)->c.add(inject(di)));
    if(cdocs.contains(null)){return null;}
    return cdocs;
    }
  public List<Core.T> injectTs(List<Full.T> ts) {
    List<Core.T>cts=L(ts,(c,ti)->c.add(inject(ti)));
    if(cts.contains(null)){return null;}
    return cts;
    }
}
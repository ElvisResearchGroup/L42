package is.L42.perftests;

import java.util.List;

import is.L42.generated.Core;
import is.L42.visitors.PropagatorCollectorVisitor;

public class CoreNodeCounter extends PropagatorCollectorVisitor {
  
  private int ctr;
  public int getNodeCount() {
    return this.ctr;
    }
  
  public void visitE(Core.E e) {
    super.visitE(e);
    this.ctr++;
    }
  public void visitXP(Core.XP xP){
    super.visitXP(xP);
    this.ctr++;
    }
  public void visitEs(List<Core.E> es){
    super.visitEs(es);
    this.ctr++;
    }
  public void visitPathSels(List<Core.PathSel> pathSels){
    super.visitPathSels(pathSels);
    this.ctr++;
    }
  public void visitXPs(List<Core.XP> xPs){
    super.visitXPs(xPs);
    this.ctr++;
    }
  public void visitMWTs(List<Core.L.MWT> mwts){
    super.visitMWTs(mwts);
    this.ctr++;
    }
  public void visitNCs(List<Core.L.NC> ncs){
    super.visitNCs(ncs);
    this.ctr++;
    }
  public void visitDs(List<Core.D> ds){
    super.visitDs(ds);
    this.ctr++;
    }
  public void visitKs(List<Core.K> ks){
    super.visitKs(ks);
    this.ctr++;
    }
  public void visitTs(List<Core.T> ts){
    super.visitTs(ts);
    this.ctr++;
    }
  public void visitDocs(List<Core.Doc> docs){
    super.visitDocs(docs);
    this.ctr++;
    }
  public void visitEX(Core.EX x){
    super.visitEX(x);
    this.ctr++;
    }
  public void visitPCastT(Core.PCastT pCastT){
    super.visitPCastT(pCastT);
    this.ctr++;
    }
  public void visitEVoid(Core.EVoid eVoid){
    super.visitEVoid(eVoid);
    this.ctr++;
    }
  public void visitL(Core.L l){
    super.visitL(l);
    this.ctr++;
    }
  public void visitInfo(Core.L.Info info){
    super.visitInfo(info);
    this.ctr++;
    }
  public void visitMWT(Core.L.MWT mwt){
    super.visitMWT(mwt);
    this.ctr++;
    }
  public void visitNC(Core.L.NC nc){
    super.visitNC(nc);
    this.ctr++;
    }
  public void visitMCall(Core.MCall mCall){
    super.visitMCall(mCall);
    this.ctr++;
    }
  public void visitBlock(Core.Block block){
    super.visitBlock(block);
    this.ctr++;
    }
  public void visitLoop(Core.Loop loop){
    super.visitLoop(loop);
    this.ctr++;
    }
  public void visitThrow(Core.Throw thr){
    super.visitThrow(thr);
    this.ctr++;
    }
  public void visitOpUpdate(Core.OpUpdate opUpdate){
    super.visitOpUpdate(opUpdate);
    this.ctr++;
    }
  public void visitD(Core.D d){
    super.visitD(d);
    this.ctr++;
    }
  public void visitK(Core.K k){
    super.visitK(k);
    this.ctr++;
    }
  public void visitT(Core.T t){
    super.visitT(t);
    this.ctr++;
    }
  public void visitDoc(Core.Doc doc){
    super.visitDoc(doc);
    this.ctr++;
    }
  public void visitPathSel(Core.PathSel pathSel){
    super.visitPathSel(pathSel);
    this.ctr++;
    }
  public void visitMH(Core.MH mh){
    super.visitMH(mh);
    this.ctr++;
    }

}

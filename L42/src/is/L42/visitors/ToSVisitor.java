package is.L42.visitors;
import static is.L42.tools.General.range;

import java.util.List;
import java.util.function.IntConsumer;

import is.L42.generated.*;

public class ToSVisitor extends CollectorVisitor{
  StringBuilder result=new StringBuilder();
  String currentIndent="";
  void nl(){result.append("\n");result.append(currentIndent);}
  void indent(){currentIndent+="  ";}
  void deIndent(){currentIndent=currentIndent.substring(2);}
  char last(){return result.charAt(result.length()-1);}
  void c(String s){
    assert !s.startsWith(",") || last()!='(':
      s;
    assert !s.startsWith(";") || ( last()!=';'&&  last()!='}'):
      s;
    result.append(s);
    }
  void sp(){result.append(" ");}

  void separeFromChar(){
    char last=last();
    if(Character.isLetter(last) || Character.isDigit(last) || last=='$'){
    result.append(" ");}
    }

  private static final IntConsumer empty=i->{}; 
  void seqHas(IntConsumer prefix, List<? extends HasVisitable> elements,String sep){
    for(int i:range(elements)){
      if(i!=0){c(sep);}
      prefix.accept(i);
      elements.get(i).visitable().accept(this);
      }
    }
  
  void seq(IntConsumer prefix, List<? extends Visitable<?>>elements,String sep){
    for(int i:range(elements)){
      if(i!=0){c(sep);}
      prefix.accept(i);
      elements.get(i).accept(this);
      }
    }

  public void visitMdf(Mdf mdf){
    c(mdf.inner);
    }
  public void visitThrow(Throw thr){
    c(thr.inner);
    }
  public void visitOp(Op op){
    c(op.inner);
    }  
  
  public void visitC(C c){
    c(c.inner());
    if(c.hasUniqueNum()){c("::"+c.uniqueNum());}
    }

  public void visitP(P p){
    if(p==P.pAny){c("Any");return;}
    if(p==P.pLibrary){c("Library");return;}
    if(p==P.pVoid){c("Void");return;}
    var p0=p.toNCs();
    c("This"+p0.n());
    seq(i->c("."),p0.cs(),"");
    }

  public void visitS(S s){
    c(s.m());
    if(s.hasUniqueNum()){c("::"+s.uniqueNum());}
    c("(");seq(empty,s.xs(),",");c(")");
    }

  public void visitX(X x){c(x.inner());}

  public void visitSTMeth(ST.STMeth stMeth){
    visitST(stMeth.st());
    c(".");
    visitS(stMeth.s());
    if(stMeth.i()!=-1){c("."+stMeth.i());}
    }

  public void visitSTOp(ST.STOp stOp){
    visitOp(stOp.op());
    for(var stz:stOp.stzs()){
      c("[");seqHas(empty,stz," ");c("]");
      }
    }

  //public void visitEX(Core.EX x){visitX(x.x());}

  public void visitPCastT(Core.PCastT pCastT){
    visitP(pCastT.p());
    c("<:");
    visitT(pCastT.t());
    }
    
  public void visitEVoid(Core.EVoid eVoid){c("void");}
  
  public void visitL(Core.L l){
    boolean inline=HasMultilinePart.inline(l);
    c("{");
    if(inline){indent();}
    if(l.isInterface()){c("interface");}
    if(!l.ts().isEmpty()){
      c("[");seq(empty,l.ts(),", ");c("] ");
      }
    var sp=empty;
    if(inline){sp=i->nl();}
    seq(sp,l.mwts(),"");
    seq(sp,l.ncs(),"");
    sp.accept(0);
    visitInfo(l.info());
    sp.accept(0);
    visitDocs(l.docs());
    sp.accept(0);
    c("}");
    if(inline){deIndent();}
    }
    
  public void visitInfo(Core.L.Info info){
    if(info.isTyped()){c("#typed}");}
    else {c("#norm{");}
    infoItem("typeDep",info.typeDep());
    infoItem("coherentDep",info.coherentDep());
    infoItem("friendsDep",info.friendsDep());
    infoItem("usedMethDep",info.usedMethDep());
    infoItem("privateImpl",info.privateImpl());
    infoItem("refined",info.refined());
    c("}");
    }
  private void infoItem(String label,List<? extends Visitable<?>> l){
    if(l.isEmpty()){return;}
    c(label+"=("); seq(empty,l,", ");c(")");
    }  
  public void visitMWT(Core.L.MWT mwt){
    visitDocs(mwt.docs());
    visitMH(mwt.mh());
    var _e0=mwt._e();
    if(_e0!=null){c("="); visitE(_e0);}
    }
  
  public void visitNC(Core.L.NC nc){
    visitDocs(nc.docs());
    visitC(nc.key());
    c("="); 
    visitL(nc.l());
    }
  private void cMethName(S s){
    if(s.hasUniqueNum()){c(s.m()+"::"+s.uniqueNum());}
    else c(s.m());
    }
  public void visitMCall(Core.MCall mCall){
    visitXP(mCall.xP());
    c(".");
    cMethName(mCall.s());
    IntConsumer pName=i->{c(mCall.s().xs().get(i)+"=");};
    c("(");
    seqHas(pName,mCall.es(),", ");
    c(")");
    }
    
  public void visitBlock(Core.Block block){
    visitDs(block.ds());
    visitKs(block.ks());
    visitE(block.e());
    }
    
  public void visitLoop(Core.Loop loop){
    visitE(loop.e());
    }
    
  public void visitThrow(Core.Throw thr){
    visitE(thr.e());
    }
    
  public void visitOpUpdate(Core.OpUpdate opUpdate){
    visitX(opUpdate.x());
    visitE(opUpdate.e());
    }
    
  public void visitD(Core.D d){
    visitT(d.t());
    visitX(d.x());
    visitE(d.e());
    }

  public void visitK(Core.K k){
    visitT(k.t());
    visitX(k.x());
    visitE(k.e());
    }

  public void visitT(Core.T t){
    visitDocs(t.docs());
    visitP(t.p());
    }
      
  public void visitDoc(Core.Doc doc){
    visitPathSel(doc.pathSel());
    visitDocs(doc.docs());
    }
     
  public void visitPathSel(Core.PathSel pathSel){
    var s0=pathSel._s();
    var x0=pathSel._x();
    visitP(pathSel.p());
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  public void visitMH(Core.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    visitDocs(docs0);
    visitT(t0);
    visitS(s0);
    visitTs(pars0);
    visitTs(exceptions0);
    }

  public void visitPCastT(Half.PCastT pCastT){
    var p0=pCastT.p();
    var t0=pCastT.t();
    visitP(p0);
    visitT(t0);
    }
    
  public void visitSlash(Half.Slash slash){
    visitSTz(slash.stz());
    }
    
  public void visitBinOp(Half.BinOp binOp){
    visitHalfXPs(binOp.es());
    }
    
  public void visitMCall(Half.MCall mCall){
    var xP0=mCall.xP();
    var s0=mCall.s();
    var es0=mCall.es();
    visitXP(xP0);
    visitS(s0);
    visitHalfEs(es0);
    }
    
  public void visitBlock(Half.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var e0=block.e();
    visitHalfDs(ds0);
    visitHalfKs(ks0);
    visitE(e0);
    }
    
  public void visitLoop(Half.Loop loop){
    visitE(loop.e());
    }
  public void visitThrow(Half.Throw thr){
    visitE(thr.e());
    }
  public void visitOpUpdate(Half.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
  public void visitD(Half.D d){
    var t0=d.t();
    var x0=d.x();
    var e0=d.e();
    visitT(t0);
    visitX(x0);
    visitE(e0);
    }
  
  public void visitK(Half.K k){
    var t0=k.t();
    var x0=k.x();
    var e0=k.e();
    visitT(t0);
    visitX(x0);
    visitE(e0);
    }

  public void visitT(Half.T t){
    visitSTz(t.stz());
    }

  public void visitCsP(Full.CsP csP){
    if(csP.cs().isEmpty()){visitP(csP._p());return;}
    assert csP._p()==null;
    visitCs(csP.cs());
    }
    
  public void visitL(Full.L l){
    var ts0=l.ts();
    var ms0=l.ms();
    var docs0=l.docs();
    visitFullTs(ts0);
    visitFullMs(ms0);
    visitFullDocs(docs0);
    }
    
  public void visitF(Full.L.F f){
    var docs0=f.docs();
    var t0=f.t();
    var s0=f.key();
    visitFullDocs(docs0);
    visitT(t0);
    visitS(s0);
    }
    
  public void visitMI(Full.L.MI mi){
    var docs0=mi.docs();
    var s0=mi.key();
    var e0=mi.e();
    visitFullDocs(docs0);
    visitS(s0);
    visitE(e0);
    }
    
  public void visitMWT(Full.L.MWT mwt){
    var docs0=mwt.docs();
    var mh0=mwt.mh();
    var _e0=mwt._e();
    visitFullDocs(docs0);
    visitMH(mh0);
    if(_e0!=null){visitE(_e0);}
    }
  
  public void visitNC(Full.L.NC nc){
    var docs0=nc.docs();
    var c0=nc.key();
    var e0=nc.e();
    visitFullDocs(docs0);
    visitC(c0);
    visitE(e0);
    }
    
  public void visitSlash(Full.Slash slash){}

  public void visitSlashX(Full.SlashX slashX){}//note, is right to not propagate on the x
  
  public void visitEString(Full.EString eString){
    visitFullEs(eString.es());
    }
  
  public void visitEPathSel(Full.EPathSel ePathSel){}

  public void visitUOp(Full.UOp uOp){
    visitE(uOp.e());
    }

  public void visitBinOp(Full.BinOp binOp){
    visitFullEs(binOp.es());
    }
    
  public void visitCast(Full.Cast cast){
    var e0=cast.e();
    var t0=cast.t();
    visitE(e0);
    visitT(t0);
    }
  
  public void visitCall(Full.Call call){
    var e0=call.e();
    var s0=call.s();
    var pars0=call.pars();
    visitE(e0);
    visitS(s0);
    visitFullPars(pars0);
    }
  
  public void visitBlock(Full.Block block){
    var ds0=block.ds();
    var ks0=block.ks();
    var ts0=block.whoopsed();
    var e0=block._e();
    visitFullDs(ds0);
    visitFullKs(ks0);
    visitFullTs(ts0);
    if(e0!=null){visitE(e0);}
    }
    
  public void visitLoop(Full.Loop loop){
    visitE(loop.e());
    }
  public void visitWhile(Full.While sWhile){
    var c0=sWhile.condition();
    var b0=sWhile.body();
    visitE(c0);
    visitE(b0);
    }
  public void visitFor(Full.For sFor){
    var ds0=sFor.ds();
    var b0=sFor.body();
    visitFullDs(ds0);
    visitE(b0);
    }
    
  public void visitThrow(Full.Throw thr){
    visitE(thr.e());
    }
    
  public void visitOpUpdate(Full.OpUpdate opUpdate){
    var x0=opUpdate.x();
    var e0=opUpdate.e();
    visitX(x0);
    visitE(e0);
    }
    
  public void visitIf(Full.If sIf){
    var _c0=sIf._condition();
    var ds0=sIf.matches();
    var then0=sIf.then();
    var _else0=sIf._else();
    if(_c0!=null){visitE(_c0);}
    visitFullDs(ds0);
    visitE(then0);
    if(_else0!=null){visitE(_else0);}
    }
  public void visitD(Full.D d){
    var tx0=d._varTx();
    var txs0=d.varTxs();
    var e0=d._e();
    if(tx0!=null){visitVarTx(tx0);}
    visitFullVarTxs(txs0);
    if(e0!=null){visitE(e0);}
    }
 
  public void visitVarTx(Full.VarTx varTx){
    var t0=varTx._t();
    var x0=varTx._x();
    visitT(t0);
    visitX(x0);
    }
  
  public void visitK(Full.K k){
    var t0=k.t();
    var x0=k._x();
    var e0=k.e();
    visitT(t0);
    if(x0!=null){visitX(x0);}
    visitE(e0);
    }
    
  public void visitPar(Full.Par par){
    var e0=par._that();
    var xs0=par.xs();
    var es0=par.es();
    if(e0!=null){visitE(e0);}
    visitXs(xs0);
    visitFullEs(es0);
    }
      
  public void visitT(Full.T t){
    var docs0=t.docs();
    var csP0=t.csP();
    visitFullDocs(docs0);
    visitCsP(csP0);
    }
    
  public void visitDoc(Full.Doc doc){
    var pathSel0=doc.pathSel();
    var docs0=doc.docs();
    visitPathSel(pathSel0);
    visitFullDocs(docs0);
    }
      
  public void visitPathSel(Full.PathSel pathSel){
    var csP0=pathSel._csP();
    var s0=pathSel._s();
    var x0=pathSel._x();
    if(csP0!=null){visitCsP(csP0);}
    if(s0!=null){visitS(s0);}
    if(x0!=null){visitX(x0);}
    }
    
  public void visitMH(Full.MH mh){
    var docs0=mh.docs();
    var t0=mh.t();
    var s0=mh.s();
    var pars0=mh.pars();
    var exceptions0=mh.exceptions();
    visitFullDocs(docs0);
    visitT(t0);
    visitS(s0);
    visitFullTs(pars0);
    visitFullTs(exceptions0);
    }
  }

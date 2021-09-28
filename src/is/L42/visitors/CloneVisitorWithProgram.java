package is.L42.visitors;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.generated.Core;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.NC;
import is.L42.generated.Full;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.Pos;

public class CloneVisitorWithProgram extends CloneVisitor {
  public CloneVisitorWithProgram(Program p){
    this.p=p;
    poss=p.top.poss();
    }
  private Program p;
  public List<Pos> poss;
  public Program p(){return this.p;}
    protected int levels=-1;
  private ArrayList<LDom> whereFromTop=new ArrayList<>();
  public ArrayList<LDom> whereFromTop(){
    return this.whereFromTop;
    }
  private LDom lastCMs=null;
  public LDom getLastCMs(){return lastCMs;}
  public CoreL pushedOp(CoreL s) {return null;}//for overriding
  public CoreL doPushedOp(CoreL s){
    Program aux=p;
    var lastPos=poss;
    poss=s.poss();
    if(lastCMs==null){
      var res=pushedOp(s);
      poss=lastPos;
      return res;
      }
    if(lastCMs instanceof C){
      p=p.push((C)lastCMs,s);
      whereFromTop.add(lastCMs);
      }
    else{
      p=p.push(s);
      whereFromTop.add(lastCMs);
      }
    levels+=1;
    var res=pushedOp(s);
    p=aux;
    levels-=1;
    whereFromTop.remove(whereFromTop.size()-1);
    poss=lastPos;
    return res;
    }
  @Override public LL visitL(Full.L s) {
    var lastPos=poss;
    poss=s.poss();
    if(lastCMs==null){
      var res=super.visitL(s);
      if(res.isFullL()){res=fullLHandler((Full.L)res);} 
      else {res=coreLHandler((CoreL)res);}
      poss=lastPos;
      return res;
      }
    Program aux=p;
    if(lastCMs instanceof C){
      p=p.push((C)lastCMs,s);
      whereFromTop.add(lastCMs);
      }
    else{
      p=p.push(s);
      whereFromTop.add(lastCMs);
      }
    levels+=1;
    var res=super.visitL(s);
    if(res.isFullL()){res=fullLHandler((Full.L)res);} 
    else{res=coreLHandler((CoreL)res);}
    p=aux;
    levels-=1;
    whereFromTop.remove(whereFromTop.size()-1);
    poss=lastPos;
    return res;
    }
  public LL fullLHandler(Full.L l){return l;/*super.visitL(l);*/}//visitL already calls super.visitL
  public CoreL coreLHandler(CoreL l){return l;/*super.visitL(l);*/}
  @Override public Full.L.NC visitNC(Full.L.NC s) {
    var lastPos=poss;
    poss=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitNC(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Full.L.MI visitMI(Full.L.MI s) {
    var lastPos=poss;
    poss=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMI(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Full.L.MWT visitMWT(Full.L.MWT s) {
    var lastPos=poss;
    poss=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMWT(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public CoreL visitL(CoreL s) {
    var lastPos=poss;
    poss=s.poss();
    if(lastCMs==null){
      assert p.top==s;
      var res=coreLHandler(super.visitL(s));
      poss=lastPos;
      return res;
      }
    Program aux=p;
    if(lastCMs instanceof C){
      p=p.push((C)lastCMs,s);
      whereFromTop.add(lastCMs);
      }
    else{
      p=p.push(s);
      whereFromTop.add(lastCMs);
      }
    levels+=1;
    var res=coreLHandler(super.visitL(s));
    p=aux;
    levels-=1;
    whereFromTop.remove(whereFromTop.size()-1);
    poss=lastPos;
    return res;
    }
  @Override public MWT visitMWT(MWT s) {
    var lastPos=poss;
    poss=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMWT(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public NC visitNC(NC s) {
    var lastPos=poss;
    poss=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitNC(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  public static class WithG extends CloneVisitorWithProgram{
    public WithG(Program p, G g){
      super(p);
      this.g=g;
      }
    public G g;
    @Override public Core.Block visitBlock(Core.Block block) {
      var ds0=block.ds();
      var ks0=block.ks();
      var e0=block.e();
      var ks=list(ks0);
      var old=g;
      g=g.plusEq(ds0);
      var ds=list(ds0);
      var e=visitE(e0);
      g=old;
      if(ds==ds0 && ks==ks0 && e==e0){return block;}
      return new Core.Block(block.pos(), ds, ks, e);
      }
    @Override public Core.K visitK(Core.K s) {
      G old=g;
      g=old.plusEq(s.x(),s.t());
      var res=super.visitK(s);
      g=old;
      return res;
      }
    @Override public MWT visitMWT(MWT s) {
      var old=g;
      g=G.of(s.mh());
      var res=super.visitMWT(s);
      g=old;
      return res;
      }
    }
  }

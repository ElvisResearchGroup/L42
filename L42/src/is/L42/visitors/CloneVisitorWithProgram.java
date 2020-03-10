package is.L42.visitors;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
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
  protected ArrayList<LDom> whereFromTop(){
    return this.whereFromTop;
    }
  private LDom lastCMs=null;
  public LDom getLastCMs(){return lastCMs;}
  @Override public LL visitL(Full.L s) {
    if(lastCMs==null){
      assert p.top==s;
      //return fullLHandler(s);//TODO: was doing double iterations before... ? mah
      var res=super.visitL(s);
      if(res.isFullL()){return fullLHandler((Full.L)res);} 
      return coreLHandler((Core.L)res);
      }
    Program aux=p;
    var lastPos=poss;
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
    else{res=coreLHandler((Core.L)res);}
    p=aux;
    levels-=1;
    whereFromTop.remove(whereFromTop.size()-1);
    poss=lastPos;
    return res;
    }
  public LL fullLHandler(Full.L l){return l;/*super.visitL(l);*/}//visitL already calls super.visitL
  public Core.L coreLHandler(Core.L l){return l;/*super.visitL(l);*/}
  @Override public Full.L.NC visitNC(Full.L.NC s) {
    var lastPos=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitNC(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Full.L.MI visitMI(Full.L.MI s) {
    var lastPos=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMI(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Full.L.MWT visitMWT(Full.L.MWT s) {
    var lastPos=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMWT(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Core.L visitL(Core.L s) {
    if(lastCMs==null){
      assert p.top==s;
      var res=super.visitL(s);
      res=coreLHandler(res);
      return res;
      }
    var lastPos=s.poss();
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
  @Override public Core.L.MWT visitMWT(Core.L.MWT s) {
    var lastPos=s.poss();
    LDom aux=lastCMs;
    lastCMs=s.key();
    var res=super.visitMWT(s);
    lastCMs=aux;
    poss=lastPos;
    return res;
    }
  @Override public Core.L.NC visitNC(Core.L.NC s) {
    var lastPos=s.poss();
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
      var ks=visitKs(ks0);
      var old=g;
      g=g.plusEq(ds0);
      var ds=visitDs(ds0);
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
    @Override public Core.L.MWT visitMWT(Core.L.MWT s) {
      var old=g;
      g=G.of(s.mh());
      var res=super.visitMWT(s);
      g=old;
      return res;
      }
    }
  }

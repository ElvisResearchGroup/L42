package is.L42.visitors;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.LDom;
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
  private List<LDom> whereFromTop=new ArrayList<>();
  protected List<LDom> whereFromTop$(){
    return this.whereFromTop;
    }
  private LDom lastCMs=null;
  public Object getLastCMs(){return lastCMs;}
  @Override public Full.L visitL(Full.L s) {
    if(lastCMs==null){
      assert p.top==s;
      var res=super.visitL(s);
      res=fullLHandler(res);
      return res;
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
    res=fullLHandler(res);
    p=aux;
    levels-=1;
    whereFromTop.remove(whereFromTop.size()-1);
    poss=lastPos;
    return res;
    }
  public Full.L fullLHandler(Full.L l){return l;}
  public Core.L coreLHandler(Core.L l){return l;}
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
    var res=super.visitL(s);
    res=coreLHandler(res);
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
  }

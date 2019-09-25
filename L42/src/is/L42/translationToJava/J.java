package is.L42.translationToJava;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.T;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.ST;


public class J extends is.L42.visitors.UndefinedCollectorVisitor{
  public J(Program p, G g, boolean wrap) {this.p=p; this.g=g; this.wrap=wrap;}
  Program p;
  G g;
  boolean wrap;
  void typeName(T t){};
  void typeName(P p){};
  void typeName(Program p){};
  void className(Program p){};
  void wrap(Boolean b){}
  void wrap(ST st){}
  @Override public void visitEX(Core.EX x){throw uc;}
  @Override public void visitPCastT(Core.PCastT pCastT){throw uc;}
  @Override public void visitEVoid(Core.EVoid eVoid){throw uc;}
  @Override public void visitMCall(Core.MCall mCall){throw uc;}
  @Override public void visitLoop(Core.Loop loop){throw uc;}
  @Override public void visitThrow(Core.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Core.OpUpdate opUpdate){throw uc;}
  @Override public void visitBlock(Core.Block block){throw uc;}
  @Override public void visitD(Core.D d){throw uc;}
  @Override public void visitK(Core.K k){throw uc;}
  @Override public void visitT(Core.T t){throw uc;}
  @Override public void visitL(Core.L l){throw uc;}
  @Override public void visitMWT(Core.L.MWT mwt){throw uc;}
  @Override public void visitMH(Core.MH mh){throw uc;}
  @Override public void visitNC(Core.L.NC nc){throw uc;}

  }

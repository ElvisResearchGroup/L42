package is.L42.constraints;

import java.util.List;

import is.L42.common.CTz;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.PCastT;
import is.L42.generated.Core.T;
import is.L42.generated.Full;
import is.L42.generated.Half;
import is.L42.generated.I;
import is.L42.generated.ST;
import is.L42.top.Top;
import is.L42.visitors.UndefinedCollectorVisitor;
import lombok.NonNull;

public class InferToCore extends UndefinedCollectorVisitor{
  I i;
  CTz ctz;
  Core.E res; 
  Top top; 
  public final Core.E compute(Half.E e){
    assert res==null;
    e.visitable().accept(this);
    assert res!=null;
    Core.E aux=res;
    res=null;
    return aux;
    }
  public final void commit(Core.E e){res=e;}
  private T infer(List<ST> stz) { return null; }//TODO:
  
  @Override public void visitEX(Core.EX x){commit(x);}
  @Override public void visitEVoid(Core.EVoid eVoid){commit(eVoid);}
  @Override public void visitL(Full.L l){
    Program p=i.p().push(i._c(),l);
    CTz ctzFrom=ctz;//TODO: from!!!
    Program pr=top.top(ctzFrom,p);//propagate errors
    ctz=ctzFrom;//TODO: from!!
    commit(pr.topCore());
    }
  @Override public void visitL(Core.L l){commit(l);}
  @Override public void visitPCastT(Half.PCastT pCastT){
    commit(new Core.PCastT(pCastT.pos(), pCastT.p(),infer(pCastT.stz())));
    }
  @Override public void visitSlashCastT(Half.SlashCastT slash){
    commit(new Core.PCastT(slash.pos(),infer(slash.stz()).p(),infer(slash.stz1()));
    }
  @Override public void visitMCall(Half.MCall mCall){
    throw uc;
    }

  @Override public void visitBinOp(Half.BinOp binOp){
    throw uc;//TODO:
    }
  @Override public void visitBlock(Half.Block block){throw uc;}
  @Override public void visitLoop(Half.Loop loop){throw uc;}
  @Override public void visitThrow(Half.Throw thr){throw uc;}
  @Override public void visitOpUpdate(Half.OpUpdate opUpdate){throw uc;}
  @Override public void visitD(Half.D d){throw uc;}
  @Override public void visitK(Half.K k){throw uc;}

  @Override public void visitSTMeth(ST.STMeth stMeth){throw uc;}
  @Override public void visitSTOp(ST.STOp stOp){throw uc;}


}

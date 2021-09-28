package is.L42.visitors;

import static is.L42.tools.General.range;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.flyweight.CoreL;
import is.L42.generated.Full;
import is.L42.generated.Full.D;
import is.L42.generated.ThrowKind;
public class Returning extends UndefinedCollectorVisitor{
  public static void ofBlock(Full.Block b){
    assert b.isCurly();    
    Full.E last=b.ds().get(b.ds().size()-1)._e();
    if(!Returning.of(last)){
      throw new EndError.NotWellFormed(last.poss(),
        ErrMsg.lastStatementDoesNotGuaranteeBlockTermination());
      }
    for(int i:range(b.ds().size()-1)){
      if(!Returning.of(b.ds().get(i)._e())){continue;}
      throw new EndError.NotWellFormed(b.ds().get(i)._e().poss(),
        ErrMsg.deadCodeAfter(i));
      }
    for(int i:range(b.ks().size())){
      if(Returning.of(b.ks().get(i).e())){continue;}
      throw new EndError.NotWellFormed(b.ks().get(i).e().poss(),
        ErrMsg.catchStatementDoesNotGuaranteeBlockTermination(i));
      }
    boolean[]hasRes={false};
    boolean[]hasCatchRet={false};
    b.accept(new PropagatorCollectorVisitor(){
      @Override public void visitThrow(Full.Throw thr){
        super.visitThrow(thr);
        hasRes[0]|=thr.thr().equals(ThrowKind.Return);
        }
      @Override public void visitK(Full.K k){
        super.visitK(k);
        hasCatchRet[0]|=ThrowKind.Return.equals(k._thr());
        }
      @Override public void visitL(Full.L l){}
      @Override public void visitL(CoreL l){}
      @Override public void visitBlock(Full.Block b0){
        if(!b0.isCurly()||b==b0){super.visitBlock(b0);}
        }
    });
    if(hasRes[0] && !hasCatchRet[0]){return;}
    if(!hasRes[0]) {throw new EndError.NotWellFormed(b.poss(),
      ErrMsg.curlyWithNoReturn());}
    throw new EndError.NotWellFormed(b.poss(),
        ErrMsg.curlyWithCatchReturn());
    }
  public static boolean of(Full.E e){
    assert e!=null;
    var r=new Returning();
    try{r.visitE(e);}
    catch(UndefinedCase uc){return false;}
    return true;    
    }
  @Override public void visitLoop(Full.Loop l){}
  @Override public void visitThrow(Full.Throw l){}
  @Override public void visitIf(Full.If i){
    if(i._else()==null){super.visitIf(i);}
    visitE(i.then());
    visitE(i._else());
    }
  @Override public void visitBlock(Full.Block b){
    if(b.isCurly()){uc();}
    if(b._e()==null){visitBlockNoE(b);return;}
    visitE(b._e());
    for(var k:b.ks()){visitE(k.e());}
    }
  private void visitBlockNoE(Full.Block b){
    if(b.ds().isEmpty() || b.dsAfter()!=b.ds().size()){super.visitBlock(b);return;}
    D last=b.ds().get(b.ds().size()-1);
    if(last._e()==null || last._varTx()!=null){super.visitBlock(b);return;}
    var e=last._e();
    visitE(e);
    for(var k:b.ks()){visitE(k.e());}    
    }
  }

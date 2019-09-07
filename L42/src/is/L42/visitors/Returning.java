package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;
class HasReturn extends PropagatorCollectorVisitor{
  boolean find=false;
  public static boolean of(Full.E e){
    var v=new HasReturn();
    v.visitE(e);
    return v.find;
    }
    @Override public void visitThrow(Full.Throw thr){
      if(thr.thr()==ThrowKind.Return){find=true;}
      }
    @Override public void visitL(Full.L l){}
    @Override public void visitL(Core.L l){}
    @Override public void visitBlock(Full.Block b){
      if(!b.isCurly()){super.visitBlock(b);}
      }
  }
public class Returning extends UndefinedCollectorVisitor{
  public static void ofBlock(Full.Block b){
    assert b.isCurly();    
    Full.E last=b.ds().get(b.ds().size()-1)._e();
    if(!Returning.of(last)){
      throw new WellFormedness.NotWellFormed(last.poss(),
        "last statement does not guarantee block termination");
      }
    for(int i:range(b.ds().size()-1)){
      if(!Returning.of(b.ds().get(i)._e())){continue;}
      throw new WellFormedness.NotWellFormed(b.ds().get(i)._e().poss(),
        "dead code after the statement "+i+" of the block");
      }
    for(int i:range(b.ks().size())){
      if(Returning.of(b.ks().get(i).e())){continue;}
      throw new WellFormedness.NotWellFormed(b.ks().get(i).e().poss(),
        "catch statement "+i+" does not guarantee block termination");
      }
    var res=Stream.concat(b.ds().stream().map(d->d._e()), b.ks().stream().map(k->k.e()))
    .filter(HasReturn::of).findFirst();
    if(!res.isEmpty()){return;}
    throw new WellFormedness.NotWellFormed(b.poss(),
      "curly block do not have any return statement");
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
    if(b._e()==null){super.visitBlock(b);}
    visitE(b._e());
    visitFullKs(b.ks());
    }
  }

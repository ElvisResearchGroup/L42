package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;

public class WellFormedness extends PropagatorCollectorVisitor{
  @SuppressWarnings("serial")
  public static class NotWellFormed extends EndError{
    public NotWellFormed(Pos pos, String msg) { super(pos, msg);}
    }
  public static boolean of(Visitable<?> v){
    var tos=new WellFormedness();
    v.accept(tos);
    return true;
    }

  Pos lastPos;
  private final void err(String msg){
    throw new NotWellFormed(lastPos,msg);
    }
  @Override public void visitE(Full.E e){lastPos=e.pos();super.visitE(e);}

  @Override public void visitVarTx(Full.VarTx d){
    assert lastPos!=null;
    super.visitVarTx(d);
    if(!d.isVar()){return;}
    Mdf _mdf=d._mdf();
    if(d._t()!=null){_mdf=d._t()._mdf();}
    if(_mdf==null){return;}
    if(!_mdf.isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err("var bindings can not be "+_mdf);
    }
  
  @Override public void visitD(Core.D d){
    lastPos=d.e().pos();
    super.visitD(d);
    if(!d.isVar()){return;}
    if(!d.t().mdf().isIn(Mdf.Capsule,Mdf.ImmutableFwd,Mdf.MutableFwd)){return;}
    err("var bindings can not be "+d.t().mdf());    
    }    
  @Override public void visitS(S s){
    super.visitS(s);
    okXs(s.xs());
    }
  private void okXs(List<X>xs) { 
    for(X x:xs){
      if(x.inner().equals("this")){err("'this' can not be used as a name, in "+xs);}
      }
    long size=xs.stream().map(X::inner).distinct().count();
    if(size!=xs.size()){err("duplicated name in "+xs);} 
    }
  public void visitPar(Full.Par par){
    super.visitPar(par);
    okXs(par.xs());
    if(par._that()==null){return;}
    if(par.xs().stream().map(X::inner).noneMatch(x->x.equals("that"))){return;}
    err("duplicated name in "+par.xs()+": 'that' is already passed as first argument");
    }
  @Override public void visitBlock(Full.Block b){
    lastPos=b.pos();
    super.visitBlock(b);
    var domDs=FV.domFullDs(b.ds());
    okXs(domDs);
    declaredVariableNotUsedInCatch(b, domDs);
    declaredVariableNotRedeclared(b, domDs);
    if(b.isCurly()){Returning.ofBlock(b);}
    else{
      int minus=0;
      if(b._e()==null){
        var last=b.ds().get(b.ds().size()-1);
        assert last._varTx()==null && last.varTxs().isEmpty();
        minus=1;
        }
      for(int i:range(b.ds().size()-minus)){
        if(!Returning.of(b.ds().get(i)._e())){continue;}
        lastPos=b.ds().get(i)._e().pos();
        err("dead code after the statement "+i+" of the block");
        }
      for(int i:range(1,b.ds().size())){
        var di=b.ds().get(i);
        var dj=b.ds().get(i-1);
        if(di._varTx()!=null || !di.varTxs().isEmpty()){continue;}
        if(b.dsAfter()==i){continue;}
        if(!CheckBlockNeeded.of(dj._e(),true)){continue;}
        lastPos=dj._e().pos();
        err("expression need to be enclose in block to avoid ambiguities");
        }
      if(b.ks().isEmpty()){return;}
      if(b.ds().size()<=b.dsAfter()){
        if(b._e()==null){return;}
        }
      else{  
        Full.D firstAfter=b.ds().get(b.dsAfter());
        if(firstAfter._varTx()!=null || !firstAfter.varTxs().isEmpty()){return;}
        }
      Full.K kLast=b.ks().get(b.ks().size()-1);
      if(!CheckBlockNeeded.of(kLast.e(),false)){return;}
      lastPos=kLast.e().pos();
      err("expression need to be enclose in block to avoid ambiguities");
      }      
    }
  private void declaredVariableNotRedeclared(Full.Block b, List<X> domDs) {
    var redefined=Stream.concat(Stream.concat(
      b.ds().stream().map(d->d._e()),//ds
      b.ks().stream().map(k->k.e())//ks
      ),Stream.of(b._e()))//e
     .filter(e->e!=null)
     .flatMap(e->Bindings.of(e.visitable()).stream())
     .filter(x->domDs.contains(x))
     .findFirst();
    if(redefined.isEmpty()){return;}
    err("binding "+redefined.get()+" is internally redefined");
    }
    private void declaredVariableNotUsedInCatch(Full.Block b, List<X> domDs) { for(var ki:b.ks()){
      var inside=FV.of(ki).stream().filter(x->domDs.contains(x)).findFirst();
      if(inside.isEmpty()){continue;}
      err("binding "+inside.get()+ " used in catch; it may not be initialized");
      } }
    /*
    * FULL/CORE B:
    no repetition in B.Ds.xs//all of the Ds, even if divided in 2 groups
    FV(Ks) disjoint dom(B.Ds)
    dom(B.Ds) disjoint bindings(B.Ds.es,B.Ks.es,e)
    if B={ Ds1 Ks WHOOPS? Ds2} then returning({ Ds1 Ks WHOOPS? Ds2})
    if B=(Ds1 Ks WHOOPS? Ds2 e) then forall D in Ds1,Ds2: not returning(D)
    if B=(Ds1 e Ks WHOOPS?) then forall D in Ds1: not returning(D) 
    if B=(_ D e _), then noBlockNeeded(D.e)
    if B=(_ K e _), then noBlockNeeded(K.e)
    */
  
  @Override public void visitBlock(Core.Block b){
    lastPos=b.pos();
    super.visitBlock(b);
    }    
  
  @Override public void visitL(Full.L l){
    lastPos=l.pos();
    super.visitL(l);
    }
  @Override public void visitL(Core.L l){
    lastPos=l.pos();
    super.visitL(l);
    }

  }

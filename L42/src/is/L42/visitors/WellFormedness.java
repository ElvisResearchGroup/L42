package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushTopL;
import static is.L42.tools.General.range;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.HasVisitable;
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
    declaredVariableNotUsedInCatch(b.ks(), domDs);
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
    declaredVariableNotRedeclared(Stream.concat(Stream.concat(
      b.ds().stream().map(d->d._e()),//ds
      b.ks().stream().map(k->k.e())//ks
      ),Stream.of(b._e())),//e
      b.ks().stream().map(k->k._x()),
      domDs
      );
   }
  private void declaredVariableNotRedeclared(Core.Block b, List<X> domDs) {
    declaredVariableNotRedeclared(Stream.concat(Stream.concat(
      b.ds().stream().map(d->d.e()),//ds
      b.ks().stream().map(k->k.e())//ks
      ),Stream.of(b.e())),//e
      b.ks().stream().map(k->k.x()),
      domDs
      );
    }
  private void declaredVariableNotRedeclared(Stream<HasVisitable>s,Stream<X>kxs, List<X> domDs) {
    var redefined=Stream.concat(kxs,s
       .filter(e->e!=null)
       .flatMap(e->Bindings.of(e.visitable()).stream())
       )
     .filter(x->domDs.contains(x))
     .findFirst();
    if(redefined.isEmpty()){return;}
    err("binding "+redefined.get()+" is internally redefined");
    }

    private void declaredVariableNotUsedInCatch(List<? extends Visitable<?>> ks, List<X> domDs) {
      for(var ki:ks){
        var inside=FV.of(ki).stream().filter(x->domDs.contains(x)).findFirst();
        if(inside.isEmpty()){continue;}
        err("binding "+inside.get()+ " used in catch; it may not be initialized");
        }
      }
  @Override public void visitBlock(Core.Block b){
    lastPos=b.pos();
    super.visitBlock(b);
    var domDs=FV.domDs(b.ds());
    okXs(domDs);
    declaredVariableNotUsedInCatch(b.ks(), domDs);
    declaredVariableNotRedeclared(b, domDs);
    }    
  @Override public void visitK(Core.K k){
    lastPos=k.e().pos();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k.x());  
    }
  @Override public void visitK(Full.K k){
    lastPos=k.e().pos();
    super.visitK(k);
    wfKCommon(k.e().visitable(), k._x());
    }
  private void wfKCommon(Visitable<?> v, X x) { var bindings=Bindings.of(v);
  if(x==null){return;}
  if(bindings.contains(x)){
    err("binding "+x+" is internally redefined");
    }
  if(x.inner().equals("this")){
    err("'this' can not be used as a name");
    } }

  @Override public void visitMI(Full.L.MI mi){
    lastPos=mi.pos();
    super.visitMI(mi);
    var l=ContainsFullL._of(mi.e());
    if(l==null){return;}
    lastPos=l.pos();
    err("Method body can not contain a full library literal");
    }

  @Override public void visitMWT(Full.L.MWT mwt){
    lastPos=mwt.pos();
    super.visitMWT(mwt);
    if(mwt._e()==null){return;}
    var l=ContainsFullL._of(mwt._e());
    if(l==null){return;}
    lastPos=l.pos();
    err("Method body can not contain a full library literal");
    }
  @Override public void visitMWT(Core.L.MWT mwt){
    lastPos=mwt.pos();
    super.visitMWT(mwt);
    if(mwt._e()==null){return;}
    var fv=FV.of(mwt._e().visitable());
    var pars=pushTopL(X.thisX,mwt.mh().s().xs());
    var extra=fv.stream().filter(x->!pars.contains(x)).findFirst();
    if(extra.isPresent()){
      err("Used binding is not in scope: "+extra.get());}
    var parT=mwt.mh().parsWithThis();
    for(var i:range(pars)){
      if(!parT.get(i).mdf().isCapsule()){continue;}
      var xi=pars.get(i);
      long count=fv.stream().filter(x->x.equals(xi)).count();
      if(count<=1){continue;}
      err("capsule binding "+xi+" used more then once");
      }
    }
  @Override public void visitMH(Full.MH mh){
    super.visitMH(mh);
    if(mh._mdf()!=null && mh._mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err("method modifier can not be fwd imm or fwd mut");} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t()._mdf();
    if(tmdf!=null && ps.stream().anyMatch(ti->ti._mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err("unusable fwd parameter given return type "+mh.t()._mdf().inner);}
      }
    if(ps.stream().anyMatch(ti->ti._mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err("unusable fwd parameter given return type "+(tmdf==null?"imm":tmdf.inner));}
      }
    if(tmdf!=null && tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti._mdf()!=null && ti._mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err("invalid fwd return type since there is no fwd parameter");}
      }   
    }
  @Override public void visitMH(Core.MH mh){
    super.visitMH(mh);
    if(mh.mdf().isIn(Mdf.ImmutableFwd, Mdf.MutableFwd)){
      err("method modifier can not be fwd imm or fwd mut");} 
    var ps=mh.parsWithThis();
    var tmdf=mh.t().mdf();
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.ImmutableFwd)){
      boolean res=tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd,Mdf.Immutable,Mdf.ImmutableFwd);
      if(!res){err("unusable fwd parameter given return type "+mh.t().mdf().inner);}
      }
    if(ps.stream().anyMatch(ti->ti.mdf()==Mdf.MutableFwd)){
      boolean res=tmdf!=null && tmdf.isIn(Mdf.Mutable,Mdf.MutableFwd);
      if(!res){err("unusable fwd parameter given return type "+tmdf.inner);}
      }
    if(tmdf.isIn(Mdf.ImmutableFwd,Mdf.MutableFwd)){
      if(ps.stream().noneMatch(ti->ti.mdf().isIn(Mdf.ImmutableFwd,Mdf.MutableFwd))){
        err("invalid fwd return type since there is no fwd parameter");}
      }   
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

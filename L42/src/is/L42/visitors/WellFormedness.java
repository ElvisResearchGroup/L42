package is.L42.visitors;

import java.util.List;

import is.L42.common.EndError;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;

public class WellFormedness extends CollectorVisitor{
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
      if(x.inner().equals("this")){err("'this' can not be used as a parameter name, in "+xs);}
      }
    long size=xs.stream().map(X::inner).distinct().count();
    if(size!=xs.size()){err("duplicated parameter name in "+xs);} 
    }
  public void visitPar(Full.Par par){
    super.visitPar(par);
    okXs(par.xs());
    if(par._that()==null){return;}
    if(par.xs().stream().map(X::inner).noneMatch(x->x.equals("that"))){return;}
    err("duplicated parameter name in "+par.xs()+": 'that' is already passed as first argument");
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

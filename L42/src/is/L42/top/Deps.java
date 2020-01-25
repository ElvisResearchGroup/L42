package is.L42.top;

import static is.L42.tools.General.bug;

import java.util.ArrayList;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.ThrowKind;
import is.L42.generated.P.NCs;
import is.L42.typeSystem.TypeManipulation;
import is.L42.visitors.Accumulate;

public class Deps extends Accumulate<ArrayList<P.NCs>>{
Program p0;
ArrayList<P.NCs> typePs;
ArrayList<P.NCs> cohePs;
public Deps(Program p0, ArrayList<P.NCs> typePs, ArrayList<P.NCs> cohePs){
  this.p0=p0;this.typePs=typePs;this.cohePs=cohePs;
  }
public ArrayList<P.NCs> empty(){return typePs;}
@Override public void visitL(Full.L l){throw bug();}
private void csAux(Program p,ArrayList<C> cs,Core.L l,Core.L topL){
  TypeManipulation.skipThis0(l.info().typeDep().stream()
    .map(e->p.from(e,0, cs)),topL).forEach(typePs::add);
  TypeManipulation.skipThis0(l.info().coherentDep().stream()
    .map(e->p.from(e,0, cs)),topL).forEach(cohePs::add);
  Program pi=p.push(l);
  for(C c: l.domNC()){
    cs.add(c);
    csAux(pi,cs,l.c(c),topL);
    cs.remove(cs.size()-1);
    }
  }
@Override public void visitL(Core.L l){
  TypeManipulation.skipThis0(l.info().typeDep().stream(),l).forEach(typePs::add);
  TypeManipulation.skipThis0(l.info().coherentDep().stream(),l).forEach(cohePs::add);
  ArrayList<C> cs=new ArrayList<>();
  Program pi=p0.push(l);
  for(C c: l.domNC()){//a little of code duplication removes the map on the streams
    cs.add(c);
    csAux(pi,cs,l.c(c),l);
    cs.remove(cs.size()-1);
    }
  }
@Override public void visitP(P p){
  if(p.isNCs()){typePs.add(p.toNCs());}
  }
@Override public void visitPCastT(Core.PCastT p){
  super.visitPCastT(p);
  if(p.t().p()==P.pAny){return;}
  if(p.p().isNCs()){cohePs.add(p.p().toNCs());}
  }
@Override public void visitK(Core.K k){
  super.visitK(k);
  if(k.thr()!=ThrowKind.Return){return;}
  if(!k.t().mdf().isClass()){return;}
  if(k.t().p().isNCs()){cohePs.add(k.t().p().toNCs());}
  }

}
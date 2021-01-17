package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.util.List;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.visitors.CloneVisitor;

public class From extends CloneVisitor{
  public From(Program program, P.NCs source, int j) {
    this.program=program; this.source=source; this.j=j; 
    }
  Program program; P.NCs source; int j;
  public int j(){return j;}
  public Program program(){return program;}
  @Override public Full.L visitL(Full.L l){throw bug();}
  public Core.L superVisitL(Core.L l){return super.visitL(l);}//Needed for code reuse
  @Override public Core.L visitL(Core.L l){
    int oldJ=j;
    Program oldP=program;
    j+=1;
    program=program.push(l);
    var res=super.visitL(l);
    j=oldJ;
    program=oldP;
    return res;
    }
  @Override public Core.L.NC visitNC(Core.L.NC nc){
    nc=nc.withDocs(visitDocs(nc.docs()));
    int oldJ=j;
    Program oldP=program;
    j+=1;
    program=program.push(nc.key(),nc.l());
    var res=super.visitL(nc.l());
    j=oldJ;
    program=oldP;
    return nc.withL(res);
    }
  @Override public P visitP(P p){
    if(!p.isNCs()){return p;}
    int n=p.toNCs().n();
    if(n<j){return p;}
    P.NCs p0=p.toNCs().withN(n-j);
    P.NCs p1=program.from(p0, source);
    return p1.withN(p1.n()+j);
    }
  @Override public List<P.NCs> visitInfoAlsoUnique(List<P.NCs> ps){
    return L(ps,(c,p)->{
      var pp=this.visitP(p);
      if(!pp.isNCs()){return;}
      var pi=pp.toNCs();
      if(!c.contains(pp)){c.add(pi);}
      if(!pi.hasUniqueNum()){return;}
      var csCut=L(pi.cs().stream().takeWhile(ci->!ci.hasUniqueNum()));
      pi=pi.withCs(csCut);
      if(!c.contains(pi)){c.add(pi);}
      });
    }
  @Override public List<P.NCs> visitInfoWatched(List<P.NCs> ps){
    return L(ps,(c,p)->{
      var pp=this.visitP(p);
      if(!pp.isNCs()){return;}
      var pi=pp.toNCs();
      if(!pi.hasUniqueNum()){
        assert !pi.equals(P.pThis0) && !c.contains(pi);
        c.add(pi);
        return;
        }
      var csCut=L(pi.cs().stream().takeWhile(ci->!ci.hasUniqueNum()));
      pi=pi.withCs(csCut);
      if(!pi.equals(P.pThis0) && !c.contains(pi)){c.add(pi);}
      });
    }
  }
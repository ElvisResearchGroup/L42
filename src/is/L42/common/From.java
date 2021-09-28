package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.util.ArrayList;
import java.util.List;

import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core.NC;
import is.L42.generated.Full;
import is.L42.top.Deps;
import is.L42.visitors.CloneVisitor;

public class From extends CloneVisitor{
  public From(Program program, P.NCs source, int j) {
    this.program=program; this.source=source; this.j=j; 
    }
  Program program; P.NCs source; int j;
  
  public int j(){return j;}
  public Program program(){return program;}
  @Override public Full.L visitL(Full.L l){throw bug();}
  public CoreL superVisitL(CoreL l){return super.visitL(l);}//Needed for code reuse
  @Override public CoreL visitL(CoreL l){
    int oldJ=j;
    Program oldP=program;
    j+=1;
    program=program.push(l);
    var res=super.visitL(l);
    j=oldJ;
    program=oldP;
    return res;
    }
  @Override public NC visitNC(NC nc){
    nc=nc.withDocs(list(nc.docs()));
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
      var piRoot=Deps._publicRoot(pi);
      if(piRoot==null){return;}
      if(!c.contains(piRoot)){c.add(piRoot);}
      });
    }
  private ArrayList<P.NCs> toWatch=new ArrayList<>();
  public List<P.NCs> visitInfoTypeDep(List<P.NCs> ps){
      toWatch.clear();
      return L(ps,(c,p)->{
          var pp=this.visitP(p);
          if(!pp.isNCs()){return;}
          var pi=pp.toNCs();
          if(!c.contains(pp)){c.add(pi);}
          var piRoot=Deps._publicRoot(pi);
          if(piRoot==null){return;}
          if(!c.contains(piRoot)){
            toWatch.add(piRoot);
            c.add(piRoot);
            }
          });
      }
  @Override public List<P.NCs> visitInfoWatched(List<P.NCs> ps){
    return L(c->{
      for(var p:ps){
        var pp=this.visitP(p);
        if(!pp.isNCs()){continue;}
        var pi=pp.toNCs();
        var piRoot=Deps._publicRoot(pi);
        if(piRoot==null){
          assert !pi.equals(P.pThis0) && !c.contains(pi);
          c.add(pi);
          continue;
          }
        if(!piRoot.equals(P.pThis0) && !c.contains(piRoot)){c.add(piRoot);}
        }
      for(var p:toWatch){
        if(!p.equals(P.pThis0) && !c.contains(p)){c.add(p);}  
        }
      });
    }
  }
package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.util.List;

import is.L42.common.Err;
import is.L42.common.PTails;
import is.L42.common.Program;
import is.L42.generated.Full;
import is.L42.generated.Full.CsP;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class Init {
  //in the formalism, it is from L to L, here with p to p,
  //we can parsing initialised programs.
  //this also twist pTails with C so that the C={} have the right content
  public static Program init(Program p){
    var ll=initTop(p);
    if(p.pTails.isEmpty()){return p.update(ll);}
    var tail=p.update(ll).pop();
    tail=init(tail);
    if(!p.pTails.hasC()){return tail.push(ll);}
    return tail.push(p.pTails.c(),ll);
    }
  public static LL initTop(Program pStart){
    var res=pStart.top.visitable().accept(new CloneVisitorWithProgram(pStart){
      @Override public Full.L visitL(Full.L s) {
        if(!s.isDots()){return super.visitL(s);}
        throw bug();
        }
      @Override public CsP visitCsP(CsP s) {
        if(s._p()!=null){return s.with_p(min(s._p()));}
        return new CsP(s.pos(),L(),min(p().resolve(s.cs(),s.poss())));
        }
      @Override public Full.T visitT(Full.T s) {
        s=super.visitT(s);
        if(s._p()!=null){return s.with_p(min(s._p()));}
        return new Full.T(s._mdf(),s.docs(),L(),min(p().resolve(s.cs(),poss)));
        }    
      @Override public Full.PathSel visitPathSel(Full.PathSel s) {
        if(s._p()!=null){return s.with_p(min(s._p()));}
        return new Full.PathSel(L(),min(p().resolve(s.cs(),poss)),s._s(),s._x());
        }
      private P min(P p){
        if(!p.isNCs()){return p;}
        var p0=p.toNCs();
        if(p0.n()>p().dept()){
          throw new Program.PathNotExistent(poss,Err.thisNumberOutOfScope(p));
          }
        return p().minimize(p0);
        }
      });
    return res;
    }
}

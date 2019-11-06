package is.L42.top;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;

import java.util.List;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.PTails;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Full.CsP;
import is.L42.translationToJava.Loader;
import is.L42.generated.LDom;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class Init {
  public Init(String s){this(Parse.program("-dummy-",s).res);}
  public final Top top;
  public final Program p;
  public Init(Full.L l){this(Program.flat(l));}
  protected Top makeTop(FreshNames f){
    return new Top(f,0,new Loader());//but can be overridden as a testing handler
    }
  public Init(Program program){
    FreshNames f=new FreshNames();
    top=makeTop(f);
    Program res=init(program,f);
    assert res.top.wf();
    p=res;
    }
  //in the formalism, it is from L to L, here with p to p,
  //we can parsing initialised programs.
  //this also twist pTails with C so that the C={} have the right content
  public static Program init(Program p,FreshNames f){
    var ll=initTop(p,f);
    if(p.pTails.isEmpty()){return p.update(ll);}
    var tail=p.update(ll).pop();
    tail=init(tail,f);
    if(!p.pTails.hasC()){return tail.push(ll);}
    return tail.push(p.pTails.c(),ll);
    }
  public static LL initTop(Program pStart,FreshNames f){
    var res=pStart.top.visitable().accept(new CloneVisitorWithProgram(pStart){
      @Override public Full.L fullLHandler(Full.L s){
        if(s.isDots()){throw bug();}//TODO: will need to be handled in the visitL instead
        var this0s=L(s.ts().stream().filter(this::invalidAfter));
        if(this0s.isEmpty()){return s;}
        throw new EndError.InvalidImplements(s.poss(),Err.nestedClassesImplemented(this0s));
        }
      @Override public Core.L coreLHandler(Core.L s){
        var this0s=L(s.ts().stream().filter(this::invalidAfter));
        if(this0s.isEmpty()){return s;}
        throw new EndError.InvalidImplements(s.poss(),Err.nestedClassesImplemented(this0s));
        }
      @Override public X visitX(X x){
        f.addToUsed(x);
        return x;
        }
      private boolean invalidAfter(Full.T t){
        assert t._p()!=null;
        return invalidAfter(t._p());
        }
      private boolean invalidAfter(Core.T t){
        return invalidAfter(t.p());
        }
      private boolean invalidAfter(P p0){
        if(!pStart.pTails.isEmpty()){return false;}
        if(!p0.isNCs()){return false;}
        P.NCs p=p0.toNCs();
        if (p.n()==0){return true;}
        if(p.cs().isEmpty()){return false;}
        C c=p.cs().get(0);
        LL l=this.p().pop(p.n()).top;
        LDom d=this.whereFromTop().get(this.whereFromTop().size()-p.n());
        int dn=-1;
        int cn=-1;
        if(l.isFullL()){
          var fl=(Full.L)l;
          for(int i:range(fl.ms())){
            var ki=fl.ms().get(i).key();
            if(ki.equals(d)){dn=i+1;}
            if(ki.equals(c)){cn=i+1;}
            }
          }
        else{
          var cl=(Core.L)l;
          if(d instanceof S){dn=0;}
          for(int i:range(cl.ncs())){
            var ki=cl.ncs().get(i).key();
            if(ki.equals(d)){dn=i+1;}
            if(ki.equals(c)){cn=i+1;}
            }      
          }
        assert dn!=-1;
        assert cn!=-1;
        assert dn!=cn;
        return dn<cn;
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
          throw new EndError.PathNotExistent(poss,Err.thisNumberOutOfScope(p));
          }
        return p().minimize(p0);
        }
      });
    return res;
    }
}

package is.L42.common;

import static is.L42.tools.General.L;
import static is.L42.tools.General.popL;

import java.util.List;

import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.LL;
import is.L42.generated.P;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

public class Program implements Visitable<Program>{
  @Override public Program accept(CloneVisitor v) {return v.visitProgram(this); }
  @Override public void accept(CollectorVisitor v) {v.visitProgram(this);}
  @Override public boolean wf() {return Constants.wf.test(this);}
  @Override public String toString() {return Constants.toS.apply(this);}
  public final LL top;
  public final PTails pTails;
  public Program(LL top,PTails pTails){this.top=top;this.pTails=pTails;}
  public static Program flat(LL top){return new Program(top,PTails.empty);}
  public static final Core.L emptyL=new Core.L(L(),false,L(),L(),L(),Core.L.Info.empty,L());
  public static final Core.L emptyLInterface=emptyL.withInterface(true);
  public LL of(P path){
    if(path==P.pAny){return emptyLInterface;}
    if(path==P.pVoid){return emptyL;}
    if(path==P.pLibrary){return emptyL;}
    return this.pop(path.toNCs().n()).top.cs(path.toNCs().cs());
    }
  public Program pop(int n){
    assert n>=0;
    if(n==0){return this;}
    return this.pop().pop(n-1);
    }
  public Program pop(){
    if(!pTails.hasC()){return new Program(pTails.coreL(),pTails.tail());}
    var newTop=pTails.ll().withCs(L(pTails.c()),
      nc->nc.withE(pTails.ll()),
      nc->nc.withL((Core.L)pTails.ll())
      );
    return new Program(newTop,pTails.tail());
    }
  public Program push(C c,LL ll){return new Program(ll,pTails.pTailC(c, top));}
  public Program push(LL ll){return new Program(ll,pTails.pTailSingle((Core.L)top));}
  public Program push(C c){return push(c,top.c(c));}
  public Program update(LL ll){return new Program(ll,pTails);}
  public P minimize(P path){
    if(!(path instanceof P.NCs)){return path;}
    var p=path.toNCs();
    if(p.n()==0){return path;}
    
    if(p.n()==1){return baseMinimize(p);}
    P.NCs tmp=pop().minimize(p.withN(p.n()-1)).toNCs();
    tmp=tmp.withN(tmp.n()+1);
    if(tmp.n()==1){return baseMinimize(tmp);}
    return tmp;
    }
  private P.NCs baseMinimize(P.NCs p) {
    assert !pTails.isEmpty();
    if(!p.cs().isEmpty()){return p;}
    if(!pTails.hasC()){return p;}
    if(!pTails.c().equals(p.cs().get(0))){return p;}
    return P.of(p.n()-1,popL(p.cs()));
    }
  }
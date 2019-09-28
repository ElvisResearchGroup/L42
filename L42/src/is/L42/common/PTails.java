package is.L42.common;

import static is.L42.tools.General.bug;

import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.LL;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

public abstract class PTails implements Visitable<PTails>{
  @Override public PTails accept(CloneVisitor v) {return v.visitPTails(this); }
  @Override public void accept(CollectorVisitor v) {v.visitPTails(this);}
  @Override public boolean wf() {return Constants.wf.test(this);}
  @Override public String toString() {return Constants.toS.apply(this);}
  private PTails(){}
  public boolean isEmpty(){return this==empty;}
  public boolean hasC(){throw bug();}
  public C c(){throw bug();}
  public Core.L coreL(){throw bug();}
  public LL ll(){return coreL();}
  public PTails tail(){throw bug();}
  public static final PTails empty=new PTails(){};
  public PTails pTailSingle(Core.L l){return new PTails(){
    public Core.L coreL(){return l;}
    public boolean hasC(){return false;}
    public PTails tail(){return PTails.this;}
    };}
  public PTails pTailC(C c, LL ll){return new PTails(){
    public boolean hasC(){return true;}
    public C c(){return c;}
    public LL ll(){return ll;}
    public Core.L coreL(){
      if(ll.isFullL()){throw bug();}
      return (Core.L)ll;
      }
    public PTails tail(){return PTails.this;}
    };}
  }
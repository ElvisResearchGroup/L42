package is.L42.common;

import static is.L42.tools.General.bug;

import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.generated.LL;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;

public abstract class PTails implements Visitable<PTails>{
  @Override public PTails accept(CloneVisitor v) {return v.visitPTails(this); }
  @Override public void accept(CollectorVisitor v) {v.visitPTails(this);}
  @Override public boolean wf() {return Constants.wf.test(this);}
  @Override public String toString() {return Constants.toS.apply(this);}
  public String printCs(){
    if(isEmpty()){return "";}
    var res=this.tail().printCs();
    if(!res.isEmpty()){res+=".";}
    if(hasC()){return res+c();}
    return res+"<No C>";
    }  
  private PTails(){}
  //public boolean isEmpty(){return this==empty;}//sadly, serialization/deserialzation breaks this
  public boolean isEmpty(){return true;}
  public boolean hasC(){throw bug();}
  public C c(){throw bug();}
  public CoreL coreL(){throw bug();}
  public LL ll(){throw bug();}
  public PTails tail(){throw bug();}
  public static final PTails empty=new PTails(){};
  public PTails pTailSingle(LL l){return new PTails(){
    public CoreL coreL(){return (CoreL)l;}
    public LL ll(){return l;}
    public boolean hasC(){return false;}
    public boolean isEmpty(){return false;}
    public PTails tail(){return PTails.this;}
    };}
  public PTails pTailC(C c, LL ll){return new PTails(){
    public boolean hasC(){return true;}
    public boolean isEmpty(){return false;}
    public C c(){return c;}
    public LL ll(){return ll;}
    public CoreL coreL(){
      if(ll.isFullL()){throw bug();}
      return (CoreL)ll;
      }
    public PTails tail(){return PTails.this;}
    };}
  }
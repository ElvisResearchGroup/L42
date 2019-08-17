package is.L42.tests;

import java.util.function.Function;
import java.util.function.Predicate;

public class TestToS {
  static E evis(E e,CV cv){return e.visitable().accept(cv);}
}

interface Visitable<R>{
  R accept(CV cv);
  void accept(PV v);
  boolean wf();
}
interface E{
  Visitable<? extends E> visitable();
  }
/*lbc*/class 
A implements E,Visitable<A>{@Override public Visitable<A>visitable(){return this;}@Override public A accept(CV cv){return cv.visitA(this);}@Override public void accept(PV pv){pv.visitA(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  /*fields*/}
class B implements E,Visitable<B>{
  @Override public Visitable<B> visitable(){return this;}
  @Override public B accept(CV cv){return cv.visitB(this);}
  @Override public void accept(PV pv){pv.visitB(this);}
  @Override public String toString(){return Constants.toS.apply(this);}
  @Override public boolean wf(){return Constants.wf.test(this);}
  /*fields*/}

/*lbc*/class 
S implements Visitable<S>{@Override public S accept(CV cv){return cv.visitS(this);}@Override public void accept(PV pv){pv.visitS(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  /*fields possibly including pos*/}

class CV {
  final E visitE(E e){return e.visitable().accept(this);}
  A visitA(A a){return a;}
  B visitB(B b){return b;}
  S visitS(S s){return s;}
}

class PV {
  final void visitE(E e){e.visitable().accept(this);}
  void visitA(A a){return;}
  void visitB(B b){return;}
  void visitS(S b){return;}
}
class ToS extends PV implements Function<Visitable<?>,String>{
  StringBuilder sb=new StringBuilder();
  static String of(Visitable<?> v){
    var pv=new ToS();
    v.accept(pv);
    return pv.sb.toString();
    }
  @Override public String apply(Visitable<?> t) { 
    t.accept(this);
    return this.sb.toString();
    }
  }
class WF extends PV implements Predicate<Visitable<?>>{
  @Override public boolean test(Visitable<?> t) { 
    t.accept(this);
    return true;//or exception
    }
  }
class Constants{
  public static Function<Visitable<?>,String> toS;
  public static Predicate<Visitable<?>> wf;
  }
class Init{}
  

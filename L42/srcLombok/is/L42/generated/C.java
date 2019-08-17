package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CollectorVisitor;
import is.L42.visitors.Visitable;
import is.L42.common.Constants;

@Value @Wither public class 
C implements LDom,Visitable<C>{@Override public C accept(CloneVisitor cv){return cv.visitC(this);}@Override public void accept(CollectorVisitor cv){cv.visitC(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}
  String inner; int uniqueNum;}//-1 for no unique num

//TODO:
/*
 
 replace ctxL with C:LL
 pTail::= CORE.L | C:LL
 
C calls static{loadclass Init}
class Init static checks:
  assertions are on
  set some global variables for caching and disablyng certain
    assertions/wf checks
all ast stuff, including Half implements a single Visitable interface
  then we have a single big visitor pattern
  including visit C, visit X and visit List<C> ...
  
define Half.E and so on

all toString cutpasted as return Stuff.toS(this) this:Visitable
also, wf cutpasted as return Stuff.wf(this) and not called automatically

check why toStringVisitor was not great

all wf for C/m/P... uses the parser

subparse for string interpolation and docs
for each wf criteria, make some negative tests

  @EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
  ?? implements ,Visitable<??>{@Override public Visitable<??>visitable(){return this;}@Override public ?? accept(CloneVisitor cv){return cv.visit??(this);}@Override public void accept(CollectorVisitor cv){cv.visit??(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}Pos pos;

@EqualsAndHashCode(exclude={"pos"})@Value @Wither public static class
?? implements M,Visitable<??>{@Override public ?? accept(CloneVisitor cv){return cv.visit??(this);}@Override public void accept(CollectorVisitor cv){cv.visit??(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}

@Value @Wither public static class
?? implements Visitable<??>{@Override public ?? accept(CloneVisitor cv){return cv.visit??(this);}@Override public void accept(CollectorVisitor cv){cv.visit??(this);}@Override public String toString(){return Constants.toS.apply(this);}@Override public boolean wf(){return Constants.wf.test(this);}


*/
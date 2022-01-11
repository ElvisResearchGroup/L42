package is.L42.visitors;

import java.util.List;
import is.L42.generated.Full;
//noBlockNeeded of 1FullGrammar
public class CheckBlockNeeded extends UndefinedCollectorVisitor{
  private static final List<Class<?>> danger=List.of(
    Full.For.class,Full.If.class,Full.Loop.class,Full.While.class);
  public static boolean of(Full.E e,boolean skipDanger){
    assert e!=null;
    if(skipDanger && !danger.contains(e.getClass())){return false;}
    var r=new CheckBlockNeeded();
    try{r.visitE(e);}
    catch(UndefinedCase uc){return true;}
    return false;    
    }
  @Override public void visitLoop(Full.Loop l){visitE(l.e());}
  @Override public void visitThrow(Full.Throw t){}
  @Override public void visitBlock(Full.Block b){}
  @Override public void visitIf(Full.If i){
    if(i._else()==null){visitE(i.then());return;}
    visitE(i._else());
    }
  @Override public void visitWhile(Full.While w){visitE(w.body());}
  @Override public void visitFor(Full.For f){visitE(f.body());}
  }
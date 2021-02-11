package is.L42.visitors;

import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.ThrowKind;
import is.L42.generated.X;

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
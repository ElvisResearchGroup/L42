package is.L42.typeSystem;

import static is.L42.tools.General.L;
import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.P;
import is.L42.generated.Core.L.NC;
import is.L42.visitors.UndefinedCollectorVisitor;

public class ProgramTypeSystem {
  Program p;
  public static void type(boolean typed,Program p){
    L l=p.topCore();
    assert !l.info().isTyped();
    assert l.ts().stream().allMatch(t->p._ofCore(t.p()).isInterface());
    for(MWT mwt:l.mwts()){typeMWT(p,mwt);}
    for(NC nc:l.ncs()){
      var pushed=p.push(nc.key(),nc.l());
      if(typed||nc.key().hasUniqueNum()){type(typed,pushed);}
      if(nc.key().hasUniqueNum()){coherent(pushed);}
      }
    }
  public static void coherent(Program p){
    //TODO:
    }
  public static void typeMWT(Program p,MWT mwt){
    if(mwt._e()!=null){visitMethE(p,mwt.mh(),mwt._e());}
    //TODO: more stuff here
    }
  private static void visitMethE(Program p,MH mh, E e){
    var g=G.of(mh);
    List<P> ps=L(mh.exceptions().stream().map(t->t.p()));
    var expected=mh.t().p();
    e.visitable().accept(new PathTypeSystem(true,p,g,L(),ps,expected));
    }

}

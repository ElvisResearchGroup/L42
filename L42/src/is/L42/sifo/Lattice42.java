package is.L42.sifo;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;
import is.L42.generated.P;

public class Lattice42 extends Lattice<P>{
  Program p;
  
  public Lattice42(Program p, P top){
    super(top);
    this.top = top;
    this.p=p;
    }
  
  public ArrayList<P> lowerLevels(P path){
    return new ArrayList<P>(L(p._ofCore(path).ts().stream().map(t->t.p())));
    }
  
  @Override
  public P getBottom() {
    return P.pAny;
  }
  }

package is.L42.sifo;

import static is.L42.tools.General.L;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.P;

public class Lattice42 extends Lattice{
  Program p;
  public Lattice42(Program p,P top){
    super(top);
    this.p=p;
    }
  public List<P> lowerLevels(P path){
    return L(p._ofCore(path).ts().stream().map(t->t.p()));
    }
  }

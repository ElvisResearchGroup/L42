package is.L42.sifo;

import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    assert path.isNCs();
    var pi=p._ofCore(path);
    assert pi!=null:
      "";
    var stream=pi.ts().stream().map(t->p.from(t.p(),path.toNCs()));
    return stream.collect(Collectors.toCollection(ArrayList::new));
    }
  
  @Override
  public P getBottom() {
    return P.pAny;
  }
  }

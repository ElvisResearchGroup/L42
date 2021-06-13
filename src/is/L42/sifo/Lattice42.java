package is.L42.sifo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.P;

public class Lattice42 extends Lattice<P>{
  Program p;
  
  public Lattice42(Program p, P top){
    super(top);
    this.p=p;
    traverseInterfaceHierarchy(top);
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
  
  @Override
  protected Map<P, Integer> getUpper(P level) {
    Map<P, Integer> uppers = new HashMap<P, Integer>();
    if (getBottom().equals(level)) {
      for (P upperLevel : inner.keySet()) {
        uppers.put(upperLevel, 1);
      }
      return uppers;
    }
    assert !getBottom().equals(level);
    uppers.put(level, 0);
    for (P upperLevel : inner.get(level)) {
      uppers.put(upperLevel, 1);
      getUpper(uppers, upperLevel, 1);
    }
    return uppers;
  }
  }

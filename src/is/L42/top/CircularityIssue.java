package is.L42.top;

import java.util.HashSet;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.EndError.PathNotExistent;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Full;
import is.L42.generated.P;
import static is.L42.tools.General.*;

public class CircularityIssue {
  List<P.NCs> typePs; Program p; E e;List<Full.L.NC>moreNCs;
  public CircularityIssue(List<P.NCs> typePs, Program p, E e,List<Full.L.NC>moreNCs) {
    this.typePs=typePs;this.p=p;this.e=e;this.moreNCs=moreNCs;
    }
  public void checkNotIllTyped(){
    var visited=new HashSet<P.NCs>();
    for(var pi:typePs){checkNotIllTyped(pi,visited);}
    }
  private void checkNotIllTyped(P.NCs pi,HashSet<P.NCs>visited){
    boolean novel=visited.add(pi);
    if(!novel){return;}
    Core.L l=(Core.L)p.of(pi,e.poss());//propagate errors for path not existent
    if(!l.info().isTyped()){reportError(pi,l);}
    for(var pj:l.info().typeDep()){checkNotIllTyped(p.from(pj,pi),visited);}
    }
  public void reportError(P.NCs path,L l0) {
    for(var pj:typePs){p.of(pj,e.poss());}//good error if pj does not exists
    var p0=p.navigate(path);
    for(var pi:l0.info().typeDep()){
      try{p0.of(pi,l0.poss());}
      catch(PathNotExistent pne){
        var subPi=pi;
        while(!subPi.cs().isEmpty()){
          var cs=subPi.cs();
          subPi=subPi.withCs(popLRight(cs));
          try{
            p0.of(subPi,l0.poss());
            subPi=subPi.withCs(cs);
            break;
            }
          catch(PathNotExistent moreLoops){}
        }
        throw new EndError.TypeError(l0.poss(),ErrMsg.uncompiledDependency(path,pi,subPi));
        }
      }
    HashSet<P> untypedDeps=new HashSet<>();
    reportError(untypedDeps,path,l0);
    throw new EndError.TypeError(l0.poss(),ErrMsg.untypedDependency(path,untypedDeps));
    }
  public void reportError(HashSet<P> untypedDeps,P.NCs path,L l){
    assert !l.info().isTyped();
    for(var pi:l.info().typeDep()){
      pi=p.from(pi,path);
      var li=(Core.L)p.of(pi,l.poss());
      if(li.info().isTyped()){continue;}
      boolean novel=untypedDeps.add(pi);
      if(novel){reportError(untypedDeps,pi,li);}
      }
    }
  }

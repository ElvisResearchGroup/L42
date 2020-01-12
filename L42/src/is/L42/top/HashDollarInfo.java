package is.L42.top;

import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.Full.L.NC;
import is.L42.visitors.PropagatorCollectorVisitor;

public class HashDollarInfo extends PropagatorCollectorVisitor{
  public HashDollarInfo(NC nc){this.visitE(nc.e());}
  public HashDollarInfo(Full.L l){
    if(l.reuseUrl().contains("#$")){this.hashDollarTop=true;}
    this.visitL(l.withReuseUrl(""));
    }
  int nestedLev=0;
  public boolean hashDollarTop=false;
  public boolean hashDollarInside=false;
  @Override public void visitL(Core.L l){}
  @Override public void visitL(Full.L l){
    nestedLev+=1;
    if(l.reuseUrl().contains("#$")){
      hashDollarInside=true;
      }
    for(var m:l.ms()){if (m instanceof Full.L.NC){visitM(m);}}
    nestedLev-=1;
    }
  @Override public void visitCall(Full.Call s){
    super.visitCall(s);
    if(s._s()==null || !s._s().m().startsWith("#$")){return;}
    if(nestedLev==0){hashDollarTop=true;}
    else{hashDollarInside=true;}
    }
  @Override public void visitMCall(Core.MCall s){
    super.visitMCall(s);
    if(!s.s().m().startsWith("#$")){return;}
    if(nestedLev==0){hashDollarTop=true;}
    else{hashDollarInside=true;}
    }
  }
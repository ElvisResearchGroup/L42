package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.util.*;
import static is.L42.tools.General.*;
import is.L42.generated.Core.MH;
import is.L42.flyweight.*;

@Value @Wither public class 
MethT {
  List<Mdf> mdfs; Mdf mdf;
  public MH mh(){
    var mdfs=this.mdfs.subList(1,this.mdfs.size());
    List<Core.T> ts=L(mdfs,(c,mi)->c.add(P.coreAny.withMdf(mi)));
    Core.T t=P.coreAny.withMdf(mdf);
    S s= new S("a",L(range(mdfs),(c,i)->c.add(X.of("x"+i))),-1);
    return new MH(this.mdfs.get(0),L(),t,s,ts,L());
    }
  public boolean wf(){
    try{mh().wf();return true;}
    catch(is.L42.common.EndError ee){return false;}
    }

  }
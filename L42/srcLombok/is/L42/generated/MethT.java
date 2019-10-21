package is.L42.generated;
import lombok.Value;
import lombok.experimental.Wither;
import java.util.*;
import static is.L42.tools.General.L;
import is.L42.generated.Core.MH;

@Value @Wither public class 
MethT {
  List<Core.T> ts; Core.T t;List<P> ps;
  public MH mh(){
    var ts=this.ts.subList(1,this.ts.size());
    var ps=L(this.ps.stream().map(p->P.coreAny.withP(p)));
    int[] index={0};
    S s= new S("a",L(ts,(c,ti)->c.add(new X("x"+index[0]++))),-1);
    return new MH(this.ts.get(0).mdf(),L(),t,s,ts,ps);
    }
  public boolean wf(){
    try{mh().wf();return true;}
    catch(is.L42.common.EndError ee){return false;}
    }

  }
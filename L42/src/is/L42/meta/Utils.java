package is.L42.meta;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.X;

public class Utils {
  public static Core.PCastT This0=new Core.PCastT(null,P.pThis0,P.coreThis0.withMdf(Mdf.Class));
  public static Core.EX this0=new Core.EX(null,X.thisX);
  public static Core.MCall ThisCall(Pos pos,S s,List<Core.E>es){
    return new Core.MCall(pos,This0.withPos(pos), s, es);
    }
  public static Core.MCall thisCall(Pos pos,S s,List<Core.E>es){
    return new Core.MCall(pos,this0.withPos(pos), s, es);
    }
  public static boolean match(Program p,MetaError err,String target,MWT m){
  for( var d:m.docs()){
    if(d._pathSel()==null){continue;}
    L ld=p._ofCore(d._pathSel().p());
    if(ld==null){ 
      err.throwErr(m,"annotation "+d._pathSel().p()+"not existent");
      }
    for(var di:ld.docs()){
      if(di.texts().size()!=1){continue;}
      if(di.texts().get(0).equals(target)){return true;}
      }
    }
  return false;
  }
  }

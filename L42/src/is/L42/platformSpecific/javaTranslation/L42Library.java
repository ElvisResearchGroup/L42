package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Core.L;

public class L42Library implements L42Any{
  Program originP;
  Program currentP=null;
  L originL;
  C originName=null;
  public L42Library(Program p) {
    originP=p.pop();
    if(p.pTails.hasC()){originName=p.pTails.c();}
    originL=p.topCore();
    }
  public void currentProgram(Program p){
    if(p==currentP){
      assert localPath!=null;
      assert unwrap!=null;
      return;
      }
    currentP=p;
    var pathC=currentP.path();
    var pathO=originP.path();
    while(!pathC.isEmpty()&&!pathO.isEmpty()&&pathC.get(0)==pathO.get(0)){
      pathC.remove(0);
      pathO.remove(0);
      }
    assert pathC.isEmpty()||pathO.isEmpty()||!pathC.get(0).equals(pathO.get(0));
    //origin:C1..Cn   {..}
    //current:C1'..Ck'
    //{..}[from Thisk.C1..Cn;currentP]
    localPath=P.of(pathC.size(),pathO);
    unwrap=(Core.L)currentP.from(originL,localPath);
    if(this.originName==null){localPath=null;}
    else{localPath=localPath.withCs(pushL(localPath.cs(),originName));}
    }
  public L unwrap;
  public P.NCs localPath;
  }
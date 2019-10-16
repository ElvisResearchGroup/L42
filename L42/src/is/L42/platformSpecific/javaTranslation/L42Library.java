package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;

import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Core.L;

public class L42Library implements L42Any{
  Program originP;
  Program currentP=null;
  L originL;
  public L42Library(Program p, L l) {this.originP=p;this.originL=l;}
  void currentProgram(Program p){
    if(p==currentP){return;}
    currentP=p;
    if(currentP==originP){unwrap=originL;}
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
    unwrap=(Core.L)currentP.from(originL,P.of(pathC.size(),pathO));
    }
  public L unwrap;
  }
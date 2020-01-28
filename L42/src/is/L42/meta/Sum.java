package is.L42.meta;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.tools.General;

public class Sum {
  public static Core.L compose(Program p,C c,Core.L l1, Core.L l2){
    return null;
    }
  public static List<List<C>> allWatched(Core.L l){return null;}
  public static List<List<C>> allRequiredCoherent(Core.L l){return null;}
  public static List<List<C>> allHiddenSupertypes(Core.L l){return null;}
  public static boolean moreThen(List<C> cs,Core.L l1,Core.L l2){return false;}
  public static boolean implemented(Program p,Core.L l,List<C> cs){return false;}
}

class Plus{
  public Plus(Program pOut, Core.L topLeft, Core.L topRight, List<C> cs) {
    this.pOut = pOut;
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.cs = cs;
    }
  Program pOut;
  Core.L topLeft;
  Core.L topRight;
  List<C> cs;
  Plus addC(C c){return new Plus(pOut,topLeft,topRight,pushL(cs, c));}
  
  Core.L plus(Core.L l1,Core.L l2){return null;}
  Core.L.NC plus(Core.L.NC nc1,Core.L.NC nc2){return null;}
  IMWT plus(IMWT imwt1,IMWT imwt2){return null;}
  List<Core.L.NC> plusNCs(List<Core.L.NC> nc1,List<Core.L.NC> nc2){return null;}
  List<IMWT> plusIMWTs(List<IMWT> imwt1,List<IMWT> imwt2){return null;}
  boolean plusInterface(boolean interface1,boolean interface2){return false;}
  Core.L squareAdd(List<C> cs, List<P> pz){return null;}
  }
class IMWT{
  final boolean isInterface;
  final Core.L.MWT mwt;
  IMWT(boolean isInterface,Core.L.MWT mwt){this.isInterface=isInterface;this.mwt=mwt;}
  public static List<IMWT> of(boolean isInterface,List<Core.L.MWT> mwts){
    return L(mwts,(c,m)->c.add(new IMWT(isInterface,m)));
    }
  }
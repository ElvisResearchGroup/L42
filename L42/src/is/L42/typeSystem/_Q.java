package is.L42.typeSystem;

import java.util.List;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.P;

public class _Q extends is.L42.visitors.UndefinedCollectorVisitor{
  public _Q(boolean typed, Program p, G g, List<T> rets, List<P> exceptions, P type) {
    this.typed = typed;
    this.p=p;
    this.g = g;
    this.rets = rets;
    this.exceptions = exceptions;
    this.type = type;
  }
  boolean typed;
  Program p;
  G g;
  List<Core.T> rets;
  List<P> exceptions;
  P type;
  P store(P newType){
    P aux=this.type;
    this.type=newType;
    return aux;
    }
  void restore(P oldType){this.type=oldType;}
  G store(G newG){
    G aux=this.g;
    this.g=newG;
    return aux;
    }
  void restore(G oldG){this.g=oldG;}
  List<Core.T> storeRets(List<Core.T> newRets){
    List<Core.T> aux=this.rets;
    this.rets=newRets;
    return aux;
    }
  void restoreRets(List<Core.T> oldRets){this.rets=oldRets;}
  List<P> store(List<P> newExceptions){
    List<P> aux=this.exceptions;
    this.exceptions=newExceptions;
    return aux;
    }
  void restore(List<P> oldExceptions){this.exceptions=oldExceptions;}
  
  }

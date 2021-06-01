package is.L42.top;

import static is.L42.tools.General.bug;

import java.io.Serializable;

import is.L42.common.EndError;

abstract class G implements Serializable{
  State state;
  public abstract Object layer();
  public abstract R _open(G cg,R cr);
  public abstract R _close(G cg,R cr);
  public abstract boolean needOpen();
  public R open(G cg,R cr){try{return _open(cg,cr);}catch(EndError err){return new R(err);}}
  public R close(G cg,R cr){try{return _close(cg,cr);}catch(EndError err){return new R(err);}}
  @Override public int hashCode(){throw bug();}
  @Override public boolean equals(Object o){throw bug();}
  }
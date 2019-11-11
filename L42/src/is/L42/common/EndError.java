package is.L42.common;

import java.util.List;

import is.L42.generated.Pos;

public abstract class EndError extends RuntimeException{
  public EndError(List<Pos> poss,String msg){
    //assert poss!=null: msg;
    assert msg!=null;
    if(!msg.isEmpty() && !msg.endsWith("\n")){msg+="\n";}
    this.msgPart=msg;
    this.poss=poss;
    }
  @Override public String getMessage(){
    if(msg==null){msg=Err.posString(poss)+msgPart+computeMsg();}
    return msg;
    }
  public String computeMsg(){return "";};//override handler
  public final List<Pos> poss;
  private final String msgPart;
  private String msg=null;
  public static class InferenceFailure extends EndError{
    public InferenceFailure(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class InvalidImplements extends EndError{
    public InvalidImplements(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class InvalidHeader extends EndError{
    public InvalidHeader(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class PathNotExistent extends EndError{
    public PathNotExistent(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class TypeError extends EndError{
    public TypeError(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class CoherentError extends EndError{
    public CoherentError(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class NotWellFormed extends EndError{
    public NotWellFormed(List<Pos> poss, String msg) { super(poss, msg);}
    }
  }

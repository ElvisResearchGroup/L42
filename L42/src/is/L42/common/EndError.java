package is.L42.common;

import java.util.List;

import is.L42.generated.Pos;


@SuppressWarnings("serial")
public abstract class EndError extends RuntimeException{
  public EndError(List<Pos> poss,String msg){
    assert poss!=null;
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
  
}

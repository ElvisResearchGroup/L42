package is.L42.common;

import is.L42.generated.Pos;


@SuppressWarnings("serial")
public abstract class EndError extends RuntimeException{
  public EndError(Pos pos,String msg){
    assert pos!=null;
    assert msg!=null;
    if(!msg.isEmpty() && !msg.endsWith("\n")){msg+="\n";}
    this.msgPart=msg;
    this.pos=pos;
    }
  @Override public String getMessage(){
    if(msg==null){msg="line " + pos.line() + ":" + pos.column() + "\n" +msgPart+computeMsg();}
    return msg;
    }
  public String computeMsg(){return "";};//override handler
  public final Pos pos;
  private final String msgPart;
  private String msg=null;
  
}

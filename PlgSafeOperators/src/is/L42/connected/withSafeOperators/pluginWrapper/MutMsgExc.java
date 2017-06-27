package is.L42.connected.withSafeOperators.pluginWrapper;

@SuppressWarnings("serial")
abstract public class MutMsgExc extends Exception{
  String mutMsg;//since java do not want setMessage  :(
  public @Override String getMessage(){return mutMsg;}
  public void setMessage(String msg){mutMsg=msg;}
  public String text(){return mutMsg;}
  public boolean equalequal(MutMsgExc that){
    return this.mutMsg.equals(that.mutMsg);
    }
  public boolean equals(Object that){
    if(!this.getClass().equals(that.getClass())){return false;}
    return equalequal((MutMsgExc)that);
    }
  public int hashCode(){return mutMsg.hashCode();}
}
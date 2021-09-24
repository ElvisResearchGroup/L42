package is.L42.common;

import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import is.L42.generated.C;
import is.L42.generated.P;
import is.L42.generated.Pos;

public abstract class EndError extends RuntimeException {
  public EndError(List<Pos> poss,Supplier<String> msg){
    assert msg!=null;
    this.msgPartSupplier=()->{String m = msg.get(); return (!m.isEmpty() && !m.endsWith("\n")) ? m + '\n' : m; };
    this.poss=poss;
    }
  public boolean ismethCallNoCompatibleMdfParametersSignature(){return false;}//override handler
  public EndError(List<Pos> poss,String msg){
    //assert poss!=null;
    assert msg!=null: msg;
    if(!msg.isEmpty() && !msg.endsWith("\n")) { msg += "\n"; }
    this.msgPartString = msg;
    this.msgPartSupplier = null;
    this.poss=poss;
    }
  public String msgPart(){
    if(msgPartString == null) { msgPartString = msgPartSupplier.get(); }
    return msgPartString;
    }
  @Override public String getMessage(){
    if(msg==null){msg=ErrMsg.posString(poss)+this.msgPart();}
    return msg;
    }
  public final List<Pos> poss;
  private String msgPartString;
  private final Supplier<String> msgPartSupplier;
  private String msg=null;
  public static class InlinedNativeInvalid extends EndError{
    public InlinedNativeInvalid(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class InferenceFailure extends EndError{
    public InferenceFailure(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class InvalidImplements extends EndError{
    public InvalidImplements(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class UrlNotExistent extends EndError{
    public UrlNotExistent(List<Pos> poss, String msg) { super(poss,ErrMsg.urlNotExistant(msg));}
    }

  public static class InvalidHeader extends EndError{
    public InvalidHeader(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class PathNotExistent extends EndError{
    public PathNotExistent(List<Pos> poss,String msg) {
      super(poss, msg);
      }
    public PathNotExistent(List<Pos> poss,Program p,P.NCs path) {
      super(poss, makeMsg(p,path));
      }
    public PathNotExistent(List<Pos> poss,List<C> cs,C c) {
      super(poss, ErrMsg.pathNotExistant(cs, ""));
      }
    public static String alternatives(Program p,P.NCs path) {
      var os=new ArrayList<P>();
      var pi=defined(p,path);
      var defined=p._ofCore(pi);
      for(var ni:defined.ncs()){
        os.add(pi.withCs(pushL(pi.cs(),ni.key())));
        }
      return "\nAvailable paths are: "+os;
      }
    static String makeMsg(Program p,P.NCs path) {
      if(path.cs().isEmpty()){return ErrMsg.pathNotExistant(path, "");}
      return ErrMsg.pathNotExistant(path,alternatives(p, path));
      }
    static P.NCs defined(Program p,P.NCs path) {
      var cs=popLRight(path.cs());
      path=path.withCs(cs);
      var defined=p._ofCore(path);
      if(defined!=null){return path;}
      return defined(p,path);
      }
    }
  public static class TypeError extends EndError{
    public TypeError(List<Pos> poss, String msg) { super(poss, msg);}
    public TypeError(List<Pos> poss, Supplier<String> msg) { super(poss, msg);}
    }
  public static class CoherentError extends EndError{
    public CoherentError(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class NotWellFormed extends EndError{
    public NotWellFormed(List<Pos> poss, String msg) { super(poss, msg);}
    }
  public static class LeakedSlaveError extends EndError{
    public LeakedSlaveError(List<Pos> poss, String msg) { super(poss, msg);}
    }
  }

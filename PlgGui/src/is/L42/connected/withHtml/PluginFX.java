package is.L42.connected.withHtml;

import is.L42.connected.withHtml.FrameFX.ApplicationDead;
import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import ast.Ast;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.*;
import static auxiliaryGrammar.EncodingHelper.*;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;


/*

Open HTML? BODY?
Close?
get id->HTML
set id HTML

open(that:BodyTag title:S)
get(id:#id) ->Html
set(that:Html id:#id)
append(that:Html id:#id)


try to learn to use ACE for the IDE?
 */

public class PluginFX implements PluginType {
  /*@Override
  public Object dispatch(boolean t,Program p, Using u) {
    ExpCore _1=u.getEs().size()<1?null:u.getEs().get(0);
    ExpCore _2=u.getEs().size()<2?null:u.getEs().get(1);
    //ExpCore _3=u.getEs().size()<3?null:u.getEs().get(2);
    switch(composedName(u)){
      case "open that title ":return t?Mopen£that£title:Mopen£that£title(_1,_2);
      //case "get id ":return t?getId:getId(_1,_2);
      case "set that id ":return t?Mset£that£id:Mset£that£id(_1,_2);
  //    case "update that id ":return t?subInt32:subInt32(_1,_2);
      case "eventPending ":return t?MeventPending:MeventPending();
      default:throw new ErrorMessage.PluginMethodUndefined(u, p.getInnerData());
    }
  }*/

  private static Ast.MethodType Mopen£that£title=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void Mopen£that£title(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    FrameFX.open(s2,s1);
    return Resources.Void.instance;
  }
  private static Ast.MethodType Mset£that£id=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library})
  public  Resources.Void Mset£that£id(Object cb1,Object cb2){
    String s1=extractStringU(cb1);
    String s2=extractStringU(cb2);
    if (s1==null||s2==null){throw new Resources.Error("InvalidStringU");}
    FrameFX.setId(s2,s1);
    return Resources.Void.instance;
  }
  
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public  Object MexecuteJs£that(Object _s1){
    String s1=EncodingHelper.ensureExtractStringU(_s1);
    String res = FrameFX.executeJs(s1);
    assert res!=null;
    return res;
  }
  private static Ast.MethodType MeventPending=mt(Path.Library());
  @ActionType({ActionType.Type.Library})
  public Object MeventPending(){
    try { return FrameFX.getEventString(); } 
    catch (ApplicationDead e) {
      throw Resources.notAct;
      }
    }
  }

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

public class PluginFX implements PluginType.WellKnown {
//OLD, unused.
  public static Ast.MethodType Mopen£that£title=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public Resources.Void Mopen£that£title(Object cb1,Object cb2){
    String s1=ensureExtractStringU(cb1);
    String s2=ensureExtractStringU(cb2);
    FrameFX.open(s2,s1);
    return Resources.Void.instance;
  }
  public static Ast.MethodType Mset£that£id=mt(Path.Void(),Path.Library(),Path.Library());
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library})
  public  Resources.Void Mset£that£id(Object cb1,Object cb2){
    String s1=ensureExtractStringU(cb1);
    String s2=ensureExtractStringU(cb2);
    FrameFX.setId(s2,s1);
    return Resources.Void.instance;
  }
  
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
  public  Object MexecuteJs£that(Object _s1){
    String s1=EncodingHelper.ensureExtractStringU(_s1);
    String res = FrameFX.executeJs(s1);
    assert res!=null;
    return res;
  }
  public static Ast.MethodType MeventPending=mt(Path.Library());
  @ActionType({ActionType.NormType.Library})
  public Object MeventPending(){
    try { return FrameFX.getEventString(); } 
    catch (ApplicationDead e) {
      throw Resources.notAct;
      }
    }
  }

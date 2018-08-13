package is.L42.connected.withHtml;

import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import static auxiliaryGrammar.EncodingHelper.*;
import auxiliaryGrammar.EncodingHelper;


public class Plugin implements PluginType.WellKnown, PluginType.UnTrusted {

  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
  public Resources.Void Mopen£xwName£xhtml£xx£xy(Object _wName, Object _html,Object _x,Object _y){
    String wName=ensureExtractStringU(_wName);
    String html=ensureExtractStringU(_html);
    int x=ensureExtractInt32(_x);
    int y=ensureExtractInt32(_y);
    Frame.load(wName, html, x, y);
    return Resources.Void.instance;
  }
  
  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library}) 
  public  Object MexecuteJs£xwName£xcommand(Object _wName, Object _command){
    String wName=EncodingHelper.ensureExtractStringU(_wName);
    String command=EncodingHelper.ensureExtractStringU(_command);
    System.out.println("REQUESTED executeJs on: "+command);
    String res = Frame.executeJs(wName,command);
    assert res!=null;
    return res;
  }

  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,})
  public Object MeventPending£xwName(Object _wName){
    String wName=EncodingHelper.ensureExtractStringU(_wName);
    return Frame.getEventString(wName);     
  }
  
  @ActionType({ActionType.NormType.Void,ActionType.NormType.Library,})
  public Resources.Void Mclose£xwName(Object _wName){
    String wName=EncodingHelper.ensureExtractStringU(_wName);
    Frame.close(wName);
    return Resources.Void.instance;
  }
}

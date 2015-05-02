package is.L42.connected.withSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.L42;
import tools.Assertions;
import ast.Ast.Mdf;
import ast.Ast;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Type;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.*;
import static auxiliaryGrammar.EncodingHelper.*;
import auxiliaryGrammar.Program;

public class Plugin implements PluginType {
  PluginType sealed;
  public Plugin(PluginType sealed){
    this.sealed=sealed;
  }
 /* @Override
  public Object dispatch(boolean t,Program p, Using u) {
    if(!u.getName().isEmpty()){throw new ErrorMessage.PluginMethodUndefined(u, p.getInnerData());}
    if(u.getXs().size()!=1){throw new ErrorMessage.PluginMethodUndefined(u, p.getInnerData());}
    if(!u.getXs().get(0).equals("that")){throw new ErrorMessage.PluginMethodUndefined(u, p.getInnerData());}
    ExpCore _1=u.getEs().get(0);
    return t?action:action(p,_1);
  }*/
  // +
  private static Ast.MethodType action=mt(Path.Void(),Path.Library());
  //never act!
  private ExpCore action(Program p,ExpCore cb1){
    // TODO:the whole class should die?
    //what I need is executing in another process?
    // using Sandbox check({permissions?}) e
    //must be able to say e uses only immutable /capsule stuff? is needed?
    //can also be the way to to remote computation?
    throw Resources.notAct;
  }
}

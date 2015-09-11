package platformSpecific.fakeInternet;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import platformSpecific.javaTranslation.Resources;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import facade.Configuration;
import facade.L42;
import facade.Parser;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import tools.Assertions;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Expression;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.Using;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;
import auxiliaryGrammar.WellFormedness;

public interface OnLineCode {
  public Expression.ClassB code();
  public static Expression.ClassB getCode(String url){
    assert url.startsWith("reuse");
    url=url.substring("reuse".length());
    url=url.trim();
    if(url.startsWith("L42.is/")){return OnLineCodeHelper.getL42Code(url.substring("L42.is/".length()));}
    throw Assertions.codeNotReachable();
  }
  public static PluginType plugin(Program p,ExpCore.Using u){
    String url=p.extractCb(u.getPath()).getDoc1().toString();
    if(!url.startsWith("@plugin\n")){throw new ErrorMessage.InvalidURL(url);}
    url=url.substring("@plugin\n".length());
    url=url.trim();
    PluginType pt = OnLineCodeHelper.getPluginType(url);
    //pt.setProgram(p);
    return pt;
  }
  public static ExpCore pluginAction(Program p,ExpCore.Using u){
    try{
      Object o= plugin(p,u).execute(p,u);
      return EncodingHelper.wrapResource(o);
      }
    catch(Resources.Error err){return EncodingHelper.wrapResource(err);}
  }
  public static Ast.MethodType pluginType(Program p,ExpCore.Using u){
    return plugin(p,u).typeOf(p,u);
  }
}

class OnLineCodeHelper{
  static PluginType getPluginType(String url){
    if(url.startsWith("L42.is/connected/")){
      PluginType plugin= OnLineCodeHelper.getWellKnownPluginType(url.substring("L42.is/connected/".length()));
      return plugin;
      }
    throw Assertions.codeNotReachable();
  }
  static PluginType getWellKnownPluginType(String url){
    String className="is.L42.connected."+url+".Plugin";
    //TODO: it may need fix up for plugins with parameters
    try {
      Class<?> clazz=Class.forName(className);
      return (PluginType)clazz.newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new ErrorMessage.InvalidURL("L42.is/connected/"+url);
    }
    //is.L42.connected.withSystem.Plugin;
    /*if(url.startsWith("withSandboxingOver/")){
        return new?? withSandboxingOver.Plugin(getPluginType(url.substring("withSandboxingOver/".length())));
    }
    switch (url){//TODO: NO NONO
      case "withAlu": return new Plugin();
      case "withHtml": return new Plugin();
      case "withIntrospection": return new Plugin();
      default: throw new ErrorMessage.InvalidURL("L42.is/connected/"+url);
      }*/
    }
  static Expression.ClassB getL42Code(String url){
    try{
      Expression data=load(url);//can be classB or classBReuse
      data=Desugar.of(data);//TODO: can be removed later, but now allows librares to be real source code
      //    Configuration.reduction.of((ClassB) data.accept(new InjectionOnCore()));
      return (Expression.ClassB)data;
      }
    catch( org.antlr.v4.runtime.misc.ParseCancellationException pce){
      System.err.println("Url is: "+url);
      throw pce;
      }
    }
  private static Expression load(String name) {
    //URL res = OnLineCode.class.getResource(name+".L42");
    //Path res = L42.path.resolve(name+".L42");
    Path res = Paths.get("localhost",name+".L42");
    assert res!=null:name;
    String s= L42.pathToString(res);
    Expression e=Parser.parse(res.toString(),s);
    assert WellFormedness.checkAll(e);
    return e;
  }

}


/*

error S"
  'IterationNotComplete,
  'use valsCut() to allows incomplete iterations
  "
*/
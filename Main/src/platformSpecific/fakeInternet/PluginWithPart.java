package platformSpecific.fakeInternet;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.ExpCore.Using;
import auxiliaryGrammar.Program;
import tools.Map;

public class PluginWithPart implements PluginType{
  String url;
  String part;
  Class<?>pointed;
  public PluginWithPart(String url, String part) {
    super();
    this.url = url;
    this.part = part;
    try {
      this.pointed=Class.forName(part);
      }
    catch (ClassNotFoundException e) {
      throw new ErrorMessage.InvalidURL(url+"\n"+part,null);
      }
    }
  public List<NormType> typeOf(Program p, Using u){
    Method method = ProtectedPluginType.getMethod(pointed, p, u.getS().getName(), u.getS().getNames().size(),u);
    Class<?> retT = method.getReturnType();//return (ast.Ast.MethodType) dispatch(true,p,u);
    Class<?>[] pars = method.getParameterTypes();
    List<Class<?>>ts=new ArrayList<>();
    ts.add(retT);
    if ((method.getModifiers() & Modifier.STATIC) == 0) {
      ts.add(pointed);
      }
    ts.addAll(Arrays.asList(pars));
    return Map.of(jt->jTo42(jt),ts);
    }
  private static ast.Ast.NormType jTo42(Class<?>jt){
    if (jt.equals(Void.TYPE)){return new NormType(Mdf.Immutable,Path.Void(),Ph.None);}
    if (jt.equals(ast.Ast.Path.class)){return new NormType(Mdf.Class,Path.Any(),Ph.None);}
    return new NormType(Mdf.Immutable,Path.Library(),Ph.None);
    }
  public Object execute(Program p, Using u){
    assert false: "is this not called with compilation?";
    Method method = ProtectedPluginType.getMethod(pointed, p, u.getS().getName(), u.getS().getNames().size(),u);
    List<ExpCore> es = u.getEs();
    ExpCore rec=null;
    if ((method.getModifiers() & Modifier.STATIC) == 0) {
    rec=es.get(0);
    es=es.subList(1,es.size());
    }  
    return ProtectedPluginType.executeMethod(method, p, rec, es.toArray());
    }
}

package platformSpecific.fakeInternet;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import platformSpecific.fakeInternet.ActionType.Type;
import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodType;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Using;
import auxiliaryGrammar.Program;
class ProtectedPluginType{
  static Method getMethod(PluginType that,Program p,Using u){
    String mName=Resources.nameOf(u.getS().getName(),u.getS().getNames());
    return getMethod(that.getClass(),p,mName,u.getS().getNames().size(),u);
    }

  static Method getMethod(Class<?> that,Program p,String mName, int argNum,Using uForError){
    Class<?>[] parameterTypes=new Class<?>[argNum];
    Arrays.fill(parameterTypes, Object.class);
    try {return that.getMethod(mName, parameterTypes);}
    catch (NoSuchMethodException e) {
      List<String> options=new ArrayList<>();
      for(Method m:that.getClass().getDeclaredMethods()){
        options.add(m.getName());
        }
      throw new ErrorMessage.PluginMethodUndefined(options,uForError,p.getInnerData(),null);
      }
    catch (SecurityException e) { throw new Error(e);}
    }
  
  static Object executeMethod(Method m,Program p,Object rec, Object[] es){
    try {
      Object r = m.invoke(rec,es);
      if(Resources.isValid(p, r, es)){return r;}
      else{throw Resources.notAct;}
      }
    catch (IllegalAccessException | IllegalArgumentException e) {
      throw new Error(e);
      }
    catch (InvocationTargetException e) {
      if(e.getCause() instanceof RuntimeException){
        throw(RuntimeException)e.getCause();
        }
      if(e.getCause() instanceof Error){
        throw(Error)e.getCause();
        }
      throw new Error(e.getCause());
      }
    }
  }
public interface PluginType {
  default List<ast.Ast.NormType> typeOf(Program p, Using u){
    Method m=ProtectedPluginType.getMethod(this,p, u);
    ActionType ann = m.getAnnotation(ActionType.class);
    assert ann!=null;
    ArrayList<Ast.NormType> ts=new ArrayList<>();
    for(ActionType.Type path:ann.value()){
      ts.add(path.type.getNT());
      }
    return ts;
    };
  default Object execute(Program p, Using u){
    assert false: "is this not called with compilation?";
    Method m=ProtectedPluginType.getMethod(this,p, u);
    return ProtectedPluginType.executeMethod(m, p, this, u.getEs().toArray());
    }
  }
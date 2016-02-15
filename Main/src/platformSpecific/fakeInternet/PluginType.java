package platformSpecific.fakeInternet;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodType;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Using;
import auxiliaryGrammar.Program;

public interface PluginType {
  //Object dispatch(boolean t,Program p, Using u);
  static Method getMethod(PluginType that,Program p,Using u){
    String mName=Resources.nameOf(u.getS().getName(),u.getS().getNames());
    Class<?>[] parameterTypes=new Class<?>[u.getS().getNames().size()];
    Arrays.fill(parameterTypes, Object.class);
    try {return that.getClass().getMethod(mName, parameterTypes);}
    catch (NoSuchMethodException e) {
      List<String> options=new ArrayList<>();
      for(Method m:that.getClass().getDeclaredMethods()){
        options.add(m.getName());
        if(m.getName().contains("eft")){
          System.out.print("");
        }
        
        if(m.getName().equals(mName)){
          System.out.print("");
        }
      }
      throw new ErrorMessage.PluginMethodUndefined(options,u,p.getInnerData());
      }
    catch (SecurityException e) { throw new Error(e);}
    }
  default ast.Ast.MethodType typeOf(Program p, Using u){//return (ast.Ast.MethodType) dispatch(true,p,u);}
  //default MethodType dispatchType(Program p, Using u){
    Method m=getMethod(this,p, u);
    ActionType ann = m.getAnnotation(ActionType.class);
    assert ann!=null;
    MethodType mt = ActionType.Type.mt(ann.mdf(),ann.value());
    assert mt.getTs().size()==u.getS().getNames().size(): m.getName()+" "+mt.getTs().size()+"::"+u.getS().getNames().size();
    return mt;
    };
  default Object execute(Program p, Using u){//return  dispatch(false,p,u);}
  //default Object dispatchAction(Program p, Using u){
    Method m=getMethod(this,p, u);
    try {
      Object[] xs = u.getEs().toArray();
      Object r = m.invoke(this,xs);
      if(Resources.isValid(p, r, xs)){return r;}
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
    };
  //default void setProgram(Program p){};
}
//for connections, like dbconnections or file connections:
/*
use the connection string as connection object:
open "...."
close "..."
read "..."

implemented as a map: each resource type is a map from string to real resource handle!


*/
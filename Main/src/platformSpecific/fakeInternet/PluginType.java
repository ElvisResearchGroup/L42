package platformSpecific.fakeInternet;


import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import platformSpecific.fakeInternet.ActionType.NormType;
import platformSpecific.fakeInternet.PluginWithPart.UsingInfo;
import platformSpecific.javaTranslation.Resources;
import tools.LambdaExceptionUtil;
import tools.StringBuilders;
import ast.Ast.MethodType;
import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Using;
import auxiliaryGrammar.Functions;
import programReduction.Program;
class ProtectedPluginType{
  static Method getMethod(PluginType that,Program p,Using u){
    String mName=Resources.nameOf(u.getS().nameToS(),u.getS().getNames());
    return getMethod(that.getClass(),p,mName,u.getS().getNames().size(),u);
    }

  static Method getMethod(Class<?> that,Program p,String mName, int argNum,Using uForError){
    Class<?>[] parameterTypes=new Class<?>[argNum];
    Arrays.fill(parameterTypes, Object.class);
    try {return that.getMethod(mName, parameterTypes);}
    catch (NoSuchMethodException e) {
      List<String> options=new ArrayList<>();
      for(Method m:that.getDeclaredMethods()){
        options.add(m.getName());
        }
      throw new ErrorMessage.PluginMethodUndefined(options,uForError,null,null);
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
  static String executableWrapper(Ast.MethodSelector ms, java.util.Map<String,Integer> labels){
  //  (plF,xsF)->plF.
  //  MnameEncoded£xn1£xn2(xsF[0],..,xsF[n]),
  String plF="L"+Functions.freshName("pl",labels);
  String xsF="L"+Functions.freshName("xs",labels);
  StringBuilder res=new StringBuilder();
  res.append("("+plF+","+xsF+")->"+plF+".");
  res.append(Resources.nameOf(ms.nameToS(),ms.getNames()));
  res.append("(");
  StringBuilders.formatSequence(res,
    IntStream.range(0, ms.nameSize()).iterator(),
    ", ",
    i->res.append(xsF+"["+i+"]"));
  res.append(")");
  return res.toString();
  }
  }
public interface PluginType {
  interface WellKnown extends PluginType {
    @Override default String url() {
      String p = this.getClass().getCanonicalName();
      assert p.startsWith("is.L42.connected.");
      return p;
   }
  }

  String url();

  default List<ast.Ast.Type> typeOf(Program p, Using u){
    Method m=ProtectedPluginType.getMethod(this,p, u);
    ActionType ann = m.getAnnotation(ActionType.class);
    assert ann!=null;
    ArrayList<Ast.Type> ts=new ArrayList<>();
    for(ActionType.NormType path:ann.value()){
      ts.add(path.type);
      }
    return ts;
    };
  default Object execute(Program p, Using u){
    //NOTE: this is not called with compilation but only with single steps";
    Method m=ProtectedPluginType.getMethod(this,p, u);
    return ProtectedPluginType.executeMethod(m, p, this, u.getEs().toArray());
    }
  default String executableJ(UsingInfo s,String e,List<String>es,java.util.Map<String,Integer> labels){
    StringBuilder res=new StringBuilder();
    assert s!=null;
    assert s.usingMs!=null;
    String plgName=this.getClass().getName();
    res.append("platformSpecific.javaTranslation.Resources.plgExecutor(");
    res.append("\""+s.usingMs.nameToS()+"\",");
    res.append("platformSpecific.javaTranslation.Resources.getP(), ");
    res.append("new "+plgName+"(), ");
    res.append(ProtectedPluginType.executableWrapper(s.usingMs, labels));
    //plgExecutor("PathName",p,new plgName(),
    //  (plF,xsF)->plF.
    //  MnameEncoded£xn1£xn2(xsF[0],..,xsF[n]),
    //()->e,e0,..,en);
    res.append(", ()->");
    res.append(e);
    res.append("");
    for(String ei:es){
      res.append(", ");
      res.append(ei);
      }
    res.append(")");
    return res.toString();
    }
  }

package platformSpecific.fakeInternet;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.ExpCore.Using;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import facade.PData;
import platformSpecific.javaTranslation.Resources;
import programReduction.Program;
import tools.Assertions;
import tools.Map;
import tools.StringBuilders;

public class PluginWithPart implements PluginType{
  public final String url;
  public final String part;
  public final Class<?>pointed;
  public PluginWithPart(String url, String part) {
    super();
    this.url = url;
    this.part = part;
    try {
      this.pointed=Class.forName(part);
      }
    catch (ExceptionInInitializerError e) {
      e.printStackTrace();
      throw e;
      }
    catch (ClassNotFoundException e) {
      throw new ErrorMessage.InvalidURL(url+"\n"+part,null);
      }
    }
  public List<Type> typeOf(Program p, Using u){
    List<Type> res=new ArrayList<>();
    UsingInfo ui=new UsingInfo(p,u);
    if(ui.methOrKs==null){ res.add(jTo42(Void.TYPE));}
    else{
      if(ui.methOrKs instanceof Constructor){
        res.add(jTo42(ui.plgInfo.plgClass));
        }
      if(ui.methOrKs instanceof Method){
        res.add(jTo42(((Method)ui.methOrKs).getReturnType()));
        }
      }
    if (!ui.staticMethod) {res.add(jTo42(ui.plgInfo.plgClass));}
    for(String t :ui.names)
    res.add(jTo42(t));
    return res;
    }
  private static ast.Ast.Type jTo42(Class<?> jt){
    if (jt.equals(Void.TYPE)){return Type.immVoid;}
    if (jt.equals(ast.Ast.Path.class)){return Type.classAny;}
    return Type.immLibrary;
    }
  private static ast.Ast.Type jTo42(String jt){
    jt=jt.substring(1);
    jt=jt.substring(jt.indexOf("_"));
    if (jt.equals("_ast%Ast$Path")){
      return Type.classAny;
      }
    return Type.immLibrary;
    }
  public Object execute(Program p, Using u){
    assert false: "is this not called with compilation?";
    //rest of implementation is wrong
    Method method = ProtectedPluginType.getMethod(pointed, p, u.getS().nameToS(), u.getS().getNames().size(),u);
    List<ExpCore> es = u.getEs();
    ExpCore rec=null;
    if ((method.getModifiers() & Modifier.STATIC) == 0) {
    rec=es.get(0);
    es=es.subList(1,es.size());
    }
    return ProtectedPluginType.executeMethod(method, p, rec, es.toArray());
    }
  public static class PlgInfo implements Serializable{
    public String plgString;
    public String plgName;
    public Class<?> plgClass;
    public PlgInfo(Doc doc){
    plgString=doc._getParameterForPlugin();
    plgString=plgString.trim();
    plgName=doc._getParameterForPluginPart();
    if(plgName!=null){
      plgName=plgName.trim();
      try{plgClass=Class.forName(plgName);}
      catch(ClassNotFoundException|SecurityException e){
        throw Assertions.codeNotReachable();
        }
      }
    }
  }
    @SuppressWarnings("serial")
    public static class UsingInfo implements Serializable{
    public static class NonExistantMethod extends RuntimeException{}
    public boolean staticMethod=false;
    public List<String> names;// avoiding _this, just the parameter names, encoding java types
    public String jMethName=null;//can also be new for constructors and instanceof
    public boolean needPData=false;//is using.ms.m starts with #
    public List<String> ts=new ArrayList<>();//types as javaCanonicalNames
    public List<Class<?>>jts=new ArrayList<>();
    //assert names.size()==ts.size(), but jts.size()==ts.size() or ts.size()+1 if needPData
    public Object methOrKs=null;//will stay null for instanceof
    public boolean isVoid=false;
    public PlgInfo plgInfo;
    public Ast.MethodSelector usingMs;
    public UsingInfo(PlgInfo pi,Method m){
      plgInfo=pi;
      methOrKs=m;
      isVoid=m.getReturnType().equals(Void.TYPE);
      staticMethod=Modifier.isStatic(m.getModifiers());
      jMethName=m.getName();
      jts.addAll(Arrays.asList(m.getParameterTypes()));
      commonInit();
      }
    private void commonInit() {
      names=new ArrayList<>();
      List<Class<?>> jtsNoPData = jts;
      needPData=false;
      if(!jts.isEmpty() && jts.get(0).equals(PData.class)){jtsNoPData.remove(0);needPData=true;}
      if(!staticMethod){names.add("_this");}
      {int i=0;for(Class<?> jt:jtsNoPData){i++;//Ok, we star at 1 for first parameter
        String t=jt.getCanonicalName();
        ts.add(t);
        String name=EncodingHelper.javaClassToX(i,jt.getName());
        names.add(name);
        }}
      assert names!=null;
      usingMs=Ast.MethodSelector.of(needPData?"#"+jMethName:jMethName, names);
    }
    public UsingInfo(PlgInfo pi,Constructor<?> m){
      plgInfo=pi;
      methOrKs=m;
      isVoid=false;
      staticMethod=true;
      jMethName="new";
      jts.addAll(Arrays.asList(m.getParameterTypes()));
      commonInit();
      }
    public UsingInfo(Program p,Using s){
      usingMs=s.getS();
      plgInfo=new PlgInfo(p.extractClassB(s.getPath()).getDoc1());
      if(usingMs.nameToS().equals("instanceof")){
        if(!(usingMs.getNames().size()==1 && usingMs.getNames().get(0).equals("_this"))){
          throw new NonExistantMethod();
          }
        names=Collections.emptyList();
        jMethName="instanceof";
        isVoid=true;//expression executed if instanceOf is True
        return;
        }
      names = s.getS().getNames();
      staticMethod=true;
      if(!names.isEmpty() && names.get(0).equals("_this")){
        staticMethod=false;
        names=names.subList(1,names.size());
        }
      jMethName=s.getS().nameToS();
      needPData=jMethName.startsWith("#");
      if(needPData){jMethName=jMethName.substring(1);}
      if(plgInfo.plgName!=null){
        for(String n:names){
          String t=EncodingHelper.xToJavaClass(n);
          Class<?>tClass;
          tClass = tToClass(t);
          ts.add(tClass.getCanonicalName());//t has $ for nestedclasses, canonicalname have . instead
          jts.add(tClass);
          }
        if(needPData){
          jts.add(0,PData.class);
          }
        if(jMethName.equals("new")){
          if(!this.staticMethod){throw new NonExistantMethod();}
          try{methOrKs=plgInfo.plgClass.getDeclaredConstructor(jts.toArray(new Class<?>[0]));}
          catch(NoSuchMethodException e){
            throw new NonExistantMethod();
            }
          }
        else{
          Method m;
          try{m=Class.forName(plgInfo.plgName).getMethod(jMethName, jts.toArray(new Class<?>[0]));}
          catch(ClassNotFoundException|NoSuchMethodException|SecurityException e){
            throw new NonExistantMethod();
            }
          if(Modifier.isStatic(m.getModifiers())!=this.staticMethod){
            throw new NonExistantMethod();
            }
          isVoid=m.getReturnType().equals(Void.TYPE);
          methOrKs=m;
          }
        }
      }
    private Class<?> tToClass(String t) {
     switch(t){
       case "byte":return byte.class;
       case "short":return short.class;
       case "int":return int.class;
       case "long":return long.class;
       case "float":return float.class;
       case "double":return double.class;
       case "char":return char.class;
       case "boolean":return boolean.class;
       }
      try {return Class.forName(t);}
      catch (ClassNotFoundException e) { throw new Error(e); }
      }
    }


  @Override
  public String executableJ(UsingInfo ui,String te,List<String>tes,Set<String> labels){
    if (ui.methOrKs==null){return executableInstanceof(ui,te,tes,labels);}
    //if static meth tes=e1..en, parRec=null, rec=plgName
    //else  tes=e0..en, parRec=e0, rec=plF //that is, e1..en do not contains the first of tes
    //if Meth par1 of type PData opt=`Resources.pData(),`
    //else opt=``
    String parRec="null";
    if(!ui.staticMethod){
      parRec=tes.get(0);
      tes=tes.subList(1,tes.size());
      }
    String opt="";
    if(ui.needPData){
      if(ui.ts.size()==0){
        opt="platformSpecific.javaTranslation.Resources.pData() ";
        }
      else{opt="platformSpecific.javaTranslation.Resources.pData(),";}
      }
    StringBuilder res=new StringBuilder();
    //plgExecutor("`PathName`",p,(plgName)`parRec`,
    res.append("platformSpecific.javaTranslation.Resources.plgExecutor(");
    res.append("\""+ui.usingMs.nameToS()+"\",");
    res.append("platformSpecific.javaTranslation.Resources.getP(), ");
    res.append("("+ui.plgInfo.plgClass.getCanonicalName()+")"+parRec+", ");
    //(plF,xsF)->{ `T1` _1;..`Tn` _n;
    String plF="L"+Functions.freshName("pl",labels);
    String trF="L"+Functions.freshName("tr",labels);
    String xsF="L"+Functions.freshName("xs",labels);
    res.append("("+plF+","+xsF+")->{\n");
    {int i=0;for(String t:ui.ts){i++;
      res.append(t+" _"+i+";\n");
      }}
    //  try{_1=(`T1`)xsF[0];.._n=(`Tn`)xsF[n-1];}
    res.append("try{\n");
    {int i=0;for(String t:ui.ts){i++;
      if(t.equals("ast.Ast.Path")){
        res.append("_"+i+"=auxiliaryGrammar.EncodingHelper.ensureExtractPathFromJava("+xsF+"["+(i-1)+"]);\n");
        }
      else {
        res.append("_"+i+"=("+t+")"+xsF+"["+(i-1)+"];\n");
        }
      }}
    res.append("}\n");
    //  catch(ClassCastException cce){assert false; throw DoNotAct;}
    res.append("catch(ClassCastException cce){assert false:\"\"+cce+java.util.Arrays.asList("+ xsF+");throw platformSpecific.javaTranslation.Resources.notAct;}");
    //  return plgName.name(`opt` _1,..,_n);
    res.append("\n try{\n");
    if(!ui.isVoid){res.append("return ");}
    if(ui.methOrKs instanceof Method){
      if(ui.staticMethod){
        res.append(ui.plgInfo.plgClass.getCanonicalName()+"."+ui.jMethName+"("+opt);
        }
      else{
        res.append(plF+"."+ui.jMethName+"("+opt);
        }
      }
    else{
      res.append("new "+ui.plgInfo.plgClass.getCanonicalName()+"("+opt);
    }
    StringBuilders.formatSequence(res,
      IntStream.range(1, ui.ts.size()+1).iterator(),
      ", ",i->res.append("_"+i));
    res.append(");\n");
    if(ui.isVoid){res.append("return platformSpecific.javaTranslation.Resources.Void.instance;\n");}
    res.append("}catch(Throwable "+trF+
            "){throw new platformSpecific.javaTranslation.Resources.Error("+trF+");}"
            + "},()->");
    //  },
    //()->`te`,`te1`,..,`ten`);
    res.append(te);
    for(String t:tes){res.append(", "+t);}
    res.append(")");
    return res.toString();
    }
  private String executableInstanceof(UsingInfo ui, String te, List<String> tes, Set<String> labels) {
    String path=ui.plgInfo.plgClass.getCanonicalName();
    String plF="L"+Functions.freshName("pl",labels);
    String xsF="L"+Functions.freshName("xs",labels);
    //plgExecutor("instanceofPath",p,obj,
    //  (plF,empty)->{ if (plF instanceof Path){throw Resources.notAct;}
    //                 return Resources.Void.instance;}
    // ,()->e);
    return "platformSpecific.javaTranslation.Resources.plgExecutor("
         + "\"instanceof "+path+"\",platformSpecific.javaTranslation.Resources.getP(),"
         + "(Object)"+tes.get(0)+" ,"
         + "("+plF+","+xsF+")->{ if ("+plF+" instanceof "+path+"){throw platformSpecific.javaTranslation.Resources.notAct;}"
         + "return platformSpecific.javaTranslation.Resources.Void.instance;}"
         + ",()->"+te+")"
        ;
  }
}
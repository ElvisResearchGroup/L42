package platformSpecific.javaTranslation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import profiling.Timer;
import tools.Assertions;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import facade.Configuration;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.InterfaceHeader;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Util;
import ast.Util.PathMwt;
import ast.ExpCore.*;
public class Translator {
  public static Object runString(String s){
    try{return runStringExc(s);}
    catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) throw (RuntimeException)ex.getCause();
      if (ex.getCause() instanceof Error) throw (Error)ex.getCause();
      throw new Error(ex.getCause());
      }
    catch(CompilationError| IllegalAccessException| IllegalArgumentException| NoSuchMethodException| SecurityException| ClassNotFoundException e){ throw new Error(e);}
    }
  public static Object runStringExc(String s) throws CompilationError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    String fileName="generated.Program42";
    SourceFile file = new SourceFile(fileName,s);
    List<SourceFile> files = Arrays.asList(file);
    //System.out.println("Compilation Iteration ready to compile");
    ClassLoader cl;
    Timer.activate("InMemoryJavaCompiler.compile");try{
    cl=InMemoryJavaCompiler.compile(files);//can throw, no closure possible
    }finally{ Timer.deactivate("InMemoryJavaCompiler.compile");}
    //System.out.println("Compilation Iteration complete compilation, start class loading");
    Class<?> cl0 = cl.loadClass(fileName);
    //System.out.println("Compilation Iteration start method retrival");
    Method m0 = cl0.getDeclaredMethod("execute0");
    //System.out.println("Compilation Iteration ready to execute");
    assert Resources.getP()!=null;
    Timer.activate("InMemoryJavaCompiler.execute");try{
    Object result = m0.invoke(null);
    //System.out.println("Compilation Iteration execution complete");
    return result;
    }finally{Timer.deactivate("InMemoryJavaCompiler.execute");}

  }
  public static String translateProgram(Program p,ExpCore e){
    Resources.clearRes();
    Map<String,ClassB> map=new LinkedHashMap<String,ClassB>();
    Map<String,ClassB> mapNorm=new LinkedHashMap<String,ClassB>();
    addP(0,p,map,p);
    StringBuilder res=new StringBuilder();
    res.append("package generated;");
    res.append("@SuppressWarnings(\"all\")");
    res.append("public class Program42{\n");
    //res.append("public static <T> T block(java.util.function.Supplier<T> p){return p.get();}\n");
    //res.append("public static platformSpecific.javaTranslation.Resources.Void Unused=null;\n");
    //res.append("public static Object getRes(String s){return  javaTranslation.Resources.getRes(s);}\n");
    res.append("public static Object execute0()");
    TranslateExpression.of(e, res);
    res.append("\n");
    for(String s:map.keySet()){
      mapNorm.put(s,normalizeClass(p,map.get(s)));
    }
    for(String s:mapNorm.keySet()){
      TranslateClass.of(p,s,mapNorm.get(s),res);
    }
    res.append("}");
    return res.toString();
    }
  public static void addP(int level,Program p,Map<String,ClassB> map,Program original){
    if(p.isEmpty()){return;}

    add(level,Collections.emptyList(),p.topCb(),map,original);
    addP(level+1,p.pop(),map,original);
  }

  public static void add(int level,List<String> cs,ClassB cb, Map<String,ClassB> map,Program original){
    Ast.Path p=Ast.Path.outer(level, cs);
    if(cb.getStage().getStage()==Stage.Star  && IsCompiled.of(cb)){//otherwise is "meta"
      map.put(nameOf(level,cs),useFrom(cb,p));
      }
    else{//generate only for metaprogramming
      map.put(nameOf(level,cs),new ExpCore.ClassB(
          Doc.factory("DebugInfo: is interface since (cb.getStage()!=Stage.Star :"
            +(cb.getStage().getStage()!=Stage.Star)+") or since !IsCompiled.of(cb) :"+!IsCompiled.of(cb)+")"
          ),Doc.empty(),true,Collections.emptyList(),Collections.emptyList(),new Util.CachedStage()));
      }
    for(Member m:cb.getMs()){
      if (!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      if(!(nc.getInner() instanceof ClassB)){continue;}
      if(cs.isEmpty() &&level>0){
        if(nc.getInner()==original.getCb(level-1)){continue;}
        //avoid generation of multiple versions of the same thing
      }
      ArrayList<String> newCs=new ArrayList<>(cs);
      newCs.add(nc.getName());
      add(level,newCs,(ClassB) nc.getInner(),map,original);
    }
  }

  private static ClassB useFrom(ClassB ct, Path p) {
    ArrayList<Member> ms=new ArrayList<Member>();
    for(Member m:ct.getMs()){
      m.match(nc->null,
        mi->ms.add(From.from(Program.extractMwt(mi, ct),p)),
        mt->ms.add(From.from(mt, p))
        );
      }
    for(PathMwt pmwt:ct.getStage().getInherited()){
      if(Program.getIfInDom(ms,pmwt.getMwt().getMs()).isPresent()){continue;}
      ms.add(From.from(pmwt.getMwt(), p));
    }
    List<Path> sup = tools.Map.of(pi->(Path)From.fromP(pi,p),ct.getSupertypes());
    return ct.withMs(ms).withSupertypes(sup);
  }
  private static ClassB normalizeClass(Program p,ClassB ct) {
    List<Path> sup = tools.Map.of(pi->(Path)
        Norm.of(p,pi),ct.getSupertypes());
    return Norm.ofAllMethodsOf(p, ct,false).withSupertypes(sup);
  }
  public static String nameOf(Ast.Type t) {
    Path p=((NormType)t).getPath();
    return Translator.nameOf(p);
    }
  public static String nameOf(Ast.MethodSelector ms) {
    return nameOf(ms.getName(),ms.getNames());
    }
  public static String nameOf(String name, List<String> names) {
    String result="M"+name;
    for(String x:names){result+="£x"+x;}
    return nameOf(result);
    }
  public static String nameOf(Path p) {
    if (p.equals(Path.Any())){return "Object";}
    if (p.equals(Path.Library())){return "Object";}
    if (p.equals(Path.Void())){return "platformSpecific.javaTranslation.Resources.Void";}
    return nameOf(p.outerNumber(),p.getCBar());
  }
  public static String nameOf(int level, List<String> cs) {
    String res="Outer"+level;
    for(String s:cs){res+="::"+s;}
    return nameOf(res);
  }
  public static String nameOf(String s){
      s=s.replace("::","£_");
      s=s.replace("%","£p");
      s=s.replace("#","£h");
      return s;
  }
  public static String name42Of(String javaName){
      String s=javaName;
      s=s.replace("£_","::");
      s=s.replace("£p","%");
      s=s.replace("£h","#");
      return s;
  }
}

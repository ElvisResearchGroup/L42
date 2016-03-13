package platformSpecific.javaTranslation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import reduction.Facade;
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
  public static Object runString( String s){
    try{return runStringExc(s);}
    catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) throw (RuntimeException)ex.getCause();
      if (ex.getCause() instanceof Error) throw (Error)ex.getCause();
      throw new Error(ex.getCause());
      }
    catch(CompilationError| IllegalAccessException| IllegalArgumentException| NoSuchMethodException| SecurityException| ClassNotFoundException e){
      //try {Files.write(Paths.get("C:\\Users\\marco\\Desktop\\eclipseMars\\L42Local\\Tests\\src\\generated\\Program42.java"), s.getBytes());}catch (IOException _e) {throw new Error(_e);}
      try {Files.write(Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/Program42.java"), s.getBytes());}catch (IOException _e) {throw new Error(_e);}
      throw new Error(e);
      }
    }
  public static Object runStringExc(String s) throws CompilationError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    String fileName="generated.Program42";
    SourceFile file = new SourceFile(fileName,s);
    List<SourceFile> files = Arrays.asList(file);
    //System.out.println("Compilation Iteration ready to compile");
    ClassLoader cl;
    Timer.activate("InMemoryJavaCompiler.compile");try{
    cl=InMemoryJavaCompiler.compile(files);//can throw, no closure possible
    Facade.setLastLoader(cl);
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
    Configuration.typeSystem.computeInherited(p.pop(),p.topCb());
    add(level,Collections.emptyList(),p.topCb(),map,original);
    addP(level+1,p.pop(),map,original);
  }

  public static void add(int level,List<String> cs,ClassB cb, Map<String,ClassB> map,Program original){
    Ast.Path p=Ast.Path.outer(level, cs);
    if(cb.getStage().getStage()==Stage.Star  && IsCompiled.of(cb)){//otherwise is "meta"
      assert cb.getStage().getInheritedPaths()!=null;
      ClassB cbUF=useFrom(cb,p);
      assert cbUF.getStage().getInheritedPaths()!=null;
      map.put(Resources.nameOf(level,cs),cbUF);
      }
    else{//generate only for metaprogramming
      ExpCore.ClassB cbMP = new ExpCore.ClassB(
          Doc.factory("DebugInfo: is interface since (cb.getStage()!=Stage.Star :"
            +(cb.getStage().getStage()!=Stage.Star)+") or since !IsCompiled.of(cb) :"+!IsCompiled.of(cb)+")"
          ),Doc.empty(),true,Collections.emptyList(),Collections.emptyList(),new Util.CachedStage());
      cbMP.getStage().setInheritedPaths(Collections.emptyList());
      cbMP.getStage().setInherited(Collections.emptyList());
      assert cbMP.getStage().getInheritedPaths()!=null;
      map.put(Resources.nameOf(level,cs),cbMP);
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
    List<Path> supAll = tools.Map.of(pi->(Path)From.fromP(pi,p),ct.getStage().getInheritedPaths());
    ClassB res= ct.withMs(ms).withSupertypes(sup);
    res=res.withStage(res.getStage().copyMostStableInfo());
    res.getStage().setInheritedPaths(supAll);
    return res;
  }
  private static ClassB normalizeClass(Program p,ClassB ct) {
    assert ct.getStage().getInheritedPaths()!=null;
    List<Path> sup = tools.Map.of(pi->(Path)
        Norm.of(p,pi),ct.getSupertypes());
    ClassB result= Norm.ofAllMethodsOf(p, ct,false).withSupertypes(sup);
    result.withStage(ct.getStage().copyMostStableInfo());
    result.getStage().setInheritedPaths(tools.Map.of(pi->(Path)
        Norm.of(p,pi),ct.getStage().getInheritedPaths()));
    return result;
  }
}

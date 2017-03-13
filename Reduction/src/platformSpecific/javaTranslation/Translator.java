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
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import profiling.Timer;
import reduction.Facade;
import tools.Assertions;
import coreVisitors.From;
import coreVisitors.IsCompiled;
import facade.Configuration;
import facade.L42;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import programReduction.Program.EmptyProgram;
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
import ast.ExpCore.ClassB.Phase;
import ast.Util;
import ast.Util.PathMwt;
import ast.ExpCore.*;
public class Translator {
  public Object runMap(){
    try{return runStringExc();}
    catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) throw (RuntimeException)ex.getCause();
      if (ex.getCause() instanceof Error) throw (Error)ex.getCause();
      throw new Error(ex.getCause());
      }
    catch(CompilationError| IllegalAccessException| IllegalArgumentException| NoSuchMethodException| SecurityException| ClassNotFoundException e){
      throw new Error(e);
      }
    }
  public Object runStringExc() throws CompilationError, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
    List<SourceFile> files =new ArrayList<>();
    for(String k :map.keySet()){//"generated.Program42";
      files.add(new SourceFile(k,map.get(k)));
      //try{Files.write(Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+k+".java"), map.get(k).getBytes());}catch (IOException _e) {throw new Error(_e);}
      //try{Files.write(Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+k+".java"), map.get(k).getBytes());}catch (IOException _e) {throw new Error(_e);}
      }
    //System.out.println("Compilation Iteration ready to compile");
    MapClassLoader cl=((Facade)Configuration.reduction).getLastLoader();
    Timer.activate("InMemoryJavaCompiler.compile");try{
    cl=InMemoryJavaCompiler.compile(cl==null?ClassLoader.getSystemClassLoader():cl,files);//can throw, no closure possible
    ((Facade)Configuration.reduction).setLastLoader(cl);
    }finally{ Timer.deactivate("InMemoryJavaCompiler.compile");}
    //System.out.println("Compilation Iteration complete compilation, start class loading");
    Class<?> cl0 = cl.loadClass("generated."+this.mainName);
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
  HashMap<String,String> map;
  String mainName;
  public static  Translator translateProgram(Program p,ExpCore e){
    Translator t=new Translator();
    t.map=new HashMap<>();
    //Resources.clearRes();
    Map<String,ClassB> map=new LinkedHashMap<String,ClassB>();
    Map<String,ClassB> mapNorm=new LinkedHashMap<String,ClassB>();
    addP(0,p,map,p);
    {
      StringBuilder res=new StringBuilder();
      t.mainName=Functions.freshName("Execute_",L42.usedNames);
      res.append("package generated;");
      res.append("@SuppressWarnings(\"all\")");
      res.append("public class "+t.mainName+"{\n");
      res.append("public static Object execute0()");
      TranslateExpression.of(e, res);
      res.append("\n");
      res.append("}");
      t.map.put(t.mainName, res.toString());
      }
    MapClassLoader cl=((Facade)Configuration.reduction).getLastLoader();
    for(String s:map.keySet()){
      if (cl!=null && cl.map().containsKey("generated."+s)){
        continue;
        //ClassB cb=map.get(s);
        //if(!cb.getDoc1().getS().contains("##@")){continue;}
      }
      if (!map.get(s).getPhase().subtypeEq(Phase.Typed)){continue;}
      ClassB cbNorm=map.get(s);//Hope it work, it was normalized before
      assert cbNorm.getPhase().subtypeEq(Phase.Typed);
      mapNorm.put(s,cbNorm);
    }

    for(String s:mapNorm.keySet()){
      StringBuilder resi=new StringBuilder();
      resi.append("package generated;");
      resi.append("@SuppressWarnings(\"all\")");
      ClassB cbNorm = mapNorm.get(s);
      assert cbNorm.getPhase().subtypeEq(Phase.Typed);
      TranslateClass.of(p,s,mapNorm.get(s),resi);
      t.map.put(s, resi.toString());
    }
    return t;
    }
  public static void addP(int level,Program p,Map<String,ClassB> map,Program original){
  add(level,Collections.emptyList(),p.top(),map,original);
  Program popped;try{popped=p.pop();}catch(EmptyProgram ep){return;}
  addP(level+1,popped,map,original);
  }

  public static void add(int level,List<Ast.C> cs,ClassB cb, Map<String,ClassB> map,Program original){
    Ast.Path p=Ast.Path.outer(level, cs);

    if(cb.getPhase().subtypeEq(Phase.Typed)  && IsCompiled.of(cb)){//otherwise is "meta"
      //assert cb.getStage().getInheritedPaths()!=null;
      ClassB cbUF=useFrom(cb,p);
      if(!cs.isEmpty()){//ok to ignore empty ones, since not complete?
        map.put(Resources.nameOf(level,cs),cbUF);
      }
      }
    else{//generate only for metaprogramming //Can be ignored now with typemap
      /*ExpCore.ClassB cbMP = new ExpCore.ClassB(
          Doc.factory("##@DebugInfo: is interface since (cb.getStage()!=Stage.Star :"
            +(cb.getStage().getStage()!=Stage.Star)+") or since !IsCompiled.of(cb) :"+!IsCompiled.of(cb)+")"
          ),Doc.empty(),true,Collections.emptyList(),Collections.emptyList(),new Util.CachedStage());
      cbMP.getStage().setInheritedPaths(Collections.emptyList());
      cbMP.getStage().setInherited(Collections.emptyList());
      assert cbMP.getStage().getInheritedPaths()!=null;
      map.put(Resources.nameOf(level,cs),cbMP);
      */}
    for(Member m:cb.getMs()){
      if (!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      if(!(nc.getInner() instanceof ClassB)){continue;}
      if(cs.isEmpty() &&level>0){
        if(nc.getInner()==original.get(level-1)){continue;}
        //avoid generation of multiple versions of the same thing
      }
      ArrayList<Ast.C> newCs=new ArrayList<>(cs);
      newCs.add(nc.getName());
      add(level,newCs,(ClassB) nc.getInner(),map,original);
    }
  }

  //this should take a class, strip out nested and 'from' it so that it is as at top level
  static ClassB useFrom(ClassB ct, Path p) {
    ArrayList<Member> ms=new ArrayList<Member>();
    for(Member m:ct.getMs()){
      m.match(nc->null,
        mi->{throw Assertions.codeNotReachable();},
        mt->ms.add(From.from(mt, p))
        );
      }
    //for(PathMwt pmwt:ct.getStage().getInherited()){
    for(PathMwt pmwt:Collections.<PathMwt>emptyList()){
      if(Functions.getIfInDom(ms,pmwt.getMwt().getMs()).isPresent()){continue;}
      ms.add(From.from(pmwt.getMwt(), p));
    }
    List<Path> sup = tools.Map.of(ti->(Path)From.fromP(ti.getNT().getPath(),p),ct.getSupertypes());
    List<Path> supAll = sup;//tools.Map.of(pi->(Path)From.fromP(pi,p),ct.getStage().getInheritedPaths());
    ClassB res= ct.withMs(ms).withSupertypes(tools.Map.of(pi->pi.toImmNT(),sup));
    return res;
  }
  
}

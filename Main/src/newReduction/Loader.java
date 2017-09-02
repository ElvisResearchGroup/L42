package newReduction;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ExpCore;
import ast.L42F;
import ast.ExpCore.ClassB;
import ast.L42F.CD;
import ast.L42F.E;
import ast.MiniJ.M;
import auxiliaryGrammar.Functions;
import ast.MiniJ;
import facade.Configuration;
import facade.L42;
import newReduction.ClassTable.Element;
import newTypeSystem.GuessTypeCore.G;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import platformSpecific.javaTranslation.Resources;
import programReduction.Paths;
import programReduction.Program;

public class Loader {
  public Loader(ClassTable ct) {
    this.ct = ct;
    try {addResource();}
    catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | CompilationError e) {throw new Error(e);}
    }
  public ClassB getLib(int index) {
    Element e=ct.get(index);
    ClassB cb=e.cachedSrc;
    if(cb!=null) {return cb;}
    e.reCache(currentP);
    assert e.cachedSrc!=null;
    return e.cachedSrc;

  }
  private void addResource()
      throws CompilationError, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    M m1=new M(true,"Object","LoadLib",Collections.singletonList("int"),Collections.singletonList("index"),new MiniJ.RawJ(
        "{System.out.println(\"askedLoadLib \"+index);return loader.getLib(index);}\n"
        + "public static newReduction.Loader loader;"));
    MiniJ.CD cdResource=new MiniJ.CD(false, "Resource",Collections.emptyList(),Collections.singletonList(m1));
    List<SourceFile> files =Collections.singletonList(new SourceFile(
      cdResource.getCn(),"package generated;\n"+MiniJToJava.of(cdResource)
      ));
    MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
    Class<?> cl0 = clX.loadClass("generated."+cdResource.getCn());
    Field fLoader = cl0.getDeclaredField("loader");
    fLoader.set(null,this);
  }
  private ClassTable ct;
  private Program currentP=null;
  private Cache cache=new Cache();
  HashMap<String, ClassFile> clMap=new HashMap<>();
  MapClassLoader cl=new MapClassLoader(clMap, ClassLoader.getSystemClassLoader());

  static public boolean validCache(ClassTable ct1, Map<Integer,String>dep, List<L42F.CD>cds){
    if(dep.size()!=cds.size()){return false;}
    for(int cn:dep.keySet()){
      if(!cds.contains(ct1.get(cn).cd)){return false;}
      }
    return true;
    }
  public void processDep(Map<Integer,String>dep){
    Cache.Element cDep = cache.get(new HashSet<>(dep.values()));
    if(cDep==null ||validCache(ct, dep, cDep.cds)){
      Set<String>oldDom=new HashSet<>(clMap.keySet());
      javac(dep);//this enrich clMap
      List<L42F.CD> cds=new ArrayList<>();
      for(int i:dep.keySet()){cds.add(ct.get(i).cd);}
      HashMap<String,ClassFile>newClMap=new HashMap<>();
      for(String s:clMap.keySet()){
        if(!oldDom.contains(s)){clMap.put(s,clMap.get(s));}
        }
      this.cache.add(new HashSet<>(dep.values()), cds, newClMap);
      return;
      }
    for(String cn : cDep.clMap.keySet()){
      ClassFile bytecode=clMap.get(cn);
      ClassFile bytecodeOld=clMap.get(cn);
      assert bytecodeOld==null || bytecodeOld.equals(bytecode);
      if(bytecodeOld==null){clMap.put(cn,bytecode);}
      }
    }
  private void javac(Map<Integer, String> dep) {
  List<SourceFile> readyToJavac=new ArrayList<>();
    for(int cn:dep.keySet()){
      String cnString=dep.get(cn);
      if(clMap.containsKey(cnString)){continue;}
      MiniJ.CD j=L42FToMiniJ.of(ct, ct.get(cn).cd);
      SourceFile src=new SourceFile(cnString,
        "package generated;\n"+MiniJToJava.of(j));
      readyToJavac.add(src); 
      }
    try{this.cl=InMemoryJavaCompiler.compile(cl,readyToJavac);}
    catch(CompilationError ce){throw new Error(ce);}
    }
  public void load(List<String>names,Program p, Paths paths){
    ct=ct.growWith(names, p, paths).computeDeps();
    List<Map<Integer,String>> chunks = ct.listOfDeps();
    Collections.sort(chunks,(s1,s2)->s1.size()-s2.size());
    //TODO: check is not the opposite
    for(Map<Integer,String>dep:chunks){
      System.out.println("Remove after checking that numbers are in increasing order:"+dep.size());
      processDep(dep);
      }
    }  
  
  ExpCore.ClassB run(Program p, ExpCore e) {
    currentP=p;
    List<Program>ps=new ArrayList<>();
    PG pg=new PG(p,G.of(Collections.emptyMap()),ps);
    E ex = e.accept(pg);
    for(Program pi:ps) {
      ct=ct.plus(new Element(pi,new CD(null,pi.top().getUniqueId(),null,null, null)));
      }
    MiniJ.S sJ=L42FToMiniJS.forBody(ct,ex);
    M m1=new M(true,"Object","execute0",Collections.emptyList(),Collections.emptyList(),sJ);
    String freshName=Functions.freshName("Main£Main", L42.usedNames);
    MiniJ.CD cd=new MiniJ.CD(false, freshName,Collections.emptyList(),Collections.singletonList(m1));
    ct=ct.computeDeps();
    try{return (ExpCore.ClassB)run(cd);}
    catch(InvocationTargetException ite) {
      if (ite.getCause() instanceof RuntimeException) throw (RuntimeException)ite.getCause();
      if (ite.getCause() instanceof Error) throw (Error)ite.getCause();
      throw new Error(ite.getCause());
      }
    catch(CompilationError| ClassNotFoundException| NoSuchMethodException| SecurityException| IllegalAccessException| IllegalArgumentException exc) {
      throw new Error(exc);
    }


    }
  Object run(MiniJ.CD j) throws CompilationError, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    List<SourceFile> files =Collections.singletonList(new SourceFile(
      j.getCn(),"package generated;\n"+MiniJToJava.of(j)
      ));
    System.out.println(MiniJToJava.of(j));
    //try{Files.write(Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+k+".java"), map.get(k).getBytes());}catch (IOException _e) {throw new Error(_e);}
    //try{Files.write(Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+j.getCn()+".java"), files.get(j.getCn()).getBytes());}catch (IOException _e) {throw new Error(_e);}
    MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
    Class<?> cl0 = clX.loadClass("generated."+j.getCn());
    Method m0 = cl0.getDeclaredMethod("execute0");
    Object result = m0.invoke(null);
    return result;
    }
  }

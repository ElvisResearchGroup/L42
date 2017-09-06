package newReduction;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.Ast;
import ast.ExpCore;
import ast.L42F;
import ast.ExpCore.ClassB;
import ast.ExpCore.EPath;
import ast.L42F.CD;
import ast.L42F.Cn;
import ast.L42F.E;
import ast.MiniJ.M;
import auxiliaryGrammar.Functions;
import ast.MiniJ;
import ast.Ast.Path;
import facade.Configuration;
import facade.L42;
import l42FVisitors.CloneVisitor;
import newReduction.ClassTable.Element;
import newTypeSystem.GuessTypeCore.G;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Revertable;
import programReduction.Paths;
import programReduction.Program;
import tools.Assertions;

public class Loader {
  public Loader(java.nio.file.Path path) {
    this.ct = ClassTable.empty;
    boolean isThere=checkPath(path);
    if(!isThere) {
      try {addResource();}
      catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | CompilationError e) {throw new Error(e);}
      }
    else {
      loadCache();
      loadLoader(cl);
      }
    }
  private boolean checkPath(java.nio.file.Path path) {
    this.cacheFile=path;
    if(path==null) {return false;}
    try {
      if(!Files.exists(path)){Files.createFile(path);}
      if(Files.size(path)>50) {return true;}
      }
    catch (IOException e) {throw new Error(e);}
    return false;
    }
  public EPath getPathOf(int index){
    if(Cn.cnAny.getInner()==index) {return EPath.wrap(Path.Any());}
    if(Cn.cnVoid.getInner()==index) {return EPath.wrap(Path.Void());}
    if(Cn.cnLibrary.getInner()==index) {return EPath.wrap(Path.Library());}
    Element e=ct.get(index);
    Path fromming = e.fromming(e.p,currentP);
    return EPath.wrap(fromming);
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
    M m1=new M(true,"Object","£CLoadLib",Collections.singletonList("int"),Collections.singletonList("index"),new MiniJ.RawJ(
      "{return loader.getLib(index);}\n"
      + "public static newReduction.Loader loader;"
      ));
    M m2=new M(true,Revertable.class.getCanonicalName(),"£COf",Collections.singletonList("int"),Collections.singletonList("index"),new MiniJ.RawJ(
      "{return ()->£CPathOf(index);}\n"
      ));
    M m3=new M(true,ast.ExpCore.class.getCanonicalName(),"£CPathOf",Collections.singletonList("int"),Collections.singletonList("index"),new MiniJ.RawJ(
      "{return loader.getPathOf(index);}\n"
      ));
    MiniJ.CD cdResource=new MiniJ.CD(false, "Resource",Collections.emptyList(),Arrays.asList(m1,m2,m3));
    String text="package generated;\n"+MiniJToJava.of(cdResource);
    List<SourceFile> files =Collections.singletonList(new SourceFile(cdResource.getCn(),text));
    //try{Files.write(java.nio.file.Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+cdResource.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
    //try{Files.write(java.nio.file.Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+cdResource.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
    MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
    loadLoader(clX);
  }
  private void loadLoader(MapClassLoader clX){
    try{
      Class<?> cl0 = clX.loadClass("generated.Resource");
      Field fLoader = cl0.getDeclaredField("loader");
      fLoader.set(null,this);
      }
    catch(IllegalAccessException | ClassNotFoundException | NoSuchFieldException | SecurityException e){throw new Error(e);}
    }
  private ClassTable ct;
  private Program currentP=null;
  private Cache cache=new Cache();
  private java.nio.file.Path cacheFile=null;
  HashMap<String, ClassFile> clMap=new HashMap<>();
  MapClassLoader cl=new MapClassLoader(clMap, ClassLoader.getSystemClassLoader());
  private void loadCache() {
    assert this.cacheFile!=null;
    cache=Cache.readFromFile(cacheFile);
    cl.updateFromMap(cache.smap);
    }

  private void saveCache() {
    if(this.cacheFile!=null) {cache.saveOnFile(cacheFile,cl);}
    }

  static public boolean validCache(ClassTable ct1, Map<Integer,String>dep, List<L42F.CD>cds){
    if(dep.size()!=cds.size()){return false;}
    for(int cn:dep.keySet()){
      if(!cds.contains(ct1.get(cn).cd)){return false;}
      }
    return true;
    }
  public void processDep(Map<Integer,String>dep){
    Cache.Element cDep = cache.get(new HashSet<>(dep.values()));
    if(cDep==null ||!validCache(ct, dep, cDep.cds)){
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
    if(!cDep.clMap.isEmpty())System.out.println("Using cache for "+cDep.clMap.keySet());
    for(String cn : cDep.clMap.keySet()){
      ClassFile bytecode=clMap.get(cn);
      ClassFile bytecodeOld=clMap.get(cn);
      assert bytecodeOld==null || bytecodeOld.equals(bytecode);
      if(bytecodeOld==null){clMap.put(cn,bytecode);}
      }
    }
  private void javac(Map<Integer, String> dep) {
    assert ct.isCoherent();
    List<SourceFile> readyToJavac=new ArrayList<>();
    for(int cn:dep.keySet()){
      String cnString=dep.get(cn);
      if(clMap.containsKey("generated."+cnString)){continue;}
      MiniJ.CD j=L42FToMiniJ.of(ct, ct.get(cn).cd);
      String text="package generated;\n"+MiniJToJava.of(j);
      SourceFile src=new SourceFile(cnString,text);
      readyToJavac.add(src);
      //try{Files.write(java.nio.file.Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+cnString+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
      //try{Files.write(java.nio.file.Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+j.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
      }
    if (readyToJavac.isEmpty()){return;}
    try{this.cl=InMemoryJavaCompiler.compile(cl,readyToJavac);}
    catch(CompilationError ce){throw new Error(ce);}
    }
  public void load(Program p, Paths paths){
    ct=ct.growWith(p, paths).computeDeps();
    List<Map<Integer,String>> chunks = ct.listOfDeps();
    Collections.sort(chunks,(s1,s2)->s1.size()-s2.size());
    for(Map<Integer,String>dep:chunks){
      processDep(dep);
      }
    }

  public ExpCore.ClassB run(Program p, ExpCore e) {
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
    String text="package generated;\n"+MiniJToJava.of(j);
    List<SourceFile> files =Collections.singletonList(new SourceFile(
      j.getCn(),text
      ));
    //try{Files.write(java.nio.file.Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+j.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
    //try{Files.write(java.nio.file.Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+j.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
    MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
    Class<?> cl0 = clX.loadClass("generated."+j.getCn());
    Method m0 = cl0.getDeclaredMethod("execute0");
    Object result = m0.invoke(null);
    return result;
    }
  public ExpCore.ClassB execute(Program p,Paths paths,ExpCore e){
    this.load(p,paths); //Loader change state here
    assert this.ct.isCoherent();
    ExpCore.ClassB res= Resources.withPDo(p.reprAsPData(),()->this.run(p,e));//Loader change state here but should be irrelevant
    res=(ExpCore.ClassB)res.accept(new coreVisitors.CloneVisitor(){
      public ExpCore visit(ClassB s) {
        s=(ExpCore.ClassB)super.visit(s);
        return s.withUniqueId(p.getFreshId());
        }
      });
    this.saveCache();
    return res;
  }
  static List<String> computeDbgNames(Program p) {
    List<String>names=new ArrayList<>();
    for(Object o:p.exploredWay()){
      if(o==null){names.add("£E");}
      else if(o instanceof Ast.C){names.add(o.toString());}
      else if(o instanceof Ast.MethodSelector){
        names.add(o.toString()
          .replace("(","£X")
          .replace(",","£X")
          .replace(")","")
          .replace(" ","")
          .replace("#","£H")
          );
        }
      else throw Assertions.codeNotReachable();
      }
    return names;
    }
  }
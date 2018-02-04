package caching;

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
import facade.ErrorFormatter;
import facade.L42;
import l42FVisitors.CloneVisitor;
import newReduction.ClassTable;
import newReduction.L42FToMiniJ;
import newReduction.L42FToMiniJS;
import newReduction.MiniJToJava;
import newReduction.PG;
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
    boolean isThere=updateCachePath(path);
    if(!isThere) {
      try {addResource();}
      catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | CompilationError e) {throw new Error(e);}
      }
    else {
      loadCache();
      loadResource(cl);
      }
    System.out.println("Test cache::: "+cache.contains("Foo"));
    }
  public boolean updateCachePath(java.nio.file.Path path) {
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
      + "public static "+Loader.class.getCanonicalName()+" loader;"
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
    ClassFile resBytes = clX.map().get("generated.Resource");
    assert resBytes!=null;
    cache.fullMap.put("generated.Resource", resBytes);
    loadResource(clX);
  }
  private void loadResource(MapClassLoader clX){
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
  MapClassLoader cl=new MapClassLoader(new HashMap<>(), ClassLoader.getSystemClassLoader());
  private void loadCache() {
    assert this.cacheFile!=null;
    cache=Cache.readFromFile(cacheFile);
    ClassFile resBytes = cache.fullMap.get("generated.Resource");
    assert resBytes!=null;
    cl.map().put("generated.Resource", resBytes);
    System.out.println("cache loaded, elements count: "+cache.smap.size());
    }

  private void saveCache() {
    if(this.cacheFile!=null) {cache.saveOnFile(cacheFile);}
    }

  static public boolean validCache(ClassTable ct1, Map<Integer,String>dep, Map<Integer,L42F.CD>cds){
    if(dep.size()!=cds.size()){
      return false;}
    for(int cn:dep.keySet()){
      CD cdsCn = cds.get(cn);
      CD ct1Cn =ct1.get(cn).cd;
      if(!cdsCn.equals(ct1Cn)){
        return false;}
      }
    return true;
    }
  public void processDep(Map<Integer,String>dep){
    Cache.Element cDep = cache.get(new HashSet<>(dep.values()));
    if(cDep==null ||!validCache(ct, dep, cDep.cds)){
      Set<String>oldDom=new HashSet<>(this.cl.map().keySet());
      javac(dep);//this enrich cl.map()
      Map<Integer,L42F.CD> cds=new HashMap<>();
      for(int i:dep.keySet()){cds.put(i,ct.get(i).cd);}
      HashMap<String,ClassFile>newClMap=new HashMap<>();
      for(String s:this.cl.map().keySet()){
        if(!oldDom.contains(s)){newClMap.put(s,cl.map().get(s));}
        }
      this.cache.add(new HashSet<>(dep.values()), cds, newClMap);
      System.out.println("Javac for  "+newClMap.keySet());
      return;
      }
    //assert validCache(ct, dep, cDep.cds);//holds, and slow
    if(!cDep.byteCodeNames.isEmpty())System.out.println("Using cache for "+cDep.byteCodeNames);
    for(String cn : cDep.byteCodeNames){
      ClassFile bytecode=cache.fullMap.get(cn);
      ClassFile bytecodeOld=cl.map().get(cn);
      bytecode.getBytes();//cache bytes
      if(bytecodeOld!=null){bytecodeOld.getBytes();}//cache bytes
      //it can be that bytecodeOld==null since byteCodeNames stores all the dependencies (not just the new one, that will be hard to model)
      assert bytecodeOld==null || bytecodeOld.equals(bytecode);
      if(bytecodeOld==null){cl.map().put(cn,bytecode);}
      }
    }
  private void javac(Map<Integer, String> dep) {
    assert ct.isCoherent();
    List<SourceFile> readyToJavac=new ArrayList<>();
    for(int cn:dep.keySet()){
      String cnString=dep.get(cn);
      assert !cl.map().containsKey("generated."+cnString);
      if(cl.map().containsKey("generated."+cnString)){continue;}
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
    cl.readAllStreams();
    }
  public void load(Program p, Paths paths){
    ct=ct.growWith(p, paths).computeDeps();
    List<Set<Integer>> chunks = new ArrayList<>(ct.listOfDeps());
    Collections.sort(chunks,(s1,s2)->s1.size()-s2.size());
    Set<Integer>seen=new HashSet<>();
    for(Set<Integer>ci:chunks) {
      ci.removeAll(seen);
      seen.addAll(ci);
      HashMap<Integer,String> ciMap=new HashMap<>();
      for(Integer i:ci) {ciMap.put(i,"generated."+ct.className(i));}
      System.out.println("Dependency about "+ci);
      processDep(ciMap);
      }
    }

  public ExpCore.ClassB run(Program p, ExpCore e) {
    currentP=p;
    List<Program>ps=new ArrayList<>();
    PG pg=new PG(p,G.of(Collections.emptyMap()),ps);
    E ex = e.accept(pg);
    assert ct.isCoherent();
    for(Program pi:ps) {
      int newId=pi.top().getUniqueId();
      Element mapped = ct._get(newId);
      if(mapped==null){
        ct=ct.plus(new Element(pi,new CD(null,newId,null,null, null)));
        }
      }
    assert ct.isCoherent();
    assert ct.isCoherent(ex);
    MiniJ.S sJ=L42FToMiniJS.forBody(ct,ex);
    M m1=new M(true,"Object","execute0",Collections.emptyList(),Collections.emptyList(),sJ);
    String freshName=Functions.freshName("Main£Main", L42.usedNames);
    MiniJ.CD cd=new MiniJ.CD(false, freshName,Collections.emptyList(),Collections.singletonList(m1));
    ct=ct.computeDeps();
    try{return (ExpCore.ClassB)run(cd);}
    catch(InvocationTargetException ite) {
      Throwable cause=ite.getCause();
      if(cause instanceof Resources.L42Throwable){
        Resources.cacheMessage((Resources.L42Throwable)cause);
        System.out.println(ErrorFormatter.reportPlaceOfMetaError(p,p.top()));
        }
      if (cause instanceof RuntimeException) throw (RuntimeException)cause;
      if (cause instanceof Error) throw (Error)cause;
      throw new Error(cause);
      }
    catch(CompilationError| ClassNotFoundException| NoSuchMethodException| SecurityException| IllegalAccessException| IllegalArgumentException exc) {
      throw new Error(exc);
      }
    catch (Throwable t) {
      throw Assertions.codeNotReachable();
      }
    }
  public Object run(MiniJ.CD j) throws CompilationError, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
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
    try{
      ExpCore.ClassB res= Resources.withPDo(p.reprAsPData(),()->this.run(p,e));//Loader change state here but should be irrelevant
      res=(ExpCore.ClassB)res.accept(new coreVisitors.CloneVisitor(){
        public ExpCore visit(ClassB s) {
          s=(ExpCore.ClassB)super.visit(s);
          return s.withUniqueId(p.getFreshId());
          }
        });
      return res;
      }
    finally{this.saveCache();}//keep cache up to date with the failed run expression (and the correct dependencies)
    }
  }
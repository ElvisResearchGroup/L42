package is.L42.platformSpecific.javaTranslation;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.L42.common.Program;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;

class ClassTable{public static ClassTable empty;}
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

private void dbgWriteJavaFiles(String className, String text) throws Error {
  assert className.startsWith("generated."):
    className;
  className=className.substring(10);
  //try{Files.write(java.nio.file.Paths.get("C:/Users/user/git/L42/Tests/src/generated/"+className+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
  //try{Files.write(java.nio.file.Paths.get("/u/staff/servetto/git/L42/Tests/src/generated/"+cdResource.getCn()+".java"), text.getBytes());}catch (IOException _e) {throw new Error(_e);}
  assert !text.contains(";;");
  }
  private ClassTable ct;
  private java.nio.file.Path cacheFile=null;
  MapClassLoader cl=new MapClassLoader(new HashMap<>(), ClassLoader.getSystemClassLoader());
  private void javac(Map<Integer, String> dep) {
    List<SourceFile> readyToJavac=new ArrayList<>();
    for(int cn:dep.keySet()){
      String cnString=dep.get(cn);
      assert !cl.map().containsKey(cnString);//"generated." is included in cnString
      if(cl.map().containsKey(cnString)){continue;}
      StringBuilder j=null;//new L42FToJavaString(ct, ct.get(cn).cd).compute();//TODO: here is J
      String text="package is.L42.metaGenerated;\n"+j;
      SourceFile src=new SourceFile(cnString,text);
      readyToJavac.add(src);
      dbgWriteJavaFiles(cnString, text);
      }
    if (readyToJavac.isEmpty()){return;}
    try{this.cl=InMemoryJavaCompiler.compile(cl,readyToJavac);}
    catch(CompilationError ce){throw new Error(ce);}
    cl.readAllStreams();
    }
  public void load(Program p){
    for(Set<Integer>ci:chunks) {
      ci.removeAll(seen);
      seen.addAll(ci);
      HashMap<Integer,String> ciMap=new HashMap<>();
      for(Integer i:ci) {ciMap.put(i,"is.L42.metaGenerated."+ct.className(i));}
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
    StringBuilder sJ=L42FToMiniJS.forBody(ct,ex);
    String freshName=Functions.freshName("MainÂ£Main", L42.usedNames);
    sJ.insert(0,"package generated;\npublic class "+freshName+"{"+
      "public static Object execute0()");
    sJ.append("}");
    ct=ct.computeDeps();
    try{return (ExpCore.ClassB)run(freshName,sJ);}
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
  public Object run(String cn,StringBuilder j) throws CompilationError, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    List<SourceFile> files =Collections.singletonList(new SourceFile(
      cn,j.toString()
      ));
    dbgWriteJavaFiles("generated."+cn, j.toString());
    MapClassLoader clX=InMemoryJavaCompiler.compile(cl,files);//can throw, no closure possible
    Class<?> cl0 = clX.loadClass("generated."+cn);
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
    finally{
      this.saveCache();
      L42.afterMainCleanUp();
      }//keep cache up to date with the failed run expression (and the correct dependencies)
    }
  }
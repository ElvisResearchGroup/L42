package is.L42.translationToJava;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L;
import is.L42.generated.P;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.ClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.MapClassLoader.SClassFile;
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.tools.InductiveSet;
import is.L42.top.Top;
import is.L42.typeSystem.Coherence;

import static is.L42.tools.General.L;

class Element{
  public Element(L l,List<C> path,String cIds,SourceFile source){
    this.l=l;this.path=path;this.cIds=cIds;this.source=source;
    }
  Core.L l;
  List<C> path;
  String cIds;
  SourceFile source; 
  }
public class Loader {
  public String toString(){
    String res="";
    for(var e:loaded.values()){res+=e.cIds+"\n"+e.source+"\n\n";}
    return res;
    }
  private final ArrayList<L42£Library> libs=new ArrayList<>();
  public int libsCachedSize(){return libs.size();}//needed for double checking on caching
  public int bytecodeSize(){return classLoader.map().size();}//needed for double checking on caching
  final HashMap<String,Element> loaded=new HashMap<>();
  final MapClassLoader classLoader=new MapClassLoader(new HashMap<>(),ClassLoader.getSystemClassLoader());
  public void loadByteCodeFromCache(List<SClassFile> bytecode,List<L42£Library>newLibs){
    libs.addAll(newLibs);
    for(var e:bytecode){
      var res=classLoader.map().put(e.name,e.toCF());
      assert res==null;
      }
    }
  public Core.L runNow(Program p,C c,Core.E e,List<SClassFile> outNewBytecode,ArrayList<L42£Library> newLibs) throws CompilationError, InvocationTargetException{
    int oldLibNum=libs.size();
    J j=new J(p,G.empty(),false,libs,false){
      @Override public boolean precomputeCoherent(){return false;}
      };
    j.visitE(e);//the goal here is not to generate the p.top class
    newLibs.addAll(libs.subList(oldLibNum,libs.size()));
    String name="£c"+c.inner()+"£n"+p.topCore().info()._uniqueId();
    if(!p.pTails.isEmpty()){name=J.classNameStr(p)+name;}
    String code=header+"\npublic class "+name+"£E"+
      "{public static L42£Library execute(){return "  
      +j.result()+";}}";
    var files=L(new SourceFile(metaPackage+name+"£E",code));
    ClassLoader classes=InMemoryJavaCompiler.compile(classLoader,files,outNewBytecode);
    assert classes==classLoader;
    return runMainName(p,c,name);
    }
  public Core.L runMainName(Program p, C c,String name) throws InvocationTargetException{
    Resources.setLibsCached(p,c,libs);
    try{
      L42£Library res=(L42£Library)classLoader.loadClass(metaPackage+name+"£E")
        .getDeclaredMethod("execute")
        .invoke(null);
      return res.unwrap;
      }
    catch(ClassNotFoundException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException|SecurityException errs){
      throw new Error(errs);
      }
    }
  public void loadNow(Program p,List<SClassFile> newBytecode,List<L42£Library> newLibs) throws CompilationError{
    ArrayList<SourceFile> files=new ArrayList<>();
    this.notOkToJava.clear();
    int oldLibNum=libs.size();
    loadRec(p,files);
    if(files.isEmpty()){return;}
    assert files.stream().allMatch(f->{
      var u=f.getName();
      return notOkToJava.stream().allMatch(s->{
        assert !u.endsWith(s+".java");
        return true;
        });
      });
    ClassLoader classes=InMemoryJavaCompiler.compile(classLoader,files,newBytecode);
    newLibs.addAll(libs.subList(oldLibNum,libs.size()));
    assert classes==classLoader;    
    }
  HashSet<String> okToJava=new HashSet<>();//remove elements when the enter in loaded
  HashSet<String> notOkToJava=new HashSet<>();//to be flushed every new load bunch
  boolean isOkToJava(Program p,String name){
    return isOkToJava(p,name,new HashSet<>(),new HashSet<>());
    }
  boolean isOkToJava(Program p,String name,HashSet<String> mayBeOkToJava,HashSet<String>passed){
    if(loaded.containsKey(name)){return true;}//already in Java
    if(okToJava.contains(name)){return true;}//already verified to be ok
    if(notOkToJava.contains(name)){return false;}
    mayBeOkToJava.add(name);
    for(var path:p.topCore().info().typeDep()){
      var canNavigate=p.pop(path.n()).top._cs(path.cs());
      if(canNavigate==null){notOkToJava.add(name);return false;}
      var p0=p.navigate(path);//test above avoid costly exception here
      //if navigate impossible; it can happen if class refers to stuff defined later
      String name0=J.classNameStr(p0);
      if(mayBeOkToJava.contains(name0)){passed.add(name0);continue;}
      HashSet<String>passed0=new HashSet<>();
      var res=isOkToJava(p0,name0,mayBeOkToJava,passed0);
      passed.addAll(passed0);
      if(!res){notOkToJava.add(name);return false;}
      }
    mayBeOkToJava.remove(name);
    passed.remove(name);
    if(passed.isEmpty()){okToJava.add(name);}
    return true;
    //* okToJava(p):
    //    name(p) already loaded
    //* okToJava(p):
    //    forall P in p(This).typeDep
    //    okToJava(p.navigate(P))
    }
  void loadRec(Program p,ArrayList<SourceFile>files){
    var l=p.topCore();
    for(var nc:l.ncs()){loadRec(p.push(nc.key(), nc.l()),files);}
    if(!l.info().isTyped()){return;}
    String name=J.classNameStr(p);
    if(this.loaded.containsKey(name)){return;}
    if(!isOkToJava(p,name)){return;}
    J j=new J(p,G.empty(),false,libs,false);
    j.mkClass();
    String code=header+j.result().toString();
    var e=new Element(l,J.classNamePath(p),name,new SourceFile(metaPackage+name,code));
    files.add(e.source);
    loaded.put(name,e);
    okToJava.remove(name);
    }
  public static final String metaPackage="is.L42.metaGenerated.";
  public static final String header="""
    package is.L42.metaGenerated;
    import is.L42.platformSpecific.javaTranslation.L42Any;
    import is.L42.platformSpecific.javaTranslation.L42£Void;
    import is.L42.platformSpecific.javaTranslation.L42£Library;
    import is.L42.platformSpecific.javaTranslation.L42ClassAny;
    import is.L42.platformSpecific.javaTranslation.L42Fwd;
    import is.L42.platformSpecific.javaTranslation.L42Throwable;
    import is.L42.platformSpecific.javaTranslation.L42Error;
    import is.L42.platformSpecific.javaTranslation.L42Exception;
    import is.L42.platformSpecific.javaTranslation.L42Return;
    import is.L42.platformSpecific.javaTranslation.L42£TrustedIO;
    import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
    import is.L42.numbers.L42£BigRational;
    import is.L42.cache.L42Cache;
    import is.L42.cache.L42CacheMap;
    import is.L42.cache.L42Cachable;
    import is.L42.cache.L42StandardCache;
    import is.L42.cache.L42SingletonCache;
    import is.L42.platformSpecific.javaTranslation.L42NoFields;
    import is.L42.meta.L42£Meta;
    import is.L42.introspection.L42£Nested;
    import is.L42.introspection.L42£Type;
    import is.L42.introspection.L42£Doc;
    import is.L42.meta.L42£Name;
    import is.L42.platformSpecific.javaTranslation.Resources;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.function.BiConsumer;
    """;
  }
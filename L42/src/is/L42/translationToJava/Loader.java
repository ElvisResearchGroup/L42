package is.L42.translationToJava;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    int oldLibNum=libs.size();
    loadRec(p,files);
    if(files.isEmpty()){return;}
    ClassLoader classes=InMemoryJavaCompiler.compile(classLoader,files,newBytecode);
    newLibs.addAll(libs.subList(oldLibNum,libs.size()));
    assert classes==classLoader;    
    }
  void loadRec(Program p,ArrayList<SourceFile>files){
    var l=p.topCore();
    for(var nc:l.ncs()){loadRec(p.push(nc.key(), nc.l()),files);}
    if(!l.info().isTyped()){return;}
    String name=J.classNameStr(p);
    if(this.loaded.containsKey(name)){return;}
    J j=new J(p,G.empty(),false,libs,false);
    j.mkClass();
    String code=header+j.result().toString();
    var e=new Element(l,J.classNamePath(p),name,new SourceFile(metaPackage+name,code));
    files.add(e.source);
    loaded.put(name,e);
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
    import is.L42.platformSpecific.javaTranslation.Resources;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.function.BiConsumer;
    """;
  }
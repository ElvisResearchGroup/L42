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
import is.L42.typeSystem.Coherence;
import is.L42.visitors.WellFormedness;

import static is.L42.tools.General.L;
import static is.L42.tools.General.checkNoException;

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
  /*public String toString(){
    String res="";
    for(var e:loaded.values()){res+=e.cIds+"\n"+e.source+"\n\n";}
    return res;
    }*/
  private final ArrayList<L42£Library> libs=new ArrayList<>();
  public int libsCachedSize(){return libs.size();}//needed for double checking on caching
  public int bytecodeSize(){return classLoader.map().size();}//needed for double checking on caching
  final HashSet<String>loaded=new HashSet<>();
  public final MapClassLoader classLoader=new MapClassLoader(new HashMap<>(),ClassLoader.getSystemClassLoader());
  public void loadByteCodeFromCache(List<SClassFile> bytecode,List<L42£Library>newLibs){
    libs.clear();
    libs.addAll(newLibs);
    for(var e:bytecode){
      classLoader.map().computeIfAbsent(e.name,k->{
        assert !loaded.contains(e.name);
        loaded.add(e.name.substring("is.L42.metaGenerated.".length()));
        return e.toCF();
        });
      }
    }
  public boolean checkByteCodeFromCache(List<SClassFile> bytecode,List<L42£Library>newLibs){
    assert libs.equals(newLibs);
    for(var e:bytecode){
      var cf=classLoader.map().get(e.name);
      assert cf.equalBytes(e);
      }
    return true;
    }
  public Core.L runNow(Program p,C c,Core.E e,ArrayList<? super SClassFile> outNewBytecode,ArrayList<? super L42£Library> newLibs) throws CompilationError, InvocationTargetException{
    int oldLibNum=libs.size();
    J j=new J(p,G.empty(),libs,false){
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
    var res=runMainName(p,c,name);
    assert checkNoException(()->res.wf());
    assert checkNoException(()->WellFormedness.checkInfo(p.push(Resources.currentC,res),res));
    return res;
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
  public void loadNow(Program p,ArrayList<? super SClassFile> newBytecode,ArrayList<? super L42£Library> newLibs) throws CompilationError{
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
    var res=notOkToJava(p,name, new HashSet<>());
    if(!res){okToJava.add(name);}
    return !res;
    }
  boolean notOkToJava(Program p,String name,HashSet<String> mayBeOkToJava){
    if(loaded.contains(name)){return false;}//already in Java
    if(okToJava.contains(name)){return false;}//already verified to be ok
    if(notOkToJava.contains(name)){return true;}
    if(p.topCore().info()._uniqueId()!=-1){return true;}//still being metaprogrammed
    mayBeOkToJava.add(name);
    for(var path:p.topCore().info().typeDep()){
      var p0=p._navigate(path);
      if(p0==null){notOkToJava.add(name);return true;}
      //if navigate impossible; it can happen if class refers to stuff defined later
      String name0=J.classNameStr(p0);
      if(name0.isEmpty() || mayBeOkToJava.contains(name0)){continue;}
      var res=notOkToJava(p0,name0,mayBeOkToJava);
      if(res){return true;}
      }
    return false;
    }
    //* okToJava(p):
    //    name(p) already loaded
    //* okToJava(p):
    //    forall P in p(This).typeDep
    //      okToJava(p.navigate(P))
    //only fails if we reach a P so that p.navigate(P) is undefined
    //* notOkToJava(p)
    //    P in p(This).typeDep and p.navigate(P) undefined
    //* notOkToJava(p)
    //    P in p(This).typeDep and notOkToJava(p.navigate(P))
  void loadRec(Program p,ArrayList<SourceFile>files){
    var l=p.topCore();
    for(var nc:l.ncs()){loadRec(p.push(nc.key(), nc.l()),files);}
    if(!l.info().isTyped()){return;}
    String name=J.classNameStr(p);
    if(name.isEmpty()){return;}
    if(this.loaded.contains(name)){return;}
    if(!isOkToJava(p,name)){return;}
    J j=new J(p,G.empty(),libs,false);
    j.mkClass();
    String code=header+j.result().toString();
    //var e=new Element(l,J.classNamePath(p),name,new SourceFile(metaPackage+name,code));
    files.add(new SourceFile(metaPackage+name,code));//e.source
    loaded.add(name);
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
    import is.L42.platformSpecific.javaTranslation.L42£NonDeterministicError;
    import is.L42.numbers.L42£BigRational;
    import is.L42.cache.L42Cache;
    import is.L42.cache.L42CacheMap;
    import is.L42.cache.L42Cachable;
    import is.L42.cache.L42StandardCache;
    import is.L42.cache.L42SingletonCache;
    import is.L42.cache.concurrency.CachedRes;
    import is.L42.platformSpecific.javaTranslation.L42NoFields;
    import is.L42.meta.L42£Meta;
    import is.L42.introspection.L42£Nested;
    import is.L42.introspection.L42£Type;
    import is.L42.introspection.L42£Doc;
    import is.L42.introspection.L42£Method;
    import is.L42.meta.L42£Name;
    import is.L42.maps.L42£Set;
    import is.L42.maps.L42£ImmMap;
    import is.L42.maps.L42£MutMap;
    import is.L42.maps.L42£SelfVector;
    import is.L42.platformSpecific.javaTranslation.Resources;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.function.BiConsumer;
    """;
  }
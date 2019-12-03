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
import is.L42.platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.SourceFile;
import is.L42.platformSpecific.javaTranslation.L42Library;
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
  private final ArrayList<L42Library> libs=new ArrayList<>();
  final HashMap<String,Element> loaded=new HashMap<>();
  final MapClassLoader classLoader=new MapClassLoader(new HashMap<>(),ClassLoader.getSystemClassLoader());
  public Core.L runNow(Program p,C c,Core.E e) throws CompilationError, InvocationTargetException{
    loadNow(p);
    J j=new J(p,G.empty(),false,libs){
      @Override public boolean precomputeCoherent(){return false;}
      };
    j.visitE(e);//the goal here is not to generate the p.top class
    String name="£c"+c;
    if(!p.pTails.isEmpty()){name=J.classNameStr(p)+name;}
    String code=header+"\npublic class "+name+"£E"+
      "{public static L42Library execute(){return "  
      +j.result()+";}}";
    var files=L(new SourceFile(metaPackage+name+"£E",code));
    ClassLoader classes=InMemoryJavaCompiler.compile(classLoader,files);
    assert classes==classLoader;
    Resources.setLibsCached(p,c,libs);
    try{
      L42Library res=(L42Library)classLoader.loadClass(metaPackage+name+"£E")
        .getDeclaredMethod("execute")
        .invoke(null);
      return res.unwrap;
      }
    catch(ClassNotFoundException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException|SecurityException errs){
      throw new Error(errs);
      }
    }
  public void loadNow(Program p) throws CompilationError{
    ArrayList<SourceFile> files=new ArrayList<>();
    loadRec(p,files);
    if(files.isEmpty()){return;}
    ClassLoader classes=InMemoryJavaCompiler.compile(classLoader,files);
    assert classes==classLoader;    
    }
  void loadRec(Program p,ArrayList<SourceFile>files){
    var l=p.topCore();
    for(var nc:l.ncs()){loadRec(p.push(nc.key(), nc.l()),files);}
    if(!l.info().isTyped()){return;}
    String name=J.classNameStr(p);
    if(this.loaded.containsKey(name)){return;}
    J j=new J(p,G.empty(),false,libs);
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
    import is.L42.platformSpecific.javaTranslation.L42Void;
    import is.L42.platformSpecific.javaTranslation.L42Library;
    import is.L42.platformSpecific.javaTranslation.L42ClassAny;
    import is.L42.platformSpecific.javaTranslation.L42Fwd;
    import is.L42.platformSpecific.javaTranslation.L42Throwable;
    import is.L42.platformSpecific.javaTranslation.L42Error;
    import is.L42.platformSpecific.javaTranslation.L42Exception;
    import is.L42.platformSpecific.javaTranslation.L42Return;
    import is.L42.platformSpecific.javaTranslation.L42TrustedIO;
    import is.L42.platformSpecific.javaTranslation.L42LazyMsg;
    import is.L42.meta.Meta;
    import is.L42.platformSpecific.javaTranslation.Resources;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.function.BiConsumer;
    """;
  }
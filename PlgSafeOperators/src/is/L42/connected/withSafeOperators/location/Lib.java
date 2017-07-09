package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;
import programReduction.Program;
import auxiliaryGrammar.Functions;
public class Lib extends Location.LocationImpl<ExpCore.ClassB,Lib>{
  boolean isBinded;
  ExpCore.ClassB root;
  List<Ast.C> path;
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    List<Ast.C> path,
    ExpCore.ClassB inner,Lib location) {super(inner,location);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      }
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    List<Ast.C> path,
    ExpCore.ClassB inner) {super(inner,null);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      this.location=this;
      }
  public static Lib newFromClass(PData pData,Path path){
    return newFromClassP(pData.p,path);
    }
  public static Lib newFromClassP(Program p,Path path){
    if(path.isPrimitive()){return new LibPrimitive(path);}
    ClassB cb=p.extractClassB(path);//it seams does not need from??
    Lib lib=new Lib(true,cb,Collections.emptyList(),cb);
    return lib;
    } 
  public static Lib newFromLibrary(ExpCore.ClassB cb){
    Lib lib=new Lib(false,cb,Collections.emptyList(),cb);
    return lib;
    } 
  Cacher<List<Lib>> nestedsC=new Cacher<List<Lib>>(){public List<Lib> cache(){
    if(Lib.this.isBinded){
      return inner.ns().stream()
        .map(n->new Lib(true,(ExpCore.ClassB)n.getInner(),Collections.emptyList(),(ExpCore.ClassB)n.getInner()))
        .collect(Collectors.toList());
      }
    return inner.ns().stream()
      .map(n->new Lib(false,root,Functions.push(path,n.getName()),(ExpCore.ClassB)n.getInner(),Lib.this))
      .collect(Collectors.toList());
    }};
  public int nestedSize(){return nestedsC.get().size();}
  public Lib nested(int that) throws NotAvailable{return Location.listAccess(nestedsC.get(), that);}

  Cacher<List<Method>> methodsC=new Cacher<List<Method>>(){public List<Method> cache(){
  return inner.mwts().stream()
    .map(mwt->new Method(mwt,Lib.this))
    .collect(Collectors.toList());}};
  public int methodSize(){return methodsC.get().size();}
  public Method method(int that) throws NotAvailable{return Location.listAccess(methodsC.get(), that);}

  Cacher<List<Type.Implemented>> implementedsC=new Cacher<List<Type.Implemented>>(){public List<Type.Implemented> cache(){
  List<Type.Implemented> res=new ArrayList<>();
  {int i=-1; for(Ast.Type ti:inner.getSupertypes()){i+=1; //starts from 0
    res.add(new Type.Implemented(i,ti,inner,Lib.this));
  }}
  return res;
  }};
  public int implementedSize(){return implementedsC.get().size();}
  public Type.Implemented implemented(int that) throws NotAvailable{return Location.listAccess(implementedsC.get(), that);}

  @Override public Doc doc(){return new Doc(inner.getDoc1(),this);}
  
  public boolean isInterface(){return this.inner.isInterface();}
  public boolean isBinded(){return this.isBinded;}
  private static boolean isRedirectableRec(ClassB cb){
    boolean res=!cb.mwts().stream().anyMatch(mwt->
    mwt.get_inner().isPresent() 
    || mwt.getMs().isUnique()
    );
    if(!res){return false;}
    res=cb.ns().stream().allMatch(nc->isRedirectableRec((ClassB)nc.getInner()));
    return res;
    }
  public boolean isRedirectable(){
    return isRedirectableRec(this.inner);
    }//deep, all abs, no private
  public boolean isPotentialInterface(){
    //freeTemplate must mean no class methods!! old idea (class methods not called) does not seams to make sense, if not called, then could be just not having them...
    //technically you can still have class NormType variables and use the method over those, but it seams like a minor case
    return !this.inner.mwts().stream().anyMatch(mwt->
      mwt.get_inner().isPresent() 
      || mwt.getMs().isUnique()
      || mwt.getMt().getMdf()==Mdf.Class);
    }//shallow for being sum with interface or redirectable to interface
  public boolean isCloseState(){
    return this.inner.mwts().stream().anyMatch(mwt->
      !mwt.get_inner().isPresent() 
      && mwt.getMs().isUnique());
    }
  public boolean isEnsuredCoherent(PData pData){
    if (this.inner.getPhase()==Phase.Coherent){return true;}
    if(this.isBinded){return false;}
    return newTypeSystem.TsLibrary.coherent(pData.p.evilPush(this.inner), false);
    
  }
  //either is labeled coherent, (may be binded)
  //or not binded and TsLibrary.coherent(pData.p.evilPush(this.inner), false);
  //in a world with the current subtype knowledge: it may give a false negative for incomplete subtyping information
  
  public Lib root(){
    if(this.root==this.inner){return this;}
    assert !this.isBinded;
    return new Lib(false,root,Collections.emptyList(),root);
    }
  public String path(){
    return PathAux.as42Path(path);
    }  
  public Doc nestedDoc(){
    if(this.path.isEmpty()){
      return new Doc(Ast.Doc.empty(),this);
      }
    NestedClass nc = this.root.getNested(path);
    return new Doc(nc.getDoc(),this);
    }//empty doc if it is root
//even if obtained with a classObj, no method to get it back
//to get a nested classObj, Refactor.navigateClassObj(classAny,Path)->classAny??
  public String toS() {return sugarVisitors.ToFormattedText.of(inner);}
   public Lib navigate(String cs){
    return navigateCs(PathAux.parseValidCs(cs));
    }
  
   public Lib navigateCs(List<Ast.C> cs){
    if (cs.isEmpty()){return this;}
    if(this.isBinded){
      ClassB cb=this.inner.getClassB(cs);//TODO: need from?
      return new Lib(true,cb,Collections.emptyList(),cb);
      }
    List<Ast.C> top = Collections.singletonList(cs.get(0));
    List<Ast.C> tail=cs.subList(1,cs.size());
    Lib nextStep=new Lib(false,root,Functions.push(path,cs.get(0)),inner.getClassB(top),this);
    return nextStep.navigateCs(tail);
    }
  //@Override public boolean equalequal(Object that) {return this.equals(that);}
  }
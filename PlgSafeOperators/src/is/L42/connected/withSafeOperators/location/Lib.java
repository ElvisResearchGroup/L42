package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.NestedClass;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Lib extends Location.LocationImpl<ExpCore.ClassB,Lib>{
  boolean isBinded;
  ExpCore.ClassB root;
  Path path;
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    Path path,
    ExpCore.ClassB inner,Lib location) {super(inner,location);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      }
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    Path path,
    ExpCore.ClassB inner) {super(inner,null);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      this.location=this;
      }
  public static Lib newFromClass(PData pData,Path path){
    ClassB cb=pData.p.extractClassB(path);//TODO: need from??
    Lib lib=new Lib(true,cb,Path.outer(0),cb);
    return lib;
    } 
  public static Lib newFromLibrary(ExpCore.ClassB cb){
    Lib lib=new Lib(false,cb,Path.outer(0),cb);
    return lib;
    } 
  Cacher<List<Lib>> nestedsC=new Cacher<List<Lib>>(){public List<Lib> cache(){
    if(Lib.this.isBinded){
      return inner.ns().stream()
        .map(n->new Lib(true,(ExpCore.ClassB)n.getInner(),Path.outer(0),(ExpCore.ClassB)n.getInner()))
        .collect(Collectors.toList());
      }
    return inner.ns().stream()
      .map(n->new Lib(false,root,path.pushC(n.getName()),(ExpCore.ClassB)n.getInner(),Lib.this))
      .collect(Collectors.toList());
    }};
  public int nestedsSize(){return nestedsC.get().size();}
  public Lib nested(int that) throws NotAvailable{return Location.listAccess(nestedsC.get(), that);}

  Cacher<List<Method>> methodsC=new Cacher<List<Method>>(){public List<Method> cache(){
  return inner.mwts().stream()
    .map(mwt->new Method(mwt,Lib.this))
    .collect(Collectors.toList());}};
  public int methodsSize(){return methodsC.get().size();}
  public Method method(int that) throws NotAvailable{return Location.listAccess(methodsC.get(), that);}

  Cacher<List<Type.Implemented>> implementedsC=new Cacher<List<Type.Implemented>>(){public List<Type.Implemented> cache(){
  List<Type.Implemented> res=new ArrayList<>();
  {int i=-1; for(Ast.Type ti:inner.getSupertypes()){i+=1; //starts from 0
    res.add(new Type.Implemented(i,ti.getNT(),inner,Lib.this));
  }}
  return res;
  }};
  public int implementedsSize(){return implementedsC.get().size();}
  public Type.Implemented implemented(int that) throws NotAvailable{return Location.listAccess(implementedsC.get(), that);}

  @Override public Doc doc(){return new Doc(inner.getDoc1(),this);}
  public boolean isCoherent(PData pData){
    if (this.isBinded){return false;}
    return newTypeSystem.TsLibrary.coherent(pData.p.evilPush(this.inner), false);
    //TODO: no, we need to navigate on a path for classAnys?
    }
  public Lib root(){
    if(this.root==this.inner){return this;}
    assert !this.isBinded;
    return new Lib(false,root,Path.outer(0),root);
    }
  public Path path(){return path;}//last is its name, empty path for root
  public Doc nestedDoc(){
    if(this.path.getCBar().isEmpty()){
      return new Doc(Ast.Doc.empty(),this);
      }
    NestedClass nc = inner.getNested(path.getCBar());
    return new Doc(nc.getDoc(),this);
    }//empty doc if it is root
//even if obtained with a classObj, no method to get it back
//to get a nested classObj, Refactor.navigateClassObj(classAny,Path)->classAny??
  public String toS() {return sugarVisitors.ToFormattedText.of(inner);}
  public Lib navigate(List<Ast.C> cs){
    if (cs.isEmpty()){return this;}
    if(this.isBinded){
      ClassB cb=this.inner.getClassB(cs);//TODO: need from?
      return new Lib(true,cb,Path.outer(0),cb);
      }
    List<Ast.C> top = Collections.singletonList(cs.get(0));
    List<Ast.C> tail=cs.subList(1,cs.size());
    Lib nextStep=new Lib(false,root,path.pushC(cs.get(0)),inner.getClassB(top),this);
    return nextStep.navigate(tail);
    }
  @Override public boolean equalequal(Object that) {
    return this.equals(that);
    }
  }
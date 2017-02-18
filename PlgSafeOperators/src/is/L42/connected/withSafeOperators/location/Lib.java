package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB.NestedClass;
import ast.PathAux;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Lib extends Location.LocationImpl<ExpCore.ClassB,Lib>{
  ExpCore.ClassB root;
  Path path;
  public Lib(
    ExpCore.ClassB root,
    Path path,
    ExpCore.ClassB inner,Lib location) {super(inner,location);
      this.root=root;
      this.path=path;
      }
  //Cacher<List<Lib>> nestedsC=new Cacher<List<Lib>>(){public List<Lib> cache(){    }}; 
  Cacher<List<Lib>> nestedsC=new Cacher<List<Lib>>(){public List<Lib> cache(){
    return inner.ns().stream()
      .map(n->new Lib(root,path.pushC(n.getName()),(ExpCore.ClassB)n.getInner(),Lib.this))
      .collect(Collectors.toList());}};
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
  public String kindS(){
    ClassKind k = ExtractInfo.classKind(root,path.getCBar(),inner,null,null,null);
    return k.name42;
    }
  public Lib root(){return new Lib(root,Path.outer(0),root,null);}
  @Override//since we pass null for root
  public Lib location() {
    Lib l=super.location();
    if (l!=null){return l;}
    return this;
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
    List<Ast.C> top = Collections.singletonList(cs.get(0));
    List<Ast.C> tail=cs.subList(1,cs.size());
    Lib nextStep=new Lib(root,path.pushC(cs.get(0)),inner.getClassB(top),this);
    return nextStep.navigate(tail);
    }
  }
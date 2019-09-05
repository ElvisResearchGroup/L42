package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.Err;
import is.L42.common.Parse.Result;
import is.L42.generated.Core;
import is.L42.generated.L42AuxParser;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Core.EVoid;
import is.L42.generated.Core.L.Info;
import is.L42.generated.L42AuxParser.InfoContext;
import is.L42.generated.L42AuxParser.PathContext;

final class InfoSupplier implements Supplier<Core.L.Info> {
  private final Result<InfoContext> r;
  private final Pos pos;
  List<P> typeDep=null;
  List<P> coherentDep=null;
  List<P> friends=null;
  List<Core.PathSel> usedMethods=null;
  List<P> privateSupertypes=null;
  List<S> refined=null;
  Boolean declaresClassMethods=null;
  Core.L.Info result;
  AuxVisitor av;
  InjectionToCore inject;
  InfoSupplier(InjectionToCore inject, Result<InfoContext> r, Pos pos) {
    this.inject=inject;this.r = r; this.pos = pos; av = new AuxVisitor(pos);
    }

  P pf(PathContext pi){
    P p=inject._inject(av.visitPath(pi));
    assert p!=null;
    return p;
    }

  Core.PathSel psf(L42AuxParser.PathSelContext pi){
    Core.PathSel p=inject._inject(av.visitPathSel(pi));
    assert p!=null;
    return p;
    }

  S sf(L42AuxParser.SelectorContext si){
    return av.visitSelector(si);
    }

  <Z,A,T>void fillInfo(String name, Z z,Function<Z,List<A>>as, Supplier<List<T>> get, Consumer<List<T>> set,Function<A,T>f){
    if(z==null){return;}
    if(get.get()!=null){
      inject.errors.append(pos+ Err.repeatedInfo(name));
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    set.accept(L(as.apply(z),(c,ei)->c.add(f.apply(ei))));
    if(get.get().isEmpty()){
      inject.errors.append(pos+Err.emptyInfo(name));
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    }

  void boolFlag(String name, Object z,Supplier<Boolean> get, Consumer<Boolean> set){
    if(z==null){return;}    
    if(get.get()!=null){
      inject.errors.append(pos + Err.repeatedInfo(name));
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    set.accept(true);
    }

  <T>void nullToDef(Supplier<T> get, Consumer<T> set, T def){
    if(get.get()==null){set.accept(def);}
    }

  @Override public Core.L.Info get(){
    if (r.hasErr()){
      inject.errors.append(pos + Err.malformedInfo(r.errorsTokenizer+"\n"+r.errorsParser));
      return new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    boolean isTyped=r.res.infoNorm()==null;
    for(var b:r.res.infoBody()){
      fillInfo("typeDep",b.typeDep(),z->z.path(),()->typeDep,v->typeDep=v,this::pf);
      fillInfo("coherentDep",b.coherentDep(),z->z.path(),()->coherentDep,v->coherentDep=v,this::pf);
      fillInfo("friends",b.friends(),z->z.path(),()->friends,v->friends=v,this::pf);
      fillInfo("usedMethods",b.usedMethods(),z->z.pathSel(),()->usedMethods,v->usedMethods=v,this::psf);
      fillInfo("privateSupertypes",b.privateSupertypes(),z->z.path(),()->privateSupertypes,v->privateSupertypes=v,this::pf);
      fillInfo("refined",b.refined(),z->z.selector(),()->refined,v->refined=v,this::sf);
      boolFlag("declaresClassMethods",b.declaresClassMethods(),()->declaresClassMethods,v->declaresClassMethods=v);
      }
    if(result!=null){return result;}
    List<P> empty=L();
    List<Core.PathSel> emptyPs=L();
    List<S> emptyS=L();
    nullToDef(()->typeDep,v->typeDep=v,empty);
    nullToDef(()->coherentDep,v->coherentDep=v,empty);
    nullToDef(()->friends,v->friends=v,empty);
    nullToDef(()->usedMethods,v->usedMethods=v,emptyPs);
    nullToDef(()->privateSupertypes,v->privateSupertypes=v,empty);
    nullToDef(()->refined,v->refined=v,emptyS);
    nullToDef(()->declaresClassMethods,v->declaresClassMethods=v,false);
    return new Core.L.Info(isTyped, typeDep, coherentDep, friends, usedMethods, privateSupertypes, refined, declaresClassMethods);
    }
}
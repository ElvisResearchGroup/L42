package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
  List<P> privateSubtypes=null;
  List<S> refined=null;
  Boolean canBeClassAny=null;
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

  <A,T>void fillInfo(String name, List<A> as, Supplier<List<T>> get, Consumer<List<T>> set,Function<A,T>f){
    if(as==null){return;}
    if(get.get()!=null){
      String msg="Error: invalid syntax for Info:"
      +"\n repeated information: "+name;
      inject.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    set.accept(L(as,(c,ei)->c.add(f.apply(ei))));
    if(get.get().isEmpty()){
      String msg="Error: invalid syntax for Info:"
      +"\n empty information for: "+name;
      inject.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    }

  void boolFlag(String name, Supplier<Boolean> get, Consumer<Boolean> set){
    if(get.get()!=null){
      String msg="Error: invalid syntax for Info:"
      +"\n repeated information: "+name;
      inject.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      result= new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    set.accept(true);
    }

  <T>void nullToDef(Supplier<T> get, Consumer<T> set, T def){
    if(get.get()==null){set.accept(def);}
    }

  @Override public Core.L.Info get(){
    if (r.hasErr()){
      String msg="Error: invalid syntax for Info:"
        +"\n"+r.errorsTokenizer
        +"\n"+r.errorsParser;
      inject.errors.append("line " + pos.line() + ":" + pos.column() + " " + msg);
      return new Core.L.Info(false,L(),L(),L(),L(),L(),L(),false);
      }
    boolean isTyped=r.res.infoNorm()==null;
    for(var b:r.res.infoBody()){
      fillInfo("typeDep",b.typeDep().path(),()->typeDep,v->typeDep=v,this::pf);
      fillInfo("coherentDep",b.coherentDep().path(),()->coherentDep,v->coherentDep=v,this::pf);
      fillInfo("friends",b.friends().path(),()->friends,v->friends=v,this::pf);
      fillInfo("usedMethods",b.usedMethods().pathSel(),()->usedMethods,v->usedMethods=v,this::psf);
      fillInfo("privateSubtypes",b.privateSubtypes().path(),()->privateSubtypes,v->privateSubtypes=v,this::pf);
      fillInfo("refined",b.refined().selector(),()->refined,v->refined=v,this::sf);
      boolFlag("canBeClassAny",()->canBeClassAny,v->canBeClassAny=v);
      }
    if(result!=null){return result;}
    List<P> empty=L();
    List<Core.PathSel> emptyPs=L();
    List<S> emptyS=L();
    nullToDef(()->typeDep,v->typeDep=v,empty);
    nullToDef(()->coherentDep,v->coherentDep=v,empty);
    nullToDef(()->friends,v->friends=v,empty);
    nullToDef(()->usedMethods,v->usedMethods=v,emptyPs);
    nullToDef(()->privateSubtypes,v->privateSubtypes=v,empty);
    nullToDef(()->refined,v->refined=v,emptyS);
    nullToDef(()->canBeClassAny,v->canBeClassAny=v,false);
    return new Core.L.Info(isTyped, typeDep, coherentDep, friends, usedMethods, privateSubtypes, refined, canBeClassAny);
    }
}
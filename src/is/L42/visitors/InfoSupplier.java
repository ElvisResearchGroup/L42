package is.L42.visitors;

import static is.L42.tools.General.L;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import is.L42.common.ErrMsg;
import is.L42.common.Parse.Result;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.L42AuxParser;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.tools.General;
import is.L42.generated.Full;
import is.L42.generated.L42AuxParser.InfoContext;
import is.L42.generated.L42AuxParser.PathContext;

final class InfoSupplier implements Supplier<Core.Info> {
  private final Result<InfoContext> r;
  private final Pos pos;
  List<P.NCs> typeDep=null;
  List<P.NCs> coherentDep=null;
  List<P.NCs> metaCoherentDep=null;
  List<P.NCs> friends=null;
  List<Core.PathSel> usedMethods=null;
  List<P.NCs> privateSupertypes=null;
  List<S> refined=null;
  Boolean close=null;
  String nativeKind=null;
  List<P> nativePar=null;
  Integer _uniqueId=null;
  Core.Info result;
  AuxVisitor av;
  InjectionToCore inject;
  InfoSupplier(InjectionToCore inject, Result<InfoContext> r, Pos pos) {
    this.inject=inject;this.r = r; this.pos = pos; av = new AuxVisitor(pos);
    }
  P.NCs pf(PathContext pi){
    P p=inject._inject(av.visitPath(pi));
    assert p!=null;
    if(!p.isNCs()){
      inject.errors.append(ErrMsg.posString(inject.result.poss())+ErrMsg.invalidPathInInfo(p));
      }
    return p.toNCs();
    }
  int toUniqueId(String s){
    if(!s.startsWith("id")){return -1;}
    s=s.substring(2);
    try{return Integer.parseInt(s);}
    catch(NumberFormatException nfe){return -1;}
    }
  Core.PathSel psf(L42AuxParser.PathSelContext pi){
    Full.PathSel tmp=av.visitPathSel(pi);
    Core.PathSel p=inject._inject(tmp);
    if(p!=null && p._s()!=null && p._x()==null){return p;}
    inject.errors.append(L(pos)+ErrMsg.invalidPathInInfo(tmp));
    return new Core.PathSel(P.pThis0,tmp._s(),tmp._x());
    }
  S sf(L42AuxParser.SelectorContext si){
    return av.visitSelector(si);
    }
  <Z,A,T>void fillInfo(String name, Z z,Function<Z,List<A>>as, Supplier<List<T>> get, Consumer<List<T>> set,Function<A,T>f){
    if(z==null){return;}
    if(get.get()!=null){
      inject.errors.append(pos+ ErrMsg.repeatedInfo(name));
      result= Core.Info.empty;
      }
    set.accept(L(as.apply(z),(c,ei)->c.add(f.apply(ei))));
    if(get.get().isEmpty()){
      inject.errors.append(pos+ErrMsg.emptyInfo(name));
      result= Core.Info.empty;
      }
    }
  <Z,E>void fillElem(String name, Z z,Function<Z,E>s, Supplier<E> get, Consumer<E> set,Predicate<E>empty){
    if(z==null){return;}
    if(get.get()!=null){
      inject.errors.append(pos+ ErrMsg.repeatedInfo(name));
      result= Core.Info.empty;
      }
    set.accept(s.apply(z));
    if(empty.test(get.get())){
      inject.errors.append(pos+ErrMsg.emptyInfo(name));
      result= Core.Info.empty;
      }
    }
  void boolFlag(String name, Object z,Supplier<Boolean> get, Consumer<Boolean> set){
    if(z==null){return;}    
    if(get.get()!=null){
      inject.errors.append(pos + ErrMsg.repeatedInfo(name));
      result= Core.Info.empty;
      }
    set.accept(true);
    }

  <T>void nullToDef(Supplier<T> get, Consumer<T> set, T def){
    if(get.get()==null){set.accept(def);}
    }

  @Override public Core.Info get(){
    if (r.hasErr()){
      inject.errors.append(pos + ErrMsg.malformedInfo(r.errorsTokenizer+"\n"+r.errorsParser));
      return Core.Info.empty;
      }
    boolean isTyped=r.res.infoNorm()==null;
    for(var b:r.res.infoBody()){
      fillInfo("typeDep",b.typeDep(),z->z.path(),()->typeDep,v->typeDep=v,this::pf);
      fillInfo("coherentDep",b.coherentDep(),z->z.path(),()->coherentDep,v->coherentDep=v,this::pf);
      fillInfo("metaCoherentDep",b.metaCoherentDep(),z->z.path(),()->metaCoherentDep,v->metaCoherentDep=v,this::pf);
      fillInfo("friends",b.watched(),z->z.path(),()->friends,v->friends=v,this::pf);
      fillInfo("usedMethods",b.usedMethods(),z->z.pathSel(),()->usedMethods,v->usedMethods=v,this::psf);
      fillInfo("privateSupertypes",b.hiddenSupertypes(),z->z.path(),()->privateSupertypes,v->privateSupertypes=v,this::pf);
      fillInfo("refined",b.refined(),z->z.selector(),()->refined,v->refined=v,this::sf);
      boolFlag("closeState",b.close(),()->close,v->close=v);
      fillElem("nativeKind",b.nativeKind(),z->z.x()!=null?z.x().getText():z.c().getText(),()->nativeKind,v->nativeKind=v,s->s.isEmpty());
      fillInfo("nativePar",b.nativePar(),z->z.path(),()->nativePar,v->nativePar=v,this::pf);
      fillElem("uniqueId",b.uniqueId(),z->toUniqueId(z.x().getText()),()->_uniqueId,v->_uniqueId=v,i->i==-1);
      }
    if(result!=null){return result;}
    List<P.NCs> empty=L();
    List<Core.PathSel> emptyPs=L();
    List<S> emptyS=L();
    nullToDef(()->typeDep,v->typeDep=v,empty);
    nullToDef(()->coherentDep,v->coherentDep=v,empty);
    nullToDef(()->metaCoherentDep,v->metaCoherentDep=v,empty);
    nullToDef(()->friends,v->friends=v,empty);
    nullToDef(()->usedMethods,v->usedMethods=v,emptyPs);
    nullToDef(()->privateSupertypes,v->privateSupertypes=v,empty);
    nullToDef(()->refined,v->refined=v,emptyS);
    nullToDef(()->close,v->close=v,false);
    nullToDef(()->nativeKind,v->nativeKind=v,"");
    nullToDef(()->nativePar,v->nativePar=v,General.<P>L());
    nullToDef(()->_uniqueId,v->_uniqueId=v,-1);
    return new Core.Info(isTyped,typeDep,coherentDep,metaCoherentDep,friends,usedMethods,
      privateSupertypes,refined,close,
      nativeKind,nativePar,_uniqueId);
    }
  }
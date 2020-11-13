package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.E;
import is.L42.generated.Core.L;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.typeSystem.Coherence;
import is.L42.typeSystem.TypeManipulation;

    /*
     *For all methods m with modifier mdf and parameters T1 x1..Tn xn
     * discover all parameters xs,
     * where there is a mdf method Ti m#default#xi(), abstract or not
     * generate a method with parameters x1..xn\xs using such default values and delagating to the orginal m
     * give error if a method with that selector is present with different types
     * We also support methods using former values as in mdf method Ti m#default#xi(T x)
     * if x is a parameter present before xi (no fwd defaults)
     * Error if there is more then one possible default method to apply
     * TODO: in the future, consider a boolean option to give error if the method is present with the right types but implemented?
     * TODO: for now, the default methods must have no exceptions, we can add exception subtype
     */
//consistency: ##apply#squareBuilder but #default#x for the #apply??
//should it be #squareBuilder and #squareBuilder#otherMeth?
//what about shortCircuts prefixes?
public class Defaults{
  MetaError err;
  Core.L l;
  public Core.L of(Program p,List<C> cs,Function<L42£LazyMsg,L42Any>wrap){
    err=new MetaError(wrap);
    if(cs.isEmpty()){this.l=p.topCore();return of();}
    var pIn=p.navigate(cs);
    this.l=pIn.topCore();
    pIn=pIn.update(of(),false);
    var res= pIn._ofCore(P.of(cs.size(),L()));
    assert res.wf();
    return res;
    }
  Core.L of(){
    fillDefs();
    var newMWTs=L(l.mwts().stream().flatMap(this::of));
    return l.withMwts(new SumMethods(err).sum(l.mwts(), newMWTs));
    }
  Stream<MWT> of(MWT m){
    if(m.key().hasUniqueNum()){return Stream.empty();}
    var pos=m.poss().get(0);
    var xs=m.key().xs();
    var ts=m.mh().pars();
    var oldXs=new ArrayList<X>();
    var collected=new ArrayList<MWT>();    
    var nonDef=new ArrayList<X>();
    var nonDefT=new ArrayList<T>();
    var ds=new ArrayList<Core.D>();
    var es=new ArrayList<E>();
    for(var i:range(ts)){
      var xi=xs.get(i);
      var ti=ts.get(i);
      var defs=this.defs.get(xi);
      assert xi!=null;
      List<MWT> okDefs=L(defs.stream().filter(d->okDef(d,m,ti,xi,oldXs)));
      checkOneDefs(m.key(),xi,okDefs);
      oldXs.add(xi);
      es.add(new Core.EX(pos,xi));
      if(okDefs.isEmpty()){
        nonDef.add(xi);
        nonDefT.add(ti);
        }
      else{
        var d=okDefs.get(0);
        collected.add(d);
        List<E> esD=L(d.key().xs().stream().map(x->new Core.EX(pos,x)));
        E ei=Utils.thisCall(pos, d.key(), esD);
        ds.add(new Core.D(false, ti, xi, ei));
        }
      }
    if(collected.isEmpty()){return Stream.empty();}
    S s=m.key().withXs(nonDef);
    var mh=m.mh();
    mh=new MH(mh.mdf(),mh.docs(),mh.t(),s,L(nonDefT.stream()),mh.exceptions());
    E e=Utils.thisCall(pos,m.key(), L(es.stream()));
    e=new Core.Block(pos, L(ds.stream()), L(), e);
    return Stream.of(new MWT(m.poss(),L(),mh,"",e));
    }
  public boolean okDef(MWT d,MWT m, T t, X x,List<X> oldXs){
    String name=m.key().m();
    String defName="#default#"+name+"#"+x.inner();
    if(!d.mh().mdf().equals(m.mh().mdf())){return false;}
    if(name.equals("#apply")){defName="#default#"+x.inner();}
    if(!defName.equals(d.key().m())){return false;}
    for(var i :range(d.key().xs())){
      var xi=d.key().xs().get(i);
      var ti=d.mh().pars().get(i);
      var j=m.key().xs().indexOf(xi);
      assert j!=-1;
      var tj=m.mh().pars().get(j);
      if(ti.mdf().isCapsule()){err.throwErr(d, "Default method "+d.key()+" uses invalid modifier capsule for parameter "+xi);}
      if(!ti.mdf().equals(tj.mdf())|| !ti.p().equals(tj.p())){
        err.throwErr(d, "Default method "+d.key()+" uses invalid type for parameter "+xi+"; it should be "+tj);
        }
      }
    var retOk= d.mh().t().p().equals(t.p()) && d.mh().t().mdf().equals(t.mdf());
    if(!retOk){err.throwErr(d,"Default method "+d.key()+" uses invalid return type "+d.mh().t()+"; it should be "+t);}
    if(!d.mh().exceptions().isEmpty()){err.throwErr(d,"Default method "+d.key()+" should throws no exceptions, but it throws "+d.mh().exceptions());}
    //TODO: complete here to handle exceptions
    if(!oldXs.containsAll(d.key().xs())){
      oldXs.removeAll(d.key().xs());
      err.throwErr(d,"Default method "+d.key()+" refers to invalid parameters:"+oldXs);
      }
    return true;
    }
  HashMap<X,List<MWT>>defs=new HashMap<>();
  void fillDefs(){
    for(MWT mi:l.mwts()){
      for(X x:mi.key().xs()){
        defs.computeIfAbsent(x,this::defsX);
        }
      }    
    }
  List<MWT> defsX(X x){return L(l.mwts().stream().filter(mi->canDefault(mi.key(),x)));}
  boolean canDefault(S s,X x){
    return !s.hasUniqueNum()
      && s.m().startsWith("#default#")
      && s.m().endsWith("#"+x.inner());
    }

  void checkOneDefs(S s,X x,List<MWT>defs){
    if(defs.size()>1){
      var dups=defs.stream().map(d->err.intro(d,false)).collect(Collectors.joining(", "));
      err.throwErr(defs.get(0),"More then one applicable default initialization for parameter "+x+" of "+s+":\n"+dups);
      }
    }
  //  String s="ambiguous field type; other options are ";
    //  Supplier<String> ss=()->s+L(getters.get(x).stream().map(m->m.mh().t().p()));
    //  err.throwErr(getters.get(x).get(0),ss);
 
  }
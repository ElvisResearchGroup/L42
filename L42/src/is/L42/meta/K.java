package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L;
import is.L42.generated.Core.T;
import is.L42.generated.P;
import is.L42.generated.S;
import is.L42.generated.X;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.typeSystem.Coherence;

public class K {
  MetaError err;
  Map<X,List<MWT>> getters=new LinkedHashMap<>();
  boolean gettersNoMut=true;
  Map<X,List<MWT>> setters=new LinkedHashMap<>();
  public Core.L k(Program p,List<C> cs,Function<L42£LazyMsg,L42Any>wrap,String mutK,String immK){
    err=new MetaError(wrap);
    if(cs.isEmpty()){return k(p,wrap,mutK,immK);}
    var pIn=p.navigate(P.of(0, cs));
    var l=k(pIn,wrap,mutK,immK);
    pIn=pIn.update(l,false);
    return pIn._ofCore(P.of(cs.size(),L()));
    }
  public Core.L k(Program p,Function<L42£LazyMsg,L42Any>wrap,String mutK,String immK){
    var l=p.topCore();
    try{S.parse(mutK+"()");S.parse(immK+"()");}
    catch(EndError ee){err.throwErr(l,"invalid provided constructor names: "+mutK+", "+immK);}
    if(mutK.equals(immK)){err.throwErr(l,"invalid provided constructor names: "+mutK+", "+immK);}
    if(l.info().close()){err.throwErr(l,"Class is already close");}
    List<MWT> abs=L(l.mwts(),(c,m)->{if(m._e()==null){c.add(m);}});
    for(var m:abs){addGettersSetters(m);}
    boolean veryImm=gettersNoMut && setters.isEmpty();
    List<X> xs=L(getters.keySet().stream());//deterministic: it is a LinkedHashMap
    List<T> mutTs=L(xs,(c,m)->c.add(forgeT(m)));
    List<T> immTs=L(mutTs,t->t.mdf().isIn(Mdf.Class,Mdf.Immutable)?t:t.withMdf(Mdf.Immutable));
    S mutS=new S(mutK,xs,-1);
    S immS=new S(immK,xs,-1);
    var immMh=new Core.MH(Mdf.Class,L(),P.coreThis0,immS,immTs,L());
    var mutMh=immMh.withS(mutS);
    if(!veryImm){mutMh=mutMh.withT(P.coreThis0.withMdf(Mdf.Mutable)).withPars(mutTs);}
    MWT immM=new MWT(l.poss(),L(),immMh,"",null);
    MWT mutM=new MWT(l.poss(),L(),mutMh,"",null);
    List<MWT> newMWT=L(c->{
      c.addAll(l.mwts());
      if(_elem(l.mwts(),immS)!=null){throw todo();}
      if(_elem(l.mwts(),mutS)!=null){throw todo();}
      c.add(immM);
      c.add(mutM);
      });
    return l.withMwts(newMWT);
    }
    public T forgeT(X x){
      List<P> options=L(getters.get(x).stream().map(m->m.mh().t().p()).distinct());
      if(options.size()!=1){
        String s="ambiguous field type; other options are ";
        Supplier<String> ss=()->s+L(getters.get(x).stream().map(m->m.mh().t().p()));
        err.throwErr(getters.get(x).get(0),ss);
        }
      P p=options.get(0);
      List<Mdf> optionsGet=L(getters.get(x).stream().map(m->m.mh().t().mdf()).distinct());
      List<Mdf> optionsSet=L(setters.getOrDefault(x,L()).stream().map(m->m.mh().t().mdf()).distinct());
      var clazz=match(Mdf.Class,L(),optionsGet) && match(null,L(Mdf.Class),optionsSet);
      if(clazz){return new T(Mdf.Class,L(),p);}
      var imm=match(null,List.of(Mdf.Immutable,Mdf.Readable),optionsGet) && match(null,L(Mdf.Immutable),optionsSet);
      if(imm){return new T(Mdf.Immutable,L(),p);}
      var mut=match(Mdf.Mutable,List.of(Mdf.Readable,Mdf.Immutable,Mdf.Lent),optionsGet) &&  match(null,List.of(Mdf.Mutable,Mdf.Capsule),optionsSet);
      if(mut){return new T(Mdf.Mutable,L(),p);}
      var caps=match(null,List.of(Mdf.Readable,Mdf.Immutable),optionsGet) &&  match(null,L(Mdf.Capsule),optionsSet);
      if(caps){return new T(Mdf.Capsule,L(),p);}
      throw err.throwErr(getters.get(x).get(0),"ambiguous field modifier; can not be neither class, mut, imm or capsule");
      }
    public boolean match(Mdf must,List<Mdf> may,List<Mdf> current){
      boolean mustFound=must==null;
      for(Mdf mdf:current){
        if(mdf==must){mustFound=true;continue;}
        if(may.contains(mdf)){continue;}
        return false;
        }
      return mustFound;
      }
    /*
     generate two constructors, a mut and an imm named mutK and immK (parameters)
     look to all the abs methods
     read/mut/lent/imm method T #*x() induces a field x 
     getters=map X->MHs
     mdfList list of all T.Mdf in getters
     setters=map X-> MHs of form mut/lent method Void #*x(T that)
     if there is already a class method mdf This named mutK or immK with parameters as
       dom(getters), then do the sum
     if setters=empty and mdfList ={imm,read,class}, generate two imm constructors
     
     xs: dom(getters)
     Ps: getters and setters must agree on the P, otherwise error.
     Mdfs: for each x collect all getters/setters mdf:
       if {class} {class?} then class
       if {imm?,read?} {imm?} then imm
       if {mut,read?,imm?,lent?} {mut?,capsule?} then mut
       if {read?,imm?} {capsule?} then capsule
       error if a mdf can not be found
     Mdfs: all imm/class for the imm constructor      
     
     */
    void addGettersSetters(MWT m){      
      if(m.key().xs().size()>1){return;}
      X x=Coherence.fieldName(m.mh());
      if(m.key().xs().size()==1){
        if(!m.key().xs().get(0).equals(X.thatX)){return;}
        if(!m.mh().t().equals(P.coreVoid)){return;}
        if(!m.mh().mdf().isIn(Mdf.Mutable, Mdf.Lent)){return;}
        var list=setters.getOrDefault(x,new ArrayList<>());
        list.add(m);
        setters.putIfAbsent(x, list);
        }
      if(!m.mh().mdf().isIn(Mdf.Mutable, Mdf.Lent,Mdf.Immutable,Mdf.Readable)){return;}
      var list=getters.getOrDefault(x,new ArrayList<>());
      list.add(m);
      if(!m.mh().t().mdf().isIn(Mdf.Immutable,Mdf.Readable,Mdf.Class)){gettersNoMut=false;}
      getters.putIfAbsent(x, list);
      }
}
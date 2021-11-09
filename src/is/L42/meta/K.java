package is.L42.meta;

import static is.L42.tools.General.L;
import static is.L42.tools.General.pushL;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.typeSystem.TypeManipulation;

    /*
     generate two constructors, a mut and an imm named mutK and immK (parameters)
     look to all the abs methods
     read/mut/lent/imm method T #*x() induces a field x 
     getters=map X->MHs
     mdfList list of all T.Mdf in getters
     setters=map X-> MHs of form mut/lent method Void #*x(T that)
     if there is already a class method mdf This named mutK or immK with parameters as
       dom(getters), then skip the generation of such new abstract method;
       search for all the possible orders of fields xs to avoid inserting another with the same parameters in different order
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
       -mut fields are always fwd mut in the constructor //no need to ever remove fwd since 'now' can only read imms and capsules, no muts
       -imm fields are fwd iff not used by any readCache and there is no eagerCache 
     */
public class K extends GuessFields{
  public CoreL k(Program p,List<C> cs,boolean autoNorm,
      Function<L42£LazyMsg,L42Any>wrap,String mutK,String immK){
    this.autoNormed|=autoNorm;
    err=new MetaError(wrap);
    if(cs.isEmpty()){return k(p,wrap,mutK,immK);}
    var pIn=p.navigate(cs);
    var l=k(pIn,wrap,mutK,immK);
    pIn=pIn.update(l,false);
    var res= pIn._ofCore(P.of(cs.size(),L()));
    assert res.wf();
    return res;
    }
  String mutK;
  String immK;
  MWT mutM;
  MWT immM;
  CoreL l;
  List<X> _pars=null;//optional factory parameters/order provided by user
  MH _existingImmM=null;
  MH _existingMutM=null;
  public CoreL k(Program p,Function<L42£LazyMsg,L42Any>wrap,String baseMutK,String baseImmK){
    l=p.topCore();
    if(l.info().close()){err.throwErr(l,"Class is already close");}
    immK=normalizeName(baseImmK);
    mutK=normalizeName(baseMutK);
    fillPars();
    checkValidNames();
    addGettersSetters(p,_pars);
    List<X> xs=L(getters.keySet().stream());//deterministic: it is a LinkedHashMap
    if(_pars!=null){xs=requiredPars(l,xs,_pars);}
    makeMutMImmM(xs);
    findExistingKs(xs);
    if(_existingMutM!=null && _existingImmM==null){ reorderImmPars(xs); }//reorder fields?      
    List<MWT> newMWT=L(c->{
      c.addAll(l.mwts());
      if(_existingImmM==null){ c.add(immM); }
      if(_existingMutM==null){ c.add(mutM); }
      });
    var i=l.info();
    if(i.typeDep().contains(P.pThis0)){return l.withMwts(newMWT);}
    i=i.withTypeDep(pushL(i.typeDep(),P.pThis0));
    return l.withMwts(newMWT).withInfo(i);
    }
  private void checkValidNames(){
    try{L42£Name.parse(mutK+"()");L42£Name.parse(immK+"()");}
    catch(NumberFormatException ee){ throw invalidConstructorNames(mutK, immK, l); }
    if(mutK.equals(immK)){ throw invalidConstructorNames(mutK, immK, l); }
    }
  private void reorderImmPars(List<X>xs) {
    assert immM.key().xs().equals(xs);
    var newXs=_existingMutM.key().xs();
    if(newXs.equals(xs)){return;}
    var ts=immM.mh().pars();
    List<T> newTs=L(newXs,(c,xi)->{
      int i=xs.indexOf(xi);
      c.add(ts.get(i));
      });
    var mh=immM.mh();
    mh=mh.withS(immM.key().withXs(newXs));
    mh=mh.withPars(newTs);
    immM=immM.withMh(mh);
    }
  private void fillPars() {
    if(!mutK.contains("(")){ return; }
    L42£Name s;try{s=L42£Name.parse(mutK);}
    catch(NumberFormatException ee){ throw invalidConstructorNames(mutK, immK, l); }
    if(!s.x().isEmpty()){ throw invalidConstructorNames(mutK, immK, l); }
    S ss=S.parse(s.selector());
    mutK=ss.m();
    _pars=ss.xs();
    }
  private String normalizeName(String mutK) {
    if(mutK.isEmpty()){ return "#apply"; }
    if(mutK.startsWith("(")){ return "#apply"+mutK;}
    //TODO: can it happens that mutK starts with This( or Cs(.. ?
    return mutK;
    }  
  void makeMutMImmM(List<X> xs) {
    boolean veryImm=gettersAllImm && setters.isEmpty();
    List<T> mutTs=L(xs,(c,x)->c.add(forgeT(x)));
    List<T> immTs=L(mutTs,this::forgeTImm);
    S mutS=new S(mutK,xs,-1);
    S immS=new S(immK,xs,-1);
    var immTsNoFwd=L(immTs.stream().map(t->t.withMdf(TypeManipulation.noFwd(t.mdf()))));
    var immMh=new Core.MH(Mdf.Class,L(),P.coreThis0,immS,immTsNoFwd,L());
    var mutMh=new Core.MH(Mdf.Class,L(),P.coreThis0,mutS,immTs,L());    
    if(!veryImm){mutMh=mutMh.withT(P.coreThis0.withMdf(Mdf.Mutable)).withPars(mutTs);}
    immM=new MWT(l.poss(),L(),immMh,"",null);
    mutM=new MWT(l.poss(),L(),mutMh,"",null);
    }
  private void findExistingKs(List<X>xs){
    for(var m:l.mwts()){
      var mh=m.mh();
      if(!mh.mdf().isClass()){ continue; }
      var xsi=mh.key().xs();
      if(xsi.size()!=xs.size()){ continue; }
      if(!GuessFields.fieldsEqAsSet(xsi, xs)){ continue; }
      String mi=mh.key().m();
      if(_existingImmM==null && mi.equals(immK)){ _existingImmM=mh; }
      if(_existingMutM==null && mi.equals(mutK)){ _existingMutM=mh; }
      }
    }
  private List<X> requiredPars(CoreL l,List<X> computed, List<X> required){
    assert required.containsAll(computed);//can not be guessed if it is not required
    if(required.size()==computed.size()){ return required; }
    var missing=required.stream().filter(x->!computed.contains(x)).toList();
    throw err.throwErr(l,"unrecognized provided constructor fields: "+missing); 
  }
  private RuntimeException invalidConstructorNames(String mutK, String immK, CoreL l) {
    return err.throwErr(l,"invalid provided constructor names: "+mutK+", "+immK); 
  }
  public T forgeTImm(T t){
    if(t.mdf().isCapsule()){return t.withMdf(Mdf.Immutable);} 
    if(t.mdf().isFwdMut()){return t.withMdf(Mdf.ImmutableFwd);}
    return t;
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
    List<Mdf> optionsSet=L(setters.getOrDefault(x,L()).stream().map(m->m.mh().pars().get(0).mdf()).distinct());
    var clazz=match(Mdf.Class,L(),optionsGet) && match(null,L(Mdf.Class),optionsSet);
    if(clazz){return new T(Mdf.Class,L(),p);}
    var imm=match(null,List.of(Mdf.Immutable,Mdf.Readable),optionsGet) && match(null,L(Mdf.Immutable),optionsSet);
    var caps=match(null,List.of(Mdf.Readable),optionsGet) &&  match(null,L(Mdf.Capsule),optionsSet);
    if(imm && (autoNormed ||!caps)){ //fieldsUsedInReadCache can also be capsule
      var mustImm=autoNormed || fieldsUsedInReadCache.contains(x);
      if(mustImm){return new T(Mdf.Immutable,L(),p);}
      return new T(Mdf.ImmutableFwd,L(),p);
      }
    var mut=match(Mdf.Mutable,List.of(Mdf.Readable,Mdf.Immutable,Mdf.Lent),optionsGet) &&  match(null,List.of(Mdf.Mutable,Mdf.Capsule),optionsSet);
    var lentCaps=match(null,List.of(Mdf.Readable,Mdf.Lent),optionsGet) &&  match(null,L(Mdf.Capsule),optionsSet);
    if(mut && !autoNormed){return new T(Mdf.MutableFwd,L(),p);}
    if(mut && autoNormed){return new T(Mdf.Mutable,L(),p);}
    if(caps || lentCaps){return new T(Mdf.Capsule,L(),p);}
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
  }
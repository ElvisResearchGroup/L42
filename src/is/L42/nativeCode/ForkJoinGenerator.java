package is.L42.nativeCode;

import static is.L42.generated.LDom._elem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import is.L42.common.EndError;
import is.L42.common.ErrMsg;
import is.L42.common.G;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.X;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.MWT;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.tools.General;
import is.L42.translationToJava.J;
import is.L42.visitors.Accumulate;
import is.L42.visitors.FV;

public class ForkJoinGenerator implements Generator{
  protected void err(List<Pos>pos,String msg){
    throw new EndError.TypeError(pos,msg);
    }
  void shapeErr(){
    err(pos,ErrMsg.nativeBodyShapeInvalid(mh,"(Ds e), with more then one declaration in Ds"));
    }
  void shapeErrMdf(String msg){
    err(pos,ErrMsg.nativeBodyShapeInvalid(mh,"parallelizable:\n"+msg));
    }
  void exceptionErr(){
    err(pos,ErrMsg.nativeBodyInvalidExceptions(mwt));
    }
  void fvErr(List<X> xs){
    err(pos,ErrMsg.nativeBodyInvalidDsFV(mwt,xs));
    }
  void errCapsMutDisj(List<S> ssi,List<S> ssk){
    List<S> ss=new ArrayList<S>(ssi);
    ss.retainAll(ssk);
    err(pos,ErrMsg.nativeBodyDisjointCapsuleFields(mwt,ss));
    }

  MWT mwt;
  MH mh;
  List<Pos>pos;
  String url;
  @Override public void check(boolean allowAbs, MWT mwt, J j){//allowAbs correctly unused
    this.mwt=mwt;
    this.mh=mwt.mh();
    this.pos=mwt._e().poss();
    this.url=mwt.nativeUrl();
    CoreL l=j.p().topCore();
    if(!mh.exceptions().isEmpty()){exceptionErr();return;}
    if(!(mwt._e() instanceof Core.Block block)){shapeErr();return;}
    if(!block.ks().isEmpty()){shapeErr(); return;}
    if(block.ds().size()<=1){shapeErr(); return;}
    var ds=block.ds();
    G g=G.of(mwt.mh());
    List<List<S>> allCapsuleMutators=new ArrayList<>();
    List<List<String>> okMuts=new ArrayList<>();
    List<List<String>> oks=new ArrayList<>();
    for(var d:ds){
      var res0=okEMut(g,d.e());
      okMuts.add(res0);
      if(res0.isEmpty()){ oks.add(res0); }
      else{ oks.add(okE(g,d.e(),l)); }
      var fv=FV.of(d.e().visitable());
      var in=ds.stream().map(di->di.x()).filter(xi->fv.contains(xi)).toList();
      if(!in.isEmpty()){fvErr(in);}
      allCapsuleMutators.add(allCapsuleMutators(l,d.e()));
      }
    boolean simple=okEMut_okE(mh.mdf(),okMuts,oks);
    assert allCapsuleMutators.size()==ds.size();
    if(simple) { return; }
    IntStream.range(0,ds.size()).forEach(i->
      IntStream.range(i+1,ds.size()).forEach(k->{
        var ssi=allCapsuleMutators.get(i);
        var ssk=allCapsuleMutators.get(k);
        var ok=Collections.disjoint(ssi,ssk);
        if(!ok){ errCapsMutDisj(ssi,ssk); return; }
        })
      );    
    }
  boolean okEMut_okE(Mdf mdf,List<List<String>>okMuts,List<List<String>>oks){
    var errOkMuts=okMuts.stream().filter(l->!l.isEmpty()).toList();
    if(errOkMuts.size()<=1) { return true; }
    var errOks=oks.stream().filter(l->!l.isEmpty()).toList();
    assert errOkMuts.size()>=errOks.size();
    if(errOks.isEmpty()){ return false; }
    String msg="None of the two allowed patterns is satisfied:";
    msg+="\n* More then one parallel computations uses mutable features:\n";
    msg+=errOkMuts.stream().flatMap(ss->ss.stream()).collect(Collectors.joining("\n"));
    var isRead=mdf.isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class,Mdf.Readable);
    msg+=isRead?"\n* Read only parallel computations invalid:\n"
      :"\n* Capsule mutators parallel computation invalid:\n";
    msg+=errOks.stream().flatMap(ss->ss.stream()).collect(Collectors.joining("\n"));
    shapeErrMdf(msg); 
    throw new Error("Unreachable");
    }
  List<S> allCapsuleMutators(CoreL l,Core.E e){
    return new Accumulate.SkipL<List<S>>() {
      @Override public List<S> empty(){return new ArrayList<>();}
      @Override public void visitMCall(Core.MCall mCall){
        super.visitMCall(mCall);
        var xp=mCall.xP();
        var isThis=xp instanceof Core.EX ex && ex.x().equals(X.thisX);
        if(!isThis){return;}
        var s=_clearOn(l,mCall.s());
        if(s!=null){ this.acc().add(s); }
        }
      }.of(e.visitable());
    }
  protected S _clearOn(CoreL l,S s){
    MWT mwt=_elem(l.mwts(),s);
    if(mwt==null){ return null; }//will fail type checking
    if(!mwt.nativeUrl().equals("trusted:invalidateCache")){ return null; }
    //if(s.xs().isEmpty()){ return null; }//OK FOR DATA.CLOSE
    //return s.xs().get(0);
    return new Accumulate.SkipL<S>() {
      @Override public void visitMCall(Core.MCall mCall){
        super.visitMCall(mCall);
        var xp=mCall.xP();
        var isThis=xp instanceof Core.EX ex && ex.x().equals(X.thisX);
        if(!isThis){return;}
        this.result=mCall.s();
        }
      }.of(mwt._e().visitable());    
    }
  
  List<String> okEMut(G g, Core.E e){
    return new Accumulate.SkipL<List<String>>() {
      @Override public List<String> empty(){return new ArrayList<>();}
      @Override public void visitEX(Core.EX ex){
        var gex=g._of(ex);
        var gexOk=gex==null || gex.mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class);
        if(gexOk){ return; }
        acc().add("Local binding "+ex+" has modifier "+gex.mdf()+".\n"
          +"Only imm, capsule and class are safelly parallelizable.");
        }
      }.of(e.visitable());
    }  
  List<String> okE(G g, Core.E e,CoreL l){
    return new Accumulate.SkipL<List<String>>() {
      @Override public List<String> empty(){return new ArrayList<>();}
      @Override public void visitEX(Core.EX ex){
        var gex=g._of(ex);
        var gexOk=gex==null || gex.mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class);
        if(gexOk){ return; }
        var thisOk=g.of(X.thisX).mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class,Mdf.Readable);
        if(gex.mdf().isRead() && thisOk){ return; }
        acc().add("Local binding "+ex+" has modifier "+gex.mdf()+".\n"
          +"Only imm, capsule, class and read are safelly parallelizable; and the read ones not in mut/lent methods.");
        }
      @Override public void visitMCall(Core.MCall mCall){
        var xp=mCall.xP();
        var isThis=xp instanceof Core.EX ex && ex.x().equals(X.thisX);
        if(!isThis){ super.visitMCall(mCall); return; }
        visitEs(mCall.es());//ok no super
        var thisOk=g.of(X.thisX).mdf().isIn(Mdf.Immutable,Mdf.Capsule,Mdf.Class,Mdf.Readable);
        if(thisOk){ return; }
        var mutThisOk=g.of(X.thisX).mdf().isIn(Mdf.Mutable,Mdf.Lent);
        var res=_clearOn(l,mCall.s());
        if(mutThisOk & res!=null){ return; }
        acc().add("method call "+mCall.s()+" in position "+mCall.pos()+
          "it is not allowed in a parallel environment;\n"
          + " it not a capsule mutator and 'this' is not imm, capsule, class or read.");
        }
      }.of(e.visitable());
    }
  @Override public void generate(MWT mwt, J j) {
    if(!(mwt._e() instanceof Core.Block block)){throw General.bug();}
    assert block.ks().isEmpty();
    assert block.ds().size()>1;
    var ds=block.ds();
    var e=block.e();
    j.generateForkJoin(ds,e);
    }
  }
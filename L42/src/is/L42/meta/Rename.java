package is.L42.meta;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.mergeU;
import static is.L42.tools.General.popL;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.range;
import static is.L42.tools.General.toOneOr;
import static is.L42.tools.General.toOneOrBug;
import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core;
import is.L42.generated.Core.L.Info;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.T;
import is.L42.generated.LDom;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.PathSel;
import is.L42.generated.Pos;
import is.L42.generated.S;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42£LazyMsg;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;
import is.L42.top.Top;
import is.L42.visitors.Accumulate;
import is.L42.visitors.CloneVisitor;

public class Rename {
  HashMap<List<C>,Core.L> addMapLs=new HashMap<>();
  HashMap<List<C>,Core.L.MWT> addMapMWTs=new HashMap<>();
  LinkedHashMap<Arrow,Arrow> map;
  HashSet<Integer> existingNs=new HashSet<>();
  int allBusyUpTo=0;//TODO: both need to be recover from environment
  Program p;//includes the top level L
  List<C> cs;
  static final Program emptyP=Program.flat(Program.emptyL);
  MetaError errName;
  MetaError errFail;
  MetaError errC;
  MetaError errM;
  LinkedHashSet<List<C>> allWatched;
  LinkedHashSet<List<C>> allHiddenSupertypes;
  C cOut;

  public Core.L apply(Program pOut,C cOut,Core.L l,LinkedHashMap<Arrow,Arrow>map,Function<L42£LazyMsg,L42Any>wrapName,Function<L42£LazyMsg,L42Any>wrapFail,Function<L42£LazyMsg,L42Any>wrapC,Function<L42£LazyMsg,L42Any>wrapM){
    this.p=pOut.push(cOut,l);
    this.cOut=cOut;
    this.map=map;
    this.errName=new MetaError(wrapName);
    this.errFail=new MetaError(wrapFail);
    this.errC=new MetaError(wrapC);
    this.errM=new MetaError(wrapM);
    //allHiddenSupertypes=allHiddenSupertypes(l1);
    allWatched=allWatched(l);
    return applyMap();
    }
  L applyMap(){
    earlyCheck();
    replaceEmpty();
    L l1=renameL(p.topCore());
    L l2=lOfAddMap();
    return new Sum().compose(p.pop(),cOut,l1,l2,errC,errM);
    }
  void replaceEmpty(){
    //TODO; we really need to compute the used ns globally and to normalize them over library reuse and end of topNC
    for(Arrow a:map.values()){
      if(!a.full || !a.isEmpty()){continue;}
      if(a._s!=null){
        int n=firstPrivateOf(p._ofCore(a.cs));
        a._cs=a.cs;
        a._sOut=a._s.withUniqueNum(n);
        continue;
        }
      var popped=a.cs.subList(0,a.cs.size()-1);
      int n=firstPrivateOf(p._ofCore(popped));
      C top=a.cs.get(a.cs.size()-1);
      a._cs=pushL(popped,top.withUniqueNum(n));
      }
    }
  int firstPrivateOf(L l){
    int count=0;
    int res=Integer.MAX_VALUE;
    for(var nci:l.ncs()){
      if(nci.key().hasUniqueNum()){
        count+=1;
        res=Math.min(nci.key().uniqueNum(),res);
        }
      }
    for(var mwti:l.mwts()){
      if(mwti.key().hasUniqueNum() &&mwti.key().uniqueNum()!=0){
        count+=1;
        res=Math.min(mwti.key().uniqueNum(),res);
        }
      }
    if(count!=0){return res;}
    allBusyUpTo+=1;
    while(existingNs.contains(allBusyUpTo)){allBusyUpTo+=1;}
    existingNs.add(allBusyUpTo);
    return allBusyUpTo;
    }
  LinkedHashSet<List<C>> allWatched(L l){//uses the map
    LinkedHashSet<List<C>> res=new LinkedHashSet<>();
      l.visitInnerLNoPrivate((li,csi)->{
      var arrow=new Arrow(csi,null,false,null,null,null);
      var a=map.get(arrow);
      if(a!=null && (a.isEmpty() || a.isP())){return;}
      for(var w:li.info().watched()){Sum.addPublicCsOfP(w,csi,res);}
      });
    return res;
    }
  List<C> watchedBy(L l,List<C> cs){//uses the map
      Object[] res={null};
      l.visitInnerLNoPrivate((li,csi)->{
      var arrow=new Arrow(csi,null,false,null,null,null);
      var a=map.get(arrow);
      if(a!=null && (a.isEmpty() || a.isP())){return;}
      for(var w:li.info().watched()){
        if(cs.equals(Sum._publicCsOfP(w, csi))){res[0]=csi;}        
        }
      });
    @SuppressWarnings("unchecked") var list = (List<C>)res[0];
    return list;
    }
  String mapToS(){
    return map.values().stream().map(e->e.toStringErr()).collect(Collectors.joining(";"));
    }
  Error err(MetaError err,String s){throw err(err, ()->s);}
  Error err(MetaError err,Supplier<String> ss){
    throw err.throwErr(p.topCore(),()->{
      String s=ss.get();
      if(!s.endsWith("\n")){s=s+"\n";}
      s+="Full mapping:"+mapToS();
      return s;
      });
    }
  void earlyCheck(){
    HashSet<List<C>> domCodom=new HashSet<>();
    HashSet<List<C>> codCs=new HashSet<>();
    HashSet<Arrow> codMeth=new HashSet<>();
    for(var a:map.values()){
      if(a._s==null){domCodom.add(a.cs);}
      if(a.isMeth()){
        var k=new Arrow(a._cs,a._sOut,false,null,null,null);
        if(codMeth.contains(k)){err(errFail,"Rename can not map two methods on the same method: "+errFail.intro(k.cs,k._s));}
        codMeth.add(k);
        }
      if(a.isCs()){
        domCodom.add(a._cs);
        if(codCs.contains(a._cs)){
          err(errFail,"Two different nested class are renamed into "+errFail.intro(a._cs,false));
          }
        codCs.add(a._cs);
        }
      earlyCheck(a);
      }
    assert !domCodom.contains(null);
    for(var a:map.values()){
      if(a.isMeth()){
        assert a.cs.equals(a._cs);
        if(domCodom.contains(a.cs)){
          err(errFail,errFail.intro(a.cs,false)+"is already involved in the rename; thus "+errFail.intro(a.cs,a._s)+"can not be renamed");
          }
        }
      }
    }
  void earlyCheck(Arrow that){
    L l=p._ofCore(that.cs);
    if(l==null){err(errName,errName.intro(that.cs,false)+"does not exists");}
    MWT mwt=null;
    if(that._s!=null){
      mwt=_elem(l.mwts(),that._s);
      if(mwt==null){err(errName,errName.intro(that.cs,that._s)+"does not exists");}
      }
    earlyCheckNoUniqueNum(that);
    if(that.isP()){earlyCheckP(that, l);}
    if(that._s!=null){
      if(!that.isEmpty() &&!that.isMeth()){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method into a nested class");
        }
      if(that.isMeth() && that._sOut.equals(that._s)){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a method on itself");
        }
      }
    else{
      if(that.isMeth()){err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class into a method");}
      if(that.isCs() && that._cs.equals(that.cs)){
        err(errFail,"mapping: "+that.toStringErr()+"\nCan not rename a nested class on itself");
        }
      }
    if(!that.full && that._s==null){
      if(allWatched.contains(that.cs)){
        err(errFail,()->"The nested class "+errFail.intro(that.cs,false)
          +"can not be made abstract since is watched by "+errFail.intro(watchedBy(p.topCore(),that.cs),false));
        }
      }
    if(that._s!=null && !that.isEmpty()){
      assert that.isMeth();
      if(!that._cs.equals(that.cs)){
        err(errFail,"methods can only be renamed from inside the same nested class, but the following mapping is present: "+that.toStringErr()+"\n");
        }
      if(that._s.xs().size()!=that._sOut.xs().size()){
        err(errFail,"methods renames need to keep the same number of parameters, but the following mapping is present: "+that.toStringErr()+"\n");
        }
      }
    }
  private void earlyCheckNoUniqueNum(Arrow that) {
    boolean priv=that.cs.stream().anyMatch(c->c.hasUniqueNum());
    if(that._s!=null){priv&=that._s.hasUniqueNum();}
    if(that._sOut!=null){priv&=that._sOut.hasUniqueNum();}
    if(that._cs!=null){priv&=that._cs.stream().anyMatch(c->c.hasUniqueNum());}
    if(priv){err(errFail,"A mapping using unique numbers was attempted");}//Should it be prevented before
    }
  private void earlyCheckP(Arrow that, L l){
    if(allWatched.contains(that.cs)){
      err(errFail,"Redirected classes need to be fully abstract and not watched, but is watched by "+errFail.intro(watchedBy(p.topCore(), that.cs),false));
      }
    for(var mwt:l.mwts()){
      if(mwt.key().hasUniqueNum()){return;}
      if(mwt._e()!=null){
        err(errFail,"Redirected classes need to be fully abstract and not watched, but the following mapping is present: "+that.toStringErr()+"\nand "+errFail.intro(mwt,false)+"is implemented");
        }
      var ts=new ArrayList<T>();
      ts.add(mwt.mh().t());
      ts.addAll(mwt.mh().pars());
      ts.addAll(mwt.mh().exceptions());
      for(T t:ts){
        if(!t.p().isNCs()){continue;}
        var pi=emptyP.from(t.p().toNCs(),that.cs);
        if(pi.n()!=0){continue;}
        var arrow=new Arrow(pi.cs(),null,false,null,null,null);
        var a=map.get(arrow);
        if(a==null || !a.isP()){
          err(errFail,"Also "+errFail.intro(pi.cs(),false)+"need to be redirected to an outer path");
          }          
        }
      }
    }
  L lOfAddMap(){throw todo();}
  MWT mwtOf(S s1){throw todo();}
  MWT renameUsages(MWT mwt){throw todo();}
  L renameL(L l){throw todo();}//and adds to AddMap
  List<NC> renameNCs(List<NC> ncs){throw todo();}//and adds to AddMap
  List<MWT> renameMWTs(List<MWT> mwt){throw todo();}//and adds to AddMap
  NC renameNC(NC nc){throw todo();}//and adds to AddMap
  MWT renameMWT(MWT mwt){throw todo();}//and adds to AddMap
  Info renameInfo(L l){throw todo();}//and adds to AddMap
  MWT rename1(MWT mwt){throw todo();}//and adds to AddMap
  MWT rename2(MWT mwt){throw todo();}//and adds to AddMap
  MWT rename3(MWT mwt){throw todo();}//and adds to AddMap
  MWT rename4(MWT mwt){throw todo();}//and adds to AddMap
  MWT rename5(MWT mwt){throw todo();}//and adds to AddMap
  MWT rename6(MWT mwt){throw todo();}//and adds to AddMap
  NC rename7(NC nc){throw todo();}//and adds to AddMap
  NC rename8(NC nc){throw todo();}//and adds to AddMap
  NC rename9(NC nc){throw todo();}//and adds to AddMap
  NC rename10(NC nc){throw todo();}//and adds to AddMap
  //7+
  }
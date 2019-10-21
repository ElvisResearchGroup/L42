package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;
import is.L42.common.TypeManipulation;
import is.L42.generated.Core;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.MethT;
import is.L42.generated.P;
import is.L42.generated.S;

public class AlternativeMethodTypes {

  static public MethT mBase(Program p, P.NCs path, S s){
    var mt=_mtDeclared(p, path, s);
    assert mt!=null;
    return mBase(mt);
    }
  static public MethT mBase(MethT mt){
    if (!TypeManipulation.fwd_or_fwdP_inTs(mt.ts().stream())){return mt;}
    return mt.withT(TypeManipulation.fwdP(mt.t()));
    }
  static public MethT mNoFwd(MethT mt){
    List<T> ts = L(mt.ts(),t->TypeManipulation.noFwd(t));
    T retT=TypeManipulation.noFwd(mt.t());
    return mt.withT(retT).withTs(ts);
    }

  static public MethT _mC(MethT mt){
    T retT=mt.t();
    if(retT.mdf()!=Mdf.Mutable){return null;}
    retT=retT.withMdf(Mdf.Capsule);
    List<T> ts = L(mt.ts(),t->TypeManipulation.mutToCapsule(t));
    return mt.withT(retT).withTs(ts);
    }
  static public MethT _mI(MethT mt){
    T retT=mt.t();
    if(!retT.mdf().isIn(Mdf.Readable,Mdf.Lent)){return null;}
    retT=retT.withMdf(Mdf.Immutable);
    List<T> ts = L(mt.ts(),t->TypeManipulation.toImmOrCapsule(t));
    return mt.withT(retT).withTs(ts);
    }

  static public MethT _mVp(MethT mt, int parNum){
    T pN=mt.ts().get(parNum);
    if (pN.mdf()!=Mdf.Mutable){return null;}
    T retT=mt.t();
    retT=TypeManipulation._toLent(retT);
    if(retT==null){return null;}
    List<T> ts = L(range(mt.ts()),mt.ts(),(c,i,ti)->{
      if(i==parNum){c.add(pN.withMdf(Mdf.Lent));}
      else{c.add(TypeManipulation.mutToCapsule(ti));}
      });
    MethT res= mt.withT(retT).withTs(ts);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mImmFwd(MethT mt){
    if(!TypeManipulation.fwd_or_fwdP_inTs(mt.ts().stream())){return null;}
    T retT=mt.t();
    if(retT.mdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.ImmutablePFwd);
    List<T> ts = L(mt.ts(),t->TypeManipulation.mutToCapsuleAndFwdMutToFwdImm(t));
    MethT res= mt.withT(retT).withTs(ts);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mRead(MethT mt){
    if(!TypeManipulation.fwd_or_fwdP_inTs(mt.ts().stream())){return null;}
    T retT=mt.t();
    if(retT.mdf()!=Mdf.MutablePFwd){return null;}
    retT=retT.withMdf(Mdf.Readable);
    List<T> ts = L(mt.ts(),t->TypeManipulation.mutToCapsuleAndFwdToRead(t));
    MethT res=mt.withT(retT).withTs(ts);
    if(res.wf()){return res;}
    return null;
    }
  static void add(List<MethT>l,MethT t){
    if(t==null){return;}
    l.add(t);
    }
  static public MethT _mtDeclared(Program p, P.NCs path, S s){
    var l=p._ofCore(path);
    if(l==null){return null;}
    var mwt=(Core.L.MWT)_elem(l.mwts(),s);
    if(mwt==null){return null;}
    var mh=mwt.mh();
    var ts=p.from(mh.parsWithThis(),path);
    var ps=L(mh.exceptions().stream().map(t->p.from(t.p(),path)));
    MethT mt = new MethT(ts,p.from(mh.t(),path),ps);
    return mt;
    }
  static public List<MethT> types(MethT mt){
    List<MethT>res=new ArrayList<>();
    MethT base=mBase(mt);
    add(res,base);
    //changed order of those 2 to try and see what happens
    MethT mRead=_mRead(base);
    add(res, mRead);
    MethT mNoFwd=mNoFwd(base);
    add(res,mNoFwd);
    MethT mImmFwd=_mImmFwd(base);
    add(res,mImmFwd);
    add(res,_mC(base));
    add(res,_mC(mNoFwd));
    add(res,_mI(base));
    if(mRead!=null){add(res,_mI(mRead));}
    if(mImmFwd!=null){add(res,mNoFwd(mImmFwd));}
    for(int i:range(base.ts())){
      add(res,_mVp(base,i)); //1 mType for each mut parameter
      add(res,_mVp(mNoFwd,i)); //1 mType for each mut parameter
      }
    return res;
    }
  static public MethT _firstMatchReturn(Program p,T nt,List<MethT> mts){
    for(MethT mt:mts){
      if(p.isSubtype(mt.t(), nt,null)){return mt;}
      }
    return null;
    }
    
  //only check mdf subtyping
  public static boolean methMdfTSubtype(MethT mSub,MethT mSuper){
    if (!Program.isSubtype(mSub.t().mdf(),mSuper.t().mdf())){return false;}
    assert mSub.ts().size()==mSuper.ts().size();
    for(int i:range(mSub.ts())){
      T tSub=mSub.ts().get(i);
      T tSuper=mSuper.ts().get(i);
      if (!Program.isSubtype(tSuper.mdf(),tSub.mdf())){return false;}
      }
    return true;
    }
  public static boolean methTSubtype(Program p,MethT mSub,MethT mSuper){
    if(!p.isSubtype(mSub.t(),mSuper.t(),null)){return false;}
    if(mSub.ts().size()!=mSuper.ts().size()){return false;}
    for(int i:range(mSub.ts())){
      T tSub=mSub.ts().get(i);
      T tSuper=mSuper.ts().get(i);
      if (!p.isSubtype(tSuper,tSub,null)){return false;}
      }
    for(P pi:mSub.ps()){
      if(!exceptionSubtype(p,pi, mSuper)){return false;}
      }
    return true;
    }
  public static boolean exceptionSubtype(Program p,P pi, MethT mSuper) {
    for(P pj:mSuper.ps()){
      if(p.isSubtype(pi,pj,null)){return true;}
      }
    return false;
    }
  
  //what is this method really doing?  
  public static MethT _bestMatchMtype(Program p,MethT superMt,List<MethT> mts){
    List<MethT> res=new ArrayList<>();
    for(MethT mt:mts){
      if(!methTSubtype(p,mt, superMt)){continue;}
      if(res.stream().anyMatch(mti->methMdfTSubtype(mti, mt))){continue;}
      res.stream().filter(mti->!methMdfTSubtype(mt, mti)).forEach(res::add);
      res.add(mt);//if there is no method that is even better, add
      }
  //assert res.size()==1: res.size(); sometime is false, for example capsule->capsule and mut->mut
  if(res.isEmpty()){return null;}
  if(res.size()==1){return res.get(0);}
  var res1 = res.stream().filter(
    mt1->res.stream().allMatch(
      mt2->Program.isSubtype(mt1.t().mdf(),mt2.t().mdf()))).findAny();
    if(res1.isPresent()){return res1.get();}
    assert false:
      "";
    return res.get(0);
  }
}

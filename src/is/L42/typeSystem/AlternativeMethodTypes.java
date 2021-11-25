package is.L42.typeSystem;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.typeSystem.TypeManipulation.*;

import java.util.ArrayList;
import java.util.List;

import is.L42.common.Program;
import is.L42.flyweight.P;
import is.L42.generated.Core;
import is.L42.generated.Mdf;
import is.L42.generated.MethT;
import is.L42.generated.S;
public class AlternativeMethodTypes {

  static public MethT mBase(Program p, P.NCs path, S s){
    var mt=_mtDeclared(p, path, s);
    assert mt!=null;
    return mBase(mt);
    }
  static public MethT mBase(MethT mt){
    if (!fwd_or_fwdP_inMdfs(mt.mdfs())){return mt;}
    return mt.withMdf(fwdPOf(mt.mdf()));
    }
  static public MethT mNoFwd(MethT mt){
    List<Mdf> mdfs = L(mt.mdfs(),m->noFwd(m));
    return mt.withMdfs(mdfs).withMdf(noFwd(mt.mdf()));
    }
  static public MethT _mC(MethT mt){
    if(mt.mdf()!=Mdf.Mutable){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->mutToCapsule(m));
    return mt.withMdfs(mdfs).withMdf(Mdf.Capsule);
    }
  static public MethT _mI(MethT mt){
    if(!mt.mdf().isIn(Mdf.Readable,Mdf.Lent)){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->toImmOrCapsule(m));
    return mt.withMdfs(mdfs).withMdf(Mdf.Immutable);
    }

  static public MethT _mVp(MethT mt, int parNum){
    if (mt.mdfs().get(parNum)!=Mdf.Mutable){return null;}
    var mdf=_toLent(mt.mdf());
    if(mdf==null){return null;}
    List<Mdf> mdfs = L(range(mt.mdfs()),mt.mdfs(),(c,i,mi)->{
      if(i==parNum){c.add(Mdf.Lent);}
      else{c.add(mutToCapsule(mi));}
      });
    MethT res= mt.withMdfs(mdfs).withMdf(mdf);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mImmFwd(MethT mt){
    if(!mt.mdf().isFwdPMut()){return null;}
    if(!fwd_or_fwdP_inMdfs(mt.mdfs())){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->mutToCapsuleAndFwdMutToFwdImm(m));
    MethT res= mt.withMdfs(mdfs).withMdf(Mdf.ImmutablePFwd);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mRead(MethT mt){
    if(!mt.mdf().isFwdPMut()){return null;}
    if(!fwd_or_fwdP_inMdfs(mt.mdfs())){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->mutToCapsuleAndFwdToRead(m));
    MethT res=mt.withMdfs(mdfs).withMdf(Mdf.Readable);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mImmFwdExtended(MethT mt){
    if(!mt.mdf().isFwdMut()){return null;}
    if(!fwd_or_fwdP_inMdfs(mt.mdfs())){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->mutToCapsuleAndFwdMutToFwdImm(m));
    MethT res=mt.withMdfs(mdfs).withMdf(Mdf.ImmutableFwd);
    if(res.wf()){return res;}
    return null;
    }
  static public MethT _mReadExtended(MethT mt){
    if(!mt.mdf().isFwdMut()){return null;}
    if(!fwd_or_fwdP_inMdfs(mt.mdfs())){return null;}
    List<Mdf> mdfs = L(mt.mdfs(),m->mutToCapsuleAndFwdToRead(m));
    MethT res=mt.withMdfs(mdfs).withMdf(Mdf.Readable);
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
    var mwt=(Core.MWT)_elem(l.mwts(),s);
    if(mwt==null){return null;}
    var mh=mwt.mh();
    List<Mdf> mdfs=L(c->{
      c.add(mh.mdf());
      for(var t:mh.pars()){c.add(t.mdf());}
      });
    return new MethT(mdfs,mh.t().mdf());
    }
  static public List<MethT> types(Program p, P.NCs source, S s){
    return types(mBase(p,source,s));
    }
  static public List<MethT> types(MethT mt){
    List<MethT>res=new ArrayList<>();
    MethT base=mBase(mt);
    add(res,base);
    MethT _mRead=_mRead(base);
    add(res, _mRead);
    MethT _mReadE=_mReadExtended(base);
    add(res, _mReadE);
    MethT mNoFwd=mNoFwd(base);
    add(res,mNoFwd);
    MethT mImmFwd=_mImmFwd(base);
    add(res,mImmFwd);
    add(res,_mC(base));
    add(res,_mC(mNoFwd));
    add(res,_mI(base));
    if(_mReadE!=null){add(res,_mI(_mReadE));}
    MethT _mImmFwdExtended=_mImmFwdExtended(base);
    add(res,_mImmFwdExtended);
    if(_mImmFwdExtended!=null){add(res,mNoFwd(_mImmFwdExtended));}
    if(_mRead!=null){add(res,_mI(_mRead));}
    if(mImmFwd!=null){add(res,mNoFwd(mImmFwd));}
    for(int i:range(base.mdfs())){
      add(res,_mVp(base,i)); //1 mType for each mut parameter
      add(res,_mVp(mNoFwd,i)); //1 mType for each mut parameter
      }
    return res;
    }
  static public MethT _firstMatchReturn(Program p,Mdf nt,List<MethT> mts){
    for(MethT mt:mts){if(Program.isSubtype(mt.mdf(),nt)){return mt;}}
    return null;
    }
    
  //only check mdf subtyping
  public static boolean methMdfTSubtype(MethT mSub,MethT mSuper){
    if (!Program.isSubtype(mSub.mdf(),mSuper.mdf())){return false;}
    assert mSub.mdfs().size()==mSuper.mdfs().size();
    for(int i:range(mSub.mdfs())){
      var subi=mSub.mdfs().get(i);
      var superi=mSuper.mdfs().get(i);
      if (!Program.isSubtype(superi,subi)){return false;}
      }
    return true;
    }
  }

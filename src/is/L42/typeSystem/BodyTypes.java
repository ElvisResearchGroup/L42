package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.unreachable;

import java.util.Collections;
import java.util.List;

import is.L42.common.EndError;
import is.L42.common.G;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;
import is.L42.generated.MethT;
import static is.L42.generated.Mdf.*;

public class BodyTypes{
  static void checkBody(Program p,G g,MH mh,Core.E e){
    List<Mdf> mdfs=L(mh.parsWithThis().stream().map(t->t.mdf()));
    List<Mdf> mdfs1=BodyTypes.of(new MethT(mdfs,mh.t().mdf()));
    EndError err=null;
    for(Mdf mdf : mdfs1){
      var ts=new MdfTypeSystem(p,g,Collections.emptySet(),mdf);
      try{e.visitable().accept(ts);return;}
      catch(EndError endErr){if(err==null){err=endErr;}}
      }
    assert err!=null;
    throw err;
    }
  static List<Mdf> of(MethT mt){
    var mdfs=mt.mdfs();
    switch(mt.mdf()){
      case Readable: return ofRead(mdfs);
      case Lent: return ofLent(mdfs);
      case Mutable: return ofMut(mdfs);
      case Immutable: return ofImm(mdfs);
      case Capsule: return ofCapsule(mdfs);
      case Class: return ofClass(mdfs);
      case ImmutableFwd: return ofFwdImm(mdfs);
      case MutableFwd: return ofFwdMut(mdfs);
      default:throw unreachable();
      }
    }
  static List<Mdf> ofRead(List<Mdf>mdfs){
    return List.of(Readable,MutablePFwd,ImmutablePFwd);
    }
  static List<Mdf> ofLent(List<Mdf>mdfs){
    return List.of(Lent,MutablePFwd);
    }
  static List<Mdf> ofMut(List<Mdf>mdfs){
    return L(MutablePFwd);
    }
  static final private List<Mdf> muts=List.of(Readable,Lent,Mutable,MutableFwd);
  static boolean hasMuts(List<Mdf>mdfs) {return mdfs.stream().anyMatch(m->muts.contains(m));} 
  static List<Mdf> ofImm(List<Mdf>mdfs){
    if(!hasMuts(mdfs)){return List.of(Readable,ImmutablePFwd,MutablePFwd);}
    if(!mdfs.contains(Mutable)){return List.of(ImmutablePFwd,MutablePFwd);}
    return L(ImmutablePFwd);
    }
  static List<Mdf> ofCapsule(List<Mdf>mdfs){
    if(!mdfs.contains(Mutable)){return L(MutablePFwd);}
    return L(Capsule);
    }
  static List<Mdf> ofClass(List<Mdf>mdfs){
    return L(Class);
    }
  static List<Mdf> ofFwdImm(List<Mdf>mdfs){
    if(!hasMuts(mdfs)){return List.of(Readable,ImmutableFwd,MutableFwd);}
    if(!mdfs.contains(Mutable)){return List.of(ImmutableFwd,MutablePFwd);}
    return L(ImmutableFwd);    
    }
  static List<Mdf> ofFwdMut(List<Mdf>mdfs){
    return L(MutableFwd);
    }
  }
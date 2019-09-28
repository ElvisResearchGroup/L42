package is.L42.typeSystem;

import static is.L42.tools.General.L;
import static is.L42.tools.General.bug;

import java.util.List;

import is.L42.common.Program;
import is.L42.generated.Core.MH;
import is.L42.generated.Mdf;

public class Coherence {
  public final Program p;
  public final List<MH> mhs;
  public final List<MH> classMhs;
  public Coherence(Program p){
    this.p=p;
    mhs=L(p.topCore().mwts(),(c,mi)->{if(mi._e()==null){c.add(mi.mh());}});
    classMhs=L(mhs.stream().filter(mh->mh.mdf().isClass()));
    }
  public boolean allowedAbstract(MH mh){
    return classMhs.stream().allMatch(k->!canAlsoBe(k.t().mdf(),mh.mdf()));
    }
  public boolean canAlsoBe(Mdf mdf0, Mdf mdf){
    return switch(mdf0){
      case Capsule,Mutable-> !mdf.isClass();
      case Lent-> mdf.isIn(Mdf.Mutable,Mdf.Lent,Mdf.Readable,Mdf.MutableFwd);
      case Readable,Immutable->mdf.isIn(Mdf.Readable,Mdf.Immutable,Mdf.ImmutableFwd);
      default->false;
      };
    }
  }

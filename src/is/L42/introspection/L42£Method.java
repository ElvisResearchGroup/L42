package is.L42.introspection;

import static is.L42.tools.General.merge;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.L42ValueCache;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
import is.L42.generated.Core.MH;
import is.L42.generated.Core.T;
import is.L42.generated.Mdf;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.meta.L42£Meta;
import is.L42.meta.L42£Name;
import is.L42.meta.MetaError;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42ClassAny;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.General;

public class L42£Method extends L42NoFields.Eq<L42£Method>{
  static public L42£Method fromNested(String pos,L42£Nested nested,L42£Name name){
    return new L42£Method(pos,nested,name).myNorm();
    }
  public L42£Doc doc(){
    return L42£Doc.fromDoc(nested().root(),nameFromRoot,L42£Doc.normalize(mwt.docs()));
    }
  public L42£Type returnType(){
    var t=mwt.mh().t();
    return L42£Type.fromType(t,nested.root(), nameFromRoot());    
    }
  public int parNum(){return mwt.mh().pars().size()+1;}
  public L42£Type parIn(int i){
    var t=mwt.mh().parsWithThis().get(i);
    return L42£Type.fromType(t,nested.root(), nameFromRoot());
    }
  public int excNum(){return mwt.mh().exceptions().size();}
  public L42£Type excIn(int i){
    var t=mwt.mh().exceptions().get(i);
    return L42£Type.fromType(t,nested.root(), nameFromRoot());
    }
  public boolean isRefined(){return nested.currentL.info().refined().contains(mwt.key());}
  public boolean isAbstract(){return mwt._e()==null;}
  public L42£Nested nested(){return nested;}
  public L42£Name nameFromRoot(){return nameFromRoot;}
  public String position(){return position;}
  
  final String position;
  final MWT mwt;
  final L42£Nested nested;
  final L42£Name nameFromRoot;
  private L42£Method(String position,L42£Nested nested,L42£Name name){
    this.position=position;
    this.nested=nested;
    assert name._s!=null;
    var mwt=_elem(nested.currentL.mwts(),name._s);
    if(mwt==null){
      assert name.cs.isEmpty() && name.selector().equals("#apply()");
      var mh=new MH(Mdf.Immutable,L(),P.coreAny,name._s,L(),L());
      mwt=new MWT(nested.currentL.poss(),L(), mh,"",null);
      }
    this.mwt=mwt;
    this.nameFromRoot=name.withX(null);
    assert nested.nameFromRoot.equals(name.onlyCs());
    } 


  @Override public String toString(){
    return new MetaError(null).intro(mwt,false)+"\n"+position;
    }
  @Override public boolean eq(L42£Method that){
    return position.equals(that.position)
      && nested.eq(that.nested)
      && nameFromRoot._s.equals(that.nameFromRoot._s);
    }
  public static final Class<L42£Method> _class=L42£Method.class;
  public static final EqCache<L42£Method> myCache=new EqCache<>(TrustedKind.Method);
  @Override public EqCache<L42£Method> myCache(){return myCache;}
  static{L42CacheMap.addCachableType_synchronized(L42£Method.class,myCache);}
  static public L42£Method instance=new L42£Method("",L42£Nested.instanceVoid,L42£Name.empty.withSelector("#apply()")).myNorm();//TODO: invalid method??
  @Override public L42£Method newInstance(){return instance;}
  }
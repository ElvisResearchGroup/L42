package is.L42.introspection;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.generated.P;
import is.L42.meta.L42£Meta;
import is.L42.meta.L42£Name;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.L42Any;
import is.L42.platformSpecific.javaTranslation.L42NoFields;

public class L42£Type extends L42NoFields<L42£Type>{
  static public L42£Type fromClass(String mdf,L42£Doc doc,L42Any clazz){
    return fromClass(mdf,doc,L42£Meta.unwrapPath(clazz));
    }
  static public L42£Type fromClass(String mdf,L42£Doc doc,P clazz){
    var refTo=L42£Nested.fromClass(clazz);
    return new L42£Type(mdf,doc,refTo);
    }
  static public L42£Type fromLibrary(String mdf,L42£Doc doc,L42£Name name){
    return new L42£Type(mdf,doc,doc.root.nestedByName(name));
    }
  public String mdf(){return mdf;}
  public L42£Doc doc(){return doc;}
  public L42£Nested nested(){return nested;}
  final String mdf;
  final L42£Doc doc;//also contains rootL and nameFromRoot
  final L42£Nested nested;
  private L42£Type(String mdf,L42£Doc doc,L42£Nested nested){
    this.mdf=mdf;
    this.doc=doc;
    this.nested=nested;
    } 
  @Override public String toString(){
    if(nested.isBinded()){
      return mdf+" "+doc+" "+nested._classAny;
      }
    return mdf+" "+doc+" "+nested.nameFromRoot;
    }
  //TODO: what should be the equals in 42? for name? for nested? for type?
  //equals for nested would leak private informations!
  
  
  public boolean eq(L42£Type that){
    if(!mdf.equals(that.mdf)){return false;}
    if(doc.eq(that.doc)){return false;}
    return nested.eq(that.nested);
    }
  public static final Class<L42£Nested> _class=L42£Nested.class;
  public static final TypeCache myCache=new TypeCache();
  static{L42CacheMap.addCachableType_synchronized(L42£Type.class,myCache);}
  @Override public TypeCache myCache(){return myCache;}
  static public L42£Type instance=new L42£Type("imm",L42£Doc.instance,L42£Nested.instanceVoid).myNorm();
  @Override public L42£Type newInstance(){return instance;}
  }
class TypeCache extends ValueCache<L42£Type>{
  @Override public Object typename() {return TrustedKind.Type;}
  @Override protected boolean valueCompare(L42£Type t1, L42£Type t2) {return t1.eq(t2);}
  }
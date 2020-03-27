package is.L42.introspection;

import static is.L42.tools.General.L;
import static is.L42.tools.General.todo;

import java.util.List;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.generated.Core.Doc;
import is.L42.meta.L42£Name;
import is.L42.platformSpecific.javaTranslation.L42NoFields;

public class L42£Doc extends L42NoFields<L42£Doc>{
  static Doc normalize(List<Doc> docs){throw todo();}
  static L42£Doc fromDoc(L42£Nested rootL,L42£Name nameFromRoot,Doc doc){
    return new L42£Doc(rootL,nameFromRoot,doc);
    }
  private L42£Doc(L42£Nested root,L42£Name nameFromRoot,Doc doc){
    this.root=root;this.nameFromRoot=nameFromRoot;this.doc=doc;
    }
  final L42£Nested root;
  final L42£Name nameFromRoot;
  final Doc doc;  
  public boolean eq(L42£Doc that){throw todo();
    }
  public static final Class<L42£Doc> _class=L42£Doc.class;
  public static final DocCache myCache=new DocCache();
  static{L42CacheMap.addCachableType_synchronized(L42£Doc.class,myCache);}
  @Override public DocCache myCache(){return myCache;}
  static Doc emptyDoc=new Doc(null,L(),L());
  static public L42£Doc instance=new L42£Doc(L42£Nested.instanceVoid,L42£Name.empty,emptyDoc).myNorm();
  @Override public L42£Doc newInstance(){return instance;}
  }
class DocCache extends ValueCache<L42£Doc>{
  @Override public String typename() {return "Doc";}
  @Override protected boolean valueCompare(L42£Doc t1, L42£Doc t2) {return t1.eq(t2);}
  }
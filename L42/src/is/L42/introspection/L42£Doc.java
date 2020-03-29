package is.L42.introspection;

import static is.L42.tools.General.L;
import static is.L42.tools.General.todo;

import java.util.Collections;
import java.util.List;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.generated.Core.Doc;
import is.L42.meta.L42£Name;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.L42NoFields;

public class L42£Doc extends L42NoFields<L42£Doc>{
    public L42£Nested root(){return root;}
    /*TODO: insert them all, then lift them as TrustedOp, then propagate them (twice) to AdamTowel
    d.root
    d.nameFromOrigin
    d.docs
    d.hasAnnotation
    d.isBinded
      d.annotatedClassAny : class Any
      d.annotatedName : Name
    d.selector
    d.x
    d.texts
    */


  static Doc normalize(List<Doc> docs){
    if(docs.isEmpty()){return emptyDoc;}
    //if(docs.size()==1){return docs.get(0);} No, otherwise we can not distinguish @{@A,@B,@C} from @A,@B,@C; but sum operator would make them different...
    return new Doc(null,Collections.nCopies(docs.size()+1,""),docs);
    }
  static L42£Doc fromDoc(L42£Nested rootL,L42£Name nameFromRoot,Doc doc){
    return new L42£Doc(rootL,nameFromRoot,doc);
    }
  private L42£Doc(L42£Nested root,L42£Name nameFromRoot,Doc doc){
    this.root=root;this.nameFromRoot=nameFromRoot;this.doc=doc;
    }
  final L42£Nested root;
  final L42£Name nameFromRoot;
  final Doc doc;  
  public boolean eq(L42£Doc that){
    return root.eq(that.root)
      && nameFromRoot.eq(that.nameFromRoot)
      && doc.equals(that.doc);
    }
  public String toString(){return doc.toString();}
  public static final Class<L42£Doc> _class=L42£Doc.class;
  public static final DocCache myCache=new DocCache();
  static{L42CacheMap.addCachableType_synchronized(L42£Doc.class,myCache);}
  @Override public DocCache myCache(){return myCache;}
  static Doc emptyDoc=new Doc(null,L(),L());
  static public L42£Doc instance=new L42£Doc(L42£Nested.instanceVoid,L42£Name.empty,emptyDoc).myNorm();
  @Override public L42£Doc newInstance(){return instance;}
  }
class DocCache extends ValueCache<L42£Doc>{
  @Override public Object typename(){return TrustedKind.Doc;}
  @Override protected boolean valueCompare(L42£Doc t1, L42£Doc t2) {return t1.eq(t2);}
  }
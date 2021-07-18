package is.L42.introspection;

import static is.L42.tools.General.L;

import java.util.Collections;
import java.util.List;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.common.Program;
import is.L42.generated.Core.Doc;
import is.L42.generated.P;
import is.L42.meta.L42£Name;
import is.L42.nativeCode.TrustedKind;
import is.L42.platformSpecific.javaTranslation.L42NoFields;
import is.L42.platformSpecific.javaTranslation.Resources;

public class L42£Doc extends L42NoFields.Eq<L42£Doc>{
    public L42£Nested root(){return root;}
    public L42£Name nameFromRoot(){return nameFromRoot;}
    public int docNum(){return doc.docs().size();}
    public L42£Doc docIn(int that){return fromDoc(root,nameFromRoot,doc.docs().get(that));}
    public String textIn(int that){return doc.texts().get(that);}//textNum==docNum+1
    public boolean hasAnnotation(){return doc._pathSel()!=null;}
    public L42£Nested nested(){
      if(doc._pathSel()==null){throw new IndexOutOfBoundsException("The Doc has no annotated Path");}
      P p=doc._pathSel().p();
      if(root.isBinded()){
        var pp=Resources.currentP.from(p, root._classAny.toNCs());
        return L42£Nested.fromClass(pp);
        }
      var p0=p.toNCs();
      if(p0.n()>nameFromRoot.cs.size()){
        p0=p0.withN(p0.n()-(nameFromRoot.cs.size()+1));
        return L42£Nested.fromClass(p0);
        }
      var pTop=Program.emptyP.from(p0,nameFromRoot.cs);
      assert pTop.n()==0;
      return root.nestedByName(L42£Name.fromCs(pTop.cs()));
      }
    public L42£Name name(){
      var res=nested().nameFromRoot;
      var ps=doc._pathSel();
      if(ps._s()!=null){res=res.withSelector(ps._s().toString());}
      if(ps._x()!=null){res=res.withX(ps._x().toString());}
      return res;
      }
  static Doc normalize(List<Doc> docs){
    if(docs.isEmpty()){return emptyDoc;}
    //if(docs.size()==1){return docs.get(0);} No, otherwise we can not distinguish @{@A,@B,@C} from @A,@B,@C; but sum operator would make them different...
    return new Doc(null,Collections.nCopies(docs.size()+1,""),docs);
    }
  static L42£Doc fromDoc(L42£Nested rootL,L42£Name nameFromRoot,Doc doc){
    return new L42£Doc(rootL,nameFromRoot,doc).myNorm();
    }
  private L42£Doc(L42£Nested root,L42£Name nameFromRoot,Doc doc){
    assert nameFromRoot!=L42£Name.instance:
    "";
    this.root=root;this.nameFromRoot=nameFromRoot;this.doc=doc;
    }
  final L42£Nested root;
  final L42£Name nameFromRoot;
  final Doc doc;
  
  @Override public boolean eq(L42£Doc that){
    return root.eq(that.root)
      && nameFromRoot.eq(that.nameFromRoot)
      && doc.equals(that.doc);
    }
  public String toString(){return doc.toString();}
  public static final Class<L42£Doc> _class=L42£Doc.class;
  public static final L42Cache<L42£Doc> myCache=new EqCache<>(TrustedKind.Doc);
  @Override public L42Cache<L42£Doc> myCache(){return myCache;}
  static Doc emptyDoc=new Doc(null,L(),L());
  static public L42£Doc instance=new L42£Doc(L42£Nested.instanceVoid,L42£Name.empty,emptyDoc).myNorm();
  @Override public L42£Doc newInstance(){return instance;}
  }
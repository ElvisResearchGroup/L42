package is.L42.introspection;

import static is.L42.tools.General.merge;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;
import static is.L42.tools.General.todo;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.cache.L42CacheMap;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Program;
import is.L42.generated.C;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.L;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import is.L42.generated.Core.L.MWT;
import is.L42.generated.Core.L.NC;
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

public class L42£Nested extends L42NoFields.Eq<L42£Nested>{
  static String posStr(List<Pos> poss){
    return poss.stream().map(L42£Nested::posStr).collect(Collectors.joining("\n"));
    }
  static String posStr(Pos pos){
    return "File name: "+pos.fileName()+"\nLine: "+pos.line()+"\nColumn: "+pos.column();
    }
  static public L42£Nested fromClass(L42Any clazz){
    return fromClass(L42£Meta.unwrapPath(clazz));
    }
  static public L42£Nested fromClass(P clazz){
    if(clazz==P.pAny){return instanceAny;}
    if(clazz==P.pVoid){return instanceVoid;}
    if(clazz==P.pLibrary){return instanceLibrary;}
    return fromClass(clazz.toNCs());
    }
  static public L42£Nested fromClass(P.NCs clazz){
    L l=Resources.currentP._ofCore(clazz);
    assert l!=null;
    return new L42£Nested(posStr(l.poss()),l,l,L42£Name.empty,clazz).myNorm();
    }
  static public L42£Nested fromLibrary(L42£Library l){
    return fromLibrary(l.unwrap);
    }
  static public L42£Nested fromLibrary(L l){
    return new L42£Nested(posStr(l.poss()),l,l,L42£Name.empty,null).myNorm();
    }
  public L42£Nested nestedByName(L42£Name name){
    assert name!=L42£Name.instance:
    "";
    L l=rootL._cs(name.cs);
    if(l==null){throw new ArrayIndexOutOfBoundsException();}//throw err name invalid
    if(_classAny==null || !_classAny.isNCs()){
      return new L42£Nested(posStr(l.poss()),l,rootL,name.onlyCs(),null).myNorm();
      }
    var newP=_classAny.toNCs();
    newP=newP.withCs(merge(newP.cs(),name.cs));
    return fromClass(newP);
    }
  public boolean hasOuter(){return _outerC()!=null;}
  public String outerName(){
    C c=_outerC();
    if(c==null){throw new ArrayIndexOutOfBoundsException();}
    return c.toString();
    }
  public C _outerC(){//TODO: we can have classAny with private stuff... how we avoid it?
    if(!isBinded()){
      var cs=nameFromRoot.cs;
      if(cs.isEmpty()){return null;}
      return cs.get(cs.size()-1);  
      }
    if(!_classAny.isNCs()){return null;}
    var path=_classAny.toNCs();
    if(path.cs().size()<=1){return null;}
    return path.cs().get(path.cs().size()-1);
    }
  public L42£Nested outer(){
    if(!isBinded()){
      if(nameFromRoot.cs.isEmpty()){throw new ArrayIndexOutOfBoundsException();}
      return nestedByName(L42£Name.fromCs(popLRight(nameFromRoot.cs)));
      }
    if(!_classAny.isNCs()){throw new ArrayIndexOutOfBoundsException();}
    var path=_classAny.toNCs();
    if(path.cs().size()<=1){throw new ArrayIndexOutOfBoundsException();}
    return L42£Nested.fromClass(path.withCs(popLRight(path.cs())));
    }
  public L42£Doc outerDoc(){
    var c=_outerC();
    if(c==null){throw new ArrayIndexOutOfBoundsException();}
    var o=outer();
    NC nc=_elem(o.currentL.ncs(),c);
    Doc doc=L42£Doc.normalize(nc.docs());
    return L42£Doc.fromDoc(o.root(),o.nameFromRoot(),doc);
    }
  public int nestedNum(){return publicNCs.size();}
  public L42£Nested nestedIn(int i){
    var nc=publicNCs.get(i);//correctly propagates ArrayOutOfBound
    var cs=pushL(this.nameFromRoot.cs,nc.key());
    return fromLibrary(rootL).nestedByName(L42£Name.fromCs(cs));
    }
  public int methodNum(){return publicMWTs.size();}
  public L42£Method methodIn(int i){
    var mwt=publicMWTs.get(i);//correctly propagates ArrayOutOfBound
    return L42£Method.fromNested(posStr(mwt.poss()),this,nameFromRoot.withSelector(mwt.key().toString())); 
    }
  public int implementedNum(){return publicNCs.size();}
  public L42£Type implementedIn(int i){
    var t=publicTs.get(i);//correctly propagates ArrayOutOfBound
    return L42£Type.fromType(t,root(),nameFromRoot());
    }
  //Doc infoTypeDep(){}//have a list of docs inside; the L42 version may just unwrap the docs
  /*..*/
  public boolean hasHiddenImplements(){return publicTs.size()!=currentL.ts().size();}
  public boolean isClose(){return currentL.info().close();}
  public boolean isInterface(){return currentL.isInterface();}
  public L42£Nested root(){
    if(_classAny!=null){return this;}
    return fromLibrary(this.rootL);
    }
  public L42£Name nameFromRoot(){return nameFromRoot;}
  public L42£Doc innerDoc(){
    return L42£Doc.fromDoc(root(),nameFromRoot,L42£Doc.normalize(currentL.docs()));
    }
  public String position(){return position;}
  public boolean isBinded(){return _classAny!=null;}
  public L42ClassAny classAny(){
    if(_classAny==null){throw new IndexOutOfBoundsException();}
    return new L42ClassAny(_classAny);
    }
  final String position;
  final L currentL;
  final List<NC> publicNCs;
  final List<MWT> publicMWTs;
  final List<T> publicTs;
  final L rootL;
  final L42£Name nameFromRoot;
  final P _classAny;
  private L42£Nested(String position,L currentL,L rootL,L42£Name nameFromRoot,P _classAny){
    this.position=position;
    this.currentL=currentL;
    this.rootL=rootL;
    this.nameFromRoot=nameFromRoot;
    this._classAny=_classAny;
    this.publicNCs=L(currentL.ncs().stream().filter(m->!m.key().hasUniqueNum()));
    this.publicMWTs=L(currentL.mwts().stream().filter(m->!m.key().hasUniqueNum()));
    this.publicTs=L(currentL.ts().stream().filter(m->!m.p().hasUniqueNum()));
    } 


  @Override public String toString(){
    return new MetaError(null).intro(currentL,false)+"\n"+position;
    }
  @Override public boolean eq(L42£Nested that){
    if(_classAny!=null){return _classAny.equals(that._classAny);}
    return this.position.equals(that.position) 
      && this.nameFromRoot.equals(that.nameFromRoot) 
      && this.rootL.equals(that.rootL);
    }
  public static final Class<L42£Nested> _class=L42£Nested.class;
  public static final EqCache<L42£Nested> myCache=new EqCache<>(TrustedKind.Nested);
  @Override public EqCache<L42£Nested> myCache(){return myCache;}
  static{L42CacheMap.addCachableType_synchronized(L42£Nested.class,myCache);}
  static public L42£Nested instanceAny=new L42£Nested("",Program.emptyL,Program.emptyLInterface,L42£Name.empty,P.pAny).myNorm();
  static public L42£Nested instanceLibrary=new L42£Nested("",Program.emptyL,Program.emptyL,L42£Name.empty,P.pVoid).myNorm();
  static public L42£Nested instanceVoid=new L42£Nested("",Program.emptyL,Program.emptyL,L42£Name.empty,P.pLibrary).myNorm();
  @Override public L42£Nested newInstance(){return instanceVoid;}
  }
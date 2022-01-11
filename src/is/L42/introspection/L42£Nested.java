package is.L42.introspection;

import static is.L42.generated.LDom._elem;
import static is.L42.tools.General.L;
import static is.L42.tools.General.merge;
import static is.L42.tools.General.popLRight;
import static is.L42.tools.General.pushL;

import java.util.List;
import java.util.stream.Collectors;

import is.L42.cache.L42Cache;
import is.L42.common.EndError;
import is.L42.common.Program;
import is.L42.flyweight.C;
import is.L42.flyweight.CoreL;
import is.L42.flyweight.P;
import is.L42.generated.Core.Doc;
import is.L42.generated.Core.MWT;
import is.L42.generated.Core.NC;
import is.L42.generated.Core.T;
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
import is.L42.typeSystem.Coherence;

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
  /*static public L42£Nested fromClassInBinded(P clazz,C _top){
    if(clazz==P.pAny){return instanceAny;}
    if(clazz==P.pVoid){return instanceVoid;}
    if(clazz==P.pLibrary){return instanceLibrary;}
    var p=clazz.toNCs();
    if(p.n()!=0){return fromClass(p.withN(p.n()-1));}
    assert _top!=null;
    return fromClass(p.withCs(pushL(p.cs(),_top)));
    }*/
  static public L42£Nested fromClass(P.NCs clazz){
    CoreL l=Resources.currentP._ofCore(clazz);
    if(l==null){
      var alt=EndError.PathNotExistent.alternatives(Resources.currentP,clazz);
      throw new IndexOutOfBoundsException(
        clazz+" not in the domain of the program.\n"
        + "It may be mispelled, or it may be still waiting compilation"
        +alt);
      }
    return new L42£Nested(posStr(l.poss()),l,l,L42£Name.empty,clazz).myNorm();
    }
  static public L42£Nested fromLibrary(L42£Library l){
    return fromLibrary(l.unwrap);
    }
  static public L42£Nested fromLibrary(CoreL l){
    return new L42£Nested(posStr(l.poss()),l,l,L42£Name.empty,null).myNorm();
    }
  public L42£Nested nestedByName(L42£Name name){
    assert name!=L42£Name.instance:
    "";
    CoreL l=rootL._cs(name.cs);
    if(l==null){
      throw new IndexOutOfBoundsException(name+" not in the domain of the nested class; the domain is: "+rootL.domNC());
      }//throw err name invalid
    if(_classAny==null || !_classAny.isNCs()){
      return new L42£Nested(posStr(l.poss()),l,rootL,name.onlyCs(),null).myNorm();
      }
    var newP=_classAny.toNCs();
    newP=newP.withCs(merge(newP.cs(),name.cs));
    return fromClass(newP);
    }
  private static String errAnon="The nested class is anonymous";
  private static String errOuter="The outer nested class is inaccessible (top level or primitive path)";
  public boolean hasOuter(){return _outerCForDoc()!=null;}
  public String outerName(){
    C c=_outerC();
    if(c==null){throw new IndexOutOfBoundsException(errAnon);}
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
    if(path.cs().isEmpty()){return null;}
    return path.cs().get(path.cs().size()-1);
    }
  private C _outerCForDoc(){//like _outerC but fails if the outerDoc could be still to compile 
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
      if(nameFromRoot.cs.isEmpty()){throw new IndexOutOfBoundsException(errAnon);}
      return nestedByName(L42£Name.fromCs(popLRight(nameFromRoot.cs)));
      }
    if(!_classAny.isNCs()){throw new IndexOutOfBoundsException(errOuter);}
    var path=_classAny.toNCs();
    if(path.cs().size()<=1){throw new IndexOutOfBoundsException(errOuter);}
    return L42£Nested.fromClass(path.withCs(popLRight(path.cs())));
    }
  public L42£Doc outerDoc(){
    var c=_outerCForDoc();
    if(c==null){throw new IndexOutOfBoundsException(errAnon);}
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
  public int implementedNum(){return publicTs.size();}
  public L42£Type implementedIn(int i){
    var t=publicTs.get(i);//correctly propagates ArrayOutOfBound
    return L42£Type.fromType(t,root(),nameFromRoot());
    }
  //TODO: Doc infoTypeDep(){}//have a list of docs inside; the L42 version may just unwrap the docs
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
  final CoreL currentL;
  final List<NC> publicNCs;
  final List<MWT> publicMWTs;
  final List<T> publicTs;
  final CoreL rootL;
  final L42£Name nameFromRoot;
  final P _classAny;
  private L42£Nested(String position,CoreL currentL,CoreL rootL,L42£Name nameFromRoot,P _classAny){
    this.position=position;
    this.currentL=currentL;
    this.rootL=rootL;
    this.nameFromRoot=nameFromRoot;
    this._classAny=_classAny;
    this.publicNCs=L(currentL.ncs().stream().filter(m->!m.key().hasUniqueNum()));
    this.publicMWTs=L(currentL.mwts().stream().filter(m->!m.key().hasUniqueNum()));
    this.publicTs=L(currentL.ts().stream().filter(m->!m.p().hasUniqueNum()));
    }
  private Program myP(){
    if(this.isBinded()){
      if(!this._classAny.isNCs()){return Program.emptyP;}
      var res=Resources.currentP._navigate(this._classAny.toNCs());
      assert res!=null;
      return res;
      }
    var root=Resources.currentP.push(Resources.currentC,this.rootL);
    return root.navigate(this.nameFromRoot.cs);
    }
  public String isCoherent() {
    try{new Coherence(myP(),false).isCoherent(false);}
    catch(EndError e) {return e.getMessage();}
    return "";
    }
  public String toFullString(){
    /*var v=new FullS();
    this.currentL.accept(v);
    return v.result().toString();*/
    return OverviewVisitor.makeOverview(this.currentL,false);
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
  public static final L42Cache<L42£Nested> myCache=new EqCache<>(TrustedKind.Nested);
  @Override public L42Cache<L42£Nested> myCache(){return myCache;}
  static public L42£Nested instanceAny=new L42£Nested("",Program.emptyL,Program.emptyLInterface,L42£Name.empty,P.pAny).myNorm();
  static public L42£Nested instanceLibrary=new L42£Nested("",Program.emptyL,Program.emptyL,L42£Name.empty,P.pVoid).myNorm();
  static public L42£Nested instanceVoid=new L42£Nested("",Program.emptyL,Program.emptyL,L42£Name.empty,P.pLibrary).myNorm();
  @Override public L42£Nested newInstance(){return instanceVoid;}
  }
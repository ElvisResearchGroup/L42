package is.L42.introspection;

/*

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
    
    L.from(class=,name=)  L.from(library=,name=)
      fromClassAny+Cs, fromLibrary+Cs
    l.nested: libs
    l.methods: methods
    l.infoXX: docs
    l.infoImplementsPrivate:Bool
    l.isInterface  
    l.implemented: types
    l.root : l
    l.nameFromOrigin
    l.docs : docs
    l.docsOfNested:docs
    l.pos:poss
    l.isBinded : Bool
      l.classAny : class Any

    m.parameters: types
    m.return: type
    m.exceptions:types
    m.root : l
    m.nameFromOrigin
    m.docs : docs
    m.pos:poss

    t.mdf:Mdf
    f.docs: docs
    t.refTo: l

    pos, doc, l,m,t
    poss docs ls, ms,ts
    name implemented as native pathSel?
      

Doc
Lib
Method
Path
  all have root Lib and pathsel

Pos
AbsolutePath/ClassAny
  do not?
  
Lib: isBinded, rootLib, pathFromRoot
  fromClassAny+Cs, fromLibrary+Cs, fromType+Cs
  nesteds
  methods
  infoXX
  ...
  infoXX
  isInterface
  implemented
  infoImplementsPrivate
  Doc
  ListPos
  isEnsuredCoherent ?? 

class Util{ 
  static TypeRefTo refTo(Program p,P path,List<C> location,L root) {
    if(!path.isNCs()){return new TypeRefTo.Binded(path);}
    var whereP=P.of(0,location);
    var p0=p.from(path.toNCs(),whereP);
    if (p0.n()==0){return new TypeRefTo.Lib(root,path);}
    p0=p0.n(p0.n()-1);
    try{
      ClassB cb=p.extractClassB(path);
      if(cb.getPhase().subtypeEq(Phase.Typed)){//typed,coherent
        return new TypeRefTo.Binded(path);
        }
  //else, phase is none but cb available and not typed yet
  return new TypeRefTo.Unavailable("Unavailable path: "+path+"; code not typed yet.");
  }
catch(ErrorMessage.PathMetaOrNonExistant pne){
  if (pne.isMeta()){return new TypeRefTo.Unavailable("Unavailable path: "+path+"; code not generated yet.");}
  return new TypeRefTo.Missing(path.toString());
  }
  }
  
}
//should reuse core Pos and Doc?
class Origin {
  public Origin(String fileName, int line,  int column) {
    this.fileName = fileName;
    this.line = line;
    this.column = column;
    }
  //public boolean equalequal(Object that){return this.equals(that);}
  public String toS(){
    return "Origin:"+fileName+"\nline " + line+ "; colum " + column;
    }
  final String fileName;
  final int line;
  final int column;
  }

class Doc{
  
  public Doc(Ast.Doc inner,Location location) {super(inner,location);}
  public int annotationSize(){
    return inner.getAnnotations().size();}
  
  public Annotation annotation(PData pData,int that) throws NotAvailable{
    Object titleObj = Location.listAccess(inner.getAnnotations(), that);
    String text = Location.listAccess(inner.getParameters(), that);
    TypeRefTo title;
    if(titleObj instanceof String){
      title=new TypeRefTo.Missing((String)titleObj);
      }
    else{
      Path path=(Path)titleObj;
      Lib lib=locationLib();
      title= Location.refTo(pData.p,path,lib.path,lib.root());
      }
    return new Annotation(title,text);
    }
  public Lib locationLib(){
    Location l=location();
    while (!(l instanceof Lib)){l=l.location();}
    return (Lib)l;
    }
  public static class Annotation{
    public TypeRefTo title() {return title;}
    public String text() {return text;}
    public Annotation(TypeRefTo key, String text) {
    super();
    this.title = key;
    this.text = text;
    }
    TypeRefTo title; 
    String text;
    //public boolean equalequal(Object that){return this.equals(that);}
    public String toS(){
      return "Annotation:@"+title+" "+text;
      }
    //Generated
    @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((title == null) ? 0 : title.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      return result;
      }
    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass())return false;
      Annotation other = (Annotation) obj;
      if (title == null) {
        if (other.title != null) return false;
      } else if (!title.equals(other.title))return false;
      if (text == null) {
        if (other.text != null) return false;
      } else if (!text.equals(other.text)) return false;
      return true;
      }
    
    }
  public String toS() {return inner.getS();}
  @Override public Doc doc() {return this;}
  //@Override public boolean equalequal(Object that) {return this.equals(that);}
  //Note: equals and hash code overriden "enough" in the super class
  }
  
  
  public class Lib extends Location.LocationImpl<ExpCore.ClassB,Lib>{
  boolean isBinded;
  ExpCore.ClassB root;
  List<Ast.C> path;
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    List<Ast.C> path,
    ExpCore.ClassB inner,Lib location) {super(inner,location);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      }
  public Lib(
    boolean isBinded,
    ExpCore.ClassB root,
    List<Ast.C> path,
    ExpCore.ClassB inner) {super(inner,null);
      this.isBinded=isBinded;
      this.root=root;
      this.path=path;
      this.location=this;
      }
  public static Lib newFromClass(PData pData,Path path){
    return newFromClassP(pData.p,path);
    }
  public static Lib newFromClassP(Program p,Path path){
    if(path.isPrimitive()){return new LibPrimitive(path);}
    ClassB cb=p.extractClassB(path);//it seams does not need from??
    Lib lib=new Lib(true,cb,Collections.emptyList(),cb);
    return lib;
    } 
  public static Lib newFromLibrary(ExpCore.ClassB cb){
    Lib lib=new Lib(false,cb,Collections.emptyList(),cb);
    return lib;
    } 
  Cacher<List<Lib>> nestedsC=new Cacher<List<Lib>>(){public List<Lib> cache(){
    if(Lib.this.isBinded){
      return inner.ns().stream()
        .map(n->new Lib(true,(ExpCore.ClassB)n.getInner(),Collections.emptyList(),(ExpCore.ClassB)n.getInner()))
        .collect(Collectors.toList());
      }
    return inner.ns().stream()
      .map(n->new Lib(false,root,Functions.push(path,n.getName()),(ExpCore.ClassB)n.getInner(),Lib.this))
      .collect(Collectors.toList());
    }};
  public int nestedSize(){return nestedsC.get().size();}
  public Lib nested(int that) throws NotAvailable{return Location.listAccess(nestedsC.get(), that);}

  Cacher<List<Method>> methodsC=new Cacher<List<Method>>(){public List<Method> cache(){
  return inner.mwts().stream()
    .map(mwt->new Method(mwt,Lib.this))
    .collect(Collectors.toList());}};
  public int methodSize(){return methodsC.get().size();}
  public Method method(int that) throws NotAvailable{return Location.listAccess(methodsC.get(), that);}

  Cacher<List<Type.Implemented>> implementedsC=new Cacher<List<Type.Implemented>>(){public List<Type.Implemented> cache(){
  List<Type.Implemented> res=new ArrayList<>();
  {int i=-1; for(Ast.Type ti:inner.getSupertypes()){i+=1; //starts from 0
    res.add(new Type.Implemented(i,ti,inner,Lib.this));
  }}
  return res;
  }};
  public int implementedSize(){return implementedsC.get().size();}
  public Type.Implemented implemented(int that) throws NotAvailable{return Location.listAccess(implementedsC.get(), that);}

  @Override public Doc doc(){return new Doc(inner.getDoc1(),this);}
  
  public boolean isInterface(){return this.inner.isInterface();}
  public boolean isBinded(){return this.isBinded;}
  private static boolean isRedirectableRec(ClassB cb){
    boolean res=!cb.mwts().stream().anyMatch(mwt->
    mwt.get_inner()!=null 
    || mwt.getMs().isUnique()
    );
    if(!res){return false;}
    res=cb.ns().stream().allMatch(nc->isRedirectableRec((ClassB)nc.getInner()));
    return res;
    }
  public boolean isRedirectable(){
    return isRedirectableRec(this.inner);
    }//deep, all abs, no private
  public boolean isPotentialInterface(){
    //freeTemplate must mean no class methods!! old idea (class methods not called) does not seams to make sense, if not called, then could be just not having them...
    //technically you can still have class NormType variables and use the method over those, but it seams like a minor case
    return !this.inner.mwts().stream().anyMatch(mwt->
      mwt.get_inner()!=null 
      || mwt.getMs().isUnique()
      || mwt.getMt().getMdf()==Mdf.Class);
    }//shallow for being sum with interface or redirectable to interface
  public boolean isCloseState(){
    return this.inner.mwts().stream().anyMatch(mwt->
      mwt.get_inner()==null 
      && mwt.getMs().isUnique());
    }
  public boolean isEnsuredCoherent(PData pData){
    if (this.inner.getPhase()==Phase.Coherent){return true;}
    if(this.isBinded){return false;}
    return newTypeSystem.TsLibrary.coherent(pData.p.evilPush(this.inner), false);
    
  }
  //either is labeled coherent, (may be binded)
  //or not binded and TsLibrary.coherent(pData.p.evilPush(this.inner), false);
  //in a world with the current subtype knowledge: it may give a false negative for incomplete subtyping information
  
  public Lib root(){
    if(this.root==this.inner){return this;}
    assert !this.isBinded;
    return new Lib(false,root,Collections.emptyList(),root);
    }
  public List<ast.Ast.C> path(){
    return path;
    }  
  public Doc nestedDoc(){
    if(this.path.isEmpty()){
      return new Doc(Ast.Doc.empty(),this);
      }
    NestedClass nc = this.root.getNested(path);
    return new Doc(nc.getDoc(),this);
    }//empty doc if it is root
//even if obtained with a classObj, no method to get it back
//to get a nested classObj, Refactor.navigateClassObj(classAny,Path)->classAny??
  public String toS() {return sugarVisitors.ToFormattedText.of(inner);}
   public Lib navigate(List<Ast.C> cs){
    return navigateCs(cs);
    }
  
   public Lib navigateCs(List<Ast.C> cs){
    if (cs.isEmpty()){return this;}
    if(this.isBinded){
      ClassB cb=this.inner.getClassB(cs);//TODO: need from?
      return new Lib(true,cb,Collections.emptyList(),cb);
      }
    List<Ast.C> top = Collections.singletonList(cs.get(0));
    List<Ast.C> tail=cs.subList(1,cs.size());
    Lib nextStep=new Lib(false,root,Functions.push(path,cs.get(0)),inner.getClassB(top),this);
    return nextStep.navigateCs(tail);
    }
  //@Override public boolean equalequal(Object that) {return this.equals(that);}
  }
  public class LibPrimitive extends Lib{
  Path primitive;
  public LibPrimitive(Path primitive){
    super(true,null,null,null);
    this.primitive=primitive;
    }
  public int nestedSize(){return 0;}
  public LibPrimitive nested(int that) throws NotAvailable{throw new NotAvailable();}
  public int methodSize(){return 0;}
  public Method method(int that) throws NotAvailable{throw new NotAvailable();}
  public int implementedSize(){return 0;}
  public Type.Implemented implemented(int that) throws NotAvailable{throw new NotAvailable();}
  @Override public Doc doc(){return new Doc(Ast.Doc.empty(),this);}
  public boolean isInterface(){return primitive==Path.Any();}
  public boolean isRedirectable(){return true;}
  public boolean isPotentialInterface(){return true;}
  public boolean isCloseState(){return false;}
  public boolean isEnsuredCoherent(PData pData){return true;}
  public Lib root(){return this;}
  public List<ast.Ast.C> path(){return Collections.emptyList();}
  public Doc nestedDoc(){return new Doc(Ast.Doc.empty(),this);}
  public String toS() {return "{/ *PrimitivePath:"+primitive+"* /}";}
  @Override public boolean equals(Object that) {
    if(this.getClass()!=that.getClass()){return false;}
    return this.primitive==((LibPrimitive)that).primitive;
    }
  @Override public int hashCode() {return primitive.hashCode();}
  }
  
  public class Method extends Location.LocationImpl<ClassB.MethodWithType, Lib>{
  public Method(MethodWithType inner, Lib location) {
    super(inner, location);
    }
  public static Method of(MethodWithType inner,ClassB top,List<Ast.C>cs){
    return new Method(inner,Lib.newFromLibrary(top).navigateCs(cs));
    }
  public static Method of(MethodWithType inner,Program p,Path path){
    return new Method(inner,Lib.newFromClassP(p, path));
    }
  public boolean isAbstract(){return inner.get_inner()==null;}
  public boolean isRefine(){return inner.getMt().isRefine();}
  public Ast.MethodSelector  selector(){return inner.getMs();}
  public String toS() {return sugarVisitors.ToFormattedText.of(this.inner);}
  @Override public Doc doc() {return new Doc(inner.getDoc(),this);}

  public Type.Return returnType(){return new Type.Return(inner.getMt().getReturnType(),inner,this);}
    
  Cacher<List<Type.Parameter>> parametersC=new Cacher<List<Type.Parameter>>(){public List<Type.Parameter> cache(){
    MethodType mt = inner.getMt();
    Ast.Type thisT=new Ast.Type(mt.getMdf(),Path.outer(0),Ast.Doc.empty());
    List<Type.Parameter> res=new ArrayList<>();
    res.add(new Type.Parameter(0,thisT,inner,Method.this));
    {int i=0; for(Ast.Type ti:mt.getTs()){i+=1; //starts from 1
      res.add(new Type.Parameter(i,ti,inner,Method.this));
    }}
    return res;
    }};
 
  public int parameterTypeSize(){return parametersC.get().size();}
  public Type.Parameter parameterType(int that) throws NotAvailable{return Location.listAccess(parametersC.get(), that);}
  

  Cacher<List<Type.Exception>> exceptionsC=new Cacher<List<Type.Exception>>(){public List<Type.Exception> cache(){
  MethodType mt = inner.getMt();
  List<Type.Exception> res=new ArrayList<>();
  {int i=-1; for(Ast.Type ti:mt.getExceptions()){i+=1; //starts from 0
    res.add(new Type.Exception(i,ti,inner,Method.this));
  }}
  return res;
  }};
  public int exceptionTypeSize(){return exceptionsC.get().size();}
  public Type.Exception exceptionType(int that) throws NotAvailable{
    return Location.listAccess(exceptionsC.get(), that);}
  //@Override public boolean equalequal(Object that) {return this.equals(that);}
  }

  public interface Type extends Location{
  Ast.Type type();
  Lib locationLib();
  default TypeRefTo refTo(PData pData) {
    return Location.refTo(pData.p,type().getPath(), locationLib().path, locationLib().root());
    }
  default Doc doc() {return new Doc(type().getDoc(),this);}
  default String toS(PData p) {
    return (type().getMdf().inner+" "+this.refTo(p).toS()).trim();
    }
  default int mdfS() {return type().getMdf().ordinal();}


  static class Return extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Return(Ast.Type type, MethodWithType inner, Method location) {
      super(inner, location);
      this.type=type;
      }
    Ast.Type type;
    @Override public Ast.Type type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    //@Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Return other = (Return) obj;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  
  static class Parameter extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Parameter(int pos, Ast.Type type,MethodWithType inner, Method location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.Type type;
    @Override public Ast.Type type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    //@Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Parameter other = (Parameter) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  static class Exception extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Exception(int pos, Ast.Type type,MethodWithType inner, Method location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.Type type;
    @Override public Ast.Type type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    //@Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Exception other = (Exception) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  static class Implemented extends Location.LocationImpl<ExpCore.ClassB, Lib> implements Type{
    public Implemented(int pos, Ast.Type type,ExpCore.ClassB inner, Lib location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }  
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.Type type;
    @Override public Ast.Type type(){return type;}
    @Override public Lib locationLib(){return location();}
    //@Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Implemented other = (Implemented) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }            
  }
public interface TypeRefTo {
  //default boolean equalequal(Object that) {return this.equals(that);}
  String toS();//toS is the full path with Thisn.
  static class Lib implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    result = prime * result + ((root == null) ? 0 : root.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Lib other = (Lib) obj;
    if (path == null) {
    if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
        return false;
    if (root == null) {
    if (other.root != null)
        return false;
    } else if (!root.equals(other.root))
        return false;
    return true;
    }
    public Lib(is.L42.connected.withSafeOperators.location.Lib root, Path path) {
      this.root = root;
      this.path = path;
      }
    is.L42.connected.withSafeOperators.location.Lib root;//to make referredLib() cheaper, we may cache it, but it may not be a good idea...
    Ast.Path path; //referring inside the Lib
    public is.L42.connected.withSafeOperators.location.Lib referredLib(){
      return root.navigateCs(path.getCBar());
      }    
    @Override public String toS() {
      return PathAux.as42Path(path.getCBar());
      }
    }
  static class Unavailable implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((repr == null) ? 0 : repr.hashCode());
    return result;
    }

    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Unavailable other = (Unavailable) obj;
    if (repr == null) {
    if (other.repr != null)
        return false;
    } else if (!repr.equals(other.repr))
        return false;
    return true;
    }

    String repr;
    @Override public String toS() {return repr; }

    public Unavailable(String repr){this.repr=repr;}
    }
  static class Binded implements TypeRefTo{//includes primitives
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Binded other = (Binded) obj;
    if (path == null) {
    if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
        return false;
    return true;
    }
    public Binded(Path path) {
      this.path = path;
      }
    Ast.Path path;//pre frommed to be ok with PData
    public Ast.Path referredClassObj(){
      return path;
      }
    public static boolean equalsClassObj(Ast.Path that,Ast.Path and){
      return that.equals(and);
      }
    @Override public String toS() {return path.toString(); }
    }
  static class Missing implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((repr == null) ? 0 : repr.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Missing other = (Missing) obj;
    if (repr == null) {
    if (other.repr != null)
        return false;
    } else if (!repr.equals(other.repr))
        return false;
    return true;
    }
    String repr;//if was Path, pre frommed to be ok with PData
    //in case of docs, can just be the doc @string
    public Missing(String repr){this.repr=repr;}
    @Override public String toS() {return repr; }

    //mostly useful for docs, where we can use lowercase annotation
    //or we may want to preserve @P where we removed the P
    //also, if an (nested) Uncompiled is resolved not existing..        
    }
  }
*/
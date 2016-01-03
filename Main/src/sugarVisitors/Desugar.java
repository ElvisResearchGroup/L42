package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import platformSpecific.fakeInternet.OnLineCode;
import privateMangling.PrivateHelper;
import tools.Assertions;
import ast.Ast;
import ast.Ast.BlockContent;
import ast.Ast.Catch;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodSelectorX;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.On;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Position;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.VarDec;
import ast.Ast.VarDecCE;
import ast.Ast.VarDecE;
import ast.Ast.VarDecXE;
import ast.ExpCore;
import ast.Expression;
import ast.Expression.BinOp;
import ast.Expression.ClassB;
import ast.Expression.ClassB.Member;
import ast.Expression.ClassB.MethodImplemented;
import ast.Expression.ClassB.MethodWithType;
import ast.Expression.ClassB.NestedClass;
import ast.Expression.ClassReuse;
import ast.Expression.CurlyBlock;
import ast.Expression.DocE;
import ast.Expression.FCall;
import ast.Expression.If;
import ast.Expression.Literal;
import ast.Expression.Loop;
import ast.Expression.MCall;
import ast.Expression.RoundBlock;
import ast.Expression.SquareCall;
import ast.Expression.SquareWithCall;
import ast.Expression.UnOp;
import ast.Expression.UseSquare;
import ast.Expression.Using;
import ast.Expression.While;
import ast.Expression.With;
import ast.Expression.X;
import ast.Expression._void;
import ast.Util.CachedStage;
import ast.Util.PathMxMx;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Program;
import coreVisitors.CollectPrivateNames;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import coreVisitors.Visitor;
import facade.Configuration;
import facade.L42;
public class Desugar extends CloneVisitor{
  public static Expression of(Expression e){
    if(L42.path!=null){e=ReplaceDots.of(L42.path, e);}
    e=DesugarW.of(e);
    Desugar d=new Desugar();
    //understand what is the current folder
    //replace ... recursively
    //replaceDots(currentFolder,e)-> clone visitor
    d.collectAllUsedLibs(e);//collect all classBreuse in a map url->core version
    L42.usedNames.addAll(CollectDeclaredClassNamesAndMethodNames.of(e));
    d.usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(e));
    d.renameAllPrivatesInUsedLibs();
    Expression result= e.accept(d);
    assert Functions.checkCore(result);
    return result;
  }
  private void renameAllPrivatesInUsedLibs() {
    for(String s: this.importedLibs.keySet()){
      this.importedLibs.put(s,renameAllPrivatesInUsedLibs(s,this.importedLibs.get(s)));
    }

  }
  private ast.ExpCore.ClassB renameAllPrivatesInUsedLibs(String libName,ExpCore.ClassB classB) {
    int cutPoint=libName.lastIndexOf("/");
    assert cutPoint!=-1;
    char first=libName.charAt(cutPoint+1);
    if(Path.isValidPathStart(first)){
    	return PrivateHelper.updatePrivateFamilies(classB);
    	}//we assume is going to be
    // -desugared, well typed and with private names normalized //TODO: do we want to check?
    /*if(!IsCompiled.of(classB)){return classB;}//TODO: just to make test easier, should be an error in production
   //else, use the old, buggy name rename, to remove later
    //collect private names
    CollectPrivateNames cpn=CollectPrivateNames.of(classB);
    //rename all
    Program emptyP=Program.empty();
    //classB=IntrospectionAdapt.sealConstructors(emptyP,classB,cpn.mapConstructors);
    //cpn.mapMx=IntrospectionAdapt.expandMapMx(emptyP,classB,cpn.mapMx);
    List<PathMxMx> mapMx = ConsistentRenaming.makeMapMxConsistent(classB,cpn.mapMx);
    classB=IntrospectionAdapt.applyMapMx(emptyP,classB,mapMx);
    classB=IntrospectionAdapt.applyMapPath(emptyP,classB,cpn.mapPath);
    */return classB;
  }
  private void collectAllUsedLibs(Expression e) {
    e.accept(new CloneVisitor(){
      @Override
      public Expression visit(ClassReuse s) {
        ClassB data = OnLineCode.getCode(s.getUrl());
        L42.usedNames.addAll(CollectDeclaredClassNamesAndMethodNames.of(data));
        ast.ExpCore.ClassB dataCore=(ast.ExpCore.ClassB) data.accept(new InjectionOnCore());
        Configuration.typeSystem.computeStage(Program.empty()/*.addAtTop(dataCore)*/,dataCore);
        dataCore.accept(new coreVisitors.CloneVisitor(){
          @Override public ExpCore visit(ExpCore.ClassB cb){ 
            CachedStage stage=cb.getStage();
            assert stage!=null;
            stage.setVerified(true);//TODO add more cached info?
            return super.visit(cb);
            }
        });
        Desugar.this.importedLibs.put(s.getUrl(),dataCore);
        return super.visit(s);
      }
    });
  }

  HashMap<String,ast.ExpCore.ClassB> importedLibs=new HashMap<>();
  Set<String> usedVars=new HashSet<String>();
  ArrayList<ClassB> p=new ArrayList<ClassB>();
  Type t=new NormType(Mdf.Immutable,Path.Void(),Ph.None);
  HashMap<String,Type> varEnv=new HashMap<String,Type>();

  public Expression visit(RoundBlock s) {
    //assert L42.checkWellFormedness(s);
    s=blockContentSepare(s);
    s=blockEtoXE(s);
    s=blockInferVar(s);
    s=blockVarClass(s);
    HashMap<String, Type> oldVarEnv = new HashMap<String, Type>(varEnv);
    try{
      if(!s.getContents().isEmpty()){addAllDec(s.getContents().get(0).getDecs());}
      Expression result= super.visit(s);
      return result;
    }
    finally{varEnv=oldVarEnv;}
  }
  private void addAllDec(List<VarDec> decs) {
    for(VarDec _dec:decs){
      if(!(_dec instanceof VarDecXE)){continue;}
      VarDecXE dec=(VarDecXE)_dec;
      assert dec.getT().isPresent();
      this.varEnv.put(dec.getX(), dec.getT().get());
    }
  }
  private RoundBlock blockContentSepare(RoundBlock s) {
    if(s.getContents().size()<=1){return s;}
    List<BlockContent> ctxTop = new ArrayList<>(s.getContents().subList(0,1));
    List<BlockContent> ctxPop = new ArrayList<>(s.getContents().subList(1, s.getContents().size()));
    RoundBlock next=blockContentSepare(s.withContents(ctxPop));
    return s.withContents(ctxTop).withInner(next);
  }
  private RoundBlock blockEtoXE(RoundBlock s) {
    if(s.getContents().isEmpty()){return s;}
    List<VarDec> decs = s.getContents().get(0).getDecs();
    List<VarDec> newDecs =new ArrayList<VarDec>();
    for(VarDec _dec:decs){
      if(!(_dec instanceof VarDecE)){
        newDecs.add(_dec);continue;}
      VarDecE dec=(VarDecE)_dec;
      String x=Functions.freshName("unused", usedVars);
      //usedVars.add(x);
      VarDecXE newXE=new VarDecXE(false,Optional.of(new NormType(Mdf.Immutable,Path.Void(),Ph.None)),x,dec.getInner());
      newDecs.add(newXE);
      }
    RoundBlock result = blockWithDec(s,newDecs);
    //assert L42.checkWellFormedness(s);
    //assert L42.checkWellFormedness(result);
    return result;
  }

  private RoundBlock blockWithDec(RoundBlock s, List<VarDec> decs) {
    assert s.getContents().size()==1: s.getContents().size();
    List<BlockContent> ctx = new ArrayList<>();
    ctx.add(new BlockContent(decs, s.getContents().get(0).get_catch()));
    return s.withContents(ctx);
  }
  private int firstVar(List<VarDec> varDecs){
    {int i=-1;
    for(VarDec _dec:varDecs){i+=1;
      if(!(_dec instanceof VarDecXE)){continue;}
      VarDecXE dec=(VarDecXE)_dec;
      assert dec.getT().isPresent();
      if(!dec.isVar()){continue;}
      return i;
      }}
    return -1;
    }
  private RoundBlock blockVarClass(RoundBlock s) {
   RoundBlock res=_blockVarClass(s);
   if(res.equals(s)){return res;}
   return blockVarClass(res);
  }
  private RoundBlock _blockVarClass(RoundBlock s) {
    if(s.getContents().isEmpty()){return s;}
    List<VarDec> varDecs = new ArrayList<VarDec>(s.getContents().get(0).getDecs());
    int pos=firstVar(varDecs);
    if(pos==-1){return s;}
    VarDecXE varDec=(VarDecXE)varDecs.get(pos);
    varDecs.set(pos,varDec.withVar(false));
    VarDecCE ce=getClassBForVar(varDec);
    VarDecXE d=getDecForVar(ce.getInner().getName(),varDec);
    X x=new X(varDec.getX());
    X z=new X(d.getX());
    int d3First=findD2(x,pos,varDecs);
    RoundBlock fake = getFakeBlock(s,x,z,s, varDecs, d3First);
    List<VarDec> trueDecs=new ArrayList<VarDec>();
    trueDecs.add(ce);
    if(d3First!=-1){trueDecs.addAll(varDecs.subList(0, d3First));}
    else{trueDecs.addAll(varDecs);}
    trueDecs.add(d);
    trueDecs.addAll(fake.getContents().get(0).getDecs());
    List<BlockContent> trueContent=new ArrayList<BlockContent>();
    trueContent.add(new BlockContent(trueDecs,fake.getContents().get(0).get_catch()));
    return fake.withContents(trueContent);
    }
  private RoundBlock getFakeBlock(Expression src,X x, X z,RoundBlock s, List<VarDec> varDecs,
      int d3First) {
    List<BlockContent> fakeContent=new ArrayList<BlockContent>();
    List<VarDec> fakeDecs=new ArrayList<VarDec>();
    if(d3First!=-1){//d3 not empty
      fakeDecs.addAll(varDecs.subList(d3First, varDecs.size()));
    }
    fakeContent.add(new BlockContent(fakeDecs,s.getContents().get(0).get_catch()));
    RoundBlock fake=new RoundBlock(s.getP(),s.getDoc(),s.getInner(),fakeContent);
    fake=(RoundBlock) XEqOpInZEqOp.of(x, z, fake);
    fake=(RoundBlock) XInE.of(x,getMCall(getPosition(src),z,"#inner", getPs()),fake);
    return fake;
  }

  private int findD2(X x,int pos, List<VarDec> decs) {
    for(VarDec _dec:decs.subList(pos+1,decs.size())){
      pos+=1;
      if(!(_dec instanceof VarDecXE)){continue;}
      VarDecXE dec=(VarDecXE)_dec;
      boolean isAssigned=Exists.of(dec.getInner(),e->{
        if(!(e instanceof BinOp)){return false;}
        BinOp bo=(BinOp)e;
        if(bo.getOp().kind!=Ast.OpKind.EqOp){return false;}
        return bo.getLeft().equals(x);
      });
      if (isAssigned){return pos;}
    }
    return -1;
  }
  private VarDecXE getDecForVar(String cName,VarDecXE varDec) {
    NormType nt=new NormType(Mdf.Mutable,Path.outer(0).pushC(cName),Ph.None);
    FCall right=new FCall(getPosition(varDec.getInner()),nt.getPath(),Doc.empty(), getPs("inner",new X(varDec.getX())));
    String nameZ=Functions.freshName(nt.getPath(), usedVars);
    //usedVars.add(nameZ);
    return new VarDecXE(false,Optional.of(nt),nameZ,right);
  }

  private VarDecCE getClassBForVar(VarDecXE varDec) {
    List<FieldDec> fs=new ArrayList<FieldDec>();
    fs.add(new FieldDec(true,_computeTypeForClassBForVar(varDec),"inner",Doc.empty()));
    ClassB cb=new ClassB(Doc.empty(),Doc.empty(),new Ast.ConcreteHeader(Mdf.Mutable, "",fs,Position.noInfo),Collections.emptyList(),Collections.emptyList(),Stage.None);
    String nameC=Functions.freshName("Var"+varDec.getX(), L42.usedNames);
    //usedCnames.add(nameC);
    return new VarDecCE(new NestedClass(Doc.getPrivate(),nameC,cb,null));
  }
  public Type _computeTypeForClassBForVar(VarDecXE varDec) {
    Type t=varDec.getT().get();
    t=t.match(
      nt->nt.withPath(computeTypeForClassBForVar(nt.getPath())),
      h -> h.withPath(computeTypeForClassBForVar(h.getPath()))
      );
    return t;
  }
  private Path computeTypeForClassBForVar(Path path) {
    if (path.isPrimitive()){return path;}
    if (!path.isCore()){return path;}
    return path.setNewOuter(path.outerNumber()+1);
    //return path;
    }
  private RoundBlock blockInferVar(RoundBlock s) {
    if(s.getContents().isEmpty()){return s;}
    HashMap<String, Type> localVarEnv = new HashMap<String, Type>(this.varEnv);
    List<VarDec> newDecs =new ArrayList<VarDec>();
    for(VarDec _dec:s.getContents().get(0).getDecs()){
      if(!(_dec instanceof VarDecXE)){
        newDecs.add(_dec);continue;}
      VarDecXE dec=(VarDecXE)_dec;
      if(dec.getT().isPresent()){
        localVarEnv.put(dec.getX(),dec.getT().get());
        newDecs.add(dec);
        continue;
        }
      Type t=GuessType.of(dec.getInner(),localVarEnv);
      localVarEnv.put(dec.getX(),t);
      newDecs.add(dec.withT(Optional.of(t)));
      }
    return blockWithDec(s, newDecs);
  }


  public Expression visit(ClassB s) {
    if(s.getH() instanceof ConcreteHeader){
      List<Member> ms = Desugar.cfType((ConcreteHeader)s.getH(),s.getDoc2());
      ms.addAll(s.getMs());
      s=s.withDoc2(Doc.empty()).withMs(ms).withH(new Ast.TraitHeader());
    }
    Set<String> oldUsedVars = this.usedVars;
    HashMap<String, Type> oldVarEnv = this.varEnv;
    try{
      p.add(0,s);
      s=(ClassB)super.visit(s);
      //TODO: it can loop, somehow!
      s=FlatFirstLevelLocalNestedClasses.of(s,this);
      s=DesugarCatchDefault.of(s);
      return s;}
    finally{
      //this.usedCnames=oldUsedCNames;->L42.usedNames//TODO: use new visitor objects! no? how to do with inheritance? new this?
      this.usedVars=oldUsedVars;
      this.varEnv=oldVarEnv;
      p.remove(0);
      }
  }
  public Expression visit(ClassReuse s) {
    ClassB res=s.getInner();//lift(s.getInner());
    { ArrayList<ClassB> oldP = this.p;
      this.p=new ArrayList<ClassB>();
      try{res=(ClassB) res.accept(this);}
      finally{this.p=oldP;}
    }
  //ClassB reused2=OnLineCode.getCode(s.getUrl());
    ExpCore.ClassB reused=this.importedLibs.get(s.getUrl());
    assert reused!=null:s.getUrl()+" "+this.importedLibs.keySet()+this.importedLibs.get(s.getUrl())+this.importedLibs;
    for(ast.ExpCore.ClassB.Member m1:reused.getMs()){
      for(Member m2:res.getMs()){
        m1.match(
            nc1->{return m2.match(nc2->{
               if (nc1.getName().equals(nc2.getName())){throw new ast.ErrorMessage.NotWellFormedMsk(s, s, "Nested class \""+nc1.getName()+"\" already present in reused library "+s.getUrl());} 
              return null;}, mi2->null, mt2->null);},
            mi1->{return m2.match(nc2->null,mi2->{
              if (mi1.getS().equals(mi2.getS())){throw new ast.ErrorMessage.NotWellFormedMsk(s, s, "Method implemented \""+mi1.getS()+"\" already present in reused library "+s.getUrl());} 
             return null;},  mt2->null);},
            mt1->{return m2.match(nc2->null,mi2->null,mt2->{
              if (mt1.getMs().equals(mt2.getMs())){throw new ast.ErrorMessage.NotWellFormedMsk(s, s, "Method with type \""+mt1.getMs()+"\" already present in reused library "+s.getUrl());} 
             return null;});}
            );
        //NotWellFormedMsk
      }
    }
    return new ClassReuse(res,s.getUrl(),reused);
  }


  public Expression visit(Path s) {
    if(s.isCore()|| s.isPrimitive()){return s;}
    List<String> rd = new ArrayList<String>(s.getRowData());
    String key=rd.get(0);
    int index = searchForScope(key);
    if (index==-1){index=0;}//to simplify testing
    rd.add(0,"Outer"+index);
    return new Path(rd);
    }

  private int searchForScope(String key) {
    int index=-1;
    for(ClassB cb:p){
      index+=1;
      //if (cb.getH())
      for(Member m:cb.getMs()){
        if(!(m instanceof NestedClass)){continue;}
        NestedClass nc=(NestedClass)m;
        if(!nc.getName().equals(key)){continue;}
        return index;
      }
    }
    return index;
  }
  protected Parameters liftPs(Parameters ps) {
    if(!ps.getE().isPresent()){return liftPsPropagate(ps);}
    List<String> xs = new ArrayList<String>(ps.getXs());
    List<Expression> es =new ArrayList<Expression>(ps.getEs());
    xs.add(0,"that");
    es.add(0,ps.getE().get());
    return liftPsPropagate(new Parameters(Optional.empty(),xs,es));
  }
  private Parameters liftPsPropagate(Parameters ps) {
    assert !ps.getE().isPresent();
    List<Expression> es = new ArrayList<Expression>();
    {int i=-1;for(Expression ei:ps.getEs()){i+=1;
      Type ti = expectedTypeFor(ps.getXs().get(i));
      es.add(withExpectedType(ti,()->lift(ei)));
    }}
    return new Parameters( Optional.empty(), ps.getXs(),   es);
  }
  public Type expectedTypeFor(String x) {
    if(this.t instanceof NormType){return this.t;}
    List<MethodSelectorX> sel = new ArrayList<>(((Ast.HistoricType)this.t).getSelectors());
    MethodSelector ms=sel.get(sel.size()-1).getMs();
    sel.set(sel.size()-1,new MethodSelectorX(ms,x));
    Type ti=((Ast.HistoricType)this.t).withSelectors(sel);
    return ti;
  }
  public Expression visit(While s) {
    Position p=getPosition(s.getThen());
    Expression cond=Desugar.getMCall(p,s.getCond(), "#checkTrue",Desugar.getPs());
    RoundBlock b=Desugar.getBlock(p,cond,s.getThen());
    Loop l=new Loop(b);
    NormType _void=new NormType(Mdf.Immutable,Path.Void(),Ph.None);
    Catch k=Desugar.getK(SignalKind.Exception, "",_void,  new _void());
    RoundBlock b2=Desugar.getBlock(p,l,k,new _void());
    return b2.accept(this);
  }
  public Expression visit(If s) {
    if(!s.get_else().isPresent()){
      return visit(s.with_else(Optional.of(new _void())));
    }
    Position p=getPosition(s.getThen());
    if(!(s.getCond() instanceof Ast.Atom)){
      String x=Functions.freshName("cond", usedVars);
      //usedVars.add(x);
      return visit(getBlock(p,x, s.getCond(),s.withCond(new X(x))));
    }
    MCall check=getMCall(p,s.getCond(),"#checkTrue", getPs());
    return visit(getBlock(p,check,getK(SignalKind.Exception,"",new NormType(Mdf.Immutable,Path.Void(),Ph.None),s.get_else().get()),s.getThen()));
  }

  static Parameters getPs(){
    return new Parameters(Optional.empty(),Collections.emptyList(),Collections.emptyList());
  }
  static Parameters getPs(Expression e){
    return new Parameters(Optional.of(e), Collections.emptyList(),Collections.emptyList());
  }
  static Parameters getPs(String pName,Expression e){
    List<String> ps=new ArrayList<>();
    List<Expression> es=new ArrayList<>();
    ps.add(pName);
    es.add(e);
    return new Parameters(Optional.empty(), ps,es);
  }
  static Position getPosition(Expression src){
    if(src instanceof Ast.HasPos){return ((Ast.HasPos)src).getP();}
    else{return CollapsePositions.of(src);}
  }
  static MCall getMCall(Position p,Expression rec,String name,Parameters ps){
    return new MCall(rec,name,Doc.empty(),ps,p);
  }
  static RoundBlock getBlock(Position p,String x,Expression xe,Expression inner){
    List<Ast.BlockContent> bc=new ArrayList<Ast.BlockContent>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecXE(false,Optional.empty(),x,xe));
    bc.add(new Ast.BlockContent(decs,Optional.empty()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,List<? extends VarDec> _decs,Expression inner){
    if(_decs.isEmpty()){return new RoundBlock(p,Doc.empty(),inner,Collections.emptyList());}
    List<VarDec> decs=new ArrayList<VarDec>(_decs);
    List<Ast.BlockContent> bc=new ArrayList<>();
    bc.add(new Ast.BlockContent(decs,Optional.empty()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,List<? extends VarDec> _decs,Expression inner,Catch k){
    if(_decs.isEmpty()){return new RoundBlock(p,Doc.empty(),inner,Collections.emptyList());}
    List<VarDec> decs=new ArrayList<VarDec>(_decs);
    List<Ast.BlockContent> bc=new ArrayList<Ast.BlockContent>();
    bc.add(new Ast.BlockContent(decs,Optional.of(k)));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,Expression xe,Expression inner){
    List<Ast.BlockContent> bc=new ArrayList<Ast.BlockContent>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecE(xe));
    bc.add(new Ast.BlockContent(decs,Optional.empty()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,Expression xe,Ast.Catch k,Expression inner){
    List<Ast.BlockContent> bc=new ArrayList<Ast.BlockContent>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecE(xe));
    bc.add(new Ast.BlockContent(decs,Optional.of(k)));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static Ast.Catch getK(SignalKind kind, String x, Type t,Expression inner){
  List<On> ons=new ArrayList<On>();
  List<Type> ts=new ArrayList<Type>();
  ts.add(t);
  ons.add(new On(ts, Optional.empty(), inner));
  return new Ast.Catch(kind,x,ons,Optional.empty());
  }
  public Expression visit(CurlyBlock s) {
    RoundBlock inner=new RoundBlock(s.getP(),s.getDoc(),new Expression._void(),s.getContents());
    String y=Functions.freshName("result",this.usedVars);
    //usedVars.add(y);
    Ast.Catch k=getK(SignalKind.Return,y,this.t,new X(y));
    Expression termination= Desugar.errorMsg("CurlyBlock-Should be unreachable code");
    RoundBlock outer=getBlock(s.getP(),inner, k,termination);
    //assert L42.checkWellFormedness(outer);
    return visit(outer);
  }
  public Expression visit(DocE s) {
    return new RoundBlock(getPosition(s),s.getDoc(),lift(s.getInner()),Collections.emptyList());
  }
  public Expression visit(BinOp s) {
    if(s.getOp()==Op.ColonEqual){
      assert s.getLeft() instanceof X:s.getLeft();
      return visit(getMCall(s.getP(),s.getLeft(),"inner",getPs(lift(s.getRight()))));
    }
    if(s.getOp().kind==Ast.OpKind.EqOp){
      Op op2=Op.fromString(s.getOp().inner.substring(0,s.getOp().inner.length()-1));
      BinOp s2=s.withOp(op2);
      s2=s2.withLeft(getMCall(s.getP(),s.getLeft(),"#inner",getPs()));
      return visit(new BinOp(s.getP(),s.getLeft(),Op.ColonEqual,visit1Step(s2)));
    }
    return visit(visit1Step(s));
  }
  static public MCall visit1Step(BinOp s) {
    return getMCall(s.getP(),s.getLeft(),desugarName(s.getOp().inner),getPs(s.getRight()));
  }
  public Expression visit(UnOp s) {
    return visit(visit1Step(s));
  }
  static public MCall visit1Step(UnOp s) {
    return getMCall(s.getP(),s.getInner(),desugarName(s.getOp().inner),getPs());
  }

  public Expression visit(FCall s) {
    return visit(visit1Step(s));
  }
  static public MCall visit1Step(FCall s) {
    return getMCall(s.getP(),s.getReceiver(),"#apply",s.getPs());
  }

  public Expression visit(SquareCall s) {
    return visit(visit1Step(s));
  }
  static public MCall visit1Step(SquareCall s) {
    Expression result=getMCall(s.getP(),s.getReceiver(),"#begin",getPs());
    result = appendAddMethods(s, result);
    return (MCall)appendEndMethod(s.getP(),result,s);
  }
  public static Expression appendAddMethods(SquareCall s, Expression result) {
    for(Parameters ps: s.getPss()){
      result=getMCall(s.getP(),result,"#add",ps);
    }
    return result;
  }
  public static Expression appendEndMethod(Position pos,Expression x,Expression inner) {
    MCall result=new MCall(x,"#end",Doc.empty(),Desugar.getPs(),pos);
    return result;
    //return x;
  }
  @Override
  public Expression visit(UseSquare s) {
    assert s.getInner() instanceof Expression.SquareCall:"The other shape is stupid: use[with...]== with...";
    return super.visit(s);
  }
  public Expression visit(Literal s) {
    return visit(visit1Step(s));
  }
  static public MCall visit1Step(Literal s) {
    String name="#stringParser";
    if(s.isNumber()){name="#numberParser";}
    return getMCall(s.getP(),s.getReceiver(),name,getPs(encodePrimitiveString(s.getInner())));
  }
  protected ast.Ast.Catch liftK(ast.Ast.Catch k){
    if(!k.getX().isEmpty()){
      List<On> ons=new ArrayList<On>();
      for(On on:k.getOns()){
        assert !varEnv.containsKey(k.getX());
        assert on.getTs().size()==1;
        varEnv.put(k.getX(),on.getTs().get(0));
        try{ons.add(liftO(on));}
        finally{varEnv.remove(k.getX());}
      }
      Optional<Expression> def=Optional.empty();
      if(k.get_default().isPresent()){
       Expression e=k.get_default().get();
       varEnv.put(k.getX(),new NormType(Mdf.Immutable,Path.Any(),Ph.None));
       try{def=Optional.of(lift(e));}
       finally{varEnv.remove(k.getX());}
      }
      return new ast.Ast.Catch(k.getKind(),k.getX(),ons,def);
      }
    String x=Functions.freshName("unused",this.usedVars);
    //this.usedVars.add(x);
    return liftK(k.withX(x));
  }
  protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
    assert !d.isVar();
    assert d.getT().isPresent();
    return withExpectedType(d.getT().get(),()->super.liftVarDecXE(d));
  }
  protected ast.Ast.VarDecE liftVarDecE(ast.Ast.VarDecE d) {
    throw Assertions.codeNotReachable();
  }
  protected ast.Ast.VarDecCE liftVarDecCE(ast.Ast.VarDecCE d) {
    return d;//ok, it have to happen after, when is lifted out of the expression
  }
  static ClassB encodePrimitiveString(String s){
    //return EncodingHelper.wrapStringU(s);//no, this produces a ExpCoreClassB
    return new ClassB(Doc.factory("@stringU\n"+EncodingHelper.produceStringUnicode(s)+"\n"),Doc.empty(),new Ast.TraitHeader(),Collections.emptyList(),Collections.emptyList(),Stage.None);
  }
  public static String desugarName(String n){
    if(n.isEmpty())return "#apply";
    if(isNormalName(n)){return n;}
    String res="#";
    for(char c:n.toCharArray()){
      switch (c){
        case '+':res+="plus";break;
        case '-':res+="less";break;
        case '~':res+="tilde";break;
        case '!':res+="bang";break;
        case '&':res+="and";break;
        case '|':res+="or";break;
        case '<':res+="left";break;
        case '>':res+="right";break;
        case '=':res+="equal";break;
        case '*':res+="times";break;
        case '/':res+="divide";break;
      }
    }
    return res;
  }
  private static boolean isNormalName(String n) {
    for(char c:n.toCharArray()){
      if ("+-~!&|<>=*/".indexOf(c)!=-1){return false;}
      }
    return true;
  }

  //---------
  protected Header liftH(Header h) {
    if(!(h instanceof Ast.ConcreteHeader)){return super.liftH(h);}
    Ast.ConcreteHeader ch=(Ast.ConcreteHeader) h;
    return super.liftH(ch.withName(desugarName(ch.getName())));
  }
  protected MethodSelector liftMs(MethodSelector ms) {
    return ms.withName(desugarName(ms.getName()));
  }
  private<T0,T> T withExpectedType(Type t,Supplier<T> f){
    Type aux=this.t;
    this.t=t;
    T result=f.get();
    this.t=aux;
    return result;
  }
  public Expression visit(MCall s) {
    Type recT=GuessType.of(s.getReceiver(), varEnv);
    List<String> names=new ArrayList<String>();
    if(s.getPs().getE().isPresent()){names.add("that");}
    names.addAll(s.getPs().getXs());
    MethodSelector ms=new MethodSelector(s.getName(),names);
    Type tt=recT.match(
        nt->{
          Path path=nt.getPath();
          List<MethodSelectorX> selectors=new ArrayList<>();
          selectors.add(new MethodSelectorX(ms,""));
          return new Ast.HistoricType(path,selectors,false);
          },
        ht->{
          List<MethodSelectorX> selectors=new ArrayList<>(ht.getSelectors());
          selectors.add(new MethodSelectorX(ms,""));
          return ht.withSelectors(selectors);
          }
        );
    //Type tt=new Ast.HistoricType();
    return new MCall(lift(s.getReceiver()),s.getName(),s.getDoc(),
      withExpectedType(tt,()->liftPs(s.getPs())),s.getP()
      );
    }
  public Expression visit(Using s) {
    Type aux=this.t;
    this.t=new NormType(Mdf.Immutable,Path.Void(),Ph.None);
    Parameters ps = liftPs(s.getPs());
    this.t=aux;
    return new Using(lift(s.getPath()),s.getName(),s.getDocs(),ps,lift(s.getInner()));
  }
  public NestedClass visit(NestedClass nc){
    this.usedVars=new HashSet<String>();
    this.varEnv=new HashMap<String, Type>();
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(nc.getInner()));
    return withExpectedType(
      new NormType(Mdf.Immutable, Path.Library(), Ph.None),
      ()->super.visit(nc));
  }
  public MethodImplemented visit(MethodImplemented mi){
    this.usedVars=new HashSet<String>();
    this.varEnv=new HashMap<String, Type>();
    String mName=desugarName(mi.getS().getName());
    mi=mi.withS(mi.getS().withName(mName));
    for(String name:mi.getS().getNames()){
      usedVars.add(name);
      List<Ast.MethodSelectorX> msxsi=new ArrayList<>();
      msxsi.add(new Ast.MethodSelectorX(mi.getS(),name));
      varEnv.put(name,new Ast.HistoricType(Path.outer(0),msxsi,false));
    }
    usedVars.add("this");
    List<Ast.MethodSelectorX> msxsi=new ArrayList<>();
    msxsi.add(new Ast.MethodSelectorX(mi.getS(),"this"));
    varEnv.put("this",new Ast.HistoricType(Path.outer(0),msxsi,false));
    List<Ast.MethodSelectorX> msxs=new ArrayList<>();
    msxs.add(new Ast.MethodSelectorX(mi.getS(),""));
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(mi.getInner()));
    final MethodImplemented mi2=mi;//final restrictions
    return withExpectedType(
      new Ast.HistoricType(Path.outer(0),msxs,false),
      ()->super.visit(mi2));
    }
  public MethodWithType visit(MethodWithType mt){
    this.usedVars=new HashSet<String>();
    this.varEnv=new HashMap<String, Type>();
    String mName=desugarName(mt.getMs().getName());
    mt=mt.withMs(mt.getMs().withName(mName));
    if(!mt.getInner().isPresent()){return super.visit(mt);}
    {int i=-1;for(String name:mt.getMs().getNames()){i+=1;
    this.usedVars.add(name);
    this.varEnv.put(name,mt.getMt().getTs().get(i));
    }}
    usedVars.add("this");
    varEnv.put("this",new NormType(mt.getMt().getMdf(),Path.outer(0),Ph.None));
    usedVars.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(mt.getInner().get()));
    final MethodWithType mt2=mt;//final restrictions
    return withExpectedType(
      liftT(mt.getMt().getReturnType()),
      ()->super.visit(mt2));
      }
  public Expression visit(With e){
    throw Assertions.codeNotReachable();
    }
  public static BlockContent getBlockContent(Expression e) {
    List<VarDec> single= new ArrayList<VarDec>();
    single.add(new VarDecE(e));
    return new BlockContent(single,Optional.empty());
  }
  public static BlockContent getBlockContent(Expression e,Catch k) {
    List<VarDec> single= new ArrayList<VarDec>();
    single.add(new VarDecE(e));
    return new BlockContent(single,Optional.of(k));
  }
  public Expression visit(SquareWithCall s) {
    throw Assertions.codeNotReachable();
    }
  public static Expression errorMsg(String msg){//could be error void, but this is more informative for debugging
    ExpCore core=EncodingHelper.wrapError(msg);
    return core.accept(new InjectionOnSugar());
  }
  //private static final Doc consistentDoc=Doc.factory("@consistent\n");
  public static List<Member> cfType(ConcreteHeader h,Doc doc){
    //doc=Doc.factory("@private");
    List<Member> result=new  ArrayList<Member>();
    cfType1(h,doc, result);
    for(FieldDec f:h.getFs()){
      Doc fDoc=doc.sum(f.getDoc());
      cfType2(h.getP(),f,fDoc,result);
      cfType3(h.getP(),f,fDoc,result);
      cfType4(h.getP(),f,fDoc,result);
    }
    return result;
  }
  static private void cfType1(ast.Ast.ConcreteHeader h, Doc doc,List<Member> result) {
    List<Doc> tDocss=new ArrayList<>();
    List<Type> ts=new ArrayList<Type>();
    List<String> names= new ArrayList<String>();
    boolean hasMut=false;
    boolean hasLentRead=false;
    for(FieldDec f:h.getFs()){
      if(f.isVar()){hasMut=true;}
      if(f.getT() instanceof NormType){
        Mdf mdf=((NormType)f.getT()).getMdf();
        if(mdf==Mdf.Lent || mdf==Mdf.Readable){hasLentRead=true;hasMut=true;}
        if(mdf==Mdf.Mutable){hasMut=true;}
        }
      tDocss.add(f.getDoc());
      ts.add(f.getT());
      names.add(f.getName());
      }
    NormType resT=new ast.Ast.NormType(h.getMdf(),ast.Ast.Path.outer(0),Ph.None);
    if(resT.getMdf()==Mdf.Immutable && hasMut){//otherwise user is overriding the constructor mdf
      resT=resT.withMdf(hasLentRead?Mdf.Lent:Mdf.Mutable);
    }
    MethodType mt=new MethodType(Doc.empty(),ast.Ast.Mdf.Type,ts,tDocss,resT,Collections.emptyList());
    MethodSelector ms=new MethodSelector(h.getName(),names);
    result.add(new MethodWithType(doc, ms,mt, Optional.empty(),h.getP()));
  }
  static private void cfType2(Expression.Position pos,ast.Ast.FieldDec f, Doc doc,List<Member> result) {
    if(!f.isVar()){return;}
    Type tt=f.getT().match(nt->nt.withPh(Ph.None), hType->hType);    
    MethodType mti=new MethodType(Doc.empty(),Mdf.Mutable,Collections.singletonList(tt),Collections.singletonList(Doc.empty()),new ast.Ast.NormType(Mdf.Immutable,Path.Void(),Ph.None),Collections.emptyList());
    MethodSelector msi=new MethodSelector(f.getName(),Collections.singletonList("that"));
    result.add(new MethodWithType(doc, msi, mti, Optional.empty(),pos));
  }
  static private void cfType3(Expression.Position pos,FieldDec f,Doc doc, List<Member> result) {
    Type tt=f.getT().match(nt->nt.withPh(Ph.None), hType->hType);
    MethodType mti=new MethodType(Doc.empty(),Mdf.Mutable,Collections.emptyList(),Collections.emptyList(),tt,Collections.emptyList());
    MethodSelector msi=new MethodSelector("#"+f.getName(),Collections.emptyList());
    result.add(new MethodWithType(doc, msi, mti, Optional.empty(),pos));
  }
  static private void cfType4(Expression.Position pos,FieldDec f,Doc doc, List<Member> result) {
    if(!( f.getT() instanceof NormType)){return;}
    NormType fieldNt=(NormType)f.getT();
    fieldNt=fieldNt.withPh(Ph.None);
    fieldNt=Functions.sharedAndLentToReadable(fieldNt);
    MethodType mti=new MethodType(Doc.empty(),Mdf.Readable,Collections.emptyList(),Collections.emptyList(),fieldNt,Collections.emptyList());
    MethodSelector msi=new MethodSelector(f.getName(),Collections.emptyList());
    result.add(new MethodWithType(doc, msi, mti, Optional.empty(),pos));
    }
}
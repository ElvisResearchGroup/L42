package sugarVisitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import platformSpecific.fakeInternet.OnLineCode;
import privateMangling.RefreshUniqueNames;
import tools.Assertions;
import ast.Ast;
import ast.ErrorMessage;
import ast.Ast.ConcreteHeader;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Header;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Op;
import ast.Ast.Parameters;
import ast.Ast.Path;
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
import ast.Expression.Catch;
import ast.Expression.Catch1;
import ast.Expression.CatchMany;
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
import ast.PathAux;

import ast.Util.PathMxMx;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import coreVisitors.Visitor;
import facade.Configuration;
import facade.L42;
import newTypeSystem.TypeManipulation;
import profiling.Timer;

public class Desugar extends CloneVisitor{
  public static Expression of(Expression e){
    long max=RefreshUniqueNames.maxUnique(e);
    assert max>=0L;
    L42.setFreshPrivateCap(max+1);
    if(L42.cacheK.fileName!=null){e=ReplaceDots.of(L42.root, e);}
    Desugar d=new Desugar();
    for(String si:CollectDeclaredVars.of(e)){Functions.addName(si, L42.usedNames);}
    e=DesugarPaths.of(e);
    e=DesugarNormalizeReceiver.of( e);
    e=DesugarContext.of( e);
    assert DesugarContext.checkRemoved(e);
    e=DesugarW.of(e);
    e=DesugarVars.of(e);
    //assert DesugarVars.assertVarsRemoved(e);
    //understand what is the current folder
    //replace ... recursively
    //replaceDots(currentFolder,e)-> clone visitor
    d.collectAllUsedLibs(e);//collect all classBreuse in a map url->core version

    for(String si:CollectDeclaredClassNamesAndMethodNames.of(e)){
      Functions.addName(si,L42.usedNames);
      }

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
    return classB;//TODO: when new private system is working modify here
  }
  private void collectAllUsedLibs(Expression e) {
    profiling.Timer.activate("sugarvisitors.collectAllUsed");
    e.accept(new CloneVisitor(){
      @Override
      public Expression visit(ClassReuse s) {
        ClassB data = OnLineCode.getCode(s.getUrl());
        for(String si:CollectDeclaredClassNamesAndMethodNames.of(data)){
          Functions.addName(si,L42.usedNames);
          }
        ast.ExpCore.ClassB dataCore=(ast.ExpCore.ClassB) data.accept(new InjectionOnCore());
        Desugar.this.importedLibs.put(s.getUrl(),dataCore);
        //Using a hash map as over means that if we import the same library twice, we get a single copy.
        //This is "ok" but then we can not rely on the identities of the positions.
        //This is why we refresh position identities at the end of desugaring.
        return super.visit(s);
      }
    });
    profiling.Timer.deactivate("sugarvisitors.collectAllUsed");
  }

  HashMap<String,ast.ExpCore.ClassB> importedLibs=new HashMap<>();
  Type t=Type.immVoid;
  HashMap<String,Type> varEnv=new HashMap<String,Type>();

  public Expression visit(RoundBlock s) {
    s=blockEtoXE(s);
    //s=blockInferVar(s);
    HashMap<String, Type> oldVarEnv = new HashMap<String, Type>(varEnv);
    try{
      if(!s.getContents().isEmpty()){addAllDec(s.getContents().get(0).getDecs());}
      Expression result= super.visit(s);
      return result;
    }
    finally{varEnv=oldVarEnv;}
  }
  @Override protected Catch liftK(Catch k){
    if(!(k instanceof Expression.Catch1)){return super.liftK(k);}
    //slower but safer?HashMap<String, NormType> oldVarEnv = new HashMap<String, NormType>(varEnv);
    String added=null;
    try{
      Expression.Catch1 k1=(Expression.Catch1)k;
      added=k1.getX();
      varEnv.put(added,k1.getT());
      return super.liftK(k);
    }
    finally{varEnv.remove(added);}
  }
  private void addAllDec(List<VarDec> decs) {
    for(VarDec _dec:decs){
      if(!(_dec instanceof VarDecXE)){continue;}
      VarDecXE dec=(VarDecXE)_dec;
      if (dec.getT().isPresent()){
        this.varEnv.put(dec.getX(), dec.getT().get());
        }
    }
  }
  private RoundBlock blockEtoXE(RoundBlock s) {
    if(s.getContents().isEmpty()){return s;}
    List<VarDec> decs = s.getContents().get(0).getDecs();
    List<VarDec> newDecs =new ArrayList<VarDec>();
    for(VarDec _dec:decs){
      if(!(_dec instanceof VarDecE)){
        newDecs.add(_dec);continue;}
      VarDecE dec=(VarDecE)_dec;
      String x=Functions.freshName("unused", L42.usedNames);
      //usedVars.add(x);
      VarDecXE newXE=new VarDecXE(false,Optional.of(Type.immVoid),x,dec.getInner());
      newDecs.add(newXE);
      }
    RoundBlock result = blockWithDec(s,newDecs);
    //assert L42.checkWellFormedness(s);
    //assert L42.checkWellFormedness(result);
    return result;
  }

  private RoundBlock blockWithDec(RoundBlock s, List<VarDec> decs) {
    assert s.getContents().size()==1:
      s.getContents().size();
    List<Expression.BlockContent> ctx = new ArrayList<>();
    ctx.add(new Expression.BlockContent(decs, s.getContents().get(0).get_catch()));
    return s.withContents(ctx);
  }

  public Type _computeTypeForClassBForVar(VarDecXE varDec) {
    Type t=varDec.getT().get();
    t=t.withPath(computeTypeForClassBForVar(t.getPath()));
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
        Type ti=dec.getT().get();
        localVarEnv.put(dec.getX(),ti);
        newDecs.add(dec);
        continue;
        }
      localVarEnv.put(dec.getX(),null);
      newDecs.add(dec.withT(Optional.ofNullable(t)));
      }
    return blockWithDec(s, newDecs);
  }



  public Expression visit(ClassB s) {
    Position pos=s.getP();
    assert !(s.getH() instanceof ConcreteHeader);
    if(!s.getFields().isEmpty()){
      List<Member> ms =s.getFields().stream().flatMap(f->Desugar.field(pos,f)).collect(Collectors.toList());
      ms.addAll(s.getMs());
      s=s.withMs(ms).withH(new Ast.TraitHeader());
      }
    HashMap<String, Type> oldVarEnv = this.varEnv;
    try{
      s=(ClassB)super.visit(s);
      s=FlatFirstLevelLocalNestedClasses.of(s,this);
      s=DesugarCatchDefault.of(s);
      return s;}
    finally{
      this.varEnv=oldVarEnv;
      }
  }
  public Expression visit(ClassReuse s) {
    ClassB res=lift(s.getInner());
  //ClassB reused2=OnLineCode.getCode(s.getUrl());
    ExpCore.ClassB _reused=this.importedLibs.get(s.getUrl());
    assert _reused!=null:s.getUrl()+" "+this.importedLibs.keySet()+this.importedLibs.get(s.getUrl())+this.importedLibs;
    ExpCore.ClassB reused=RefreshUniqueNames.refreshTopLevel(_reused);
    for(Member m2:res.getMs()){
      m2.match(
        nc2->{for(ast.ExpCore.ClassB.NestedClass nc1:reused.ns()){
          if (nc1.getName().equals(nc2.getName())){
            throw new ast.ErrorMessage.NotWellFormedMsk(nc1.getP(),s, s, "Nested class \""+nc1.getName()+"\" already present in reused library "+s.getUrl());
            }
        }return null;},
        mi->{return null;},
        mwt2->{for(ast.ExpCore.ClassB.MethodWithType mwt1:reused.mwts()){
          if (mwt1.getMs().equals(mwt2.getMs())){
            throw new ast.ErrorMessage.NotWellFormedMsk(mwt1.getP(),s, s, "Method with type \""+mwt1.getMs()+"\" already present in reused library "+s.getUrl());
            }  
        }return null;});
      }    
    return new ClassReuse(res,s.getUrl(),reused);
  }


  protected Path liftP(Path s) {
    assert s.isCore()|| s.isPrimitive();
    return s;
  }
  protected List<Catch> liftKs(List<Catch> ks) {
    List<Catch> result=new ArrayList<>();
    String x=Functions.freshName("catched", L42.usedNames);
    for(Catch k:ks){
      if( k instanceof DesugarCatchDefault.CatchToComplete){
        Catch k2=this.liftK(((DesugarCatchDefault.CatchToComplete) k).catch1);
        result.add(new DesugarCatchDefault.CatchToComplete((Catch1) k2));
        continue;
        }
      k.match(k1->result.add(liftK(k1)), kM->{
        for(Type t:kM.getTs()){
          result.add(liftK(new Expression.Catch1(kM.getP(),kM.getKind(),t,x,kM.getInner())));
        }
        return false;
      },
      kP->{
        for(Type t:kP.getTs()){
          //S on T e ==  catch exception T x S e(x)
          Expression inner=kP.getInner();
          inner=new Expression.FCall(kP.getP(),inner,Doc.empty(),
              new ast.Ast.Parameters(Optional.of(new X(kP.getP(),x)),Collections.emptyList(),Collections.emptyList()));
          inner=new Expression.Signal(kP.getKind(),inner);
          result.add(liftK(new Expression.Catch1(kP.getP(),SignalKind.Exception,t,x,inner)));
        }
        return false;
      });
      }
    return result;
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
      es.add(withExpectedType(null,()->lift(ei)));
    }}
    return new Parameters( Optional.empty(), ps.getXs(),   es);
  }

  public Expression visit(While s) {
    Expression cond=Desugar.getMCall(s.getP(),s.getCond(), "#checkTrue",Desugar.getPs());
    RoundBlock b=Desugar.getBlock(s.getP(),cond,s.getThen());
    Loop l=new Loop(b);
    Type _void=Type.immVoid;
    Expression.Catch k=Desugar.getK(s.getP(),SignalKind.Exception, "",_void,  Expression._void.instance);
    RoundBlock b2=Desugar.getBlock(s.getP(),l,Collections.singletonList(k),Expression._void.instance);
    return b2.accept(this);
  }
  public Expression visit(If s) {
    if(!s.get_else().isPresent()){
      return visit(s.with_else(Optional.of(Expression._void.instance)));
    }
    Position p=s.getP();
    if(!(s.getCond() instanceof Ast.Atom)){
      String x=Functions.freshName("cond", L42.usedNames);
      return visit(getBlock(p,x, s.getCond(),s.withCond(new X(p,x))));
    }
    MCall check=getMCall(p,s.getCond(),"#checkTrue", getPs());
    Expression.Catch k = getK(p,SignalKind.Exception,"",Type.immVoid,s.get_else().get());
    return visit(getBlock(p,check,Collections.singletonList(k),s.getThen()));
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
    List<Expression.BlockContent> bc=new ArrayList<>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecXE(false,Optional.empty(),x,xe));
    bc.add(new Expression.BlockContent(decs,Collections.emptyList()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,List<? extends VarDec> _decs,Expression inner){
    if(_decs.isEmpty()){return new RoundBlock(p,Doc.empty(),inner,Collections.emptyList());}
    List<VarDec> decs=new ArrayList<VarDec>(_decs);
    List<Expression.BlockContent> bc=new ArrayList<>();
    bc.add(new Expression.BlockContent(decs,Collections.emptyList()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }

  static RoundBlock getBlock(Position p,Expression xe,Expression inner){
    List<Expression.BlockContent> bc=new ArrayList<>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecE(xe));
    bc.add(new Expression.BlockContent(decs,Collections.emptyList()));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static RoundBlock getBlock(Position p,Expression xe,List<Expression.Catch> ks,Expression inner){
    List<Expression.BlockContent> bc=new ArrayList<>();
    List<VarDec> decs = new ArrayList<VarDec>();
    decs.add(new VarDecE(xe));
    bc.add(new Expression.BlockContent(decs,ks));
    return new RoundBlock(p,Doc.empty(),inner,bc);
  }
  static Expression.Catch getK(Position pos,SignalKind kind, String x, Type t,Expression inner){
  if (x==""){return new Expression.CatchMany(pos,kind,Collections.singletonList(t),inner);}
  return new Expression.Catch1(pos,kind,t,x,inner);
  }
  public Expression visit(CurlyBlock s) {
    assert s.getContents().size()==1;
    assert s.getContents().get(0).get_catch().isEmpty();
    assert s.getContents().get(0).getDecs().size()==1;
    assert s.getContents().get(0).getDecs().get(0) instanceof VarDecE;
    Expression inner=((VarDecE)s.getContents().get(0).getDecs().get(0)).getInner();
    String y=Functions.freshName("result",L42.usedNames);
    Expression.Catch k=getK(s.getP(),SignalKind.Return,y,Type.immAny,new X(s.getP(),y));
    Expression termination= Desugar.errorMsg("CurlyBlock-Should be unreachable code");
    RoundBlock outer=getBlock(s.getP(),inner, Collections.singletonList(k),termination);
    return visit(outer);
  }
  public Expression visit(DocE s) {
    return new RoundBlock(getPosition(s),s.getDoc(),lift(s.getInner()),Collections.emptyList());
  }
  public Expression visit(BinOp s) {
    Op op=s.getOp();
    if(op==Op.ColonEqual){
      return s.withRight(lift(s.getRight()));//TODO: set the right expect type?
    }
    if(op.kind==Ast.OpKind.EqOp){
      //go from, for example a++=b into a:=a ++b
      Op op2=op.nonEqOpVersion();
      BinOp s2=s.withOp(op2);
      //NO!s2=s2.withLeft(getMCall(s.getP(),s.getLeft(),"#inner",getPs()));
      return visit(new BinOp(s.getP(),s.getLeft(),Op.ColonEqual,Doc.empty(),s2));
    }
    if (op.negated){
      BinOp s2=s.withOp(op.nonNegatedVersion());
      return visit(getMCall(s.getP(),s2,desugarName(Op.Bang.inner),getPs()));
      }
    if (op.normalized){
      return visit(getMCall(s.getP(),s.getLeft(),desugarName(s.getOp().inner),getPs(s.getRight())));
      }
    String x=Functions.freshName("opNorm", L42.usedNames);
    BinOp s2=new BinOp(s.getP(),s.getRight(),op.normalizedVersion(),s.getDoc(),new Expression.X(s.getP(),x));
    return visit(getBlock(s.getP(),x, s.getLeft(),s2));
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
  public MCall visit1Step(SquareCall s) {
    //we can assumethe receivers are normalized after DesugarContext
    //assert s.getReceiver() instanceof Ast.Atom:      s.getReceiver();
    //but nor really, since vars accesses are replaced by meth calls. In that case is fine to not have an atom.
    //(b=r.builder() b.a() b.b() b.c() .... b)
    List<VarDec> vd=new ArrayList<>();
    Expression k=getMCall(s.getP(),s.getReceiver(),"#seqBuilder",getPs());
    String x=Functions.freshName("b", L42.usedNames);
    X b=new X(s.getP(),x);
    vd.add(new VarDecXE(false, Optional.empty(), x, k));
    for(Parameters ps:s.getPss()){
      vd.add(new VarDecE(getMCall(s.getP(),b,"#add",ps)));
    }
    Expression inner=getBlock(s.getP(), vd, b);
    Parameters ps=new Parameters(Optional.empty(), Collections.singletonList("seqBuilder"),Collections.singletonList(inner));
    return getMCall(s.getP(),s.getReceiver(),"#from",ps);
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
    //we can assumethe receivers are normalized after DesugarContext
    return visit1Step(s).accept(this);
  }

  public MCall visit1Step(Literal s) {
    //(b=r.builder() b.a() b.b() b.c() .... b)
    List<VarDec> vd=new ArrayList<>();
    Expression k=getMCall(s.getP(),s.getReceiver(),"#builder",getPs());
    String x=Functions.freshName("b", L42.usedNames);
    X b=new X(s.getP(),x);
    vd.add(new VarDecXE(false, Optional.empty(), x, k));
    for(char ch:s.getInner().toCharArray()){
      String name=Character.toString(ch);
      if(!Character.isAlphabetic(ch) && ! Character.isDigit(ch) ){
        name=desugarSymbol(name);
        }
      vd.add(new VarDecE(getMCall(s.getP(),b,"#"+name,getPs())));
    }
    Expression inner=getBlock(s.getP(), vd, b);
    Parameters ps=new Parameters(Optional.empty(), Collections.singletonList("builder"),Collections.singletonList(inner));
    return getMCall(s.getP(),s.getReceiver(),"#from",ps);
  }
  static Ast.MethodSelector literalGuessedSelector(){
    return Ast.MethodSelector.of("#from",Collections.singletonList("builder"));
  }
  static Ast.MethodSelector squareGuessedSelector(){
    return Ast.MethodSelector.of("#from",Collections.singletonList("seqBuilder"));
  }
  protected ast.Ast.VarDecXE liftVarDecXE(ast.Ast.VarDecXE d) {
    if(d.getT().isPresent()){
      return withExpectedType(d.getT().get(),()->super.liftVarDecXE(d));
      }
    return super.liftVarDecXE(d);//TODO: or set the type to void?
    }
  protected ast.Ast.VarDecE liftVarDecE(ast.Ast.VarDecE d) {
    throw Assertions.codeNotReachable();
  }
  protected ast.Ast.VarDecCE liftVarDecCE(ast.Ast.VarDecCE d) {
    return d;//ok, it have to happen after, when is lifted out of the expression
  }

  protected Doc liftDoc(Doc doc) {
    return super.liftDoc(doc.toMultiline());
  }

  static ClassB encodePrimitiveString(String s){
    //return EncodingHelper.wrapStringU(s);//no, this produces a ExpCoreClassB
    return new ClassB(Doc.factory(true,"@stringU\n"+EncodingHelper.produceStringUnicode(s)+"\n"),new Ast.TraitHeader(),Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Position.noInfo);
  }
  public static String desugarName(String n){
    if(n.isEmpty())return "#apply";
    if(isNormalName(n)){return n;}
    return "#"+desugarSymbol(n);
    }
  public static String desugarSymbol(String n){
    String res="";
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
        case '(':res+="oRound";break;
        case ')':res+="cRound";break;
        case '[':res+="oSquare";break;
        case ']':res+="cSquare";break;
        case '{':res+="oCurly";break;
        case '}':res+="cCurly";break;
        case '\"':res+="dQuote";break;
        case '\'':res+="sQuote";break;
        case '`':res+="hQuote";break;
        case '?':res+="qMark";break;
        case '^':res+="hat";break;
        case ',':res+="comma";break;
        case ';':res+="semicolon";break;
        case ':':res+="colon";break;
        case '.':res+="dot";break;
        case '_':res+="underscore";break;
        case '#':res+="hash";break;
        case '@':res+="at";break;
        case '$':res+="$";break;//yes, is not an operator, but is not alphabetic :(
        case '%':res+="%";break;//yes, is not an operator, but is not alphabetic :(
        case '\\':res+="backslash";break;
        case ' ':res+="space";break;
        case '\n':res+="newline";break;
        case '\t':
        throw new AssertionError("Tab in string?");
      }
    }
    assert res.length()>0:"\""+n+"\"";
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
    if (t==null){t=Path.Any().toImmNT();}
    Type aux=this.t;
    this.t=t;
    T result=f.get();
    this.t=aux;
    return result;
  }
  public Expression visit(MCall s) {
    List<String> names=new ArrayList<String>();
    if(s.getPs().getE().isPresent()){names.add("that");}
    names.addAll(s.getPs().getXs());
    return new MCall(lift(s.getReceiver()),s.getName(),s.getDoc(),
      liftPs(s.getPs()),s.getP()
      );
    }
  public Expression visit(Using s) {
    Type aux=this.t;
    this.t=Type.immVoid;
    Parameters ps = liftPs(s.getPs());
    this.t=aux;
    return new Using(liftP(s.getPath()),s.getName(),s.getDocs(),ps,lift(s.getInner()));
  }
  public NestedClass visit(NestedClass nc){
    while(nc.getInner() instanceof Expression.DocE){
      nc=nc.withInner(((Expression.DocE)nc.getInner()).getInner());
      }//TODO: document stripping of comments and decide scope
    NestedClass nc1=nc;
    this.varEnv=new HashMap<String, Type>();
    for(String si:CollectDeclaredVars.of(nc.getInner())){Functions.addName(si,L42.usedNames);}
    return withExpectedType(
      Type.immLibrary,
      ()->super.visit(nc1));
  }
  public MethodImplemented visit(MethodImplemented mi){
    this.varEnv=new HashMap<String, Type>();
    String mName=desugarName(mi.getS().nameToS());
    mi=mi.withS(mi.getS().withName(mName));
    for(String name:mi.getS().getNames()){
      Functions.addName(name,L42.usedNames);
      varEnv.put(name,null);
    }
    Functions.addName("this",L42.usedNames);
    for(String si:CollectDeclaredVars.of(mi.getInner())){Functions.addName(si,L42.usedNames);}
    final MethodImplemented mi2=mi;//final restrictions
    return withExpectedType(null,
      ()->super.visit(mi2));
    //well... this is an issue> method desugaring for method implemented does not know its return type?
    }
  public MethodWithType visit(MethodWithType mt){
    this.varEnv=new HashMap<String, Type>();
    String mName=desugarName(mt.getMs().getName());
    mt=mt.withMs(mt.getMs().withName(mName));
    if(!mt.getInner().isPresent()){return super.visit(mt);}
    {int i=-1;for(String name:mt.getMs().getNames()){i+=1;
    Functions.addName(name,L42.usedNames);
    this.varEnv.put(name,mt.getMt().getTs().get(i));
    }}
    Functions.addName("this",L42.usedNames);
    varEnv.put("this",new Type(mt.getMt().getMdf(),Path.outer(0),mt.getDoc()));
    for(String si:CollectDeclaredVars.of(mt.getInner().get())){Functions.addName(si,L42.usedNames);}
    final MethodWithType mt2=mt;//final restrictions
    return withExpectedType(
      liftT(mt.getMt().getReturnType()),
      ()->super.visit(mt2));
      }
  public Expression visit(With e){
    throw Assertions.codeNotReachable();
    }
  public static Expression.BlockContent getBlockContent(Expression e) {
    List<VarDec> single= new ArrayList<VarDec>();
    single.add(new VarDecE(e));
    return new Expression.BlockContent(single,Collections.emptyList());
  }
  public static Expression.BlockContent getBlockContent(Expression e,Expression.Catch k) {
    List<VarDec> single= new ArrayList<VarDec>();
    single.add(new VarDecE(e));
    return new Expression.BlockContent(single,Collections.singletonList(k));
  }
  public Expression visit(SquareWithCall s) {
    throw Assertions.codeNotReachable();
    }
  public static Expression errorMsg(String msg){//could be error void, but this is more informative for debugging
    ExpCore core=EncodingHelper.wrapError(msg);
    return core.accept(new InjectionOnSugar());
  }
  //private static final Doc consistentDoc=Doc.factory("@consistent\n");
 /* public static List<Member> cfType(ConcreteHeader h,Doc doc){
    //doc=Doc.factory("@private");
    List<Member> result=new  ArrayList<Member>();
    MethodWithType k = cfMutK(doc,h);
    Mdf nameMdf=mdfForNamedK(h);
    if(nameMdf==Mdf.Lent){k=cfLentK(k);}
    MethodWithType kOut =cfNameK(doc, nameMdf, h, k.getMs());
    result.add(k);
    result.add(kOut);
    //cfType1(h,doc, result);
    for(FieldDec f:h.getFs()){
      Doc fDoc=doc.sum(f.getDoc());
      cfSetter(h.getP(),f,fDoc,result);
      cfExposer(h.getP(),f,fDoc,result);
      cfGetter(h.getP(),f,fDoc,result);
    }
    return result;
  }
*/
  static private MethodWithType cfNameK(Doc doc,Mdf mdf,ast.Ast.ConcreteHeader h,MethodSelector called) {
    List<Type> ts=new ArrayList<Type>();
      for(FieldDec fi:h.getFs()){
        Type ti=fi.getT();
        ts.add(ti.withDoc(ti.getDoc().sum(fi.getDoc())));
        }
    MethodSelector ms=called.withName(h.getName());
    Type resT=new ast.Ast.Type(mdf,ast.Ast.Path.outer(0),Doc.empty());
    MethodType mt=new MethodType(false,ast.Ast.Mdf.Class,ts,resT,Collections.emptyList());
    Parameters ps=new Parameters(Optional.empty(),called.getNames(), called.getNames().stream().map(n->new X(Position.noInfo,n)).collect(Collectors.toList()));
    MCall body=new MCall(new Expression.EPath(h.getP(),Path.outer(0)),called.nameToS(),Doc.empty(),ps,h.getP());
    return new MethodWithType(doc, ms,mt, Optional.of(body),h.getP());
  }
  static public MethodWithType cfLentK(MethodWithType mutK) {
    mutK=mutK.withMs(mutK.getMs().withName("#lentK"));
    Type resT=new ast.Ast.Type(Mdf.Lent,ast.Ast.Path.outer(0),Doc.empty());
    MethodType mt = mutK.getMt();
    mt=mt.withReturnType(resT).withTs(mt.getTs().stream()
        .map(t->(Type)t)
        .map(nt->nt.withMdf(nt.getMdf()==Mdf.Mutable?Mdf.Lent:nt.getMdf()))
        .collect(Collectors.toList()));
    mutK=mutK.withMt(mt);
    return mutK;
  }
  /*static public MethodWithType cfMutK(Doc doc,ast.Ast.ConcreteHeader h) {
    return cfMutK(doc,h.getFs(),h.getP());
    }*/
  /*static public MethodWithType cfMutK(Doc doc,List<FieldDec>fields,Position pos) {
    List<String> names= new ArrayList<String>();
    List<NormType> ts=new ArrayList<NormType>();
    for(FieldDec fi:fields){
      NormType ti=fi.getT().match(nt->{
        if(nt.getMdf()==Mdf.Capsule){nt=nt.withMdf(Mdf.Mutable);}
        return nt;
        },ht->ht);
      ti=ti.withDoc(ti.getDoc().sum(fi.getDoc()));
      ts.add(ti);
      names.add(fi.getName());
      }
    MethodSelector ms=MethodSelector.of("#mutK",names);
    NormType resT=new ast.Ast.NormType(Mdf.Mutable,ast.Ast.Path.outer(0),Doc.empty());
    MethodType mt=new MethodType(false,ast.Ast.Mdf.Class,ts,resT,Collections.emptyList());
    return new MethodWithType(doc, ms,mt, Optional.empty(),pos);
    }*/
  static private Mdf mdfForNamedK(ast.Ast.ConcreteHeader h){
    boolean canImm=true;
    for(FieldDec f:h.getFs()){
      if(!(f.getT() instanceof Type)){return Mdf.Mutable;}//TODO: will disappear?
      Type nt=(Type)f.getT();
      Mdf m=nt.getMdf();
      if(m==Mdf.Lent || m==Mdf.Readable){return Mdf.Lent;}
      if(m!=Mdf.Immutable && m!=Mdf.Class && m!=Mdf.ImmutableFwd){canImm=false;}
      if(f.isVar()){canImm=false;}
      }
    if(canImm){return Mdf.Immutable;}
    return Mdf.Mutable;
  }

  static private Stream<Member> field(Position pos,Ast.FieldDec f){
    Stream<Member> s=Stream.of();
    //if var, do setter
    if(f.isVar()){s=Stream.concat(s,Stream.of(generateSetter(pos,f,f.getDoc())));}
    s=Stream.concat(s, Stream.of(
      generateGetter(pos,f,f.getDoc())
      ));
    Mdf m=f.getT().getMdf();
    if(m==Mdf.Lent ||
         m==Mdf.Mutable ||
         m==Mdf.MutableFwd ||
         m==Mdf.Capsule ){
      s=Stream.concat(s, Stream.of(    
        generateExposer(pos,f,f.getDoc())
        ));
      }
    return s;
    }
  private static boolean requireExposer(Type t) {
    Mdf mdf= ((Type)t).getMdf();
    return mdf==Mdf.Mutable || mdf==Mdf.Capsule|| mdf==Mdf.Lent;

  }

  static private void cfSetter(Expression.Position pos,ast.Ast.FieldDec f, Doc doc,List<Member> result) {
    if(!f.isVar()){return;}
    result.add(generateSetter(pos, f, doc));
  }
  private static MethodWithType generateSetter(Expression.Position pos, ast.Ast.FieldDec f, Doc doc) {
    Type tt=TypeManipulation.noFwd(f.getT());
    MethodType mti=new MethodType(false,Mdf.Mutable,Collections.singletonList(tt),Type.immVoid,Collections.emptyList());
    MethodSelector msi=MethodSelector.of(f.getName(),Collections.singletonList("that"));
    MethodWithType mwt = new MethodWithType(doc, msi, mti, Optional.empty(),pos);
    return mwt;
  }
  //left cfExposer generating exposer since is different from generateExposer code for # and capsule
  /*static private void cfExposer(Expression.Position pos,FieldDec f,Doc doc, List<Member> result) {
    NormType tt=f.getT().match(nt->Functions.toComplete(nt), hType->hType);
    MethodType mti=new MethodType(false,Mdf.Mutable,Collections.emptyList(),tt,Collections.emptyList());
    MethodSelector msi=MethodSelector.of("#"+f.getName(),Collections.emptyList());
    result.add(new MethodWithType(doc, msi, mti, Optional.empty(),pos));
  }*/
    private static MethodWithType generateExposer(Expression.Position pos, FieldDec f, Doc doc) {
    Type tt=TypeManipulation.noFwd(f.getT());
    if(tt.getMdf()==Mdf.Capsule){tt=tt.withMdf(Mdf.Lent);}
    
    MethodType mti=new MethodType(false,Mdf.Mutable,Collections.emptyList(),tt,Collections.emptyList());
    MethodSelector msi=MethodSelector.of("#"+f.getName(),Collections.emptyList());
    MethodWithType mwt = new MethodWithType(doc, msi, mti, Optional.empty(),pos);
    return mwt;
  }
  private static MethodWithType generateGetter(Expression.Position pos, FieldDec f, Doc doc) {
    Type fieldNt=(Type)f.getT();
    fieldNt=TypeManipulation.noFwd(fieldNt);
    Mdf mdf=fieldNt.getMdf();
    if(mdf==Mdf.Capsule || mdf==Mdf.Mutable || mdf==Mdf.Lent){
      fieldNt=fieldNt.withMdf(Mdf.Readable);
      }
    MethodType mti=new MethodType(false,Mdf.Readable,Collections.emptyList(),fieldNt,Collections.emptyList());
    String name=f.getName();
    //if(name.startsWith("#")){name=name.substring(1);}
    MethodSelector msi=MethodSelector.of(name,Collections.emptyList());
    MethodWithType mwt=new MethodWithType(doc, msi, mti, Optional.empty(),pos);
    return mwt;
  }
}
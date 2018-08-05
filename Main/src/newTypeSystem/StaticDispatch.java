package newTypeSystem;

import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.EPath;
import ast.ExpCore.Loop;
import ast.ExpCore.MCall;
import ast.ExpCore.OperationDispatch;
import ast.ExpCore.Signal;
import ast.ExpCore.UpdateVar;
import ast.ExpCore.Using;
import ast.ExpCore.WalkBy;
import ast.ExpCore.X;
import ast.ExpCore._void;
import auxiliaryGrammar.Functions;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Type;
import ast.ErrorMessage;
import coreVisitors.CloneVisitor;
import coreVisitors.From;
import coreVisitors.Visitor;
import programReduction.Program;
import tools.Assertions;

public class StaticDispatch implements Visitor<ExpCore>{
  public static  ExpCore of(Program p,G g,ExpCore e,boolean forceError){
    StaticDispatch sd=new StaticDispatch(p, g, forceError);
    ExpCore res=e.accept(sd);
    assert res!=null;
    return res;
    }
  public static  List<Dec> of(Program p,G g,List<Dec>ds,Ast.Position pos,boolean forceError){
    StaticDispatch sd=new StaticDispatch(p, g, forceError);
    return sd.liftDecs(ds,pos);
    }

  Program p;
  G g;
  boolean errors=false;
  boolean forceError;
  private StaticDispatch(Program p,G g,boolean forceError){this.p=p;this.g=g;this.forceError=forceError;}

  //at the end, this.g will include some of ds.
  List<ExpCore.Block.Dec> liftDecs(List<ExpCore.Block.Dec>ds,Ast.Position pos){
    this.g=this.g.add(p,ds);
    while(true){
      List<ExpCore.Block.Dec>oldDs=ds;
      ds = liftDecsAux(ds);
      if(!this.errors){return ds;}
      G old=this.g;
      this.g=this.g.add(p,ds);
      if (this.g.dom().size()!=old.dom().size()){continue;}
      assert this.g.dom().equals(old.dom());
      assert ds.equals(oldDs);
      if(forceError){improveError(ds,pos);}
      this.errors=true;
      return ds;
      }
    }
private static class ESDispatch extends StaticDispatch{
  ESDispatch(Program p,G g,List<ExpCore>log){super(p,g,false);this.log=log;}
  List<ExpCore>log;
  @Override public void logErr(ExpCore e,G g) {
    e=e.accept(new CloneVisitor(){
      public ExpCore visit(X s) {
        Type _t=g._g(s.getInner());
        if(_t==null){return s;}
        return new Block(Doc.factory(true, _t.getMdf().name()), 
                Collections.emptyList(), new EPath(s.getP(),_t.getPath()), Collections.emptyList(), s.getP(), null);
      }
    public ExpCore visit(ClassB s) { return s;}
    });
    log.add(e);
    }
  public StaticDispatch newAllowingError(Program p,G g) {
    return new ESDispatch(p,g,log);
    }
}
private void improveError(List<ExpCore.Block.Dec> ds,Ast.Position pos) {
  List<ExpCore>log=new ArrayList<>();
  new ESDispatch(p, g, log).liftDecs(ds,pos);
  Optional<OperationDispatch> failed=log.stream()
    .filter(e-> e instanceof OperationDispatch)
    .map(e->(OperationDispatch)e).findFirst();
  if(failed.isPresent()) {throw new ErrorMessage.OperatorDispachFail(failed.get(), failed.get().getP());}
  String onMeth="";
  if(log.get(0) instanceof ExpCore.MCall){onMeth="\nSuch method selector may not be defined.\n";}
  String msg=sugarVisitors.ToFormattedText.of(log.get(0));
  msg=msg.replace("\n", " ");
  if(msg.length()>100){msg=msg.substring(0,100)+"..";}
  throw new ErrorMessage.InferenceFail(ds,pos).msg("The type of the following expression can not be inferred:"+msg+onMeth);
  }
  private ExpCore _liftAllowError(boolean[]error,ExpCore e){
    StaticDispatch fresh=newAllowingError(p,g);
    ExpCore res = e.accept(fresh);
    error[0]=fresh.errors;
    return res;
    }
  public void logErr(ExpCore e,G g) {}
  public StaticDispatch newAllowingError(Program p,G g) {
    return new StaticDispatch(p,g,false);
    }
  //will set this.errors as secondary return value.
  List<ExpCore.Block.Dec> liftDecsAux(List<Dec>ds){
    boolean err=false;
    List<Dec>res=new ArrayList<>();
    for(Dec d:ds){
      boolean error[]={false};
      ExpCore _e=_liftAllowError(error,d.getInner());
      if(error[0]){
        err=true;res.add(d);continue;
        }
      d=d.withInner(_e);
      if(d.get_t()!=null){res.add(d);continue;}
      Type _t=GuessTypeCore._of(p,g,_e,false);
      if(_t==null){
        logErr(_e,g);
        err=true;res.add(d);continue;
      }
      _t=Functions.capsuleToMut(_t);
      _t=Functions.adaptTypeToVarName(d.getX(),_t);
      d=d.with_t(_t);
      res.add(d);
      }
    this.errors=err;
    return res;
    }
  public ExpCore visit(MCall s) {
    ExpCore inner=s.getInner().accept(this);
    assert inner!=null;
    return s.withInner(inner);
    }
  public ExpCore visit(Block s) {
    List<Dec> ds1 = liftDecs(s.getDecs(),s.getP());
    assert ds1!=null;
    assert !forceError || ds1.stream().allMatch(d->d.get_t()!=null);
    G old=g;
    g=g.add(p, ds1);
    try{return s.withDecs(ds1).withE(s.getE().accept(this));}
    finally{g=old;}
  }
  public ExpCore visit(OperationDispatch s) {
    End:{
      List<Type> Ts =new ArrayList<>();
      List<List<Type>> TiTsis =new ArrayList<>();
      List<List<MethodWithType>>mssi=new ArrayList<>();
      for(ExpCore ei:s.getEs()){
        ExpCore.X xi=(ExpCore.X)ei;
        Type ti=g._g(xi.getInner());
        if(ti==null){break End;}
        Ts.add(ti);
        }
      fillMTs("#"+s.getS().getName()+"#",s.getS().getNames(),Ts,TiTsis,mssi);
      int maxJ=0;//index can be larger then number of methods
      for(List<MethodWithType> allMsi:mssi){maxJ=Math.max(maxJ, allMsi.size());}
      for(int j=0;j<maxJ;j+=1){//j: the layer of iterations
        {int i=-1;for(List<MethodWithType> allMsi:mssi){i+=1;//the method in parameter i
          if (allMsi.size()<=j ||allMsi.get(j)==null){continue;}//par i have no method for layer j
          List<MethodType> mts = AlternativeMethodTypes.types(allMsi.get(j).getMt());
          if(!oneFits(TiTsis.get(i),mts)){continue;}
          List<ExpCore> es=new ArrayList<>(s.getEs());
          es.remove(i);
          return new ExpCore.MCall(s.getEs().get(i),allMsi.get(j).getMs(),s.getDoc(),es,s.getP(),null,null);
          }}
        }
      logErr(s,g);//this logs failures *not* caused by absence of inferred types.
      }
    if(forceError){
      throw new ErrorMessage.OperatorDispachFail(s,s.getP());
      }
    this.errors=true;
    return s;
  }

  private boolean oneFits(List<Type> TiTsi,List<MethodType> mts){
    Out:for(MethodType mt:mts){//p|-TiTsi  <=mt.getTs()
      {int par=-1;for(Type t:TiTsi){par+=1;
        if(par==0){
          if(Functions.isSubtype(t.getMdf(),mt.getMdf())){continue;}
          continue Out;
          }
        ErrorKind err = TypeSystem._subtype(p,t,mt.getTs().get(par-1));
        if(err!=null){continue Out;}
        }}
      return true;
      }
    return false;
    }
  private void fillMTs(String nameStart,List<String>names,
      List<Type> Ts,List<List<Type>> TiTsis,List<List<MethodWithType>>mssi) {
    {int i=-1;for(Type ti:Ts){i+=1;
      List<String> namesi=new ArrayList<>();
      List<Type> tsi=new ArrayList<>();
      tsi.add(ti);
      {int j=-1;for(Type tj:Ts){j+=1;
        if(i!=j){
          namesi.add(names.get(j));
          tsi.add(tj);
          }
        }}
      List<MethodWithType> mwtsi;
      if(ti.getPath().isPrimitive()){mwtsi=Collections.emptyList();}
      else {mwtsi= p.extractClassB(ti.getPath()).mwts();}
      List<MethodWithType> allMsi=new ArrayList<>();
      for(MethodWithType mwtk:mwtsi){
        //forall ms in Ti, if ms=msj, allMsi[j]=ms
        int j=matchMs(nameStart,mwtk.getMs(),new MethodSelector(names.get(i),-1,namesi));
        if(j!=-1){setSparse(allMsi,j,From.from(mwtk.with_inner(null),ti.getPath()));}
        }
      mssi.add(allMsi);
      TiTsis.add(tsi);
      }}
    }
  private <T> void setSparse(List<T>arr,int index,T el){
    while(index>=arr.size()){arr.add(null);}
    arr.set(index, el);
    }

  private int matchMs(String nameStart,MethodSelector actual, MethodSelector ms) {
    if(!actual.getName().startsWith(nameStart)){return -1;}
    if(!actual.getNames().equals(ms.getNames())){return -1;}
    StringBuilder index=new StringBuilder();
    StringBuilder rest=new StringBuilder();
    actual.getName().chars().skip(nameStart.length()).forEach(c->{
      if(rest.length()!=0 || !Character.isDigit(c)){rest.appendCodePoint(c);}
      else {index.appendCodePoint(c);}
      });
    if(!rest.toString().equals(ms.getName())){return -1;}
    int res=Integer.parseInt(index.toString());
    return res;
    }

public ExpCore visit(EPath s) {return s;}
  public ExpCore visit(X s) {return s;}
  public ExpCore visit(_void s) {return s;}
  public ExpCore visit(WalkBy s) {return s;}
  public ExpCore visit(Using s) {return s;}
  public ExpCore visit(Signal s) {return s;}
  public ExpCore visit(ClassB s) {return s;}
  public ExpCore visit(Loop s) {return s;}
  public ExpCore visit(UpdateVar s) {return s;}
}

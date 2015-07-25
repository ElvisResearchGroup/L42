package reduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import platformSpecific.javaTranslation.Resources;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import tools.Match;
import coreVisitors.*;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.NormType;
//import ast.Ast.*;//NO too much clashes
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.Block;
import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.Member;
import ast.Redex.LoopR;
import ast.Redex.Using;
import ast.ExpCore.*;
import ast.ExpCore.Block.*;
import ast.ExpCore.ClassB.*;
import ast.Redex;
import ast.Redex.*;
import auxiliaryGrammar.*;

abstract public class Executor {
  abstract protected ClassB meta1(Program p, ClassB cb, NestedClass m);
  abstract protected void log(String s);
  static public ExpCore last1=null;
  static public ExpCore last2=null;

public static void dbgRecordNext(ExpCore e){
  last2=last1;
  last1=e;
  }
public static ExpCore stepStar(Executor executer,ExpCore e){
  final Program emptyP=Program.empty();
  int iteration=0;
  dbgRecordNext(e);
  executer.log("--------------------"+(iteration+=1));
  //TestHelper.dbgCompact(e);
  try{while(true){
    assert coreVisitors.CheckNoVarDeclaredTwice.of((ClassB) e);
    e=NormalizeBlocks.of(e);
    e=executer.step(emptyP,e);
    dbgRecordNext(e);
    assert coreVisitors.CheckNoVarDeclaredTwice.of((ClassB) e);
    executer.log("--------------------"+(iteration+=1));
    //TestHelper.dbgCompact(e);
  }}
  //catch(ErrorMessage.NormalForm mess){ return mess.getE();}
  catch(ErrorMessage.CtxExtractImpossible mess){
    assert e instanceof ClassB;
    ClassB ct= Configuration.typeSystem.typeExtraction(emptyP,(ClassB)e);
    Program p1=emptyP.addAtTop(ct);
    Configuration.typeSystem.checkCt( emptyP, ct);
    if(!p1.checkComplete()){//also check is star
      p1.checkComplete();//to let debugger enter
      throw new ErrorMessage.MalformedFinalResult(ct,
        "Some class can not be completely typed as is still incomplete or refers to incomplete classes"
          +ErrorFormatter.reportPlaceOfMetaError(p1,ct)
          );
      };
    return e;}
}
final public ExpCore step(Program p,ExpCore e){
  try{
  Ctx<Redex> ctx=ExtractCtxVal.of(p, e);
  log("---REDEX--: "+ctx.hole.getClass().getSimpleName());
  return Match.<ExpCore>of(ctx.hole)
      .add(Redex.Garbage.class, r->garbage(ctx.ctx,r))
      .add(Redex.CaptureOrNot.class, r->captureOrNot(p,ctx.ctx,r))
      .add(Redex.BlockElim.class, r->blockElim(ctx.ctx,r))
      .add(Redex.Subst.class, r->subst(ctx.ctx,r))
      .add(Redex.MethCall.class, r->methCall(p,ctx.ctx,r))
      .add(Redex.Ph.class, r->ph(p,ctx.ctx,r))
      .add(Redex.NoThrowRemoveOn.class, r->removeCatch(ctx.ctx,r))
            .add(Redex.LoopR.class, r->loopR(ctx.ctx,r))
      .add(Redex.UsingOut.class, r->usingOut(p,ctx.ctx,r))
      .add(Redex.Using.class, r->using(p,ctx.ctx,r))
      .add(Redex.Meta.class, r->meta(p,ctx.ctx,r))
      .end();}
catch(ErrorMessage.CtxExtractImpossible rethrow){
  throw rethrow;//to debug
}
}

private static ExpCore using(Program p, ExpCore ctxVal, Using r) {
  return ReplaceCtx.of(ctxVal,r.getToReplace());
}
private static ExpCore usingOut(Program p, ExpCore ctxVal, UsingOut r) {
  ExpCore inner=r.getThat().getInner();
  return ReplaceCtx.of(ctxVal,inner);
}
private static ExpCore loopR(ExpCore ctxVal, LoopR r) {
  HashSet<String> usedAround = new HashSet<String>(HB.of(ctxVal, false));
  HashSet<String> usedSomewhere = new HashSet<String>(HB.of(r.getThat().getInner(), false));
  HashSet<String> forbidden = new HashSet<String>(usedSomewhere);
  forbidden.addAll(usedAround);
  String x=Functions.freshName(Path.Void(), forbidden);
  ExpCore newInner=r.getThat().getInner();
  HashMap<String,String> renames=new HashMap<String,String>();
  for(String s:usedSomewhere){
    String s2=Functions.freshName(s,forbidden);
    renames.put(s, s2);
  }
  newInner=RenameVars.of(newInner,renames);
  assert checkSuccessRename(usedSomewhere, newInner);
  Block.Dec xDec=new Block.Dec(new NormType(Mdf.Immutable,Path.Void(),Ph.None),x,newInner);
  Block result=new Block(null,Doc.empty(),xDec,r.getThat());
  return ReplaceCtx.of(ctxVal, result);
}
//NO! the ctc could no be extracted//.end(new ErrorMessage.NormalForm(e,p.getInnerData()));}
private ExpCore meta(Program p, ExpCore ctx, Meta r) {
  ClassB cb=r.getThat();
  assert !IsCompiled.of(cb);
  Member m=Functions.firstIncomplete(cb);
  if(m instanceof NestedClass && IsCompiled.of(((NestedClass)m).getInner())){
    return ReplaceCtx.of(ctx, meta1(p,cb,(NestedClass)m));
  }
  if(m instanceof NestedClass){//&& not is compiled
    return ReplaceCtx.of(ctx, meta1Prop(p,cb,(NestedClass)m));
  }
  return ReplaceCtx.of(ctx, metaMethod(p,cb,m));
}
protected ClassB meta1Prop(Program p, ClassB cb, NestedClass m) {
  log("---meta1Prop--");
  //get cb-->ct
  ClassB ct= Configuration.typeSystem.typeExtraction(p,cb);
  ct=ct.withMember(m.withBody(new WalkBy()));
  //get p'
  Program p1=p.addAtTop(ct);
  //extract e
  ExpCore e=m.match(
      nc->nc.getInner(),
      mi->mi.getInner(),
      mt->mt.getInner().get()
      );
  //extract cb
  Ctx<ClassB> ctxC=ExtractCtxCompiled.of(e);
  //run cb1-->cb2
  ClassB cb2=(ClassB)step(p1, ctxC.hole);
  ExpCore e2=ReplaceCtx.of(ctxC.ctx,cb2);
  //compose cb with new member
  return cb.withMember(m.withBody(e2));
}

protected ClassB metaMethod(Program p, ClassB cb, Member m) {
  log("---meta2--");
  //get cb-->ct
  ClassB ct= Configuration.typeSystem.typeExtraction(p,cb);
  //get p'
  Program p1=p.addAtTop(ct);
  //extract e
  ExpCore e=m.match(
      nc->nc.getInner(),
      mi->mi.getInner(),
      mt->mt.getInner().get()
      );
  //extract cb
  Ctx<ClassB> ctxC=ExtractCtxCompiled.of(e);
  //run cb1-->cb2
  ClassB cb2=(ClassB)step(p1, ctxC.hole);
  ExpCore e2=ReplaceCtx.of(ctxC.ctx,cb2);
  //compose cb with new member
  return cb.withMember(m.withBody(e2));
}

private static ExpCore removeCatch(ExpCore ctxVal, NoThrowRemoveOn r) {
  return ReplaceCtx.of(ctxVal,r.getThat().with_catch(Optional.empty()));
}
private ExpCore methCall(Program p, ExpCore ctxVal, MethCall r) {
  MCall mc = r.getThat();
  Path pathR=Functions.classOf(p, ctxVal, mc.getReceiver());
  MethodWithType mwt = p.method(pathR,new MethodSelector(mc.getName(), mc.getXs()),false);
  if(mwt.getInner().isPresent()){
    return normalMeth(pathR,mwt,ctxVal,mc);
  }
  HashSet<String> usedNames = new HashSet<String>(HB.of(ctxVal, false));
  usedNames.add("this");
  usedNames.addAll(HB.of(mc, false));
  if (!IsValue.isAtom(mc.getReceiver())){
    return ReplaceCtx.of(ctxVal,primCallRec( mc, pathR, mwt, usedNames));
    }
  for(int i=0;i<mc.getEs().size();i++){
    if(!IsValue.isAtom(mc.getEs().get(i))){
      return ReplaceCtx.of(ctxVal,primCallArg(mc,i,mwt,usedNames));
      }
    }
  //case new
  if(mwt.getMt().getMdf()==ast.Ast.Mdf.Type){
    return ReplaceCtx.of(ctxVal,rNew(mc,mwt,usedNames));
  }
  //assert IsValue.of(Resources.getP(),mc.getReceiver());
 // assert new IsValue().validDvs(ctxVal.coreVisitors.Dec.of(ctxVal, ((ExpCore.X)mc.getReceiver()).getInner(),false));
  //case getter-exposer
  if(mwt.getMs().getNames().isEmpty()){
    return fieldA(ctxVal, mc, mwt, usedNames);
  }
  //case setter
  return fieldU(ctxVal, mc);
}
private ExpCore fieldU(ExpCore ctxVal, MCall mc) {
  log("---meth FieldU--");
  String x=((ExpCore.X)mc.getReceiver()).getInner();
  Ctx<Block> _ctx=ExtractCtxUpToX.of(x,ctxVal);
  //ctxVal[mc] --> ctxVal_0[ctxVal_1[mc]]
  //ctxVal_1 is Block, have dvs containing x mut or lent
  ExpCore ctxVal0=_ctx.ctx;
  Block   ctxVal1=_ctx.hole;
  Block.Dec xDec=ctxVal1.getDecs().get(ctxVal1.domDecs().indexOf(x));
  Mdf xMdf=xDec.getNT().getMdf();
  if(xMdf!=Mdf.Mutable && xMdf!=Mdf.Lent){
    throw new ErrorMessage.IllegalAttemptedModification(ctxVal1,xDec,mc);
  }
  ExpCore newVal=mc.getEs().get(0);
  assert IsValue.isAtom(newVal);
  Block ctxVal2=ctxVal1;
  if(newVal instanceof Block.X){
    ctxVal2=Move.of(ctxVal1, ((Block.X)newVal).toString());
    }
  ctxVal2=(Block)ReplaceCtx.of(ctxVal2,new ExpCore._void());
  ctxVal2=ctxVal2.withDecs(Functions.replace(ctxVal2.getDecs(), mc));
  return ReplaceCtx.of(ctxVal0,ctxVal2);
}
private ExpCore fieldA(ExpCore ctxVal, MCall mc, MethodWithType mwt,HashSet<String> usedNames) {
  //case get-exposer
  ExpCore decRec=coreVisitors.Dec.of(ctxVal, ((ExpCore.X)mc.getReceiver()).getInner(),false);
  if(decRec instanceof Block){
    coreVisitors.Dec.of(ctxVal, ((ExpCore.X)mc.getReceiver()).getInner(),true);
    //this may throw errors on purpose
    //fieldABlock
    Set<String> around = new HashSet<String>(HB.of(ctxVal, false));
    return ReplaceCtx.of(ctxVal,fieldABlock(around,mc, mwt, usedNames, (Block)decRec));
    }
  //fieldAObj
  log("---meth fieldA obj--");
  String fName=mc.getName();
  if(fName.startsWith("#")){fName=fName.substring(1);}
  MCall decRec2=(MCall)decRec;
  //assert IsValue.of(Resources.getP(),decRec2);
  for(int i=0;i<decRec2.getXs().size();i++){
    if(!decRec2.getXs().get(i).equals(fName)){continue;}
    return ReplaceCtx.of(ctxVal, decRec2.getEs().get(i));
  }
  throw Assertions.codeNotReachable();
}
private Block fieldABlock(Set<String> around,MCall mc, MethodWithType mwt,HashSet<String> usedNames, Block decRec) {
  log("---meth fieldABlock--");
  HashSet<String> inside = new HashSet<String>(HB.of(decRec, false));
  HashSet<String> forbidden=new HashSet<String>(around);
  forbidden.addAll(inside);
  forbidden.addAll(usedNames);
  HashMap<String,String> renames=new HashMap<String,String>();
  for(String s:inside){
    if(!around.contains(s)){continue;}
    String s2=Functions.freshName(s,forbidden);
    renames.put(s, s2);
  }
  decRec=(Block)RenameVars.of(decRec,renames);
  assert checkSuccessRename(around, decRec);
  Path path1=((NormType)mwt.getMt().getReturnType()).getPath();
  Type tz=new NormType(Mdf.Immutable,path1,Ast.Ph.None);
  String z=Functions.freshName(path1, forbidden);
  MCall mcz=mc.withReceiver(decRec.getInner());
  ExpCore ez=decRec.withInner(mcz);
  Block result=new Block(mc.getSource(),Doc.empty(),new Block.Dec(tz,z,ez),new ExpCore.X(z));
  return result;
}
private ExpCore rNew(MCall mc, MethodWithType mwt,HashSet<String> usedNames) {
  log("---method rNew--");
  NormType t0=(NormType)mwt.getMt().getReturnType();
  String x0=Functions.freshName(t0.getPath(), usedNames);
  return new Block(null,Doc.empty(),new Block.Dec(t0,x0,mc),
    new ExpCore.X(x0));
}
private ExpCore primCallArg(MCall mc, int i, MethodWithType mwt, HashSet<String> usedNames) {
  //String xRole=ms.getXs().get(i);
  log("---primCallArg--");
  NormType ti=(NormType)mwt.getMt().getTs().get(i);
  Path pi=ti.getPath();
  ti=ti.withPh(Ph.None);
  String xi=Functions.freshName(pi,usedNames);
  ExpCore ei=mc.getEs().get(i);
  ArrayList<ExpCore> es = new ArrayList<ExpCore>(mc.getEs());
  es.set(i, new ExpCore.X(xi));
  return new Block(mc.getSource(),Doc.empty(),new Block.Dec(ti,xi,ei),
      mc.withEs(es));
}
private ExpCore primCallRec(MCall mc, Path pathR,MethodWithType mwt, HashSet<String> usedNames) {  NormType t1=new NormType(mwt.getMt().getMdf(),pathR,Ast.Ph.None);
  log("---primCallRec--");
  String x1=Functions.freshName(pathR,usedNames);
  ExpCore e1=mc.getReceiver();
  return new Block(mc.getSource(),Doc.empty(),new Block.Dec(t1,x1,e1),
      mc.withReceiver(new ExpCore.X(x1)));
}
private ExpCore normalMeth(Path pathR,MethodWithType mwt, ExpCore ctxVal, MCall mc) {
  log("---normalMeth--");
  HashSet<String> around = new HashSet<String>(HB.of(ctxVal, false));//TODO: it may be true in a more permissive scoping
  around.add("this");
  HashSet<String> aroundAndParameters = new HashSet<String>(around);
  aroundAndParameters.addAll(HB.of(mc.getReceiver(), false));
  for(ExpCore ei:mc.getEs()){
    aroundAndParameters.addAll(HB.of(ei, false));
  }
  HashSet<String> inside = new HashSet<String>(HB.of(mwt.getInner().get(), false));
  inside.addAll(mwt.getMs().getNames());
  inside.add("this");
  HashSet<String> forbidden=new HashSet<String>(aroundAndParameters);
  forbidden.addAll(inside);
  HashMap<String,String> renames=new HashMap<String,String>();
  for(String s:inside){
    if(!aroundAndParameters.contains(s)){continue;}
    String s2=Functions.freshName(s,forbidden);
    renames.put(s, s2);
  }
  ExpCore e=RenameVars.of(mwt.getInner().get(),renames);
  assert checkSuccessRename(aroundAndParameters, e);
  for(String pari:mwt.getMs().getNames()){
    if(!renames.containsKey(pari)){
      renames.put(pari, pari);
    }
  }
  ArrayList<Block.Dec> decs=new ArrayList<Block.Dec>();
  decs.add( new Block.Dec(
      new NormType(mwt.getMt().getMdf(),pathR,Ast.Ph.None),
      renames.get("this"),
      mc.getReceiver()));

  for(int i=0;i<mc.getEs().size();i++){
    decs.add(new Block.Dec(
      mwt.getMt().getTs().get(i),
      renames.get(mwt.getMs().getNames().get(i)),
      mc.getEs().get(i)));
  }
  Block e2=new Block(mc.getSource(),mc.getDoc(),decs,e);
  //System.out.println(aroundAndParameters+"\n"+ToFormattedText.of(e2)+"\n@@\n@@\n"+ToFormattedText.of(mc));
  assert checkSuccessRename(around, e2);
  ExpCore result= ReplaceCtx.of(ctxVal,e2);
  assert !(result instanceof ExpCore.ClassB) || WellFormedness.checkCoreVariables((ExpCore.ClassB)result);
  return result;
}
private static boolean checkSuccessRename(Set<String> around, ExpCore e) {
  HashSet<String> eIn = new HashSet<String>(HB.of(e, false));
  for(String s:eIn){
    assert !around.contains(s):s;
  }
  return true;
}

private static ExpCore garbage(ExpCore ctxVal,Redex.Garbage r){
  return ReplaceCtx.of(ctxVal,r.getThatLessGarbage());
}
private static ExpCore captureOrNot(Program p,ExpCore ctxVal,Redex.CaptureOrNot r){
  Block e1=r.getThat();
  Signal s=r.getThrowExtracted();
  int i=r.getThrowIndex();
  ArrayList<Block.Dec> decsUpToI = new ArrayList<Block.Dec>();
  for(int ii=0;ii<i;ii++){
    decsUpToI.add(e1.getDecs().get(ii));
    }
  //case notCapture zero ons not here
  //case capture
  //-subtype
  Path c=Functions.classOf(p, ctxVal,decsUpToI, s.getInner());
  Catch catch_ = e1.get_catch().get();
  NormType onT=Norm.of(p, catch_.getOns().get(0).getT());
  //TODO: -hope garbage remove dvx' properly
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(decsUpToI);
  if(s.getKind()==catch_.getKind() && Functions.isSubtype(p, c, onT.getPath())){
    decs.add(new Block.Dec(
      onT,catch_.getX(),s.getInner()));
    Block e2=new Block(e1.getSource(),e1.getDoc(),decs,catch_.getOns().get(0).getInner());
    return ReplaceCtx.of(ctxVal,e2);
    }
  //case notCapture
  decs.add(e1.getDecs().get(i).withE(s));
  return ReplaceCtx.of(ctxVal,rOnMiss(decs,e1));
}
private static ExpCore rOnMiss(ArrayList<Block.Dec> ds,Block b) {
  Block result=removeOneOn(b);
  return result.withDecs(ds);
}
private static Block removeOneOn(Block e1){
  Catch k=e1.get_catch().get();
  Optional<Catch> k2=Optional.empty();
  if(k.getOns().size()!=1){
    ArrayList<On> oneOnLess = new ArrayList<On>(k.getOns().subList(1, k.getOns().size()));
    k2=Optional.of(k.withOns(oneOnLess));
    }
  return e1.with_catch(k2);

}

private ExpCore blockElim(ExpCore ctxVal,Redex.BlockElim r){
  log("-- block elim --");
  //System.out.println("Block elim over"+r.getElimIndex()+ToFormattedText.of(r.getThat()));
  Block e1=r.getThat();
  int ii=r.getElimIndex();
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(e1.getDecs());
  Block eInner=(Block)decs.get(ii).getE();
  assert !eInner.get_catch().isPresent();
  decs.set(ii,decs.get(ii).withE(eInner.getInner()));
  decs.addAll(ii, eInner.getDecs());
  //Note: we lose the docs on eInner, is it ok?
  Block result=e1.withDecs(decs);
  return ReplaceCtx.of(ctxVal,result);
}
public static ExpCore subst(ExpCore ctxVal,Redex.Subst r){
  Block e1=r.getThat();
  int i=r.getSubstIndex();
  ExpCore val=e1.getDecs().get(i).getE();
  String x=e1.getDecs().get(i).getX();
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(e1.getDecs());
  decs.remove(i);
  Block e2=new Block(e1.getSource(),e1.getDoc(),decs,e1.getInner(),e1.get_catch());
  ExpCore result=ReplaceX.of(e2,val,x);
  return ReplaceCtx.of(ctxVal,result);
}
private static ExpCore ph(Program p,ExpCore ctxVal,Redex.Ph r){
  Block b=r.getThat();
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(b.getDecs());
  Dec deci = decs.get(r.getPhIndex());
  NormType ti=deci.getNT();
  Path path=Functions.classOf(p, ctxVal, deci.getE());
  ti=ti.withPh(Ph.None).withPath(path);
  decs.set(r.getPhIndex(),deci.withT(ti));
  return ReplaceCtx.of(ctxVal,b.withDecs(decs));
}
}
package reduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import platformSpecific.javaTranslation.Resources;
import programReduction.Program;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.Map;
import tools.Match;
import coreVisitors.*;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.L42;
import facade.PData;
import facade.Parser;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
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
import auxiliaryGrammar.Ctx;
import auxiliaryGrammar.Functions;
import ast.ExpCore.*;
import ast.ExpCore.Block.*;
import ast.ExpCore.ClassB.*;
import ast.Redex;
import ast.Redex.*;

abstract public class Executor {
  abstract protected ExpCore executeAtomicStep(PData p1, ExpCore e1,Ast.C nestedName);
  abstract protected ClassB meta1(Program p, ClassB cb, NestedClass m);
  abstract protected void log(String s);
  static public ExpCore last1=null;
  static public ExpCore last2=null;

public static void dbgRecordNext(ExpCore e){
  last2=last1;
  last1=e;
  }
public static ExpCore stepStar(Executor executer,ExpCore e){
  final Program emptyP=Program.emptyLibraryProgram();
  int iteration=0;
  dbgRecordNext(e);
  executer.log("--------------------"+(iteration+=1));
  //TestHelper.dbgCompact(e);
  try{while(true){
    assert coreVisitors.CheckNoVarDeclaredTwice.of((ClassB) e);
    e=NormalizeBlocks.of(e);
    e=executer.step(new PData(emptyP),e);
    dbgRecordNext(e);
    assert coreVisitors.CheckNoVarDeclaredTwice.of((ClassB) e);
    executer.log("--------------------"+(iteration+=1));
    //TestHelper.dbgCompact(e);
  }}
  //catch(ErrorMessage.NormalForm mess){ return mess.getE();}
  catch(ErrorMessage.CtxExtractImpossible mess){
    assert e instanceof ClassB;
    if(!L42.trustPluginsAndFinalProgram){
      ClassB ct=(ClassB)e;
      Program p1=emptyP.evilPush(ct);
      assert false;//I think this is all old code?
      //Configuration.typeSystem.checkCt( emptyP, ct);
      /*if(!p1.checkComplete()){//also check is star
        throw new ErrorMessage.MalformedFinalResult(ct,
          "Some class can not be completely typed as is still incomplete or refers to incomplete classes"
            +ErrorFormatter.reportPlaceOfMetaError(p1,ct)
            );
        }*/
      }
    return e;}
}
final public ExpCore step(PData p,ExpCore e){
  throw Assertions.codeNotReachable();//the whole reduction will soon disappear
  /*try{
  Ctx<Redex> ctx=ExtractCtxVal.of(p.p, e);
  log("---REDEX--: "+ctx.hole.getClass().getSimpleName());
  return Match.<ExpCore>of(ctx.hole)
      .add(Redex.Garbage.class, r->garbage(ctx.ctx,r))
      .add(Redex.CaptureOrNot.class, r->captureOrNot(p.p,ctx.ctx,r))
      .add(Redex.BlockElim.class, r->blockElim(ctx.ctx,r))
      .add(Redex.Subst.class, r->subst(ctx.ctx,r))
      .add(Redex.MethCall.class, r->methCall(p.p,ctx.ctx,r))
      .add(Redex.Ph.class, r->ph(p.p,ctx.ctx,r))
      .add(Redex.NoThrowRemoveOn.class, r->removeCatch(ctx.ctx,r))
            .add(Redex.LoopR.class, r->loopR(ctx.ctx,r))
      .add(Redex.UsingOut.class, r->usingOut(p.p,ctx.ctx,r))
      .add(Redex.Using.class, r->using(p.p,ctx.ctx,r))
      .add(Redex.Meta.class, r->meta(p.p,ctx.ctx,r))
      .end();}
catch(ErrorMessage.CtxExtractImpossible rethrow){
  throw rethrow;//to debug
  
}*/
}

public static ExpCore subst(ExpCore ctxVal,Redex.Subst r){
Block e1=r.getThat();
int i=r.getSubstIndex();
ExpCore val=e1.getDecs().get(i).getInner();
String x=e1.getDecs().get(i).getX();
ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(e1.getDecs());
decs.remove(i);
Block e2=new Block(e1.getDoc(),decs,e1.getInner(),e1.getOns(),e1.getP());
ExpCore result=ReplaceX.of(e2,val,x);
return ReplaceCtx.of(ctxVal,result);
}
protected ClassB meta1Prop(Program p, ClassB cb, NestedClass m) {
log("---meta1Prop--");
//get cb-->ct
//get p'
Program p1=p.evilPush(cb);
//extract e
ExpCore e=m.getInner();
//extract cb
Ctx<ClassB> ctxC=ExtractCtxCompiled.of(e);
//run cb1-->cb2
ClassB cb2=(ClassB)step(new PData(p1), ctxC.hole);
ExpCore e2=ReplaceCtx.of(ctxC.ctx,cb2);

//compose cb with new member
return cb.withMember(m.withInner(e2));
}

protected ClassB metaMethod(Program p, ClassB cb, Member m) {
log("---meta2--");
//get cb-->ct
//get p'
Program p1=p.evilPush(cb);
//extract e
ExpCore e=m.getInner();
//extract cb
Ctx<ClassB> ctxC=ExtractCtxCompiled.of(e);
//run cb1-->cb2
ClassB cb2=(ClassB)step(new PData(p1), ctxC.hole);
ExpCore e2=ReplaceCtx.of(ctxC.ctx,cb2);
//compose cb with new member
return cb.withMember(m.withInner(e2));
}
/*
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
  Block.Dec xDec=new Block.Dec(NormType.immVoid,x,newInner);
  Ast.Position pos=null; if(r.getThat().getInner() instanceof Ast.HasPos){pos=((Ast.HasPos)r.getThat().getInner()).getP();}
  Block result=new Block(Doc.empty(),Collections.singletonList(xDec),r.getThat(),Collections.emptyList(),pos);
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

private static ExpCore removeCatch(ExpCore ctxVal, NoThrowRemoveOn r) {
  return ReplaceCtx.of(ctxVal,r.getThat().withOns(Collections.emptyList()));
}

private ExpCore methCall(Program p, ExpCore ctxVal, MethCall r) {
  MCall mc = r.getThat();
  Path pathR=Functions.classOf(p, ctxVal, mc.getInner());
  MethodWithType mwt = p.method(pathR,mc.getS(),mc,false);
  if(mwt.get_inner().isPresent()){
    return normalMeth(pathR,mwt,ctxVal,mc);
  }
  HashSet<String> usedNames = new HashSet<String>(HB.of(ctxVal, false));
  usedNames.add("this");
  usedNames.addAll(HB.of(mc, false));
  if (!IsValue.isAtom(mc.getInner())){
    return ReplaceCtx.of(ctxVal,primCallRec( mc, pathR, mwt, usedNames));
    }
  for(int i=0;i<mc.getEs().size();i++){
    if(!IsValue.isAtom(mc.getEs().get(i))){
      return ReplaceCtx.of(ctxVal,primCallArg(p,mc,i,mwt,usedNames));
      }
    }
  //case new
  if(mwt.getMt().getMdf()==ast.Ast.Mdf.Class){
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
  String x=((ExpCore.X)mc.getInner()).getInner();
  Ctx<Block> _ctx=ExtractCtxUpToX.of(x,ctxVal);
  //ctxVal[mc] --> ctxVal_0[ctxVal_1[mc]]
  //ctxVal_1 is Block, have dvs containing x mut or lent
  ExpCore ctxVal0=_ctx.ctx;
  Block   ctxVal1=_ctx.hole;
  Block.Dec xDec=ctxVal1.getDecs().get(ctxVal1.domDecs().indexOf(x));
  Mdf xMdf=xDec.getT().getNT().getMdf();
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
  ExpCore decRec=coreVisitors.Dec.of(ctxVal, ((ExpCore.X)mc.getInner()).getInner(),false);
  if(decRec instanceof Block){
    coreVisitors.Dec.of(ctxVal, ((ExpCore.X)mc.getInner()).getInner(),true);
    //this may throw errors on purpose
    //fieldABlock
    Set<String> around = new HashSet<String>(HB.of(ctxVal, false));
    return ReplaceCtx.of(ctxVal,fieldABlock(around,mc, mwt, usedNames, (Block)decRec));
    }
  //fieldAObj
  log("---meth fieldA obj--");
  String fName=mc.getS().nameToS();
  if(fName.startsWith("#")){fName=fName.substring(1);}
  MCall decRec2=(MCall)decRec;
  //assert IsValue.of(Resources.getP(),decRec2);
  for(int i=0;i<decRec2.getS().getNames().size();i++){
    if(!decRec2.getS().getNames().get(i).equals(fName)){continue;}
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
  Type tz=path1.toImmNT();
  String z=Functions.freshName(path1, forbidden);
  MCall mcz=mc.withInner(decRec.getInner());
  ExpCore ez=decRec.withInner(mcz);
  Block result=new Block(Doc.empty(),Collections.singletonList(new Block.Dec(tz,z,ez)),new ExpCore.X(z),Collections.emptyList(),mc.getP());
  return result;
}
private ExpCore rNew(MCall mc, MethodWithType mwt,HashSet<String> usedNames) {
  log("---method rNew--");
  NormType t0=(NormType)mwt.getMt().getReturnType();
  String x0=Functions.freshName(t0.getPath(), usedNames);
  return new Block(Doc.empty(),Collections.singletonList(new Block.Dec(t0,x0,mc)),
    new ExpCore.X(x0),Collections.emptyList(),mc.getP());
}
private ExpCore primCallArg(Program p,MCall mc, int i, MethodWithType mwt, HashSet<String> usedNames) {
  //String xRole=ms.getXs().get(i);
  log("---primCallArg--");
  NormType ti=mwt.getMt().getTs().get(i).match(n->n, hType->Norm.resolve(p,hType));
  Path pi=ti.getPath();
  ti=Functions.toComplete(ti);
  String xi=Functions.freshName(pi,usedNames);
  ExpCore ei=mc.getEs().get(i);
  ArrayList<ExpCore> es = new ArrayList<ExpCore>(mc.getEs());
  es.set(i, new ExpCore.X(xi));
  return new Block(Doc.empty(),Collections.singletonList(new Block.Dec(ti,xi,ei)),
      mc.withEs(es),Collections.emptyList(),mc.getP());
}
private ExpCore primCallRec(MCall mc, Path pathR,MethodWithType mwt, HashSet<String> usedNames) { 
  NormType t1=Functions.toComplete(new NormType(mwt.getMt().getMdf(),pathR,Doc.empty()));
  log("---primCallRec--");
  String x1=Functions.freshName(pathR,usedNames);
  ExpCore e1=mc.getInner();
  return new Block(Doc.empty(),Collections.singletonList(new Block.Dec(t1,x1,e1)),
      mc.withInner(new ExpCore.X(x1)),Collections.emptyList(),mc.getP());
}
private ExpCore normalMeth(Path pathR,MethodWithType mwt, ExpCore ctxVal, MCall mc) {
  log("---normalMeth--");
  HashSet<String> around = new HashSet<String>(HB.of(ctxVal, false));//TODO: it may be true in a more permissive scoping
  around.add("this");
  HashSet<String> aroundAndParameters = new HashSet<String>(around);
  aroundAndParameters.addAll(HB.of(mc.getInner(), false));
  for(ExpCore ei:mc.getEs()){
    aroundAndParameters.addAll(HB.of(ei, false));
  }
  HashSet<String> inside = new HashSet<String>(HB.of(mwt.getInner(), false));
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
  ExpCore e=RenameVars.of(mwt.getInner(),renames);
  assert checkSuccessRename(aroundAndParameters, e);
  for(String pari:mwt.getMs().getNames()){
    if(!renames.containsKey(pari)){
      renames.put(pari, pari);
    }
  }
  ArrayList<Block.Dec> decs=new ArrayList<Block.Dec>();
  decs.add( new Block.Dec(
          Functions.toComplete(new NormType(mwt.getMt().getMdf(),pathR,Doc.empty())),
      renames.get("this"),
      mc.getInner()));

  for(int i=0;i<mc.getEs().size();i++){
    decs.add(new Block.Dec(
      mwt.getMt().getTs().get(i),
      renames.get(mwt.getMs().getNames().get(i)),
      mc.getEs().get(i)));
  }
  Block e2=new Block(mc.getDoc(),decs,e,Collections.emptyList(),mc.getP());
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
  List<On> catch_ = e1.getOns();
  assert !catch_.isEmpty();
  On on=catch_.get(0);
  NormType onT=Norm.of(p, on.getT());
  //TODO: -hope garbage remove dvx' properly
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(decsUpToI);
  if(s.getKind()==on.getKind() && Functions.isSubtype(p, c, onT.getPath())){
    decs.add(new Block.Dec(
      onT,on.getX(),s.getInner()));
    Block e2=new Block(e1.getDoc(),decs,on.getInner(),Collections.emptyList(),e1.getP());
    return ReplaceCtx.of(ctxVal,e2);
    }
  //case notCapture
  decs.add(e1.getDecs().get(i).withInner(s));
  return ReplaceCtx.of(ctxVal,rOnMiss(decs,e1));
}
private static ExpCore rOnMiss(ArrayList<Block.Dec> ds,Block b) {
  Block result=removeOneOn(b);
  return result.withDecs(ds);
}
private static Block removeOneOn(Block e1){
  List<On> k = e1.getOns();
  assert !k.isEmpty();
  return e1.withOns(k.subList(1, k.size()));
}

private ExpCore blockElim(ExpCore ctxVal,Redex.BlockElim r){
  log("-- block elim --");
  //System.out.println("Block elim over"+r.getElimIndex()+ToFormattedText.of(r.getThat()));
  Block e1=r.getThat();
  int ii=r.getElimIndex();
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(e1.getDecs());
  Block eInner=(Block)decs.get(ii).getInner();
  assert eInner.getOns().isEmpty();
  decs.set(ii,decs.get(ii).withInner(eInner.getInner()));
  decs.addAll(ii, eInner.getDecs());
  //Note: we lose the docs on eInner, is it ok?
  Block result=e1.withDecs(decs);
  return ReplaceCtx.of(ctxVal,result);
}

private static ExpCore ph(Program p,ExpCore ctxVal,Redex.Ph r){
  Block b=r.getThat();
  ArrayList<Block.Dec> decs = new ArrayList<Block.Dec>(b.getDecs());
  Dec deci = decs.get(r.getPhIndex());
  NormType ti=deci.getT().getNT();
  Path path=Functions.classOf(p, ctxVal, deci.getInner());
  ti=Functions.toComplete(ti).withPath(path);
  decs.set(r.getPhIndex(),deci.withT(ti));
  return ReplaceCtx.of(ctxVal,b.withDecs(decs));
}
*/
}
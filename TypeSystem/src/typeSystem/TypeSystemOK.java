package typeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import coreVisitors.IsCompiled;
import facade.Configuration;
import facade.L42;
import profiling.Timer;
import tools.Assertions;
import ast.Ast;
import ast.ErrorMessage;
import ast.Ast.SignalKind;
import ast.Ast.Stage;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.NormType;
import ast.Ast.Mdf;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodImplemented;
import ast.ExpCore.ClassB.MethodWithType;
import ast.Util.CachedStage;
import ast.Util.PathMwt;
import ast.ExpCore.*;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TypeSystemOK {

  public static void checkAll(Program p){
    assert !p.isEmpty();
    if(!p.pop().isEmpty()){checkAll(p.pop());}
    checkTop(p);
  }

  /**pre: p.getStage()==Meta
    modify p putting * or - labelled
    throws typechecking error
    */
  public static void checkTop(Program p){
    Program p1=p.pop();
    ClassB ct = Program.replaceWalkByWith(p.topCb(),new Signal(SignalKind.Error, new _void()));
    //it is ok, indeed we check the program only when there is a meta expression in the path of the program, so
    //the line itself can not be typechecked yet!
    //This means that we cut off some cases: some classes may enjoy  the type information for some classes on the side branches.
    //but all the use cases of such solution looks suspisciuos?
    checkCt(p1,ct);
  }

/*
  public static void checkAll(Program p0){
    assert !p0.isEmpty();
    Program p1=p0.pop();
    ExpCore replacement=new Signal(SignalKind.Error, new _void());
    ClassB ct = Program.replaceWalkByWith(p0.top(),replacement);
    Program p=pWalkByEqual(p1,ct);
    //it is ok, indeed we check the program only when there is a meta expression in the path of the program, so
    //the line itself can not be typechecked yet!
    //This means that we cut off some cases: some classes may enjoy  the type information for some classes on the side branches.
    //but all the use cases of such solution looks suspisciuos?
    checkCt(p,ct);
    if(!p.isEmpty()){checkAll(p);}
  }

  private static Program pWalkByEqual(Program p,ExpCore replacement){
    if(p.isEmpty()){return p;}
    ClassB ct = Program.replaceWalkByWith(p.top(),replacement);
    return p.pop().addAtTop(ct);
  }

  public static void checkTop(Program p0){
    Program p1=p0.pop();
    ExpCore replacement=new Signal(SignalKind.Error, new _void());
    ClassB ct = Program.replaceWalkByWith(p0.top(),replacement);
    Program p=pWalkByEqual(p1,ct);
    //it is ok, indeed we check the program only when there is a meta expression in the path of the program, so
    //the line itself can not be typechecked yet!
    //This means that we cut off some cases: some classes may enjoy  the type information for some classes on the side branches.
    //but all the use cases of such solution looks suspisciuos?
    checkCt(p,ct);
  }*/
  /**
   * @param p can be modified adding annotations// TODO: really? test for it?
   * @param cb
   * @throws typechecking error
   */
  public static boolean isExePlus(ClassB cb){
    if(cb.getStage().getStage()==Stage.Less){return false;}
    if(!IsCompiled.of(cb)){return false;}
    return true;
  }
  public static void checkCt(Program p,ClassB cb){
    //dispatch over check ct1 or check ct2
    /*try{Norm.ctorOkAndAllCanBeNormalized(p, cb);}
    //TODO: never forgot: here 23/3/2015 by not getting the norm for
    //every method body as an optimization
    //while cheching the constructor is well formed,
    //I do not get the error under!
    catch(ErrorMessage prop){checkCt1(p,cb);return;}
    checkCt2(p,cb);
    //TODO: now 27/07/2015 I still get trouble for not normalizable stuff
    */
    if(cb.getStage().getStage()==Stage.Star || cb.getStage().getStage()==Stage.Plus){
      try{checkCt2(p,cb);return;}
      catch(ErrorMessage.NormImpossible __){checkCt1(p,cb);return;}
      catch(ErrorMessage.PathNonExistant __){checkCt1(p,cb);return;}//this happens when
      //a class would require the current metaprogrammed computed class to be already completed.
    }
    else{checkCt1(p,cb);return;}
    }
  private static void checkCt1(Program p, ClassB ct) {
    for(Member m:ct.getMs()){
      m.match(
        nc->{
          if(!(nc.getInner() instanceof ClassB)){return null;}
          //Program p1=p.addAtTop(null,ct.withMember(m.withBody(new WalkBy())));
          Program p1=p.addAtTop(ct);
          checkCt(p1,(ClassB)nc.getInner());
          return null;
          },
        mi->null,
        mt->null
        );};
  }

  private static void checkCt2(Program p, ClassB ct) {
    if(L42.trustPluginsAndFinalProgram){if(ct.getStage().isVerified()){return;}}
    assert ct.getStage().getGivenName()!=null;
    String name=ct.getStage().getGivenName();
    if(!name.isEmpty()){Timer.activate("TypeSystem.checkCt2."+name);}try{
    for(Member m:ct.getMs()){
      try{checkCt2Member(p, ct, m);}
      catch(ErrorMessage err){ throw ErrorMessage.PosImprove.improve(err, m.getP());}
      }
    }finally{if(!name.isEmpty()){Timer.deactivate("TypeSystem.checkCt2."+name);}}
    ct.getStage().setVerified(true);
  }

  private static void checkCt2Member(Program p, ClassB ct, Member m) {
    m.match(
      nc->{
        assert nc.getInner() instanceof ClassB;
        //Program p1=p.addAtTop(null,ct.withMember(m.withBody(new WalkBy())));
        Program p1=p.addAtTop(ct);
        checkCt(p1,(ClassB)nc.getInner());
        return null;
        },
      mi->{methodOk(p.addAtTop(ct),mi,ct.getStage());return null;},
      mt->{
        methodOk(p.addAtTop(ct),mt);
        return null;
        }
      );
  }

  private static void methodOk(Program p, MethodImplemented mi,CachedStage c) {
    for(PathMwt mt:c.getInherited()){
      if(!mt.getMwt().getMs().equals(mi.getS())){continue;}
      methodOk(p,mt.getMwt().withInner(mi.getInner()));
      return;
    }
    throw Assertions.codeNotReachable();
  }
  private static void methodOk(Program p, MethodWithType mt) {
    //if star
    //check all types of parameters exists
    //check all types of local var exists
    mt=Norm.of(p, mt,false);
    checkExists(p,mt.getMt().getReturnType(),mt.getP());
    for(Type t:mt.getMt().getTs() ){
      checkExists(p,t,mt.getP());
      }
    for(Path pi:mt.getMt().getExceptions()){
      checkExists(p,pi,mt.getP());
    }
    if(!mt.get_inner().isPresent()){return;}
    HashMap<String,NormType> varEnv=new HashMap<>();
    varEnv.put("this",new NormType(mt.getMt().getMdf(),Path.outer(0),Ph.None));
    {int i=-1;for(String parName:mt.getMs().getNames()){i+=1;
      Type ti=mt.getMt().getTs().get(i);
      varEnv.put(parName, (NormType)ti);
    }}
    SealEnv sealEnv=SealEnv.empty();
    sealEnv.xssK.add(new HashSet<>(varEnv.keySet()));
    ThrowEnv throwEnv=new ThrowEnv();
    throwEnv.exceptions.addAll(mt.getMt().getExceptions());
    NormType suggested=Functions.toPartial((NormType)mt.getMt().getReturnType());
    p.exePlusOk(varEnv);
    TypeSystem.typecheckSure(
      false,
      p,
      varEnv,
      sealEnv,
      throwEnv,
      suggested,
      mt.getInner());
  }

  private static Void checkExists(Program p,Path pi,Ast.Position pos) {
    if(pi.isPrimitive()){return null;}
    try{p.extractCb(pi);}
    catch(ErrorMessage.ProgramExtractOnMetaExpression err){
      throw new ErrorMessage.PathNonExistant(pi.getCBar(),null,pos);
    }
    return null;
  }

  private static void checkExists(Program p,Type returnType,Ast.Position pos) {
    returnType.match(nt->checkExists(p,nt.getPath(),pos), hType->checkExists(p,hType.getPath(),pos));
  }
}

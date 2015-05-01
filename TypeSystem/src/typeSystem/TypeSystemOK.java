package typeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import coreVisitors.IsCompiled;
import facade.Configuration;
import facade.L42;
import tools.Assertions;
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
import ast.ExpCore.ClassB.MethodWithType;
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
    ClassB ct = Program.replaceWalkByWith(p.top(),new Signal(SignalKind.Error, new _void()));
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
    if(cb.getStage()==Stage.Less){return false;}
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
    */
    if(cb.getStage()==Stage.Star || cb.getStage()==Stage.Plus){checkCt2(p,cb);}
    else{checkCt1(p,cb);}
    }
  private static void checkCt1(Program p, ClassB cb) {
    for(Member m:cb.getMs()){
      m.match(
        nc->{
          if(!(nc.getInner() instanceof ClassB)){return null;}
          Program p1=p.addAtTop(cb.withMember(m.withBody(new WalkBy())));
          checkCt(p1,(ClassB)nc.getInner());
          return null;          
          },
        mi->null,
        mt->null
        );};    
  }

  private static void checkCt2(Program p, ClassB ct) {
    for(Member m:ct.getMs()){
      m.match(
        nc->{
          assert nc.getInner() instanceof ClassB;
          Program p1=p.addAtTop(ct.withMember(m.withBody(new WalkBy())));
          checkCt(p1,(ClassB)nc.getInner());
          return null;          
          },
        mi->{throw Assertions.codeNotReachable();},
        mt->{
          methodOk(p.addAtTop(ct),mt);
          return null;
          }
        );};    
  }


  private static void methodOk(Program p, MethodWithType mt) {
    //if star
    //check all types of parameters exists
    //check all types of local var exists
    mt=Norm.of(p, mt,false);
    if(!mt.getInner().isPresent()){return;}
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
    NormType suggested=(NormType)mt.getMt().getReturnType();
    p.exePlusOk(varEnv);
    TypeSystem.typecheckSure(
      false,
      p,
      varEnv,
      sealEnv,
      throwEnv,
      suggested,
      mt.getInner().get());
  }
}

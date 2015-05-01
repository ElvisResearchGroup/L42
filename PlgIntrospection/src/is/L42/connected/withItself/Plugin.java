package is.L42.connected.withItself;

import introspection.IntrospectionAdapt;
import introspection.IntrospectionSum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import facade.Configuration;
import facade.L42;
import sugarVisitors.Desugar;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast;
import ast.Ast.MethodType;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Stage;
import ast.Ast.TraitHeader;
import ast.Ast.Type;
import ast.Ast.MethodSelector;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.*;
import static auxiliaryGrammar.EncodingHelper.*;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class Plugin implements PluginType{


  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object Mget£that£node(Object _lib,Object _path){
    ClassB lib=ensureExtractClassB(_lib);
    Path path=ensureExtractInternalPath(_path);
    //ClassB target = getTarget(lib, path);
    Program target = getProgramTarget(Resources.getP(),lib, path);
    ClassB cb=target.top();
    target=target.pop();
    cb=(ClassB)cb.accept(new CloneVisitorWithProgram(target){
      public ExpCore visit(Path s) {
        Path s1= Norm.of(this.p, s);
        return s1;//toDebug
        }
    });
    return ToFormattedText.of(cb);}

  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetOrElse£that£interfaceNum£node(Object _lib,Object _num,Object _path){
    ClassB lib=ensureExtractCt(_lib,Resources.getP());
    Path path=ensureExtractInternalPath(_path);
    Integer num=ensureExtractInt32(_num);
    ClassB target = getTarget(lib, path);
    if(!path.isCore()){path=Path.outer(0,path.getRowData());}
    Path result=ifExists(()->target.getSupertypes().get(num));
    result=From.fromP(result, path);
    return buildAdapterIn_or_AdapterOut(result);
  }

  //getOrElse(that:ClassB  methodNum:num exceptionNum:num node:Path)  -> ClassAdaptForm    
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetOrElse£that£methodNum£exceptionNum£node(Object _lib,Object _mNum,Object _eNum,Object _path){
    MethodWithType m = extractMethod(_lib, _mNum, _path);
    Integer eNum=ensureExtractInt32(_eNum);
    Path result=ifExists(()->m.getMt().getExceptions().get(eNum));
    return buildAdapterIn_or_AdapterOut(result);
  }
  
  //%1-9
  //% -getXXXOrElse(that:ClassB methodNum:num node:Path )
  //%[9]NameSuffixesMethod:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc/DocExceptions
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetOrElse£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    return ToFormattedText.of(extractMethod(_lib, _mNum, _path));}
//%2-9
  //% -getXXXOrElse(that:ClassB methodNum:num node:Path )
  //%[9]NameSuffixesMethod:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc/DocExceptions
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetNameOrElse£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    MethodWithType result = extractMethod(_lib, _mNum, _path);
    return result.getMs().toString();}
  //%3-9
  //% -getXXXOrElse(that:ClassB methodNum:num node:Path )
  //%[9]NameSuffixesMethod:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc/DocExceptions
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetMdfOrElse£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    MethodWithType result = extractMethod(_lib, _mNum, _path);
    return result.getMt().getMdf().name();}
  //%4-9
  //% -getXXXOrElse(that:ClassB methodNum:num node:Path )
  //%[9]NameSuffixesMethod:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc/DocExceptions
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetTypeOrElse£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    MethodWithType result = extractMethod(_lib, _mNum, _path);
    return ToFormattedText.of(result.getMt().getReturnType());}
  //%5-9
  //% -getXXXOrElse(that:ClassB methodNum:num node:Path )
  //%[9]NameSuffixesMethod:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc/DocExceptions
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetTypePathOrElse£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    MethodWithType result = extractMethod(_lib, _mNum, _path);
    return buildTypePathAdapter(result.getMt().getReturnType());}
    
  //%10-9
  //% -ifIsFieldDo(that:ClassB methodNum:num node:Path )
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MifIsFieldDo£that£methodNum£node(Object _lib,Object _mNum,Object _path){
    MethodWithType result = extractMethod(_lib, _mNum, _path);
    boolean isAbstract=!result.getInner().isPresent();
    if(!isAbstract){return Resources.Void.instance;}
    throw Resources.notAct;//allows action from 42
    }
  //%1-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    StringBuilder b=new StringBuilder();
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{
      b.append(ToFormattedText.of(t));
      b.append(" ");
      b.append(n);
      if(!doc.isEmpty()){b.append(ToFormattedText.of(doc));}
    });
    return b.toString();}
  //%2-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetNameOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    StringBuilder b=new StringBuilder();
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{
      b.append(n);
    });
    return b.toString();}
  //%3-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetTypeOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    StringBuilder b=new StringBuilder();
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{
      b.append(ToFormattedText.of(t));
    });
    return b.toString();}
  //%4-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetTypePathOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    Type[]ts=new Type[1];
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{ts[0]=t;});
    return buildTypePathAdapter(ts[0]);}
  //%5-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetTypeMdfOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    Type[]ts=new Type[1];
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{ts[0]=t;});
    return buildTypeMdf(ts[0]);}
  //%6-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Void,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Resources.Void MifTypePhDo£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    Type[]ts=new Type[1];
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{ts[0]=t;});
    boolean b=buildTypePh(ts[0]);
    if(b){return Resources.Void.instance;}
    throw Resources.notAct;}  
  //%7-7
  //% -getXXXOrElse(that:ClassB methodNum:num parameterNum:num node:Path )
  //%[7]NameSuffixesMethodParameter:{}/Name/Mdf/Type/TypePath/TypeMdf/TypePh/Doc
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetDocOrElse£that£methodNum£parameterNum£node(Object _lib,Object _mNum,Object _pNum,Object _path){
    StringBuilder b=new StringBuilder();
    extractField(_lib,_mNum,_pNum,_path,(n,t,doc)->{
      b.append(doc);
    });
    return b.toString();}

  
  public void extractField(Object _lib, Object _mNum,Object _pNum, Object _path,
      StringBuilders.TriConsumer<String,Type,Doc> consumer) {  
    MethodWithType m = extractMethod(_lib, _mNum, _path);
    Integer pNum=ensureExtractInt32(_pNum);
    String n=ifExists(()->m.getMs().getNames().get(pNum));
    Type t=ifExists(()->m.getMt().getTs().get(pNum));
    Doc tDoc = ifExists(()->m.getMt().getTDocs().get(pNum));
    consumer.accept(n, t, tDoc);
    }
  
  private Object buildTypePathAdapter(Type t) {
    NormType nt=null;
    if(t instanceof NormType){  nt = (NormType)t; }
    else {
      assert false:" fix it, now is unlikly to work since the type t refers to the wrong scope";
      //TODO: fixme, either do a from a priori, and then check the buildAdapterIn_or_AdapterOut
     //method, or just append to getP() the originating class of t
      try{ nt=Norm.of(Resources.getP(), t);}   
      catch(ErrorMessage err){throw Resources.notAct;}
    }
    return buildAdapterIn_or_AdapterOut(nt.getPath());
  }
  public String buildTypeMdf(Type t) {
    NormType nt=null;
    if(t instanceof NormType){  nt = (NormType)t; }
    try{ nt=Norm.of(Resources.getP(), t);}
    catch(ErrorMessage err){throw Resources.notAct;}
    return nt.getMdf().name();
  }
  public boolean buildTypePh(Type t) {
    NormType nt=null;
    if(t instanceof NormType){  nt = (NormType)t; }
    try{ nt=Norm.of(Resources.getP(), t);}
    catch(ErrorMessage err){throw Resources.notAct;}
    return nt.getPh()==Ph.Ph;
  }
  
  public MethodWithType extractMethod(Object _lib, Object _mNum, Object _path) {
    ClassB lib=ensureExtractCt(_lib,Resources.getP());
    Path path=ensureExtractInternalPath(_path);
    Integer mNum=ensureExtractInt32(_mNum);
   ClassB target = getTarget(lib, path);
    if(!path.isCore()){path=Path.outer(0,path.getRowData());}
    MethodWithType result = ifExists(()->extractM(target,mNum));
    result=From.from(result, path);
    return result;
  }
  
  
  
  public Object buildAdapterIn_or_AdapterOut(Path result) {

    if(result.outerNumber()==0){
      Path oldRes=result;
      result=new Path(result.getCBar());
      assert !result.isCore():oldRes;
      ClassB adapter=(ClassB) IntrospectionAdapt.buildPathAdapterIn(result);
      //System.out.println("Adapter is:"+ToFormattedText.of(adapter));
      return adapter;
      //return validateResult(pInternal,adapter,null,null,result);
    }
    assert result.outerNumber()>0:result;
    result=Path.outer(result.getN()-1,result.getCBar());
    ClassB adapter= (ClassB) IntrospectionAdapt.buildPathAdapterOut(result);
    return adapter;
  }   
    
  private static MethodWithType extractM(ClassB target,int mNum){
    for(Member m:target.getMs()){
      if(m instanceof MethodWithType){
        if(mNum==0){return (MethodWithType)m;}
        mNum--;
      }
    }
    throw new IndexOutOfBoundsException();
  }
  private static <T> T ifExists(Supplier<T> f){
    try{ return f.get();}    
    catch(IndexOutOfBoundsException err){throw Resources.notAct;}
    }
  
  public static ClassB getTarget(ClassB lib, Path path) {
    ClassB target=lib;
    if(!path.equals(Path.outer(0))){
      try{
        target=Program.extractCBar(path.getRowData(), lib);
      }catch(ErrorMessage err){throw new Resources.Error(err);}}
    return target;
  }  
  public static Program getProgramTarget(Program p,ClassB lib, Path path) {
    return getProgramTarget(p,lib,path.getCBar());
  }    
  public static Program getProgramTarget(Program p,ClassB lib, List<String>path) {
    if(path.isEmpty()){   return p.addAtTop(lib);  }
    Optional<Member> optNc = Program.getIfInDom(lib.getMs(),path.get(0));
    if(!optNc.isPresent()){
      throw new Resources.Error("PathNotExistant:"+path);
      }
    NestedClass nc=(NestedClass)optNc.get();
    ExpCore ec=nc.getInner();
    if(ec instanceof ExpCore.WalkBy){throw new Resources.Error("ProgramExtractOnWalkBy");}
    if(!(ec instanceof ClassB)){throw new Resources.Error("ProgramExtractOnMetaExpression");}
    ClassB cb=(ClassB)nc.getInner();
    return getProgramTarget(p.addAtTop(lib.withMember(nc.withInner(new WalkBy()))),cb,path.subList(1,path.size()));
    }
  // sum
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MsumLib£l1£l2(Object cb1,Object cb2){
    ClassB l1=extractClassB(cb1);
    ClassB l2=extractClassB(cb2);
    if (l1==null ||l2==null){throw new Resources.Error("InvalidClassB");}
    ExpCore result=IntrospectionSum.sum(l1, l2,Path.outer(0));
    //return validateResult(pInternal, result,l1,l2,null);
    return result;
  }
  /*public static ExpCore validateResult(Program p, ExpCore result,ClassB l1,ClassB l2,Path path) {
    if(!(result instanceof ClassB)){return result;}
    boolean strict=true;
    if(l1!=null){
      ClassB ct1= Configuration.typeExtraction.of(p,l1);
      if(ct1.getStage()==Stage.Less){strict=false;}
    }
    if(l2!=null){
      ClassB ct2= Configuration.typeExtraction.of(p,l2);
      if(ct2.getStage()==Stage.Less){strict=false;}
    }
    if(path!=null && path.isCore()){
      if(p.extract(path).getStage()==Stage.Less){strict=false;}
    }
    ClassB ct= Configuration.typeExtraction.of(p,(ClassB)result);
    try{Configuration.typeSystem.checkCt( p, ct);}
    catch(ErrorMessage msg){
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"+ToFormattedText.of(ct)+"\n\n"+msg+"\n---------------\n");
    }
    if(strict && ct.getStage()==Stage.Less){
      throw Assertions.codeNotReachable("try to make this happen, is it possible? it should mean bug in plugin code\n"+ToFormattedText.of(ct));
    }
    return result;
  }*/
 
  
  // adapt
  //private static Ast.MethodType MadaptLib£l1£l2=mt(Path.Library(),Path.Library(),Path.Library());
  @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
  public Object MadaptLib£l1£l2(Object cb1,Object cb2){
    ClassB l1=extractClassB(cb1);
    ClassB l2=extractClassB(cb2);
    if (l1==null ||l2==null){throw new Resources.Error("InvalidClassB");}
    ExpCore result=IntrospectionAdapt.adapt(Resources.getP(),l1, l2);
    //return validateResult(pInternal,result,l1,l2,null);
    return result;
  }
  //case "nameToAdapter that ":return t?nameToAdapter:nameToAdapter(p,_1,_2);
  //??case "adapterToName that ":return t?adaptLib:adaptLib(p,_1,_2);
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public Object MnameToAdapter£that(Object cb1){
    String s1=extractStringU(cb1);
    if (s1==null){throw new Resources.Error("InvalidStringU");}
    L42.usedNames.add(s1);
    MethodSelector ms=IntrospectionAdapt.extractMS(s1);
    if(ms!=null){
      ClassB adapter=(ClassB)IntrospectionAdapt.buildMethodNameAdapter(ms);
      //return validateResult(pInternal,adapter,null,null,null);
      return adapter;
      }
    Path path=IntrospectionAdapt.extractPath(s1);
    if(path!=null){
      ClassB adapter=(ClassB) IntrospectionAdapt.buildPathAdapterIn(path);
      //return validateResult(pInternal,adapter,null,null,path);
      return adapter;
      }
    throw new Resources.Error("Invalid name or path: "+s1);
  }
    
  @ActionType({ActionType.Type.Library,ActionType.Type.TypeAny})
  public Object MtypeNameToAdapter£that(Object o){
    Path path=extractPathFromJava(o);
    if (path==null){throw new Resources.Error("ShouldNotHappen::InvalidPath");}
    //return validateResult(pInternal,IntrospectionAdapt.buildPathAdapterOut(path),null,null,path);
    return IntrospectionAdapt.buildPathAdapterOut(path);
  }
  
  @ActionType({ActionType.Type.Library,ActionType.Type.Library})
  public Object MgetFreshName£that(Object o){
    ClassB cb=ensureExtractClassB(o);
    Optional<Member> memberOpt = Program.getIfInDom(cb.getMs(),"%o_0%");
    if(!memberOpt.isPresent()){throw new  Resources.Error("Invalid adapter: "+ToFormattedText.of(cb));}
    NestedClass nc=(NestedClass)memberOpt.get();
    ClassB face=(ClassB)nc.getInner();
    if(face.getDoc1().isEmpty()){//method name adapter
      //TODO: class adapter from @comments!
      return freshMethodNameAdapter(cb, nc, face);
      }
    else{//class adapter
      return freshNestedClassNameAdapter(cb, nc, face);
    }
  }
  public Object freshNestedClassNameAdapter(ClassB cb, NestedClass nc,ClassB face) {
    if(face.isInterface()){throw new  Resources.Error("Invalid adapter: "+ToFormattedText.of(cb));}
    //throw Assertions.codeNotReachable();
    //dhdhhdd//TODO:enable again
    assert !face.isInterface();
    Path seed=face.getDoc1().getPaths().get(0);
    String fresh="%"+Functions.freshName(seed, L42.usedNames);
    List<String> _seed = new ArrayList<>(seed.getCBar());
    _seed.set(_seed.size()-1, fresh);
    seed=new Path(_seed);
    return (ClassB) IntrospectionAdapt.buildPathAdapterIn(seed);
    }
  public Object freshMethodNameAdapter(ClassB cb, NestedClass nc, ClassB face) {
    MethodWithType mt=null;
    for(Member m:face.getMs()){
      if(!(m instanceof MethodWithType)){continue;}
      MethodWithType mti=(MethodWithType)m;
      if(mti.getInner().isPresent()){continue;}
      mt=mti;break;
      }
    String seed=mt.getMs().getName();
    String fresh=Functions.freshName(seed, L42.usedNames);
    fresh=Functions.freshName(seed, L42.usedNames);
    MethodSelector newMs = mt.getMs().withName(fresh);
    return (ClassB)IntrospectionAdapt.buildMethodNameAdapter(newMs);
  }
  
  }

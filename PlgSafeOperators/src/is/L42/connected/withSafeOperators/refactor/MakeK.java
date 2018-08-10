package is.L42.connected.withSafeOperators.refactor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore.Block.Dec;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import ast.Ast;
import ast.ExpCore;
import ast.PathAux;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Position;
import ast.Ast.Type;
import auxiliaryGrammar.Functions;
import facade.L42;
import facade.PData;
import is.L42.connected.withSafeOperators.Errors42;
import is.L42.connected.withSafeOperators.Push;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ParseFail;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import newTypeSystem.TypeManipulation;
import programReduction.Program;
import tools.LambdaExceptionUtil;
import tools.LambdaExceptionUtil.Function_WithExceptions;
public class MakeK {
  public static ClassB makeKS(ClassB that,String name,String path,boolean immK,boolean fwd) throws PathUnfit, ParseFail, ClassUnfit{
    return makeKJ(that,name,PathAux.parseValidCs(path),immK,fwd);
    }
  public static ClassB makeKJ(ClassB that,String name,List<Ast.C> path,boolean immK,boolean fwd) throws PathUnfit, ParseFail, ClassUnfit{
    if(!MembersUtils.isPathDefined(that, path)){throw new RefactorErrors.PathUnfit(path);}
    if(MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
    ClassB lPath=that.getClassB(path);
    List<String> fields = collectFieldNames(lPath);
    return makeK(that,name,path,fields,immK,fwd);
    }

public static List<String> collectFieldNames(ClassB lPath) {
List<String> fields=new ArrayList<>();
for(MethodWithType mwt:lPath.mwts()){
  if(mwt.getMt().isRefine()){continue;}//To prevent toS() and class() to be seen as fields
  if(mwt.get_inner()!=null){continue;}
  if(mwt.getMt().getMdf()==Mdf.Class){continue;}
  if(mwt.getMs().getNames().size()>1){continue;}
  if(mwt.getMs().getNames().size()==1
    && (!mwt.getMt().getReturnType().equals(Type.immVoid)
      || !mwt.getMs().getNames().get(0).equals("that")
     )){continue;}
  String name=mwt.getMs().getName();
  if(name.startsWith("#")){
    continue;//prevent operators from being fields
    //name=name.substring(1,name.length());//what if it has more # inside?
    }
  if(!MethodSelector.checkX(name,false)){continue;}
  if(fields.contains(name)){continue;}
  fields.add(name);
  }
return fields;
}

  public static ClassB makeK(ClassB that,String name,List<Ast.C> path, List<String> fieldNames,boolean immK,boolean fwd) throws PathUnfit, ParseFail, ClassUnfit{
    if(path.isEmpty()){return makeK(name,that,that,path,fieldNames,immK,fwd);}
    if(!MembersUtils.isPathDefined(that, path)){throw new RefactorErrors.PathUnfit(path);}
    if(!MembersUtils.isPrivate(path)){throw new RefactorErrors.PathUnfit(path);}
    Function_WithExceptions<NestedClass,Optional<NestedClass>,Exception> f=nc->Optional.of(nc.withInner(makeK(name,that,(ClassB)nc.getInner(),path,fieldNames,immK,fwd)));
    return that.onNestedNavigateToPathAndDo(path,LambdaExceptionUtil.rethrowFunction(f));
    }

  private static ClassB makeK(String kName,ClassB top, ClassB that,List<Ast.C>path,List<String> fieldNames,boolean immK,boolean fwd) throws ParseFail, ClassUnfit {
    MethodWithType candidateK = candidateK(kName, that, fieldNames, fwd);
    if(immK){
      for(Type t:candidateK.getMt().getTs()){
        assert !t.getMdf().isIn(Mdf.Lent,Mdf.Readable);
        if(t.getMdf().isIn(Mdf.Mutable,Mdf.MutableFwd)){throw new RefactorErrors.ClassUnfit();}
        }
      candidateK=candidateK.withMt(candidateK.getMt().withReturnType(Type.immThis0));
    }
    List<Member> result=new ArrayList<>(that.getMs());
    if(Functions.getIfInDom(result,candidateK.getMs()).isPresent()){throw new RefactorErrors.ClassUnfit();}
    result.add(candidateK);
    return that.withMs(result);
  }

  /**
   DataOpen: mutK,immK, L1, path
create fresh fwdK (not unique)
return L1[create fwdK, mutK->fwdK+inv immK->fwdK+inv]
{
class method mut This fwdKj(T1 x1..Tn xn)
class method mut This mutK(T'1 x1..T'n xn) (
  res=This.fwdKj(x1:x1..xn:xn)
  res.#invariant()
  res
  )
class method This mutK(T''1 x1..T''n xn) (
  res=This.fwdKj(x1:x1..xn:xn)
  res.#invariant()
  res
  )
}
Tk1..Tkn= Ts as in K
Ti=Tki[capsule->mut]
T'i=noFwd(Tki)
T'i=Tki[with mdf=imm]
   */

public static MethodWithType candidateK(String kName, ClassB that, List<String> fieldNames, boolean fwd)
        throws ParseFail, ClassUnfit {
List<Type>fieldTypes=new ArrayList<>();
for(String f :fieldNames){
  if(!MethodSelector.checkX(f,false)){throw new RefactorErrors.ParseFail(f,RefactorErrors.ParseFail.Format.Id);}
  fieldTypes.add(candidate(that.getMs(),f));
  }
MethodWithType protoK = prototypeK(kName,Doc.empty(),fieldNames,fieldTypes,that.getP());
if(fwd){protoK=changeMt(protoK,MakeK::fwdK);}
return protoK;
}

static private MethodWithType changeMt(MethodWithType proto,Function<MethodType,MethodType>f) {
  return proto.withMt(f.apply(proto.getMt()));
  }


static private Type mdfChange(Type nt,Mdf m1,Mdf m2){
  if(nt.getMdf().equals(m1)){return nt.withMdf(m2);}
    return nt;
  }
static private Type addFwd(Type n){
  return Functions.toPh(n);
  }
static private MethodType fwdK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(MakeK::addFwd).collect(Collectors.toList()));
}

   private static MethodWithType prototypeK(String kName,Doc doc,List<String>fieldNames,List<Type>fieldTypes,Position pos) {
    MethodSelector ms=MethodSelector.of(kName,fieldNames);
    Type resT=Type.mutThis0;
    MethodType mt=new MethodType(false,Mdf.Class,fieldTypes,resT,Collections.emptyList());
    return new MethodWithType(doc, ms,mt,null,pos);
    }

  private static ast.Ast.Type candidate(List<Member> ms, String fName) throws ClassUnfit{
    Optional<Member> s1 = Functions.getIfInDom(ms, MethodSelector.of(fName,Collections.singletonList("that")));
    Optional<Member> s2 = Functions.getIfInDom(ms, MethodSelector.of("#"+fName,Collections.singletonList("that")));
    Optional<Member> g1 = Functions.getIfInDom(ms, MethodSelector.of(fName,Collections.emptyList()));
    Optional<Member> g2 = Functions.getIfInDom(ms, MethodSelector.of("#"+fName,Collections.emptyList()));
    Type ts1=getType(s1);
    Type ts2=getType(s2);
    Type tg1=getType(g1);
    Type tg2=getType(g2);
    if (ts1!=null && ts2!=null){
      if(!ts1.equals(ts2)){ throw new RefactorErrors.ClassUnfit();}
      return ts1;
      }
    if(ts1!=null){return ts1;}
    if(ts2!=null){return ts2;}
    //no setters, getters may disagree mut/capsule
    if (tg1!=null && tg2!=null){
      if(tg1.equals(tg2)){ return tg1;}
      if(tg1.getMdf()==Mdf.Capsule && tg2.getMdf()==Mdf.Mutable){
        return tg1;
        }
      if(tg2.getMdf()==Mdf.Capsule && tg1.getMdf()==Mdf.Mutable){
        return tg2;
        }
      throw new RefactorErrors.ClassUnfit();
      }
    if(tg1!=null){return tg1;}
    if(tg2!=null){return tg2;}
    throw new RefactorErrors.ClassUnfit();
    }
  private static ast.Ast.Type getType(Optional<Member>opt) throws ClassUnfit{
    if(!opt.isPresent()){return null;}
    Member m=opt.get();
    MethodWithType mwt=(MethodWithType)m;
    if(mwt.getMs().getNames().isEmpty()){
      //getter
      return mdfChange(mdfChange(mwt.getMt().getReturnType(),Mdf.Lent,Mdf.Capsule),Mdf.Readable,Mdf.Mutable);
      }
    //setter
    Type res=mwt.getMt().getTs().get(0);
    if(res.getMdf().isIn(Mdf.Lent,Mdf.Readable)){throw new RefactorErrors.ClassUnfit();}
    return  res;
    }
  }

/*

--old design:
  forall f in fs, get candidates
  if ki name valid, generate ki using candiates, fs
  if clone name valid, generate clone

  if set lent/read exists, mutK will not be generated
  if Ti is capsule or if only get and is lent, mutk will take capsule par.

--new simpler design:
  T1 x1..Tn xn=candidates()
  Ti is type of setter, if not exists, is type of getter,
    where lent->capsule and read->mut
  lent,read are not valid setter types.

  class method mut This #apply(T'1 x1..T'n xn)


  */

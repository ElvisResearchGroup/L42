package is.L42.connected.withSafeOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.*;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.FieldDec;
import ast.Ast.Mdf;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.Ast.Type;
import ast.Ast.Position;
import ast.Ast.Type;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import sugarVisitors.Desugar;
public class MakeKs {
  public static ClassB makeKs(ClassB that,List<Ast.C> path, List<String> fieldNames,boolean fwd){
    MakeKs m=new MakeKs();
    if(path.isEmpty()){return m.makeKs(that,that,fieldNames,fwd);}
    Errors42.checkExistsPathMethod(that, path, Optional.empty());
    return that.onNestedNavigateToPathAndDo(path,
      nc->Optional.of(nc.withInner(m.makeKs(that,(ClassB)nc.getInner(),fieldNames,fwd)))
      );
    }

  private boolean hasReadLentSetter=false;
private boolean hasReadFields;
  private ClassB makeKs(ClassB top, ClassB that,List<String> fieldNames,boolean fwd) {
    List<Type>fieldTypes=new ArrayList<>();
    for(String f :fieldNames){
      if(!MethodSelector.checkX(f,true)){throw new Error("Invalid field name provided:["+f+"]");}
      fieldTypes.add(candidate(that.getMs(),f));
      }
    List<MethodWithType> toAdd=new  ArrayList<>();
    MethodWithType protoK = prototypeK(Doc.empty(),fieldNames,fieldTypes,that.getP());
    if(fwd){protoK=changeMt(protoK,MakeKs::fwdK);}
    
    /*MethodWithType _mutK = changeMt(protoK,MakeKs::mutK);
    MethodWithType _lentK = changeMt(protoK,MakeKs::lentK);
    MethodWithType _readK = changeMt(protoK,MakeKs::readK);
    MethodWithType _immK = changeMt(protoK,MakeKs::immK);
    */
    addWithName(mutK, toAdd, _mutK);}
    addWithName(lentK, toAdd, _lentK);
    addWithName(readK, toAdd, _readK);
    addWithName(immK, toAdd, _immK);
    List<Member> result=new ArrayList<>(that.getMs());
    for(MethodWithType m:toAdd){
      if(Functions.getIfInDom(result,m.getMs()).isPresent()){continue;}
      result.add(m);
      }
    return that.withMs(result);
  }

  private void addWithName(String nameK, List<MethodWithType> toAdd, MethodWithType k) {
    if(MethodSelector.checkX(nameK,true)){toAdd.add(k.withMs(k.getMs().withName(nameK)));}
  }

static private MethodWithType changeMt(MethodWithType proto,Function<MethodType,MethodType>f) {
  return proto.withMt(f.apply(proto.getMt()));
  }


static private Type mdfChange(Type n,Mdf m1,Mdf m2){
  Type nt=(Type)n;
  if(nt.getMdf().equals(m1)){return nt.withMdf(m2);}
    return nt;
  }
static private Type addFwd(Type n){
  return Functions.toPh(n);
  }
static private MethodType fwdK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(MakeKs::addFwd).collect(Collectors.toList()));
}
static private MethodType mutK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(t->mdfChange(t,Mdf.Readable,Mdf.Mutable))
    .collect(Collectors.toList()));
  }

static private MethodType lentK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(t->mdfChange(t,Mdf.Mutable,Mdf.Lent)).
    collect(Collectors.toList()))
    .withReturnType(mdfChange(proto.getReturnType(),Mdf.Mutable,Mdf.Lent));
  }

static private MethodType readK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(t->mdfChange(t,Mdf.Mutable,Mdf.Readable))
    .map(t->mdfChange(t,Mdf.Lent,Mdf.Readable))
    .collect(Collectors.toList()))
    .withReturnType(mdfChange(proto.getReturnType(),Mdf.Mutable,Mdf.Readable));
  }
static private MethodType immK(MethodType proto) {
  return proto.withTs(proto.getTs().stream()
    .map(t->{
      Type nt=(Type)t;
      if(!nt.getMdf().equals(Mdf.Class)){return nt.withMdf(Mdf.Immutable);}
      return nt;
      })
    .collect(Collectors.toList()))
    .withReturnType(mdfChange(proto.getReturnType(),Mdf.Mutable,Mdf.Immutable));
  }
  //can not reuse the desugar one, here we create ExpCore stuff, also , the sugar one may disappear
   private MethodWithType prototypeK(Doc doc,List<String>fieldNames,List<Type>fieldTypes,Position pos) {
    MethodSelector ms=MethodSelector.of("",fieldNames);
    Type resT=Type.mutThis0;
    if (this.hasReadFields){resT=resT.withMdf(Mdf.Lent);}
    MethodType mt=new MethodType(false,ast.Ast.Mdf.Class,fieldTypes,resT,Collections.emptyList());
    return new MethodWithType(doc, ms,mt, Optional.empty(),pos);
    }

  private ast.Ast.Type candidate(List<Member> ms, String fName){
    Optional<Member> a = Functions.getIfInDom(ms, MethodSelector.of(fName,Collections.singletonList("that")));
    Optional<Member> b = Functions.getIfInDom(ms, MethodSelector.of("#"+fName,Collections.singletonList("that")));
    Optional<Member> c = Functions.getIfInDom(ms, MethodSelector.of(fName,Collections.emptyList()));
    Optional<Member> d = Functions.getIfInDom(ms, MethodSelector.of("#"+fName,Collections.emptyList()));
    Type ta=getType(a);
    Type tb=getType(b);
    Type tc=getType(c);
    Type td=getType(d);
    ast.Ast.Type res=null;
    if (a.isPresent() && b.isPresent()){
      if(!ta.equals(tb)){ throw new Error();}
      }
    if(a.isPresent()) {res=ta;}
    else if(b.isPresent()){res=tb;}
    if(res!=null){
      if(c.isPresent() && !more(res,tc)){throw new Error();}
      if(d.isPresent() && !more(res,td)){throw new Error();}
      }
    if(c.isPresent() && d.isPresent() && !compatible(tc,td)){throw new Error();}
    if (res==null){res=moreSpecific(tc,td);}
    if (res==null){throw new Error();}
    if((a.isPresent() || b.isPresent()) && (res.getMdf().equals(Mdf.Lent)|| res.getMdf().equals(Mdf.Readable))){
      this.hasReadLentSetter=true;}
    if(!a.isPresent() && !b.isPresent() && res.getMdf().equals(Mdf.Lent)){
      return res.withMdf(Mdf.Capsule);
      }
    return res;
    }
  private ast.Ast.Type getType(Optional<Member>opt){
    if(!opt.isPresent()){return null;}
    Member m=opt.get();
    MethodWithType mwt=(MethodWithType)m;
    if(mwt.getMs().getNames().isEmpty()){return (Type) mwt.getMt().getReturnType();}
    return (Type) mwt.getMt().getTs().get(0);
    }
  private boolean compatible(ast.Ast.Type t1, ast.Ast.Type t2){
    if (!t1.getPath().equals(t2.getPath())){return false;}
    if (Functions.isSubtype(t1.getMdf(),t2.getMdf())){return true;}
    if (Functions.isSubtype(t2.getMdf(),t1.getMdf())){return true;}
    return false;
  }
  private boolean more(ast.Ast.Type t1, ast.Ast.Type t2){
    if (!t1.getPath().equals(t2.getPath())){return false;}
    if (Functions.isSubtype(t1.getMdf(),t2.getMdf())){return true;}
    return false;
  }
  private ast.Ast.Type moreSpecific(ast.Ast.Type t1, ast.Ast.Type t2){
    if(t1==null){return t2;}
    if(t2==null){return t1;}
    if (Functions.isSubtype(t1.getMdf(),t2.getMdf())){return t1;}
    return t2;
    }
}

/*


  forall f in fs, get candidates
  if ki name valid, generate ki using candiates, fs
  if clone name valid, generate clone

  if set lent/read exists, mutK will not be generated
  if Ti is capsule or if only get and is lent, mutk will take capsule par.





  */

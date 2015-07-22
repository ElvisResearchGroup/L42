package is.L42.connected.withSafeOperators;

import platformSpecific.javaTranslation.Resources;
import sugarVisitors.ToFormattedText;
import ast.Ast;
import ast.ErrorMessage;
import ast.Ast.Mdf;
import ast.Ast.MethodSelectorX;
import ast.Ast.Path;
import ast.Ast.Type;
import ast.ErrorMessage.NormImpossible;
import ast.Ast.NormType;
import ast.Ast.Ph;
import ast.Ast.Doc;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.*;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
//TODO: modify Program so that it contains pairs of classB,ct!
//BIG THING!

public class Introspection {
  public static ClassB giveInfo(ClassB that,List<String> path){
    ClassB current = Program.extractCBar(path, that);
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    return Resources.Error.multiPartStringClassB("PathInfo",
      "Kind",ExtractInfo.classKind(that, path, current,null,null, null,null).name(),
      "MemberNumber",""+current.getMs().size(),
      "ImplementedNumber",""+current.getSupertypes().size(),
      "AllAsString",""+ToFormattedText.of(current)
    );}

  public static ClassB giveInfoMember(ClassB that,List<String> path,int memberN){
    assert memberN!=0;
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    if(current.getMs().size()<memberN){throw Resources.notAct;}
    Member mN=current.getMs().get(memberN-1);
    String[] thisMdf={""};//java limitations on closures
    int[] excNum={0};
    int[] parNum={0};
    String[] key={null};
    Ast.Doc[] doc={null};
    mN.match(
      nc->{
        doc[0]=nc.getDoc();
        key[0]=String.join("::",path);
        if(key[0].isEmpty()){key[0]="::"+nc.getName();}
        else{key[0]="::"+key[0]+"::"+nc.getName();}
        return null;
          },
      mi->{//we want to use mi instead of mt of ct: even the number of members may change.
        doc[0]=mi.getDoc();
        key[0]=mi.getS().toString();
        parNum[0]=mi.getS().getNames().size();
        return null;
      },
      mt->{
        doc[0]=mt.getDoc();
        key[0]=mt.getMs().toString();
        parNum[0]=mt.getMs().getNames().size();
        excNum[0]=mt.getMt().getExceptions().size();
        thisMdf[0]=mt.getMt().getMdf().toString();
        return null;
      });
    //TODO: doc exceptions should become a doc for each exception
    return Resources.Error.multiPartStringClassB("MemberInfo",
      "Doc",liftDoc(path,doc[0]),
      "ThisMdf",thisMdf[0],
      "key",key[0],//Selector/Path
      "ExceptionNumber",excNum[0],
      "ParameterNumber",parNum[0],
      "MemberKind",ExtractInfo.memberKind(mN),
      "AllAsString",ToFormattedText.of(mN)
      );
    }
  static enum TypeKind{Normal, Alias, AliasUnresolvable}
  //if member ==0, talk about implemented interfaces, nested classes have no types
  public static ClassB giveInfoType(Program p,ClassB that,List<String> path,int memberN,int typeN){
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    if(current.getMs().size()<memberN){throw Resources.notAct;}
    if(memberN<0){throw Resources.notAct;}//TODO: is this a good idea?
    if(memberN==0){
      Path implN = current.getSupertypes().get(typeN-1);
      Doc dImplN=Doc.factory(implN);
      return typeReport(path, TypeKind.Normal,Mdf.Immutable,Mdf.Immutable,
          dImplN,dImplN, false,false,"","",current.getDoc2(),ToFormattedText.of(implN));  
    }
    assert memberN>0;
    Member mi = current.getMs().get(memberN-1);
    //mi.match(
    //   nc->{throw Resources.notAct;},//TODO: is this a good idea?
    //   mi->{throw Resources.notAct;},//TODO: is this a good idea?make only parameters?
    if(!(mi instanceof MethodWithType)){throw Resources.notAct;}
    MethodWithType mwt=(MethodWithType)mi;
     Type ti = mwt.getMt().getReturnType();
     if(typeN>mwt.getMt().getTs().size()){throw Resources.notAct;}
     if(typeN<-mwt.getMt().getExceptions().size()){throw Resources.notAct;}
    if(typeN>0){ti=mwt.getMt().getTs().get(typeN-1);}
    if(typeN<0){ti=new NormType(Mdf.Immutable,mwt.getMt().getExceptions().get((typeN*-1)-1),Ph.None);}
    NormType normTi=(ti instanceof NormType)?(NormType)ti:null;
    NormType resolvedTi=null;
    try{resolvedTi=Norm.of(p, ti);}catch(ErrorMessage.NormImpossible ni){}
    TypeKind tk=(ti instanceof NormType)?TypeKind.Normal:(resolvedTi==null)?TypeKind.AliasUnresolvable:TypeKind.Alias;
    Mdf mdf=(normTi!=null)?normTi.getMdf():Mdf.Immutable;    
    Mdf resMdf=(resolvedTi!=null)?resolvedTi.getMdf():Mdf.Immutable;
    Path pi=(normTi!=null)?normTi.getPath():((Ast.HistoricType)ti).getPath();
    Path resPi=(resolvedTi!=null)?resolvedTi.getPath():pi;
    Doc dPi=Doc.factory(pi);
    Doc dResPi=Doc.factory(resPi);
    boolean ph=(normTi!=null)?normTi.getPh()==Ph.Ph:((Ast.HistoricType)ti).isForcePlaceholder();
    boolean resPh=(resolvedTi!=null)?resolvedTi.getPh()==Ph.Ph:ph;
    String suffix=(ti instanceof Ast.HistoricType)?selectorsToString(((Ast.HistoricType)ti).getSelectors()):"";
    String parName=(typeN>0)?mwt.getMs().getNames().get(typeN-1):"";
    Doc doc=mwt.getDoc();
    String allAsString=ToFormattedText.of(mwt);
    return typeReport(path, tk, mdf, resMdf, dPi, dResPi, ph, resPh, suffix, parName, doc, allAsString);
      }

  private static String selectorsToString(List<MethodSelectorX> selectors) {
    // TODO Auto-generated method stub
    return null;
  }

  private static ClassB typeReport(List<String> path, TypeKind kind, Mdf mdf, Mdf resMdf, Doc pi, Doc resPi, boolean ph, boolean resPh, String suffix, String parName, Doc doc, String allAsString) throws Error {
    return Resources.Error.multiPartStringClassB("TypeInfo",
      "Kind",""+kind,//:Normal, Alias, AliasUnresolvable
      "Mdf",""+mdf,//:String
      "ResolvedMdf",""+resMdf,//:String
      "Path",""+liftDoc(path,pi),//:Doc with one annotation, can be typeAny or not
      "ResolvedPath",""+liftDoc(path,resPi),//:Doc with one annotation, can be typeAny or not
      "Ph",""+ph,//:Boolean
      "ResolvedPh",""+resPh,//:Boolean
      "Suffix",""+suffix,//:String (may be empty for not alias type)
      "ParName",""+parName,//String (empty for 0 ... -n)
      "Doc",""+liftDoc(path,doc),//Doc
      "AllAsString",""+allAsString//String
      );
  }/*
  4)extractDocAsString:Lib*,path,pathNumber,boolean_hardWrap->Library:String //error for number bigger than possible
  //for type any generate some str repr, with 0 just gives you the whole doc
  5)extractDocPath:Lib*,path, pathNumber ->typeAny //error for internal paths or number bigger than possible.
*/

  private static Object liftDoc(List<String> path, Doc doc) {
    // TODO Auto-generated method stub
    return null;
  }
}

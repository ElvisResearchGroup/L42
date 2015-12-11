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
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import coreVisitors.From;
import facade.Configuration;

public class Introspection {//TODO: we keep 5 methods, but we merge the PathReport and MemberReport content.
  public static List<Object>  giveInfo(ClassB that,List<String> path){
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    Doc[] docRef={null};
    ClassB current = Program.extractCBar(path, that,docRef);
    return Arrays.asList(
      "MemberKind","NestedClass",
      "MemberDoc",(docRef[0]==null)?"":liftDoc(path.subList(0,path.size()-1),docRef[0],1),
      "Key",""+String.join("::",path),
      "AllAsString",ToFormattedText.ofNoStage(current),
      "ClassKind",ExtractInfo.classKind(that, path, current,null, null,null).name(),
      "LibraryDoc",liftDoc(path,current.getDoc1(),1),
      "MemberNumber",""+current.getMs().size(),
      "ImplementedNumber",""+current.getSupertypes().size()
    );}

  public static ClassB giveInfoMember(ClassB that,List<String> path,int memberN){
    assert memberN!=0;
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    if(current.getMs().size()<memberN){
      throw Resources.notAct;
      }
    Member mN=current.getMs().get(memberN-1);
    ClassB[] result={null};
    mN.match(
      nc->{
        List<String> fullPath=new ArrayList<>(path);
        fullPath.add(nc.getName());
        result[0]=Resources.Error.multiPartStringClassB("MemberReport",
          "MemberKind","NestedClass",
          "MemberDoc",liftDoc(path,nc.getDoc(),1),
          "Key",String.join("::",fullPath),
          "AllAsString",ToFormattedText.ofNoStage(nc.getInner()),
          "ClassKind",ExtractInfo.classKind(that, path,(ClassB) nc.getInner(),null, null,null).name(),
          "LibraryDoc",liftDoc(path,current.getDoc1(),1),
          "MemberNumber",""+current.getMs().size(),
          "ImplementedNumber",""+current.getSupertypes().size()
          );
        return null;
          },
      mi->{//we want to use mi instead of mt of ct: even the number of members may change.
        result[0]=Resources.Error.multiPartStringClassB("MemberReport",
          "MemberKind",ExtractInfo.memberKind(mN),
          "MemberDoc",liftDoc(path,mi.getDoc(),1),
          "Key",""+mi.getS(),//Selector/Path
          "AllAsString",ToFormattedText.of(mi),
          "ParameterNumber",""+mi.getS().getNames().size()
            );
        return null;
      },
      mt->{
        result[0]=Resources.Error.multiPartStringClassB("MemberReport",
          "MemberKind",ExtractInfo.memberKind(mN),
          "MemberDoc",liftDoc(path,mt.getDoc(),1),
          "ThisMdf",""+mt.getMt().getMdf(),
          "Key",""+mt.getMs(),//Selector/Path
          "AllAsString",ToFormattedText.of(mt),
          "ExceptionNumber",""+mt.getMt().getExceptions().size(),
          "ParameterNumber",""+mt.getMs().getNames().size()
          );
        return null;
      });    //TODO: doc exceptions should become a doc for each exception
    return result[0];
    }
  //static enum TypeKind{InternalNormal,ExternalNormal,InternalAlias,ExternalAlias, InternalAliasUnresolvable,ExternalAliasUnresolvable,InternalExternalAlias}
  static enum TypeKind{Normal,Alias,AliasUnresolvable}
  //if member ==0, talk about implemented interfaces, nested classes have no types
  public static ClassB giveInfoType(Path  isExternal,Program p,ClassB that,List<String> path,int memberN,int typeN){
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    if(current.getMs().size()<memberN){throw Resources.notAct;}
    if(memberN<0){throw Resources.notAct;}//TODO: is this a good idea?
    if(memberN==0){
      if(typeN<=0){throw Resources.notAct;}
      if(current.getSupertypes().size()<typeN){throw Resources.notAct;}
      Path implN = current.getSupertypes().get(typeN-1);
      return typeReport(isExternal,p.addAtTop(that), new NormType(Mdf.Immutable,implN,Ph.None),path,current.getDoc2());
      }
    assert memberN>0;
    Member mi = current.getMs().get(memberN-1);
    if(!(mi instanceof MethodWithType)){throw Resources.notAct;}
    MethodWithType mwt=(MethodWithType)mi;
     Type ti = mwt.getMt().getReturnType();
     Doc doci=mwt.getDoc();
     if(typeN>mwt.getMt().getTs().size()){throw Resources.notAct;}
     if(typeN<-mwt.getMt().getExceptions().size()){throw Resources.notAct;}
    if(typeN>0){
      ti=mwt.getMt().getTs().get(typeN-1);
      doci=mwt.getMt().getTDocs().get(typeN-1);
      }
    if(typeN<0){
      ti=new NormType(Mdf.Immutable,mwt.getMt().getExceptions().get((typeN*-1)-1),Ph.None);
      //TODO: doci=?? add docs for exceptions, return type and implements
      }
    return typeReport(isExternal,p.addAtTop(that), ti,path,doci);
    }
  private static ClassB typeReport(Path isExternal,Program p,Type ti, List<String> src, Doc doc){
    p=p.navigateInTo(src);
    NormType normTi=(ti instanceof NormType)?(NormType)ti:null;
    NormType resolvedTi=null;
    try{resolvedTi=Norm.of(p, ti);}catch(ErrorMessage.NormImpossible ni){}
    TypeKind tk = getTypeKind(src,ti, resolvedTi);
    Mdf mdf=(normTi!=null)?normTi.getMdf():Mdf.Immutable;
    Mdf resMdf=(resolvedTi!=null)?resolvedTi.getMdf():Mdf.Immutable;
    Path pi=(normTi!=null)?normTi.getPath():((Ast.HistoricType)ti).getPath();
    Path resPi=(resolvedTi!=null)?resolvedTi.getPath():pi;
    boolean ph=(normTi!=null)?normTi.getPh()==Ph.Ph:((Ast.HistoricType)ti).isForcePlaceholder();
    boolean resPh=(resolvedTi!=null)?resolvedTi.getPh()==Ph.Ph:ph;
    String suffix=(ti instanceof Ast.HistoricType)?selectorsToString(((Ast.HistoricType)ti).getSelectors()):"";
    String allAsString=ToFormattedText.of(ti);
    return typeReport(isExternal,src, tk, mdf, resMdf, pi, resPi, ph, resPh, suffix, doc, allAsString);
    //unfold?
  }

  //private static boolean isExternal(List<String>path,Path pi){return pi.isPrimitive()||pi.outerNumber()>path.size(); }
  private static TypeKind getTypeKind(List<String>path,Type ti, NormType resolvedTi) {
    if(ti instanceof NormType){return TypeKind.Normal;
      //NormType nt=(NormType)ti;
      //if(isExternal(path,nt.getPath())){return TypeKind.ExternalNormal;}
      //return TypeKind.InternalNormal;
    }
    if(resolvedTi==null){return TypeKind.AliasUnresolvable;}
    return TypeKind.Alias;
    //Ast.HistoricType ht=(Ast.HistoricType)ti;
    //boolean tiExt=isExternal(path,ht.getPath());
    //if(resolvedTi==null && tiExt){return TypeKind.ExternalAliasUnresolvable;}
    //if(resolvedTi==null){return TypeKind.InternalAliasUnresolvable;}
    //if(tiExt){return TypeKind.ExternalAlias;}
    //if(isExternal(path,resolvedTi.getPath())){return TypeKind.InternalExternalAlias;}
    //return TypeKind.InternalExternalAlias;
  }

  private static String selectorsToString(List<MethodSelectorX> selectors) {
    String result="";
    for(MethodSelectorX msx:selectors){
      result+="::"+msx.getMs().toString();
      if(!msx.getX().isEmpty()){result+="::"+msx.getX();}
    }
    return result;
  }

  private static ClassB typeReport(Path isExternal,List<String> path, TypeKind kind, Mdf mdf, Mdf resMdf, Path pi, Path resPi, boolean ph, boolean resPh, String suffix,  Doc doc, String allAsString) throws Error {
    assert mdf!=null && resMdf!=null;
    Doc dPi=liftDoc(path,Doc.factory(pi),1);
    Doc dResPi=liftDoc(path,Doc.factory(resPi),1);
    if(isExternal!=null && dPi.getAnnotations().get(0) instanceof String){
      dPi=Doc.factory(Functions.add1Outer(Functions.add1Outer(From.fromP(pi,isExternal))));
    }
    if(isExternal!=null && dResPi.getAnnotations().get(0) instanceof String){
      dResPi=Doc.factory(Functions.add1Outer(Functions.add1Outer(From.fromP(resPi,isExternal))));
    }
    return Resources.Error.multiPartStringClassB("TypeReport",
      "TypeKind",""+kind,//:Normal, Alias, AliasUnresolvable
      "Mdf",""+((mdf==Mdf.Immutable)?resMdf:mdf),//:String
      //"ResolvedMdf",""+resMdf,//:String
      "Path",dPi,//:Doc with one annotation, can be typeAny or not
      "ResolvedPath",dResPi,//:Doc with one annotation, can be typeAny or not
      "Ph",""+ph,//:Boolean
      "ResolvedPh",""+resPh,//:Boolean
      "Suffix",""+suffix,//:String (may be empty for not alias type)
      "Doc",liftDoc(path,doc,1),//Doc
      "AllAsString",""+allAsString//String
      );
  }
  /*
   annotationN==-1 is HardWrap
   Hard wrap: just wrap the content of the comment in @stringU
   Soft wrap: if the content is already wrapped in @stringU, leave it as it is.
   What should we do for @int32 and similar?
    */
  public static String extractDocAsString(ClassB that,List<String>path,int annotationN){
    //System.out.println("extractDocAsString("+path+" annotationN:"+annotationN+")");
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    Doc d=current.getDoc1();
    d=liftDoc(path,d,0);
    //System.out.println("extractDocAsString("+d+")");
    if(annotationN<=0){
      String result=d.toString();
      //System.out.println("extractDocAsString(result:"+result+")");
      if(annotationN==-1 || !result.startsWith("@stringU\n")){
        return result;// extra @stringU will be added since we return "String"
        }
      assert result.endsWith("\n");
      if(result.startsWith("@stringU\n")){result=result.substring("@stringU\n".length(),result.length()-1);}

      return EncodingHelper.parseStringUnicode(result);
    }
    if(d.getAnnotations().size()<annotationN){throw Resources.notAct;}
    Object o=d.getAnnotations().get(annotationN-1);
    return o.toString();
  }
  public static Path extractDocPath(ClassB that,List<String>path,int annNumber){
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    Doc d=current.getDoc1();
    d=liftDoc(path,d,0);//ok, it is as should be if wrote in the class (see under)
    if(d.getAnnotations().size()<annNumber){throw Resources.notAct;}
    Object o=d.getAnnotations().get(annNumber-1);
    //if(o instanceof String){throw Resources.notAct;}
    if(!(o instanceof Path)){
      throw Resources.notAct;
      }
    Path pRes=(Path)o;
    assert pRes.isPrimitive() || pRes.outerNumber()>=1;
    if(pRes.isPrimitive()){return pRes;}
    pRes=pRes.setNewOuter(pRes.outerNumber()-1);
    return pRes;
  }
  /*
  5)extractDocPath:Lib*,path, pathNumber ->typeAny //error for internal paths or number bigger than possible.
*/

  private static Doc liftDoc(List<String> path, Doc doc,int newNested) {
    //for all external paths (pi.outern>path.size) new outern=oldOutern-size+newNested
    //for all internal paths(otherwise) becomes a ::string, normalized in path,
    List<Object> ann=new ArrayList<>();
    for(Object o:doc.getAnnotations()){
      if(o instanceof String){ann.add(o);continue;}
      assert o instanceof Path:o.getClass().getCanonicalName();
      Path pi=(Path)o;
      if(pi.isPrimitive()){ann.add(o);continue;}
      if(pi.outerNumber()>path.size()){
          o=pi.setNewOuter((pi.outerNumber()-path.size())+newNested);
          ann.add(o);continue;
          }
      List<String>topPi=ClassOperations.toTop(path, pi);
      o="::"+String.join("::",topPi);
      ann.add(o);//continue;
    }
    return doc.withAnnotations(ann);
  }
}

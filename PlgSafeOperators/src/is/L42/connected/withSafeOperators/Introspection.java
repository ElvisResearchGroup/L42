package is.L42.connected.withSafeOperators;

import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Translator;
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
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import coreVisitors.From;

public class Introspection {
  public static ClassB giveInfo(ClassB that,List<String> path){
    ClassB current = Program.extractCBar(path, that);
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    return Resources.Error.multiPartStringClassB("PathReport",
      "ClassKind",ExtractInfo.classKind(that, path, current,null, null,null).name(),
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
    return Resources.Error.multiPartStringClassB("MemberReport",
      "Doc",liftDoc(path,doc[0],1),
      "ThisMdf",thisMdf[0],
      "key",key[0],//Selector/Path
      "ExceptionNumber",excNum[0],
      "ParameterNumber",parNum[0],
      "MemberKind",ExtractInfo.memberKind(mN),
      "AllAsString",ToFormattedText.of(mN)
      );
    }
  //static enum TypeKind{InternalNormal,ExternalNormal,InternalAlias,ExternalAlias, InternalAliasUnresolvable,ExternalAliasUnresolvable,InternalExternalAlias}
  static enum TypeKind{Normal,Alias,AliasUnresolvable}
  //if member ==0, talk about implemented interfaces, nested classes have no types
  public static ClassB giveInfoType(Program p,ClassB that,List<String> path,int memberN,int typeN){
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    if(current.getMs().size()<memberN){throw Resources.notAct;}
    if(memberN<0){throw Resources.notAct;}//TODO: is this a good idea?
    if(memberN==0){
      Path implN = current.getSupertypes().get(typeN-1);
      boolean isExternal=false;
      if(implN.isPrimitive() ||implN.outerNumber()>path.size()){isExternal=true;}
      Doc dImplN=Doc.factory(implN);//is ok since I have liftDoc
      return typeReport(path, 
          TypeKind.Normal,//isExternal?TypeKind.ExternalNormal:TypeKind.InternalNormal,
          Mdf.Immutable,Mdf.Immutable,
          dImplN,dImplN, false,false,"","",current.getDoc2(),ToFormattedText.of(implN));
    }
    assert memberN>0;
    Member mi = current.getMs().get(memberN-1);
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
    TypeKind tk = getTypeKind(path,ti, resolvedTi);
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

  private static ClassB typeReport(List<String> path, TypeKind kind, Mdf mdf, Mdf resMdf, Doc pi, Doc resPi, boolean ph, boolean resPh, String suffix, String parName, Doc doc, String allAsString) throws Error {
    assert mdf!=null && resMdf!=null;
    return Resources.Error.multiPartStringClassB("TypeReport",
      "TypeKind",""+kind,//:Normal, Alias, AliasUnresolvable
      "Mdf",""+((mdf==Mdf.Immutable)?resMdf:mdf),//:String
      //"ResolvedMdf",""+resMdf,//:String
      "Path",liftDoc(path,pi,1),//:Doc with one annotation, can be typeAny or not
      "ResolvedPath",liftDoc(path,resPi,1),//:Doc with one annotation, can be typeAny or not
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
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    Doc d=current.getDoc1();
    d=liftDoc(path,d,0);
    if(annotationN<=0){
      String result=d.toString();
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
    d=liftDoc(path,d,0);
    if(d.getAnnotations().size()<annNumber){throw Resources.notAct;}
    Object o=d.getAnnotations().get(annNumber-1);
    if(!(o instanceof Path)){throw Resources.notAct;}
    //TODO: after is wrong, we need to use this kind of stuff.String s=Translator.nameOf((Path)o);
    //test returning paths and then throw catch them,
    //test especially difference between Library and Any.
    return (Path)o;
  }
  /*
  5)extractDocPath:Lib*,path, pathNumber ->typeAny //error for internal paths or number bigger than possible.
*/
  //TODO: composition errors should be exceptions. This mean that <<, >>, ++, ** have to become right associative

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

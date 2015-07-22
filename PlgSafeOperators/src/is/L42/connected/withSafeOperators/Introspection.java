package is.L42.connected.withSafeOperators;

import platformSpecific.javaTranslation.Resources;
import sugarVisitors.ToFormattedText;
import ast.Ast;
import ast.Ast.Doc;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.*;
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
    int meth=0;
    int nest=0;
    for(Member m:current.getMs()){
     if(m instanceof NestedClass){nest+=1;}
     else{meth+=1;}
    }
    return Resources.Error.multiPartStringClassB("PathInfo",
      "Kind",ExtractInfo.classKind(that, path, current,null,null, null,null).name(),
      "MethodNumber",""+meth,
      "NestedNumber",""+nest,
      "ImplementedNumber",""+current.getSupertypes().size(),
      "AllAsString",""+ToFormattedText.of(current)
    );}

  public static ClassB giveInfoMember(ClassB that,List<String> path,int memberN){
    assert memberN!=0;
    Errors42.checkExistsPathMethod(that, path,Optional.empty());
    ClassB current = Program.extractCBar(path, that);
    int meth=0;
    int nest=0;
    Member mN=null;
    for(Member m:current.getMs()){
     if(m instanceof NestedClass){nest+=1;}
     else{meth+=1;}
     if(meth==memberN){mN=m;}
     if(nest==-memberN){mN=m;}
    }
    if(mN==null){throw Resources.notAct;}
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
/*
  3)
  //zero for return type, negative for exceptions, if methNumber0, return types of implemented interfaces?
  giveInfoMethodType:Lib*, path, methNumber, parameterNumber->Library:{
    Kind:Normal, Alias, AliasUnresolvable
    Mdf:String
    ResolvedMdf:String
    Path:Doc with one annotation, can be typeAny or not
    ResolvedPath:Doc with one annotation, can be typeAny or not
    Ph:Boolean
    ResolvedPh:Boolean
    Suffix:String (may be empty for not alias type)
    ParName:String (empty for 0 ... -n)
    Doc:Doc
    AllAsString:String
    }
  4)extractDocAsString:Lib*,path,pathNumber,boolean_hardWrap->Library:String //error for number bigger than possible
  //for type any generate some str repr, with 0 just gives you the whole doc
  5)extractDocPath:Lib*,path, pathNumber ->typeAny //error for internal paths or number bigger than possible.
*/

  private static Object liftDoc(List<String> path, Doc doc) {
    // TODO Auto-generated method stub
    return null;
  }
}

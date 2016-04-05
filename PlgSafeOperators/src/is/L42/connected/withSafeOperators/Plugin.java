package is.L42.connected.withSafeOperators;

import static auxiliaryGrammar.EncodingHelper.ensureExtractClassB;
import static auxiliaryGrammar.EncodingHelper.ensureExtractStringU;
import static auxiliaryGrammar.EncodingHelper.ensureExtractInt32;
import static auxiliaryGrammar.EncodingHelper.ensureExtractPathFromJava;
import static auxiliaryGrammar.EncodingHelper.ensureExtractDoc;

import java.util.ArrayList;
import java.util.List;


import ast.Ast;
import ast.Ast.Doc;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import auxiliaryGrammar.Functions;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import facade.Configuration;
import facade.L42;
import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Revertable;
import sugarVisitors.ToFormattedText;

//empty scheleton
public class Plugin implements PluginType{

    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object Mcompose£xleft£xright(Object _left,Object _right){
      ClassB left=ensureExtractClassB(_left);
      ClassB right=ensureExtractClassB(_right);
      try{return Sum.sum(Resources.getP(),left,right);
      }catch(ArrayIndexOutOfBoundsException exc){
        exc.printStackTrace();
        throw exc;
      }
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MrenameClass£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<String> src=Path.parseValidCs(ensureExtractStringU(_src));
      List<String> dest=Path.parseValidCs(ensureExtractStringU(_dest));
      System.out.println("####################RENAMECLASS:"+src+"   "+dest);
      return Rename.renameClass(Resources.getP(),that,src,dest);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MrenameMethod£xthat£xpath£xsrc£xdest(Object _that,Object _path,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
       MethodSelector src = MethodSelector.parse(ensureExtractStringU(_src));
       MethodSelector dest = MethodSelector.parse(ensureExtractStringU(_dest));
      return Rename.renameMethod(Resources.getP(),that,path,src,dest);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MsumMethods£xthat£xpath£xsrc1£xsrc2£xdest£xname(Object _that,Object _path,Object _src1,Object _src2,Object _dest,Object _name){
      ClassB that=ensureExtractClassB(_that);
      String name=ensureExtractStringU(_name);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
       MethodSelector src1 = MethodSelector.parse(ensureExtractStringU(_src1));
       MethodSelector src2 = MethodSelector.parse(ensureExtractStringU(_src2));
       MethodSelector dest = MethodSelector.parse(ensureExtractStringU(_dest));
      return SumMethods.sumMethods(that,path,src1,src2,dest,name);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.TypeAny})
    public Object Mredirect£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<String> src=Path.parseValidCs(ensureExtractStringU(_src));
      Path dest=ensureExtractPathFromJava(_dest);
      assert dest.isCore() || dest.isPrimitive():
        dest;
      if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}//TODO: see if extractPath should be changed
      return Redirect.redirect(Resources.getP(),that,Path.outer(0,src),dest);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MremoveImplementation£xthat£xpath(Object _that,Object _path){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      return Abstract.toAbstract(that,path);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MremoveImplementation£xthat£xpath£xselector(Object _that,Object _path,Object _sel){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      MethodSelector sel = MethodSelector.parse(ensureExtractStringU(_sel));
      return Abstract.toAbstract(Resources.getP(),that,path,sel,null);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MmoveImplementation£xthat£xpath£xsrc£xdest(Object _that,Object _path,Object _sel1,Object _sel2){
      ClassB that=ensureExtractClassB(_that);
      assert that.getStage().isInheritedComputed();
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      MethodSelector sel1 = MethodSelector.parse(ensureExtractStringU(_sel1));
      MethodSelector sel2 = MethodSelector.parse(ensureExtractStringU(_sel2));
      return Abstract.toAbstract(Resources.getP(),that,path,sel1,sel2);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MaddDocumentation£xthat£xpath£xdoc(Object _that,Object _path,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnNestedClass(Resources.getP(),that,path,doc);
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MaddDocumentation£xthat£xpath£xselector£xdoc(Object _that,Object _path,Object _sel,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      MethodSelector sel = MethodSelector.parse(ensureExtractStringU(_sel));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnMethod(Resources.getP(),that,path,sel,doc);
      }

    @ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public Object MremoveUnreachableCode£xthat(Object _that){
      ClassB that=ensureExtractClassB(_that);
      return RemoveCode.removeUnreachableCode(that);
    }
    @ActionType({ActionType.Type.Void,ActionType.Type.Library})
    public  Resources.Void MifInvalidDo£xselector(Object _selector){
      String s=ensureExtractStringU(_selector);
     try{Ast.MethodSelector.parse(s);}
     catch(Resources.Error err){throw Resources.notAct;}
     return Resources.Void.instance;
    }
    @ActionType({ActionType.Type.Void,ActionType.Type.Library})
    public  Resources.Void MifInvalidDo£xpath(Object _path){
      String s=ensureExtractStringU(_path);
     try{Ast.Path.parseValidCs(s);}
     catch(Resources.Error err){
       throw Resources.notAct;
       }
      return Resources.Void.instance;
    }

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    //-----5 +5 introspections //lib
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectLibraryReport£xthat£xpath(Object _that,Object _path){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      List<Object> result= new ArrayList<>(Introspection.giveInfo(that, path));
      result.add("MyClass");
      result.add(Doc.factory("@."+String.join(".",path)));
      return Resources.Error.multiPartStringClassB("MemberReport",result.toArray());
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectLibraryReportMember£xthat£xpath£xmemberN(Object _that,Object _path,Object _memberN){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      return Introspection.giveInfoMember(that, path,memberN);
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectLibraryReportType£xthat£xpath£xmemberN£xtypeN(Object _that,Object _path,Object _memberN,Object _typeN){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      int typeN=ensureExtractInt32(_typeN);
      return Introspection.giveInfoType(null,Resources.getP(), that, path, memberN, typeN);
    }

    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectLibraryDocAsString£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Introspection.extractDocAsString(that, path, annotationN);
    }
    @ActionType({ActionType.Type.TypeAny,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectLibraryDocPath£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Configuration.reduction.convertPath(Introspection.extractDocPath(that, path, annotationN));
    }

    //-----5 +5 introspections //type
    @ActionType({ActionType.Type.Library,ActionType.Type.TypeAny,ActionType.Type.Library})
    public Object MintrospectTypeReport£xthat£xpath(Object _that,Object _path){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      if (iPath.isPrimitive()){throw Resources.notAct;}
      //TODO: provide fake classes
      ClassB that=Resources.getP().extractCb(iPath);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      List<Object> result= new ArrayList<>(Introspection.giveInfo(that, path));
      result.add("MyClass");
      List<String> cs = new ArrayList<>(iPath.getCBar());
      cs.addAll(path);
      iPath=Path.outer(iPath.outerNumber(),cs);
      iPath=Functions.add1Outer(Functions.add1Outer(iPath));
      //iPath=Functions.add1Outer(iPath);
      result.add(Doc.factory(iPath));
      return Resources.Error.multiPartStringClassB("MemberReport",result.toArray());
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.TypeAny,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectTypeReportMember£xthat£xpath£xmemberN(Object _that,Object _path,Object _memberN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractCb(iPath);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      return Introspection.giveInfoMember(that, path,memberN);
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.TypeAny,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectTypeReportType£xthat£xpath£xmemberN£xtypeN(Object _that,Object _path,Object _memberN,Object _typeN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractCb(iPath);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      int typeN=ensureExtractInt32(_typeN);
      return Introspection.giveInfoType(iPath,Resources.getP(), that, path, memberN, typeN);
    }

    @ActionType({ActionType.Type.Library,ActionType.Type.TypeAny,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectTypeDocAsString£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractCb(iPath);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Introspection.extractDocAsString(that, path, annotationN);
    }
    @ActionType({ActionType.Type.TypeAny,ActionType.Type.TypeAny,ActionType.Type.Library,ActionType.Type.Library})
    public Object MintrospectTypeDocPath£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractCb(iPath);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Configuration.reduction.convertPath(Introspection.extractDocPath(that, path, annotationN));
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public Object MprivateNormalize£xthat(Object _that){
      ClassB that=ensureExtractClassB(_that);
      //if(that.getStage().isPrivateNormalized()){return that;}
      return NormalizePrivates.normalize(Resources.getP(),that);
    }

    @ActionType({ActionType.Type.Library,ActionType.Type.Library})
    public  Object MfreshName£xthat(Object _s){
      String name=ensureExtractStringU(_s);
      if (name.isEmpty()){name="fresh";}
      return Functions.freshName(name,L42.usedNames);
      //good for both selectors and class names
      //good for current operators, but for tomorrow ones?
    }

   @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public  Object MmakeMethod£xthat£xpath£xselector£xmdfs£xexceptionN(Object _lib,Object _path,Object _selector, Object _mdfs, Object _execptionN){
      ClassB lib=ensureExtractClassB(_lib);
      List<String> path = Path.parseValidCs(ensureExtractStringU(_path));
      MethodSelector selector=MethodSelector.parse(ensureExtractStringU(_selector));
      String mdfs= ensureExtractStringU(_mdfs);
      int exceptionN=ensureExtractInt32(_execptionN);
      return MakeMethod.addMethod(lib, path, selector, mdfs, exceptionN);
    }
    @ActionType({ActionType.Type.Library,ActionType.Type.ImmAny,ActionType.Type.Library,ActionType.Type.Library})
    public  Object MliftValue£xthat£xselector£xlib(Object _that,Object _selector, Object _lib){
      ClassB lib=ensureExtractClassB(_lib);
      MethodSelector selector=MethodSelector.parse(ensureExtractStringU(_selector));
      ExpCore val=Revertable.doRevert(_that);//TODO: would go in loop for circular graphs?
      return LiftValue.liftValue(val,  selector, lib);
    }

  }

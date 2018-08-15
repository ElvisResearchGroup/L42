package is.L42.connected.withSafeOperators;

import static auxiliaryGrammar.EncodingHelper.ensureExtractClassB;
import static auxiliaryGrammar.EncodingHelper.ensureExtractStringU;
import static auxiliaryGrammar.EncodingHelper.ensureExtractInt32;
import static auxiliaryGrammar.EncodingHelper.ensureExtractPathFromJava;
import static auxiliaryGrammar.EncodingHelper.ensureExtractDoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Doc;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Position;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.PathAux;
import auxiliaryGrammar.Functions;
import programReduction.Program;
import coreVisitors.CloneVisitorWithProgram;
import coreVisitors.From;
import facade.L42;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SubtleSubtypeViolation;
import is.L42.connected.withSafeOperators.refactor.Compose;
import is.L42.connected.withSafeOperators.refactor.MakeK;
import is.L42.connected.withSafeOperators.refactor.RedirectObj;
import is.L42.connected.withSafeOperators.refactor.SumMethods;
import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import platformSpecific.javaTranslation.Resources.Revertable;
import privateMangling.RefreshUniqueNames;
import profiling.Timer;
import sugarVisitors.ToFormattedText;
import tools.Map;

//empty scheleton
@SuppressWarnings("unchecked")
public class Plugin implements PluginType.WellKnown {

   /* //we keep it for testing in testAux
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object Mcompose£xleft£xright(Object _left,Object _right) {
      ClassB left=ensureExtractClassB(_left);
      ClassB right=ensureExtractClassB(_right);
      try {return new Compose(left,right).compose(Resources.getP(),left,right);}
      catch (MethodClash | SubtleSubtypeViolation | ClassClash e) {
        throw new Error(e);
        }
      }*/
/*    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MrenameClass£xthat£xsrc£xdest(Object _that,Object _src,Object _dest) throws MethodClash, SubtleSubtypeViolation, ClassClash, PathUnfit{
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> src=PathAux.parseValidCs(ensureExtractStringU(_src));
      List<Ast.C> dest=PathAux.parseValidCs(ensureExtractStringU(_dest));
      //System.out.println("####################RENAMECLASS:"+src+"   "+dest);
      return Rename.renameClassAux(Resources.getP(),that,src,dest);
      }*/
  /*  @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MrenameMethod£xthat£xpath£xsrc£xdest(Object _that,Object _path,Object _src,Object _dest) throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
       MethodSelector src = MethodSelector.parse(ensureExtractStringU(_src));
       MethodSelector dest = MethodSelector.parse(ensureExtractStringU(_dest));
      return new RenameMethods().addRename(path,src,dest)
              .actP(Resources.getP(),that);
      }*/
   /* @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MsumMethods£xthat£xpath£xsrc1£xsrc2£xdest£xname(Object _that,Object _path,Object _src1,Object _src2,Object _dest,Object _name){
      ClassB that=ensureExtractClassB(_that);
      String name=ensureExtractStringU(_name);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
       MethodSelector src1 = MethodSelector.parse(ensureExtractStringU(_src1));
       MethodSelector src2 = MethodSelector.parse(ensureExtractStringU(_src2));
       MethodSelector dest = MethodSelector.parse(ensureExtractStringU(_dest));
      return SumMethods.sumMethods(that,path,src1,src2,dest,name);
      }*/
    //we keep it for testing in testAux
   /* @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.TypeAny})
    public Object Mredirect£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> src=PathAux.parseValidCs(ensureExtractStringU(_src));
      Path dest=ensureExtractPathFromJava(_dest);
      assert dest.isCore() || dest.isPrimitive():
        dest;
      if(dest.isCore()){dest=dest.setNewOuter(dest.outerNumber()+1);}//TODO: see if extractPath should be changed
      try {return new RedirectObj(that).redirect(Resources.getP(),src,dest);}
      catch (ClassUnfit | IncoherentMapping | MethodClash | PathUnfit e) {
        throw new Error(e);
        }
      }*/
    /*@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MremoveImplementation£xthat£xpath(Object _that,Object _path){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      return Abstract.toAbstract(that,path);
      }*/

    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MaddDocumentation£xthat£xpath£xdoc(Object _that,Object _path,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnNestedClass(Resources.getP(),that,(List<Ast.C>)_path,doc);
      }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MaddDocumentation£xthat£xpath£xselector£xdoc(Object _that,Object _path,Object _sel,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      MethodSelector sel = MethodSelector.parse(ensureExtractStringU(_sel));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnMethod(Resources.getP(),that,path,sel,doc);
      }

    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MremoveUnreachableCode£xthat(Object _that){
      ClassB that=ensureExtractClassB(_that);
      return RemoveCode.removeUnreachableCode(that);
    }
    @ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public  Resources.Void MifInvalidDo£xselector(Object _selector){
      String s=ensureExtractStringU(_selector);
     try{Ast.MethodSelector.parse(s);}
     catch(Resources.Error err){throw Resources.notAct;}
     return Resources.Void.instance;
    }
    @ActionType({ActionType.NormType.Void,ActionType.NormType.Library})
    public  Resources.Void MifInvalidDo£xpath(Object _path){
      String s=ensureExtractStringU(_path);
     try{PathAux.parseValidCs(s);}
     catch(Resources.Error err){
       throw Resources.notAct;
       }
      return Resources.Void.instance;
    }
/*
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
    //-----5 +5 introspections //lib
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectLibraryReport£xthat£xpath(Object _that,Object _path){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      List<Object> result= new ArrayList<>(Introspection.giveInfo(that, path));
      result.add("MyClass");
      result.add(Doc.factory(true,"@."+String.join(".",Map.of(ci->""+ci, path))));
      return Resources.Error.multiPartStringClassB("MemberReport",result.toArray());
    }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectLibraryReportMember£xthat£xpath£xmemberN(Object _that,Object _path,Object _memberN){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      return Introspection.giveInfoMember(that, path,memberN);
    }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectLibraryReportType£xthat£xpath£xmemberN£xtypeN(Object _that,Object _path,Object _memberN,Object _typeN){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      int typeN=ensureExtractInt32(_typeN);
      return Introspection.giveInfoType(null,Resources.getP(), that, path, memberN, typeN);
    }

    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectLibraryDocAsString£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Introspection.extractDocAsString(that, path, annotationN);
    }
    @ActionType({ActionType.NormType.TypeAny,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectLibraryDocPath£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Configuration.reduction.convertPath(Introspection.extractDocPath(that, path, annotationN));
    }

    //-----5 +5 introspections //type
    @ActionType({ActionType.NormType.Library,ActionType.NormType.TypeAny,ActionType.NormType.Library})
    public Object MintrospectTypeReport£xthat£xpath(Object _that,Object _path){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      if (iPath.isPrimitive()){throw Resources.notAct;}
      //TODO: provide fake classes
      ClassB that=Resources.getP().extractClassB(iPath);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      List<Object> result= new ArrayList<>(Introspection.giveInfo(that, path));
      result.add("MyClass");
      List<Ast.C> cs = new ArrayList<>(iPath.getCBar());
      cs.addAll(path);
      iPath=Path.outer(iPath.outerNumber(),cs);
      iPath=Functions.add1Outer(Functions.add1Outer(iPath));
      //iPath=Functions.add1Outer(iPath);
      result.add(Doc.factory(iPath));
      return Resources.Error.multiPartStringClassB("MemberReport",result.toArray());
    }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.TypeAny,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectTypeReportMember£xthat£xpath£xmemberN(Object _that,Object _path,Object _memberN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractClassB(iPath);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      return Introspection.giveInfoMember(that, path,memberN);
    }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.TypeAny,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectTypeReportType£xthat£xpath£xmemberN£xtypeN(Object _that,Object _path,Object _memberN,Object _typeN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractClassB(iPath);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int memberN=ensureExtractInt32(_memberN);
      int typeN=ensureExtractInt32(_typeN);
      return Introspection.giveInfoType(iPath,Resources.getP(), that, path, memberN, typeN);
    }

    @ActionType({ActionType.NormType.Library,ActionType.NormType.TypeAny,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectTypeDocAsString£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractClassB(iPath);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Introspection.extractDocAsString(that, path, annotationN);
    }
    @ActionType({ActionType.NormType.TypeAny,ActionType.NormType.TypeAny,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MintrospectTypeDocPath£xthat£xpath£xannotationN(Object _that,Object _path,Object _annotationN){
      Path iPath=(_that instanceof Path)?(Path)_that:(Path)((Revertable)_that).revert();
      ClassB that=Resources.getP().extractClassB(iPath);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
      int annotationN=ensureExtractInt32(_annotationN);
      return Configuration.reduction.convertPath(Introspection.extractDocPath(that, path, annotationN));
    }
    */
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MprivateNormalize£xthat(Object _that){
      ClassB that=ensureExtractClassB(_that);
      //if(that.getStage().isPrivateNormalized()){return that;}
      return RefreshUniqueNames.refresh(that);
    }

    /*@ActionType({ActionType.NormType.Library,ActionType.NormType.Library})
    public  Object MfreshName£xthat(Object _s){
      String name=ensureExtractStringU(_s);
      if (name.isEmpty()){name="fresh";}
      return Functions.freshName(name,L42.usedNames);
      //good for both selectors and class names
      //good for current operators, but for tomorrow ones?
    }*/

   @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public  Object MmakeMethod£xthat£xpath£xselector£xmdfs£xexceptionN(Object _lib,Object _path,Object _selector, Object _mdfs, Object _execptionN){
      ClassB lib=ensureExtractClassB(_lib);
      List<Ast.C> path = PathAux.parseValidCs(ensureExtractStringU(_path));
      MethodSelector selector=MethodSelector.parse(ensureExtractStringU(_selector));
      String mdfs= ensureExtractStringU(_mdfs);
      int exceptionN=ensureExtractInt32(_execptionN);
      return MakeMethod.addMethod(lib, path, selector, mdfs, exceptionN);
    }
    @ActionType({ActionType.NormType.Library,ActionType.NormType.ImmAny,ActionType.NormType.Library,ActionType.NormType.Library})
    public  Object MliftValue£xthat£xselector£xlib(Object _that,Object _selector, Object _lib){
      ClassB lib=ensureExtractClassB(_lib);
      MethodSelector selector=(MethodSelector)_selector;
      ExpCore val=Revertable.doRevert(_that);//TODO: would go in loop for circular graphs?
      val=From.from(val,Path.outer(1));
      return LiftValue.liftValue(val,  selector, lib);
    }

    /*@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public  Object MaddKs£xthat£xpath£xfields£xmutK£xlentK£xreadK£ximmK£xisFwd(
        Object _that,Object _path, Object _fields, Object _mutK,Object _lentK,Object _readK,Object _immK,Object _fwd
        ){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path = PathAux.parseValidCs(ensureExtractStringU(_path));
      String[] fieldNames=ensureExtractStringU(_fields).split(",");
      String mutK=ensureExtractStringU(_mutK);
      String lentK=ensureExtractStringU(_lentK);
      String readK=ensureExtractStringU(_readK);
      String immK=ensureExtractStringU(_immK);
      boolean fwd=ensureExtractInt32(_fwd)==0?false:true;
      return MakeK.makeKs(that, path, Arrays.asList(fieldNames), mutK, lentK, readK, immK, fwd);
    }*/
//--------------------------
   /*@ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MhideMethod£xthat£xpath£xsrc(Object _that,Object _path,Object _src){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> path=PathAux.parseValidCs(ensureExtractStringU(_path));
       MethodSelector src = MethodSelector.parse(ensureExtractStringU(_src));
       MethodSelector dest = src.withUniqueNum(L42.freshPrivate());
      return Rename.renameMethod(Resources.getP(),that,path,src,dest);
      }*/
   /* @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MhideClass£xthat£xsrc(Object _that,Object _src){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> src=PathAux.parseValidCs(ensureExtractStringU(_src));
      List<Ast.C> dest=new ArrayList<>(src);
      dest.set(dest.size()-1, dest.get(dest.size()-1).withUniqueNum(L42.freshPrivate()));
      return Rename.renameClass(Resources.getP(),that,src,dest);
      }*/

    /*
    @ActionType({ActionType.NormType.Library,ActionType.NormType.Library,ActionType.NormType.Library})
    public Object MhideState£xthat£xsrc(Object _that,Object _src){
      ClassB that=ensureExtractClassB(_that);
      List<Ast.C> src=PathAux.parseValidCs(ensureExtractStringU(_src));
      ClassB target=that.getClassB(src);
      List<MethodWithType>mwtA=target.mwts().stream().filter(mwt->!mwt.get_inner().isPresent()).collect(Collectors.toList());
      kjkkj typos to remember I was here
      we should take in input a candidete ks?

              map can go num to num!
              -state can all be labeled with the same num
              -change in consistent: fields inherit num of unique constructor
              -now, a num to num map keep consistent classes consistent!

              we can make a pluginwithpart taking a list of strings for candidate state?
              -new(path), addSelector, decorate(lib)?
               decorate: choose uniqueNum n.
                 for all selectors msi: RenameMethod path,msi, msi.with(n)

      //return Rename.renameClass(Resources.getP(),that,src,dest);
      return null;
      }*/

  }

package is.L42.connected.withSafeOperators;

import static auxiliaryGrammar.EncodingHelper.ensureExtractClassB;
import static auxiliaryGrammar.EncodingHelper.ensureExtractStringU;
import static auxiliaryGrammar.EncodingHelper.ensureExtractPathFromJava;
import static auxiliaryGrammar.EncodingHelper.ensureExtractDoc;

import java.util.List;

import static auxiliaryGrammar.EncodingHelper.ensureExtractInt32;
import static auxiliaryGrammar.EncodingHelper.ensureExtractInternalPath;

import ast.Ast.Doc;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;
import coreVisitors.CloneVisitorWithProgram;
import platformSpecific.fakeInternet.ActionType;
import platformSpecific.fakeInternet.PluginType;
import platformSpecific.javaTranslation.Resources;
import sugarVisitors.ToFormattedText;

//empty scheleton
public class Plugin implements PluginType{

    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object Mcompose£xleft£xright(Object _left,Object _right){
      ClassB left=ensureExtractClassB(_left);
      ClassB right=ensureExtractClassB(_right);
      return Sum.sum(Resources.getP(),left,right);      
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MrenameClass£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<String> src=Path.parseValidCs(ensureExtractStringU(_src));
      List<String> dest=Path.parseValidCs(ensureExtractStringU(_dest));
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
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.TypeAny})
    public Object Mredirect£xthat£xsrc£xdest(Object _that,Object _src,Object _dest){
      ClassB that=ensureExtractClassB(_that);
      List<String> src=Path.parseValidCs(ensureExtractStringU(_src));
      Path dest=ensureExtractPathFromJava(_dest);
      dest=dest.setNewOuter(dest.outerNumber()+1);//TODO: see if extractPath should be changed
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
      return Abstract.toAbstract(that,path,sel);      
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MaddDocumentation£xthat£xpath£xdoc(Object _that,Object _path,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnNestedClass(that,path,doc);      
      }
    @ActionType({ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library,ActionType.Type.Library})
    public Object MaddDocumentation£xthat£xpath£xselector£xdoc(Object _that,Object _path,Object _sel,Object _doc){
      ClassB that=ensureExtractClassB(_that);
      List<String> path=Path.parseValidCs(ensureExtractStringU(_path));
      MethodSelector sel = MethodSelector.parse(ensureExtractStringU(_sel));
      Doc doc=ensureExtractDoc(_doc);
      return AddDocumentation.addDocumentationOnMethod(that,path,sel,doc);      
      }
    //toabstract good name could be removeImplementation, and then add comment. still to add purge privates
   
}

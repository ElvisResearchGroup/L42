package coreVisitors;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import platformSpecific.javaTranslation.Resources;
import tools.Assertions;
import tools.Map;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.TraitHeader;
import ast.ExpCore;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.ClassB;
import ast.ExpCore.WalkBy;
import ast.ExpCore.ClassB.Member;
import auxiliaryGrammar.EncodingHelper;
import auxiliaryGrammar.Program;

public class RetainOnlyAndRenameAs extends CloneVisitor{
  //private Path path;
  private List<String> path;
  private MethodSelector ms1;
  private MethodSelector ms2;
  public RetainOnlyAndRenameAs(Path path, MethodSelector ms1,MethodSelector ms2) {
    this.path = new ArrayList<>(path.getCBar());
    this.ms1=ms1;
    this.ms2=ms2;
    }  
  public List<Member> liftMembers(List<Member> s) {
    if(!path.isEmpty()){
      String name=path.get(0);
      List<Member> result=new ArrayList<>();
      Optional<Member> mOpt = Program.getIfInDom(s, name);
      if(!mOpt.isPresent()){
        throw new Resources.Error(EncodingHelper.wrapStringU("RenamedNestedClassNotExistant:"+name));
        }
      //retain only
      result.add(mOpt.get());
      return Map.of(this::liftM,result);
      }
    //rename as
    List<Member> result=new ArrayList<>();
    Optional<Member> mOpt = Program.getIfInDom(s, this.ms1);
    if( mOpt.isPresent() ){
      Member m=mOpt.get();
      m=m.match(nc->{throw Assertions.codeNotReachable();},
          mi->{throw Assertions.codeNotReachable();},
        //mi->mi.withS(ms2).withInner(renameParameterAsVars(mi.getInner(),ms1,ms2)),
        mt->mt.withMs(ms2).withInner(Map.of(eMt->renameParameterAsVars(eMt,ms1,ms2),mt.getInner()))
        );
      result.add(m);
      }
    return result;
  }
  private static ExpCore renameParameterAsVars(ExpCore inner, MethodSelector ms1, MethodSelector ms2) {
    HashMap<String, String> toRename=new HashMap<>();
    {int i=-1;for(String x1i:ms1.getNames()){i+=1;
      String x2i=ms2.getNames().get(i);
      toRename.put(x1i, x2i);
      }}
    return RenameVars.of(inner, toRename);
  }
  public ExpCore visit(ClassB cb){
    ClassB cb2=new ClassB(Doc.empty(),Doc.empty(),cb.isInterface(),Collections.emptyList(),
        cb.getMs());
    return super.visit(cb2);
  }
  public ClassB.NestedClass visit(ClassB.NestedClass nc){
    if(!path.get(0).equals(nc.getName())){
      return nc;
      }
    List<String> aux = new ArrayList<>(path);
    path.remove(0);
    try{return super.visit(nc);}
    finally{path=aux;}
    }
  public static ClassB of(ClassB s,Path path, MethodSelector ms1,MethodSelector ms2) {
    RetainOnlyAndRenameAs rm=new RetainOnlyAndRenameAs(path, ms1,ms2);
    assert path.outerNumber()==0;
    return (ClassB)s.accept(rm);
  }
}
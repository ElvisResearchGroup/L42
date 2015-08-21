package ast;

import lombok.*;
import lombok.experimental.Wither;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.*;

public class Util {
  @Value @Wither public static class CsMx{
    @NonNull java.util.List<String> cs;@NonNull MethodSelector ms;
    public String toString(){
      String prefix="Outer0";
      if(!cs.isEmpty()){prefix=String.join("::",cs);}
      return prefix+"."+ms;}
    }
  @Value @Wither public static class PathMx{
    @NonNull Path path;@NonNull MethodSelector ms;
    public String toString(){return ""+path+"."+ms;}}
  @Value @Wither public static class PathMxMx{
    @NonNull Path path;@NonNull MethodSelector ms1;@NonNull MethodSelector ms2;}
  @Value @Wither public static class PathPath{
    @NonNull Path path1; @NonNull Path path2;
    public String toString(){return ""+path1+"->"+path2;}
    }
  

  public interface Locator{
	java.util.List<ClassB.Member> getMTail();
	java.util.List<Integer> getMPos();
	java.util.List<ClassB> getMOuters();
  @Data public static class ImplLocator implements Locator{
    @NonNull java.util.List<ClassB.Member> mTail;
  @NonNull java.util.List<Integer> mPos;
  @NonNull java.util.List<ClassB> mOuters;
    }
  }
  @Data public static class MethodLocator implements Locator{
    @NonNull java.util.List<ClassB.Member> mTail;
	@NonNull java.util.List<Integer> mPos;
	@NonNull java.util.List<ClassB> mOuters;
    @NonNull ClassB.MethodWithType that;
	MethodSelector newName;
    public String toString(){return coreVisitors.PathAnnotateClass.computeComment(mTail, mPos)+that.getMs()+newName;}}
  
  @Data public static class NestedLocator implements Locator{
    @NonNull java.util.List<ClassB.Member> mTail;
	@NonNull java.util.List<Integer> mPos;
	@NonNull java.util.List<ClassB> mOuters;
	@NonNull String that;
	String newName;
	Path newPath;//either newName or newPath always null
    public String toString(){return coreVisitors.PathAnnotateClass.computeComment(mTail, mPos)+that+newName;}}

  @Data public static class CachedStage{
	@NonNull ast.Ast.Stage stage=Stage.None;
	final java.util.Map<Path,ClassB>dependencies=new java.util.HashMap<>();
	final java.util.List<Path> allSupertypes=new java.util.ArrayList<>();
	boolean verified=false;	  
    }
  @Data public static class CachedMt{
	  ast.Ast.MethodType mt;
	  Path path;
  }
}

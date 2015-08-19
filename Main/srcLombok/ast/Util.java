package ast;

import lombok.NonNull;
import lombok.Value;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
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
  
  @Data @AllArgsConstructor public static class MethodLocator{@NonNull java.util.List<ClassB.Member> mTail; @NonNull java.util.List<Integer> mPos; ClassB.MethodWithType that; MethodSelector newName;
    public String toString(){return coreVisitors.PathAnnotateClass.computeComment(mTail, mPos)+that.getMs()+newName;}}
  @Data public static class NestedLocator{@NonNull java.util.List<ClassB.Member> mTail; @NonNull java.util.List<Integer> mPos;  @NonNull String that; String newName;Path newPath;//either newName or newPath always null
    public String toString(){return coreVisitors.PathAnnotateClass.computeComment(mTail, mPos)+that+newName;}}
}

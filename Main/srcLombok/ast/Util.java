package ast;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;
import ast.Ast.MethodSelector;
import ast.Ast.Path;

public class Util {
  @Value @Wither public static class PathMx{
    @NonNull Path path;@NonNull MethodSelector ms;
    public String toString(){return ""+path+"."+ms;}}
  @Value @Wither public static class PathMxMx{
    @NonNull Path path;@NonNull MethodSelector ms1;@NonNull MethodSelector ms2;}
  @Value @Wither public static class PathPath{
    @NonNull Path path1; @NonNull Path path2;}
  
}

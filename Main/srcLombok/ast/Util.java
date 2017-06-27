package ast;

import lombok.experimental.Wither;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;
import ast.Util.InvalidMwtAsState;


public class Util {
  @Value public static class PrivatePedex{
    int family;
    int number;
  }
  @Value public static class InfoAboutMs{
    @NonNull java.util.List<Path> allSuper;
    @NonNull Path original;
    @NonNull ast.Ast.MethodType mt;
  }
  @Value public static class InvalidMwtAsState{
    @NonNull String reason;
    @NonNull ExpCore.ClassB.MethodWithType mwt;
  }
  @Value public static class PathMwt{
    @NonNull Path original;
    @NonNull ast.ExpCore.ClassB.MethodWithType mwt;
    public String toString(){
      return ""+this.original+"::"
        +sugarVisitors.ToFormattedText.of(this.mwt).trim().replace("\n","");
      }
  }
  @Value @Wither public static class CsMx{
    @NonNull java.util.List<Ast.C> cs;
    @NonNull MethodSelector ms;
    public String toString(){
      String prefix=PathAux.as42Path(cs);
      return prefix+"::"+ms;}
    }
  @Value @Wither public static class CsPath{
    @NonNull java.util.List<Ast.C> cs;
    @NonNull Path path;
    public String toString(){
      String prefix=PathAux.as42Path(cs);
      return prefix+"->"+path;}
    }
  @Value @Wither public static class CsMxMx{
    @NonNull java.util.List<Ast.C> cs;
    boolean flag;
    MethodSelector ms1;
    MethodSelector ms2;
    public String toString(){
      String prefix=PathAux.as42Path(cs);
      return prefix+"["+flag+"]"+ms1+"->"+ms2;}
    }
  @Value @Wither public static class PathMx{
    @NonNull Path path;@NonNull MethodSelector ms;
    public String toString(){return ""+path+"::"+ms;}}
  @Value @Wither public static class PathMxMx{
    @NonNull Path path;@NonNull MethodSelector ms1;@NonNull MethodSelector ms2;}
  @Value @Wither public static class PathPath{
    @NonNull Path path1; @NonNull Path path2;
    public String toString(){return ""+path1+"->"+path2;}
    }
  @Data @Wither public static class CsSPath{
    @NonNull List<Ast.C> cs;
    @NonNull java.util.Set<Path> pathsSet;
    public String toString(){return ""+cs+"->"+pathsSet;}
    }
  @Data @Wither public static class CsMwtPMwt{
    @NonNull List<Ast.C> src1; 
    @NonNull ClassB.MethodWithType mwt1;
    @NonNull Path src2; 
    @NonNull ClassB.MethodWithType mwt2;
    //@NonNull List<Path> paths1; @NonNull List<Path> paths2;
    //public String toString(){return ""+paths1+"->"+paths2;}
    }

}

package ast;

import lombok.*;
import lombok.experimental.Wither;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ExpCore.*;
import ast.ExpCore.ClassB.Member;


public class Util {
  @Value public static class InfoAboutMs{
    @NonNull java.util.List<Path> allSuper;
    @NonNull Path original;
    @NonNull ast.Ast.MethodType mt;
  }
  @Value public static class PathMwt{
    @NonNull Path original;
    @NonNull ast.ExpCore.ClassB.MethodWithType mwt;
  }
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
  @Data @Wither public static class PathSPath{
    @NonNull Path path; @NonNull List<Path> paths;
    public String toString(){return ""+path+"->"+paths;}
    }
  @Data @Wither public static class SPathSPath{
    @NonNull Path src; @NonNull ClassB.MethodWithType mwt1;@NonNull ClassB.MethodWithType mwt2;
    //@NonNull List<Path> paths1; @NonNull List<Path> paths2;
    //public String toString(){return ""+paths1+"->"+paths2;}
    }  

  @Data public static class CachedStage{
	@NonNull ast.Ast.Stage stage=Stage.None;
	final java.util.List<ClassB>dependencies=new java.util.ArrayList<>();
	//final java.util.List<Path> allSupertypes=new java.util.ArrayList<>();
	java.util.ListIterator<PathMwt> inherited=null;
	boolean verified=false;
	boolean coherent=true;
	String givenName="";
	boolean privateNormalized=false;
	final java.util.List<Integer> families=new java.util.ArrayList<>();
    }
  @Data public static class CachedMt{
	  ast.Ast.MethodType mt;
	  Path path;
  }
}

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


public class Util {

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

  @Data @Wither public static class CsSPath{
    @NonNull List<Ast.C> cs;
    @NonNull java.util.Set<Path> pathsSet;
    public String toString(){return PathAux.as42Path(cs)+"->"+PathAux.asSet(pathsSet);}
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

package platformSpecific.fakeInternet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import ast.Ast.Mdf;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.NormType;
import ast.Ast.Path;
import ast.Ast.Ph;
import ast.Ast.Type;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionType {
  public static enum Type{
    Library(new Ast.NormType(Mdf.Immutable,Path.Library(),Ph.None)),
    Void(new Ast.NormType(Mdf.Immutable,Path.Void(),Ph.None)),
    ImmAny(new Ast.NormType(Mdf.Immutable,Path.Any(),Ph.None)),
    TypeAny(new Ast.NormType(Mdf.Type,Path.Any(),Ph.None)),
    CapsuleAny(new Ast.NormType(Mdf.Capsule,Path.Any(),Ph.None)),
    MutAny(new Ast.NormType(Mdf.Mutable,Path.Any(),Ph.None)),
    LentAny(new Ast.NormType(Mdf.Lent,Path.Any(),Ph.None)),
    ReadAny(new Ast.NormType(Mdf.Readable,Path.Any(),Ph.None));
    Ast.Type type;
    Type(Ast.Type type){this.type=type;}
    
    public static final Ast.MethodType mt(Mdf mdf,Type []paths){
      assert paths.length>0;
      ArrayList<Doc> docs=new ArrayList<>();
      ArrayList<Ast.Type> ts=new ArrayList<>();
      for(int i=1;i<paths.length;i++){
        ts.add(paths[i].type);
        docs.add(Doc.empty());
        }
      return new Ast.MethodType(Doc.empty(),mdf,ts,docs,paths[0].type,Collections.emptyList());
    }
  }
  Type[] value();
  Mdf mdf() default Mdf.Immutable;
}

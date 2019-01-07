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
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.Type;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionType {
  public static enum NormType{
    Library(Ast.Type.immLibrary),
    Void(Ast.Type.immVoid),
    //ImmAny(Ast.Type.immAny),
    TypeAny(Ast.Type.classAny);
    //CapsuleAny(Ast.Type.immAny.withMdf(Mdf.Capsule)),
    //MutAny(Ast.Type.immAny.withMdf(Mdf.Mutable)),
    //LentAny(Ast.Type.immAny.withMdf(Mdf.Lent)),
    //ReadAny(Ast.Type.immAny.withMdf(Mdf.Readable));
    Ast.Type type;
    NormType(Ast.Type type){this.type=type;}
  }
  NormType[] value();
  //Mdf mdf() default Mdf.Immutable;
}

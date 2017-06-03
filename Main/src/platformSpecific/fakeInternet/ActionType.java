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
import ast.Ast.NormType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionType {
  public static enum NormType{
    Library(Ast.NormType.immLibrary),
    Void(Ast.NormType.immVoid),
    ImmAny(Ast.NormType.immAny),
    TypeAny(Ast.NormType.classAny),
    CapsuleAny(Ast.NormType.immAny.withMdf(Mdf.Capsule)),
    MutAny(Ast.NormType.immAny.withMdf(Mdf.Mutable)),
    LentAny(Ast.NormType.immAny.withMdf(Mdf.Lent)),
    ReadAny(Ast.NormType.immAny.withMdf(Mdf.Readable));
    Ast.NormType type;
    NormType(Ast.NormType type){this.type=type;}
  }
  NormType[] value();
  //Mdf mdf() default Mdf.Immutable;
}

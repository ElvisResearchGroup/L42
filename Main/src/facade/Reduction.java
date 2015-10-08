package facade;

import ast.Ast.Path;
import ast.ExpCore.ClassB;

public interface Reduction {
  ClassB of(ClassB topLevel);

  Object convertPath(Path p);
}

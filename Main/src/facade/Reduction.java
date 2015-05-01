package facade;

import ast.ExpCore.ClassB;

public interface Reduction {
  ClassB of(ClassB topLevel);
}

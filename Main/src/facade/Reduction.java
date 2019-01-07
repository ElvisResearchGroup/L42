package facade;

import ast.Ast.Path;
import ast.Ast;
import ast.ExpCore;
import ast.ExpCore.ClassB;

public interface Reduction {
  ClassB of(ClassB topLevel);
  Object convertPath(Path p);
  ExpCore metaExp(PData p, ExpCore e,Ast.C nameDebug);
}

package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.PathAux;
import facade.PData;
import programReduction.Program;

public class Rename {
  public static ClassB renameClass(PData pData,ClassB that,String nameSrc,String nameDest){
    return is.L42.connected.withSafeOperators.Rename.renameClass(pData.p, that, PathAux.parseValidCs(nameSrc), PathAux.parseValidCs(nameDest));
    }
}

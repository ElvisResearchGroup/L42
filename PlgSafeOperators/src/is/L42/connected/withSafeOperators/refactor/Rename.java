package is.L42.connected.withSafeOperators.refactor;

import ast.Ast.Path;
import ast.ExpCore.ClassB;
import ast.PathAux;
import facade.PData;
import programReduction.Program;
interface RenameSpec{
/**
Pop: remove the last layer (may need to check for no one use the last layer)
  As in: {C:{..}}[pop]={..}[from This0.C]
  defined only if
  -there is only one nested, and no other methods
  -no interface, no implements
  -no reference to This0
*/void pop();

/**
Push: L[push Cs]
  push all in a nested of its own
  As in: {..}[push C]={C:{..}[from This1??]}
*/void push();

/**
DirectRename
L0[DirectRename src->dest]p =L //with src!=dest._ and dest!= src._
  L1=L0[PathRename src->dest]
  L2=L1\src //similar to L[src=undefined]
  L3=L1(src)
  L4=L3[from This(dest.size()-1).src.removeLast()][push dest]
  L=L2++p L4
*/void directRename();

/**
Rename:  L[rename src -> dest]
  if with src!=dest._ and dest!= src._
    then L[rename src -> dest]=L[directRename src -> dest]
  else
    L[rename src -> dest]=
      L[push Fresh0]
      [directRename Fresh0.src->Fresh1]
      [directRename Fresh1->Fresh0.dest]
      [pop Fresh0]
*/void rename();
}

public class Rename {
  /**{@link RenameSpec#pop}*/
  public ClassB pop(ClassB l){
    return l;
    }
  public static ClassB renameClass(PData pData,ClassB that,String nameSrc,String nameDest){
    return is.L42.connected.withSafeOperators.Rename.renameClass(pData.p, that, PathAux.parseValidCs(nameSrc), PathAux.parseValidCs(nameDest));
    }
}


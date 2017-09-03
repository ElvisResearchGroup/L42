package newReduction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import ast.Ast.Doc;
import ast.ExpCore;
import ast.ExpCore.ClassB.Phase;
import ast.Ast;
import ast.MiniJ;
import ast.MiniJ.M;
import helpers.TestHelper;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;
import programReduction.Program;

public class TestLoader {
@Test  public void testPrintHiHi() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CompilationError {
  Loader l=new Loader();
  M m1=new M(true,"Object","execute0",Collections.emptyList(),Collections.emptyList(),new MiniJ.RawJ(
    "{System.out.println(\"hihi\");return null;}"));
  l.run(new MiniJ.CD(false, "Hi",Collections.emptyList(),Collections.singletonList(m1)));
  }

@Test  public void testReturnEmptyLib(){
  Loader l=new Loader();
  ExpCore.ClassB cbA=new ExpCore.ClassB(Doc.factory(false,"hello there"),false,Collections.emptyList(),Collections.emptyList(), Ast.Position.noInfo, Phase.Coherent,34);
  ExpCore.ClassB cbB=l.run(Program.emptyLibraryProgram(), cbA);
  TestHelper.assertEqualExp(cbA,cbB);
  }


}

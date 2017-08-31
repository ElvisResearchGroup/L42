package newReduction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import ast.MiniJ;
import ast.MiniJ.M;
import platformSpecific.inMemoryCompiler.InMemoryJavaCompiler.CompilationError;

public class TestLoader {
@Test  public void test() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CompilationError {
  
  Loader l=new Loader();
  M m1=new M("Object","execute0",Collections.emptyList(),Collections.emptyList(),new MiniJ.RawJ(
    "{System.out.println(\"hihi\");return null;}"));
  l.runAndTrash(new MiniJ.CD(false, "Hi",Collections.emptyList(),Collections.singletonList(m1)));
  }
}

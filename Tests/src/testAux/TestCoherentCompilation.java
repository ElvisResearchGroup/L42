package testAux;

import ast.ErrorMessage;
import ast.ErrorMessage.FinalResult;
import ast.ErrorMessage.PathMetaOrNonExistant;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Functions;
import facade.ErrorFormatter;
import facade.L42;
import facade.Parser;
import helpers.TestHelper;
import newTypeSystem.ErrorKind;
import newTypeSystem.FormattedError;
import org.junit.Test;
import platformSpecific.javaTranslation.Resources;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;

public class TestCoherentCompilation
{
  public static void tp(String ...code)
  {
    TestHelper.configureForTest();
    String testCode = Functions.multiLine(
      "{                                       ",
      "  $: {" + Functions.multiLine(code) + "}",
      "  $$: (                                 ",
      "    class Any t = $                     ", // Force compilation of 'Test
      "    {//@exitStatus\n//0\n}              ", // L42.RunSlow needs this
      "  )                                     ",
      "}                                       "
    );

    try { L42.runSlow(null, testCode); }
    catch(ErrorMessage msg)
    {
      ErrorFormatter.topFormatErrorMessage(msg);
      throw msg;
    }
  }
  // Note: I use the term 'constructor' to mean an abstract class method of a coherent class (i.e. calling it will create a new instance of the class)

  @Test public void test_empty(){tp( // Works!
    // No abstract class methods
  );}
  @Test public void test_uninihabited(){tp(
    // No abstract class methods
    "method This0 foo(This0 a, This0 b)"
  );}


// TODO: Play with fwd's! (Since Java code generation is different for them)
  @Test public void test_multi_ctor_empty(){tp( // Works!
    // Both constructors have the same fields
    "class method This0 c1()",
    "class method This0 c2()"
  );}

  @Test public void test_multi_ctor_duplicate(){tp(
    // Both constructors have the same fields
    "class method This0 c1(Void f)",
    "class method This0 c2(Void f)"
  );}

  @Test public void test_multi_ctor_different(){tp(
    // Both constructors have the same fields
    "class method This0 c1(Void f1, class Any f2)",
    "class method This0 c2(Void f2, class Any f1)"
  );}

  @Test public void test_immutable_class(){tp(
    // Has no mut/lent/capsule constructors
    "class method This0()",
    // Has a mut/lent/capsule abstract method
    "mut method Void foo()"
  );}

  @Test public void test_ungettable_field(){tp( // Works!
    // f has no getters
    "class method mut This0(class Any f)",
    "mut method Void f(Library that)",
    "mut method Void #f(mut Void that)"
  );}

  @Test public void test_non_void_setter(){tp( // Works!
    "class method mut This0(Void f)",
    // Returns a supertype of imm Void
    "mut method read Any f(Void that)"
  );}

  @Test public void test_multiple_getters(){tp( // Works!
    "class method capsule This0(capsule Library f)",
    // Getter types are both supertypes of capsule Library
    "capsule method Any f()",
    "capsule method read Library #f()"
  );}
  @Test public void test_supertype_getters(){tp(
      "class method capsule This0 a(Library f)",
      "class method capsule This0 b(Void f)",
      // Getter types are both supertypes of capsule Library
      "method Any f()"
    );}
}
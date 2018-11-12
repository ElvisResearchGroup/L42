package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;

import ast.Ast;
import ast.Ast.*;
import ast.ExpCore;
import ast.L42F;
import helpers.TestHelper.ErrorCarry;
import is.L42.connected.withSafeOperators.pluginWrapper.*;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.RedirectError;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.RedirectError.*;
import is.L42.connected.withSafeOperators.refactor.RedirectObj;
import helpers.TestHelper;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.ExpCore.ClassB;
import programReduction.Program;
import tools.*;

@RunWith(Parameterized.class)
public class TestRedirect {

  // TODO@James: consider making a git hook to block commits unless startLine=0 is enabled

  // SKIP NEW TESTS
  static int startLine=51; // was 159

  static Object NestedClassTest =  RedirectError.ClassUnfit.class;
  static Object NestedClassTest(Object o) { return o; }
  static Object ExceptionTest = RedirectError.DeductionFailure.class;

  // TODO: I am annotating tests with this when the input is complete, but the output is not
  // Due to 'possibleRedirect' failing on the input
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public Object _p;
  @Parameter(2) public String _cb1;
  @Parameter(3) public String _path1;
  @Parameter(4) public String _path2;
  @Parameter(5) public Object _expected;

  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    ErrorCarry ec = new ErrorCarry();

    List<Object[]> tests= Arrays.asList(new Object[][]{

    {lineNumber(), new String[]{"{ED: {} EB: {C: {ED m}} EA: {method EB m()}}"},
      "{" +
        "D:{}" + 
        "B: {C: {D m}}" +
        "A: {method B m()}" +
        "method Void result(A a, B b, D c)" +
        "}",
        "A", "This0.EA",
        NestedClassTest //{method Void result(This1.EA a, This1.EA.B b, This1.EC c)}
    },{lineNumber(), new String[]{"{" +
        "EB1: {interface C:{interface implements EB0.C}}" +
        "EB0: {interface implements EB1 C:{interface}}" +
        "EA: {method EB0 m()}" + "}"
    },"{" +
        "B: {C: {}}" +
        "A: {method B m()}" + // Should fail!
        "method Void result(A a, B b)" +
        "}",
        "A", "This0.EA",
        NestedClassTest //{method Void result(This1.EA a, This1.EB0 b)}
    },{lineNumber(), new String[]{"{" + // No way our algorithm can solve this one!
          "EB1: {interface C:{implements EBC}}" +
          "EB0: {interface implements EB1 C:{}}" +
          "EBC: {interface}" +
          "EA: {method EB0 m(EBC x)}" + "}"
      },"{" +
          "B: {C: {}}" +
          "A: {method B m(B.C x)}" + // EBA <= A, B.C <= EBC
          "method Void result(A a, B b)" +
          "}",
          "A", "This0.EA",
          NestedClassTest(RedirectError.DeductionFailure.class) //{method Void result(This1.EA a, This1.EB1 b)}
    }, {lineNumber(), new String[]{"{" + // Test for possible redirect with NCs
          "EB2: {interface                C:{Void f class method Void cm()}}" +
          "EB1: {interface implements EB2 C:{interface class method Void cm()}}" +
          "EB0: {interface implements EB1 C:{}}" +
          "EA: {method EB0 m()}}"
    },"{" +
          "B: {interface method Void foo() C:{class method Void cm()}}" +
          "A: {method B m()}" +
          "method Void result(A a, B b)" +
          "}",
          "A", "This0.EA",
          NestedClassTest //{method Void result(This1.EA a, This1.EB2 b)}
    }, {lineNumber(), new String[]{"{" + // Test for possible redirect:
          "EB1: {interface method Void foo()}" +
          "EB0: {interface implements EB1 method Void bar()}" +
          "EA: {method EB0 m()}}"
    },"{" +
          "B: {interface method Void foo()}" +
          "A: {method B m()}" +
          "method Void result(A a, B b)" +
          "}",
          "A", "This0.EA",
          "{method Void result(This1.EA a, This1.EB1 b)}"
    }, {lineNumber(), new String[]{"{" + // Test for possible redirect:
        "EI1: {interface}" +
        "EI2: {interface}" +
        "EI3: {interface implements EI2}" +
        "EC:  {implements EI1, EI3}" +
        "EBB: {interface method read EI2 foo()}" +
        "EB1: {interface implements EBB refine method EI1 foo()}" +
        "EB0: {interface implements EBB refine method EI2 foo()}" +
        "EB:  {implements EB0, EB1, EBB refine method EC foo()}" +
        "EA:  {method EB m1()}}"
    },"{" +
           "B: {interface method read This2.EI2 foo()}" +
           "A: {method B m1() }" +
           "method Void result(A a, B b)" +
           "}",
           "A", "This0.EA",
           "{method Void result(This1.EA a, This1.EBB b)}"
    }, {lineNumber(), new String[]{"{" + // This is just a horribley stupid thing I mindlessley wrote...
      "EC: {interface}" +
      "ED: {interface method Void n(EB2 b, EA2 a)}"+
      "EB:  {interface                method ED m(EC x)}" +
      "EB2: {interface implements EB  refine method ED m(EC x)}" +
      "EA:  {interface implements EB2  refine method ED m(EC x)}" +
      "EA2: {interface implements EA, EB, EB2}" +
      "EV:  {method Void m(EA x)}" +
    "}"},"{" +
      "C :{}" +
      "D: {interface method Void n(B b, A a)}" +
      "B: {interface           method D m(C x)}" +
      "A: {implements B refine method D m(C x)}" +
      "V: {method Void m(A x)}" +
      "method Void result(V r, A a, B b, C c)" +
      "}",
      "V", "This0.EV",
      "{method Void result(This1.EV r, This1.EA2 a, This1.EB2 b, This1.EC c)}"
//----------------------------
    }, {lineNumber(), new String[]{"{" +
      "EBB: {interface method EA f(EBB x)}" +
      "EB: {interface implements EBB}" +
      "EA: {interface method EB n()}" +
      "EV:  {method Void m(EA x)}" +
    "}"},"{" +
      "B: {interface method A f(B x)}" +
      "A: {method B n()}" +
      "V: {method Void m(A x)}" +
      "method Void result(V r, A a, B b)" +
      "}",
      "V", "This0.EV",
      "{method Void result(This1.EV r, This1.EA a, This1.EBB b)}"
    }, {lineNumber(), new String[]{"{" +
      "EB: {method EA f()}" +
      "EA: {interface method EB n()}" +
      "EV:  {method Void m(EA x)}" +
    "}"},"{" +
      "B: {method A f()}" +
      "A: {method B n()}" +
      "V: {method Void m(A x)}" +
      "method Void result(V r, A a, B b)" +
      "}",
      "V", "This0.EV",
      "{method Void result(This1.EV r, This1.EA a, This1.EB b)}"
      }, {lineNumber(), new String[]{"{EB: {interface method Void f(EA x)}" +
                  "EA: {interface method EB n()}" +
                  "ER: {method Void m(EA x)}" +
                  "}"
              },"{" +
                  "B: {interface method Void f(A x)}\n" +
                  "A: {interface method B n()}" +
                  "R: {method Void m(A x)}" +
                  "method Void result(R r, A a, B b)" +
                  "}",
                      "R", "This0.ER",
                      "{method Void result(This1.ER r, This1.EA a, This1.EB b)}"
      }, {lineNumber(), new String[]{"{EB: {}" +
          "EA: {interface method EB n(EA z)}" +
          "ER: {method Void m(EA x)}" +
          "}"
      },"{" +
          "B: {}\n" +
          "A: {interface method B n(A z)}" +
          "R: {method Void m(A x)}" +
          "method Void result(R r, A a, B b)" +
      "}",
          "R", "This0.ER",
          "{method Void result(This1.ER r, This1.EA a, This1.EB b)}"

    }, {lineNumber(), new String[]{"{EB: {}}"},"{" +
          "X: {interface}" +
          "B: {implements X}" +
          "method Void result(B b, X x)" +
          "}",
          "B", "This0.EB",
          "{method Void result(This1.EB b, Any x)}"
//------------------------------------------
    },{lineNumber(),
      "{EA:{interface method Any m()}" +
      "  EA1:{interface implements EA}" +
      "  EA2:{interface implements EA refine method Void m()}" +
      "  E:{implements This1.EA1 This1.EA2 method Void m2(EA1 that)}" +
      "  EV:{method E m()}}",
      "{I:{interface implements IA}" +
      "  IA:{interface method Void m()}" +
      "  V:{method I m()}" +
      "  method IA a()}",
      "V", "This0.EV",
      "{method This1.EA2 a()}"
    },{lineNumber(), new String[]{"{"+ // Fails, cant resolve ambiguity when interfaces have different methods
            "EA:{interface method Void m()}" +
            "EA1:{interface implements EA}" +
            "EA2:{interface implements EA}" +
            "E:{implements This1.EA1 This1.EA2 method Void m2(EA1 that)}}"
    },"{" +
            "I:{implements IA method Void m2(IA that)}" +
            "IA:{interface method Void m()}" +
            "method IA a()" +
            "}",
            "I", "This0.E",
            "{method This1.EA1 a()}" // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{"+ // Fails, cant resolve ambiguity when interfaces have different methods
            "EI1:{interface method Any m1()}" +
            "EI2:{interface method Any m2()}" +
            "EA1:{interface implements EI1 EI2 refine method Void m1()}" +
            "EA2:{interface implements EI1 EI2}" +
            "E:{implements This1.EA1 This1.EA2 method Void bar()}}"
    },"{" +
            "I:{implements IA}" +
            "IA:{interface method Void m1() method Any m2()}" +
            "method IA a()" +
            "}",
            "I", "This0.E",
            "{method This1.EA1 a()}" // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{"+ // Fails, cant resolve ambiguity when interfaces have different methods
            "EI1:{interface method Void m1()}" +
            "EI2:{interface method Void m2()}" +
            "EA1:{interface implements EI1 EI2}" +
            "EA2:{interface implements EI1 EI2}" +
            "E:{interface implements This1.EA1 This1.EA2}}"
    },"{" +
            "I:{implements IA}" +
            "IA:{interface method Void m1() method Void m2()}" +
            "method IA a()" +
            "}",
            "I", "This0.E",
            "{method This1.E a()}" // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{"+ // Fails, cant resolve ambiguity when interfaces have different methods
            "EA:{interface method Void ma()}" +
            "EB:{interface method Void mb()}" +
            "E:{implements This1.EA This1.EB}}"
    },"{" +
            "I:{implements IA IB}" +
            "IA:{interface method Void ma()}" +
            "IB:{interface method Void mb()}" +
            "method IA a() method IB b()" +
            "}",
            "I", "This0.E",
            "{method This1.EA a() method This1.EB b()}" // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{EB: {" // Unable to handle subtyping
        + "AI :{interface}"
        + "A: {implements AI}"
        + "method A bar()"
        + "method Void foo(AI that)}}"},

        "{B: {A:{} method A bar() method Void foo(A that)} method Void baz(B b, B.A ba)}",
        "B", "This0.EB",
        "{method Void baz(This1.EB b, This1.EB.A ba)}" // FAILS IncoherentMapping

    },{lineNumber(), new String[]{"{EB: {method Void foo()}}"}, // Unable to handle subtyping
        "{B: {method Any foo()}}",
        "B", "This0.EB",
        "{}"

    },{lineNumber(), new String[]{"{"+ // Fails, due to no f-bound polymorphism trick
            "E: {interface method Void equals() method Void foo()} \n" +
            "EA: {implements This1.E}}"},
            "{\n"+
                    "A: {implements This1.I, This2.E}\n"+
                    "I: {interface implements This2.E refine method Void foo()}\n" +
                    "method I i()\n" +
                    "}",
            "A", "This0.EA",
            "{method This1.E i()}" // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{EA1: {} " // Fails, exception spec redirection is ambiguous?
        + "E: {method Void m1() exception EA1 method Void m12() exception EA1}}"},
        "{A1:{}, A2: {}\n" +
        "A:{" +
        "  method Void m1() exception A1" +
        "  method Void m12() exception A1 A2" +
        "}" +
        // Should this fail?
        "method Void m(A a, A1 a1, A2 a2)" +
        "}",
        "A", "This0.E",
        ExceptionTest

    },{lineNumber(), new String[]{"{"+ // Fails, no ambiguity resolution when interfaces have different methods
            "EI2:{interface method Void foo()}\n" +
            "EI1:{interface implements EI2 method Void bar()}\n" +
            "E:{implements EI1, EI2}\n"+
            "}"
    },
            "{\n"+
                    "I:{interface method Void foo()}"+
                    "A:{implements I}"+
                    "method I m(I x)" +
                    "}",
            "A", "This0.E",
            "{method This1.EI2 m(This1.EI2 x)}" // Because only EI1 contains just "foo"

    },{lineNumber(),// Fails, redirecting interface with different methods
            new String[]{"{E:{interface method Void foo()}}"},
            "{I:{interface} A:{implements I}}",
            "I", "This0.E", // This0.E <= I <= This0.E // {}
        RedirectError.InvalidMapping.class
    },{lineNumber(),// Fails, redirecting interface with different method signature
            new String[]{"{E:{interface method Void foo()}}"},
            "{I:{interface method Void foo() exception I}\n"+
                    "A:{implements I refine method Void foo() exception I}}",
            "I", "This0.E",
        RedirectError.InvalidMapping.class // I.foo() and E.foo() are incompatable
    },{lineNumber(),// Fails, redirecting interface with different methods
                    "E1:{interface method Void foo()}\n" +
                    "E2:{interface method Void foo()}\n" +
                    "E: {method E1 e1() method E2 e2()}\n",
            "{\n" +
                    "I1:{interface} I2:{interface} \n"+
                    "B: {method I1 e1() method I2 e2()}\n"+
                    "A:{interface implements I1 I2}\n" +
                    "}",
            "B", "This0.E",
            "{A: {interface}}" // TODO: OK? Can redirect I1 and I2 to any!
            // SHOULD FAIL
    }, {lineNumber(), // Fails, as no p.equiv
            mkp("{C: {EA:{EB:{}, method This2.C.EA.EB g() method This0.EB f()}}}").push(C.of("C")),
            //mkp("{C: {EA:{EB:{}, method This2.C.EA.EB g() method This0.EB f()}}}").push(C.of("C")),
            "{A:{method B g() method B f()} B:{} method B foo()}",
            "A", "This0.EA",
            "{method This1.EA.EB foo()}"

    },{lineNumber(), // Fails, bad error message
      new String[]{"{EA:{interface A:{implements EA}}}"},
      "{interface A:{implements This1}}",
      "A", "This0.EA.A",
      RedirectError.ClassUnfit.class // Cannot redirect This
//===============================================================================
    },{lineNumber(),//PASS, but next
      new String[]{"{}"}, "{A:{}}", "A", "Void", "{}"
    },{lineNumber(),//PASS, but next
      new String[]{"{C:{EB:{}, EA:{method This1.EB f() method This2.C.EB g()}}}"},
      "{A:{method B f() method B g()} B:{} method B foo()}",
      "A", "This0.C.EA",
      "{method This1.C.EB foo()}"
    },{lineNumber(), new String[]{"{"+ // PASS!
        "E1: {interface} E2: {interface}\n" +
        "EA: {implements This1.E1 This1.E2}}"},
        "{\n"+
                "A: {implements This1.I, This2.E2}\n"+
                "I: {interface}\n" +
                "method I foo()\n" +
                "}",
        "A", "This0.EA",
        RedirectError.DeductionFailure.class // UNSPECIFIC: Can't decide whether I should be E1, E2, or Any

    },{lineNumber(), // PASS!
            new String[]{"{E:{class method Void foo()}}"},
            "{Z:{class method Void foo() exception Z}\n"+
                    "A:{method Void foo() exception Z Z.foo()}}",
            "Z", "This0.E",
            "{A:{method Void foo() exception This2.E This2.E.foo()}}"
    },{lineNumber(), // PASS
            new String[]{"{E:{interface method Void foo() exception E}}"},
            "{I:{interface method Void foo()}\n"+
                    "A:{implements I refine method Void foo()}"+
                    "class method Void(I i) i.foo()}",
            "I", "This0.E",
            RedirectError.InvalidMapping.class // Target foo() throws more exceptions that source!
            // SHOULD FAIL
    },{lineNumber(),//PASS
            new String[]{"{Foo: {implements This1.EA}, EA: {interface B:{}}}"},
            "{A:{interface      B:{implements A}} method Void result(A a, A.B b)}",
            "A.B", "This0.Foo",
            "{method Void result(This1.EA a, This1.Foo b)}"
    },{lineNumber(),//PASS//Should be shown in the article
            new String[]{"{EA:{interface B:{implements EA}}}"},
            "{A:{interface      B:{implements A}} class method A m(A.B x)}",
            "A.B", "This0.EA.B",
            "{class method This1.EA m(This1.EA.B x) }"
// TODO END
    }, {lineNumber(), new String[]{"{A:{}}"},
      "{InnerA:{} B:{ method InnerA m(InnerA a) a}}","InnerA", "This0.A",
      "{B:{ method This2.A m(This2.A a) a}}"
    }, {lineNumber(),//
          "D:{method Library m(This1.D a)}",
          "{A:{method Library m(This1.A a) } method Void b(This0.A m)}"
          ,"A","This0.D",
          "{method Void b(This1.D m)}"
    },{lineNumber(), new String[]{"{A:{}}"},
        "{InnerA:{}  method InnerA m(InnerA a) a}","InnerA", "This0.A",
        "{ method This1.A m(This1.A a) a}"
    },{lineNumber(), new String[]{ // Redirect free templates into primitive types
        "{A:{ method Void fun(Library that, Any other)}}"},
        "{InnerVoid:{} InnerLib:{} InnerAny:{}"
        + "InnerA:{method InnerVoid fun(InnerLib that, InnerAny other)}"
        + "method InnerAny moreFun(InnerVoid that, InnerLib other)"
        + "}","InnerA", "This0.A",
        RedirectError.DeductionFailure.class // InnerAny can be redirected to any subtype of Any, so it is ambiguous
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","InnerA", "This0.A1",
        "{ method This1.A1 m(This1.A1 a) a}"
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","InnerA", "This1.A2",
        "{ method This2.A2 m(This2.A2 a) a}"
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes
        "{InnerA:{}  method InnerA m(InnerA a) a}","InnerA", "This0.A1.A1n","{ method This1.A1.A1n m(This1.A1.A1n a) a}"
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes in further out scope
        "{InnerA:{}  method InnerA m(InnerA a) a}","InnerA", "This1.A2.A2n",
        "{ method This2.A2.A2n m(This2.A2.A2n a) a}"
    },{lineNumber(), new String[]{"{A:{method B getB()} B:{}}"}, // cascade: a return value in A redirects B
        "{InnerA:{method InnerB getB()} InnerB:{} method InnerB getB()}",
        "InnerA", "This0.A","{ method This1.B getB()}"
    },{lineNumber(), new String[]{"{A:{method Void useB(B that)} B:{}}"}, // cascade: a parameter in A redirects B
        "{InnerA:{method Void useB(InnerB that)} InnerB:{} method Void useB(InnerB that)}",
        "InnerA", "This0.A","{ method Void useB(This1.B that)}"
    },{lineNumber(), new String[]{"{A:{method Void do() exception B} B:{}}"}, // cascade: an exception in A redirects B  // TODO:FAILS, since exception clause is ignored
        "{InnerA:{method Void do() exception InnerB} InnerB:{} method Void useB(InnerB that)}",
        "InnerA", "This0.A", ExceptionTest // "{ method Void useB(This1.B that)}"
    },{lineNumber(), new String[]{      // serial cascade: return ~> parameter ~> exception // TODO:FAILS, since exception clause is ignored
                    "{D:{}}",
                    "{C:{ method Void do() exception This2.D}}",
                    "{B:{ method Void useC(This2.C that)}}",
                    "{A:{method This2.B getB()}}"},
        "{InnerA:{method InnerB getB()} InnerB:{method Void useC(InnerC that)} "
        + "InnerC:{method Void do() exception InnerD} InnerD:{} method InnerD freeIdent(InnerD that)}",
        "InnerA", "This0.A",ExceptionTest //{ method This4.D freeIdent(This4.D that)}
    },{lineNumber(), new String[]{      // parallel cascade: return, parameter & exception address the same class  // TODO:FAILS, since exception clause is ignored
                    "{B:{method B ident(B that)}}",
                    "{A:{method This2.B getB() method Void useB(This2.B that) method Void do() exception This2.B}}"},
        "{InnerA:{method InnerX getB()"
        + "       method Void useB(InnerY that)"
        + "       method Void do() exception InnerZ"
        + "} "
        + "InnerX:{method InnerX ident(InnerX that)} "
        + "InnerY:{} "
        + "InnerZ:{method InnerZ ident(InnerZ that)} "
        + "method Void multiUse(InnerX x, InnerY y, InnerZ z) }",
        "InnerA", "This0.A", ExceptionTest // { method Void multiUse(This2.B x, This2.B y, This2.B z)}
    },{lineNumber(), new String[]{      // redirection of a method containing a library literal
    "{A:{ }}" },
    "{InnerA:{ } M:{class method Library defA_maker() {class method InnerA beA_maker() InnerA()}}}",
    "InnerA", "This0.A",
    "{M:{class method Library defA_maker() {class method This3.A beA_maker() This3.A.#apply()}}}"
    },{lineNumber(), new String[]{      // redirecting a nested library, into a differently nested target
                                        // This0 vs explicit class
                    "{X:{Y:{A:{class method This()  class method A fun()}}}}" },
        "{InnerZ:{InnerA:{  class method This0 fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "}",
        "InnerZ.InnerA", "This0.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "}"
    },{lineNumber(), new String[]{      // same, but swapping the This0 on fun()
                    "{X:{Y:{A:{class method This()  class method This0 fun()}}}}" },
        "{InnerZ:{InnerA:{  class method InnerA fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"  //  So this call to get a library value is imaginary, as shown below
        + "}",
        "InnerZ.InnerA", "This0.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}"
    },{lineNumber(), new String[]{      // same, with two explicit classes on fun()
                    "{X:{Y:{A:{class method This()  class method A fun()}}}}" },
        "{InnerZ:{InnerA:{  class method InnerA fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"
        + "}",
        "InnerZ.InnerA", "This0.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}"
    },{lineNumber(), new String[]{      // same, with two outers on fun()
                    "{X:{Y:{A:{class method This()  class method This0 fun()}}}}" },
        "{InnerZ:{InnerA:{  class method This0 fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"
        + "}",
        "InnerZ.InnerA", "This0.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}"
    },{lineNumber(), new String[]{      // writing methods and class methods that presume the surrounding program
                    "{X:{Y:{A:{class method This()  class method This1 fun(This2 that)}}}}" },
        "{InnerZ:{InnerA:{  class method This3.X.Y fun(This3.X that)}}"
        + "InnerB:{class method Library makeLib() {class method InnerZ.InnerA fred(This3.X.Y that)}}"
        + "}",
        "InnerZ.InnerA", "This0.X.Y.A",
        "{InnerZ:{}"
        + "InnerB:{class method Library makeLib() {class method This3.X.Y.A fred(This3.X.Y that)}}"
        + "}"
    },{lineNumber(), new String[]{   // Cascade coherently from a class to its surrounding class.
                                     // Requires matching subtrees of names, except at the root.
                    "{X:{Y:{FluffyA:{ class method This1 fun()}"
                    + "}}}" },
        "{InnerZ:{FluffyA:{ class method This1 fun()}}"
        + "B:{method InnerZ moreFun() "
        + "   method InnerZ.FluffyA mostFun() InnerZ.FluffyA.fun()}"
        + "}",
        "InnerZ.FluffyA", "This0.X.Y.FluffyA",
        "{B:{method This2.X.Y moreFun() "
        + "method This2.X.Y.FluffyA mostFun() This2.X.Y.FluffyA.fun()}}"
    },{lineNumber(), new String[]{  // Redirect a FreeTemplate to an Interface
        "{A:{interface class method Void fun(Void that)}}"
        },
        "{InnerA:{class method Void fun(Void that)}"
        + "TestB:{method InnerA moreFun()}"
        + "}",
        "InnerA", "This0.A",
        RedirectError.InvalidMapping.class

    // NOTE: This redirect has 4 possible solutions! (the most we have with any finite solutions)
    },{lineNumber(), new String[]{  // Redirect a FreeTemplate with two interfaces to an OpenClass with one.
                                    // Under the just-one-target-is-unambiguous rule,
                                    // which is a logical consequence of the intersecting-to-one-target-is-unambiguous rule,
                                    // this should combine the two interfaces
        "{I:{interface}\n"
        + "A:{ implements I method Void fun(Void that) void}}"
        },
        "{InnerI1:{interface} InnerI2:{interface}"
        + "InnerA:{ implements InnerI1 InnerI2\n"
        + "method Void fun(Void that)\n"
        + "}"
        + "TestB:{method InnerI1 fun() method InnerI2 moreFun()}"
        + "}",
        "InnerA", "This0.A",
        "{TestB:{method This2.I fun() method This2.I moreFun()}}"

        // TODO@James: (NOW) when the test above passes, add some methods, and set up implementing classes for the inner interfaces that don//t each have enough methods for the outer interface

    },{lineNumber(), new String[]{   // Cascade redirect an interface, via a redirect-my-pile-of-stuff class
            "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{ implements I1 I2 method fun() void method moreFun() void}"
                    + "%Redirect:{D_I1:{ implements I1} D_I2:{ implements I2} method A d_A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{ implements This2.I1 InnerI2} "  // redirected class can//t have implementation, so it can//t mention methods
        + "%Redirect:{D_I2:{ implements InnerI2} method InnerA d_A()}"
        + "TestB:{ implements InnerI2 method moreFun() void}\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        NestedClassTest //{TestB:{ implements This2.I2 method moreFun() void}}
    },{lineNumber(), new String[]{   // Same test as above, with more interesting method selectors and trivial order changes
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{ implements I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{D_I1:{ implements I1} D_I2:{ implements I2} method A d_A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{ implements InnerI2 This2.I1} \n"  // again, no implementation
        + "%Redirect:{D_I2:{ implements InnerI2} method InnerA d_A()}"
        + "TestB:{ implements InnerI2 method moreFun(that, other) void}\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        NestedClassTest //{TestB:{ implements This2.I2 method moreFun(that, other) void}}
    },{lineNumber(), new String[]{   // Redirect, via a pile, when the underlying types are used in aliases
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{ implements I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{ implements InnerI2 This2.I1} \n"  // again, no implementation
        + "%Redirect:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{ implements InnerI2 method moreFun(that, other) void \n"
        + "       class method Library () {} }\n"
        + "TestC:{method InnerI2 notSoFun() {} }\n"
        + "TestD:{method Void mostFun() {} }\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun(that, other) void\n"
        + "      class method Library () {}}\n"
        + "TestC:{method This2.I2 notSoFun() {}}\n"
        + "TestD:{method Void mostFun() {} }\n"
        + "}"
    },{lineNumber(), new String[]{   // Redirect, via a pile, using the things that will disappear as aliases
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{ implements I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{ implements InnerI2 This2.I1} \n"  // again, no implementation
        + "%Redirect:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{ implements InnerI2 method moreFun(that, other) void \n"
        + "       class method Library () {} }\n"
        + "TestC:{method Void notSoFun() {} }\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun(that, other) void\n"
        + "      class method Library () {}}\n"
        + "TestC:{method Void notSoFun() {}}\n"
        + "}"
    },{lineNumber(), new String[]{   // Redirect, via aliases,
                                     // trying to exploit a rumour that
                                     // identically shaped aliases can be redirected onto one-another
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method This1 fun(Void that)}\n"
                    + "       Aliases:{method This1 notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method This1 fun(Void that)} \n"
        + "   Aliases:{method This1 notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z.FluffyA fun()}"
        + "}",
        "Z.Aliases", "This0.X.Y.Aliases",
        NestedClassTest //{TestA:{method This2.X.Y.FluffyA fun()}}

        // TODO@James: (NOW) when the test above passes, redirect via a pile, where the types in the internal pile are aliases to a mix of internal, internal->external and external

        // TODO@James: (NOW) play with aliases to parameters vs aliases to return values

    },{lineNumber(), new String[]{   // Try redirecting a class that implements two internal interfaces,
                                     // via a pile that explicitly directs both
                    "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{ implements I1 I2 }\n"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}\n"
                    + "}"},
        "{InnerI1:{interface method Void fun()}\n"
        + "InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{ implements InnerI1 InnerI2}\n"  // redirected class can//t have implementation, so it can//t mention methods
        + "%Redirect:{method InnerI1 _I1() method InnerI2 _I2() method InnerA _A()}\n"
        + "TestA:{method InnerA aFun()}"
        + "TestB:{ implements InnerI1 InnerI2  method fun() void method moreFun() void}\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        "{"
        + "TestA:{method This2.A aFun()}"
        + "TestB:{ implements This2.I1 This2.I2 method fun() void method moreFun() void}"
        + "}"

    },{lineNumber(), new String[]{   // Redirect three classes, via a pile, so that the
                                     // intersection-of-ambiguity rule makes the
                                     // implemented interfaces and method errors unambiguous
        // TODO:FAILS due to insanity with exceptions!
                    "{Iab:{interface} Ibc:{interface} Ica:{interface}\n"
                    + "Eab:{} Ebc:{} Eca:{}\n"
                    + "A:{ implements Ica Iab method Void fun() exception Eca Eab}\n"
                    + "B:{ implements Iab Ibc method Void fun() exception Eab Ebc}\n"
                    + "C:{ implements Ibc Ica method Void fun() exception Ebc Eca}\n"
                    + "%Redirect:{method A _A() method B _B() method C _C() }\n"
                    + "}"},
        "{InnerIab:{interface} InnerIbc:{interface} InnerIca:{interface}\n"
        + "InnerEab:{} InnerEbc:{} InnerEca:{}\n"
        // same order, then interfaces swapped, then errors swapped, just in case it matters
        + "InnerA:{ implements InnerIca InnerIab method Void fun() exception InnerEca InnerEab}\n"
        + "InnerB:{ implements InnerIbc InnerIab method Void fun() exception InnerEab InnerEbc}\n"
        + "InnerC:{ implements InnerIbc InnerIca method Void fun() exception InnerEca InnerEbc}\n"
        + "%Redirect:{method InnerA _A() method InnerB _B() method InnerC _C()}\n"
        + "TestX:{method InnerIab abFun() exception InnerEab\n"
        + "       method InnerIbc bcFun() exception InnerEbc\n"
        + "       method InnerIca caFun() exception InnerEca}\n"
        + "}",
        "%Redirect", "This0.%Redirect",
        ExceptionTest
        /*{TestX:{method This2.Iab abFun() exception This2.Eab
          method This2.Ibc bcFun() exception This2.Ebc
          method This2.Ica caFun() exception This2.Eca}}*/

        // TODO@James do something with piles containing alias types that refer to the library return values of methods
        // but this might not be possible in a unit-test without metaprogramming capability

        // TODO@James (NOW; might be a dup) play with throwing interfaces that implement classes

    // the errors have variable portions.
	// try to explore the cardinality space of the variable portions
	//   for each error.
    // the cardinality, or option space, of each parameter is listed in parentheses.

    // SourceUnfit: SrcPath(1), DestExternalPath(1), PrivatePath(t/f), SrcKind(enum(5)), DestKind(enum(5)),
    //   UnexpectedMethods(0..), UnexpectedImplementedInterfaces(0..)
    },{lineNumber(), new String[]{"{A:{ }}"},  // from module with an unexpected function
        "{InnerA:{class method Void fun()} }","InnerA", "This0.A",
       RedirectError.InvalidMapping.class
    },{lineNumber(), new String[]{"{A:{ }}"},  // same test, but with a method argument, using the new mechanism
        "{InnerA:{class method Void fun(Void that)} }","InnerA", "This0.A",
        RedirectError.InvalidMapping.class
    },{lineNumber(), new String[]{  // FreeTemplate -> Interface, with some matching methods
        "{A:{interface class method Void fun(Void that)  method Void mostFun(Void that, Library other) }}"
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "InnerA", "This0.A",
        RedirectError.InvalidMapping.class
    },{lineNumber(), new String[]{  // with a mismatch on parameter names in the method selector
                                    // Also Template (by return value) -> Interface, which is infeasible
        "{A:{interface class method Void fun(Void that) class method Void moreFun()"
        + "method Void mostFun(Void that, Library mineAllMine) method Void notSoFun() } }",
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } "
        + "D:{method This1.InnerA makeA() InnerA.fun(void)} "
        + "}",
        "InnerA", "This0.A",
        RedirectError.InvalidMapping.class
    },{lineNumber(), new String[]{  // Template (by parameter value) -> OpenClass extra subclass
        "{A:{ method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ C:{} } "
        + "D:{class method Void useType(class Any that) void"
        + "   class method Void useA() D.useType(InnerA) } "
        + "}",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{  // Template (by exception) -> OpenClass extra subclass
        "{A:{ class method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ class method Void ignoreMe() C:{} } "
        + "D:{class method Void doWithoutA() exception Void  exception InnerA.ignoreMe() } "
        + "}",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{   // Redirect a class (InnerA) which implements interface methods
                                     // ie OpenClass->OpenClass
                                     // NB: for classes of incompatible kinds, unexpected members and interfaces are not shown.
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{ implements I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "D_Target:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{ implements InnerI2 This2.I1 method fun(that) void} \n"
        + "D_Source:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{ implements InnerI2 method moreFun(that, other) void \n"
        + "       class method Library () {} }\n"
        + "TestC:{method InnerI2 notSoFun() {} }\n"
        + "TestD:{method Void mostFun() {} }\n"
        + "}",
        "D_Source", "This0.D_Target",
        RedirectError.ClassUnfit.class // InnerA.fun(that) is implemented!
    },{lineNumber(), new String[]{  // OpenClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method //@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void C:{} }}",
        "InnerA", "This0.A",
        RedirectError.ClassUnfit.class  // InnerA.ignoreMe() is implemented!
    },{lineNumber(), new String[]{  // ClosedClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method //@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void "+
        "     method //@private \n Void ignoreMeMost() " +
        "     C:{} " +
        " }}",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{  // Interface with extra method
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "InnerA", "This0.A",
        RedirectError.InvalidMapping.class
    },{lineNumber(), new String[]{  // Interface with inner class
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{} } }",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{  // Interface with unexpected inner interface
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{interface} } }",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{  // Implementing the interface does not change the error
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{ implements InnerA.C} "
        + "}",
        "InnerA", "This0.A",
        NestedClassTest
     },{lineNumber(), new String[]{  // Expected interface, with unexpected method, implemented in implementing class
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{ implements InnerA.C"
        + "         method mostFun() void"    // With implementation and without respecified types
        + "       } "
        + "}",
        "InnerA", "This0.A",
        NestedClassTest
    },{lineNumber(), new String[]{ // Redirect free templates with methods into primitive types
        "{A:{ method Void fun(Library that, Any other)}}"},
        "{InnerVoid:{class method This #apply() } InnerLib:{ class method This #apply() } InnerAny:{ class method This #apply() }"
        + "InnerA:{method InnerVoid fun(InnerLib that, InnerAny other)}"
        + "method InnerAny moreFun(InnerVoid that, InnerLib other)"
        + "}","InnerA", "This0.A",
        RedirectError.DeductionFailure.class // Cannot redirect InnerLib/InnerVoid to Library/Any
    },{lineNumber(), new String[]{  // One unimplemented interface; no unexpected members
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"},
        "{BlockingInterface1:{interface} "
        + "InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "  C:{interface implements BlockingInterface1 } } \n"
        + "C_impl:{ implements InnerA.C"
        + "       }"
        + "method Void result(BlockingInterface1 bi, InnerA a, InnerA.C ic)"
        + "}",
        "InnerA", "This0.A",
        NestedClassTest //{method Void result(This1.A.C bi, This1.A a, This1.A.C ic) C_impl: {implements This2.A.C}}
    },{lineNumber(), new String[]{  // Matching nested interfaces, the inner of which implements two internal and one external blocking interfaces
                                    // NB: in the test harness, must specify outer numbers for outers.
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{BlockingInterface1:{interface} \n"
        + "BlockingInterface2:{interface} \n"
        + "InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface  implements BlockingInterface1 This2.BlockingInterface2 This3.A.C"
        + "  } } \n"
        + "C_impl:{ implements InnerA.C"
        + "         method Void mostFun()"    //
        + "       } "
        + "}",
        "InnerA", "This0.A",
        NestedClassTest // {C_impl:{implements This2.A.C method Void mostFun()}}

    },{lineNumber(), new String[]{  // When a cascade redirect renames another interface, the reported unexpected interface is the name before the rename.
        "{  A:{interface class method C fun(Void that)  method Void moreFun(Void that, Library other)} \n"
        + " C:{interface}"
        + " }"
        },
        "{ InnerA:{interface class method InnerC fun(Void that)  method Void moreFun(Void that, Library other)} \n"
        + "InnerC:{interface  implements InnerA"
        + "       }  \n"
        + "C_impl:{ implements InnerC"
        + "       } "
        + "}"
        + "",
        "InnerA", "This0.A",
        RedirectError.DeductionFailure.class // This1.C is not a subtype of This1.A


    // IncoherentRedirectMapping: Src(1..), Dest(1..), IncoherentSrc(1..), IncoherentDest(0, 2..)
    // TODO@James: enumerate the parameters and explore them thoroughly
    // TODO@James: explore the relationship between intersection unambiguity and forced split
    },{lineNumber(), new String[]{      // Incoherent redirect, forcing InnerAB to be split into both A and B
                    "{A:{} B:{}"
                    + "C:{ method A fun() method B moreFun()}"
                    + "}" },
        "{InnerAB:{} "
        + "InnerC:{method InnerAB fun() method InnerAB moreFun() } "
        + "}",
        "InnerC", "This0.C",
        "{}",
    },{lineNumber(), new String[]{   // Incoherent redirect: Matching functions (FluffyA.fun()) disagree about the position of their return value
                                     // NB: There is no reliable theory to filter out only nested redirects, so all redirects up to the failure are include in the error.
                    "{X:{Y:{FluffyA:{ class method This2 fun()}" // Target of original redirect
                    + "    }"
                    + "FluffyA:{class method This1 fun()}"  // The phantom required for the redirect to avoid SourceUnfit.
                    + "}}" },
        "{InnerZ:{FluffyA:{ class method This1 fun()}}" +
            "method Void result(InnerZ iz, InnerZ.FluffyA fa)" +
            "}",
        "InnerZ.FluffyA", "This0.X.Y.FluffyA",
        NestedClassTest("{method Void result(This1.X iz, This1.X.Y.FluffyA fa)}") //{method Void result(This1.X iz, This1.X.Y.FluffyA fa)}
    },{lineNumber(), new String[]{   // Try cascading two interfaces on one class, which should fail because
                                     // disambiguating it turned out to be prohibitive.
                    "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{ implements I1 I2 }"
                    + "}"},
        "{InnerI1:{interface method Void fun()}\n"
        + "InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{ implements InnerI1 InnerI2}"
        + "TestB:{ implements InnerI1 InnerI2  method fun() void method moreFun() void}\n"
        + "}",
        "InnerA", "This0.A",
        "{TestB: {implements This2.I1, This2.I2 " + 
            "refine method Void fun() void " + 
            "refine method Void moreFun() void}}"

    // TODO@James: when the error for the test above has settled, do a pile redirect of two classes,
    // where one uses two interfaces and the other uses only one of them, to confirm that the disambiguation
    // of one internal interface on a class does not disambiguate the other

    // next error with variable portions.
    // ClassClash can//t happen on a redirect

    // MethodClash: Path(1), Left(1), Right(1), LeftKind(enum(4)), RightKind(enum(4)),
    // DifferentParameters(0..), DifferentReturnType(t/f), DifferentThisMdf(t/f), IncompatibleException(t/f)

    },{lineNumber(), new String[]{   // Redirect, using the matching-alias rule, one primitive class onto another
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method Library fun(Void that)}\n"
                    + "       Aliases:{method Library notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Void fun(Void that)} \n"
        + "   Aliases:{method Void notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z.FluffyA fun()}"
        + "}",
        "Z.Aliases", "This0.X.Y.Aliases",
        RedirectError.InvalidMapping.class // Z.notSoFun() and X.Y.Aliases.notSoFun() have incompatable types
    },{lineNumber(), new String[]{   // Redirect to, but not from, incompatible which is a path
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method This1 fun(Void that)}\n"
                    + "       Aliases:{method This1 notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Void fun(Void that)} \n"
        + "   Aliases:{method Void notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z.FluffyA fun()}"
        + "}",
        "Z.Aliases", "This0.X.Y.Aliases",
        RedirectError.InvalidMapping.class // Z.notSoFun() and X.Y.Aliases.notSoFun() have incompatable types
// TODO@James: play properly with redirects and primitive types

/* TODO@James : try this test, when I get to method clashes
    },{lineNumber(), new String[]{ // mismatches in class vs instance method
        "{A:{class method Void fun(Void that) method Void moreFun(Void that)"
        + "class method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "InnerA", "This0.A",
        ec
          .set("UnexpectedMethods", "[moreFun(that), mostFun()]").str(), true
          */

          /* TODO@James: with this test, I get MemberUnavailable, which I don//t understand yet
    },{lineNumber(), new String[]{  // Matched inner interface shows as non-free
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{ implements InnerA.C"
        + "         method Void mostFun()"    // Uncommenting this line changes from SourceUnfit to MemberUnavailable
        + "       } "
        + "}",
        "InnerA", "This0.A",
        ec
          .set("SrcPath", "This0.InnerA.C", "DestExternalPath", "//@This1.A.C",
               "SrcKind", "Interface",
               "UnexpectedMembers", "[mostFun()]")
          .str(), true
           */

    // MemberUnavailable: Path(1), Selector(0..1), InvalidKind(enum 4)
    // In practice, Selector(0), InvalidKind(enum 2) in this context,
    // because all of the other cases are reported as SourceUnfit
    },{lineNumber(),   // Redirect a private class
        new String[]{"{A:{ }}"},
        "{InnerA_$_1:\n {class method Void fun(Void that)} }",
        "InnerA_$_1", "This0.A",
        RedirectError.PathUnfit.class // InnerA_$_1 is private
    },{lineNumber(),   // Redirect a nonexistent class
        new String[]{"{A:{ }}"},
        "{InnerNotA:{} }",
        "InnerA", "This0.A",
        RedirectError.PathUnfit.class // InnerA does not exist
/* Privacy does not trigger MemberUnavailable
    },{lineNumber(),   // Redirect to a private class
        new String[]{"{A://@private\n { }}"},
        "{InnerA:{} }",
        "InnerA", "This0.A",
        ec.load("MemberUnavailable",
                "Path", "//This2.A",
                "Selector", "",
                "InvalidKind", "PrivatePath"
               ).str(), true
    },{lineNumber(),      // Redirect a class with method, for which the target has a private matching method
       new String[]{"{A:{class method //@private\n Void fun(Void that) }}"},
        "{InnerA:{class method Void fun(Void that)} "
        + "InnerB:{class method Void moreFun(Void that) InnerA.fun(that)}"
        + "}",
        "InnerA", "This0.A",
        ec.set("Selector", "fun(that)"
                )
          .str(), true
*/
    },{
      lineNumber(), new String[]{"{A:{method Void() exception This1.A Library }}"},
       "{InnerA:{method Void() exception This2.A Library }\n"+
       " B:{ method InnerA m(InnerA a) a}}","InnerA", "This0.A",
        "{B:{ method This2.A m(This2.A a) a}}"
    },{
      lineNumber(), new String[]{"{EI: {interface}"
          + "Input: {"
          + "method Any m1(Any x) "
          + "method EI m2(EI x)"
          + "method EI m3(EI x)}}"},
       "{"
           + "Map: {method I1 m1(I1 x)"
           + " method I2 m2(I2 x)"
           + " method I3 m3(I3 x)}"
           + "I1: {interface} I2: {interface} I3: {interface}"
       + "C:{implements I1, I2, I3}}",
       "Map", "This0.Input",
        "{C:{implements This2.EI}}"
/*
    C:{ EB:{}
        EA:{method This1.EB f() This2.C.EB g()}
      }
    {A:{method B f() method B g()} B:{} method B foo()}[A to C.EA]

 */

/*
 Test1
  EI1
  B{    Any }
 --
  {I1:{interface}
  A{ I1 Any }       // I1 -> {Any}
  }[A into B]
  output: either error or  A->B, I1->Any
  -----------
  Test2
  EI1
  B{EI1 Any}

  {I1:{interface}
  A{ I1 Any}
  }[A into B] // I1 -> {EI1, Any}
  output: either errorIncoherent or  A->B, I1->Any or  A->B,I1->EI1


 */

    }});
    return TestHelper.skipUntilLine(tests, startLine);
}

//},{"This2.D.C","This1.C",new String[]{"{A:{}}","{C:{}}","{D:##walkBy}"}

public static Program mkp(String...ss) { Program p=TestHelper.getProgram(ss); return p; }

@Test  public void test() throws RedirectError {
  TestHelper.configureForTest();
  Program p;
  if(_p instanceof String) { p = mkp((String)_p); }
  else if(_p instanceof String[]) {p=TestHelper.getProgram((String[])_p);}
  else {p=(Program)_p;}
  //
  ClassB cb1=getClassB(true,p,"cb1", _cb1);

  PathMap map = new PathMap();
  { var Csz = this._path1.split(",");
    var Pz = this._path2.split(",");
    assert Csz.length == Pz.length;
    for (int i = 0; i < Csz.length; i++) {
      var Cs = Csz[i].trim();
      var P = Pz[i].trim();
      map.add(Cs.equals("This") ? List.of() : Path.parse("This0." + Cs).getCBar(), Path.parse(P)); }}

  if (_expected == null) {
    System.err.println("SKIPPED");
  } else if (_expected instanceof Class) {
    var expectedType = (Class)_expected;
    if (!Redirect.earlyErrorDetection) {
      if (expectedType.equals(InvalidMapping.class)) { expectedType = DeductionFailure.class; }
      if (expectedType.equals(PathUnfit.class)) { expectedType = ClassUnfit.class; }}

    ClassB res = null;
    try { res = Redirect.redirect(p,cb1,map); }
    catch (RedirectError e) {
      System.err.println(e.getClass().getSimpleName() + ": " + e.getShortMessage());
      Assert.assertEquals(expectedType, e.getClass());//earlyErrorDetection
    }
    if (res != null) { TestHelper.assertEqualExp(new ExpCore.X(null, "ERROR"), res); }
  } else {
    ClassB expected=getClassB(true,p,"expected", _expected.toString());
    //ClassB res=new RedirectObj(cb1).redirect(p,path1,path2);
    ClassB res=Redirect.redirect(p,cb1,map);
    TestHelper.assertEqualExp(expected,res);
  }
  }
}
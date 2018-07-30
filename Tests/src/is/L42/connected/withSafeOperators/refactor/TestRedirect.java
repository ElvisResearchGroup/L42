package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.IncoherentMapping;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.refactor.RedirectObj;
import helpers.TestHelper;
import static helpers.TestHelper.lineNumber;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import platformSpecific.javaTranslation.Resources;
import ast.Ast.MethodSelector;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import programReduction.Program;

@RunWith(Parameterized.class)
public class TestRedirect {

  // TODO@James: consider making a git hook to block commits unless startLine=0 is enabled

  // Skip failing, but uninteresting tests here
//  static int startLine=580;
  static int startLine=0;

  @Parameter(0) public int _lineNumber;
  @Parameter(1) public Object _p;
  @Parameter(2) public String _cb1;
  @Parameter(3) public String _path1;
  @Parameter(4) public String _path2;
  @Parameter(5) public String _expected;
  @Parameter(6) public boolean isError;

  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    ErrorCarry ec = new ErrorCarry();

    List<Object[]> tests= Arrays.asList(new Object[][] {
    {lineNumber(), new String[]{"{A:{}}"},
      "{InnerA:{} B:{ method InnerA m(InnerA a) a}}","This0.InnerA","This1.A",
      "{B:{ method This2.A m(This2.A a) a}}",false
    },{lineNumber(), new String[]{"{A:{}}"},
        "{InnerA:{}  method InnerA m(InnerA a) a}","This0.InnerA","This1.A",
        "{ method This1.A m(This1.A a) a}",false
    },{lineNumber(), new String[]{ // Redirect free templates into primitive types
        "{A:{ method Void fun(Library that, Any other)}}"},
        "{InnerVoid:{} InnerLib:{} InnerAny:{}"
        + "InnerA:{method InnerVoid fun(InnerLib that, InnerAny other)}"
        + "method InnerAny moreFun(InnerVoid that, InnerLib other)"
        + "}","This0.InnerA","This1.A",
        "{method Any moreFun(Void that, Library other)}",false
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","This0.InnerA","This1.A1",
        "{ method This1.A1 m(This1.A1 a) a}",false
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","This0.InnerA","This2.A2",
        "{ method This2.A2 m(This2.A2 a) a}",false
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes
        "{InnerA:{}  method InnerA m(InnerA a) a}","This0.InnerA",
        "This1.A1.A1n","{ method This1.A1.A1n m(This1.A1.A1n a) a}",false
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes in further out scope
        "{InnerA:{}  method InnerA m(InnerA a) a}","This0.InnerA","This2.A2.A2n",
        "{ method This2.A2.A2n m(This2.A2.A2n a) a}",false
    },{lineNumber(), new String[]{"{A:{method B getB()} B:{}}"}, // cascade: a return value in A redirects B
        "{InnerA:{method InnerB getB()} InnerB:{} method InnerB getB()}",
        "This0.InnerA","This1.A","{ method This1.B getB()}",false
    },{lineNumber(), new String[]{"{A:{method Void useB(B that)} B:{}}"}, // cascade: a parameter in A redirects B
        "{InnerA:{method Void useB(InnerB that)} InnerB:{} method Void useB(InnerB that)}",
        "This0.InnerA","This1.A","{ method Void useB(This1.B that)}",false
    },{lineNumber(), new String[]{"{A:{method Void do() exception B} B:{}}"}, // cascade: an exception in A redirects B
        "{InnerA:{method Void do() exception InnerB} InnerB:{} method Void useB(InnerB that)}",
        "This0.InnerA","This1.A","{ method Void useB(This1.B that)}",false
    },{lineNumber(), new String[]{      // serial cascade: return ~> parameter ~> exception
                    "{D:{}}",
                    "{C:{ method Void do() exception This2.D}}",
                    "{B:{ method Void useC(This2.C that)}}",
                    "{A:{method This2.B getB()}}"},
        "{InnerA:{method InnerB getB()} InnerB:{method Void useC(InnerC that)} "
        + "InnerC:{method Void do() exception InnerD} InnerD:{} method InnerD freeIdent(InnerD that)}",
        "This0.InnerA","This1.A","{ method This4.D freeIdent(This4.D that)}",false
    },{lineNumber(), new String[]{      // parallel cascade: return, parameter & exception address the same class
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
        "This0.InnerA","This1.A","{ method Void multiUse(This2.B x, This2.B y, This2.B z)}",false
    },{lineNumber(), new String[]{      // redirection of a method containing a library literal
    "{A:{ }}" },
    "{InnerA:{ } M:{class method Library defA_maker() {class method InnerA beA_maker() InnerA()}}}",
    "This0.InnerA","This1.A",
    "{M:{class method Library defA_maker() {class method This3.A beA_maker() This3.A.#apply()}}}",false
    },{lineNumber(), new String[]{      // redirecting a nested library, into a differently nested target
                                        // This0 vs explicit class
                    "{X:{Y:{A:{class method This()  class method A fun()}}}}" },
        "{InnerZ:{InnerA:{  class method This0 fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "}",
        "This0.InnerZ.InnerA","This1.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "}",false
    },{lineNumber(), new String[]{      // same, but swapping the This0 on fun()
                    "{X:{Y:{A:{class method This()  class method This0 fun()}}}}" },
        "{InnerZ:{InnerA:{  class method InnerA fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"  //  So this call to get a library value is imaginary, as shown below
        + "}",
        "This0.InnerZ.InnerA","This1.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // same, with two explicit classes on fun()
                    "{X:{Y:{A:{class method This()  class method A fun()}}}}" },
        "{InnerZ:{InnerA:{  class method InnerA fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"
        + "}",
        "This0.InnerZ.InnerA","This1.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // same, with two outers on fun()
                    "{X:{Y:{A:{class method This()  class method This0 fun()}}}}" },
        "{InnerZ:{InnerA:{  class method This0 fun()}}"
        + " M:{class method Library defA_maker() {class method InnerZ.InnerA beA_maker() InnerZ.InnerA()}}"
        + "B:{C: {} }"
        + "}",
        "This0.InnerZ.InnerA","This1.X.Y.A",
        "{InnerZ:{}"
        + "M:{class method Library defA_maker() {class method This3.X.Y.A beA_maker() This3.X.Y.A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // writing methods and class methods that presume the surrounding program
                    "{X:{Y:{A:{class method This()  class method This1 fun(This2 that)}}}}" },
        "{InnerZ:{InnerA:{  class method This3.X.Y fun(This3.X that)}}"
        + "InnerB:{class method Library makeLib() {class method InnerZ.InnerA fred(This3.X.Y that)}}"
        + "}",
        "This0.InnerZ.InnerA","This1.X.Y.A",
        "{InnerZ:{}"
        + "InnerB:{class method Library makeLib() {class method This3.X.Y.A fred(This3.X.Y that)}}"
        + "}",false
    },{lineNumber(), new String[]{   // Cascade coherently from a class to its surrounding class.
                                     // Requires matching subtrees of names, except at the root.
                    "{X:{Y:{FluffyA:{ class method This1 fun()}"
                    + "}}}" },
        "{InnerZ:{FluffyA:{ class method This1 fun()}}"
        + "B:{method InnerZ moreFun() "
        + "   method InnerZ.FluffyA mostFun() InnerZ.FluffyA.fun()}"
        + "}",
        "This0.InnerZ.FluffyA","This1.X.Y.FluffyA",
        "{B:{method This2.X.Y moreFun() "
        + "method This2.X.Y.FluffyA mostFun() This2.X.Y.FluffyA.fun()}}",false
    },{lineNumber(), new String[]{  // Redirect a FreeTemplate to an Interface
        "{A:{interface class method Void fun(Void that)}}"
        },
        "{InnerA:{class method Void fun(Void that)}"
        + "TestB:{method InnerA moreFun()}"
        + "}",
        "This0.InnerA","This1.A",
        "{TestB:{method This2.A moreFun()}}", false
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
        "This0.InnerA","This1.A",
        "{TestB:{method This2.I fun() method This2.I moreFun()}}", false

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
        "This0.%Redirect","This1.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun() void}}",false
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
        "This0.%Redirect","This1.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun(that, other) void}}",false
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
        "This0.%Redirect","This1.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun(that, other) void\n"
        + "      class method Library () {}}\n"
        + "TestC:{method This2.I2 notSoFun() {}}\n"
        + "TestD:{method Void mostFun() {} }\n"
        + "}",false
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
        "This0.%Redirect","This1.%Redirect",
        "{TestB:{ implements This2.I2 method moreFun(that, other) void\n"
        + "      class method Library () {}}\n"
        + "TestC:{method Void notSoFun() {}}\n"
        + "}",false
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
        "This0.Z.Aliases","This1.X.Y.Aliases",
        "{TestA:{method This2.X.Y.FluffyA fun()}}",false

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
        "This0.%Redirect","This1.%Redirect",
        "{"
        + "TestA:{method This2.A aFun()}"
        + "TestB:{ implements This2.I1 This2.I2 method fun() void method moreFun() void}"
        + "}",false

    },{lineNumber(), new String[]{   // Redirect three classes, via a pile, so that the
                                     // intersection-of-ambiguity rule makes the
                                     // implemented interfaces and method errors unambiguous
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
        "This0.%Redirect","This1.%Redirect",
        "{"
        + "TestX:{method This2.Iab abFun() exception This2.Eab\n"
        + "       method This2.Ibc bcFun() exception This2.Ebc\n"
        + "       method This2.Ica caFun() exception This2.Eca}\n"
        + "}",false

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
        "{InnerA:{class method Void fun()} }","This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"
       +"Unexpected members in dest:[fun()]"
      ,true
    },{lineNumber(), new String[]{"{A:{ }}"},  // same test, but with a method argument, using the new mechanism
        "{InnerA:{class method Void fun(Void that)} }","This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[fun(that)]"
        , true
    },{lineNumber(), new String[]{  // FreeTemplate -> Interface, with some matching methods
        "{A:{interface class method Void fun(Void that)  method Void mostFun(Void that, Library other) }}"
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[moreFun(that), notSoFun()]"
        , true
    },{lineNumber(), new String[]{  // with a mismatch on parameter names in the method selector
                                    // Also Template (by return value) -> Interface, which is infeasible
        "{A:{interface class method Void fun(Void that) class method Void moreFun()"
        + "method Void mostFun(Void that, Library mineAllMine) method Void notSoFun() } }",
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } "
        + "D:{method This1.InnerA makeA() InnerA.fun(void)} "
        + "}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[moreFun(that), mostFun(that,other)]"
        , true
    },{lineNumber(), new String[]{  // Template (by parameter value) -> OpenClass extra subclass
        "{A:{ method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ C:{} } "
        + "D:{class method Void useType(class Any that) void"
        + "   class method Void useA() D.useType(InnerA) } "
        + "}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[C]"
        , true
    },{lineNumber(), new String[]{  // Template (by exception) -> OpenClass extra subclass
        "{A:{ class method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ class method Void ignoreMe() C:{} } "
        + "D:{class method Void doWithoutA() exception Void  exception InnerA.ignoreMe() } "
        + "}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[C]"
        , true
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
        "This0.D_Source","This1.D_Target",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "not redirectable"
        , true
    },{lineNumber(), new String[]{  // OpenClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method //@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void C:{} }}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "not redirectable"
        , true
    },{lineNumber(), new String[]{  // ClosedClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method //@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void "+
        "     method //@private \n Void ignoreMeMost() " +
        "     C:{} " +
        " }}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "not redirectable"
        , true
    },{lineNumber(), new String[]{  // Interface with extra method
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[mostFun(that,other), notSoFun()]"
        , true
    },{lineNumber(), new String[]{  // Interface with inner class
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{} } }",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[C]"
        , true
    },{lineNumber(), new String[]{  // Interface with unexpected inner interface
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{interface} } }",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[C]"
        , true
    },{lineNumber(), new String[]{  // Implementing the interface does not change the error
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{ implements InnerA.C} "
        + "}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA to This1.A:\n"+
        "Unexpected members in dest:[C]"
        , true
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
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA.C to This1.A.C:\n"+
        "Unexpected members in dest:[mostFun()]"
        , true
    },{lineNumber(), new String[]{ // Redirect free templates with methods into primitive types
        "{A:{ method Void fun(Library that, Any other)}}"},
        "{InnerVoid:{class method This #apply() } InnerLib:{ class method This #apply() } InnerAny:{ class method This #apply() }"
        + "InnerA:{method InnerVoid fun(InnerLib that, InnerAny other)}"
        + "method InnerAny moreFun(InnerVoid that, InnerLib other)"
        + "}","This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerVoid to Void:\n"+
        "Unexpected members in dest:[#apply()]"
       , true
    },{lineNumber(), new String[]{  // One unimplemented interface; no unexpected members
        "{A:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{BlockingInterface1:{interface} "
        + "InnerA:{interface class method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface  implements BlockingInterface1 } } \n"
        + "C_impl:{ implements InnerA.C"
        + "       } "
        + "}",
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA.C to This1.A.C:\n"+
        "Unexpected implemented interface in dest:[BlockingInterface1]"
        , true
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
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerA.C to This1.A.C:\n"+
        "Unexpected implemented interface in dest:[BlockingInterface1, BlockingInterface2, This1.A.C]"
         , true

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
        "This0.InnerA","This1.A",
        "ClassUnfit::Redirecting InnerC to This1.C:\n"+
        "Unexpected implemented interface in dest:[InnerA]"
        , true


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
        "This0.InnerC","This1.C",
        "IncoherentMapping::\n"+
        "ambiguities:[InnerC->[This1.C], InnerAB->[]]\n"+
        "incoherent destinations for InnerAB:[This1.A, This1.B]"
        , true
    },{lineNumber(), new String[]{   // Incoherent redirect: Matching functions (FluffyA.fun()) disagree about the position of their return value
                                     // NB: There is no reliable theory to filter out only nested redirects, so all redirects up to the failure are include in the error.
                    "{X:{Y:{FluffyA:{ class method This2 fun()}" // Target of original redirect
                    + "    }"
                    + "FluffyA:{class method This1 fun()}"  // The phantom required for the redirect to avoid SourceUnfit.
                    + "}}" },
        "{InnerZ:{FluffyA:{ class method This1 fun()}}"
        + "}",
        "This0.InnerZ.FluffyA","This1.X.Y.FluffyA",
        "IncoherentMapping::\n"+
        "verified:[InnerZ.FluffyA->This1.X.Y.FluffyA, InnerZ->This1.X]\n"+
        "ambiguities:[InnerZ->[This1.X], InnerZ.FluffyA->[This1.X.FluffyA]]\n"+
        "incoherent destinations for InnerZ.FluffyA:[This1.X.FluffyA, This1.X.Y.FluffyA]"
        , true
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
        "This0.InnerA","This1.A",
        "IncoherentMapping::\n"+
        "verified:[InnerA->This1.A]\n"+
        "ambiguities:[InnerI1->[This1.I1, This1.I2], InnerI2->[This1.I1, This1.I2]]"
        , true

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
        "This0.Z.Aliases","This1.X.Y.Aliases",
        "MethodClash::Issues: Return type"
        , true
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
        "This0.Z.Aliases","This1.X.Y.Aliases",
        "MethodClash::Issues: Return type"
        , true
// TODO@James: play properly with redirects and primitive types

/* TODO@James : try this test, when I get to method clashes
    },{lineNumber(), new String[]{ // mismatches in class vs instance method
        "{A:{class method Void fun(Void that) method Void moreFun(Void that)"
        + "class method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        },
        "{InnerA:{class method Void fun(Void that) class method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "This0.InnerA","This1.A",
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
        "This0.InnerA","This1.A",
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
        "This0.InnerA","This1.A",
        "PathUnfit::Non existant path"
        , true
    },{lineNumber(),   // Redirect a nonexistent class
        new String[]{"{A:{ }}"},
        "{InnerNotA:{} }",
        "This0.InnerA","This1.A",
        "PathUnfit::Non existant path"
        , true
/* Privacy does not trigger MemberUnavailable
    },{lineNumber(),   // Redirect to a private class
        new String[]{"{A://@private\n { }}"},
        "{InnerA:{} }",
        "This0.InnerA","This1.A",
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
        "This0.InnerA","This1.A",
        ec.set("Selector", "fun(that)"
                )
          .str(), true
*/


    },{
      lineNumber(), new String[]{"{A:{method Void() exception This1.A Library }}"},
       "{InnerA:{method Void() exception This2.A Library }\n"+
       " B:{ method InnerA m(InnerA a) a}}","This0.InnerA","This1.A",
        "{B:{ method This2.A m(This2.A a) a}}",false
    },{
      lineNumber(), new String[]{"{}"},
       "{I: {interface}, C:{implements I, Any}}",
       "This0.I","Any",
        "{C:{implements Any, Any}}",false
    },{
      lineNumber(), new String[]{"{"+
          "EA:{interface method Void ma()}" +
          "EB:{interface method Void mb()}" +
          "E:{implements This1.EA This1.EB}}"
      },"{" +
            "I:{implements IA IB}" +
            "IA:{interface method Void ma()}" +
            "IB:{interface method Void mb()}" +
            "method IA a() method IB b()" +
      "}",
      "This0.I","This1.E",
      "{method EA a() method EB b()}", false // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{"+
            "E: {interface method Void equals() method Void foo()} \n" +
            "EA: {implements This1.E}}"},
            "{\n"+
            "A: {implements This1.I, This2.E}\n"+
            "I: {interface implements This2.E refine method Void foo()}\n" +
            "method I i()\n" +
            "}",
            "This0.A", "This1.EA",
            "{method This1.E i()}", false // FAILS IncoherentMapping
    },{lineNumber(), new String[]{"{"+
    "E1: {interface} E2: {interface}\n" +
    "EA: {implements This1.E1 This1.E2}}"},
    "{\n"+
    "A: {implements This1.I, This2.E2}\n"+
    "I: {interface}\n" +
    "method I foo()\n" +
    "}",
    "This0.A", "This1.EA",
    "{method This1.E foo()}", false // Should fail with IncohrentMapping?
    },{lineNumber(), new String[]{"{"+
    "EI2:{interface method Void foo()}\n" +
    "EI1:{interface implements EI2 method Void bar()}\n" +
    "E:{implements EI1}\n"+
    "}"
    },
    "{\n"+
    "I:{interface method Void foo()}"+
    "A:{implements I}"+
    "method I m(I x)" +
    "}",
    "This0.A", "This1.E",
    "IncoherentMapping::\n" +
    "verified:[A->This1.E]\n" +
    "ambiguities:[I->[This1.EI1, This1.EI2]]", true // IncoherentMapping due to E normalising to {implements EI1, EI2}

    },{lineNumber(),
      new String[]{"{E:{class method Void foo()}}"},
      "{Z:{class method Void foo() exception Z}\n"+
       "A:{method Void foo() exception Z Z.foo()}}",
      "This0.Z", "This1.E",
      "{A:{method Void foo() exception This2.E This2.E.foo()}}", false
    },{lineNumber(),
        new String[]{"{E:{interface method Void foo()}}"},
        "{I:{interface} A:{implements I}}",
        "This0.I", "This1.E",
        "{A: {implements This2.E}}", true // SHOULD FAIL, as A dosn't implement This2.E.foo
    },{lineNumber(),
        new String[]{"{E:{interface method Void foo()}}"},
        "{I:{interface method Void foo() exception I}\n"+
         "A:{implements I refine method Void foo() exception I}}",
        "This0.I", "This1.E",
        "{A: {implements This2.E refine method Void foo() exception This2.E }}",
         true  // SHOULD FAIL, as A dosn't implement This2.E.foo
    },{lineNumber(),
      new String[]{"{E:{interface method Void foo() exception E}}"},
      "{I:{interface method Void foo()}\n"+
       "A:{implements I refine method Void foo()}"+
       "class method Void(I i) i.foo()}",
      "This0.I", "This1.E",
      "MethodClash::Issues:  Incompatible exceptions ", true
            // SHOULD FAIL
    },{lineNumber(),
        new String[]{"{" +
                "E1:{interface method Void foo()}\n" +
                "E2:{interface method Void foo()}\n" +
                "E: {method E1 e1() method E2 e2()}\n" +
        "}"},
        "{\n" +
            "I1:{interface} I2:{interface} \n"+
            "B: {method I1 e1() method I2 e2()}\n"+
            "A:{interface implements I1 I2}\n" +
        "}",
        "This0.B", "This1.E",
            "{A: {interface implements This2.E1, This2.E2}}", true
            // SHOULD FAIL
    },{lineNumber(),
      new String[]{"{Foo: {implements This1.EA}, EA: {interface B:{}}}"},
      "{A:{interface      B:{implements A}}}",
      "This0.A.B", "This1.Foo",
          "IncoherentMapping::\n" +
          "verified:[A.B->This1.Foo, A->This1.EA]\n" +
          "ambiguities:[A->[This1.EA], A.B->[This1.EA.B]]\n" +
          "incoherent destinations for A.B:[This1.EA.B, This1.Foo]", true
    },{lineNumber(),
      new String[]{"{EA:{interface B:{implements EA}}}"},
      "{A:{interface      B:{implements A}}}",
      "This0.A.B", "This1.EA.B",
          "{}", false
    },{lineNumber(),
      new String[]{"{EA:{interface A:{implements EA}}}"},
      "{interface A:{implements This1}}",
      "This0.A", "This1.EA.A",
          "PathUnfit::Private path", true
    },{lineNumber(),
      new String[]{"{C:{EB:{}, EA:{method This1.EB f() method This2.C.EB g()}}}"},
      "{A:{method B f() method B g()} B:{} method B foo()}",
      "This0.A", "This1.C.EA",
          "{method This1.C.EB foo()}", false
    },{lineNumber(),
      mkp("{C: {EA:{EB:{}, method This2.C.EA.EB g() method This0.EB f()}}}")
        .push(new ast.Ast.C("C", -1)),
      "{A:{method B g() method B f()} B:{} method B foo()}",
      "This0.A", "This1.EA",
          "{method This1.EA.EB foo()}", false
          // Should work!

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

public static Program mkp(String...ss) {
  Program p=TestHelper.getProgram(ss);
  return p;
  }

@Test  public void test() throws ClassUnfit, IncoherentMapping, MethodClash, PathUnfit {

  TestHelper.configureForTest();
  Program p;
  if(_p instanceof String[]) {p=TestHelper.getProgram((String[])_p);}
  else {p=(Program)_p;}
  //
  ClassB cb1=getClassB(true,p,"cb1", _cb1);
  Path path1=Path.parse(_path1);
  Path path2=Path.parse(_path2);
  if(!isError){
    ClassB expected=getClassB(true,p,"expected", _expected);
    ClassB res=new RedirectObj(cb1).redirect(p,path1.getCBar(),path2);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{new RedirectObj(cb1).redirect(p,path1.getCBar(),path2);fail("error expected");}
    catch(ClassUnfit err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(IncoherentMapping err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(MethodClash err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    catch(PathUnfit err){
      String txt=err.getClass().getSimpleName()+"::"+err.getMessage();
      assertEquals(_expected,txt);
      }
    }
  }
}
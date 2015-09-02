package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;
import static helpers.TestHelper.lineNumber;

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
import auxiliaryGrammar.Program;

public class TestRedirect{
@RunWith(Parameterized.class)

public static class TestRedirect1 {//add more test for error cases

  // TODO@James: consider making a git hook to block commits unless startLine=0 is enabled
  
  // Skip failing, but uninteresting tests here
//  static int startLine=580;
  static int startLine=0;

  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String[] _p;
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
      "{InnerA:{} B:{ method InnerA m(InnerA a) a}}","Outer0::InnerA","Outer1::A",
      "{B:{ method Outer2::A m(Outer2::A a) a}}",false
    },{lineNumber(), new String[]{"{A:{}}"},
        "{InnerA:{}  method InnerA m(InnerA a) a}","Outer0::InnerA","Outer1::A",
        "{ method Outer1::A m(Outer1::A a) a}",false
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","Outer0::InnerA","Outer1::A1",
        "{ method Outer1::A1 m(Outer1::A1 a) a}",false
    },{lineNumber(), new String[]{"{A2:{  }}","{A1:{  }}"}, // redirecting into one of multiple outer scopes
        "{InnerA:{}  method InnerA m(InnerA a) a}","Outer0::InnerA","Outer2::A2",
        "{ method Outer2::A2 m(Outer2::A2 a) a}",false
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes
        "{InnerA:{}  method InnerA m(InnerA a) a}","Outer0::InnerA",
        "Outer1::A1::A1n","{ method Outer1::A1::A1n m(Outer1::A1::A1n a) a}",false
    },{lineNumber(), new String[]{"{A2:{ A2n:{}  }}","{A1:{ A1n:{}  }}"}, // redirecting into nested classes in further out scope
        "{InnerA:{}  method InnerA m(InnerA a) a}","Outer0::InnerA","Outer2::A2::A2n",
        "{ method Outer2::A2::A2n m(Outer2::A2::A2n a) a}",false
    },{lineNumber(), new String[]{"{A:{method B getB()} B:{}}"}, // cascade: a return value in A redirects B
        "{InnerA:{method InnerB getB()} InnerB:{} method InnerB getB()}",
        "Outer0::InnerA","Outer1::A","{ method Outer1::B getB()}",false
    },{lineNumber(), new String[]{"{A:{method Void useB(B that)} B:{}}"}, // cascade: a parameter in A redirects B
        "{InnerA:{method Void useB(InnerB that)} InnerB:{} method Void useB(InnerB that)}",
        "Outer0::InnerA","Outer1::A","{ method Void useB(Outer1::B that)}",false
    },{lineNumber(), new String[]{"{A:{method Void do() exception B} B:{}}"}, // cascade: an exception in A redirects B
        "{InnerA:{method Void do() exception InnerB} InnerB:{} method Void useB(InnerB that)}",
        "Outer0::InnerA","Outer1::A","{ method Void useB(Outer1::B that)}",false
    },{lineNumber(), new String[]{      // serial cascade: return ~> parameter ~> exception
                    "{D:{}}",
                    "{C:{ method Void do() exception Outer2::D}}",
                    "{B:{ method Void useC(Outer2::C that)}}",
                    "{A:{method Outer2::B getB()}}"},
        "{InnerA:{method InnerB getB()} InnerB:{method Void useC(InnerC that)} "
        + "InnerC:{method Void do() exception InnerD} InnerD:{} method InnerD freeIdent(InnerD that)}",
        "Outer0::InnerA","Outer1::A","{ method Outer4::D freeIdent(Outer4::D that)}",false
    },{lineNumber(), new String[]{      // parallel cascade: return, parameter & exception address the same class
                    "{B:{method B ident(B that)}}",
                    "{A:{method Outer2::B getB() method Void useB(Outer2::B that) method Void do() exception Outer2::B}}"},
        "{InnerA:{method InnerX getB()"
        + "       method Void useB(InnerY that)"
        + "       method Void do() exception InnerZ"
        + "} "
        + "InnerX:{method InnerX ident(InnerX that)} "
        + "InnerY:{} "
        + "InnerZ:{method InnerZ ident(InnerZ that)} "
        + "method Void multiUse(InnerX x, InnerY y, InnerZ z) }",
        "Outer0::InnerA","Outer1::A","{ method Void multiUse(Outer2::B x, Outer2::B y, Outer2::B z)}",false
    },{lineNumber(), new String[]{      // redirection of a method containing a library literal
    "{A:{()}}" },
    "{InnerA:{()} M:{type method Library defA_maker() {type method InnerA beA_maker() InnerA()}}}",
    "Outer0::InnerA","Outer1::A",
    "{M:{type method Library defA_maker() {type method Outer3::A beA_maker() Outer3::A.#apply()}}}",false
    },{lineNumber(), new String[]{      // redirecting a nested library, into a differently nested target
                                        // Outer0 vs explicit type
                    "{X:{Y:{A:{()  type method A fun()}}}}" },
        "{InnerZ:{InnerA:{()  type method Outer0 fun()}}"
        + " M:{type method Library defA_maker() {type method InnerZ::InnerA beA_maker() InnerZ::InnerA()}}"
        + "}",
        "Outer0::InnerZ::InnerA","Outer1::X::Y::A",
        "{InnerZ:{}"
        + "M:{type method Library defA_maker() {type method Outer3::X::Y::A beA_maker() Outer3::X::Y::A.#apply()}} "
        + "}",false
    },{lineNumber(), new String[]{      // same, but swapping the Outer0 on fun()
                    "{X:{Y:{A:{()  type method Outer0 fun()}}}}" },
        "{InnerZ:{InnerA:{()  type method InnerA fun()}}"
        + " M:{type method Library defA_maker() {type method InnerZ::InnerA beA_maker() InnerZ::InnerA()}}"
        + "B:{C: {} }"  //  So this call to get a library value is imaginary, as shown below
        + "}",
        "Outer0::InnerZ::InnerA","Outer1::X::Y::A",
        "{InnerZ:{}"
        + "M:{type method Library defA_maker() {type method Outer3::X::Y::A beA_maker() Outer3::X::Y::A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // same, with two explicit classes on fun()
                    "{X:{Y:{A:{()  type method A fun()}}}}" },
        "{InnerZ:{InnerA:{()  type method InnerA fun()}}"
        + " M:{type method Library defA_maker() {type method InnerZ::InnerA beA_maker() InnerZ::InnerA()}}"
        + "B:{C: {} }"  
        + "}",
        "Outer0::InnerZ::InnerA","Outer1::X::Y::A",
        "{InnerZ:{}"
        + "M:{type method Library defA_maker() {type method Outer3::X::Y::A beA_maker() Outer3::X::Y::A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // same, with two outers on fun()
                    "{X:{Y:{A:{()  type method Outer0 fun()}}}}" },
        "{InnerZ:{InnerA:{()  type method Outer0 fun()}}"
        + " M:{type method Library defA_maker() {type method InnerZ::InnerA beA_maker() InnerZ::InnerA()}}"
        + "B:{C: {} }" 
        + "}",
        "Outer0::InnerZ::InnerA","Outer1::X::Y::A",
        "{InnerZ:{}"
        + "M:{type method Library defA_maker() {type method Outer3::X::Y::A beA_maker() Outer3::X::Y::A.#apply()}} "
        + "B:{C:{}}}",false
    },{lineNumber(), new String[]{      // writing methods and type methods that presume the surrounding program
                    "{X:{Y:{A:{()  type method Outer1 fun(Outer2 that)}}}}" },
        "{InnerZ:{InnerA:{()  type method Outer3::X::Y fun(Outer3::X that)}}"
        + "InnerB:{type method Library makeLib() {type method InnerZ::InnerA fred(Outer3::X::Y that)}}"
        + "}",
        "Outer0::InnerZ::InnerA","Outer1::X::Y::A",
        "{InnerZ:{}"
        + "InnerB:{type method Library makeLib() {type method Outer3::X::Y::A fred(Outer3::X::Y that)}}"
        + "}",false
    },{lineNumber(), new String[]{   // Cascade coherently from a class to its surrounding class.
                                     // Requires matching subtrees of names, except at the root.
                    "{X:{Y:{FluffyA:{ type method Outer1 fun()}"
                    + "}}}" },
        "{InnerZ:{FluffyA:{ type method Outer1 fun()}}"
        + "B:{method InnerZ moreFun() "
        + "   method InnerZ::FluffyA mostFun() InnerZ::FluffyA.fun()}"
        + "}",
        "Outer0::InnerZ::FluffyA","Outer1::X::Y::FluffyA",
        "{B:{method Outer2::X::Y moreFun() "
        + "method Outer2::X::Y::FluffyA mostFun() Outer2::X::Y::FluffyA.fun()}}",false
    },{lineNumber(), new String[]{  // Redirect a FreeTemplate to an Interface
        "{A:{interface type method Void fun(Void that)}}"
        },
        "{InnerA:{type method Void fun(Void that)}"
        + "TestB:{method InnerA moreFun()}"
        + "}",
        "Outer0::InnerA","Outer1::A",
        "{TestB:{method Outer2::A moreFun()}}", false
    },{lineNumber(), new String[]{  // Redirect a FreeTemplate with two interfaces to an OpenClass with one.
                                    // Under the just-one-target-is-unambiguous rule,
                                    // which is a logical consequence of the intersecting-to-one-target-is-unambiguous rule,
                                    // this should combine the two interfaces
        "{I:{interface}\n"
        + "A:{<:I method Void fun(Void that) void}}"
        },
        "{InnerI1:{interface} InnerI2:{interface}"
        + "InnerA:{<:InnerI1 InnerI2\n"
        + "method Void fun(Void that)\n"
        + "}"
        + "TestB:{method InnerI1 fun() method InnerI2 moreFun()}"
        + "}",
        "Outer0::InnerA","Outer1::A",
        "{TestB:{method Outer2::I fun() method Outer2::I moreFun()}}", false
        
        // TODO@James: (NOW) when the test above passes, add some methods, and set up implementing classes for the inner interfaces that don't each have enough methods for the outer interface

    },{lineNumber(), new String[]{   // Cascade redirect an interface, via a redirect-my-pile-of-stuff class
                    "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{<:I1 I2 method fun() void method moreFun() void}"
                    + "%Redirect:{D_I1:{<:I1} D_I2:{<:I2} method A d_A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{<:Outer2::I1 InnerI2} "  // redirected class can't have implementation, so it can't mention methods
        + "%Redirect:{D_I2:{<:InnerI2} method InnerA d_A()}"
        + "TestB:{<:InnerI2 method moreFun() void}\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{TestB:{<:Outer2::I2 method moreFun() void}}",false
    },{lineNumber(), new String[]{   // Same test as above, with more interesting method selectors and trivial order changes
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{<:I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{D_I1:{<:I1} D_I2:{<:I2} method A d_A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{<:InnerI2 Outer2::I1} \n"  // again, no implementation
        + "%Redirect:{D_I2:{<:InnerI2} method InnerA d_A()}"
        + "TestB:{<:InnerI2 method moreFun(that, other) void}\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{TestB:{<:Outer2::I2 method moreFun(that, other) void}}",false        
    },{lineNumber(), new String[]{   // Redirect, via a pile, when the underlying types are used in aliases
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{<:I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{<:InnerI2 Outer2::I1} \n"  // again, no implementation
        + "%Redirect:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{<:InnerI2 method moreFun(that, other) void \n"
        + "       type method Library () {} }\n"
        + "TestC:{method Outer1::%Redirect::_I2() notSoFun() {} }\n"
        + "TestD:{method Outer1::%Redirect::_I2()::moreFun(that,other)::other mostFun() {} }\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{TestB:{<:Outer2::I2 method moreFun(that, other) void\n"
        + "      type method Library () {}}\n"
        + "TestC:{method Outer2::%Redirect::_I2() notSoFun() {}}\n"
        + "TestD:{method Outer2::%Redirect::_I2()::moreFun(that,other)::other mostFun() {} }\n"
        + "}",false
    },{lineNumber(), new String[]{   // Redirect, via a pile, using the things that will disappear as aliases
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{<:I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{<:InnerI2 Outer2::I1} \n"  // again, no implementation
        + "%Redirect:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{<:InnerI2 method moreFun(that, other) void \n"
        + "       type method Library () {} }\n"
        + "TestC:{method Outer1::InnerI2::moreFun() notSoFun() {} }\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{TestB:{<:Outer2::I2 method moreFun(that, other) void\n"
        + "      type method Library () {}}\n"
        + "TestC:{method Outer2::I2::moreFun() notSoFun() {}}\n"
        + "}",false
    },{lineNumber(), new String[]{   // Redirect, via aliases,
                                     // trying to exploit a rumour that
                                     // identically shaped aliases can be redirected onto one-another
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method Outer1 fun(Void that)}\n"
                    + "       Aliases:{method FluffyA::fun() notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Outer1 fun(Void that)} \n"
        + "   Aliases:{method FluffyA::fun() notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z::FluffyA fun()}"
        + "}",
        "Outer0::Z::Aliases","Outer1::X::Y::Aliases",
        "{TestA:{method Outer2::X::Y::FluffyA fun()}}",false

        // TODO@James: (NOW) when the test above passes, redirect via a pile, where the types in the internal pile are aliases to a mix of internal, internal->external and external

        // TODO@James: (NOW) play with aliases to parameters vs aliases to return values
        
    },{lineNumber(), new String[]{   // Try redirecting a class that implements two internal interfaces,
                                     // via a pile that explicitly directs both
                    "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{<:I1 I2 method Void fun() method Void moreFun()}\n"
                    + "%Redirect:{method I1 _I1() method I2 _I2() method A _A()}\n"
                    + "}"},
        "{InnerI1:{interface method Void fun()}\n"
        + "InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{<:InnerI1 InnerI2}\n"  // redirected class can't have implementation, so it can't mention methods
        + "%Redirect:{method InnerI1 _I1() method InnerI2 _I2() method InnerA _A()}\n"
        + "TestA:{method InnerA aFun()}"
        + "TestB:{<:InnerI1 InnerI2  method fun() void method moreFun() void}\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{"
        + "TestA:{method Outer2::A aFun()}"
        + "TestB:{<:Outer2::I1 Outer2::I2 method fun() void method moreFun() void}"
        + "}",false
        
    },{lineNumber(), new String[]{   // Redirect three classes, via a pile, so that the
                                     // intersection-of-ambiguity rule makes the 
                                     // implemented interfaces and method errors unambiguous
                    "{Iab:{interface} Ibc:{interface} Ica:{interface}\n"
                    + "Eab:{} Ebc:{} Eca:{}\n"
                    + "A:{<:Ica Iab method Void fun() error Eca Eab}\n"
                    + "B:{<:Iab Ibc method Void fun() error Eab Ebc}\n"
                    + "C:{<:Ibc Ica method Void fun() error Ebc Eca}\n"
                    + "%Redirect:{method A _A() method B _B() method C _C() }\n"
                    + "}"},
        "{InnerIab:{interface} InnerIbc:{interface} InnerIca:{interface}\n"
        + "InnerEab:{} InnerEbc:{} InnerEca:{}\n"
        // same order, then interfaces swapped, then errors swapped, just in case it matters
        + "InnerA:{<:InnerIca InnerIab method Void fun() error InnerEca InnerEab}\n"
        + "InnerB:{<:InnerIbc InnerIab method Void fun() error InnerEab InnerEbc}\n"
        + "InnerC:{<:InnerIbc InnerIca method Void fun() error InnerEca InnerEbc}\n"
        + "%Redirect:{method InnerA _A() method InnerB _B() method InnerC _C()}\n"
        + "TestX:{method InnerIab abFun() error InnerEab\n"
        + "       method InnerIbc bcFun() error InnerEbc\n"
        + "       method InnerIca caFun() error InnerEca}\n"
        + "}",
        "Outer0::%Redirect","Outer1::%Redirect",
        "{"
        + "TestX:{method Outer2::Iab abFun() error Outer2::Eab\n"
        + "       method Outer2::Ibc bcFun() error Outer2::Ebc\n"
        + "       method Outer2::Ica caFun() error Outer2::Eca}\n"
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
        "{InnerA:{type method Void fun()} }","Outer0::InnerA","Outer1::A",
        "{"+"Kind:{'@stringU\n'SourceUnfit\n}"
           +"SrcPath:{'@::InnerA\n}"
           +"DestExternalPath:{'@Outer2::A\n}"
           +"SrcKind:{'@stringU\n'FreeTemplate\n}"
           +"DestKind:{'@stringU\n'Template\n}"
           +"UnexpectedMembers:{'@stringU\n'[fun()]\n}"
           +"UnexpectedImplementedInterfaces:{'@stringU\n'[]\n}"
        + "}",true
    },{lineNumber(), new String[]{"{A:{ }}"},  // same test, but with a method argument, using the new mechanism
        "{InnerA:{type method Void fun(Void that)} }","Outer0::InnerA","Outer1::A",
        ec.load("SourceUnfit",
                "SrcPath", "'@::InnerA",
                "DestExternalPath", "'@Outer2::A",
                "SrcKind", "FreeTemplate",
                "DestKind", "Template",
                "UnexpectedMembers", "[fun(that)]",
                "UnexpectedImplementedInterfaces", "[]"
                )
          .str(), true
    },{lineNumber(), new String[]{  // FreeTemplate -> Interface, with some matching methods
        "{A:{interface type method Void fun(Void that)  method Void mostFun(Void that, Library other) }}"
        },
        "{InnerA:{type method Void fun(Void that) type method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcKind", "FreeTemplate", "DestKind", "Interface",
               "UnexpectedMembers", "[moreFun(that), notSoFun()]")
          .str(), true
    },{lineNumber(), new String[]{  // with a mismatch on parameter names in the method selector
                                    // Also Template (by return value) -> Interface, which is infeasible
        "{A:{interface type method Void fun(Void that) type method Void moreFun()"
        + "method Void mostFun(Void that, Library mineAllMine) method Void notSoFun() } }",
        },
        "{InnerA:{type method Void fun(Void that) type method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } "
        + "D:{method Outer1::InnerA makeA() InnerA.fun(void)} "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcKind", "Template", "UnexpectedMembers", "[moreFun(that), mostFun(that,other)]")
          .str(), true
    },{lineNumber(), new String[]{  // Template (by parameter value) -> OpenClass extra subclass
        "{A:{ method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ C:{} } "
        + "D:{type method Void useType(type Any that) void"
        + "   type method Void useA() D.useType(InnerA) } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set( "DestKind", "OpenClass",
               "UnexpectedMembers", "[C]")
          .str(), true
    },{lineNumber(), new String[]{  // Template (by exception) -> OpenClass extra subclass
        "{A:{ type method Void ignoreMe() void " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ type method Void ignoreMe() C:{} } "
        + "D:{type method Void doWithoutA() exception Void  exception InnerA.ignoreMe() } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .str(), true
    },{lineNumber(), new String[]{   // Redirect a class (InnerA) which implements interface methods
                                     // ie OpenClass->OpenClass
                                     // NB: for classes of incompatible kinds, unexpected members and interfaces are not shown.
                    "{"
                    + "I1:{interface method Void fun(Void that)}\n"
                    + "I2:{interface method Void moreFun(Library that, Void other)}\n"
                    + "A:{<:I1 I2 method fun(that) that method moreFun(that, other) other}"
                    + "D_Target:{method I1 _I1() method I2 _I2() method A _A()}"
                    + "}"},
        "{InnerI2:{interface method Void moreFun(Library that, Void other)}\n"
        + "InnerA:{<:InnerI2 Outer2::I1 method fun(that) void} \n"
        + "D_Source:{method InnerI2 _I2() method InnerA _A()}\n"
        + "TestB:{<:InnerI2 method moreFun(that, other) void \n"
        + "       type method Library () {} }\n"
        + "TestC:{method Outer1::D_Source::_I2() notSoFun() {} }\n"
        + "TestD:{method Outer1::D_Source::_I2()::moreFun(that,other)::other mostFun() {} }\n"
        + "}",
        "Outer0::D_Source","Outer1::D_Target",
        ec
          .set("SrcKind", "OpenClass", "UnexpectedMembers", "[]")
          .str(), true
    },{lineNumber(), new String[]{  // OpenClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method '@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void C:{} }}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcKind", "OpenClass", "DestKind", "ClosedClass")
          .str(), true
    },{lineNumber(), new String[]{  // ClosedClass -> ClosedClass (because I can) with missing subclass
        "{A:{ method Void ignoreMe() void " +
        "     method '@private \n Void ignoreMeMore() " +
        "     B:{ method Void ignoreMe() void} } }",
        },
        "{InnerA:{ method Void ignoreMe() void "+
        "     method '@private \n Void ignoreMeMost() " +
        "     C:{} " +
        " }}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcKind", "ClosedClass", "DestKind", "ClosedClass")
          .str(), true
    },{lineNumber(), new String[]{  // Interface with extra method
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcKind", "Interface", "DestKind", "Interface",
               "UnexpectedMembers", "[mostFun(that,other), notSoFun()]")
          .str(), true
    },{lineNumber(), new String[]{  // Interface with inner class
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{} } }",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("UnexpectedMembers", "[C]")
          .str(), true
    },{lineNumber(), new String[]{  // Interface with unexpected inner interface
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) "
        + "C:{interface} } }",
        "Outer0::InnerA","Outer1::A",
        ec
          .str(), true
    },{lineNumber(), new String[]{  // Implementing the interface does not change the error
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) }}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{<:InnerA::C} "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .str(), true
     },{lineNumber(), new String[]{  // Expected interface, with unexpected method, implemented in implementing class
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{<:InnerA::C"
        + "         method mostFun() void"    // With implementation and without respecified types
        + "       } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcPath", "'@::InnerA::C", "DestExternalPath", "'@Outer2::A::C",
               "UnexpectedMembers", "[mostFun()]")
          .str(), true
    },{lineNumber(), new String[]{  // One unimplemented interface; no unexpected members
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{BlockingInterface1:{interface} "
        + "InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface <:BlockingInterface1 } } \n"
        + "C_impl:{<:InnerA::C"
        + "       } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set(
               "UnexpectedMembers", "[]",
               "UnexpectedImplementedInterfaces", "[Outer0::BlockingInterface1]"
               )
          .str(), true
    },{lineNumber(), new String[]{  // Matching nested interfaces, the inner of which implements two internal and one external blocking interfaces
                                    // NB: in the test harness, must specify outer numbers for outers.
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{BlockingInterface1:{interface} \n"
        + "BlockingInterface2:{interface} \n"
        + "InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface <:BlockingInterface1 Outer2::BlockingInterface2 Outer3::A::C"
        + "  } } \n"
        + "C_impl:{<:InnerA::C"
        + "         method Void mostFun()"    //
        + "       } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("UnexpectedImplementedInterfaces", "[Outer0::BlockingInterface1, Outer0::BlockingInterface2, Outer1::A::C]"
               )
          .str(), true

    },{lineNumber(), new String[]{  // When a cascade redirect renames another interface, the reported unexpected interface is the name before the rename.
        "{  A:{interface type method C fun(Void that)  method Void moreFun(Void that, Library other)} \n"
        + " C:{interface}"
        + " }"
        },
        "{ InnerA:{interface type method InnerC fun(Void that)  method Void moreFun(Void that, Library other)} \n"
        + "InnerC:{interface <:InnerA"
        + "       }  \n"
        + "C_impl:{<:InnerC"
        + "       } "
        + "}"
        + "",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcPath", "'@::InnerC", "DestExternalPath", "'@Outer2::C",
               "UnexpectedImplementedInterfaces", "'[@::InnerA]"
               )
          .str(), true

    // IncoherentRedirectMapping
    // TODO@James: enumerate the parameters and explore them thoroughly
    // TODO@James: explore the relationship between intersection unambiguity and forced split
    },{lineNumber(), new String[]{      // Incoherent redirect, forcing InnerAB to be split into both A and B
                                        // @Marco, I protest against "IncoherentDests"; the trailing s is unnecessary, and isn't present on "Dest"
                    "{A:{} B:{}"
                    + "C:{ method A fun() method B moreFun()}"
                    + "}" },
        "{InnerAB:{} "
        + "InnerC:{method InnerAB fun() method InnerAB moreFun() } "
        + "}",
        "Outer0::InnerC","Outer1::C",
        ec.load("IncoherentRedirectMapping",
                "Src", "'[@::InnerC]",
                "Dest", "'[@Outer2::C]",
                "IncoherentSrc", "'@::InnerAB",
                "IncoherentDest", "'[@Outer2::A, @Outer2::B]").str(), true
    },{lineNumber(), new String[]{   // Incoherent redirect: Matching functions (FluffyA::fun()) disagree about the position of their return value
                                     // NB: There is no reliable theory to filter out only nested redirects, so all redirects up to the failure are include in the error.
                    "{X:{Y:{FluffyA:{ type method Outer2 fun()}" // Target of original redirect
                    + "    }"
                    + "FluffyA:{type method Outer1 fun()}"  // The phantom required for the redirect to avoid SourceUnfit.
                    + "}}" },
        "{InnerZ:{FluffyA:{ type method Outer1 fun()}}"
        + "}",
        "Outer0::InnerZ::FluffyA","Outer1::X::Y::FluffyA",
        ec.set(
               "Src", "'[@::InnerZ::FluffyA, @::InnerZ, @::InnerZ, @::InnerZ::FluffyA]",
               "Dest","'[@Outer2::X::Y::FluffyA, @Outer2::X, @Outer2::X, @Outer2::X::FluffyA]",
        	   "IncoherentSrc", "'@::InnerZ::FluffyA",
        	   "IncoherentDest", "'[@Outer2::X::FluffyA, @Outer2::X::Y::FluffyA]"
        	   ).str(), true
    },{lineNumber(), new String[]{   // Try cascading two interfaces on one class, which should fail because
                                     // disambiguating it turned out to be prohibitive.
                                     // @Marco, the mapping list would be shorter if IncoherentSrc was a list, so that the two sets could be put into the Incoherent parameters
                                     // @Marco, I am going to write the error that I want, knowing that the test will fail, and that changing the system to fix this one would break the one above
                    "{I1:{interface method Void fun()}\n"
                    + "I2:{interface method Void moreFun()}\n"
                    + "A:{<:I1 I2 method Void fun() method Void moreFun()}"
                    + "}"},
        "{InnerI1:{interface method Void fun()}\n"
        + "InnerI2:{interface method Void moreFun()}\n"
        + "InnerA:{<:InnerI1 InnerI2}"
        + "TestB:{<:InnerI1 InnerI2  method fun() void method Void moreFun() void}\n"
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec.set(
               "Src", "'[@::InnerA]",
               "Dest","'[@Outer2::A]",
        	   "IncoherentSrc", "'[@::InnerI1, @::InnerI2]",
        	   "IncoherentDest", "'[@Outer2::I1, @Outer2::I2]"
        ).str(), true

    // TODO@James: when the error for the test above has settled, do a pile redirect of two classes,
    // where one uses two interfaces and the other uses only one of them, to confirm that the disambiguation
    // of one internal interface on a class does not disambiguate the other
    
    // next error with variable portions.
    // ClassClash can't happen on a redirect

    // MethodClash: Path(1), Left(1), Right(1), LeftKind(enum(4)), RightKind(enum(4)),
    // DifferentParameters(0..), DifferentReturnType(t/f), DifferentThisMdf(t/f), IncompatibleException(t/f)

    },{lineNumber(), new String[]{   // Redirect, using the matching-alias rule, to a class with an incompatible method
                                     // @Marco, I protest against tabs in method specs in these errors
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method Library fun(Void that)}\n"
                    + "       Aliases:{method FluffyA::fun() notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Void fun(Void that)} \n"
        + "   Aliases:{method FluffyA::fun() notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z::FluffyA fun()}"
        + "}",
        "Outer0::Z::Aliases","Outer1::X::Y::Aliases",
        ec.load("MethodClash",
                "Path", "'@::Z::FluffyA",
                "Left", "method Void fun(Void that)",
                "Right", "method Library fun(Void that)",
                "LeftKind", "AbstractMethod",
                "RightKind", "AbstractMethod",
                "DifferentParameters", "[]",
                "DifferentReturnType", "true",
                "DifferentThisMdf", "false",
                "IncompatibleException", "false").str(), true
    },{lineNumber(), new String[]{   // Redirect to, but not from, incompatible which is a path
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method Outer1 fun(Void that)}\n"
                    + "       Aliases:{method FluffyA::fun() notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Void fun(Void that)} \n"
        + "   Aliases:{method FluffyA::fun() notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z::FluffyA fun()}"
        + "}",
        "Outer0::Z::Aliases","Outer1::X::Y::Aliases",
        ec.set("Right", "method Outer1::X::Y fun(Void that)").str(), true
/* TODO@James: play properly with redirects and primitive types        
    },{lineNumber(), new String[]{   // Redirect from, but not to, incompatible which is a path
                                     // @Marco, I think that we should keep getting MethodClash here, because Void is not a source-created path to add to the cascade
                                     //TODO: @James, I think it should be src unfit instrad. We *can* redirect stuff to Void, Any or Library. For example we can have collections of Libraries.
                    "{"
                    + "X:{Y:{\n"
                    + "       FluffyA:{method Void fun(Void that)}\n"
                    + "       Aliases:{method FluffyA::fun() notSoFun()}\n"
                    + "}}"
                    + "}"},
        "{Z:{\n"
        + "   FluffyA:{method Outer1 fun(Void that)} \n"
        + "   Aliases:{method FluffyA::fun() notSoFun()}\n"
        + "}\n"
        + "TestA:{method Z::FluffyA fun()}"
        + "}",
        "Outer0::Z::Aliases","Outer1::X::Y::Aliases",
        ec.set("Left", "method Outer1 fun(Void that)",
               "Right", "method Void fun(Void that)").str(), true
*/

/* TODO@James : try this test, when I get to method clashes
    },{lineNumber(), new String[]{ // mismatches in type vs instance method
        "{A:{type method Void fun(Void that) method Void moreFun(Void that)"
        + "type method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        },
        "{InnerA:{type method Void fun(Void that) type method Void moreFun(Void that)"
        + "method Void mostFun(Void that, Library other) method Void notSoFun() } }",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("UnexpectedMethods", "[moreFun(that), mostFun()]").str(), true
          */

          /* TODO@James: with this test, I get TargetUnavailable, which I don't understand yet
    },{lineNumber(), new String[]{  // Matched inner interface shows as non-free
        "{A:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other) \n"
        + " C:{interface}}}"
        },
        "{InnerA:{interface type method Void fun(Void that)  method Void moreFun(Void that, Library other)\n"
        + "C:{interface method Void mostFun() } } \n"
        + "C_impl:{<:InnerA::C"
        + "         method Void mostFun()"    // Uncommenting this line changes from SourceUnfit to TargetUnavailable
        + "       } "
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec
          .set("SrcPath", "Outer0::InnerA::C", "DestExternalPath", "'@Outer1::A::C",
               "SrcKind", "Interface",
               "UnexpectedMembers", "[mostFun()]")
          .str(), true
           */

    // MemberUnavailable:
    // TODO@James: set up these tests properly; this is a temporary job to keep errors i found by accident 
    // Path(1), Left(1), Right(1), LeftKind(enum(4)), RightKind(enum(4)),
    // DifferentParameters(0..), DifferentReturnType(t/f), DifferentThisMdf(t/f), IncompatibleException(t/f)


    },{lineNumber(),   // Redirect a private class
        new String[]{"{A:{ }}"},
        "{InnerA:'@private\n {type method Void fun(Void that)} }",
        "Outer0::InnerA","Outer1::A",
        ec.load("MemberUnavailable",
                "Path", "'@::InnerA",
                "Selector", "",
                "InvalidKind", "PrivatePath"
               ).str(), true
    },{lineNumber(),      // Redirect a class with a private method, for which the target has a matching method
       new String[]{"{A:{type method Void fun(Void that) }}"},
        "{InnerA:{type method '@private\n Void fun(Void that)} "
        + "InnerB:{type method Void moreFun(Void that) InnerA.fun(that)}"
        + "}",
        "Outer0::InnerA","Outer1::A",
        ec.set("Selector", "fun(that)"
                )
          .str(), true

    }});
    return TestHelper.skipUntilLine(tests, startLine);
}

//},{"Outer2::D::C","Outer1::C",new String[]{"{A:{}}","{C:{}}","{D:##walkBy}"}


@Test  public void test() {

  TestHelper.configureForTest();
  Program p=TestHelper.getProgram(_p);
  ClassB cb1=getClassB("cb1", _cb1);
  Path path1=Path.parse(_path1);
  Path path2=Path.parse(_path2);
  ClassB expected=getClassB("expected", _expected);
  if(!isError){
    ClassB res=Redirect.redirect(p, cb1,path1,path2);
    TestHelper.assertEqualExp(expected,res);
    }
  else{
    try{Redirect.redirect(p, cb1,path1,path2);fail("error expected");}
    catch(Resources.Error err){
      ClassB res=(ClassB)err.unbox;
      TestHelper.assertEqualExp(expected,res);
    }
  }
}
}


}
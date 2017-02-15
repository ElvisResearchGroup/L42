package testAux;

import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import facade.PData;
import facade.Parser;
import reduction.SmallStep;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import ast.Ast.Stage;
import ast.ExpCore;
import ast.Expression;
import ast.Ast.Path;
import ast.ExpCore.ClassB;
import auxiliaryGrammar.Norm;
import auxiliaryGrammar.Program;

public class TestSingleStep {

  @RunWith(Parameterized.class)
  public static class Test1 {
    @Parameter(0) public int _lineNumber;
    @Parameter(1) public String e1;
    @Parameter(2) public String e2;
    @Parameters(name = "{index}: line {0}")
    public static List<Object[]> createData() {
      return Arrays.asList(new Object[][] {
         {lineNumber(),"(Any x= This0.C.new() void)","(This0.C x= This0.C.new() void)"
       },{lineNumber(),"( This0.C xx= This0.C.new()"
        + "  mut This0.C x= (This0.C z=This0.C.new() y)"
        + "  This0.C zz= This0.C.new()"
        + "  xx)",
         "(This0.C xx= This0.C.new()"
       + " This0.C z=This0.C.new()"
       + " mut This0.C x= y"
       + " This0.C zz= This0.C.new()"
       + " xx)"

       },{lineNumber(),"( mut This0.D x= (This0.D z=This0.D.new(x:zz) This0.C zz=This0.C.new() y)"
           + "  x)",
           " (This0.D z=This0.D.new(x:zz)"
          + " This0.C zz= This0.C.new()"
          + " mut This0.D x= y"
          + " x)"
       },{lineNumber(),"{ A:{//@plugin\n//L42.is/connected/withAlu\n()}"
        + " C: use A check sumInt32(n1:{} n2:{}) error void}",
          "{ A:{//@plugin\n//L42.is/connected/withAlu\n()}##star ^##"
        + " C:error {//@stringU\n//InvalidInt32\n}##star ^##}"

       },{lineNumber(),"{ A:{//@plugin\n//L42.is/connected/withAlu\n()}"
           + " C: use A check sumInt32(n1:{//@int32\n//5\n} n2:{//@int32\n//3\n}) error void}",
             "{ A:{//@plugin\n//L42.is/connected/withAlu\n()}##star ^##"
           + " C:{//@int32\n//8\n}}"


       },{lineNumber(),"(This0.C x= This0.C.new() void)","(void)"
       },{lineNumber(),"loop void","(Void void0=void, loop void)"
       },{lineNumber(),"(This0.C x= This0.C.new() Any y= y.m() void)","(Any y= y.m() void)"
       },{lineNumber(),"(fwd Any x= This0.C.new() x)","(This0.C x= This0.C.new() x)"

       },{lineNumber(),"(capsule Any x= This0.C.new() x)",
          "(capsule This0.C x= This0.C.new() x)"

       },{lineNumber(),"(capsule This0.C x= This0.C.new() x)",
          "(This0.C.new())"

       },{lineNumber(),"( Any x= y x)","(y)"
       },{lineNumber(),"( mut Any x= (This0.C z=This0.C.new() y) x)","( This0.C z=This0.C.new() mut Any x=  y x)"
       },{lineNumber(),"( Any x= error (This0.C z=This0.C.new() z) catch error This0.D y y x)","( Any x= error (This0.C z=This0.C.new() z) x)"
       },{lineNumber(),"( Any x= error (This0.C z=This0.C.new() z) catch error This0.C y y x)","( This0.C y=(This0.C z=This0.C.new() z) y)"
       },{lineNumber(),"( Any x= (Any z=This0.C.new() z) catch error This0.C y y x)",
          "( Any x= (This0.C z=This0.C.new() z) catch error This0.C y y x)"

       },{lineNumber(),"( Any x= (This0.C z=This0.C.new() z) catch error This0.C y y x)",
          "( This0.C x= (This0.C z=This0.C.new() z) catch error This0.C y y x)"

       },{lineNumber(),"( This0.C x= (This0.C z=This0.C.new() z) catch error This0.C y y catch error This0.D y y x)",
         "( This0.C x= (This0.C z=This0.C.new() z)  x)"

       },{lineNumber(),"This0.C.foo(bar:This0.C)","(class This0.C this0=This0.C,class This0.C bar=This0.C (bar.foo(bar:this0)))"

       },{lineNumber()," (class This0.C this0=This0.C,class This0.C bar=This0.C (bar.foo(bar:this0)))",
          " (class This0.C bar=This0.C ( bar.foo(bar:This0.C) ) )"

       },{lineNumber()," (class This0.C bar=This0.C ( bar.foo(bar:This0.C) ) )",
          " ((This0.C.foo(bar:This0.C)))"


       },{lineNumber(),"(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) (r).x())","(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) (read This0.D d=(r) d.x()))"
       },{lineNumber(),"(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) r.x(that:(c)))","(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c) (This0.C c0=(c) r.x(that:c0)))"
       },{lineNumber(),"This0.C.new()","(This0.C c=This0.C.new() c)"
       //
       },{lineNumber(),"(This0.C c=This0.C.new() This0.D r=(This0.D d=This0.D.new(x:c) d) r.x())",
          "(This0.C c=This0.C.new() This0.D r=( This0.D d=This0.D.new(x:c) d ) ( This0.C c0=( This0.D d0=This0.D.new(x:c) d0.x() ) c0 ) )"

       },{lineNumber(),"(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c)  r.x())",
          "(This0.C c=This0.C.new() This0.D r=This0.D.new(x:c)  c)"

       },{lineNumber(),"(This0.C c1=This0.C.new() This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c1)  r.x(that:c2))",
         "(This0.C c1=This0.C.new() This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c2)  void)"
       },{lineNumber(),"(This0.C c1=This0.C.new() ( This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c1)  r.x(that:c2)))",
          "(This0.C c1=This0.C.new() ( This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c2)  void  ))"

       },{lineNumber(),"use Any check m(that:(This0.C c1=This0.C.new() This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c1)  r.x(that:c2))) void",
          "use Any check m(that:(This0.C c1=This0.C.new() This0.C c2=This0.C.new() mut This0.D r=This0.D.new(x:c2)  void)) void"

       },{lineNumber()," (use Any check m(that:(error void)) void catch error Void x void void)",
         "( Void x=( void ) void )"

       },{lineNumber(),"(C c1=C.new() ( C c2=C.new() mut D r=D.new(x:c1)  r.x(c2)))",
          "(C c1=C.new() ( C c2=C.new() mut D r=D.new(x:c2)  void  ))"
       },{lineNumber(),"(C c1=C.new() mut D r=D.new(x:c1) (C c2=C.new() r.x(c2)))",
          "(C c1=C.new() mut D r=D.new(x:c2) C c2=C.new() (void))"
//not well typed
       },{lineNumber(),"( mut D r=D.new(x:r) (mut D r1a=D.new(x:r1b) mut D r1b=D.new(x:r1b) (mut D r2=D.new(x:r1b) Any aa=r.x(r2) r1a)))",
          "( mut D r=D.new(x:r2) mut D r1b=D.new(x:r1b) mut D r2=D.new(x:r1b)  (mut D r1a=D.new(x:r1b) (Any aa= void r1a)))"
       },{lineNumber(),"( mut D r=D.new(x:r) (mut D r1a=D.new(x:r1b) mut D r1b=D.new(x:r1b) (mut D r2=D.new(x:r1a) r.x(r2))))",
          "( mut D r=D.new(x:r2) mut D r1a=D.new(x:r1b) mut D r1b=D.new(x:r1b) mut D r2=D.new(x:r1a) ((void)))"
       },{lineNumber(),"( mut D r=D.new(x:r) (mut D r1a=D.new(x:r1b) mut D r1b=D.new(x:r1b) (mut D r2a=D.new(x:r2b)mut D r2b=D.new(x:r1a) r.x(r2a))))",
          "( mut D r=D.new(x:r2a)mut D r1a=D.new(x:r1b)mut D r1b=D.new(x:r1b) mut D r2a=D.new(x:r2b)  mut D r2b=D.new(x:r1a) ((void)))"
       },{lineNumber(),"{outer() C: {new() class method Library m() {inner()}} D: C.m()}",
            "{outer() C: {new() class method Library m() {inner()}}##star ^## D:(class C this0=C { inner()}##star ^## )}"
       },{lineNumber(),"{ method Library m() ({outer() C: {new() class method Library m() ({inner()})} D: C.m()})}",
         "{ method Library m() ({ outer()C:{ new()class method Library m() ({ inner()})}##star ^## D:(class C this0=C ({ inner()}##star ^## ))})}"


         //can not work without the normalization
       //},{"{ C:( Library result=( ( ( Library result0=(  {//@exitStatus\n//0\n} ) result0 ) ) ) result )}",
         },{lineNumber(),"{ C:( Library result=( ( ( Library result0=  {//@exitStatus\n//0\n}  result0 ) ) ) result )}",
           " { C:( Library result=( ( ( {//@exitStatus\n//0\n}##star ^## ) ) ) result )}"
       },{lineNumber(),TestHelper.multiLine("{"
        ,"C:{ k() class method Library ok() ({//@oK\n})"
        ,"        class method Library ko() ({//@kO\n})"
        ,"  }"
        ,"I:{interface }"
        ,"AI:{ k() implements This1.I}"
        ,"D:("
        ,"Any z=error This0.AI.k()"
        ,"  catch error This0.AI x This0.C.ok()"
        ,"  This0.C.ko()"
        ,"  )}"),
        TestHelper.multiLine(
         "{C:{ k()class method Library ok() ("
        ,"  {//@oK"
        ,"}"
        ,"  )class method Library ko() ("
        ,"  {//@kO"
        ,"}"
        ,"  )}##star ^## I:{interface }##star ^## AI:{ k() implements This1.I}##star ^## D:("
        ,"  Any z=error ("
        ,"class This0.AI this0=This0.AI"
        ,"This0.AI.#mutK()"
        //,"    This0.AI aI=This0.AI.k()"
        //,"    aI"
        ,"    )"
        ,"  catch error This0.AI x This0.C.ok()"
        ,"  This0.C.ko()"
        ,"  )}")
       },{lineNumber(),
TestHelper.multiLine("{"
,"AI:{ k()}"
,"Box:{ lent k(var fwd read Any f)}"
,"D:("
,"  lent Box box=Box.#lentK(f:box)"
,"  Any z1=("
,"    This0.AI any=(AI aI=AI.#mutK() aI)"
,"    box.f(any))"
,"  Any z2=error box.f()"
,"  {ok()}"
,"  )}"
),TestHelper.multiLine("{"
,"AI:{ mut k()}##star ^##"
,"Box:{ lent k(var fwd read Any f)}##star ^##"
,"D:("
,"  This0.AI any=(AI aI=AI.#mutK() aI)"
,"  lent Box box=Box.#lentK(f:any)"
,"  Any z1=(void)"
,"  Any z2=error box.f()"
,"  {ok()}##star ^##"
,"  )}")

},{lineNumber(),TestHelper.multiLine(" "
,"(Library cond=("
,"    Void unused5=("
,"      Void unused6=error void"
,"      ( Void unused4=return {}  void )"
,"      )"
,"    catch return Library result0 result0 "
,"    error void"
,"    )"
,"  void)"),
TestHelper.multiLine(" "
,"("
,"  Library cond=("
,"    Void unused5=error ( void )"
,"    error void"
,"    )"
,"  void)")

}});}

    @Test
    public void testStep() {
      TestHelper.configureForTest();
      ExpCore ee1=Desugar.of(Parser.parse(null," "+e1)).accept(new InjectionOnCore());
      ExpCore ee2=Desugar.of(Parser.parse(null," "+e2)).accept(new InjectionOnCore());
      Program p=TestHelper.getProgramCD();
      ExpCore eRed=new SmallStep().step(new PData(p), ee1);
      TestHelper.assertEqualExp(eRed,ee2);
    }

    }


}
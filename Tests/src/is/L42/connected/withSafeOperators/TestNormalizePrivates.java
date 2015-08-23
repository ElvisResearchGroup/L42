package is.L42.connected.withSafeOperators;

import static helpers.TestHelper.getClassB;
import helpers.TestHelper.ErrorCarry;
import static org.junit.Assert.fail;
import helpers.TestHelper;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
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

public class TestNormalizePrivates{
@RunWith(Parameterized.class)
public static class TestCollectPrivates {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String expected;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {return Arrays.asList(new Object[][] {{
    lineNumber(),"{}","[]\n[]\n[]\ntrue"
  },{
    lineNumber(),"{method Void foo()}","[]\n[]\n[]\ntrue"
  },{
    lineNumber(),"{method'@private\n Void foo()}","[]\n[Outer0::foo()[0]foo__0_0()]\n[]\nfalse"
  },{
    lineNumber(),"{method'@private\n Void foo__42()}","[42]\n[Outer0::foo__42()[0]null]\n[]\ntrue"
  },{
    lineNumber(),"{method'@private\n Void foo__42()method'@private\n Void foo__43()}",
    "[42, 43]\n[Outer0::foo__42()[0]null, Outer0::foo__43()[0]null]\n[]\ntrue"
  },{
    lineNumber(),
    "{method'@private\n Void foo__42()"
    + " this({ method '@private\n Void bla__33()  }, second:{})"
    + "}",
    "[33, 42]\n[Outer0::foo__42()[0]null, Outer0::foo__42()[1]bla__33()[0]null]\n[]\ntrue"
  },{
    lineNumber(),
    "{ make(Foo f)'@private\n method'@private\n Void foo()"
    + " this({ method '@private\n Void bla() void }, second:{mut kame(mut Hame ha)'@private\n})"
    + "}",
    "[]\n"
    + "[Outer0::make(f)[0]make__0_0(f__0_0),"
    + " Outer0::#f()[0]#f__0_0(),"
    + " Outer0::f()[0]f__0_0(),"
    + " Outer0::foo()[0]foo__1_0(),"
    + " Outer0::foo()[1]bla()[0]bla__2_0(),"
    + " Outer0::foo()[2]kame(ha)[0]kame__3_0(ha__3_0),"
    + " Outer0::foo()[2]#ha()[0]#ha__3_0(),"
    + " Outer0::foo()[2]ha()[0]ha__3_0()]\n[]\nfalse"
  },{
    lineNumber(),
    "{method'@private\n Void foo__42()"
    + " this({ method '@private\n Void bla()  }, second:{})"
    + "}",
    "[42]\n[Outer0::foo__42()[0]null, Outer0::foo__42()[1]bla()[0]null]\n[]\nfalse"
  
  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  NormalizePrivates.reset();
  ClassB cb1=getClassB("cb1", _cb1);
  CollectedLocatorsMap result = NormalizePrivates.collectPrivates(cb1);
  if (result.pedexes.size()==0){
    result.computeNewNames();
  }
  Assert.assertEquals(expected, result.toString());
  }
}
@RunWith(Parameterized.class)
public static class TestNormalizePrivates1 {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _expected;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {return Arrays.asList(new Object[][] {{
    lineNumber(),"{}","{}"
  },{
    lineNumber(),"{method Void foo()}","{method Void foo()}"
  },{
    lineNumber(),"{method Void fo__o()}","{method Void fo_$%o()}"
  },{
    lineNumber(),"{method Void foo___a()}","{method Void foo_$%_a()}"
  },{
    lineNumber(),"{method Void foo____a()}","{method Void foo_$%_$%a()}"
  },{
    lineNumber(),"{method Void foo_____a()}","{method Void foo_$%_$%_a()}"
  },{
    lineNumber(),"{method Void fo$%$%o__a()}","{method Void fo$%$%o_$%$%$%a()}"
  },{
    lineNumber(),"{ C__A:{ <:C__A }}","{ C_$%A:{ <:C_$%A }}"                                                           
  
  },{
    lineNumber(),"{ C:'@private\n{ <:C }}","{ C__0_0:'@private\n{ <:C__0_0 }}"                                                           
  },{
    lineNumber(),"{ C:'@private\n{ D:{<:C} }}","{ C__0_0:'@private\n{ D:{<:C__0_0} }}"
  },{
    lineNumber(),"{ C:'@private\n{ A:{B:{<:C }}}}","{ C__0_0:'@private\n{ A:{B:{<:C__0_0 }}}}"   
  },{
    lineNumber(),"{ D:{<:C} C:'@private\n{ <:C }}","{ D:{<:C__0_0 } C__0_0:'@private\n{ <:C__0_0 }}" 
  },{
    lineNumber(),"{ D:{<:A::C} A:{C:'@private\n{ <:C }}}","{ D:{<:A::C__0_0 } A:{C__0_0:'@private\n{ <:C__0_0 }}}" 
  },{
    lineNumber(),"{ D:{<: A::C::D} A:{C:'@private\n{ D:{}}}}","{ D:{<:A::C__0_0::D } A:{C__0_0:'@private\n{ D:{}}}}" 
  },{
    lineNumber(),"{ D:{<:A::C, A::C::D} A:{C:'@private\n{ <:C D:{}}}}","{ D:{<:A::C__0_0,A::C__0_0::D } A:{C__0_0:'@private\n{ <:C__0_0 D:{}}}}" 
  
  },{
    lineNumber(),"{ A:{ method '@private\n Void foo() void}}","{ A:{ method '@private\n Void foo__0_0() void}}" 
  },{
    lineNumber(),"{  method '@private\n Void foo() void}","{  method '@private\n Void foo__0_0() void}" 
  },{
    lineNumber(),"{ method Library bar() {  method '@private\n Void foo() void}}","{ method Library bar() {  method '@private\n Void foo__0_0() void}}" 
    
  }});}


@Test  public void test() {
  TestHelper.configureForTest();
  NormalizePrivates.reset();
  ClassB cb1=getClassB("cb1", _cb1);
  ClassB expected=getClassB("expected", _expected);
  cb1=NormalizePrivates.normalize(cb1);
  TestHelper.assertEqualExp(expected,cb1);
  }
}
}
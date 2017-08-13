package is.L42.connected.withSafeOperators.refactor;

import static helpers.TestHelper.getClassB;

import org.junit.Test;

import ast.ExpCore.ClassB;
import facade.L42;
import helpers.TestHelper;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.ClassUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.MethodClash;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.PathUnfit;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.SelectorUnfit;
import programReduction.Program;

public class TestRenameMethod {

public static final RenameMethods rm=new RenameMethods();

public static void check(RenameMethods rms,String in,String out) throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
  L42.resetFreshPrivate();
  L42.usedNames.clear();
  ClassB cb=getClassB(true,null,in);
  ClassB res=rms.actP(Program.emptyLibraryProgram(),cb);
  ClassB expected=getClassB(true,null,out);
  TestHelper.assertEqualExp(expected, res);
  }
@Test public void test1() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
  check(rm.addRenameS("This","m()","k()"),"{method Any m()}","{method Any k()}");
  }
@Test(expected=SelectorUnfit.class) public void test1Fail() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addRenameS("This","m()","k(x)"),"{method Any m()}","{method Any k()}");
}
@Test public void test2() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addCloseS("This"),
"{mut Any a, Void b, class method mut This (mut Any a,Void b)}",
"{ "
+ "read method read Any a_$_2() "
+ "mut method mut Any #a_$_2() "
+ "read method Void b_$_2() "
+ "class method mut This0 #apply_$_2(mut Any a, Void b) }");
}
@Test public void test2Bis() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addCloseS("This"),
"{mut Any a, Void b, class method mut This (mut Any a,Void b)}",
"{ "
+ "read method read Any a_$_2() "
+ "mut method mut Any #a_$_2() "
+ "read method Void b_$_2() "
+ "class method mut This0 #apply_$_2(mut Any a, Void b) }");
}

@Test public void test3() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addAbstractS("This","k()"),
"{ class method Any k() void}",
"{ class method Any k() }"
);}

@Test public void test4() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addAbstractAliasS("This","k()","kk()"),
"{ class method Any k() void}",
"{ class method Any k() class method Any kk() void}"
);}

@Test public void test4bis() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addAbstractAliasS("This","k(x)","kk(y)"),
"{ class method Any k(Any x) x}",
"{ class method Any k(Any x) class method Any kk(Any y) y}"
);}


@Test public void test5() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addHideS("This", "foo(x,y)"),
"{ class method Any foo(Void x,Void y) void   class method Any f()this.foo(x:void,y:void)}",
"{ class method Any foo0_$_2(Void x,Void y) void   class method Any f()this.foo0_$_2(x:void,y:void)}"
);}

@Test public void test5Bis() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addHideS("This", "foo(x,y)"),
"{ class method Any foo(Void x,Void y) void   class method Any f()this.foo(x:void,y:void)}",
"{ class method Any foo0_$_2(Void x,Void y) void   class method Any f()this.foo0_$_2(x:void,y:void)}"
);}

@Test public void testAddHideRenameShadow1() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addHideS("This", "mMain()").addRenameS("This","mNew()", "mMain()"),
"{ class method Any mMain() this.mNew() class method Any mNew() }",
" { "+
"class method "+ 
"Any mMain0_$_2() this.mMain() "+
"class method  "+
"Any mMain() }"
);}

@Test public void testAddHideRenameShadow2() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addRenameS("This","mNew()", "mMain()").addHideS("This", "mMain()"),
"{ class method Any mMain() this.mNew() class method Any mNew() }",
" { "+
"class method "+ 
"Any mMain0_$_2() this.mMain() "+
"class method  "+
"Any mMain() }"
);}


@Test public void test6() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addRenameS("This", "foo(x,y)","bar(beer,buz)"),
"{ class method Any foo(Void x,Void y) void   class method Any f()this.foo(x:void,y:void)}",
"{ class method Any bar(Void beer,Void buz) void   class method Any f()this.bar(beer:void,buz:void)}"
);}


@Test public void test7() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addRenameS("This", "foo()","bar()")
        .addRenameS("This", "foo2()","bar2()"),
"{ class method Any foo() void class method Any foo2()}",
"{ class method Any bar() void class method Any bar2()}"
);}



@Test public void test8() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm.addRenameS("This", "foo()","unit()")
        .addRenameS("This", "foo2()","unit()"),
"{ class method Any foo() void class method Any foo2()}",
"{ class method Any unit() void}"
);}


@Test(expected=MethodClash.class) public void testM1() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm
  .addRenameS("This", "foo1()","unit()")
  .addRenameS("This", "foo2()","unit()")
  .addRenameS("This", "foo3()","unit()")
,"{ class method Any foo1() void class method Void foo2() class method Library foo3()}"
,"--"//can not work, we need a most specific, not most general
);}

@Test public void testM2() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm
  .addRenameS("This", "foo1()","unit()")
  .addRenameS("This", "foo2()","unit()")
  .addRenameS("This", "foo3()","unit()")
,"{A:{interface} B:{interface} C:{implements A,B}"
+ " class method A foo1()"
+ " class method B foo2()"
+ " class method C foo3()}"
,"{A:{interface} B:{interface} C:{implements A,B}"
+ " class method C unit()}");}

//I'm confused... I think this should fail with the current "broken" code (see TODO)
@Test public void testM3() throws PathUnfit, SelectorUnfit, MethodClash, ClassUnfit{
check(rm
  .addRenameS("This", "foo2()","unit()")
  .addRenameS("This", "foo3()","unit()")
  .addRenameS("This", "foo1()","unit()")
,"{A:{interface} B:{interface} C:{implements A,B}"
+ " class method A foo1()"
+ " class method B foo2()"
+ " class method C foo3()}"
,"{A:{interface} B:{interface} C:{implements A,B}"
+ " class method C unit()}");}

}

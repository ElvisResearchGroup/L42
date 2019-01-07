package oldTestsNotMaintained.testSlow;

import helpers.TestHelper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;





import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reduction.SmallStep;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import typeSystem.TypeSystemOK;
import ast.ErrorMessage;
import ast.Expression;
import ast.ExpCore.ClassB;
import ast.Ast;
import ast.Ast.Stage;
import ast.Ast.Path;
import programReduction.Program;
import facade.L42;
import facade.Parser;

public class TestL42Slow {

@Before public void configure(){
  TestHelper.configureForTest();
  L42.setRootPath(Paths.get("dummy"));
  }

@Test
public void testIntrospectionPlugin() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"C:{"
,"  mapS=IntrospectionPlugin.typeNameToAdapter(S)"
,"  tRen=IntrospectionPlugin.nameToAdapter(S\"T\".that())"
,"  map= IntrospectionPlugin.adaptLib(l1:mapS,l2:tRen)"
,"  return ExitCode.normal()"
,"  }}"
)).getErrCode(),0);}


@Test
public void test0() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"C:{"
,"var x=True()"
,"x:=False()"
,"if x (return ExitCode.failure())"
,"return ExitCode.normal()}}"
)).getErrCode(),0);}

@Test
public void test1() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase1"
,"C:ExitCode.normal()}"
)).getErrCode(),0);}

@Test
public void test2() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase2"
,"A:{(As as)}"
,"As:Collections.list(A)"
,"C: ExitCode.normal()}"
)).getErrCode(),0);}

  @Test
  public void test2a() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"  {reuse L42.is/nanoBase0"
,"    List:{_new(Cell head)"
,"      class method List () (List._new(head:CellEnd()))"
,"      method List #add(Library that) ("
,"        List._new(head:CellNext("
,"          elem:that,"
,"          next:this.head()))"
,"        )"
,"      class method This0 #begin() This0()"
,"      method List #end()this"
,"      Cell:{interface}"
,"      CellEnd:{() implements Cell}"
,"      CellNext:{(Library elem, Cell next) implements Cell}"
,"      }"
,"    C:{"
,"      l=List[{a()};{b()};]"
,"      if False() (return ExitCode.failure())"
,"      return ExitCode.normal()}}"
)).getErrCode(),0);}

  @Test
  public void testThis0ToThis1C_1() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"A:{new()"
,"  class method A a() This0.b()"
,"  class method This0 b() A.new()"
,"  }"
,"C:ExitCode.normal()"
,"}"
)).getErrCode(),0);}


   @Test
  public void testThis0ToThis1C() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"A:{new()"
,"  class method A a() A.new()"
,"  class method This0 b() This0.a()"
,"  }"
,"C:ExitCode.normal()"
,"}"
)).getErrCode(),0);}


/*public  static final String codeLong=
  TestHelper.multiLine(""
,"    {"
,"    Bool:{interface"
,"    class method"
,"    This0 #apply()"
,"    method"
,"    Void #checkTrue() exception Void"
,"    method"
,"    This0 #and(This0 that) }"
,"    True:{ _private() implements This1::Bool"
,"    method #apply() (this._private())"
,"    method #checkTrue() (void)"
,"    method #and(that ) (that)}"
,"    False:{ _private() implements This1::Bool"
,"    method #apply() (this._private())"
,"    method #checkTrue() (exception void)"
,"    method #and(that ) (this)}"
,"    ExitCode:{"
,"    class method"
,"    Library normal() ("
,"      Void unused=("
,"        Void unused0=return {'@exitStatus"
,"    '0"
,"    }"
,"        void"
,"        )"
,"      catch return result ("
,"        on Library result"
,"        )"
,"      error void"
,"      )"
,"    class method"
,"    Library failure() ("
,"      Void unused=("
,"        Void unused0=return {'@exitStatus"
,"    '42000"
,"    }"
,"        void"
,"        )"
,"      catch return result ("
,"        on Library result"
,"        )"
,"      error void"
,"      )}"
,"    List:{ _new(This0::Cell head)"
,"    class method"
,"    This1::List #apply() (This1::List._new(head:This0::CellEnd.#apply()))"
,"    method"
,"    This1::List #add(Library that) (This1::List._new(head:This0::CellNext.#apply(elem:that, next:this.head())))"
,"    method"
,"    Library top() ("
,"      Void unused=("
,"        Void unused0=exception this.head()"
,"        catch exception x ("
,"          on This0::CellNext return x.elem()"
,""
,"          on This0::Cell error void"
,"          )"
,"        void"
,"        )"
,"      catch return result ("
,"        on Library result"
,"        )"
,"      error void"
,"      )"
,"    method"
,"    This1::List pop() ("
,"      Void unused=("
,"        Void unused0=exception this.head()"
,"        catch exception x ("
,"          on This0::CellNext return This1::List._new(head:x.next())"
,""
,"          on This0::Cell error void"
,"          )"
,"        void"
,"        )"
,"      catch return result ("
,"        on This1::List result"
,"        )"
,"      error void"
,"      )"
,"    Cell:{interface }"
,"    CellEnd:{ #apply() implements This1::Cell}"
,"    CellNext:{ #apply(Library elem This1::Cell next) implements This1::Cell}}"
,"    C:("
,"      Void unused=("
,"        Void unused0=return ("
,"          This0::List result0=("
,"            This0::List::CellNext cell2=("
,"              This0::List::CellNext cell=("
,"                This0::List::CellNext cell0=("
,"                  This0::List::CellNext cell1=("
,"                    This0::List::CellNext cell1000=("
,"                      This0::List::CellNext cell40000=("
,"                        This0::List::CellNext cell000000=("
,"                          This0::List::CellNext cell30000=("
,"                            This0::List::CellNext cell200000=("
,"                              This0::List::CellNext cell0100000=("
,"                                This0::List::CellNext cell100000=("
,"                                  This0::List::CellNext cell4000000=("
,"                                    This0::List::CellNext cell20000000=("
,"                                      This0::List::CellEnd cell3000000=("
,"                                        This0::List::CellEnd cell10000000=("
,"                                          This0::List::CellEnd cell00000000=("
,"                                            This0::List::CellEnd cellEnd00000000=This0::List::CellEnd.#apply()"
,"                                            cellEnd00000000"
,"                                            )"
,"                                          ("
,"                                            This0::List list00000000=This0::List._new(head:cell00000000)"
,"                                            cell00000000"
,"                                            )"
,"                                          )"
,"                                        cell10000000"
,"                                        )"
,"                                      ("
,"                                        This0::List::CellNext cellNext0000000=This0::List::CellNext.#apply(elem:{ a()}, next:cell3000000)"
,"                                        cellNext0000000"
,"                                        )"
,"                                      )"
,"                                    ("
,"                                      This0::List list1000000=This0::List._new(head:cell20000000)"
,"                                      cell20000000"
,"                                      )"
,"                                    )"
,"                                  cell4000000"
,"                                  )"
,"                                ("
,"                                  This0::List::CellNext cellNext100000=This0::List::CellNext.#apply(elem:{'@exitStatus"
,"    '42000"
,"    }, next:cell100000)"
,"                                  cellNext100000"
,"                                  )"
,"                                )"
,"                              ("
,"                                This0::List list000000=This0::List._new(head:cell0100000)"
,"                                cell0100000"
,"                                )"
,"                              )"
,"                            cell200000"
,"                            )"
,"                          ("
,"                            This0::List::CellNext cellNext00000=This0::List::CellNext.#apply(elem:{'@exitStatus"
,"    '0"
,"    }, next:cell30000)"
,"                            cellNext00000"
,"                            )"
,"                          )"
,"                        ("
,"                          This0::List list10000=This0::List._new(head:cell000000)"
,"                          cell000000"
,"                          )"
,"                        )"
,"                      cell40000"
,"                      )"
,"                    ("
,"                      This0::List::CellNext cellNext1000=This0::List::CellNext.#apply(elem:{ b()}, next:cell1000)"
,"                      cell1000"
,"                      )"
,"                    )"
,"                  cell1"
,"                  )"
,"                cell0"
,"                )"
,"              cell"
,"              )"
,"            ("
,"              This0::List list=This0::List._new(head:cell2)"
,"              list"
,"              )"
,"            )"
,"          result0"
,"          ).pop().top()"
,"        void"
,"        )"
,"      catch return result ("
,"        on Library result"
,"        )"
,"      error void"
,"      )}"
);
*/


/*
  @Test()
  public void testStress() {
    TestHelper.configureForTest();
    System.out.println("Start");
    Expression parsed = Parser.parse(null,this.codeLong);
    System.out.println("Parsed");
    Expression desugared = Desugar.of(parsed);
    System.out.println("Desugared");
    TestHelper.assertEqualExp(parsed, desugared);
    ClassB cb1=(ClassB)desugared.accept(new InjectionOnCore());
    System.out.println("Injected");
    Set<Path> needed=new HashSet<Path>();
    needed.add(Path.parse("This0::Bool"));
    Program p=Program.empty();
    cb1=TypeExtraction.etFull(p,cb1);
    p=p.addAtTop(cb1);
    //ExtractTypeStep.annotateAllLevels(p, needed);
    //NO//p.updateTop(p.top().withStage(Stage.Star));
    System.out.println("ReadyToCheck");
    TypeSystemOK.checkAll(p);
    System.out.println("Checked");
    new SmallStep().step(Program.empty(), cb1);
    }
*/

  @Test
    public void test3() throws IOException{
      TestHelper.configureForTest();
      Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"List:{_new(Cell head)"
,"  class method List () (List._new(head:CellEnd()))"
,"  method List #add(Library that) ("
,"    List._new(head:CellNext("
,"      elem:that,"
,"      next:this.head()))"
,"      )"
,"  class method This0 #begin() This0()"
,"  method List #end()this"
,"  method Library top(){"
,"    exception this.head()"
,"    catch exception x ("
,"      on CellNext return x.elem()"
,"      on Cell error void"
,"      )"
,"    }"
,"  method List pop(){"
,"    exception this.head()"
,"    catch exception x ("
,"      on CellNext return List._new(head:x.next())"
,"      on Cell error void"
,"      )"
,"    }"
,"  Cell:{interface}"
,"  CellEnd:{() implements Cell}"
,"  CellNext:{(Library elem, Cell next) implements Cell}"
,"  }"
,"C:{"
,"  l=List[{a()};ExitCode.normal();ExitCode.failure();{b()};]"
,"  return l.pop().pop().top()'ok, the stack start from the end!"
,"  }"
,"}"
)).getErrCode(),0);}

  @Test
  public void test4() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"List:{mut _new(mut Cell head)"
,"  class method mut List () (List._new(head:CellEnd()))"
,"  mut method mut List #add(Library that) ("
,"    List._new(head:CellNext("
,"      elem:that,"
,"      next:this.#head()))"
,"    )"
,"  class method mut This0 #begin() This0()"
,"  mut method mut This0 #end()this"
,"  mut method Library top(){"
,"    return this.#head()'test that with return non si puo' cambiare il tipo!"
,"    catch return x ("
,"      on  mut CellNext return x.elem()"
,"      on  mut Cell error void"
,"      )"
,"    }"
,"  mut method mut List pop(){"
,"    return this.#head()"
,"    catch return x ("
,"      on mut CellNext return List._new(head:x.#next())"
,"      on mut Cell error void"
,"      )"
,"    }"
,"  Cell:{interface}"
,"  CellEnd:{mut() implements Cell}"
,"  CellNext:{mut(var Library elem, mut Cell next) implements Cell}"
,"  }"
,"C:{"
,"  l=List[{a()};ExitCode.normal();ExitCode.failure();{b()};]"
,"  return l.pop().pop().top()'ok, the stack start from the end!"
,"  }"
,"}"
)).getErrCode(),0);}

  @Test
  public void test5() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"List:{mut _new(mut Cell head)"
,"  class method mut List () (List._new(head:CellEnd()))"
,"  mut method mut List #add(Library that) ("
,"    List._new(head:CellNext("
,"      elem:that,"
,"      next:this.#head()))"
,"    )"
,"  class method mut This0 #begin() This0()"
,"  mut method mut This0 #end()this"
,"  mut method Library top(){"
,"    return this.#head()'test that with return non si puo' cambiare il tipo!"
,"    catch return x ("
,"      on  mut CellNext return x.elem()"
,"      on  mut Cell error void"
,"      )"
,"    }"
,"  mut method Void set(Library that){"
,"    return this.#head()"
,"    catch return x ("
,"      on  mut CellNext return x.elem(that)"
,"      on  mut Cell error void"
,"      )"
,"    }"
,"  mut method mut List pop(){"
,"    return this.#head()"
,"    catch return x ("
,"      on mut CellNext return List._new(head:x.#next())"
,"      on mut Cell error void"
,"      )"
,"    }"
,"  Cell:{interface}"
,"  CellEnd:{mut() implements Cell}"
,"  CellNext:{mut(var Library elem, mut Cell next) implements Cell}"
,"  }"
,"C:{"
,"  l=List[{a()};{b()};{c()};]"
,"  l.pop().set(ExitCode.normal())"
,"  return l.pop().top()"
,"  }"
,"}"
)).getErrCode(),0);}




  @Test
  public void test6() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"    C:{"
,"      if 5N+3N==8N (return ExitCode.normal())"
,"      return ExitCode.failure()"
,"      }"
,"    }"
)).getErrCode(),0);}


//in superSlow
  //@Test
  public void test7() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"  C:{"
,"  t1=5N>2N"
,"  t2=5N>=2N"
,"  t3=2N<5N"
,"  t4=2N<=5N"
,"  t5=2N!=5N"
,"  t6=5N+3N==8N"
,"  t7=5N-3N==2N"
,"  t8=5N*3N==15N"
,"  t9=5N/3N==1N"
,"  if t1 & t2 & t3 & t4 & t5 &t6 &t7 &t8 &t9  (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }}"
)).getErrCode(),0);}

  @Test
  public void test8() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"Id:{() class method Library id(Library that) (that)}"
,"D:"
,"Id.id("
,"{()"
,"  class method Library b(){return ExitCode.normal()}"
,"  })"
,"C:{"
,"  var s=S\"foo\""
,"  s++=S\"bar\" 'use ++ for string contatenation?"
,"  use Alu check stringDebug(s.that()) void"
,"  return D.b()"
,"  }"
,"}"
)).getErrCode(),0);
  Assert.assertEquals(L42.record.toString(),"foobar\n");
  L42.record=new StringBuilder();
  }


  @Test
  public void test9() throws IOException{
    TestHelper.configureForTest();
    Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"    D:"
,"    use IntrospectionPlugin check sumLib("
,"      l1:{()"
,"        class method Library a(){"
,"          return this.b()'tried also D.b()"
,"          }"
,"        class method Library b()"
,"        }"
,"      l2:{"
,"        class method Library b(){return ExitCode.normal()}"
,"        }"
,"      ) error void"
,""
,"    C: ( D.a() )"
,"    }"
)).getErrCode(),0);}

@Test
public void test10() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"D:"
,"use IntrospectionPlugin check adaptLib("
,"  l1:{()"
,"    T:{}"
,"    class method T id(T that) (that)"
,"    }"
,"  l2:{"
,"    T:{'@Library\n}"
,"    }"
,"  ) error void"
,"C: D.id(ExitCode.normal())"
,"}"
)).getErrCode(),0);}



@Test
public void test11() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"A:{()"
,"  class method Library foo() ("
,"  {()"
,"    T:{()}"//path not star???
,"    Foo:{() class method Foo new() ( Foo() )}"
,"    class method T id(T that, Foo foo) (that)"
,"    })}"
,"D: A.foo()"
,"C: ExitCode.normal()"
,"}"
)).getErrCode(),0);}


@Test
public void test12() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"LibList: Collections.list(Library)"
//,"Lib2:Collections.list(LibList)"
,"C:{"
//,"  l=Lib2[LibList[{a()};{b()};];LibList[];]"
//,"  l=LibList[{a()};{b()};]"
,"  if False() (return ExitCode.failure())"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);}


@Test
public void test12a() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"LibList: Collections.list(Library)"
,"Lib2:Collections.list(LibList)"
,"C:{"
,"  l=Lib2[LibList[{a()};{b()};];LibList[];]"
,"  if False() (return ExitCode.failure())"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);}


@Test
public void test14() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"A: use IntrospectionPlugin check adaptLib("
,"    l1:{() B:{() class method Bool foo(Bool x) !x}}"
,"    l2:{B:{ method Void foo(Void x) this.bar(y:x) method Void bar(Void y)}}"
,"    ) error void"
,"C:{"
,"  if A::B.bar(y:False()) ( return ExitCode.normal()  )"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);}
/*
@Test
public void test15() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"A:{(As as)}"
,"As:Collections.list(A)"
,"C: ExitCode.normal()"
,"}"
)).getErrCode(),0);}
*/
@Test(expected=ErrorMessage.ProgramExtractOnWalkBy.class)
//need to be handled and explained in error reporting...
public void testAttemptUseCircular() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
//,"{reuse L42.is/microBase"
,"{"
,"A:{(As as) }"
,"As:A.as().foo()"
,"}"
)).getErrCode(),0);}

//unuseful
/*@Test
public void testMiniBase_() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase_"
,"C:( x=Name\"T\".isInternalPathName()    ExitCode.normal())"
,"}"
)).getErrCode(),0);}
*/

@Test
public void testTinyBase0() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/tinyBase0"
,"C:( x=Name\"T\".isInternalPathName()    ExitCode.normal())"
,"}"
)).getErrCode(),0);}

@Test
public void testTinyBase1() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/tinyBase1"
,"C: ExitCode.normal()"
,"}"
)).getErrCode(),0);}


@Test
public void testMiniBase() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C: ExitCode.normal()"
,"}"
)).getErrCode(),0);}

@Test
public void testOnCase1() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  class method N max( NList that) {"
,"  var result=that.top()"
,"  with current in that.vals() ( on N case current>result  result:=current )"
,"  return result"
,"    }' should work also without on Any. when there is any, what happens to mutability?"
,"     ' why the compiler give an error about the iterator?"
,"  }"
,"RunExercise1:{ return ExitCode.normal() }"
,"}"
      )).getErrCode(),0);}

@Test
public void testOnCase0a() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
," Main:( with current=True() ( on Bool void )"
,"   ExitCode.normal() )"
,"}"
      )).getErrCode(),0);}

@Test
public void testOnCase0b() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
," Main:( with current=ExitCode ( on class ExitCode void )"
,"   ExitCode.normal() )"
,"}"
      )).getErrCode(),0);}


@Test
public void testOnCase0() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
," Main:{ var current=True() with current ( on True current:=False() )"
,"  if current ( return ExitCode.failure()) "
,"  return ExitCode.normal() }"
,"}"
      )).getErrCode(),0);}


@Test
public void testOnCase1a() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  class method Void max( NList that) "
,"    with current in that.vals() ("
,"      with current ( "
,"         on N void"
,"         default void))"
,"  }"
,"RunExercise1:ExitCode.normal()"
,"}"
      )).getErrCode(),0);}

@Test
public void testOnCase1b() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  class method Void max( NList that) "
,"  with current in that.vals() ( on N void )"
,"  }"
,"RunExercise1:ExitCode.normal()"
,"}"
      )).getErrCode(),0);}


@Test
public void testOnCase1c() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  class method N max( NList that) {"
,"  var result=that.top()"
,"  with current in that.vals() ( on Any case current>result  result:=current )"
,"  return result"
,"    }' should work also without on Any. when there is any, what happens to mutability?"
,"     ' why the compiler give an error about the iterator?"
,"  }"
,"RunExercise1:{ return ExitCode.normal() }"
,"}"
      )).getErrCode(),0);}


@Test
public void testOnCase2() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  class method N max( NList that) {"
,"  var result=that.top()"
,"  with current in that.vals() (  case current>result  result:=current )"
,"  return result"
,"    }"//what happen if the elements in the collection are mut?
,"  }"
,"RunExercise1:{ return ExitCode.normal() }"
,"}"
      )).getErrCode(),0);}

@Test
public void testOnCase2a() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase1"
,"Foo: ("
,"Any that=void"
,"{"
,"with that ("
,"    on Void return ExitCode.normal()"
,"    default return ExitCode.failure()"
,"    )}"
,")}"
)).getErrCode(),0);}

@Test
public void test17() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
//,"{reuse L42.is/tinyBaseCut"
,"C:{"
,"  Library succ={"
,"    method N one()"
,"    method N succ(N that) that+this.one()"
,"    }"
,"  Library pred={"
,"    method N one()"
,"    method N pred(N that) that-this.one()"
,"    }"
,"  return Use[succ;pred;]<{()  method N one() 1N}"
,"  }"
,"Main:{"
,"  c=C()"
,"  if c.succ(3N)==4N ("
,"    return ExitCode.normal()"
,"    )"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);}



@Test
public void test17Old() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/tinyBaseOld"
//,"{reuse L42.is/tinyBaseCut"
,"C:{"
,"  Library succ={"
,"    method N one()"
,"    method N succ(N that) that+this.one()"
,"    }"
,"  Library pred={"
,"    method N one()"
,"    method N pred(N that) that-this.one()"
,"    }"
,"  return Use[succ;pred;]<{()  method N one() 1N}"
,"  }"
,"Main:{"
,"  c=C()"
,"  if c.succ(3N)==4N ("
,"    return ExitCode.normal()"
,"    )"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);}

@Test
public void test17mini() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
//,"{reuse L42.is/tinyBaseCut"
,"{reuse L42.is/miniBase"
,"C:Opt(N)"
,"Main: ExitCode.normal()"
,"}"
)).getErrCode(),0);}


@Test
public void test18() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"'C:Adapt[Name\"foo\", of:Name\"This0\", into:Name\"bar\";]<{() method N foo() 42N }"
,"C:Adapt[Name\"foo\", into:Name\"bar\";]<{() method N foo() 42N }"
,""
,"Main:{"
,"  if C().bar()==42N ("
,"    return ExitCode.normal()"
,"    )"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);}


@Test
public void testDeepMakePrivate() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"C:Adapt[Name\"Bar\", into:Name\"This0\";Name\"Bar::Foo\" into:Name\"Bar::Foo1\"]<{class method N top() Bar::Foo.foo() Bar:{Foo:{ class method N foo() 42N }}}"
,""
,"Main:{"
,"  if C.top()==42N ("
,"    return ExitCode.normal()"
,"    )"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);}


@Test
public void testDeepMakePrivate3() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"Vector:{"
,"  class method Library vector(class Any that)"
,"    Adapt["
,"      Name\"Elem\" into: Name(that);"
,"      Name\"VectorStruct\" into:Name\"This0\";"
//,"      makePrivate:Name\"VectorStruct::Cell\";"
,"      ]<{"
,"      Elem:{interface}"
,"      Kind:{ class method Elem elem() error S\"InternalUseOnly\" }"
,"      VectorStruct:{ class method Kind::elem() fuffa() this.fuffa() "
        //+ "Cell:{class method N foo() 42N}"
        + "}"
,"      }"
,"  }"
,""
,"ListArray:("
,"  Debug(S\"InListArray\")"
,"  x=Vector.vector(S)"
,"  Debug(S\"computed\")"
,"  ExitCode.normal()"
,"  )"
,"}"
)).getErrCode(),0);}


@Test
public void testMiniBaseExists() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"Main:ExitCode.normal()}"
)).getErrCode(),0);}


@Test
public void testStringConcat1() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"C:{"
,"  var s=S\"foo\""
,"  s++=s"
,"  s++=s"
,"  use Alu check stringDebug(s.that()) void"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"foofoofoofoo\n");
L42.record=new StringBuilder();
}

@Test
public void testStringConcat2() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/microBase"
,"C:{"
,"  var s=S\""
,"  'foo"
,"  \""
,"  s++=s"
,"  s++=s"
,"  use Alu check stringDebug(s.that()) void"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
Assert.assertEquals(L42.record.toString(),"foo\nfoo\nfoo\nfoo\n\n");
L42.record=new StringBuilder();
}


@Test
public void testAccumulator1() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"NList:Collections.list(N)"
,"Exercise1:{()"
,"  Accumulator:..."
,"  Accumulate:Accumulator(itType:NList::Iterator,elemType:N,"
,"    better:{"
,"      class  method"
,"      N (N a, N b){ if b>a (return b) return a }})"
//,"Exercise1:{()"
//,"  class method N max6(NList that) Accumulate(that.vals())"
,"  class method N max6(mut NList::Iterator iter) Accumulate(iter)"
//," 'class method N max6(NList that){ return Accumulate(that.vals()) }"
,"  }"
,"RunExercise1:{"
,"  N res=Exercise1.max6(iter:NList[1N;2N].vals())"
,"  return ExitCode.normal()"
,"  }"
,"}"
)).getErrCode(),0);
}

@Test(expected=ErrorMessage.PathsNotSubtype.class)
//TODO: it would be then wrapped as IncompleteClassRequired in the error reporting
public void testMissingMethod() throws IOException{
  TestHelper.configureForTest();
  Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/nanoBase0"
,"A:{()"
,"  class method Bool a()"
,"  }"
,"C:{"
,"  if A.a() (return ExitCode.normal())"
,"  return ExitCode.failure()"
,"  }"
,"}"
)).getErrCode(),0);
}


@Test
public void testVectorStruct() throws IOException{
TestHelper.configureForTest();
Assert.assertEquals(L42.runSlow(null,TestHelper.multiLine(""
,"{reuse L42.is/miniBase"
,"Vector:{"
,"  class method Library vector(class Any that)"
,"    Adapt["
//      Name"Elem" into: Name(that);"
,"      Name\"VectorStruct\" into:Name\"This0\";"
//      makePrivate:Name"VectorStruct::Cell";"
//      makePrivate:Name"VectorStruct::CellNext";"
//      makePrivate:Name"VectorStruct::CellEnd";"
//      makePrivate:Name"VectorStruct::Iterator";"
//      makePrivate:Name"VectorStruct::VarIterator";"
,"      ]<{"
,"      Elem:{interface}"
,"      Kind:'@private"
,"      {"
,"      class method"
,"      Elem elem() error void"
,"      class method"
,"      Elem elemRead() error void"
,"      }"
,"      VectorStruct:..."
,"      }"
,"  }"
,"ListArray:("
,"  Debug(S\"InListArray\")"
,"  x=Vector.vector(S)"
,"  Debug(S\"computed\")"
,"  x"
,"  )"
,"C:ExitCode.normal()"
,"}"
)).getErrCode(),0);
}


}
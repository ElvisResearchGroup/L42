package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.common.Program.InvalidImplements;
import is.L42.common.Program.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.tools.AtomicTest;
import is.L42.top.Top;
import is.L42.visitors.FullL42Visitor;
import is.L42.visitors.WellFormedness.NotWellFormed;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class TestTopNorm
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
//WellFormedness
   top("{}","{#norm{}}")
   ),new AtomicTest(()->
   top("{C={}}","{C={#norm{}}#norm{}}")
   ),new AtomicTest(()->
   top("{method Any foo()=void}","{method Any foo()=void #norm{}}")

   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{[I], I={interface }}",Err.nestedClassesImplemented(hole))
   ),new AtomicTest(()->
   topFail(InvalidImplements.class,"{A={[I]} J={interface method This m()} I={interface [J] method This m()}}",Err.nestedClassesImplemented(hole))

   ),new AtomicTest(()->
   top("{[I]}A={I={interface } A={} }","{[This1.I] #norm{typeDep=This1.I}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface } I={interface [J]} A={} }","{[This1.I,This1.J] #norm{typeDep=This1.I,This1.J}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface method This m()} I={interface [J]}A={}}","{[This1.I,This1.J] method This1.J m() #norm{typeDep=This1.I,This1.J}}")
   ),new AtomicTest(()->
   top("{[I]}A={J={interface method This m()} I={interface [J] method This m()}A={}}","{[This1.I,This1.J] method This1.I m() #norm{typeDep=This1.I,This1.J}}")

   ),new AtomicTest(()->
   top(
   "{J={interface method This m()} I={interface [J] method This m()} A={[I]} }",
   """
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I, This1.J]imm method imm This1.I m()#norm{typeDep=This1.I, This1.J}}
   #norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   A={[I1,I2]} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This1.I1 m()#norm{typeDep=This1.I1,This1.I2,This1.J}}#norm{}}
   """)

   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   A={[I1,I2] method m()=this} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   A={[This1.I1,This1.I2 This1.J]imm method imm This1.I1 m()=this #norm{typeDep=This1.I1,This1.I2,This1.J}}#norm{}}
   """)
   ),new AtomicTest(()->top("""
   {J={interface method This m()}
   I1={interface [J] method This m()}
   I2={interface [J] method This m()}
   B={C={A={[I1,I2] method m()=this}}} }
   ""","""
   {J={interface imm method imm This0 m()#norm{typeDep=This0}}
   I1={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   I2={interface[This1.J]imm method imm This0 m()#norm{typeDep=This1.J, This0}}
   B={C={A={[This3.I1,This3.I2 This3.J]imm method imm This3.I1 m()=this #norm{typeDep=This3.I1,This3.I2,This3.J}}#norm{}}#norm{}}#norm{}}
   """)


  ));}
private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";

public static void top(String program,String out){
  assertEquals(new Top().top(L(),Program.parse(program)).p().top,Core.L.parse(out));
  }
public static void topFail(Class<?> kind,String program,String ...output){
  checkFail(()->new Top().top(L(),Program.parse(program)), output, kind);
  }

}
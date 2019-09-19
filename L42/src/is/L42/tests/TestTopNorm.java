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
   top("{[I], I={interface }}","{[I], I={interface #norm{}}#norm{}}")//TODO: add error in init (and remove the todo in file 3)
   ),new AtomicTest(()->
   top("{[I]}A={A={} I={interface }}","{[This1.I] #norm{}}")
   ),new AtomicTest(()->
   top("{[I]}A={A={} J={interface } I={interface [J]}}","{[This1.I,This1.J] #norm{}}")
   ),new AtomicTest(()->
   top("{[I]}A={A={} J={interface method This m()} I={interface [J]}}","{[This1.I,This1.J] method This1.J m() #norm{}}")

  ));}
private static String emptyP="{#norm{}}{#norm{}}{#norm{}}{#norm{}}{#norm{}}";

public static void top(String program,String out){
  assertEquals(new Top().top(L(),Program.parse(program)).p().top,Core.L.parse(out));
  }
}
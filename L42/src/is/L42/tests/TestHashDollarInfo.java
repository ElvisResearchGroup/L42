package is.L42.tests;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.L42.common.Constants;
import is.L42.common.EndError;
import is.L42.common.Parse;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.X;
import is.L42.tools.AtomicTest;
import is.L42.top.HashDollarInfo;
import is.L42.visitors.FV;
import is.L42.visitors.FullL42Visitor;


import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.*;

public class TestHashDollarInfo
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   passL("{}",false,false)
   ),new AtomicTest(()->
   passL("{reuse [A]}",false,false)
   ),new AtomicTest(()->
   passL("{reuse [#$A]}",true,false)
   ),new AtomicTest(()->
   passL("{reuse [A] B=void}",false,false)
   ),new AtomicTest(()->
   passL("{reuse [#$A] B=void}",true,false)
   ),new AtomicTest(()->
   passL("{reuse [A] B=C.#$aa()}",false,true)
   ),new AtomicTest(()->
   passL("{reuse [#$A] B=C.#$aa()}",true,true)

   ),new AtomicTest(()->
   passNC("{B=C.#$aa()}",true,false)
   ),new AtomicTest(()->
   passNC("{B=C.#$aa(a={reuse[A]} b={C=Foo.bar()})}",true,false)
   ),new AtomicTest(()->
   passNC("{B=C.#$aa(a={reuse[#$A]} b={C=Foo.bar()})}",true,true)
   ),new AtomicTest(()->
   passNC("{B=C.#$aa(a={reuse[A]} b={C=Foo.#$bar()})}",true,true)
   ),new AtomicTest(()->
   passNC("{B=C.aa()}",false,false)
   ),new AtomicTest(()->
   passNC("{B=C.aa(a={reuse[A]} b={C=Foo.bar()})}",false,false)
   ),new AtomicTest(()->
   passNC("{B=C.aa(a={reuse[#$A]} b={C=Foo.bar()})}",false,true)
   ),new AtomicTest(()->
   passNC("{B=C.aa(a={reuse[A]} b={C=Foo.#$bar()})}",false,true)

  ));}
public static void passL(String input,boolean top,boolean inner){
  var r=Parse.e(Constants.dummy,input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  var info =new HashDollarInfo((Full.L)r.res);
  assertEquals(info.hashDollarTop,top);
  assertEquals(info.hashDollarInside,inner);
  }
public static void passNC(String input,boolean top,boolean inner){
  var r=Parse.e(Constants.dummy,input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  var nc=((Full.L)r.res).ms().get(0);
  var info =new HashDollarInfo((Full.L.NC)nc);
  assertEquals(info.hashDollarTop,top);
  assertEquals(info.hashDollarInside,inner);
  }
}
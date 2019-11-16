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
import is.L42.visitors.FV;
import is.L42.visitors.FullL42Visitor;


import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.range;
import static org.junit.jupiter.api.Assertions.*;

public class TestFV
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
   pass("foo","foo")
   ),new AtomicTest(()->
   pass("(var mut A x=y x)","y")
   ),new AtomicTest(()->
   pass("(x=(z x y) x)","y","z")
   ),new AtomicTest(()->
   pass("((a,b)=c b)","c")
   ),new AtomicTest(()->
   pass("(a=c d=c catch T x (x a) d)","c")
   ),new AtomicTest(()->
   pass("if Void x void","x")
   ),new AtomicTest(()->
   pass("if Void x=void void")
   ),new AtomicTest(()->
   pass("if Void x x","x")
   ),new AtomicTest(()->
   pass("if Void x=void x")
   
   ),new AtomicTest(()->
   passC("(capsule This0 a=c catch error This0 x a a)")
   ),new AtomicTest(()->
   failC("(capsule This0 a=c imm Void g=a catch error This0 x a a)")
   ),new AtomicTest(()->
   passC("(imm This0 a=c imm Void g=a catch error This0 x a a)")


  ));}
public static String inCore(String s){
  return "{imm method imm Void a()="+s+" #norm{}}";
  }
public static void pass(String input,String ...output){
  var r=Parse.e(Constants.dummy,input);
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  var fv=FV.of(r.res.visitable());
  var out=new HashSet<X>();
  for(var o:output){out.add(new X(o));}
  if(out.containsAll(fv)){return;}
  assertEquals(fv.toString(),out.toString());
  }
public static void failC(String input,String ...output){
  var r=Parse.e(Constants.dummy,inCore(input));
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  var c=(Core.L)r.res;
  var e=c.mwts().get(0)._e();
  try{FV.of(e.visitable());}
  catch(EndError.NotWellFormed nwf){
    String msg=nwf.getMessage();
    for(var s:output){if(!msg.contains(s)){throw nwf;}}
    for(var s:output){assertTrue(msg.contains(s));}
    }
  }
public static void passC(String input){
  var r=Parse.e(Constants.dummy,inCore(input));
  assert !r.hasErr():r.errorsParser+" "+r.errorsTokenizer+" "+r.errorsVisitor;
  var c=(Core.L)r.res;
  var e=c.mwts().get(0)._e();
  FV.of(e.visitable());// may throw NotWellFormed
  }
}
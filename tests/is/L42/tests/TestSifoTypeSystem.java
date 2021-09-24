package is.L42.tests;

import static is.L42.tests.TestHelpers.checkFail;
import static is.L42.tools.General.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import is.L42.common.EndError;
import is.L42.common.EndError.CoherentError;
import is.L42.common.EndError.TypeError;
import is.L42.common.ErrMsg;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.flyweight.C;
import is.L42.flyweight.P;
import is.L42.platformSpecific.inMemoryCompiler.JavaCodeStore;
import is.L42.platformSpecific.javaTranslation.L42£Library;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.sifo.SifoTopTS;
import is.L42.tools.AtomicTest;
import is.L42.top.CachedTop;
import is.L42.top.Init;
import is.L42.top.State;
import is.L42.typeSystem.ProgramTypeSystem;

public class TestSifoTypeSystem
extends AtomicTest.Tester{
public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->
//---------------
  pass(testMeth("class method @Left A main(@Left A a)=a"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Right A a)=a"),
     SifoTopTS.notSubErr("[###]","[###]"))
//------------------
   ),new AtomicTest(()->
   pass(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Right A x=a that)"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Right A x=a a)"),
     SifoTopTS.notSubErr("[###]","[###]"))
//------------------
   ),new AtomicTest(()->
   pass(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Left A x=this.main(that,a=a),x)"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Right A x=this.main(that,a=a),that)"),
     SifoTopTS.notSubErr("[###]","[###]"))
//------------------
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Left @Right A that, @Right A a)=(@Right A x=a that)"),
       SifoTopTS.moreThanOneAnnotationErr("[###]"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left @Right A main(@Right A that, @Right A a)=(@Right A x=a that)"),
       SifoTopTS.moreThanOneAnnotationErr("[###]"))
   ),new AtomicTest(()->
   fail(testMeth("imm @Left @Right method @Left A main(@Right A that, @Right A a)=(@Right A x=a that)"),
       SifoTopTS.moreThanOneAnnotationErr("[###]"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left @Right A main(@Right A that, @Right A a)=(@Right @Left A x=a that)"),
       SifoTopTS.moreThanOneAnnotationErr("[###]"))
   ),new AtomicTest(()->
   pass(testMeth("class method @Left A main(@Left A that, @Right A a)=this.main(that,a=a)"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Left A that, @Right A a)=this.main(that,a=that)"),
     SifoTopTS.notSubErr("[###]","[###]"))
   ),new AtomicTest(()->
   fail(testMeth("class method @Left A main(@Left A that, @Right A a)=this.main(a,a=a)"),
     SifoTopTS.notSubErr("[###]","[###]"))
   ),new AtomicTest(()->
   pass(testMeth("class method @Top A main(@Left A that, @Right A a)=this.main(that,a=a)"))
   ),new AtomicTest(()->
   pass(testMeth("class method @Top A main(@Left A that, @Right A a)=this.main(that,a=that)"))
   ),new AtomicTest(()->
   pass(testMeth("class method @Top A main(@Left A that, @Right A a)=this.main(a,a=a)"))   
   ),new AtomicTest(()->
   pass(testMeth("class method @Top A main(@Left A that, @Right A a)=this.main(a,a=that)"))   
   ),new AtomicTest(()->
   pass(testMeth("class method @Top A main(mut @Left A that, mut @Right A a)=this.main(that,a=a)"))   
   ),new AtomicTest(()->
   fail(testMeth("class method @Top A main(mut @Left A that, mut @Right A a)=this.main(a,a=that)"),
     SifoTopTS.notSubErr("[###]","[###]"))
   ),new AtomicTest(()->
   pass(testMeth("imm @Left method @Top A main(@Left A that, @Right A a)=this.main(a,a=that)"))   
   ),new AtomicTest(()->
   fail(testMeth("imm @Top method @Top A main(mut @Left A that, @Right A a)=this.main(that,a=a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //this is ok to pass as typing method call, but should fail on typing method declaration
   ),new AtomicTest(()->
   fail(testMeth("imm @Left method @Left A main(@Left A that, mut @Right A a)=this.main(that,a=a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //this is ok to pass as typing method call, but should fail on typing method declaration
   ),new AtomicTest(()->
   fail(testMeth("imm @Left method @Left A main(mut @Left A that, mut @Right A a)=this.main(that,a=a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //this should fail on typing method declaration right parameter is not higher than receiver
   ),new AtomicTest(()->
   pass(testMeth("imm @Left method @Left A main(mut @Left A that, @Right A a)=this.main(that,a=a)"))
   //this should pass on typing method declaration right parameter is imm
   ),new AtomicTest(()->
   fail(testMeth("imm @Top method @Left A main(@Left A that, @Right A a)=this.main(that,a=a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //this should fail on typing method declaration return is not higher than receiver
   ),new AtomicTest(()->
   pass(testMeth("class method mut @Left A main(mut @Left A that, @Right A a)=(mut @Left A x=this.main(that,a=a),that)"))
   //this should pass as mut is correctly assigned
   ),new AtomicTest(()->
   fail(testMeth("class method mut @Left A main(mut @Left A that, @Right A a)=(imm @Left A x=this.main(that,a=a),that)"), 
     ErrMsg.methCallNoCompatibleMdfParametersSignature("[###]", "[###]"))
   //fail, assigend mut to imm
   ),new AtomicTest(()->
   fail(testMeth("class method mut @Left A main(mut @Left A that, @Right A a)=(mut @Right A x=this.main(that,a=a),that)"),
       SifoTopTS.notEqualErr("[###]","[###]"))
   //fail, assigend mut left to mut right
   ),new AtomicTest(()->
   fail(testMeth("class method mut @Left A main(mut @Left A that, mut @Right A a)=(mut @Top A x=this.main(that,a=a),that)"),
       SifoTopTS.notEqualErr("[###]","[###]"))
   //fail, assigend mut left to mut top
   ),new AtomicTest(()->
   fail(testMeth("class method mut @Left A main(mut @Left A that, mut @Right A a)=(mut @Left A x=this.main(that,a=a),a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //fail, return right but left is expected
   ),new AtomicTest(()->
   pass(testMeth("class method mut @Left A main(@Left A that, @Right A a)=(mut @Left A x=this.main(that,a=a),x)"))
   //pass, mut returned as mut
   ),new AtomicTest(()->
   pass(testMeth("class method mut @Left A main(capsule @Left A that, mut @Left A a)=(mut @Top A x=that,a)"))
   //pass, left capsule is promoted to mut top
   ),new AtomicTest(()->
   fail(testMeth("class method mut @Top A main(capsule @Left A that, mut @Left A a)=(mut @Top A x=this.main(that,a=a),a)"),
       SifoTopTS.notSubErr("[###]","[###]"))
   //fail, left cannot be returned as top
   ),new AtomicTest(()->
   pass(testMeth("class method mut @Top A main(capsule @Left A that, mut @Top A a)=(mut @Top A x=this.main(that,a=a),a)"))
   //pass, call correct and top returned
   ),new AtomicTest(()->
   pass(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Top A x=this.main(that,a=a),that)"))
   //pass, return left is promoted to top
   ),new AtomicTest(()->
   pass(testMeth("class method @Left A main(@Left A that, @Right A a)=(@Top A x=this.main(that,a=a),that)"))
   //pass, imm return promoted to top
   ),new AtomicTest(()->
   pass(testMeth("""
     class method @Left A main(@Left A that, @Right A a)=(
       A x=this.main(that,a=a),that)
     """))
   ),new AtomicTest(()->
   pass(testMeth("""
     class method Void err()[A]=void
     class method @Left A main(@Left A that, @Right A a)[A]
       =(this.err(),@Left A x=this.main(that,a=a),that)
     """))
   ),new AtomicTest(()->
   fail(testMeth("""
     class method Void err()[@Left A, @Right B]=void
     class method @Left A main(@Left A that, @Right A a)[@Left A, @Right B]
       =(this.err(),@Left A x=this.main(that,a=a),that)
     """), SifoTopTS.differentSecurityLevelsExceptionsErr("[###]"))
   ),new AtomicTest(()->
   fail(testMeth("""
     imm @Left method Void err()[@Right B]=void
     imm @Left method @Left A main(@Left A that, @Left A a)[@Right B]
       =(this.err(),@Left A x=this.main(that,a=a),that)
     """), SifoTopTS.notSubErr("[###]","[###]"))
 ),new AtomicTest(()->
 fail(testMeth("""
   class method Void err()[@Left A]=void
   class method @Left A main(@Left A that, @Right A a)[@Right A]
     =(this.err(),@Left A x=this.main(that,a=a),that)
   """),SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    I1={interface method @Left A foo(@Right B that)[@Top B]}
    C1={[I1] method @Right A foo(@Right B that)[@Top B]=this.foo(that)}
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    I1={interface method @Left A foo(@Right B that)[@Top B]}
    C1={[I1] method @Left A foo(@Left B that)[@Top B]=this.foo(that)}
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    I1={interface method @Left A foo(@Right B that)[@Left B]}
    C1={[I1] method @Left A foo(@Right B that)[@Right B]=this.foo(that)}
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    I1={interface method @Left A foo(@Right B that)[@Left B]}
    C1={[I1] method @Left A foo(@Right B that)[@Right B,@Left A]=this.foo(that)}
    """),
    ErrMsg.methSubTypeExpectedExc("[###]","[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    I1={interface imm @Left method @Top A foo(@Right B that)}
    C1={[I1] imm @Right method @Top A foo(@Right B that)=this.foo(that)}
    """), SifoTopTS.notEqualErr("[###]","[###]"))
  //receiver is changed, should throw an error
  ),new AtomicTest(()->
  fail(testMeth("""
   class method @Left A main(@Left A that, @Right A a)
     =(@Left A x=this.main(that,a=a) catch exception @Left A y (y) that)
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    class method @Left A main(@Left A that, A a)
      =(@Left A x=this.main(that,a=a) catch exception @Left A y (y) that)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Right method @Left A main(@Left A that, @Left A a)=that
    """),
    SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Left method @Right A main(@Left A that)
      =(@Right A x=this.main(that)  x)
    """),SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Right A main(@Left A that)
      =(@Right A x=this.main(that) catch exception @Right A y (y) x)
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  //the existence of the right exception tell us about the state of left
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Right A main(@Left A that)
      =(@Right A x=this.main(that) catch exception @Left A y (this.main(that)) x)
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm @Left method @Left A main(@Left A that)
      =(@Left A x=this.main(that) catch exception @Left A y1 (this.main(that)) x)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Left method @Left A main(@Left A that)
      =(@Left A x=this.main(that) catch exception @Right A y (this.main(that)) x)
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Left method @Left This main(@Right A a)
      =(var @Right A x=a
       (x:=a catch exception @Right A y (this) this))
    """),SifoTopTS.notSubErr("[###]","[###]"))
      ),new AtomicTest(()->
   pass(testMeth("""
     imm @Left method @Left Void main(@Right A a)
       =(var @Right A x=a
        (x:=a catch exception @Right A y (void) void))
     """))//passes with Void result
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Left method @Left Void main(@Left A that, @Right A a)
      =(var @Right A x=a
       (x:=a catch exception @Left A y (void) void))
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm @Right method @Top A main(@Left A that, @Right A a)
      =(var @Right A x=a
       (x:=a catch exception @Right A y (that) that))
    """),SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Left A main(@Left A that)
      =(@Left A x=this.main(that) catch exception @Right A y (this.main(that)) x)
    """),SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm method @Top A main(@Left A that)
      =(@Top A x=this.main(that) catch error @Top A y (y) x)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Top A main(@Left A that)
      =(@Top A x=this.main(that) catch error @Left A y (y) x)
    """),SifoTopTS.allMustTopErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm method @Top A main(@Left A that)
      =(@Top A x=this.main(that) catch exception @Left A y (y) x)
    """))//passes with exception but fails (above) with error
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method mut A main(mut @Left A that, mut A a)
      =(mut A x=this.main(that,a=a) catch exception @Left A y (a) a)
    """),SifoTopTS.differentSecurityLevelsVariablesErr("[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm method @Left A main(@Left A that)=(
      var @Left A x=that
      (x:=x
      x))
    """))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm method @Left A main(@Left A that)=(
      var @Left A x=that
      (x:=x
       catch exception @Left A a (x)
       x))
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Left A main(@Left A that, @Right A a)=(
      var @Left A x=that
      var @Right A y=a
      (x:=x  y:=y
       catch exception @Left A b (x)
       x))
    """), SifoTopTS.differentSecurityLevelsVariablesErr("[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Left A main(@Left A that, @Right A a)=(
      var @Left A x=that
      var @Right A y=a
      (x:=x
      @Right A z=y
       catch exception @Left A b (x)
       x))
    """),SifoTopTS.notSubErr("[###]","[###]"))
  //----------
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Top A main(@Top A top,@Left A that, @Right A a)=(
      var @Left A x=that
      var @Right A y=a
      (x:=x  y:=y
       catch exception @Top A b (top)
       top))
    """), SifoTopTS.differentSecurityLevelsVariablesErr("[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Top A main(@Top A top, @Left A that)=(
      var @Left A x=that
      (//top.op()//fails becouse this could be there
        x:=x//and this can leak information on the success of top.op()
       catch exception @Top A b (top)
       top))
    """),SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
    imm method @Top Void main(@Left A that, @Right A a)=(
      var @Left A x=that
      var @Right A y=a
      (y:=y
      //@Left A z=x
       catch exception @Top A b (void)
       void))
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method @Top Void main(@Left A that, @Right A a)=(
      var @Left A x=that
      var @Right A y=a
      (y:=y
      @Left A z=x
       catch exception @Top A b (void)
       void))
    """),SifoTopTS.notSubErr("[###]","[###]"))      
  ),new AtomicTest(()->
  fail(testMeth("""
    imm method A newATrue()=this.newATrue()
    imm method A newAFalse()=this.newAFalse()
    imm method A isTrue(A that)[A]=that
    imm method @Right A main(@Left A that)=(
      var @Right A y=this.newAFalse()
      @Top Void unused=(
        @Left A z=this.isTrue(that)
        catch exception @Top A b (void)
        y:=this.newATrue()
        void)
      y
      )
    """),SifoTopTS.notSubErr("[###]","[###]"))
    ),new AtomicTest(()->
      pass(testMeth("""
        mut method Void setEntry(A a)=void
        mut @Left method Void main(A a)=this.setEntry(a=a)
        """))//minimizing the test below
  ),new AtomicTest(()->
  pass(testMeth("""
    mut method Void setEntry(A a1, A a2, A a3)=void
    mut @Left method @Left Void main()=(
        p1=A()
        p2=A()
        p3=A()
        _=this.setEntry(a1=p1,a2=p2,a3=p3)
        void
      )
    """))//return of Void is promoted to Left and then returned. Trashing the result is not working
  ),new AtomicTest(()->
  pass(testMeth("""
    mut method Void setEntry(A a1, A a2, A a3)=void
    mut @Left method @Left Void main()=(
        p1=A()
        p2=A()
        p3=A()
        _=this.setEntry(a1=p1,a2=p2,a3=p3)
        void
      )
    """))
  ),new AtomicTest(()->
  pass(testMeth("""
    mut method Void setEntry(A a1, A a2, A a3)=void
    mut @Left method @Left Void main()=(
        p1=A()
        p2=A()
        p3=A()
        this.setEntry(a1=p1,a2=p2,a3=p3)
      )
    """))
  ),new AtomicTest(()->
  pass(testMeth("""
  mut @Left A aleft
  mut @Right A aright
  class method mut This newA(mut @Left A aleft, mut @Right A aright)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
  mut @Left A aleft
  mut @Right A aright
  class method mut This (mut @Right A aleft, mut @Left A aright)
    """), SifoTopTS.notEqualErr("[###]","[###]"))
  ),new AtomicTest(()->
  fail(testMeth("""
  mut @Left A aleft
  mut @Right A aright
  class method mut @Left This (mut @Right A aleft, mut @Left A aright)
    """), SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()-> //loop tests
  pass(testMeth("""
  read method Void #checkTrue()[Void]=void                                          
  imm @Left method @Left A main(@Left A that, @Left B b)=(  
    Void fresh0=( 
      loop(
        Void fresh2=b.#checkTrue() 
        void
        )
      catch exception Void fresh3 void
      )
    that)
    """))
  ),new AtomicTest(()->  
  pass(testMeth("""
  read method Void #checkTrue()[Void]=void                                          
  imm @Left method @Left A main(@Left A that, @Left B b)=(
      while b (
        void
        )
      that)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
  mut method Void #checkTrue()
  class method @Left A getLeft(@Left A a)=(a)                                      
  class method @Left A main(@Left A that, mut B b)=(
      while b (_=this.getLeft(a=that) void)
      that)
    """), SifoTopTS.notSubErr("[###]","[###]"))
  ),new AtomicTest(()->
  pass(testMeth("""
  mut method Void #checkTrue()
  imm @Left method @Left A getLeft(@Left A a)=(a)                                      
  imm @Left method @Left A main(@Left A that, mut @Left B b)=(
      while b (_=this.getLeft(a=that) void)
      that)
    """))
  ),new AtomicTest(()-> //example where the guard is low
  pass(testMeth("""
  read method Void #checkTrue()
  imm @Left method @Left A getLeft(@Left A a)=(a)                                      
  imm @Left method @Left A main(@Left A that, B b)=(
      while b (_=this.getLeft(a=that) void)
      that)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
  read method Void #checkTrue()
  class method @Left A main(@Left A that, mut B b)=(
      while b (_=this.main(that, b=b) void)
      that)
    """), SifoTopTS.notSubErr("[###]","[###]"))
  //  consider if we where to call main2 instead class method @Left A main2(@Left A that, mut B b)[Void]
  //  in this case it must fail or be unsound (b could count rounds and that could chose to throw or not)
  ),new AtomicTest(()->
  pass(testMeth("""
  read method Void #checkTrue()                                    
  imm @Left method @Left A main(A that, mut @Left B b)=(
      while b (_=this.main(that, b=b) void)
      that)
    """))
  ),new AtomicTest(()->
  pass(testMeth("""
  read method Void #checkTrue()                                    
  class method @Left A main(A that, mut @Left B b)=(
      while b (_=this.main(that, b=b) void)
      that)
    """))
  ),new AtomicTest(()->
  pass(testMeth("""
  mut method Void #checkTrue()                                          
  class method @Left A main(@Left A that, mut @Left B b)=(
      var @Left A a = that
      while b (a:=that void)
      that)
    """))
  ),new AtomicTest(()->
  fail(testMeth("""
  mut method Void #checkTrue()                                          
  class method @Left A main(A that, mut @Left B b)=(
      var A a = that
      while b (a:=that void)
      that)
    """), SifoTopTS.differentSecurityLevelsVariablesErr("[###]"))
  //TODO: tests with loops above
  ));}

public static void pass(String program){
  Resources.clearResKeepReuse();
  class StateForTest extends State{
    public StateForTest(FreshNames f,ArrayList<HashSet<List<C>>>c, int u,JavaCodeStore b,ArrayList<L42£Library> l){
      super(f,c,u,b,l);
      }
    @Override protected Program flagTyped(Program p) throws EndError{return p;}
    @Override public State copy(){
      return new StateForTest(freshNames.copy(),copyAlreadyCoherent(),
        uniqueId,allByteCode.next(),new ArrayList<>(allLibs));
      }
    }
  Init init=new Init("{"+program+"}"){
    @Override protected State makeState(){
      return new StateForTest(f,new ArrayList<>(),0,new JavaCodeStore(),new ArrayList<>());
      }
    };
  Program p=Program.flat(init.topCache(new CachedTop(L(),L())));
  ProgramTypeSystem.type(true, p);
  TestTypeSystem.allCoherent(p);
  var top=P.NCs.of(0,List.of(C.of("A",-1),C.of("Top",-1)));
  new SifoTopTS(p,top).visitL(p.topCore());//a visitor
  }
public static void failC(String program,String...out){
  checkFail(()->{
    pass(program);
    }, out, CoherentError.class);  }

public static void fail(String program,String...out){
  checkFail(()->{
    pass(program);
    }, out, TypeError.class);
  }
public static String testMeth(String s){
  return """
      A={
      Left={interface @{securityLevel}}
      Right={interface@{securityLevel}}
      Top={interface [Left,Right] @{securityLevel}}
      W={
        A={class method This()}
        B={
      """+s+"""
          }
        }       
      }
      """;
  }
}
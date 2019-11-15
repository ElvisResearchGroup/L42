package is.L42.tests;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import is.L42.common.CTz;
import is.L42.common.EndError;
import is.L42.common.Err;
import is.L42.common.Parse;
import is.L42.common.Program;
import is.L42.constraints.FreshNames;
import is.L42.common.EndError.InvalidImplements;
import is.L42.common.EndError.PathNotExistent;
import is.L42.generated.Core;
import is.L42.generated.Full;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.platformSpecific.javaTranslation.Resources;
import is.L42.tools.AtomicTest;
import is.L42.top.Init;
import is.L42.top.Top;
import is.L42.translationToJava.Loader;
import is.L42.visitors.FullL42Visitor;

import static is.L42.tests.TestHelpers.*;
import static is.L42.tools.General.L;
import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;
import static is.L42.common.Err.hole;

import static org.junit.jupiter.api.Assertions.*;

public class DeployAdamTowel
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(new AtomicTest(()->

























































//formatting up to line 100 so the error lines are easier to figure out.
//somehow the parser have trouble counting empty lines as lines!?

top("""
//first, the minimal infrastructure to deploy a towel
S={#norm{nativeKind=String}}
Debug={
  class method This0 of()        
  method Void deployLibrary(This1.S that,Library lib)=
    native{trusted:deployLibrary} error void
    #norm{declaresClassMethods, typeDep=This0, This1.S, nativeKind=TrustedIO}        
  }
SB={
  class method mut This0 of()
  mut method Void _a()=
    native{trusted:'a'} error void
  read method This1.S toS()=
    native{trusted:toS} error void
    #norm{declaresClassMethods, typeDep=This0, This1.S, nativeKind=StringBuilder}
  }
DeployAAA=(
  mut SB sb=SB.of()
  sb._a()sb._a()sb._a()
  lib={
//now, deploying aaa: Strings, booleans and Size
    B={
      class method This0 false()
      class method This0 true()=(This false=this.false() false.not())
      method This0 #if()=this
      method Void #checkTrue()[Void]=native{trusted:checkTrue} error void
      method This0 not()=native{trusted:OP!} error void
      method This0 and(This0 that)=native{trusted:OP&} error void
      method This0 or(This0 that)=native{trusted:OP|} error void
      read method This1.S toS()=native{trusted:toS} error void
      #norm{declaresClassMethods, typeDep=This0, This1.S, nativeKind=Bool}
      }
    Size={
      class method This0 zero()
      method This0 #plus0(This0 that)=native{trusted:OP+} error void
      read method This1.S toS()=native{trusted:toS} error void
      class method This0 #from(This1.SB stringLiteral)=(
        This1.S s=stringLiteral.toS()
        s.toInt()
        )
      #norm{declaresClassMethods, typeDep=This0, This1.S, This1.SB nativeKind=Int}
      }
    S={
      method This0 #plusplus0(This0 that)=native{trusted:OP+} error void
      imm method This0 toS()=this//TODO: the mdf imm would not work if we implement a read method S toS from an interface
      read method This1.Size toInt()=native{trusted:toInt} error void
      method This0 sum(This0 that)=native{trusted:OP+} error void
      class method This0 #from(This1.SB stringLiteral)=stringLiteral.toS()
      #norm{declaresClassMethods, typeDep=This0, This1.Size, This1.SB, nativeKind=String}
      }
    Debug={
      class method Void #apply(This1.S that)=(
        This d=This<:class This.of() 
        d.strDebug(that=that)
        )
      class method mut This0 of()        
      method Void strDebug(This1.S that)=
        native{trusted:strDebug} error void
      method Void deployLibrary(This1.S that,Library lib)=
        native{trusted:deployLibrary} error void
      #norm{declaresClassMethods, typeDep=This0, This1.S, nativeKind=TrustedIO}        
      }
    SB={
      mut method Void #d0()=native{trusted:'0'}error void
      mut method Void #d1()=native{trusted:'1'}error void
      mut method Void #d2()=native{trusted:'2'}error void
      mut method Void #d3()=native{trusted:'3'}error void
      mut method Void #d4()=native{trusted:'4'}error void
      mut method Void #d5()=native{trusted:'5'}error void
      mut method Void #d6()=native{trusted:'6'}error void
      mut method Void #d7()=native{trusted:'7'}error void
      mut method Void #d8()=native{trusted:'8'}error void
      mut method Void #d9()=native{trusted:'9'}error void

      mut method Void #lq()=native{trusted:'q'}error void
      mut method Void #lw()=native{trusted:'w'}error void
      mut method Void #le()=native{trusted:'e'}error void
      mut method Void #lr()=native{trusted:'r'}error void
      mut method Void #lt()=native{trusted:'t'}error void
      mut method Void #ly()=native{trusted:'y'}error void
      mut method Void #lu()=native{trusted:'u'}error void
      mut method Void #li()=native{trusted:'i'}error void
      mut method Void #lo()=native{trusted:'o'}error void
      mut method Void #lp()=native{trusted:'p'}error void
      mut method Void #la()=native{trusted:'a'}error void
      mut method Void #ls()=native{trusted:'s'}error void
      mut method Void #ld()=native{trusted:'d'}error void
      mut method Void #lf()=native{trusted:'f'}error void
      mut method Void #lg()=native{trusted:'g'}error void
      mut method Void #lh()=native{trusted:'h'}error void
      mut method Void #lj()=native{trusted:'j'}error void
      mut method Void #lk()=native{trusted:'k'}error void
      mut method Void #ll()=native{trusted:'l'}error void
      mut method Void #lz()=native{trusted:'z'}error void
      mut method Void #lx()=native{trusted:'x'}error void
      mut method Void #lc()=native{trusted:'c'}error void
      mut method Void #lv()=native{trusted:'v'}error void
      mut method Void #lb()=native{trusted:'b'}error void
      mut method Void #ln()=native{trusted:'n'}error void
      mut method Void #lm()=native{trusted:'m'}error void

      mut method Void #uQ()=native{trusted:'Q'}error void
      mut method Void #uW()=native{trusted:'W'}error void
      mut method Void #uE()=native{trusted:'E'}error void
      mut method Void #uR()=native{trusted:'R'}error void
      mut method Void #uT()=native{trusted:'T'}error void
      mut method Void #uY()=native{trusted:'Y'}error void
      mut method Void #uU()=native{trusted:'U'}error void
      mut method Void #uI()=native{trusted:'I'}error void
      mut method Void #uO()=native{trusted:'O'}error void
      mut method Void #uP()=native{trusted:'P'}error void
      mut method Void #uA()=native{trusted:'A'}error void
      mut method Void #uS()=native{trusted:'S'}error void
      mut method Void #uD()=native{trusted:'D'}error void
      mut method Void #uF()=native{trusted:'F'}error void
      mut method Void #uG()=native{trusted:'G'}error void
      mut method Void #uH()=native{trusted:'H'}error void
      mut method Void #uJ()=native{trusted:'J'}error void
      mut method Void #uK()=native{trusted:'K'}error void
      mut method Void #uL()=native{trusted:'L'}error void
      mut method Void #uZ()=native{trusted:'Z'}error void
      mut method Void #uX()=native{trusted:'X'}error void
      mut method Void #uC()=native{trusted:'C'}error void
      mut method Void #uV()=native{trusted:'V'}error void
      mut method Void #uB()=native{trusted:'B'}error void
      mut method Void #uN()=native{trusted:'N'}error void
      mut method Void #uM()=native{trusted:'M'}error void

      mut method Void #splus()=native{trusted:'+'}error void
      mut method Void #sless()=native{trusted:'-'}error void
      mut method Void #stilde()=native{trusted:'~'}error void
      mut method Void #sbang()=native{trusted:'!'}error void
      mut method Void #sand()=native{trusted:'&'}error void
      mut method Void #sor()=native{trusted:'|'}error void
      mut method Void #sleft()=native{trusted:'<'}error void
      mut method Void #sright()=native{trusted:'>'}error void
      mut method Void #sequal()=native{trusted:'='}error void
      mut method Void #stimes()=native{trusted:'*'}error void
      mut method Void #sdivide()=native{trusted:'/'}error void
      mut method Void #soRound()=native{trusted:'('}error void
      mut method Void #scRound()=native{trusted:')'}error void
      mut method Void #soSquare()=native{trusted:'['}error void
      mut method Void #scSquare()=native{trusted:']'}error void
      mut method Void #soCurly()=native{trusted:oCurly}error void
      mut method Void #scCurly()=native{trusted:cCurly}error void
      mut method Void #sdQuote()=native{trusted:'"'}error void
      mut method Void #ssQuote()=native{trusted:'''}error void
      mut method Void #shQuote()=native{trusted:'`'}error void
      mut method Void #sqMark()=native{trusted:'?'}error void
      mut method Void #shat()=native{trusted:'^'}error void
      mut method Void #scomma()=native{trusted:','}error void
      mut method Void #ssemicolon()=native{trusted:';'}error void
      mut method Void #scolon()=native{trusted:':'}error void
      mut method Void #sdot()=native{trusted:'.'}error void
      mut method Void #sunderscore()=native{trusted:'_'}error void
      mut method Void #shash()=native{trusted:'#'}error void
      mut method Void #sat()=native{trusted:'@'}error void
      mut method Void #sdollar()=native{trusted:'$'}error void
      mut method Void #spercent()=native{trusted:'%'}error void
      mut method Void #sbackSlash()=native{trusted:'\\'}error void
      mut method Void #sspace()=native{trusted:space}error void
      mut method Void #snewLine()=native{trusted:newLine}error void

      class method mut This0 #stringLiteralBuilder()
      read method This1.S toS()=
        native{trusted:toS} error void
      #norm{declaresClassMethods, typeDep=This0, This1.S, nativeKind=StringBuilder}
      }
    }
  Debug.of().deployLibrary(sb.toS(),lib=lib)
  {#norm{}}//TODO: can be removed after 'adapt' is completed
  )
Test1={reuse[#$aaa]
  Task=(
    half=21Size
    answer=half+half
    Debug(answer.toS())
    Debug(S"Hello World "++answer.toS()++S"!")
    err=Size"oh NO!"
    catch error S x (Debug(x) {#norm{}}) 
    {#norm{}}
    )
  }
""")
   
   //),new AtomicTest(()->
  //top("{} A={A={}} B={B={A={}}}","{#norm{}}")

  ));}

public static void top(String program){
  Init init=new Init("{"+program+"}");
  init.top.top(new CTz(),init.p);
  assertFalse(Resources.out().isEmpty());
  }
}
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
import is.L42.visitors.WellFormedness.NotWellFormed;

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
  class method mut This0 of()        
  method Void deployLibrary(This1.S that,Library lib)=
    native{trusted:deployLibrary} error void
    #norm{typeDep=This0, This1.S, nativeKind=TrustedIO}        
  }
SB={
  class method mut This0 of()
  mut method Void _a()=
    native{trusted:_a} error void
  read method This1.S toS()=
    native{trusted:toS} error void
    #norm{typeDep=This0, This1.S, nativeKind=StringBuilder}
  }
DeployAAA=(
  mut SB sb=SB.of()
  sb._a()sb._a()sb._a()
  lib={
//now, deploying aaa: Strings, booleans and Size
    S={
      method This0 sum(This0 that)=
        native{trusted:OP+} error void
      class method This0 #from(This1.SB stringLiteral)=stringLiteral.toS()
      #norm{typeDep=This0, This1.SB, nativeKind=String}
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
      #norm{typeDep=This0, This1.S, nativeKind=TrustedIO}        
      }
    SB={
      mut method Void #q()=native{trusted:_q}error void
      mut method Void #w()=native{trusted:_w}error void
      mut method Void #e()=native{trusted:_e}error void
      mut method Void #r()=native{trusted:_r}error void
      mut method Void #t()=native{trusted:_t}error void
      mut method Void #y()=native{trusted:_y}error void
      mut method Void #u()=native{trusted:_u}error void
      mut method Void #i()=native{trusted:_i}error void
      mut method Void #o()=native{trusted:_o}error void
      mut method Void #p()=native{trusted:_p}error void
      mut method Void #a()=native{trusted:_a}error void
      mut method Void #s()=native{trusted:_s}error void
      mut method Void #d()=native{trusted:_d}error void
      mut method Void #f()=native{trusted:_f}error void
      mut method Void #g()=native{trusted:_g}error void
      mut method Void #h()=native{trusted:_h}error void
      mut method Void #j()=native{trusted:_j}error void
      mut method Void #k()=native{trusted:_k}error void
      mut method Void #l()=native{trusted:_l}error void
      mut method Void #z()=native{trusted:_z}error void
      mut method Void #x()=native{trusted:_x}error void
      mut method Void #c()=native{trusted:_c}error void
      mut method Void #v()=native{trusted:_v}error void
      mut method Void #b()=native{trusted:_b}error void
      mut method Void #n()=native{trusted:_n}error void
      mut method Void #m()=native{trusted:_m}error void
      class method mut This0 #stringLiteralBuilder()
      read method This1.S toS()=
        native{trusted:toS} error void
      #norm{typeDep=This0, This1.S, nativeKind=StringBuilder}
      }
    }
  Debug.of().deployLibrary(sb.toS(),lib=lib)
  {#norm{}}//TODO: can be removed after 'adapt' is completed
  )
Test1={reuse[#$aaa]
  Task=(Debug(S"helloworld"){#norm{}})
  }
""")
   
   //),new AtomicTest(()->
  //top("{} A={A={}} B={B={A={}}}","{#norm{}}")

  ));}

public static void top(String program){
  Init init=Init.parse("{"+program+"}");
  init.top.top(new CTz(),init.p);
  assertFalse(Resources.out().isEmpty());
  }
}
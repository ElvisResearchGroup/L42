package auxiliaryGrammar;

import static helpers.TestHelper.lineNumber;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.Ast.Path;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Member;
import helpers.TestHelper;
import programReduction.Program;
import programReduction.TestProgram;
import tools.Assertions;
import tools.Map;

public class TestWellFormednessCore {

@RunWith(Parameterized.class)
public static class Test1Member {
  @Parameter(0) public int _lineNumber;
  @Parameter(1) public String _cb;
  @Parameter(2) public boolean ok;
  @Parameterized.Parameters
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(),"{B:error void}",true
  },{lineNumber(),"{method Any m() this}",true
  },{lineNumber(),"{method Any m() (capsule Any x=this x)}",true
  },{lineNumber(),"{method Any m() (capsule Any x=this capsule Any y=x y)}",true
  },{lineNumber(),"{method Any m() (capsule Any x=this capsule Any y=x x)}",false

  }});}
@Test  public void test() {
  ExpCore.ClassB l=(ExpCore.ClassB)TestHelper.getExpCore(TestWellFormednessCore.class.getSimpleName(),_cb);
  Member m=l.getMs().get(0);
  try{
    m.match(nc->{WellFormednessCore.capsuleOnlyOnce(nc);return null;},
          mi->Assertions.codeNotReachable(),
          mwt->{WellFormednessCore.capsuleOnlyOnce(mwt);return null;}
          );
    if(!this.ok){ throw new AssertionError("FailureExpected");}
    }
  catch(ErrorMessage.CapsuleUsedMoreThenOne e){
    if (this.ok){throw e;}
    }
  }
}

}

package repl;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;


public class ReplStateTest {

  @Test
  public void test() {
    TestHelper.configureForTest();
    ReplGuiMock m=new ReplGuiMock();
    m.defaultStart();
    Assert.assertEquals("Hello world\n",L42.record.toString());
  }

  @Test
  public void testPrint2() {
    TestHelper.configureForTest();
    ReplGuiMock m=new ReplGuiMock();
    m.defaultStart();
    m.auxRunCode("Main2:{\n" +
        "Debug(S\"hi\") return ExitCode.normal()\n}");
    Assert.assertEquals("Hello world\nhi\n",L42.record.toString());
  }
  @Test
  public void testC() {
    TestHelper.configureForTest();
    ReplGuiMock m=new ReplGuiMock();
    m.defaultStart();
    m.auxRunCode("C:Collections.vector(of:Num)\n Main2:{\n" +
        "Debug(S\"hi\") return ExitCode.normal()\n}");
    m.auxRunCode("Main3:{\n" +
        "Debug(C[1Num]) return ExitCode.normal()\n}");
    Assert.assertEquals("Hello world\nhi\n[1]\n",L42.record.toString());
  }
  @Test
  public void testC2() {
    TestHelper.configureForTest();
    ReplGuiMock m=new ReplGuiMock();
    m.defaultStart();
    m.auxRunCode("C2:Collections.vector(of:Num)\n Main22:{\n" +
        "Debug(S\"hi\") return ExitCode.normal()\n}");
    m.auxRunCode("Main32:{\n" +
        "Debug(C2[1Num]) return ExitCode.normal()\n}");
    Assert.assertEquals("Hello world\nhi\n[1]\n",L42.record.toString());
  }

}

package testJamesV;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;

public class gui {
	  @Before
	  public void initialize() {
	    //TestHelper.configureForTest();
	    System.out.println("AssertionsDisabled");
	    //ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
	    L42.trustPluginsAndFinalProgram=true;
	    }

	  @Test
	  public  void _01_01UseJamesGui() throws Throwable{
	    TestHelper.configureForTest();
	    //L42.setRootPath(Paths.get("dummy"));
	    //L42.main(new String[]{"examples/testJamesV/miniCode.L42"});
	    L42.main(new String[]{"examples/testJamesV/oldJamesGUI.L42"});
	    Assert.assertTrue(L42.record.toString().contains("#@Success@#"));
	    }
  }

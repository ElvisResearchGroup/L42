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

public class assignment1 {
	@Before
	public void initialize() throws Throwable {
	    //TestHelper.configureForTest();
	    //System.out.println("AssertionsDisabled");
	    //ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(false);
	    L42.trustPluginsAndFinalProgram=true;
	    //_02_00DeployAdamTowel02();
    }

	@Test
	public  void assignment_one() throws Throwable{
		TestHelper.configureForTest();
		L42.main(new String[]{"examples/testJamesV/Assignment1.L42"});
		
		String record = L42.record.toString();
		int errors = getErrorCount(record);
		if(errors > 0){
			fail(errors + " error(s) have occured in the test cases!");
		}
		System.out.println(record);
	}
	
	/**
	 * Gets the ammount of errors recorded in the code
	 * @param s
	 * @return
	 */
	private int getErrorCount(String s){
		final String FAILSTRING = "[FAIL] ";
		
		int fails = 0;
		
		int index = s.indexOf(FAILSTRING);
		while( index != -1 ){
			fails++;
			index = s.indexOf(FAILSTRING, index+1);
		}
		return fails;
	}
	
	@Test
	public  void assignment1_test() throws Throwable{
		TestHelper.configureForTest();
		L42.main(new String[]{"examples/testJamesV/test.L42"});
		Assert.assertEquals(L42.record.toString(), "DanielWriteHere\n");
	}

	
	@Test
	public  void assignment1_testFailure() throws Throwable{
		TestHelper.configureForTest();
		L42.main(new String[]{"examples/testJamesV/failTest.L42"});
		
		String record = L42.record.toString();
		int errors = getErrorCount(record);
		if(errors > 0){
			fail(errors + " error(s) have occured in the test cases!");
		}
		System.out.println(record);
	}
  }

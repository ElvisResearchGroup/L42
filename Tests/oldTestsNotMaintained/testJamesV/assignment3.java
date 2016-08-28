package oldTestsNotMaintained.testJamesV;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.Reader;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;



public final class assignment3{


    @Before
	public void initialize() throws Throwable {
	    L42.trustPluginsAndFinalProgram=true;
    }

	@Test
	public  void assignment3_test() throws Throwable{
		TestHelper.configureForTest();
		L42.main(new String[]{"examples/testJamesV/assignment3/TestDatabase.L42"});
		Assert.assertEquals(L42.record.toString(), "DanielWriteHere\n");
	}
}
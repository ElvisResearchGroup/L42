package testSlow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import facade.L42;
import helpers.TestHelper;

public class TestFiles {

	@Before public void configure(){
		  TestHelper.configureForTest();
		  //L42.setRootPath(Paths.get("dummy"));
		  }
	@Test
	public void testHelloWorld() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
		 L42.main(new String[]{"examples/HelloWorld.L42"});
		 System.out.println(L42.record);
	}
	@Test
	public void testWithMissingMethod() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
		 L42.main(new String[]{"dummy/WithMissingMethod.L42"});
		 Assert.assertFalse(L42.record.toString().contains("line:-"));
	}
  @Test
	public void testMissingReceiver() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
	   L42.main(new String[]{"dummy/UnknownVariableDotDotDot"});
	   //Assert.assertFalse(L42.record.toString().contains("line:-"));
	}
  @Test
  public void testReadMakeItNotCoherent() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
     L42.main(new String[]{"dummy/ReadIncoherent.L42"});
     Assert.assertFalse(L42.record.toString().contains("PathsNotSubtype"));
     //TODO: we want a better error here, something like runaway incoherence/abstractness cause all to be unusuable.
  }
}

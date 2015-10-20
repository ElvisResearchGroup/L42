package profiling;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestTimer {
  public static String foo="foo";
  public static String bar="bar";
  public static String beer="beer";
  public static String top="TOP";
  @Test
  public void singleWithoutTOP(){
    Timer.restart();
    Timer.fakeTimes=Arrays.asList(0L,60000L);
    Timer.activate(foo);
    Timer.deactivate(foo);
    String res=Timer.report();
    System.out.print(res);
    assertTrue(res.contains(top));
    assertTrue(res.contains(foo));
	assertTrue(res.contains("percentage:1,00/0,00 tot:1,000/0,000 max:1,000/0,000 number:1"));
    assertTrue(res.contains("percentage:1,00/1,00 tot:1,000/1,000 max:1,000/1,000 number:1"));
  }
  @Test
  public void two(){
    Timer.restart();
    Timer.fakeTimes=Arrays.asList(0L,60000L,120000L,180000L,180000L,240000L,420000L,480000L);
    Timer.activate("TOP");
    Timer.activate(foo);
    Timer.activate(bar);
    Timer.deactivate(bar);
    Timer.deactivate(foo);
    Timer.activate(foo);
    Timer.deactivate(foo);
    String res=Timer.report();
    System.out.print(res);
    assertTrue(res.contains(top));
    assertTrue(res.contains(foo));
    assertTrue(res.contains(bar));
    assertTrue(res.contains("percentage:1,00/0,38 tot:8,000/3,000 max:8,000/3,000 number:1"));
    assertTrue(res.contains("percentage:0,63/0,50 tot:5,000/4,000 max:3,000/3,000 number:2"));
	assertTrue(res.contains("percentage:0,13/0,13 tot:1,000/1,000 max:1,000/1,000 number:1"));
  }
  @Test
  public void cluster(){
	  Timer.restart();
	  Timer.fakeTimes=Arrays.asList(0L,60000L,120000L,180000L,180000L,240000L,420000L,480000L, 540000L, 600000L, 660000L, 720000L);
	  Timer.activate(top);
	  	Timer.activate(foo);
	  		Timer.activate(bar);
	  		Timer.deactivate(bar);
	  		Timer.activate(beer);
	  			Timer.activate(bar);
	  			Timer.deactivate(bar);
	  		Timer.deactivate(beer);
	  	Timer.deactivate(foo);
	  	Timer.activate(beer);
	  	Timer.deactivate(beer);
	  String res=Timer.report();
	  System.out.print(res);
	  assertTrue(res.contains(top));
	  assertTrue(res.contains(foo));
	  assertTrue(res.contains(bar));
	  assertTrue(res.contains(beer));
	  assertTrue(res.contains("percentage:1,00/0,25 tot:12,000/3,000 max:12,000/3,000 number:1"));
	  assertTrue(res.contains("percentage:0,67/0,17 tot:8,000/2,000 max:8,000/2,000 number:1"));
	  assertTrue(res.contains("percentage:0,50/0,25 tot:6,000/3,000 max:5,000/1,000 number:2"));
	  assertTrue(res.contains("percentage:0,33/0,33 tot:4,000/4,000 max:3,000/3,000 number:2"));
  }
  @Test
  public void nested(){
	  Timer.restart();
	  Timer.fakeTimes=Arrays.asList(0L,60000L,120000L,180000L,240000L,300000L,360000L,420000L);
	  Timer.activate(top);
	  	Timer.activate(foo);
	  	Timer.activate(beer);
	  	Timer.activate(foo);
	  	Timer.deactivate(foo);
	  	Timer.deactivate(beer);
	  	Timer.deactivate(foo);
	  String res=Timer.report();
	  System.out.print(res);
	  assertTrue(res.contains(top));
	  assertTrue(res.contains(foo));
	  assertTrue(res.contains(beer));
	  assertTrue(res.contains("percentage:1,00/0,29 tot:7,000/2,000 max:7,000/2,000 number:1"));
	  assertTrue(res.contains("percentage:0,71/0,43 tot:5,000/3,000 max:5,000/2,000 number:2"));
	  assertTrue(res.contains("percentage:0,43/0,29 tot:3,000/2,000 max:3,000/2,000 number:1"));
  }
}

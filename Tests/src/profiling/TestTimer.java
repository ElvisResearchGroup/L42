package profiling;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestTimer {
  public static String foo="foo";
  public static String bar="bar";
  public static String beer="beer";
  @Test
  public void single(){
    Timer.restart();
    Timer.fakeTimes=Arrays.asList(0L,60000L);
    Timer.activate(foo);
    Timer.deactivate(foo);
    String res=Timer.report();
    System.out.print(res);
    assertTrue(res.contains(foo));
    assertTrue(res.contains("tot:1.000 max:1.000 number:1"));
  }
  @Test
  public void two(){
    Timer.restart();
    Timer.fakeTimes=Arrays.asList(60000L,120000L,180000L,180000L,240000L,300000L);
    Timer.activate(foo);
    Timer.activate(bar);
    Timer.deactivate(bar);
    Timer.deactivate(foo);
    Timer.activate(foo);
    Timer.deactivate(foo);
    String res=Timer.report();
    System.out.print(res);
    assertTrue(res.contains(foo));
    assertTrue(res.contains(bar));
    assertTrue(res.contains("tot:1.000 max:1.000 number:1"));
    assertTrue(res.contains("tot:3.000 max:2.000 number:2"));
  }
}

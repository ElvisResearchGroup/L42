package testSpecial;

import org.junit.Test;

import is.L42.connected.withHtml.Frame;


public class FrameTest {
  private static final String simpleHTML=
"<html><head><title>Hi there</title></head><body>This is a page a simple page</body></html>";
  @Test
  public void f() throws InterruptedException {
    Frame.load("a1", simpleHTML,200,200);
    //Frame.executeJs("a1","alert(12);");
    //Frame.executeJs("a1","alert(\"He\\\"llo\");");
    System.out.println(Frame.executeJs("a1","event42('foo');"));
    System.out.println(Frame.getEventString("a1"));
    System.out.println(Frame.getEventString("a1"));
    //Frame.load("a2", simpleHTML,300,300);
    //Frame.load("a3", simpleHTML,400,400);
    Thread.sleep(3000);
  }
}

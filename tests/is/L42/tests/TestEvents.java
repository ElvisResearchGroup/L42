package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import is.L42.platformSpecific.javaEvents.ConcreteEvent;

public class TestEvents {
  static{ConcreteEvent.test_only_initialize();}
  ConcreteEvent event=ConcreteEvent.instance();
  @Test void test1Stream(){
    event.setTimeout(30);
    event.submitEvent("bar", "baz", "msg");
    var res0=event.nextEvent("bar");
    assertEquals("bar\nbaz\nmsg",res0);
    var res1=event.nextEvent("bar");
    assertEquals("bar\n##Empty##\n",res1);
    }
  @Test void test2Streams(){
    event.setTimeout(30);
    event.submitEvent("bar1", "baz", "msg");
    event.submitEvent("bar2", "bazz", "msgg");
    var res0=event.nextEvent("bar1\nbar2");
    assertEquals("bar1\nbaz\nmsg",res0);
    var res1=event.nextEvent("bar1\nbar2");
    assertEquals("bar2\nbazz\nmsgg",res1);
    var res3=event.nextEvent("bar");
    assertEquals("bar\n##Empty##\n",res3);
    }

}

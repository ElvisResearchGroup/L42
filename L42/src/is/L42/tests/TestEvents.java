package is.L42.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import is.L42.platformSpecific.javaEvents.Event;

public class TestEvents {
  @Test void test1Stream(){
    Event.setTimeout(30);
    Event.submitEvent("bar", "baz", "msg");
    var res0=Event.nextEvent("bar");
    assertEquals("bar\nbaz\nmsg",res0);
    var res1=Event.nextEvent("bar");
    assertEquals("bar\n##Empty##\n",res1);
    }
  @Test void test2Streams(){
    Event.setTimeout(30);
    Event.submitEvent("bar1", "baz", "msg");
    Event.submitEvent("bar2", "bazz", "msgg");
    var res0=Event.nextEvent("bar1\nbar2");
    assertEquals("bar1\nbaz\nmsg",res0);
    var res1=Event.nextEvent("bar1\nbar2");
    assertEquals("bar2\nbazz\nmsgg",res1);
    var res3=Event.nextEvent("bar");
    assertEquals("bar\n##Empty##\n",res3);
    }

}

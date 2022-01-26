m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90"> Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#JavaServer',`Java Server')

</ol>
CFloat

</p><p id="JavaServer">
WBigTitle(Java Server)
This library helps to create a bidirectional event based
communication with Java.
More detailed info can be seen in the tutorial
Wlink(tutorial_09InputOutput,`chapter 9').

WTitle(Importing process and example usage)
OBCode
reuse[L42.is/AdamsTowel]
J0 = Load:{reuse [L42.is/JavaServer]}
J = J0(slaveName=S"mySlave{}")
Model = J.Handler:{
  @J.Handler mut method Void fromJavaToL42(S msg) = void
  }
CCode

WTitle(Overview)
OFoldedCode
[OVERVIEW_HERE]
CCode

More in the details:
WBR
The loaded Java code will be able to see any Wcode(`*.jar')
present in the 42 project folder.
WBR
The loaded Java code need to represent a class with a constructor
taking in input an Wcode(Event) instance.
Directly after creating such class and loading the code, a new instance of such a class is created,
with an Wcode(Event) instance able to talk back to 42.
The Wcode(Event) interface is as follows:
 
OJCode
package is.L42.platformSpecific.javaEvents;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Event{
  /**
   * registerEvent(key,c) registers an event handler for an asynchronous event.
   * Any time an event key,id,msg is submitted for the specified 'key',
   * the BiConsumer<id,msg> 'c' is called in response.
   * All events for the same key are processed in order.
   * Registering a new event handler for a key also unregisters any former
   * event handler for that same key.
   * Keys for events and askEvents are handled independently, so there is no
   * conflict if the same key is used in both environments for different purposes.
   * */
  void registerEvent(String key,BiConsumer<String,String> c);
  /**
   * registerEvent(key,id,c) acts similar to
   * registerEvent(key,c), but only registers an handler 
   * for a specific combination of key and id.
   * An event handler for a key,id combination does not unregister
   * any event handler, but take precedence over an handler for the same key.
   * A common pattern is to first call registerEvent("Channel",..)
   * with a fallback operation to perform on any unrecognized id,
   * then call registerEvent("Channel","id1",..) ..registerEvent("Channel","idn",..)
   * to handle specialized actions for certain ids.
   * In this example, if the event "Channel","id1","hi" is submitted,
   * only the specialized action is called.
   * If the event "Channel","pizza","hi" is submitted, then the fallback action
   * is called.
   * */
  void registerEvent(String key,String id,Consumer<String> c);
  /**
   * Unregisters any former event handler for the key.
   * */
  void resetEvent(String key);

  /**
   * registerAskEvent(key,c) registers an event handler for a synchronous event, producing a String result.
   * Any time an event key,id,msg is submitted for the specified 'key',
   * the BiFunction<id,msg> 'f' is called in response.
   * 42 will wait until the function is completed and will receive the message.
   * Registering a new event handler for a key also unregisters any former
   * event handler for that same key.
   * Keys for events and askEvents are handled independently, so there is no
   * conflict if the same key is used in both environments for different purposes.
   * */  
  void registerAskEvent(String key,BiFunction<String,String,String> f);
  /**
   * registerAskEvent(key,id,f) works as registerEvent(key,id,c)
   * but it works in a synchronous manner.
   * */
  void registerAskEvent(String key,String id, Function<String,String> f);

  /**
   * Unregisters any former askEvent handler for the key.
   * */
  void resetAskEvent(String key);
  
  /**
   * Submits an event. If an event handler for this key has been registered,
   * this event is going to be processed by Java, otherwise it
   * is going to be processed by 42.
   * */
  void submitEvent(String key,String id,String msg);

  /**
   * The key of the special event representing the end of the event stream.
   * On default it is "##End##\n"
   * */
  void setEnd(String idMsg);

  /**
   * The key of the special event representing that no new events
   * have been produced up to the timeout.
   * On default it is "##Empty##\n"
   * */
  void setEmpty(String idMsg);
  
  /**
   * The default result for an askEvent with no associated handler function.
   * On default it is ""
   * */
  void setDefaultAsk(String answer);

  /**
   * 42 will receive at least one event any 'timeout' milliseconds.
   * On default it is 1000.
   * */
  void setTimeout(int timeout);
  }
CCode
m4_include(`../CommonHtmlDocumentation/footer.h')

WComm build using
WComm m4 -P Doc.c > Doc.xhtml

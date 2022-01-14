WBigTitle(Input Output with Object Capabilities)
WTitle((1/5) Controlling non determinism)
Traditionally, in imperative languages I/O and side effects can happen everywhere, while
in pure functional languages like Haskell they are kept in check by monads.

In 42 we have type modifiers to keep mutation and aliasing under control.
With only the features shown up to now, 42 is a deterministic language, thus
every expression that only takes immutable objects
can be called multiple times with the same values and will produce the same result.

WBR
The whole caching system relies on this property to work.
WBR
Thus input output, random numbers and any other kind of observable non determinism must preserve this property.
WBR
Introducing object capabilities:
WBR
An WTerm(object capability) is a mutable object whose methods can do some non deterministic, or otherwise privileged operation.
If a method is given a Wcode(mut) reference to an object capability, then it can be non deterministic,
otherwise it will be deterministic.
Creation of new object capabilities is only possible by either relying on an existent object capability
or by using a WTerm(capability method): a method whose name starts with Wcode(`#$'); however those specially named methods can only be called from a main, from another capability method or from a Wcode(mut) method of a capability object.

WTitle((2/5) Example: File System)

To read and write files we need to load a Wcode(FileSystem) library as shown below:
OBCode
Fs = Load:{reuse [L42.is/FileSystem]}
CCode
Wcode(Fs) is the local name for the library located at Wcode(L42.is/FileSystem).
The Wcode(Load) decorator embeds the library in the current environment.
We can now use our file system:
OBCode
Main1 = (
  mut Fs f = Fs.Real.#$of()
  f.write(on=Url"data.txt",content=S"SomeContent0") //non deterministic operation
  S s=f.read(Url"data.txt") //non deterministic operation
  //the result depends on the current file content
  f.write(on=Url"data.txt",content=S"SomeContent") //non deterministic operation
  //the operation could go in error if there is not enough space to write the content on disk.
  Debug(s)
  )
CCode
The crucial point in the former code example is the call to 
Wcode(Fs.Real.#$of()).
This instantiates a capability object using the capability method Wcode(#$of()).

We could write the code inside a method in the following way:
OBCode
ReadWrite = { class method Void (mut Fs f)[_] = (
  S s=f.read(Url"data.txt")
  f.write(on=Url"data.txt",content=S"SomeContent")
  Debug(s)
  ) }
Main1 = ReadWrite(f=Fs.Real.#$of())
CCode
Note how we pass the capability object explicitly to the method.
This is the most common style, and have great testing advantages:
Indeed, Wcode(Fs) corresponds to the following interface:
OBCode
interface
mut method Void delete(Url that)[Fs.Fail]
mut method Void makeDirs(Url that)[Fs.Fail]
mut method S read(Url that)[Fs.Fail]
mut method S readBase64(Url that)[Fs.Fail]
mut method Void write(Url on, S content)[Fs.Fail]
mut method Void write(Url on, S contentBase64)[Fs.Fail]
CCode
and Wcode(Fs.Real) is simply an implementation of such interface connected with the real file system.
Thus, we can write a simple mock to check that the function behaves as expected:
OBCode
Mock = Data:{[Fs]
  var S log=S""
  method delete(that) = error X""
  method makeDirs(that) = error X""
  method read(that) = (
    X[actual=that expected=Url"data.txt"]
    this.log(\log++S"read")
    S"oldContent"
    )
  method readBase64(that) = error X""
  method write(on,content) = ( 
    X[actual=on expected=Url"data.txt";
      actual=content expected=S"SomeContent";]
    this.log(\log++S"write")
    )
  method write(on,contentBase64) = error X""
  }
Test1= (
  m=Mock()
  ReadWrite(f=m)
  {}:Test"ReadWriteOk"(actual=m.log() expected=S"readwrite")
  )
CCode

WTitle((3/5) Object capabilities programming patterns)

The advantage of the division of the file system in an interface and a Wcode(Real) implementation are not limited to testing.
For example, the user could embed some security and some restrictions in an alternative implementation of a file system.
Consider the following code:

OBCode
OnlyTxt = Public:{[Fs]
  mut Fs inner

  read method Void checkTxt(Url that) = X.Guarded[
    that.toS().endsWith(S".txt")
    ]
  method makeDirs(that) = error X""
  method write(on,contentBase64) = error X""
  method readBase64(that) = error X""
  method delete(that) = (
    this.checkTxt(that)
    this.#inner().delete(that)
    )
  method write(on,content) = ( 
    this.checkTxt(on)
    this.#inner().write(on=on, content=content)
    )
  method read(that) = ( 
    this.checkTxt(that)
    this.#inner().read(that)
    )
  @Public class method mut This #$of() = //example user declared #$ method, that can
    This(inner=Fs.Real.#$of())   //use $# methods inside its body
  @Public class method mut This(mut Fs inner)
  }
SaferMain = (
  fs = OnlyTxt.#$of()
  ReadWrite(f=fs)
  Debug(S"done")
  )
CCode
Any code that would take in input a Wcode(mut OnlyTxt) would have a limited access to the file system; only able to read and write on Wcode(`*.txt') files.
Here we see for the first time the decorator Wcode(Public).
Wcode(Public) explores all the nested classes of the decorated code, and if there is at least a Wcode(@Public) annotation, all the other members of such nested class will become private.
Methods implemented from interfaces are left untouched.
In the example above, Wcode(Public) leaves the two factory methods visible and hides the field getter and exposer.
We discuss Wcode(Public) more in the detail later.
WBR
Instances of Wcode(mut OnlyTxt) are capability objects; note how Wcode(OnlyTxt) can even declare a Wcode(#$of) method. In this way for the user there is no syntactical difference between using Wcode(Fs.Real) or using Wcode(OnlyTxt).
Capability objects are a useful abstraction and can be designed and implemented by normal 42 programs; they are not just a way for the language implementation to expose native code.
We have just shown that new object capabilities can easy be defined by simple wrapping over existing capability objects.

WBR
Since inner is of type Wcode(Fs), this programming patterns allows us to layer many levels of security / restrictions on top of a capability object, as shown below:
OBCode
fs = OnlyTxt(inner=OnlySmallFiles(inner=Fs.Real.#$of()))
CCode


WTitle((4/5) Connection with other languages)
In general, all the  non determinism in 42 is obtained by communicating with other languages.
42 allows us to connect with Java, and Java allows us to connect with C/assembly.
The best way to connect with java is to use the library Wcode(JavaServer) as shown below:
OBCode
reuse [L42.is/AdamsTowel]
J0 = Load:{reuse [L42.is/JavaServer]}
J = J0(slaveName=S"mySlave{}")
CCode
The code above loads the library Wcode(JavaServer). It is a generic library: before being used we need to provide a name for the Java slave.
A 42 program is not a single process but a cluster of intercommunicating Java processes.
There is one master process where the 42 computation is actually running and many other slave processes allowing safe input output and safe interaction with arbitrary code.
Such slave processes have their own name: in this case Wcode(mySlave).
Slaves also have a set of options, that can be specified between the Wcode({}).
We do not describe the details of those options here.
The class Wcode(J) can now be used to communicate with the Java slave as shown below:
OBCode
MainAsk = (
  j = J.#$of()
  j.loadCode(fullName=S"foo.Bar1",code=S"""
    |package foo;
    |import is.L42.platformSpecific.javaEvents.Event;
    |public record Bar1(Event event){//class Bar1 will be instantiated by 42
    |  public Bar1{                  //and the Event parameter is provided
    |    event.registerAskEvent("BarAsk",(id,msg)->
    |      "any string computed in Java using "+id+" and "+msg);
    |    }
    |  }
    """)
  S.Opt text = j.askEvent(key=S"BarAsk", id=S"anId",msg=S"aMsg")
  {}:Test"OptOk"(actual=text, expected=S"""
    |<"any string computed in Java using anId and aMsg">
    """.trim())
  )
CCode
This code asks the event Wcode("anId", "aMsg") on the channel Wcode("BarAsk").
The Java code registers the capacity of answering to the channel Wcode("BarAsk") and
computes an answer parameterized over Wcode(id) and Wcode(msg).
The method Wcode(askEvent) is synchronous: it will wait for Java to provide an answer as an optional string; optional since Java can return Wcode(null) as a result.

As you can see, you can embed arbitrary Java code in 42 and communicate back and forth serializing data and instructions as strings.

Synchronous communication is sometimes undesirable. 
For example, to use Java to open a GUI it would be better to have asynchronous communication and a queue of events.
You can do this with Wcode(J.Handler), as shown below:
OBCode
Model = Data:J.Handler:{
  var I count, mut J j
  @J.Handler mut method Void fromJavaToL42(S msg)=(
    this.count(\count+1\)
    Debug(S"pong received %msg with count=%this.count()")
    if this.count()<40I (
      this.#j().submitEvent(key=S"BarIn", id=S"ping", msg=S"the message %this.count()")
      whoops J.Fail
      )
    else this.#j().kill()
    )
  }
MainPingPong = (
  j=J.#$of()
  j.loadCode(fullName=S"foo.Bar2",code=S"""
    |package foo;
    |import is.L42.platformSpecific.javaEvents.Event;
    |public record Bar2(Event event){
    | public Bar2{
    |    event.registerEvent("BarIn","ping",(msg)->{
    |      System.out.println("Ping Event received ping "+msg);
    |      event.submitEvent("BarOut","fromJavaToL42","pong");
    |      });
    |    event.registerEvent("Kill",(id,msg)->{
    |      System.out.println("Doing cleanup before slave JVM is killed");
    |      System.exit(0);
    |      });
    |    }
    |  }
    """)
  model=Model(count=0I, j=j)
  model.fromJavaToL42(msg=S"Initial message")
  keys=S.List[S"BarOut"]
  models=J.Handler.Map[key=S"BarOut" mutVal=model]
  for e in j(keys) ( e>>models )
  Debug(S"Completed")
  )
CCode
The class Wcode(Model) handles the events inside of 42:
if Java send an event with id Wcode("fromJavaToL42") then the method
Wcode(Model.fromJavaToL42(msg)) will be called.
In turn, such method
sends to java the message 
Wcode("ping", "the message %this.count()") on channel Wcode("BarIn") using
Wcode(mut method J.submitEvent(key,id,msg))
up to 40 times, and kills the slave JVM after that.

In Wcode(MainPingPong) we initialize the slave JVM to respond to two channels:
Wcode("BarIn") and Wcode("Kill").
In our example Java will submit an asynchronous event to 42 as a response to
the Wcode("BarIn":"ping") event and will terminate the slave on any Wcode("Kill") event.
The slave should always terminate its JVM when receiving a kill, but can do any kind of clean-up before that.
After a JVM is terminated, it can be restarted by simply calling Wcode(J.loadCode(fullName,code)) again.

Finally, we set up the event loop:
An event loop will collect events from a list of Wcode(keys) and dispatch them
to a map of Wcode(models), mapping every key to a specific Wcode(model).
Note that both Wcode(keys) and Wcode(models) are mutable objects. In this way we can dynamically
register and unregister keys/models by mutating Wcode(keys) and Wcode(models).
If the JVM is killed or the list of keys becomes empty, the event loop Wcode(for e in j(keys)) will terminate.
The operation Wcode(e>>models) dispatches the event to the model.
WBR
We need to use two different channels (Wcode("BarIn") and Wcode("BarOut")) to distinguish if an event is should be handled by 42 or by Java.


WTitle((5/5) Object capabilities summary)
<ul><li>
While most languages run in a single process, 42 runs in a cluster of processes; this is needed so that the master process is protected from any slave process going into undefined behaviour.
This is the final piece of the puzzle allowing the correctness properties of 42 to be ensured in any circumstance.
</li><li>
To enable non deterministic behaviour we need to call those specially named Wcode(#$) methods.
Since they can only be called in a few controlled places, we can control what parts of the code can perform non determinism by explicitly passing capability objects.
</li><li>
Capability objects are a very convenient centralized point of control to inject security or other kinds of restrictions.
</li></ul>

WTitle(Digressions / Expansions)

WTitle(Non deterministic errors)

When discussing errors, we did not mention how to handle errors happening in a non deterministic way; for example, how to recover when the execution run out of memory space.
In 42 this is modelled by non deterministic errors. They can only be caught in a main, in another capability method or in a Wcode(mut) method of a capability object. 
AdamsTowel offers a single non deterministic error: Wcode(System.NonDeterministicError). When a non deterministic error happens, we can recover it by catching an Wcode(error System.NonDeterministicError).

The code below shows how to cause a stack overflow and to recover from it.
OBCode
Looping={
  class method Void loop() = this.loop()
  class method Void #$loopStop() = (
    Looping.loop()
    catch error System.NonDeterministicError e (
      Debug(S"This is printed")
      )
    Debug(S"And then this is printed")
    )
  }
Main1=Looping.#$loopStop()
CCode

That is, to recover from a non deterministic error we need to satisfy both the requirements of Wcode('#$') non determinism and of strong error safety.

WTitle(Aborting wasteful cache eagers)
Wcode(@Cache.Eager) methods may return a cached result; such result is guaranteed to be the same that would be computed if we were to directly execute the method.
How does this work if the method is non terminating or simply outrageously slow?
Those eager cache methods will eagerly spend precious machine resources. If the results of those computations are ever needed by a main, the whole 42 process will get stuck waiting, as it would indeed happen if we were to directly execute the method. All good: in this case 42 correctly lifted the behavioural bug into caching.
However, if the result is never needed by a main, it would be nice to be able to stop those runaway pointless computations.
We can obtain this by calling Wcode(Cache.#$stopCacheEager()).
WBR
This works no matter if they are in loop, simply slow, or stuck waiting on another cache being slowly computed.
In some cases, we can even have multiple computations stuck waiting on each other in a circular fashion.
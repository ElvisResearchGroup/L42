WBigTitle(Errors and Exceptions: Messages in AdamsTowel)

WTitle((1/5)`Errors, Messages, Asserts, Guards, .... So much terminology') 
In 42, when something takes an unexpected turn,
you can throw an Wcode(error).
This is similar to Java unchecked exceptions.
Every immutable object can be thrown as an error.
While it is possible to thrown informative strings, they do no offer enough
structure to fully take advantage of the error mechanism.
AdamsTowel defines the interface Wcode(Message):
a structured way to provide a certain kind of message to the user.
There are two main kinds of Wcode(Message): 
Wcode(Guard) and Wcode(Assert).
While assertions are useful to observe bugs, the application
logic should not depend on them, since they may change
in unpredictable ways during library evolutions, and can be enabled or disabled.
Since program logic can depend on guards being thrown,
guards need to be consistent across library evolution.

Assertions are a convenient tool to prevent the code from proceeding
out of our designed state space. The assertion class called Wcode(X) 
looks like a road sign
and
 represents a feeling of "NO/PROHIBITED/FORBIDDEN" or something similar.

Assertions are also very convenient for checking pre/post conditions.
The following code show usages of Wcode(X.Pre) (for preconditions and, in general, blaming the client of a function)
 and Wcode(X) (for postcondition checks in the middle and, in general, blaming the function implementation).

OBCode
method Num confirmAnswer(Num answer) (
  X.Pre[ //preconditions
    answer>0Num; //simplest form
    answer<10000Num msg=S"here with personalized message answer= %answer";
    actual=answer, expected=42Num //do a better error reporting
    ] //in a bunch of assertions, they are all going to be checked/reported together.
  recomputedAnswer = 6Num*7Num
  X[//postconditions/checks in the middle
    actual=recomputedAnswer
    expected=42Num
    ]
  X[answer==recomputedAnswer]
  if answer>50Num (//how to just throw error X
    error X""
    )
CCode
As you have seen, we have various ways to check for condition:
Wcode(`answer>0Num;') checks a boolean condition,
Wcode(`answer<.. msg=S"..";') checks the condition and uses a custom error message,
Wcode(`actual=answer, expected=42Num;') takes two immutable values
and checks that they are structurally equivalent.
WP
Wcode(X) is often used as last case in a sequence of if-return;
for example, instead of defining Wcode(opposite) inside of Wcode(Direction), we could compute it externally as shown below:
OBCode
Direction = Collection.Enum:{
  North = {}, East = {}, South = {}, West = {}
  }
/*..*/
Direction opposite = {
  if d==Direction.North() return Direction.South()
  if d==Direction.South() return Direction.North()
  if d==Direction.East() return Direction.West()
  X[d==Direction.West()] return Direction.East()
  }
CCode
As you can see, since there are only 4 directions, we believe by exclusion that the last case must hold. However, we prefer to
make our assumptions clear and have them checked.


WTitle(`(2/5) Create, throw and capture')

WTitle(Create and throw)

You can create new kinds of messages using Wcode(Message)
as a decorator:

OBCode
AnswerNotUnderstood = Message:{[Message.Guard]}
//this is a new kind of message, implementing Guard.
//you can also add methods to your kind of message.
//you can add fields, we will see this more in detail later.
/*..*/
//throwing an error
if this.ohNoNoNOOO() (error AnswerNotUnderstood"Well, too bad")
CCode

In 42 interfaces can not have implemented methods, not even class ones, so you may be surprised that we can use Wcode(Message) as a decorator, since decorating is a method call.
When operators are called on a class name directly, they are desugared as a method on one of its
nested libraries. For example
Wcode(`Message:{..}') becomes
Wcode(`Message.ClassOperators():{..}').
It is very common for an interface to be usable as a decorator, creating new code with a meaningful default implementation for the interface.

WTitle(Capturing errors and exceptions)

In 42 there is no explicit Wcode(try) statement,
but any block of code delimited by round or curly brackets can contain Wcode(catch).
In the code example below, lines 2 and 3 are conceptually inside the implicit Wcode(try) statement.
If nothing is thrown then lines 6, 7 and 8 are executed.
Note that Wcode(b3) and Wcode(b4) can see Wcode(b1) and Wcode(b2); this would not naturally happen in a language with explicit Wcode(try) statements; Wcode(b1) and Wcode(b2) would become local bindings inside the Wcode(try) statement.
OBCode
res = (
 b1 = CanGoWrong()
 b2 = CanGoWrong() //see b1
 catch error Wrong msg1  S"hi 1" //does not see b1, b2
 catch error Message.Guard msg2  S"hi 2" //does not see b1, b2
 b3 = CanGoWrong(b1) //does see b1, b2
 b4 = CanGoWrong(b2) //does see b1, b2, b3
 S"hi 3" //does see b1, b2, b3, b4
 )
CCode
The catches above do not see local variables Wcode(b1) and Wcode(b2) because they may be capturing an error raised by the execution of the initialization of such variable.
L42 never exposes uninitialized data.
If a catch is successful, then the result of its catch expression
will be the result of the whole code block.
In this way, blocks with catches behave like conditionals.
That is, the code above can assign to Wcode(res) either 
Wcode(S"hi 1"),
Wcode(S"hi 2") or
Wcode(S"hi 3").

WTitle(Strong error safety)

In 42, error handling guarantees a property called strong error safety
(strong exception safety in the Java/C++ terminology).
This means that the body of a catch must not be able to observe
state mutated by the computation that threw the error.
This is enforced by disallowing catching errors in some situations.
WBR
That is, the following code do not compile
OBCode
p = Person(name=S"Bill" age=23Year)
res = (
 p.age(p.age()+1Year)
 p.age(p.age()+1Year)
 catch error Message.Guard msg2 (
   /*could see p with 23 or 24 years*/)
 p
 )
CCode

While the following is accepted.

OBCode
res = (
 p = Person(name=S"Bill" age=23Year)
 p.age(p.age()+1Year)
 p.age(p.age()+1Year)
 catch error Message.Guard msg2 (/*can not see p*/)
 p
 )
CCode

As you can see, in the first version of the code, Wcode(p) is declared outside of the block
and Wcode(p.age(p.age()+1Year)) mutates it.
Wcode(p) would be visible after the Wcode(catch) is completed.
In the second version instead, 
Wcode(p) is out of scope after the Wcode(catch) is completed, and the whole mutable ROG reachable from Wcode(p) is ready to be garbage collected.
WP
Intuitively, a programmer who does a bunch of sequential operations on some mutable objects
would expect them all to be executed.
They expect the intermediate states of those objects not to be relevant to the surrounding program. Consider the following example:
OBCode
method Void birthDay(mut Person bob) = (
  bob.age(\age+1\)
  bob.drunkCount(\drunkCount+1\)
  bob.partyCount(\partCount+1\)
  )
CCode
Reading this code, most programmers would expect this method to keep the 3 counters aligned.
WP
Exceptions and errors violate this intuition, since they can be raised in the middle of the sequence and prevent the later operations.
For example, if Wcode(`bob.drunkCount(\drunkCount+1\)') fails, then Bob will miss his party, possibly because he is too drunk.
This violates the programmers' expectations
outlined above.
WP
Exceptions can be accounted for, since the type system knows about them; so the programmer can be expected to plan for them.
On the other hand, errors can be raised
 anywhere and human programmers often
 account for them only as a last resort.
WP
Thanks to strong error safety, this natural attitude of human programmers is somewhat mitigated: while it is true that Bob will miss his party, the program will never observe him in this sorry state. Bob is, indeed, ready to be garbage collected.
WBR
Without strong error safety, we could simply catch the error and keep observing Bob in his distress.

WTitle(`(3/5) Exceptions and errors')

Exceptions are like checked exceptions in Java.
As with errors, every immutable object can be thrown as an exception.
You can just write Wcode(exception) instead of Wcode(error) while throwing or catching. When catching, Wcode(exception) is the default, so you can write Wcode(catch Foo x)
instead of Wcode(catch exception Foo x).
WBR
Exceptions represent expected, documented and reliable behaviour;
they are just another way to express control flow.
They are useful to characterize multiple outcomes of an operation,
where it is important to prevent the programmer from forgetting about
the many possible outcomes while focusing only on their preferred one.
Exceptions are checked, so methods leaking exceptions have to
mention it in their headers, as in the following.
OBCode
/*somewhere in a GUI library*/
mut method
S promptUser(S text)[CancelPressed] = {
  /*implementation to open a text dialog*/
  }
CCode
The programmer using Wcode(promptUser) has to handle 
the possibility that the cancel button was pressed.
However, L42 supports exception inference; to simply propagate the exceptions leaked out of the methods called in a method body, you can write Wcode(_), as shown below:
OBCode
/*somewhere in a GUI library*/
mut method
S promptUser(S text)[_] = {
  /*implementation to open a text dialog*/
  }
CCode
Exceptions do not enforce strong exception safety as errors do,
so they can be used more flexibly, and since they are documented in
the types, we can take their existence in account while writing programs.
WP
Often, the programmer wants to just turn exceptions into errors.
While this can be done manually, L42 offers a convenient syntax: Wcode(whoops).

OBCode
//long version
Res foo = {
  return DoStuff()
  catch FileNotFound fnf 
    error fnf
  catch FileCorrupted fc
    error fc
  }

//short version
Res foo = {
  return DoStuff()
  whoops FileNotFound, FileCorrupted
  }
CCode

The two snippets of code behave nearly identically: 
in the second, the thrown objects are also notified of the 
position in the code where they are whoopsed.
This is conceptually similar to the very common
Java patten where checked exceptions are wrapped in unchecked ones.
WBR

As we have shown before,  we can use Wcode(X) to mark branches of code
that the programmer believes will never be executed.
Wcode(X) implements Wcode(Assert), so code capturing
Wcode(X) is unreliable: as explained before, AdamsTowel programmers are free
to change when and how assertion violations are detected.
In particular, the programmer may recognize that
such a branch could be actually executed, and thus replace the error with correct behaviour.
WP
Assertions should not be thrown as exceptions, but only as errors.


WTitle(`(4/5) Return')

As we have seen, we have used Wcode(return) to exit  
from the closest surrounding pair of curly brackets.
Also curly brackets can have 
Wcode(catch exception) or Wcode(catch error), which must complete by throwing a
Wcode(return),
Wcode(error) or
Wcode(exception).
WBR Let's see some examples: 
OBCode
{
  x = DoStuff()
  catch Stuff e1
    return S"a" //just swallow the exception
  catch Message.Guard e2 (
    obj.doSideEffect()
    return S"b" //do something and return
    )
  catch error Message e3
    error X"not supposed to happen"
  (//example of a nested block
    y = DoStuff(x)
    return y
    whoops Message.Guard
    )
  }
CCode

Moreover, curly brackets can be used
to Wcode(return) a different result if some computation fails: 

OBCode
res = {
  return PlanA()
  catch error Message.Guard x
    return PlanB()
  }
CCode

WTitle(`Return looks similar to error/exception')
Return is actually another thing that can be thrown and captured.
While only immutable values can be thrown as errors/exceptions,
return can throw any kind of value, but returns can not leak
outside of the scope of a method.
Hold your head before it explodes, but curly brackets are just a syntactic sugar
 to capture returns; these two snippets of code are equivalent: 
<div class= "compare">
OBCode
Num res = {
  if bla ( return e1 )
  return e2
  
  }
CCode
OBCode
Num res = (
  if bla ( return e1 )
  return e2
  catch return Num x ( x )
  )
CCode
</div>
WP
Depending on how your brain works,
knowing the mechanics of Wcode({..return..})
can help you to use return better and understand why you can omit 
Wcode({..return..}) for simple method bodies, and why you can
write multiple groups of curly brackets and have local returns.
Or it may just be very confusing. If you are in the second group, just
never ever write Wcode(catch return) explicitly and continue
on with your 42 experience.

WTitle(`(5/5) Errors, exceptions and return, summary')
<ul><li>
Always detect misbehaviour in your code, and 
terminate it with an Wcode(Assert).
</li><li>
Whenever something outside your
 control happens, give it a name and throw it as an error, as in:
OBCode
NameOfIssue = Message:{[Message.Guard]}
/*...*/
if /*..*/ ( error NameOfIssue"more info" )
CCode
It just takes 2 lines, and will make debugging your code so much 
easier.
</li><li>
Use errors intensively, but use exceptions sparingly:
 they are needed only in a few 
cases, mostly when designing public libraries.
</li><li>
To convert exception into errors, use the convenient short
syntax Wcode(`whoops T1,..,Tn').
</li><li>
Instead of manually writing long lists of leaked exceptions, 
you can use Wcode([_]). This is particularly convenient for small auxiliary methods.
</li><li>
It is sometimes possible to write elegant and correct code
that is not covered in layers upon layers of error/exception checking,
but often is not possible or not convenient.
Up to half of good 42 code will be composed of
just error/exception handling, repackaging and lifting.
Do not be scared of turning your code into it's own policemen.
</li></ul>
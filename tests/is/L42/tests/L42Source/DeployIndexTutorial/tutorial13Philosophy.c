 <!--


 <!--WTitle(Simpler complete program)

Let now starts showing the simplest 42 program: an empty library.

OBCode
{}
CCode

If we save this valid program in a file Wcode(Test.L42) and we run Wcode(L42 Test), we get an error.
WP
As you see 42 is very intuitive, as you would expect from your former life experiences, most simple things just does not work.
Note how valid programs can produce errors.
We will soon learn how to produce errors in controlled and elegant ways.
-->
<!--A 42 program execution WEmph(is) the generation of all its nested classes/interface.
, code is simply executed from top to bottom as in 
Python, Javascript or Php. However, the
top level expression is a Library, and code can go in libraries 
as an initializer for you need to put the code into an expression
-->

<!-- LATER?
Wcode(MyCode.hello(..)) 
use directly the Wcode(MyCode) class instance as
receiver. We can also give it a name 
e se vuoi puoi anche salvarlo su un binding locale, 
tipo x= MyCode  x.hello(...)
-->


TODO:
In 42 we have single and multiline strings, and string interpolation works on both.
String interpolation 

%x.foo()
%varName/ClassName .foo()[].foo()[]()
or
%(...)
Yes comments when reasonable
string literals only in () and in multiline strings
Bug: (un)balanced parenthesis in string literals, multi line str lit and comments are not ignored



WBigTitle(Guarantees and Philosophy)

WTitle((1/5)Language invariants)
42 guarantees a set of language invariants; properties that are always true.

WTitle(Immutability)
In 42, once an object become immutable,
it will never change again.
It's whole reachable object graph is frozen for the rest of its lifetime.
( In some languages this is called value semantic,
 to not be confused with pass by value/pass by reference)

WTitle(Encapsulation)
A capsule binding (not a capsule field)
is encapsulated, that is: 
such capsule binding is the only way to reach
that object and its whole (mutable) object graph.
It is irrelevant if immutable leaves are shared or not.


WTitle(Hygienic aliasing)
A family of references is hygienic if each pair of references
point to disjoint object graphs (modulo immutable leaves).
WP
Hygienic families are preserved during
the execution of any operation requiring at most one mutable references.
That is: 
<ul><li>
capsule references can be used only once, so they will not be available any more,
so it is irrelevant what they referred to;
</li><li>
class and immutable references  are irrelevant since it can not be observed if two references are the same object or
an identical clone;
</li><li>
read and lent references can not be stored inside of other object graphs.
Lent view point adaptation is designed to carefully preserve 
Hygienic aliasing while allowing lent reference to do mutation by promoting them to mutable in a controlled scope.
</li><li>
mutable references can not be stored inside a lent reference.
</li></ul>

WTitle(Strong error safety)
If an error is captured, the catch body will observe the same state that was present
at the start of its guarded paragraph.
That is, paragraphs guarded by a catch error can not modify externally visible state.
WTitle(Checked exceptions)
It is always statically known what exceptions every piece of code can raise.
As for Java, this is obtained by declaring exceptions on the method signature.
However, 42 offers convenient syntactic sugar to turn exceptions into errors/other exceptions.
WTitle(Subtyping control)
In Java subtype is always possible (when not prevented by the Wcode(final) keyword), while 42 is more restrictive:
Only interface provide subtyping, while classes are all exact types,
that is, if a method takes a Wcode(Point), and Wcode(Point) is not
an interface, that method is always going to receive exactly a point. 
On the other side, if Wcode(Point) is an interface, you know is going to be some class implementing that interface.
This subtyping restriction, coupuled with metaprogramming operations like Wcode(Redirect) and generics, encourages frameworks to be instantiated at metaprogramming time instead of using subtyping.
We believe this allows to reduce the use of subtyping only when is really needed, allowing easier reasoning on the code.
WTitle(MetaSafety)

Most other meta-programming approaches allow for new type errors to be introduced by metaprogramming.
WP
In 42 meta-programming can not add type errors to otherwise
well-typed code. Note that code can be non well-typed either because it is ill-typed or
because it refers to classes and interfaces that have not yet been produced.
WP
This implies that 
Metaprogramming operations in 42 can produce non well-typed
code only if at least one input is non well-typed.
That is, if all of the inputs are well-typed and a result is produced (instead of, eg. a dynamic error or non temination), then the result is well-typed.
WP
An important corollary: every expression that does not contains a library literal, will never produce
non well-typed library literals.
WBR
This includes every trait (class methods with no argument returning Wcode(Library)) invocation
and expressions like Wcode(Collections.vector(of:Point));
since they do not contains any library literal.

WP



Nested library declarations containing library literals,
as for example
Wcode(`Point: Data <>< {Num x, Num y, method This add(S x) This.with(x: \+x)}'), can produce
non well-typed code, if some contained library literals was non well-typed.
In the case of our example, we mistakenly used the Wcode(S) type instead of 
Wcode(Num); this will cause a type error in the result, which can be traced back to a type error
in the initial library literal.


WTitle(Object always fully initialized)
In 42 all object are born with all of the fields containing objects of the right types, and this will hold for the whole lifespan of the object.
There is no Wcode(null) and no uninitialized values.
To allow for the initialization of circular object graph 
you can use a feature called Wcode(fwd) references, that we have not explored in this tutorial.
See (link)


WTitle((2/5)Philosopy of 42 and AdamsTowel)
Other languages have a weak division between language features
and their standard library.
42 have a very strong separation between language and libraries.
You can see that from the mentality of the language and
the (different) mentality of the popular Wcode(AdamsTowel).

WTitle(Philosopy of 42)
<ul><li>
42 is just an instrument; use it as best you can,
to do what you prefer.
</li><li>
No idea is too crazy for 42;
no matter what you do, you can not break the language
invariants; so play hard with it; It can take it!
</li><li>
Do not let other people or libraries 
tell you what to prefer or avoid.
</li><li>
There is no intrinsic meaning in 42, no language feature
is designed to be used only in a certain way.
</li><li>
42 is based on a minimal core, composed of little more than
method calls and exception handling.
Then there is a thick layer of syntactic sugar, allowing 
for more convenient syntax.

Please,experiment with the fixed but flexible syntactic sugar of
42, and find new idiomatic ways to mesh 42 into expressing what you want in the
way that you want it.
</li></ul>


WTitle(Philosopy of AdamsTowel)

<ul><li>
AdamsTowel offers a large set of simple concepts, which you can use
to encode the domain of your problem.
<BR/>
Basic classes should represent atoms of knowledge.
<BR/>
Collections should represent homogeneous groups of objects,
where every object serves the same role in your domain.
<BR/>
Instances of data classes are agglomerations of instances of other classes, subject to an invariant.
<BR/>
Modules have only class methods and are a simple way to organize your code.
<BR/>
Resources serve to indicate constants.
<BR/>
Algorithmic classes will implement a certain algorithm interface;
and the concrete behaviour will be selected polymorphically; i.e. depending on the concrete class of the
instance.
<BR/>
Messages will report errors and exceptions.
<BR/>
Decorators will complete your code,
 adding the needed boilerplate.
</li><li>
AdamsTowel can be stained and embroidered to create many variations,
and those variations can all play together with little effort and discipline.
</li><li>
You can use modifiers in a disciplined way to express meaning: 
<ul><li>
Immutable references are abstract/mathematical concepts, they are used to model the world 
of your program but are not materialized in your world.
For example, a Wcode(Car) has a Wcode(Kg) weight, but Wcode(25Kg) is not a thing in the world of cars.
</li><li>
Class objects model kinds of things, and you can use
class methods as convenient ways to refer to general concepts not specially connected to any entity
in the domain of your program.
Class methods in interfaces fill a special role:
expressing behaviour that is parametric on the kind of object without
resorting to metaprogramming; in a pattern similar to dependency injection, see (link).
This requires using references to class objects instead of just naming them by path.

<!--For example if the Wcode(WebServer) interface 
has a Wcode(class method This connect(S connectionString))
then you can write code taking in input
-->
Another point where we need to use class references is metaprogramming; for example in the target of  Wcode(Refactor.Redirect).

</li><li>
Mutable references point to mutable objects.
Shallow mutable objects can be the centrepiece of your design.
However, while everything sort of turns around them, most functions will work only on their (immutable) content.

Deep mutable objects need to be handled with more care.
</li><li>
Lent references/parameters indicate the desire of a method to be hygienic with
a chunk of data.
If a method has at most one mutable parameter including the method modifier,
we suggest to  not making it lent; nothing would change and it would only look more involved.
If a method has more than one mutable parameter, then if possible keep the 
method modifier mutable and make the other parameters lent.
The only case where it is reasonable to have a lent method is when there are at least two 
other mutable parameters, and the method is using the information inside of the receiver to decide how to mix
their reachable graphs.
</li><li>
Readable references/parameters indicate the desire of a method to just read 
the content of a chunk of data, without storing it or mutating it.
If possible, make methods and methods parameters read if they can not be immutable.
</li></ul>
</li></ul>


WTitle(`(3/5) Reconciling opposite views')
How can the philosophy of AdamsTowel be so different from
the philosophy of 42?
Actually, AdamsTowel is following all the suggestions of 42: 
WBR
It is using 42 as an instrument, as best it can.
By checking for specific usage pattens, it can enforce 
class invariants by building on the language invariants.
Certain refactoring decorators performs crazily complicated 
operations, but they can be abstracted to a simple high level concept.

AdamsTowel goes a long way to support units of measure and alphanumerics, even if 
most other standard libraries would avoid going that way.

Strings in AdamsTowel use the square bracket Wcode([]) syntax to do 
string interpolation, instead of just sequence building.
Wcode(Enumeration) uses the string literal postfix operator to generate enumeration 
classes.
Who knows what new and creative 
applications of the 42 syntax could be used by another, more mature towel.



WTitle(`(4/5) Embrace failure')
We love programs that fail.
WBR
We love more the ones that fails early and with good error messages.
WBR
We love static type system; it allows us to fail very fast.
WBR
We love to have code analysis; it makes our code fail quite fast and reliably. 
WBR
We love to check for additional constraints at run time.
WBR
The important think is that code behaving differently from what we expected, should
never be allowed to produce a (non error) result.

WP
This mindset is different from the one found in many other language communities.
WBR
For example languages supporting flexible, silent, automatic conversions between different 
datatypes (as string, bools and numbers) are clearly searching for a 
way to interpret a possibly confusing programmer request and give it a meaning.
For example Wcode("2"+2) may mean Wcode("22") while Wcode(2+"2") may mean Wcode(4).
WBR
Those are reasonable interpretations, motivated by certain examples, but they can not possibly scale to
the general case.
WP
On the other hand, programmers coming from languages that support very strong type systems
and encourage type safety as a way of mind, 
would prefer to either encode a precondition at the type level, 
or to encode a generalized behaviour where such a precondition is not needed, to dodge the problem.
For example, in their mindset, Wcode(max) should not return an element, but an optional
element, and the option of no element will be produced in case of an empty list.
This approach does not scale: 
certain conditions can not be expressed in the type system;
some other conditions could be expressed but it would be too cumbersome.
WBR
In our vision, the purpose of the type system is help to make the program adhere to
its intended behaviour or break fast.
We do not want to bend our intended behaviour so that code
could never be observed to fail.
Such code can still behave unexpectedly (with respect to the original intended behaviour).





WTitle(`(5/5) Going forward')

You are now ready to do simple programs in 42.
While coding, you should refer to (link) where you can find
detailed documentation for all the classes of AdamsTowel and many useful libraries.

WP

If you want to go forward and have a better understanding
of 42, you can now read, in any order,

<ul><li>
42 core language design, for programming language experts
</li><li>
42 syntactic sugar, in detail
</li><li>
42 type system, exact rules for promotions and usage of fwd types
</li><li>
42 metaprogramming guide and how "there is no state"
</li><li>
42 testing, mocking and configuration
</li><li>
deploying 42 onto different platforms
</li><li>
42 and native code; how to import libraries from other languages
</li><li>
42 optimizers 
</li></ul>
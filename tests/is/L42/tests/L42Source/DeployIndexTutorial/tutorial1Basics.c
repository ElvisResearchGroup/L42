WBigTitle(`Target audience')
This tutorial is design for expert programmers, already knowledgeable in 
at least two or three of the languages Java, C#, C++ and Python.
This tutorial lays out the basic knowledge for programming in 42 using AdamsTowel, but
does not explore the foundational theory behind 42,
or the mathematical rationale for the correctness of 42.
WBR
The language 42 and many 42 metaphors are inspired by
 The Hitchhiker's Guide to the Galaxy by Douglas Adams.


</p><h2 id="Download"> Downloading and running </h2> <p>
WComm WTitle(`Downloading and running')

Currently, you can download and run 42 as a 
Java program.
<ul>
<li>
  For windows download here <a href=
  "https://l42.is/L42PortableWin.zip"
  >L42PortableWin.zip</a>
  WComm "https://github.com/Language42/Language42.github.io/releases/download/d025/L42PortableWin.zip"
  WComm For windows download here <a href="http://130.195.5.18/L42PortableWin.zip">L42PortableWin.zip</a>
  WComm or <a href="L42Win.jar">L42Win.jar</a>
</li>
<li>
  For Linux download here <a href=
  "https://l42.is/L42PortableLinux.zip"
  >L42PortableLinux.zip</a>
  WComm For Linux download here <a href="http://130.195.5.18/L42PortableLinux.zip">L42PortableLinux.zip</a>
  WComm <a href="L42Linux.jar">L42Linux.jar</a>
</li>
<li>
  For Mac download here <a href=
  "https://l42.is/L42PortableMac.zip"
  >L42PortableMac.zip</a>
  WComm For Mac download here <a href="http://130.195.5.18/L42PortableMac.zip">L42PortableMac.zip</a>
</li>
WComm <li>  The portable version contains also an appropriate JVM.</li>
  <li>  Run Wcode(L42)
    WComm  Wcode(java --enable-preview -jar L42.jar) 
    to start the IDE.
</li>
<li>
  Run Wcode(L42 ProjectName)
  WComm with Wcode(Java --enable-preview -jar L42.jar ProjectName)
  to run the 42 program inside of the folder Wcode(ProjectName) from the command line.
</li>
</ul>
WBigTitle(Basics)
WTitle((1/5)Simple hello world program)
Let's look at a simple hello world program: 

OBCode 
reuse [L42.is/AdamsTowel]
Main = Debug(S"Hello world")
CCode
WBR
When we write Wcode(reuse [L42.is/AdamsTowel]) we are asking 42 to
reuse the code of the library found in the internet address 
Wcode(L42.is/AdamsTowel).
AdamsTowel is our WTerm(towel), that is the set of classes and interfaces that we wish to start from.
WComm Wlink(towel,Deploy code)
A WTerm(towel) usually plays the role of "the standard library" of most languages.
Wcode(L42.is) is the main website of 42, where most commonly used libraries are hosted. To reuse code you 
need an internet connection; but this also means that you will never have to manually import any code.
Required code will be downloaded and cached on your machine, so you need not to be aware of the existence of this mechanism.
WP
We do not need to always start from AdamsTowel; there are many interesting towels out there, and you may also become skilled in the 
advanced technique of towel embroidery.
In this tutorial, all of our examples are expressed reusing Wcode(L42.is/AdamsTowel).
WP

At the right of Wcode(Main = ) we write the expression that
we wish to execute; in this case we just print out using the Wcode(Debug) class.
Wcode(Main) is not a method, and Wcode(Main) is not special name either. You can replace it with Wcode(Task) or any other valid
upper-case name. In 42 there is no concept of main method as in
Java or C. 
For now you can think of Wcode(Main = ) as a top level command. We will understand later how this fits with the general language design.
WP
Wcode(Debug)
is a simple class whose most important method print a message on the terminal.
WP
In 42, when a class has a WEmph(`most important') method, it is conventional to use the empty name, so that can be used with the short syntax Wcode(`Debug(S"Hello world")') instead of a more verbose Wcode(Debug.println(..)). 

WBR
In 42, Strings and numbers need to be created using their type, as in
Wcode(S"Hello world") or Wcode(12Num).
Indeed Wcode(12Num) is just a convenience syntax equivalent to Wcode(Num"1"); the syntax with quotes is needed to express negative or fractional number literals, as for example Wcode(Num"-12")
 or Wcode(Num"53/21").

WP

WTitle((2/5)Method declaration and call)
Let's now define a method and call it.
OBCode
reuse [L42.is/AdamsTowel]
MyCode = {
  class method
  S hello(S nickName) = { //we can use usual if/while
    if nickName.isEmpty() (return S"Hello!")
    return S"Hello %nickName!"
    } 
  }
Main = Debug(MyCode.hello(nickName=S"Marvin"))
//will print "Hello Marvin!"
CCode
WBR
Here we define a class to host our Wcode(hello(nickName)) method.
We write Wcode(class method) to define a method that can be called on the class object, as in Wcode(MyCode.hello(nickName=S"Marvin")).
This is roughly equivalent to a static method in languages like Java or C++ , or class methods in Python.
Note that Wcode(%) inserts a value into a string.
WP
Note that the method is called using the parameter name explicitly.
We believe this increases readability.
WP
You may also notice how there are two different usages for curly brackets:  if there is at least one Wcode(return) keyword then the expression is a block of statements,
otherwise the expression is a library literal, which can contains methods and nested libraries.
A WEmph(nested library)
is denoted by an upper-case name, and can be created from a library literal or from an expression producing a library literal.
A library literal can be a class (default case) or an interface (starts with the Wcode(interface) keyword).
A nested library in 42 is similar to a static inner class in Java, or a nested class in C++. It is just a convenient way to separate the various components of our program and organize them into a tree shape.
WP
The class Wcode(MyCode) from before offers a single class method, has no fields and you can not create instances of Wcode(MyCode), since no factory is present.
In 42 we do not have constructors. Objects are created by WTerm(factory methods), that are just normal methods that happen to return an instance of their class. We believe this is a much simpler and more consistent approach to object initialization than having special syntax that encourages programmers to make assumptions about the behaviour of the operations.

WTitle((3/5)Simple class with internal state)
Now we show a class with state and a factory method: 
OBCode
Point = Data:{
  Num x
  Num y
  method
  Point add(Num x) = //long version
    Point(x=x+this.x(), y=this.y())
  method
  Point add(Num y) = //shorter
    this.with(y=y+this.y())
  method
  Point sum(Point that) =
    Point(x=this.x()+that.x(), y=this.y()+that.y())
  }
CCode
WBR
Here you can see we define a Wcode(Point) class with coordinates Wcode(x) and Wcode(y) of type Wcode(Num),
unlimited precision rational number.

In addition to Wcode(x),
 Wcode(y),
 Wcode(add(x))
 and Wcode(add(y)),
 Wcode(Point) will offer many other useful methods, since it has been declared using
 Wcode(Data).
 WP
Indeed, Wcode(Data) is a decorator. Decorators are classes/objects that offer an operator Wcode(:), called the decorator operator,
whose goal is to translate a library into a WEmph(better) library.
In this case, Wcode(Data) is translating the class Wcode(`{Num x, Num y .....}')
 into a much longer class, with
a factory method taking in input the fields and initializing them; but also containing
 boring but useful definitions for
equality, inequality, conversion to string and many others.
<!--from and to human readable strings, XML and binary representations for (de)serialization.-->
WP
Finally, we define a methods to add to each of the coordinates.
For very short methods we can omit the curly brackets and Wcode(return).
Indeed, method bodies are just expressions, and the curly brackets turn a block of statements into one expression. 

In the method Wcode(add(x)) we show how to create a new 
Wcode(Point) instance and how to call WTerm(getter methods).
In the method Wcode(add(y)) we show an improved version, using the Wcode(with) method, another gift of Data, which allows us to easily create a clone with one or more fields updated.
We can define two methods, Wcode(add(x)) and Wcode(add(y)) with the same method name, if parameter names are different.
WP
Note that we always use getters and we never access fields directly.
In many other languages we can use write Wcode(a.fieldName) and Wcode(a.fieldName= newValue). Such syntax does not exists in 42. The same goes for object instantiation; in many languages there is a dedicated  Wcode(new ClassName(..)) syntax, while in 42 it is just a method call.
WP
Also, similarly to what happens in Python, we need to use Wcode(this.methodName()) to call methods when the receiver is Wcode(this).
While it makes some code more verbose, naming the receiver avoids ambiguities about scoping and nesting for method resolution.


WTitle(Decorators)
Decorators, such as Wcode(Data), are one of the main concepts used by 42 programmers. We will encounter many decorators in this tutorial.
For now, just get used to the pattern of writing
Wcode(:) to go from a minimal chunk of code, with method declarations for the application specific bits, to a fully fledged usable class.

WTitle(The backslash Wcode(\))
In 42, we can use the Wcode(\) character as a shortcut.
There are two different ways to use the backslash:
as a keyword or immediately followed by a lowercase identifier.
WP
As a keyword, Wcode(\) represents the expected type of the surrounding expression.
The slash searches outwards on super expressions until it finds a place with an easily guessable type:
the return type of the method, a method parameter or a local binding with an explicit type.
For example:
OBCode
method Point add(Num x) = Point(x=x+this.x(), y=this.y())
CCode
could be shortened as 
OBCode
method Point add(Num x) = \(x=x+this.x(), y=this.y())
CCode
Consider these other examples:
OBCode
method Num foo() = 1\+2\ // \ is Num
method class Num bar() = \ // \ is Num
method Num baz() = \.zero().toS().size() // \ is Num
CCode
WP
Followed by a method name (and method parameters if any)
a Wcode(\) represents the receiver of the innermost method invocation.
Thus, for example
OBCode
method Point add(Num y) = this.with(y=y+this.y())
CCode
could be shortened as 
OBCode
method Point add(Num y) = this.with(y=y+\y)
CCode
Consider this other example:
OBCode
method Point bar(Point that) = that.with(y=\sum(that).y())
method Point bar(Point that) = that.with(y=that.sum(that).y())//equivalent
CCode

WP
In the rest of the tutorial, we will use 
Wcode(\) when it saves space. This shortcut seems unusual at first, but with a little of experience becomes very clear. 42 is a pure OO language, where the method call is the central operation. 
This syntax allows for the expressions of the method parameters to depend on the method receiver. We will see that this enables many interesting micropatterns.

WTitle((4/5)Collection.list)

Lists can be defined using Wcode(Collection.list(_)), as in the example below,

OBCode
Nums = Collection.list(Num) //declaration for vectors of nums

Points = Collection.list(Point) //same for points

Main1 = Debug(Nums[ 10\; 20\; 30\ ]) //here \ is Num
Main2 = Debug(Points[\(x=10\ y=20\);\(x=1\ y=2\)]) //here the outer \ is Point
CCode
where we define new classes Wcode(Nums)
and Wcode(Points). Note that those are new classes in a nominal type system, so in
OBCode
Nums1 = Collection.list(Num)
Nums2 = Collection.list(Num)
CCode
Wcode(Nums1) and
Wcode(Nums2) denote different classes, with different types.
As you can see, lists can be initialized with Wcode([_;_;_]).
In this case, this syntax is equivalent to creating a new empty list and then calling the Wcode(add(that)) method
one time for each of the expressions separated by Wcode(;).
Of course, the parameter type of that method is the element type of the list, so
Wcode(\) finds it as an easily guessable type.

WP

Consider now the following code:
OBCode
xs = Nums[ 10\; 20\; 30\ ]
ys = Nums[ \"-1"; \"2/3"; 3\ ]
points = Points[]
for x in xs, y in ys (
  points.add(\(x=x, y=y))// here \ is Point
  )
Debug(points) //prints
//[Point(x=10, y="-1"); Point(x=20, y="2/3"); Point(x=30, y=3)]
CCode
As you can see, we can use Wcode(for) to iterate on multiple collections at once.
WBR
In 42 as in most other languages you can have blocks of code where multiple
WTerm(local bindings) are introduced by associating a lowercase name with an initialization expression.
Similarly, the Wcode(for) introduces local bindings whose values will range over collection elements by associating them with initialization expressions for iterators.


WTitle(`(5/5)First summary')

<ul><li>
At the start of your program, import a towel using 
Wcode(reuse _), as in Wcode(reuse [L42.is/AdamsTowel]).
</li><li>
To define a simple class exposing its state and 
some methods working with those, use Wcode(Data), as in
Wcode(`Point = Data:{Num x, Num y}').
</li><li>
You can define methods in classes with the Wcode(method) keyword.
Use Wcode(class method) for methods that can be called on the class object directly.
</li><li>
To introduce the concept of list for a certain type, use 
Wcode(Collection.list(_))
WBR as in the class declaration
Wcode(Points = Collection.list(Point))
</li></ul>

</p><div style="margin-left: 30px;">
<h2> Object creation summary </h2> <p>
42 supports many different syntactic forms that are convenient for creating objects: 
<ul><li>
12Num:  from a numeric representation
</li><li>
S"foo":  from a string representation
</li><li>
Point(x=_,y=_):  from the parameter values
</li><li>
Points[_;_;_]:  from a variable length sequence of values.
</li></ul>

Note that in 42 these are all just expressions, and represent one or more methods in the named class.
This means that even concepts quite different from numbers, strings and collections may benefit from this syntactic support.
</p></div>
<p>
WTitle(Digressions / Expansions)
Here, after the summaries, we will digress and expand
on topics related to the chapter. 
Digressions/expansions may be more technical and challenging, and may refer to any content in any of the other chapters, including the forward ones.
For example, we are now going to make some more precise remarks about:
WTitle(Method selector)
In 42 a method selector is the method name plus the list of all the parameter names, in order.
Methods in a class must be uniquely identified by their method selectors.
This provides a good part of the expressive power of overloading, while avoiding all the complexities of type driven overloading resolution.

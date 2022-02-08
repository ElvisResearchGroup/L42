m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

WComm WTitle(`First, read in any order those introductions')
WComm <Button class= 'button' type= 'button' onclick= 'selectDiv("a");'>
WComm Why 42
WComm </Button>

WComm <Button class= 'button' type= 'button' onclick= 'selectDiv("c");'>
WComm Pure OO
WComm </Button>

<Button class= 'button' type= 'button' onclick=
 "location.href = 'download.xhtml';">
Download
</Button>


WComm <Button class= 'button' type= 'button' onclick= 'selectDiv("b");'>
WComm Learn through examples
WComm </Button>


<Button class= 'button' type= 'button' onclick=
 "location.href = 'tutorial_01Basics.xhtml';">
See the full Tutorial
</Button>

<Button class= 'button' type= 'button' onclick=
 "location.href = 'https://github.com/ElvisResearchGroup/L42';">
Git Repository
</Button>

<Button class= 'button' type= 'button' onclick=
 "location.href = 'https://www.youtube.com/MarcoServetto';">
42 Videos
</Button>

<Button class= 'button' type= 'button' onclick=
 "location.href = 'indexLib.xhtml';">
Libraries index
</Button>

<Button class= 'button' type= 'button' onclick=
 "location.href = 'https://ecs.wgtn.ac.nz/mailman/listinfo/team42';">
Mailing List
</Button>


<Div><p>
WTitle(Why 42)

42 is a new programming language, whose name is inspired by The Hitchhiker's Guide to the Galaxy.
Why should you give 42 a try?
WP
42 programs are secure, easy to optimize and customize. In 42 you get:
WBR
<ul>
<li><a href="#SeamlessCaching">
Seamless caching.
</a></li><li><a href="#DeterministicParallelism">
Deterministic parallelism.
</a></li><li><a href="#TypedClassDecorators">
Typed class decorators.
</a></li><li><a href="#AlwaysOnConstraints">
Always-on constraints.
</a></li><li><a href="#FineGrainedPermissions">
Fine grained permissions.
</a></li><li><a href="#IntegratedDeployment">
Integrated deployment.
</a></li><li><a href="#NoNeedOfSystemKnowledge">
No need of system knowledge
</a></li><li><a href="#Towels">
Towels!
</a></li></ul>

More in details:
WP
<ul><li>
<h5 id="SeamlessCaching">Seamless caching</h5>
Most of the performance issues are not about smart ways to generate ultra fast assembly.
Most often they are about code repeating the same operations over and over again.
In 42 various forms of caching are proven semantically unobservable.
This means that those optimizations are easy to insert and are unable
to induce bugs into the system.
</li><li>
<h5 id="DeterministicParallelism">Deterministic parallelism</h5>
Caching and parallelism are the two main ways to optimize code.
While caching avoids doing the same operation over and over again,
parallelism allows doing more things at the same time.
In other languages, parallelism allows for non deterministic behaviour;
that is a nightmare to debug, especially when it is unintentional.

In 42 all parallelism is deterministic by construction,
thus no bugs can be caused by the quirks of parallel execution.
This allows us to take full advantage of many cores while keeping the simple deterministic and
sequential execution model in mind when testing and debugging code.
</li><li>
<h5 id="TypedClassDecorators">Typed class decorators</h5>
In a typical 42 program, the huge majority of the code is
automatically generated by user defined class decorators.
This allows for exceptionally compact programs, like a fully 3-tier application in less then 100 lines.
Instead of providing the full code, a code decorator takes a suggestion (a fragment of the full code)
and produces the full result.
In this way the programmer can focus the majority of their time
on writing the important bits of their code,
while redundant and repetitive code is automatically generated.
</li><li>
<h5 id="AlwaysOnConstraints">Always-on constraints</h5>
<q>Personal names start with an upper-case letter</q>,
<q>Those two lists have the same size</q>,
<q>This map contains an entry for all the cities of Japan</q>.
When writing code, we often assume our input data to satisfy certain expectations.
In 42 we can encode those expectations as user defined constraints on the data,
and it is then impossible to observe broken constraints.
In this way the data we work on can never become ill formed.
</li><li>
<h5 id="FineGrainedPermissions">Fine grained permissions</h5>
A secure program never does the wrong action.
For example, it never commits ill-formed data to the database.
In 42 a single localized part of code can clearly specify what those actions are
and what are the corresponding correctness criteria.
The rest of the code will be unable to break those criteria, and thus be unable to perform wrong actions.
In this way the programmer can focus the majority of their time on writing correct software,
while the security is guaranteed by the system.
This has pervasive security implications: in a large application, the most experienced programmer can 
write those constraints. Other programmers and library code will be unable to break those constraints,
no matter how hard they could try.
</li><li>
<h5 id="IntegratedDeployment">Integrated deployment</h5>
In other languages, writing a program is just the first step.
The program will need to be then tested and uploaded so that clients can use it.
This usually requires using a plethora of different tools.
This is not the case in 42: A typical 42 program will compile, test and deploy itself.
In 42 everything about an application is encoded by the code of that application.
</li><li>
<h5 id="NoNeedOfSystemKnowledge">No need of system knowledge</h5>
42 is designed from the ground up to avoid the need of any system knowledge:
A good 42 programmer just need to be good at designing code.
On the other side, to be a good programmer in other languages, you would also need to
know about shell commands, operative system conventions, environment variables, character encodings
and anything else usually connected with a specific operative system.
</li><li>
<h5 id="Towels">Towels!</h5>
Not all projects have the same requirements, and not all the developers have the same preferences.
Most languages are stuck with a single standard library,
that ends up being obsolete after a few years.
WComm At best, developers can use a linter to enforce a specific programming style.
In 42, towels cover the same role of the standard library,
but towels can be personalized, to fit better a given company, project or programmer.
Methods and classes can be added, removed or simply renamed.
This can be done while preserving compatibility with the all of the third party libraries.
The same features allowing programmatic refactoring of the towel can be used on smaller units of code,
allowing to reuse, compose and customize code in a very flexible way.
</li>

WComm <li><h5>Security by Modularization</h5>
WComm Third parties libraries could be (or become) adversarial. 
WComm In 42 Libraries are forced to use only explicitly provided resources,
WComm stopping library hackers.
WComm The software architecture of a 42 program can enforce that important actions
WComm are only performed when precise security conditions are met.
WComm </li><li>
WComm <h5>Performance by Abstraction</h5>
WComm Caching and parallelism are the two main ways to optimize code.
WComm Caching avoids doing the same operation over and over again,
WComm and Parallelism allows doing more things at the same time.
WComm Writing optimized code is hard, and many optimizations may break the code in unexpected
WComm  and hard to debug ways.
WComm In 42 supports correct by construction caching and parallelism.
WComm This means that those optimizations are easy to insert and are unable
WComm to impact the behaviour of the system
WComm </li><li>
WComm <h5>Customization by Composition</h5>
WComm In 42 code can be used, reused, composed and customized in a very flexible way.
WComm This allows to mandate dependencies and programming style from an architectural
WComm designer standpoint.

WComm <h5>Dependency hell is over</h5>
WComm In 42 has been conceived from the ground up so that 
WComm a large number of third party libraries, and even multiple 
WComm versions of the same library, can transparently coexists.
WComm This all works without the need of any external tool like Maven or Pip.
WComm </li><li>
WComm 
WComm <h5>No need of any operative system knowledge</h5>
WComm 42 is designed from the ground up to avoid the need of any system knowledge:
WComm A good 42 programmer just need to be good at designing code.
WComm On the other side, to be a good programmer in other languages, you would also need to
WComm know about shell commands, system conventions, environment variables, character encodings
WComm and anything else usually connected with a specific operative system.
WComm </li><li>
WComm 
WComm <h5>Bounded complexity</h5>
WComm Very abstract language features like Lambdas and Generics allows some mathematically 
WComm adept programmers to write very compact and reusable code,
WComm while being one of the biggest roadblocks for everyone else.
WComm 
WComm In 42 lambdas and generics are replaced with other, more intuitive features.
WComm </li><li>
WComm 
WComm 
WComm <h5>A typed language with flexible syntax</h5>
WComm 42 is a typed language, like Java and C#. This allows to catch errors earlier and, most importantly,
WComm allowing tools and libraries to support the developer since they can have
WComm  a rich static understanding of the code base.
WComm 42 have a flexible syntax, like python. This allows to elegantly express many different concepts.
WComm </li><li>
WComm 
WComm 
WComm <h5>Security by Modularization</h5>
WComm A secure program never does the wrong action. For example, it never commits to the database ill formed data.
WComm In 42 you can clearly specify what those actions are and what are the corresponding correctness criteria. The rest of the code will be unable to break those constraints.
WComm In this way the programmer can focus the majority of their time on writing correct software, while the security is guaranteed by the system.
WComm </li><li>
WComm 
WComm <h5>Performance by Abstraction</h5>
WComm Most of the performance issues are not about smart ways to generate ultra fast assembly.
WComm Most often they are about code repeating the same operations over and over again.
WComm It may be a poorly designed algorithm, abstraction mismatch, or maintenance spoiling your perfomance by slowly twisting assumptions.
WComm In 42 various forms of caching and automatic parallelism are proven semantically unobservable.
WComm This means that those optimizations are easy to insert and are unable
WComm to impact the behaviour of the system
WComm </li><li>
WComm 
WComm <h5>Customization by Composition</h5>
WComm Not all projects have the same requirements, and not all the developers have the same preferences. Most languages are stuck with a single standard library, that ends up being obsolete after a few years. At best, developers can use a linter to enforce a specific programming style.
WComm In 42, towels cover the same role of the standard library, but towels can be personalized, to fit better a given company, project or programmer.
WComm Methods and classes can be added, removed or simply renamed. This can be done while preserving compatibility with all of the third party libraries ecosystem.
WComm </li>
</ul>
WP
All those statements looks unbelievable given current programming wisdom.
<ul><li>
In the Wlink(tutorial_01Basics,guide), we explain the language, and we explain those statements and how they work.
</li><li>
You can also Wlink(download,`download 42') and try it yourself.
</li><li>
You can also engage with the open source GitHub <a href="https://github.com/ElvisResearchGroup/L42">repository</a>.
</li>
</ul>



<!--
42 is a new programming language,
whose name is inspired by The Hitchhiker's Guide to the Galaxy.
The language's goal is to allow the transparent cooperation and composition of millions of libraries at the same time, while providing a much higher security that any other widly used language.
WP
In 42 you will be able to import libraries just by naming them and to
compose them in your code without worrying about their dependencies.
Normally libraries are big monolithic things, coming from outside and manually imported/installed. The user has little control over
the library code, and the library has no influence over the importing context.
WP
In 42 libraries are usually much smaller and are first class entities, that can be manipulated as values, loaded, stored, modified, adapted, saved or simply used. You will write code that manipulate libraries, but more importantly you will use libraries that manipulate libraries in non-trivial ways.
Indeed in 42 manipulating libraries is so natural that is convenient to think of your own code as a library that you offer to yourself only.
WP
42 enforces a high level of abstraction, thus there is no concept of null or default initialization.
While is possible to write in a fully functional style in 42, usually 42 programs mix
WEmph(`mutable datastructures with aliasing')
and
WEmph(`deeply immutable data').
By using reference and object capabilities,
42 supports mutability and aliasing control, helping in mixing the two styles in an easy and natural way.

WP
Reference and object capabilities
enforce the correctness of caching, representation invariants and parallelism.
-->
</p></Div>


<Div id= "c"><p>
WTitle(Pure and fair Object Oriented language)
42 is a pure object oriented language,
where every value is an object. This means that: 
<ul>
<li>
Numbers are objects, 
so thay can have methods like Wcode(.sqrt()) or Wcode(.abs()). 
</li><li>
All operators are just method calls, thus Wcode(a + b) can be sugar for Wcode(`a.#plus0(b)'), 
and so on. Note how Wcode(`#plus0') is just an ordinary method name.
Any class that offers the method supports the operator.

</li><li>
Classes are objects, so when you write Wcode(Foo.bar()) you refer to the method Wcode(.bar()) offered by the object denoted by Wcode(Foo).
class objects are just objects, and you can also store them in local bindings if you wish, as in Wcode(x = Foo)
</li><li>
The code is an object, but only at the granularity of 
Libraries; that is, balanced pairs of curly brackets representing classes (or interfaces) with methods and nested libraries.
This is useful for meta-programming, which we will see later.

</li><li>
Differently from other pure object oriented languages, in 42
all objects have the same treatment, both syntactically and semantically.
For example Wcode(x =  S"Hello " ++ S"World")
This is a declaration for a local binding Wcode(x), using the string class Wcode(S) and the method operator Wcode(++),
used as string (or in general sequence/collection) concatenation.

Coming from another programming language, you may be surprised that we have to write Wcode(S) before Wcode("Hello ") and Wcode("World").
This extra verbosity is needed to provide fair treatment to all classes. (Wlink(`http://l42.is/tutorial.xhtml#BasicClasses',see more in Basic classes))
In 42 we do not give preferential treatment
to special objects/classes. In this sense, we consider
most other languages to be discriminatory.
They give priority to their "preferred" version of numbers and strings, and this puts into a position of unfair disadvantage library code trying to define its own kinds of numbers/strings.
Instead in 42 you may encounter strings like Wcode(Url"www.google.com") or
 Wcode(Email"Arthur.Dent@gmail.com").
The same for numeric classes: to talk about a street, 20 meters long, you would write
Wcode(streetLength= 20Meter).
Note that we write the class name after the number,
while usually the class name is before.
42 is a little incoherent in this point, both for better readability and because Wcode(Meter20) would be considered an identifier by the parser.
You may encounter Wcode(I), Wcode(Num), Wcode(Double), Wcode(Meter), Wcode(Kg), Wcode(Second), Wcode(Year), Wcode(PhoneNumber) and many other numeric classes.
</li></ul>

WTitle(A simple language)
We believe 42 is a simple language,
where all classes are final and interfaces are the only instrument to obtain subtyping.
Moreover, 42 does not offer many controversial features usually found in other programming
languages, including: 
(method) overloading; threading; numeric conversions and coercions; var-args; primitive types and automatic (un-)boxing;
inner classes; arrays; generics; scope-hiding of local variables/bindings, methods and fields;
closures/lambdas and null.

</p></Div>

WComm It may be a poorly designed algorithm, abstraction mismatch, or maintenance spoiling your performance by slowly twisting assumptions.
WComm Writing optimized code is hard, and many optimizations may break the code in unexpected and hard to debug ways.

WComm m4_include(`syntaxCompare.h')m4_dnl

WComm WTitle(`Now, dig into the tutorial!')

WComm Wlink(tutorial_01Basics,Let's board the Vogon ship)


m4_include(`../CommonHtmlDocumentation/footer.h')m4_dnl
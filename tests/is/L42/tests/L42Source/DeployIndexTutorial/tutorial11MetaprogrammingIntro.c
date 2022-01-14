WBigTitle(`Finally, Metaprogramming')

Metaprogramming is the most important feature of 42.
All the decorators that you have seen up to now are implemented with metaprogramming, 
which shows that 42 offers a good balance of freedom and safety.
WP

The main idea of 42 metaprogramming is that only library literals can
be manipulated.
Metaprogramming is evaluated top down, most-nested first.
Once a library literal has a name, it can not be independently metaprogrammed; but only influenced
by metaprogramming over the library that contains it.

WP

Wcode(Trait) is a decorator allowing to store code in a reusable format.
Wcode(Class) is a decorator that extracts the code from a trait. For example
OBCode
TraitMsg = Trait:{
  class method S msg() = S"Hello world" 
  }
TraitPrint = Trait:{
  class method Void printMsg() = Debug(this.msg())
  class method S msg()
  }
TraitPrintMsg = TraitPrint:TraitMsg //TraitPrintMsg is another trait
ClassPrintMsg = Class:TraitPrintMsg //produces usable code
ClassPrintMsg = Class:TraitPrint:TraitMsg //equivalent
ClassPrintMsg = {//also equivalent
  class method Void printMsg() = Debug(this.msg())
  class method S msg() = S"Hello world" 
  }
Main0 = ClassPrintMsg.printMsg() //prints Hello world
//Main0 = TraitPrintMsg.printMsg() //does not compile
CCode

In this code we show that Wcode(ClassPrintMsg) contains all the code of both Wcode(TraitPrint) and Wcode(TraitMsg).
Note how the abstract Wcode(class method S msg()) in Wcode(TraitPrint) is merged with the corresponding implemented method in Wcode(TraitMsg).

Traits allow us to merge code from different sources, as it happens with multiple inheritance.
However, traits are flattened: The code is actually copied in the result.

Traits are boxes containing the code and offering methods to manipulate such code.
A trait can be created by doing either Wcode(Trait(theCode)) or Wcode(Trait:theCode).
The trait object has a method Wcode(`.code()') returning the contained code.
Trait content can be composed using the operator Wcode(`:') (as shown above) or Wcode(`+').
For traits there is no difference in behaviour between Wcode(`:') and Wcode(`+'), but the operator precedence and associativity is different.

WP

Simply composing traits allows us to emulate a large chunk of the expressive power of conventional inheritance.
For example, in Java we may have an abstract class offering some implemented and some abstract methods.
Then multiple heir classes can extend such abstract class implementing those abstract methods.
The same scenario can be replicated with traits: a trait can offer some implemented and some abstract methods. Then multiple classes can be obtained composing that trait with some other code implementing those abstract methods.

OBCode
A = Trait:{/*implemented and abstract methods here*/
  method S exampleMethod1()
  method S exampleMethod2() = this.exampleMethod1()
  }
B = Data:Class:A:{/*more code here, reusing the code of A*/
  method S exampleMethod1() = S"Hi"
  method S exampleMethod3() = this.exampleMethod2()
  }
CCode

WTitle((2/5)Trait composition: methods, nested classes and state.)
Trait composition merges members with the same name. As shown above, this allows method composition.
Also nested classes can be merged in the same way: nested classes with the same name are recursively composed, as shown below:
OBCode
T1 = 
  Trait({
    Foo = {
      method S hello()
      method S helloWorld() = this.hello()++S" World"
      }
    })
  +
  Trait({
    Foo = {
      method S hello() = S"Hi" 
      }
    })
//it is equivalent to
T1 = 
  Trait({
    Foo = {
      method S hello() = S"Hi" 
      method S helloWorld() = this.hello()++S" World"
      }
    })
CCode

WTitle(`Fields?')
But what about fields? how are fields and constructors composed by traits?
The answer to this question is quite interesting:
In 42 there are no true fields or constructors; they are just abstract methods serving a specific role.

That is, the following code declares a usable Wcode(Point) class:
OBCode
TraitGeometryPoint = Trait:{
  Point = {
    read method Num x()
    mut method Void x(Num that)
    read method Num y()
    mut method Void y(Num that)
    class method mut This (Num x, Num y)
    method This double() = \(x=this.x()*2\, y=this.y()*2\)
    }
  }
Geometry1 = Class:TraitGeometryPoint //declaring class Geometry1
..
  imm p = Geometry1.Point(x=3\, y=4\)
  p2 = p.double()//example usage
CCode
That is, any Wcode(read), Wcode(imm) or Wcode(mut) no-arg abstract method can play the role of a getter for a correspondingly named field, and any abstract Wcode(class) method can play the role of a factory, where the parameters are used to initialize the fields.
Finally, Wcode(mut) methods with one argument called Wcode(that) can play the role of a setter.
Candidate getters and setters are connected with the parameters of candidate factories by name.
To allow for more then one getter/setter for each parameter, getters/setters names can also start with any number of Wcode(#).
WBR
We call those abstract methods WEmph(Abstract State Operations).
In Java and many other languages, a class is abstract if it has any abstract methods.
In 42, a class is coherent if its set of abstract state operations ensure 
that all the callable methods have a defined behaviour; this includes the initialization of all the usable getters. 
WBR
In more detail, a class is coherent if:
<ul><li>
All candidate factories provide a value for all candidate getters, and all the types of those values
agree with the return type of the corresponding getters.
The parameter type of all candidate setters agrees with the return type of the corresponding getters.
</li><li>
Additionally, any non-class method can be abstract if none of the candidate factories return a value whose modifier allows to call such a method.
</li></ul>
In particular, this implies that if a class has no candidate factories, 
any non class method may be abstract, as shown below:
OBCode
Foo = {
  class method Void bar()=Debug(S"coherent!") 
  method I answer(Time long)//abstract no problem!
  }
Main = Foo.bar()
CCode

A main can call class methods only on coherent classes that only depend from other coherent classes, thus for example
OBCode
Foo0 ={class method Void bar0()}
Foo = {class method Void bar()=Foo0.bar0() }
Main = Foo.bar()
CCode
The decorators Wcode(Class) and Wcode(Data) also checks for coherence: the following application of Wcode(Class)
OBCode
T1 = Trait:{class method Void bar()}
C1 = Class:T1
CCode
would fail with the following message: WEmph(The class is not coherent. Method bar() is not part of the abstract state).
We can use Wcode(Class.Relax) and Wcode(Data.Relax) to suppress this check when needed.
Indeed Wcode(myTrait.code()) behaves exactly as Wcode(Class.Relax:myTrait).
WP
Earlier in this tutorial we have shown code like
OBCode
Person = Data:{S name, var I age, mut S.List friends}
CCode
In that code Wcode(S name) looks like a conventional field declaration, but it is simply syntactic sugar for the following set of methods:

OBCode
Person = Data:{
  read method S name()
  read method I age()
  mut method Void age(I that)
  read method read S.List friends()
  mut method mut S.List #friends()
  }
CCode
Then, Wcode(Data) will discover that Wcode(name), Wcode(age) and Wcode(friends) are good candidate fields and will add factories
OBCode
class method mut This(S name, I age, mut S.List friends)
class method This #immK(S name, I age, S.List friends)
CCode
and a lot of other utility methods.


WTitle((3/5)Nested Trait composition: a great expressive power.)

Composing traits with nested classes allows us to merge arbitrarily complex units of code.
In other languages this kind of flexibility requires complex patterns like dependency injection, as shown below:

OBCode
TraitGeometryPoint = Trait:{/*Same as before*/}

TraitGeometryRectangle = Trait:{
  Point = {method Point double()} // Declare only the necessary methods
  Rectangle = {
    method Point upLeft()
    method Point downRight()
    method This This(Point upLeft, Point downRight)
    method This double() = \(
      this.upLeft().double()
      this.downRight().double()
      )
    }
  }
...
Geometry2 = Class:TraitGeometryPoint:TraitGeometryRectangle
CCode
As you can see, we can define more code using Wcode(Point) while only repeating the needed dependencies.
We will use this idea in the following, more elaborated scenario:
Bob and Alice are making a video game. In particular, Alice is doing the code related to loading the game map from a file.

OBCode
Game = { //example game code, NOT MODULARISED
  Item = {interface 
    Point point
    Item hit
    }
  Rock = {[Item]
    Num weight
    class method This(Point point, Num weight)
    method Item hit() = \(point=this.point(), weight=this.weight()/2\)
    }
  Wall = {[Item]
    Num height
    class method This(Point point, Num height)
    method Item hit() = Rock(point=this.point(), weight=..)
    }
  Map = {..//map implementation by Bob
    class method mut This empty() = ..
    read method Item val(Point that) = ..
    mut method Void set(Item that) = ..
    }
  class method Void #$run() = /*..*/this.load(..)/*..*/ //implemented by Bob
//------------------------------------//Anything under this line is implemented by Alice
  class method Map load(mut FS files, S fileName) = (//Alice writes load(_)
    map = Map.empty()
    ..//read from file and divide in lines,
    for line in lines ( this.load(map=map, line=line) )
    )
  class method Void load(mut Map map, S line) = (
    ..//example line: S"Rock 23 in 12, 7"
    if line.startsWith(S"Rock") (
      map.set(Rock(point=\(x=.., y=..), weight=..))
    if line.startsWith(S"Wall")) ..
    ..
    )
  }
Main = (.. Game.#$run() ..)
CCode

As you can see from the non modularized code above, Alice code is tightly connected with Bob code:
She have to instantiate Wcode(Map) and all the kinds of Wcode(Item)s. In a language like Java, Alice would need to write her code after Bob have finished writing his, or they would have to agree to use dependency injection and all the related indirections.

Instead, in 42 they could simply factorize their code into two independent traits:

OBCode
TraitBob = Trait:{ //all code is as before, but load is abstract
  Item = {interface 
    Point point
    Item hit
    }
  Rock = {[Item]
    Num weight
    class method This(Point point, Num weight)
    method Item hit() = \(point=this.point(), weight=this.weight()/2\)
    }
  Wall = {[Item]
    Num height
    class method This(Point point, Num height)
    method Item hit() = Rock(point=this.point(), weight=..)
    }
  Map = {..
    class method mut This empty() = ..
    read method Item val(Point that) = ..
    mut method Void set(Item that) = ..
    }
  class method Void #$run() = /*..*/this.load(..)/*..*/
  class method Map load(mut FS files, S fileName)
  }

TraitAlice = Trait:{//here we just repeat the used signatures
  Item = {interface }
  Rock = {[Item] class method This(Point point, Num weight)}
  Wall = {[Item] class method This(Point point, Num height)}
  Map  = { class method This empty(),  mut method Void set(Item that)}
  
  class method Map load(mut FS files, S fileName) = (//the actual Alice code
    map = Map.empty()   //is untouched; it can stay completely identical
    ..
    for line in lines ( this.load(map=map, line=line) )
    )
  class method Void load(mut Map map, S line) = (
    ..//example line: S"Rock 23 in 12, 7"
    if line.startsWith(S"Rock") (
      map.set(Rock(point=\(x=.., y=..), weight=..))
    if line.startsWith(S"Wall")) ..
    ..
    )
  }
Game=Class:TraitBob:TraitAlice
Main = (.. Game.#$run() ..)
CCode

Now that the code of Alice and Bob are separated, they can test their code in isolation:

OBCode
MockAlice = Class:TraitAlice:{
  Item = {interface, S info}
  Rock = {[Item]
    class method This(S info)
    class method This(Point point, Num weight) = 
      \(info=S"Rock: %point -> %weight")
    }
  Wall = /*..*/
  Map = {
    var S info
    class method mut This (S info)
    class method mut This empty() = \(S"")
    mut method Void set(Item i) = this.info(\info++i.info()++S.nl())
    }
  }
TestAlice = (
  files=FS.#$()
  {}:Test"justARock"(
    actual=MockAlice.load(files=files, fileName=S"justARock.txt")
    expected=S"""
      |Rock: Point(5,6) -> 35
      """)
  {}:Test"rockAndWall"(
    actual=MockAlice.load(files=files, fileName=S"rockAndWall.txt")
        expected=S"""
      |Rock: Point(x=5, y=6) -> 35
      |Wall: Point(x=1, y=2) -> 10
      """)
  ..//more tests here
  )
CCode

WTitle((4/5)Typing considerations)

Object oriented programs often contain entangled and circular type definitions.
For example, strings Wcode(S) have methods Wcode(I size()) and Wcode(Bool isEmpty()), while
both Wcode(I) and Wcode(Bool) offer a Wcode(S toS()) method.
That is, while circular values are a double edged sword (useful but dangerous), circular/recursive types are unavoidable even in simple programs.
So, how do recursive types interact with metaprogramming?
Path names can only be used in execution when the corresponding nested class is fully typed,
thus the following example code would not work:
OBCode
Foo = { class method Bar bar(Bar that)=that }
Foos = Collection.list(Foo)
Bar = { class method Foos foos(Foos that)=that }
CCode
We can not start computing Wcode(Foos) since Wcode(Foo) depends from Wcode(Bar), that is defined later.
Swapping lines would not help, since Wcode(Bar), in turn, depends from Wcode(Foos).
Later we will learn how to overcome this issues and still generate code with the intended structure.
As shown below, library literals can instead be manipulated even if
they are not fully typed.
OBCode
Person = Data:{S name, I age, Dog dog}
Dog = Data:{S name, Person owner}
CCode
Here Wcode(Person) can be computed even if Wcode(Dog) is still unavailable.
However, such a manipulation must happen in place: we can not use traits to reuse untyped code; that is, the following would not work:
OBCode
TraitPerson = Trait:Data:{S name, I age, Dog dog}
Person = Class:TraitPerson //fails
Dog = Data:{S name, Person owner}
CCode
Wcode(TraitPerson) can not be used before Wcode(Dog) is defined.
WP
This also allows us to avoid defining many redundant abstract methods.
Consider the following working code:
OBCode
TraitPrintName = Trait:{
  class method S name()//abstract
  class method Void printName() = Debug(this.name())
  }
Bob = TraitPrintName:{
  class method S name() = S"Bob"
  class method Void printTwice() = (
    this.printName()
    this.printName()
    )
  }
Main = Bob.printTwice() //prints "Bob" twice
CCode
This code is allowed even if Wcode(Bob) does not contain an abstract definition for Wcode(printName()).
This feature is often used by 42 programmers without even recognizing it, but it is brittle:
when method calls are chained (as in Wcode(a.b().c())) or when binary operators or type inference are involved, the system needs to be able to guess the return type of those missing methods.

WTitle((5/5)Metaprogramming summary)
Here we have introduced the way that 42 handles metaprogramming and code reuse.
We focused on Wcode(Class) and Wcode(Trait).
Composing code with Wcode(:) or Wcode(+) we can partition our code-base in any way we need,
enabling simple and natural testing and code reuse patterns.
WBR
When reusing code, we have to be mindful of missing types and missing methods. Structuring the code-base to avoid those issues will require some experience, and is indeed one of the hardest parts about writing complex 42 programs.

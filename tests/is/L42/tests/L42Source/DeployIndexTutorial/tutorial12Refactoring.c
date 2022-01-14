WBigTitle(`Traits and rename')

WTitle((1/5)An introduction to Programmatic Refactoring)

Traits allow us to programmatically rename methods and nested classes to other names.
Consider the following example code, defining trait Wcode(Code1), containing many nested classes:
the interface Wcode(A), the class Wcode(B) implementing Wcode(A),
the class Wcode(C), that is similar to Wcode(B) but does not implements Wcode(A)
and finally the class Wcode(D) that uses both Wcode(B) and Wcode(C):

OBCode
Code1 = Trait:{
  A = {interface,  method S m()}
  B = {[A],        method S m()=S"Hi"                    class method This()}
  C = {            method S m()=S" world"                class method This()}
  D = {            method S callBoth()=B().m()++C().m()  class method This()}
  }
Concrete1 = Class:Code1
Main1 = Debug(Concrete1.D().callBoth())
CCode
We can extract the code into class Wcode(Concrete1), and then use it to print 
Wcode("Hi World").
We show below how we can use the operator Wcode([_]) to programmatically rename elements of Wcode(Code1) while preserving the semantic:
OBCode
Concrete2 = Class:Code1['A.m()=>'A.k()]
Concrete2 = {//equivalent to just writing the following directly:
  A = {interface,  method S k()}
  B = {[A],        method S k()=S"Hi"                    class method This()}
  C = {            method   m()=S" world"                class method This()}
  D = {            method S callBoth()=B().k()++C().m()  class method This()}
  }
Main2 = Debug(Concrete2.D().callBoth()) //still prints "Hi World"
CCode
As you can see above, the mapping Wcode('A.m()=>'A.k()) renamed all
the occurrences of Wcode(A.m()) into Wcode(A.k()). This includes all the
declarations refining such method and all the usages of such method, either from the
static type Wcode(A) or from any nested class implementing Wcode(A).
On the other side, you can see how Wcode(C().m()) was not renamed; the rename is type driven.
We can also rename multiple names at once; in this way we can even swap names:

OBCode
Concrete2 = Class:Code1[
  'A.m()=>'A.k();
  'B.#apply() =>'B.of();
  'C=>'D;
  'D=>'C;
  ]
Concrete3 = {//equivalent to just writing the following directly:
  A = {interface,  method S k()}
  B = {[A],        method S k()=S"Hi"                       class method This of()}
  C = {            method S callBoth()=B.of().k()++D().m()  class method This()}
  D = {            method   m()=S" world"                   class method This()}
  }
Main3 = Debug(Concrete3.C().callBoth()) //still prints "Hi World"
CCode
Note how the call Wcode(B()) is now replaced with Wcode(B.of()).
Wcode(#apply()) is the extended name for the method with the empty name.
Also binary operator methods can be renamed by using their extended name;
The complete list of extended names for binary operators is discussed later, but you can also just use the overview feature to see them in any compiled class.
The Wcode(') sign is a syntactic sugar similar to the Wcode(\);
indeed Wcode('Foo.bar(x)) is equivalent to
Wcode(\"Foo.bar(x)"); where the text after the Wcode(') has strict syntactic restrictions, requiring it to be either a valid path (as Wcode(Beer.Foo) or Wcode(This), for example), a valid method selector, or 
a path followed by a method selector.
A method selector can also be followed by an argument name, as in Wcode('Foo.bar(x).x)
In 42 programmatic refactoring and other tasks requiring us to express paths and method selectors are very common, and writing Wcode(myTrait['A=>'B]) is so much more convenient that writing Wcode(myTrait[Name"A"=>Name"B"]).

WTitle((2/5) Programmatic Refactoring: all kinds of operations)
WTitle(Single)
Programmatic refactoring of nested classes is transitive by default.
All the nested classes are going to be renamed together with the renamed root.
The code below shows how to specify a single rename instead:
OBCode
MultiLevel = Trait:{
  A = {
    class method S hi() = S"hi"
    B = {class method S world() = S"world"}
    }
  C = {
    class method Void print() = Debug(A.hi()++D.space()++A.B.world())
    D = { class method S space() = S" " }
    }
  }
MultiConcrete = Class:MultiLevel[ single='C=>'K; 'A=>'D.H ]
MultiConcrete = {//equivalent to just writing the following directly:
  D = {
    H = {
      class method S hi() = S"hi"
      B = {class method S world() = S"world"}
      }
    }
  C = {
    D = { class method S space() = S" " }
    }
  K = {
    class method Void print() = Debug(D.H.hi()++C.D.space()++D.H.B.world())
    }
  }
Main4 = MultiConcrete.K.print() //still prints "hi world"
CCode
As you can see, we did a single rename Wcode('C=>'K) and a transitive rename Wcode('A=>'D.H).
Since there were nested classes inside of Wcode(C), the single rename has left a shell of
Wcode(C) in place so that the nested Wcode(C.D) could stay in position.
WP

We can also rename Wcode('This).
For example, with the code below we can make the original top level into a nested class, and the nested class Wcode(NewTop) into the top level:
OBCode
Res=Class:Trait({
  method S originalTop()=S"originalTop"
  NewTop ={method S newTop()=S"newTop"}
  })[single='This=>'OriginalTop;single='NewTop=>'This]
CCode
WP
On the other side, self rename, for example Wcode('C=>'C) is usually an error, and thus it will raise an exception. However, we can silence such errors and turn self rename into a no-op by using
Wcode(myCode[ignoreSelfRename='C=>'C]) or Wcode(myCode[ignoreSelfRenameSingle='C=>'C]).

WTitle(Hide)
The code below shows how to  hide a method or a class:
OBCode
MultiConcrete2 = Class:MultiLevel[ hide='A; hide='D.space() ]
MultiConcrete2 = {//equivalent to just writing the following directly:
  A::3 = {
    class method S hi::4() = S"hi"
    B::5 = {class method S world::6() = S"world"}
    }
  C = {
    class method Void print() = Debug(A::3.hi::4()++D.space::7()++A:3.B::5.world::6())
    D = { class method S space::7() = S" " }
    }
  }
Main5 = MultiConcrete2.C.print() //still prints "hi world"
CCode

Private members in 42 are obtained by relying on unique unguessable numbers:
These names do not show up in the outline and can not be invoked by user code; moreover those numbers are automatically renamed to avoid clashing during code composition; overall, those numbers are completely invisible for the user of the code.
While it is possible to manually use unique numbers, it is so much more convenient to write open code and then seal it later.

WTitle(Clear)
Hidden code is still part of the result, but it is no more accessible.
Symmetrically, cleared code is not part of the result, but its entry point is still accessible, but abstract; clearing code allows us to override already defined behaviour, as shown below:

OBCode
MultiConcrete3 = Class:MultiLevel[ clear='A ]:{
  A = {
    class method S hi() = S"hello"
    B = {class method S world() = S"42"}
    }
  }
Main6 = MultiConcrete3.C.print() //now prints "hello 42"
CCode

Of course, Wcode(clearSingle='A) would clear only the nested class Wcode(A) and not also Wcode(A.B).


WTitle(Soft rename)
Clearing code allows us to override code by removing code.
This is different with respect to what happens with overriding in most languages, where the former code still exists and can be invoked, for example with Wcode(super).

In 42 we can easily emulate super by using Wcode(->) instead of Wcode(clear); the code below shows how Wcode(->) can be used on both methods and nested classes:
OBCode
MultiConcrete4 = Class:MultiLevel[ 'A->'SuperA; 'C.D.space()->'C.D.superSpace() ]:{
  SuperA = { class method S hi() B={class method S world()}}
  A = {
    class method S hi() = S"[%SuperA.hi()]"
    B = {class method S world() = S"[%SuperA.B.world()]"}
    }
  C={
    D={
      class method S superSpace()
      class method S space()=S"[%this.superSpace()]"
      }
    }
  }
Main7 = MultiConcrete4.C.print() //now prints "[hi][ ][world]"
CCode

Note how in this case we explicitly declare Wcode(SuperA.hi()), Wcode(SuperA.B.world()) and
Wcode(C.D.superSpace()) in the composed code, even if they are already present in the 
result of 
Wcode(MultiLevel[ 'A->'SuperA; 'C.D.space()->'C.D.superSpace() ]).

We will soon show a way to avoid redeclaring them, but our experience programming in 42 suggests that when only a few methods are involved, the code is often more clear and easier to understand by redeclaring them.


WTitle(Redirect)
Finally, programmatic refactoring allows us to rename a nested class into an externally declared class. We call this kind of rename WTerm(redirect).
This also provides a simple encoding for generics.
Consider the following code:
OBCode
BoxTrait = Trait:Data:{
  T = {}//declaring a nested class called 'T'
  T that //a field of type T called 'that'
  }
NumBox = Class:BoxTrait['T=>Num]
Main = (
  myBox = NumBox(3Num)
  Debug(myBox)
  Num n = myBox.that()
  Debug(n)
  )
CCode
Note how we wrote Wcode(BoxTrait['T=>Num]) and not Wcode(BoxTrait['T=>'Num]):
In Wcode('T=>Num), Wcode(Num) is the numeric class defined outside.
If we instead wrote Wcode('T=>'Num),
Wcode('Num) would be the class nested inside Wcode(BoxTrait) and called Wcode(Num).

Generics classes are straightforward to implement with redirect, and indeed
Wcode(Collection) uses the redirect operator internally.
WP
We can redirect multiple nested classes at the same time, and we 
can put arbitrary constraints on the structural type of the destination types simply by specifying abstract methods and implemented interfaces.
Consider the following example:

OBCode
Operation = Trait:{
  Elem = {Index myIndex}
  Index = {[HasToS]
    method Num eval(Elem that)
    }
  class method Elem best(Elem e1,Elem e2) = {
    res=e1.myIndex().eval(e2)>e2.myIndex().eval(e1)
    if res return e1
    return e2
    }
  }
Adventurer = Data:{S name, Num attack, Level level}
Level = Data:{
  Num exp
  S profession
  method Num eval(Adventurer that) = {..}
  }

DuelOperation = Class:Operation
  ['Elem.myIndex()=>'Elem.level()]
  ['Elem=>Adventurer;'Index=>Level]

Main= /*..*/ DuelOperation.best(e1=luke e2=gandalf) /*..*/
CCode

Here we can define a generic Wcode(Operation) working on Wcode(Elem) and Wcode(Index).
Elements must have an Wcode(Index myIndex()) method and indexes must
implement Wcode(HasToS) and offer a Wcode(method Num eval(Elem that)).
In a language like Java with F-Bound polymorphism, we would have been required to rely on a Wcode(HasEval<Elem>) interface, while in 42 we can simply list the required operations.

Note how  before specifing the actual types for Wcode(Elem) and Wcode(Index) we can 
tweak the Wcode(Operation), so that we can accept the Wcode(level()) method instead of the
Wcode(myIndex()) one.

Redirect is very powerful; checking also subtype relationships between redirected members, as shown below:
OBCode
GeometryOperation = Trait:{
  Shape = {interface}
  Triangle = {[Shape]
    class method This (Point p1,Point p2,Point p3)
    }
  Line = {[Shape]
    Point p1, Point p2
    class method This (Point p1,Point p2)
    }
  class method Triangle reorganize(Line base,line extra) = 
    Triangle(p1=base.p1(), p2=base.p2(), p3=extra.p1())
  }
CCode
Note how we can also require class methods on the redirect nested classes.
Overall, the whole philosophy of generic programming is different in 42:
instead of raising the level of abstraction and designing classes with type parameters,
we just design normal classes with nested classes, that just so happens to be fully abstract.
Those classes will represent external dependencies.
Then we can redirect those nested classes onto others.

WTitle((3/5) Different ways to supply missing dependencies)
As we have seen, in 42 it is convenient to write self contained code, where the dependencies 
can be specified as nested classes with abstract methods.
In 42 there are three different ways to satisfy those dependencies:
<ul>
<li>
Sum:
We can compose two traits with the operators Wcode(:) or Wcode(+) to provide some of the missing method implementations.
</li>
</ul>
OBCode
Trait({
  A = { method I a() }
  B = { method I b(A a)=a.a() }
  })
+Trait({
  A = Data:{ I a }
  })
CCode
<ul>
<li>
Redirect:
We can rename a class to an external one 
</li>
</ul>
OBCode
Foo = Data:{ I a }
Trait({
  A = { method I a() }
  B = { method I b(A a)=a.a() }
  })['A=>Foo]
CCode
<ul>
<li>
Rename:
We can rename a member to another member in the same unit of code:
</li>
</ul>
OBCode
Trait({
  A = { method I a() }
  B = { method I b(A a)=a.a() }
  C = Data:{ I a }
  })['C=>'A]
CCode

This last solution works like the sum, but is happening inside of the a single unit of code.
If this inner sum is successful, it behaves as trait composition would.
There are a few corner cases where this inner sum will fail; they involve details of composing classes with interfaces and adding methods to interfaces.


WTitle((4/5) Introspection and Info)
It is also possible to programmatically query the code structure and make decisions about it.
For example
OBCode
Larger = {class method Trait (Trait t1, Trait t2)={
  if t1.info().methods().size()>t2.info().methods().size() return t1
  return t2
  }}
MyClass = Class:Larger(t1=ATrait, t2=AnotherTrait)
CCode

The method Wcode(Trait.info()) returns an instance of class
Wcode(Info), offering methods to query all the visible information about
the trait code.
The class Wcode(Info) represents a nested class, and also contains 
classes representing other kinds of code elements:
Wcode(Method), Wcode(Type) and Wcode(Doc).
WBR
Wcode(Info) contains a lot of useful methods.
Some of those methods query information about the class and how it is used in its code unit: for example the method Wcode(watched()) returns the list of types whose private members are used.
WBR
A class that is watched can not be cleared. Indeed, all the possible errors of programmatic refactoring can be predicted by relying on the methods of Wcode(Info).
WBR
Wcode(Trait({..}).info()) provides an Wcode(Info) for a library literal and
Wcode(Info(..)) provides an Wcode(Info) for a Wcode(class Any).
For example, while 
Wcode(Trait({ method S foo()}).info()) can be used to know about this Wcode(foo()) method, 
Wcode(Info(S)) can be used to get information about the Wcode(S.size()) method.


WTitle((5/5)Programmatic refactoring summary)
<ul><li>
Many kinds of operations can be performed on code
</li><li>
Rename, as for Wcode('A=>'B) or Wcode('A.foo()=>'A.bar()),
is used to rename all the occurrences of a member into another name or
form, for the sake of the final user.
</li><li>
Soft Rename, as for Wcode('A->'B) or Wcode('A.foo()->'A.bar()),
only moves the declaration. It leaves in place all the usages and an abstract version of the original signature.
</li><li>
Clear, as for Wcode(clear='A) or Wcode(clear='A.foo()),
removes the implementation and all the private details of a member. It leaves in places all the usages and an abstract version of the original signature.
</li><li>
Hide, as for Wcode(hide='A) or Wcode(hide='A.foo()),
renames all the occurrences of a member into an uniquely named one.
This new name is now completely invisible from outside the code unit.
</li><li>
Redirect, as for Wcode('A=>Num),
redirects all the usages of a nested class into an externally declared one.
The internal declaration is simply trashed.
</li></ul>
Finally, Wcode(Info) allows us to explore the shape of code as metadata.


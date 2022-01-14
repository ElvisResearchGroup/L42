WBigTitle(`Keep control:  Modifiers, kinds of references and objects')

WTitle(`(1/5)Kinds of objects')
In object oriented languages, objects can form complex networks of dependencies by referring to each other using their fields.
The Reachable Object Graph (ROG) of a given object is the set of all objects reachable from it, including itself.
WP
An object is WEmph(mutated) if a field of an object in its ROG is WEmph(updated).
A WTerm(mutable object) is an object that can be mutated.
The 42 type system is able to ensure that some objects can not be mutated. We call those WTerm(immutable objects).

All instances of Wcode(Point) are immutable.
The ROG of a Wcode(Point) object contains the Wcode(Point) itself and the Wcode(Num) coordinates.
Fields Wcode(x) and Wcode(y) can not be updated and the Wcode(Num) objects are immutable.

Immutable objects are very easy to use but may be inadequate when representing entities whose state can change across time.
WP
Let's now define a mutable Wcode(Animal), whose location can be updated:
OBCode
Animal = Data:{
  var Point location //getter: this.location()
                     //setter: this.location(newVal)
  mut method
  Void run() =
    this.location(\location.add(x=20Num))//update the field with a new point
    //here \location == this.location()
  }
CCode
There are two new keywords used here: 
<ul><li>
The Wcode(location) field is Wcode(var).
  This is called a WTerm(variable field): a field that can be WEmph(updated) by calling a setter.
  Non-variable fields can not be updated.
</li><li>
Wcode(run()) is a Wcode(mut method).
WBR
We have seen a Wcode(class method) already, and we have seen methods 
such as Wcode(add(x)) and Wcode(add(y))
showing no modifier;
they implicitly have the default modifier Wcode(imm).
Similarly, whenever a type does not specify a modifier,
it has the default modifier Wcode(imm).
WBR
  Wcode(mut) methods can mutate the Wcode(this) object. If you have experience with C++
  you can see the contrast with const methods.
  Immutable (default) methods works only on immutable Wcode(this) objects.
  Later, we will see much more about modifiers.
</li></ul>

WP
As you see, we are using the Wcode(add(x)) method from before.
Also notice that we are calling the setter Wcode(location(that)) without providing the parameter name.
While this is usual in other languages, in 42 parameters are selected by name.
Sometimes writing down all the parameter names can get tedious.

If the first parameter is called Wcode(that), we can omit it:
Writing Wcode(a.b(that=c)) is equivalent to writing Wcode(a.b(c)).
This works also for methods with multiple parameters, if the first one is called Wcode(that).
Writing Wcode(a.b(that=c, x=d)) is equivalent to writing Wcode(a.b(c, x=d)).
WP
We can use Wcode(Animal) by writing, for example: 

OBCode
mut Animal dog1 = Animal(location=\(x=0\, y=0\))
dog2 = Animal(location=\(x=0\, y=0\)) //type 'mut Animal' inferred for dog2
dog2.run()
CCode

WTitle(`(2/5)Interaction between mutable and immutable')

We now explore some interaction between mutable and immutable objects.
OBCode
Animal = Data:{
  var Point location
  mut Points path
  mut method
  Void move() = (
    this.location(\path.left())
    this.#path().removeLeft()
    )
  }
CCode
Here we use Wcode(mut Points path) to denote a mutable list of points.
Note the absence of Wcode(var); this is conceptually similar to a Wcode(Points * const path;) in C++  or Wcode(final Points path;) in Java.
To contrast, the declaration Wcode(var Point location) is similar to
Wcode(Point const * location;) in C++  or Wcode(ImmPoint location;) in Java, for an opportune Wcode(ImmPoint) class.

Wcode(mut) references always refer to mutable objects.
Wcode(imm) references always refer to immutable objects.

Fields can be declared Wcode(var) independently from their modifier:
In the code above, you can see that Wcode(var Point location) is a Wcode(var) field of Wcode(imm) type.
On the other hand, 
Wcode(mut Points path)
is a non-Wcode(var) field of Wcode(mut) type.

WP
The method Wcode(move())
first uses the Wcode(location(that)) setter method to update the Wcode(location) field
with the Wcode(imm Point) leftmost element of the field Wcode(mut Points path).
By the way, collections in Wcode(AdamsTowel) are primarily designed to store and retrieve
immutable objects; later we will show also how to manipulate mutable ones.
WBR
The method then uses the Wcode(`#path()')
 WEmph(exposer) method and 
the Wcode(removeLeft()) method to mutate the list of points.
Both exposers and getters provide access to the value of a field;
exposers are used to access the values of mutable fields.
Exposers should be used with care: 
long term handling of references 
to (parts of) a mutable object could cause 
spooky action at a distance.
WBR
In general, methods starting with `#' should be used with care.
WP
This code models an animal following a path. It can be used like this:
OBCode
zero = Point(x=0\, y=0\)
ps1  = Points[\(x=12\, y=20\); \(x=1\, y=2\)]
ps2  = Points[zero; \(x=1\, y=2\)]
dog1 = Animal(location=zero, path=ps1)
dog2 = Animal(location=zero, path=ps2)
dog1.move()
dog2.move()
CCode

In this code the first dog goes to 12: 20.
The second dog goes to 0: 0. 

This code involves a mutable animal with a mutable field. This is often
a terrible idea, since its behaviour may depend on aliasing:  what happens if two dogs follow the same path?
OBCode
zero = Point(x=0\, y=0\)
ps   = Points[\(x=12\, y=20\); \(x=1\, y=2\) ]
dog1 = Animal(location=zero, path=ps)
dog2 = Animal(location=zero, path=ps)
dog1.move()
dog2.move()
CCode
The first dog moves and consumes the path for the second one as well.
That is, the first goes to 12: 20 and the second goes to 1: 2.

This is because Wcode(Animal) is WEmph(deeply mutable): a mutable object with mutable fields. 
An amazing amount of bugs is caused by deep mutability.

Note that we are using the exposer method Wcode(`#path()')
in a safe pattern: it is only called over Wcode(this), and the returned reference does not leak out of the method.
The problem here arises since the object was shared to begin with. 

WTitle(`(3/5)Capsules:  Keep aliasing graphs untangled')

In 42 we can change Wcode(Animal) to prevent this aliasing issue.
OBCode
Animal = Data:{
  var Point location
  capsule Points path
  mut method
  Void move() = (
    this.location(\path.left())
    this.removeLeftPath()
    )
  @Cache.Clear class method
  Void removeLeftPath(mut Points path) =
    path.removeLeft()
  }
CCode
Now we use the modifier Wcode(capsule); this requires the value of the field to be encapsulated.
Immutable objects are also encapsulated since
they do not influence aliasing, so they are free from aliasing limitations.
The Wcode(capsule)
 modifier WEmph(forces) the users to provide Wcode(capsule) values,
 and WEmph(ensures)
 that instances of Wcode(Animal) have WTerm(encapsulated state);
 that is, the values of all fields in an Wcode(Animal) are encapsulated.
WBR
A mutable object with encapsulated state can only be mutated by calling one of its methods.
This allows for the same kind of local reasoning as if all of the fields were immutable.
WP
A WEmph(capsule mutator) is a class method whose first parameter is the capsule field as Wcode(mut).
It is a way to mutate the value of a capsule field without exposing it.
Wcode(Data) recognizes only the methods annotated with Wcode(@Cache.Clear) as capsule mutators.
Those methods can then be safely accessed as instance methods with the same name.
WBR
The annotation is called Wcode(@Cache.Clear) because
capsule mutators also clear all the object based caches. Automatic caching is one of the coolest features of 42 and we will explore it later in this tutorial.
WP
Note that we cannot have Wcode(mut) exposers (Wcode(#path())) for capsule fields:
other code could keep those references and then 
mutate the ROG of the field,
breaking local reasoning about Animals.

With Wcode(capsule Points path), we are forced to initialize two animals using different paths:
OBCode
zero = Point(x=0Num, y=0Num)
capsule Points ps = Points[\(x=12\, y=20\);\(x=1\, y= 2\)]
dog1 = Animal(location=zero, path=ps)
//dog2= Animal(location=zero, path=ps) Does not compile
dog2 = Animal(location=zero, path=\[\(x=12\, y=20\); \(x= 1\, y=2\)])
dog1.move()
dog2.move()
CCode
where the Wcode(ps) local binding is Wcode(capsule); 
it can satisfy the Animal.path requirement, but it can be used only once.
Wcode(dog2)
has to use another capsule. It is okay to just write the object creation in place as is done.
Alternatively, lists offer a Wcode(clone()) method,
so in this case we could write

Wcode(dog2= Animal(location: zero, path: dog1.path().clone()))


WTitle(`(4/5)Handle mutability')

WTitle(`Immutable objects of any class')

How can we get an immutable Wcode(Animal)?
When an Wcode(Animal) is created using Wcode(Animal(location=_,path=_)) we create a Wcode(mut Animal).

In most cases you can promote such reference to immutable/capsule; just make the type of the local binding explicit.
 The type system will take care of the rest.
If a reference can not be safely promoted to immutable/capsule, you may have to clone some data or to refactor your code.
OBCode
mut Animal dog1 = Animal(__) //no promotion here
Animal dog2 = Animal(__) //promotion mutable->immutable
dog1.move()
//dog2.move()  //ill-typed, requires a mut Animal
CCode

We will not explain in this tutorial the exact rules for promotion, but the main idea is that if the initialization expression uses local bindings in a controlled/safe way, then promotion can be applied.
For example, a mutable expression using only capsule or immutable references can be promoted to capsule or immutable, as we prefer.

WTitle(`lent and read')
We have seen immutable, mutable, capsule and class.
The are still two modifiers: Wcode(lent) and Wcode(read).
They are hygienic references: they can be read but can not be stored in mutable/capsule/immutable fields.
Wcode(lent) is an hygienic mutable reference, allowing mutation but not long term storage.
Wcode(read) is an hygienic read-only reference.
WP
A method with a single mut parameter can still be called using a lent reference in place of it.

Wcode(read) is the common supertype of Wcode(capsule),Wcode(imm), Wcode(mut) and Wcode(lent).
In general, we can 
use Wcode(read) when we do not care about the mutability of an object.
For example, we could add to Wcode(Animal)

OBCode
read method
Bool hasArrived() =
  this.path().isEmpty()
CCode
This method can be called on both mutable and immutable animals: 

OBCode
Debug(dog1.hasArrived())
Debug(dog2.hasArrived())
CCode

WTitle(`(5/5) Summary')
  
WTitle(`Kinds of classes, summary')
  
<ul>
<li>
immutable classes:  have only immutable fields.
It is useful to model mathematical concepts.
It is easy to reason about code using immutable classes,
but some properties of real objects can be better modelled with state mutation.
</li><li>
shallow mutable classes:  have only (variable) fields of immutable or capsule type (or class, as we will see later). 
Reasoning with shallow mutable classes is near as easy as reasoning with immutable ones, and often more natural.
</li><li>
deep mutable classes:  have mutable fields.
Reasoning with deep mutable classes can be very hard.
</li></ul>
  
WTitle(`Modifiers: summary')

<ul>
<li>
immutable:  the default. When you omit the modifier,
 you mean immutable. 
An immutable reference points to an object that is never changing. Its whole reachable object graph never changes and is immutable as well.

</li><li>
mutable:  A mutable reference behaves like a normal reference in Java, C#, C++ , Python and many other languages.
Mutable references require mutable objects and allow mutating the referred object.
</li><li>
capsule:  capsule references are used only once and they guarantee that the whole reachable object graph is reachable only through that
capsule reference. 
Capsule references provide a structured way to reason over deep mutable objects.

Fields can be annotated capsule, the meaning is that they need to be initialized/updated with capsule variables.
We will discuss more about capsule fields and how they differ from capsule references later.
</li><li>
read:  A readable reference can not be used to mutate the referred object; but other mutable references pointing to the same object can mutate it.
Read references can point to both mutable and immutable objects.
It is easy to be confused between read and immutable references.
As a rule of thumb, if you are in doubt about whether to use an immutable or a readable reference,
you probably want an immutable reference.

</li><li>
lent:  a hygienic mutable reference allowing mutation but not storage.
Lent and read are useful to handle in controlled way the state of deep mutable classes;
moreover using lent and read on method parameters 
allows to make explicit what are the method intentions and requirements.
</li><li>

class:  class references denote the class object,
  on methods the meaning is the same of static methods in many languages, but it can consistently be used on parameters/local variables/fields
to encode behaviours similar to dependency injection.


</li></ul>



WTitle(`Keep control, summary')

<ul>
<li>
mutable:  mutable objects can be freely aliased and mutated. They allow for a liberal programming style like we can find in Java/C++/C# or Python.
They can be referred to by capsule, mutable, lent and read references.
</li><li>
immutable: immutable objects 
 can be obtained by promoting instances of mutable classes.
 They
can be referred to only by immutable and read references.

</li><li>
class:  class objects can be accessed from anywhere by using the corresponding class name;
It is also possible to 
store them into (class) local binding.
Some programmers found the fact that class objects are instances of themselves deeply concerning
or disturbing, while for others it is just a good story to tell to break the ice at parties.
</li></ul>
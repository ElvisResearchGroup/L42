WBigTitle(Caching)
WTitle((1/5) Normalization)
One of the big advantages of deeply immutable objects is that two structurally identical objects are referentially transparent, that is, you can not distinguish whether they are represented in memory by one object or two.
This means that it is possible to reuse the same objects to save memory. While in other languages the programmer would have to implement some specific support to reuse objects, in L42 this is supported directly in the language, by a process called WEmph(normalization).
An immutable object can be turned into its normalized version using Wcode(.norm()).
OBCode
Person = Data:{S name}
..
var bob1 = Person(S"Bob")//one bob in memory
var bob2 = Person(S"Bob")//two bobs in memory
bob1:=bob1.norm()
bob2:=bob2.norm()//most likely now bob2 refers to
//the same object of bob1.
//The object originally pointed to by bob2 is now inaccessible and
//can be garbage collected.
bob2:=bob2.norm()//repeating normalization has no effect; normalization is idempotent.
//The second calls to norm is as fast as a field access.
CCode
As you can see, objects are born not normalized and can be normalized manually by the programmer.
Normalization is cheap but it is not free, so it is not convenient to apply it on short lived objects.
The programmer can express that all objects of a class are normalized during creation by passing parameters to Wcode(Data):
OBCode
Person = Data('This,autoNorm=\.true()):{S name}
..
var bob1 = Person(S"Bob")//one bob in memory
var bob2 = Person(S"Bob")//still only one bob in memory
bob2:=bob2.norm()//no-op, bob2 was already normalized
CCode
Normalization starts by checking if another structurally equivalent object has ever been normalized.
Normalization normalizes all the sub objects
and also supports circular objects.
WBR
Consider the following richer example:
OBCode
Person = Data:{S name}//no more autoNorm!
Dog = Data:{S name, Person owner}
..
bob1 = Person(S"Bob")//one bob in memory
bob2 = Person(S"Bob")//two bobs in memory
dog1 = Dog(S"Grunthos", bob1)
dog2 = Dog(S"Agrajag", bob2)//two Dogs in memory
//dog1.owner() is a different object from dog2.owner()
//albeit they are structurally identical and we
//can not tell them apart using 42.
dog1.norm()//note: we do not reassign dog1 or dog2
dog2.norm()
//dog1 and dog2 are different objects, but
//dog1.owner() is now the same object as dog2.owner()
CCode
Normalizing an object normalizes the whole ROG.
In the example, normalizing the two dogs also normalizes their owners, to the same normalized object.
All of the dogs' fields are then replaced with the normalized versions, so the two dogs now share an owner object.
Note that bob1 and bob2 are still two different objects.
WP

The logic needed for normalization is the same needed to check if two arbitrary objects are structurally equal, to print an object to a readable string and to clone objects.
Thus data allows for all of those operations indirectly relying on normalization.
Those are all operations requiring to scan the whole ROG of the object, so the cost of normalization is acceptable in context.

WTitle((2/5) Lazy Caching)

Some methods may take a long time to compute, but they are deterministic, and thus we could cache the result and reuse it many times.
A typical example is Fibonacci:
OBCode
class method Num slowFibo(Num n) = {
  if n==0Num || n==1Num (return n)
  return This.slowFibo(n=n-1Num)+This.slowFibo(n=n-2Num)
  }
CCode
This Fibonacci implementation would take a very long time to run, since it would require recomputing the same
results an exponential amount of times.

This tweaked implementation relying on caching is much faster.
OBCode
ComputeFibo = Data:{
  Num that
  @Cache.Lazy method Num () = {
    n = this.that()
    if n==0Num||n==1Num ( return n )
    This fibo1 = This(n-1Num)//a 'computation object'
    return fibo1()+This(n-2Num)()
    }
  }
//...
//usage example
ComputeFibo(42\)() == 267914296Num
CCode
As you can see, instead of a method with parameters we can declare a class with fields and an empty named method doing the actual computation.
Wcode(Cache.Lazy) is an annotation recognized by Wcode(Data) that works only on Wcode(imm) or Wcode(class) methods with no arguments and with an Wcode(imm) result.

That is, 42 does not directly recognize the annotation Wcode(Cache.Lazy).
Decorating the surrounding library with Wcode(Data) translates Wcode(Cache.Lazy) into
an actual implementation.

Wcode(ComputeFibo fibo1) is a WEmph(computation object): an imm object whose only goal is to support one (or more) computationally intense methods.
Thanks to normalization, the cache of computation objects is centrally stored, and thus recursive calls computing Fibonacci will be able to reuse the cache from other objects.
That is, the method result is cached on the normalized version of the receiver. In this way, 
all the redundant Fibonacci calls are avoided.

WP
As you can see, the caching is is completely handled by the language and is not connected with the specific algorithm. This pattern is general enough to support any method from immutable data to an immutable result.

WTitle((3/5) Automatic parallelism)
When decorated by Wcode(Data), Wcode(Cache.Lazy) caches the results of methods after they have been called the first time.
However, why wait for the methods to be called?
Once the receiver object is created, the method could be computed WEmph(eagerly) in a separate worker, so that when we call the method, we may get the result without waiting at all.
That is, if we use Wcode(Cache.Eager) we can get automatic parallelism: the language will handle a set of parallel workers to execute such method bodies.

WP
An important consideration here is that both Wcode(Cache.Lazy) and Wcode(Cache.Eager) are unobservable; that is, the observed result of the code is not impacted by lazy or eager cache.

Consider the following code:

OBCode
Task = Data:{ //Tasks are also computation objects
  S text
  @Cache.Eager method Bool isPolite() = ( .. )
  @Cache.Eager method Bool isGrammatical() = ( .. )
  }
Tasks = Collection.list(Task)

Main=(
  tasks=Tasks[
    \(text=S"..");
    ..
    \(text=S"..");
    ]
  for t in tasks (
    Debug( t.isPolite() )
    )
  )
CCode
Here we declare a Task object with a string field and two eager methods: one will check if the text in the string is polite and another will check if the string is grammatically correct.
This can take quite a while. By using eager methods, it is sufficient to just create the objects to start those computations in parallel.
When we need the results, we can just iterate on those task objects and call the methods.
Objects with eager methods are automatically normed during creation, as if we used 
Wcode(Data('This,autoNorm=\.true())) instead of Wcode(Data).

As you can see, in 42, parallelism and caching are just two sides of the same coin.

WTitle((4/5) Invariants and derived fields)
We have seen that cached behaviour can be computed lazily or eagerly on immutable objects.
But we can bring caching even earlier and compute some behaviour WEmph(at the same time) 
as object instantiation.
This allows us to encode derived fields: 
fields whose values are completely determined
by other values in the same object.
Consider the following example:
OBCode
Point = Data:{//not ok, the three-arg factory still exists
  Double x
  Double y
  Double distanceFromOrigin
  //should always be the square root of x^2+y^2
  class method This (Double x, Double y) = \(
    x=x
    y=y
    distanceFromOrigin=((x*x)+(y*y)).pow(exp=\"0.5")
    )
  }
CCode
where the class Wcode(Point) has 3 fields, but the value of the third one should depend only on the other two.
In 42, the code above would simply define a class with three unrelated fields, and while we are offering a factory that conveniently takes Wcode(x) and Wcode(y) and initialize the third field with 
the computed value, the user could easy create invalid instances by calling the factory method with three arguments.
As we will see later, in 42 we can prevent this from happening by making such a method private.
However, we would still be able to create an invalid Wcode(Point) inside of other Wcode(Point) methods.
Ideally, we would like to truly have only two fields, and have the third one as a precomputed derived value.

In 42, we can encode such concept using Wcode(Cache.Now):
OBCode
Point = Data:{
  Double x
  Double y
  @Cache.Now class method Double distanceFromOrigin(Double x, Double y) = 
    ((x*x)+(y*y)).pow(exp=\"0.5")
  }
CCode
The Wcode(Point) class defined above has a single factory method taking just Wcode(x) and Wcode(y). In this way there is no need to have multiple ways to build the object and then hide the dangerous ones after the fact.

The method Wcode(distanceFromOrigin(x,y)) is computed when a Wcode(Point) object is created.
Moreover, Wcode(Data) adds a method Wcode(read method Double distanceFromOrigin()), allowing us to read the computed/cached value as we would if it were a field.
Note that Wcode(Data) makes a
Wcode(read) method calling the Wcode(class) one.

If the method Wcode(distanceFromOrigin(x,y)) leaks an error, it will be propagated out as if the method were manually called during object construction.

This means that any time you receive a Wcode(Point), it has a valid distance.
WP
We can build on this behaviour to encode class invariants:
Wcode(Cache.Now) methods with Wcode(Void) return type
designed to simply throw error if the state is incorrect.
For example, consider this updated version of Wcode(Point):

OBCode
Point = Data:{
  Double x
  Double y
  @Cache.Now class method Double distanceFromOrigin(Double x, Double y) = 
    ((x*x)+(y*y)).pow(exp=\"0.5")
  @Cache.Now class method Void invariant(Double x, Double y) = 
    if !(x>=0Double && y>=0Double) error X"""%
      | Invalid state:
      | x = %x
      | y = %y
      """
  }
CCode

Now, every time user code receives a Wcode(Point), they can rely on the fact that Wcode(x) and Wcode(y) are
non-negative and not NaN.

WTitle((5/5) Summary)
In 42 immutable objects, can be normalized in order to save memory.
This works also on circular object graphs. In case you are interested in the details, it relies on a variation of DFA normalization.
As a special case, objects without fields (immutable or not) are always represented in memory as a single object.
Results of Wcode(Cache.Lazy) and Wcode(Cache.Eager)
 are attached to the normalized version of an object, thus making it possible to recover them simply by building a structurally identical object.
WP
There are three kinds of caching, depending on the time the caching behaviour activates:

<ul><li>
Wcode(Cache.Lazy) computes the cached value when the annotated method is first called.
It works on Wcode(imm) and Wcode(class) no-arg methods.
An obvious workaround for the no-arg limitation is to define computation objects; this also works well with normalization: computation objects will 
retrieve the cached results of any structurally equivalent object.
</li><li>
Wcode(Cache.Eager) computes the cached value in a separate parallel worker, starting when the object is created. It only works on Wcode(imm) no-arg methods of classes whose objects are all deeply immutable.
Those classes will automatically normalize their instances upon creation.
</li><li>
Wcode(Cache.Now) computes the cached value during object construction.
Since the object does not exist yet, the annotation can only be placed on a Wcode(class) method whose parameters represent the needed object fields.
This annotation does influence the observable behaviour.
If there is no error computing the Wcode(Cache.Now) methods,
then the fully initialized object is returned.
But, if an error is raised computing the cache,
instead of returning the broken object, the error is leaked during object construction.
WBR
This, in turn, allows us to encode class invariants and to provide a static guarantee that users of a class can rely upon.

</li></ul>
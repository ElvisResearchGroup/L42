WBigTitle(Caching on Mutable objects)
WTitle((1/5) Cache invalidation)
The main advantage of caching methods of immutable objects is that the cache stays valid.
L42 can also cache methods of mutable objects, and discovers on its own when the cache needs to be invalidated.
Consider this trivial variation of the Wcode(Point) example above where the fields can be updated:

OBCode
Point = Data:{
  var Double x
  var Double y
  @Cache.Now class method Double distanceFromOrigin(Double x, Double y) = 
    ((x*x)+(y*y)).pow(exp=\"0.5")
  @Cache.Now class method Void invariant(Double x, Double y) = 
    if x<0Double || y<0Double error X".."
  }
CCode
When a setter for Wcode(x) or Wcode(y) is invoked, then the two Wcode(Cache.Now) methods are recomputed.

In other programming languages, this behaviour can be encoded by
making the fields private and customizing the implementations of the setters to recompute the distance when needed. This pattern can grow very complex very fast.
L42 guarantees that a cached value is always structurally equivalent to the value that would be returned by calling the method again.
Moreover, for Wcode(Cache.Now), L42 also guarantees that if the computation was re-run then it would terminate without errors.
Thus, when Wcode(Cache.Now) is used to emulate invariants, those invariants are guaranteed to hold for all observable objects, that is, all objects where the annotated method could possibly be called.

This is possible thanks to the strong L42 type system, and we believe this property can not be broken.
That is, we believe this property to hold even in the presence of exceptions, errors, aliasing, input output and non deterministic behaviour.
It is possible to make L42 work together with Java or even with (possibly broken) native code, and we believe our property will continue to hold.

WTitle((2/5) Deeply mutable objects)
As discussed above, a deeply mutable object is a mutable object with some mutable fields.
Also deeply mutable objects can support Wcode(Cache.Now), 
but such objects must have WEmph(encapsulated) state, as we have seen before for the class
Wcode(Animal).

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

The field Wcode(`capsule Points path') is an encapsulated mutable field.
It can be accessed as Wcode(read) by doing Wcode(`this.path()'), but can not be directly accessed as Wcode(mut).
However, we can write WEmph(capsule mutator) methods by using 
Wcode(Cache.Clear).
Similarly to Wcode(Cache.Now), a class method can be annotated with Wcode(Cache.Clear) and can
take parameters representing the object's fields.
In addition, more parameters can be present encoding extra arguments.
To clarify, consider this richer example, where our Wcode(Animal) has an invariant and another capsule mutator method:

OBCode
Point = Data:{ Double x, Double y
  method Double distance(Point that) = (
    x = this.x()-that.x()
    y = this.y()-that.y()
    (x*x)+(y*y)).pow(exp=\"0.5")
    )
  }

Points = Collection.list(Point)

Animal = Data:{
  var Point location
  capsule Points path

  mut method
  Void move() = (
    this.location(\path.left())
    this.removeLeftPath()
    )

  mut method
  Void trim() = 
    this.removeFarthest(location=\location, distance=3Double)

  @Cache.Clear class method
  Void removeLeftPath(mut Points path) =
    path.removeLeft()
  
  @Cache.Clear class method
  Void removeFarthest(mut Points path, Point location, Double distance) = (
    var maxD=distance
    var maxI=I"-1"
    for p in path, i in Range(path.size()) (
      currentD = location.distance(p)
      if currentD>maxD (
        maxI:=i
        maxD:=currentD
        )
      )
    if maxI!=I"-1" path.remove(maxI)
    )

  @Cache.Now class method
  Void invariant(read Points path, Point location) = 
    if path.contains(location) error X"..."
  }
CCode
We added a method to remove the farthest away point if it is over a certain distance.
As you can see, the parameters Wcode(path) and Wcode(location) corresponds to fields, while
the parameter Wcode(distance) is extra needed information.
When we call Wcode(`this.removeFarthest(distance=3\)') we pass only Wcode(distance); the other parameters are passed automatically.
WBR
As an invariant, we require that the current location is not in the Wcode(path).
This code, in the current form, has a bug; can you spot it?
Look carefully at the method Wcode(move()):

OBCode
  mut method
  Void move() = (
    this.location(\path.left())
    this.removeLeftPath()
    )
CCode
Here we first set up the location, then we remove it from the path.
The method Wcode(move()) recomputes the invariant twice: one after the field setter 
and one after the call to the Wcode(Cache.Clear) method.
This first check is going to fail, since the leftmost element of the path has not been removed yet.
In this case we can solve the problem by swapping the lines:

OBCode
  mut method
  Void move() = (
    left=this.path().left() //store left value
    this.removeLeftPath()   //before removing it
    this.location(left)     //set location to left
    )
CCode

However, this kind of solution does not scale in the general case. 
Next, we will see a 
programming pattern that allows 
 the invariant checks
 (and more generally
    the recomputation of Wcode(Cache.Now) methods)
to be delayed in a controlled way.
WP
WTitle(Cache.Lazy and Cache.LazyRead)
As we have seen before, 
we can annotate Wcode(imm) and Wcode(class) methods 
with Wcode(@Cache.Lazy) so that 
the result will be computed once, the first time that
the method is called.
We can also annotate Wcode(read) methods in the same way.
However, the cache is now stored in the actual objects and not in the normalized versions.
This happens because a Wcode(read) reference can refer to either mutable or immutable objects, and only immutable objects
can have normalized versions.
If anything in the ROG of the Wcode(read) object is mutated, then the cache is invalidated,
and the result will be recomputed the next time the method is called.
Indeed this annotation enables lazy caching on mutable data-structures, where the cache is automatically invalidated and removed when a Wcode(Cache.Clear) method terminates.
Finally, since the type system can not track when the ROG from Wcode(mut) fields is mutated, a Wcode(@Cache.Lazy read method) can only be applied to 
classes whose fields are all
Wcode(imm), Wcode(capsule) or Wcode(class);
that is, their instance all have WTerm(encapsulated state).

WP
If a class has Wcode(mut) fields, but those are not actually used to compute the cached value,
we can apply the Wcode(@Cache.LazyRead) annotation to an opportune Wcode(class) method instead.
Every method annotated as Wcode(Cache.Now) could instead be annotated as 
Wcode(Cache.LazyRead).
This annotation is a point in the middle between Wcode(Cache.Now) and Wcode(Cache.Lazy); it produces the same behaviour as Wcode(Cache.Lazy) but works similarly to Wcode(Cache.Now): it is applied to
Wcode(class) methods whose parameters represent fields, and Wcode(Data) generates a correspondent no-arg Wcode(read) method.
Wcode(Cache.Now), Wcode(Cache.Lazy) and Wcode(Cache.LazyRead) methods all behave as if they where recomputing the result, but with a different performance.
WP
Cache invalidation is considered one of the <a href="https://martinfowler.com/bliki/TwoHardThings.html">great challenges</a> in writing correct programs; L42 can handle it correctly and automatically.
However, there is a cost: you have to encode the algorithm so that the type system accepts your code and so that the caching annotations can be applied.

WTitle((3/5) Box patten)

As we have seen, in order to
write mutable objects with encapsulated state,
we need to design them well, using Wcode(capsule) to initialize the mutable data, using Wcode(Cache.Clear) to mutate such state, and Wcode(Cache.Now) for the invariant.
However, we can also program a naive deeply mutable object and box it up as a second step.
This can require a little more code, but it is more intuitive, and works very well for arbitrarily complex cases.
Consider the following code:

OBCode
Bike = Data:{
  var mut Wheel front
  var mut Wheel back
  var mut Seat seat
  var mut Chain chain
  mut method Void nail() = this.#front().addHole()
  mut method Void rain(Second time) = this.#chain().addRust(time)
  read method Void invariant() = X[
    this.front().size()==this.back().size();
    this.seat().isComfortable();
    ]    
  }
//components can mutate (get damaged)
//and be updated (replaced with new ones)
CCode
As you can see, the Wcode(Bike) is a deeply mutable class, designed with no attention to
correctness: if the programmer is not careful, the same Wcode(Wheel)
may end up being used for multiple bikes at the same time.
Also, the method called Wcode(invariant) only represents a programmer intention, but it is not enforced in any way, so it could be silently broken.

We can easy create a Wcode(BikeBox) class containing and encapsulating, such a Wcode(Bike):
OBCode
BikeBox = Data:{
  capsule Bike box
  
  @Cache.Now class method
  Void invariant(read Bike box) = box.invariant()
  
  @Cache.Clear class method
  Void nail(mut Bike box) = box.nail()
  
  @Cache.Clear class method
  Void rain(mut Bike box, Second time) = box.rain(time=time)

  @Cache.Clear class method
  Void front(mut Bike box, capsule Wheel that) = box.front(that)
  }
..
//user code
b = BikeBox(box=\(front=\(..) back=\(..) seat=\(..) chain=\(..)))
b.nail()
//b.box().nail()/ ill-typed
Debug(b.box().front())//will display the unfortunate wheel
b.front(\(..)) //looks just like a normal setter, but acts on the internal object
CCode

As you can see, no matter how complex some class code is, we can simply wrap it into a box and apply Wcode(Cache.Now)
 and Wcode(Cache.Clear) on top of it.
In this way we end up with two types:
Wcode(Bike), that does not offers any guarantee,
 and Wcode(BikeBox), ensuring the invariant and
encapsulating the state.



The methods Wcode(`BikeBox.nail()'),
Wcode(`BikeBox.rain(time)')
and
Wcode(`BikeBox.front(that)')
 will check for the invariant exactly one time, at the end of their execution.
Following this pattern, we can perform an arbitrarily long computation before the checks are triggered.

When writing other classes, we can choose to use Wcode(Bike)
or Wcode(BikeBox), depending on the specific details of our code.
If we chose to use Wcode(Bike) as a field of another class, we can still check 
the Wcode(Bike) invariant inside the invariant of the composite class:
OBCode
FamilyGarage = Data:{
  mut Bike daddyBike
  mut Bike mummyBike
  mut Trike timmyBike  
  ..
  read method Void invariant() = X[
    this.daddyBike().invariant();
    this.mummyBike().invariant();
    this.timmyBike().invariant();
    ]
  }
FamilyGarageBox = Data:{
  capsule FamiliyGarage box

  @Cache.Now class method
  Void invariant(read FamilyGarage box) = box.invariant()  
  }
CCode
WP
As we have seen, with the box pattern we
can have the flexibility of temporarily open invariants without any of the drawbacks.
Of course, programmers will need to keep in mind which values are protected by invariants
and which values are unsupervised by invariants.
In 42 this is under the control of the type system: a value of type Wcode(Bike) has no special guarantees, while a value of type 
Wcode(BikeBox) will ensure the invariant.

WTitle((4/5) Controlling the ROG shape)

An attentive reader may have notice that we would allow for fields Wcode(front)
 and Wcode(back) to point to the
 same Wcode(Wheel) object.
A Java programmer may be tempted to just add 
Wcode(this.front()!=this.back();) in the invariant,
but this would just use the user defined Wcode(!=) operator, that
on classes created using Wcode(Data) is likely to check for structural equality instead of pointer equality.
AdamsTowel offers Wcode(`System.mutReferenceEquality(a and=b)') to check for reference equality, but 
this method only works for Wcode(mut) objects.
The wheels are indeed Wcode(mut) objects,
but the invariant method takes a Wcode(read) receiver; thus we can only see the wheels as Wcode(read).
In this case, the inability to use pointer equality is actually a good thing, since it does not correspond to what we really wanted to express: what if the two wheels are different objects but they share the same Wcode(mut Tire) object?
What we want is to check that the mutable objects are not aliased in physically unreasonable ways.
Generally, what we often want is to ensure the tree shape of the mutable part of the object graph.

In 42, we can create classes where all of the instances are guaranteed to follow this property, by making all fields either Wcode(capsule) types of classes that recursively respect this property
or Wcode(imm)/Wcode(class).
However, according to what we have seen up to now, Wcode(capsule) fields can only be mutated by defining Wcode(Cache.Clear) methods, and those methods will be unable to mutate any other 
Wcode(capsule) field.
Consider the following code:

OBCode
Tire = Data:{var Num pressure}
Wheel = Data:{var mut Tire tire}
Seat = Data:{var S description}
Chain = Data:{ var Num damage 
  mut method Void onWheel(lent Wheel that) = (
    lent Tire t=that.#tire()
    t.pressure(\pressure-1\)
    this.damage(\damage+1\)
    )
  }
CCode

Here the Wcode(Chain) can rub onto the wheel, damaging it.
The parameter of method Wcode(onWheel) is
Wcode(lent). This guarantees that the object graphs of the chain and the wheel will 
not be mangled together by the method Wcode(onWheel).
Can we assemble a Wcode(Bike) while being able to both use 
Wcode(onWheel) and guaranteeing the tree structure?
The trick is to declare Wcode(lent) exposers manually:
OBCode
Bike = Data:{
  var capsule Wheel front
  var capsule Wheel back
  lent method lent Wheel #front() //lent exposer
  lent method lent Wheel #back() //lent exposer
  var capsule Seat seat
  var capsule Chain chain
  lent method lent Chain #chain()//lent exposer
  mut method Void chainOnWheel() = 
    this.#chain().onWheel(this.#back())
    
  }
BikeBox = Data:{..}//as before
CCode
That is, by declaring lent exposers manually we gain the possibility of writing methods that mutate the capsule fields without using the Wcode(Cache.Clear) pattern.
In exchange for this extra flexibility, 
those fields do not count as Wcode(capsule) fields for the sake of 
Wcode(Cache.Clear), Wcode(Cache.Now) or Wcode(Cache.LazyRead) annotations.
However, we can still use through the box pattern, as show before.



WTitle((5/5) Summary)

Representation invariants and lazy caching
can be applied to mutable objects as well as immutable ones.

Proving properties on mutable
 objects requires us to know and apply various patterns.
Historically, in software verification, representation invariants where small 
units of code, mostly focusing on the direct content of the fields and mostly relying on either pointer equality or the binary value of primitive datatypes, where the invariants could be deliberately  broken while the program was still able to observe the broken object.
None of this is possible in 42; in particular, 42 guarantees that no broken objects can be observed.
The box pattern allows us to divide the value into two types:
the one with an enforced invariant and the raw object state.
This recovers 
the flexibility of temporarily open invariants
without any of the drawbacks.

Note that a sound language with normalization and caching/invariants
can not offer pointer equality tests on immutable objects.

Consider the example of a list whose invariant requires all
 of its elements to have a distinct pointer values.
A list with such an invariant may contain two structurally equal but not pointer equal elements.
Suppose such a list became immutable and was
normalized.
Now those two elements
would be pointer equal, and the invariant would have been broken by normalization.
WP
It can be hard to remember the differences between all of the Wcode(Cache.***) annotations.
Below, we show a table summarizing them.

<table>
<tr>
<th>&nbsp;&nbsp;annotation&nbsp;&nbsp;</th>
<th>&nbsp;&nbsp;recType&nbsp;&nbsp;</th>
<th>&nbsp;&nbsp;parameters&nbsp;&nbsp;</th>
<th>&nbsp;&nbsp;transformedInto&nbsp;&nbsp;</th>
<th>&nbsp;&nbsp;storage&nbsp;&nbsp;</th>
<th>&nbsp;&nbsp;timing&nbsp;&nbsp;</th>
</tr>
<tr>
<td>Cache.Lazy</td>
<td>class</td>
<td>zero</td>
<td></td>
<td>class</td>
<td>first call</td>
</tr>
<tr>
<td>Cache.Lazy</td>
<td>imm</td>
<td>zero</td>
<td></td>
<td>norm</td>
<td>first call</td>
</tr>
<tr>
<td>Cache.Lazy</td>
<td>read*</td>
<td>zero</td>
<td></td>
<td>instance</td>
<td>invalidation</td>
</tr>
<tr>

<td>Cache.Eager</td>
<td>imm*</td>
<td>zero</td>
<td></td>
<td>norm</td>
<td>parallel</td>
</tr>
<tr>

<td>Cache.LazyRead</td>
<td>class</td>
<td>fields</td>
<td>read0</td>
<td>instance</td>
<td>invalidation</td>
</tr>

<tr>
<td>Cache.Now</td>
<td>class</td>
<td>fields</td>
<td>read0</td>
<td>instance</td>
<td>invalidation+</td>
</tr>
<tr>
<td>Cache.Clear</td>
<td>class</td>
<td>fields+</td>
<td>mut+</td>
<td>instance-</td>
<td>when called</td>
</tr>
</table>

WP
Notes:
<ul>
<li>
imm* = applicable only on classes with all fields Wcode(imm)/Wcode(class) and not Wcode(var).
</li>
<li>
read* = applicable only on classes with all fields Wcode(imm)/Wcode(capsule).
</li>
<li>
fields  = method parameters are field names.
Capsule fields are seen as Wcode(read).
</li>
<li>
fields+ = the first parameter is a Wcode(capsule) field, seen as Wcode(mut). Additional parameters can be Wcode(imm),Wcode(capsule) or Wcode(class).
</li>
<li>
invalidation = the method is executed on the first call, or the first call after invalidation.
</li>
<li>
invalidation+ = the method is executed during the factory, and immediately after any invalidation.
</li>
<li>
instance- = caches in the instance are invalidated.
</li>
<li>
read0 = a Wcode(read) method with zero parameters.
</li>
<li>
mut+ = a Wcode(mut) method with the additional parameters as for fields+.
</li>
<li>
parallel = executed in a parallel worker starting after the factory.
</li>
</ul>

WTitle(Digressions / Expansions)
As we have seen, parallel programming can be viewed as a form of caching.
In some cases, we need parallel programming on mutable data.
In our experience, this is not very common; the cost of copying data around is much smaller that most programmers assume.
Let us repeat this very clearly: there are many other ways to optimize software, and they are much easier and much,
much more rewarding than avoiding copying the few mutable parts of your data structure a couple of times.

We think that only highly skilled and motivated programmers 
can discover and hunt down all of those other much more pressing algorithmic issues that
often spoil performance.
Only after approaching the performance limits of Wcode(Cache.Eager) with good algorithms,
it could make sense to adopt parallel programming on mutable data to avoid some
extra clones.

However, if you are in such a situation,
you can use the annotation Wcode(@Cache.ForkJoin) as shown below.
Again, the 42 type system will ensure that parallelism is not observable.
WP
OBCode
Example = Data:{
  @Cache.ForkJoin class method capsule D foo(capsule A a,capsule B b, capsule C c) = (
    mut A a0=a.op()
    mut B b0=b.op()
    mut C c0=c.op()
    a0.and(b0).and(c0)
    )
  }
CCode
Like other Wcode(@Cache.***) annotations, 
Wcode(@Cache.ForkJoin) is translated by Wcode(Data) 
into an actual implementation.

Wcode(@Cache.ForkJoin) works only on methods whose body is exactly a round parenthesis block.
The initialization expressions for Wcode(a0), Wcode(b0), and 
Wcode(c0) are run in parallel, and the final expression is run only when 
all of the initialization expressions are completed.
The method itself can take any kind of parameters, and they can all be used in the final expression, but the initialization expressions need to fit one of the following three safe parallel patterns:

WTitle(Non-Mutable computation)
In this pattern, none of the initialization expressions can use Wcode(mut) or Wcode(lent) parameters.
In this way nothing can be mutated while the forkjoin is open, thus parallelism is safe.
This is more expressive than Wcode(Cache.Eager) since it allows us to run parallel code on Wcode(read) references of mutable objects.

WTitle(Single-Mutable computation)
In this pattern, a single initialization expression can use any kind of parameter, while the other ones can not 
use Wcode(mut), Wcode(lent) or Wcode(read) parameters.
This pattern allows the initialization expression that can use Wcode(mut) to recursively explore a complex mutable data structure and to command updates to immutable elements arbitrarily nested inside of it.
Consider for example this code computing in parallel 
new immutable string values for all of
the entries in a mutable list:

OBCode
UpdateList=Public:Data:{
  @Public class method S map(S that)=that++that//could be any user defined code
  @Public class method Void (mut S.List that) = this(current=0I,data=that)  
  class method Void (I current, mut S.List data) = (
    if current<data.size() 
      this(current=current,elem=data.val(current),data=data)
    )
  @Cache.ForkJoin class method Void (I current, S elem, mut S.List data) =(
    S newElem=this.map(elem)
    this(current=current+1I,data=data)
    data.set(current,val=newElem)
    )
  }
MainUpdate = (
  mut S.List data = S.List[S"a";S"b";S"c";S"d";S"e";]
  Debug(data)
  UpdateList(data)
  Debug(data)//["aa"; "bb"; "cc"; "dd"; "ee"]
  )
CCode
As you can see, we do not need to ever copy the whole list. We can update the elements in place one by one.
If the operation Wcode(`map(that)') is complex enough, running it in parallel could be beneficial.
As you can see, it is trivial to adapt that code to explore other kinds of collections, like for example a binary tree.
Or, in other words, if you are unsure on how to adapt that code to work on a tree, you should stick with Wcode(Cache.Eager) and accept that you are not (yet) one of the few elite programmers with enough skill to take advantage of the 42 fork join.

AdamsTowel could use metaprogramming features to define code that parallelises user defined operations on lists, maps and other common datastructures. However, we think this is beyond the responsibility of AdamsTowel, and should instead be handled by some user defined library.

WTitle(This-Mutable computation)
In this pattern, the 'this' variable is considered specially.
The method must be declared Wcode(mut), and the 
initialization expressions
can not
use Wcode(mut), Wcode(lent) or Wcode(read) parameters.
However, the parameter Wcode(this) can be used to directly call
Wcode(Cache.Clear) methods. 
As we have seen before, Wcode(Cache.Clear) methods can mutate a Wcode(capsule) field; thus different initialization expressions must use Wcode(Cache.Clear) methods updating different Wcode(capsule) fields.

In this way, we can make parallel computation processing arbitrary complex mutable objects inside well encapsulated data structures .
Consider the following example, where Wcode(Foo)s could be arbitrarily complex; containing complex (possibly circular) graphs of mutable objects.
OBCode
Foo=Data:{.. /*mut method Void op(I a, S b)*/ ..}

Tree={interface [HasToS]    mut method Void op(I a, S b) }

Node = Data:{[Tree] 
  capsule Tree left, capsule Tree right
  @Cache.ForkJoin mut method Void op(I a, S b) = (
    this.leftOp(a=a,b=b)
    this.rightOp(a=a,b=b)
    void//this void is needed so that the two lines
    )    // above are both declarations
    @Cache.Clear class method Void leftOp(mut Tree left,I a, S b) = left.op(a=a,b=b)
    @Cache.Clear class method Void rightOp(mut Tree right,I a, S b) = right.op(a=a,b=b)
    }
Leaf = Data:{[Tree]
  capsule Foo label
  @Cache.Clear class method Void op(mut Foo label,I a, S b) = label.op(a=a,b=b)
  }
MainTree = (
  mut Tree top = Node(
    left=Node(
      left=Leaf(label=\(..))
      right=Leaf(label=\(..))
      )
    right=Node(
      left=Leaf(label=\(..))
      right=Leaf(label=\(..))
      )
    )
  Debug(top)
  top.op(a=15I b=S"hello")
  Debug(top)
  )
CCode

This pattern relies on the fact that using Wcode(capsule) fields we can define arbitrary complex data structures composed of disjointed mutable object graphs.
Note that Wcode(read) aliases to parts of the data structure can be visible outside.
This is accepted since we can not access them when the forkjoin is open. The declarations can not use Wcode(read) parameters.
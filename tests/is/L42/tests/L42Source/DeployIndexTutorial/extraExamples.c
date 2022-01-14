Possible to add a FAQ / exercises to all the chapters?

- Can you show a class with a field foo that can be updated but not mutated and a field bar that can be mutated but not updated?
A = Data:{ var S.List foo, mut S.List bar}

- Is it true that:
* All the imm references refer to immutable objects
  Yes

* All the read references refer to read objects
  No, there is no such thing as a read object, objects are only imm, mut and class.

* All the mut references refer to mutable objects
  Yes

* All the mut fields store mutable objects
  No, mut fields of immutable objects store immutable objects.
  This is implied from the concept of deep immutability.

* All the capsule fields store capsule objects
  No, therere are no capsule objects. Capsule fields are just a sugar
  for fields that can only be initialized and updated with capsule references and that can not leak the value as mut.

- Can you explain the difference between: object, reference, local binding and field?
* An object is 
* A reference is a key to access an object, and can have a modifier that works as an access permission. Many different references can access the same object, and they may have different modifiers.
 However, if one of those modifiers is 'imm', then all no other reference will be 'mut', 'lent' or 'capsule'.
* A local binding stores a reference. A variable local binding can
be updated to change the stored reference.
Local bindings are scoped, and they live for the duration of their scope.
* A field stores a reference. A variable field can
be updated to change the stored reference.
Fields are contained in objects, and they live until the object containing them lives.






MultiConcrete5 = Class:Trait({
  $ = Class.Relax:MultiLevel[ 'A->'SuperA; 'C.D.space()->'C.D.superSpace() ]
  A = {
    class method S hi() = S"[%$.SuperA.hi()]"
    B = {class method S world() = S"[%$.SuperA.B.world()]"}
    }
  C={ D={ class method S space()=S"[%$.C.D.superSpace()]" } }
  })['$=>'This]
  
Main8 = MultiConcrete5.C.print() //now prints "[hi][ ][world]"
  
Concrete3 = Class:Code1['C.m()->'C.superM()]
  : {C={method S superM() method S m()=this.superM()++S"!" } }
Main3 = (
  Debug(Concrete3.D().callBoth())
  )


Operator Wcode(:) merges multiple units of code together; but sometime code needs some kind of adaptation before it can be merged

Note that Wcode(traitEnsureTransaction()) is just a normal 
class method that directly returns a library literal.
Traits in 42 are nothing fancier than that.

Now Wcode(MyAction) will execute the operation inside of a transaction.

However, as you can see declaring Wcode(MyAction) using 
Wcode(Refactor.compose) is verbose,
and we need to know the code of Wcode(traitEnsureTransaction())
to use it;
we now show how to improve.
WP

Manually declaring a class just to define a single trait method
returning a library literal is verbose.
In AdamsTowel we can use the class Wcode(Resource)
which automate this process.
WBR
For example: 
OBCode
TraitEnsureTransaction: Resource <>< {
  class method
  Void (mut Db.Connection connection) //method selector here is '(connection)'
  exception Db.Query.Failure 

  class method
  Void (mut Db.Connection that) //method selector here is '(that)'
  exception Db.Query.Failure (/*..as before..*/)
  }

MyAction: Refactor.compose(
  left: TraitEnsureTransaction()
  right: { /*..as before..*/})
CCode

This let us save just a couple of lines. 
We can improve further and make a Wcode(Transaction)
class decorator: 

OBCode
Transaction: {
  InvalidAction: Message.$ <>< {implements MetaGuard}
  //meta guard is the root of all the metaprogramming guards
  class method //using <>< to define the babelfish operator
  Library <>< (Library that) 
  exception InvalidAction {
    i= Introspection(lib: that)
    if !i.hasMethod(\"(connection)") (exception InvalidAction
      "Action method '(connection)' missing")
    composed= Refactor.compose(  left: TraitEnsureTransaction(), right: that  )
    exception on MetaGuard ( InvalidAction
      "Action invalid: type of '(connection)' does not fit or already defined '(that)'")
    return Refactor.HideSelector(\"(connection)") <>< composed
    error on Metaguard
      X"'(connection)' is there, ready to be hidden"
    }  
  }
//So, MyAction becomes shorter and better checked: 
MyAction: Transaction <>< {
  class method
  Void(mut Db.Connection connection)
  exception Db.Query.Failure {
    /*..my operation..*/
    }
  }
CCode

Note how we check some well formedness of the parameter
in an Wcode(if), then we catch and wrap the exceptions of Wcode(compose(left,right)),
and finally we state our assumption that Wcode(HideSelector) can not fail in
that context.

Now we can use Wcode(Transaction) as a decorator.


WTitle((3/5)Extend)

Wcode(Extend)
 is a decorator implemented using 
Wcode(Refactor) and
Wcode(Introspection)
which provides a flexible model of multiple inheritance with super calls in AdamsTowel.
WBR
As an example, in a game we can have a chest which contains objects in certain positions,
a boat which host humanoids, and
a cargo boat, which host humanoids and contains objects like a chest.
We want to reuse the code of chest and boat to obtain the cargo boat.
WBR 
For example: 
OBCode
ChestTrait: Resource <>< {
  mut Objects objects
  /*.. methods to validate access to objects..*/
  read method
  Kg weight() {
    var Kg res= 0Kg
    with o in this.objects().vals() (res+= o.weight() )
    return res
    }
  }

BoatTrait: Resource <>< {
  mut Humanoids crew
  Kg maxCapacity
  /*.. methods to validate access to crew..*/
  read method
  Kg weight() {/*..with-loop on the crew..*/}
  
  read method
  Kg capacityLeft()
    this.maxCapacity()-this.weight()    
  }

Chest: Data <>< ChestTrait()
Boat: Data <>< BoatTrait()
CargoBoat: Data <>< Extend[ChestTrait();BoatTrait()] <>< {
  read method @override //explained below
  Kg weight() this.#1weight()+this.#2weight()
  }
CCode

As you see, we annotate with Wcode(@override) to 
override the Wcode(weight()) method, and we use 
Wcode(`#'1weight()) and 
Wcode(`#'2weight()) to refer to the super implementations.
As an alternative to Wcode(@override),
we could use Wcode(@hide) to just hide the old methods and put our new
version on top. There are two main difference between Wcode(@override) and 
Wcode(@hide).
With override internal references will refer to the new implementation,
while with hide they will refer to the old one.
With override the method type must be identical,
while with hide they can be completely different.


WTitle((4/5)An intolerant type system)

As an exercise, lets try to use what we learned to add a Wcode(sum()) method to
a vector.

OBCode
Nums: Extends[Collections.vector(of: Num)] <>< {
  read method
  Num sum(){
    var Num res= 0Num
    with n in this.vals() (res+= n )
    return res
    }
  }
CCode

Easy.
However, note that we are calling Wcode(this.vals()) to
do the iteration, and we are not declaring a Wcode(vals())
method.
The idea is that while computing Wcode(Nums), the type system is temporary allowing for incomplete/untypable code at the right of the Wcode(:).
The typesystem will check that all is ok when the declaration of Wcode(Nums) is complete.
WP
However, we have done an extension only on our specific Wcode(Nums) vector, we would have to repeat
such code for each vector.
Can we directly produce vectors that will have a Wcode(sum()) method?
Well, this can only work for vectors of elements with a Wcode(+) operator, and a zero concept. Luckily, all 
numeric classes offer a Wcode(zero()) 
and Wcode(one()) method.
WBR
Building on that, we could attempt the following, invalid solution: 
OBCode
MyCollection: {
  class method
  Library traitSum()
    { //my sum feature
    T: {
      class method T zero()
      method T +(T that)
      }
    read method
    Num sum(){
      var T res= T.zero()
      with n in this.vals() (res+= n ) //error here, vals() undefined
      return res
      }
    }
  class method
  Library vector(class Any of) {
    oldPart= Collections.vector(of: of)
    newPart= Refactor.Redirect(Path"T" to: of) <>< this.traitSum()
    return Refactor.compose(left: oldPart, right: newPart)
    }
CCode

Conceptually, we define a new trait for the sum method,
and we make it general introducing Wcode(T) and our
needed requirements.
Sadly, this is not going to compile, since 
in the method Wcode(sum()) we call Wcode(this.vals()),
and there is no definition for such method.
Similar code worked in the former example, but here
the definition of Wcode(MyCollection) gets completed,
and the code in the method Wcode(traitSum()) is still 
incomplete.
We could just repeat there the definition of Wcode(vals()),
but that would be duplicating code; moreover, Wcode(vals()) returns an iterator, which has methods too...
WP

Wcode(Collection) offers a solution: a trait containing
the minimal code skeleton to make Wcode(vals()) over path 
Wcode(T).
WBR
The idea is that
the composition of Wcode(traitSum()) and
Wcode(Collections.traitValsT()) is complete code.
However, even declaring Wcode(traitSum()) as
OBCode
class method
Library traitSum() 
  Extend[Collections.traitValsT()] <>< {/*my sum feature as before*/}
CCode

whould not work: the Wcode(<><) method would
be called when Wcode(traitSum()) runs, leaving incomplete code in the resulting library literal.
We need to force the computation to happen before
Wcode(MyColleciton) is completed.
A solution is to use Wcode(Resource).

OBCode
TraitSum: Resource <>< Extend[Collections.traitValsT()] <>< {/*my sum feature as before*/}
MyCollection: {
  class method
  Library vector(class Any of) (
    oldPart= Collections.vector(of: of) //surely works
    {newPart= Refactor.Redirect(Path"T" to: of) <>< TraitSum()
    return Extend[oldPart] <>< newPart
    catch exception MetaGuard g return oldPart
    })
CCode

By the way, earlier we also forgot to handle exceptions!
If our parameter does not support zero and plus,
we will just return a normal collection. We need to insert additional brackets otherwise the 
binding Wcode(oldPart) would not be visible in the catch body.

As you may notice there is some incoherence in our programming style: 
should traits be methods in a class or Resources?
should we use 
the more primitive
Wcode(Refactor.compose(left,right))
or the more flexible Wcode(Extend[] <><)?
In the current state of the art we do not have an answer for what is the best in 42.
WBR
Indeed, we still do not understand the question.







WTitle((1/5)Refactor and Introspection)

WTitle(Refactor)
Wcode(Refactor) is a class supporting modification of
library literals.
For example, you may want to rename the method Wcode(importStructure(that)) into just Wcode(import(that)).
You can do the following: 
OBCode
{reuse L42.is/AdamsTowel
Db: Refactor.RenameSelector(
  Selector"importStructure(that)" to: Selector"import(that)"
  ) <>< Load <>< {reuse L42.is/Db}
UnivDb: Db.import(Db.ConnectionS"...")
/*..*/
}
CCode
The type Wcode(Selector) represent method selectors;
in the same way the type Wcode(Path) represent 
paths inside library literals, as in Wcode(Path"MyNested.MyNestedNested") or
Wcode(Path"This").

There are a lot of refactoring operations nested under Wcode(Refactor): 
<ul><li>
Wcode(RenameSelector)
and 
Wcode(RenamePath)
rename methods either at top level (as we just did) or
in an arbitrary nested library;
or rename paths into other paths
</li><li>

Wcode(Redirect)
removes a nested library and redirects all its references to
an external one. This emulates generics, as we will see later.
</li><li>
Wcode(UpdateDocumentationSelector)
and Wcode(UpdateDocumentationPath)
add to, alter or delete the documentation of methods/paths.
</li><li>
Wcode(MakeAbstractSelector)
and Wcode(MakeAbstractPath)
remove all the implementation out of a method or path,
leaving only the public skeleton
</li><li>
Wcode(HideSelector)
and Wcode(HidePath)
mark methods or paths as private.
We have not seen details on private members, the main idea is that
they are renamed into invisible names that you can never guess, and automatically renamed to avoid collisions 
by refactoring operations.
</li></ul>
WP
In addition to all those nested classes,
Wcode(Refactor) offers Wcode(`Refactor.compose(left,right)')
allowing a simmetric sum of two library literals.
The main idea is that members with the same name are recursively composed

WTitle(Introspection)
Wcode(Introspection) is
a class for exploring libraries, to discover what methods they have and so on.

The main classes inside of Introspection are
Wcode(Introspection.NestedLibrary),
 Wcode(Introspection.Method) and
Wcode(Introspection.Type).
You can obtain a nested library by calling the factory methods 
Wcode(Introspection(lib)) and Wcode(Introspection(classObj)),
respectively for library literals or class objects.
We will see some example later of use of Wcode(Introspection).



WTitle((5/5)Metaprogramming summary)
<ul><li>
Metaprogramming is hard; 42 tries to make it simpler, but not trivial.
</li><li>
Error handling is important while writing decorators.
More then half of decorators code should be dedicated
to handling errors and lifting them into a more understandable
form, for the sake of the final user.
</li><li>
We are just scratching the surface of what we
can do with metaprogramming.
If you are interested in becoming a Magrathean, then
refer to the painful metaprogramming guide (link);
otherwise just use existing metaprogramming libraries 
and use Wcode(Refactor) only when all the other options feel more painful.
</li></ul>






---------------------
//Example below is nice but it is now redundant
For example, we wish to encode a rich map from Wcode(Point) into Wcode(Item),
where an Wcode(Item) knows about its Wcode(Point) location and where the Wcode(toS()) avoid repeating the Wcode(Point) information from both the Wcode(Point) and the Wcode(Item)
OBcode
Point = Data:{Num x, Num y}
Item = {interface [HasToS],    Point location}
Tree = Data:{[Item]}//inherits the location 'field'
NaiveMap = Collection.map(key=Point, val=Item)
//myNaiveMap.put(key=myItem.location(),val=myItem) is overly verbose
//myNaiveMap.toS() would be repetitive, as in {Point(..)->Tree(location=Point(..))}

//GoodMap addresses those issues:
GoodMap = Organize:{
  $ = Class.Relax:Trait(Collection.map(key=Point, val=Item))
    [clear='toS()]:{
    mut method mut This self() = this
    }
  mut method mut $ self()
  mut method Void add(Item that) =
    this.self().put(key=that.location(), val=that)
  read method S toS() = (
    var res=S""
    for (key,val) in this.self() ( res++=val.toS()++S.nl() )
    res
    )
  }
CCode
As you can see, we can just write the code as if we were aiming to create two classes, 
Wcode(GoodMap) and Wcode(GoodMap.$), and Wcode(Organize) will merge them at the end.
Note the Wcode(self()) method pattern:
To convert from instances of Wcode(GoodMap) to instances of Wcode(GoodMap.$)
we invoke an abstract method Wcode(self()).
When Wcode(GoodMap) is merged with Wcode(GoodMap.$) the method Wcode(self()) will trivially be implemented.
In this example we declared a Wcode(mut method mut $ self()).
In other examples Wcode(method $ self()) may be needed instead.
In all the realistic examples we have ever encountered, it was sufficient to declare (self()) for a single modifier,
but if needed, we could instead declare Wcode(class method fwd mut $ self(fwd mut $ that)) to support Wcode(imm), Wcode(mut) and Wcode(read) at once polymorphically. 
WP

The patterns shown above works for the most common circular type dependencies, but sometime are not sufficient.

One such case happend while designing a little 42 videogame; where we encountered the setting below:
We have Wcode(NPC) following each others in a Wcode(Map).
As for the example above, the map is going to have specialized location aware operations.
The Wcode(NPC) knows about the Wcode(Map), the Wcode(Map) values are Wcode(NPCs).
The map implementation relies on the operations of Wcode(NPC)
and the operations generated by Wcode(Collection.Map).
The Wcode(NPC) operations rely on the operations of the map.

OBCode
Point = Data:{I x, I y}
Game = Organize:{
  NPC$ = { mut method mut This self() = this }
  Map = Collection.map(key=Point, val=NPC$)
  NPC = Data:{
    var Point location
    var I lifePoints
    var mut This target
    mut method mut NPC$ self()  
    mut method Void step(mut Map$ map) =
      /*..*/map.get().lifepoints()/*..*/
    }
  Map$ = {
    mut method mut Map self()
    mut method Void stepAll() = 
      for (val) in this.self() (val.step(this.self()))
    mut method Void add(mut NPC that) = 
      this.self().#put(key=that.location(), val=that.self())
    mut method Void remove(mut NPC that) = 
      this.self().remove(key=that.location())
    mut method Void move(mut NPC that,Position to) = (
      this.remove(that)
      that.location(to)
      this.add(that)
      )
    mut method NPC get(Point that) = (
      this.self().#val(key=that)//Type error! NPC vs NPC$
      )
    }
  }
CCode

As you can see, we get stuck! The Wcode(get(that)) method
should return a Wcode(NPC) but we can only return an Wcode(NPC$). Calling Wcode(.self()) there would not help, such method goes in the other direction.
Even if those types are going to be the same, they are different at this point in the code.
There are many different ways we could solve this, and we think it is instructive to show them in the detail.

We could instead repeat the needed signature in Wcode(Map)
as show below:
OBCode
Game = Organize:{ //NPC$, Map, NPC unchanged
  Map$ = {/*as before*/
    mut method NPC #val(Point key)
    mut method NPC get(Point that) =
      this.#val(key=that)
    }
  }
CCode

We could insert an unreachable runtime error:
OBCode
Game = Organize:{ //NPC$, Map, NPC unchanged
  Map$ = {/*as before*/
    mut method NPC get(Point that) = {
      if NPC res=this.self().#val(key=that) return res
      error X"unreachable"
      }
    }
  }
CCode

We could insert an extra type designed to delay type checking untile the Wcode(Organize) is completed:
OBCode
Game = Organize:{ //NPC$, Map, NPC unchanged
  Map$ = {/*as before*/
    mut method NPC get(Point that) =
      this.#val(key=that)
    @Late
    }
  }
Late = {}
CCode
While this solution avoids the need of many Wcode(self()) calls, it does not allows to remove them completelly.
That is, this next version
would fail in Wcode(stepAll()) with error
Wcode(The type of [mut This.#iterator()] can not be inferred; no informations about it are available).
OBCode
Game = Organize:{
  NPC$ = {}
  Map = Collection.map(key=Point, val=NPC$)
  NPC = Data:{
    var Point location
    var I lifePoints
    var mut This target
    mut method Void step(mut Map$ map) = void
      ///*..*/map.get().lifepoints()/*..*/
    @Late
    }
  Map$ = {
    mut method Void stepAll() = for (val) in this val.step(this)
    mut method Void add(mut NPC that) = this.#put(key=that.location(), val=that)
    mut method Void remove(mut NPC that) = this.remove(key=that.location())
    mut method Void move(mut NPC that,Point to) = (
      this.remove(that)
      that.location(to)
      this.add(that)
      )
    mut method NPC get(Point that) = this.#val(key=that)
    @Late
    }
  }
Late = {}
CCode

We could define a Wcode(_self()) method that goes the other
way:
OBCode
Game = Organize:{
  NPC$ = {
    mut method mut This self() = this
    class method mut This _self(mut This that) = this 
    }
  Map = Collection.map(key=Point, val=NPC$)
  NPC = Data:{/*as in the first attempt, but also with*/
    mut method mut NPC$ self()
    class method mut NPC _self(mut NPC$ that)
    }
  Map$ = {/*as in the first attempt*/
    mut method NPC get(Point that) =
      NPC._self(this.self().#val(key=that))
    }
  }
CCode
We could use two levels of Wcode(Organize).
In the first level we declare all fields, all the Decorator generated methods and all the custom methods as abstract.
In the second level we implement all those custom methods.
OBCode
Game = Organize:{
  $ = Organize:{
    Map = Collection.map(key=Point, val=NPC)
    Map$ = {
      mut method Void add(mut NPC that)
      mut method Void remove(mut NPC that)
      mut method Void move(mut NPC that)
      mut method mut This self() = this
      }
    NPC = Data:{
      var Point location
      var mut Item target
      mut method Void step(mut Map map)
      mut method mut This self() = this      
      }
    }
  NPC = {
    mut method mut $.NPC self()
    mut method Void step(mut $.Map map) = ..
    }
  Map = {
    mut method mut $.Map self()
    mut method Void add(mut $.NPC that) = ..
    mut method Void remove(mut $.NPC that) = ..
    mut method Void move(mut $.NPC that) = ..
    mut method Void stepAll() = 
      for (val) in this.self() (val.step(this.self()))
    }
  }
CCode
This last version is the most verbose, but is also the most relaiable and maintanable.
The extra verbosity comes from having to list the abstract signature of all the methods used in those complex type circular patterns.
This last version is the general case. Any kind of circular code can be obtained with two level of Wcode(Organize) used in this way, and all the code is able to see all methods of all the dependencies, allowing type inference to work well.

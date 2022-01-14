WBigTitle(`Collection: list, map, set, optional and their operations')

WTitle((1/5)lists manipulation)

As we have seen before, lists can be defined using Wcode(Collection.list(that)), as in the example below.

OBCode
Nums: Collection.list(Num) //declaration for a list of nums
/*..*/
xs0 = Nums[10\;20\;30\] //xs0 <: mut Nums
Nums xs1 = Nums[10\;20\;30\] //xs1 <: imm Nums == //xs1 <: Nums
imm xs2 = Nums[10\;20\;30\] //xs2 <: imm Nums == //xs2 <: Nums
xs3 = Num.List[10\;20\;30\] //xs3 is another kind of list of nums
CCode
As you can see above, many of the most common Wcode(AdamsTowel) classes have a nested class
called Wcode(.List) as a convenience feature, to avoid having to define your own in most programs.

WBR

Lists can be created with square brackets; they are born mutable but can become immutable 
if they are directly assigned to an immutable local binding or parameter, or by other forms of promotion; for example,
a method without Wcode(mut) parameters returning a 
Wcode(mut) reference
can be used to initialize an immutable binding.
You need to specify the type of the local binding to force the promotion.
WBR
For example: 

OBCode
imm myNums = DoIt.getMutableNums() //ok promotions happens, myNums is immutable

myNums = DoIt.getMutableNums() //myNums type is inferred to be mut Nums
CCode

Immutable lists can be combined with operators.
The general idea is that operators 
Wcode(`+,-') work 
on one sequences and one element,
while the corresponding doubled-up operators
Wcode(`++,--')
work on two sequences.
You can see the details of this below.
OBCode
X[
  //element addition
  Nums[a;b;c]+d == Nums[a;b;c;d];
  //element addition works both ways
  k+Nums[a;b;c]+d == Nums[k;a;b;c;d];
  //sequence concatenation
  Nums[a;b]++Nums[c;d] == Nums[a;b;c;d];
  //element removal
  Nums[a;b;b;c]-b == Nums[a;c];
  //sequence subtraction
  Nums[a;b;b;c]--Nums[b;c] == Nums[a];
  ]
CCode
In addition of operators, immutable lists also support a plethora of methods:
OBCode
X[
  //replacement
  Nums[a;b;c;d].with(2I,val=e) == Nums[a;b;e;d];
  Nums[a;b;c;d].with(left=e) == Nums[e;b;c;d];//equivalent to Nums[..].with(0I val=e)
  Nums[a;b;c;d].with(right=e) == Nums[a;b;c;e];//equivalent to Nums[..].with(\size-1I val=e)
  //insertion
  Nums[a;b;c;d].withAlso(2I,val=e) == Nums[a;b;e;c;d];
  Nums[a;b;c;d].withAlso(left=e) == Nums[e;a;b;c;d];
  Nums[a;b;c;d].withAlso(right=e) == Nums[a;b;c;d;e];
  //skipping/filtering
  Nums[a;b;c;d].without(2I) == Nums[a;b;d];
  Nums[a;b;c;d].withoutLeft() == Nums[b;c;d];
  Nums[a;b;c;d].withoutRight() == Nums[a;b;c];
  Nums[a;b;c;b;d].withoutAll(val=b) == Nums[a;c;d]; // like '--'
  Nums[a;b;c;b;d].without(left=b) == Nums[a;c;b;d]; //filters out the leftmost b
  Nums[a;b;c;b;d].without(right=b) == Nums[a;b;c;d]; //filter out the rightmost b
  ]
CCode
As you notice, there are different kind of actions: 
replace an element (Wcode(with)),
insert an element (Wcode(withAlso))
and skipping/filtering elements out (Wcode(without)).
Then, elements can be specified by index, by being
the leftmost or the rightmost.
To filter elements out,
you can also just provide the element.

WP


Immutable collections (and also mutable ones, as we will see later)
can be accessed with the following methods: 

OBCode
X[
  //access
  Nums[a;b;c;d].left() == a;
  Nums[a;b;c;d].right() == d;
  Nums[a;b;c;d].val(2I) == c;
  Nums[a;b;c;d].size() == 4I;
  !Nums[a;b;c;d].isEmpty();
  ]
CCode

WTitle(`Mutate sequences')

Mutable sequences can be more efficient that 
immutable ones, and are more general, since they 
can store mutable objects.
WBR


Now we show some methods over a mutable list Wcode(`foo = Nums[a;b;c;d]'); consider each following line independently: 
OBCode
//setting a value in a position
foo.set(2I val=e) //foo == Nums[a;b;e;d]
//setting at left or right
foo.left(e) //foo == Nums[e;b;c;d]
foo.right(e) //foo == Nums[a;b;c;e]

//add a value in a position
foo.add(2I val=e) //foo == Nums[a;b;e;c;d]

//add at left or right
foo.add(left=e) //foo == Nums[e;a;b;c;d]
foo.add(right=e) //foo == Nums[a;b;c;d;e]

//removal
foo.remove(2I) //foo == Nums[a;b;d]
foo.removeLeft() //foo == Nums[b;c;d]
foo.removeRight() //foo == Nums[a;b;c]

//removal
foo.removeAll(val=b) //foo == Nums[a;c;d]
foo.remove(left=b) //remove the leftmost b
foo.remove(right=b) //remove the rightmost b
CCode


WTitle((2/5) `Iterations on lists and views')

We now show various pattens to iterate on lists.
First some usual foreach:
OBCode
vec = S.List[S"foo"; S"bar"; S"beer"]
var result = S""
var max = 0I
for myElem in vec (
  result++=myElem 
  max:=max.max(myElem.size())
  )
X[result==S"foobarbeer";max==4I]
CCode

In 42 foreach allows to iterate on multiple collections at once, and also to update the collections:
OBCode
rs = Nums[  1\;  2\;  3\]
as = Nums[ 10\; 20\; 30\]
bs = Nums[100\;200\;300\]
for a in as, b in bs, var r in rs ( r:= r+a+b )
X[ rs==Nums[111\;222\;333\] ]
CCode

In the example above, a dynamic error would be raised if 
Wcode(rs),
 Wcode(as) and
 Wcode(bs) have different length.
We believe this is the right default behaviour.
To allow, for example, Wcode(bs) to be longer then Wcode(as) and Wcode(rs),the programmer can 
use some variants of Wcode(vals(that,to)); a method producing an iterator on a subsequence of the original sequence.
The following variants are available: Wcode(vals(that,to)), Wcode(vals(that)), Wcode(vals(to)), Wcode(#vals(that,to)), Wcode(#vals(that)) and Wcode(#vals(to)).
In the example below we use them to control iteration:
OBCode
for a in as, b in bs.vals(to=as.size()), var r in rs ( r:= r+a+b )
//will give error if bs.size()<as.size() or as.size()!=rs.size()

for a in as.vals(1I), b in bs.vals(1I to=as.size()), var r in rs ( r:= r+a+b )
//will skip the first element of as and bs. Will skip any extra element of bs.
//will give error if as.size()!=rs.size()+1I
CCode

The class Wcode(Collection.View) provides flexible iterators and sub-sequences.
Consider the following code examples:
OBCode
NView = Collection.View(Num.List).cut()
MainCut = {
  xs= Num.List[..]
  ys= Num.List[..]
  for x in xs, y in NView(ys) ( .. )
    //will iterate as long as xs, even if ys is longer.
    //will stop after xs.size() cycles, and fail if xs.size()>ys.size()
  for x in NView(xs), y in NView(ys) ( .. )
    //will iterate for as long as both xs and ys have elements.
    //similar to a functional zip
  }
NViewM = Collection.View(Num.List).more()
MainMore = {
  xs= Num.List[..]
  ys= Num.List[..]
  for x in xs, y in NViewM(ys, more=30Num) ( .. )
    //will iterate as long as xs, even if ys is shorter.
    //y = 30Num when the iteration get over ys.size()
  for x in NViewM(xs, more=10Num), y in NViewM(ys, more=30Num) ( .. )
    //will iterate for as long as either of xs and ys have elements, and values
    //x = 10Num, y = 30Num are used when the collections exhausted their elements.
  for x in NView(xs), y in NViewM(ys, more=30Num) ( .. )
    //behaves as in the "x in xs" case: a 'cut' view will consume 'more' elements
    //if they are available
  }
CCode

WTitle(`The power of the \')

There are various methods taking advantage of the Wcode(\) syntactic sugar.
They provide an expressive power similar to what list-comprehensions provide in python and streams in Java, but by just using simple control flow like for/if:

OBCode
as =  Num.List[1\;2\;3\;4\;]

//mapping
bs0 = Num.List()( for a in as \add(a*10Num) )
X[ bs0==Num.List[10\;20\;30\;40\] ]

//filtering
bs1 = Num.List()( for a in as if a>2Num \add(a) )
X[ bs1==Num.List[3\;4\] ]

//flatmapping
bs2 = Num.List()( for a in as for b in bs0 \add(a+b) )
X[ bs0==Num.List[11\;21\;31\;41\;12\;22\;32\;42\;13\;23\;33\;43\;14\;24\;34\;44\;] ]

//reduce to string
str0 = S"the content is: ".builder()( for a in as \add(a) )
X[ str0 == S"the content is: 1234" ]
str1 = S"[%as.left()".builder()( for n in as.vals(1I) \add(S", %n") )++S"]"
X[ str1 == S"[1, 2, 3, 4]" ]

//reduce/fold
acc  = ( var x = 0Num, for a in as ( x+=a ), x )
acc1  = 0Num.acc()( for a in as \add(a) ) //also \addIf, \times, \val ..
X[ acc==acc1; acc1 == 10Num ]

//checks a property; great in an if or an X[]
ok0 = Match.Some()( for a in as \add(a>3Num) )
X[ ok0 ]

X[ !Match.All()( for a in as \add(a>3Num) ) ]

X[ !Match.None()( for a in as \add(a>3Num) ) ]

X[ 0I.acc()( for a in as \addIf(a>3Num) )==2I ]

asContainsAllBs = Match.All()( for b in bs \add(b in as) )

asIntersectBs = Match.Some()( for b in bs \add(b in as) )

asDisjointBs = Match.None()( for b in bs \add(b in as) )
//Note: b in as == as.contains(b)
CCode
The language 42 is expression based. Expressions that look like statements are just expressions with the
Wcode(Void) return type.
Those methods that take advantage of the Wcode(\) are simply methods with a single parameter 
Wcode(Void that).
The Wcode(\add(that)) method in the 
Wcode(Match.**) examples short circuit when appropriate, so that the for can terminate as soon as the result is known.


WTitle((3/5) `Lists with mutable and immutable elements')

Mutable sequences can contain mutable objects.
While this can be useful in special circumstances, it can create aliasing issues similar to the
ones of the animals example of before.
To warn against such issues, methods Wcode(left()), Wcode(right()) and Wcode(val(that)) return 
Wcode(read) references to mutable objects. In order to obtain 
a Wcode(mut) reference, the user needs to use the methods
Wcode(`#left()'),
 Wcode(`#right()')
 and Wcode(`#val(that)').

WP

Up to now we focused on lists of Wcode(Num), but 
all instances of Wcode(Num) are immutable; we now discuss what happens where
mutable lists contains a mixture of mutable and immutable elements.
Consider the following code:
OBCode
Point  = Data:{var Num x, var Num y}
Points = Collection.list(Point)
..
imm p0 = Point(x=0\,y=0\) //an imm point
p1 = Point(x=1\,y=1\) //a mut point
ps = Points[p0;mutVal=p1] //a mut list with both points
X[
  p0==ps.val(0I);
  p0==ps.left();
  p1.readEquality(ps.#val(1I)); // '==' only works on imms, and check on norms
  p1.readEquality(ps.#right()); // readEquality checks for structural equality
  p0.readEquality(ps.readVal(0I)); // .readVal generalizes over imm/mut values
  p1.readEquality(ps.readVal(1I));
  ]
for read p in ps ( Debug(p) )
CCode

As you can see, to insert a mutable point we need to use Wcode(mutVal) and to
take the point out we have to add the Wcode(`#') to the method.
When iterating on a list, if we expect a mixture of Wcode(mut) and Wcode(imm) values we must add Wcode(read)
to avoid a runtime error.
If we expect all values to be Wcode(mut), we can write Wcode(mut) instead.
When a Wcode(mut) collection is promoted to Wcode(imm), it still remembers what values were originally inserted as Wcode(mut).
To make it so that all values can be read as Wcode(imm), we can use the method
Wcode(.immNorm()). In addition of normalizing the collection, it also marks all values
accessible as Wcode(imm), as shown in the code below:

OBCode
Points immPs=(
  imm p0=Point(x=0\,y=0\) //an imm point
  p1=Point(x=1\,y=1\) //a mut point
  Points[p0;mutVal=p1] //a mut list with both points
  ).immNorm()
for p in ps ( Debug(p) ) //works, it would fail without 'immNorm()'
CCode

WTitle(`(4/5) Map, set, opt..')

Wcode(Collection) also support maps, sets, optional and enumerations.
We will add more kinds of collections in the future.

WTitle(`Optional')
In 42 there is no concept of null, and all the values are always intentionally initialized before
they can be read.
There are two main reasons programmers rely on nulls: optional values and circular/delayed initialization.
Circular initialization can be solved with a Wcode(fwd) types, an advanced typing feature that we do not discuss here.
Optional values are a staple of functional programming and are logically equivalent to a collection of zero or one element, or, if you prefer, a box that may or may not contain an element of a certain type.
Optionals values can be obtained with Wcode(Collection.optional(that)) as shown below.
Optionals are also optimized so that they do not require the creation of any new objects at run time.

OBCode
Point = Data:{ var Num x, var Num y }
OPoint = Collection.optional(Point)
Main = (
  imm p00 = Point(x=0\ y=0\)//an imm Point in 00
  mut p01 = Point(x=0\ y=1\)//a mut Point originally in 01
  var imm p00Box = OPoint(p00) //immutable Optional Point
  var mut p01Box = OPoint(p01) //mutable Optional Point
  X[
    p00 in p00Box; //the in syntax checks if an object is in the box
    p00Box.val()==p00; //Data defines == only for imm references
    p01Box.val().readEquality(p01);//here .val() gives us a read reference
    ]
  if p00Box ( Debug(S"printing %p00Box") )//printing <Point(x=0, y=0)>
  //we can just check if a box is not empty as if it was a boolean
  p01Box.#val().x(50\)//updates the x value of the point
  Debug(S"printing %p01")//printing Point(x=50, y=1)
  p00Box:= OPoint()//updates the local variables with empty boxes
  p01Box:= OPoint()
  if !p00Box ( Debug(S"printing %p00Box") )//printing <>
  X[
    !(p00 in p00Box);
    !p00Box;//using isPresent() or not is just a matter of style
    !p00Box.isPresent();
    ]  
  )
CCode
At this point in the tutorial, some readers will be confused that we can update the local variable binding 
Wcode(p00Box:= OPoint()) even if it is immutable.
Other readers instead will remember that immutability is a property of the reference and not of the binding/field: a local binding and fields declared Wcode(var) can be updated.
The updated value needs to respect the modifier of the field/binding type: if it is Wcode(mut/imm) it needs to be updated with another Wcode(mut/imm); if it is Wcode(read) then it can be updated with either
Wcode(mut), Wcode(imm) or Wcode(read).
Oh, yes, another reader will realize ... and a Wcode(capsule) reference can be assigned to any of Wcode(mut), Wcode(imm), Wcode(lent) or Wcode(read).

Note how both local bindings are updated using the same exact expression:
Wcode(p00Box:= OPoint()    p01Box:= OPoint())
In 42 Wcode(OPoint()) can be either Wcode(mut) or Wcode(imm) (or Wcode(capsule) indeed)
On the other side, consider 
Wcode(OPoint(p00)) and Wcode(OPoint(p01)): the first one is immutable since Wcode(p00) is Wcode(imm),
while the second one is mutable since Wcode(p01) is Wcode(mut).

WP
Optionals can be combined with the short circuting operators Wcode(`&&') and Wcode(`||').
Many interesting patterns emerge from this:
OBCode
S.Opt o1 = ..
S.Opt o2 = ..

if o1 && o2 (/*executed only if both are present*/)

o3 = o1 || o2 //o3==o1 if o1 is present, otherwise o3==o2; thus
//o3 is empty only if they are both empty. Very useful pattern

o4 = o1 && o2 //o4==o2 if also o1 is present, otherwise o4 is empty

Bool b = !!o1 //hacky but effective conversion to bool :-P

s1 = o1.val() //dynamic error if o1 is empty
s2 = o1.val(orElse=S"default") //with a default value if o1 is empty.

//what if computing the default value is a slow operation?
(val) = o1 || S.Opt(MakeDefault.lotsOfTime())
//This pattern computes the default value only if o1 is empty
CCode

WTitle(`Map')
Thanks to normalization 42 can have very fast and most reliable hash sets and hash maps.
The values of sets and the keys of maps must be immutable, and are normalized just before being inserted in the collection.
Then, the value of the normalized pointer is used to check for equality and hashcode.
This has various positive effects:

<ul><li>
The user does not need to write equality and hashing behaviour
</li><li>
There is no risk of mistake in the equality and hashing behaviour
</li><li>
The intrinsic invariants of the hashmap/hashset are never violated/corrupted.
</li><li>
The equality is a perfect structural equality, but is as fast as pointer equality; for maps with large keys this can make a massive performance difference.
</li></ul>

Maps and sets have less methods than lists, but they can still be iterated upon, as shown in the following code:

OBCode
Point = Data:{var Num x, var Num y}
Points = Collection.list(Point)
PointToString = Collection.map(key=Point, val=S)
Roads = Collection.map(key=S, val=Point)
Main = (
  map = PointToString[
    key=\(x=3\ y=4\), val=S"MyBase";
    key=\(x=0\ y=0\), val=S"Source";
    key=\(x=5\ y=8\), val=S"EnemyBase";
    ]
  for (key,val) in map ( Debug(S"%key->%val") )
  //we can use (..) to extract the key/val fields from PointToString.Entry
  //this iteration is straightforward since all values are imm
  roads = Roads[
    key=S"Kelburn Parade", val=\(x=0\ y=0\); //immutable to immutable
    key=S"The Terrace", mutVal=\(x=0\ y=0\);//immutable to mutable
    key=S"Cuba Street", mutVal=\(x=0\ y=0\);//immutable to mutable
    ]
  for read (key, val) in roads ( Debug(S"%key->%val") )
  //we add 'read' in front to be able to read mixed imm/mut values
  //if all the values were mutable, we could just add 'mut' in front
  )
  mut Roads.OVal optPoint = roads.#val(key=S"Cuba Street"))
  optPoint.#val().x(50\)//update the field of the object inside the map
  CCode

As you can see, when objects are retried from the map, we obtain an optional value; this is because statically we can not know if a key is mapped to a value or not.
We can use Wcode(val(orElse)) or the Wcode(||) pattern to provide a default value if the key is not contained in the map.
WBR
In addition to conventional Wcode(size()) and Wcode(isEmpty()),
maps offers the following methods:
<ul><li>
To extract a value using the key:
Wcode(val(key)), Wcode(#val(key)) and Wcode(readVal(key)); to extract an optional
Wcode(imm), Wcode(mut) or a Wcode(read) reference, respectively.
As for lists, it is always safe to extract a Wcode(read) reference. An empty optional will be produced when attempting to extract as Wcode(imm/mut) a value that was inserted as Wcode(mut/imm) instead, so to reliably ask if a key is contained in the map we should write Wcode(map.readVal(key=myKey).isPresent()).

</li><li>
Mutable maps can be modified by
inserting immutable values with Wcode(put(key,val)) and mutable values with Wcode(#put(key,val)).
Finally, an association can be removed using Wcode(remove(key)).
</li><li>
Wcode(Collection.map(that)) creates a class remembering the insertion order.
This is needed to make the iteration deterministic.
The keys can be retrieved with their order using
Wcode(key(that)) passing the desired Wcode(I index), from zero to Wcode(\size-1I)
The corresponding value can be retrieved by methods Wcode(val(that)), Wcode(#val(that)) and Wcode(readVal(that)) 
to extract a
Wcode(imm), Wcode(mut) or a Wcode(read) (not optional) reference to the value, respectively.
</li></ul>
     

WTitle(`Set')
Sets behave a lot like maps where the values are irrelevant, and have differently named methods.
In particular, in addition to conventional Wcode(size()) and Wcode(isEmpty()),
sets offer methods Wcode(add(that)) and Wcode(remove(that)) to add and remove an element,
and elements can be extracted in the insertion order by using method Wcode(val(that))

We are considering adding operators Wcode(`+,-,++,--') to sets, as supported by lists.
On the other side, boolean methods like Wcode(intersect(that)) Wcode(disjoint(that)) and Wcode(containsAll(that)) can already be easily emulated with Wcode(Match) as we shown for lists.

WTitle(`(5/5) Collection summary')

<ul><li>
There are tons of methods and operators to know, but since most code works 
around collections, it is worth the effort to memorize them.
</li><li>
Immutable collections are easy to play with, using operators and Wcode(with**) methods.
</li><li>
Mutable collections can be more efficient and flexible, but they come with additional difficulties.
</li><li>
Most methods have a general version that works with an index, and specialized Wcode(left) and
Wcode(right) variants.
</li><li>
Wcode(\) can help remove a lot of boilerplate, but is a concept unique to 42, and require some effort to get used to.
</li><li>
Wcode(for) is very useful and flexible. It is common to find methods composed from just a large
Wcode(for) statement plus a little pre and post processing around it.
</li></ul>

WTitle(Digressions / Expansions)
Collections support iteration with the Wcode(for) syntax.
Iteration in 42 is way more flexible than in most other languages, and it is delegated on method calls.
Iteration in 42 is designed to support two main iteration strategy:
explicit indexes and iterator objects.
For example, the code

OBCode
for read a in as, var b in bs ( b:=b.foo(a) )
CCode
would expand to
OBCode
aIt = as.#iterator()
bIt = bs.##iterator()
var ai = as.#startIndex()
var bi = bs.#startIndex()
while aIt.#hasElem(ai).#itAnd(bIt.#hasElem(bi)) (
  var a=aIt.#elem#read(ai)
  var b = bIt.#elem#default(bi)
  b := bIt.#update#default(bi,val=b.foo(a))
  ai := ai.#succ()
  bi := bi.#succ()
  ) 
CCode
Since Wcode(a) is declared Wcode(read), Wcode(#elem#read) is used instead of Wcode(#elem#default).
Since Wcode(b) is declared Wcode(var) Wcode(.##iterator) is used instead of Wcode(.#iterator).


WTitle(`Iteration methods in detail')

<ul>
<li>
Wcode(#iterator) and Wcode(##iterator)
WBR
They return an object able to provide the elements of the list. The second variant
returns a Wcode(mut) object, this is needed to provide the mutable version of those elements, and to update those elements in the list.
The second variant is used if the local binding is explicitly declared either Wcode(var), Wcode(mut),Wcode(lent) or Wcode(capsule).
For complex bindings, like Wcode(`(key, mut val)=e'), the second variant is used if any binding component would require it.
</li><li>
Wcode(#startIndex) and Wcode(#succ)
WBR
The initial iteration hint is produced by Wcode(#startIndex) and moved forward by Wcode(#succ).
</li><li>
Wcode(#hasElem), Wcode(#itAnd) and Wcode(#more)
WBR
The iterator checks if it has more elements to provide by calling Wcode(#hasElem).
Since iterations in 42 can work on multiple collections at the same time,
Wcode(#hasElem) results can be combined with Wcode(#itAnd).
This design offers a lot of flexibility;
special kinds of collections may return special data types to coordinate termination in some way.
The AdamsTowel collections opt for an efficient solution where 
there are four logical results for Wcode(#hasElem):
Wcode(mustContinue), Wcode(mustStop), Wcode(canContinue) and Wcode(canStop).
The iteration will stop if all the Wcode(#hasElem) return Wcode(mustStop) or Wcode(canStop),
and an error is raised if some Wcode(#hasElem) returns Wcode(mustContinue) and some other returns Wcode(mustStop).

With this design, the result of the single Wcode(#hasElem) method coordinates the various iterators to check if more elements are possibly available, and to decide how strongly to insist for those elements to be visited.
</li></ul>

WTitle(Possible implementations of Iteration methods)

The iterator methods allows for a range of possible options.
The simplest one, and possibly the most efficient, is to delegate everything to the collection object:
in this case, there is no need to create a new object to serve as an iterator, since
the collection itself is able to simply access/update its elements by index, thus 
Wcode(#iterator) and Wcode(#varIterator) may simply return Wcode(this).
Wcode(#startIndex) returns the Wcode(0I)
and Wcode(#hasElem) returns Wcode(0I) if Wcode(that<this.size()) and Wcode(I"-1") otherwise.
Finally, Wcode(#close) will throw error if Wcode(#hasElem) would return Wcode(0I).
WP
Another option would be the one of a linked list, where it would be inefficient to
rely on the index to access the elements.
In this case,
the Wcode(#iterator) and Wcode(#varIterator) may simply return a singleton object
delegating the behaviour to the index object.
Wcode(#startIndex) can simply expose the first internal
node, and Wcode(#succ) can produce the next node.
The singleton object may be able to see private methods of those
internal nodes, thus even if we expose the internal nodes, they will be 
just unusable black boxes to the library user, and all the interactions can be mediated, and checked by the singleton iterator object.
WP
Iterators in Java and other languages will throw an error if the collection is somehow modified during the iteration. This could be supported by providing a specialized sublist object that can remember its version; but it is unclear if this is a good idea in 42, where most collections would be iterated while they are immutable.
The few cases of iteration on mutable collections
may be the ones where there are good reasons to perform mutation during iteration.
While adding elements at the start of a collection under iteration is most likely a bug, other operations have very reasonable use cases.
For example, 
appending elements at the end of a list while computing a fixpoint, removing tasks from the end of a list if they are now unneeded,
or replacing already visited elements with new ones.
WBigTitle(Basic classes)

WTitle((1/5) Num, I, Double and Math.Long)
Wcode(Num) is a general number type,
implemented as an arbitrary precision rational.
When in doubt of what numeric type to use, Wcode(Num)
is a good first guess.
Some examples of usage: 
OBCode
little      = 123Num
stillLittle = 4567890Num
big         = 100000000000000000Num
bigger      = 100000000000000000.0001Num
fraction1   = Num"1234567/890"
Debug(fraction1) //will print '1234567/890'
Debug(Num"12/4") //will print '3'
CCode

Another useful numeric type is Wcode(I), for index and index offsets.
It corresponds to sizes and indexes in sequences.
Wcode(I)s are returned by Wcode(size()) methods
and are expected as parameter by indexing methods.
Wcode(I) represent 32 bit integers with the usual 
but tricky modulo arithmetic.

WTitle(Other numeric types)

AdamsTowel offers two other numeric types:
Wcode(Double) (64 bits floating point) and Wcode(Math.Long) (64 bits integers, rarely used).


WTitle(Conversions)
Conversions between various numeric classes must be performed explicitly.

AdamsTowel offers a simple way to convert between numeric classes; all numeric  classes implements Wcode(Math.Numeric)
so that they can be converted in to each other using the empty named method. For example we can convert indexes into doubles by writing Wcode(Double(12I)).
This will avoid precision loss as much as possible.

WTitle((2/5) Units: An example library)

We will now see how to load and use an interesting 42 Library:Wcode(Unit).

Consider the following code, where the class decorator Wcode(Load) allows us to load libraries and embed them in the 
current context, while the
Wcode(reuse) keyword imports the code from the web. 
OBCode
reuse [L42.is/AdamsTowel]
Unit = Load:{reuse [L42.is/Unit]}
Year = Unit(I)
Person = Data:{S name, Year age}
CCode

The library
Wcode(Unit)
offers methods to create units out of numeric supports, like Wcode(Num) and Wcode(I).
The code above shows how to create a Wcode(Year) unit and use it to represent a person age.

Units can be added to themselves and multiplied by constants; for example
Wcode(3Year+2Year == 5Year) and Wcode(3Year *2I == 6Year) would hold, but Wcode(3Year * 2Year) would not compile.

Units could be used to manually define 
all of the units of the SI system.
Prebuilt reusable code for the SI system is already provided in the library; we simply need to specify the desired support, as shown in the code below:

OBCode
SI = Class:Unit.TraitSI['Support=>Num]
..
res = (6SI.Meter + 4SI.Meter) * 2Num //20Meter
//wrong = 6SI.Meter + 2SI.Second
CCode

Wcode(Unit.TraitSI) is a WTerm(trait); traits contains reusable code and operations to adapt it to the current needs.
We will see more on traits (much) later in this guide.
WBR
In the case of Wcode(Unit.TraitSI), we can adapt it to many kinds of numeric support and extract the code using 
Wcode(Class:Unit.TraitSI['Support=>Num]).
The syntax Wcode(['Support=>Num]) maps the class called Wcode(Support) inside of the library onto the class Wcode(Num) defined outside of the library. We will explain the precise use of such mappings later.

WP
As you can see, we can sum meters together, and we can use the support for multiplication, but we can not mix different units of measure.

Mathematically you can obtain the support out of the unit by
division; that is, 42 meters divided by 2 meters is  21.
Units also provide method  Wcode(`#'inner()),
which is just extracting the value of the support from the unit.
This can be convenient during programming but 
does not make a lot of sense mathematically and thus 
it should be used with care, similarly to other methods starting with Wcode(`#').
OBCode
Num n1 = 42SI.Meter / 2SI.Meter //= 21Num
Num n2 = 42SI.Meter.#inner() //= 42Num
CCode
WP


Some code which uses units in interesting ways:

OBCode
SI.Meter res1 = (6SI.Meter+4SI.Meter)*2Num //20M

Num res2 = 42SI.Meter/2SI.Meter

Num res3 = (42SI.Meter).#inner()

SI.Velocity fast1 =  42SI.Meter/0.1SI.Second

fast2 = SI.Velocity"420" //equivalent ways to initialize it
fast3 = SI.Velocity"840/2"

distance1 = 60SI.Second * fast1

g = 9.8SI.Acceleration

speedAfter = 10SI.Second * g //98 m/s

t = 10SI.Second

//free fall distance d=(gt^2)/2
distance2 = (g*t*t)/2Num //490 m after 10s free fall

//Newton=Kg*m/s2 = Kg*Acceleration
rocketForce = 900SI.Newton
stackWeight = 60SI.Kg+20SI.Kg //my weight+rocket weight
gForceOnMe = stackWeight*g //little less than 800
myLift = rocketForce-gForceOnMe
if myLift>0SI.Newton (Debug(S"I can fly"))
myAcc = myLift/stackWeight
reachedHeight = (myAcc*t*t)/2Num //after t (10 sec)
//works assuming the rocket fuel burnt in 10 sec is negligible
CCode


WTitle((3/5) Alphanumeric)
In the same way Wcode(Units) allows easy creation of
arithmetic classes,
Wcode(Alphanumeric) allows easy creation of alphanumeric classes: 
classes that can be instantiated from a string literal that follow certain 
properties.


OBCode
Email = S.Alphanumeric:{
  S local //fields
  S domain
  
  class method
  This (S string)={
    index= string.indexOf(S"@") //works only for simple emails
    if index==I"-1" (error S.ParseError"@ not found")
    local= string.subString(0I to=index) //string slicing
    domain= string.subString(index+1I to=\size) //string slicing
    if domain.contains(S"@") (error S.ParseError"multiple @ found")
    return This(string,local=local,domain=domain)
    } //call the factory with fields plus the original string
  }
...
myEmail = Email"arthur.dent@gmail.com"
myEmail.local()==S"arthur.dent" //holds
myEmail.domain()==S"gmail.com" //holds
myEmail.toS()==S"arthur.dent@gmail.com" //holds
CCode

Note: we raise an error if Wcode(string) does not have the shape we expected.
We will see errors/exception in more detail soon.
We can define fields, and compute their values by parsing the string.
It is common to propagate the original string from the factory into the object; but 
it is not mandatory. For example you could apply some form of normalization, as shown below: 

OBCode
Email = S.Alphanumeric:{
  S local //fields
  S domain
  
  class method
  This (S string)={
    /*..*/
    local = string.subString(0\ to=index).replace(S"." with=S"")
    domain = /*..*/
    normedEmail = S"%local@%domain"
    /*..*/
    return This(normedEmail, local=local, domain=domain)
    } 
  }
/*..*/
myEmail = Email"arthur.dent@gmail.com"
myEmail.local()==S"arthurdent" //holds
myEmail.toS()==S"arthurdent@gmail.com" //holds
CCode

WTitle((4/5) Enumerations)

Enumerations can be obtained with Wcode(Enum), as in the following code:
OBCode
Direction = Collection.Enum:{
  North={} East={} South={} West={}
  }
/*..*/
Debug(Direction.Vals()) // [North; East; South; West]
n = Direction.North()
s = Direction.South()
if n==s (/* .. dead code .. */)
Debug(n) //North
for d in Direction.Vals() (
  Debug(d) //prints all the directions in order.
  )
n==Direction.Vals(S"North") //holds
CCode

Enumerations allows us to add operations as methods on the cases, as shown below:
OBCode
Direction = Collection.Enum:{
  method This opposite()
  North = {method This1 opposite() = South() }
  East = {method This1 opposite() = West() }
  South = {method This1 opposite() = North() }
  West = {method This1 opposite() = East() }
  }
CCode

WTitle((5/5) Summary)

<ul><li>
We had a look at
the most basic features of AdamsTowel.
There is rich support for defining your own specialized data structures instead of having to rely on the ones provided by default.
</li><li>
Use Wcode(Unit), Wcode(S.Alphanumeric) and Wcode(Enum) to give meaning to your constants.
In this way, the type system will help you to use values with the semantics that you decided.
Be sure to define all of the right base classes to establish a convenient vocabulary
to talk about your problem domain.
</li></ul>
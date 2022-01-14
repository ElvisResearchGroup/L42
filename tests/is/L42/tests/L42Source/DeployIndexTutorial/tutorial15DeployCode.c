WBigTitle(Deploy 42)
In the context of 42 and AdamsTowel, there are three things that can be deployed:
 Executable programs,  Towels and Modules.

WTitle((1/5)Deploy Towels)

A towel is about the most massively useful thing a programmer can have.
A towel has immense psychological value, and you should always know where your towel is.
All the classes that we have used up to now without defining them, are defined in AdamsTowel.
They are all normal classes/libraries.
WBR
You can code without a towel, but this means starting from first principles,
which could be quite unpleasant; especially since the only
primitive things that 42 offers are Library literals
(code as first class entities), the constant Wcode(void),
and the types Wcode(Library), Wcode(Void) and Wcode(Any).
WP

Towels are libraries providing standard
functionalities and types, such as number, boolean,
string and various kinds of decorators and system errors.

WP
However, we do not expect all 42 programs to reuse the same exact towel.
For hygienic reasons, in real life everyone tends to use their own towel.
For similar reasons, any sizeable 42 program will use its own towel.
WP

We expect different programs to use massively different libraries for
what in other languages is the standard library.
That is, there is no such thing as 'the 42 standard library'.

WTitle(Using multiple Towels)

Towels shines when multiple towels are used at the same time.

OBCode
reuse [L42.is/AdamsTowel]
//here you can access to lots of utility classes defined inside the towel
//including numbers, strings and so on.
C = {reuse [L42.is/FordTowel]
  //here you can access a different set of classes.
  //For example, Num would refer to the number in FordTowel
  //and to see the number defined in AdamsTowel you have to write This1.N
  }
CCode

Different code parts build upon different set of classes.
That is, by introducing multiple towels in nested scopes,
the names of the other scopes are WTerm(masked).
This is very useful for code that reasons on code; such task is pervasive in 42. 

WTitle(Staining Towels)
If you are writing a sizeable program, 
or many similar programs, it make sense to
enrich a towel with some pre loaded libraries
and basic classes.

OBCode
reuse [L42.is/AdamsTowel]
RichTowel = Trait:{
  reuse [L42.is/AdamsTowel]
  Unit = Load:{reuse [L42.is/Unit]}
  Kg = Units.of(Num)
  Meter = Units.of(Num)
  }
Secret = {...}//Be careful to not commit the file 'Secret'
  //It should contain your passwords/tokens, as in 
  //class method S #$of()=S"ghp_..."
GW = Load:{reuse [L42.is/GitWriter]}
LoadDeploy = Load:{reuse [L42.is/Deploy]}
DeployGit = LoadDeploy.with(writer=GW)
DeployRicherTowel = DeployGit.towel(RichTowel()
  on=Url"github.com/Bob/Modules42/RichTowel.L42"
  writer=GW.#$of(token=Secret.#$of(),message=S".."))
CCode
If you have write access to a github project under Wcode(Bob/Modules42), the former code will create your towel and update it
on your github repository every time you run it.
If you want to just write on your hard drive, you could just do

OBCode
FS = Load:{reuse [L42.is/FileSystem]}
LoadDeploy = Load:{reuse [L42.is/Deploy]}
DeployFS = LoadDeploy.with(writer=FS)
DeployRicherTowel = DeployFS.towel(RichTowel()
  on=Url"myLocalPath/RichTowel.L42"
  writer=FS.#$of())
CCode

We are considering adding more variants, for example to allow writing on your FTP servers, google drives, dropbox and other similar services.
WP
A WEmph(Stained Towel) is a towel that looks like another but it is enriched by adding more things, either at the bottom.
In our example, Wcode(RichTowel) is just a stained variation of Wcode(AdamsTowel).


WTitle((2/5)Module deployment)

If you start writing in 42, you will soon feel the need
to factorize your project into libraries that can
be independently tested, deployed and loaded.
We call those library WTerm(Modules).
Very successful modules are used by multiple 
independent projects and developers; they are what is often called a third party library.
However, most modules exists just as development tools in 
order to keep the complexity of big projects under control.
WP
In 42 it is easy to code with multiple modules, and modules can be much smaller than usual third party libraries and frameworks in other languages.
WP
In 42 it is possible to employ a programming model where every developer (or every pair of developers in a pair programming style) is the
only one responsible of one (or more) modules and their maintenance process, while the group leader gives specifications and tests to be met by the various module developers and will glue all the code together.
WP

Modules can be deployed in a way similar to towel deployment;
Wcode(Load) is used to load libraries,
but it also contains all the knowledge to deploy
them.
WBR
The following example code deploys a Module
using Wcode(AdamsTowel):
OBCode
reuse [L42.is/AdamsTowel]
//could be L42.is/RichTowel and nothing would change
//...
Module = Trait:{
  reuse [L42.is/RichTowel]
  //need to be RichTowel in this example,
  //where AirplaneUnits is using Unit
  AirplaneUnitsUtilities = {...}
  AirplaneUnits = {...}
  }
DeployAirplaneModule = DeployGit.module(Module()
  name='AirplaneUnits
  on=Url"github.com/Bob/Modules42/AirplaneUnits.L42"
  writer=GW.#$of(token=Secret.#$of(),message=S".."))
CCode

This code deploys Wcode(Module.AirplaneUnits) to an URL as a module,
and turns Wcode(AirplaneUnitsUtilities) and any other 
nested classes stained on top of Wcode(AdamsTowel) private.
This includes 
Wcode(Unit), Wcode(Kg) and Wcode(Meter) from Wcode(RichTowel).
WBR
If there were any nested classes unreachable from public classes inside
Wcode(AirplaneUnitsUtilities) it will be pruned away.
Same for any nested class stained on top of Wcode(AdamsTowel) and for 
any private unreachable one in Wcode(AirplaneUnits).
WP
The deployed library can be imported as usual.
For example the main
Wcode(AirplaneUnits = Load:{reuse [github.com/Bob/Modules42/AirplaneUnits]})
allows us to see the content of Wcode(AirplaneUnits).
WP

All the deployed code is closed code.
Towels are closed because they contain all the code to implement strings, numbers and so on.
Modules are also closed. They have abstract classes/methods
for each of the original towel concepts (before staining), and they can be rebound
to a multitude of towels.
In particular all stained versions of the same towel are compatible.
Every needed nested library
that was not present in the original towel, will be made private.
On the other side, all the classes in the original towel will 
be made abstract by Wcode(DeployGit.module(..))
and will be rebound to the current towel by Wcode(Load).
WP

Thus, in our example, 
Wcode(Unit),
Wcode(Kg),
Wcode(Meter) and Wcode(AirplaneUnitsUtilities)
would become a private implementation detail of the exposed library.


WTitle((3/5)Deploy programs)

We can run our  applications inside 42, but we can also deploy them as  Jars, so that they can be run as a Java application.
WBR
In 42 libraries can be directly manipulated, and
one possible manipulation is to convert them in 
another format, like an executable jar or a native program
and then save the result somewhere, such as on the website where the users can download it.
WBR
For example, we could rework the code of the former chapter as follows:

OBCode
reuse [L42.is/AdamsTowel]
ToJar = Trait:{reuse [L42.is/AdamsTowel]
  Unit = Load:{reuse [L42.is/Unit]}
  LoadJ = Load:{reuse [L42.is/JavaServer]}
  LoadGui = Load:{reuse [L42.is/GuiBuilder]}
  Query = Load:{reuse [L42.is/Query]}
  Year = Unit(I)
  Meter = Unit(Num)
  Kg = Unit(Num)
  DBJ = LoadJ(slaveName=S"dbServer{}")
  DB = Query.sql(connectionString=S"jdbc:derby:PersonsGui;create=true", javaServer=DBJ)
  Table = DB.#$of().tables()
  Queries = DB.QueryBox:{...}
  GuiJ = LoadJ(slaveName=S"miniGuiSlave{}")
  IQL = Query.iql(javaServer=GuiJ)
  Dialogs = IQL.QueryBox:{...}
  Gui = LoadGui(javaServer=GuiJ)  
  Model = Data:GuiJ.Handler:{...}
  OpenGui = {...}
  class method Void #$main() = ( //method #$main instead of 'Main'
    j=GuiJ.#$of()
    sql=Queries(DB.#$of())
    iql=Dialogs(IQL(j))
    model=Model(j=j,sql=sql,iql=iql)
    OpenGui(j=j)
    for e in j(\['Example]) ( e>>model )
    )
  }
//..
Tast = DeployGit.jar(ToJar()
  on=Url"github.com/Bob/Modules42/MyApplication.jar"
  writer=GW.#$of(token=Secret.#$of(),message=S".."))
CCode
As you can see,
we are wrapping the application code into a trait, including
a second reuse of Wcode(AdamsTowel).
In this way the code of Wcode(ToJar) is fully self contained, and 
Wcode(Main) in the outer scope can still use all of the towel features by taking them from the outer Wcode(reuse [AdamsTowel]).
We then use Wcode(`DeployGit.jar(..)') to deploy our application onto a jar in a specific location.
Again, we could just deploy it on our file system or on another kind of service by using another kind of Wcode(writer).
WP
When 42 is used to deploy an application as a Jar, you can see the whole 42 execution as a comprehensive compilation framework,
encompassing all the tools and phases that could possibly be needed into a single cohesive abstraction.

Such jar can be run with the following command
Wcode(java -cp "L42.jar;MyApplication.jar" is.L42.metaGenerated.ExportedMain)
WP
In this example we reuse AdamsTowel both outside Wcode(ToJar)
and inside of it.
The two towels do not need to be the same.
The outermost just has to support the deployment process
Wcode(DeployGit), while the inner one is needed to make
Wcode(ToJar) a closed library: only libraries that do not refer to external classes can be deployed.

WTitle(A 42 project testing and deploying)

A common way to use 42 is to have a folder with the name of your project, containing
a folder Wcode(Main) with all the actual code,
and then various files providing testing and deploying functionalities, as in the following example:

OBCode
reuse [L42.is/AdamsTowel]
Main = Trait:{
  ...
  Tests = {
    TestSuite_1 = {...}
    /*..*/
    TestSuite_n = {...}
    }
  }
Secret = {...}
GW = Load:{reuse [L42.is/GitWriter]}
LoadDeploy = Load:{reuse [L42.is/Deploy]}
DeployGit = LoadDeploy.with(writer=GW)
Tast = DeployGit.jar(Main()
  on=Url"github.com/Bob/Modules42/MyApplication.jar"
  writer=GW.#$of(token=Secret.#$of(),message=S".."))
CCode

In general, for medium size projects is a good idea to keep executing the tests before the deployment; for example
we can have a test suite after the Wcode(...).
Do not panic, If the test are not reachable from Wcode(Main.#$main()), they are not going to be included in the executable jar.
WP


WTitle((4/5)Towel embroidery: Define and deploy our own towel)


Towel embroidery it is like adding your initials to your towel.
WP
While we can simply add to the end by staining, embroidery is much more powerful.
WP
The most common embroidery tool 
is Wcode(Organize).
Together with late casts, we can add methods to any existing class as shown below:
OBCode
Code = Trait:Organize:{reuse [L42.is/AdamsTowel]
  S$ = {/*..more methods for string here..*/
    method S reverse() = (/*..*/ (this<:@This1 S).size() /*..*/)
    }
  Num$ = {/*..more methods for numbers here..*/}
  }
CCode



The advantage with respect to composing two separated
libraries is that the scope is the same:
the implementation of Wcode(reverse()) will be able to use Wcode(Bool), Wcode(Num) and so on.


Towel staining is a very minimal personalization, and stained towels are fully compatible with the original one.

With embroidery you can personalize the content of your towel a lot more,
but when module deployment 
relies on an embroidered towel, compatibility with the original towel is lost.
For example, an embroidered version of 
Wcode(AdamsTowel) 
can Wcode(Load) a library developed on the original 
Wcode(AdamsTowel), but a library developed on the embroidered version 
needs to be loaded into a similarly embroidered towel.

One typical reason to embroider a towel is to
extend the set of classes that are shared between libraries.
For example, one may want to develop a Towel for scientific use
where the existence of some units of measure can be shared between all the libraries.
We could do the following:
OBCode
RawCode = Trait:{reuse [L42.is/AdamsTowel]
  Unit = Class:Trait({@AbstractTowel{
    "en.wikipedia.org/wiki/Quantity"}}):
    Load:{reuse [L42.is/Unit]}
  SI = Class:Unit.TraitSI['Support=>Num]:{@AbstractTowel{
    "en.wikipedia.org/wiki/International_System_of_Units"}}
  Load$={
    class method Introspection.Nested.List _baseDeps()
    class method Introspection.Nested.List baseDeps() = this._baseDeps().withAlso(\[
      Info(Unit);
      Info(SI);
      ])      
    }
  }
Secret = {...}
GW = Load:{reuse [L42.is/GitWriter]}
LoadDeploy = Load:{reuse [L42.is/Deploy]}
DeployGit = LoadDeploy.with(writer=GW)
DeployRicherTowel = DeployGit.towel(
  Organize:RawCode
    ['Load.baseDeps()->'Load._baseDeps()]
    [hide='Load._baseDeps()]
  on=Url"github.com/Bob/Modules42/SITowel.L42"
  writer=GW.#$of(token=Secret.#$of(),message=S".."))
CCode
Now Wcode(github.com/Bob/Modules42/SITowel) can be used as a towel,
and can be used to deploy modules that can be loaded by 
Wcode(github.com/Bob/Modules42/SITowel).
WP
By using semantic URIs as
ontological nodes, we
can create a basis for other libraries when trying to infer the meaning of our added types.
In the example above we used wikipedia links to relevant concepts.
This may not be the best solution, devising a good solution for this problem would require very intense research in the area of Ontological mapping.
WP
Wcode(SITowel) can be used now to deploy and load libraries wrote in
Wcode(SITowel), and libraries deployed and loaded in this way will 
share a unique definition for certain units of measure.
Note that libraries originally developed for
Wcode(AdamsTowel) can still be loaded normally since Wcode(SITowel) is structurally a superset of Wcode(AdamsTowel).

WTitle(`(5/5)Deployment: programs, libraries and towels; summary')
<ul><li>
42 is a metaprogramming tool.
It is natural to use 42 either as a language (to run a program)
or as a compiler (to deploy programs, libraries and towels).
</li><li>
Indeed we expect all sizeable 42 projects to use 42 as a compiler,
to produce some reusable artefacts.
</li><li>
The distinction between towels (that do not need Wcode(Load))
and modules is 
introduced not by 42, but by Wcode(AdamsTowel); radically different towels may provide different meaning for the concepts of deploying and
loading libraries/towels.
</li><li>
Application developers can freely stain and embroider towels;
in this way they can adapt Wcode(AdamsTowel) to serve them better.
However, library developers need to carefully consider the effect of embroidery.
</li></ul>
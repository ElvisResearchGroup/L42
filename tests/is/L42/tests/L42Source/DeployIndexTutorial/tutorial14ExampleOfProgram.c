WBigTitle(Example of a 42 program)

In this chapter, we will see how to develop a GUI connected with a DataBase.
For simplicity, consider that we have a database with a single table Wcode(Person) that contains a few fields.
We want to make a GUI that displays the data and allows us to edit it.

WTitle((1/5) Query boxes)
First we load the libraries we need:
Unit, JavaServer, GuiBuilder and Query.

OBCode
reuse [L42.is/AdamsTowel]
Unit = Load:{reuse [L42.is/Unit]}
LoadJ = Load:{reuse [L42.is/JavaServer]}
LoadGui = Load:{reuse [L42.is/GuiBuilder]}
Query = Load:{reuse [L42.is/Query]}
CCode
We then declare some useful units. Wcode(Person) objects have ages expressed in years,
heights expressed in meters and weights expressed in Kgs.
OBCode
Year = Unit(I)
Meter = Unit(Num)
Kg = Unit(Num)
CCode
WP
We then ask Wcode(Query.sql) to generate a class to support SQL queries using a Java slave and a connection string.
As an example, we are using a derby DB.
For now, we consider the DB to be already populated. In the end we discuss how to initialize the DB itself.
OBCode
DBJ = LoadJ(slaveName=S"dbServer{}") //slave for the DB connection
DB = Query.sql(//instantiate Query for SQL queries
  connectionString=S"jdbc:derby:PersonsGui;create=true",
  javaServer=DBJ)
CCode


The class Wcode(DB) can now reify the DB tables; here it is just Wcode(Table.Person).
OBCode
Table = DB.#$of().tables()
CCode


Finally, we can make a query box with all the queries that are relevant for this application.
A query box is a capability class, whose methods are able to do queries on a given database.
It is obtained with the decorartor Wcode(DB.QueryBox),
that can recognize nested classes created with the
Wcode(DB.query) method.
Since Wcode(DB) was created by providing the connection string, queries are aware of their database.

OBCode
Queries = DB.QueryBox:{...}
CCode
OBCode
//file Queries.L42
All = DB.query[Table.Person.List]"SELECT * FROM Person"
  
Insert = DB.query[Void;S;Year;Meter;Kg]"""
  |INSERT INTO Person (name,age,height,weight)
  |Values (@name,@age,@height,@weight)
  """
DeleteId = DB.query[Void;I]"DELETE FROM Person WHERE id=@id"
DeleteName = DB.query[Void;S]"DELETE FROM Person WHERE name=@name"
CCode
The symbol Wcode(@) identifies parameters in the queries, while the types
in Wcode([..]) are the query result type followed by any parameters.
Queries return lists of objects. Those objects are constructed by calling a (factory) method whose arguments have the same name as the query fields.
WP

Right now the class Wcode(Query) supports both Wcode(SQL) and Wcode(IQL).
We expect to add more query languages in the future.
Wcode(IQL) is a query language to query the user by asking them to complete a form.
Using Wcode(IQL) in 42 is very similar to using Wcode(SQL).
In particular, the result of both SQL and IQL queries is a lists of objects instantiated using a unique #immK(..) method.
While this is a consistent and flexible way to process tabular data,
it means  that for queries returning a single column we must have a 
type with such a single field.
In 42, declaring those types and their corresponding list type takes just a single line.
Note how for Person we can use our specialized units Wcode(Year), Wcode(Meter) and Wcode(Kg).
OBCode
Key = Data:Data.AddList:{I id}
PName = Data:Data.AddList:{S name}
Person = Data:Data.AddList:{S name, Year age, Meter height, Kg weight}
CCode
In the same way, if a query returns a single row, we will have it served as the only element of a length 1 list.
WP
We can now make the set of all user queries with another Wcode(QueryBox):

OBCode
GuiJ = LoadJ(slaveName=S"miniGuiSlave{}") //slave for IQL and Gui
IQL = Query.iql(javaServer=GuiJ)//instantiate Query for IQL queries
Dialogs = IQL.QueryBox:{...}//A box with all the queries we want to support
CCode
OBCode
//file Dialogs.L42
AddPersons = IQL.query[Person.List]"""
  | 'Add persons' Pages('Add data for more persons')
  | name 'person name:' String
  | age  'person age:'  Integer
  | height 'person height:' Decimal{regex='[0-9]*\\.[0-9][0-9]'}
  | weight 'person weight:' Decimal{regex='[0-9]*\\.[0-9]'}
  """
RemoveById=IQL.query[Key.List]"""
  | 'Deleting an entry' Single('Entry to delete?')
  | id 'index' String
  """
RemoveByName = IQL.query[PName.List]"""
  | 'Deleting entries' Tabular('Entries to delete?')
  | name 'person name:' String
  """
CCode
For a complete guide to IQL, you can refer to the well designed
IQL guide, located in the readme of the
<a href='https://github.com/RegevBenita/IQL'>IQL repository</a>.

WTitle((2/5) Model)
To write a GUI we need a WTerm(Model) and a WTerm(View).
The model is an object whose methods are triggered by events of the view.
Comparing this with the conventional MVC, here the model serves both the roles of the model and the controller.
In this example, the model will have the two boxes and the java slave to control the Gui.
OBCode
Gui = LoadGui(javaServer=GuiJ)  
Model = Data:GuiJ.Handler:{...} //the model answering to Java events
CCode
OBCode
//file Model.L42
mut GuiJ j
mut Queries sql
mut Dialogs iql
@GuiJ.Handler mut method Void printAll(S msg)=(/*..*/)
@GuiJ.Handler mut method Void addPerson(S msg)=(/*..*/)
@GuiJ.Handler mut method Void removeById(S msg)=(/*..*/)
@GuiJ.Handler mut method Void removeByName(S msg)=(/*..*/)
CCode
Methods annotated with Wcode(GuiJ.Handler) will respond to the corresponding event from the view.
Those methods must all take a single Wcode(S) parameter, used by the view to communicate extra information. This parameter is often unused.
Those methods are defined as follows:
OBCode
@GuiJ.Handler mut method Void printAll(S msg)=(
  this.#j().submitEvent(key='Example.Display, id=S"tableClear", msg=S"")
  for (id,name,age,height,weight) in this.#sql().all()() (
    this.#j().submitEvent(key='Example.Display, id=S"tableAdd",
      msg=S"%id,%name,%age,%Double(height),%Double(weight),")
    )
  whoops DB.Fail, GuiJ.Fail
  )
CCode
Wcode(printAll(msg)) first asks the view to clear the table; then
it executes the query Wcode(all) by doing Wcode(this.#sql().all()()).
That is:
Wcode(this.#sql()) is the Wcode(QueryBox) offering access to all the individual query objects.
Wcode(this.#sql().all()) is the field access for the query object doing the Wcode(DB.query[Table.Person.List]"SELECT * FROM Person") query.
Finally, since this query takes no parameters, we just use Wcode(`()') to call it. Calling the query returns a Wcode(Table.Person.List) object,
that is iterated with the Wcode(for). We map the fields of Wcode(Person) onto local variables Wcode((id,name,age,height,weight)) for easy access in the Wcode(for) body.
For each Wcode(Person), the body asks the view to add a line into the table. Information has to be encoded as a string to be passed to the view. String interpolation Wcode(%) make this easy, and Wcode(Num) values are converted as Wcode(Double) to print them with decimal points instead of printing them as a fraction.
Finally, we Wcode(whoops) exceptions to assert that we do not expect them to be leaked.
WBR
This is quite a mouthful.
42 code tends to be quite compact, but for the sake of clearity and to support learning, we will now encode it again in a more verbose style:
OBCode
@GuiJ.Handler mut method Void printAll(S msg)=(
  eventChannel = S"Example.Display" //the view lissens on this channel
  eventId = S"tableClear" //the view reacts to this id
  this.#j().submitEvent(key=eventChannel, id=eventId, msg=S"")
  selectAll = this.#sql().all()
  Table.Person.List allPersons = selectAll()
  for person in allPersons (
    id = person.id()
    name = person.name()
    age = person.age()
    height = Double(person.height())//creates a Double from a Num
    weight = Double(person.weight())
    msg=S"%id,%name,%age,%height,%weight,"//a string format for the view
    this.#j().submitEvent(key=eventChannel, id=S"tableAdd", msg=msg)
    )
  catch exception DB.Fail x error x
  catch exception GuiJ.Fail x error x  
  )
CCode
As you can see, you are free to make the code more readable by declaring a lot of local variables, but you can also keep a more compact style.
In the end, more verbose code may end up less readable simply because there is much more of it.

WP
The other methods have a similar structure.
OBCode
@GuiJ.Handler mut method Void addPerson(S msg)=(
  for (name,age,height,weight) in this.#iql().addPersons()() (
    this.#sql().insert()(name=name, age=age, height=height, weight=weight)
    )
  this.printAll(msg=msg)
  whoops DB.Fail, IQL.Fail
  )
CCode
The method Wcode(addPerson(msg)) ask the user to provide data for a list of persons by running the Wcode(.addPersons()) IQL query,
returning a Wcode(Person.List).
Then, the data of each Wcode(Person) is inserted in the database.
Note how the parameters of the query Wcode(.insert()) are provided using the names of the query declaration
Wcode(".. Values (@name,@age,@height,@weight)"):
Wcode(@name) in the query string was used to create the Wcode(name) parameter; and so on for the others. Thanks to metaprogramming
the query method is synthesized out of the query string.
WBR
After inserting all the new data in the database, we refresh the displayed table by manually calling Wcode(.printAll(msg)).

WP
OBCode
@GuiJ.Handler mut method Void removeById(S msg)=(
  for (id) in this.#iql().removeById()() (//zero or one time
    this.#sql().deleteId()(id=id)
    )
  this.printAll(msg=msg)
  whoops DB.Fail, IQL.Fail
  )
@GuiJ.Handler mut method Void removeByName(S msg)=(
  for (name) in this.#iql().removeByName()() (//zero or many times
    this.#sql().deleteName()(name=name)
    )
  this.printAll(msg=msg)
  whoops DB.Fail, IQL.Fail
  )
CCode
Methods Wcode(removeById(msg)) and Wcode(removeByName(msg))
are very similar; they call the corresponding Wcode(IQL) query, 
extract the single field (using the notations Wcode((id)) and
Wcode((name))) and update the database using the corresponding Wcode(SQL) query.

WP
In this setting we represent persons in two different classes:
Wcode(Person) and Wcode(Table.Person).
This allows for those two different classes to actually be quite different:
Wcode(Table.Person) have an Wcode(id) field and fields Wcode(weight) and Wcode(height) are of type Wcode(Num).
On the other side Wcode(Person) have no Wcode(id) field and 
fields Wcode(weight) and Wcode(height) are of type Wcode(Kg) and Wcode(Meter).
Those two classes serve different roles, and if we wish to change the
kind of data the user must provid we can change Wcode(Person) and make it even more distant with respect to the information stored in the database.
On the other side, if we wanted to apply a transformation on the data readed from the DB, we could use another custom person class, instead of Wcode(Table.Person), and define and appropriate Wcode(#immK) method to adapt the data from the database into any shape we need.
WP
It is also interesting to consider what happens if the database schema changes.
If the person table is removed, or the person fields are renamed,
then we will get an error while typing the model.
WBR
In some sense we are turning events that would have caused a runtime exception into
understandable compile time errors.


WTitle((3/5) View)
The library Wcode(GuiBuilder) allows to write a GUI
using Java Swing.
For safety reasons, Java code is compiled and executed on a separated JVM.
We can easily generate a GUI for our example in the following way:
OBCode
OpenGui = {...}
CCode
OBCode
//file OpenGui.L42
class method Void (mut GuiJ j)[_] = (
  gui=Gui(j=j,package=S"miniGui",imports=S"""%
    | %Gui.defaultImports()
    | import javax.swing.table.DefaultTableModel;
    """,
    name='Example,x=800\,y=600\
    )
  gui"""%
    |JPanel screen1=new JPanel();
    |{add(screen1);}
    |JPanel buttons=new JPanel();
    |{addNorth(screen1,buttons);}
    |%gui.button(id=S"addPerson",msg='PressedAdd,text=S"add")
    |{addFlow(buttons,addPerson);}
    |%gui.button(id=S"removeById",msg='PressedRemove,text=S"remove by id")
    |{addFlow(buttons,removeById);}
    |%gui.button(id=S"removeByName",msg='PressedRemove,text=S"remove by name")
    |{addFlow(buttons,removeByName);}
    |%gui.button(id=S"printAll" msg='PressedPrint text=S"printAll")
    |{addFlow(buttons,printAll);}
    |Object[] tLabels={"id","name","age","height","weight"};
    |DefaultTableModel tModel=new DefaultTableModel(new Object[][]{},tLabels);
    |JTable table = new JTable(tModel);
    |{addCenter(screen1,new JScrollPane(table));}
    |{event.registerEvent("Example.Display","tableAdd",
    |  (k,id,msg)->SwingUtilities.invokeLater(()->tModel.addRow(msg.split(","))));}
    |{event.registerEvent("Example.Display","tableClear",
    |  (k,id,msg)->SwingUtilities.invokeLater(()->tModel.setRowCount(0)));}
    """
  )
CCode

Where Wcode(gui.addButton(..)) is a convenient way to generate a button raising 42 events.
When such 42 code will run, the following Java code will be generated:
OJCode
package miniGui; //generated and compiled code, it is not saved on any file
import javax.swing.*;
import l42Gui.*;//contained in GuiSupport.jar
import is.L42.platformSpecific.javaEvents.Event; //standard 42 Event class
import javax.swing.table.DefaultTableModel;

public class Example {
  public Example (Event event){
    l42Gui.L42Frame f=l42Gui.L42Frame.open(
      ()->new l42Gui.L42Frame(event,"Example",800,600){
        JPanel screen1=new JPanel();
        {add(screen1);}
        JPanel buttons=new JPanel();
        {addNorth(screen1,buttons);}
        JButton addPerson = new JButton("add");{
          addPerson.addActionListener(e->event
            .submitEvent("Example","addPerson","PressedAdd"));
        }
        {addFlow(buttons,addPerson);}
        JButton removeById = new JButton("remove by id");{
          removeById.addActionListener(e->event
            .submitEvent("Example","removeById","PressedRemove"));
        }
        {addFlow(buttons,removeById);}
        JButton removeByName = new JButton("remove by name");{
          removeByName.addActionListener(e->event
            .submitEvent("Example","removeByName","PressedRemove"));
        }
        {addFlow(buttons,removeByName);}
        JButton printAll = new JButton("printAll");{
          printAll.addActionListener(e->event
            .submitEvent("Example","printAll","PressedPrint"));
        }
        {addFlow(buttons,printAll);}
        Object[] tLabels={"id","name","age","height","weight"};
        DefaultTableModel tModel=new DefaultTableModel(new Object[][]{},tLabels);
        JTable table = new JTable(tModel);
        {addCenter(screen1,new JScrollPane(table));}
        {event.registerEvent("Example.Display","tableAdd",
          (k,id,msg)->SwingUtilities.invokeLater(
            ()->tModel.addRow(msg.split(","))));
        }
        {event.registerEvent("Example.Display","tableClear",
          (k,id,msg)->SwingUtilities.invokeLater(
            ()->tModel.setRowCount(0)));
        }
      }
    );
  }
}
CCode
and in the java main Wcode(new Example(eventCapability))
will be called.
As you can see, the code provided by the user is simply injected into
the body of a Wcode(JFrame) class.
From that context we can declare fields and methods, and we can declare initialization actions using (non-static) java initialization blocks.

This code would look trivial if you are a Java Swing expert, and very obscure otherwise.

Note how we use Wcode(tModel.addRow(msg.split(",")))
to add the row to the table: in java Wcode(addRow) wants an array
and Wcode(msg.split(",")) will produce an array from a string whose parts are separated by Wcode(',').
We made in this way for the sake of a simple example, but
we are unsatisfied by this brittle solution: it only works since names or numbers should not have the Wcode(',') inside.

WTitle((4/5) Putting all together)
Finally, a Wcode(Main) puts this all together
OBCode
reuse [L42.is/AdamsTowel]
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

Main=(
  j=GuiJ.#$of()//the Java GUI slave
  sql=Queries(DB.#$of())//sql queries and the Java DB slave
  iql=Dialogs(IQL(j))//iql queries supported by the GUI slave
  model=Model(j=j,sql=sql,iql=iql)
  OpenGui(j=j)
  for e in j(\['Example]) ( e>>model )//event loop
  )
CCode
As you can see, the GUI produces events on the channel Wcode(Example) (the name of the generated Java class) and 
consumes events on the channel Wcode(Example.Display).
WP
If we wanted to add functionalities to initialize and 
to clear the database, we could do as follow: 
OBCode
Create = DB.query[Void]"""
  |CREATE TABLE Person (
  |  id int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
  |    (Start with 1, Increment by 1),
  |  name varchar(255),
  |  age int,
  |  height decimal(5,2),
  |  weight decimal(5,1)
  |  )
  """
PopulateIfEmpty = { //if the DB is already set up, executing Create
  return Create(DB.#$of())() //will throw an exception.
  catch Message _ return void // if we ignore it such exception encodes
  } //an "initialize the DB if needed" operation.
CCode
WP
OBCode
Drop = DB.query[Void]"DROP TABLE Person"
AfterMain = Drop(DB.#$of())()
CCode

WTitle((5/5) Summary)
42 metaprogramming allows for complex applications to be written in compact and secure ways:
in this application we used 
Wcode(Unit),
Wcode(JavaServer),
Wcode(GuiBuilder) and
Wcode(Query).
Those are all normal 42 libraries that 42 programmers could write themselves, and indeed studying the implementation of those libraries is currently the best way to become a Magrathean.
WP
In particular, Wcode(Query) allows us 
to take queries written in another language (for now, just SQL and IQL, but the concept is expandable)
and converts them into a simple 42 well typed API that can be used
to build programs in an compact and elegant way.
Concepts like the Wcode(QueryBox) can be used to control what part of an application is allowed to do important operations, adding a great deal of security.
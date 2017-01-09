# L42

Here I will try t explain how to set up L42 in eclipse.
On Eclipse Luna, you may need to install EGit
On Eclipse Mars EGit installation is not needed.

Do (file->)import ->git-> projects(from git)->clone uri
If asked, import Eclipse projects (instead of NewProject or GeneralProject)
You should get all the projects already  setted up with the right dependencies.

The following should be all the relevant projects:
1 main
2 reduction
3 typesystem
4 tests
5 .. n plus all the plugins

If you have any problem of dependencies, you can manually set up dependences in the class paths.
2,3 and 5..n depends from 1. Tests(4) depends from everyone else.
Additionally, main and tests depends from antlr.jar and main depends from lombock.jar.
Both antlr and lombock are "generators of code" and there are
corresponding launch configurations in the project "main".
**If you do not change Main/L42.g4 you do not need to run AntlrGeneration
**If you do not change Main/srcLombock you do not need to run DelombockAst
**On need you can launch them by simply right clicking on the corresponding .lauch file

You have to set up tests so that is going to use the 
JRE inside a JDK
and the version should be after jdk 1.8.0_40
I suggest you to put in the default vm args of the JDK -ea, so that the assertions are always enabled.
To do so: windows/preferences/installed JREs/<yours>/edit/

In order to define the AST and some more datastructures I used lombock, that
saved me from writing about 5k of boilerplate code.
lombock is a metaprogramming tool that exapand some annotations in usefull methods.

## Building it from the command line
Currently, only Main is built (without running ANTLR and lombok):

* Install sbt
* Change JDK path in javacOptions in build.sbt

Then in the repository directory, run:

    sbt compile
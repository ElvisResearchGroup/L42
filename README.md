# L42

Here I will try t explain how to set up L42 in eclipse.
On Eclipse Luna, you may need to install EGit
On Eclipse Mars EGit installation is not needed.

Do import ->git-> projects->clone uri
You should get all the projects already  setted up with the right dependencies.

The following should be all the relevant projects:
1 main
2 reduction
3 typesystem
4 all plugins
5 tests
If you have any problem of dependencies, you can manually set up dependences in the class paths.
2,3 and 4 depends from 1. Tests depends from everyone else.
Additionally, main and tests depends from antlr.jar and main depends from lombock.jar.
Both antlr and lombock are "generators of code" and there are
corresponding launch configurations in the project "main".
You can launch them by simply right clicking on the corresponding .lauch file

You have to set up tests so that is going to use the 
JRE inside a JDK
and the version should be after jdk 1.8.0_40
I suggest you to put in the default options of the JDK -ea, so that the assertions are always enabled.


In order to define the AST and some more datastructures I used lombock, that
saved me from writing about 5k of boilerplate code.
lombock is a metaprogramming tool that exapand some annotations in usefull methods.


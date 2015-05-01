# L42

Here I will try t explain how to set up
L42 in eclipse.
You will need to create multiple projects.

The following should be all the relevant projects:
1 main
2 reduction
3 typesystem
4 all plugins
5 tests

set up dependences in the class paths.
2,3 and 4 depends from 1.
tests depends from everyone else.
Additionally, main and tests depends from antlr.jar
and main depends from lombock.jar.
Both antlr and lombock are "generators of code" and there are
corresponding launch configurations in the project "main".

Install testNG for tests. http://testng.org/doc/download.html
under help/install new software

You have to set up tests so that is going to use the 
JRE inside a JDK
and the version should be after jdk 1.8.0_40


In order to define the AST and some more datastructures I used lombock, that
saved me from writing about 5k of boilerplate code.
lombock is a metaprogramming tool that exapand some annotations in usefull methods.


This folder contains the code mentioned in our paper.
The full source code for 42 is available at <https://github.com/ElvisResearchGroup/L42/tree/invariant-artifact>.

The concrete syntax of L42 is different from the more Java like syntax shown in the paper; in particular in L42 we use `Data<><{/*code*/}` in order to insert invariant checks on SafeMovable (as well as checking the additional requirement needed over the base L42 type system), as you can see in line 24 of L42/Gui/This.L42


L42.jar
-------------------------
This is a very minimal L42 IDE, it does take a while to load the first time, so please be patient and wait for the top right button to be enabled. It is tested as working with Oracle JDK 8 and 10 on Linux and Windows; note that the 'java' inside the JDK is required, and it does not work with the 'java' inside a JRE or OpenJDK. The IDE also acts as a compiler, just run it from the command line with 'java -jar L42.jar FileName.L42", note that the compiler requires a pair of curly brackets around the source code
while the IDE does not. The jar needs to run from the same folder containing the 'localhost' directory. Thus just double clicking on the jar may not work (depending how your system is set up). From the command line, typing 'java -jar L42.jar' will open up the IDE correctly.
 
D
--------
This contains our encoding of our family example in D (see Appendix C).

Eiffel
--------------------
This contains our encoding of our family example in Eiffel (see Appendix C).

L42/Family
---------------------
This contains our encoding of our family example in L42 (see Appendix C).

L42/GUI
------------------------
This folder contains the source code for our GUI example, just open the folder in the IDE and press the run button (in the top right, if it says loading just wait a bit).

L42/Spec# Papers
------------------------
This folder contains our L42 encoding of the examples from the 'Verification of Object-Oriented Programs with Invariants' paper (see Appendix C).


L42/WidgetGui
-----------------
This contains the source code for L42 Widget library we wrote us part of our case study. We have already included the compiled version of it, but it can be recompiled by running it in the IDE (as above).

localhost
-------------------------
This file contains pre compiled 42 libraries and other resources needed for the IDE to work. You can ignore this folder.

Spec#/Debug
-----------
This folder contains our code we used to count the number of invariant checks in Spec#. Note that it is not run through the static verifiery.

Spec#/GUI
-------------------------
This folder contains the source code for our Spec# version of the GUI. It requires Visual Studio 2012 and Spec#. If you want to run the verifier, you will need Z3 version 2.15 exactly.

Spec#/Family
--------------
This contains our encoding of our family example in Spec# (see Appendix C).

Spec#/Papers
--------------
This folder contains our Spec# encoding of the examples from the 'Verification of Object-Oriented Programs with Invariants' paper (see Appendix C).

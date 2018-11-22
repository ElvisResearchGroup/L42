
>  **WARNING! This file is encoded in UTF-8**
> Notation: This file is written in markdown, some conventions I'm using:
> * ==text surrounded by double-equal signs, is a comment, i.e. not part of the presentation==
> * > text prefixed by a > symbol is to be spoken, but not put on the slides
> * I will use dashes/lines, to delineate slides:
>  ----------------------------------------------------------------------------------------------------------------------------
>  I will use indented dashed lines to indicate a subslide break:
>> -----------------------------------------------------------------------------


----------------------------------------------------------------------------------------------------------------------------
### Callability Control

Isaac Oscar Gariano and Marco Servetto
Victoria University of Wellington

----------------------------------------------------------------------------------------------------------------------------

### The Problem

Consider an imperative language, such as C♯, which:
* is statically-typed
* has named/identifiable functions (e.g. static/instance methods or constructors)
* has dynamic dispatch (e.g. interfaces, delegates, and virtual methods)
* supports dynamic code loading and execution
==TODO shorten the above, or just speek it?==

>----------------------------------------------------------------------------------
1. What *could* this method do?
```CSharp
static void m1() { Math.abs(0); }
```
> Well it looks like it does nothing, it just gets the absolute value of 0 (which is 0) and discards the result.
> But how do we know Math.abs won't send your home folder to an Haker?
> Well the documentation dosn't say it will, and we trust the implementator, Microsoft, to follow it.
> But, if we didn't trust it, what could we do?

>--------------------------------------------------------------------------------
2. What about this, what *could* it do?
```CSharp
static void m2(Object o) { o.ToString(); }
```
> This one can do anything the language will let it, so it is likely to be memory and type safe. However it can still do all sorts of stuff, like perform I/O or use reflection to inspect and invoke arbitrary code.
> The reason why we don't know what this can do is that Object.ToString, is a *virtual* method, this means that it is *dynamicaly dispatched* and in this case, literally anyone can override it. So we have to trust the writer of *every*  class that `o` could be at run-time. 

>-----------------------------------------------------------------------------------
3. How about this?
```CSharp
static void m3(String url) {
	var code = Assembly.LoadFrom(url); // Load code (possibly from the internet)
	code.GetType("T").GetMethod("M").Invoke(null, null); } // Call T.M()
```
> This is very problematic, url could refer to code written by anyone, it might not have even been written yet when m3 was written. So we cannot inspect the code or simply trust their authors.

----------------------------------------------------------------------------------------------------------------------------
### Callability 

#### *callability* is the ability to *call* a function/operation.
> ------------------------------------------------------------------------------------
### A *function’s* callability is the set of things it can *call*.

> We will abstract away performing an 'operation' (such as reading a file, create a new object, doing integer addition etc)  as *calling* a named function (or method).
> In this way we can look at a functions *callability* to constrain what a function  can do. /* Improve: For example if it only has the ability to call functions we know wont read from the file system, then we know it wont.*/

> ------------------------------------------------------------------------------------
### Restatment of the Problem:
1.  What is the callability of `Math.Abs`? 
2. What is the callability of `o.ToString`? 
3. What is the callability of `UntrustedClass.UntrustedFunction`?

> We will present a system that can answer these questions by constraining their call-abilities.


----------------------------------------------------------------------------------------------------------------------------
### Operations
To simplify things we will assume that the language provides only two intrinsic functions:
```CSharp
class Operation {
	static Object Unrestricted(String operation, params Object[] args);
	static Object Restricted(String operation, params Object[] args); }
```
==Because everything needs to be in a class? Or should I just make them freestanding?==
> The params thing is just the C♯ way of doing varadics
> We can use them above to model all the languages primtive operations as either Unrestricted or Restricted.
> All other functions will have user-written bodies in the same language.

> -----------------------------------------------------------------------------------------------------------------------------------
#### Example
```String
Operation.Unrestricted("Add", 1, 2); // Returns 3
Operation.Restricted("CCall", "printf", "Hello World!"); // Prints Hello World!
```
> Note that if a function has Operation.Restricted in it's call-ability, it is effectively unconstrained, perhaps it could generate and execute arbitrary assembly code that corrupts your heap.

------------------------------------------------------------------------------------------------------------------------------------
### The Basics 

#### The `calls[f1, …, fn]` annotation
> A function declaration can be suffixed with it, indicating that it has `f`, …, `fn` in its callability. (I.e. that it can call them.)

```
static Void Main(String[] argv) calls[Operation.Restricted] {
	Operation.Restricted("CCall", "printf", "Hello World!"); }
```
> At first glance this looks like we have to list every single function we want to call, however this is not what the anotation means, rather it is the starting point of functions that can be called.

> ----------------------------------------
#### The callability rules:
1. Can call everything in its calls annotation $g  ∈ calls(f) ⇒ f ⇝ g$,

2. Can call any function whose calls annotation subsumes??    $(∀ h ∈ calls(f) ⦁ f ⇝ h) ⇒ f  ⇝ g$


--------------------------------------------------------------------------------------------------------------------------------------
### What they mean / how they work?
==Some simple examples?==

--------------------------------------------------------------------------------------------------------------------------------------
### How to solve Problem 1

--------------------------------------------------------------------------------------------------------------------------------------
### How to solve Problem 2 (generic interfaces)


--------------------------------------------------------------------------------------------------------------------------------------
### How to solve Problem 3 (dynamic checking)
==this is easy, the VM will just do a runtime check when you call `Invoke` based on the callability of the caller. Or perhaps a better idea is to just make the `Invoke` method callability generic (and still do a run time check)==

==Allow dynamically loaded code to call methods that the loading assembly dosn't know about? (even if they are callabilit restricted)==

Object invoke<'a>(Object receiver, Object[] params) calls['a];


--------------------------------------------------------------------------------------------------------------------------------------
### Future work & Problems we still have
* Making it less verbose:
   * inference
   * Implication/wildcards (e.g. naming a set of methods)
* Deal with dynamic loading more
* Formalisism
  *   Formalise a complete language (with generics)
  *  Formalise the properties we want and prove them


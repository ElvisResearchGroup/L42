

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
	```csharp
	static void m1() { Math.abs(0); }
	```
	> Well it looks like it does nothing, it just gets the absolute value of 0 (which is 0) and discards the result.
	> But how do we know Math.abs won't send your home folder to an Haker?
	> Well the documentation dosn't say it will, and we trust the implementator, Microsoft, to follow it.
	> But, if we didn't trust it, what could we do?

>--------------------------------------------------------------------------------
2. What about this, what *could* it do?
==Note: I am now using a user-defined interface, so that I can freely play with it's declaration==
	```csharp
	interface I { void Run(); }
	static void m2(I x) { x.Run(); }
	```
	> This one can do anything the language will let it, so it is likely to be memory and type safe. However it can still do all sorts of stuff, like perform I/O or use reflection to inspect and invoke arbitrary code.
	> The reason why we don't know what this can do is that `I.Run`, is a *virtual* method, this means that it is *dynamically dispatched* and in this case, literally anyone can override it. So we have to trust the writer of *every*  class that `x` could be at run-time. 

>-----------------------------------------------------------------------------------
4. How about this?
	```csharp
	static void m3(String url) {
		var code = Assembly.LoadFrom(url); // Load code (possibly from the internet)
		code.GetType("T").GetMethod("M").Invoke(null, null); } // Call T.M()
	```
	> This is very problematic, url could refer to code written by anyone, it might not have even been written yet when m3 was written. So we cannot inspect the code or simply trust their authors.

----------------------------------------------------------------------------------------------------------------------------
### Callability 

#### *callability* is the ability to *call* a function/operation.
> ------------------------------------------------------------------------------------
#### A *function’s* callability is the set of things it can *call*.

> We will abstract away performing an 'operation' (such as reading a file, create a new object, doing integer addition etc)  as *calling* a named function
> In this way we can look at a functions *callability* to constrain what a function  can do. /* Improve: For example if it only has the ability to call functions we know wont read from the file system, then we know it wont.*/

> ------------------------------------------------------------------------------------
#### Restatment of the Problem:
1.  What is the callability of `Math.Abs`? 
2. What is the callability of `x.Run`? 
3. What is the callability of `T.M`?

> We will present a system that can answer these questions by constraining their call-abilities.


----------------------------------------------------------------------------------------------------------------------------
### Operations
To simplify things we will assume that the language provides only two intrinsic functions:
```csharp
static class Operation {
	static Object Unrestricted(String operation, params Object[] args);
	static Object Restricted(String operation, params Object[] args); }
```
==Because everything needs to be in a class? Or should I just make them freestanding?==
> The params thing is just the C♯ way of doing varadics
> We can use them above to model all the languages primtive operations as either Unrestricted or Restricted.
> All other functions will have user-written bodies in the same language.

>    -------------------------------------------------------------------------------------------
#### Example
```csharp
Operation.Unrestricted("Add", 1, 2); // Returns 3
Operation.Restricted("CCall", "puts", "Hello World!"); // Prints Hello World!
```
> Note that if a function has Operation.Restricted in it's callability, it is effectively unconstrained, perhaps it could generate and execute arbitrary assembly code that corrupts your heap.

------------------------------------------------------------------------------------------------------------------------------------
### The Basics 

#### The `calls[f1, …, fn]` annotation
> A function declaration can be suffixed with it, indicating that it has `f`, …, `fn` in its callability. (I.e. that it can call them.)

```csharp
static Void Main(String[] argv) calls[Operation.Restricted] {
	Operation.Restricted("CCall", "printf", "Hello World!"); }
```
> At first glance this looks like we have to list every single function we want to call, however this is not what the anotation means, rather it is the starting point of functions that can be called.
> In addition, the system can in principle infer these annotations, but we will write the out explicitly to aid explanation.

> ----------------------------------------
#### The Callability  Relation: $f ⇝ g$

> As part of typechecking, whenever their is a call to $g$ from within the body of a function $f$, we will check that f ⇝ g.

>    -------------------------------------------------------------------------------------------
==I'm thinking of right-alligning the formalisism, so the english is on the left of the slide, and the formalisism on the right? Maybye I can also put boxes or shading arround the formalisism, to make it clear that is not part of the text per se.==
A function $f$ can call $g$ iff: $f ⇝ g$
1. $g$ is in the `calls[…]` annotation of $f$: $g  ∈ Calls(f) ⇒ f ⇝ g$.
	> this is exactly what the calls annotation means

	Example
	```csharp
	void Foo() calls[Bar,Baz] { Bar(); }
	```

>    -------------------------------------------------------------------------------------------
2. $f$ can call every function in the `calls[…]` annotation of $g$: $(∀ h ∈ Calls(f)\ ⦁\ f ⇝ h) ⇒ f  ⇝ g$
	> this makes sense, since you could have just inlined the body of $g$, so your not really gaining any power.
	
	Example:
	```csharp
	void Bar() calls[Baz] { Baz(); }
	void Foo() calls[Baz] { Bar(); }
	```
> Note that these rules imply that functions can always recursively call themselves, so you don't have to write loops.
--------------------------------------------------------------------------------------------------------------------------------------
### Consequences
==This slide is about controling when a function can be called==
```csharp
static class Operation {
```

> These rules have two important uses, nameley

> ----------------------------------------------------------------------------------------------------------------------
1. A function with `calls[]` can be called anywhere:
	```csharp
	static Object Unrestricted(String operation, params Object[] args) calls[];
	```
	> thanks to Rule 2.
> ----------------------------------------------------------------------------------------------------------------------
2. A function $f$ annotated with `calls[f, …]` can only be called by functions also annotated with `calls[f, …]`
	> this prevents Rule 2 from every applying when rule 1 does not
	
	==TODO give a better (and shorter) explanation for the slide==
	```csharp
	static Object Restricted(String operation, params Object[] args) calls[Restricted]; }
	```

--------------------------------------------------------------------------------------------------------------------------------------
### The Solution to Problem 1 (Static Dispatch)
==This slide is about reasoning on what a function can do based on it's declaration==
What can `Math.abs(0);` do?
> To answer this question, we will have to look at the declaration of `Maths.abs(0)`, for it's `calls` annotation, consider the following possibilities
```csharp
static class Math {
```
> ------------------------------------------
1. (indirectly) perform only `Unrestricted` operations:
	```csharp
	static Int32 Abs(Int32 x) calls[] { … }
	```
	> this is probably what you'd want for an Abs function, it could do arithmetic negation and comparison, but not restricted operations like I/O.

> ------------------------------------------
2. also (indirectly) perform *some*  `Restricted` operations:
	```csharp
	static void Print(String s) calls[Operation.Restricted] {
		Operation.Restricted("CCall", "puts", s}; }
	static Int32 Abs(Int32 x) calls[Print] { … }
	```
	> By looking at the body of Print, we know that Abs can only print to stdout, but do no other I/O.

> ------------------------------------------
3. also (indirectly) perform *any* `Restricted` operation:
	```csharp
	static Int32 Abs(Int32 x) calls[Operation.Restricted] { … }
	```

> ------------------------------------------
> 
#### Benefits
*  No need to look at the body of `Abs` 
> since the language will ensure that it is well-typed
* No need to look at all code we are compiling with
> We only need to look at things mensioned by the declaration of Abs
* Our reasoning is static and sound
> We don't need to first run the program, and once we do, our conclusions will still hold
--------------------------------------------------------------------------------------------------------------------------------------
### How to solve Problem 2 (Dynamic Dispatch)
What can `x.Run` do?

> How does this work in dynamic dispatch
> Well we can just use the same reasoning we used above by looking at the declaration of `I.Run`

> ---------------------------------------------------------------------------------------------------------
```csharp
interface I { void Run() calls[Print]; }
```
> And so it can Print but perform no other restricted operation.

> This approach is however inflexible?
==Describe properly whats wrong with it==

> ------------------------------------------------------------------------------------------------------
#### Callability Generics
> The obvious solution is to add a layer of abstraction

Consider this:
```csharp
interface I<'a> { void Run() calls['a]; }
```
> Here `'a` is a *generic callability paramter*, it can be substiuited for any list of functions.

> ---------------------------------------------------------------------------------
Now to answer the question: what can `x.Run` do?
> well it depends on how you declare it's type:

1.  Only unrestricted operations:
	```csharp
	static void m2(I<[]> x) calls[] { x.Run(); }
	```
2. Can also print,
	```csharp
	static void m3(I<[Print]> x) calls[Print] { x.Run();}
	```
3. Any *restricted* operation:
	```csharp
	static void m3(I<[Operation.Restricted]> x) calls[Operation.Restricted] { x.Run();}
	```
5. Whatever you want
	```csharp
	static void m3<'a>(I<'a> x) calls[I<'a>.Run] { x.Run();}
	```
--------------------------------------------------------------------------------------------------------------------------------------
### How to solve Problem 3 (Dynamic Loading)

> If we don't statically know the declaration of the function we are calling, we cannot statically check it.
> But, we can always do a runtime-check.

> ------------------------------------------
> Consider this class
```csharp
class FunctionHandle<'a> {
	FunctionHandle(Assembly a, String f) calls[] { 
		// throw an exception if FunctionHandle<'a>.Invoke ⇝̸ f
		… } 
	Object Invoke(params Object[] args) calls['a] { /* return f(args)*/ }}
```
> the idea being that `new FunctionHandle(f)` will check that the declared call-ability of f

> we can now use this to safely run code downloaded from the internet!

> ---------------------------------------------------------------
```csharp
static void m3(String url) calls[] {
	var code = Assembly.LoadFrom(url);
	new FunctionHandle<[]>(code, "T.M").Invoke(); } // calls T.M()
```
> This will throw an exception if T.M isn't declared to only call Unrestricted operations.
> Note that the check will happen before you invoke `T.M`, giving you an early error.

--------------------------------------------------------------------------------------------------------------------------------------
### Future Work
* Make it less verbose
   * inference of `calls` annotations
   * add wildcard support
   * allow named groups of functions
* Improve the support for dynamic loading
  *  Allow calling new functions, even they call themselves
* Formalise the reasoning properties we want from the system


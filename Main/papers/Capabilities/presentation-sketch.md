>  **WARNING! This file is encoded in UTF-8**
> Notation: This file is written in markdown, some conventions I'm using:
> * ==text surrounded by double-equal signs, is a comment, i.e. not part of the presentation==
> * > text prefixed by a > symbol is to be spoken, but not put on the slides
> * I will use dashes/lines, like this:
>  ----------------------------------------------------------------------------------------------------------------------------
>  to delineate slides. 


<br></br>
<br></br>

----------------------------------------------------------------------------------------------------------------------------
### Callability Control

Isaac Oscar Gariano and Marco Servetto
Victoria University of Wellington

----------------------------------------------------------------------------------------------------------------------------

### The Problem

Consider an imperative language, such as C♯.
> In principle, our work would work in any statically-compiled language with named functions.

==The 3 dotpoints bellow should probably be on different 'subslides'?==

1. What *could* this method do?
```CSharp
static void m1() { Math.abs(0); }
```
> Well it looks like it does nothing, it just gets the absolute value of 0 (which is 0) and discards the result.
> But how do we know Math.abs won't send your home folder to Russia?
> Well the documentation dosn't say it will, and we trust the implementator, Microsoft, to follow it.
> But, if we didn't trust it, what could we do?

2. What about this, what *could* it do?
```CSharp
static void m2(Object o) { o.ToString(); }
```
> This one can do anything the language will let it (it probably can't read all local-variables on the stack, but it can still do all sorts of stuff, read or write files, use reflection to inspect our your code, use reflection to access private fields, etc.
> The reason why we don't know what this can do is that Object.ToString, is a *virtual* method, this means that it is *dynamicaly dispatched* and in this case, literally anyone can override it. So we have to trust the writer of *every*  class that `o` could be at run-time. 

3. How about this?
```CSharp
static void m3(String url) {
 // Load code (possibly from the internet)
 var code = Assembly.LoadFrom(url);
 // Call UntrustedClass.UntrustedFunction()
 code.GetType("UntrustedClass").GetMethod("UntrustedFunction").Invoke(null, null);}
```
> This is verry problematic, url could refer to code written by anyone, it might not have even been written yet when m3 was written. So we cannot inspect the code or simply trust their authors.

==A shorter version of this example  would be ```code.CreateInstance("UntrustedClass")```which executes `new UntrustedClass()`, but that is a constructor call, and it may be easier to just talk about methods, my example above is more clear==

----------------------------------------------------------------------------------------------------------------------------
### Callability 
> Ok, so what is callability, and how was the previous slide related to it?

#### *callability* is the ability to *call* a function/operation.

### A *function’s* callability is the the set of things it can *call*.

> We will abstract away performing an 'operation' (such as reading a file, create a new object, doing integer addition etc)  as *calling* a named function.
> In this way we can look at a functions *callability* to constrain what it can do, if it only has the ability to call functions we know wont read from the file system, then we know it wont.


### Restatment of the Problem:
1.  What is the callability of `Math.Abs`? 
2. What is the callability of `o.ToString`? 
3. What is the callability of `UntrustedClass.UntrustedFunction`?

>  Of course we can try and answer these questions directly, or we could instead try and directly constrain the callability of `m1`, `m2` and `m3`, which is what our work allows you to do.


----------------------------------------------------------------------------------------------------------------------------

What kind of solution do we want?
------------------
> There are currently many ways to control and analyse callability, however we know of none that satisfies all the following points.

==I think I need help with this slide...==


--------------------------------------------------------------------------------------------------------------------------------------

### The basics

#### The `calls[f, …]` anotation
#### The callability rules:
1. $f ⇝ f$
2. $g  ∈ f.calls ⇒ f ⇝ g$
3. $(∀ h ∈ g.calls\  ⦁\ f ⇝ h) ⇒ f  ⇝ g′$

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

--------------------------------------------------------------------------------------------------------------------------------------
### Future work & Problems we still have
* Making it less verbose:
   * inference
   * Implication/wildcards (e.g. naming a set of methods)
* Deal with dynamic loading more
* Formalisism
  *   Formalise a complete language (with generics)
  *  Formalise the properties we want and proove them


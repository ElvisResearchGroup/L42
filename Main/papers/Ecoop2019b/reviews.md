Lines/paragraphs starting with a '>' are comments we have explicitly decided to ignore, ones starting with ### are ones we have yet to deal with (or note in the paper itself)
 
 REVIEW 1, Overall evaluation: -2 (reject, serious problems)
--------------------------------------------------------------------------------------------------

> It is still not clear how expressive the proposed mechanism is, considering the proposed set of several syntactic and semantic restrictions required by capsules, capabilities, type modifiers and invariant methods.  
 
> It is not clear, how a user of a class can access and know about its invariant.  
  
### The writing of the paper is not easy to follow and not accessible to a general reader. 
> This may be because there are multiple problems that are simultaneously involved in making the verification of invariants difficult and each of these problems requires a separate or a combination of solutions. Maybe separate explanations of all these problems (purity, determinism, aliasing, and mutability control along with subtyping, dynamic dispatch, exceptions and IO) and their solutions (type modifiers and object capabilities), before combining them all into the main problem and a solution in a single example (on page 2) can help.  
  
  
###  It is not clear how discussions about  
### (1) exceptions  
### (2) nondeterminism (sequential or parallel code)  
### (3) IO  
### all together add to the main proposal of the paper.  
### If not essential, these discussions could be taken out to make the paper more focused.  

  
> The user of a class cannot learn its invariant from the interface of the class, because the invariant is encoded in the implementation of the method "invariant". Consequently, the user cannot know from the class interface what are valid and invalid states of the objects of the class. These two problems together, make invariant and validity both inaccessible and less useful to the user.  
In addition, any invocation of the "invariant" method always returns true.  
  
> The invariant can be encoded in the interface of the class as an annotation. However, there should be a mechanism to ensure that the invariant checked by the method "invariant" is the same as the invariant encoded in the interface of the class. Both of these are currently missing.  
  
> It is not clear how dependency of  
(1) invariant protocol  
(2) capsule annotation semantics  
on the entirety of the object graph of a reference limits the expressiveness of the proposal.  
> "Our invariant protocol guarantees that the whole ROG of any object involved in  
execution (formally, in a redex) is valid"  
  
> It is not clear what are the limitations of capsule annotations especially that  
> (1) they control the entire object graph of a reference  
> (2) their underlying implementation is using linear types  
> - "everything in the ROG (including itself) of a capsule reference is mutable only through that reference; however immutable references can be freely shared across capsule boundaries."  
>- "Capsule references are usually expressed using linear/affine types [14] when they  
are passed around and manipulated."  
Also, it is not clear why "capsule" annotation is introduced, considering that it can probably be implemented using ownership annotations such as "rep" and "peer" and in a more expressive way.  
  
> The evaluation seems to be trivial.  
>- "we used this implementation to implement and test an interactive GUI involving a class with an invariant."  
>- "On a test case with 5 objects with an invariant,"  
  
> There are comparisons in the paper that seem to be both unnecessary and insufficient.  
For example, the comparison between the number of annotations in the proposed model and Spec#'s model is unnecessary and ignores the fact that Spec# model may be more expressive.  
> - "In Spec# we had to add 10 different annotations, of 8 different kinds; some of which were quite involved. In comparison, our approach requires only 7 simple keywords, of  
3 different kinds,"  
> - "We also compared with Spec#, whose invariant protocol provides the same performance as ours, however the annotation burden was almost 4 times higher than our approach."  
  
### The writing is not focused in several places, combines several topics of discussions together and is difficult to understand and follow. It also contains several syntactic and grammatical issues. See the detailed point in the MINOR section of the comments. Some of these grammatical issues were brought up in the previous review and are not fixed.  
  
  
### - "class-invariants" vs "class invariants"  
### - "type-modifiers" vs "type modifiers"  
### - "runtime-verification"  
### - "pack-operation"  
### - "type-system"  

> - Maybe use before rather than after  
> " checked immediately after an invariant violation could have occurred"  

> Is it possible to omit constructors completely, instead of just their bodies?  
"here and in the rest of the paper we omit the bodies of constructors that simply initialise fields with the value of constructors parameters."  
  
### It is not clear why the first example uses reference type Double and its C# equivalent uses primitive double. It is better if both use Double.  
    
  
REVIEW 2, Overall evaluation: 2 (accept, minor revision)  
--------------------------------------------------------------------------------------------------

### This uses both type modifiers and object capabilities. What is the advantage of using both together? Is there any problem with using both together?  
  
### This work uses runtime checking. Clearly when it can be done static checking is better. Could there be a statically checked core and runtime checking for what couldn't be statically checked?  

### Single digit numbers should be spelled out

### [GUI] Justify why your example is representitive. That is why it isn't the peculiar case that works well.
  
### REVIEW 3, Overall evaluation: 0 (acceptable with major revision)  
 
### The comparison would have been much stronger, however, had  their been more than a single synthetic benchmark with a  single invariant, and had their been some other user(s) than  the authors. The authors' anecotal experience does a good job convincing that encoding the desired properties for this  benchmark in Spec# was much more difficult than in L42. But  Spec# is more expressive, so is the added complexity really  avoidable if their goal is to reason about properties beyond what L42 is capable of? It would have nice to identify some  published Spec# benchmark programs, select those which can  be encoded in L42, and do a head-to-head-comparison. Better  yet, a couple users would have been asked to learn L42 and  Spec# to get some anecdotal experience of the relative  merits. As is, the comparison hints at some interesting  observations, but I worry that this one benchmark may not tell the whole story. If accepted for publication, I would hope to see a larger evaluation.  
  
>p.8: The end of Section 2 seems very L42-specific. Consider  
reframing this more directly as a comparison to other TM/OC  
languages and to Spec#.  
  
### p.8/9: Section 3 feels like a hodgepodge of informal   descriptions. It would be nice if the main ideas behind the   formalization (now moved to the Appendix) could be described  here clearly with a few selected rewrite rules and examples.  
  
### p.1/p.4/...: ", however" ==> ". However," Throughout the    paper, there are many similar misuses of commas (and   semicolons), which ought to be fixed.  
  

Here you can find a sequence of files aiming to provide a 
compact formal definition for the programming language L42.

We chose to use ASCII for the simplicity of editing and visualizing it,
and to avoid the temptation of losing time in choosing colours, fonts and
other graphical details.

-----------------------------------------------------------
Part 0: notations 
-----------------------------------------------------------
ASCII rendering of horizontal bar notation:
* Letters i,j,k,m,n are integers;
  usually indices into sequences or lengths of sequences

* U is the set union,

* \ is the set subtraction

* we use "and" "or" "disjoint" and "intersection" as words.
  "empty" is the empty set/sequence etc.

* we use s instead of overbar, so Cs::=C1..Cn
  This can be ambiguous for metavariables ending with s.
  In that case, the explicitly defined metavariable takes precedence;
  for example ms is defined explicitly, so ms is never interpreted as m1..mn
  In case of need we use parenthesis, so (T x)s::=T1 x1..Tn xn

* -->p is p under the arrow, same for +p
  a^i is a apex i, ai is a pedex i

* [] is the hole/square

* We use <;> as tuple notation

* In latex we use two different fonts for "(" of the grammar and for
  "(" used for disambiguation and function call.  Here we just hope that it is clear :/

* Generally, we use <space> , ; and | to disambiguate grouping, thus
  a b, c;d |e == ((((a b), c);d) |e)

* When a term of the grammar can be accessed with functional notation,
  as in a(b), then dom(a) is {b | a(b) is defined}

* DOT NOTATION b.a extracts the element of form a from b.
  For example, if we suppose that:
    M ::= C:e
    M1 = C1:e1
  then
    M1.e=e1
  More precisely: a term of a grammar is a tree of nodes and terminals.
  The root-most node of "b" that can be represented with the metavariable "a"
  is produced.  That is, if there are two "a"s at the same depth level, 
  then the "." operator is undefined.

* a[with b=b1] replace the element of form b in a with a new b,
  so that (a[with b=b1]).b = b1

* a[b=c2] when b in dom(a), a(b)=c1 modify a so that a(b)=c2
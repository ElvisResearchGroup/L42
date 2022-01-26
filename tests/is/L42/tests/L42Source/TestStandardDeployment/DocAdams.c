m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#AdamsTowel',`AdamsTowel')
WMenuItem(`#Collection',`Collection')
WMenuItem(`#Message',`Message')
</ol>
CFloat

</p><p id="AdamsTowel">
WBigTitle(AdamsTowel)

Wcode(AdamsTowel) is the most developed 42 towel, and it is a good starting point for varius 
forms of towel staining and embroidery.
Refer to the Wlink(`../tutorial_01Basics',tutorial)
for more informations.
WTitle(Importing process and example usage)
OBCode
reuse[L42.is/AdamsTowel]
..
Main = Debug(S"Hello world")
CCode

WTitle(Overview)
OFoldedCode
[OVERVIEW_HERE]
CCode

</p><p id="Collection">
WBigTitle(Collection)
Wcode(Collection) allows to create many kinds of collections.
Here you can see examples of collections of Wcode(Person).
The following code: 
OBCode
Point = Data:{Num x, Num y}
Person = Data:{S name var Point location}

ListPerson = Collection.list(Person)
SetPerson  = Collection.set(Person)
MapSPerson = Collection.map(key=S val=Person)
OptPerson  = Collection.optional(Person)
MatrixPerson  = Collection.matrix(ListPerson, row=10I, col=20I)
LinkeListPerson = Collection.linkedList(Person)
CCode
Would produce the following:
OFoldedCode
[Collection_OVERVIEW_HERE]
CCode

</p><p id="Message">
WBigTitle(Message)
Wcode(Message) allows to create many kinds of messages.
Here you can see an example of a custom message, with an extra method and 
additionally implementing Wcode(Message.Guard).
The following code:
OBCode
KindInformation = Message:{[Message.Guard]
  method S extra()=S"Wow that was a great explanation!"
  }
CCode
Would produce the following:
OFoldedCode
[Message_OVERVIEW_HERE]
CCode

m4_include(`../CommonHtmlDocumentation/footer.h')
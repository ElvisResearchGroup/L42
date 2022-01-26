m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#Unit',`The Unit module')

</ol>
CFloat

</p><p id="Unit">
WBigTitle(The Unit module)
WTitle(Importing process and example usage)

Wcode(Unit) allows to make unit of measures.
While Wcode(Unit) can be used with a mathematical mindset to make many interconnected units of measures,
it is also very useful to simply define incompatible versions of number types.
  
We can import the Wcode(Unit) library as follows:
OBCode
Unit = Load:{reuse[L42.is/Unit]}
CCode

WTitle(Overview under AdamsTowel)
This will give us the following:
OFoldedCode
[OVERVIEW_HERE]
CCode

We can then use Wcode(Unit)
OBCode
SI = Class:Unit.TraitSI['Support=>Num]
Year = Unit(I)
Mana = Unit(Double)
CCode
This will give us the following:
OFoldedCode
[Example_OVERVIEW_HERE]
CCode

m4_include(`../CommonHtmlDocumentation/footer.h')
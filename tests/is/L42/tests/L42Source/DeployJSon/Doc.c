m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(index)
<div class="rotate90"> Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#Json',`Json')
</ol>
CFloat

</p><p id="Json">
WBigTitle(Json)
WTitle(Importing Json and example usage)

The Json module allows for creation and handling of Json objects. 
The standard datatypes are supported. Result is immutable
OBCode
reuse [L42.is/AdamsTowel]
Json = Load:{reuse[L42.is/Json]}
Main=(
  Json.Value v=Json"""
    |[{ "a":1, "b":true, "c":["Hello","World"]}]
    """
  Debug(v)
  )
CCode

WTitle(Overview under AdamsTowel)

OFoldedCode
[OVERVIEW_HERE]
CCode

m4_include(`../CommonHtmlDocumentation/footer.h')


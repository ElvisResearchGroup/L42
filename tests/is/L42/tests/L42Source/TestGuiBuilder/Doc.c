m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#GuiBuilder',`The Gui Builder library')

</ol>
CFloat

</p><p id="GuiBuilder">
WBigTitle(The Gui Builder library)
WTitle(Importing process and example usage)

OBCode
GuiBuilder = Load:{reuse[L42.is/GuiBuilder]}
..
  (
  )
CCode

WTitle(Overview under AdamsTowel)
OFoldedCode
[OVERVIEW_HERE1]
CCode
Exmaple instantiation
OFoldedCode
[OVERVIEW_HERE2]
CCode


m4_include(`../CommonHtmlDocumentation/footer.h')

WComm build using
WComm m4 -P Doc.c > Doc.xhtml
WComm can be seen at https://raw.githack.com/Language42/is/main/testing/GuiBuilder.xhtml

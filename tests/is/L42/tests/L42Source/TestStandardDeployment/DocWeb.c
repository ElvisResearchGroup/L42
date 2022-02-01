m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#WebIntegrated',`The Web Integrated library')

</ol>
CFloat

</p><p id="WebIntegrated">
WBigTitle(The Web Integrated library)
WTitle(Importing process and example usage)


OBCode
WebLib = Load:{reuse[L42.is/WebIntegratedLib]}
..
  (
  )
CCode

WTitle(Overview under AdamsTowel)
OFoldedCode
[OVERVIEW_HERE]
CCode
m4_include(`../CommonHtmlDocumentation/footer.h')

WComm build using
WComm m4 -P Doc.c > Doc.xhtml
WComm can be seen at https://raw.githack.com/Language42/is/main/testing/WebIntegratedLib.xhtml

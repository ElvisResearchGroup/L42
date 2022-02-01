m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#GitWriter',`A library to write on GitHub')

</ol>
CFloat

</p><p id="GitWriter">
WBigTitle(A library to write on GitHub)
WTitle(Importing process and example usage)


OBCode
GW = Load:{reuse[L42.is/GitWriter]}
CCode

WTitle(Overview under AdamsTowel)
OFoldedCode
[OVERVIEW_HERE]
CCode
m4_include(`../CommonHtmlDocumentation/footer.h')

WComm build using
WComm m4 -P Doc.c > Doc.xhtml
WComm can be seen at https://raw.githack.com/Language42/is/main/testing/GitWriter.xhtml

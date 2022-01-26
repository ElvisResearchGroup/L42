m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90"> Index of Content</div>
ContinueFloat
<ol>
WComm WMenuItem(`#Basics',`Basic concepts')
WMenuItem(`#ChapterTitleHere',`Chapter Title Here')

</ol>
CFloat
 
WComm WChapter(`Basics',`tutorial1Basics.c')

</p><p id="ChapterTitleHere">
WBigTitle(Chapter Title Here)
WTitle((1/5) Sub title)
some text
OBCode
[OVERVIEW_HERE]
CCode
more text
m4_include(`../CommonHtmlDocumentation/footer.h')

WComm build using
WComm m4 -P Doc.c > Doc.xhtml

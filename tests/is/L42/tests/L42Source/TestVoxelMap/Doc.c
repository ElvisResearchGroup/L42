m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(baseHeight index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#Process',`The process library')

</ol>
CFloat

</p><p id="Process">
WBigTitle(The process library)
WTitle(Importing process and example usage)

The process library allows to run processes from AdamsTowel.
Wcode(`Process.Real.#$of(..)') creates a capability object permanently connected with a specific
command and option, but the standard input of the program can be specified by the capability user.
OBCode
Process = Load:{reuse[L42.is/Process]}
..
  (
  Process pLinux=Process.Real.#$of(\[S"ls";S"-l"])
  res=pLinux.start(input=S"")
  Debug(res.out())
  Debug(res.err())
  catch Process.Fail f Debug(S"oh no!")
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
WComm can be seen at https://raw.githack.com/Language42/is/main/testing/Process.xhtml

m4_changecom()m4_dnl
m4_define(`OCode',`<pre class="l42"><![CDATA[')m4_dnl
m4_define(`CCode',`]]></pre>')m4_dnl
m4_define(`OBCode',`<pre class="l42Big"><![CDATA[')m4_dnl
m4_define(`OFoldedCode',`<pre class="l42BigFolded"><![CDATA[')m4_dnl
m4_define(`OJCode',`<pre class="java"><![CDATA[')m4_dnl
m4_define(`OHCode',`<pre class="html"><![CDATA[')m4_dnl
m4_define(`CJCode',`]]></pre>')m4_dnl
m4_define(`Wcode',<it style="font-style: bold; color:green;">&#171;</it>`<span class="code"><![CDATA[$1]]></span>'<it style="font-style: bold; color:green;">&#187;</it>)m4_dnl
m4_define(`WcodeQuote',`<h6 class="code">$1</h6>')m4_dnl
m4_define(`Wlink',`<a href="$1.xhtml">$2</a>')m4_dnl
m4_define(`WP',`</p><p>')m4_dnl
m4_define(`WBR',`<br/>')m4_dnl
m4_define(`WTitle',`</p><h2> $1 </h2> <p>')m4_dnl
m4_define(`WBigTitle',`</p>WNewPage<h1> $1 </h1> <p>')m4_dnl
m4_define(`WMS',`<span style="color:#ff0000">Marco:$1</span>')m4_dnl
m4_define(`WEmph',`<span style="color:#ff1111; font-weight: bold;"> $1 </span>')m4_dnl
m4_define(`WTerm',`<it style="font-style: italic;">$1</it>')m4_dnl
m4_define(`WChapter',` </p><p id="$1"> m4_include(`$2')')m4_dnl
m4_define(`WMenuItem',` <li><a href="$1"> $2 </a></li>')m4_dnl
m4_define(`OFloatClass',`<div class="$1"><div class="wrapFloat"><div class="inFloat">')m4_dnl
m4_define(`OFloat',`<div><div class="wrapFloat"><div class="inFloat">')m4_dnl
m4_define(`ContinueFloat',`</div><div class="inFloat">')m4_dnl
m4_define(`WNewPage',`<div style="break-after:page"></div>')m4_dnl
m4_define(`CFloat',`</div></div><div class="finishFloat"></div></div>')m4_dnl
m4_define(`WComm',`m4_dnl ')m4_dnl
m4_define(`WPrevNext',
`<br/>
OFloatClass(index)
ContinueFloat
&nbsp; &nbsp; &nbsp; <a href="tutorial_$1.xhtml"> Previous </a>...<a href="tutorial_$2.xhtml"> Next </a>
CFloat
<br/>'
)m4_dnl

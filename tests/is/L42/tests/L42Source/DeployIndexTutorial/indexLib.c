m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

m4_define(`url',`###libraries###/')
m4_define(`combine', `$1$2')

OFloatClass(index)
<div class="rotate90"> Index of Libraries</div>
ContinueFloat
<ol>
WMenuItem(combine(url,`AdamsTowel.xhtml'),`AdamsTowel')
WMenuItem(combine(url,`FileSystem.xhtml'),`FileSystem')
WMenuItem(combine(url,`Json.xhtml'),`Json')
WMenuItem(combine(url,`Time.xhtml'),`Time')
WMenuItem(combine(url,`Unit.xhtml'),`Unit')
WMenuItem(combine(url,`Process.xhtml'),`Process')
WMenuItem(combine(url,`VoxelMap.xhtml'),`VoxelMap')
WMenuItem(combine(url,`JavaServer.xhtml'),`JavaServer')
WMenuItem(combine(url,`GuiBuilder.xhtml'),`GuiBuilder')
WMenuItem(combine(url,`Query.xhtml'),`Query')
WMenuItem(combine(url,`RawQuery.xhtml'),`RawQuery')
WMenuItem(combine(url,`HttpRequest.xhtml'),`HttpRequest')
WMenuItem(combine(url,`GitWriter.xhtml'),`GitWriter')
WMenuItem(combine(url,`Deploy.xhtml'),`Deploy')
WMenuItem(combine(url,`WebIntegrated42Lib.xhtml'),`WebIntegrated42Lib')
</ol>
CFloat
<p> Here you can find the documentation for the 42 modules and towels
</p>
m4_include(`../CommonHtmlDocumentation/footer.h')
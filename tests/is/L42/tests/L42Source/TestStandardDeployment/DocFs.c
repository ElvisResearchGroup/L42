m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(index)
<div class="rotate90">Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#FileSystem',`The File System module')

</ol>
CFloat

</p><p id="FileSystem">
WBigTitle(The File System module)
WTitle(Importing process and example usage)

The File System module allows basic interactions with the file system
Wcode(`Fs.Real.#$of(..)') creates a capability object connected with the real file system.
OBCode
Fs = Load:{reuse[L42.is/FileSystem]}
..
Fs fs = Fs.Real.#$of()
S res = fs.read(Url"a/b.txt")
fs.write(on=Url"a/b.txt" content=res++res)
CCode

WTitle(Overview under AdamsTowel)
OFoldedCode
[OVERVIEW_HERE]
CCode
m4_include(`../CommonHtmlDocumentation/footer.h')
m4_include(`../CommonHtmlDocumentation/header.h')m4_dnl
m4_include(`../CommonHtmlDocumentation/postHeader.h')m4_dnl

OFloatClass(index)
<div class="rotate90"> Index of Content</div>
ContinueFloat
<ol>
WMenuItem(`#Time',`Time')
</ol>
CFloat

</p><p id="Time">
WBigTitle(Time)
WTitle(Importing time and example usage)
The time module enables retrieval of the unix universal time and automatic conversion of
that time into a timezone with a preferred format.
OBCode
reuse [L42.is/AdamsTowel]
Time = Load:{reuse[L42.is/Time]}
Main=(
  t = Time.Real.#$of()
  current = t.currentTime()
  date = t.dateTime(zoneId=S"Australia/Sydney",pattern=S"yyyy/MM/dd HH:mm:ss OOOO")
  Debug(current)
  Debug(date)
  )
CCode
WTitle(Overview under AdamsTowel)
OFoldedCode
[OVERVIEW_HERE]
CCode
m4_include(`../CommonHtmlDocumentation/footer.h')

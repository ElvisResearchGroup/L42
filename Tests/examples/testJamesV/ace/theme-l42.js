define("ace/theme/l42",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = true;
exports.cssClass = "ace-l42-theme";
exports.cssText = "\
.ace-l42-theme .ace_gutter {\
background: #1a0005;\
color: steelblue\
}\
.ace-l42-theme .ace_print-margin {\
width: 1px;\
background: #1a1a1a\
}\
.ace-l42-theme {\
background-color: black;\
color: #DEDEDE\
}\
.ace-l42-theme .ace_cursor {\
color: #9F9F9F\
}\
.ace-l42-theme .ace_marker-layer .ace_selection {\
background: #424242\
}\
.ace-l42-theme.ace_multiselect .ace_selection.ace_start {\
box-shadow: 0 0 3px 0px black;\
}\
.ace-l42-theme .ace_marker-layer .ace_step {\
background: rgb(0, 0, 0)\
}\
.ace-l42-theme .ace_marker-layer .ace_bracket {\
background: #090;\
}\
.ace-l42-theme .ace_marker-layer .ace_bracket-start {\
background: #090;\
}\
.ace-l42-theme .ace_marker-layer .ace_bracket-unmatched {\
margin: -1px 0 0 -1px;\
border: 1px solid #900\
}\
.ace-l42-theme .ace_marker-layer .ace_active-line {\
background: #2A2A2A\
}\
.ace-l42-theme .ace_gutter-active-line {\
background-color: #2A112A\
}\
.ace-l42-theme .ace_marker-layer .ace_selected-word {\
border: 1px solid #424242\
}\
.ace-l42-theme .ace_invisible {\
color: #343434\
}\
.ace-l42-theme .ace_keyword,\
.ace-l42-theme .ace_meta,\
.ace-l42-theme .ace_empty,\
.ace-l42-theme .ace_storage,\
.ace-l42-theme .ace_storage.ace_type,\
.ace-l42-theme .ace_support.ace_type {\
color: blue;\
font-weight: bold;\
}\
.ace-l42-theme .ace_testerx{\
color: yellow;\
background-color: red;\
font-weight: bold;\
}\
.ace-l42-theme .ace_testers{\
color: red;\
font-weight: bold;\
}\
.ace-l42-theme .ace_testerm{\
color: gray;\
font-weight: bold;\
}\
.ace-l42-theme .ace_testere{\
color: blue;\
font-weight: bold;\
}\
.ace-l42-theme .ace_keyword.ace_operator {\
color: white;\
font-weight: normal\
}\
.ace-l42-theme .ace_constant.ace_character,\
.ace-l42-theme .ace_constant.ace_language,\
.ace-l42-theme .ace_constant.ace_numeric,\
.ace-l42-theme .ace_keyword.ace_other.ace_unit,\
.ace-l42-theme .ace_support.ace_constant,\
.ace-l42-theme .ace_variable.ace_parameter {\
color: #E78C45\
}\
.ace-l42-theme .ace_constant.ace_other {\
color: gold\
}\
.ace-l42-theme .ace_invalid {\
color: yellow;\
background-color: red\
}\
.ace-l42-theme .ace_invalid.ace_deprecated {\
color: #CED2CF;\
background-color: #B798BF\
}\
.ace-l42-theme .ace_fold {\
background-color: #7AA6DA;\
border-color: #DEDEDE\
}\
.ace-l42-theme .ace_entity.ace_name.ace_function,\
.ace-l42-theme .ace_support.ace_function,\
.ace-l42-theme .ace_variable {\
color: #7AA6DA\
}\
.ace-l42-theme .ace_support.ace_class,\
.ace-l42-theme .ace_support.ace_type {\
color: #E7C547\
}\
.ace-l42-theme .ace_heading,\
.ace-l42-theme .ace_string {\
color: darkred\
}\
.ace-l42-theme .ace_multistring {\
color: darkred\
}\
.ace-l42-theme .ace_upperIdentifiers {\
font-weight: bold\
}\
.ace-l42-theme .ace_lowerIdentifiers {\
color: pink;\
font-style: italic;\
}\
.ace-l42-theme .ace_methodParameters {\
color: darkgreen;\
font-style: italic;\
}\
.ace-l42-theme .ace_objectCall {\
color: darkgreen;\
font-style: italic;\
}\
.ace-l42-theme .ace_entity.ace_name.ace_tag,\
.ace-l42-theme .ace_entity.ace_other.ace_attribute-name,\
.ace-l42-theme .ace_meta.ace_tag,\
.ace-l42-theme .ace_string.ace_regexp,\
.ace-l42-theme .ace_variable {\
color: #D54E53\
}\
.ace-l42-theme .ace_comment {\
color: green;\
font-family: \"New Times Roman\",\
font-size: \"10pt\"\
}\
.ace-l42-theme .ace_indent-guide {\
background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWNgYGBgYLBWV/8PAAK4AYnhiq+xAAAAAElFTkSuQmCC) right repeat-y;\
}\
";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

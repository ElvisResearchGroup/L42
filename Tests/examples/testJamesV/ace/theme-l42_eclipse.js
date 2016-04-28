define("ace/theme/l42_eclipse",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = false;
exports.cssClass = "ace-l42-eclipse";
exports.cssText = ".ace-l42-eclipse .ace_gutter {\
background: #ebebeb;\
border-right: 1px solid rgb(159, 159, 159);\
color: rgb(136, 136, 136);\
}\
.ace-l42-eclipse .ace_print-margin {\
width: 1px;\
background: #ebebeb;\
}\
.ace-l42-eclipse {\
background-color: #FFFFFF;\
color: black;\
}\
.ace-l42-eclipse .ace_fold {\
background-color: rgb(60, 76, 114);\
}\
.ace-l42-eclipse .ace_cursor {\
color: black;\
}\
.ace-l42-eclipse .ace_storage,\
.ace-l42-eclipse .ace_keyword,\
.ace-l42-eclipse .ace_variable {\
color: rgb(127, 0, 85);\
}\
.ace-l42-eclipse .ace_constant.ace_buildin {\
color: rgb(88, 72, 246);\
}\
.ace-l42-eclipse .ace_constant.ace_library {\
color: rgb(6, 150, 14);\
}\
.ace-l42-eclipse .ace_function {\
color: rgb(60, 76, 114);\
}\
.ace-l42-eclipse .ace_errorHighlight {\
color: yellow;\
background-color: red;\
font-weight: bold\
}\
.ace-l42-eclipse .ace_string {\
color: rgb(42, 0, 255);\
}\
.ace-l42-eclipse .ace_comment {\
color: rgb(113, 150, 130);\
}\
.ace-l42-eclipse .ace_comment.ace_doc {\
color: rgb(63, 95, 191);\
}\
.ace-l42-eclipse .ace_comment.ace_doc.ace_tag {\
color: rgb(127, 159, 191);\
}\
.ace-l42-eclipse .ace_constant.ace_numeric {\
color: darkblue;\
}\
.ace-l42-eclipse .ace_tag {\
color: rgb(25, 118, 116);\
}\
.ace-l42-eclipse .ace_type {\
color: rgb(127, 0, 127);\
}\
.ace-l42-eclipse .ace_xml-pe {\
color: rgb(104, 104, 91);\
}\
.ace-l42-eclipse .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-l42-eclipse .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
}\
.ace-l42-eclipse .ace_meta.ace_tag {\
color:rgb(25, 118, 116);\
}\
.ace-l42-eclipse .ace_invisible {\
color: #ddd;\
}\
.ace-l42-eclipse .ace_entity.ace_other.ace_attribute-name {\
color:rgb(127, 0, 127);\
}\
.ace-l42-eclipse .ace_active-line {\
background: rgb(232, 242, 254);\
}\
.ace-l42-eclipse .ace_gutter-active-line {\
background-color : #DADADA;\
}\
.ace-l42-eclipse .ace_marker-layer .ace_selected-word {\
border: 1px solid rgb(181, 213, 255);\
}\
.ace-l42-eclipse .ace_indent-guide {\
background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==\") right repeat-y;\
}\
.ace-l42-eclipse .ace_multistring {\
color: darkred\
}\
.ace-l42-eclipse .ace_upperIdentifiers {\
font-weight: bold\
}\
.ace-l42-eclipse .ace_lowerIdentifiers {\
color: pink;\
font-style: italic;\
}\
.ace-l42-eclipse .ace_methodParameters {\
color: blue;\
font-style: italic;\
}\
.ace-l42-eclipse .ace_objectCall {\
font-style: italic;\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

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
.ace-l42-eclipse .ace_fold {\
background-color: rgb(60, 76, 114);\
}\
.ace-l42-eclipse .ace_cursor {\
color: black;\
}\
.ace-l42-eclipse .ace_keyword{\
color: rgb(127, 0, 85);\
}\
.ace-l42-eclipse .ace_errorHighlight {\
color: yellow;\
background-color: red;\
font-weight: bold\
}\
.ace-l42-eclipse .ace_reuselibrary {\
font-weight: bold;\
font-style: italic;\
}\
.ace-l42-eclipse .ace_string {\
color: rgb(42, 0, 255);\
}\
.ace-l42-eclipse .ace_comment {\
color: rgb(113, 150, 130);\
}\
.ace-l42-eclipse .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-l42-eclipse .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
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
.ace-l42-eclipse .ace_upperIdentifiers {\
font-weight: bold\
}\
.ace-l42-eclipse .ace_methodParameters {\
color: blue;\
font-style: italic;\
}\
.ace-l42-eclipse .ace_objectCall {\
color: blue;\
font-style: italic;\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

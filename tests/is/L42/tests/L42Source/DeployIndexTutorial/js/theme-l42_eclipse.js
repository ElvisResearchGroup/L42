define("ace/theme/l42_eclipse",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = false;
exports.cssClass = "ace-l42-eclipse";
exports.cssText = ".ace-l42-eclipse {\
background: " + style.background + ";\
color: " + style.fontColor + ";\
font-family:'" + style.font + "'!important;\
}\
.ace-l42-eclipse .ace_gutter {\
background: " + style.sidebarBackgroundColor + ";\
border-right: 1px solid rgb(159, 159, 159);\
color: " + style.sidebarTextColor + ";\
}\
.ace-l42-eclipse .ace_fold {\
background-color: " + style.foldBackground + ";\
}\
.ace-l42-eclipse .ace_cursor {\
color: " + style.cursorColor + ";\
}\
.ace-l42-eclipse .ace_keyword{\
color: " + style.keywordColor + ";\
}\
.ace-l42-eclipse .ace_errorHighlight {\
color: " + style.errorTextColor + ";\
background-color: " + style.errorBackgroundColor + ";\
}\
.ace-l42-eclipse .ace_reuselibrary {\
font-weight: bold;\
font-style: italic;\
}\
.ace-l42-eclipse .ace_string {\
color: " + style.stringColor + ";\
}\
.ace-l42-eclipse .ace_comment {\
color: " + style.commentColor + ";\
}\
.ace-l42-eclipse .ace_marker-layer .ace_selection {\
background: " + style.selectedBackground + ";\
}\
.ace-l42-eclipse .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid " + style.bracketBorder + ";\
}\
.ace-l42-eclipse .ace_active-line {\
background: " + style.activeLineBackground + ";\
}\
.ace-l42-eclipse .ace_gutter-active-line {\
background-color : " + style.activeLineSideBarBackground + ";\
}\
.ace-l42-eclipse .ace_marker-layer .ace_selected-word {\
border: 1px solid " + style.selectedBorder + ";\
}\
.ace-l42-eclipse .ace_upperIdentifiers {\
font-weight: bold\
}\
.ace-l42-eclipse .ace_methodParameters {\
color: " + style.parameterColor + ";\
font-style: italic;\
}\
.ace-l42-eclipse .ace_objectCall {\
color: " + style.objectCallColor + ";\
font-style: italic;\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

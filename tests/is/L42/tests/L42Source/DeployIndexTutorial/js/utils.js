allDivs=[
  "a",
  "b",
  "c"
  ];

var doOnLoad=function (){
  hide(allDivs);
  // Get every l42 Div
  setAllAs("l42IDE",{
    fontSize: style.fontSize + "px",
    fontFamily: style.font,
    mode:"ace/mode/l42",
    theme:"ace/theme/l42_eclipse",
    tabSize: 100,
    useSoftTabs: true,
    behavioursEnabled: false,
    wrapBehavioursEnabled: false
    });
  setAllAs("l42",{
    fontSize:"95%",
    mode:"ace/mode/l42",
    theme:"ace/theme/l42_eclipse"
    });
  var config = {
    fontSize:"115%",
    maxLines:3000,
    mode:"ace/mode/l42",
    theme:"ace/theme/l42_eclipse"
    };
  setAllAs("l42Big",config);
  setAllAs("l42BigFolded",config);
  setAllAs("java",{
    fontSize:"95%",
    mode:"ace/mode/java",
    theme:"ace/theme/github"
    });
  setAllAs("html",{
    fontSize:"115%",
    mode:"ace/mode/html",
    theme:"ace/theme/github"
    });
  setOurMinMax();
  window.onresize=function(){setTimeout(setOurMinMax, 100);};
  }
var setOurMinMax=function(){
  var body=document.body;
  var html=document.documentElement;
  var height = Math.max(body.scrollHeight, body.offsetHeight,
    html.clientHeight, html.scrollHeight, html.offsetHeight );
  var lineNum = height/style.fontSize;
  setAllAs("l42IDE",{
    maxLines: 3000,//if its less than num of lines in file it creates a second scroll bar
    minLines: lineNum,
    autoScrollEditorIntoView: true
    });
  }
var toFoldAll=false;
var foldAll=function(){ toFoldAll=true; }
var setAllAs=function(className,options){
  var list = document.getElementsByClassName(className);
  for (var i = list.length - 1; i >= 0; i--) {
    // Turn it into an ace window and apply features
    var l42Box = ace.edit(list[i]);
    l42Box.setOptions(options);
    //l42Box.setBehavioursEnabled(false);
    //l42Box.commands.removeCommand('find');
    l42Box.setReadOnly(false);  // false to make it editable
    l42Box.setScrollSpeed(0.025);
    var text = ""+l42Box.getValue();
    if (className!="l42IDE"){ text=text.trim(); }
    l42Box.setValue(text,-1);
    l42Box.getCopyText=function(){
      var text = this.getSelectedText();
      //htmlFx.printOut("copy called on "+text);
      setTimeout(function(){if(htmlFx){htmlFx.copy(text);}}, 100);
      //it was overriding the clipboard later :-(
      this._signal("copy", text);
      return text;
      };
    l42Box.resize(true);
    if(toFoldAll || className=="l42BigFolded"){ timeoutFoldAll(l42Box); }
    l42Box.focus();
    }
  }
var timeoutFoldAll=function(box){
  setTimeout(function(){
    box.getSession().foldAll();
    box.getSession().unfold(1,false);
    }, 500)
  }
var hide=function(elements) {
  elements = elements.length ? elements : [elements];
  for (var index = 0; index < elements.length; index++) {
    var e=document.getElementById(elements[index]);
    if(e!=null){e.style.display = 'none';}
  }
}
var show=function (element) { document.getElementById(element).style.display = 'block'; }

var showAll=function(elements){
  elements = elements.length ? elements : [elements];
  for (var index = 0; index < elements.length; index++) {
    show(elements[index]);
  }
}
var selectDiv=function(id){
  hide(allDivs)
  show(id)
  }

var defaultStyle = {
  background:"rgb(245,245,245)", font:"monospace", fontColor:"rgb(0,0,0)",
  fontSize:20, sidebarTextColor:"rgb(136, 136, 136)",
  sidebarBackgroundColor:"rgb(235,235,235)", activeLineBackground:"rgb(232, 242, 254)",
  activeLineSideBarBackground:"rgb(218,218,218)", errorTextColor:"rgb(255,255,0)",
  errorBackgroundColor:"rgb(255,0,0)", stringColor:"rgb(42, 0, 255)",
  commentColor:"rgb(113, 150, 130)", parameterColor:"rgb(0,0,255)",
  keywordColor:"rgb(127, 0, 85)", objectCallColor:"rgb(0,0,255)",
  selectedBackground:"rgb(181, 213, 255)", selectedBorder:"rgb(181, 213, 255)",
  bracketBorder:"rgb(192, 192, 192)", foldBackground:"rgb(60, 76, 114)",
  cursorColor:"rgb(0,0,0)"
  };
var style = (typeof styling!== 'undefined') ? styling : defaultStyle;
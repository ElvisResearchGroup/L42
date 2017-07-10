allDivs=[
  "a",
  "b",
  "c"
  ];
  
function doOnLoad(){
  hide(allDivs);
  // Get every l42 Div
  setAllAs("l42",{
      fontSize:"95%",
      maxLines:3000,
      mode:"ace/mode/l42",
      theme:"ace/theme/l42_eclipse"
      });
    setAllAs("l42Big",{
      fontSize:"115%",
      maxLines:3000,
      mode:"ace/mode/l42",
      theme:"ace/theme/l42_eclipse"
      });
  setAllAs("java",{
      fontSize:"95%",
      maxLines:3000,
      mode:"ace/mode/java",
      theme:"ace/theme/github"
      });
  setAllAs("html",{
      fontSize:"115%",
      maxLines:3000,
      mode:"ace/mode/html",
      theme:"ace/theme/github"
      });
  }
function setAllAs(className,options){
  var list = document.getElementsByClassName(className);
  for (var i = list.length - 1; i >= 0; i--) {
    // Turn it into an ace window and apply features
    var l42Box = ace.edit(list[i]);
    l42Box.setOptions(options);
    l42Box.setBehavioursEnabled(false);
    l42Box.setReadOnly(false);  // false to make it editable
    // If our code as unrequired spaces at the front or back of the code. Remove them.
    var text = ""+l42Box.getValue().trim();
    l42Box.setValue(text,-1);
    }
  }

function hide(elements) {
  elements = elements.length ? elements : [elements];
  for (var index = 0; index < elements.length; index++) {
    var e=document.getElementById(elements[index]);
    if(e!=null){e.style.display = 'none';}
  }
}
function show (element) { document.getElementById(element).style.display = 'block'; }

function showAll(elements){
  elements = elements.length ? elements : [elements];
  for (var index = 0; index < elements.length; index++) {
    show(elements[index]);
  }
}

function selectDiv(id){
  hide(allDivs)
  show(id)
  }
 

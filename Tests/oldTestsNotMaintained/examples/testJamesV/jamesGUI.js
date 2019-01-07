loadScript("ace/ace.js", 
function(){
  document.getElementById("alpha").innerHTML = "Running callback " + script;
  var editor = ace.edit("editor");
  editor.setTheme("ace/theme/l42");
  editor.getSession().setMode("ace/mode/l42");
  document.getElementById("alpha").innerHTML = "SUCCESS " + script;
})


function loadScript(url, callback)
{
  // Adding the script tag to the head as suggested before
  var head = document.getElementsByTagName('head')[0];
  var script = document.createElement('script');
  script.type = 'text/javascript';
  script.src = url;

  // Then bind the event to the callback function.
  // There are several events for cross browser compatibility.
  script.onreadystatechange = callback;
  script.onload = callback;

  // Fire the loading
  var result = head.appendChild(script);
  document.getElementById("alpha").innerHTML = "Loaded Script " + head + " " + result;
}
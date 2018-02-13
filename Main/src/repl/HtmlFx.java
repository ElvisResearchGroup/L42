package repl;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

import ast.Ast;
import ast.ErrorMessage;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import facade.Parser;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import programReduction.Program;

public class HtmlFx extends StackPane{
  public WebEngine webEngine;
  Region outerPanel;

  public HtmlFx(Region outer) {
    super();
    this.outerPanel=outer;
  }

  public final Events events=new Events();

  private Void initWeb(CountDownLatch latch,URL url){
    WebView browser = new WebView();
    this.webEngine = browser.getEngine();
    this.webEngine.getLoadWorker().stateProperty().addListener(
      (ov, oldState,newState)->{
        if (newState == Worker.State.SUCCEEDED) {latch.countDown();}
        });
    this.webEngine.load(url.toExternalForm());
    this.webEngine.setOnAlert(event->{
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Information Dialog");
      alert.setHeaderText(null);
      alert.setContentText(event.getData());
      alert.showAndWait();
      //alert.setOnCloseRequest(e->{  alert.close(); });
      });
    //---cut and paste
    browser.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
      if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V){
        // PASTE
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        webEngine.executeScript(" pasteContent(\""+
                org.apache.commons.text.StringEscapeUtils
                .escapeEcmaScript(content)+"\") ");
      }
      if(keyEvent.getCode() == KeyCode.PERIOD) {
        if(outerPanel!=null && outerPanel instanceof ReplTextArea) {
          ReplTextArea editor=((ReplTextArea)outerPanel);
          Object o=webEngine.executeScript("ace.edit(\"textArea\").getCursorPosition()");
          assert o instanceof JSObject : o.toString();
          JSObject jsobj=(JSObject)o;
          int row=Integer.parseInt(jsobj.getMember("row").toString());
          int col=Integer.parseInt(jsobj.getMember("column").toString());
          try { displayDoc(editor,row,col); }
          catch(IllegalArgumentException e) {}
        }
      }

      //---CTRL+S save
      if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.S){
        if(outerPanel!=null && outerPanel instanceof ReplTextArea) {
          ReplTextArea editor=((ReplTextArea)outerPanel);
          editor.saveToFile();
          editor.removeStar();
        }
      } else { //file has been modified (NOT SAVED)
        if(outerPanel!=null && outerPanel instanceof ReplTextArea) {
          ReplTextArea editor=((ReplTextArea)outerPanel);
          editor.addStar();
        }
      }

    });
    // retrieve copy event via javascript:alert
    webEngine.setOnAlert((WebEvent<String> we) -> {
      if(we.getData()!=null && we.getData().startsWith("copy: ")){
         // COPY
         final Clipboard clipboard = Clipboard.getSystemClipboard();
         final ClipboardContent content = new ClipboardContent();
         content.putString(we.getData().substring(6));
         clipboard.setContent(content);
      }
    });
    //----
    this.getChildren().clear();
    this.getChildren().add(browser);
    latch.countDown();
    return null;
    }

  private void displayDoc(ReplTextArea editor, int row, int col) {
    editor.setDoc("Row: "+row+" Col: "+col+"\n");
    FromDotToPath r=new FromDotToPath(editor.getText(),row,col);
    Program p=ReplGui.main.repl.p;
    p=p.navigate(r.cs);
    //try {p=p.pop();}catch(Throwable  t) {}
    ClassB top=p.top();
    editor.appendDoc(top.getDoc1().toString());
//    editor.appendDoc("\nMembers:\n");
//    for(Member m : top.getMs()) {
//      editor.appendDoc(m.toString());
//    }
  }

  public static Error propagateException(Throwable t){
    if (t instanceof RuntimeException){throw (RuntimeException)t;}
    if (t instanceof Error){throw (Error)t;}
    if (t instanceof InterruptedException){Thread.currentThread().interrupt();}
    throw new Error(t);
    }

  public void createHtmlContent(CountDownLatch latch,URL url) {
    assert Platform.isFxApplicationThread();
    initWeb(latch,url);
    //
    Object o=this.webEngine.executeScript(
        "window.event42=function(s){ "
        + "if(event42.eventCollector){"
        + "event42.eventCollector.add(s);"
        + "return 'Event '+s+' added '+event42.eventCollector.toString();} "
        + "return 'Event '+s+' not added';}");
    assert o instanceof JSObject : o.toString();
    JSObject jsobj = (JSObject)o;
    jsobj.setMember("eventCollector",this.events);
    latch.countDown();
  }
}
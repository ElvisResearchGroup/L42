package repl;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class HtmlFx extends Pane{
  WebEngine webEngine;
  Region outerPanel;

  public HtmlFx(Region outer) {
    super();
    this.outerPanel=outer;
  }

  public final Events events=new Events();

  private Void initWeb(CountDownLatch latch,String html){
    WebView browser = new WebView();
    this.webEngine = browser.getEngine();
    this.webEngine.getLoadWorker().stateProperty().addListener(
      (ov, oldState,newState)->{
        if (newState == Worker.State.SUCCEEDED) {latch.countDown();}
        });
    this.webEngine.loadContent(html);
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
  });
  //TODO keep track when is text modified
  //browser.setOnKeyPressed(new EventHandler<Event>() {
  //  public void handle(Event arg0) {
  //  if(outerPanel!=null) outerPanel.changed.set(true);
  //}
  //});
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
    return null;
    }
  public static Error propagateException(Throwable t){
    if (t instanceof RuntimeException){throw (RuntimeException)t;}
    if (t instanceof Error){throw (Error)t;}
    if (t instanceof InterruptedException){Thread.currentThread().interrupt();}
    throw new Error(t);
    }

  public void createHtmlContent(CountDownLatch latch,String html) {
    assert Platform.isFxApplicationThread();
    initWeb(latch,html);
    //
    Object o=this.webEngine.executeScript(
"window.event42=function(s){ if(event42.eventCollector){event42.eventCollector.add(s);return 'Event '+s+' added '+event42.eventCollector.toString();} return 'Event '+s+' not added';}");
    assert o instanceof JSObject : o.toString();
    JSObject jsobj = (JSObject)o;
    jsobj.setMember("eventCollector",this.events);
  }
}
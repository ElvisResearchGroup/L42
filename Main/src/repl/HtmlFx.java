package repl;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class HtmlFx extends Pane{
  public WebEngine webEngine;
  public ReplTextArea outerPanel;

  public HtmlFx(ReplTextArea outer) {
    super();
    this.outerPanel=outer;
  this.createHtmlContent("<body></body");
//  this.createHtmlContent(readURL(url));

//    this.createHtmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//        " <!DOCTYPE html\n" +
//        "     PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
//        "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
//        " <html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
//        "\n" +
//        "  <head>\n" +
//        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />\n" +
//        "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" media=\"all\"/>\n" +
//        "    <script src=\"js/ace.js\"></script>\n" +
//        "    <script src=\"js/utils.js\"></script>\n" +
//        "  </head>\n" +
//        "  <body onload='doOnLoad();'>\n" +
//        "<pre id=\"textArea\" class=\"l42Big\"><![CDATA[\n" +
//        "reuse L42.is/AdamTowel02\n" +
//        "\n" +
//        "CacheAdamTowel02:Load.cacheTowel()\n" +
//        "\n" +
//        "Main: {\n" +
//        "  Debug(S\"Hello world\")\n" +
//        "  return ExitCode.normal()\n" +
//        "  }\n" +
//        "\n" +
//        "]]></pre>\n" +
//        "  </body>\n" +
//        "</html>");

//    this.webEngine.getLoadWorker().stateProperty().addListener(
//        (bservable,oldValue,newValue) -> {
//          if( newValue != Worker.State.SUCCEEDED ) {
//            return;
//          }
//
//          URL url=getClass().getResource("textArea.xhtml");
//        this.webEngine.load(url.toExternalForm());
//        } );

  CountDownLatch latch=new CountDownLatch(1);
  this.webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> { //to make synchronous
      if (newState != Worker.State.SUCCEEDED) {
        latch.countDown();
      }
  });
  URL url=getClass().getResource("textArea.xhtml");
  this.webEngine.load(url.toExternalForm());

//  this.webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> { //to make synchronous
//      if (newState == Worker.State.SUCCEEDED) {
//        String res = (String) this.webEngine.executeScript(
//            "ace.edit(\"textArea\").getValue()"
//            );
//        System.out.println("RES:: "+res);
//        return; //new CountDownLatch(1).countDown();
//      }
//  });
//  URL url=getClass().getResource("textArea.xhtml");
//  this.webEngine.load(url.toExternalForm());
  }

  public final Events events=new Events();

  private Void initWeb(CountDownLatch latch,String html){
    WebView browser = new WebView();
    this.webEngine = browser.getEngine();
    this.webEngine.getLoadWorker().stateProperty().addListener(
      (ov, oldState,newState)->{
        if (newState != Worker.State.SUCCEEDED) {latch.countDown();}
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
  browser.setOnKeyPressed(new EventHandler<Event>() {
    public void handle(Event arg0) {
	  if(outerPanel!=null) outerPanel.changed.set(true);
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
    return null;
    }
  public static Error propagateException(Throwable t){
    if (t instanceof RuntimeException){throw (RuntimeException)t;}
    if (t instanceof Error){throw (Error)t;}
    if (t instanceof InterruptedException){Thread.currentThread().interrupt();}
    throw new Error(t);
    }

  private String readURL(URL resource) {
    try {
//      URLConnection connection = resource.openConnection();
//      String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
//      return text;
      BufferedReader in = new BufferedReader(
          new InputStreamReader(resource.openStream()));
      StringBuilder response = new StringBuilder();
      String inputLine;
      while ((inputLine = in.readLine()) != null) {response.append(inputLine);}
      in.close();
      return response.toString();
    }catch (IOException e) {throw new Error(e);}
  }

  public void createHtmlContent(String html) {
    CountDownLatch latch = new CountDownLatch(1);
    System.out.println("createhtmlcontent1 FX: "+Platform.isFxApplicationThread());
    initWeb(latch,html);

    System.out.println("createhtmlcontent2 FX: "+Platform.isFxApplicationThread());
    Object o=this.webEngine.executeScript(
  		"window.event42=function(s){ if(event42.eventCollector){event42.eventCollector.add(s);return 'Event '+s+' added '+event42.eventCollector.toString();} return 'Event '+s+' not added';}");
    assert o instanceof JSObject : o.toString();
    JSObject jsobj = (JSObject)o;
    jsobj.setMember("eventCollector",this.events);
    }

}
package is.L42.connected.withHtml;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class HtmlFx{
  WebEngine webEngine;
  JFXPanel jfxPanel;
  public HtmlFx(JFXPanel jfxPanel) {
    super();
    this.jfxPanel = jfxPanel;
  }

  final Events events=new Events();

  private Void initWeb(CountDownLatch latch,String html){
    Group root = new Group();
    Scene scene = new Scene(root);
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
    root.getChildren().add(browser);
    jfxPanel.setScene(scene);
    return null;
    }

  protected void createHtmlContent(String html) {
    CountDownLatch latch = new CountDownLatch(1);
    FutureTask<Void> future=new FutureTask<Void>(()->initWeb(latch,html));
    Platform.runLater(future);
    try {future.get();}
    catch (ExecutionException e) {throw Frame.propagateException(e.getCause());}
    catch (InterruptedException e) {throw Frame.propagateException(e);}
    try {latch.await();}
    catch (InterruptedException e) {throw Frame.propagateException(e);}
    future=new FutureTask<Void>(()->{
      Object o=this.webEngine.executeScript(
"window.event42=function(s){ if(event42.eventCollector){event42.eventCollector.add(s);return 'Event '+s+' added '+event42.eventCollector.toString();} return 'Event '+s+' not added';}");
      assert o instanceof JSObject : o.toString();
      JSObject jsobj = (JSObject)o;
      jsobj.setMember("eventCollector",this.events);
      return null;
      });
    Platform.runLater(future);
    try {future.get();}
    catch (ExecutionException e) {throw Frame.propagateException(e.getCause());}
    catch (InterruptedException e) {throw Frame.propagateException(e);}
    }

}
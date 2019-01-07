package is.L42.connected.withHtml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import facade.L42;
import netscape.javascript.JSObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FrameFX {
  public static void open(String title,String content){
    try{
      openThrow(title,content);
      loadEventHandler();
      }
    catch(IOException| InterruptedException e ){throw new Error(e);}
  }
  private static void openThrow(String _title,String _content) throws IOException, InterruptedException {
    title=_title;
    content =head+"<base href='"+L42.root.toString()+"' />"+_content+"</html>";
    System.out.println(content);
    synchronized(dumbLock){
      laucherThread.start();
      waitForCompletion();//of html loading
      }
    }
  private static void loadEventHandler() {
    synchronized(dumbLock){
      Platform.runLater(() ->{
        Object o=NestedPrivate.webEngine.executeScript("window.event42");
        assert o instanceof JSObject : o.toString();
        JSObject jsobj = (JSObject)o;
        FrameFX.eventCollector=new EventCollected();
        jsobj.setMember("eventCollector",eventCollector );

        NestedPrivate.webEngine.setOnAlert(event->{
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Information Dialog");
          alert.setHeaderText(null);
          alert.setContentText(event.getData());
          alert.showAndWait();
          });
        //<a href="" onclick="java.add('bla');">here</a>
        synchronized(dumbLock){dumbLock.notifyAll();}
      });
      waitForCompletion();
      }
    }
  public static void setId(String id,String body) {
    synchronized(dumbLock){
      Platform.runLater(() ->setIdFx(id,body));
      waitForCompletion();
      }
  }
  private static void setIdFx(String id, String body) {
    body=body.replace("\"","\\\"");
    body=body.replace("\n","\\n");
    String submitted="$(\"#"+id+"\").replaceWith( \""+body+"\" );";
    //System.out.println(submitted);
    NestedPrivate.webEngine.executeScript(submitted);
    //executeScript *is syncronus*
    synchronized(dumbLock){dumbLock.notifyAll();}
  }
  @SuppressWarnings("serial")
  public static class ApplicationDead extends Exception{}
  public static String getEventString() throws ApplicationDead{
    synchronized(eventCollector){
      while(eventCollector.events.isEmpty()){
        if(!laucherThread.isAlive()){throw new ApplicationDead();}
        try{eventCollector.wait(500);}
        catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new Error(e);
          }
        }
      return eventCollector.events.remove(0);
    }

  }


  public static final Object dumbLock=new Object();
  public static volatile String title;
  public static volatile String content;
  public static void waitForCompletion() {
      try{dumbLock.wait();}
      catch(InterruptedException e){
        Thread.currentThread().interrupt();
        throw new Error(e);
        }
    }
  public static class NestedPrivate extends Application {
    public NestedPrivate(){}
    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle(title);
        primaryStage.setOnCloseRequest((boh)->Platform.exit());
        load(content);
        StackPane root = new StackPane();
        root.getChildren().add(wv);
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }

    private WebView load(String content){
      webEngine.loadContent(content);
      webEngine.getLoadWorker().stateProperty().addListener((ChangeListener<State>)
        (ObservableValue<? extends State> ov, State oldState, State newState)->{
          if (newState == Worker.State.SUCCEEDED) {
            synchronized(dumbLock){dumbLock.notifyAll();}
            }
        });
      return wv;
    }
    public static final WebView wv =new WebView();
    public static final WebEngine webEngine = wv.getEngine();
  }
  private static final String head;
  private static final Thread laucherThread=new Thread(){
    public void run(){
      Application.launch(NestedPrivate.class,new String[0]);
      synchronized(eventCollector){eventCollector.notifyAll();}
      }
    };
  private static EventCollected eventCollector;
  static {try (
      BufferedReader reader = new BufferedReader(new InputStreamReader(FrameFX.class.getResourceAsStream("header.html")))
      ){
      StringBuilder out = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        out.append(line);
        out.append("\n");
        }
      head = out.toString();
      }
    catch (IOException  e) {throw new Error(e);}
    }
  public static class EventCollected {
    private final List<String>events=Collections.synchronizedList(new ArrayList<>());
    public void add(String event) {//this happens in the FX thread
      synchronized(this){
        events.add(event);
        this.notifyAll();
        }
      //System.out.println(events);
      //System.out.println("*---------------");
      }
    }
  public static String executeJs(String s1) {
    synchronized(dumbLock){
      String[] res=new String[1];//stupid final limitations
      Platform.runLater(() ->{res[0]=executeJsFx(s1);});
      waitForCompletion();//TODO:Do not work!
      return res[0];
      }
  }
  private static String executeJsFx(String body) {
    body=body.replace("\"","\\\"");
    body=body.replace("\n","\\n");
    //String submitted="$(\"#"+id+"\").replaceWith( \""+body+"\" );";
    //System.out.println(submitted);
    String res=executeAndConvert(body);
    //executeScript *is syncronus*
    synchronized(dumbLock){dumbLock.notifyAll();}
    return res;
  }
  public static String executeAndConvert(String body) {
    try{
      Object res=NestedPrivate.webEngine.executeScript(body);
      if(res ==null){return "";}
      if(res instanceof String){return (String)res;}
      if(res instanceof Integer){return  res.toString();}
      if(res instanceof Double){return  res.toString();}
      if(res instanceof Boolean){return  res.toString();}
      //if(res instanceof netscape.javascript.JSObject){return  "";}
      return "";
      }
    catch(netscape.javascript.JSException jsExc){return ""+jsExc;}
  }
  }


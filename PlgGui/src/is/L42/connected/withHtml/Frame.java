package is.L42.connected.withHtml;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import facade.L42;
import is.L42.connected.withHtml.FrameFX.NestedPrivate;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

import java.awt.event.WindowAdapter;
import platformSpecific.javaTranslation.Resources;
import repl.HtmlFx;
import repl.ReplGui;
//@SuppressWarnings("serial")
public class Frame extends Stage{
  private static final HashMap<String,Frame> windows=new HashMap<>();
  static {//run when the class is loaded
    L42.registerCleanUp(()->{
      for(String name:windows.keySet()) {close(name);}
    });
  }
  HtmlFx htmlFx=new HtmlFx(new Pane());
  public static void load(String wName,String html,int x,int y){
    Frame f=windows.get(wName);
    if (f!=null){f.close();}
    f=Frame.createNew(wName,html,x,y);
    windows.put(wName,f);
    }
  private static Frame createNew(String wName,String html,int x, int y) {
    new JFXPanel();//this is added so that an exception "toolkit not initialised" doesnt occur
    return ReplGui.runAndWait(3,latch->{
      final Frame frame = new Frame(Frame.extractTitle(html));
      frame.htmlFx.createHtmlContent(latch,wv->wv.loadContent(html));
      frame.setMinWidth(x);
      frame.setMinHeight(y);
      frame.setOnCloseRequest(event -> {
        System.out.println("Stage "+wName+" is closing");
        Frame.close(wName);
        });
      new NestedPrivate().start(frame);
      return frame;
      });
    }

  public static class NestedPrivate extends Application {
    @Override public void start(Stage primaryStage) {
        assert (primaryStage instanceof Frame) : "Stage must be an instance of Frame";
        Pane webview = ((Frame)primaryStage).htmlFx;
        primaryStage.setScene(new Scene(webview, primaryStage.getMinWidth(), primaryStage.getMinHeight()));
        primaryStage.show();
    }
  }

  private static String extractTitle(String html) {
    String htmlUP=html.toUpperCase();
    int start=htmlUP.indexOf("<TITLE>");//TODO: better parsing?
    int end=htmlUP.indexOf("</TITLE>");
    return html.substring(start+("<TITLE>".length()),end);
  }

  public static void close(String wName){
    Frame f=windows.get(wName);
    if(f!=null){
      System.out.println(wName+" is disposed");
      Platform.runLater(() -> f.close()); //need to do this because otherwise we get "Not on FX application thread" exception
      windows.remove(wName);
      f.htmlFx.events.isDisposed=true;
      }
    }

  public static String executeJs(String wName, String command){
    Frame f=windows.get(wName);
    if(f!=null){return f.executeJs(command);}
    throw new Resources.Error("wName not active:"+wName);
    }

  private Frame(String title){this.setTitle(title);}

  private String executeJs(String command) {
    FutureTask<String> future = new FutureTask<>(()->executeJsFX(command));
    Platform.runLater(future);
    try {return future.get();}
    catch (ExecutionException e) {throw HtmlFx.propagateException(e.getCause());}
    catch (InterruptedException e) {throw HtmlFx.propagateException(e);}
    }
  private String executeJsFX(String command) {
    try{
      Object res=htmlFx.webEngine.executeScript(command);
      if(res ==null){return "";}//TODO: get a 42 error
      if(res instanceof String){return (String)res;}
      if(res instanceof Integer){return  res.toString();}
      if(res instanceof Double){return  res.toString();}
      if(res instanceof Boolean){return  res.toString();}
      //if(res instanceof netscape.javascript.JSObject){return  "";}
      return "";
      }
    catch(netscape.javascript.JSException jsExc){
      throw new Resources.Error("JavascriptError:"+jsExc);
      }
  }
  public static String getEventString(String wName){
    Frame f=windows.get(wName);
    if (f!=null){return f.getEventString();}
    throw new Resources.Error("wName not active:"+wName);
  }
  public String getEventString(){return htmlFx.events.get();}
}



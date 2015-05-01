package is.L42.connected.withHtml;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import netscape.javascript.JSObject;
import javafx.application.Platform;

import java.awt.event.WindowAdapter;
import platformSpecific.javaTranslation.Resources;
@SuppressWarnings("serial")
public class Frame extends JFrame{
  private static final HashMap<String,Frame> windows=new HashMap<>();
  
  public static void load(String wName,String html,int x,int y){
    Frame f=windows.get(wName);
    if (f!=null){f.dispose();}
    f=Frame.createNew(wName,html,x,y);windows.put(wName,f);
    }
  private static Frame createNew(String wName,String html,int x, int y) {
    FutureTask<Frame> future = new FutureTask<>(()-> {    
      final Frame frame = new Frame(Frame.extractTitle(html));
      JFXPanel jfxPanel = new JFXPanel();
      frame.createHtmlContent(jfxPanel,html);
      frame.getContentPane().add(jfxPanel);  
      frame.setMinimumSize(new Dimension(x, y));  
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.addWindowListener(
          new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          Frame.close(wName);
          }});
      frame.setVisible(true);
      return frame;
        });
    SwingUtilities.invokeLater(future);
    try {return future.get();}
    catch (ExecutionException e) {throw propagateException(e.getCause());}       
    catch (InterruptedException e) {throw propagateException(e);}
    }
  private static String extractTitle(String html) {
    String htmlUP=html.toUpperCase();
    int start=htmlUP.indexOf("<TITLE>");//TODO: better parsing?
    int end=htmlUP.indexOf("</TITLE>");
    return html.substring(start+("<TITLE>".length()),end);
  }
  
  private Void initWeb(CountDownLatch latch, JFXPanel jfxPanel,String html){
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
  private void createHtmlContent(JFXPanel jfxPanel,String html) {
    CountDownLatch latch = new CountDownLatch(1);
    FutureTask<Void> future=new FutureTask<Void>(()->initWeb(latch,jfxPanel,html));
    Platform.runLater(future);
    try {future.get();}   
    catch (ExecutionException e) {throw propagateException(e.getCause());}
    catch (InterruptedException e) {throw propagateException(e);}
    try {latch.await();}
    catch (InterruptedException e) {throw propagateException(e);}
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
    catch (ExecutionException e) {throw propagateException(e.getCause());}
    catch (InterruptedException e) {throw propagateException(e);}
    }
  
  public static void close(String wName){
    Frame f=windows.get(wName);
    if(f!=null){
      System.out.println(wName+" is disposed");
      f.dispose();
      windows.remove(wName);
      f.events.isDisposed=true;
      }
    }

  public static String executeJs(String wName, String command){
    Frame f=windows.get(wName);
    if(f!=null){return f.executeJs(command);}
    throw new Resources.Error("wName not active:"+wName);
    }
  
  private Frame(String title){super(title);}
  private WebEngine webEngine;
 
  private String executeJs(String command) {
    FutureTask<String> future = new FutureTask<>(()->executeJsFX(command));
    Platform.runLater(future);
    try {return future.get();}
    catch (ExecutionException e) {throw propagateException(e.getCause());}       
    catch (InterruptedException e) {throw propagateException(e);}
    }
  private String executeJsFX(String command) {
    try{
      Object res=webEngine.executeScript(command);
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
  private static Error propagateException(Throwable t){
    if (t instanceof RuntimeException){throw (RuntimeException)t;}
    if (t instanceof Error){throw (Error)t;}
    if (t instanceof InterruptedException){Thread.currentThread().interrupt();}
    throw new Error(t);
  }
  public static class Events{
    private boolean isDisposed=false;
    private final List<String>data=new ArrayList<>();
    public synchronized void add(String s){data.add(s);}
    public synchronized String toString(){return data.toString();}
    public synchronized String get(){
      while(!isDisposed && data.isEmpty()){
        try{this.wait(100);}
        catch (InterruptedException e) {propagateException(e);}      
        }
      if(isDisposed){throw new Resources.Error("Requested EventQueue is disposed");}
      return data.remove(0);
      }
    }
  private final Events events=new Events();
  public static String getEventString(String wName){
    Frame f=windows.get(wName);
    if (f!=null){return f.getEventString();}
    throw new Resources.Error("wName not active:"+wName);
  }
  public String getEventString(){return events.get();}
    

}
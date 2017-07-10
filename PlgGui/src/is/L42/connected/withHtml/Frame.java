package is.L42.connected.withHtml;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.embed.swing.JFXPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;

import java.awt.event.WindowAdapter;
import platformSpecific.javaTranslation.Resources;
@SuppressWarnings("serial")
public class Frame extends JFrame{
  private static final HashMap<String,Frame> windows=new HashMap<>();
  HtmlFx htmlFx=new HtmlFx(new JFXPanel());
  public static void load(String wName,String html,int x,int y){
    Frame f=windows.get(wName);
    if (f!=null){f.dispose();}
    f=Frame.createNew(wName,html,x,y);
    windows.put(wName,f);
    }
  private static Frame createNew(String wName,String html,int x, int y) {
    FutureTask<Frame> future = new FutureTask<>(()-> {
      final Frame frame = new Frame(Frame.extractTitle(html));
      frame.htmlFx.createHtmlContent(html);
      frame.getContentPane().add(frame.htmlFx.jfxPanel);
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

  public static void close(String wName){
    Frame f=windows.get(wName);
    if(f!=null){
      System.out.println(wName+" is disposed");
      f.dispose();
      windows.remove(wName);
      f.htmlFx.events.isDisposed=true;
      }
    }

  public static String executeJs(String wName, String command){
    Frame f=windows.get(wName);
    if(f!=null){return f.executeJs(command);}
    throw new Resources.Error("wName not active:"+wName);
    }

  private Frame(String title){super(title);}

  private String executeJs(String command) {
    FutureTask<String> future = new FutureTask<>(()->executeJsFX(command));
    Platform.runLater(future);
    try {return future.get();}
    catch (ExecutionException e) {throw propagateException(e.getCause());}
    catch (InterruptedException e) {throw propagateException(e);}
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
  protected static Error propagateException(Throwable t){
    if (t instanceof RuntimeException){throw (RuntimeException)t;}
    if (t instanceof Error){throw (Error)t;}
    if (t instanceof InterruptedException){Thread.currentThread().interrupt();}
    throw new Error(t);
  }
  public static String getEventString(String wName){
    Frame f=windows.get(wName);
    if (f!=null){return f.getEventString();}
    throw new Resources.Error("wName not active:"+wName);
  }
  public String getEventString(){return htmlFx.events.get();}
}



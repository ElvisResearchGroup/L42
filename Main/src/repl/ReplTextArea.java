package repl;

import java.awt.Dimension;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class ReplTextArea extends JFXPanel{
  private static final long serialVersionUID = 1L;
  HtmlFx htmlFx;
  public ReplTextArea(URL url) {
   htmlFx=new HtmlFx(this);
   htmlFx.createHtmlContent("<body></body>");
   this.setPreferredSize(new Dimension(800,900));
   Platform.runLater(()->htmlFx.webEngine.load(url.toExternalForm()));
   }
  public String getText(){
    FutureTask<String> query = new FutureTask<>(()->{
      String res = (String) htmlFx.webEngine.executeScript(
          "ace.edit(\"textArea\").getValue()"
          );
      return res;
      });
    Platform.runLater(query);
    try {return query.get();}
    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }
  public void setText(String input){
    FutureTask<?> query = new FutureTask<>(()->{
      htmlFx.webEngine.executeScript(
          "ace.edit(\"textArea\").setValue(\""+input.replace("\"", "\\\"").replace("\n", "\\n")+"\")"
          );
      return null;
      });
    Platform.runLater(query);
    try {query.get();}
    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }

}

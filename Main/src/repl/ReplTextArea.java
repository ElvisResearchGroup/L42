package repl;

import java.awt.Dimension;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class ReplTextArea extends SplitPane {
  private static final double DIVIDER_POSN = 0.7f;

  HtmlFx htmlFx;
  TextArea documentationArea;
  public ReplTextArea(URL url) {
   StackPane codingArea = new StackPane();
   documentationArea = new TextArea();
   documentationArea.setEditable(false);

   this.getItems().addAll(codingArea, documentationArea);
   this.setDividerPositions(DIVIDER_POSN);
   codingArea.minWidthProperty().bind(this.widthProperty().multiply(DIVIDER_POSN)); //to lock the divider

   htmlFx=new HtmlFx(codingArea);
   htmlFx.createHtmlContent("<body></body>");
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
//    try {query.get();}
//    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }

  public void setDocumentation(String input){
    documentationArea.setText(input);
    }

}

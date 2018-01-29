package repl;

import java.awt.Dimension;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class ReplTextArea extends SplitPane {
  private static final double DIVIDER_POSN = 0.75f;

  SimpleBooleanProperty changed= new SimpleBooleanProperty(false);

  String filename;
  HtmlFx htmlFx;
  TextArea documentationArea;

  public ReplTextArea(URL url) {
    htmlFx=new HtmlFx(this);
    System.out.println("repltextarea construct FX: "+Platform.isFxApplicationThread());
//    Platform.runLater(()->htmlFx.webEngine.load(url.toExternalForm()));
    htmlFx.webEngine.load(url.toExternalForm());
    htmlFx.webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
        if (newState != Worker.State.SUCCEEDED) {
        	return;
        }
    });

    documentationArea = new TextArea();
    documentationArea.setEditable(false);

    this.getItems().addAll(htmlFx, documentationArea);
    this.setDividerPositions(DIVIDER_POSN);
    }

  public String getText(){
	System.out.println("gettext FX: "+Platform.isFxApplicationThread());
	String res = (String) htmlFx.webEngine.executeScript(
	        "ace.edit(\"textArea\").getValue()"
	        );
	return res;
//    FutureTask<String> query = new FutureTask<>(()->{
//      String res = (String) htmlFx.webEngine.executeScript(
//          "ace.edit(\"textArea\").getValue()"
//          );
//      return res;
//      });
//    Platform.runLater(query);
//    try {return query.get();}
//    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }

  public void setText(String input){
	  System.out.println("settext FX: "+Platform.isFxApplicationThread());
	  System.out.println("gettext: "+getText());
//	  Platform.runLater(()->
	  htmlFx.webEngine.executeScript(
	        "ace.edit(\"textArea\").setValue(\""+input.replace("\"", "\\\"").replace("\n", "\\n")+"\")"
	        );//);
	  System.out.println("DONE");
//	  FutureTask<?> query = new FutureTask<>(()->{
//      htmlFx.webEngine.executeScript(
//          "ace.edit(\"textArea\").setValue(\""+input.replace("\"", "\\\"").replace("\n", "\\n")+"\")"
//          );
//      return null;
//      });
//    Platform.runLater(query);
//    try {query.get();}
//    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }

  public void setDocumentation(String input){
    documentationArea.setText(input);
    }

}
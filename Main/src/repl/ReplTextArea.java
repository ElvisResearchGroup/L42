package repl;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
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

    htmlFx.createHtmlContent("<body></body>");
    Platform.runLater(()->htmlFx.webEngine.load(url.toExternalForm()));

//    htmlFx.createHtmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//      " <!DOCTYPE html\n" +
//      "     PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
//      "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
//      " <html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
//      "\n" +
//      "  <head>\n" +
//      "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />\n" +
//      "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" media=\"all\"/>\n" +
//      "    <script src=\"js/ace.js\"></script>\n" +
//      "    <script src=\"js/utils.js\"></script>\n" +
//      "  </head>\n" +
//      "  <body onload='doOnLoad();'>\n" +
//      "<pre id=\"textArea\" class=\"l42Big\"><![CDATA[\n" +
//      "reuse L42.is/AdamTowel02\n" +
//      "\n" +
//      "CacheAdamTowel02:Load.cacheTowel()\n" +
//      "\n" +
//      "Main: {\n" +
//      "  Debug(S\"Hello world\")\n" +
//      "  return ExitCode.normal()\n" +
//      "  }\n" +
//      "\n" +
//      "]]></pre>\n" +
//      "  </body>\n" +
//      "</html>");

//    CountDownLatch latch=new CountDownLatch(1);
//    htmlFx.webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//        if (newState == Worker.State.SUCCEEDED) {
//          latch.countDown();
//        }
//    });
//    htmlFx.webEngine.load(url.toExternalForm());
//
//    try {latch.await();}
//    catch (InterruptedException e) {throw new Error(e);}

    documentationArea=new TextArea();
    documentationArea.setEditable(false);

    this.getItems().addAll(htmlFx, documentationArea);
    this.setDividerPositions(DIVIDER_POSN);
    }

  public String getText(){
//	System.out.println("gettext FX: "+Platform.isFxApplicationThread());
//	String res = (String) htmlFx.webEngine.executeScript(
//	        "ace.edit(\"textArea\").getValue()"
//	        );
//	return res;

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
	  System.out.println("settext FX: "+Platform.isFxApplicationThread());
	  //System.out.println("gettext: "+getText());

//	  htmlFx.webEngine.executeScript(
//	        "ace.edit(\"textArea\").setValue(\""+input.replace("\"", "\\\"").replace("\n", "\\n")+"\")"
//	        );
//	  System.out.println("DONE");

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

  public void setDocumentation(String input){
    documentationArea.setText(input);
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

}
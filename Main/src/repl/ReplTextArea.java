package repl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;

public class ReplTextArea extends SplitPane {
  private static final double DIVIDER_POSN = 0.75f;

  SimpleBooleanProperty changed= new SimpleBooleanProperty(false);

  String filename;
  HtmlFx htmlFx;
  TextArea documentationArea;

  public ReplTextArea(CountDownLatch latch,String content) {
    assert Platform.isFxApplicationThread();
    htmlFx=new HtmlFx(this);
    htmlFx.createHtmlContent(latch,content);
    documentationArea=new TextArea();
    documentationArea.setEditable(false);
    this.getItems().addAll(htmlFx, documentationArea);
    this.setDividerPositions(DIVIDER_POSN);
    }

  public void setDocumentation(String input){
    documentationArea.setText(input);
    }

  public String getText(){
    assert !Platform.isFxApplicationThread();
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
	  assert !Platform.isFxApplicationThread();
	  StringBuffer b=new StringBuffer();
	  input.codePoints().forEachOrdered(i->{
	    if(i=='\"') {b.append("\\\"");}
	    else if(i=='\\'){b.append("\\\\");}
	    else if(i=='\n'){b.append("\\n");}
	    else if(i=='\r') {}
	    else {b.appendCodePoint(i);}
	  });
	  String command="ace.edit(\"textArea\").setValue(\""+b+"\")";
	  System.out.println(input);
	  System.out.println(command);
	  FutureTask<?> query = new FutureTask<>(()->{
      htmlFx.webEngine.executeScript(command);
      return null;
      });
    Platform.runLater(query);
    try {query.get();}
    catch (InterruptedException | ExecutionException e) {throw new Error(e);}
    }

}
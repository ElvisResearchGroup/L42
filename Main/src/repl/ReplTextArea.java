package repl;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Worker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;

public class ReplTextArea extends SplitPane {
  private static final double DIVIDER_POSN = 0.75f;

  SimpleBooleanProperty changed= new SimpleBooleanProperty(false);

  String filename;
  HtmlFx htmlFx;
  TextArea documentationArea;

  public ReplTextArea(CountDownLatch latch,URL url) {
    assert Platform.isFxApplicationThread();
    htmlFx=new HtmlFx(this);
    htmlFx.createHtmlContent(latch,url);
    documentationArea=new TextArea();
    documentationArea.setEditable(false);
    this.getItems().addAll(htmlFx, documentationArea);
    this.setDividerPositions(DIVIDER_POSN);
    latch.countDown();
  }

  public void setDocumentation(String input){
    documentationArea.setText(input);
  }

  public Function<CountDownLatch,String> getText(){
    assert Platform.isFxApplicationThread();
    return l->(String) htmlFx.webEngine.executeScript(
        "ace.edit(\"textArea\").getValue()"
        );
  }

  public void setText(String input){
	  assert Platform.isFxApplicationThread();
	  StringBuffer b=new StringBuffer();
	  input.codePoints().forEachOrdered(i->{
	    if(i=='\"') {b.append("\\\"");}
	    else if(i=='\\'){b.append("\\\\");}
	    else if(i=='\n'){b.append("\\n");}
	    else if(i=='\r') {}
	    else {b.appendCodePoint(i);}
	  });
    htmlFx.webEngine.executeScript("ace.edit(\"textArea\").setValue(\""+b+"\")");
  }

}
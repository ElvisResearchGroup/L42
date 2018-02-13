package repl;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;

import facade.L42;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Worker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class ReplTextArea extends SplitPane {
  private static final double DIVIDER_POSN = 0.75f;

  Tab tab;
  final String filename;
  final HtmlFx htmlFx;
  private final TextArea docPanel;

  public ReplTextArea(CountDownLatch latch, String fname, URL url) {
    assert Platform.isFxApplicationThread();
    htmlFx=new HtmlFx(this);
    htmlFx.createHtmlContent(latch,url);
    filename=fname;
    docPanel=new TextArea();
    docPanel.setEditable(false);
    this.getItems().addAll(htmlFx, docPanel);
    this.setDividerPositions(DIVIDER_POSN);
    latch.countDown();
  }

  public void setDoc(String input){
    docPanel.setText(input);
  }

  public void appendDoc(String text) {
    docPanel.appendText(text);
  }

  public String getText(){
    assert Platform.isFxApplicationThread();
    return (String) htmlFx.webEngine.executeScript(
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

  void saveToFile() {
    assert Platform.isFxApplicationThread();
    String content=getText();
    System.out.println(content);
    Path file=L42.root.resolve(this.filename);
    assert file!=null && Files.exists(file);
    try { Files.write(file, content.getBytes()); }
    catch (IOException e) {throw new Error(e);}
  }

  void refresh() {
    assert Platform.isFxApplicationThread();
    Path file=L42.root.resolve(this.filename);
    assert file!=null && Files.exists(file);
    String content; try {content = new String(Files.readAllBytes(file));}
    catch (IOException e) {throw new Error(e);}
    setText(content);
    removeStar();
  }

  void addStar() {
    if(!tab.getText().endsWith("*")) {
      tab.setText(filename+"*");
    }
  }

  void removeStar() {
    if(tab.getText().endsWith("*")) {
      tab.setText(filename);
    }
  }

}
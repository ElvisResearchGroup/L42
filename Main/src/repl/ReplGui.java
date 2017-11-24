package repl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import facade.ErrorFormatter;
import facade.L42;
import profiling.Timer;
@SuppressWarnings("serial")
public class ReplGui extends Application {

  private static final int SCENE_WIDTH = 1000;
  private static final int SCENE_HEIGHT = 800;

  ReplState repl=null;

  TextArea loadedSrc=new TextArea();
  ReplTextArea newSrc=new ReplTextArea(getClass().getResource("textArea.xhtml"));
  /*{newSrc.setText("reuse L42.is/AdamTowel02\n"+
    "Main:{\n"+
    "  Debug(S\"hi!!\")\n"+
    "  return ExitCode.normal()\n"+
    "}");
   }*/
  TextArea output=new TextArea();
  TextArea errors=new TextArea();
  Button runB;

  public static void main(String[] args) {
    L42.setRootPath(Paths.get("localhost"));
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane borderPane = new BorderPane();

    //set the 'loaded', 'output' and 'errors' tabs to be read-only (cannot be edited)
    loadedSrc.setEditable(false);
    output.setEditable(false);
    errors.setEditable(false);

    TabPane tabbedPane = new TabPane();
    tabbedPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    tabbedPane.getTabs().addAll(
    		new Tab("new code", newSrc),
    		new Tab("loaded", loadedSrc),
    		new Tab("output", output),
    		new Tab("errors", errors));

    //System.out.println(System.out.getClass().getName());
    //System.out.println(System.err.getClass().getName());
    System.setOut(delegatePrintStream(err,System.out));
    System.setErr(delegatePrintStream(err,System.err));
    borderPane.setCenter(tabbedPane);

    runB = new Button("Run!");
    runB.setOnAction(e->runCode());
    runB.setMaxWidth(Double.MAX_VALUE);
    borderPane.setBottom(runB);

    Scene scene = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT); // Manage scene size
    primaryStage.setTitle("L42 IDE");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(scene.getWidth());
    primaryStage.setMinHeight(scene.getHeight());
    primaryStage.show();
  }

  @Override
  public void stop(){
    if (L42.profilerPrintOn){
      System.out.print(Timer.report());
    }
    //g.dispose();
    System.gc();
    System.runFinalization();
    System.exit(0);
  }

StringBuffer err=new StringBuffer();
boolean running=false;
ExecutorService executor = Executors.newFixedThreadPool(1);
void runCode(){
  if(running){throw new Error("Was running");}
  running=true;
  runB.setText("Running");
  runB.setDisable(true);
  /*Future<Object> future = */executor.submit(this::auxRunCode);
  }

void auxRunCode(){
  boolean[] success= {false};
  try{
  String code=newSrc.getText();
  L42.cacheK.setFileName("ReplCache.L42",code);
  if(repl==null){ repl=ReplState.start("{"+code+"}");}
  else{
    ReplState newR=repl.add(code);
    if(newR!=null){repl=newR;}
    }
  success[0]=true;
  }
  catch(ParseCancellationException parser){
    System.out.println(parser.getMessage());
    }
  catch(ErrorMessage msg){
    ErrorFormatter.topFormatErrorMessage(msg);
    }
  catch(Throwable t){
     //somehow t.printstacktrace freeze stuff as well as inspecting t.cause
      System.out.println(
            ""+t+"\n"+
           Arrays.asList(t.getStackTrace()).stream()
           .map(e->e.toString()+"\n").reduce("",(a,b)->a+b));
      }
  finally{
    Platform.runLater(()->this.updateTextFields(success[0]));
    }
  }
private  int iterations=0;
private void updateTextFields(boolean success){
  try{
    assert L42.record!=null:"d";
    assert err!=null:"a";
    assert errors!=null:"b";
    assert loadedSrc!=null:"c";
    output.setText(L42.record.toString());
    String newErr=err.toString();
    errors.setText(newErr);
    if(repl==null){return;}
    loadedSrc.setText(repl.originalS);
    if(success) {
      iterations+=1;
      newSrc.setText(
        "Main"+iterations+":{//make more stuff happen!\n"+
        "  return ExitCode.normal()\n  }"
        );
      }
    }
  finally{
    this.running=false;
    runB.setDisable(false);
    runB.setText("Run!");
    }
  }
private void doAndWait(Runnable r){
  try {executor.submit(r).get();}
  catch (InterruptedException | ExecutionException e) {
    throw new Error(e);
    }
  }
public static PrintStream delegatePrintStream(StringBuffer err,PrintStream prs){
  return new PrintStream(prs){
    public void print(String s) {
//      doAndWait(()->{
//        prs.print(s);
        err.append(s);
//        });
      super.print(s);
      }
    public void println(String s) {
//      doAndWait(()->{
        String ss=s+"\n";
//        prs.println(ss);
        err.append(ss);
//        });
      super.println(s);
      }
    };
  }
}
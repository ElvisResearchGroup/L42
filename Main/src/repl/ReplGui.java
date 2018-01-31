package repl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import ast.PathAux;
import facade.ErrorFormatter;
import facade.L42;
import profiling.Timer;
@SuppressWarnings("serial")
public class ReplGui extends Application {

  private static final int SCENE_WIDTH = 1000;
  private static final int SCENE_HEIGHT = 800;

  ReplState repl=null;

  TextArea output=new TextArea();
  TextArea errors=new TextArea();
  Button runB;

  Tab selectedTab=null;

  private boolean rootPathSet=false;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane borderPane = new BorderPane();

    TabPane tabPane = new TabPane();
    tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
      @Override public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, Tab newTab) {
        selectedTab = newTab;
      }
    });

    MenuBar leftBar = new MenuBar();
    MenuBar rightBar = new MenuBar();

    Region spacer = new Region();
    spacer.getStyleClass().add("menu-bar");
    HBox.setHgrow(spacer, Priority.SOMETIMES);
    HBox menubars = new HBox(leftBar, spacer, rightBar);

    Menu menuNew = new Menu("New Project");
    MenuItem menuItemNew = new MenuItem();
    menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select an existing folder for the project or enter a new folder name!");
        File outputFolder = directoryChooser.showDialog(primaryStage);

        L42.setRootPath(outputFolder.toPath());
        rootPathSet=true;

        Tab tab = new Tab();

        if (outputFolder != null) {
    	  tab.setText(outputFolder.getPath());
    	} else
            tab.setText("This.L42");
        ReplTextArea newSrc=new ReplTextArea(getClass().getResource("textArea.xhtml"));
        tab.setContent(newSrc);
        tabPane.getTabs().add(tab);
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tab);
      }
    });
    this.setupSingleMenuItem(menuNew, menuItemNew);

    Menu menuOpen = new Menu("Open Project");
    MenuItem menuItemOpen = new MenuItem();
    menuItemOpen.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("L42 files", "*.L42"));

        File fileToOpen = fc.showOpenDialog(primaryStage);

        //TODO CHECK IS VALID L42 PROJECT otherwise give error
        //L42.setRootPath(outputFolder.toPath());
        //rootPathSet=true;

        if(fileToOpen!=null) {
          // Read the file, and set its contents within the editor
          String openFileName = fileToOpen.getAbsolutePath();
          StringBuffer sb = new StringBuffer();
          try(FileInputStream fis = new FileInputStream(fileToOpen);
        		  BufferedInputStream bis = new BufferedInputStream(fis)) {
            while(bis.available()>0) {
              sb.append((char)bis.read());
            }
          } catch(Exception e) {
            e.printStackTrace();
          }
          System.out.println(getClass().getResource("textArea.xhtml"));
          ReplTextArea editor=new ReplTextArea(getClass().getResource("textArea.xhtml"));
          //System.out.println(sb.toString());
          // editor.setText(sb.toString());
          editor.setText("heelo");
          //editor.htmlFx.webEngine.load(sb.toString());
          editor.filename = openFileName;          //
          //System.out.println(editor.getText());


          Tab tab = new Tab();
          tab.setText(fileToOpen.getName());
          tab.setContent(editor);
          tabPane.getTabs().add(tab);

          SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
          selectionModel.select(tab);
        }
      }
    });
    this.setupSingleMenuItem(menuOpen, menuItemOpen);

    Menu menuRun = new Menu("Run!");
    MenuItem menuItemRun = new MenuItem();
    menuItemRun.setOnAction(e->runCode());
    this.setupSingleMenuItem(menuRun, menuItemRun);

    leftBar.getMenus().addAll(menuNew, menuOpen); //, menuFileExit);
    rightBar.getMenus().addAll(menuRun);
    borderPane.setTop(menubars);


    //System.out.println(System.out.getClass().getName());
    //System.out.println(System.err.getClass().getName());
    System.setOut(delegatePrintStream(err,System.out));
    System.setErr(delegatePrintStream(err,System.err));

    TabPane outputPane = new TabPane();
    outputPane.setSide(Side.LEFT);
    outputPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    output.setEditable(false);
    errors.setEditable(false);
    outputPane.getTabs().add(new Tab("output", output));
    outputPane.getTabs().add(new Tab("errors", errors));

    SplitPane splitPane = new SplitPane();
    splitPane.getItems().addAll(tabPane, outputPane);
    splitPane.setDividerPositions(0.7f);
    splitPane.setOrientation(Orientation.VERTICAL);

    borderPane.setCenter(splitPane);

    Scene scene = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT); // Manage scene size
    primaryStage.setTitle("L42 IDE");
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(scene.getWidth());
    primaryStage.setMinHeight(scene.getHeight());
    primaryStage.show();
  }

private void setupSingleMenuItem(Menu menu, MenuItem menuItem) {
  menu.getItems().add(menuItem);
  menu.showingProperty().addListener((observableValue, oldValue, newValue) -> {
    if (newValue) {
      // the first menuItem is triggered
      menu.getItems().get(0).fire();
      }
  });
}

  @Override
  public void stop(){
    if (L42.profilerPrintOn){
      System.out.print(Timer.report());
    }
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
  System.out.println("Auxruncode starting FX: "+Platform.isFxApplicationThread());
  this.auxRunCode();
  System.out.println("Auxruncode done FX: "+Platform.isFxApplicationThread());
  }

void auxRunCode(){

  if(!rootPathSet) {
	Alert alert = new Alert(AlertType.ERROR);
	alert.setTitle("Invalid Run");
	alert.setHeaderText("Invalid Run");
	alert.setContentText("Please either create a new project or open an existing L42 project before you can run the code");
	alert.show();
    return;
  }

  try{
    //check first 2 line of This.l42
    File thisL42 = new File(L42.root.toFile(), "This.L42");

    CodeInfo res=new CodeInfo(thisL42);

    //check if library already cached in L42IDE folder
    Path currentRoot=L42.root;
    L42.setRootPath(Paths.get("L42IDE"));

    File directory = new File(res.cacheLibName);

    if (!directory.exists()){ //if not already cached before
      directory.mkdir();

      L42.setRootPath(Paths.get("L42IDE", res.cacheLibName));

      L42.cacheK.setFileName("This.L42",res.first2Line);
      repl=ReplState.start("{"+res.first2Line+"}");
    }

    L42.setRootPath(currentRoot); //go back to project folder

    //Copy the files into the current project
    Path src=Paths.get("L42IDE", res.cacheLibName);
    Path dest=currentRoot;
    this.copyEntireDirectory(src,dest);

    L42.cacheK.setFileName("This.L42",res.restOfCode);
    ReplState newR=repl.add(res.restOfCode);
    if(newR!=null){repl=newR;}

//  Map<String,ReplState> fileNameToCache = new HashMap<>();
//
////  List<Path> filesInProject = Files.walk(L42.root)
////  .filter(Files::isRegularFile)
////  .filter(File)
////  .collect(Collectors.toList())
//
//  try (Stream<Path> paths = Files.walk(L42.root)) {
//	    paths.filter(Files::isRegularFile)
//	    paths.fi
//	        .forEach(System.out::println);
//	}



  }
  catch(IllegalArgumentException e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Invalid Run");
    alert.setHeaderText("Invalid Run");
    alert.setContentText("Missing two line at the start of This.L42 file for the caching library");
    alert.show();
  }
  catch(NullPointerException e) {
	  throw new Error(e);
	}
  catch(ParseCancellationException parser){
    throw new Error(parser);
    }
  catch(ErrorMessage msg){
    ErrorFormatter.topFormatErrorMessage(msg);
    }
  catch(Throwable t){
     //somehow t.printstacktrace freeze stuff as well as inspecting t.cause
      throw new Error(
            ""+t+"\n"+
           Arrays.asList(t.getStackTrace()).stream()
           .map(e->e.toString()+"\n").reduce("",(a,b)->a+b));
      }
  finally{
    this.updateTextFields();
    }
  }

//returns a String array with 3 things ==>  {first2Line,cacheLibName,restOfCode}
protected static class CodeInfo{
  String first2Line;
  String cacheLibName;
  String restOfCode;
  CodeInfo(File file){
    try(Scanner sc = new Scanner(file)) {
    	sc.useDelimiter(Pattern.compile("(\\n)| |,")); //newline, spaces and comma

  	  if(!sc.next().equals("reuse")) {throw new IllegalArgumentException();}
  	  this.cacheLibName=sc.next();
      String className=sc.next();
  	  if(!PathAux.isValidClassName(className)) {throw new IllegalArgumentException();}
  	  if(!sc.next().equals(":")) {throw new IllegalArgumentException();}
  	  if(!sc.next().equals("Load.cacheTowel()")) {throw new IllegalArgumentException();}

    	this.first2Line="reuse "+cacheLibName+"\n"+className+":"+"Load.cacheTowel()";
      sc.useDelimiter("\\z");
    	this.restOfCode=sc.next();
    }
  catch (FileNotFoundException e) {throw new Error(e);}
  }
}

private void copyEntireDirectory(Path src, Path dest) {
  try (Stream<Path> stream = Files.walk(src)) {
    stream.forEach(sourcePath -> {
  	  try {
  	    Files.copy(sourcePath, src.resolve(dest.relativize(sourcePath)));
  	  } catch (Exception e) {throw new Error(e);}
    });
  } catch (IOException e1) {throw new Error(e1);}

}

private void updateTextFields(){
  try{
    assert L42.record!=null:"d";
    assert err!=null:"a";
    assert errors!=null:"b";
    output.setText(L42.record.toString());
    String newErr=err.toString();
    errors.setText(newErr);
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
package repl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
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

  public static void main(String[] args) {
    L42.setRootPath(Paths.get("localhost"));
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

//  private void indicateFileModified() {
//      if ( currentEditor != null && currentEditor.modified ) {
//          return;
//      }
//
//      // Get current tab, add an "*" to its name to indicate modified
//      System.out.println("Indicating text modified");
//      SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//      Tab selectedTab = selectionModel.getSelectedItem();
//      TextArea area = (TextArea)selectedTab.getContent();
//      currentEditor = getEditorForTextArea(area);
//      String modName = selectedTab.getText();
//      if ( ! modName.endsWith("*") ) {
//          modName += "*";
//          selectedTab.setText(modName);
//      }
//      currentEditor.modified = true;
//  }
//
//  private SimpleEditor getEditorForTextArea(TextArea area) {
//      Iterator<SimpleEditor> iter = editors.iterator();
//      while ( iter.hasNext() ) {
//          SimpleEditor editor = iter.next();
//          if ( area == (TextArea)editor.getRoot() )
//              return editor;
//      }
//
//      return null;
//  }
//
//  private void saveFileRev() {
//      System.out.println("saving file");
//      boolean success = false;
//      SimpleEditor editor = null;
//      File file = null;
//
//      SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//      Tab selectedTab = selectionModel.getSelectedItem();
//      editor = getEditorForTextArea((TextArea)selectedTab.getContent());
//      if ( editor == null )
//          return;
//      String openFileName = editor.filename;
//
//      if ( openFileName == null ) {
//          // No file was opened. The user just started typing
//          // Save new file now
//          FileChooser fc = new FileChooser();
//          File newFile = fc.showSaveDialog(null);
//          if ( newFile != null ) {
//              // Check for a file extension and add ".txt" if missing
//              if ( ! newFile.getName().contains(".") ) {
//                  String newFilePath = newFile.getAbsolutePath();
//                  newFilePath += ".txt";
//                  newFile.delete();
//                  newFile = new File(newFilePath);
//              }
//              file = newFile;
//              openFileName = new String(newFile.getAbsolutePath());
//              editor.filename = openFileName;
//              selectedTab.setText(newFile.getName());
//          }
//      }
//      else {
//          // User is saving an existing file
//          file = new File(openFileName);
//      }
//
//      // Write the content to the file
//      try ( FileOutputStream fos = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(fos) ) {
//          String text = editor.getText();
//          bos.write(text.getBytes());
//          bos.flush();
//          success = true;
//      }
//      catch ( Exception e ) {
//          success = false;
//          System.out.println("File save failed (error: " + e.getLocalizedMessage() + ")");
//          e.printStackTrace();
//      }
//      finally {
//          if ( success ) {
//              if ( editor != null ) {
//                  editor.modified = false;
//              }
//
//              // The the tab's filename
//              selectedTab.setText(file.getName());
//          }
//      }
//  }

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
  /*Future<Object> future = */executor.submit(this::auxRunCode);
  }

void auxRunCode(){
  boolean[] success= {false};
  try{
  String code="";//newSrc.getText();
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
    this.updateTextFields(success[0]);
    }
  }

void rollBackCode(){
  if(repl == null) {
	return; //do nothing
  }

  iterations--;
  //newSrc.setText(repl.code);
  repl= repl.oldRepl;

//  if(repl==null) { loadedSrc.clear(); }
//  else { loadedSrc.setText(repl.originalS); }

  }

private  int iterations=0;
private void updateTextFields(boolean success){
  try{
    assert L42.record!=null:"d";
    assert err!=null:"a";
    assert errors!=null:"b";
//    assert loadedSrc!=null:"c";
    output.setText(L42.record.toString());
    String newErr=err.toString();
    errors.setText(newErr);
    if(repl==null){return;}
//    loadedSrc.setText(repl.originalS);
    if(success) {
      iterations+=1;
//      newSrc.setText(
//        "Main"+iterations+":{//make more stuff happen!\n"+
//        "  return ExitCode.normal()\n  }"
//        );
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
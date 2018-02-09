package repl;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import facade.L42;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import profiling.Timer;

public class ReplGui extends Application {
  static ReplMain main;

  private static final int SCENE_WIDTH = 1500;
  private static final int SCENE_HEIGHT = 1000;

  TabPane tabPane=new TabPane();
  TextArea output=new TextArea();
  TextArea errors=new TextArea();
  StringBuffer err=new StringBuffer();
  Button runB;
  boolean rootPathSet=false;
  boolean running=false;

  Tab selectedTab=null;

  @SuppressWarnings("unchecked")
  public static <T>T runAndWait(int operations,Function<CountDownLatch,T>task){
    assert !Platform.isFxApplicationThread();
    CountDownLatch latch = new CountDownLatch(operations);
    Object[]res={null};
    Platform.runLater(()->res[0]=task.apply(latch));
    try {latch.await();}
    catch (InterruptedException e) {throw HtmlFx.propagateException(e);}
    return(T)res[0];
    }

  @Override
  public void start(Stage primaryStage) throws Exception {
    assert Platform.isFxApplicationThread();
    ReplMain.gui=this;
    BorderPane borderPane = new BorderPane();

    tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
      @Override public void changed(ObservableValue<? extends Tab> tab, Tab oldTab, Tab newTab) {
        selectedTab = newTab;
      }
    });

    Button newProjectBtn = new Button("New Project");
    newProjectBtn.setOnAction(t->{
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setTitle("Select an existing folder for the project or enter a new folder name!");
//        File outputFolder = directoryChooser.showDialog(primaryStage);
//
//        L42.setRootPath(outputFolder.toPath());
//        rootPathSet=true;
//
//        Tab tab = new Tab();
//
//        if (outputFolder != null) {
//    	  tab.setText(outputFolder.getPath());
//      	} else
//            tab.setText("This.L42");
//        ReplTextArea newSrc=new ReplTextArea(getClass().getResource("textArea.xhtml"));
//        tab.setContent(newSrc);
//        tabPane.getTabs().add(tab);
//        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
//        selectionModel.select(tab);
    });

    Button openProjectBtn = new Button("Open Project");
    openProjectBtn.setOnAction(t->{
      assert Platform.isFxApplicationThread();
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Select an L42 project to open!");

      File outputFolder = directoryChooser.showDialog(primaryStage);

      //check is a valid L42 project
      if(outputFolder==null) {return;} //no selection has been made
      ReplMain.runLater(()->main.loadProject(outputFolder.toPath()));
    });

    runB = new Button("Run!");
    runB.setOnAction(e->ReplMain.runLater(()->{
      if(running){throw new Error("Was running");}
      this.disableRunB();
      main.runCode();
    }));

    Pane empty=new Pane();
    HBox.setHgrow(empty, Priority.ALWAYS);

    ToolBar toolbar = new ToolBar(newProjectBtn, openProjectBtn, empty, runB);
    borderPane.setTop(toolbar);

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

    SplitPane splitPane = new SplitPane(tabPane, outputPane);
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

  @Override
  public void stop(){
    if (L42.profilerPrintOn){
      System.out.print(Timer.report());
    }
    System.gc();
    System.runFinalization();
    System.exit(0);
  }

  void enableRunB() {
    running=false;
    runB.setDisable(false);
    runB.setText("Run!");
  }
  void disableRunB() {
    running=true;
    runB.setDisable(true);
    runB.setText("Running");
  }

  void openTab(ReplTextArea editor,String openFileName,String tabContent) {
    assert Platform.isFxApplicationThread();
    editor.filename = openFileName;
    editor.setText(tabContent);
    Tab tab = new Tab();
    tab.setText(editor.filename);
    tab.setContent(editor);
    tabPane.getTabs().add(tab);
    SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    selectionModel.select(tab);
    }

  void makeAlert(String title, String content) {
    assert Platform.isFxApplicationThread();
    Alert alert = new Alert(AlertType.ERROR);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
    }

  void updateTextFields(){
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
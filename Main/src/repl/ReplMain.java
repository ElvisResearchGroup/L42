package repl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import ast.PathAux;
import facade.ErrorFormatter;
import facade.L42;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class ReplMain {
  static ReplGui gui;
  static ExecutorService executor = Executors.newFixedThreadPool(1);
  ReplState repl=null;

  public static void main(String []arg) {
    ReplGui.main=new ReplMain();
    Application.launch(ReplGui.class,arg);
    System.out.println("Hi");
  }

  public static void runLater(Runnable runnable) {
    assert Platform.isFxApplicationThread();
    executor.execute(runnable);
  }
  public static void runAndWait(Runnable r){//TODO: blocks the application
    assert Platform.isFxApplicationThread();
    try {executor.submit(r).get();}
    catch (InterruptedException | ExecutionException e) {
      throw new Error(e);
      }
    }

  public void loadProject(Path path) {
    Path thisFile=path.resolve("This.L42");
    if(!Files.exists(thisFile)) {
      Platform.runLater(()->gui.makeAlert("Invalid Project","Selected project does not contain a 'This.L42' file"));
      return;
    }
    L42.setRootPath(path);
    gui.rootPathSet=true;
    openFileInNewTab(thisFile);
  }

  protected void openFileInNewTab(Path file) {
    assert file!=null && Files.exists(file);
    String content;try {content = new String(Files.readAllBytes(file));}
    catch (IOException e) {
      Platform.runLater(()->gui.makeAlert("Invalid Project content","Lost contact with project folder"));
      return;
      }
    String openFileName = file.getFileName().toString();
    makeReplTextArea(openFileName,content);
    }

  private void makeReplTextArea(String openFileName,String tabContent) {
    Path path;try{path=Paths.get(getClass().getResource("textArea.xhtml").toURI());}
    catch (URISyntaxException e) {throw new Error(e);}
    ReplTextArea editor[]= {null};
    String content;try {content = new String(Files.readAllBytes(path));}
    catch (IOException e) {throw new Error(e);}
    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(()->{editor[0]=new ReplTextArea(latch,content);});
    //try {future.get();}
    //catch (ExecutionException e) {throw propagateException(e.getCause());}
    //catch (InterruptedException e) {throw propagateException(e);}
    try {latch.await();}
    catch (InterruptedException e) {throw HtmlFx.propagateException(e);}

    editor[0].filename = openFileName;
    editor[0].setText(tabContent);
    gui.openTab(editor[0]);
    }

  void runCode(){
    //TODO: make run button disabled when project not loader OR run is already running
  /*if(!rootPathSet) {
      Platform.runLater(()->gui.makeAlert("Invalid Run","Create new project or open an existing L42 project to run some code"));
      gui.running=false;
      runB.setDisable(false);
      runB.setText("Run!");
      return;
    }
  */
    try{
      String content=L42.pathToString(L42.root.resolve("This.L42"));
      repl=copyResetKVCthenRun(content);
    }
    catch(IllegalArgumentException e) {
      Platform.runLater(()->gui.makeAlert("Invalid Run","Missing two line at the start of 'This.L42' file"));
      return;
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
      Platform.runLater(()->gui.updateTextFields());
      }
    }

  public static ReplState copyResetKVCthenRun(String fileContent, String... doNotCopyFiles) throws IOException {
    ReplState repl=null;

    //check first 2 line of This.l42
    CodeInfo res=new CodeInfo(fileContent);

    //check if library already cached in L42IDE folder
    Path currentRoot=L42.root;
    L42.setRootPath(Paths.get("L42IDE")); //TODO: check later where is created?

    Path dirPath=L42.root.resolve(res.cacheLibName);
    if (!Files.exists(dirPath)){ //if not already cached before
      Files.createDirectory(dirPath);
      L42.setRootPath(Paths.get("L42IDE").resolve(res.cacheLibName));
      L42.cacheK.setFileName("This.L42",res.first2Line);
      repl=ReplState.start("{"+res.first2Line+"}", L42.root.resolve("This.C42")); //create the cache
    }

    //Copy the files into the current project
    Path src=Paths.get("L42IDE", res.cacheLibName);
    Path dest=currentRoot;
    ReplMain.copyEntireDirectory(src,dest,doNotCopyFiles);

    L42.setRootPath(currentRoot); //go back to project folder
    Path pathC=L42.root.resolve("This.C42");
    //if(repl==null) {
      L42.cacheK.setFileName("This.L42",res.first2Line);
      repl=ReplState.start("{"+res.first2Line+"}", pathC);
    //} else {
    //  repl.reduction.loader.updateCachePath(pathC); //TODO: see why does not cache C properly (saved not in right place?)
    //}

    L42.cacheK.setFileName("This.L42",res.restOfCode);
    ReplState newR=repl.add(res.restOfCode);
    if(newR!=null){repl=newR;}

    return repl;
  }

  protected static class CodeInfo{
    String first2Line;
    String cacheLibUrl;
    String cacheLibName;
    String restOfCode;
    CodeInfo(String string){
      try(Scanner sc = new Scanner(string)) {
        Pattern delimit= Pattern.compile("(\\n| |,)*"); //newline, spaces and comma
        sc.skip(delimit);

        if(!sc.next().equals("reuse")) {throw new IllegalArgumentException();}
        sc.skip(delimit);

        this.cacheLibUrl=sc.next();
        this.cacheLibName=URLEncoder.encode(this.cacheLibUrl, "UTF-8");

        sc.skip(delimit);

        String[] secondLine=sc.nextLine().split(":");
        if(secondLine.length!=2) {throw new IllegalArgumentException();}

        String className=secondLine[0];
        String lastPart=secondLine[1];
        if(!PathAux.isValidClassName(className)) {throw new IllegalArgumentException();}
        if(!lastPart.equals("Load.cacheTowel()")) {throw new IllegalArgumentException();}

        this.first2Line="reuse "+cacheLibUrl+"\n"+className+":"+"Load.cacheTowel()";

        sc.useDelimiter("\\z"); //rest of the content
        this.restOfCode=sc.next();
      } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }

  private static void copyEntireDirectory(Path src, Path dest, String... doNotCopyFiles) {
    try (Stream<Path> stream = Files.list(src)) {
      stream
      .filter(x -> !Arrays.asList(doNotCopyFiles).contains(x.getName(x.getNameCount()-1).toString()))
      .forEach(sourcePath -> {
        try {
          Path target= dest.resolve(sourcePath.getName(sourcePath.getNameCount()-1));
          Files.copy(sourcePath, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {throw new Error(e);}
      });
    } catch (IOException e1) {throw new Error(e1);}

  }



}

package repl;

import java.awt.*; import java.awt.event.*;
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

import javax.swing.*;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import ast.ErrorMessage;
import facade.Configuration;
import facade.ErrorFormatter;
import facade.L42;
import profiling.Timer;
@SuppressWarnings("serial")
public class ReplGui extends JFrame {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    //Configuration.loadAll();
    L42.setRootPath(Paths.get("localhost"));
    L42.cacheK.fileName="ReplCache.L42";
    SwingUtilities.invokeLater(()-> {
    ReplGui g = new ReplGui();
    g.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    g.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        if (L42.profilerPrintOn){
          System.out.print(Timer.report());
          }
        }
      });
    g.getRootPane().setLayout(new BorderLayout());
    g.runB = new JButton("Run!");
    g.runB.addActionListener(e->g.runCode());
    g.getRootPane().add(g.runB, BorderLayout.SOUTH);
    g.buildGui(g.getRootPane());
    g.pack();
    g.setVisible(true);
    });
  }

JTextArea loadedSrc=new JTextArea(20, 50);
ReplTextArea newSrc=new ReplTextArea(getClass().getResource("textArea.xhtml"));
/*{newSrc.setText("reuse L42.is/AdamTowel02\n"+
  "Main:{\n"+
  "  Debug(S\"hi!!\")\n"+
  "  return ExitCode.normal()\n"+
  "}");
 }*/
JTextArea output=new JTextArea(20, 50);
JTextArea errors=new JTextArea(20, 50);
ReplState repl=null;
StringBuffer err=new StringBuffer();
boolean running=false;
JButton runB;
ExecutorService executor = Executors.newFixedThreadPool(1);
void runCode(){
  if(running){throw new Error("Was running");}
  running=true;
  runB.setText("Running");
  runB.setEnabled(false);
  /*Future<Object> future = */executor.submit(this::auxRunCode);
  }
void auxRunCode(){
  boolean[] success= {false};
  try{
  String code=newSrc.getText();
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
    SwingUtilities.invokeLater(()->this.updateTextFields(success[0]));
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
    runB.setEnabled(true);
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
void buildGui(JRootPane pane){
  JTabbedPane tabbedPane = new JTabbedPane();
  loadedSrc.setEditable(false);
  output.setEditable(false);
  errors.setEditable(false);
  tabbedPane.addTab("new code", new JScrollPane(newSrc));
  tabbedPane.addTab("loaded", new JScrollPane(loadedSrc));
  tabbedPane.addTab("output", new JScrollPane(output));
  tabbedPane.addTab("errors", new JScrollPane(errors));
  newSrc.setFont(newSrc.getFont().deriveFont(40f));
  //System.out.println(System.out.getClass().getName());
  //System.out.println(System.err.getClass().getName());
  System.setOut(delegatePrintStream(err,System.out));
  System.setErr(delegatePrintStream(err,System.err));
  pane.add(tabbedPane,BorderLayout.CENTER);
  }
}
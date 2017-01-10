package repl;

import java.awt.*; import java.awt.event.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.swing.*;

import facade.L42;
@SuppressWarnings("serial")
public class ReplGui extends JFrame {
 public static void main(String[] args) {
 helpers.TestHelper.configureForTest();
 SwingUtilities.invokeLater(()-> {
 //UIManager.getLookAndFeelDefaults()
 //.put("defaultFont", new Font("Arial", Font.PLAIN, 24));
 ReplGui g = new ReplGui();
 g.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 g.getRootPane().setLayout(new BorderLayout());
 g.runB = new JButton("------Bar------");
 g.runB.addActionListener(new ActionListener() {
 public void actionPerformed(ActionEvent e) {g.runCode();}});
 g.getRootPane().add(g.runB, BorderLayout.SOUTH);
 g.buildGui(g.getRootPane());
 g.pack();
 g.setVisible(true);
 });
 }

JTextArea loadedSrc=new JTextArea(20, 50);
JTextArea newSrc=new JTextArea(2, 50);
JTextArea output=new JTextArea(20, 50);
JTextArea errors=new JTextArea(20, 50);
ReplState repl=null;
//BufferedReader err;
ByteArrayOutputStream err;
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
  try{
  String code=newSrc.getText();
  if(repl==null){ repl=ReplState.start("{"+code+"}");}
  else{
    ReplState newR=repl.add(code);
    if(newR!=null){repl=newR;}
    }
  }
  catch(Throwable t){
     //somehow t.printstacktrace freeze stuff as well as inspecting t.cause
      System.out.println(
            ""+t+"\n"+
           Arrays.asList(t.getStackTrace()).stream()
           .map(e->e.toString()+"\n").reduce("",(a,b)->a+b));
      }
  finally{
    SwingUtilities.invokeLater(()->{try{
      output.setText(L42.record.toString());
      String newErr=err.toString();
      //String newErr="";
      //try {while(err.ready()){newErr+="\n"+err.readLine();}}
      //catch (IOException e) {throw new Error(e);}
      errors.setText(errors.getText()+newErr);
      if(repl==null){return;}
      loadedSrc.setText(repl.originalS);
      }
      finally{
        this.running=false;
        runB.setEnabled(true);
        runB.setText("Run!");
      }});
    }
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
  //PipedOutputStream pErr = new PipedOutputStream();   
  //System.setErr(new PrintStream(pErr));
  //System.setOut(new PrintStream(pErr)); 
  
  ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  System.setOut(new PrintStream(buffer));
  System.setErr(new PrintStream(buffer));
  //PipedInputStream pIn;try {pIn = new PipedInputStream(pErr);}
  //catch (IOException e) {throw new Error(e);}  
  err = buffer;//new BufferedReader(new InputStreamReader(pIn));
  pane.add(tabbedPane,BorderLayout.CENTER);
  }


}
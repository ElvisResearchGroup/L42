package is.L42.connected.withHtml;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import repl.HtmlFx;

public class TestWebEngine {
  @After
  public void tearDown() throws InterruptedException {
     Thread.sleep(25000);
  }
  @Test
  public void test() throws InvocationTargetException, InterruptedException,FileNotFoundException {
    //@SuppressWarnings("resource")
    //String content = new Scanner(new File("htmlTests/tutorial.xhtml")).useDelimiter("\\Z").next();
    SwingUtilities.invokeLater(()->{
      HtmlFx h=new HtmlFx(new JFXPanel());
      h.createHtmlContent("<body><div>hi</div><div>hi</div></body>");
      URL url = getClass().getResource("tutorial.xhtml");
      Platform.runLater(()->h.webEngine.load(url.toExternalForm()));
      JFrame f=new JFrame("myTitle");
      JPanel p=new JPanel();
      p.setPreferredSize(new Dimension(500,500));
      p.setLayout(new BorderLayout());
      p.add(h.jfxPanel,BorderLayout.CENTER);
      f.add(p);
      f.pack();f.setVisible(true);
    });

  }

}



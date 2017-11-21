package is.L42.connected.withHtml;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
    JFXPanel fxPanel = new JFXPanel(); //this is added so that an exception "toolkit not initialised" doesnt occur
    Platform.runLater(new Runnable() {
      public void run() {
          new TestFXClass().start(new Stage());
      }
   });
  }

  public static class TestFXClass extends Application {

    @Override
    public void start(Stage stage) {
      StackPane root = new StackPane();

      HtmlFx h=new HtmlFx(root);
      h.createHtmlContent("<body><div>hi</div><div>hi</div></body>");
      URL url = getClass().getResource("tutorial.xhtml");
      Platform.runLater(()->h.webEngine.load(url.toExternalForm()));

      Scene scene = new Scene(root, 500, 500);
      stage.setTitle("myTitle!");
      stage.setScene(scene);
      stage.show();
    }
  }

}



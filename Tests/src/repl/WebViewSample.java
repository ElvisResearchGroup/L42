package repl;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class WebViewSample extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser(),750,500, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        this.webEngine.getLoadWorker().stateProperty().addListener(
          (ov, oldState,newState)->{
            if (newState == Worker.State.SUCCEEDED) {
              String content="<!DOCTYPE html>\n" +
                  "<html>\n" +
                  "<head>\n" +
                  "  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                  "  <link rel=\"stylesheet\" type=\"text/css\" href=\"stylish.css\" media=\"all\"/>\n" +
                  "</head>\n" +
                  "  <body>\n" +
                  "    <h1>See</h1><h2>you</h2><h3>later</h3>\n" +
                  "  </body>\n" +
                  "</html>";
              webEngine.loadContent(content);
              }
            });
        webEngine.load(getClass().getResource("header.html").toExternalForm());
        //add the web view to the scene
        getChildren().add(browser);

    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
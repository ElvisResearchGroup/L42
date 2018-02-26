package repl;

import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.event.HyperlinkEvent.EventType;

import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.MethodSelector;
import ast.Ast.MethodType;
import ast.ExpCore;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Duration;
import sugarVisitors.ToFormattedText;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB;
class Cell{
  String label;
  String tooltip;
  Cell(MethodWithType mwt){
    tooltip=mwt.getDoc().toString();
    mwt=mwt.with_inner(null);
    mwt=mwt.withDoc(Doc.empty());
    label=ToFormattedText.of(mwt);
    label=label.replaceAll("\\n", " ");
  }
  Cell(NestedClass nc){
    label=nc.getName().toString();
    tooltip=nc.getDoc().toString();
    }
}
public class DocPanel extends VBox{
  ObservableList<Cell> ms;
  ObservableList<Cell> ns;
  Consumer<Object>refresh;
  Label label=new Label("------");
  private Node content() {
    Callback<ListView<Cell>, ListCell<Cell>> f=l->new ListCell<Cell>() {
      @Override public void updateItem(Cell c, boolean empty) {
        super.updateItem(c, empty);
        if(c==null) {return;}
        setText(c.label);
        DocPanel.setTooltip(this,c.tooltip);
        }
      };

    ListView<Cell> methods = new ListView<>();
    ms =FXCollections.observableArrayList();
    methods.setItems(ms);
    methods.setCellFactory(f);
    methods.setCacheShape(false);

    ListView<Cell> nesteds = new ListView<>();
    ns =FXCollections.observableArrayList();
    nesteds.setItems(ns);
    nesteds.setCellFactory(f);
    nesteds.setCacheShape(false);

    TitledPane msTP = new TitledPane("Mehtods",methods);
    TitledPane nsTP = new TitledPane("Nesteds",nesteds);
    Accordion acc=new Accordion(msTP,nsTP);
    refresh=o->Platform.runLater(()->{
      msTP.setAnimated(false);
      nsTP.setAnimated(false);
      msTP.setExpanded(false);
      nsTP.setExpanded(false);
      Timeline timeline = new Timeline(new KeyFrame(
          Duration.millis(150),
          ae ->{
            msTP.setExpanded(true);
            msTP.setAnimated(true);
            nsTP.setAnimated(true);
          }));
      timeline.play();
      });
    return acc;
  }
  public DocPanel() {
    super();
    //label.setFont(Font.font(label.getFont().getSize()*2));
    //grows also the font of the tooltip??
    this.getChildren().add(label);
    this.getChildren().add(content());
    }
  public void setClassB(Ast.Path name,ClassB cb) {
    this.label.setText(name.toString());
    ms.clear();
    for(MethodWithType mwt:cb.mwts()) {
      ms.add(new Cell(mwt));
    }
    ns.clear();
    for(NestedClass nc:cb.ns()) {
      ns.add(new Cell(nc));
    }
    setTooltip(label,"Supertypes: "+cb.getSuperPaths()+"\n"+cb.getDoc1());
    refresh.accept(null);
  }

  public static void setTooltip(Node n,String text) {
    Tooltip t = new Tooltip(text);
    n.setOnMouseEntered(event->{
      Bounds lb = n.getLayoutBounds();
      Point2D p = n.localToScreen(lb.getMaxX(),lb.getMaxY());
      t.show(n, p.getX(), p.getY());
      });
    n.setOnMouseExited(event->t.hide());
    }
  }

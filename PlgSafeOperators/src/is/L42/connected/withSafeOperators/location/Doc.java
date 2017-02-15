package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.Ast.Position;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Doc extends Location.LocationImpl<Ast.Doc,Location>{
  public Doc(Ast.Doc inner,Location location) {super(inner,location);}
  public int annotationsSize(){return inner.getAnnotations().size();}
  
  public Annotation annotation(int that) throws NotAvailable{
    Object ann=Location.listAccess(inner.getAnnotations(), that);
    return null;//TODO: waiting for the RefTo kinds to be implemented
    }
  public static class Annotation{
    public TypeRefTo key() {return key;}
    public void key(TypeRefTo key) {this.key = key;}
    public String text() {return text;}
    public void text(String text) {this.text = text;}
    public Annotation(TypeRefTo key, String text) {
    super();
    this.key = key;
    this.text = text;
    }
    TypeRefTo key; 
    String text;
    }
  @Override public String toS() {return inner.getS();}
  @Override public Doc doc() {return this;}
  }
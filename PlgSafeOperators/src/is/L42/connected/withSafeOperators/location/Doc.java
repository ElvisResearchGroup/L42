package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.Ast.Position;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Doc extends Location.LocationImpl<Ast.Doc,Location>{
  public Doc(Ast.Doc inner,Location location) {super(inner,location);}
  public int annotationSize(){return inner.getAnnotations().size();}
  
  public Annotation annotation(int that) throws NotAvailable{
    Object ann = Location.listAccess(inner.getAnnotations(), that);
    assert false;
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
    public boolean equalequal(Object that){return this.equals(that);}
    public String toS(){
      return "Annotation:@"+key+" "+text;
      }
    //Generated
    @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      return result;
      }
    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass())return false;
      Annotation other = (Annotation) obj;
      if (key == null) {
        if (other.key != null) return false;
      } else if (!key.equals(other.key))return false;
      if (text == null) {
        if (other.text != null) return false;
      } else if (!text.equals(other.text)) return false;
      return true;
      }
    
    }
  @Override public String toS() {return inner.getS();}
  @Override public Doc doc() {return this;}
  @Override public boolean equalequal(Object that) {
    return this.equals(that);
    }
  //Note: equals and hash code overriden "enough" in the super class
  }
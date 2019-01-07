package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import ast.Ast.Position;
import ast.Ast.Path;
import facade.PData;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;

public class Doc extends Location.LocationImpl<Ast.Doc,Location>{
  public Doc(Ast.Doc inner,Location location) {super(inner,location);}
  public int annotationSize(){return inner.getAnnotations().size();}
  
  public Annotation annotation(PData pData,int that) throws NotAvailable{
    Object titleObj = Location.listAccess(inner.getAnnotations(), that);
    String text = Location.listAccess(inner.getParameters(), that);
    TypeRefTo title;
    if(titleObj instanceof String){
      title=new TypeRefTo.Missing((String)titleObj);
      }
    else{
      Path path=(Path)titleObj;
      Lib lib=locationLib();
      title= Location.refTo(pData.p,path,lib.path,lib.root());
      }
    return new Annotation(title,text);
    }
  public Lib locationLib(){
    Location l=location();
    while (!(l instanceof Lib)){l=l.location();}
    return (Lib)l;
    }
  public static class Annotation{
    public TypeRefTo title() {return title;}
    public String text() {return text;}
    public Annotation(TypeRefTo key, String text) {
    super();
    this.title = key;
    this.text = text;
    }
    TypeRefTo title; 
    String text;
    //public boolean equalequal(Object that){return this.equals(that);}
    public String toS(){
      return "Annotation:@"+title+" "+text;
      }
    //Generated
    @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((title == null) ? 0 : title.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      return result;
      }
    @Override public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass())return false;
      Annotation other = (Annotation) obj;
      if (title == null) {
        if (other.title != null) return false;
      } else if (!title.equals(other.title))return false;
      if (text == null) {
        if (other.text != null) return false;
      } else if (!text.equals(other.text)) return false;
      return true;
      }
    
    }
  public String toS() {return inner.getS();}
  @Override public Doc doc() {return this;}
  //@Override public boolean equalequal(Object that) {return this.equals(that);}
  //Note: equals and hash code overriden "enough" in the super class
  }
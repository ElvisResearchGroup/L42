package is.L42.connected.withSafeOperators.location;

import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.PathAux;

public interface TypeRefTo {
  boolean equalequal(Object that);
  String toS();//toS is the full path with Thisn.
  static class Lib implements TypeRefTo{
    public Lib(is.L42.connected.withSafeOperators.location.Lib root, Path path) {
      this.root = root;
      this.path = path;
      }
    is.L42.connected.withSafeOperators.location.Lib root;//to make referredLib() cheaper, we may cache it, but it may not be a good idea...
    Ast.Path path; //referring inside the Lib
    public is.L42.connected.withSafeOperators.location.Lib referredLib(){
      return root.navigateCs(path.getCBar());
      }    
    @Override public String toS() {
      return PathAux.as42Path(path.getCBar());
      }
    @Override public boolean equalequal(Object that) {
      return this.equals(that);
      }
    }
  static class Unavailable implements TypeRefTo{
    String repr;
    @Override public String toS() {return repr; }
    @Override public boolean equalequal(Object that) {
      return this.equals(that);
      }
    public Unavailable(String repr){this.repr=repr;}
    }
  static class Binded implements TypeRefTo{//includes primitives
    public Binded(Path path) {
      this.path = path;
      }
    Ast.Path path;//pre frommed to be ok with PData
    public Ast.Path referredClassObj(){
      return path;
      }
    public static boolean equalsClassObj(Ast.Path that,Ast.Path and){
      return that.equals(and);
      }
    @Override public String toS() {return path.toString(); }
    @Override public boolean equalequal(Object that) {
      return this.equals(that);
      }
    }
  static class Missing implements TypeRefTo{
    String repr;//if was Path, pre frommed to be ok with PData
    //in case of docs, can just be the doc @string
    public Missing(String repr){this.repr=repr;}
    @Override public String toS() {return repr; }
    @Override public boolean equalequal(Object that) {
      return this.equals(that);
      }
    //mostly useful for docs, where we can use lowercase annotation
    //or we may want to preserve @P where we removed the P
    //also, if an (nested) Uncompiled is resolved not existing..        
    }
  }

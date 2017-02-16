package is.L42.connected.withSafeOperators.location;

import java.util.List;

import ast.Ast;
import ast.Ast.Path;

public interface TypeRefTo {
  String toS();//toS is the full path with Thisn.
  static class Lib implements TypeRefTo{
    public Lib(is.L42.connected.withSafeOperators.location.Lib root, Path path) {
      this.root = root;
      this.path = path;
      }
    is.L42.connected.withSafeOperators.location.Lib root;//to make referredLib() cheaper, we may cache it, but it may not be a good idea...
    Ast.Path path; //referring inside the Lib
    public is.L42.connected.withSafeOperators.location.Lib referredLib(){
      return root.navigate(path.getCBar());
      }    
    @Override public String toS() {return path.toString(); }
    }
  static class Unavailable implements TypeRefTo{
    Ast.Path path;//pre frommed to be ok with PData
    @Override public String toS() {return path.toString(); }
    }
  static class Binded implements TypeRefTo{//includes primitives
    public Binded(Path path) {
      this.path = path;
      }
    Ast.Path path;//pre frommed to be ok with PData
    public Ast.Path referredClassObj(){return path;}
    @Override public String toS() {return path.toString(); }
    }
  static class Missing implements TypeRefTo{
    String path;//if was Path, pre frommed to be ok with PData
    //in case of docs, can just be the doc @string
    @Override public String toS() {return path; }
    //mostly useful for docs, where we can use lowercase annotation
    //or we may want to preserve @P where we removed the P
    //also, if an (nested) Uncompiled is resolved not existing..        
    }
  }

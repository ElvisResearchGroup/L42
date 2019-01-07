package is.L42.connected.withSafeOperators.location;

import java.util.List;

import ast.Ast;
import ast.Ast.Path;
import ast.PathAux;

public interface TypeRefTo {
  //default boolean equalequal(Object that) {return this.equals(that);}
  String toS();//toS is the full path with Thisn.
  static class Lib implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    result = prime * result + ((root == null) ? 0 : root.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Lib other = (Lib) obj;
    if (path == null) {
    if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
        return false;
    if (root == null) {
    if (other.root != null)
        return false;
    } else if (!root.equals(other.root))
        return false;
    return true;
    }
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
    }
  static class Unavailable implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((repr == null) ? 0 : repr.hashCode());
    return result;
    }

    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Unavailable other = (Unavailable) obj;
    if (repr == null) {
    if (other.repr != null)
        return false;
    } else if (!repr.equals(other.repr))
        return false;
    return true;
    }

    String repr;
    @Override public String toS() {return repr; }

    public Unavailable(String repr){this.repr=repr;}
    }
  static class Binded implements TypeRefTo{//includes primitives
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Binded other = (Binded) obj;
    if (path == null) {
    if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
        return false;
    return true;
    }
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
    }
  static class Missing implements TypeRefTo{
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((repr == null) ? 0 : repr.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Missing other = (Missing) obj;
    if (repr == null) {
    if (other.repr != null)
        return false;
    } else if (!repr.equals(other.repr))
        return false;
    return true;
    }
    String repr;//if was Path, pre frommed to be ok with PData
    //in case of docs, can just be the doc @string
    public Missing(String repr){this.repr=repr;}
    @Override public String toS() {return repr; }

    //mostly useful for docs, where we can use lowercase annotation
    //or we may want to preserve @P where we removed the P
    //also, if an (nested) Uncompiled is resolved not existing..        
    }
  }

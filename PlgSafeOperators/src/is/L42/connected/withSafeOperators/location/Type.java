package is.L42.connected.withSafeOperators.location;

import ast.Ast;
import ast.ErrorMessage;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.MethodWithType;
import ast.ExpCore.ClassB.Phase;
import ast.PathAux;
import programReduction.Program;
import coreVisitors.From;
import facade.PData;
import tools.Assertions;

public interface Type extends Location{
  Ast.NormType type();
  Lib locationLib();
  default TypeRefTo refTo(PData pData) {
    Path path=type().getPath();
    Path whereP=Path.outer(0,locationLib().path);
    path=From.fromP(path,whereP);
    if (path.outerNumber()==0){
      return new TypeRefTo.Lib(locationLib().root(),path);
      }
    Program p=pData.p.evilPush(locationLib().root().inner);
    //will be evilPush
    try{
      ClassB cb=p.extractClassB(path);
      //if(coreVisitors.IsCompiled.of(cb)){
      if(cb.getPhase()!=Phase.None){//norm,typed,coherent
        return new TypeRefTo.Binded(path);
        }
      //else, phase is none but cb available and not normalized
      return new TypeRefTo.Unavailable();//TODO: borderline ok?
      }
    catch(ErrorMessage.PathMetaOrNonExistant pne){
      if (pne.isMeta()){return new TypeRefTo.Unavailable();}
      return new TypeRefTo.Missing();
      }

    }
  default Doc doc() {return new Doc(type().getDoc(),this);}
  default String toS() {return type().toString();}
  default Mdf mdf() {return type().getMdf();}


  static class Return extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Return(Ast.NormType type, MethodWithType inner, Method location) {
      super(inner, location);
      this.type=type;
      }
    Ast.NormType type;
    @Override public Ast.NormType type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    @Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Return other = (Return) obj;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  
  static class Parameter extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Parameter(int pos, Ast.NormType type,MethodWithType inner, Method location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.NormType type;
    @Override public Ast.NormType type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    @Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Parameter other = (Parameter) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  static class Exception extends Location.LocationImpl<ExpCore.ClassB.MethodWithType, Method> implements Type{
    public Exception(int pos, Ast.NormType type,MethodWithType inner, Method location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.NormType type;
    @Override public Ast.NormType type(){return type;}
    @Override public Lib locationLib(){return location().location();}
    @Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Exception other = (Exception) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }
  static class Implemented extends Location.LocationImpl<ExpCore.ClassB, Lib> implements Type{
    public Implemented(int pos, Ast.NormType type,ExpCore.ClassB inner, Lib location) {
      super(inner, location);
      this.pos=pos;
      this.type=type;
      }  
    int pos;
    public int pos(){return pos;}//0 for this
    Ast.NormType type;
    @Override public Ast.NormType type(){return type;}
    @Override public Lib locationLib(){return location();}
    @Override public boolean equalequal(Object that){return this.equals(that);}
    @Override
    public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + pos;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
    }
    @Override
    public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!super.equals(obj))
        return false;
    if (getClass() != obj.getClass())
        return false;
    Implemented other = (Implemented) obj;
    if (pos != other.pos)
        return false;
    if (type == null) {
    if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
        return false;
    return true;
    }
    
    }            
  }

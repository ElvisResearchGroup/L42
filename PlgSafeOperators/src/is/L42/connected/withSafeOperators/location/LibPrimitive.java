package is.L42.connected.withSafeOperators.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ast.Ast;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.PathAux;
import facade.PData;
import is.L42.connected.withSafeOperators.ExtractInfo;
import is.L42.connected.withSafeOperators.ExtractInfo.ClassKind;
import is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors.NotAvailable;
import auxiliaryGrammar.Functions;
public class LibPrimitive extends Lib{
  Path primitive;
  public LibPrimitive(Path primitive){
    super(true,null,null,null);
    this.primitive=primitive;
    }
  public int nestedSize(){return 0;}
  public LibPrimitive nested(int that) throws NotAvailable{throw new NotAvailable();}
  public int methodSize(){return 0;}
  public Method method(int that) throws NotAvailable{throw new NotAvailable();}
  public int implementedSize(){return 0;}
  public Type.Implemented implemented(int that) throws NotAvailable{throw new NotAvailable();}
  @Override public Doc doc(){return new Doc(Ast.Doc.empty(),this);}
  public boolean isInterface(){return primitive==Path.Any();}
  public boolean isRedirectable(){return true;}
  public boolean isPotentialInterface(){return true;}
  public boolean isCloseState(){return false;}
  public boolean isEnsuredCoherent(PData pData){return true;}
  public Lib root(){return this;}
  public String path(){return PathAux.as42Path(Collections.emptyList());}
  public Doc nestedDoc(){return new Doc(Ast.Doc.empty(),this);}
  public String toS() {return "{/*PrimitivePath:"+primitive+"*/}";}
  @Override public boolean equals(Object that) {
    if(this.getClass()!=that.getClass()){return false;}
    return this.primitive==((LibPrimitive)that).primitive;
    }
  @Override public int hashCode() {return primitive.hashCode();}
  }
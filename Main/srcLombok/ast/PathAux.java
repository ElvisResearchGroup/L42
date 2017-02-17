package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ast.Ast.Atom;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Ph;
import platformSpecific.javaTranslation.Resources;
import tools.Assertions;

public class PathAux {
//Factories
public static PathAux.Path sugarParse(List<String> rowData){
  PathAux.Path res=PathPrimitive._parsePrimitive(rowData);
  if (res!=null){return res;}
  res=PathSugar._instance(rowData);
  if (res!=null){return res;}
  res=PathCore._parsePathCode(rowData);
  if (res!=null){return res;}
  throw Assertions.codeNotReachable("InvalidPath: " + rowData);
  }
public static Path sugarParse(String path) {
  List<String> rowData = Arrays.asList(path.split("\\."));
  return sugarParse(rowData);
  }
public static PathAux.Path parse(List<String> rowData){
  PathAux.Path res=PathPrimitive._parsePrimitive(rowData);
  if (res!=null){return res;}
  res=PathCore._parsePathCode(rowData);
  if (res!=null){return res;}
  throw Assertions.codeNotReachable("InvalidPath: " + rowData);
  //this does not accept the sugarPath
  }
public static Path parse(String path) {
  List<String> rowData = Arrays.asList(path.split("\\."));
  return sugarParse(rowData);
  }
public static Path outer(int n, List<String> cs){
  return PathCore.instance(n,cs);
  }
public static Path outer(int n){
  return PathCore.instance(n,Collections.emptyList());
  }
public static Path Void() {return PathPrimitive._Void;}
public static Path Any() {return  PathPrimitive._Any;}
public static Path Library() {return PathPrimitive._Library;}




public static boolean isValidOuter(String name) {
  // thus invalid as pathName
  if (name.equals("This")) { return true;}
  if (name.equals("This0")) { return true;}
  if (!name.startsWith("This")) { return false;}
  int firstN = "This".length();
  char c = name.charAt(firstN);
  // first is 1--9 and all rest is 0-9
  if ("123456789".indexOf(c) != -1) {return false;}
  for (int i = firstN + 1; i < name.length(); i++) {
    if ("0123456789".indexOf(name.charAt(i)) == -1) {
      return false;
      }
    }
  return true;
  }
public static List<String> toValidCs(List<String> cs){
  assert (cs=assertingToValidCs(cs))!=null;
  return cs;
  }
public static List<String> assertingToValidCs(List<String> cs){
  for (String s : cs) {
    if (!PathAux.isValidClassName(s)) {
      throw Assertions.codeNotReachable("InvalidPath: " + cs);
      }
    }
  return Collections.unmodifiableList(cs);
  }
public static List<String> parseValidCs(String cs) {
  if (cs.equals("This0") || cs.equals("This")) {
    return Collections.emptyList();
    }
  List<String> rowData = Arrays.asList(cs.split("\\."));
  return toValidCs(rowData);
  }

public static boolean isValidPrimitiveName(String name){
  Path p=PathPrimitive._parsePrimitive(name);
  return p!=null;
  }

public static boolean isValidClassName(String name) {
  if(name.isEmpty()){ return false;}
  if (isValidOuter(name)) { return false;}
  if(isValidPrimitiveName(name)){return false;}
  if (!isValidPathStart(name.charAt(0))) { return false;}
  for (int i = 1; i < name.length(); i++) {
    if (!isValidPathChar(name.charAt(i))) { return false; }
    }
  return true;
  }
public static boolean isValidPathStart(char c) {
  if (c == '%') { return true;}
  if (c == '$') { return true;}
  return Character.isUpperCase(c);
  }
public static boolean isValidPathChar(char c) {
  if (c == '%') { return true;}
  if (c == '$') { return true;}
  if (c == '_') { return true;}
  assert c!='\t': c;
  return Character.isUpperCase(c) 
    || Character.isLowerCase(c)
    || Character.isDigit(c);
  }


  public static abstract class Path implements Expression, ExpCore, Atom{
    public NormType toImmNT(){return new NormType(Mdf.Immutable,null/*this*/,Ph.None,Doc.empty());}
    public <T> T accept(sugarVisitors.Visitor<T> v) {
      return null;//v.visit(this);
      }
    public <T> T accept(coreVisitors.Visitor<T> v) {
      return null;// v.visit(this);
      }
    public boolean isPrimitive() {return false;}
    public boolean isCore() { return false; }
    public String toString() { return sugarVisitors.ToFormattedText.of((Ast.Path)null/*this*/);}

    public PathAux.Path popC(){throw Assertions.codeNotReachable("path.pocC on not core:"+this);}
    public PathAux.Path pushC(String c){throw Assertions.codeNotReachable("path.pushC on not core:"+this);}
    public List<String> getCBar(){throw Assertions.codeNotReachable("path.getCBar on not core:"+this);}
    public PathAux.Path setNewOuter(int n){throw Assertions.codeNotReachable("path.setNewOuter on not core:"+this);}
    public int outerNumber(){throw Assertions.codeNotReachable("path.outerNumber on not core:"+this);}
  }
}  

class PathCore extends PathAux.Path{
  public PathCore(int outerN, List<String> cBar) {
    this.outerN = outerN;
    this.cBar = cBar;
    }
  public static PathAux.Path instance(int n,List<String> cs){
    if (n==0 && cs.isEmpty()){return _This0;}
    return new PathCore(n,PathAux.toValidCs(cs));
    }
  public static PathAux.Path _parsePathCode(List<String>names){
    if(names.isEmpty()){return null;}
    String first=names.get(0);
    if(!PathAux.isValidOuter(first)){return null;}
    first = first.substring("This".length());
    int n=0;
    if(!first.isEmpty()){
      n = Integer.parseInt(first);
      }
    List<String>tail=names.subList(1, names.size());
    return instance(n,tail);
    }
  private static final PathCore _This0 = new PathCore(0, PathAux.toValidCs(Arrays.asList("This0")));
  final int outerN;
  final List<String> cBar;
  public boolean isCore() { return true; }
  public PathAux.Path popC(){return new PathCore(outerN,cBar.subList(0,cBar.size()-1));}
  public PathAux.Path pushC(String c){
    assert PathAux.isValidClassName(c);
    List<String> newCBar = new ArrayList<>(cBar);
    newCBar.add(c);
    return new PathCore(outerN,PathAux.toValidCs(newCBar));
    }
  public List<String> getCBar(){return cBar;}
  public PathAux.Path setNewOuter(int n){return new PathCore(n,cBar);}
  public int outerNumber(){return outerN;}
  }
  
class PathPrimitive extends PathAux.Path{
  private PathPrimitive(String name) {this.name = name;}
  final String name;
  public boolean isPrimitive() {return true;}
  public static PathAux.Path _parsePrimitive(List<String> names){
    if(names.size()!=1){return null;}
    String name=names.get(0);
    return _parsePrimitive(name);
    }
  public static PathAux.Path _parsePrimitive(String name){
    if(name.equals(_Void.name)){return _Void;}
    if(name.equals(_Any.name)){return _Any;}
    if(name.equals(_Library.name)){return _Library;}
    return null;
    }
  static final PathPrimitive _Void = new PathPrimitive("Void");
  static final PathPrimitive _Any = new PathPrimitive("Any");
  static final PathPrimitive _Library = new PathPrimitive("Library");
  }
  
class PathSugar extends PathAux.Path{
  public static PathAux.Path _instance(List<String> names){
    for(String s:names){
      if(!PathAux.isValidClassName(s)){
        return null;    
        }
      }
    assert (names=Collections.unmodifiableList(names))!=null;//ok for testing efficiency 
    return new PathSugar(names);
    }
  private PathSugar(List<String> names) {
    this.names = names;
    }
  final List<String> names;
  }
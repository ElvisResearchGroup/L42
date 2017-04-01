package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ast.Ast.Atom;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.NormType;
import ast.Ast.Op;
import ast.Ast.Path;
import sugarVisitors.Desugar;
import tools.Assertions;
import tools.StringBuilders;
//import ast.Ast.Doc;
//import ast.Ast.MethodSelector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import platformSpecific.javaTranslation.Resources;

public interface Ast {

 public interface Atom {
 }

 @Value
 public class Parameters {
  Optional<Expression> e;
  List<String> xs;
  List<Expression> es;
 }


 public interface VarDec {
  <T> T match(Function<VarDecXE, T> xe, Function<VarDecE, T> e, Function<VarDecCE, T> ce);
 }

 @Value
 @Wither
 public class VarDecXE implements VarDec {
  boolean isVar;
  Optional<Type> t;
  String x;
  Expression inner;

  public <T> T match(Function<VarDecXE, T> xe, Function<VarDecE, T> e, Function<VarDecCE, T> ce) {
   return xe.apply(this);
  }
 }

 @Value
 public class VarDecE implements VarDec {
  Expression inner;

  public <T> T match(Function<VarDecXE, T> xe, Function<VarDecE, T> e, Function<VarDecCE, T> ce) {
   return e.apply(this);
  }
 }

 @Value
 public class VarDecCE implements VarDec {
  Expression.ClassB.NestedClass inner;

  public <T> T match(Function<VarDecXE, T> xe, Function<VarDecE, T> e, Function<VarDecCE, T> ce) {
   return ce.apply(this);
  }
 }


 public interface Header {
  <T> T match(Function<ConcreteHeader, T> concreteH, Function<TraitHeader, T> traitH,
    Function<InterfaceHeader, T> interfH);
 }

 @Value
 @Wither
 @EqualsAndHashCode(exclude = "p")
 @ToString(exclude = "p")
 public class ConcreteHeader implements Header, Expression.HasPos {
  Mdf mdf;
  String name;
  List<FieldDec> fs;
  Position p;

  public <T> T match(Function<ConcreteHeader, T> concreteH, Function<TraitHeader, T> traitH,
    Function<InterfaceHeader, T> interfH) {
   return concreteH.apply(this);
  }
 }

 @Value
 public class TraitHeader implements Header {
  public <T> T match(Function<ConcreteHeader, T> concreteH, Function<TraitHeader, T> traitH,
    Function<InterfaceHeader, T> interfH) {
   return traitH.apply(this);
  }
 }

 @Value
 public class InterfaceHeader implements Header {
  public <T> T match(Function<ConcreteHeader, T> concreteH, Function<TraitHeader, T> traitH,
    Function<InterfaceHeader, T> interfH) {
   return interfH.apply(this);
  }
 }

 @Value
 @Wither
 public class FieldDec {
  boolean isVar;
  Type t;
  String name;
  Doc doc;
 }

 public interface Type {
      <T> T match(Function<NormType, T> normType, Function<HistoricType, T> hType);
      default ast.Ast.NormType getNT() {
        assert this instanceof ast.Ast.NormType : this;
        return (ast.Ast.NormType) this;
        }
      Doc getDoc();
      Type withDoc(Doc doc);
   }

 @Value
 @Wither
 public class NormType implements Type {
  Mdf mdf;
  Path path;
  Doc doc;
  public static final NormType mutThis0=new NormType(Mdf.Mutable,Path.outer(0),Doc.empty());
  public static final NormType immThis0=new NormType(Mdf.Immutable,Path.outer(0),Doc.empty());
  public static final NormType immVoid=new NormType(Mdf.Immutable,Path.Void(),Doc.empty());
  public static final NormType immLibrary=new NormType(Mdf.Immutable,Path.Library(),Doc.empty());
  public static final NormType immAny=new NormType(Mdf.Immutable,Path.Any(),Doc.empty());
  public static final NormType classAny=new NormType(Mdf.Class,Path.Any(),Doc.empty());
  public String toString() {
   return mdf.name() + "[" + this.path.toString()+"]";
  }

  public <T> T match(Function<NormType, T> normType, Function<HistoricType, T> hType) {
   return normType.apply(this);
  }
 }

 @Value
 @Wither
 public class MethodSelectorX {
  MethodSelector ms;
  String x;
 }

 @Value
 @Wither
 public class HistoricType implements Type {
  Path path;
  List<MethodSelectorX> selectors;
  Doc doc;
  public <T> T match(Function<NormType, T> normType, Function<HistoricType, T> hType) {
   return hType.apply(this);
  }
 }

 @Value
 public class FreeType implements Type {
  public <T> T match(Function<NormType, T> normType, Function<HistoricType, T> hType) {
   throw tools.Assertions.codeNotReachable();
  }
  public Doc getDoc(){return Doc.empty();}
  public Type withDoc(Doc doc){return this;}
 }

 @Value
 @Wither
 public class MethodSelector {
  String name;
  long uniqueNum;
  public boolean isUnique(){return uniqueNum!=-1;}
  List<String> names;
  public static MethodSelector of(String name,List<String>names){
    int index=name.lastIndexOf("_$_");
    long uniqueNum=C.sToNumber(name, index);
    if (uniqueNum!=-1){name=name.substring(0,index);}
    names=java.util.Collections.unmodifiableList(names);
    MethodSelector res=new MethodSelector(name,uniqueNum,names);
    assert res.invariant();
    return res;
    }
  public boolean isOperator(){
    return this.name.startsWith("#");//for now, to improve later
    }
  public boolean invariant(){
    // not good enought, it can also be empty or operator
    // assert checkX(name,true);
    assert !name.contains("\t"):
      name;
    if(!( name.isEmpty() && this.uniqueNum==-1)){
      assert checkX(Desugar.desugarName(name),true);
      }
    for(String n:names){assert checkX(n,false);}
    return true;
    }

  public String toSrcEquivalent() {
   String result = "ast.Ast.MethodSelector.of(\"" + nameToS() + "\",java.util.Arrays.asList(";
   result += String.join(",", tools.Map.of(ni -> "\"" + ni + "\"", names));
   return result + "))";
  }
  public String nameToS(){
    if(uniqueNum==-1){return name;}
    return name+"_$_"+uniqueNum;
    }
  public String toString() {
   String nameU=nameToS();
   if (nameU.isEmpty() && names.isEmpty()) {
    return "()";
   }
   if (names.isEmpty()) {
    return nameU + "()";
   }
   StringBuilder result = new StringBuilder();
   result.append(nameU + "(");
   tools.StringBuilders.formatSequence(result, names.iterator(), ",", result::append);
   result.append(")");
   return result.toString();
  }

  public static MethodSelector parse(String s) {
   if (s.equals("()")) {
    return MethodSelector.of(Desugar.desugarName(""), Collections.emptyList());
   }
   String name = s;
   List<String> xs = new ArrayList<String>();
   assert!s.isEmpty();
   char last = s.charAt(s.length() - 1);
   if (last != ')') {
    throw new Resources.Error("InvalidSelector: " + s);
   }
   int i = s.indexOf('(');
   if (i == -1) {
    throw new Resources.Error("InvalidSelector: " + s);
   }
   name = s.substring(0, i);
   String parenthesis = s.substring(i + 1, s.length() - 1).trim();
   if (!parenthesis.isEmpty()) {
    String[] names = parenthesis.split(",");// single representation
              // required
    for (String si : names) {
     if (!checkX(si, false)) {
      throw new Resources.Error("InvalidSelector: " + s);
     }
     if(xs.contains(si)){
       throw new Resources.Error("InvalidSelector: " + s+" a parameter is repeted: "+si);
     }
     xs.add(si);
    }
   }
   name = Desugar.desugarName(name);
   if (!checkX(name, true)) {
    throw new Resources.Error("InvalidSelector: " + s);
   }
   return MethodSelector.of(name, xs);
  }

  public static boolean checkX(String s, boolean allowHash) {
   if (s.isEmpty()) {return false;}
   char c0 = s.charAt(0);
   if (allowHash && c0 == '#') {
    if (s.length() == 1) {return false;}
    char c1 = s.charAt(1);
    if (c1 == '#') {return false;}
    //return checkX(s.substring(1), allowHash);
   }
   for (char c : s.toCharArray()) {
    if (allowHash && c == '#') {continue;}
     if (ast.PathAux.isValidPathChar(c)) {continue;}
       return false;
   }
   return c0 == '_' ||c0 == '#' || (c0 >= 'a' && c0 <= 'z');
  }
 }

 @Value
 @Wither
 public class MethodType {
  boolean refine;
  Mdf mdf;
  List<Type> ts;
  Type returnType;
  List<Type> exceptions;
  @Override public String toString(){
    String res=
      this.mdf+":"+ts.stream().map(e->e.toString()).collect(Collectors.joining(","))
      +"->"+returnType;
    if (!exceptions.isEmpty()){res=res+" exceptions:"+exceptions.stream().map(e->e.toString()).collect(Collectors.joining(","));}   
    return res;
    }
 }
 @Value @Wither
 public static class C{
   String inner;
   long uniqueNum;
   public boolean isUnique(){return uniqueNum!=-1;}
   public C(String inner,long uniqueNum){
     this.inner=inner;this.uniqueNum=uniqueNum;
     assert PathAux.isValidClassName(inner);
     }
   public static long sToNumber(String name,int index){
     if(index==-1){return -1L;}
     String numS=name.substring(index+3,name.length());
     try{
       long num=Long.parseLong(numS);
       return num;
       }
     catch(NumberFormatException nfe){return -1L;}
     }
   public static C of(String name){
     int index=name.lastIndexOf("_$_");
     long uniqueNum=sToNumber(name,index);
     if(uniqueNum==-1){return new C(name,-1);}
     return new C(name.substring(0,index),uniqueNum);       
     }
   public String toString(){
     if(uniqueNum==-1){return inner;}
     return inner+"_$_"+uniqueNum;
     }
   }
 
 //-------------------
 public static abstract class Path {
 public static Path sugarParse(List<String> rowData){
   Path res=PathSugar._instance(rowData);
   if (res!=null){return res;}
   return parse(rowData);
   }
 public static Path sugarParse(String path) {
   List<String> rowData = Arrays.asList(path.split("\\."));
   return sugarParse(rowData);
   }
 public static Path parse(List<String> rowData){
   Path res=PathPrimitive._parsePrimitive(rowData);
   if (res!=null){return res;}
   res=PathCore._parsePathCode(rowData);
   if (res!=null){return res;}
   throw Assertions.codeNotReachable("InvalidPath: " + rowData);
   //this does not accept the sugarPath
   }
 public static Path parse(String path) {
   List<String> rowData = Arrays.asList(path.split("\\."));
   return parse(rowData);
   }
 public static Path outer(int n, List<C> cs){
   return PathCore.instance(n,cs);
   }
 public static Path outer(int n){
   return PathCore.instance(n,Collections.emptyList());
   }
 public static Path Void() {return PathPrimitive._Void;}
 public static Path Any() {return  PathPrimitive._Any;}
 public static Path Library() {return PathPrimitive._Library;}

 public NormType toImmNT(){return new NormType(Mdf.Immutable,this,Doc.empty());}
   public boolean isPrimitive() {return false;}
   public boolean isCore() { return false; }
   public String toString() { return sugarVisitors.ToFormattedText.of(this);}

   public Path popC(){throw Assertions.codeNotReachable("path.pocC on not core:"+this);}
   public Path pushC(C c){throw Assertions.codeNotReachable("path.pushC on not core:"+this);}
   public List<C> getCBar(){throw Assertions.codeNotReachable("path.getCBar on not core:"+this);}
   public Path setNewOuter(int n){throw Assertions.codeNotReachable("path.setNewOuter on not core:"+this);}
   public int outerNumber(){throw Assertions.codeNotReachable("path.outerNumber on not core:"+this);}
   public List<C> sugarNames(){throw Assertions.codeNotReachable("path.outerNumber on not core:"+this);}
   
 }  
 //-----------------------------
 
 @Value
 @EqualsAndHashCode(exclude = "p") /*to string manually defined so @ToString(exclude = "p") not needed*/
 public static class Doc implements Expression.HasPos{
  boolean multiline;
  String s;
  List<Object> annotations;
  List<String> parameters;
  Position p;

  public List<Path> getPaths() {
   List<Path> result = new ArrayList<>();
   for (Object o : annotations) {
    if (o instanceof Path) {
     result.add((Path) o);
    }
   }
   return result;
  }
  public String _getParameterFor(Object annotation){
    int i=annotations.indexOf(annotation);
    if(i==-1){return null;}
    return parameters.get(i);
    }
  public Doc withNewlineTerminator(){
    if(s.endsWith("\n")){return this;}
    return this.withS(s+"\n");
    }

  /*public boolean isPrivate() {
   if (this.annotations.contains("private")) {
    return true;
   }
   // if(this.toString().startsWith("@private")){return true;}
   return false;
  }*/
        //public static Doc getPrivate(){return privateInstance;}
        //private static final Doc privateInstance=Doc.factory(true,"@private");
  public static Doc factory(Path single) {
   return new Doc(true,"%s\n", Collections.singletonList((Object) single), Collections.singletonList(""),Position.noInfo);
  }

  public static Doc factory(boolean multiline,String s) {
    return factory(multiline, s,Position.noInfo );
  }
  public static Doc factory(boolean multiline,String s, Position pos) {
   if (!multiline & !s.endsWith("\n")) { s += "\n";}
   List<Object> annotations = new ArrayList<>();
            List<String> parameters = new ArrayList<>();
   StringBuilder sb = new StringBuilder();
   for (int i = 0; i < s.length(); i++) {
    char ci = s.charAt(i);
    if (ci == '%') {
     sb.append('%');
     sb.append('%');//NOTE: this is to allow String.format in toString
     continue;
    }
    if (ci != '@') {
     sb.append(ci);
     continue;
    } else {// ci=='@'
     char next = '\n';
     if (i + 1 < s.length()) {
      next = s.charAt(i + 1);
     }
     if (next == '.' || PathAux.isValidPathChar(next)) {
      sb.append("%s");
      i = readAnnotation(s, i + 1, annotations);
                     readParameter(s, i + 1, parameters);
     } else {
      throw Assertions.codeNotReachable("invalid use of @ in |" + next + "| " + s);
     } // if(!PathAux.isValidPathStart(next)){sb.append(ci);continue;}
    }
   }
   return new Doc(multiline,sb.toString(), annotations,parameters,pos);
  }

  private static final Doc empty = new Doc(true,"", Collections.emptyList(), Collections.emptyList(),Position.noInfo);

  public static Doc empty() {
   return empty;
  }
  public Doc withAnnotations(final List<Object> ann) { //customized to make the string change not break the parameters structure
    assert ann.size()==this.annotations.size();
    assert ann.stream().allMatch(Doc::isValidAnnotation)://
      "";
    Doc tmp = new Doc(this.multiline, this.s, ann, this.parameters, this.p);
    assert Doc.factory(this.multiline,tmp.toStringWeak()).equals(tmp)://
      "";
    return tmp;
  }
  public static boolean isValidAnnotation(Object o){
    if (o instanceof Path){return true;}
    if (!(o instanceof String)){return false;}
    String s=(String)o;
    if(s.isEmpty()){return true;}
    if (PathAux.isValidPathStart(s.charAt(0))){return false;}
    for(char c : s.toCharArray()) {
      if(c=='.'){continue;}
      if(!PathAux.isValidPathChar(c)){return false;}
      }
    return true;
    }
  public Doc toMultiline(){
    Doc tmp=new Doc(true, s, this.annotations, this.parameters, this.p);
    assert Doc.factory(true,tmp.toStringWeak()).equals(tmp);
    return tmp;
    }
  public Doc withS(final String s) { //customized to make the string change not break the parameters structure
    if (this.s==s || this.s.equals(s)){return this;}
    Doc tmp=new Doc(this.multiline, s, this.annotations, this.parameters, this.p);
    return Doc.factory(this.multiline,tmp.toStringWeak());
    }
     public String _getParameterForPlugin(){return _getParameterFor("plugin");}
        public String _getParameterForPluginPart(){return _getParameterFor("pluginPart");}
        
        public String toStringWeak() {
          List<Object> paths = new ArrayList<>();
          for (Object pi : this.annotations) {
            if (pi instanceof Path) { 
              paths.add("@" + sugarVisitors.ToFormattedText.of((Path) pi));
            } else {
              paths.add("@" + (String) pi);  
            }
          }
          return String.format(this.s, paths.toArray());
          }
        public String toString() {
          String text=toStringWeak();
          assert Doc.factory(this.multiline, text).equals(this)://
            "";
          return text;
        }
      public String toCodeFormattedString() {
        String text=toString();
        if(text.isEmpty()){return text;}
        if(this.multiline){return "/*"+text+"*/";}
        assert text.endsWith("\n"):"|"+text+"|";
        String[] splitted=text.substring(0,text.length()-1).split("\n",-1);//on its line for ease of testing//This was a bad move, javaSplit, you despicable bastard
        StringBuffer res=new StringBuffer();
        {int i=-1;for(String s:splitted){i+=1;
          if(s.isEmpty()&& i==0){continue;}
          res.append("//");
          res.append(s);
          res.append("\n");
        }}
        return res.toString();
        }

  public boolean isEmpty() {
   return this.s.isEmpty();
  }

  public Doc sum(Doc that) {
   
   //be carefull, a more optimized implementation would mess up annotations as in 
   // /*@a b*/ +/* c*/ would create annotations=[" b"] while
   // /*@a b c*/ would create annotations=[" b c"]
   String tosThis=this.toString();
   String tosThat=that.toString();
   return Doc.factory(true, tosThis+tosThat,this.p.sum(that.p));
  }
  public Doc formatNewLinesAsList() {
    String newS=this.s.trim();
    newS=newS.replace("\n", ", ");
    newS="["+newS+"]\n";
      return this.withS(newS);
    }

  private static int readAnnotation(String s, int start, List<Object> paths) {
    boolean isPath=PathAux.isValidPathStart(s.charAt(start));        
    StringBuilder sb = new StringBuilder();
    for (int i = start; i < s.length(); i++) {
      char ci = s.charAt(i);
      if (PathAux.isValidPathChar(ci)) {sb.append(ci);}
      else if (ci == '.' && isPath && i+1<s.length()&& PathAux.isValidPathStart(s.charAt(i+1))) {sb.append(ci);}
      else if (ci == '.' && !isPath) {sb.append(ci);}
      else {break;}
      }
    String res = sb.toString();
    if (isPath) {paths.add(Path.sugarParse(res));}
    else {paths.add(res);}
    return start + res.length() - 1;  
    }

  private static void readParameter(String s, int start, List<String> parameters) {
    int i=s.indexOf('@',start);
    if (i==-1){i=s.length();}
    String par=s.substring(start, i);
    while(par.endsWith("\n")){
      par=par.substring(0, par.length()-1);
      }
    parameters.add(par);
    }
  }

 public static enum SignalKind {
  Error("error"), Exception("exception"), Return("return");
  public final String content;

  SignalKind(String content) {
   this.content = content;
  }

  public static SignalKind fromString(String s) {
   for (SignalKind sk : SignalKind.values()) {
    if (sk.content.equals(s))
     return sk;
   }
   throw tools.Assertions.codeNotReachable();
  }
 }

 public static enum Mdf {
  Immutable(""), Mutable("mut"), Readable("read"), Lent("lent"), Capsule("capsule"), Class("class"),
  ImmutableFwd("fwd"),ImmutablePFwd("%fwd"),MutableFwd("fwd mut"),MutablePFwd("%fwd mut");
  public final String inner;

  Mdf(String inner) {
   this.inner = inner;
  }

  public static Mdf fromString(String s) {
   for (Mdf mdf : Mdf.values()) {
    if (mdf.inner.equals(s))
     return mdf;
   }
   throw tools.Assertions.codeNotReachable();
  }
  public static List<Mdf> muts=Arrays.asList(Mdf.Mutable,Mdf.MutablePFwd,Mdf.MutableFwd);
  public static List<Mdf> imms=Arrays.asList(Mdf.Immutable,Mdf.ImmutablePFwd,Mdf.ImmutableFwd);
 }

 public static enum OpKind {
  Unary, BoolOp, RelationalOp, DataOp, EqOp
 }

 public static enum Op {
  Tilde("~", OpKind.Unary, true, true,false),//
  Bang("!", OpKind.Unary, true, true,false),//

  And("&", OpKind.BoolOp, true,true,false),//
  Or("|", OpKind.BoolOp, true, true,false),//

  LTEqual("<=", OpKind.RelationalOp, false, true,false),//
  GTEqual(">=", OpKind.RelationalOp, true, true,false),//
  LT("<", OpKind.RelationalOp, false, true,false),//
  GT(">",OpKind.RelationalOp, true,true,false),//
  LTLT("<<", OpKind.RelationalOp, false, false,false),//
  GTGT(">>",OpKind.RelationalOp, true, false,false),//
  LTLTEqual("<<=",OpKind.RelationalOp,false,true,false),//
  GTGTEqual(">>=",OpKind.RelationalOp,true,true,false),//
  EqualEqual("==", OpKind.RelationalOp, true, false,false),//

  BangLTEqual("!<=", OpKind.RelationalOp, false, true,true),//
  BangGTEqual("!>=", OpKind.RelationalOp, true, true,true),//
  BangLT("!<", OpKind.RelationalOp, false, true,true),//
  BangGT("!>",OpKind.RelationalOp, true,true,true),//
  BangLTLT("!<<", OpKind.RelationalOp, false, false,true),//
  BangGTGT("!>>",OpKind.RelationalOp, true, false,true),//
  BangLTLTEqual("!<<=",OpKind.RelationalOp,false,true,true),//
  BangGTGTEqual("!>>=",OpKind.RelationalOp,true,true,true),//
  BangEqual("!=",OpKind.RelationalOp, true,true,true),//
  
  
  Plus("+", OpKind.DataOp, true, true,false),//
  Minus("-", OpKind.DataOp, true,true,false),//
  Times("*", OpKind.DataOp, true, true,false),//
  Divide("/", OpKind.DataOp,true, true,false),//
  PlusPlus("++",OpKind.DataOp, true, false,false),//
  MinusMinus("--",OpKind.DataOp, true, false,false),//
  TimesTimes("**",OpKind.DataOp, true,false,false),//
  BabelFishL("<><",OpKind.DataOp, true,false,false),//
  BabelFishR("><>",OpKind.DataOp, false,false,false),//
  
  PlusEqual("+=", OpKind.EqOp,true, true,false),//
  MinusEqual("-=",OpKind.EqOp, true,true,false),//
  TimesEqual("*=",OpKind.EqOp,true,true,false),//
  DivideEqual("/=",OpKind.EqOp,true,true,false),//
  AndEqual("&=",OpKind.EqOp,true,true,false),//
  OrEqual("|=",OpKind.EqOp,true,true,false),//
  PlusPlusEqual("++=",OpKind.EqOp,true,true,false),//
  MinusMinusEqual("--=",OpKind.EqOp,true,true,false),//
  TimesTimesEqual("**=",OpKind.EqOp,true,true,false),//
  ColonEqual(":=",OpKind.EqOp,true,true,false),//
  BabelFishLEqual("<><=",OpKind.EqOp, true,false,false),//
  BabelFishREqual("><>=",OpKind.EqOp, true,false,false);//

  public final String inner;
  public final OpKind kind;
  public final boolean normalized;// false for <<,<, <=,<<=,><> etc
  public final boolean leftAssociative;// false for ++ << >> ** ==
  public final boolean negated;//true for all starting with !
  
  Op(String inner, OpKind kind, boolean normalized, boolean leftAssociative, boolean negated) {
   this.inner = inner;
   this.kind = kind;
   this.normalized = normalized;
   this.leftAssociative = leftAssociative;
   this.negated=negated;
  }

  public static Op fromString(String s) {
   for (Op op : Op.values()) {
    if (op.inner.equals(s))
     return op;
   }
   throw tools.Assertions.codeNotReachable();
  }
  public Op nonNegatedVersion(){
    if (!this.negated){return this;}
    String ops=this.inner.substring(1);
    if(ops.equals("=")){ops="==";}
    return Op.fromString(ops);
    }
  public Op normalizedVersion(){
    if(this.normalized){return this;}
    String ops=inner.replace('<',' ').replace('>','<').replace(' ','>');
    return Op.fromString(ops);
    }
 }

 public static enum Stage {
  None(""), Less("##less"),
  ToIterate("##toIterateTemp"),
  // Meta("##meta"),
  Plus("##plus"), Star("##star");
  // Needed("##needed"),
  // Needable("##needable");
  public final String inner;

  Stage(String inner) {
   this.inner = inner;
  }

  public static Stage fromString(String s) {
   for (Stage st : Stage.values()) {
    if (st.inner.equals(s))
     return st;
   }
   throw tools.Assertions.codeNotReachable();
  }
 }

 /*public static enum Ph {
  None, Ph, Partial
 }*/

 public static @Wither @Value class Position {
  public static final Position noInfo = new Position(null, Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2, 0, 0,null);
  public Position sum(Position that){
    if (this==noInfo){return that;}
    if (that==noInfo || that==null){return this;}
    if(this._next==null){return this.with_next(that);}
    return this.with_next(this._next.sum(that));
    }
  String file;
  int line1;
  int pos1;
  int line2;
  int pos2;
  Position _next;

  public String toString() {
   int line1 = this.line1 - 1;
   int line2 = this.line2 - 1;
   String res = "";
   if (line1 == line2) {
    res = "line:" + line1 + ", pos:" + pos1 + "--" + pos2;
   } else {
    res = "from line:" + line1 + "(pos:" + pos1 + ") to line:" + line2 + "(pos:" + pos2 + ")";
   }
   if (file == null) {
    return "fileUnknown; " + res;
   }
   String fileName = null;
   // if(file!=null){fileName="..."+file.substring(file.lastIndexOf("\\"));}
   int pos = file.lastIndexOf("\\");
   if (pos != -1) {
    pos = file.substring(0, pos).lastIndexOf("\\");
   }
   fileName = "..." + ((pos == -1) ? file : file.substring(pos));
   return "file: " + fileName + "; " + res;
  }
 }

 public static interface HasPos {
  Position getP();
 }
 public static interface HasReceiver extends HasPos,Expression{
  Expression getReceiver();
  Expression withReceiver(Expression receiver);
 }
}

package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;

public interface Ast {
//Alpha
  public interface Atom {}
  @Value public class Parameters {Optional<Expression> e; List<String> xs; List<Expression>es;}
  @Value public class BlockContent {List<VarDec> decs; Optional<Catch> _catch; }
  public interface VarDec {
    <T> T match(Function<VarDecXE, T> xe,Function<VarDecE, T> e,Function<VarDecCE, T> ce);
    }
  @Value @Wither public class VarDecXE implements VarDec{boolean isVar;Optional<Type> t; String x; Expression inner;
  public<T> T match(Function<VarDecXE, T> xe,Function<VarDecE, T> e,Function<VarDecCE, T> ce){return xe.apply(this);} }
  @Value public class VarDecE implements VarDec{Expression inner;
  public<T> T match(Function<VarDecXE, T> xe,Function<VarDecE, T> e,Function<VarDecCE, T> ce){return e.apply(this);}}
  @Value public class VarDecCE implements VarDec{Expression.ClassB.NestedClass inner;
  public<T> T match(Function<VarDecXE, T> xe,Function<VarDecE, T> e,Function<VarDecCE, T> ce){return ce.apply(this);}}
  @Value @Wither public class Catch{SignalKind kind; String x; List<On> ons;Optional<Expression> _default;}
  @Value @Wither public class On{ List<Type> ts;Optional<Expression>_if; Expression inner;}

  public interface Header{
    <T> T match(Function<ConcreteHeader, T> concreteH,Function<TraitHeader, T> traitH,Function<InterfaceHeader, T> interfH);
    }
  @Value @Wither public class ConcreteHeader implements Header{Mdf mdf;String name;List<FieldDec> fs;
    public<T> T match(Function<ConcreteHeader, T> concreteH,Function<TraitHeader, T> traitH,Function<InterfaceHeader, T> interfH){return  concreteH.apply(this);}}
  @Value public class TraitHeader implements Header{
    public<T> T match(Function<ConcreteHeader, T> concreteH,Function<TraitHeader, T> traitH,Function<InterfaceHeader, T> interfH){return  traitH.apply(this);}}
  @Value public class InterfaceHeader implements Header{
    public<T> T match(Function<ConcreteHeader, T> concreteH,Function<TraitHeader, T> traitH,Function<InterfaceHeader, T> interfH){return  interfH.apply(this);}}
  @Value @Wither public class FieldDec{boolean isVar; Type t; String name;Doc doc;}
  public interface Type{
    <T> T match(Function<NormType, T> normType,Function<HistoricType, T> hType);
    }
  @Value @Wither public class NormType implements Type{Mdf mdf; Path path; Ph ph;
  public String toString(){return ""+mdf.name()+""+this.path.rowData+((ph==Ph.Ph)?"^":(ph==Ph.None)?"":"%");}
    public <T> T match(Function<NormType, T> normType,Function<HistoricType, T> hType){return normType.apply(this);}}
  @Value public class MethodSelectorX{MethodSelector ms;String x;}
  @Value @Wither public class HistoricType implements Type{Path path; List<MethodSelectorX> selectors;boolean forcePlaceholder;
    public <T> T match(Function<NormType, T> normType,Function<HistoricType, T> hType){return hType.apply(this);}}
  @Value public class FreeType implements Type{
    public <T> T match(Function<NormType, T> normType,Function<HistoricType, T> hType){throw tools.Assertions.codeNotReachable();}}

  @Value @Wither public class MethodSelector{String name;List<String>names;
    public String toString(){
      if(name.isEmpty() && names.isEmpty()){return "()";}
      if(names.isEmpty()){return name;}
      StringBuilder result=new StringBuilder();
      result.append(name+"(");
      tools.StringBuilders.formatSequence(result,names.iterator(),",",result::append);
      result.append(")");
      return result.toString();
    }}
  @Value @Wither public class MethodType{
  Doc docExceptions;Mdf mdf; List<Type> ts;List<Doc> tDocs; Type returnType; List<Path> exceptions;}

  @Value @ToString(exclude="n") public class Path implements Expression, ExpCore, Atom{
    int n;List<String> rowData;
    private Path(int n,List<String> rowData){
      rowData=Collections.unmodifiableList(rowData);
      this.n=n;this.rowData=rowData;
    }
    public Path(List<String> rowData){
      rowData=Collections.unmodifiableList(rowData);
      this.rowData=rowData;
      String start=this.rowData.get(0);
      boolean isCore=isOutern(start);
      if(isCore){
        start=start.substring("Outer".length());
        n=Integer.parseInt(start);
        }
      else{n=-1;}
    }
  @Override public <T> T accept(sugarVisitors.Visitor<T> v){return v.visit(this);}
  @Override public <T> T accept(coreVisitors.Visitor<T> v){return v.visit(this);}
  public boolean isPrimitive(){
    return
      this.equals(Path.Void()) ||
      this.equals(Path.Library())||
      this.equals(Path.Any());
    }
  public boolean isCore(){
    return n!=-1;
    }
  private static boolean isOutern(String start){
    if(!start.startsWith("Outer")){return false;}
    start=start.substring("Outer".length());
    for(char c: start.toCharArray()){
      if(!Character.isDigit(c))return false;
      }
    return true;
    }
  public Path popC(){
    assert outerNumber()==0;
    List<String> s=new ArrayList<String>(this.rowData);
    //s.remove(1);
    s.remove(s.size()-1);
    return new Path(n,s);
  }
  public Path pushC(String c){
    assert outerNumber()==0;
    List<String> s=new ArrayList<String>(this.rowData);
    s.add(c);
    return new Path(n,s);
  }
  public List<String> getCBar(){
    assert !this.isPrimitive();
    if(this.isCore()){
      return rowData.subList(1,rowData.size());
      }
    return rowData;
  }
  public Path setNewOuter(int n){
    assert isCore();
    List<String> s=new ArrayList<String>(this.rowData);
    s.set(0, "Outer"+n);
    return new Path(n,s);
  }
  public int outerNumber(){
    assert isCore();
    return this.n;
    }
  private static final Path _Outer0=new Path(0,Arrays.asList("Outer0"));
  private static final Path _Void=new Path(-1,Arrays.asList("Void"));
  private static final Path _Any=new Path(-1,Arrays.asList("Any"));
  private static final Path _Library=new Path(-1,Arrays.asList("Library"));
  public static Path outer(int n,List<String> cs){
    List<String> arr=new ArrayList<>();
    arr.add("Outer"+n);
    arr.addAll(cs);
    return new Path(n,arr);
  }
  public static Path outer(int n){
    assert n>=0;
    if(n==0){return _Outer0;}
    return new Path(n,Arrays.asList("Outer"+n));
    }
  public static Path Void(){return _Void;}
  public static Path Any(){return _Any;}
  public static Path Library(){return _Library;}
  public static Path parse(String path) {
    List<String> rowData=Arrays.asList(path.split("::"));
    for(String s:rowData){
      assert isValidPathStart(s.charAt(0));
      for(int i=1;i<s.length();i++){
        assert isValidPathChar(s.charAt(i));    
      }
    }
    return new Path(rowData);
  }}
  public static boolean isValidPathStart(char c){
    if(c=='%'){return true;}
    if(c=='$'){return true;}
    return Character.isUpperCase(c);
  }
  public static boolean isValidPathChar(char c){
      if(c=='%'){return true;}
      if(c=='$'){return true;}
      if(c=='_'){return true;}
      return 
              Character.isUpperCase(c) 
              ||
              Character.isLowerCase(c)
              ||
              Character.isDigit(c)
              ;
    }
  @Value @Wither public static class Doc{
    String s;
    List<Path>paths;
    public static Doc factory(String s){
      List<Path> paths=new ArrayList<>();
      StringBuilder sb=new StringBuilder();
      for(int i=0;i<s.length();i++){
        char ci=s.charAt(i);
        if(ci=='%'){sb.append('%');sb.append('%');continue;}
        if(ci!='@'){sb.append(ci);continue;}
        else{
          char next='\n';
          if(i+1<s.length()){next=s.charAt(i+1);}
          if(!Character.isUpperCase(next)){sb.append(ci);continue;}
          sb.append("%s");i=readPath(s,i+1,paths);
          }
        }
      return new Doc(sb.toString(),paths);
    }
    private static final Doc empty=new Doc("",Collections.emptyList());
    public static Doc empty(){return empty;}
    public String toString(){
      List<String> paths=new ArrayList<>();
      for(Path pi:this.paths){
        paths.add("@"+sugarVisitors.ToFormattedText.of(pi));
      }
      
      return String.format(this.s,paths.toArray());
    }
    public boolean isEmpty(){return this.s.isEmpty();}
    public Doc sum(Doc that){
      List<Path>ps=new ArrayList<>(this.paths);
      ps.addAll(that.paths);
      return new Doc(this.s+that.s,ps);
    }
    private static int readPath(String s,int start,List<Path>paths){
      StringBuilder sb=new StringBuilder();
      for(int i=start;i<s.length();i++){
        char ci=s.charAt(i);
        //missing % _ and $
        if(ci==':' ||Character.isAlphabetic(ci) || Character.isDigit(ci)){sb.append(ci);}
        else{
          paths.add(Path.parse(sb.toString()));
          return start+i;
        }
      }
      paths.add(Path.parse(sb.toString()));
      return s.length();
    }
  }
  public static enum SignalKind{
    Error("error"),
    Exception("exception"),
    Return("return");
    public final String content;
    SignalKind(String content) {this.content=content;}
    public static SignalKind fromString(String s){
      for(SignalKind sk:SignalKind.values()){if (sk.content.equals(s))return sk;}
      throw tools.Assertions.codeNotReachable();
      }
    }
    public static enum Mdf{
      Immutable(""),
      Mutable("mut"),
      Readable("read"),
      Lent("lent"),
      Capsule("capsule"),
      Type("type");
      public final String inner;
      Mdf(String inner) {this.inner=inner;}
      public static Mdf fromString(String s){
        for(Mdf mdf:Mdf.values()){if (mdf.inner.equals(s))return mdf;}
        throw tools.Assertions.codeNotReachable();
      }

  }
  public static enum OpKind{Unary,BoolOp,RelationalOp,DataOp,EqOp}
  public static enum Op{
    Tilde("~",OpKind.Unary),
    Bang("!",OpKind.Unary),
    And("&",OpKind.BoolOp),
    Or("|",OpKind.BoolOp),
    LTEqual("<=",OpKind.RelationalOp),
    GTEqual(">=",OpKind.RelationalOp),
    LT("<",OpKind.RelationalOp),
    GT(">",OpKind.RelationalOp),
    EqualEqual("==",OpKind.RelationalOp),
    BangEqual("!=",OpKind.RelationalOp),
    Plus("+",OpKind.DataOp),
    Minus("-",OpKind.DataOp),
    Times("*",OpKind.DataOp),
    Divide("/",OpKind.DataOp),
    LTLT("<<",OpKind.DataOp),
    GTGT(">>",OpKind.DataOp),
    PlusPlus("++",OpKind.DataOp),
    TimesTimes("**",OpKind.DataOp),
    PlusEqual("+=",OpKind.EqOp),
    MinusEqual("-=",OpKind.EqOp),
    TimesEqual("*=",OpKind.EqOp),
    DivideEqual("/=",OpKind.EqOp),
    AndEqual("&=",OpKind.EqOp),
    OrEqual("|=",OpKind.EqOp),
    LTLTEqual("<<=",OpKind.EqOp),
    GTGTEqual(">>=",OpKind.EqOp),
    PlusPlusEqual("++=",OpKind.EqOp),
    TimesTimesEqual("**=",OpKind.EqOp),
    ColonEqual(":=",OpKind.EqOp);

    public final String inner;
    public final OpKind kind;
    Op(String inner, OpKind kind) {
      this.inner=inner;
      this.kind=kind;
      }
    public static Op fromString(String s){
      for(Op op:Op.values()){if (op.inner.equals(s))return op;}
      throw tools.Assertions.codeNotReachable();
    }
  }
  public static enum Stage{
    None(""),
    Less("##less"),
    //Meta("##meta"),
    Plus("##plus"),
    Star("##star");
    //Needed("##needed"),
    //Needable("##needable");
    public final String inner;
    Stage(String inner) {this.inner=inner;}
    public static Stage fromString(String s){
      for(Stage st:Stage.values()){if (st.inner.equals(s))return st;}
      throw tools.Assertions.codeNotReachable();
    }
  }
  public static enum Ph{None,Ph,Partial}
  public static @Value class Position{
    public static final Position noInfo=new Position(null,Integer.MAX_VALUE/2, Integer.MAX_VALUE/2, 0,0);
    String file;
    int line1;int pos1;int line2;int pos2;
    public String toString(){
      int line1=this.line1-1;
      int line2=this.line2-1;
      String res="";
      if(line1==line2){res= "line:"+line1+", pos:"+pos1+"--"+pos2;}
      else{res= "from line:"+line1+"(pos:"+pos1+") to line:"+line2+"(pos:"+pos2+")";}
      if(file==null){return "fileUnknown; "+res;}
      String fileName=null;
      //if(file!=null){fileName="..."+file.substring(file.lastIndexOf("\\"));}
      int pos=file.lastIndexOf("\\");
      fileName = "..." + ((pos==-1)?file:file.substring(pos));
      return "file: "+fileName+"; "+res;
    }
    }
}

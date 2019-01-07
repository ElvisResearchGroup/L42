package ast;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import ast.Ast.Mdf;
import ast.L42F.Cn;
import lombok.Value;
import lombok.experimental.Wither;
import l42FVisitors.Visitor;
import l42FVisitors.BodyVisitor;
import java.io.Serializable;
import lombok.ToString;

public class L42F {
public static interface Kind{
  String className(CD cd);
  String boxedClassName(CD cd);
  }
public static enum SimpleKind implements Kind{
Class{
public String className(CD cd){return cd.dbgName();}
},
Interface{
public String className(CD cd){return cd.dbgName();}
},
NativeInt32{
public String className(CD cd){return "int";}
public String boxedClassName(CD cd){return "Integer";}
},
NativeString{
public String className(CD cd){return "String";}
},
NativeFloat32{
public String className(CD cd){return "float";}
public String boxedClassName(CD cd){return "Float";}
};
public String boxedClassName(CD cd){return className(cd);}
}
@Value @Wither public static class
CD implements Serializable{Kind kind; int cn; List<String>dbgName; List<Integer> cns; List<M> ms;
  public String dbgName() {
    StringBuilder b=new StringBuilder();
    tools.StringBuilders.formatSequence(b, dbgName.iterator(), "£C",s->b.append(s));
    b.append("£Id"+cn);
    return b.toString();
    }
  public String className(){return kind.className(this);}
  public String boxedClassName(){return kind.boxedClassName(this);}
  public String l42ClassName(){return dbgName();}
  }

@Value @Wither public static class
TX implements Serializable{T t; String x;}

@Value @Wither public static class
M implements Serializable{
  boolean refine; T returnType;
  Ast.MethodSelector selector; List<TX> txs; Body body;
  }
public static interface Body extends Serializable{
  <T> T accept(BodyVisitor<T> v);
  }
public static enum SimpleBody implements Body{
  Empty{public <T> T accept(BodyVisitor<T> v){return v.visitEmpty(this);}},
  Setter{public <T> T accept(BodyVisitor<T> v){return v.visitSetter(this);}},
  Getter{public <T> T accept(BodyVisitor<T> v){return v.visitGetter(this);}},
  New{public <T> T accept(BodyVisitor<T> v){return v.visitNew(this);}},
  NewWithFwd{public <T> T accept(BodyVisitor<T> v){return v.visitNewWithFwd(this);}},
  NewFwd{public <T> T accept(BodyVisitor<T> v){return v.visitNewFwd(this);}},
  NativeIntSum{public <T> T accept(BodyVisitor<T> v){return v.visitNativeIntSum(this);}};
  }
public static interface E extends Body{
  <T> T accept(Visitor<T> v);
  default <T> T accept(BodyVisitor<T> v){return v.visitE(this);}
  }

@Value @Wither public static class
Block implements E{ List<D> ds; List<K> ks; E e; T type;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
X implements E{String inner;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Cn implements E{int inner;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}
  public static final Cn cnAny=new Cn(1);
  public static final Cn cnVoid=new Cn(2);
  public static final Cn cnLibrary=new Cn(3);
  public static final Cn cnResource=new Cn(4);
  public static final Cn cnFwd = new Cn(5);
  }

@Value @Wither public static class
_void implements E{
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}
@Value @Wither public static class
Null implements E{
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}
@Value @Wither public static class
Unreachable implements E{
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}


@Value @Wither public static class
BreakLoop implements E{
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Throw implements E{Ast.SignalKind kind; String x; T tContext;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Loop implements E{E inner;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Call implements E{
  int cn; Ast.MethodSelector ms;
  List<String> xs;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither /*already since transient @EqualsAndHashCode(exclude = "ui")*/ @ToString(exclude = "ui")public static class
Use implements E{
  Ast.Doc doc;
  transient platformSpecific.fakeInternet.PluginWithPart.UsingInfo ui;
  Ast.MethodSelector ms;
  List<String> xs; E inner;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
If implements E{
  String condition; E then; E _else;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Update implements E{String x1; String x2;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
Cast implements E{T t; String x;
  @Override public <T> T accept(Visitor<T> v){return v.visit(this);}}

@Value @Wither public static class
K  implements Serializable{Ast.SignalKind kind; T t; String x; E e;}

@Value @Wither public static class
D  implements Serializable{boolean var; T t; String x; E e;}
@Value @Wither public static class
T  implements Serializable{Ast.Mdf mdf; int cn;
  public static final T immAny=new T(Mdf.Immutable,Cn.cnAny.inner);
  public static final T immVoid=new T(Mdf.Immutable,Cn.cnVoid.inner);
  public static final T immLibrary=new T(Mdf.Immutable,Cn.cnLibrary.inner);
  public static final T classAny=new T(Mdf.Class,Cn.cnAny.inner);
  }
}

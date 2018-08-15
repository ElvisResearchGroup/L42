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
import l42FVisitors.JVisitor;

public class MiniJ {

@Value @Wither public static class
CD{boolean isInterface; String cn; List<String> cns; List<M> ms;}

@Value @Wither public static class
M{boolean isStatic; String retT;String name;List<String>ts;List<String>xs; S body;}

public static interface S{
  <V> V accept(JVisitor<V> v);
  }
public static interface E extends S{}

@Value @Wither public static class
VarDec implements S{
  String cn; String x;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
VarAss implements S{
  String x; E e;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
If implements S{
  String cond; S then; S _else;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
IfTypeCase implements S{
  boolean positive;
  String x0;String x1; String cn; S then;S _else;
  //if(x0.inner() instanceof cn){cn x1=(cn)x0.inner(); [then]}
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
WhileTrue implements S{
 String label;
 S s;
 public <V> V accept(JVisitor<V> v){return v.visit(this);}
 }
@Value @Wither public static class
Break implements S{
  String label;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Return implements S{
  E e;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Throw implements S{//throw new cn(x) or throw x if cn==null;
  Ast.SignalKind k; String x;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Try implements S{
 B b; List<K> ks;
 public <V> V accept(JVisitor<V> v){return v.visit(this);}
 }
@Value @Wither public static class
K{ast.Ast.SignalKind t; String x; B b;}

@Value @Wither public static class
MCall implements E{
  String cn; String mName; List<String> xs;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
UseCall implements E{
  Ast.Doc doc;
  platformSpecific.fakeInternet.PluginWithPart.UsingInfo ui;
  String mName; List<String> xs;
  S inner;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }

@Value @Wither public static class
X implements E{
  String inner;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
RawJ implements E{
  String inner;
  public RawJ(String inner){
    assert coherent(inner);
    this.inner=inner;
    }
  static boolean coherent(String inner){
    int pos=inner.indexOf("£M");
    if(pos==-1 ||pos==0){return true;}
    inner=inner.substring(0,pos);
    return inner.endsWith(" ") || inner.endsWith(".") || inner.endsWith("\n");
    }
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Cast implements E{
  String cn;
  String x;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Null implements E{
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }

@Value @Wither public static class
B implements S{
  String label; List<S> ss;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
}
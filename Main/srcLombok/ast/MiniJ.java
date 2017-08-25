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
import l42FVisitors.JVisitor;
public class MiniJ {
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
  String cond; B then; B _else;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
IfTypeCase implements S{
  String x0;String x1; String cn; B then; B _else;
  //if(x0 instanceof cn){cn x1=(cn)x0;..}else{..}
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
WhileTrue implements S{
 B b;
 public <V> V accept(JVisitor<V> v){return v.visit(this);}
 }
@Value @Wither public static class
Break implements S{
  String label;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Return implements S{
  String x;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
@Value @Wither public static class
Throw implements S{
  String cn; String x;
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
  List<String>xs;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }

@Value @Wither public static class
X implements E{
  String inner;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }

@Value @Wither public static class
B implements S{
  String label; List<S> ss;
  public <V> V accept(JVisitor<V> v){return v.visit(this);}
  }
}
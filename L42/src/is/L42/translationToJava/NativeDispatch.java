package is.L42.translationToJava;

import static is.L42.tools.General.bug;
import static is.L42.tools.General.range;
import static is.L42.tools.General.todo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


import is.L42.generated.Core;
import is.L42.generated.Core.E;

import static is.L42.translationToJava.TrustedKind.*;

public class NativeDispatch {
  public static String nativeCode(String nativeKind, String nativeUrl, List<String> xs, E e) {
    if(!nativeUrl.startsWith("trusted:")){return untrusted(nativeKind,nativeUrl,xs,e);}
    String nativeOp=nativeUrl.substring("trusted:".length());
    var k=TrustedKind.fromString(nativeKind);
    var op=TrustedOp.fromString(nativeOp);
    return op.code.get(k).apply(xs,e);
    }
  public static String nativeFactory(String nativeKind, List<String> xs) {
    var k=TrustedKind.fromString(nativeKind);
    return k.factory(xs);
    }
  public static String untrusted(String nativeKind, String nativeUrl, List<String> xs, E e) {
    //String[]parts=nativeUrl.split(":");
    //examples of possible nativeUrl strings: 
    //slaveName{}\n#1.foo(#2)
    //slaveName{}\nReadFile.fileName(#1)
    //slaveName{
    //  timeLimit: xxx //all entries are optional
    //  memoryLimit: xxx
    //  classPath: xxx //if present will be the only path visible, thus no shared .class 
    //  nativePath: xxx//will list *.so and *.dll stuff
    //  }
    //ReadFile.fileName(#1)
    
    //IF a slave with slaveName is already active (even if with different parameters), it is just
    //reusing the current slave instance
    //IF a slave with slaveName is not active (either never activated or died), it is creating
    //and caching a new slave
    
    //anything in nativeUrl after first occurrence of the token "}\n" can be turned in a lambda
    String toLambda="()->"+nativeUrl.substring(nativeUrl.indexOf("}\n")+2); 
    for(int i:range(xs)){//it might be just this simple
      toLambda=toLambda.replaceAll("#"+i, xs.get(i));
      }
    String slaveName=nativeUrl.substring(0,nativeUrl.indexOf("{")).trim();
    int timeLimit=100;//seconds, please, show me how to set it up
    int MemoryLimit=100;//megabites, please, show me how to set it up
    String classPath="";
    String nativePath="";
    if(nativeUrl.contains("classPath:")){
      //so we can test both ways
      classPath="a local path that works for you";
      nativePath="a local path to a trivial *.so";
      }
    //return "return <YourMap>.of("+slaveName+","+toLambda+").get();";
    //the of method may also handle exceptions in some reasonable way (Marco will handle this)
    return "return \"TODO\";";
    }
  }

enum TrustedKind {
  Int("int"){public String factory(List<String> xs){
    assert xs.size()==1;//just this
    return "return 0;";
    }},
  String("String"){public String factory(List<String> xs){
    assert xs.size()==1;
    return "return \"\";";
    }},
  Limit("Void"){public String factory(List<String> xs){
    assert false;
    throw bug();
    }};
  public final String inner;
  TrustedKind(String inner){this.inner = inner;}
  public abstract String factory(List<String> xs);
  public static TrustedKind fromString(String s) {
   for (TrustedKind t : TrustedKind.values()) {
    if (t.inner.equals(s))return t;
    }
   throw todo();
  }
 }
enum TrustedOp {
  _a("_a",Map.of(String,(xs,e)->"return "+xs.get(0)+" + \"a\";")),
  _b("_b",Map.of(String,(xs,e)->"return "+xs.get(0)+" + \"b\";")),
  StrDebug("strDebug",Map.of(String,(xs,e)->
    "Resources.out("+xs.get(0)+"); return L42Void.instance;"
    )),
  LimitTime("limitTime",Map.of(Limit,(xs,e)->
    "System.out.println("+xs.get(1)+"); return L42Void.instance;"
    )),
  Plus("OP+",Map.of(
    Int,(xs,e)->"return "+xs.get(0)+" + "+xs.get(1)+";",
    String,(xs,e)->"return "+xs.get(0)+" + "+xs.get(1)+";"
    )),
  Mul("OP*",Map.of(Int,(xs,e)->"return "+xs.get(0)+" * "+xs.get(1)+";"));
  public final String inner;
  Map<TrustedKind,BiFunction<List<String>,Core.E,String>>code;
  TrustedOp(String inner,Map<TrustedKind,BiFunction<List<String>,Core.E,String>>code){
    this.inner = inner;
    this.code=code;
    }
  public static TrustedOp fromString(String s) {
   for (TrustedOp t : TrustedOp.values()) {
    if (t.inner.equals(s))return t;
    }
   throw todo();
  }
 }

 
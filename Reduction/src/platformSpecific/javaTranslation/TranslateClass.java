package platformSpecific.javaTranslation;

import java.util.ArrayList;
import java.util.List;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast;
import ast.Ast.Type;
import ast.Ast.Path;
import ast.Ast.NormType;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Program;

public class TranslateClass {
  public static void of(Program p, String s, ClassB ct, StringBuilder res) {
  //classes that should exists only as class obj any for metaprogramming, are also interfaces here
    boolean isInterface=getClassOpen(s,ct,res);
    res.append("{\n");
    if(!isInterface){
      generateClassFacade(s, ct, res);
    }
    else{
      generateInterfaceFacade(s, ct, res);
    }
    getMethods(p,ct,res,isInterface);
    res.append("}");
  }

  private static void generateInterfaceFacade(String s, ClassB ct, StringBuilder res) {
    getPhNestedNotIntantiable(s,ct,res,true);
    getIType(s,ct,res);
  }

  private static void generateClassFacade(String s, ClassB ct, StringBuilder res) {
    MethodWithType ctor = extractConstructor(ct);
    if(ctor==null){
      getReverterForNotInstantiableClass(s,res);
      getPhNestedNotIntantiable(s,ct,res,false);
      getTypeForNotInstantiableClass(s,res);
      return;
      }
    getFields(ctor,res);
    getConstructor(s,ctor,res);
    getReverter(s,ctor,res);
    getGettesSettersAndExposers(ct,res);
    getPhNested(s,ctor,res);
    getType(s,ctor,res);
  }

  private static void getTypeForNotInstantiableClass(String s, StringBuilder res) {
      assert !s.contains("%"):
          "break";
    res.append("public static final "+s+" type=new "+s+"();");
  }

  private static void getITReverter(String s,StringBuilder res) {
    res.append("public ast.ExpCore revert(){\n");
    res.append("return ast.Ast.Path.parse(\"");
    res.append(Resources.name42Of(s));
    res.append("\");}\n");
    }
  private static void getReverterForNotInstantiableClass(String s,StringBuilder res) {
    res.append("public ast.ExpCore revert(){\n");
    res.append("ast.Ast.Path receiver=ast.Ast.Path.parse(\"");
    res.append(Resources.name42Of(s));
    res.append("\");\n");
    res.append("if(this==type){return receiver;}\n");
    res.append("throw new Error(\"Impossible to revert an instance of a not instantiable class, how you get this instance?\");");
    res.append("}\n");
  }

  private static void getReverter(String s, MethodWithType ctor,StringBuilder res) {
    res.append("public ast.ExpCore revert(){\n");
    res.append("ast.Ast.Path receiver=ast.Ast.Path.parse(\"");
    res.append(Resources.name42Of(s));
    res.append("\");\n");
    res.append("if(this==type){return receiver;}\n");
    //res.append("java.util.ArrayList<String> xs=new java.util.ArrayList<>(java.util.Arrays.asList(");
    List<String> ns = ctor.getMs().getNames();
    //StringBuilders.formatSequence(res,ns.iterator(),
    //  ", ",n->res.append("\""+n+"\""));
    //res.append("));\n");
    res.append("java.util.ArrayList<ast.ExpCore> es=new java.util.ArrayList<>(java.util.Arrays.asList(");
    StringBuilders.formatSequence(res,ns.iterator(),
      ", ",n->res.append(
          "platformSpecific.javaTranslation.Resources.Revertable.doRevert(this.F"+Resources.nameOf(n)+")"
          ));
    res.append("));\n");
    res.append("return new ast.ExpCore.MCall(receiver,"+ctor.getMs().toSrcEquivalent()+",ast.Ast.Doc.empty(),es,null);\n");
    res.append("}\n");
  }

  private static boolean getClassOpen(String s, ClassB ct, StringBuilder res) {
    boolean isInterface=ct.isInterface();
    res.append("public static ");
    if(isInterface){
      res.append("interface ");
      if(!ct.getDoc1().isEmpty()){res.append("/*"+ct.getDoc1().toString()+"*/");}
      }
    else{res.append("class ");}
    res.append(s);
    if(isInterface){res.append(" extends ");}
    else{res.append(" implements ");}
    res.append("platformSpecific.javaTranslation.Resources.Revertable");
    for(Path pi:ct.getStage().getInheritedPaths()){
      //if (pi.equals(Path.outer(0))){continue;}
      res.append(", ");
      res.append(Resources.nameOf(pi));
    }
    return isInterface;
  }

  private static MethodWithType extractConstructor(ClassB ct) {
    for(Member m:ct.getMs()){
      assert m instanceof MethodWithType;
      MethodWithType mt=(MethodWithType)m;
      if(mt.getInner().isPresent()){continue;}
      if(mt.getMt().getMdf()!=Ast.Mdf.Class){continue;}
      return mt;
    }
    return null;
  }
  private static void getFields(MethodWithType ctor, StringBuilder res) {
    List<String> ns = ctor.getMs().getNames();
    List<Type> ts = ctor.getMt().getTs();
    StringBuilders.formatSequence(res,ns.iterator(),ts.iterator(),
      ";\n", (n,t)->{
        assert t instanceof NormType;
        res.append("public ");
        res.append(Resources.nameOf(t));
        res.append(" F"+Resources.nameOf(n));
        });
    res.append(";\n");
  }
  private static void getConstructor(String s, MethodWithType ctor, StringBuilder res) {
    res.append("public ");
    res.append(s);
    res.append("( ");
    List<String> ns = ctor.getMs().getNames();
    List<Type> ts = ctor.getMt().getTs();
    StringBuilders.formatSequence(res,ns.iterator(),ts.iterator(),
      ", ",(n,t)->{
        n=Resources.nameOf(n);
        res.append(Resources.nameOf(t));
        res.append(" P"+n);
      });
    res.append("){\n");
    StringBuilders.formatSequence(res,ns.iterator(),ts.iterator(),
    "\n",(n,t)->{
      n=Resources.nameOf(n);
      res.append("this.");
      res.append("F"+n);
      res.append("=");
      res.append("P"+n);
      res.append(";\n");
      //if(d instanceof PhI<?>){((PhI<D>)d).addAction((val)->this.d=val);}
      res.append("if(");
      res.append("P"+n);
      res.append(" instanceof platformSpecific.javaTranslation.Resources.PhI<?>){((platformSpecific.javaTranslation.Resources.PhI<"+Resources.nameOf(t)+">)");
      res.append("P"+n);
      res.append(").addAction((val)->this.");
      res.append("F"+n);
      res.append("=val);}\n");
    });
    res.append("}\n");
  }

  private static void getGettesSettersAndExposers(ClassB ct, StringBuilder res) {
    for(Member m:ct.getMs()){
      assert m instanceof MethodWithType;
      MethodWithType mt=(MethodWithType)m;
      if(mt.getInner().isPresent()){continue;}
      if(mt.getMt().getMdf()==Ast.Mdf.Class){continue;}//constructor
      if(mt.getMt().getTs().isEmpty()){
        getGetterOrExposer(mt,res);
      }
      else {getSetter(mt,res);}
    }

  }


  private static void getSetter(MethodWithType mt, StringBuilder res) {
    getMethodHeader(mt, res);
    res.append("{this.");
    String name=Resources.nameOf(mt.getMs().getName());
    res.append("F"+name);
    res.append("=Pthat;return platformSpecific.javaTranslation.Resources.Void.instance;}");
    }

  private static void getGetterOrExposer(MethodWithType mt, StringBuilder res) {
    getMethodHeader(mt, res);
    res.append("{return this.");
    String name=mt.getMs().getName();
    if(name.startsWith("#")){name=name.substring(1);}
    name=Resources.nameOf(name);
    res.append("F"+name);
    res.append(";}");
    }

  public static void getMethodHeader(MethodWithType mt, StringBuilder res) {
    res.append("\npublic ");
    res.append(Resources.nameOf(mt.getMt().getReturnType()));
    res.append(" ");
    res.append(Resources.nameOf(mt.getMs()));
    res.append("(");
    StringBuilders.formatSequence(res,
      mt.getMs().getNames().iterator(),
      mt.getMt().getTs().iterator(), ", ",
      (n,t)->{
        n=Resources.nameOf(n);
        res.append(Resources.nameOf(t));
        res.append(" P"+n);
      });
    res.append(")\n");
  }

  private static void getPhNested(String s, MethodWithType ctor, StringBuilder res) {
    res.append("public static final class Ph extends ");
    res.append(s+" implements platformSpecific.javaTranslation.Resources.PhI<"+s+">{\n");
    res.append("  private final java.util.ArrayList<java.util.function.Consumer<"+s+">> actions=new java.util.ArrayList<>();\n");
    res.append("  public void commit("+s+" val){ for(java.util.function.Consumer<"+s+"> r:actions){r.accept(val);} }");
    res.append("  public void addAction(java.util.function.Consumer<"+s+"> r){actions.add(r);}");
    res.append("  public Ph() {super(");
    StringBuilders.formatSequence(res, ctor.getMs().getNames().iterator(),
      ", ",n->res.append("null"));
    res.append(");}\n  }\n");
    }
  private static void getPhNestedNotIntantiable(String s,ClassB cb, StringBuilder res,boolean isInterface) {
    if(isInterface){
    res.append("public static final class Ph implements ");
    res.append(s+", platformSpecific.javaTranslation.Resources.PhI<"+s+">{\n");
    }
    else{
      res.append("public static final class Ph extends ");
      res.append(s+" implements platformSpecific.javaTranslation.Resources.PhI<"+s+">{\n");
    }
    res.append("  private final java.util.ArrayList<java.util.function.Consumer<"+s+">> actions=new java.util.ArrayList<>();\n");
    res.append("  public void commit("+s+" val){ for(java.util.function.Consumer<"+s+"> r:actions){r.accept(val);} }");
    res.append("  public void addAction(java.util.function.Consumer<"+s+"> r){actions.add(r);}");
    res.append(" public ast.ExpCore revert()");
    res.append("{throw new Error(\"PhInvocation\");}\n");
    for(Member m:cb.getMs()){
      MethodWithType mt=(MethodWithType)m;
      getMethodHeader(mt, res);
      res.append("{throw new Error(\"PhInvocation\");}\n");
    }
    res.append("  public Ph(){ }\n  }\n");
    }

  private static void getType(String s, MethodWithType ctor, StringBuilder res) {
    res.append("public static final "+s+" type=new "+s+"(");
    StringBuilders.formatSequence(res,ctor.getMs().getNames().iterator(),
        ", ",n->res.append("null"));
    res.append(");\n");
    getMethodHeader(ctor, res);
    res.append("{return new "+s+"(");
    StringBuilders.formatSequence(res,ctor.getMs().getNames().iterator(),
      ", ",n->res.append("P"+Resources.nameOf(n)));
    res.append(");}\n");
    }
  private static void getIType(String s, ClassB cb, StringBuilder res) {
    res.append("public static final "+s+" type=new "+s+"(){\n");
    getITReverter(s,res);
    for(Member m:cb.getMs()){
      MethodWithType mt=(MethodWithType)m;
      getMethodHeader(mt, res);
      res.append("{throw new Error(\""+"Calling an interface method"+":"+mt.getMs()+"\");}\n");
      }
    res.append("};\n");
    }

  private static void getMethods(Program p,ClassB ct, StringBuilder res,boolean isInterface) {
    for(Member m:ct.getMs()){
      assert m instanceof MethodWithType;
      MethodWithType mt=(MethodWithType)m;
      if( !isInterface && !mt.getInner().isPresent()){continue;}
      //if(mt.getMt().getMdf()!=Ast.Mdf.Type){continue;}
      getMethod(p,mt,res);
    }
  }
  private static void getMethod(Program p,MethodWithType mt, StringBuilder res) {
    getMethodHeader(mt, res);
    if(mt.getInner().isPresent()){
      TranslateExpression.of(mt.getInner().get(),res);
      }
    else{res.append(";\n");}
    }
  }

package helpers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import reduction.Executor;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import sugarVisitors.ToFormattedText;
import ast.Ast.Path;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.Expression;
import auxiliaryGrammar.Program;
import facade.Configuration;
import facade.L42;
import facade.Parser;

public class TestHelper {
  public static void assertEqualExp(ExpCore e1,ExpCore e2){
    assert e1!=null;
    assert e2!=null;
    String s1=ToFormattedText.of(e1);
    String s2=ToFormattedText.of(e2);
    if(e1.equals(e2) && s1.equals(s2)){return;}
    if(s1.equals(s2)){
      String s1A="ToTextEqual,AstIs:"+e1;
      String s2A="ToTextEqual,AstIs:"+e2;
      if(s1A.equals(s2A)){
        Assert.assertEquals("EqualText and ast, as:"+s1+" : "+e1,"");
        }
      Assert.assertEquals(s1+" : "+s1A,s2+" : "+s2A);
      }
    Assert.assertEquals(s1,s2);
  }
  public static void assertEqualExp(Expression e1,Expression e2){
    assert e1!=null;
    assert e2!=null;
    String s1=ToFormattedText.of(e1);
    String s2=ToFormattedText.of(e2);
    if(e1.equals(e2) && s1.equals(s2)){return;}
    if(s1.equals(s2)){
      String s1A="ToTextEqual,AstIs:"+e1;
      String s2A="ToTextEqual,AstIs:"+e2;
      if(s1A.equals(s2A)){
        Assert.assertEquals("EqualText and ast, as:"+s1+" : "+e1,"");
        }
      Assert.assertEquals(s1+" : "+s1A,s2+" : "+s2A);
      }
    Assert.assertEquals(s1,s2);
  }
  public static void isETop(String s){
    //Parser.getParser(s).nudeE();
    Parser.parse(null,s);
    }
  public static void notETop(String s){
    try{
      Parser.parse(null,s);
/*      ETopContext e2=(ETopContext)(Parser.getParser(s).nudeE()).children.get(0);
      String rep="";
 	    for( ParseTree e:e2.children){rep+=e.getClass().getSimpleName()+" "+e.toString();}
 	    */
      throw new Error(s+" should be parse error but is accepted");
      }
    catch(ParseCancellationException e){}
    catch(ErrorMessage e){}
    catch(IllegalArgumentException e){}
    }


 /* public static Expression _testParseString(String s){
    LoggedPrintStream lpsErr = LoggedPrintStream.create(System.err);
    System.setErr(lpsErr);
    try{
      NudeEContext r = Parser.getParser(s).nudeE();
      return r.accept(new ToAst());
      }
    catch(ParseCancellationException e){
      throw new ParseCancellationException(e.getMessage()+"\n"+lpsErr.buf.toString());
//      throw new RuntimeException(
//          lpsErr.buf.toString()
//          );
    }finally{System.setErr(lpsErr.underlying);}
    }*/
  public static Program getProgramCD(){
    return getProgram(new String[]{"{C:{new() type method Outer1::C foo(type Outer1::C bar) (bar.foo(bar:this))}, D:{new(var Outer1::C x)}}"});
  }

  public static ClassB getClassB(String e1) {
    return (ClassB)Desugar.of(Parser.parse(null," "+e1)).accept(new InjectionOnCore());
  }

   public static Program getProgram(/*List<Path> paths,*/String[] code){
    Program p0=Program.empty();
    for(String s:code){
      Expression e=Parser.parse(null,s);
      ClassB ec=(ClassB)Desugar.of(e).accept(new InjectionOnCore());
      ec=Configuration.typeSystem.typeExtraction(p0, ec);
      p0=p0.addAtTop((ClassB)ec);
      }
    return p0;
  }
  public static String multiLine(String ...ss){
    StringBuffer res=new StringBuffer();
    for(String s:ss){res.append(s);res.append("\n");}
    return res.toString();
  }/*
  static class LoggedPrintStream extends PrintStream {
    final StringBuilder buf;
    final PrintStream underlying;
    LoggedPrintStream(StringBuilder sb, OutputStream os, PrintStream ul) {
        super(os);
        this.buf = sb;
        this.underlying = ul;
    }
  public static LoggedPrintStream create(PrintStream toLog) {//from http://stackoverflow.com/questions/4334808/how-could-i-read-java-console-output-into-a-string-buffer
    try {
      final StringBuilder sb = new StringBuilder();
      Field f = FilterOutputStream.class.getDeclaredField("out");
      f.setAccessible(true);
      OutputStream psout = (OutputStream) f.get(toLog);
      return new LoggedPrintStream(sb, new FilterOutputStream(psout) {
        public void write(int b) throws IOException {
          super.write(b);
          sb.append((char) b);
          }
        }, toLog);
      }
    catch (NoSuchFieldException|IllegalArgumentException |IllegalAccessException e) {
      throw Assertions.codeNotReachable();
      }}}*/

  public static void _dbgCompact(ExpCore e){
    assert e instanceof ClassB;
    ClassB cb=(ClassB)e;
    ArrayList<Member> ms = new ArrayList<>();
    for(Member m:cb.getMs()){
      if(!(m instanceof NestedClass)) {continue;}
      NestedClass nc=(NestedClass)m;
      if((nc.getInner() instanceof ClassB)){continue;}
      ms.add(nc);break;
      }
    cb=cb.withMs(ms);
    System.out.println(ToFormattedText.of(cb));
  }
  public static void configureForTest() {
    Configuration.reduction=new reduction.Facade();
    Configuration.typeSystem=new typeSystem.Facade();
    L42.record=new StringBuilder();
    L42.usedNames.clear();
  }
  public static void reportError(ErrorMessage e){
  if(Executor.last1==null||Executor.last2==null){throw e;}
  ClassB c1=(ClassB)Executor.last1;
  ClassB c2=(ClassB)Executor.last2;
  ArrayList<Member> ms1 = new ArrayList<>();
  ArrayList<Member> ms2 = new ArrayList<>();
  {int i=-1;for(Member e1:c1.getMs()){i+=1;
    Member e2=c2.getMs().get(i);
    if(e1.equals(e2)){continue;}
    ms1.add(e1);
    ms2.add(e2);
  }}
  c1=c1.withMs(ms1);
  c2=c2.withMs(ms2);
  //TestHelper.assertEqualExp(c1, c2);
  }

}

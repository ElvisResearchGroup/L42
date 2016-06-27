package helpers;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

import javax.print.DocFlavor.STRING;

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
import facade.L42.ExecutionStage;
import is.L42.connected.withSafeOperators.NormalizePrivates;
import platformSpecific.javaTranslation.Resources;
import profiling.Timer;

public class TestHelper {
  public static void check42Fails(String s){
    if(s.isEmpty()){fail("String is empty, may have not even started!"); throw new Error();}
    if(s.contains("Error kind:")){fail("Error of some kind"); throw new Error();}
    //if(s.contains("Error kind: MalformedFinalResult")){fail("Error kind: MalformedFinalResult"); throw new Error();}
    final String failString = "[FAIL] ";
    int fails = 0;
    int index = s.indexOf(failString);
    while( index != -1 ){
      fails++;
      index = s.indexOf(failString, index+1);
      }
    if(fails > 0){fail(fails+ " error(s) have occured in the test cases!"); throw new Error();}
  }

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
    return getProgram(new String[]{
        "{C:{"
        + "class method This new() class method This1.C foo(class This1.C bar) (bar.foo(bar:this))}, D:{"
        + "class method This new(fwd This1.C x)"
        + "read method This1.C x()"
        + "mut method This1.C #x()"
        + "mut method Void x(This1.C that)"
        + "}}"
        });
  }

  public static ClassB getClassB(String source, String e1) {
    Expression code1=Parser.parse("GeneratedByTestHelper_"+source,e1);
    auxiliaryGrammar.WellFormedness.checkAll(code1);
    Expression code2=Desugar.of(code1);
    assert auxiliaryGrammar.WellFormedness.checkAll(code2);
    ExpCore.ClassB code3=(ExpCore.ClassB)code2.accept(new InjectionOnCore());
    assert coreVisitors.CheckNoVarDeclaredTwice.of(code3);
    return code3;
  }
  public static ClassB getClassB(String e1) {
    return getClassB(null, e1);
  }

   public static Program getProgram(/*List<Path> paths,*/String[] code){
    Program p0=Program.empty();
    Integer outerCount = code.length;
    for(String s:code){
      Expression e=Parser.parse("This"+outerCount,s);
      --outerCount;
      ClassB ec=(ClassB)Desugar.of(e).accept(new InjectionOnCore());
      Configuration.typeSystem.computeStage(p0, ec);
      p0=p0.addAtTop(ec);
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
    NormalizePrivates.reset();
    Resources.clearRes();
    Timer.restart();
    Timer.activate("TOP");
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

  public static class ErrorCarry {
    /* When testing for errors,
     * when the errors have many parameters,
     * and adjacent errors share most of them,
     * this class carries those parameters
     * from test to test,
     * and gives each test a formatted error string.
     *
     * Because the presenting problem is the verbosity
     * of typing all of the error parameters repeatedly,
     * function names in this class put a premium on brevity.
     */

    ArrayList<String> _parameters = null;
    // invariant: even number of entries; category first then details

    static String valueFormat(String value) {
      /* If value is null, then no string should go into the error representation.
       * If the string is already formatted as an annotation,
       * (begins with '@ or '[@ or '[] )
       * output it as formatted,
       * otherwise annotate it as a unicode string
       */
      if (null == value)
        return "";

      if (value.startsWith("//@") || value.startsWith("//[@")  || value.startsWith("//[]"))
        return  value;
      else
        return "//@stringU\n//" + value;
    }

    public String str() {
      return this.str(null);
    }

    public String str(java.io.PrintStream debugTo) {
      StringBuffer result = new StringBuffer();
      Iterator<String> itr = this._parameters.iterator();

      result.append("{");
      while (itr.hasNext()) {
        result.append(itr.next());
        result.append(":{");
        result.append(valueFormat(itr.next()));
        result.append("\n}");
      }
      result.append("}");

      if (null != debugTo)
        debugTo.println(result.toString());

      return result.toString();
    }

    public ErrorCarry load(String kind, String ... parameters) {
      assert(parameters.length %2 == 0);
      _parameters = new ArrayList<String>();
      _parameters.add("Kind");
      _parameters.add(kind);
      _parameters.addAll(Arrays.asList(parameters));

      return this;
    }

    public ErrorCarry set(String ... parameters) {
      /* set the values of some loaded parameters,
       * which must be supplied in the same order as in the load().
       */
      assert(parameters.length %2 == 0);
      Iterator<String> setItr = Arrays.asList(parameters).iterator();

      ListIterator<String> parmItr = _parameters.listIterator();
      while (setItr.hasNext() && parmItr.hasNext()) {
        String nextSet = setItr.next();

        //System.out.println("looking for '"+nextSet+"'");
        while (parmItr.hasNext()) {
          Boolean match = (nextSet.equals(parmItr.next()));
          parmItr.next();
          if (match) {
            //System.out.println("Putting '"+nextVal+"' in '"+nextSet+"'");
            String nextVal = setItr.next();
            parmItr.set(nextVal);
            break;
          }
        }
      }

      assert(!setItr.hasNext());  // some element was out of order or didn't match at all
      return this;
    }

    public ErrorCarry rename(String oldLabel, String newLabel) {
      /* this function is an allowance for errors in the spelling
       * of kinds or parameter labels.
       * It changes one label to the new spelling,
       * enabling a test of the old spelling to fail
       * without breaking all subsequent tests.
       *
       * Could be abused to update parameter values;
       * not intended for this purpose.
       */
      ListIterator<String> parmItr = _parameters.listIterator();
      while (parmItr.hasNext()) {
        Boolean match = (oldLabel.equals(parmItr.next()));
        if (match) {
          parmItr.set(newLabel);
          return this;
        }
      }
      assert(false);  // the rename didn't match, so something is broken
      return this;
    }
  }

  public static int lineNumber() {
    return Thread.currentThread().getStackTrace()[2].getLineNumber();
  }

  public static List<Object[]> skipUntilLine(List<Object[]> tests, int startLine) {
    // Element 0 of each test must be an int,
    // being the line number in the test file at which the test starts.
    // These will be assumed to be in ascending order.
    // Find the first one >= startLine, and return the remainder of the list.
    // Attempting to return no tests is an error.
    ListIterator<Object[]> t = tests.listIterator();
    while (t.hasNext()) {
      if (startLine <= (int)(t.next()[0])) {
        return tests.subList(t.previousIndex() ,tests.size());
      }
    }
    assert(false);  // no tests left, so something is broken
    return null;
  }
  static String shortName(java.nio.file.Path p){
    // Remove the uninteresting bits from a path, in order to make a short name for a test
    final String preamble = "/Tests/bin/";
    final String middle1 = "libTests";
//    final String middle2 = "libProject";
    
    String ret = p.toString();
    int preStart = ret.indexOf(preamble);
    ret = ret.substring(preStart + preamble.length());
    ret = ret.replace(middle1, "");
//    ret = ret.replace(middle2,  "");
    return ret;
  }
}

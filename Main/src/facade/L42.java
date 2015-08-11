package facade;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import coreVisitors.InjectionOnSugar;
import sugarVisitors.CollapsePositions;
import sugarVisitors.CollectDeclaredClassNamesAndMethodNames;
import sugarVisitors.CollectDeclaredVarsAndCheckNoDeclaredTwice;
import sugarVisitors.Desugar;
import sugarVisitors.InjectionOnCore;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast;
import ast.Ast.Position;
import ast.ErrorMessage;
import ast.ExpCore;
import ast.ErrorMessage.FinalResult;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.MethodWithType;
import auxiliaryGrammar.Program;
import ast.Expression;

public class L42 {
  public static enum ExecutionStage{None,Reading,Parsing,CheckingWellFormedness,Desugaring,MetaExecution,Closing;}
  private static ExecutionStage stage=ExecutionStage.None;
  public static ExecutionStage getStage(){return stage;}
  public static int compilationRounds=0;
  public static StringBuilder record=new StringBuilder();
  public static String[] programArgs=null;
  public static List<URL> pluginPaths=null;
  public static final Set<String> usedNames=new HashSet<String>();
  public static void printDebug(String s){
    record.append(s);
    record.append("\n");
    System.err.println(s);
  }

  private static void setClassPath(Path p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
   assert Files.isDirectory(p);
   List<URL> fileNames = new ArrayList<>();
   try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(p)){
     for (Path path : directoryStream) {
       fileNames.add(path.toUri().toURL());
       }
     }
   catch (IOException ex) {
     Assertions.codeNotReachable(""+ex);
     }
   //System.out.println(fileNames);
   L42.pluginPaths=fileNames;
   URLClassLoader loader= (URLClassLoader) ClassLoader.getSystemClassLoader();
   Method method= URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
   method.setAccessible(true);
   for(URL url:fileNames){
     method.invoke(loader, new Object[] { url });
     }
   }

  public static void main(String [] arg) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
    setClassPath(Paths.get("Plugins"));
    L42.programArgs=arg;
    try{
      Configuration.loadAll();
      Path path = Paths.get(arg[arg.length-1]);

      String code=null;
      if(Files.isDirectory(path)){
        L42.setRootPath(path);
        code=L42.pathToString(path.resolve("Outer0.L42"));
        }
      else {
        L42.setRootPath(path.getParent());
        code=L42.pathToString(path);
        }
      FinalResult res = L42.runSlow(path.toString(),code);
      System.out.println("------------------------------");
      System.out.println("END (zero for success): "+res.getErrCode());
    }
    catch(ParseCancellationException parser){
      System.out.println(parser.getMessage());
      //parser.printStackTrace(System.out);
      }
    catch(ErrorMessage msg){
      //System.out.println(ErrorFormatter.formatError(msg).getErrorTxt());
    	L42.printDebug(
    	    ErrorFormatter.formatError(Program.empty(),msg).getErrorTxt()
    	    );
    	for(Field f:msg.getClass().getDeclaredFields()){
        f.setAccessible(true);
        if(!f.getName().equals("p")){continue;}
        List<ClassB>program=(List<ClassB>)f.get(msg);
    	  for(ClassB cb:program){
    	    L42.printDebug(ErrorFormatter.displayAbstractMethods(cb));
    	  }
      }
    }
  }


  public static String pathToString(Path p) {
    StringBuffer b=new StringBuffer();
    try {
      Files.lines(p).forEach((l)->{b.append("\n");b.append(l);});
      b.append("\n");
      return b.toString();
      }
    catch (IOException e) {throw new Error(e);}
    }
  public static int runSlow(Path p) throws IOException{
    try{
      stage=ExecutionStage.Reading;
      String code=pathToString(p);
      throw runSlow(p.toString(),code);}
    catch(ErrorMessage e){return e.getErrCode();}
  }
  public static void finalErr(ClassB result,String s){
    throw new ErrorMessage.MalformedFinalResult(result, s);
  }
  public static ErrorMessage.FinalResult checkFinalError(ClassB result){
    stage=ExecutionStage.Closing;
    ClassB.NestedClass last=(ClassB.NestedClass)result.getMs().get(result.getMs().size()-1);
    if(!(last.getInner() instanceof ClassB)){finalErr(result,"The last class can not be completed");}
    ClassB lastC=(ClassB)last.getInner();
    //TODO: booh what is the right kind? if(!(lastC.getH() instanceof Ast.TraitHeader)){finalErr(result,"The last class is not a Trait");}
    if(!lastC.getMs().isEmpty()){finalErr(result,"The last class contains members");}
    if(!lastC.getDoc2().isEmpty()){finalErr(result,"The last class have non empty second documentation: "+lastC.getDoc2());}
    String errCode=lastC.getDoc1().toString();
    if(!errCode.startsWith("@exitStatus\n")){finalErr(result,"The last class is not an exitStatus class:"+errCode);}
    errCode=errCode.substring("@exitStatus\n".length());
    int errCodeInt=0;
    try{errCodeInt=Integer.parseInt(errCode.substring(0,errCode.length()-1));}
    catch(NumberFormatException e){finalErr(result,"The exitStatus is not a valid number: "+errCode);}
    if(errCodeInt>0 && errCodeInt<100){finalErr(result,"The exitStatus is reserved: "+errCodeInt);}
    return new ErrorMessage.FinalResult(errCodeInt,result);
  }
  public static ErrorMessage.FinalResult runSlow(String fileName,String code){
    stage=ExecutionStage.Parsing;
    Expression code1=Parser.parse(fileName,code);
    stage=ExecutionStage.CheckingWellFormedness;
    auxiliaryGrammar.WellFormedness.checkAll(code1);
    stage=ExecutionStage.Desugaring;
    Expression code2=Desugar.of(code1);
    assert auxiliaryGrammar.WellFormedness.checkAll(code2);
    ExpCore.ClassB code3=(ExpCore.ClassB)code2.accept(new InjectionOnCore());
    assert coreVisitors.CheckNoVarDeclaredTwice.of(code3);
    //L42.usedNames.addAll(CollectDeclaredVarsAndCheckNoDeclaredTwice.of(code2));
    //L42.usedNames.addAll(CollectDeclaredClassNamesAndMethodNames.of(code2));
    stage=ExecutionStage.MetaExecution;
    //ClassB result= (ClassB)Executor.stepStar(exe,code3);
    ClassB result= Configuration.reduction.of(code3);
    //System.out.println("--------------------------");
    //System.out.println(ToFormattedText.of(result));
    //System.out.println("--------------------------");
    return checkFinalError(result);
  }

  public static Path path;
  public static void setRootPath(Path path) {
    L42.path=path;
  }
}

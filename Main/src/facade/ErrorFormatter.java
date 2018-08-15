package facade;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import sugarVisitors.CollapsePositions;
import sugarVisitors.ToFormattedText;
import tools.Assertions;
import tools.StringBuilders;
import ast.Ast;
import ast.Ast.Doc;
import ast.Ast.Mdf;
import ast.Ast.Path;
import ast.Ast.Stage;
import ast.ErrorMessage;
import ast.ErrorMessage.UserLevelError.Kind;
import ast.ExpCore;
import ast.ExpCore.ClassB;
import ast.ExpCore.ClassB.Member;
import ast.ExpCore.ClassB.NestedClass;
import ast.ExpCore.ClassB.Phase;
import ast.Expression;

import ast.Ast.Position;
import ast.ExpCore.ClassB.MethodWithType;
import coreVisitors.InjectionOnSugar;
import coreVisitors.IsCompiled;
import facade.L42.ExecutionStage;
import newTypeSystem.FormattedError;
import platformSpecific.javaTranslation.Resources;
import programReduction.Program;
import programReduction.Program.EmptyProgram;

public class ErrorFormatter {
  //TODO: in future, not display for coherent classes
  public static String displayAbstractMethods(ClassB cb){
    StringBuilder result=new StringBuilder();
    //result.append("Abstract methods:\n");
    //displayAbstractMethods(cb,result,"");
    return result.toString();
  }

@SuppressWarnings("unused")//TODO: what was this for
private static void displayAbstractMethods(ClassB cb,StringBuilder result,String nesting){

    result.append("{\n");
    for(Member m:cb.getMs()){
      m.match(nc->{
        if(!(nc.getInner() instanceof ClassB)){return null;}
        if(((ClassB)nc.getInner()).isInterface()){return null;}
        StringBuilder inner=new StringBuilder();
        displayAbstractMethods((ClassB)nc.getInner(),inner,nesting+"  ");
        String innerStr=inner.toString();
        if(!innerStr.contains("method")){return null;}
        result.append(nesting);
        result.append(nc.getName());
        result.append(":");
        result.append(innerStr);
        return null;
        },
      mi->{
        return null;
        },
      mt->{
        if(mt.get_inner()!=null){return null;}
        if(mt.getMt().getMdf()==Mdf.Class){return null;}
        result.append(nesting);
        result.append(ToFormattedText.of(mt).replace("\n", "\n"+nesting));
        result.append("\n");
        return null;
        });
    }
    result.append("}\n");
  }
  public interface Reporter{ String toReport(ArrayList<Ast.Position>ps);}
  public static ErrorMessage.UserLevelError formatError(Program p,ErrorMessage msg) {
    Class<?> c=msg.getClass();
    try {return formatError(p,msg, c);}
    catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {throw new Error(e);}
  }
  public static ErrorMessage.UserLevelError formatError(Program p,ErrorMessage msg, Class<?> c) throws IllegalAccessException, NoSuchFieldException, SecurityException {
    String errorStart="";
    ArrayList<Ast.Position> ps=new ArrayList<Ast.Position>();
    String errorTxt="";
    errorTxt+= infoFields(msg, c,ps);
    try{
      errorTxt+="Surrounding context:\n";
      errorTxt+= envs(msg, c,ps);
      ArrayList<Position> ps2 = ps;
      if(!ps.isEmpty()){ps2=new ArrayList<>();}
      try{errorTxt+= ctxP(msg, c,ps2);}catch(NoSuchFieldException ignored){}
    }
    catch(NoSuchFieldException ignored){}
    pos(msg,c,ps);
    Position pos=positionsFilter(ps);
    if(c==ErrorMessage.DotDotDotCanNotBeResolved.class){
//        ErrorMessage.DotDotDotCanNotBeResolved ddd=(ErrorMessage.DotDotDotCanNotBeResolved)msg;

    }
    //errorTxt="Error kind: "+c.getSimpleName()+"\nPosition:"+
    //((pos==null)?"unknown":pos.toString())+"\n"+errorTxt;
    //ps+"\n"+errorTxt;
    ErrorMessage.UserLevelError.Kind kind=findKind(msg);
    switch (kind){
      case Unclassified:
        if(msg instanceof FormattedError) {
          errorTxt=msg.getMessage();
          break;
          }
      case TypeError:
      case ParsingError:
      case WellFormedness:
        String intro=errorStart+"runStatus: "+kind.name()+":"+msg.getClass().getSimpleName()+"\n";
        if(msg.getMessage().length()!=0){
          intro+="Message: "+msg.getMessage()+"\n";
          }
        errorTxt=intro+errorTxt;
        break;
      case MetaError:
        errorTxt=errorStart+"runStatus: "+kind.name()+"\n"+
        "Error in generating the following class: \n"
            +reportPlaceOfMetaError(p,msg)
            ;///*.replace(".\n","\n")*/+"\n----------\n"+errorTxt;
      default:
        break;
      }

    Throwable cause=null;
    if(msg.getCause()!=null){
      if(msg.getCause() instanceof ErrorMessage){
        ErrorMessage.UserLevelError uleCause=formatError(p,(ErrorMessage)msg.getCause());
        cause=uleCause;
        errorTxt+="\n-------- caused by -----\n"+uleCause.getErrorTxt();
        }
      else {cause=msg.getCause();}
      }
    ErrorMessage.UserLevelError result= new ErrorMessage.UserLevelError(kind,pos,msg,errorTxt);
    result.initCause(cause);
    return result;

  }

  private static String reportPlaceOfMetaError(Program p,ErrorMessage msg) {
    ErrorMessage.MalformedFinalResult _msg=(ErrorMessage.MalformedFinalResult)msg;
    ClassB cb=_msg.getFinalRes();
    //String path=_msg.getReason();
    String path=reportPlaceOfMetaError(p,cb);
    return path;//+"\n-----\n"+_msg.getReason();
  }
  public static String reportPlaceOfMetaError(Program p,ClassB cb) {
    for(Member m:cb.getMs()){
      if(IsCompiled.of(m)){continue;}
      return m.match(
        nc->nc.getName()+
        "\nError is:\n"+L42.messageOfLastTopLevelError+"\n"+
        "reconstructed-stackTrace:"+L42.reconstructedStackTrace+"\n"+
        isItErrorPlace(p,nc.getInner()),
        mi->formatSelectorCompact(mi.getS())+"."+isItErrorPlace(p,mi.getInner()),
        mt->formatSelectorCompact(mt.getMs())+"."+isItErrorPlace(p,mt.getInner())
        );
      }
    for(Member m:cb.getMs()){
      String err=m.match(nc->{
        ClassB ncb=(ClassB)nc.getInner();
        if(ncb.getPhase()!=Phase.Coherent){
          return nc.getName()+"."+isItErrorPlace(p,nc.getInner());
          }
        return null;
        }, mi->null, mt->null);
      if(err!=null){
        return "NotStarOf "+err;
        }
    }
    return "it should be a nested somewhere";//TODO: improve//
    //throw Assertions.codeNotReachable();
  }
  private static String isItErrorPlace(Program p,ExpCore inner) {//TODO: rename method in giveErrorText?
    if(inner instanceof ClassB){return reportPlaceOfMetaError(p,(ClassB)inner);}
    //return "\n    "+reportMetaError(p,inner);
    return "";
  }
  private static class ReportThrow extends coreVisitors.CloneWithPath{
    List<Ast.C> collectedPath=null;
    ExpCore.Signal collectedErr=null;
    public ClassB.NestedClass visit(ClassB.NestedClass nc){
      if(nc.getInner() instanceof ExpCore.Signal){
        collectedPath=this.getLocator().getClassNamesPath();
        collectedErr=(ExpCore.Signal )nc.getInner();
      }
      return super.visit(nc);}
  }
  public static String reportMetaError(Program p,ExpCore inner) {
    ReportThrow rt=new ReportThrow();
    inner.accept(rt);
    String str1="";
    if(rt.collectedErr!=null){
      str1=ToFormattedText.of(rt.collectedErr)+"\nContained inside:"+Path.outer(0, rt.collectedPath)+"\n";
      }
    str1+=ToFormattedText.of(inner).replace("\n"," ");
    return str1;
  }
  private static Kind findKind(ErrorMessage msg) {
    if (L42.getStage()==ExecutionStage.CheckingWellFormedness){
      return Kind.WellFormedness;}
    if(msg instanceof ErrorMessage.PreParserError){return Kind.ParsingError;}
    if(msg instanceof ErrorMessage.TypeError){return Kind.TypeError;}
    if(msg instanceof ErrorMessage.MalformedFinalResult){return Kind.MetaError;}
    return Kind.Unclassified;

  }
  private static Position positionsFilter(ArrayList<Position> ps) {
    if(ps.isEmpty()){return null;}
    Position p=ps.get(0);
    for(Position pi:ps){
      p=CollapsePositions.accumulatePos(p, pi);
    }
    return p;
  }
  public static String envs(ErrorMessage msg, Class<?> c,ArrayList<Ast.Position> ps)
      throws NoSuchFieldException, IllegalAccessException {
    String errorTxt="";
    Field f=c.getField("envs");
    f.setAccessible(true);
    Collection<?> envs=(Collection<?>)f.get(msg);
    for(Object o:envs){
      errorTxt+=errorFormat(o,ps)+"\n";
    }
    return errorTxt;
  }
  public static void pos(ErrorMessage msg, Class<?> c,ArrayList<Ast.Position> ps)
      throws IllegalAccessException {
    try{
      Field f=c.getDeclaredField("pos");
      f.setAccessible(true);
      if( f.get(msg) instanceof Ast.Position){
        Ast.Position pos=(Ast.Position)f.get(msg);
        ps.add(pos);
        }
    }
    catch(NoSuchFieldException ignored){}
  }
  public static String ctxP(ErrorMessage msg, Class<?> c,ArrayList<Ast.Position>ps)
      throws NoSuchFieldException, IllegalAccessException {
    String errorTxt="";
    Field f=c.getDeclaredField("p");
    f.setAccessible(true);
    Collection<?> envs=(Collection<?>)f.get(msg);
    if(envs!=null){
      for(Object o:envs){
        errorTxt+=errorFormat(o,ps)+"\n";
      }}
    return errorTxt;
  }

  public static String infoFields(ErrorMessage msg, Class<?> c,ArrayList<Ast.Position>ps)
      throws IllegalAccessException {
    String errorTxt="";
    for(Field f:c.getDeclaredFields()){
      f.setAccessible(true);
      if(f.getName().equals("p")){continue;}
      if(f.getName().equals("cb")){continue;}
      Object obj=f.get(msg);
      if(obj==null){continue;}
      errorTxt+=f.getName();
      errorTxt+=": ";
      errorTxt+=errorFormat(obj,ps);
      errorTxt+="\n";
    }
    return errorTxt;
  }
  public static String errorFormat(Object obj,ArrayList<Ast.Position>ps) {
    if(obj instanceof ast.Ast.HasPos){ps.add(((ast.Ast.HasPos)obj).getP());   }
    if(obj instanceof String){return (String)obj;}
    if(obj instanceof Integer){return obj.toString();}
    if(obj instanceof Expression){
      Ast.Position p=CollapsePositions.of((Expression)obj);
      ps.add(p);
      return ToFormattedText.ofCompact((Expression)obj);
      }
    if(obj instanceof ExpCore){//thanks to Path, this have to be after Expression
      Ast.Position p=CollapsePositions.of((ExpCore)obj);
      ps.add(p);
      ExpCore exp=(ExpCore)obj;
      Expression expression=exp.accept(new InjectionOnSugar());
      return errorFormat(expression,ps);
      }
    if(obj instanceof Ast.MethodSelector){
      return formatSelectorCompact((Ast.MethodSelector)obj);
          }
    if(obj instanceof ExpCore.ClassB.MethodWithType){
      return ToFormattedText.of((MethodWithType)obj).trim().replace("\n", " ");
      }
  //  if(obj instanceof Ast.MethodType){
  //    }
    if(obj instanceof Reporter){
      return ((Reporter)obj).toReport(ps);
    }
    if(obj instanceof Collection<?>){
      Collection<?> c=(Collection<?>)obj;
      if(c.size()==0){return "[]";}
      if(c.size()==1){return "["+errorFormat(c.iterator().next(),ps)+"]";}
      String res="[\n";
      for(Object o:c){
        res+="  "+errorFormat(o,ps)+"\n";
      }
      return res+"  ]\n";
    }
    if(obj instanceof Ast.Type){return obj.toString();}

    if(obj instanceof ClassB.Member){return ToFormattedText.of((ClassB.Member)obj);}
    //if(obj instanceof Expression){return ToFormattedText.of((Expression)obj);}
    if(obj instanceof Expression.ClassB.Member){return ToFormattedText.of((Expression.ClassB.Member)obj);}
    if(obj instanceof java.nio.file.Path){return obj.toString();}
    if(obj instanceof Ast.Position){ return obj.toString();}
    if(obj instanceof HashMap){ return obj.toString();}
    if(obj instanceof Collection){ return obj.toString();}
    if(obj instanceof ast.Ast.C){ return obj.toString();}
    if(obj instanceof Boolean){ return obj.toString();}
    if(obj instanceof ast.Ast.Path){ return obj.toString();}
    return "unknown kind "+obj.getClass().getName();
  }
  public static String formatSelectorCompact(Ast.MethodSelector ms) {
    StringBuilder sb=new StringBuilder();
    sb.append(ms.nameToS()+"(");
    StringBuilders.formatSequence(sb,ms.getNames().iterator(),", ",n->sb.append(n));
    return sb.toString()+")";
  }

  public static void printType(Program p) {
    //for(int i=0;i<50;i++){System.out.print("\n");}
    //System.out.print("\n*************************\n");
    //printType(0,p);
    }
@SuppressWarnings("unused")//TODO: what was used for?
private static void printType(int i, Program p) {
    printType(i,"",p.top());
    try{printType(i+1,p.pop());}
    catch(EmptyProgram ep){}

  }
  private static void printType(int i,String prefix, ClassB top) {
    System.out.print("This"+i+prefix+" :{\n");
    printMembers(top);
    System.out.print("  }\n");
    for(Member m:top.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      if(nc.getInner() instanceof ClassB){
        printType(i,"."+nc.getName(),(ClassB)nc.getInner());
      }
    }

  }
  private static void printMembers(ClassB top) {
    for(Member m:top.getMs()){
      if(!(m instanceof MethodWithType)){continue;}
      MethodWithType mwt=(MethodWithType)m;
      mwt=mwt.withDoc(Doc.empty());
      mwt=mwt.with_inner(null);
      String txt=sugarVisitors.ToFormattedText.of(mwt);
      txt=txt.replace("{","");
      txt=txt.replace("}","");
      txt=txt.replace("\n"," ");
      System.out.print("  "+txt+"\n");
/*      System.out.print("  "+mwt.getMt().getReturnType());
      System.out.print(" "+mwt.getMs().getName());
      System.out.print("(");
      {int i=-1;for(String n:mwt.getMs().getNames()){i+=1;
      System.out.print(mwt.getMt().getTs()
      }}
      System.out.print(")\n");
  */
    }

  }
  private static String whyIsNotExecutable(ClassB cb) {
    /*if(cb.getH() instanceof Ast.TraitHeader){
      return "\n  The requested path is a trait";
    }*/
    for(Member m: cb.getMs()){
      if(!(m instanceof MethodWithType)){continue;}
      /*MethodWithType mt=(MethodWithType)m;
      if (!mt.getInner().isPresent() && !mt.isFieldGenerated()){
        return "\n  The method "+mt.getMs()+" of the requested path is abstract";
      }*/
    }
    for(Member m: cb.getMs()){
      if(!(m instanceof NestedClass)){continue;}
      NestedClass nc=(NestedClass)m;
      if (!(nc.getInner() instanceof ClassB)){
        return "\n  The nested class "+nc.getName()+" of the requested path is not compiled yet";
      }
      String nestedRes=whyIsNotExecutable((ClassB)nc.getInner());
      if(nestedRes!=null){
        return "."+nc.getName()+nestedRes;
      }
    }
    return null;
  }

  public static String whyIsNotExecutable(Path path, Program p1) {
    ClassB cb=p1.extractClassB(path);
    String whyNot=whyIsNotExecutable(cb);
    if (whyNot!=null){
      return "The requested path is incomplete.\n  "
          +ToFormattedText.of(path)+whyNot;
    }
    return "The requested path is incomplete since it refers to other incomplete classes in the program";
  }

@SuppressWarnings("unchecked")
public static void topFormatErrorMessage(ErrorMessage msg) {
    //System.out.println(ErrorFormatter.formatError(msg).getErrorTxt());
    L42.print42Err("###-###-###-###-###-###-###-###-###-###-###-###-###-###-###-###-###-###");
    L42.print42Err("###--#---#---#---#---#---#---#---#---#---#---#---#---#---#---#---#--###");
    //The text over is the tag for the test to check it is failed

    L42.print42Err(
        formatError(Program.emptyLibraryProgram(),msg).getErrorTxt()
        );
    for(Field f:msg.getClass().getDeclaredFields()){
      f.setAccessible(true);
      if(!f.getName().equals("p")){continue;}
      List<ClassB> program;
      try { program = (List<ClassB>)f.get(msg);}
      catch (IllegalArgumentException | IllegalAccessException e) { throw new Error(e);}
      if(program!=null){
        for(ClassB cb:program){
          System.out.println(displayAbstractMethods(cb));
        }
      }
    }
  }
}

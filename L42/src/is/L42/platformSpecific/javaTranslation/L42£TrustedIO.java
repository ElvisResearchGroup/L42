package is.L42.platformSpecific.javaTranslation;

import static is.L42.tools.General.todo;
import static is.L42.tools.General.unreachable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.stream.Collectors;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.L42SingletonCache;
import is.L42.cache.nativecache.ValueCache;
import is.L42.common.Err;
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.P;
import is.L42.generated.Pos;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;
import is.L42.visitors.CloneVisitorWithProgram;

public class L42£TrustedIO extends L42NoFields<L42£TrustedIO>{
  private L42£TrustedIO(){}
  public static L42£TrustedIO instance=new L42£TrustedIO();
  public L42£Void strDebug(String s){
    Resources.out(s);
    return L42£Void.instance;
    }
  public L42£Void addToLog(String logName,String text){
    String oldText=Resources.logs().putIfAbsent(logName,text);
    if(oldText!=null){Resources.logs().put(logName,oldText+text);}
    return L42£Void.instance;
    }
  public L42£Void clearLog(String logName){
    Resources.logs().remove(logName);
    return L42£Void.instance;
    }
  public String readLog(String logName){
    String res=Resources.logs().get(logName);
    return res==null?"":res;
    }    
  public L42£Void testActualExpected(L42£Library hasPos,String name, String actual, String expected,String message,String hole){
    System.out.println("testActualExpected");
    System.out.println(name);
    System.out.println(actual);
    System.out.println(expected);
    boolean cond=hole.isEmpty()?actual.equals(expected):Err.strCmpAux(actual,expected,hole);
    Pos pos=hasPos.unwrap.pos();
    assert !name.contains("\n");
    assert !pos.fileName().toString().contains("\n"):"|"+pos.fileName().toString()+"|";
    StringBuilder sb=new StringBuilder("###################\n");
    if(cond){sb.append("#Pass    "+name+"\n");}
    else{sb.append("#Fail    "+name+"\n");}
    sb.append("#line: "+pos.line());
    sb.append("    ");
    sb.append(pos.fileName().toString()+"\n");
    if(!cond){
      sb.append("#StrCompare\n");
      sb.append("#Actual\n");
      if(actual.isEmpty()){sb.append("#|");}
      else{sb.append(actual.lines().map(l->"#|"+l).collect(Collectors.joining("\n")));}
      sb.append("\n#Expected\n");
      if(expected.isEmpty()){sb.append("#|");}
      else{sb.append(expected.lines().map(l->"#|"+l).collect(Collectors.joining("\n")));}
      sb.append("\n");
      }  
    Resources.tests(sb.toString());
    return L42£Void.instance;
    }
    
  public L42£Void testCondition(L42£Library hasPos,String name,boolean cond,String message){
    Pos pos=hasPos.unwrap.pos();
    assert !name.contains("\n");
    assert !pos.fileName().toString().contains("\n");
    StringBuilder sb=new StringBuilder("###################\n");
    if(cond){sb.append("#Pass    "+name+"\n");}
    else{sb.append("#Fail    "+name+"\n");}
    sb.append("#line: "+pos.line());
    sb.append("    ");
    sb.append(pos.fileName().toString()+"\n");
    if(!cond){
      message=message.lines().map(l->"#|"+l).collect(Collectors.joining("\n"));
      sb.append(message);
      if(!message.isEmpty() && !message.endsWith("\n")){sb.append("\n");}
      }  
    Resources.tests(sb.toString());
    return L42£Void.instance;
    }
  public L42£Void deployLibrary(String s, L42£Library l42Lib){
    Core.L l=l42Lib.unwrap;
    assert l.wf();
    Program p=Program.flat(l);
    l.accept(new CloneVisitorWithProgram(p){//could be an accumulator visitor to be more efficient
      @Override public P visitP(P p){
        boolean open=p.isNCs() && (p.toNCs().n()>p().dept() || this.p()._ofCore(p)==null);
        if(open){
          System.err.println("Path "+p+" not defined inside of deployed code");
          throw todo();
          //TODO: replace it with a 42 error for deploy non selfcontained library 
          }
        return p;
        }
      });
    ProgramTypeSystem.type(true, p);
    //TODO: wrap an EndError above as a L42 exception
    l=l.accept(new CloneVisitor(){
      @Override public Core.L.Info visitInfo(Core.L.Info info){
        return info.withTyped(true);
        }});
    try(
      var file=new FileOutputStream("localhost"+File.separator+s+".L42"); 
      var out=new ObjectOutputStream(file);
      ){
      out.writeObject(l);
      }
    catch (FileNotFoundException e) {throw unreachable();}
    catch (IOException e) {
      e.printStackTrace();
      throw todo();
      }
    //TODO: should throw a non deterministic exception as for 
    //memory overflow/stack overflow. It should be error S,
    //the same type of the String
    return L42£Void.instance;
    }
  public static final Class<L42£TrustedIO> _class = L42£TrustedIO.class;
  public static final L42SingletonCache<L42£TrustedIO> myCache=L42CacheMap.newSingletonCache("L42£TrustedIO",L42£TrustedIO._class);
  @Override public L42Cache<L42£TrustedIO> myCache(){return myCache;}
  }
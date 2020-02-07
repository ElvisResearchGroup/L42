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
import is.L42.common.Program;
import is.L42.generated.Core;
import is.L42.generated.Pos;
import is.L42.typeSystem.ProgramTypeSystem;
import is.L42.visitors.CloneVisitor;

public class L42£TrustedIO extends L42Singleton<L42£TrustedIO>{
  public L42£Void strDebug(String s){
    Resources.out(s);
    return L42£Void.instance;
    }
  private static HashMap<String,String> logs=new HashMap<>();
  public L42£Void addToLog(String logName,String text){
    String oldText=logs.putIfAbsent(logName,text);
    if(oldText!=null){logs.put(logName,oldText+text);}
    return L42£Void.instance;
    }
  public L42£Void clearLog(String logName){
    logs.remove(logName);
    return L42£Void.instance;
    }
  public String readLog(String logName){
    String res=logs.get(logName);
    return res==null?"":res;
    }    
  public L42£Void testActualExpected(L42£Library hasPos,String name, String actual, String expected,String message){
    boolean cond=actual.equals(expected);
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
      sb.append("#StrCompare\n");
      sb.append("#Actual\n");
      actual=actual.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(actual);
      sb.append("\n#Expected\n");
      expected=expected.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(expected+"\n");
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
      message=message.lines().map(l->"#|"+l).collect(Collectors.joining());
      sb.append(message);
      if(!message.isEmpty() && !message.endsWith("\n")){sb.append("\n");}
      }  
    Resources.tests(sb.toString());
    return L42£Void.instance;
    }
  public L42£Void deployLibrary(String s, L42£Library l42Lib){
    Core.L l=l42Lib.unwrap;
    ProgramTypeSystem.type(true, Program.flat(l));
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
  public static final L42SingletonCache<L42£TrustedIO> myCache=new L42SingletonCache<L42£TrustedIO>("L42£TrustedIO",L42£TrustedIO._class);
  @Override public L42Cache<L42£TrustedIO> myCache(){return myCache;}
  }
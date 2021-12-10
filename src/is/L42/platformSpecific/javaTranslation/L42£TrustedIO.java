package is.L42.platformSpecific.javaTranslation;

import java.util.stream.Collectors;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.common.ErrMsg;
import is.L42.generated.Pos;

public class L42£TrustedIO extends L42NoFields<L42£TrustedIO>{
  private L42£TrustedIO(){}
  public static L42£TrustedIO instance=new L42£TrustedIO();
  public L42£Void strDebug(String s){
    Resources.out(s);
    return L42£Void.instance;
    }
  public L42£Void addToLog(String logName,String text){
    synchronized(Resources.logs()){
      String oldText=Resources.logs().putIfAbsent(logName,text);
      if(oldText!=null){Resources.logs().put(logName,oldText+text);}
      return L42£Void.instance;
      }
    }
  public L42£Void clearLog(String logName){
    synchronized(Resources.logs()){
      Resources.logs().remove(logName);
      return L42£Void.instance;
      }
    }
  public String readLog(String logName){
    synchronized(Resources.logs()){
      String res=Resources.logs().get(logName);
      return res==null?"":res;
      }
    }
  public static final String testsSeparator="###################\n";
  public L42£Void testActualExpected(L42£Library hasPos,String name, String actual, String expected,String message,String hole){
    //System.out.println("testActualExpected");
    //System.out.println(name);
    //System.out.println(actual);
    //System.out.println(expected);
    boolean cond=hole.isEmpty()?actual.equals(expected):ErrMsg.strCmpAux(actual,expected,hole);
    Pos pos=hasPos.unwrap.pos();
    assert !name.contains("\n");
    assert !pos.fileName().toString().contains("\n"):"|"+pos.fileName().toString()+"|";
    StringBuilder sb=new StringBuilder(testsSeparator);
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
    StringBuilder sb=new StringBuilder(testsSeparator);
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
  public static final Class<L42£TrustedIO> _class = L42£TrustedIO.class;
  public static final L42Cache<L42£TrustedIO> myCache=L42CacheMap.newSingletonCache("L42£TrustedIO",L42£TrustedIO._class);
  @Override public L42Cache<L42£TrustedIO> myCache(){return myCache;}
  }
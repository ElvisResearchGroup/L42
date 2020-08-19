package is.L42.platformSpecific.javaEvents;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Event{
  public static String nextEvent(){
    String res;try{res=events.pollFirst(1,TimeUnit.SECONDS);}
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
      throw new Error(e);
      }
    if(res==null){return "";}
    return res;
    }
  public static void submitEvent(String s){
    events.addLast(s);
    }
  private static LinkedBlockingDeque<String> events=new LinkedBlockingDeque<>();
  }

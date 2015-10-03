package profiling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Timer {
  public static <T> T record(String name,Supplier<T> fun){
  Timer.activate(name);try{  return fun.get();
  }finally{Timer.deactivate(name);}
  }
  public static List<Long> fakeTimes=new ArrayList<>();//for testing
  public static long giveTime(){
    if(fakeTimes.isEmpty()){return System.currentTimeMillis();}
    long res=fakeTimes.get(0);
    fakeTimes=fakeTimes.subList(1, fakeTimes.size());
    return res;
  }
  private static class TimerData{
    List<Long> opens=new ArrayList<>();
    List<Long> closes=new ArrayList<>();
    List<Long> times=new ArrayList<>();
    List<Long> timesLess=new ArrayList<>();
    long tot=0;
  }
  private static class TimerEntry{
      String name;long time;
      TimerEntry(String name,long time){this.name=name;this.time=time;}
  }
  public static final List<TimerEntry>timers=new ArrayList<>();
  public static void activate(String name){
    assert timers.isEmpty() || !timers.get(timers.size()-1).name.equals("o_"+name);
    timers.add(new TimerEntry("o_"+name,giveTime()));
  }
  public static void deactivate(String name){
    timers.add(new TimerEntry("c_"+name,giveTime()));
  }
 
  public static void restart(){timers.clear(); }
  public static String report(){
    if(timers.size()%2!=0){
      Timer.deactivate("TOP");
    }
    Map<String,TimerData> data=new HashMap<>();
    for(TimerEntry te:timers){
      assert te.name!=null;
      String name=te.name.substring(2);
      if(!data.containsKey(name)){ data.put(name, new TimerData());      }
      TimerData interval = data.get(name);
      boolean isOpen=te.name.startsWith("o");
      assert isOpen|| interval.closes.size()+1==interval.opens.size(): interval.closes+" "+interval.opens;//is close
      assert!isOpen|| interval.closes.size()==interval.opens.size():
        te.name;//is open
      //those check verify no recursion in timing
      if(isOpen){interval.opens.add(te.time);}
      else{
        interval.closes.add(te.time);
        long current=te.time-interval.opens.get(interval.opens.size()-1);
        interval.times.add(current);
        interval.tot+=current;
      }
    }
    
    String result="\n*******************************\n";
    List<String>names=new ArrayList<>(data.keySet());
    names.sort((n1,n2)->(int)(data.get(n2).tot-data.get(n1).tot));
    long totTop=data.get("TOP").tot;
    for(String name:names){
      TimerData td=data.get(name);
      result+="# "+name+"\n";
      long max=0;
      for(long time:td.times){
        if(max<time){max=time;}
        }
      result+=String.format("percentage:%.2f tot:%.3f max:%.3f number:%d",(td.tot/(float)totTop),td.tot/60000f,max/60000f,td.times.size())+"\n";
      }
    return result+"*************************************\n";
  }
  
}

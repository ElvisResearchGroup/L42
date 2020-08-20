package is.L42.platformSpecific.javaEvents;

import static is.L42.tools.General.range;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Event{
  public static final String end="##End##\n";
  public static final String empty="##Empty##\n";
  public static final int longWait=1000;
  public static final int shortWait=10;
  @FunctionalInterface
  public static interface Consumer3<T1,T2,T3>{void accept(T1 t1,T2 t2,T3 t3);}
  static {Consumer3.class.getClass();}//just loading Consumer3 together with Event
  static private final ExecutorService executor = Executors.newFixedThreadPool(1);
  private static LinkedBlockingDeque<String> clearDeque(Consumer3<String,String,String>c,String key,LinkedBlockingDeque<String>deque){
    while(!deque.isEmpty()){
      String s=deque.getFirst();
      int index=s.indexOf("\n");
      if(index==-1) {throw new Error("invalid event shape "+s);}
      String id=s.substring(0,index);
      String msg=s.substring(index+1);
      c.accept(key, id,msg);          
      }
    return null;    
    } 
  public static void registerEvent(String key,Consumer3<String,String,String>c){
    callbacks.compute(key,(k,v)->{
      var newV=executorAction(c);
      streams.computeIfPresent(k,(k0,v0)->clearDeque(newV, k0,v0));
      return newV;
      });
    }
  public static void resetEvent(String key){
    callbacks.put(key,Event::defaultAction);
    }
  public static String nextEvent(String keys){
    var ks=keys.split("\n");
    if(ks.length==0) {return end;}
    if(ks.length==1) {
      var k=ks[0].trim();
      return k+"\n"+nextEvent1(k);}
    @SuppressWarnings("unchecked")
    var qs=(LinkedBlockingDeque<String>[])new LinkedBlockingDeque[ks.length];
    try{
      for(var pi:ps){
        var res=oneRound(ks,qs,pi);
        if(res!=null){return res;}
        }
      }
    catch(InterruptedException ie){
      Thread.currentThread().interrupt();
      throw new Error(ie);
      }
    return empty;
    }
  private static final List<Poller>ps=List.of(
    q->q.poll(),
    q->q.poll(shortWait,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*3,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*9,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*27,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*81,TimeUnit.MILLISECONDS)
    );
  private static interface Poller{String apply(LinkedBlockingDeque<String> q)throws InterruptedException;}
  private static String oneRound(String[]ks,LinkedBlockingDeque<String>[]qs, Poller poll)throws InterruptedException{
    var res=poll.apply(qs[0]);
    if(res!=null){return ks[0].trim()+"\n"+res;}
    for(var i=1;i<ks.length;i++){
      var resi=qs[i].pollFirst();
      if(resi!=null){return ks[i].trim()+"\n"+resi;}
      }
    return null;
    }
  private static String nextEvent1(String key){
    var events=streams.computeIfAbsent(key,k->new LinkedBlockingDeque<>());
    String res;try{res=events.pollFirst(longWait,TimeUnit.MILLISECONDS);}
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
      throw new Error(e);
      }
    if(res==null){return empty;}
    return res;
    }
  public static void submitEvent(String key,String id,String msg){
    callbacks.compute(key,(k,v)->{
      if(v==null){v=Event::defaultAction;}
      v.accept(k, id, msg);
      return v;
      });
    }
  private static Consumer3<String,String,String> executorAction(Consumer3<String,String,String> c){
    return (key,id,msg)->executor.submit(()->c.accept(key, id, msg));
    }
  private static void defaultAction(String key,String id,String msg){
    streams.computeIfAbsent(key,k->new LinkedBlockingDeque<>()).addLast(id+"\n"+msg);
    }
  private static final Map<String,LinkedBlockingDeque<String>> streams=Collections.synchronizedMap(new  LinkedHashMap<>());
  private static final Map<String,Consumer3<String,String,String>> callbacks=Collections.synchronizedMap(new LinkedHashMap<>());
  }

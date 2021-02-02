package is.L42.platformSpecific.javaEvents;

import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Event{
  private static Event instance=null;
  public static void initialize(){
    if(instance==null){instance=new Event();}
    }
  //Mah, when initialize is protected, we get a runtime security error
  public static void test_only_initialize(){instance=new Event();}
  private Event(){}
  public static Event instance(){
    if(instance!=null){return instance;}
    throw new Error("Event instance not initialized; this may be an issue with multiple class loaders");
    }
  private final String end="##End##\n";
  public String end(){return end;}
  private String empty="##Empty##\n";
  public String empty(){return empty;}
  public void setEmpty(String idMsg){empty=idMsg;}
  private int longWait=1000;
  private int shortWait=8;//longWait/120
  public void setTimeout(int timeout){
    longWait=timeout;
    if(timeout>=500){
      shortWait=timeout/120;
      ps=psLong;
      }
    else{
      shortWait=timeout/4;
      ps=psShort;
      }
    }
  @FunctionalInterface
  public static interface Consumer3{void accept(String key,String id,String msg);}
  public static interface Function3{String accept(String key,String id,String msg);}
  public static interface FFunction3{CompletableFuture<String> accept(String key,String id,String msg);}
  static{preLoad();}//just loading Consumer3,Function3 together with Event
  static void preLoad(){
    Consumer3.class.getClass();
    Function3.class.getClass();
    FFunction3.class.getClass();
    }
  static private final ExecutorService executor = Executors.newFixedThreadPool(1);
  private static LinkedBlockingDeque<String> clearDeque(Consumer3 c,String key,LinkedBlockingDeque<String>deque){
    while(!deque.isEmpty()){
      String s;try {s=deque.takeFirst();}
      catch (InterruptedException e) {throw unreachable();}//not empty
      int index=s.indexOf("\n");
      if(index==-1) {throw new Error("invalid event shape "+s);}
      String id=s.substring(0,index);
      String msg=s.substring(index+1);
      c.accept(key, id,msg); 
      }
    return null;
    } 
  public void registerEvent(String key,Consumer3 c){
    callbacks.compute(key,(k,v)->{
      Consumer3 newV=executorAction(c);
      streams.computeIfPresent(k,(k0,v0)->clearDeque(newV, k0,v0));
      return newV;
      });
    }
  public void resetEvent(String key){
    callbacks.put(key,this::defaultAction);
    }
  public String nextEvent(String keys){
    var ks=keys.split("\n");
    if(ks.length==0) {return end;}
    if(ks.length==1) {
      var k=ks[0].trim();
      var res=k+"\n"+nextEvent1(k);
      return res;
      }
    @SuppressWarnings("unchecked")
    var qs=(LinkedBlockingDeque<String>[])new LinkedBlockingDeque[ks.length];
    for(var i:range(ks.length)){
      qs[i]=streams.computeIfAbsent(ks[i],k->new LinkedBlockingDeque<>());
      }
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
    return ks[0]+"\n"+empty;
    }  
  private final List<Poller>psShort=List.of(
      q->q.poll(),
      q->q.poll(shortWait,TimeUnit.MILLISECONDS),
      q->q.poll(shortWait*3,TimeUnit.MILLISECONDS)
      );
  private final List<Poller>psLong=List.of(
    q->q.poll(),
    q->q.poll(shortWait,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*3,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*9,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*27,TimeUnit.MILLISECONDS),
    q->q.poll(shortWait*81,TimeUnit.MILLISECONDS)
    );
  private List<Poller>ps=psLong;
  private static interface Poller{String apply(LinkedBlockingDeque<String> q)throws InterruptedException;}
  private String oneRound(String[]ks,LinkedBlockingDeque<String>[]qs, Poller poll)throws InterruptedException{
    var res=poll.apply(qs[0]);
    if(res!=null){return ks[0].trim()+"\n"+res;}
    for(var i=1;i<ks.length;i++){
      var resi=qs[i].pollFirst();
      if(resi!=null){return ks[i].trim()+"\n"+resi;}
      }
    return null;
    }
  private String nextEvent1(String key){
    var events=streams.computeIfAbsent(key,k->new LinkedBlockingDeque<>());
    String res;try{res=events.pollFirst(longWait,TimeUnit.MILLISECONDS);}
    catch (InterruptedException e){
      Thread.currentThread().interrupt();
      throw new Error(e);
      }
    if(res==null){return empty;}
    return res;
    }
  public void submitEvent(String key,String id,String msg){
    try{auxSubmitEvent(key, id, msg);}
    catch(RuntimeException err){err.printStackTrace();throw err;}
    catch(Error err){err.printStackTrace();throw err;}
    }
  public void auxSubmitEvent(String key,String id,String msg){
    callbacks.compute(key,(k,v)->{
      System.out.println(key+" "+k+" "+id+" "+msg+" "+v);
      System.out.println(callbacks);
      if(v==null){v=this::defaultAction;}
      v.accept(k, id, msg);
      return v;
      });
    }
  public CompletableFuture<String> askEvent(String key,String id,String msg){
    var v=askCallbacks.get(key);
    if(v==null){return this.defaultAskAction;}
    return CompletableFuture.supplyAsync(()->v.accept(key, id, msg), executor);
    }
  public void registerAskEvent(String key,Function3 c){
    askCallbacks.put(key,c);
    }
  public void resetAskEvent(String key){
    askCallbacks.remove(key);
    }
  private Consumer3 executorAction(Consumer3 c){
    return c;
    //return (key,id,msg)->executor.submit(()->c.accept(key, id, msg));
    }
  private void defaultAction(String key,String id,String msg){
    var s=streams.computeIfAbsent(key,k->new LinkedBlockingDeque<>());
    s.addLast(id+"\n"+msg);
    }
  private final CompletableFuture<String>defaultAskAction=CompletableFuture.completedFuture("");
  private final Map<String,LinkedBlockingDeque<String>> streams=Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String,Consumer3> callbacks=Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String,Function3> askCallbacks=Collections.synchronizedMap(new LinkedHashMap<>());
  }

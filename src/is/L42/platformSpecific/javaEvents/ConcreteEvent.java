package is.L42.platformSpecific.javaEvents;

import static is.L42.tools.General.range;
import static is.L42.tools.General.unreachable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConcreteEvent implements Event{
  private static ConcreteEvent instance=null;
  public static void initialize(){
    if(instance==null){instance=new ConcreteEvent();}
    }
  //Mah, when initialize is protected, we get a runtime security error
  public static void test_only_initialize(){instance=new ConcreteEvent();}
  private ConcreteEvent(){}
  public static ConcreteEvent instance(){
    if(instance!=null){return instance;}
    throw new Error("Event instance not initialized; this may be an issue with multiple class loaders");
    }
  private String defaultAsk="";
  public String defaultAsk(){return defaultAsk;}
  public void setDefaultAsk(String answer){defaultAsk=answer;}
  private String end="##End##\n";
  public String end(){return end;}
  private String empty="##Empty##\n";
  public String empty(){return empty;}
  public void setEmpty(String idMsg){empty=idMsg;}
  public void setEnd(String idMsg){end=idMsg;}
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
  public static interface FFunction3{CompletableFuture<String> accept(String key,String id,String msg);}
  static private final ExecutorService executor = Executors.newFixedThreadPool(1);
  private static LinkedBlockingDeque<String> clearDeque(BiConsumer<String,String> c,String key,LinkedBlockingDeque<String>deque){
    while(!deque.isEmpty()){
      String s;try {s=deque.takeFirst();}
      catch (InterruptedException e) {throw unreachable();}//not empty
      int index=s.indexOf("\n");
      if(index==-1) {throw new Error("invalid event shape "+s);}
      String id=s.substring(0,index);
      String msg=s.substring(index+1);
      c.accept(id,msg); 
      }
    return null;
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
    var v = callbacks.getOrDefault(key,this.defaultAction(key));
    v.accept(id, msg);
    }
  public CompletableFuture<String> askEvent(String key,String id,String msg){
    BiFunction<String,String,String> v=askCallbacks.getOrDefault(key,defaultAskAction);
    return CompletableFuture.supplyAsync(()->v.apply(id,msg), executor);
    }

  public void registerEvent(String key,BiConsumer<String,String> c){
    callbacks.compute(key,(k,v)->{
      streams.computeIfPresent(k,(k0,v0)->clearDeque(c, k0,v0));
      return c;
      });
    }
  public void registerEvent(String key,String id,Consumer<String> c){
    callbacks.compute(key,(k,v)->{
      BiConsumer<String,String> vOld=v!=null?v:this.defaultAction(key);
      BiConsumer<String,String> cIf=(_id,_msg)->{
        if(id.equals(_id)){c.accept(_msg);}
        else {vOld.accept(_id, _msg);}
        };
      streams.computeIfPresent(k,(k0,v0)->clearDeque(cIf, k0,v0));
      return cIf;
      });
    }
  public void resetEvent(String key){
    callbacks.put(key,this.defaultAction(key));
    }
  public void registerAskEvent(String key,BiFunction<String,String,String> c){
    askCallbacks.put(key,c);
    }
  public void registerAskEvent(String key,String id, Function<String,String> f){
    var kb=askCallbacks.get(key);
    if (kb==null){ askCallbacks.put(key,new AskIdCallBack(id,f)); return; }
    if(kb instanceof AskIdCallBack a){ a.add(id, f); return; }
    askCallbacks.put(key,new AskIdCallBack(id,f,kb));
    }
  public void resetAskEvent(String key){
    askCallbacks.remove(key);
    }
  private BiConsumer<String,String> defaultAction(String key){return (id,msg)->{
    var s=streams.computeIfAbsent(key,k->new LinkedBlockingDeque<>());
    s.addLast(id+"\n"+msg);
    };}
  private final BiFunction<String,String,String>defaultAskAction=(id,msg)->this.defaultAsk;
  private final Map<String,LinkedBlockingDeque<String>> streams=Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String,BiConsumer<String,String>> callbacks=Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String,BiFunction<String,String,String>> askCallbacks=Collections.synchronizedMap(new LinkedHashMap<>());
  class AskIdCallBack implements BiFunction<String,String,String>{
    AskIdCallBack(String id,Function<String,String>f){add(id,f);}
    AskIdCallBack(String id,Function<String,String>f,BiFunction<String,String,String> delegate){
        this.delegate=delegate; 
        add(id,f);
        }
    BiFunction<String,String,String> delegate=defaultAskAction;
    HashMap<String,Function<String,String>>ops=new HashMap<>();
    public void add(String id,Function<String,String>f){ ops.put(id, f); }
    public String apply(String id, String msg) {
      var f=ops.get(id);
      if(f==null){ return delegate.apply(id, msg); }
      return f.apply(msg);
      }
    }
  }
package is.L42.maps;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
/*
can have both imm and mut vals, but the keys are all normalized imms.
three generics: key T and OptT, and OptT need to be generic on T
In the future can be optimized; may be a array could be used for small maps.
*/
public class L42£Map<K,T>{
  LinkedHashMap<K,T> mapImms=null;//new LinkedHashMap<>();
  LinkedHashMap<K,T> mapMuts=null;//new LinkedHashMap<>();
  K[] keys=null;
  T[] vals=null;
  public boolean isEmpty(){
    return (mapImms==null || mapImms.isEmpty()) && (mapMuts==null || mapMuts.isEmpty());
    }
  private int sizeImm(){return mapImms==null?0:mapImms.size();}
  private int sizeMut(){return mapMuts==null?0:mapMuts.size();}
  static private final Object[] emptyArr=new Object[]{};
  static private final Object unnull=new Object();
  @SuppressWarnings("unchecked")
  private void loadIteration(){
    if(keys!=null){return;}
    LinkedHashMap<K,T> lone=mapImms==null?mapMuts:mapMuts==null?mapImms:null;
    if(isEmpty()){
      keys=(K[])emptyArr;
      vals=(T[])emptyArr;
      return;
      }
    if(lone!=null){
      keys=(K[])lone.keySet().toArray();
      vals=(T[])lone.values().toArray();
      return;
      }
    keys=(K[])Stream.concat(mapImms.keySet().stream(),mapMuts.keySet().stream()).toArray();
    vals=(T[])Stream.concat(mapImms.values().stream(),mapMuts.values().stream()).toArray();
    }
  public int size() {return sizeImm()+sizeMut();}
  public K keyIndex(int index){loadIteration();return keys[index];}
  public T valIndex(int index){loadIteration();return vals[index];}

  public /*Opt<T>*/T val(K key){//can never be null, we will insert unnull on null (for opts and opts supertypes)!
    T valImm=mapImms==null?null:mapImms.get(key);
    T val=valImm!=null?valImm:mapMuts==null?null:mapMuts.get(key);
    return val;//this, in 42 must be an Opt<T> native
    }
  public /*Opt<T>*/T immVal(K key){
    return mapImms==null?null:mapImms.get(key);
    }
  public /*Opt<T>*/T mutVal(K key){
    return mapMuts==null?null:mapMuts.get(key);
    }
  @SuppressWarnings("unchecked")
  public void addImm(K key,T val){
    if(val==null){val=(T)unnull;}
    if(mapImms==null){mapImms=new LinkedHashMap<>();}
    if(mapMuts!=null){mapMuts.remove(key);}
    mapImms.put(key, val);
    }
  public void addMut(K key,T val){}//same reverse
  public void remove(K key){}
  }
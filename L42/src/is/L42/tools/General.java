package is.L42.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class General {
  public static final class IterableWrapper implements List<Integer>, Iterator<Integer>{
    int current;
    final int end;
    IterableWrapper(int start,int end){this.current=start;this.end=end;}
    @Override public Iterator<Integer> iterator() {return this;}
    @Override public boolean hasNext() { return current<end;}
    @Override public Integer next() {return current++;}
    @Override public int size() {return end-current;}
    @Override public boolean isEmpty() {return size()==0;}
    @Override public boolean contains(Object o) { 
      return o instanceof Integer && ((Integer)o)<end && ((Integer)o)>=current;}
    @Override public Object[] toArray() {throw new UnsupportedOperationException("Range only operations");}
    @Override public <T> T[] toArray(T[] a){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean add(Integer e){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean remove(Object o){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean containsAll(Collection<?> c) { return c.stream().allMatch(e->contains(e));}
    @Override public boolean addAll(Collection<? extends Integer> c){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean addAll(int index, Collection<? extends Integer> c){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean removeAll(Collection<?> c){throw new UnsupportedOperationException("Range only operations");}
    @Override public boolean retainAll(Collection<?> c){throw new UnsupportedOperationException("Range only operations");}
    @Override public void clear(){throw new UnsupportedOperationException("Range only operations");}
    @Override public Integer get(int index) {
      if(current+index<end){return current+index;}
      throw new IndexOutOfBoundsException();
      }
    @Override public Integer set(int index, Integer element){throw new UnsupportedOperationException("Range only operations");}
    @Override public void add(int index, Integer element){throw new UnsupportedOperationException("Range only operations");}
    @Override public Integer remove(int index){throw new UnsupportedOperationException("Range only operations");}
    @Override public int indexOf(Object o) {
      if (!(o instanceof Integer)){return -1;}
      int i=(Integer)o;
      if(i<current || i>=end){return -1;}
      return i-current;}
    @Override public int lastIndexOf(Object o) {return indexOf(o);}
    @Override public ListIterator<Integer> listIterator() {throw new UnsupportedOperationException("Range only operations");}
    @Override public ListIterator<Integer> listIterator(int index) {throw new UnsupportedOperationException("Range only operations");}
    @Override public List<Integer> subList(int fromIndex, int toIndex) {throw new UnsupportedOperationException("Range only operations");}
    }
  public static IterableWrapper range(List<?> list) {return range(0,list.size());}
  public static IterableWrapper range(int endExclusive) {return range(0,endExclusive);}
  public static IterableWrapper range(int startInclusive,int endExclusive) {
    return new IterableWrapper(startInclusive,endExclusive);
    }
  public static <T, E extends RuntimeException> BinaryOperator<T> toOneOr(Supplier<E> err){
    return (element, otherElement) -> {throw err.get();};
    }
  public static <T, E extends RuntimeException> BinaryOperator<T> toOneOrBug(){
    return toOneOr(()->bug());
    }

  public static <A,B extends A> List<B> typeFilter(Stream<A> a,Class<B> clazz){
    return a.filter(clazz::isInstance).map(clazz::cast)
      .collect(Collectors.toList());
    }
  public static <T> List<T> merge(List<T>l1,List<T>l2){
    if(l1.isEmpty()){return l2;}
    if(l2.isEmpty()){return l1;}
    ArrayList<T> res=new ArrayList<>();
    res.addAll(l1);
    res.addAll(l2);
    return Collections.unmodifiableList(res);
    }
  //merge unique
  public static <T> List<T> mergeU(List<T>l1,List<T>l2){
    if(l1.isEmpty()){return l2;}
    if(l2.isEmpty()){return l1;}
    ArrayList<T> res=new ArrayList<>();
    res.addAll(l1);
    for(T t:l2){if(!l1.contains(t)){res.add(t);}}
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T> unique(List<T>l){return unique(l,false);}
  public static <T> List<T> uniqueWrap(List<T>l){return unique(l,true);}
  private static <T> List<T> unique(List<T>l,boolean changed){
    ArrayList<T> res=new ArrayList<>();
    for(T t:l){if(!res.contains(t)){res.add(t);}else{changed=true;}}
    if(!changed){return l;}
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T>popL(List<T>l){
    assert !l.isEmpty();
    ArrayList<T> res=new ArrayList<>(l.size()-1);
    int size=l.size();
    for(int i=1;i<size;i++){res.add(l.get(i));}
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T>popLRight(List<T>l){
    assert !l.isEmpty();
    ArrayList<T> res=new ArrayList<>(l.size()-1);
    int size=l.size()-1;
    for(int i=0;i<size;i++){res.add(l.get(i));}
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T>pushL(T e,List<T>l){
    ArrayList<T> res=new ArrayList<>();
    res.add(e);
    res.addAll(l);
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T>pushL(List<T>l,T e){
    ArrayList<T> res=new ArrayList<>();
    res.addAll(l);
    res.add(e);
    return Collections.unmodifiableList(res);
    }

  public static <T> List<T>L(Stream<T> s){return s.collect(Collectors.toList());}
  public static <T> List<T>L(){return Collections.emptyList();}
  public static <T> List<T>L(T e){return Collections.singletonList(e);}
  public static <T> List<T>L(Consumer<ArrayList<T>> c){
    ArrayList<T> res=new ArrayList<>();
    c.accept(res);
    return Collections.unmodifiableList(res);
    }
  /**
   Mapping from T into T with flyweight pattern. if the function return null, the element is filtered away
   */
  public static <T> List<T>L(List<T> a,Function<T,T> f){
    if(a.isEmpty()){return L();}
    int size=a.size();
    ArrayList<T> res=new ArrayList<>(size);
    boolean change=false;
    for(int i=0;i<size;i++){
      var e0=a.get(i);
      var e=f.apply(e0);
      change|=e0!=e;
      if(e!=null){res.add(e);}
      }
    if(!change){return a;}
    return Collections.unmodifiableList(res);
    }
  public static <A,R> List<R>L(List<A> a,BiConsumer<ArrayList<R>,A> c){
    if(a.isEmpty()){return L();}
    ArrayList<R> res=new ArrayList<>();
    for(int i=0;i<a.size();i++)c.accept(res,a.get(i));
    return Collections.unmodifiableList(res);
    }
  @FunctionalInterface
  public static interface Consumer3<T1,T2,T3>{void accept(T1 t1,T2 t2,T3 t3);}
  public static <A,B,R> List<R>L(List<A> a,List<B> b,Consumer3<ArrayList<R>,A,B> c){
    if(a.size()!=b.size()){
      throw new Error("different sizes of \n"+a+"\n"+b);}
    if(a.isEmpty()){return L();}
    ArrayList<R> res=new ArrayList<>();
    for(int i=0;i<a.size();i++){
      c.accept(res,a.get(i),b.get(i));
      }
    return Collections.unmodifiableList(res);
    }
  @FunctionalInterface
  public static interface Consumer4<T1,T2,T3,T4>{void accept(T1 t1,T2 t2,T3 t3,T4 t4);}
  public static <A,B,C,R> List<R>L(List<A> a,List<B> b,List<C> c,Consumer4<ArrayList<R>,A,B,C> d){
    if(a.size()!=b.size() || a.size()!=c.size()){throw new Error("different sizes of \n"+a+"\n"+b+"\n"+c);}
    if(a.isEmpty()){return L();}
    ArrayList<R> res=new ArrayList<>();
    for(int i=0;i<a.size();i++){
      d.accept(res,a.get(i),b.get(i),c.get(i));
      }
    return Collections.unmodifiableList(res);
    }
  public static RuntimeException unreachable(){throw new Error("Postcondition violation");}
  public static RuntimeException todo(){throw new Error("Not implemented yet");}
  public static RuntimeException bug(){throw new Error("Precondition violation");}
  public static boolean checkNoException(Supplier<Boolean> s){
    try {return s.get();}
    catch(Throwable t){throw new AssertionError("",t);}
    } 
  @SuppressWarnings("unused") private static <T> void testL(T e) {
    List<T> x = L(e);
    List<Integer> y=L();
    List<T> z=L(c->{c.add(e);c.add(e);});
    Consumer<ArrayList<T>> ec=c->{c.add(e);c.add(e);};
    List<Consumer<ArrayList<T>>> z2=General.<Consumer<ArrayList<T>>>L(ec);
    //incredibly hard to call the wrong version
    List<T> xx=L(x,y,(c,ai,bi)->{
      c.add(ai);
    });
  }
}

package is.L42.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

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
  public static <T> List<T>popL(List<T>l){return l.subList(1, l.size());}
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

  public static <T> List<T>L(){return Collections.emptyList();}
  public static <T> List<T>L(T e){return Collections.singletonList(e);}
  public static <T> List<T>L(Consumer<List<T>> c){
    ArrayList<T> res=new ArrayList<>();
    c.accept(res);
    return Collections.unmodifiableList(res);
    }
  public static <T> List<T>L(List<T> a,Function<T,T> f){
    int size=a.size();
    ArrayList<T> res=new ArrayList<>(size);
    boolean change=false;
    for(int i=0;i<size;i++){
      var e0=a.get(i);
      var e=f.apply(e0);
      change|=e0!=e;      
      res.add(e);
      }
    if(!change){return a;}
    return Collections.unmodifiableList(res);
    }
  public static <A,R> List<R>L(List<A> a,BiConsumer<List<R>,A> c){
    ArrayList<R> res=new ArrayList<>();
    for(int i=0;i<a.size();i++)c.accept(res,a.get(i));
    return Collections.unmodifiableList(res);
    }
  @FunctionalInterface
  public static interface Consumer3<T1,T2,T3>{void accept(T1 t1,T2 t2,T3 t3);}
  public static <A,B,R> List<R>L(List<A> a,List<B> b,Consumer3<List<R>,A,B> c){
    ArrayList<R> res=new ArrayList<>();
    if(a.size()!=b.size())throw new Error();
    for(int i=0;i<a.size();i++)
      c.accept(res,a.get(i),b.get(i));
    return Collections.unmodifiableList(res);
    }
  public static Error unreachable(){throw new Error("Postcondition violation");}
  public static Error todo(){throw new Error("Not implemented yet");}
  public static Error bug(){throw new Error("Precondition violation");}
 
  @SuppressWarnings("unused") private static <T> void testL(T e) {
    List<T> x = L(e);
    List<Integer> y=L();
    List<T> z=L(c->{c.add(e);c.add(e);});
    Consumer<List<T>> ec=c->{c.add(e);c.add(e);};
    List<Consumer<List<T>>> z2=General.<Consumer<List<T>>>L(ec);
    //incredibly hard to call the wrong version
    List<T> xx=L(x,y,(c,ai,bi)->{
      c.add(ai);
    });
  }
}

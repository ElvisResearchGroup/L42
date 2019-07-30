package is.L42.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class General {
  public static final class IterableWrapper implements Iterable<Integer>, Iterator<Integer>{
    int current;
    final int end;
    IterableWrapper(int start,int end){this.current=start;this.end=end;}
    @Override public Iterator<Integer> iterator() {return this;}
    @Override public boolean hasNext() { return current<end;}
    @Override public Integer next() {return current++;}
    }
  public static IterableWrapper range(int endExclusive) {return range(0,endExclusive);}
  public static IterableWrapper range(int startInclusive,int endExclusive) {
    return new IterableWrapper(startInclusive,endExclusive);
    }
  public static <T> List<T>L(){return Collections.emptyList();}
  public static <T> List<T>L(T e){return Collections.singletonList(e);}
  public static <T> List<T>L(Consumer<List<T>> c){
    ArrayList<T> res=new ArrayList<>();
    c.accept(res);
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
  private static <T> void testL(T e) {
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

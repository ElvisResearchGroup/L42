package tools;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class StringBuilders {
  public static <T> void formatSequence(StringBuilder b,Iterator<T> i,String separator, Consumer<T> v){
    if(!i.hasNext()){return;}
    v.accept(i.next());
    while(i.hasNext()){
      b.append(separator);
      v.accept(i.next());
      }
    }
  public static <T1,T2> void formatSequence(StringBuilder b,Iterator<T1> i1,Iterator<T2> i2,String separator, BiConsumer<T1,T2> v){
    if(!i1.hasNext()){return;}
    v.accept(i1.next(),i2.next());
    while(i1.hasNext()){
      b.append(separator);
      v.accept(i1.next(),i2.next());
      }}
  @FunctionalInterface
  public interface TriConsumer<T1,T2,T3> {public abstract void accept(T1 arg1,T2 arg2,T3 arg3);}
  public static <T1,T2,T3> void formatSequence(StringBuilder b,Iterator<T1> i1,Iterator<T2> i2,Iterator<T3> i3,String separator, TriConsumer<T1,T2,T3> v){
    if(!i1.hasNext()){return;}
    v.accept(i1.next(),i2.next(),i3.next());
    while(i1.hasNext()){
      b.append(separator);
      v.accept(i1.next(),i2.next(),i3.next());
      }}

  //For more then two, we will need such horror: Iterator<Integer> blah=IntStream.range(0,ps.getEs().size()).iterator();
   
}

package tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Map {
  public static <E, T extends Collection<E>>
  T of(Function<E,E>f,T seq){
    try {
      T result = getInstance(seq);
      for(E e:seq){result.add(f.apply(e));}
      return result;}
    catch (InstantiationException | IllegalAccessException e1) {
      throw Assertions.codeNotReachable(e1.toString());}
    }
  @SuppressWarnings("unchecked") private static <E,T extends Collection<E>> T getInstance(T seq) throws InstantiationException, IllegalAccessException {
    if(seq instanceof List<?>){
      return (T) new ArrayList<E>();
      }
    T result = (T)seq.getClass().newInstance();
    return result;
  }
  public static <E>
  Optional<E> of(Function<E,E>f,Optional<E> el){
      if(!el.isPresent()){return Optional.empty();}
      return Optional.of(f.apply(el.get()));
    }
    
  }


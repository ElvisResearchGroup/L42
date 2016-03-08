package tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Map {
  public static <E1,E2, T extends List<E2>>
  List<E1> of(Function<E2,E1>f,T seq){  return seq.stream().map(f).collect(Collectors.toList());   }

  public static <E1,E2, T extends Set<E2>>
  Set<E1> of(Function<E2,E1>f,T seq){  return seq.stream().map(f).collect(Collectors.toSet());   }


  public static <E1,E2>
  Optional<E1> of(Function<E2,E1>f,Optional<E2> el){
      if(!el.isPresent()){return Optional.empty();}
      return Optional.of(f.apply(el.get()));
    }

  }


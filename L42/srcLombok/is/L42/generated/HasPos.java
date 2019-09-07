package is.L42.generated;

import java.util.Collections;
import java.util.List;

public interface HasPos{
  Pos pos();
  default List<Pos>poss(){return Collections.singletonList(pos());}}

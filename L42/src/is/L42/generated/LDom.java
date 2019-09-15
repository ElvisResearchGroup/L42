package is.L42.generated;

import java.util.List;
public interface LDom{
  int uniqueNum();
  default boolean hasUniqueNum(){return uniqueNum()!=-1;}
  public static <T extends HasKey> T _elem(List<T> ts,LDom key){
    for(T t:ts){if(t.key().equals(key)){return t;}}
    return null;
    }
  interface HasKey{LDom key();}
  }

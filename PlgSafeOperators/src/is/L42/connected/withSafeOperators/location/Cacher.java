package is.L42.connected.withSafeOperators.location;

public abstract class Cacher<T> {
  private T inner=null;
  abstract T cache();
  public T get(){
    if(inner==null){return inner=cache();}
    return inner;
    }

}

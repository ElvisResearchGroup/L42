package is.L42.cache;

import java.util.Set;

/**
 * A class that contains either a normalized object or a ring
 * of objects
 * 
 * @author Claire
 */
public class NormResult<T> {
  
  private final T result;
  private final boolean isPartOfCircle;
  private final Set<Object> circle;
  
  public NormResult(T result) {
    this.result = result;
    this.isPartOfCircle = false;
    this.circle = null;
    }
  
  public NormResult(Set<Object> circle) {
    this.result = null;
    this.isPartOfCircle = true;
    this.circle = circle;
    }
  
  public boolean hasResult() { return !isPartOfCircle; }
  
  public T result() { return result; }
  
  public Set<Object> circle() { return this.circle; }

}

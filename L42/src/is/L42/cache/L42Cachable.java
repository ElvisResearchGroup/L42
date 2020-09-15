package is.L42.cache;

public interface L42Cachable<T> {
  
  Object[] allFields();
  
  /**
   * Sets the field at index i. Returns 
   * <code>ArrayIndexOutOfBoundsException</code> if
   * the numbered field doesn't exist.
   * 
   * @param i index of the field
   * @param o the field's new value.
   */
  void setField(int i, Object o);
  
  /**
   * Sets the field at index i. Returns 
   * <code>ArrayIndexOutOfBoundsException</code> if
   * the numbered field doesn't exist.
   * 
   * @param i index of the field
   * @return the field's value.
   */
  Object getField(int i);
  
  /** 
   * @return The number of accessible fields this class contains
   */
  int numFields();
  
  /**
   * @return A reference to my cache object
   */
  L42Cache<T,?> myCache();
  
  default boolean isNorm() {
    return myNorm() != null;
    }

  /**
   * Sets the norm version of this object
   * 
   * @param t
   */
  void setNorm(T t);
  
  /**
   * @return The normalized version of this object, or
   * <code>null</code> if there is none.
   */
  T myNorm();
  
  /**
   * @return a new, blank instance of this class with no initialized fields
   */
  T newInstance();
  
}

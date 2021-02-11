package is.L42.cache;

/**
 * A wrapper class for integer references to circularlly
 * referenced objects. Used in keys.
 * 
 * @author Claire
 */
public class KeyVarID {
  
  private final int value;
  
  public KeyVarID(int value) {
    assert value >= 0;
    this.value = value;
    }
  
  public KeyVarID offset(int num){
    return new KeyVarID(this.value + num);
    }
  
  public int value() { return this.value; }
  
  public int hashCode() {
    return value;
    }
  
  public boolean equals(Object o) {
    if(o instanceof KeyVarID) { return ((KeyVarID) o).value == this.value; }
    return false;
    }
  
  public String toString() { return String.format("v%d", value); }

}

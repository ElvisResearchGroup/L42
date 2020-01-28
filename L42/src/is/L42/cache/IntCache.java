package is.L42.cache;

import java.util.List;

import is.L42.tools.General;

public class IntCache implements Cache<Integer> {

  @Override 
  public void addObjectOverride(KeyNorm2D key, Integer value) {}
  
  @Override
  public Integer normalize(Integer t) { return t; }
  
  @Override
  public NormResult<Integer> normalizeInner(Integer t, List<Object> chain) {
    return new NormResult<Integer>(t);
    }
  
  @Override
  public boolean isNorm(Integer t) { return true; }

  @Override
  public boolean structurallyEquals(Integer t1, Integer t2) {
    return t1.intValue() == t2.intValue();
    }
  
  @Override
  public boolean identityEquals(Integer t1, Integer t2) {
    return t1.intValue() == t2.intValue();
    }

  public Object f(Integer t, int i) {
    throw new ArrayIndexOutOfBoundsException();
    }

  @Override
  public Object[] f(Integer t) {
    return new Object[0];
    }

  @Override
  public void f(Integer t, Object o, int i) {
    throw new ArrayIndexOutOfBoundsException();
    }

  @Override
  public String typename(Integer t) {
    return "£nativeint";
    }

  @Override
  public KeyNorm2D computeKeyNN(Integer t) {
    return this.simpleKey(t);
    }

  @Override
  public NormResult<Integer> computeKeyNNInner(Integer t, List<Object> chain) {
    return new NormResult<Integer>(t);
    }

  @SuppressWarnings("rawtypes") 
  @Override 
  public Cache rawFieldCache(int i) {
    return this;
    }
  
  @Override
  public KeyNorm2D simpleKey(Integer t) {
    Object[][] key = new Object[][] {{ this, t.intValue()}};
    return new KeyNorm2D(key);
    }
  
  @Override
  public boolean isValueType() {
    return true;
    }
  }

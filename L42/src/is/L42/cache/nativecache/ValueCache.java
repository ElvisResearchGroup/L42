package is.L42.cache.nativecache;

import java.util.List;

import is.L42.cache.L42Cache;
import is.L42.cache.KeyNorm2D;
import is.L42.cache.NormResult;
import is.L42.tools.General;

public abstract class ValueCache<Type> implements L42Cache<Type> {

  @Override 
  public void addObjectOverride(KeyNorm2D key, Type value) {}
  
  @Override
  public Type normalize(Type t) { return t; }
  
  @Override
  public NormResult<Type> normalizeInner(Type t, List<Object> chain) {
    return new NormResult<Type>(t);
    }
  
  @Override
  public boolean isNorm(Type t) { return true; }
  
  protected abstract boolean valueCompare(Type t1, Type t2);

  @Override
  public boolean structurallyEquals(Type t1, Type t2) {
    return valueCompare(t1, t2);
    }
  
  @Override
  public boolean identityEquals(Type t1, Type t2) {
    return valueCompare(t1, t2);
    }

  public Object f(Type t, int i) {
    throw new ArrayIndexOutOfBoundsException();
    }

  @Override
  public Object[] f(Type t) {
    return new Object[0];
    }

  @Override
  public void f(Type t, Object o, int i) {
    throw new ArrayIndexOutOfBoundsException();
    }

  @Override
  public KeyNorm2D computeKeyNN(Type t) {
    return this.simpleKey(t);
    }

  @Override
  public NormResult<Type> computeKeyNNInner(Type t, List<Object> chain) {
    return new NormResult<Type>(t);
    }

  @SuppressWarnings("rawtypes") 
  @Override 
  public L42Cache rawFieldCache(int i) {
    return this;
    }
  
  @Override
  public KeyNorm2D simpleKey(Type t) {
    Object[][] key = new Object[][] {{ this, t }};
    return new KeyNorm2D(key);
    }
  
  @Override
  public boolean isValueType() {
    return true;
    }
  }

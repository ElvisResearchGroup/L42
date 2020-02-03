package is.L42.cache.tests;

import static is.L42.cache.L42CacheMap.expandedKey;
import static is.L42.cache.L42CacheMap.getCacheObject;
import static is.L42.cache.L42CacheMap.getKey;
import static is.L42.cache.L42CacheMap.normalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static is.L42.nativeCode.Flags.ImmElem;
import static is.L42.nativeCode.Flags.MutElem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Supplier;

import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.L42Cachable;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;
import is.L42.cache.exampleobjs.S1;
import is.L42.nativeCode.Flags;

public class NormalizationTests {
  
  enum Donut { spy, bread };
  
  @Test
  public void __testL42List() {
    L42List<String> list = new L42List<String>(String.class);
    assert list.length() == 0;
    assert list.size() == 0;
    assertThrows(IndexOutOfBoundsException.class, () -> { list.getImm(0); });
    assertThrows(IndexOutOfBoundsException.class, () -> { list.getMut(0); });
    assert list.getUnderlyingList().size() == 2;
    list.addAsImm("I am imm");
    assert list.length() == 1;
    assert list.getImm(0).equals("I am imm");
    assertThrows(NullPointerException.class, () -> { list.getMut(0); });
    assert list.getUnderlyingList().size() == 4;
    assert list.getUnderlyingList().get(2).equals("I am imm");
    assert ((Flags) ((Object) list.getUnderlyingList().get(3))) == ImmElem;
    list.addAsMut("I am mut");
    assert list.getMut(1).equals("I am mut");
    assertThrows(NullPointerException.class, () -> { list.getImm(1); });
    assert list.getUnderlyingList().size() == 6;
    assert list.getUnderlyingList().get(4).equals("I am mut");
    assert ((Flags) ((Object) list.getUnderlyingList().get(5))) == MutElem;
    }
  
  @Test
  public void identityTest() {
    testSelfProperties(() -> { return new I(); });
    }
  
  @Test
  public void testA() {
    int i1 = (int) (Math.random() * 100000);
    int i2 = (int) (Math.random() * 100000);
    A a = testSelfProperties(() -> { return new A(i1, i2); });
    assertEquals(i1, a.getI1());
    assertEquals(i2, a.getI2());
    }
  
  @Test 
  public void testTree() {
    testSelfProperties(() -> {
      A a1 = new A(50, 1);
      A a2 = new A(0, 1);
      R1 r1 = new R1(a1);
      R2 r2 = new R2(r1, a2);
      R2 r3 = new R2(r2, a1);
      R1 r4 = new R1(new I());
      R2 r5 = new R2(r3, r4);
      return r5;
      });
    }
  
  @Test
  public void testBasicCircle() {
    testSelfProperties(() -> {
      R1 r1 = new R1(null);
      r1.referenced = r1;
      return r1;
      });
    }
  
  @Test
  public void testLongerCircle() {
    R1 myR = testSelfProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      return r1;
      });
    assertTrue(myR == myR.referenced);
    }
  
  @Test
  public void testEvenLongerCircle() {
    R1 myR = testSelfProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      R1 r4 = new R1(r3);
      R1 r5 = new R1(r4);
      R1 r6 = new R1(r5);
      R1 r7 = new R1(r6);
      r1.referenced = r7;
      return r1;
      });
    assertTrue(myR == myR.referenced);
    }
  
  @Test
  public void testLongShortCircleEQ() {
    testBiProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      return r1;
      }, () -> {
      R1 r1 = new R1(null);
      r1.referenced = r1;
      return r1;
      }, false);
    }
  
  @Test 
  public void testSecondKeyUse() {
    R1 r1 = new R1(null);
    R1 r2 = new R1(r1);
    R1 r3 = new R1(r2);
    r1.referenced = r3;
    r1 = normalize(r1);
    assertTrue(r1 == normalize(new R1(r1)));
    assertTrue(r1 == normalize(r2));
    assertTrue(r1 == normalize(r3));
    assertTrue(r1 == normalize(new R1(r3)));
    assertTrue(r1 == r1.myNorm());
    assertTrue(r1 == r2.myNorm());
    assertTrue(r1 == r3.myNorm());
    }
  
  @Test
  public void testString() {
    testSelfProperties(() -> {
      return new StringBuilder().append("a short little string").toString();
      });
    }
  
  @Test
  public void testNullField() {
    testSelfProperties(() -> {
      return new S1(null);
      });
    }
  
  @Test
  public void testDoubleFieldCircle() {
    testSelfProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(null);
      R2 r3 = new R2(r1, r2);
      r1.referenced = r3;
      r2.referenced = r3;
      return r3;
      });
  }
  
  @Test
  public void testNullObjectField() {
    R1 r1null = new R1(null);
    assertThrows(NullPointerException.class, () -> { normalize(r1null); });
    assertThrows(NullPointerException.class, () -> { getKey(r1null, false); });
    assertThrows(NullPointerException.class, () -> { expandedKey(r1null, true, false); });
    }
  
  @Test
  public void testALWork() {
    testSelfProperties(() -> {
      L42List<String> l1 = new L42List<String>(String.class);
      l1.addAsImm("Hello");
      l1.addAsMut("Goodbye");
      return l1.getUnderlyingList();
      });
    }
  
  @Test
  public void testALWork2() {
    testSelfProperties(() -> {
      L42List<R1> l1 = new L42List<R1>(R1._class);
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      l1.addAsImm(r1);
      l1.addAsMut(r2);
      return l1.getUnderlyingList();
      });
    }
  
  @Test
  public void testALWork3() {
    testSelfProperties(() -> {
      L42List<Object> l1 = new L42List<Object>(Object.class);
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      l1.addAsImm(r1);
      l1.addAsMut(r2);
      return l1.getUnderlyingList();
      });
    }
  
  
  @SuppressWarnings({ "rawtypes" }) 
  @Test
  public void testALCircle() {
    testSelfProperties(() -> {
      L42List<ArrayList> l1 = new L42List<ArrayList>(ArrayList.class);
      L42List<ArrayList> l2 = new L42List<ArrayList>(ArrayList.class);
      L42List<ArrayList> l3 = new L42List<ArrayList>(ArrayList.class);
      l1.addAsImm(l2.getUnderlyingList());
      l2.addAsImm(l3.getUnderlyingList());
      l3.addAsImm(l1.getUnderlyingList());
      return l1.getUnderlyingList();
      });
    }
  
  @Test
  public void testParialALCircle() {
    testSelfProperties(() -> {
      L42List<Object> l1 = new L42List<Object>(Object.class);
      L42List<Object> l2 = new L42List<Object>(Object.class);
      L42List<Object> l3 = new L42List<Object>(Object.class);
      l1.addAsImm(l2.getUnderlyingList());
      l2.addAsImm(l3.getUnderlyingList());
      R1 r1 = new R1(l1.getUnderlyingList());
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      l3.addAsImm(r3);
      return l1.getUnderlyingList();
      });
    }
  
  @Test
  public void testAnnoying() {
    testSelfProperties(() -> {
      L42List<R1> l1 = new L42List<R1>(R1._class);
      l1.addAsImm(new R1(l1.getUnderlyingList()));
      l1.addAsImm(new R1(new I()));
      l1.addAsMut(null);
      return l1.getUnderlyingList();
      });
    }
  
  private static <T> T testSelfProperties(Supplier<T> supplier) {
    return testBiProperties(supplier, supplier, true);
    }
  
  private static <T> T testBiProperties(Supplier<T> supplier1, Supplier<T> supplier2, boolean readEQ) {
    T n1old, n2old;
    T n1 = n1old = supplier1.get();
    T n2 = n2old = supplier2.get();
    L42Cache<T> cache = getCacheObject(n1);
    assertTrue(n1 != n2);
    if(readEQ) { assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false)); }
    n1 = normalize(n1);
    n2 = normalize(n2);
    if(!cache.isValueType()) {
      assertTrue(cache.getMyNorm(n1old) == n1);
      assertTrue(cache.getMyNorm(n2old) == n1);
      }
    assertTrue(cache.identityEquals(n1, n2));
    testDeepFieldEQ(n1, n2, Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>())); 
    assertEquals(getKey(n1, false), getKey(n2, false));
    assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false));
    assertEquals(getKey(n1, true), getKey(n2, true));
    assertEquals(expandedKey(n1, true, true), expandedKey(n2, true, true));
    return n2;
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  private static void testDeepFieldEQ(Object n1, Object n2, Set<Object> alreadyChecked) {
    if(alreadyChecked.contains(n1) && alreadyChecked.contains(n2)) { return; };
    if(n1 == null) { assertTrue(n2 == null); return; }
    L42Cache cache = getCacheObject(n1);
    assertTrue(cache.identityEquals(n1, n2));
    alreadyChecked.add(n1);
    Object[] n1f = cache.f(n1);
    Object[] n2f = cache.f(n2);
    for(int i = 0; i < n1f.length; i++) { testDeepFieldEQ(n1f[i], n2f[i], alreadyChecked); }
    }
  
  private static final class L42List<T> {
    private final ArrayList<Object> underlying = new ArrayList<>();
    
    public L42List(Class<T> myClass) {
      underlying.add(L42CacheMap.getCacheObject(myClass));
      underlying.add(null);
    }
    
    public int length() {
      return (underlying.size() - 2) / 2;
      }
    
    public int size() {
      return this.length();
      }
    
    public void addAsImm(T t) {
      underlying.add(t);
      underlying.add(ImmElem);
      }
    
    public void addAsMut(T t) {
      underlying.add(t);
      underlying.add(MutElem);
      }
    
    public boolean isImm(int pos) {
      pos *= 2;
      pos += 2;
      return underlying.get(pos + 1) == ImmElem;
      }
    
    @SuppressWarnings("unchecked") 
    public T getImm(int pos) {
      if(isImm(pos)) {
        pos *= 2;
        pos += 2;
        return (T) underlying.get(pos);
        } else {
        throw new NullPointerException();
        }
      }
    
    @SuppressWarnings("unchecked") 
    public T getMut(int pos) {
      if(!isImm(pos)) {
        pos *= 2;
        pos += 2;
        return (T) underlying.get(pos);
        } else {
        throw new NullPointerException();
        }
      }
    
    @SuppressWarnings("unchecked") 
    public ArrayList<T> getUnderlyingList() { return (ArrayList<T>) underlying; }
    
    }

}

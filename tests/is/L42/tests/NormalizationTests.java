package is.L42.tests;

import static is.L42.cache.L42CacheMap.getCacheObject;
import static is.L42.cache.L42CacheMap.getKey;
import static is.L42.cache.L42CacheMap.normalize_internal;
import static is.L42.nativeCode.Flags.ImmElem;
import static is.L42.nativeCode.Flags.MutElem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import is.L42.cache.KeyNorm2D;
import is.L42.cache.L42Cache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;
import is.L42.cache.exampleobjs.S1;
import is.L42.cache.exampleobjs.Singleton;
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
    assert list.getUnderlyingList().size() == 1;
    list.addAsImm("I am imm");
    assert list.length() == 1;
    assert list.getImm(0).equals("I am imm");
    assertThrows(NullPointerException.class, () -> { list.getMut(0); });
    assert list.getUnderlyingList().size() == 3;
    assert list.getUnderlyingList().get(1).equals("I am imm");
    assert ((Flags) ((Object) list.getUnderlyingList().get(2))) == ImmElem;
    list.addAsMut("I am mut");
    assert list.getMut(1).equals("I am mut");
    assertThrows(NullPointerException.class, () -> { list.getImm(1); });
    assert list.getUnderlyingList().size() == 5;
    assert list.getUnderlyingList().get(3).equals("I am mut");
    assert ((Flags) ((Object) list.getUnderlyingList().get(4))) == MutElem;
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
  public void testSingleton() {
    testSelfProperties(() -> {
      R1 r1 = new R1(Singleton.construct());
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
    r1 = normalize_internal(r1);
    assertTrue(r1 == normalize_internal(new R1(r1)));
    assertTrue(r1 == normalize_internal(r2));
    assertTrue(r1 == normalize_internal(r3));
    assertTrue(r1 == normalize_internal(new R1(r3)));
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
    assertThrows(AssertionError.class, () -> { normalize_internal(r1null); });
    assertThrows(AssertionError.class, () -> { getKey(r1null, false); });
    assertThrows(AssertionError.class, () -> { eKey(r1null, true, false); });
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
  
  @Test
  public void testSubCircle() {
    R2 res = testSelfProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      R2 r21 = new R2(null, r1);
      R2 r22 = new R2(null, r21);
      R2 r23 = new R2(r22, r21);
      r22.referenced = r23;
      r21.referenced = r22;
      return r21;
      });
    assertTrue(((R1) res.referenced2).referenced == ((R1) ((R1) res.referenced2).referenced).referenced);
    assertTrue(((R2) res.referenced).referenced == res.referenced);
    }
  
  @SuppressWarnings({ "unchecked", "rawtypes" }) 
  @Test
  public void testFivePointedStar() {
    for(int k = 0; k < 100; k++) {
      ArrayList<ArrayList<?>> al = testSelfProperties(() -> {
        List<L42List<ArrayList<?>>> rlist = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
          rlist.add((L42List<ArrayList<?>>) new L42List(ArrayList.class));
          }
        List<L42List<ArrayList<?>>> rlist2 = List.copyOf(rlist);
        for(int i = 0; i < 5; i++) {
          Collections.shuffle(rlist);
          for(int j = 0; j < 5; j++) {
            rlist2.get(i).addAsImm(rlist.get(j).getUnderlyingList());
            }
          }
        return rlist.get(0).getUnderlyingList();
        }, false);
      for(int i =1; i < al.size(); i += 2) {
        assert al.get(i) == al;
        }
      }
    }
  
  @Test
  public void testMarcosCase() {
    testBiProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(null);
      R2 r3 = new R2(r1, null);
      R2 r4 = new R2(r2, r3);
      r3.referenced2 = r4;
      r1.referenced = r3;
      r2.referenced = r4;
      R1 r5 = new R1(r4);
      return r5;
      }, () -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(null);
      R2 r3 = new R2(r1, null);
      R2 r4 = new R2(r2, r3);
      r3.referenced2 = r4;
      r1.referenced = r3;
      r2.referenced = r3;
      R1 r5 = new R1(r4);
      return r5;
      }, false);
  }
  
  @Test
  public void testAlkyne() {
    R2 res = testSelfProperties(() -> {
      R2 r1 = new R2(null, "Hello");
      R2 r2 = new R2(r1, "Goodbye");
      r1.referenced = r2;
      return r1;
      });
    assertTrue(res.referenced2.equals("Hello"));
    assertTrue(((R2) res.referenced).referenced2.equals("Goodbye"));
    }
  
  private static <T> T testSelfProperties(Supplier<T> supplier) {
    return testBiProperties(supplier, supplier, true);
    }
  
  private static <T> T testSelfProperties(Supplier<T> supplier, boolean readEQ) {
    return testBiProperties(supplier, supplier, readEQ);
    }
  private static KeyNorm2D eKey(Object o,boolean all,boolean norm){
    return new L42CacheMap.KeyExpander(o,all,norm).expandedKey();
    }
  private static <T> T testBiProperties(Supplier<T> supplier1, Supplier<T> supplier2, boolean readEQ) {
    T n1old, n2old;
    T n1 = n1old = supplier1.get();
    T n2 = n2old = supplier2.get();
    L42Cache<T> cache = getCacheObject(n1);
    T dup1 = cache.dup(n1);
    T dup2 = cache.dup(n2);
    var kDup1=eKey(dup1,true,false);
    var kN1=eKey(n1,true,false);
    if(!kDup1.equals(kN1)){
      System.out.print("");
      kDup1.equals(kN1);
    }
    assertEquals(kDup1,kN1 );
    assertEquals(eKey(dup2,true,false), eKey(n2,true,false));
    assertTrue(n1 != n2);
    if(readEQ) { assertEquals(eKey(n1, true, false), eKey(n2, true, false)); }
    n1 = normalize_internal(n1);
    n2 = normalize_internal(n2);
    if(!cache.isValueType()){
      var c1=cache.getMyNorm(n1old);
      var c2=cache.getMyNorm(n2old);
      assertTrue(c1== n1);
      assertTrue(c2==n1);
      }
    assertTrue(cache.identityEquals(n1, n2));
    testDeepFieldEQ(n1, n2, L42CacheMap.identityHashSet()); 
    assertEquals(getKey(n1, false), getKey(n2, false));
    assertEquals(eKey(n1, true, false), eKey(n2, true, false));
    assertEquals(getKey(n1, true), getKey(n2, true));
    assertEquals(eKey(n1, true, true), eKey(n2, true, true));
    return n2;
    }
  
  private static <T> void testDeepFieldEQ(T n1, T n2, Set<Object> alreadyChecked) {
    if(alreadyChecked.contains(n1) && alreadyChecked.contains(n2)) { return; };
    if(n1 == null) { assertTrue(n2 == null); return; }
    L42Cache<T> cache = getCacheObject(n1);
    assertTrue(cache.identityEquals(n1, n2));
    alreadyChecked.add(n1);
    int size=cache.fn(n1);
    assert size==cache.fn(n2);
    for(int i = 0; i < size; i++){
      testDeepFieldEq(cache,i,n1,n2,alreadyChecked); 
      }
    }
  static <T>void testDeepFieldEq(L42Cache<T>cache,int i,T n1,T n2,Set<Object>alreadyChecked){
    testDeepFieldEQ(cache.f(n1,i),cache.f(n2,i), alreadyChecked);
    }
  private static final class L42List<T> {
    private final ArrayList<Object> underlying = new ArrayList<>();
    
    public L42List(Class<T> myClass) {
      underlying.add(null);
    }
    
    public int length() {
      return (underlying.size() - 1) / 2;
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
      pos += 1;
      return underlying.get(pos + 1) == ImmElem;
      }
    
    @SuppressWarnings("unchecked") 
    public T getImm(int pos) {
      if(isImm(pos)) {
        pos *= 2;
        pos += 1;
        return (T) underlying.get(pos);
        } else {
        throw new NullPointerException();
        }
      }
    
    @SuppressWarnings("unchecked") 
    public T getMut(int pos) {
      if(!isImm(pos)) {
        pos *= 2;
        pos += 1;
        return (T) underlying.get(pos);
        } else {
        throw new NullPointerException();
        }
      }
    
    @SuppressWarnings("unchecked") 
    public ArrayList<T> getUnderlyingList() { return (ArrayList<T>) underlying; }
    
    }

}

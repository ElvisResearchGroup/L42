package is.L42.cache.tests;

import static is.L42.cache.RootCache.expandedKey;
import static is.L42.cache.RootCache.getCacheObject;
import static is.L42.cache.RootCache.getKey;
import static is.L42.cache.RootCache.normalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Supplier;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;
import is.L42.cache.exampleobjs.S1;

public class NormalizationTests {
  
  @Test
  public void identityTest() {
    testSelfProperties(() -> { return new I(); });
    }
  
  @Test
  public void testA() {
    int i1 = (int) (Math.random() * 100);
    int i2 = (int) (Math.random() * 100);
    testSelfProperties(() -> { return new A(i1, i2); });
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
    testSelfProperties(() -> {
      R1 r1 = new R1(null);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      r1.referenced = r3;
      return r1;
      });
    }
  
  @Test
  public void testEvenLongerCircle() {
    testSelfProperties(() -> {
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
      });
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
  
  private static void testSelfProperties(Supplier<Object> supplier) {
    testBiProperties(supplier, supplier);
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  @Test
  public void testALCircle() {
    testSelfProperties(() -> {
      List l1 = new ArrayList();
      List l2 = new ArrayList();
      List l3 = new ArrayList();
      l1.add(l2);
      l2.add(l3);
      l3.add(l1);
      return l1;
      });
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  @Test
  public void testParialALCircle() {
    testSelfProperties(() -> {
      List l1 = new ArrayList();
      List l2 = new ArrayList();
      List l3 = new ArrayList();
      l1.add(l2);
      l2.add(l3);
      R1 r1 = new R1(l1);
      R1 r2 = new R1(r1);
      R1 r3 = new R1(r2);
      l3.add(r3);
      return l1;
      });
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  private static void testBiProperties(Supplier<Object> supplier1, Supplier<Object> supplier2) {
    Object n1old, n2old;
    Object n1 = n1old = supplier1.get();
    Object n2 = n2old = supplier2.get();
    Cache cache = getCacheObject(n1);
    assertTrue(n1 != n2);
    assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false));
    n1 = normalize(n1);
    n2 = normalize(n2);
    if(n1old instanceof ForeignObject) {
      assertTrue(((ForeignObject) n1old).myNorm() == n1);
      assertTrue(((ForeignObject) n2old).myNorm() == n1);
      }
    assertTrue(cache.identityEquals(n1, n2));
    testDeepFieldEQ(n1, n2, Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>())); 
    assertEquals(getKey(n1, false), getKey(n2, false));
    assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false));
    assertEquals(getKey(n1, true), getKey(n2, true));
    assertEquals(expandedKey(n1, true, true), expandedKey(n2, true, true));
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  private static void testDeepFieldEQ(Object n1, Object n2, Set<Object> alreadyChecked) {
    if(alreadyChecked.contains(n1) && alreadyChecked.contains(n2)) { return; };
    if(n1 == null) { assertTrue(n2 == null); return; }
    Cache cache = getCacheObject(n1);
    assertTrue(cache.identityEquals(n1, n2));
    alreadyChecked.add(n1);
    Object[] n1f = cache.f(n1);
    Object[] n2f = cache.f(n2);
    for(int i = 0; i < n1f.length; i++) { testDeepFieldEQ(n1f[i], n2f[i], alreadyChecked); }
    }

}

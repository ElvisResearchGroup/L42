package is.L42.cache.tests;

import static is.L42.cache.RootCache.expandedKey;
import static is.L42.cache.RootCache.getCacheObject;
import static is.L42.cache.RootCache.getKey;
import static is.L42.cache.RootCache.normalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Supplier;

import is.L42.cache.Cache;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;

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
  
  private static void testSelfProperties(Supplier<Object> supplier) {
    testBiProperties(supplier, supplier);
    }
  
  private static void testBiProperties(Supplier<Object> supplier1, Supplier<Object> supplier2) {
    Object n1 = supplier1.get();
    Object n2 = supplier2.get();
    assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false));
    n1 = normalize(n1);
    n2 = normalize(n2);
    assertTrue(n1 == n2);
    testDeepFieldEQ(n1, n2, Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>())); 
    assertEquals(getKey(n1, false), getKey(n2, false));
    assertEquals(expandedKey(n1, true, false), expandedKey(n2, true, false));
    }
  
  @SuppressWarnings({ "rawtypes", "unchecked" }) 
  private static void testDeepFieldEQ(Object n1, Object n2, Set<Object> alreadyChecked) {
    if(alreadyChecked.contains(n1) && alreadyChecked.contains(n2)) { return; };
    assertTrue(n1 == n2);
    alreadyChecked.add(n1);
    Cache cache = getCacheObject(n1);
    Object[] n1f = cache.f(n1);
    Object[] n2f = cache.f(n2);
    for(int i = 0; i < n1f.length; i++) { testDeepFieldEQ(n1f[i], n2f[i], alreadyChecked); }
    }

}

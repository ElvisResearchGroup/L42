package is.L42.cache.tests;

import java.util.ArrayList;
import java.util.List;

import is.L42.cache.Cache;
import is.L42.cache.ForeignObject;
import is.L42.cache.ForeignObjectCache;
import is.L42.cache.KeyNorm2D;
import is.L42.cache.RootCache;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;

public class TemporaryNonJUnitTestsForCaching {
	
	public static final void main(String[] args)
	{
		I i1 = new I();
		I i2 = new I();
		i1 = RootCache.normalize(i1);
		i2 = RootCache.normalize(i2);
		I i3 = RootCache.normalize(new I());
		
		assert i1 != null;
		assert i1 == i2;
		assert i2 == i3;
		
		A a1 = RootCache.normalize(new A(50, 50));
		A a2 = RootCache.normalize(new A(50, 0));
		A a3 = RootCache.normalize(new A(50, 1));
		
		assert a1 != a2;
		assert a1 != a3;
		assert a2 != a3;
		
		assert a1.toString().equals("A [i1=50, i2=50]");
		assert a2.toString().equals("A [i1=50, i2=0]");
		assert a3.toString().equals("A [i1=50, i2=1]");
		
		A a4 = RootCache.normalize(new A(50, 0));
		
		assert a4 == a2;
		
		R1 r1 = new R1(null);
		R1 r2 = new R1(r1);
		R1 r3 = new R1(r2);
		r1.referenced = r3;

    assert r1!=r2;
    assert r1!=r3;
    assert r2!=r3;
		
		r1 = RootCache.normalize(r1);
		r2 = RootCache.normalize(r2);
		r3 = RootCache.normalize(r3);

//    assert r1==r2;
//    assert r1==r3;
//    assert r2==r3;


		R1 r11 = new R1(null);
		R1 r21 = new R1(r11);
		R1 r31 = new R1(r21);
		r11.referenced = r31;
		
		r11 = RootCache.normalize(r1);
		r21 = RootCache.normalize(r2);
		r31 = RootCache.normalize(r3);
		
		assert r11 == r1;
		assert r21 == r2;
		assert r31 == r3;
		
		R1 C1 = new R1(null);
		R1 C2 = new R1(null);
		R2 B = new R2(C1, C2);
		C1.referenced = B;
		C2.referenced = B;
		R1 A = new R1(B);
		A = RootCache.normalize(A);
		
		R1 A2 = RootCache.normalize(new R1(B));
		
		assert A == A2;
		
		R1 X = new R1(null);
		R2 Y = new R2(X, i1);
		R1 Z = new R1(Y);
		X.referenced = Z;
		
		X = RootCache.normalize(X);
		
		assert ((R2) ((R1) X.referenced).referenced).referenced2 == RootCache.normalize(new I());
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(new I());
		list.add(new A(50, 50));
		list = RootCache.normalize(list);
		
		assert list.get(0) == i1;
		assert list.get(1) == a1;
		
		List<Object> list2 = new ArrayList<Object>(list);
		assert list2 != list;
		list2 = RootCache.normalize(list2);
  	assert list2 == list;
  	{
    List<Object> list3 = new ArrayList<Object>();
    list3.add(new I());
    list3.add(new A(50, 50));
    assert list3 != list;
    list3 = RootCache.normalize(list3);
    assert list3 == list;
  	}{
    List<Object> list3 = new ArrayList<Object>();
    list3.add(50);
    List<Object> list4 = new ArrayList<Object>();
    list4.add(50);
    assert list3 != list4;
    list3 = RootCache.normalize(list3);
    list4 = RootCache.normalize(list4);
    assert list3 == list4;
  	}{
  	IntBox box1=new IntBox(30000);
  	IntBox box2=new IntBox(30000);
  	assert box1!=box2;
  	box1=RootCache.normalize(box1);
  	box2=RootCache.normalize(box2);
  	assert box1==box2;
  	}
  	
		KeyNorm2D key = RootCache.getCacheObject(B).computeKeyNN(B);
		KeyNorm2D key2 = RootCache.getCacheObject(list2).computeKeyNN(list2);
		KeyNorm2D key3 = RootCache.expandedKey(A2, true, false);
		
		System.out.println("All tests succeeded");
		
	}

}

class IntBox implements ForeignObject<IntBox> {
  private static final Cache<IntBox> myCache=
    RootCache.addCacheableType(IntBox.class,new ForeignObjectCache<IntBox>());
  int f;IntBox(int f){this.f=f;}
  @Override public Object[] allFields() {return new Object[]{f};}
  @Override public void setField(int i, Object o) { this.f=(Integer)o;}
  @Override public Cache<IntBox> myCache() {return myCache;}
  @Override public String typename() {return "IntBox";}
  private boolean norm = false;
  @Override public boolean norm(boolean set) {return norm |= set;}
  }

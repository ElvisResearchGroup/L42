package is.L42.cache.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import is.L42.cache.L42StandardCache;
import is.L42.cache.KeyNorm2D;
import is.L42.cache.L42Cachable;
import is.L42.cache.L42Cache;
import is.L42.cache.LoopCache;
import is.L42.cache.L42CacheMap;
import is.L42.cache.exampleobjs.A;
import is.L42.cache.exampleobjs.I;
import is.L42.cache.exampleobjs.R1;
import is.L42.cache.exampleobjs.R2;

public class TemporaryNonJUnitTestsForCaching {
	public static final void main(String[] args){
	  
		I i1 = new I();
		I i2 = new I();
		i1 = L42CacheMap.normalize(i1);
		i2 = L42CacheMap.normalize(i2);
		I i3 = L42CacheMap.normalize(new I());
		
		assert i1 != null;
		assert i1 == i2;
		assert i2 == i3;
		
		A a1 = L42CacheMap.normalize(new A(50, 50));
		A a2 = L42CacheMap.normalize(new A(50, 0));
		A a3 = L42CacheMap.normalize(new A(50, 1));
		
		assert a1 != a2;
		assert a1 != a3;
		assert a2 != a3;
		
		assert a1.toString().equals("A [i1=50, i2=50]");
		assert a2.toString().equals("A [i1=50, i2=0]");
		assert a3.toString().equals("A [i1=50, i2=1]");
		
		A a4 = L42CacheMap.normalize(new A(50, 0));
		
		assert a4 == a2;
		
		R1 r1 = new R1(null);
		R1 r2 = new R1(r1);
		R1 r3 = new R1(r2);
		r1.referenced = r3;

    assert r1!=r2;
    assert r1!=r3;
    assert r2!=r3;
		
		r1 = L42CacheMap.normalize(r1);
		r2 = L42CacheMap.normalize(r2);
		r3 = L42CacheMap.normalize(r3);

    assert r1==r2;
    assert r1==r3;
    assert r2==r3;

		R1 r11 = new R1(null);
		R1 r21 = new R1(r11);
		R1 r31 = new R1(r21);
		r11.referenced = r31;
		
		r11 = L42CacheMap.normalize(r1);
		r21 = L42CacheMap.normalize(r2);
		r31 = L42CacheMap.normalize(r3);
		
		assert r11 == r1;
		assert r21 == r2;
		assert r31 == r3;
		
		R1 C1 = new R1(null);
		R1 C2 = new R1(null);
		R2 B = new R2(C1, C2);
		C1.referenced = B;
		C2.referenced = B;
		
		assert B.referenced != B.referenced2;
		
		R1 A = new R1(B);
		A = L42CacheMap.normalize(A);
		
		//DFA --> C1 == C2
		assert !LoopCache.USE_DFA_MINIMIZATION || B.referenced == B.referenced2;
		
		R1 A2 = L42CacheMap.normalize(new R1(B));
		
		assert A == A2;
		
		R1 X = new R1(null);
		R2 Y = new R2(X, i1);
		R1 Z = new R1(Y);
		X.referenced = Z;
		
		X = L42CacheMap.normalize(X);
		
		assert ((R2) ((R1) X.referenced).referenced).referenced2 == L42CacheMap.normalize(new I());
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(new I());
		list.add(new A(50, 50));
		list = L42CacheMap.normalize(list);
		
		assert list.get(0) == i1;
		assert list.get(1) == a1;
		
		List<Object> list2 = new ArrayList<Object>(list);
		assert list2 != list;
		list2 = L42CacheMap.normalize(list2);
  	assert list2 == list;
  	{
    List<Object> list3 = new ArrayList<Object>();
    list3.add(new I());
    list3.add(new A(50, 50));
    assert list3 != list;
    list3 = L42CacheMap.normalize(list3);
    assert list3 == list;
  	}{
    List<Object> list3 = new ArrayList<Object>();
    list3.add(50);
    List<Object> list4 = new ArrayList<Object>();
    list4.add(50);
    assert list3 != list4;
    list3 = L42CacheMap.normalize(list3);
    list4 = L42CacheMap.normalize(list4);
    assert list3 == list4;
  	}{
  	IntBox box1=new IntBox(30000);
  	IntBox box2=new IntBox(30000);
  	assert box1!=box2;
  	box1=L42CacheMap.normalize(box1);//not calling RootCache.expandedKey(box1,false,true);
  	box2=L42CacheMap.normalize(box2);
  	assert box1==box2;
  	}{
  	IntBox box1=new IntBox(30000);
    IntBox box2=new IntBox(320000);
    IntBox box3=new IntBox(320000);
  	KeyNorm2D box1Key=L42CacheMap.expandedKey(box1,true,false);
  	KeyNorm2D box2Key=L42CacheMap.expandedKey(box2,true,false);
  	KeyNorm2D box3Key=L42CacheMap.expandedKey(box3,true,false);
  	assert !box1Key.equals(box2Key);
  	assert box2Key.equals(box3Key);
  	System.out.println(box1Key.toString());
  	System.out.println(box1.times2());
  	System.out.println(box1.times2());
  	IntBox boxF=new IntBox(20);
  	System.out.println(boxF.fibonacci());
    }{
    R1 r=new R1(null);
    r.referenced=r;
    KeyNorm2D rKey=L42CacheMap.expandedKey(r,true,false);
    assert rKey.equals(rKey);
    }{
    R2 r=new R2(null,null);
    r.referenced=r;
    r.referenced2=r;
    KeyNorm2D rKey=L42CacheMap.expandedKey(r,true,false);
    assert rKey.equals(rKey);
    }{
    R2 r=new R2(null,null);
    R1 rr=new R1(r);
    r.referenced=rr;
    r.referenced2=rr;
    KeyNorm2D rKey=L42CacheMap.expandedKey(r,true,false);
    assert rKey.equals(rKey);
    }
  	
		KeyNorm2D key = L42CacheMap.getCacheObject(B).computeKeyNN(B);
		KeyNorm2D key2 = L42CacheMap.getCacheObject(list2).computeKeyNN(list2);
		KeyNorm2D key3 = L42CacheMap.expandedKey(A2, true, false);
		
		System.out.println("All tests succeeded");
	}

}

/*
 Probably, for all nativeKinds:
 either we can reuse the behaviour of Integer/String
 or we can just implement ForeignObject on all the 
 custom defined nativeKinds like L42£Meta and TrustedIO?
 Could there be an opminized cache for objects like I(), that are all the same?
 //could we also optimize the case of "wrapper objects with only one field"
  
  
 */



//ForeignObject = L42Cachable
//ForeignObjectCache = L42StandardCache
//RootCache,= L42CacheMap
//Cache = L42Cache

interface Box{}

class IntBox implements L42Cachable<IntBox>,Box{
  static final Class<IntBox> _class=IntBox.class;
  private static final L42StandardCache<IntBox> myCache=
    new L42StandardCache<IntBox>("IntBox",IntBox._class);
  static{myCache.lateInitialize(int.class);}
  //setted up to work transparantly for both int.class and Integer.class
  int f;IntBox(int f){this.f=f;}
  @Override public Object[] allFields() {return new Object[]{f};}
  @Override public void setField(int i, Object o) { this.f=(Integer)o;}
  @Override public L42Cache<IntBox> myCache() {return myCache;}
  private IntBox norm;
  @Override public Object getField(int i){return f;}
  @Override public void setNorm(IntBox t) {this.norm=t;}
  @Override public IntBox myNorm() {return this.norm;}

  //int times2(){ return f*2; }
  boolean isTimes2=false;
  int times2=0;
  int auxTimes2(){ 
    System.out.println("double computing");
    return f*2; }
  int times2(){
    if(norm==null){norm=myCache.normalize(this);}
    if(!norm.isTimes2){norm.times2=auxTimes2(); norm.isTimes2=true;}
    return norm.times2;
    }
  //int fibonacci(){..}  
  int auxFibonacci(){
    System.out.println("fibonacci computing");
    if(f==0|| f==1){return 1;}
    return new IntBox(f-1).fibonacci()+new IntBox(f-2).fibonacci();
    }
  boolean isFibonacci;
  int fibonacci;
  int fibonacci(){
    if(norm==null){norm=myCache.normalize(this);}
    if(!norm.isFibonacci){norm.fibonacci=auxFibonacci(); norm.isFibonacci=true;}
    return norm.fibonacci;    
    }
  @Override public int numFields(){return 1;}
  }
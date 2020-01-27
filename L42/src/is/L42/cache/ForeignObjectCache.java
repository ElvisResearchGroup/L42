package is.L42.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForeignObjectCache<T extends ForeignObject<T>> implements Cache<T> {
	
	private final Map<KeyNorm2D, Object> normMap = RootCache.newNormMap();
	
	private void add(KeyNorm2D key, T t) {
		normMap.put(key, t);
		t.norm(true);
		}
	
	 @Override 
	 public void addObjectOverride(KeyNorm2D key, T value) {
	   normMap.put(key, value);
     }
	
	@Override
	public T normalize(T t) {
		NormResult<T> res = normalizeInner(t, new ArrayList<Object>());
		if(res.hasResult()) { return res.result(); }
		else { return LoopCache.normalizeCircle(t, res.circle()); }
		}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public NormResult<T> normalizeInner(T t, List<Object> prevs) {
		if(!this.isNorm(t)) {
			prevs.add(t);
			boolean inCircle = false;
			Object[] fields = t.allFields();
			Set<Object> circle = null;   
			for(int i = 0; i < fields.length; i++) {
				if(prevs.contains(fields[i])) {
					List<Object> sl = prevs.subList(prevs.indexOf(fields[i]), prevs.size());
					if(circle == null) { circle = new HashSet<Object>(sl); }
					else { circle = union(circle, sl); }
					inCircle = true;
					continue;
					}
				Cache cache = RootCache.getCacheObject(fields[i].getClass());
				NormResult res = cache.normalizeInner(fields[i], new ArrayList<Object>(prevs));
				if(res.hasResult()) {
					t.setField(i, fields[i] = res.result());
					} else if(!res.circle().contains(t)) {
					t.setField(i, fields[i] = LoopCache.normalizeCircle(fields[i], res.circle())); 
					} else {
					inCircle = true;
					circle = circle == null ? res.circle() : union(circle, res.circle());
					}
				}
			if(inCircle) { return new NormResult(circle); }
			KeyNorm2D key = this.simpleKey(t);
			if(normMap.containsKey(key)) { return new NormResult(normMap.get(key)); }
			this.add(key, t);
			return new NormResult<T>(t);			
			} else { return new NormResult<T>(t); }
		}
	
	@Override
	public KeyNorm2D computeKeyNN(T t) {
		NormResult<T> res = computeKeyNNInner(t, new ArrayList<Object>());
		if(res.hasResult()) { return this.simpleKey(res.result()); }
		else { return LoopCache.getKeyCircleNN(t, res.circle()); }
		}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public NormResult<T> computeKeyNNInner(T t, List<Object> prevs) {
		prevs.add(t);
		boolean inCircle = false;
		Object[] fields = t.allFields();
		Set<Object> circle = null;   
		for(int i = 0; i < fields.length; i++) {
			if(prevs.contains(fields[i])) {
				List<Object> sl = prevs.subList(prevs.indexOf(fields[i]), prevs.size());
				if(circle == null) { circle = new HashSet<Object>(sl); }
				else { circle = union(circle, sl); }
				inCircle = true;
				continue;
				}
			Cache cache = RootCache.getCacheObject(fields[i].getClass());
			NormResult<T> res = cache.computeKeyNNInner(fields[i], new ArrayList<Object>(prevs));
			if(!res.hasResult() && res.circle().contains(t)) {
				inCircle = true;
				circle = circle == null ? res.circle() : union(circle, res.circle());
				}
			}
		if(inCircle) { return new NormResult(circle); }
		KeyNorm2D key = this.simpleKey(t);
		if(normMap.containsKey(key)) { return new NormResult(normMap.get(key)); }
		else { return new NormResult<T>(t);	}		
		}

	@Override
	public boolean isNorm(T t) { return t.norm(false); }

	@Override
	public boolean structurallyEqual(T t1, T t2) {
		t1 = normalize(t1);
		t2 = normalize(t2);
		return t1 == t2;
		}
	
	@Override
	public Object[] f(T t) { return t.allFields(); }
	
	@Override
	public Object f(T t, int i) { return t.allFields()[i]; }
	
	@Override
	public void f(T t, Object o, int i) { t.setField(i, o); }
	
	public static <T> Set<T> union(Collection<T> l1, Collection<T> l2) {
		Set<T> set = new HashSet<T>();
		set.addAll(l1);
		set.addAll(l2);
		return set;
		}
	
	//TODO: Find a more robust way to do this
	private String typename;
	
	@Override
	public String typename(T t) {
		if(typename == null) { typename = t.typename(); }
		return typename;
		}
	
}

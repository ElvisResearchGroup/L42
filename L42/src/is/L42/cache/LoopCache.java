package is.L42.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LoopCache {
	
	private static final Map<KeyNorm2D, Object> circularIndex = RootCache.newNormMap();
	
	/**
	 * Normalizes a circle via progressive approximation, first creating keys for each object, then phasing
	 * out duplicates. Ones no more approximations can be done, the keys are entered into the lookup table
	 * to discover if the circle already exists in memory. If it does, the version in memory is returned.
	 * Otherwise, the given circle is entered into memory and returned.
	 * 
	 * @param desired The object the caller wants returned as a normalized variant
	 * @param circle Set of all objects in the circle
	 * @return the normalized variant of o
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T normalizeCircle(Object desired, Set<Object> circle) {
		//TODO: Break this up into smaller parts
		Map<Object, CircleObject> circleobjects = new HashMap<>();
		for(Object circleobj : circle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
		do {	
			KeyNorm2D retKey = null;
			Map<KeyNorm2D, Object> tempKeyMap = new HashMap<>();
			Map<Object, Object> replacements = new HashMap<>();
			for(Object circleobj : circle) {
				KeyNorm2D key = keyFromCircleObject(circleobj, circleobjects);
				if(circleobj == desired) { retKey = key; }
				if(tempKeyMap.containsKey(key)) {
					if(circleobj != desired) {
						replacements.put(circleobj, tempKeyMap.get(key));
						} else {
						replacements.put(tempKeyMap.get(key), circleobj);
						tempKeyMap.put(key, circleobj);
						}
					} else {
					tempKeyMap.put(key, circleobj);
					}
				}
			if(replacements.size() == 0) {
				if(!circularIndex.containsKey(retKey)) {
					for(Map.Entry<KeyNorm2D, Object> entry : tempKeyMap.entrySet()) {
					  Cache cache = RootCache.getCacheObject(entry.getValue());
						circularIndex.put(entry.getKey(), entry.getValue());
						cache.addObjectOverride(simpleKeyFromChonker(entry.getValue(), entry.getKey()), entry.getValue());
						}
					}
				return (T) circularIndex.get(retKey);
				} else {
				for(Object key : replacements.keySet()) {
					circleobjects.remove(key);
					circle.remove(key);
					}
				for(Map.Entry<Object, CircleObject> entry : circleobjects.entrySet()) {
					circleobjects.put(entry.getKey(), entry.getValue().replace(replacements));
					}
				}
			} while(true);
		}
	
	public static KeyNorm2D getKeyCircleNN(Object desired, Set<Object> circle) {
		Map<Object, CircleObject> circleobjects = new HashMap<>();
		for(Object circleobj : circle) { circleobjects.put(circleobj, new CircleObject(circleobj)); }
		do {	
			KeyNorm2D retKey = null;
			Map<KeyNorm2D, Object> tempKeyMap = new HashMap<>();
			Map<Object, Object> replacements = new HashMap<>();
			for(Object circleobj : circle) {
				KeyNorm2D key = keyFromCircleObject(circleobj, circleobjects);
				if(circleobj == desired) { retKey = key; }
				if(tempKeyMap.containsKey(key)) {
					if(circleobj != desired) {
						replacements.put(circleobj, tempKeyMap.get(key));
						} else {
						replacements.put(tempKeyMap.get(key), circleobj);
						tempKeyMap.put(key, circleobj);
						}
					} else {
					tempKeyMap.put(key, circleobj);
					}
				}
			if(replacements.size() == 0) {
				return retKey;
				} else {
				for(Object key : replacements.keySet()) {
					circleobjects.remove(key);
					circle.remove(key);
					}
				for(Map.Entry<Object, CircleObject> entry : circleobjects.entrySet()) {
					circleobjects.put(entry.getKey(), entry.getValue().replaceNN(replacements));
					}
				}
		} while(true);
	}
	
	private static KeyNorm2D keyFromCircleObject(Object circleobj, Map<Object, CircleObject> circleobjects)
	{
		Map<Object, Integer> varnames = new HashMap<>();
		List<Object> order = new ArrayList<>();
		circleobjects.get(circleobj).constructVarMap(varnames, order, circleobjects, new AtomicInteger(0));
		Object[][] lines = new Object[order.size()][];
		for(int i = 0; i < lines.length; i++) { lines[i] = (circleobjects.get(order.get(i))).toKey(varnames); }
		return new KeyNorm2D(lines);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	private static KeyNorm2D simpleKeyFromChonker(Object obj, KeyNorm2D key) {
	  Cache cache = RootCache.getCacheObject(obj);
	  Object[] ln1 = key.lines()[0];
	  Object[] ln1cpy = new Object[ln1.length];
	  System.arraycopy(ln1, 0, ln1cpy, 0, ln1.length);
	  for(int i = 1; i < ln1.length; i++) {
	    if(ln1[i] instanceof £KeyVarID) {
	      ln1cpy[i] = cache.f(obj, i - 1);
	      }
	    }
	  return new KeyNorm2D(new Object[][] { ln1cpy });
	  }
	
	protected static class Field {	
		boolean isNorm;
		Object value;
		
		public Field(Object o, boolean isnorm) {
			this.value = o;
			this.isNorm = isnorm;
			}
		
		public String toString(Map<Object, Integer> varmap) {
			if(isNorm) { return Integer.toHexString(System.identityHashCode(value)); }
			return "" + varmap.get(this.value);
			}
		
		public Object toKeyObject(Map<Object, Integer> varmap) {
			if(isNorm) { return value; }
			else { return new £KeyVarID(varmap.get(this.value)); }
			}
		
		public void replaceNN(Map<Object, Object> replacements) {
			if(replacements.containsKey(value)) {
				value = replacements.get(value);
				isNorm = RootCache.isNorm(value);
				}
			}
		}
	
	protected static class CircleObject  {
		private final Object obj;
		private final String typename;
		private final Field[] params;
		
		@SuppressWarnings("rawtypes")
		private final Cache cache;
		
		@SuppressWarnings({ "unchecked" })
		public CircleObject(Object obj) {
			Class<?> class_ = obj.getClass();
			cache = RootCache.getCacheObject(class_);
			Object[] rawfields = cache.f(obj);
			Field[] fields = new Field[rawfields.length];
			for(int i = 0; i < rawfields.length; i++) {
				fields[i] = new Field(rawfields[i], RootCache.isNorm(rawfields[i]));
				}
			this.obj = obj;
			this.typename = class_.getSimpleName();
			this.params = fields;
			}
		
		public Object getUnderlyingObject() { return this.obj; }
		
		@SuppressWarnings("unchecked")
		public CircleObject replace(Map<Object, Object> replacements) {
			//TODO: Optimize this
			Object[] fields = cache.f(obj);
			for(int i = 0; i < fields.length; i++)
				if(replacements.containsKey(fields[i]))
					cache.f(obj, replacements.get(fields[i]), i);
			return new CircleObject(this.obj);
			}
		
		public CircleObject replaceNN(Map<Object, Object> replacements) {
			for(Field field : params) {field.replaceNN(replacements); }
			return this;
			}
		
		public void constructVarMap(Map<Object, Integer> varmap, List<Object> order, Map<Object, CircleObject> amap, AtomicInteger i) {
			varmap.put(this.obj, i.getAndAdd(1));
			order.add(this.obj);
			for(Field f : params)
				if(!varmap.containsKey(f.value) && amap.containsKey(f.value))
					amap.get(f.value).constructVarMap(varmap, order, amap, i);
			}
		
		public String toString(Map<Object, Integer> map) {
			StringBuilder builder = new StringBuilder();
			builder.append(map.get(this.obj));
			builder.append("=n ");
			builder.append(typename);
			builder.append("(");
			for(int i = 0; i < params.length; i++) {
				builder.append(params[i].toString(map));
				if(i < params.length - 1) { builder.append(","); }
				}
			builder.append(");");
			return builder.toString();
			}
		
		public Object[] toKey(Map<Object, Integer> map) {
			Object[] line = new Object[this.params.length + 1];
			line[0] = this.typename;
			for(int i = 0; i < this.params.length; i++) {
				line[i + 1] = this.params[i].toKeyObject(map);
				}
			return line;
			}
		}

}

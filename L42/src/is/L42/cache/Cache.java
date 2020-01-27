package is.L42.cache;

import java.util.List;

public interface Cache<T> {
	
	/**
	 * Produces the normalized version of this object, such that
	 * if a version of this object already exists and was 
	 * included into a call to normalize(), than that 'canonical'
	 * version will be returned. If this object doesn't already
	 * exist, then it will be recorded in a cache and returned
	 * as-is. During this process, all fields of the given
	 * object are also normalized, accounting for any circular
	 * references that may exist                              <br>
	 *                                                        <br>
	 * or, more simply                                        <br>
	 *                                                        <br>
	 * if t exists in cache                                   <br>&nbsp;&nbsp;&nbsp;&nbsp;
	 *    return cache(t)                                     <br>
	 * else                                                   <br>&nbsp;&nbsp;&nbsp;&nbsp;
	 *    add t to cache                                      <br>&nbsp;&nbsp;&nbsp;&nbsp;
	 *    return t                                            <br>
	 * 
	 * @param t The object you wish to normalize
	 * @return The normalized version of the object
	 */
	T normalize(T t);
	
	/**
	 * An internal function used to normalize objects. This
	 * method should only be called by other cache objects.<br>
	 *                                                     <br>
	 * Normalizes an object in the same manner as described
	 * in <code>normalize(T t)</code>, but tracks the call
	 * hierarchy to guard against circular references.     <br>
	 *                                                     <br>
	 * If circular references are discovered, every object
	 * that is identified to be within the circle is
	 * returned within the <code>NormResult</code>.
	 * 
	 * @param t The object you mean to normalize
	 * @param chain A list representing the call hierarchy
	 * of <code>
	 * normalizeInner(T, List&lt;Object&gt;)</code>,
	 * where the most recent calls are last, and the objects
	 * are the <code>t</code>'s of each respective call.
	 * 
	 * @return A NormResult either containing a normalized
	 * version of a given object, or all the objects in the
	 * discovered circular reference loop
	 */
	NormResult<T> normalizeInner(T t, List<Object> chain);
	
	/**
	 * Compute's an object's representation without normalizing
	 * it or modifying it in any way.
	 * 
	 * @param t The object
	 * @return The object's key
	 */
	KeyNorm2D computeKeyNN(T t);
	
	/**
	 * Functions similarly to normalizeInner, but doesn't
	 * modify any objects or add them to the normalization
	 * table. Used by <code>computeKeyNN(T)</code>
	 * 
	 * @param t The object you mean to normalize
	 * @param chain A list representing the call hierarchy
	 * of <code>
	 * normalizeInner(T, List&lt;Object&gt;)</code>,
	 * where the most recent calls are last, and the objects
	 * are the <code>t</code>'s of each respective call.
	 * 
	 * @return A NormResult either containing a normalized
	 * version of a given object, or all the objects in the
	 * discovered circular reference loop
	 */
	NormResult<T> computeKeyNNInner(T t, List<Object> chain);
	
	/**
	 * Adds an object with the specified key, without doing
	 * any checks with respect to the structure of the cache
	 */
	void addObjectOverride(KeyNorm2D key, T obj);
	
	/**
	 * Returns whether a given object has already been
	 * normalized
	 * 
	 * @param t The object you wish to check
	 * @return <code>true</code> if normalized, <code>
	 * false</code> otherwise.
	 */
	boolean isNorm(T t);
	
	/**
	 * Compares two objects and returns whether they are
	 * structurally equal, adding them to the cache
	 * in the process.                                   <br>
	 *                                                   <br>
	 * equivalent to <code>normalize(t1)==normalize(t2)
	 *                                  
	 * @param t1 
	 * @param t2
	 * @return <code>true</code> if they are structurally
	 * equal, <code>false</code> otherwise.
	 */
	boolean structurallyEqual(T t1, T t2);
	
	/**
	 * Given an object of type T, returns an array of 
	 * all t's fields. The fields are in an arbitrary
	 * but set order that is congruent with<code>
	 * f(T, int)</code> and <code>f(T, Object,
	 * int)</code>
	 * 
	 * @param t The object
	 * @return The array of fields
	 */
	Object[] f(T t);
	
	/**
	 * Given an object of type T, returns the field
	 * of t at index i. Congruent with <code>f(T)</code>
	 * and <code>f(T, Object, int)</code>
	 * 
	 * @param t The object
	 * @param i The index of the field
	 * @return The value of the field at index i.
	 */
	Object f(T t, int i);
	
	/**
	 * Given an object of type T, sets the value of a field
	 * of t at index i. Congruent with <code>f(T)</code>
	 * and <code>f(T, int)</code>
	 * 
	 * @param t The object
	 * @param o The new value of the field
	 * @param i The index of the field
	 */
	void f(T t, Object o, int i);
	
	/**
	 * Given an object of type T, returns it's canonical
	 * typename.
	 * 
	 * @param t An object of type T.
	 * @return The canonical type name
	 */
	String typename(T t);
	
	/**
	 * Produces a key as though this object contained no circular references. 
	 * For internal use.
	 * 
	 * @param t The object to resolve the key for
	 * @return The key
	 */
	default KeyNorm2D simpleKey(T t)
	{
		Object[] fields = this.f(t);
		Object[][] key = new Object[1][fields.length + 1];
		key[0][0] = this.typename(t);
		System.arraycopy(fields, 0, key[0], 1, fields.length);
		return new KeyNorm2D(key);
	}

}

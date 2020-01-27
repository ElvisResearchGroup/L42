package is.L42.cache;

public interface ForeignObject<T> {
	
	Object[] allFields();
	
	/**
	 * Sets the field at index i. Returns 
	 * <code>ArrayIndexOutOfBoundsException</code> if
	 * the numbered field doesn't exist.
	 * 
	 * @param i index of the field
	 * @param o the field's new value.
	 */
	void setField(int i, Object o);
	
	/**
	 * @return A reference to my cache object
	 */
	Cache<T> myCache();
	
	/**
	 * @return The canonical type name of this object. 
	 * Assumed to be unique.
	 */
	String typename();
	
	/**
	 * Can either silently read whether an object is normalized by passing
	 * <code>false</code>, or set the object as normalized by passing <code>true</code>
	 * 
	 * @param set Whether or not you want to mark this object as normalized.
	 * @return whether this object is normalized or not.
	 */
	boolean norm(boolean set);
	//There is no need to set norm to false, as all norm objects are imm

}

package colibri.lib;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * An abstract class that implements methods of the interface
 * <code>Relation</code>.
 * Methods that are dependent on the exact data structure used by the 
 * implementation must be implemented in the subclasses.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public abstract class StdRelation implements Relation {
	protected Map<Comparable, ComparableSet> objectMap;
	protected Map<Comparable, ComparableSet> attributeMap;
	
	protected ComparableSet allObjects;
	protected ComparableSet allAttributes;	
	
	protected boolean changesDisallowed;
	
	
	/**
	 * Activates write protection on this set.
	 * <p>
	 * After this method has been called, this set is unmodifiable,
	 * i.e.&nbsp;if there is a call attempting to change the contents of 
	 * this set, an exception will be thrown.
	 * @return <code>true</code>
	 */
	public boolean disallowChanges() {
		changesDisallowed = true;
		
		allObjects.disallowChanges();
		allAttributes.disallowChanges();
		
		Set<Entry<Comparable, ComparableSet>> entriesO = objectMap.entrySet();
		Iterator<Entry<Comparable, ComparableSet>> entryIteratorO = entriesO.iterator();
		
		while (entryIteratorO.hasNext()) {
			ComparableSet attributeSet = entryIteratorO.next().getValue();
			if (attributeSet != null) {
				attributeSet.disallowChanges();
			}
		}
		
		Set<Entry<Comparable, ComparableSet>> entriesA = attributeMap.entrySet();
		Iterator<Entry<Comparable, ComparableSet>> entryIteratorA = entriesA.iterator();
		
		while (entryIteratorA.hasNext()) {
			ComparableSet objectSet = entryIteratorA.next().getValue();
			if (objectSet != null) {
				objectSet.disallowChanges();
			}
		}
		
		return true;
	}
	
	
	/**
	 * Returns the number of objects contained in the object set of this relation.
	 * @return the number of objects contained in the object set of this relation.
	 */
	public int getSizeObjects() {
		return allObjects.size();
	} 
	
	
	/**
	 * Returns the number of attributes contained in the attribute set of this relation.
	 * @return the number of attributes contained in the attribute set of this relation.
	 */
	public int getSizeAttributes() {
		return allAttributes.size();
	}
	
	
	/**
	 * Returns a string representation of this relation.
	 * @return a string representation of this relation.
	 */
	public String toString() {
		return objectMap.toString().concat(attributeMap.toString());
	}
	
	
	/**
	 * Returns a copy of the set of all objects contained in this relation.
	 * <p>
	 * The set returned by this method is modifiable and modifying 
	 * that set does not affect the contents of this relation.
	 * @return the set of all objects contained in the relation.
	 */
	public ComparableSet getAllObjects () {
		return (ComparableSet)allObjects.clone();
	}
	
	
	/**
	 * Returns a copy of the set of all attributes contained in this relation.
	 * <p>
	 * The set returned by this method is modifiable and modifying 
	 * that set does not affect the contents of this relation.
	 * @return the set of all attributes contained in this relation.
	 */
	public ComparableSet getAllAttributes () {
		return (ComparableSet)allAttributes.clone();
	}
	
	
	/**
	 * Returns an iterator over the objects which have the specified
	 * <code>attribute</code>.
	 * <p>
	 * More formally, this method returns an iterator iterating over
	 * all objects <code>o</code> for which it is true that the pair
	 * (<code>o</code>, <code>attribute</code>)
	 * is contained in this relation.
	 * <p>
	 * There are no guarantees concerning the order in which the objects
	 * are returned (unless this relation is an instance of a class that
	 * provides a guarantee).
	 * @param attribute the attribute whose objects shall be returned.
	 * @return an iterator over the objects of <code>attribute</code>.
	 */
	public Iterator<Comparable> getObjects (Comparable attribute) {
		return attributeMap.get(attribute).iterator();
	}
	
	
	/**
	 * Returns an iterator over the attributes the specified
	 * <code>object</code> has.
	 * <p>
	 * More formally, this method returns an iterator iterating over
	 * all attributes <code>a</code> for which it is true that the pair 
	 * (<code>object</code>, <code>a</code>) is contained in this relation.
	 * <p>
	 * There are no guarantees concerning the order in which the attributes
	 * are returned (unless this relation is an instance of a class that
	 * provides a guarantee).
	 * @param object the object whose objects shall be returned.
	 * @return an iterator over the attributes of <code>object</code>.
	 */
	public Iterator<Comparable> getAttributes (Comparable object) {
		return objectMap.get(object).iterator();
	}
	
	
	/**
	 * Returns a set that contains all objects which have the
	 * specified <code>attribute</code>. 
	 * Note that the set that is returned by this method might be
	 * unmodifiable.
	 * @param attribute the attribute whose objects shall be returned.
	 * @return a set that contains all objects of <code>attribute</code>.
	 */
	public ComparableSet getObjectSet (Comparable attribute) {
		return attributeMap.get(attribute);
	}
	
	
	/**
	 * Returns a set that contains all attributes the specified
	 * <code>object</code> has. 
	 * Note that the set that is returned by this method might be
	 * unmodifiable.
	 * @param object the object whose attributes shall be returned.
	 * @return a set that contains all attribuets of <code>object</code>.
	 */
	public ComparableSet getAttributeSet (Comparable object) {
		return objectMap.get(object);
	}
		
	
	/**
	 * Removes the pair (<code>object</code>, <code>attribute</code>) from this relation
	 * <p>
	 * If <code>object</code> is <code>null</code> then <code>attribute</code> will
	 * be removed from the attribute set of this relation and all pairs whose attribute
	 * is <code>attribute</code> will be removed from this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> then <code>object</code> will
	 * be removed from the attribute set of this relation and all pairs whose object
	 * is <code>object</code> will be removed from this relation.
	 * @param object the object.
	 * @param attribute the attribute.
	 */
	public void remove(Comparable object, Comparable attribute) {
		
		if (changesDisallowed) {
			throw new UnsupportedOperationException();
		}
		
		try {
			if (object == null && attribute != null) {
				ComparableSet objects = getAllObjects();
				Iterator<Comparable> objectIterator = objects.iterator();
				
				while (objectIterator.hasNext()) {
					Comparable obj = (objectIterator.next());
					remove(obj, attribute);
				}
				
				attributeMap.remove(attribute);
				allAttributes.remove(attribute);
			}
			
			if (attribute == null && object != null && objectMap.containsKey(object)) {
				ComparableSet attributes = getAllAttributes();
				Iterator<Comparable> attributeIterator = attributes.iterator();
				
				while (attributeIterator.hasNext()) {
					Comparable attr = (attributeIterator.next());
					remove (object, attr);
				}
				
				objectMap.remove(object);
				allObjects.remove(object);
			}
			
			if (object != null && attribute != null && objectMap.containsKey(object) 
					&& attributeMap.containsKey(attribute)) {
				//remove the attribute
				ComparableSet attributeSet = objectMap.get(object);
				attributeSet.remove(attribute);
				
				//remove the object
				ComparableSet objectSet = attributeMap.get(attribute);
				objectSet.remove(object);
			}
		}
		catch (ClassCastException e) {
			//Whenever a ClassCastException has been thrown the pair is not contained in the relation anyway
			//therefore a ClassCastException can be ignored
		}
		
	}
	
	
	/**
	 * Return <code>true</code> if and only if the pair
	 * (<code>object</code>, <code>attribute</code>) is contained in this relation.
	 * <p>
	 * If <code>object</code> is <code>null</code> this method will return
	 * <code>true</code> if and only if <code>attribute</code> is contained
	 * in the attribute set of this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> this method will return
	 * <code>true</code> if and only if <code>object</code> is contained
	 * in the object set of this relation.
	 * @param object the object.
	 * @param attribute the attribute.
	 * @return <code>true</code>, iff the pair (<code>object</code>, <code>attribute</code>)
	 * is contained in the relation
	 */
	public boolean contains (Object object, Object attribute) {
		try {
			if (object != null && attribute != null) {
				if (allObjects.contains(object)) {
					ComparableSet attributes = objectMap.get(object);
					return attributes.contains(attribute);
				}
			}
			
			if (object != null && attribute == null)
				return allObjects.contains(object);
			
			if (attribute != null && object == null)
				return allAttributes.contains(attribute);
			
			return false;
		}
		catch (ClassCastException e) {
			//Whenever a ClassCastException has been thrown the pair is not contained in the relation
			return false;
		}
	}
}

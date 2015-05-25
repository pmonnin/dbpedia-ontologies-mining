package colibri.lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class that implements the interface <code>Relation</code>
 * using <code>ComparableHashSet</code> objects.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class HashRelation extends StdRelation {
	/**
	 * Constructs a new empty relation.
	 */
	public HashRelation () {
		objectMap = new HashMap<Comparable, ComparableSet>();
		attributeMap = new HashMap<Comparable, ComparableSet>();
		
		allObjects = new ComparableHashSet();
		allAttributes = new ComparableHashSet();
	}


	/**
	 * Adds the pair (<code>object</code>, <code>attribute</code>) to this relation.
	 * <p>
	 * If <code>object</code> is not yet contained in the object set of this relation
	 * it will be added to that set. Similarly, if <code>attribute</code> is not yet
	 * contained in the attribute set of this relation it will be added to that
	 * set.
	 * <p>
	 * If <code>object</code> is <code>null</code> no pair will be added to this
	 * relation but <code>attribute</code> will be added to the attribute set of this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> no pair will be
	 * added to this relation but <code>object</code> will be added to the
	 * attribute set of this relation.
	 * @param object the object. If this is <code>null</code>, only the <code>attribute</code> will be added
	 * to the attribute set.
	 * @param attribute the attribute. If this is null, only the <code>object</code> will be added
	 * to the object set
	 * @throws ClassCastException if <code>object</code> is not mutually comparable with the objects contained
	 * in the object set of this relation or <code>attribute</code> is not mutually comparable with the
	 * attributes contained in the attribute set of this relation.
	 * @throws UnsupportedOperationException if this relation is write protected.
	 */
	public void add(Comparable object, Comparable attribute) throws ClassCastException, UnsupportedOperationException {
		
		if (changesDisallowed) {
			throw new UnsupportedOperationException();
		}
		
		//add the object
		if (object != null && !allObjects.contains(object)) {
			ComparableSet set = new ComparableHashSet();
			objectMap.put(object, set);
			allObjects.add(object);
		}
		
		//add the attribute
		if (attribute != null && !allAttributes.contains(attribute)) {
			ComparableSet set = new ComparableHashSet();
			attributeMap.put(attribute, set);
			allAttributes.add(attribute);
		}
		
		//add the pair
		if (object != null && attribute != null) {
			ComparableSet attributeSet = objectMap.get(object);
			attributeSet.add(attribute);			
			ComparableSet objectSet = attributeMap.get(attribute);
			objectSet.add(object);			
		}	
	}
	
	
	/**
	 * Returns a set of objects which have all the specified attributes.
	 * <p>
	 * More formally, this method returns a set that contains all objects
	 * <code>o</code> for which it is true that for all attributes
	 * <code>a</code> that are contained in <code>coll</code>
	 * the pair (<code>o</code>, <code>a</code>) is contained in
	 * this relation.
	 * @param attributes a collection of attributes. 
	 * @return the set of objects that have all the attributes
	 * contained in the collection <code>attributes</code>.
	 * @throws IllegalArgumentException if <code>attributes</code> is <code>null</code>
	 * one of the elements contained in <code>attributes</code> is not
	 * contained in the attribute set of this relation.
	 * @throws IllegalArgumentException if <code>attributes</code> is <code>null</code>
	 * one of the elements contained in <code>attributes</code> is not
	 * contained in the attribute set of this relation.
	 */
	public ComparableSet commonObjects(Collection<Comparable> attributes) throws IllegalArgumentException {
		if (attributes == null)
			throw new IllegalArgumentException("commonObjects may not be called with null argument");
		
		Iterator<Comparable> attributeIterator = attributes.iterator();
		if (!attributeIterator.hasNext()) {
			//return a set with all objects
			return getAllObjects();
		}
					
		ComparableSet objectSet = new ComparableHashSet();
		
		try {
			Comparable attr = attributeIterator.next();
			objectSet.addAll(attributeMap.get(attr));
			
			while(attributeIterator.hasNext()) {
				attr = attributeIterator.next();
				objectSet.retainAll(attributeMap.get(attr));
			}
		} catch (NoSuchElementException e) {
			return objectSet;
		} catch (NullPointerException e) {
				throw new IllegalArgumentException();
		}
		
		return objectSet;
	}
	
	
	/**
	 * Returns a set of attributes which are associated with all the
	 * specified objects.
	 * <p>
	 * More formally, this method returns a set that contains all attributes
	 * <code>a</code> for which it is true that for all objects
	 * <code>o</code> that are contained in <code>coll</code>
	 * the pair (<code>o</code>, <code>a</code>) is contained in
	 * this relation.
	 * @param objects a collection of objects.
	 * @return the set of attributes all objects contained in the
	 * collection <code>objects</code> have.
	 * @throws IllegalArgumentException if <code>objects</code> is <code>null</code>
	 * or one of the elements contained in <code>objects</code> is not
	 * contained in the object set of this relation.
	 */
	public ComparableSet commonAttributes(Collection<Comparable> objects) throws IllegalArgumentException {
		if (objects == null)
			throw new IllegalArgumentException("commonAttributes may not be called with null argument");
		
		Iterator<Comparable> objectIterator = objects.iterator();
		
		if (!objectIterator.hasNext()) {
			//return a set containing all attributes
			return getAllAttributes();
		}
		
		ComparableSet attributeSet = new ComparableHashSet();
		
		try {
			Comparable obj = objectIterator.next();
			attributeSet.addAll(objectMap.get(obj));
			
			while(objectIterator.hasNext()) {
				obj = objectIterator.next();
				attributeSet.retainAll(objectMap.get(obj));
			}
		} catch (NoSuchElementException e) {
			return attributeSet;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException();
		}
		
		return attributeSet;
	}
}

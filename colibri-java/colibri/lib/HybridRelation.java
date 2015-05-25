package colibri.lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A class that implements the interface <code>Relation</code> using
 * <code>HybridSet</code> objects.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class HybridRelation implements Relation {
	
	private Map<Comparable, ComparableSet> objectMap;
	private Map<Comparable, ComparableSet> attributeMap;
	
	private ComparableSet allObjects;
	private ComparableSet allAttributes;
	
	private HybridTranslator objectTranslator;
	private HybridTranslator attributeTranslator;
	
	
	HybridRelation(Relation rel) {
		
		objectMap = new HashMap<Comparable, ComparableSet>();
		attributeMap = new HashMap<Comparable, ComparableSet>();
		
		objectTranslator = new HybridTranslator(rel.getAllObjects());
		attributeTranslator = new HybridTranslator(rel.getAllAttributes());
		
		allObjects = new HybridSet(rel.getAllObjects(), objectTranslator);
		allAttributes = new HybridSet(rel.getAllAttributes(), attributeTranslator);
		
		Iterator<Comparable> objectIterator = rel.getAllObjects().iterator();
		
		while (objectIterator.hasNext()) {
			Comparable object = objectIterator.next();
			objectMap.put(object, new HybridSet(rel.getAttributeSet(object), attributeTranslator));
		}
		
		Iterator<Comparable> attributeIterator = rel.getAllAttributes().iterator();
		
		while (attributeIterator.hasNext()) {
			Comparable attribute = attributeIterator.next();
			attributeMap.put(attribute, new HybridSet(rel.getObjectSet(attribute), objectTranslator));
		}
	}

	
	public void add(Comparable object, Comparable attribute) throws ClassCastException {
		throw new UnsupportedOperationException();
	}

	public ComparableSet commonAttributes(Collection<Comparable> coll) throws IllegalArgumentException {
		ComparableSet result = (ComparableSet)allAttributes.clone();
		
		Iterator<Comparable> it = coll.iterator();
		
		while (it.hasNext()) {
			Comparable object = it.next();
			result.retainAll(objectMap.get(object));
		}
		
		return result;
	}

	public ComparableSet commonObjects(Collection<Comparable> coll) throws IllegalArgumentException {
		ComparableSet result = (ComparableSet)allObjects.clone();
		
		Iterator<Comparable> it = coll.iterator();
		
		while (it.hasNext()) {
			Comparable attribute = it.next();
			result.retainAll(attributeMap.get(attribute));
		}
		
		return result;
	}

	public boolean contains(Object object, Object attribute) {
		try {
			if (object != null && attribute != null) {
				return objectMap.get(object).contains(attribute);
			}
			if (object == null && attribute != null) {
				return allAttributes.contains(attribute);
			}
			if (object != null && attribute == null) {
				return allObjects.contains(object);
			}
			return false;
		}
		catch (ClassCastException e) {
			//Whenever a ClassCastException has been thrown the pair is
			//not contained in the relation
			return false;
		}
		catch (NullPointerException e) {
			//Whenever a NullPointerException has been thrown the pair is
			//not contained in the relation
			return false;
		}
	}

	public boolean disallowChanges() {
		//HybridRelations are unmodifiable anyway.
		return true;
	}

	public ComparableSet getAllAttributes() {
		return (ComparableSet)allAttributes.clone();
	}

	public ComparableSet getAllObjects() {
		return (ComparableSet)allObjects.clone();
	}

	public ComparableSet getAttributeSet(Comparable object) {
		return objectMap.get(object);
	}

	public Iterator<Comparable> getAttributes(Comparable object) {
		return objectMap.get(object).iterator();
	}

	public ComparableSet getObjectSet(Comparable attribute) {
		return attributeMap.get(attribute);
	}

	public Iterator<Comparable> getObjects(Comparable attribute) {
		return attributeMap.get(attribute).iterator();
	}

	public int getSizeAttributes() {
		return attributeTranslator.getSize();
	}

	public int getSizeObjects() {
		return objectTranslator.getSize();
	}

	public void remove(Comparable object, Comparable attribute) {
		throw new UnsupportedOperationException();
	}
	
	
	HybridTranslator getAttributeTranslator() {
		return attributeTranslator;
	}
	
	
	HybridTranslator getObjectTranslator() {
		return objectTranslator;
	}
}

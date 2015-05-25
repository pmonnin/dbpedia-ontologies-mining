package colibri.lib;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * A class that provides implementations for all methods defined in the
 * interface <code>Lattice</code>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public abstract class LatticeImpl implements Lattice {
	
	protected Relation relation = null;
	protected ComparableSet allObjects = null;
	protected ComparableSet allAttributes = null;
	protected Concept top = null;
	protected Concept bottom = null;
	
	
	/**
	 * Returns the least upper bound of the concepts contained in
	 * the collection <code>concepts</code>.
	 * @param concepts the concepts whose least upper bound shall be computed.
	 * @return the least upper bound of the concepts contained in <code>concepts</code>.
	 */
	public Concept join (Collection<Concept> concepts) {
		ComparableSet attributes = relation.getAllAttributes();
		
		Iterator<Concept> conceptIterator = concepts.iterator();
		while (conceptIterator.hasNext()) {
			attributes.retainAll(conceptIterator.next().getAttributes());
		}
		
		return conceptFromAttributes(attributes);
	}
	
	
	/**
	 * Returns the greatest lower bound of the concepts contained in
	 * the collection <code>concepts</code>.
	 * @param concepts the concepts whose greatest lower bound shall be computed.
	 * @return the greatest lower bound of the concepts contained in <code>concepts</code>
	 */
	public Concept meet (Collection<Concept> concepts) {
		ComparableSet objects = relation.getAllObjects();
		
		Iterator<Concept> conceptIterator = concepts.iterator();
		while (conceptIterator.hasNext()) {
			objects.retainAll(conceptIterator.next().getObjects());
		}
		
		return conceptFromObjects(objects);
	}
	
	
	/**
	 * Returns the least concept that contains all objects contained in <code>objects</code>.
	 * <p>
	 * Returns the concept that contains the common attributes of the
	 * objects contained in <code>objects</code> and their common objects, 
	 * but no other objects or attributes.
	 * <p>
	 * More formally, returns the concept (<code>object</code>'', <code>object</code>').
	 * @param objects the set of objects from which the concept shall be computed.
	 * @return the concept computed from <code>objects</code>.
	 */
	public Concept conceptFromObjects (Collection<Comparable> objects) throws IllegalArgumentException {
		ComparableSet attributeSet = relation.commonAttributes(objects);
		ComparableSet objectSet = relation.commonObjects(attributeSet);
		
		Concept concept = new Concept(objectSet, attributeSet);
		
		return concept;
	}
	
	
	/**
	 * Returns the greatest concept that contains all attributes contained in <code>attributes</code>.
	 * <p>
	 * Returns the concept that contains the common objects of the
	 * attributes contained in <code>attributes</code> and their common attributes,
	 * but no other objects or attributes.
	 * <p>
	 * More formally, returns the concept (<code>attributes</code>', <code>attributes</code>'').
	 * @param attributes the set of attributes from which the concept shall be computed.
	 * @return the concept computed from <code>attributes</code>.
	 */
	public Concept conceptFromAttributes (Collection<Comparable> attributes) throws IllegalArgumentException {
		ComparableSet objectSet = relation.commonObjects(attributes);
		ComparableSet attributeSet = relation.commonAttributes(objectSet);
		
		Concept concept = new Concept(objectSet, attributeSet);
		
		return concept;
	}
	
	
	/**
	 * Returns the <i>top</i> concept, i.e.&nbsp;the concept that contains
	 * all objects.
	 * @return the <i>top</i> concept.
	 */
	public Concept top () {
		return top;
	}
	
	
	/**
	 * Returns the <i>bottom</i> concept, i.e.&nbsp;the concept that contains
	 * all attributes.
	 * @return the <i>bottom</i> concept.
	 */
	public Concept bottom () {
		return bottom;
	}
	
	
	/**
	 * Returns an iterator over the lower neighbors of <code>concept</code>.
	 * <p>
	 * There are no guarantees concerning the order in which the lower
	 * neighbors are returned. The exact order may depend on the
	 * implementation of the class of the underlying relation and
	 * on other factors.
	 * @param concept the concept whose lower neighbors shall be computed.
	 * @return an iterator over the lower neighbors of <code>concept</code>.
	 */
	public Iterator<Concept> lowerNeighbors (Concept concept) {
		LinkedList<Concept> list = new LinkedList<Concept>();
		ComparableSet min = relation.getAllAttributes();
		
		Iterator<Comparable> attributeIterator = allAttributes.iterator();
		ComparableSet attributes = concept.getAttributes();
		min.removeAll(attributes);
		
		//for each attribute that is not contained in the concept
		while (attributeIterator.hasNext()) {
			//build a concept from the attributes contained in the concept and another attribute
			Comparable attribute = attributeIterator.next();
			
			if (!attributes.contains(attribute)) {
				Concept nextConcept = computeLowerCandidate(concept, attribute);
				
				//check whether the new concept is a lower neighbor that won't be computed again later 
				ComparableSet set = nextConcept.getAttributes();
				if (set.containsNone(min, attribute)) {
					//if the new concept is a lower neighbor that won't be computed again later
					//add it to the list
					list.add(nextConcept);
				}
				else {
					min.remove(attribute);
				}
			}
		}
		
		return list.iterator();
	}
	
	
	/**
	 * Returns an iterator over the upper neighbors of <code>concept</code>.
	 * <p>
	 * There are no guarantees concerning the order in which the upper
	 * neighbors are returned. The exact order may depend on the
	 * implementation of the class of the underlying relation and
	 * on other factors.
	 * @param concept the concept whose upper neighbors shall be computed.
	 * @return an iterator over the upper neighbors of <code>concept</code>.
	 */
	public Iterator<Concept> upperNeighbors (Concept concept) {
		LinkedList<Concept> list = new LinkedList<Concept>();
		ComparableSet min = relation.getAllObjects();
		
		Iterator<Comparable> objectIterator = allObjects.iterator();
		ComparableSet objects = concept.getObjects();
		min.removeAll(objects);
		
		//for each object that is not contained in the concept
		while (objectIterator.hasNext()) {
			//build a concept from the objects contained in the concept and another object
			Comparable object = objectIterator.next();
			
			if (!objects.contains(object)) {
				Concept nextConcept = computeUpperCandidate(concept, object);
				
				//check whether the new concept is an upper neighbor that won't be computed again later
				ComparableSet set = nextConcept.getObjects();
				if (set.containsNone(min, object)) {
					//if the new concept is an upper neighbor that won't be computed again later
					//add it to the list
					list.add(nextConcept);
				}
				else {
					min.remove(object);
				}
			}
		}
		
		return list.iterator();
	}
	
	
	/**
	 * Returns an iterator over all concepts of this lattice.
	 * <p>
	 * The concepts will be returned in the order specified by the
	 * <code>trav</code> argument.
	 * @see Traversal
	 * @param trav the desired traversal.
	 * @return an iterator over all concepts of this lattice.
	 */
	public Iterator<Concept> conceptIterator(Traversal trav) {
		switch (trav) {
		case BOTTOM_OBJ:
			return new ConceptIteratorBUBF(this, ConceptOrder.OBJ_STD);
		case BOTTOM_OBJSIZE:
			return new ConceptIteratorBUBF(this, ConceptOrder.OBJ_SIZEFIRST);
		case BOTTOM_ATTR:
			return new ConceptIteratorBUBF(this, ConceptOrder.ATTR_STD);
		case BOTTOM_ATTRSIZE:
			return new ConceptIteratorBUBF(this, ConceptOrder.ATTR_SIZEFIRST);
		case TOP_OBJ:
			return new ConceptIteratorTDBF(this, ConceptOrder.OBJ_STD);
		case TOP_OBJSIZE:
			return new ConceptIteratorTDBF(this, ConceptOrder.OBJ_SIZEFIRST);
		case TOP_ATTR:
			return new ConceptIteratorTDBF(this, ConceptOrder.ATTR_STD);
		case TOP_ATTRSIZE:
			return new ConceptIteratorTDBF(this, ConceptOrder.ATTR_SIZEFIRST);
		case TOP_DEPTHFIRST:
			return new ConceptIteratorTDDF(this);
		case BOTTOM_DEPTHFIRST:
			return new ConceptIteratorBUDF(this);
		default:
			throw new IllegalArgumentException("This traversal is not supported.");
		}
	}
	
	
	/**
	 * Returns an iterator over all edges of this lattice.
	 * More formally, returns an iterator over all pairs of concepts,
	 * that are neighbors of each other.
	 * <p>
	 * The order in which the edges (pairs of upper and lower neighbors)
	 * are returned depends on the <code>trav</code> argument.
	 * <p>
	 * A top-down-breadth-first traversal guarantees that all edges having
	 * the same upper neighbor will be returned consecutively. However,
	 * there are no guarantees concerning the order in which the edges
	 * having the same upper neighbor are returned.
	 * Similarly, a bottom-up-breadth-first traversal guarantees that
	 * all edges having the same lower neighbor will be returned consecutively
	 * but there are no guarantees concerning the order in which the edges
	 * having the same lower neighbor are returned.
	 * @param trav the desired traversal.
	 * @return an iterator over all edges of this lattice.
	 */
	public Iterator<Edge> edgeIterator(Traversal trav) {
		switch (trav) {
		case BOTTOM_OBJ:
			return new EdgeIteratorBUBF(this, ConceptOrder.OBJ_STD);
		case BOTTOM_OBJSIZE:
			return new EdgeIteratorBUBF(this, ConceptOrder.OBJ_SIZEFIRST);
		case BOTTOM_ATTR:
			return new EdgeIteratorBUBF(this, ConceptOrder.ATTR_STD);
		case BOTTOM_ATTRSIZE:
			return new EdgeIteratorBUBF(this, ConceptOrder.ATTR_SIZEFIRST);
		case TOP_OBJ:
			return new EdgeIteratorTDBF(this, ConceptOrder.OBJ_STD);
		case TOP_OBJSIZE:
			return new EdgeIteratorTDBF(this, ConceptOrder.OBJ_SIZEFIRST);
		case TOP_ATTR:
			return new EdgeIteratorTDBF(this, ConceptOrder.ATTR_STD);
		case TOP_ATTRSIZE:
			return new EdgeIteratorTDBF(this, ConceptOrder.ATTR_SIZEFIRST);
		case TOP_DEPTHFIRST:
			return new EdgeIteratorTDDF(this);
		case BOTTOM_DEPTHFIRST:
			return new EdgeIteratorBUDF(this);
		default:
			throw new IllegalArgumentException("This traversal is not supported.");
		}
	}
	
	
	/**
	 * Returns an iterator over pairs of neighboring concepts that are
	 * similar to each other. How similar they have to be in order
	 * to be returned by this iterator is specified by
	 * the three arguments <code>supp</code>, <code>conf</code> and 
	 * <code>diff</code>.
	 * @param supp the minimal support, i.e.&nbsp;the minimal number of objects contained
	 * in the lower neighbor.
	 * @param conf the minimal confidence, i.e.&nbsp;the minimal fraction l/u, where
	 * l is the number of objects in the lower neighbor and u is the
	 * number of objects in the upper neighbor. Must be a value between
	 * 0 and 1.
	 * @param diff the maximal difference between the number of attributes
	 * in the lower neighbor and the number of attributes in the upper
	 * neighbor.
	 * @return an iterator over pairs of similar neighboring concepts.
	 */
	public Iterator<Edge> violationIterator(int supp, float conf, int diff) {
		return new ViolationIteratorTD(new EdgeIteratorTDBF(this, ConceptOrder.OBJ_SIZEFIRST), supp, conf, diff);
	}
	
	
/*	/**
	 * 
	 * @param concept
	 * @return the objects that are contained in concept but not in any of its subconcepts
	 */
/*	public Iterator<Comparable> newObjects (Concept concept) {
		ComparableSet set = new ComparableTreeSet(concept.getObjects());
		
		Iterator<Concept> neighbors = lowerNeighbors(concept);
		
		while (neighbors.hasNext()) {
			Concept neighbor = neighbors.next();
			ComparableSet objects = neighbor.getObjects();
			set.removeAll(objects);
		}
		
		return set.iterator();
	}
*/	
	
/*	/**
	 * 
	 * @param concept
	 * @return the attributes that are contained in concept but not in any of its superconcepts
	 */
/*	public Iterator<Comparable> newAttributes (Concept concept) {
		ComparableSet set = new ComparableTreeSet(concept.getAttributes());
		
		Iterator<Concept> neighbors = upperNeighbors(concept);
		
		while (neighbors.hasNext()) {
			Concept neighbor = neighbors.next();
			ComparableSet attributes = neighbor.getAttributes();
			set.removeAll(attributes);
		}
		
		return set.iterator();
	}
*/	
	
	/**
	 * Computes the concept that contains all attributes contained in the
	 * specified <code>concept</code> and the specified <code>attribute</code>.
	 * @param concept the concepts whose attributes shall be contained in the
	 * concept to be computed.
	 * @param attribute the additional attribute that shall be contained in the
	 * concept to be computed.
	 * @return the concept that contains all attributes contained in the
	 * specified <code>concept</code> and the specified <code>attribute</code>.
	 */
	private Concept computeLowerCandidate(Concept concept, Comparable attribute) {
		ComparableSet objects1 = (ComparableSet)relation.getObjectSet(attribute).clone();
		ComparableSet objects2 = concept.getObjects();
		
		objects1.retainAll(objects2);
		
		if (objects1.isEmpty())
			return bottom();
		
		ComparableSet attributes = relation.commonAttributes(objects1);
		return new Concept(objects1, attributes);
	}
	
	
	/**
	 * Computes the concept that contains all object contained in the
	 * specified <code>concept</code> and the specified <code>object</code>.
	 * @param concept the concepts whose objects shall be contained in the
	 * concept to be computed.
	 * @param object the additional object that shall be contained in the
	 * concept to be computed.
	 * @return the concept that contains all object contained in the
	 * specified <code>concept</code> and the specified <code>object</code>.
	 */
	private Concept computeUpperCandidate(Concept concept, Comparable object) {
		ComparableSet attributes1 = (ComparableSet)relation.getAttributeSet(object).clone();
		ComparableSet attributes2 = concept.getAttributes();

		attributes1.retainAll(attributes2);
		
		if (attributes1.isEmpty())
			return top();
		
		ComparableSet objects = relation.commonObjects(attributes1);
		return new Concept(objects, attributes1);		
	}

}

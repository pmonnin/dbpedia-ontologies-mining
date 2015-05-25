package colibri.lib;

import java.util.Collection;
import java.util.Iterator;


/**
 * Representation of a concept lattice.
 * <p>
 * A <code>Lattice</code> object represents the concept lattice
 * of a given context. Since a context is typically represented
 * by a <code>Relation</code> object, the classes implementing
 * the <code>Lattice</code> interface should provide a constructor with
 * a single argument of type <code>Relation</code>, which creates
 * a lattice for that relation. Depending on the concrete
 * implementation, changing a relation after it has been passed
 * to such a constructor might lead to wrong computations by
 * the <code>Lattice</code> object. See the documentation of
 * the classes implementing this interface for details.
 * <p>
 * The interface <code>Lattice</code> defines the most important
 * methods for formal concept analysis. In particular, it provides
 * methods <code>conceptIterator</code> and <code>EdgeIterator</code>
 * that return iterators which iterate over all concepts and over all
 * pairs of neighboring concepts in the lattice, respectively.
 * In addition, the methods <code>upperNeighbors</code> and 
 * <code>lowerNeighbors</code> can be used to traverse the concept
 * lattice manually.
 * 
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public interface Lattice {
	/**
	 * Returns the least upper bound of the concepts contained in
	 * the collection <code>concepts</code>.
	 * @param concepts the concepts whose least upper bound shall be computed.
	 * @return the least upper bound of the concepts contained in <code>concepts</code>.
	 */
	public Concept join (Collection<Concept> concepts);
	
	/**
	 * Returns the greatest lower bound of the concepts contained in
	 * the collection <code>concepts</code>.
	 * @param concepts the concepts whose greatest lower bound shall be computed.
	 * @return the greatest lower bound of the concepts contained in <code>concepts</code>.
	 */
	public Concept meet (Collection<Concept> concepts);
	
	
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
	public Concept conceptFromObjects (Collection<Comparable> objects) throws IllegalArgumentException;
	
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
	public Concept conceptFromAttributes (Collection<Comparable> attributes) throws IllegalArgumentException;
	
	
	/**
	 * Returns the <i>top</i> concept, i.e.&nbsp;the concept that contains
	 * all objects.
	 * @return the <i>top</i> concept.
	 */
	public Concept top ();
	
	/**
	 * Returns the <i>bottom</i> concept, i.e.&nbsp;the concept that contains
	 * all attributes.
	 * @return the <i>bottom</i> concept.
	 */
	public Concept bottom ();
	
	
	/**
	 * Returns an iterator over the lower neighbors of <code>concept</code>.
	 * <p>
	 * There are no guarantees concerning the order in which the lower
	 * neighbors are returned. The exact order may depend on the
	 * implementation of this method and on the implementation of the
	 * class of the underlying relation and on other factors.
	 * @param concept the concept whose lower neighbors shall be computed.
	 * @return an iterator over the lower neighbors of <code>concept</code>.
	 */
	public Iterator<Concept> lowerNeighbors (Concept concept);
	
	/**
	 * Returns an iterator over the upper neighbors of <code>concept</code>.
	 * <p>
	 * There are no guarantees concerning the order in which the upper
	 * neighbors are returned. The exact order may depend on the
	 * implementation of this method and on the implementation of the
	 * class of the underlying relation and on other factors.
	 * @param concept the concept whose upper neighbors shall be computed.
	 * @return an iterator over the upper neighbors of <code>concept</code>.
	 */
	public Iterator<Concept> upperNeighbors (Concept concept);
	
	
	/**
	 * Returns an iterator over all concepts of this lattice.
	 * <p>
	 * The concepts will be returned in the order specified by the
	 * <code>trav</code> argument.
	 * @see Traversal
	 * @param trav the desired traversal.
	 * @return an iterator over all concepts of this lattice.
	 */
	public Iterator<Concept> conceptIterator(Traversal trav);
	
	
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
	public Iterator<Edge> edgeIterator(Traversal trav);
	
	
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
	public Iterator<Edge> violationIterator(int supp, float conf, int diff);	
}


package colibri.lib;


/**
 * Enumeration of supported traversals for concept and edge iterators.
 * <p>
 * The lattice can be traversed either depth-first or breadth-first.
 * Top-down-breadth-first traversal guarantees, that each concept
 * will be returned before its subconcepts. Similarly,
 * bottom-up-breadth-first traversal guarantees, that each concept
 * will be returned before its superconcepts.
 * The exact order in which the concepts are traversed depends on the
 * lexical ordering of their object or attribute sets and sometimes on
 * the size of their object or attribute sets.
 * <p>
 * This is the definition of lexical ordering. If two sets
 * are different then the disjunction of those two sets is
 * not empty. Let <i>k</i> be the smallest element in that disjunction.
 * Then the set <i>not</i> containing <i>k</i> lexically precedes
 * the other set.
 * <p>
 * Thus, a subset always lexically precedes its superset. However,
 * the converse is not true, i.e.&nbsp;the lexically smaller set is not
 * always a subset of a set that is lexically greater.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public enum Traversal {
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in descending lexical order
	 * of their attribute sets.
	 */
	BOTTOM_ATTR,
	
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in descending order
	 * of the size of their attribute sets,
	 * i.e.&nbsp;concepts containing more attributes will be returned
	 * before concepts containing fewer attributes.
	 * Concepts that have the same number of attributes will be
	 * returned in descending lexical order of their attribute sets.
	 */
	BOTTOM_ATTRSIZE,
	
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in ascending lexical order
	 * of their object sets.
	 */
	BOTTOM_OBJ,
	
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in ascending order
	 * of the size of their object sets.
	 * i.e.&nbsp;concepts containing fewer objects will be returned
	 * before concepts containing more objects.
	 * Concepts that have the same number of objects will be
	 * returned in ascending lexical order of their object sets.
	 */
	BOTTOM_OBJSIZE,
	
	
	/**
	 * Bottom-up and depth-first traversal.
	 */
	BOTTOM_DEPTHFIRST,
	
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in ascending lexical order
	 * of their attribute sets.
	 */
	TOP_ATTR,
	
	/**
	 * Top-down and breadth-first traversal.
	 * Concepts will be returned in ascending order
	 * of the size of their attribute sets.
	 * i.e.&nbsp;concepts containing fewer attributes will be returned
	 * before concepts containing more attributes.
	 * Concepts that have the same number of attributes will be
	 * returned in ascending lexical order of their attribute sets.
	 */
	TOP_ATTRSIZE,
	
	/**
	 * Bottom-up and breadth-first traversal.
	 * Concepts will be returned in descending lexical order
	 * of their object sets.
	 */
	TOP_OBJ,
	
	/**
	 * Top-down and breadth-first traversal.
	 * Concepts will be returned in descending order
	 * of the size of their object sets.
	 * i.e.&nbsp;concepts containing more objects will be returned
	 * before concepts containing fewer objects.
	 * Concepts that have the same number of objects will be
	 * returned in descending lexical order of their object sets.
	 */
	TOP_OBJSIZE,
	
	/**
	 * Top-down and depth-first traversal.
	 */
	TOP_DEPTHFIRST;
}

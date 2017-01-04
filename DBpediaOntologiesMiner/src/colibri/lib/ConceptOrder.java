package colibri.lib;

/**
 * Enumeration of types of concept orders.
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
enum ConceptOrder {
	/**
	 * Compare by the lexical order of the object set.
	 */
	OBJ_STD,
	
	/**
	 * Compare by the size of the object set.
	 * If the sizes of two object sets are equal,
	 * compare by the lexical order of the object set.
	 */
	OBJ_SIZEFIRST,
	
	/**
	 * Compare by the lexical order of the attribute set.
	 */
	ATTR_STD,
	
	
	/**
	 * Compare by the size of the attribute set.
	 * If the sizes of two attribute sets are equal,
	 * compare by the lexical order of the attribute set.
	 */
	ATTR_SIZEFIRST;
}

package colibri.lib;
import java.util.Set;

/**
 * This interface extends the <code>Set</code> interface and implements
 * the <code>Comparable</code> interface.
 * 
 * All elements inserted into a <code>ComparableSet</code> must be
 * <code>Comparable</code> objects that are mutually comparable.
 * That means, for all elements <code>e1</code> and <code>e2</code> in
 * the <code>ComparableSet</code>, the call <code>e1.compareTo(e2)</code>
 * must not throw an exception.
 * <p>
 * Furthermore, the ordering must be <i>consistent with equals</i>.
 * (For a precise definition of <i>consistent with equals</i> see the
 * <code>Comparable</code> interface.) In addition,  an object must not
 * be mutated while it is an element of a <code>ComparableSet</code>.
 * <p>
 * Note that implementations of this interface may further restrict
 * which elements may be added to the set. In addition, write protection
 * can be activated in order to turn the <code>ComparableSet</code> into
 * an unmodifiable set. Any attempt to change the contents of an
 * unmodifiable set will cause an exception to be thrown.
 * <p>
 * Note that although the elements must be <code>Comparable</code>
 * objects, there are no guarantees concerning the order in which the
 * elements are returned by the iterator obtained when calling the
 * <code>iterator()</code> method.
 * <p>
 * Two instances of <code>ComparableSet</code> can be mutually compared
 * if all elements contained in the sets are mutually comparable.
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
public interface ComparableSet extends Set<Comparable>, Comparable, Cloneable {
	/**
	 * Creates and returns a copy of this <code>ComparableSet</code>.
	 * <p>
	 * The purpose of this method is to create a copy of a
	 * <code>ComparableSet</code> that is an instance of the same class
	 * as the original object.
	 * <p>
	 * If it is not necessary that the copy is an instance of the same
	 * class it is recommended not to use this method but a constructor
	 * of a class that implements <code>java.util.Collection</code>
	 * instead. For example, a copy of the <code>ComparableSet</code>
	 * <code>cs</code> can be created using 
	 * <code>Set copy = new HashSet(cs)</code> rather than 
	 * <code>Set copy = cs.clone()</code> if the copy does not need
	 * to be a <code>ComparableSet</code> object.
	 * @see java.util.Collection
	 * @return a copy of this instance that is not write protected.
	 */
	public Object clone();
	
	/**
	 * Returns <code>true</code> if this set contains none of the elements
	 * of the specified argument set, i.e.&nbsp;if the sets are <i>disjoint</i>.
	 * @param set set to be checked for being disjoint with this set.
	 * @return <code>true</code> if this set contains none of the elements of the
	 * specified argument set.
	 */
	public boolean containsNone(ComparableSet set);
	
	
	/**
	 * Returns <code>true</code> if either this set contains none of
	 * the elements of the specified argument set, or the only
	 * element that is contained both in this set and in the argument
	 * set is the specified <code>Comparable</code> object.
	 * @param set set to be checked for being disjoint with the set
	 * containing all elements of this set except <code>c</code>.
	 * @param c an element that may be contained in both sets.
	 * @return <code>true</code> if either this set and the argument set are
	 * disjoint or <code>c</code> is their only common element.
	 */
	public boolean containsNone(ComparableSet set, Comparable c);
	
	/**
	 * Activates write protection on this set.
	 * <p>
	 * After this method has been called, this set shall be unmodifiable,
	 * i.e.&nbsp;if there is a call attempting to change the contents of 
	 * this set, an exception will be thrown.
	 * @return <code>true</code> iff write protection is activated.
	 */
	public boolean disallowChanges();
	
	
	/**
	 * Compares two <code>ComparableSet</code> objects lexically.
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
	 * @param anotherSet the <code>ComparableSet</code> to be compared.
	 * @return the value 0 if the argument set is equal to this set;
	 * a value less than zero if this set is lexically smaller than
	 * the argument set; a value greater than zero if this set is
	 * lexically greater than the argument set.
	 */
	public int compareTo(Object anotherSet);
}

package colibri.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * This class implements the <code>ComparableSet</code> interface
 * and extends <code>java.util.HashSet</code>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class ComparableHashSet extends HashSet<Comparable> implements ComparableSet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2550395488238965571L;
	
	private	boolean writeProtected = false;
	
	/**
	 * Constructs a new, empty set.
	 *
	 */
	public ComparableHashSet() {
		super();
	}
	
	
	/**
	 * Constructs a new set containing the elements contained in the
	 * specified collection.
	 * @param c the collection whose elements are to be placed into this set.
	 */
	public ComparableHashSet(Collection<Comparable> c) {
		super(c);
	}
	
	
	/**
	 * Activate write protection on this set.
	 * <p>
	 * After this method has been called, this set shall be unmodifiable,
	 * i.e.&nbsp;if there is a call attempting to change the contents of 
	 * this set, an exception will be thrown.
	 * @return iff write protection is activated.
	 */
	public boolean disallowChanges() {
		return false;
	}
	
	
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
	public int compareTo(Object anotherSet) {
		if (this.isEmpty()) {
			if (((Set)anotherSet).isEmpty()) {
				return 0;
			}
			else {
				return -1;
			}
		}
		else if (((Set)anotherSet).isEmpty()) {
			return 1;
		}
		else {
			Iterator<Comparable> it1 = this.iterator();
			Iterator<Comparable> it2 = ((Set)anotherSet).iterator();
			Comparable leastDistinct1 = null;
			Comparable leastDistinct2 = null;
			
			while (it1.hasNext()) {
				Comparable next = it1.next();
				if ((leastDistinct1 == null || next.compareTo(leastDistinct1) < 0) && !((ComparableSet)anotherSet).contains(next)) {
					leastDistinct1 = next;
				}
			}
			
			while (it2.hasNext()) {
				Comparable next = it2.next();
				if ((leastDistinct2 == null || next.compareTo(leastDistinct2) < 0) && !this.contains(next)) {
					leastDistinct2 = next;
				}
			}
			
			if (leastDistinct1 == null) {
				if (leastDistinct2 == null) {
					return 0;
				}
				else {
					return -1;
				}
			}
			else if (leastDistinct2 == null) {
				return 1;
			}
			else if (leastDistinct1.compareTo(leastDistinct2) < 0) {
				return 1;
			}
			else {
				return -1;
			}
			
		}
	}
	
	
	/**
	 * Creates and returns a copy of this <code>ComparableSet</code>.
	 * <p>
	 * The purpose of this method is to create a copy of a
	 * <code>ComparableHashSet</code> that is itself an instance of 
	 * <code>ComparableHashSet</code>.
	 * <p>
	 * If it is not necessary that the copy is an instance of
	 * <code>ComparableHashSet</code> it is recommended not to use 
	 * this method but a constructor of a class that implements 
	 * <code>java.util.Collection</code> instead. For example, 
	 * a copy of the <code>ComparableHashSet</code>
	 * <code>cs</code> can be created using 
	 * <code>Set copy = new HashSet(cs)</code> rather than 
	 * <code>Set copy = cs.clone()</code> if the copy does not need
	 * to be an instance of <code>ComparableHashSet</code>.
	 * @see java.util.Collection
	 * @return a copy of this instance that is not write protected.
	 */
	public Object clone() {
		return super.clone();
	}
	
	
	/**
	 * Returns <code>true</code> if this set contains none of the elements
	 * of the specified argument set, i.e.&nbsp;if the sets are <i>disjoint</i>.
	 * @param set set to be checked for being disjoint with this set.
	 * @return <code>true</code> if this set contains none of the elements of the
	 * specified argument set.
	 */
	public boolean containsNone(ComparableSet set) {
		if (this.size() < set.size()) {
			Iterator<Comparable> it = this.iterator();
			while(it.hasNext()) {
				if (set.contains(it.next()))
					return false;
			}
		}
		else {
			Iterator<Comparable> it = set.iterator();
			while(it.hasNext()) {
				if (this.contains(it.next()))
					return false;
			}
		}
		
		return true;
	}
	
	
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
	public boolean containsNone(ComparableSet set, Comparable c) {
		if (this.size() < set.size()) {
			Iterator<Comparable> it = this.iterator();
			while(it.hasNext()) {
				Comparable next = it.next();
				if (!next.equals(c) && set.contains(next))
					return false;
			}
		}
		else {
			Iterator<Comparable> it = set.iterator();
			while(it.hasNext()) {
				Comparable next = it.next();
				if (!next.equals(c) && this.contains(next))
					return false;
			}
		}
		
		return true;
	}


	public boolean add(Comparable arg0) throws UnmodifiableSetException {
		if (!writeProtected)
			return super.add(arg0);
		else
			throw new UnmodifiableSetException();
	}


	public boolean addAll(Collection<? extends Comparable> arg0) throws UnmodifiableSetException {
		if (!writeProtected)
			return super.addAll(arg0);
		else
			throw new UnmodifiableSetException();
	}


	public void clear() throws UnmodifiableSetException {
		if (!writeProtected)
			super.clear();
		else
			throw new UnmodifiableSetException();
		
	}


	public boolean remove(Object arg0) throws UnmodifiableSetException {
		if (!writeProtected)
			return super.remove(arg0);
		else
			throw new UnmodifiableSetException();
	}


	public boolean removeAll(Collection<?> arg0) throws UnmodifiableSetException {
		if (!writeProtected)
			return super.removeAll(arg0);
		else
			throw new UnmodifiableSetException();
	}


	public boolean retainAll(Collection<?> arg0) throws UnmodifiableSetException {
		if (!writeProtected)
			return super.retainAll(arg0);
		else
			throw new UnmodifiableSetException();
	}
}

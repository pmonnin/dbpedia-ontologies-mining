package colibri.lib;


/**
 * Representation of a pair of concepts that are neighbors of each other
 * in a concept lattice.
 * By definition a concept <code>u</code> is the upper neighbor of a concept
 * <code>l</code> in a concept lattice, iff the object set of <code>l</code> is
 * a strict subset of the object set of <code>u</code> and there exists no other
 * concept <code>c</code> in the lattice such that the object set of <code>l</code> is a
 * strict subset of the object set of <code>c</code> and the object set of
 * <code>c</code> is a strict subset of the object set of <code>u</code>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class Edge {
	private Concept upper;
	private Concept lower;
	
	
	/**
	 * Constructs an <code>Edge</code>-object from the specified concepts.
	 * <p>
	 * Note that the constructor does not check whether or not <code>upper</code>
	 * is indeed an upper neighbor of <code>lower</code>.
	 * @param upper the upper neighbor.
	 * @param lower the lower neighbor.
	 */
	Edge (Concept upper, Concept lower) {
		if (upper == null || lower == null) {
			throw new IllegalArgumentException();
		}
		this.upper = upper;
		this.lower = lower;
	}
	
	
	/**
	 * Returns the upper neighbor
	 * contained in this <code>Edge</code> object.
	 * @return the upper neighbor contained in 
	 * this <code>Edge</code> object.
	 */
	public Concept getUpper() {
		return upper;
	}
	
	
	/**
	 * Returns the lower neighbor
	 * contained in this <code>Edge</code> object.
	 * @return the lower neighbor contained in 
	 * this <code>Edge</code> object.
	 */
	public Concept getLower() {
		return lower;
	}
	
	
	/**
	 * Returns a string representation of this edge.
	 * <p>
	 * That string representation consists of the string
	 * representation of the lower neighbor, followed by
	 * "<code> -> </code>", followed by the string representation
	 * of the upper neighbor.
	 * @return a string representation of this edge.
	 */
	public String toString() {
		return lower.toString() + " -> " + upper.toString();
	}
	
	
	/**
	 * Compares the specified object with this <code>Edge</code> for equality.
	 * Returns <code>true</code> iff <code>object</code> is an
	 * instance of <code>Edge</code> and 
	 * <code>getUpper().equals(object.getUpper()) == true</code> and
	 * <code>getLower().equals(object.getLower()) == true</code>.
	 * @param object the object to be compared for equality with this <code>Edge</code>.
	 * @return <code>true</code> iff this object and <code>object</code>
	 * are logically equal.
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Edge)) {
			return false;
		}
		else {
			return (upper.equals(((Edge)object).upper) && lower.equals(((Edge)object).lower));
		}
	}
	
	
	/**
	 * Returns the hash code value for this <code>Edge</code>.
	 * <p>
	 * The hash code value of an <code>Edge</code> is defined as the sum of
	 * the hash code values of its concepts.
	 * @return a hash code value for this <code>Edge</code>.
	 */
	public int hashCode() {
		if (upper == null || lower == null) {
			return 0;
		}
		else {
			return upper.hashCode() + lower.hashCode();
		}
	}
}

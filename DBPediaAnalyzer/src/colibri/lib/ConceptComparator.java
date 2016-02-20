package colibri.lib;
import java.util.Comparator;


/**
 * A comparator that imposes a total ordering on concepts.
 * <p>
 * The ordering imposed by this comparator is consistent with
 * equals, i.e.&nbsp;<code>compare(c1, c2)</code> returns 0 if and only if
 * <code>c1.equals(c2)</code> returns <code>true</code>, provided
 * that the <code>c1</code> and <code>c2</code> belong to the same
 * concept lattice.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class ConceptComparator implements Comparator<Concept> {
	
	private ConceptOrder order;
	private int factor;
	
	
	/**
	 * Constructs a concept comparator that compares the concepts
	 * in increasing order of their object set.
	 *
	 */
	ConceptComparator() {
		this(ConceptOrder.OBJ_STD);
	}
	
	
	/**
	 * Constructs a concept comparator that compares the concepts
	 * according to the specified order.
	 * @param order the order by which the concepts will be compared.
	 */
	ConceptComparator(ConceptOrder order) {
		this(order, 1);
	}
	
	
	/**
	 * Constructs a concept comparator that compares the concepts
	 * according to the specified order. The result of the comparation
	 * will then be multiplied by the <code>factor</code> provided.
	 * <p>
	 * If <code>factor</code> is negative the comparation will be
	 * inverted. For example, if <code>order</code> is
	 * <code>ConceptOrder.OBJ_SIZEFIRST</code> and <code>factor</code>
	 * is positive, a concept that containing more objects is
	 * regarded as <i>greater</i> then a concept with fewer objects
	 * by this comparator. If <code>factor</code> is negative, a
	 * concept containing more objects will be regarded as
	 * <i>smaller</i> then a concept with fewer objects by this comparator.
	 * @param order the order by which the concepts will be compared.
	 * @param factor the factor by which the result of the comparation
	 * will be multiplied.
	 * @throws IllegalArgumentException if <code>factor == 0</code>
	 */
	ConceptComparator(ConceptOrder order, int factor) throws IllegalArgumentException {
		this.order = order;
		this.factor = factor;
		
		if (factor == 0)
			throw new IllegalArgumentException("Using 0 as a factor will " 
				+ "cause the compare method to always return 0, " 
				+ "which means all concepts are equal. "
				+ "I really doubt you intend to get that result.");
	}
	
	
	/**
	 * Compares the two argument concepts for order. Returns a negative
	 * integer if the first concept is smaller than the second, a positive
	 * integer if the first argument is greater than the second and zero
	 * if both concepts are equal. Whether a concept is regarded to be
	 * smaller or larger than the other one is determined by the arguments
	 * passed to the constructor.
	 * @param o1 the first concept to be compared.
	 * @param o2 the second concept to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 * concept is less, equal, or greater than the second one.
	 */
	public int compare(Concept o1, Concept o2) {
		switch (order) {
		case OBJ_STD: {
			ComparableSet objects1 = o1.getObjects();
			ComparableSet objects2 = o2.getObjects();
			return objects1.compareTo(objects2) * factor;
		}
		case OBJ_SIZEFIRST: {
			ComparableSet objects1 = o1.getObjects();
			ComparableSet objects2 = o2.getObjects();
			
			if (objects1.size() < objects2.size()) {
				return -1 * factor;
			}
			else if (objects1.size() > objects2.size()) {
				return 1 * factor;
			}
			else {
				return objects1.compareTo(objects2) * factor;				
			}
		}
		case ATTR_STD: {
			ComparableSet attributes1 = o1.getAttributes();
			ComparableSet attributes2 = o2.getAttributes();
			return attributes1.compareTo(attributes2) * factor;
		}
		case ATTR_SIZEFIRST: {
			ComparableSet attributes1 = o1.getAttributes();
			ComparableSet attributes2 = o2.getAttributes();
			
			if (attributes1.size() < attributes2.size()) {
				return -1 * factor;
			}
			else if (attributes1.size() > attributes2.size()) {
				return 1 * factor;
			}
			else {
				return attributes1.compareTo(attributes2) * factor;				
			}
		}
		default:
			throw new RuntimeException("The comparator is in some strange state.");
		}
	}
}

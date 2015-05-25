package colibri.lib;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import colibri.lib.Agenda;




/**
 * An iterator over the concepts contained in the underlying
 * lattice. Traverses the lattice <i>top-down</i> and 
 * <i>breadth-first</i>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class ConceptIteratorTDBF implements Iterator<Concept> {
	
	private Lattice lattice;
	private Comparator<Concept> comparator;
	private Agenda<Concept> agenda;
	
	
	/**
	 * Constructs an iterator that iterates over the concepts
	 * of the given lattice in the specified order.
	 * @param lattice the underlying lattice.
	 * @param order the order to be used to compare
	 * the concepts.
	 */
	ConceptIteratorTDBF(Lattice lattice, ConceptOrder order) {
		this.lattice = lattice;
		
		switch (order) {
		case OBJ_STD:
		case OBJ_SIZEFIRST:
			comparator = new ConceptComparator(order, -1);
			break;
		case ATTR_STD:
		case ATTR_SIZEFIRST:
			comparator = new ConceptComparator(order);
			break;
		default:
			throw new IllegalArgumentException("This concept order is not supported.");
		}

		agenda = new Agenda<Concept>(comparator);
		Concept top = lattice.top();
		agenda.add(top);
	}
	
	
	/**
	 * Returns <code>true</code> iff the lattice contains more concepts.
	 * In other words returns <code>true</code> if the lattice contains
	 * a concept that has not been returned by a previous
	 * call of the <code>next</code> method.
	 * @return <code>true</code> iff the lattice contains more concepts.
	 */
	public boolean hasNext() {
		return !agenda.isEmpty();
	}
	
	
	/**
	 * Returns the next concept in the iteration.
	 * @return the next concept in the iteration.
	 */
	public Concept next() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		Concept concept = agenda.pop();
		Iterator<Concept> iterator = lattice.lowerNeighbors(concept);
		while (iterator.hasNext()) {
			agenda.add(iterator.next());
		}
		return concept;
	}
	
	
	/**
	 * Throws an <code>UnsupportedOperationException</code>
	 * since concepts may not be removed from the lattice.
	 * @throws <code>UnsupportedOperationException</code>.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Concepts can not be removed from the lattice.");
	}
	
}

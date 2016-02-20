package colibri.lib;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * An ordered collection that contains no duplicate elements.
 * The order is determined by a <code>Comparator</code>, which is
 * passed to the constructor.
 * Note that the ordering implemented by the comparator
 * must be consistent with equals. (See the 
 * <code>Comparator</code> interface for a
 * precise definition of <i>consistent with equals</i>).
 * <p>
 * An element can be added to the collection if it is
 * mutually comparable to the elements that are already present
 * in the agenda by the comparator passed to the constructor.
 * <p>
 * The <code>pop</code> method returns the smallest element
 * contained in the agenda and removes it from the agenda.
 * @param <T> The type of the elements stored in the agenda.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class Agenda<T> {
	private SortedSet<T> agenda;
	
	
	/**
	 * Constructs an agenda whose elements will be ordered
	 * according to the order implemented by the comparator
	 * <code>c</code>.
	 * <p>
	 * The comparator is used to order the concepts added
	 * to the agenda. For the agenda to work properly it is
	 * necessary that the objects added to it are mutually
	 * comparable, i.e.&nbsp;<code>c.compare(e1, e2)</code> must
	 * not throw an exception for any elements <code>e1</code>,
	 * <code>e2</code> in the agenda.
	 * <p>
	 * Note that the ordering implemented by the comparator
	 * must be consistent with equals. (See the 
	 * <code>Comparator</code> interface for a
	 * precise definition of <i>consistent with equals</i>).
	 * @param c The comparator that will be used to compare
	 * the elements. For the agenda to work properly,
	 * the comparator must be <i>consistent with equals</i>
	 * (See the <code>Comparator</code> interface for a
	 * precise definition of <i>consistent with equals</i>).
	 */
	Agenda (Comparator<T> c) {
		agenda = new TreeSet<T>(c);
	}
	
	
	/**
	 * Returns the smallest element contained in the agenda
	 * and removes it from the agenda.
	 * @return the smallest element in the agenda.
	 */
	T pop() {
		T first = agenda.first();
		agenda.remove(first);		
		
		return first;
	}
	
	
	/**
	 * Adds an element to the Agenda.
	 * @param item the element to be added.
	 */
	void add(T item) {
		agenda.add(item);
	}
	
	
	/**
	 * Returns true iff the agenda does not contain any elements.
	 * @return true iff the agenda does not contain any elements.
	 */
	boolean isEmpty() {
		return agenda.isEmpty();
	}
}

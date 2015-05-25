package colibri.lib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;



/**
 * An iterator over the concepts contained in the underlying
 * lattice. Traverses the lattice <i>bottom-up</i> and 
 * <i>depth-first</i>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class ConceptIteratorBUDF implements Iterator<Concept> {
	
	private Lattice lattice;
	
	private LinkedList<Set<ComparableSet>> past;	//stores the attribute sets of the concepts that have already been completely explored
	private LinkedList<Concept> current;			//stores the current path
	private LinkedList<Iterator<Concept>> future;	//stores the concepts that will be considered in the future
	
	private Concept nextConcept;					//stores the concept that will be returned next by next()
	
	
	/**
	 * Constructs an iterator that iterates over the concepts
	 * of the given lattice.
	 * @param lattice the underlying lattice.
	 */	
	ConceptIteratorBUDF(Lattice lattice) {
		this.lattice = lattice;
		
		past = new LinkedList<Set<ComparableSet>>();
		current = new LinkedList<Concept>();
		future = new LinkedList<Iterator<Concept>>();
		
		Concept bottom = lattice.bottom();
		
		//create an iterator for the uppermost level (thus only containing the top concept) of the future list
		LinkedList<Concept> bottomList = new LinkedList<Concept>();
		bottomList.add(bottom);
		Iterator<Concept> bottomIterator = bottomList.iterator();
		
		//add this iterator to the future list
		future.addLast(bottomIterator);
		//for easy handling, the past list should always be as long as the future list
		past.addLast(new TreeSet<ComparableSet>());
	}
	
	
	/**
	 * Returns <code>true</code> iff the lattice contains more concepts.
	 * In other words returns <code>true</code> if the lattice contains
	 * a concept that has not been returned by a previous
	 * call of the <code>next</code> method.
	 * @return <code>true</code> iff the lattice contains more concepts.
	 */
	public boolean hasNext() {
		return ((nextConcept != null) || computeNext());
	}
	
	
	/**
	 * Returns the next concept in the iteration.
	 * @return the next concept in the iteration.
	 */
	public Concept next() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		Concept concept = nextConcept;
		nextConcept = null;
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
	
	
	/**
	 * Returns <code>true</code> iff <code>concept</code> has
	 * already been discovered by a previous call of <code>computeNext</code>.
	 * @param concept
	 * @return <code>true</code> iff <code>concept</code> has
	 * already been discovered by a previous call of <code>computeNext</code>.
	 */
	private boolean seenBefore(Concept concept) {
		
		Iterator<Set<ComparableSet>> outerIterator = past.iterator();
		while (outerIterator.hasNext()) {
			Iterator<ComparableSet> innerIterator = outerIterator.next().iterator();
			while (innerIterator.hasNext()) {
				if (concept.getObjects().containsAll(innerIterator.next())) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Computes the next concept and stores it in the nextConcept variable.
	 * Computes the next concept only if the nextConcept variable contains null.
	 * If there are no more concepts, the nextConcept variable will be set to null and false will be returned.
	 * @return false iff there is no unexplored concept or nextConcept was not null.
	 */
	private boolean computeNext() {
		boolean accept = false;
		
		Concept concept = null;
		
		//compute only if there is something left in the future list
		//and nextConcept doesn't already have the next concept stored
		while (!future.isEmpty() && !accept && nextConcept == null) {
			//get the next concept from the future list
			Iterator<Concept> futureLevel = future.getLast();
			concept = futureLevel.next();
			
			//if this concept hasn't already been returned:
			if (!seenBefore(concept)) {
				accept = true;
				
				//compute the lowerNeighbors and add them to the next level of the future list
				Iterator<Concept> upperNeighbors = lattice.upperNeighbors(concept);
				future.addLast(upperNeighbors);
				//for easy handling later (past list should be as long as future list add another level to the past list
				past.addLast(new TreeSet<ComparableSet>());
				//set futureLevel to point to the last level in the future list
				futureLevel = future.getLast();
				//add the concept to the next level of the current list
				current.addLast(concept);
			}
			
			//if there is nothing left at the current level of the future list, clean up
			while(!future.isEmpty() && (futureLevel == null || !futureLevel.hasNext())) {
				//the last level of the future list should be removed, as there's nothing left there
				future.removeLast();
				//past list should always be as long as future list
				past.removeLast();
				
				if (!future.isEmpty()) {
					//set future level to point to the last level in the future list
					//(if future list is already empty, the while loop will be left anyway)
					futureLevel = future.getLast();
				}
				
				
				if (!current.isEmpty()) {
					//add the object set of the concept that has now been completely processed
					//(i.e. all it's subconcepts have already been explored)
					//to the past list
					ComparableSet objects = current.getLast().getObjects();
					past.getLast().add(objects);
					
					//remove the last level of the current list, since this concept has been completely processed
					current.removeLast();
				}
				
			}
		}
		
		if (accept) {
			nextConcept = concept;		//store it where it has to be stored
		}
		else {
			nextConcept = null;			//store null if computation failed
		}
		
		return accept;
	}
}

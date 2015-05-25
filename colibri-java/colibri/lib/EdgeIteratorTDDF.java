package colibri.lib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;



/**
 * An iterator over the edges (i.e.&nbsp;pairs of neighboring concepts)
 * of the underlying lattice. Traverses the lattice <i>top-down</i> 
 * and <i>depth-first</i>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class EdgeIteratorTDDF implements Iterator<Edge> {
	
	private Lattice lattice;
	
	private LinkedList<Set<ComparableSet>> past;	//stores the attribute sets of the concepts that have already been completely explored
	private LinkedList<Concept> current;			//stores the current path
	private LinkedList<Iterator<Concept>> future;	//stores the concepts that will be considered in the future
	
	
	/**
	 * Constructs an iterator.
	 * @param lattice the underlying lattice.
	 */
	EdgeIteratorTDDF(Lattice lattice) {
		this.lattice = lattice;
		
		past = new LinkedList<Set<ComparableSet>>();
		current = new LinkedList<Concept>();
		future = new LinkedList<Iterator<Concept>>();
		
		Concept top = lattice.top();
		
		//create an iterator for the uppermost level (thus only containing the top concept) of the future list
		LinkedList<Concept> topList = new LinkedList<Concept>();
		topList.add(top);
		Iterator<Concept> topIterator = topList.iterator();
		
		//add this iterator to the future list
		future.addLast(topIterator);
		//for easy handling, the past list should always be as long as the future list
		past.addLast(new TreeSet<ComparableSet>());
		
		//compute the level below top to be able to return the first edge later
		future.addLast(lattice.lowerNeighbors(top));
		past.addLast(new TreeSet<ComparableSet>());
		current.addLast(top);
	}
	
	
	/**
	 * Returns <code>true</code> iff there is
	 * an edge that has not been returned by a previous
	 * call of the <code>next</code> method.
	 * @return <code>true</code> if there is another edge
	 * that has not been returned yet.
	 */
	public boolean hasNext() {
		return (!future.isEmpty() && !current.isEmpty());
	}
	
	
	/**
	 * Returns the next edge in the iteration.
	 * @return the next edge in the iteration.
	 */
	public Edge next() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		return computeNext();
	}
	
	
	/**
	 * Throws an <code>UnsupportedOperationException</code>
	 * since edges may not be removed from the lattice.
	 * @throws <code>UnsupportedOperationException</code>.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Edges can not be removed from the lattice.");
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
				if (concept.getAttributes().containsAll(innerIterator.next())) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Computes the next edge.
	 * @return the next edge to be returned.
	 */
	private Edge computeNext() {
		if (future.isEmpty()) {
			return null;
		}
		
		Concept concept = null;
		
		//get the next concept from the future list
		Iterator<Concept> futureLevel = future.getLast();
		concept = futureLevel.next();
		
		Edge edge = new Edge (current.getLast(), concept);
		
		//if this concept hasn't already been explored:
		if (!seenBefore(concept)) {
			
			//compute the lowerNeighbors and add them to the next level of the future list
			Iterator<Concept> lowerNeighbors = lattice.lowerNeighbors(concept);
			future.addLast(lowerNeighbors);
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
				//add the attribute set of the concept that has now been completely processed
				//(i.e. all it's subconcepts have already been explored)
				//to the past list
				ComparableSet attributes = current.getLast().getAttributes();
				past.getLast().add(attributes);
				
				//remove the last level of the current list, since this concept has been completely processed
				current.removeLast();
			}
			
		}
		
		return edge;
	}
}

package colibri.lib;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that iterates over neighboring concepts of a
 * concept lattice that are similar to each other.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class ViolationIteratorTD implements Iterator<Edge>{
	
	private int supp;
	private float conf;
	private int diff;
	
	private Iterator<Edge> edgeIterator;
	
	private Edge nextViolation;
	
	/**
	 * 
	 * @param edgeIterator
	 * @param supp the minimal support, i.e.&nbsp;the minimal number of objects contained
	 * in the lower neighbor.
	 * @param conf the minimal confidence, i.e.&nbsp;the minimal fraction l/u, where
	 * l is the number of objects in the lower neighbor and u is the
	 * number of objects in the upper neighbor. Must be a value between
	 * 0 and 1.
	 * @param diff the maximal difference between the number of attributes
	 * in the lower neighbor and the number of attributes in the upper
	 * neighbor.
	 */
	ViolationIteratorTD(Iterator<Edge> edgeIterator, int supp, float conf, int diff) {
		this.supp = supp;
		this.conf = conf;
		this.diff = diff;
		
		this.edgeIterator = edgeIterator;
	}

	public boolean hasNext() {
		return (nextViolation != null || computeNext());
	}

	public Edge next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		Edge violation = nextViolation;
		nextViolation = null;
		return violation;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	
	private boolean computeNext() {
		while (nextViolation == null && edgeIterator.hasNext()) {
			Edge edge = edgeIterator.next();
			Concept upper = edge.getUpper();
			Concept lower = edge.getLower();
			
			if (upper.getObjects().size() < supp) {
				return false;
			}
			
			if (lower.getObjects().size() >= supp
					&& lower.getAttributes().size() - upper.getAttributes().size() <= diff
					&& (float)(lower.getObjects().size()) / (float)(upper.getObjects().size()) >= conf) {
				
				nextViolation = edge;
				return true;
			}
		}
		
		return false;
	}
}

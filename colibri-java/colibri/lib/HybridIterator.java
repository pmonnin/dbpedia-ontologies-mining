package colibri.lib;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class for the hybrid implementation of concept analysis.
 * It is used to iterate over the elements of a <code>HybridSet</code>.
 * The <code>next</code> method will return a <code>Comparable</code>
 * object since <code>HybridSet</code> implements 
 * <code>ComparableSet</code>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class HybridIterator implements Iterator<Comparable> {
	
	
	private BitSet bitset;
	private HybridTranslator translator;
	private int pos;
	
	
	/**
	 * Constructs an iterator over the <code>Comparable</code>
	 * objects represented by the <code>bitset</code> with respect
	 * to the <code>translator</code>.
	 * @param bitset the <code>BitSet</code> which is the basis of the iteration.
	 * @param translator the <code>HybridTranslator</code> that will be
	 * used to translate elements of the <code>bitset</code> into 
	 * <code>Comparable</code> objects.
	 */
	HybridIterator(BitSet bitset, HybridTranslator translator) {
		this.bitset = bitset;
		this.translator = translator;
		pos = bitset.nextSetBit(0);
	}
	
	
	/**
	 * Returns <code>true</code> iff there is another element in
	 * the iteration.
	 * @return <code>true</code> iff there is another element in
	 * the iteration.
	 */
	public boolean hasNext() {
		return pos >= 0;
	}
	
	
	/**
	 * Returns the next element in the iteration.
	 * @return the next element in the iteration.
	 */
	public Comparable next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		Comparable retVal = translator.translate(pos);
		pos = bitset.nextSetBit(pos + 1);
		return retVal;
	}
	
	
	/**
	 * Throws an <code>UnsupportedOperationException</code> since
	 * <code>HybridSet</code> objects are not modifiable.
	 * @throws <code>UnmodifiableOperationException</code>.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

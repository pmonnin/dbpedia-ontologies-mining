package colibri.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * A class responsible for the translation between <code>Comparable</code>
 * objects and integers for the bitset representation.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class HybridTranslator {
	private int size;
	private Vector<Comparable> mapToComparable;
	private Map<Comparable, Integer> mapToInteger;

	
	/**
	 * Constructs a translator for the specified set.
	 * @param set the set of objects/attributes that shall be
	 * translated.
	 */
	HybridTranslator(ComparableSet set) {
		size = set.size();
		
		mapToComparable = new Vector<Comparable>();
		mapToInteger = new HashMap<Comparable, Integer>();
		
		Iterator<Comparable> it = set.iterator();
		
		for (int i = 0; i < size; i++) {
			Comparable element = it.next();
			mapToComparable.add(i, element);
			mapToInteger.put(element, i);
		}
	}
	
	
	/**
	 * Returns the integer value of <code>c</code> as determined by
	 * this translator.
	 * @param c the <code>Comparable</code> whose integer translation
	 * shall be returned.
	 * @return the integer translation of <code>c</code>.
	 */
	int translate(Comparable c) {
		return mapToInteger.get(c);
	}
	
	
	/**
	 * Returns the <code>Comparable</code> object represented by <code>i</code>
	 * as determined by this translator.
	 * @param i the integer whose <code>Comparable</code> translation
	 * shall be returned.
	 * @return the <code>Comparable</code> translation of <code>i</code>.
	 */
	Comparable translate(int i) {
		return mapToComparable.get(i);
	}
	
	
	/**
	 * Returns the size of the set associated with this translator, 
	 * i.e.&nbsp;the set passed to the construtor.
	 * @return the size of the set associated with this translator.
	 */
	int getSize() {
		return size;
	}
}

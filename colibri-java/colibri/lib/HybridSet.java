package colibri.lib;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class that uses a <code>BitSet</code> as internal data structure
 * for fast performance but behaves like a <code>ComparableSet</code>
 * to the outside and can therefore be used with the standard implementation
 * of <code>LatticeImpl</code>.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
class HybridSet implements ComparableSet {
	
	private BitSet bitset;
	private int size;
	private HybridTranslator translator;
	private boolean writeProtected = false;
	
	
	private HybridSet(BitSet bitset, HybridTranslator translator) {
		this.translator = translator;
		this.size = translator.getSize();
		this.bitset = (BitSet)bitset.clone();
	}
	
	
	HybridSet(HybridTranslator translator) {
		this.translator = translator;
		size = translator.getSize();
		bitset = new BitSet(size);
	}
	
	
	HybridSet(ComparableSet set, HybridTranslator translator) {
		this.translator = translator;
		size = translator.getSize();
		bitset = new BitSet(size);
		addAll(set);
	}
	

	public int compareTo(Object o) {
		if (o instanceof HybridSet && ((HybridSet)o).translator == translator) {
			BitSet xor = new BitSet(size);
			xor.or(bitset);
			xor.xor(((HybridSet)o).bitset);
			if (xor.isEmpty()) {
				//there are no distinct elements, the sets are equal
				return 0;
			}
			
			if (bitset.get(xor.nextSetBit(0))) {
				//the smallest distinct element is in this set
				return 1;
			}
			else {
				//the smallest distinct element is in the other set
				return -1;
			}
		}
		else {
			ComparableSet me = new ComparableTreeSet(this);
			return me.compareTo(o);
		}
	}

	public boolean containsNone(ComparableSet set) {
		if (set instanceof HybridSet && ((HybridSet)set).translator == translator) {
			return !bitset.intersects(((HybridSet)set).bitset);
		}
		else {
			ComparableSet me = new ComparableTreeSet(this);
			return me.containsNone(set);
		}
	}

	public boolean containsNone(ComparableSet set, Comparable c) {
		if (set instanceof HybridSet && ((HybridSet)set).translator == translator) {
			BitSet b = (BitSet)((HybridSet)set).bitset.clone();
			b.clear(translator.translate(c));
			return !b.intersects(bitset);
		}
		else {
			ComparableSet me = new ComparableTreeSet(this);
			return me.containsNone(set, c);
		}
	}

	public boolean disallowChanges() {
		writeProtected = true;
		return true;
	}

	public boolean add(Comparable arg0) {
		if (!writeProtected) {
			int a = translator.translate(arg0);
			if (bitset.get(a)) {
				return false;
			}
			else {
				bitset.set(a);
				return true;
			}
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public boolean addAll(Collection<? extends Comparable> arg0) {
		if (!writeProtected) {
			if (arg0 instanceof HybridSet && ((HybridSet)arg0).translator == translator) {
				bitset.or(((HybridSet)arg0).bitset);
			}
			else {
				Iterator it = arg0.iterator();
				while (it.hasNext()) {
					bitset.set(translator.translate((Comparable)it.next()));
				}
			}
			return false; //TODO correct return
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public void clear() {
		if (!writeProtected) {
			bitset.clear();
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public boolean contains(Object arg0) {
		return bitset.get(translator.translate((Comparable)arg0));
	}

	public boolean containsAll(Collection<?> arg0) {
		if (arg0 instanceof HybridSet && ((HybridSet)arg0).translator == translator) {
			BitSet xor = (BitSet)bitset.clone();
			xor.xor(((HybridSet)arg0).bitset);
			return !xor.intersects(((HybridSet)arg0).bitset);
		}
		else {
			Iterator it = arg0.iterator();
			while (it.hasNext()) {
				if (!bitset.get(translator.translate((Comparable)it.next()))) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean isEmpty() {
		return bitset.isEmpty();
	}

	public Iterator<Comparable> iterator() {
		return new HybridIterator(bitset, translator);
	}

	public boolean remove(Object arg0) {
		if (!writeProtected) {
			bitset.clear(translator.translate((Comparable)arg0));
			return false; //TODO correct return
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public boolean removeAll(Collection<?> arg0) {
		if (!writeProtected) {
			if (arg0 instanceof HybridSet && ((HybridSet)arg0).translator == translator) {
				bitset.andNot(((HybridSet)arg0).bitset);
			}
			else {
				Iterator it = arg0.iterator();
				while (it.hasNext()) {
					bitset.set(translator.translate((Comparable)it.next()));
				}
			}
			return false; //TODO correct return
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public boolean retainAll(Collection<?> arg0) {
		if (!writeProtected) {
			if (arg0 instanceof HybridSet && ((HybridSet)arg0).translator == translator) {
				bitset.and(((HybridSet)arg0).bitset);
			}
			else {
				for(int i=bitset.nextSetBit(0); i>=0 && i<size; i=bitset.nextSetBit(i+1)) {
					if (!arg0.contains(translator.translate(i))) {
						bitset.clear(i);
					}
				}
			}
			return false; //TODO correct return
		}
		else {
			throw new UnmodifiableSetException();
		}
	}

	public int size() {
		return bitset.cardinality();
	}

	public Object[] toArray() {
		return new ComparableTreeSet(this).toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		return new ComparableTreeSet(this).toArray(arg0);
	}
	
	
	public Object clone () {
		return new HybridSet(bitset, translator);
	}
	
	
	public String toString() {
		String result = "[";
		Iterator<Comparable> it = iterator();
		
		while (it.hasNext()) {
			result += it.next();
			if (it.hasNext())
				result += ", ";
		}
		
		result += "]";
		
		return result;
	}
	
	
	public boolean equals (Object o) {
		if (!(o instanceof HybridSet)) {
			return false;
		}
		else {
			return (bitset.equals(((HybridSet)o).bitset) && translator == (((HybridSet)o).translator));
		}
	}
	
	
	public int hashCode() {
		return bitset.hashCode();
	}
}

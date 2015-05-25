package colibri.lib;

/**
 * A fast implementation of the <code>Lattice</code> interface. It uses
 * a data structure based on bitsets internally.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class HybridLattice extends LatticeImpl {
	/**
	 * Constructs a lattice from the passed <code>relation</code>.
	 * Note that the lattice object constructed by this constructor will not
	 * keep any reference to the <code>Relation</code> object specified.
	 * Therefore, changes to that <code>relation</code> will never affect
	 * the computations performed by this <code>Lattice</code> object.
	 * Since changes to the original <code>relation</code> will not affect
	 * the computations performed by this <code>Lattice</code> object,
	 * write protection on that <code>relation</code> will not be activated
	 * by this constructor.
	 * @param relation the relation from which the lattice shall be
	 * constructed.
	 */	
	public HybridLattice (Relation relation) {
		this.relation = new HybridRelation(relation);
		allObjects = this.relation.getAllObjects();
		allAttributes = this.relation.getAllAttributes();
		
		top = conceptFromAttributes(new HybridSet((((HybridRelation)(this.relation))).getAttributeTranslator()));			
		bottom = conceptFromObjects(new HybridSet((((HybridRelation)(this.relation))).getObjectTranslator()));
	}
	
}

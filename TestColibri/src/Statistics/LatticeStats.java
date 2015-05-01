package Statistics;

/**
 * Some statistics on the lattice
 * 
 * @author Damien Flament
 */
public class LatticeStats {
    
    private int nbConcept, depthLattice;
    
    public LatticeStats() {
    }

    public int getNbConcept() {
        return nbConcept;
    }

    public void setNbConcept(int nbConcept) {
        this.nbConcept = nbConcept;
    }

    public int getDepthLattice() {
        return depthLattice;
    }

    public void setDepthLattice(int depthLattice) {
        this.depthLattice = depthLattice;
    }  
}

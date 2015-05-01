package Statistics;

/**
 * Some statistics on the comparison 
 * 
 * @author Damien Flament
 */
public class ComparisonStats {

    private int nbCategoriesFound, nbCategoriesProposed, nbOntologiesFound, 
            nbOntologiesProposed, nbYagoFound, nbYagoProposed;
    
    public ComparisonStats() {
    }

    public int getNbCategoriesFound() {
        return nbCategoriesFound;
    }

    public void setNbCategoriesFound(int nbCategoriesFound) {
        this.nbCategoriesFound = nbCategoriesFound;
    }

    public int getNbCategoriesProposed() {
        return nbCategoriesProposed;
    }

    public void setNbCategoriesProposed(int nbCategoriesProposed) {
        this.nbCategoriesProposed = nbCategoriesProposed;
    }

    public int getNbOntologiesFound() {
        return nbOntologiesFound;
    }

    public void setNbOntologiesFound(int nbOntologiesFound) {
        this.nbOntologiesFound = nbOntologiesFound;
    }

    public int getNbOntologiesProposed() {
        return nbOntologiesProposed;
    }

    public void setNbOntologiesProposed(int nbOntologiesProposed) {
        this.nbOntologiesProposed = nbOntologiesProposed;
    }

    public int getNbYagoFound() {
        return nbYagoFound;
    }

    public void setNbYagoFound(int nbYagoFound) {
        this.nbYagoFound = nbYagoFound;
    }

    public int getNbYagoProposed() {
        return nbYagoProposed;
    }

    public void setNbYagoProposed(int nbYagoProposed) {
        this.nbYagoProposed = nbYagoProposed;
    }
}

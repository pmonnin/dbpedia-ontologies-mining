package statistics;

import dbpediaobjects.DBPage;
import pedialattice.PediaConcept;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class computing statistics over the built pedia lattice
 *
 * @author Pierre Monnin
 */
public class DBPediaLatticeStatistics {
    private int latticeDepth;
    private int conceptsNumber;
    private int edgesNumber;
    private int pagesNumber;

    public DBPediaLatticeStatistics() {
        this.latticeDepth = -1;
        this.conceptsNumber = -1;
        this.edgesNumber = -1;
        this.pagesNumber = -1;
    }

    public void computeStatistics(ArrayList<PediaConcept> pediaLattice, PediaConcept top, HashMap<String, DBPage> pages) {
        this.conceptsNumber = pediaLattice.size();
        this.pagesNumber = pages.size();

        this.edgesNumber = 0;
        for(PediaConcept c : pediaLattice) {
            this.edgesNumber += c.getChildren().size();
        }
    }

    public void displayStatistics() {
        System.out.println("=== LATTICE STATISTICS ===");
        System.out.println("Selected pages number: " + this.pagesNumber);
        System.out.println("Lattice edges number: " + this.edgesNumber);
        System.out.println("Lattice concepts number: " + this.conceptsNumber);
        System.out.println("Lattice depth: " + this.latticeDepth);
        System.out.println("==========================");
    }
}

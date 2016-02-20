package statistics;

import pedialattice.PediaConcept;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class computing statistics over the built pedia lattice
 *
 * @author Pierre Monnin
 */
@Deprecated
public class DBPediaLatticeStatistics {
    private int latticeDepth;
    private int conceptsNumber;
    private int edgesNumber;
    private int conceptsWithoutCategories;
    private int conceptsWithoutOntologies;
    private int conceptsWithoutYagoClasses;

    public DBPediaLatticeStatistics() {
        this.latticeDepth = 0;
        this.conceptsNumber = 0;
        this.edgesNumber = 0;
        this.conceptsWithoutCategories = 0;
        this.conceptsWithoutOntologies = 0;
        this.conceptsWithoutYagoClasses = 0;
    }

    public void computeStatistics(ArrayList<PediaConcept> pediaLattice, PediaConcept top, PediaConcept bottom) {

        this.conceptsNumber = pediaLattice.size();

        for(PediaConcept c : pediaLattice) {
            this.edgesNumber += c.getChildren().size();

            if(c.getCategories().size() == 0) {
                this.conceptsWithoutCategories++;
            }

            if(c.getOntologies().size() == 0) {
                this.conceptsWithoutOntologies++;
            }

            if(c.getYagoClasses().size() == 0) {
                this.conceptsWithoutYagoClasses++;
            }
        }

        Queue<PediaConcept> queue = new LinkedList<>();
        top.setDepth(0);
        queue.add(top);

        while(!queue.isEmpty()) {
            PediaConcept c = queue.poll();

            for(PediaConcept child : c.getChildren()) {
                if(child.getDepth() < c.getDepth() + 1) {
                    child.setDepth(c.getDepth() + 1);
                    if(!queue.contains(child)) {
                        queue.add(child);
                    }
                }
            }
        }

        this.latticeDepth = bottom.getDepth();
    }

    public void displayStatistics() {
        System.out.println("=== LATTICE STATISTICS ===");
        System.out.println("Lattice edges number: " + this.edgesNumber);
        System.out.println("Lattice concepts number: " + this.conceptsNumber);
        System.out.println("Lattice concepts without categories: " + this.conceptsWithoutCategories);
        System.out.println("Lattice concepts without ontologies: " + this.conceptsWithoutOntologies);
        System.out.println("Lattice concepts without yago classes: " + this.conceptsWithoutYagoClasses);
        System.out.println("Lattice depth: " + this.latticeDepth);
        System.out.println("==========================");
    }
}

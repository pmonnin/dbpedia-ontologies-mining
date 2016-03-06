package dbpediaanalyzer.statistics;

import dbpediaanalyzer.dbpediaobjects.HierarchyElement;

import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchyStatistics {
    private int elementsNumber;
    private int orphansNumber;
    private int depth;
    private int directSubsumptions;
    private int inferredSubsumptions;

    public HierarchyStatistics(HashMap<String, ? extends HierarchyElement> hierarchy) {
        this.elementsNumber= -1;
        this.orphansNumber = -1;
        this.depth = -1;
        this.directSubsumptions = -1;
        this.inferredSubsumptions = -1;

        computeStatistics(hierarchy);
    }

    private void computeStatistics(HashMap<String, ? extends HierarchyElement> hierarchy) {
        this.elementsNumber = hierarchy.size();

        this.orphansNumber++;
        for(String key : hierarchy.keySet()) {
            if(hierarchy.get(key).getParents().size() == 0) {
                this.orphansNumber++;
            }
        }
    }

    public void display() {
        System.out.println("Elements number: " + this.elementsNumber);
        System.out.println("Orphans number: " + this.orphansNumber);
        System.out.println("Depth: " + this.depth);
        System.out.println("Direct subsumptions number: " + this.directSubsumptions);
        System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
    }

    public int getElementsNumber() {
        return this.elementsNumber;
    }

    public int getOrphansNumber() {
        return this.orphansNumber;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getDirectSubsumptions() {
        return this.directSubsumptions;
    }

    public int getInferredSubsumptions() {
        return this.inferredSubsumptions;
    }
}

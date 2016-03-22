package dbpediaanalyzer.statistic;

import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeStatistics {
    private int depth;
    private int conceptsNumber;
    private int edgesNumber;
    private int conceptsWithoutCategoriesNumber;
    private int conceptsWithoutOntologyClassesNumber;
    private int conceptsWithoutYagoClassesNumber;
    private double averageCategoriesNumberPerConcept;
    private double averageOntologyClassesNumberPerConcept;
    private double averageYagoClassesNumberPerConcept;
    private double averagePageNumberPerConcept;
    private double averageRelationshipsNumberPerConcept;

    public LatticeStatistics(Lattice lattice) {
        this.conceptsNumber = lattice.getConcepts().size();

        for(Concept concept : lattice.getConcepts()) {
            this.edgesNumber += concept.getParents().size();

            this.averageCategoriesNumberPerConcept += concept.getCategories().size();
            this.averageOntologyClassesNumberPerConcept += concept.getOntologyClasses().size();
            this.averageYagoClassesNumberPerConcept += concept.getYagoClasses().size();
            this.averagePageNumberPerConcept += concept.getObjects().size();
            this.averageRelationshipsNumberPerConcept += concept.getAttributes().size();

            if(concept.getCategories().size() == 0) {
                this.conceptsWithoutCategoriesNumber++;
            }

            if(concept.getOntologyClasses().size() == 0) {
                this.conceptsWithoutOntologyClassesNumber++;
            }

            if(concept.getYagoClasses().size() == 0) {
                this.conceptsWithoutYagoClassesNumber++;
            }
        }

        this.averageCategoriesNumberPerConcept /= (double) this.conceptsNumber;
        this.averageOntologyClassesNumberPerConcept /= (double) this.conceptsNumber;
        this.averageYagoClassesNumberPerConcept /= (double) this.conceptsNumber;
        this.averagePageNumberPerConcept /= (double) this.conceptsNumber;
        this.averageRelationshipsNumberPerConcept /= (double) this.conceptsNumber;

        computeLatticeDepth(lattice);
    }

    private void computeLatticeDepth(Lattice lattice) {
        HashMap<Concept, Integer> depths = new HashMap<>();
        for(Concept concept : lattice.getConcepts()) {
            depths.put(concept, -1);
        }

        Queue<Concept> queue = new LinkedList<>();
        queue.add(lattice.getTop());
        depths.put(lattice.getTop(), 1);

        while(!queue.isEmpty()) {
            Concept concept = queue.poll();

            for(Concept child : concept.getChildren()) {
                if(depths.get(child) < depths.get(concept) + 1) {
                    queue.add(child);
                    depths.put(child, depths.get(concept) + 1);
                }
            }
        }

        this.depth = depths.get(lattice.getBottom());
    }

    public int getDepth() {
        return this.depth;
    }

    public int getConceptsNumber() {
        return this.conceptsNumber;
    }

    public int getEdgesNumber() {
        return this.edgesNumber;
    }

    public int getConceptsWithoutCategoriesNumber() {
        return this.conceptsWithoutCategoriesNumber;
    }

    public int getConceptsWithoutOntologyClassesNumber() {
        return this.conceptsWithoutOntologyClassesNumber;
    }

    public int getConceptsWithoutYagoClassesNumber() {
        return this.conceptsWithoutYagoClassesNumber;
    }

    public double getAverageCategoriesNumberPerConcept() {
        return this.averageCategoriesNumberPerConcept;
    }

    public double getAverageOntologyClassesNumberPerConcept() {
        return this.averageOntologyClassesNumberPerConcept;
    }

    public double getAverageYagoClassesNumberPerConcept() {
        return this.averageYagoClassesNumberPerConcept;
    }

    public double getAveragePageNumberPerConcept() {
        return this.averagePageNumberPerConcept;
    }

    public double getAverageRelationshipsNumberPerConcept() {
        return this.averageRelationshipsNumberPerConcept;
    }
}

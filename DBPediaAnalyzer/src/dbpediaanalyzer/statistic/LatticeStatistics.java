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
    private int gapConceptsInCategories;
    private int gapConceptsInOntologyClasses;
    private int gapConceptsInYagoClasses;
    private double averageCategoriesNumberPerConcept;
    private double averageOntologyClassesNumberPerConcept;
    private double averageYagoClassesNumberPerConcept;
    private double averagePageNumberPerConcept;
    private double averageRelationshipsNumberPerConcept;

    public LatticeStatistics(Lattice lattice) {
        this.depth = -1;
        this.edgesNumber = 0;
        this.conceptsWithoutCategoriesNumber = 0;
        this.conceptsWithoutOntologyClassesNumber = 0;
        this.conceptsWithoutYagoClassesNumber = 0;
        this.gapConceptsInCategories = 0;
        this.gapConceptsInOntologyClasses = 0;
        this.gapConceptsInYagoClasses = 0;
        this.averageCategoriesNumberPerConcept = 0.0;
        this.averageOntologyClassesNumberPerConcept = 0.0;
        this.averageYagoClassesNumberPerConcept = 0.0;
        this.averagePageNumberPerConcept = 0.0;
        this.averageRelationshipsNumberPerConcept = 0.0;

        this.conceptsNumber = lattice.getConcepts().size();

        for(Concept concept : lattice.getConcepts()) {
            this.edgesNumber += concept.getParents().size();

            this.averageCategoriesNumberPerConcept += concept.getCategories().size();
            this.averageOntologyClassesNumberPerConcept += concept.getOntologyClasses().size();
            this.averageYagoClassesNumberPerConcept += concept.getYagoClasses().size();
            this.averagePageNumberPerConcept += concept.getObjects().size();
            this.averageRelationshipsNumberPerConcept += concept.getAttributes().size();

            if(concept.getCategories().isEmpty()) {
                this.conceptsWithoutCategoriesNumber++;

                boolean parentsHaveClasses = false;
                for(Concept parent : concept.getParents()) {
                    if(!parent.getCategories().isEmpty()) {
                        parentsHaveClasses = true;
                    }
                }

                boolean childrenHaveClasses = false;
                for(Concept child : concept.getChildren()) {
                    if(!child.getCategories().isEmpty()) {
                        childrenHaveClasses = true;
                    }
                }

                if(parentsHaveClasses && childrenHaveClasses) {
                    this.gapConceptsInCategories++;
                }
            }

            if(concept.getOntologyClasses().isEmpty()) {
                this.conceptsWithoutOntologyClassesNumber++;

                boolean parentsHaveClasses = false;
                for(Concept parent : concept.getParents()) {
                    if(!parent.getOntologyClasses().isEmpty()) {
                        parentsHaveClasses = true;
                    }
                }

                boolean childrenHaveClasses = false;
                for(Concept child : concept.getChildren()) {
                    if(!child.getOntologyClasses().isEmpty()) {
                        childrenHaveClasses = true;
                    }
                }

                if(parentsHaveClasses && childrenHaveClasses) {
                    this.gapConceptsInOntologyClasses++;
                }
            }

            if(concept.getYagoClasses().isEmpty()) {
                this.conceptsWithoutYagoClassesNumber++;

                boolean parentsHaveClasses = false;
                for(Concept parent : concept.getParents()) {
                    if(!parent.getYagoClasses().isEmpty()) {
                        parentsHaveClasses = true;
                    }
                }

                boolean childrenHaveClasses = false;
                for(Concept child : concept.getChildren()) {
                    if(!child.getYagoClasses().isEmpty()) {
                        childrenHaveClasses = true;
                    }
                }

                if(parentsHaveClasses && childrenHaveClasses) {
                    this.gapConceptsInYagoClasses++;
                }
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

    public int getGapConceptsInCategories() {
        return this.gapConceptsInCategories;
    }

    public int getGapConceptsInOntologyClasses() {
        return this.gapConceptsInOntologyClasses;
    }

    public int getGapConceptsInYagoClasses() {
        return this.gapConceptsInYagoClasses;
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

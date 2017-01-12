package io.github.pmonnin.statistics;

import io.github.pmonnin.fca.FormalLattice;
import io.github.pmonnin.kcomparison.Subsumption;

import java.util.List;

/**
 * Compute and store statistics about the mining results and the annotated lattice
 * @author Pierre Monnin
 */
public class MiningStatistics {
    private int commonPredicates;
    private int conceptsNumber;
    private int latticeDepth;
    private int emptyProperAnnotations;
    private double avgClassesPerAnnotation;
    private double avgClassesPerProperAnnotation;
    private int newNumber;
    private int inferredNumber;
    private int directNumber;
    private int reversedNumber;

    public MiningStatistics(FormalLattice lattice, List<Subsumption> results) {
        conceptsNumber = lattice.getConceptsNumber();
        commonPredicates = lattice.getTop().getIntentSize();
        latticeDepth = lattice.getBottom().getDepth();

        emptyProperAnnotations = 0;
        avgClassesPerAnnotation = 0.0;
        avgClassesPerProperAnnotation = 0.0;
        for (int i = 0 ; i < lattice.getConceptsNumber() ; i++) {
            if (lattice.getConcept(i).getAnnotation("proper-annotation").isEmpty())
                emptyProperAnnotations++;

            else {
                avgClassesPerProperAnnotation += lattice.getConcept(i).getAnnotation("proper-annotation").size();
            }

            avgClassesPerAnnotation += lattice.getConcept(i).getAnnotation("annotation").size();
        }

        avgClassesPerAnnotation /= (double) conceptsNumber;
        avgClassesPerProperAnnotation /= (double) conceptsNumber;

        newNumber = 0;
        inferredNumber = 0;
        directNumber = 0;
        reversedNumber = 0;

        for (Subsumption s : results) {
            switch (s.getType()) {
                case NEW:
                    newNumber++;
                    break;

                case DIRECT:
                    directNumber++;
                    break;

                case INFERRED:
                    inferredNumber++;
                    break;

                case REVERSED:
                    reversedNumber++;
                    break;
            }
        }
    }

    public int getCommonPredicates() {
        return commonPredicates;
    }

    public int getConceptsNumber() {
        return conceptsNumber;
    }

    public int getLatticeDepth() {
        return latticeDepth;
    }

    public int getEmptyProperAnnotations() {
        return emptyProperAnnotations;
    }

    public double getAvgClassesPerAnnotation() {
        return avgClassesPerAnnotation;
    }

    public double getAvgClassesPerProperAnnotation() {
        return avgClassesPerProperAnnotation;
    }

    public int getNewNumber() {
        return newNumber;
    }

    public int getInferredNumber() {
        return inferredNumber;
    }

    public int getDirectNumber() {
        return directNumber;
    }

    public int getReversedNumber() {
        return reversedNumber;
    }
}

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
    private int newNumber;
    private int inferredNumber;
    private int directNumber;
    private int reversedNumber;

    public MiningStatistics(FormalLattice lattice, List<Subsumption> results) {
        conceptsNumber = lattice.getConceptsNumber();
        commonPredicates = lattice.getTop().getIntentSize();

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
}

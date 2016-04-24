package dbpediaanalyzer.statistic;

import dbpediaanalyzer.comparison.*;

import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class ComparisonResultsStatistics {
    private static final double UNDEFINED_VALUE = -1.0;

    private int resultsNumber;

    private int confirmedDirectNumber;
    private int proposedInferredToDirectNumber;
    private int proposedNewNumber;

    public ComparisonResultsStatistics(List<ComparisonResult> comparisonResults) {
        this.resultsNumber = comparisonResults.size();

        this.confirmedDirectNumber = 0;
        this.proposedInferredToDirectNumber = 0;
        this.proposedNewNumber = 0;

        for(ComparisonResult cr : comparisonResults) {
            if(cr.getType() == ComparisonResultType.CONFIRMED_DIRECT) {
                this.confirmedDirectNumber++;
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT) {
                this.proposedInferredToDirectNumber++;
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_NEW) {
                this.proposedNewNumber++;
            }
        }

    }

    public int getResultsNumber() {
        return this.resultsNumber;
    }

    public int getConfirmedDirectNumber() {
        return this.confirmedDirectNumber;
    }

    public int getProposedInferredToDirectNumber() {
        return this.proposedInferredToDirectNumber;
    }

    public int getProposedNewNumber() {
        return this.proposedNewNumber;
    }

}

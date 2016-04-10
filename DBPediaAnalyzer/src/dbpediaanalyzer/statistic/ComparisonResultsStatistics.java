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
    private int resultsNumber;
    private int confirmedDirectNumber;
    private double confirmedDirectAverageValue;
    private int proposedInferredToDirectNumber;
    private double proposedInferredToDirectAverageValue;
    private int proposedNewNumber;
    private double proposedNewAverageValue;

    public ComparisonResultsStatistics(List<ComparisonResult> comparisonResults) {
        this.resultsNumber = comparisonResults.size();

        this.confirmedDirectNumber = 0;
        this.proposedInferredToDirectNumber = 0;
        this.proposedNewNumber = 0;

        this.confirmedDirectAverageValue = 0.0;
        this.proposedInferredToDirectAverageValue = 0.0;
        this.proposedNewAverageValue = 0.0;

        for(ComparisonResult cr : comparisonResults) {
            if(cr.getType() == ComparisonResultType.CONFIRMED_DIRECT) {
                this.confirmedDirectNumber++;
                this.confirmedDirectAverageValue += cr.getValue();
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT) {
                this.proposedInferredToDirectNumber++;
                this.proposedInferredToDirectAverageValue += cr.getValue();
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_NEW) {
                this.proposedNewNumber++;
                this.proposedNewAverageValue += cr.getValue();
            }
        }

        this.confirmedDirectAverageValue /= (double) this.confirmedDirectNumber;
        this.proposedInferredToDirectAverageValue /= (double) this.proposedInferredToDirectNumber;
        this.proposedNewAverageValue /= (double) this.proposedNewNumber;

    }

    public int getResultsNumber() {
        return this.resultsNumber;
    }

    public int getConfirmedDirectNumber() {
        return this.confirmedDirectNumber;
    }

    public double getConfirmedDirectAverageValue() {
        return this.confirmedDirectAverageValue;
    }

    public int getProposedInferredToDirectNumber() {
        return this.proposedInferredToDirectNumber;
    }

    public double getProposedInferredToDirectAverageValue() {
        return this.proposedInferredToDirectAverageValue;
    }

    public int getProposedNewNumber() {
        return this.proposedNewNumber;
    }

    public double getProposedNewAverageValue() {
        return this.proposedNewAverageValue;
    }
}

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
    private int confirmedDirectRelationshipNumber;
    private int proposedInferredToDirectRelationshipNumber;
    private int proposedNewRelationshipNumber;

    public ComparisonResultsStatistics(List<ComparisonResult> comparisonResults) {
        this.resultsNumber = comparisonResults.size();

        this.confirmedDirectRelationshipNumber = 0;
        this.proposedInferredToDirectRelationshipNumber = 0;
        this.proposedNewRelationshipNumber = 0;
        for(ComparisonResult cr : comparisonResults) {
            if(cr.getType() == ComparisonResultType.CONFIRMED_DIRECT) {
                this.confirmedDirectRelationshipNumber++;
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT) {
                this.proposedInferredToDirectRelationshipNumber++;
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_NEW) {
                this.proposedNewRelationshipNumber++;
            }
        }

    }

    public int getResultsNumber() {
        return this.resultsNumber;
    }

    public int getConfirmedDirectRelationshipNumber() {
        return this.confirmedDirectRelationshipNumber;
    }

    public int getProposedInferredToDirectRelationshipNumber() {
        return this.proposedInferredToDirectRelationshipNumber;
    }

    public int getProposedNewRelationshipNumber() {
        return this.proposedNewRelationshipNumber;
    }
}

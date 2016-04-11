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
    private double confirmedDirectAverageValue;
    private double confirmedDirectMinimumValue;
    private double confirmedDirectMaximumValue;

    private int proposedInferredToDirectNumber;
    private double proposedInferredToDirectAverageValue;
    private double proposedInferredToDirectMinimumValue;
    private double proposedInferredToDirectMaximumValue;

    private int proposedNewNumber;
    private double proposedNewAverageValue;
    private double proposedNewMinimumValue;
    private double proposedNewMaximumValue;

    public ComparisonResultsStatistics(List<ComparisonResult> comparisonResults) {
        this.resultsNumber = comparisonResults.size();

        this.confirmedDirectNumber = 0;
        this.confirmedDirectAverageValue = 0.0;
        this.confirmedDirectMinimumValue = UNDEFINED_VALUE;
        this.confirmedDirectMaximumValue = UNDEFINED_VALUE;

        this.proposedInferredToDirectNumber = 0;
        this.proposedInferredToDirectAverageValue = 0.0;
        this.proposedInferredToDirectMinimumValue = UNDEFINED_VALUE;
        this.proposedInferredToDirectMaximumValue = UNDEFINED_VALUE;

        this.proposedNewNumber = 0;
        this.proposedNewAverageValue = 0.0;
        this.proposedNewMinimumValue = UNDEFINED_VALUE;
        this.proposedNewMaximumValue = UNDEFINED_VALUE;

        for(ComparisonResult cr : comparisonResults) {
            if(cr.getType() == ComparisonResultType.CONFIRMED_DIRECT) {
                this.confirmedDirectNumber++;
                this.confirmedDirectAverageValue += cr.getValue();

                if(this.confirmedDirectNumber == 1) {
                    this.confirmedDirectMinimumValue = cr.getValue();
                    this.confirmedDirectMaximumValue = cr.getValue();
                }

                else {
                    if(cr.getValue() < this.confirmedDirectMinimumValue) {
                        this.confirmedDirectMinimumValue = cr.getValue();
                    }

                    if(cr.getValue() > this.confirmedDirectMaximumValue) {
                        this.confirmedDirectMaximumValue = cr.getValue();
                    }
                }
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT) {
                this.proposedInferredToDirectNumber++;
                this.proposedInferredToDirectAverageValue += cr.getValue();

                if(this.proposedInferredToDirectNumber == 1) {
                    this.proposedInferredToDirectMinimumValue = cr.getValue();
                    this.proposedInferredToDirectMaximumValue = cr.getValue();
                }

                else {
                    if(cr.getValue() < this.proposedInferredToDirectMinimumValue) {
                        this.proposedInferredToDirectMinimumValue = cr.getValue();
                    }

                    if(cr.getValue()> this.proposedInferredToDirectMaximumValue) {
                        this.proposedInferredToDirectMaximumValue = cr.getValue();
                    }
                }
            }

            else if(cr.getType() == ComparisonResultType.PROPOSED_NEW) {
                this.proposedNewNumber++;
                this.proposedNewAverageValue += cr.getValue();

                if(this.proposedNewNumber == 1) {
                    this.proposedNewMinimumValue = cr.getValue();
                    this.proposedNewMaximumValue = cr.getValue();
                }

                else {
                    if(cr.getValue() < this.proposedNewMinimumValue) {
                        this.proposedNewMinimumValue = cr.getValue();
                    }

                    if(cr.getValue() > this.proposedNewMaximumValue) {
                        this.proposedNewMaximumValue = cr.getValue();
                    }
                }
            }
        }

        if(this.confirmedDirectNumber != 0) {
            this.confirmedDirectAverageValue /= (double) this.confirmedDirectNumber;
        }

        else {
            this.confirmedDirectAverageValue = UNDEFINED_VALUE;
        }

        if(this.proposedInferredToDirectNumber != 0) {
            this.proposedInferredToDirectAverageValue /= (double) this.proposedInferredToDirectNumber;
        }

        else {
            this.proposedInferredToDirectAverageValue = UNDEFINED_VALUE;
        }

        if(this.proposedNewNumber != 0) {
            this.proposedNewAverageValue /= (double) this.proposedNewNumber;
        }

        else {
            this.proposedNewAverageValue = UNDEFINED_VALUE;
        }

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

    public double getConfirmedDirectMinimumValue() {
        return this.confirmedDirectMinimumValue;
    }

    public double getConfirmedDirectMaximumValue() {
        return this.confirmedDirectMaximumValue;
    }

    public int getProposedInferredToDirectNumber() {
        return this.proposedInferredToDirectNumber;
    }

    public double getProposedInferredToDirectAverageValue() {
        return this.proposedInferredToDirectAverageValue;
    }

    public double getProposedInferredToDirectMinimumValue() {
        return this.proposedInferredToDirectMinimumValue;
    }

    public double getProposedInferredToDirectMaximumValue() {
        return this.proposedInferredToDirectMaximumValue;
    }

    public int getProposedNewNumber() {
        return this.proposedNewNumber;
    }

    public double getProposedNewAverageValue() {
        return this.proposedNewAverageValue;
    }

    public double getProposedNewMinimumValue() {
        return this.proposedNewMinimumValue;
    }

    public double getProposedNewMaximumValue() {
        return this.proposedNewMaximumValue;
    }
}

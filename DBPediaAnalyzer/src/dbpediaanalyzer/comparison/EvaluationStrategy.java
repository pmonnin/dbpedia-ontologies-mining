package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public abstract class EvaluationStrategy {
    public static final double INVALID_RESULT_VALUE = -1.0;

    public abstract String getName();

    public double evaluateSubsumption(DataBasedSubsumption subsumption) {
        if(subsumption.getTop().hasAncestor(subsumption.getBottom())) {
            return INVALID_RESULT_VALUE;
        }

        return computeValue(subsumption);
    }

    protected abstract double computeValue(DataBasedSubsumption subsumption);

}

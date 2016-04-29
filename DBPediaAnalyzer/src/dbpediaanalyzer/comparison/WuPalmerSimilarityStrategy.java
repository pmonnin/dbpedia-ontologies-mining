package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class WuPalmerSimilarityStrategy extends EvaluationStrategy {

    @Override
    public String getName() {
        return "WuPalmerSimilarity";
    }

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        return 0.0;
    }

}

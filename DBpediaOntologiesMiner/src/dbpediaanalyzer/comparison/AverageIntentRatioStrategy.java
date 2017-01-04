package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

import java.util.List;

/**
 * Evaluates an axiom suggested by the annotated lattice computing the average intent ratio
 *
 * @author Pierre Monnin
 *
 */
public class AverageIntentRatioStrategy extends EvaluationStrategy {
    @Override
    public String getName() {
        return "AverageIntensionsRatio";
    }

    @Override
    protected double computeValue(DataBasedSubsumption subsumption) {
        double retVal = 0.0;

        List<Double> intensionsRatios = subsumption.getIntensionsRatios();

        for(double d : intensionsRatios) {
            retVal += d;
        }

        return retVal / (double) intensionsRatios.size();
    }
}

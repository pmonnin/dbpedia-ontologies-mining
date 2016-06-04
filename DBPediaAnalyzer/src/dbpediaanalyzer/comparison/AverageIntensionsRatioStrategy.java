package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class AverageIntensionsRatioStrategy extends EvaluationStrategy {
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

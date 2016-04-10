package dbpediaanalyzer.comparison;

import dbpediaanalyzer.extraction.DataBasedSubsumption;

import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class AverageExtensionsRatioStrategy extends EvaluationStrategy {

    @Override
    public double computeValue(DataBasedSubsumption subsumption) {
        double retVal = 0.0;
        List<Double> extensionsRatios = subsumption.getExtensionsRatios();

        for(double d : extensionsRatios) {
            retVal += d;
        }

        return retVal / (double) extensionsRatios.size();
    }

}

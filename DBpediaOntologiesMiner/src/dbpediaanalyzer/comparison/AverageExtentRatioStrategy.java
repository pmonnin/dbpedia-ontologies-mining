package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

import java.util.List;

/**
 * Evaluates an axiom suggested by the annotated lattice computing the average extent ratio
 *
 * @author Pierre Monnin
 *
 */
public class AverageExtentRatioStrategy extends EvaluationStrategy {

    @Override
    public String getName() {
        return "AverageExtensionsRatio";
    }

    @Override
    protected double computeValue(DataBasedSubsumption subsumption) {
        double retVal = 0.0;
        List<Double> extensionsRatios = subsumption.getExtensionsRatios();

        for(double d : extensionsRatios) {
            retVal += d;
        }

        return retVal / (double) extensionsRatios.size();
    }

}

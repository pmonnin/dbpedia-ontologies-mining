package dbpediaanalyzer.factory;

import dbpediaanalyzer.comparison.AverageExtensionsRatioStrategy;
import dbpediaanalyzer.comparison.DistanceViaLCAStrategy;
import dbpediaanalyzer.comparison.EvaluationStrategy;
import dbpediaanalyzer.comparison.NumberOfSubmissionsStrategy;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class EvaluationStrategyFactory {

    public static EvaluationStrategy createEvaluationStrategy(String strategyName) {
        if("NumberOfSubmissions".equals(strategyName)) {
            return new NumberOfSubmissionsStrategy();
        }

        else if("AverageExtensionsRatio".equals(strategyName)) {
            return new AverageExtensionsRatioStrategy();
        }

        else if("DistanceFromLCA".equals(strategyName)) {
            return new DistanceViaLCAStrategy();
        }

        return null;
    }

}

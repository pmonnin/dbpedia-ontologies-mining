package dbpediaanalyzer.factory;

import dbpediaanalyzer.comparison.AverageExtensionsRatioStrategy;
import dbpediaanalyzer.comparison.DistanceViaLCAStrategy;
import dbpediaanalyzer.comparison.EvaluationStrategy;
import dbpediaanalyzer.comparison.NumberOfSubmissionsStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class EvaluationStrategiesFactory {

    public static List<EvaluationStrategy> getEvaluationStrategies() {
        List<EvaluationStrategy> strategies = new ArrayList<>();

        strategies.add(new NumberOfSubmissionsStrategy());
        strategies.add(new AverageExtensionsRatioStrategy());
        strategies.add(new DistanceViaLCAStrategy());

        return strategies;
    }

}

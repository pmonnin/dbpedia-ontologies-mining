package dbpediaanalyzer.factory;

import dbpediaanalyzer.comparison.*;

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
        strategies.add(new WuPalmerSimilarityStrategy());

        return strategies;
    }

}

package dbpediaanalyzer.factory;

import dbpediaanalyzer.comparison.ComparisonResult;
import dbpediaanalyzer.comparison.ComparisonResultType;
import dbpediaanalyzer.comparison.EvaluationStrategy;
import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class KnowledgesComparisonResultFactory {

    public static List<ComparisonResult> createKnowledgesComparisonResults(Collection<DataBasedSubsumption> dataBasedKnowledge) {

        List<ComparisonResult> results = new ArrayList<>();

        for(DataBasedSubsumption dbs : dataBasedKnowledge) {

            // Is this an already existing direct relationship?
            if(dbs.getBottom().getParents().contains(dbs.getTop())) {
                results.add(createComparisonResult(ComparisonResultType.CONFIRMED_DIRECT, dbs));
            }

            // Is this an already existing inferred relationship?
            else if(dbs.getBottom().hasAncestor(dbs.getTop())) {
                results.add(createComparisonResult(ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT, dbs));
            }

            // Is this a new relationship?
            else {
                results.add(createComparisonResult(ComparisonResultType.PROPOSED_NEW, dbs));
            }

        }

        return results;

    }

    private static ComparisonResult createComparisonResult(ComparisonResultType type, DataBasedSubsumption dbs) {
        List<EvaluationStrategy> strategies = EvaluationStrategiesFactory.getEvaluationStrategies();

        ComparisonResult cr = new ComparisonResult(type, dbs.getBottom(), dbs.getTop());

        for(EvaluationStrategy strategy : strategies) {
            cr.addValue(strategy.getName(), strategy.evaluateSubsumption(dbs));
        }

        return cr;
    }
}

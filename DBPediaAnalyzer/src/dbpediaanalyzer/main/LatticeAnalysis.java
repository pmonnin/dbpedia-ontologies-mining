package dbpediaanalyzer.main;

import dbpediaanalyzer.comparison.ComparisonResult;
import dbpediaanalyzer.comparison.EvaluationStrategy;
import dbpediaanalyzer.factory.KnowledgesComparisonResultFactory;
import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.DataBasedKnowledgeFactory;
import dbpediaanalyzer.factory.EvaluationStrategyFactory;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.ComparisonResultsStatisticsWriter;
import dbpediaanalyzer.io.ComparisonResultsWriter;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.ComparisonResultsStatistics;
import dbpediaanalyzer.util.TimeMeasurer;

import java.util.Collection;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeAnalysis {

    /**
     * TODO JAVADOC
     * @param args
     */
    public static void main(String[] args) {
        boolean argumentsCorrect = true;
        EvaluationStrategy strategyConfirmedDirect = null, strategyProposedInferredToDirect = null, strategyProposedNew = null;

        if(args.length != 6) {
            argumentsCorrect = false;
        }

        else {
            strategyConfirmedDirect = EvaluationStrategyFactory.createEvaluationStrategy(args[1]);
            strategyProposedInferredToDirect = EvaluationStrategyFactory.createEvaluationStrategy(args[2]);
            strategyProposedNew = EvaluationStrategyFactory.createEvaluationStrategy(args[3]);

            if(strategyConfirmedDirect == null || strategyProposedInferredToDirect == null || strategyProposedNew == null) {
                argumentsCorrect = false;
            }
        }

        if(!argumentsCorrect) {
            System.out.println("Usage:\n java LatticeAnalysis lattice evaluation-strategy^{3} output comparison-statistics");
            System.out.println("\t lattice\n\t\t JSON file corresponding to the lattice to analyze");
            System.out.println("\t evaluation-strategy^{3}\n\t\t Strategies to be used when evaluating a relationship proposal");
            System.out.println("\t\t Meaning:");
            System.out.println("\t\t - First\n\t\t\t evaluation of confirmed direct relationships");
            System.out.println("\t\t - Second\n\t\t\t evaluation of relationships proposed to be changed from inferred to direct");
            System.out.println("\t\t - Third\n\t\t\t evaluation of new relationships proposed");
            System.out.println("\t\t Possible values:");
            System.out.println("\t\t NumberOfSubmissions\n\t\t\t value will be set to number of times the proposal has been submitted");
            System.out.println("\t\t AverageExtensionsRatio\n\t\t\t value will be set to the average extensions ratio of each pair of concepts proposing the relationship");
            System.out.println("\t\t DistanceFromLCA\n\t\t\t value will be set to 1 / (distance from Lowest Common Ancestor)");
            System.out.println("\t output\n\t\t file where comparison results will be written (CSV format)");
            System.out.println("\t comparison-statistics\n\t\t file where statistics of comparison results will be written");
        }

        else {
            System.out.println("=== LATTICE ANALYSIS ===");
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("Data loading...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = HierarchiesFactory.createHierarchies();
            System.out.println("\t Loading lattice from file...");
            Lattice lattice = (new LatticeReader()).readLattice(args[0], hm);

            System.out.println("Lattice analysis...");
            System.out.println("\t Extracting knowledge from lattice");
            Collection<DataBasedSubsumption> dataBasedKnowledge = DataBasedKnowledgeFactory.createDataBasedKnowledge(lattice);
            System.out.println("\t Computing comparison results...");
            List<ComparisonResult> comparisonResults = KnowledgesComparisonResultFactory.createKnowledgesComparisonResults(dataBasedKnowledge,
                    strategyConfirmedDirect, strategyProposedInferredToDirect, strategyProposedNew);
            System.out.println("\t Computing comparison statistics...");
            ComparisonResultsStatistics statistics = new ComparisonResultsStatistics(comparisonResults);

            System.out.println("Saving results...");
            System.out.println("\t Saving comparison results...");
            ComparisonResultsWriter resultsWriter = new ComparisonResultsWriter(args[1]);
            resultsWriter.writeColumnsHeader();
            resultsWriter.writeComparisonResults(comparisonResults);
            resultsWriter.close();
            System.out.println("\t Saving comparison results statistics...");
            ComparisonResultsStatisticsWriter statisticsWriter = new ComparisonResultsStatisticsWriter(args[2]);
            statisticsWriter.writeComparisonResultsStatistics(statistics);
            statisticsWriter.close();

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of lattice analysis program ===");
        }
    }
}

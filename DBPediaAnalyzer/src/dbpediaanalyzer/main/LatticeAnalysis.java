package dbpediaanalyzer.main;

import dbpediaanalyzer.comparison.ComparisonResult;
import dbpediaanalyzer.comparison.ComparisonResultType;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.factory.KnowledgesComparisonResultFactory;
import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.DataBasedKnowledgeFactory;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.ComparisonResultsStatisticsWriter;
import dbpediaanalyzer.io.ComparisonResultsWriter;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.ComparisonResultsStatistics;
import dbpediaanalyzer.util.TimeMeasurer;

import java.util.ArrayList;
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
        if(args.length != 3) {
            System.out.println("Usage:\n java LatticeAnalysis lattice output comparison-statistics");
            System.out.println("\t lattice\n\t\t JSON file corresponding to the lattice to analyze");
            System.out.println("\t output\n\t\t file where comparison results will be written (JSON format)");
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
            List<DataBasedSubsumption> dataBasedKnowledge = DataBasedKnowledgeFactory.createDataBasedKnowledge(lattice);
            lattice = null;
            System.out.println("\t Computing comparison results...");
            List<ComparisonResult> comparisonResults = KnowledgesComparisonResultFactory.createKnowledgesComparisonResults(dataBasedKnowledge);
            dataBasedKnowledge = null;
            System.out.println("\t Computing comparison statistics...");
            List<ComparisonResultsStatistics> statistics = new ArrayList<>();
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.CONFIRMED_DIRECT,
                    Category.DBPEDIA_CATEGORY_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT,
                    Category.DBPEDIA_CATEGORY_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_NEW,
                    Category.DBPEDIA_CATEGORY_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.CONFIRMED_DIRECT,
                    OntologyClass.DBPEDIA_ONTOLOGY_CLASS_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT,
                    OntologyClass.DBPEDIA_ONTOLOGY_CLASS_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_NEW,
                    OntologyClass.DBPEDIA_ONTOLOGY_CLASS_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.CONFIRMED_DIRECT,
                    YagoClass.DBPEDIA_YAGO_CLASS_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_INFERRED_TO_DIRECT,
                    YagoClass.DBPEDIA_YAGO_CLASS_URI_PREFIX));
            statistics.add(new ComparisonResultsStatistics(comparisonResults, ComparisonResultType.PROPOSED_NEW,
                    YagoClass.DBPEDIA_YAGO_CLASS_URI_PREFIX));

            System.out.println("Saving results...");
            System.out.println("\t Saving comparison results...");
            (new ComparisonResultsWriter()).writeComparisonResults(comparisonResults, args[1]);
            System.out.println("\t Saving comparison results statistics...");
            ComparisonResultsStatisticsWriter statisticsWriter = new ComparisonResultsStatisticsWriter(args[2]);
            for(ComparisonResultsStatistics stat : statistics) {
                statisticsWriter.writeComparisonResultsStatistics(stat);
            }
            statisticsWriter.close();

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of lattice analysis program ===");
        }
    }
}

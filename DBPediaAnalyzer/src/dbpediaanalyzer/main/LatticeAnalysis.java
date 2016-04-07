package dbpediaanalyzer.main;

import dbpediaanalyzer.comparison.ComparisonResult;
import dbpediaanalyzer.comparison.KnowledgesComparator;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.extraction.DataBasedKnowledgeManager;
import dbpediaanalyzer.extraction.LatticeKnowledgeExtractor;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.ComparisonResultsStatistics;
import dbpediaanalyzer.util.TimeMeasurer;

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
     * @param args should contain
     *             - name of JSON file corresponding to the lattice to analyze
     *             - name of output file where comparison results will be written
     *             - name of output file where statistics of comparison results will be written
     */
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Usage:\n java LatticeAnalysis lattice output");
            System.out.println("\t lattice\n\t\t JSON file corresponding to the lattice to analyze");
            System.out.println("\t output\n\t\t file where comparison results will be written (CSV format)");
            System.out.println("\t comparison-statistics\n\t\t file where statistics of comparison results will be written");
        }

        else {
            System.out.println("=== LATTICE ANALYSIS ===");
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("Data loading...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();
            System.out.println("\t Loading lattice from file...");
            Lattice lattice = (new LatticeReader()).readLattice(args[0], hm);

            System.out.println("Lattice analysis...");
            System.out.println("\t Extracting knowledge from lattice");
            DataBasedKnowledgeManager dbkm = (new LatticeKnowledgeExtractor()).analyze(lattice);
            System.out.println("\t Computing comparison results...");
            List<ComparisonResult> comparisonResults = (new KnowledgesComparator()).compareKnowledges(dbkm);
            System.out.println("\t Computing comparison statistics...");
            ComparisonResultsStatistics statistics = new ComparisonResultsStatistics(comparisonResults);

            System.out.println("Saving results...");
            System.out.println("\t Saving comparison results...");
            System.out.println("\t Saving comparison results statistics...");

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of lattice analysis program ===");
        }
    }
}

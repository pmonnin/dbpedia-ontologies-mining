package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.io.LatticeStatisticsWriter;
import dbpediaanalyzer.io.LatticeWriter;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.factory.DataSetFactory;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.factory.LatticeFactory;
import dbpediaanalyzer.io.DataSetStatisticsWriter;
import dbpediaanalyzer.statistic.DataSetStatistics;
import dbpediaanalyzer.statistic.LatticeStatistics;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Main class crawling objects from Virtuoso server (DBPedia information), computing
 * the lattice and storing it in a file using JSON format. Statistics are calculated
 * on data set and lattice and stored in files.
 *
 * @author Pierre Monnin
 *
 */
public class LatticeGeneration {

    /**
     * Crawls objects from Virtuoso server (DBPedia information), computes the lattice and stores it in a file using
     * JSON format. Statistics are calculated on data set and lattice and displayed in the standard output
     * @param args Should contain
     *             - YYYY-MM-DD minimal death date for data set creation
     *             - YYYY-MM-DD maximal death date for data set creation
     *             - Name of JSON output file for lattice
     *             - Name of output file for data set
     *             - Name of output file for lattice statistics
     */
    public static void main(String[] args) {
        boolean incorrectParams;

        if(args.length != 5) {
            incorrectParams = true;
        }

        else {
            // TODO Improve this pattern
            Pattern pattern = Pattern.compile("[0-9][0-9][0-9][0-9]-(1[0-2]|0[0-9])-(3[0-1]|[0-2][0-9])");
            incorrectParams = !pattern.matcher(args[0]).find() && !pattern.matcher(args[1]).find();
        }

        if(incorrectParams) {
            System.out.println("Usage:\n  java LatticeGeneration minimalDeathDate maximalDeathDate output statistics");
            System.out.println("\t minimalDeathDate\n\t\t minimal death date for data set creation (YYYY-MM-DD)");
            System.out.println("\t maximalDeathDate\n\t\t maximal death date for data set creation (YYYY-MM-DD)");
            System.out.println("\t output\n\t\t JSON file used to store generated lattice");
            System.out.println("\t statistics-dataset\n\t\t file used to store data set statistics");
            System.out.println("\t statistics-lattice\n\t\t file used to store lattice statistics");
        }

        else {
            System.out.println("=== LATTICE GENERATION ===");

            System.out.println("Data set creation...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();

            System.out.println("\t Querying and parsing data set pages...");
            HashMap<String, Page> dataSet = (new DataSetFactory()).createDataSet(args[0], args[1], hm);

            System.out.println("\t Computing data set statistics...");
            DataSetStatistics dataSetStatistics = new DataSetStatistics(dataSet, hm);

            System.out.println("\t Saving data set statistics...");
            DataSetStatisticsWriter dataSetStatisticsWriter = new DataSetStatisticsWriter(args[3]);
            dataSetStatisticsWriter.writeDataSetStatistics(dataSetStatistics);
            dataSetStatisticsWriter.close();

            System.out.println("Lattice creation...");
            System.out.println("\t Computing lattice from data set...");
            Lattice lattice = (new LatticeFactory()).createLatticeFromDataSet(dataSet, hm);

            System.out.println("\t Computing lattice statistics...");
            LatticeStatistics latticeStatistics = new LatticeStatistics(lattice);

            System.out.println("\t Saving lattice statistics...");
            LatticeStatisticsWriter latticeStatisticsWriter = new LatticeStatisticsWriter(args[4]);
            latticeStatisticsWriter.writeLatticeStatistics(latticeStatistics);
            latticeStatisticsWriter.close();

            System.out.println("\t Saving lattice...");
            (new LatticeWriter()).writeLattice(lattice, latticeStatistics, args[2]);
        }
    }
}

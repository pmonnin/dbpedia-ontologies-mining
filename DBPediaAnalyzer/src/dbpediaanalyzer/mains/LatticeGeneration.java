package dbpediaanalyzer.mains;

import dbpediaanalyzer.factories.DataSetFactory;

import java.util.regex.Pattern;

/**
 * Main class crawling objects from Virtuoso server (DBPedia information), computing
 * the lattice and storing it in a file using JSON format. Statistics are calculated
 * on data set and lattice and displayed in the standard output.
 *
 * @author Pierre Monnin
 */
public class LatticeGeneration {

    /**
     * Crawls objects from Virtuoso server (DBPedia information), computes the lattice and stores it in a file using
     * JSON format. Statistics are calculated on data set and lattice and displayed in the standard output
     * @param args Should contain
     *             - YYYY-MM-DD minimal death date for data set creation
     *             - YYYY-MM-DD maximal death date for data set creation
     *             - Name of JSON output file for lattice
     *             - Name of output file for data set and lattice statistics
     */
    public static void main(String[] args) {
        System.out.println("=== LATTICE GENERATION ===");
        boolean incorrectParams;

        if(args.length != 4) {
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
            System.out.println("\t statistics\n\t\t file used to store data set and lattice statistics");
        }

        else {
            (new DataSetFactory()).createDataSet(args[0], args[1]);
        }
    }
}

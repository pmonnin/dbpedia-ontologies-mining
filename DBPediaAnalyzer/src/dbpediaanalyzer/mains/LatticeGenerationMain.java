package dbpediaanalyzer.mains;

/**
 * Main class crawling objects from Virtuoso server (DBPedia information), computing
 * the lattice and storing it in a file using JSON format. Statistics are calculated
 * on data set and lattice and displayed in the standard output.
 *
 * @author Pierre Monnin
 */
public class LatticeGenerationMain {

    /**
     * Crawls objects from Virtuoso server (DBPedia information), computes the lattice and stores it in a file using
     * JSON format. Statistics are calculated on data set and lattice and displayed in the standard output
     * @param args Should contain
     *             - Name of JSON output file for lattice
     */
    public static void main(String[] args) {
        System.out.println("=== LATTICE GENERATION ===");

        if(args.length != 1) {
            System.out.println("Usage: java LatticeGenerationMain output");
            System.out.println("\t output: JSON file used to store generated lattice");
        }

        else {
            System.out.println("Querying data set from server...");
        }
    }
}

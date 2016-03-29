package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.lattice.Lattice;

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
     *             - name of output file where results will be written
     */
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage:\n java LatticeAnalysis lattice output");
            System.out.println("\t lattice\n\t\t JSON file corresponding to the lattice to analyze");
            System.out.println("\t output\n\t\t file where comparison results will be written");
        }

        else {
            System.out.println("=== LATTICE ANALYSIS ===");

            System.out.println("Data loading...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();
            System.out.println("\t Loading lattice from file...");
            Lattice lattice = (new LatticeReader()).readLattice(args[0], hm);

            System.out.println("Lattice analysis...");
            System.out.println("\t Computing comparison results...");
            System.out.println("\t Writing results inside output file...");
        }
    }
}

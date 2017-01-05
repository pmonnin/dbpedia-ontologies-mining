package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.LatticeReader;
import dbpediaanalyzer.io.LatticeStatisticsWriter;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.LatticeStatistics;
import io.github.pmonnin.util.TimeMeasurer;

/**
 * Compute statistics over a generated lattice
 *
 * @author Pierre Monnin
 *
 */
public class LatticeStatisticsComputation {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage:\n java LatticeStatisticsComputation lattice output");
            System.out.println("\t lattice\n\t\t the lattice on which statistics will be computed");
            System.out.println("\t output\n\t\t file used to store computed lattice statistics");
        }

        else {
            System.out.println("=== LATTICE STATISTICS ===");
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("Data loading...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = HierarchiesFactory.createHierarchies();
            System.out.println("\t Loading lattice from file...");
            Lattice lattice = (new LatticeReader()).readLattice(args[0], hm);

            System.out.println("Lattice analysis...");
            System.out.println("\t Computing lattice statistics...");
            LatticeStatistics statistics = new LatticeStatistics(lattice);
            System.out.println("\t Saving lattice statistics...");
            LatticeStatisticsWriter statisticsWriter = new LatticeStatisticsWriter(args[1]);
            statisticsWriter.writeLatticeStatistics(statistics);
            statisticsWriter.close();

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of lattice statistics computation program ===");
        }
    }

}

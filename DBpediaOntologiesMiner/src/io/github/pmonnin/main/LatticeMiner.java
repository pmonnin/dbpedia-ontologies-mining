package io.github.pmonnin.main;

import io.github.pmonnin.fca.FormalLattice;
import io.github.pmonnin.io.FormalLatticeReader;
import io.github.pmonnin.io.MiningResultsWriter;
import io.github.pmonnin.io.MiningStatisticsWriter;
import io.github.pmonnin.kcomparison.LatticeAnnotator;
import io.github.pmonnin.kcomparison.LatticeOntologyMiner;
import io.github.pmonnin.kcomparison.Subsumption;
import io.github.pmonnin.settings.Settings;
import io.github.pmonnin.statistics.MiningStatistics;
import io.github.pmonnin.util.TimeMeasurer;

import java.util.List;

/**
 * Main class to mine subsumptions from a lattice and compare them with an existing ontology
 * @author Pierre Monnin
 */
public class LatticeMiner {

    /**
     * Main function to mine subsumptions from a lattice and compare them with an existing ontology
     * @param args Should contain:
     *             - the file path to the lattice (SOFIA output)
     *             - the ontology configuration to use for the lattice mining
     *             - the file path for the results output
     *             - the file path for the statistics output
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java io.github.pmonnin.main.LatticeMiner lattice configuration output statistics");
            System.out.println("\tlattice\tFile of the lattice produced by SOFIA");
            System.out.println("\tconfiguration\tThe configuration for the ontology to use for the lattice mining");
            System.out.println("\toutput\tFile where the results will be written");
            System.out.println("\tstatistics\tFile where the statistics of the results will be written");
        }

        else if (!Settings.ontologySettings.containsKey(args[1])) {
            System.out.println("[ERROR] Ontology " + args[0] + "unknown.");
            System.out.println("[ERROR] Possible values are: " + Settings.ontologySettings.keySet());
        }

        else {
            System.out.println("[INFO] LatticeMiner begin on " + args[0]);
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("[INFO] Reading lattice from file");
            FormalLattice lattice = (new FormalLatticeReader()).readLattice(args[0]);

            if (lattice != null) {
                System.out.println("[INFO] Annotating lattice");
                (new LatticeAnnotator()).annotateLattice(lattice, Settings.ontologySettings.get(args[1]));

                System.out.println("[INFO] Comparing lattice and ontology");
                List<Subsumption> subsumptions = (new LatticeOntologyMiner()).mineLatticeWrtOntology(lattice,
                        Settings.ontologySettings.get(args[1]));

                System.out.println("[INFO] Computing statistics");
                MiningStatistics stats = new MiningStatistics(lattice, subsumptions);
                System.out.println("[INFO] Writing statistics");
                MiningStatisticsWriter statsWriter = new MiningStatisticsWriter();
                statsWriter.open(args[3]);
                statsWriter.writeStatistics(stats);
                statsWriter.close();

                System.out.println("[INFO] Writing mining results");
                MiningResultsWriter resultsWriter = new MiningResultsWriter();
                resultsWriter.writeMiningResults(subsumptions, args[2]);
            }

            tm.stop();
            System.out.println("[INFO] LatticeMiner on " + args[0] + " finished in " + tm);
        }
    }
}

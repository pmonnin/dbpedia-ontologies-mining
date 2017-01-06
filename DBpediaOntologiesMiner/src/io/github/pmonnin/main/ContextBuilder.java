package io.github.pmonnin.main;

import io.github.pmonnin.factories.FormalContextFactory;
import io.github.pmonnin.fca.FormalContext;
import io.github.pmonnin.io.FormalContextStatisticsWriter;
import io.github.pmonnin.io.FormalContextWriter;
import io.github.pmonnin.settings.Settings;
import io.github.pmonnin.statistics.FormalContextStatistics;
import io.github.pmonnin.util.TimeMeasurer;

/**
 * Main class to build a context for the lattice
 * @author Pierre Monnin
 */
public class ContextBuilder {

    /**
     * main function to build the context of the lattice
     * @param args Should contain the name of one context configuration
     *             The name of the file where the context will be written
     *             The name of the file where the statistics will be written
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java io.github.pmonnin.main.ContextBuilder configuration output statistics");
            System.out.println("\tconfiguration\tThe configuration for the context");
            System.out.println("\toutput\tFile where the context will be written");
            System.out.println("\tstatistics\tFile where the statistics of the context will be written");
        }

        else if (!Settings.contextSettings.containsKey(args[0])) {
            System.out.println("[ERROR] Configuration " + args[0] + "unknown.");
            System.out.println("[ERROR] Possible values are: " + Settings.contextSettings.keySet());
        }

        else {
            System.out.println("[INFO] ContextBuilder begin on " + args[0]);
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            FormalContext fc = (new FormalContextFactory()).buildFormalContext(Settings.contextSettings.get(args[0]));
            FormalContextWriter writer = new FormalContextWriter();
            writer.open(args[1]);
            writer.writeFormalContext(fc);
            writer.close();

            FormalContextStatistics stats = new FormalContextStatistics(fc);
            FormalContextStatisticsWriter fcsw = new FormalContextStatisticsWriter();
            fcsw.open(args[2]);
            fcsw.writeFormalContextStatistics(stats);
            fcsw.close();

            tm.stop();
            System.out.println("[INFO] ContextBuilder on " + args[0] + " finished in " + tm);
        }
    }
}

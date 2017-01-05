package io.github.pmonnin.main;

import io.github.pmonnin.factories.OntologyFactory;
import io.github.pmonnin.io.OntologyStatisticsWriter;
import io.github.pmonnin.semanticwebobjects.OntologyClass;
import io.github.pmonnin.settings.OntologySettings;
import io.github.pmonnin.settings.Settings;
import io.github.pmonnin.statistics.OntologyStatistics;
import io.github.pmonnin.util.TimeMeasurer;

import java.util.Map;

/**
 * Main class to compute statistics on one ontology
 *
 * @author Pierre Monnin
 */
public class OntologyStatisticsCalculator {

    /**
     * main function to compute statistics on one ontology
     * @param args Should contain the name of one ontology among
     *             The name of the file where the statistics will be written
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java io.github.pmonnin.main.OntologyStatisticsCalculator ontology output");
            System.out.println("\tontology\tThe ontology to query");
            System.out.println("\toutput\tFile where the statistics will be written");
        }

        else if (!Settings.ontologySettings.containsKey(args[0])){
            System.out.println("[ERROR] Ontology " + args[0] + "unknown.");
            System.out.println("[ERROR] Possible values are: " + Settings.ontologySettings.keySet());
        }

        else {
            System.out.println("[INFO] OntologyStatisticsCalculator begin on " + args[0]);
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            OntologySettings settings = Settings.ontologySettings.get(args[0]);

            Map<String, OntologyClass> ontology = (new OntologyFactory()).buildOntology(settings.getUriPrefix(),
                    settings.getCreationPrefixes(), settings.getCreationWhereConditions(),
                    settings.getParentsPrefixes(), settings.getParentsPredicate());

            OntologyStatistics statistics = new OntologyStatistics(ontology);
            OntologyStatisticsWriter writer = new OntologyStatisticsWriter();
            writer.open(args[1]);
            writer.writeStatistics(statistics);
            writer.close();

            tm.stop();
            System.out.println("[INFO] OntologyStatisticsCalculator on " + args[0] + " finished in " + tm);
        }
    }
}

package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.HierarchiesStatisticsWriter;
import dbpediaanalyzer.statistic.HierarchyStatistics;
import io.github.pmonnin.util.TimeMeasurer;

/**
 * Computes and saves statistics about DBpedia ontologies used to classify pages
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesStatistics {

    /**
     * TODO JAVADOC
     * @param args
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage:\n java HierarchiesStatistics output");
            System.out.println("\t output\n\t\t file used to store computed hierarchies statistics");
            System.out.println("\t\t If cycles are detected within a hierarchy, they will be stored inside a file " +
                    "named output-hierarchy-name-cycles");
        }

        else {
            System.out.println("=== HIERARCHIES STATISTICS ===");
            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("Querying hierarchies from server...");
            HierarchiesManager hm = HierarchiesFactory.createHierarchies();

            System.out.println("Computing statistics...");
            System.out.println("\tComputing categories statistics");
            HierarchyStatistics categoriesStatistics = hm.getCategoriesStatistics();
            System.out.println("\tComputing ontology classes statistics");
            HierarchyStatistics ontologyClassesStatistics = hm.getOntologyClassesStatistics();
            System.out.println("\tComputing yago classes statistics");
            HierarchyStatistics yagoClassesStatistics = hm.getYagoClassesStatistics();

            System.out.println("Saving statistics into file " + args[0] + "...");
            HierarchiesStatisticsWriter writer = new HierarchiesStatisticsWriter(args[0]);
            System.out.println("\tSaving categories statistics");
            writer.writeHierarchyStatistics(categoriesStatistics, "Categories");
            System.out.println("\tSaving ontology classes statistics");
            writer.writeHierarchyStatistics(ontologyClassesStatistics, "Ontology classes");
            System.out.println("\tSaving yago classes statistics");
            writer.writeHierarchyStatistics(yagoClassesStatistics, "Yago classes");
            writer.close();

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of hierarchies statistics computing program ===");
        }
    }
}

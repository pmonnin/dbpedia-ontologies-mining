package dbpediaanalyzer.mains;

import dbpediaanalyzer.dbpediaobjects.HierarchiesManager;
import dbpediaanalyzer.factories.HierarchiesFactory;
import dbpediaanalyzer.io.HierarchiesStatisticsWriter;
import dbpediaanalyzer.statistics.HierarchyStatistics;

/**
 * TODO JAVADOC
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
            System.out.println("Usage:\n  java HierarchiesStatistics output");
            System.out.println("\t output\n\t\t file used to store computed hierarchies statistics");
        }

        else {
            System.out.println("=== HIERARCHIES STATISTICS ===");

            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();

            HierarchyStatistics categoriesStatistics = hm.getCategoriesStatistics();
            HierarchyStatistics ontologyClassesStatistics = hm.getOntologyClassesStatistics();
            HierarchyStatistics yagoClassesStatistics = hm.getYagoClassesStatistics();

            System.out.println("--- Categories statistics ---");
            categoriesStatistics.display();
            System.out.println("--- Ontology classes statistics ---");
            ontologyClassesStatistics.display();
            System.out.println("--- Yago classes statistics ---");
            yagoClassesStatistics.display();

            HierarchiesStatisticsWriter writer = new HierarchiesStatisticsWriter(args[0]);
            writer.writeHierarchyStatistics(categoriesStatistics, "Categories");
            writer.writeHierarchyStatistics(ontologyClassesStatistics, "Ontology classes");
            writer.writeHierarchyStatistics(yagoClassesStatistics, "Yago classes");
            writer.close();
        }
    }
}

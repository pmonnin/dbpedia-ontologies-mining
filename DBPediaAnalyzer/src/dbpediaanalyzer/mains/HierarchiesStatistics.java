package dbpediaanalyzer.mains;

import dbpediaanalyzer.dbpediaobjects.HierarchiesManager;
import dbpediaanalyzer.factories.HierarchiesFactory;

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

            System.out.println("--- Categories statistics ---");
            hm.getCategoriesStatistics().display();
            System.out.println("--- Ontology classes statistics ---");
            hm.getOntologyClassesStatistics().display();
            System.out.println("--- Yago classes statistics ---");
            hm.getYagoClassesStatistics().display();
        }
    }
}

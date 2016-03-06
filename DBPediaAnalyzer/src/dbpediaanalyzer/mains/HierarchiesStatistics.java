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
            // TODO Usage output
        }

        else {
            System.out.println("=== HIERARCHIES STATISTICS ===");

            HierarchiesManager hm = (new HierarchiesFactory()).createHierarchies();

            hm.getCategoriesStatistics().display();
            hm.getOntologyClassesStatistics().display();
            hm.getYagoClassesStatistics().display();
        }
    }
}

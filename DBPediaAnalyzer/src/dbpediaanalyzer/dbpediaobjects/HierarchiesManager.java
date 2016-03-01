package dbpediaanalyzer.dbpediaobjects;

import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesManager {
    private HashMap<String, Category> categories;
    private HashMap<String, OntologyClass> ontologyClasses;
    private HashMap<String, YagoClass> yagoClasses;

    public HierarchiesManager() {
        this.categories = new HashMap<>();
        this.ontologyClasses = new HashMap<>();
        this.yagoClasses = new HashMap<>();
    }
}

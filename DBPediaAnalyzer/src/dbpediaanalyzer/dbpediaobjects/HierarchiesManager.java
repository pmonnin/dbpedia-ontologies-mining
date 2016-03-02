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

    public HierarchiesManager(HashMap<String, Category> categories, HashMap<String, OntologyClass> ontologyClasses,
                              HashMap<String, YagoClass> yagoClasses) {
        this.categories = categories;
        this.ontologyClasses = ontologyClasses;
        this.yagoClasses = yagoClasses;
    }
}

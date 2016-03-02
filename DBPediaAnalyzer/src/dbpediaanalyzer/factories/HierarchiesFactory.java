package dbpediaanalyzer.factories;

import dbpediaanalyzer.dbpediaobjects.Category;
import dbpediaanalyzer.dbpediaobjects.HierarchiesManager;
import dbpediaanalyzer.dbpediaobjects.OntologyClass;
import dbpediaanalyzer.dbpediaobjects.YagoClass;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;
import dbpediaanalyzer.io.SparqlField;

import java.io.IOException;
import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesFactory {

    /**
     * TODO JAVADOC
     */
    public HierarchiesManager createHierarchies() {
        return new HierarchiesManager(createCategoriesHierarchy(), createOntologyClassesHierarchy(),
                createYagoClassesHierarchy());
    }

    private HashMap<String, Category> createCategoriesHierarchy() {
        HashMap<String, Category> categories = new HashMap<>();

        try {
            SparqlResponse response = (new ServerQuerier()).runQuery(
                    "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> " +
                    "select distinct ?child ?parent where {" +
                    "?child rdf:type skos:Concept . " +
                    "FILTER (REGEX(STR(?child), \"http://dbpedia.org/resource/Category\", \"i\")) . " +
                    "OPTIONAL {" +
                    "?child skos:broader ?parent . " +
                    "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\")) } }"
                    );

            if(response.getResults() != null && response.getResults().getBindings() != null) {
                for(SparqlRecord r : response.getResults().getBindings()) {
                    SparqlField child = r.getFields().get("child");

                    if(!categories.containsKey(child.getValue())) {
                        categories.put(child.getValue(), new Category(child.getValue()));
                    }

                    SparqlField parent = r.getFields().get("parent");
                    if(parent != null) {
                        if(!categories.containsKey(parent.getValue())) {
                            categories.put(parent.getValue(), new Category(parent.getValue()));
                        }

                        Category childCategory = categories.get(child.getValue());
                        Category parentCategory = categories.get(parent.getValue());

                        childCategory.addParent(parentCategory);
                        parentCategory.addChild(childCategory);
                    }
                }
            }
        }

        catch(IOException e) {
            System.err.println("An exception was caught during categories hierarchy creation. Consequently, an empty " +
                    "hierarchy was created");
            System.err.println("Caused by:\n" + e.getMessage());
        }

        return categories;
    }

    private HashMap<String, OntologyClass> createOntologyClassesHierarchy() {
        return null;
    }

    private HashMap<String, YagoClass> createYagoClassesHierarchy() {
        return null;
    }
}

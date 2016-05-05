package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;
import dbpediaanalyzer.io.SparqlValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesFactory {
    private static final String[] QUERY_SUFFIXES = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "[^a-z]"};

    /**
     * TODO JAVADOC
     */
    public static HierarchiesManager createHierarchies() {
        return new HierarchiesManager(createCategoriesHierarchy(), createOntologyClassesHierarchy(),
                createYagoClassesHierarchy());
    }

    private static Map<String, Category> createCategoriesHierarchy() {
        HashMap<String, Category> categories = new HashMap<>();

        for(String suffix : QUERY_SUFFIXES) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> " +
                        "select distinct ?category where {" +
                        "?category rdf:type skos:Concept . " +
                        "FILTER (REGEX(STR(?category), \"http://dbpedia.org/resource/Category:" + suffix + "\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue value = r.getFields().get("category");

                        if(value != null && !categories.containsKey(value.getValue())) {
                            categories.put(value.getValue(), new Category(value.getValue()));
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying categories... New try... (" + e.getMessage() + ")");
                }
            }
        }

        for(Map.Entry<String, Category> entry : categories.entrySet()) {
            boolean done = false;

            while(!done) {
                try  {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> " +
                        "select distinct ?parent where {" +
                        "?parent rdf:type skos:Concept . " +
                        "<" + entry.getKey() + "> skos:broader ?parent . " +
                        "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue value = r.getFields().get("parent");

                        if(value != null) {
                            if(!categories.containsKey(value.getValue())) {
                                System.err.println(value.getValue() + " was discovered as parent but not found in all " +
                                        "categories");
                                categories.put(value.getValue(), new Category(value.getValue()));
                            }

                            Category parent = categories.get(value.getValue());
                            Category child = entry.getValue();

                            child.addParent(parent);
                            parent.addChild(child);
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying categories parents... New try... (" +
                            e.getMessage() + ")");
                }
            }
        }

        return categories;
    }

    private static Map<String, OntologyClass> createOntologyClassesHierarchy() {
        HashMap<String, OntologyClass> ontologyClasses = new HashMap<>();

        try {
            SparqlResponse response = (new ServerQuerier()).runQuery(
                    "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                    "select distinct ?child ?parent where { " +
                    "?child rdf:type owl:Class . " +
                    "FILTER (REGEX(STR(?child), \"http://dbpedia.org/ontology\", \"i\")) . " +
                    "OPTIONAL { " +
                    "?child rdfs:subClassOf ?parent . " +
                    "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/ontology\", \"i\")) } }"
            );

            for(SparqlRecord r : response.getRecords()) {
                SparqlValue child = r.getFields().get("child");

                if(!ontologyClasses.containsKey(child.getValue())) {
                    ontologyClasses.put(child.getValue(), new OntologyClass(child.getValue()));
                }

                SparqlValue parent = r.getFields().get("parent");
                if(parent != null) {
                    if(!ontologyClasses.containsKey(parent.getValue())) {
                        ontologyClasses.put(parent.getValue(), new OntologyClass(parent.getValue()));
                    }

                    OntologyClass childOntology = ontologyClasses.get(child.getValue());
                    OntologyClass parentOntology = ontologyClasses.get(parent.getValue());

                    childOntology.addParent(parentOntology);
                    parentOntology.addChild(childOntology);
                }
            }
        }

        catch(IOException e) {
            System.err.println("An exception was caught during ontology classes hierarchy creation. Consequently, an " +
                    "empty hierarchy was created");
            System.err.println("Caused by:\n" + e.getMessage());
        }

        return ontologyClasses;
    }

    private static Map<String, YagoClass> createYagoClassesHierarchy() {
        HashMap<String, YagoClass> yagoClasses = new HashMap<>();

        try {
            SparqlResponse response = (new ServerQuerier()).runQuery(
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                    "select distinct ?child ?parent where {" +
                    "?child owl:equivalentClass ?ec ." +
                    "FILTER (REGEX(STR(?child), \"http://dbpedia.org/class/yago\", \"i\")) ." +
                    "FILTER (REGEX(STR(?ec), \"http://yago-knowledge.org\", \"i\")) . " +
                    "OPTIONAL {" +
                    "?child rdfs:subClassOf ?parent . " +
                    "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/class/yago\", \"i\")) } }"
            );

            for(SparqlRecord r : response.getRecords()) {
                SparqlValue child = r.getFields().get("child");

                if(!yagoClasses.containsKey(child.getValue())) {
                    yagoClasses.put(child.getValue(), new YagoClass(child.getValue()));
                }

                SparqlValue parent = r.getFields().get("parent");
                if(parent != null) {
                    if(!yagoClasses.containsKey(parent.getValue())) {
                        yagoClasses.put(parent.getValue(), new YagoClass(parent.getValue()));
                    }

                    YagoClass childYago = yagoClasses.get(child.getValue());
                    YagoClass parentYago = yagoClasses.get(parent.getValue());

                    childYago.addParent(parentYago);
                    parentYago.addChild(childYago);
                }
            }
        }

        catch(IOException e) {
            System.err.println("An exception was caught during yago classes hierarchy creation. Consequently, an " +
                    "empty hierarchy was created");
            System.err.println("Caused by:\n" + e.getMessage());
        }

        return yagoClasses;
    }
}

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
                        SparqlValue categoryUri = r.getFields().get("category");

                        if(categoryUri != null && !categories.containsKey(categoryUri.getValue())) {
                            categories.put(categoryUri.getValue(), new Category(categoryUri.getValue()));
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
                        "<" + entry.getKey() + "> skos:broader ?parent . " +
                        "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue parentUri = r.getFields().get("parent");

                        if(parentUri != null) {
                            if(!categories.containsKey(parentUri.getValue())) {
                                System.err.println(parentUri.getValue() + " was discovered as parent but not found in " +
                                        "all categories");
                            }

                            else {
                                Category parent = categories.get(parentUri.getValue());
                                Category child = entry.getValue();

                                child.addParent(parent);
                                parent.addChild(child);
                            }
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

        for(String suffix : QUERY_SUFFIXES) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                        "select distinct ?ontologyClass where { " +
                        "?ontologyClass rdf:type owl:Class . " +
                        "FILTER (REGEX(STR(?ontologyClass), \"http://dbpedia.org/ontology:" + suffix + "\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue ontologyClassUri = r.getFields().get("ontologyClass");

                        if(ontologyClassUri != null && !ontologyClasses.containsKey(ontologyClassUri.getValue())) {
                            ontologyClasses.put(ontologyClassUri.getValue(), new OntologyClass(ontologyClassUri.getValue()));
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying ontology classes... New try... (" + e.getMessage() + ")");
                }
            }
        }

        for(Map.Entry<String, OntologyClass> entry : ontologyClasses.entrySet()) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>  " +
                        "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                        "select distinct ?parent where { " +
                        "<" + entry.getKey() + "> rdfs:subClassOf ?parent . " +
                        "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/ontology\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue parentUri = r.getFields().get("parent");

                        if(parentUri != null) {
                            if(!ontologyClasses.containsKey(parentUri.getValue())) {
                                System.err.println(parentUri.getValue() + " was discovered as parent but not found in " +
                                        "all ontology classes");
                            }

                            else {
                                OntologyClass parent = ontologyClasses.get(parentUri.getValue());
                                OntologyClass child = entry.getValue();

                                parent.addChild(child);
                                child.addParent(parent);
                            }
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying ontology classes parents... New try... (" +
                            e.getMessage() + ")");
                }
            }
        }

        return ontologyClasses;
    }

    private static Map<String, YagoClass> createYagoClassesHierarchy() {
        HashMap<String, YagoClass> yagoClasses = new HashMap<>();

        for(String suffix : QUERY_SUFFIXES) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX owl:<http://www.w3.org/2002/07/owl#> " +
                        "select distinct ?yagoClass where {" +
                        "?yagoClass owl:equivalentClass ?ec ." +
                        "FILTER (REGEX(STR(?yagoClass), \"http://dbpedia.org/class/yago:" + suffix + "\", \"i\")) ." +
                        "FILTER (REGEX(STR(?ec), \"http://yago-knowledge.org\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue yagoClassUri = r.getFields().get("yagoClass");

                        if(yagoClassUri != null && !yagoClasses.containsKey(yagoClassUri.getValue())) {
                            yagoClasses.put(yagoClassUri.getValue(), new YagoClass(yagoClassUri.getValue()));
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying yago classes... New try... (" + e.getMessage() + ")");
                }
            }
        }

        for(Map.Entry<String, YagoClass> entry : yagoClasses.entrySet()) {
            boolean done = false;

            while(!done) {
                try {
                    SparqlResponse response = (new ServerQuerier()).runQuery(
                        "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
                        "select distinct ?parent where {" +
                        "<" + entry.getKey() + "> rdfs:subClassOf ?parent . " +
                        "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/class/yago\", \"i\")) . }"
                    );

                    for(SparqlRecord r : response.getRecords()) {
                        SparqlValue parentUri = r.getFields().get("parent");

                        if(parentUri != null) {
                            if(!yagoClasses.containsKey(parentUri.getValue())) {
                                System.err.println(parentUri.getValue() + " was discovered as parent but not found in " +
                                        "all yago classes");
                            }

                            else {
                                YagoClass parent = yagoClasses.get(parentUri.getValue());
                                YagoClass child = entry.getValue();

                                child.addParent(parent);
                                parent.addChild(child);
                            }
                        }
                    }

                    done = true;
                }

                catch(IOException e) {
                    System.err.println("Exception while querying yago classes parents... New try... (" +
                            e.getMessage() + ")");
                }
            }
        }

        return yagoClasses;
    }
}

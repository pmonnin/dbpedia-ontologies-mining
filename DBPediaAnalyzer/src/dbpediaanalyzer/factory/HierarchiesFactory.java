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
                    SparqlValue child = r.getFields().get("child");

                    if(!categories.containsKey(child.getValue())) {
                        categories.put(child.getValue(), new Category(child.getValue()));
                    }

                    SparqlValue parent = r.getFields().get("parent");
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

            if(response.getResults() != null && response.getResults().getBindings() != null) {
                for(SparqlRecord r : response.getResults().getBindings()) {
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
        }

        catch(IOException e) {
            System.err.println("An exception was caught during ontology classes hierarchy creation. Consequently, an " +
                    "empty hierarchy was created");
            System.err.println("Caused by:\n" + e.getMessage());
        }

        return ontologyClasses;
    }

    private HashMap<String, YagoClass> createYagoClassesHierarchy() {
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

            if(response.getResults() != null && response.getResults().getBindings() != null) {
                for(SparqlRecord r : response.getResults().getBindings()) {
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
        }

        catch(IOException e) {
            System.err.println("An exception was caught during yago classes hierarchy creation. Consequently, an " +
                    "empty hierarchy was created");
            System.err.println("Caused by:\n" + e.getMessage());
        }

        return yagoClasses;
    }
}

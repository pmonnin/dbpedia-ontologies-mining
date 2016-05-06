package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.*;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataSetFactory {

    /**
     * TODO JAVADOC
     * @param minDeathDate
     * @param maxDeathDate
     */
    public static Map<String, Page> createDataSet(String minDeathDate, String maxDeathDate, HierarchiesManager hierarchiesManager) {
        HashMap<String, Page> dataSet = new HashMap<>();
        HashMap<String, Boolean> missingCategories = new HashMap<>();
        HashMap<String, Boolean> missingOntologyClasses = new HashMap<>();
        HashMap<String, Boolean> missingYagoClasses = new HashMap<>();

        try {
            SparqlResponse response = (new ServerQuerier()).runQuery(
                    "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX dbo:<http://dbpedia.org/ontology/> " +
                    "select distinct ?page where { " +
                    "?page dbo:wikiPageID ?pageId . " +
                    "?page rdf:type/rdfs:subClassOf* dbo:Person . " +
                    "?page dbo:deathDate ?deathDate . " +
                    "FILTER(?deathDate >= \"" + minDeathDate + "\"^^xsd:date) . " +
                    "FILTER(?deathDate < \"" + maxDeathDate + "\"^^xsd:date) }"
                    );

            for(SparqlRecord r : response.getRecords()) {
                boolean tryAgain = true;

                while(tryAgain) {
                    tryAgain = false;

                    try {
                        Page page = new Page(r.getFields().get("page").getValue());
                        dataSet.put(r.getFields().get("page").getValue(), page);

                        // For each page, we get additional data
                        // Relationships
                        SparqlResponse responseRelationships = (new ServerQuerier()).runQuery(
                                "select distinct ?r where {" +
                                        "<" + page.getURI() + "> ?r ?other }"
                        );

                        for(SparqlRecord record : responseRelationships.getRecords()) {
                            page.addRelationship(record.getFields().get("r").getValue());
                        }

                        // Categories
                        SparqlResponse responseCategories = (new ServerQuerier()).runQuery(
                                "PREFIX dcterms:<http://purl.org/dc/terms/> " +
                                        "select distinct ?c where { " +
                                        "<" + page.getURI() + "> dcterms:subject ?c . " +
                                        "FILTER (REGEX(STR(?c), \"http://dbpedia.org/resource/Category\", \"i\")) }"
                        );

                        for(SparqlRecord record : responseCategories.getRecords()) {
                            String categoryUri = record.getFields().get("c").getValue();

                            if(categoryUri != null) {
                                Category category = hierarchiesManager.getCategoryFromUri(categoryUri);

                                if(category != null) {
                                    page.addCategory(category);
                                }

                                else {
                                    missingCategories.put(categoryUri, true);
                                }
                            }
                        }

                        // Ontology classes
                        SparqlResponse responseOntologyClasses = (new ServerQuerier()).runQuery(
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                        "select distinct ?o where { " +
                                        "<" + page.getURI() + "> rdf:type ?o . " +
                                        "FILTER(REGEX(STR(?o), \"http://dbpedia.org/ontology\", \"i\")) }"
                        );

                        for(SparqlRecord record : responseOntologyClasses.getRecords()) {
                            String ontologyClassUri = record.getFields().get("o").getValue();

                            if(ontologyClassUri != null) {
                                OntologyClass ontologyClass = hierarchiesManager.getOntologyClassFromUri(ontologyClassUri);

                                if(ontologyClass != null) {
                                    page.addOntologyClass(ontologyClass);
                                }

                                else {
                                    missingOntologyClasses.put(ontologyClassUri, true);
                                }
                            }
                        }

                        // Yago classes
                        SparqlResponse responseYagoClasses = (new ServerQuerier()).runQuery(
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                        "select distinct ?y where { " +
                                        "<" + page.getURI() + "> rdf:type ?y . " +
                                        "FILTER(REGEX(STR(?y), \"http://dbpedia.org/class/yago\", \"i\")) }"
                        );

                        for(SparqlRecord record : responseYagoClasses.getRecords()) {
                            String yagoClassUri = record.getFields().get("y").getValue();

                            if(yagoClassUri != null) {
                                YagoClass yagoClass = hierarchiesManager.getYagoClassFromUri(yagoClassUri);

                                if(yagoClass != null) {
                                    page.addYagoClass(yagoClass);
                                }

                                else {
                                    missingYagoClasses.put(yagoClassUri, true);
                                }
                            }
                        }
                    }

                    catch(IOException e) {
                        tryAgain = true;
                        System.err.println("Error while fetching more data from server for a page (" + e.getMessage() + ")... New try...");
                    }
                }
            }

            displayMissingHierarchyElementsMessage(missingCategories, "categories");
            displayMissingHierarchyElementsMessage(missingOntologyClasses, "ontology classes");
            displayMissingHierarchyElementsMessage(missingYagoClasses, "yago classes");
        }

        catch(IOException e) {
            System.err.println("Error while trying to create data set (" + e.getMessage() + "). As a consequence, " +
                    "an empty data set will be used.");
            dataSet = new HashMap<>();
        }

        return dataSet;
    }

    private static void displayMissingHierarchyElementsMessage(Map<String, Boolean> missingElements, String hierarchyElementsName) {
        if(missingElements.size() != 0) {
            System.err.println(missingElements.size() + " " + hierarchyElementsName + " where discovered during pages " +
                    "analysis but not found in all " + hierarchyElementsName);
        }
    }
}

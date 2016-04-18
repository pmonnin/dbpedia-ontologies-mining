package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.dbpediaobject.Page;
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

                        for (SparqlRecord record : responseRelationships.getRecords()) {
                            page.addRelationship(record.getFields().get("r").getValue());
                        }

                        // Categories
                        SparqlResponse responseCategories = (new ServerQuerier()).runQuery(
                                "PREFIX dcterms:<http://purl.org/dc/terms/> " +
                                        "select distinct ?c where { " +
                                        "<" + page.getURI() + "> dcterms:subject ?c . " +
                                        "FILTER (REGEX(STR(?c), \"http://dbpedia.org/resource/Category\", \"i\")) }"
                        );

                        for (SparqlRecord record : responseCategories.getRecords()) {
                            page.addCategory(hierarchiesManager.getCategoryFromUri(record.getFields().get("c").getValue()));
                        }

                        // Ontology classes
                        SparqlResponse responseOntologyClasses = (new ServerQuerier()).runQuery(
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                        "select distinct ?o where { " +
                                        "<" + page.getURI() + "> rdf:type ?o . " +
                                        "FILTER(REGEX(STR(?o), \"http://dbpedia.org/ontology\", \"i\")) }"
                        );

                        for (SparqlRecord record : responseOntologyClasses.getRecords()) {
                            page.addOntologyClass(hierarchiesManager.getOntologyClassFromUri(record.getFields().get("o").getValue()));
                        }

                        // Yago classes
                        SparqlResponse responseYagoClasses = (new ServerQuerier()).runQuery(
                                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                                        "select distinct ?y where { " +
                                        "<" + page.getURI() + "> rdf:type ?y . " +
                                        "FILTER(REGEX(STR(?y), \"http://dbpedia.org/class/yago\", \"i\")) }"
                        );

                        for (SparqlRecord record : responseYagoClasses.getRecords()) {
                            page.addYagoClass(hierarchiesManager.getYagoClassFromUri(record.getFields().get("y").getValue()));
                        }
                    }

                    catch(IOException e) {
                        tryAgain = true;
                        System.err.println("Error while fetching more data from server for a page (" + e.getMessage() + ")... New try...");
                    }
                }
            }
        }

        catch(IOException e) {
            System.err.println("Error while trying to create data set (" + e.getMessage() + "). As a consequence, " +
                    "an empty data set will be used.");
            dataSet = new HashMap<>();
        }

        return dataSet;
    }
}

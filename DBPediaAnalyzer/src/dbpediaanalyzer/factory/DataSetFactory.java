package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.io.ServerQuerier;
import dbpediaanalyzer.io.SparqlRecord;
import dbpediaanalyzer.io.SparqlResponse;

import java.io.IOException;
import java.util.HashMap;

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
    public HashMap<String, Page> createDataSet(String minDeathDate, String maxDeathDate) {
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
                    "FILTER(?deathDate < \"" + maxDeathDate + "\"^^xsd:date) . }"
                    );

            for(SparqlRecord r : response.getRecords()) {
                Page page = new Page(r.getFields().get("page").getValue());
                dataSet.put(r.getFields().get("page").getValue(), page);

                // For each page, we get additional data
                SparqlResponse responseRelationships = (new ServerQuerier()).runQuery(
                        "select distinct ?r where {" +
                        "<" + page.getURI() + "> ?r ?other . }"
                        );

                for(SparqlRecord relationship : responseRelationships.getRecords()) {
                    page.addRelationship(relationship.getFields().get("r").getValue());
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

package io.github.pmonnin.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Class responsible of the communication with the SPARQL endpoint
 *
 * @author Pierre Monnin
 */
public class ServerQuerier {

    /**
     * Local address of the Virtuoso server. To be changed for a custom location.
     * Settings:
     *  - default-graph-uri: http://dbpedia.org
     *  - format: application/sparql-results+json
     *  - debug: on
     *  - query: to be completed with a string concatenation
     */
    private static final String SERVER_BASE_URL = "http://localhost:8890/sparql/?" +
            "default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&debug=on&query=";

    private static ObjectMapper mapper = new ObjectMapper();

    public ServerQuerier() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    /**
     * Execute a SPARQL query on the server
     * @param query SPARQL query to run on the server
     * @return the SPARQL response to the given query
     * @throws IOException when there is a communication problem with the server
     */
    public SparqlResponse runQuery(String query) throws IOException {
        return mapper.readValue(new URL(ServerQuerier.SERVER_BASE_URL + URLEncoder.encode(query, "UTF-8")), SparqlResponse.class);
    }

}

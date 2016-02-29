package dbpediaanalyzer.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URL;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 */
public class ServerQuerier {

    /**
     * Local address of the Virtuoso server
     * Sets
     *  - default-graph-uri: http://dbpedia.org
     *  - format: application/sparql-results+json
     *  - debug: on
     *  - query: empty (to be completed with a string concatenation)
     */
    private static final String SERVER_BASE_URL = "http://localhost:8890/sparql/?" +
            "default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&debug=on&query=";

    private static ObjectMapper mapper = new ObjectMapper();

    public ServerQuerier() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * TODO JAVADOC
     * @param query
     * @return
     * @throws IOException
     */
    public SparqlResponse runQuery(String query) throws IOException {
        return mapper.readValue(new URL(ServerQuerier.SERVER_BASE_URL + query), SparqlResponse.class);
    }

}

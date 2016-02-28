package dbpediaanalyzer.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
    private static final String SERVER = "http://localhost:8890/sparql/?" +
            "default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&debug=on&query=";

    private ObjectMapper mapper;

    public ServerQuerier() {
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}

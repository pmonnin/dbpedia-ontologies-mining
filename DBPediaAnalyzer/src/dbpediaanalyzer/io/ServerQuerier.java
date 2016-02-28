package dbpediaanalyzer.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 */
public class ServerQuerier {
    private static final String SERVER_CRAN = "http://localhost:8890/sparql/?" +
            "default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&debug=on&query=";

    private ObjectMapper mapper;

    public ServerQuerier() {
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}

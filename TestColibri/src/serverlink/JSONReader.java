package serverlink;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Java requests to HTTP Server. Automatic crawling of server answer and auto-binding to given model class. Server answer must be JSON 
 */
public final class JSONReader {
    /**
     * Server address
     */
    public static final String SERVER_LOC = "http://sbc2015.telecomnancy.univ-lorraine.fr:10000/publi/query?output=json&query=";
    public static final String DBPEDIA_LOC = "http://dbpedia.org/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&timeout=30000&debug=on&query=";
    public static final String SERVER_CRAN = "http://localhost:8890/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&debug=on&query=";
    private static ObjectMapper mapper = new ObjectMapper();

    public JSONReader() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Crawls all children and parents of DBPedia hierarchy type (category, ontology, yago class)
     * 
     * @param request SPARQL request to execute
     * @return children and parents list
     */
    public static List<ChildAndParent> getChildrenAndParents(String request) {
        SparqlResponse response = new SparqlResponse();

        try {
            response = mapper.readValue(new URL(SERVER_CRAN + request), SparqlResponse.class);
        } catch (JsonParseException | JsonMappingException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getResults().getBindings();
    }
}

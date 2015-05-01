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
 * Requêtes Java vers serveur HTTP. Récupération automatique de la réponse et auto-binding dans la classe modèle fournie. La réponse du serveur doit être en JSON.
 */
public final class JSONReader {
    /**
     * Adresse du serveur
     */
    public static final String SERVER_LOC = "http://sbc2015.telecomnancy.univ-lorraine.fr:10000/publi/query?output=json&query=";
    public static final String DBPEDIA_LOC = "http://dbpedia.org/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&format=application%2Fsparql-results%2Bjson&timeout=30000&debug=on&query=";
    private static ObjectMapper mapper = new ObjectMapper();

    public JSONReader() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Cherche tous les enfants et parents d'un type de hiérarchie dbPedia (catégorie, ontologie ou Yago)
     * 
     * @param request
     *            Requête SPARQL à exécuter
     * @return liste des parents et leurs enfants
     */
    public static List<ChildAndParent> getChildrenAndParents(String request) {
        SparqlResponse response = new SparqlResponse();

        try {
            response = mapper.readValue(new URL(DBPEDIA_LOC + request), SparqlResponse.class);
        } catch (JsonParseException | JsonMappingException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getResults().getBindings();
    }
}

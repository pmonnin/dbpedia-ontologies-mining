package dbpediaobjects;

import java.util.HashMap;

/**
 * Manager of DB Ontologies list created from DBOntologiesCrawler
 *
 * @author Pierre Monnin
 */
public class DBOntologiesManager {
    public HashMap<String, DBOntology> ontologies;

    public DBOntologiesManager(HashMap<String, DBOntology> ontologies) {
        this.ontologies = ontologies;
    }

    public DBOntology getOntologyFromUri(String uri) {
        return this.ontologies.get(uri);
    }
}

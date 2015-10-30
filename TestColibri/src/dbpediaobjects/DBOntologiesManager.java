package dbpediaobjects;

import java.util.HashMap;

/**
 * Manager of DB Ontologies list created from DBOntologiesCrawler
 *
 * @author Pierre Monnin
 */
public class DBOntologiesManager {
    public HashMap<String, DBOntologyClass> ontologies;

    public DBOntologiesManager(HashMap<String, DBOntologyClass> ontologies) {
        this.ontologies = ontologies;
    }
}

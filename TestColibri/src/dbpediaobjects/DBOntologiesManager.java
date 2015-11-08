package dbpediaobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
}

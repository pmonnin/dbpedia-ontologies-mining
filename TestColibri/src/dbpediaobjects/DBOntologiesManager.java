package dbpediaobjects;

import java.util.ArrayList;
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

    public ArrayList<String> getSelfAndAncestors(String ontology) {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(ontology);

        return retVal;
    }
}

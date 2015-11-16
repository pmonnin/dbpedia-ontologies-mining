package dbpediaobjects;

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

    public DBOntology getOntologyFromUri(String uri) {
        return this.ontologies.get(uri);
    }

    public int getDataSetOntologiesNumber(HashMap<String, DBPage> dataSet) {
        // Initialization of ontologies
        for(String ontoUri : ontologies.keySet()) {
            ontologies.get(ontoUri).setSeen(false);
        }

        // Initialization of queue
        Queue<DBOntology> queue = new LinkedList<>();
        for(String pageUri : dataSet.keySet()) {
            for(DBOntology ontology : dataSet.get(pageUri).getOntologies()) {
                if(!ontology.getSeen()) {
                    ontology.setSeen(true);
                    queue.add(ontology);
                }
            }
        }

        // Ontologies traversal
        while(!queue.isEmpty()) {
            DBOntology ontology = queue.poll();

            for(DBOntology parent : ontology.getParents()) {
                if(!parent.getSeen()) {
                    parent.setSeen(true);
                    queue.add(parent);
                }
            }
        }

        // Count + ontologies reset
        int retVal = 0;
        for(String ontoUri : ontologies.keySet()) {
            if(ontologies.get(ontoUri).getSeen()) {
                retVal++;
                ontologies.get(ontoUri).setSeen(false);
            }
        }

        return retVal;
    }
}

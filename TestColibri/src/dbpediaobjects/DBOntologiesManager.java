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

    public ArrayList<String> getPageOntologiesAndAncestors(DBPage page) {
        ArrayList<String> retVal = new ArrayList<>();

        for(String ontology : page.getOntologies()) {
            for(String o : getSelfAndAncestors(ontology)) {
                if(!retVal.contains(o)) {
                    retVal.add(o);
                }
            }
        }

        return retVal;
    }

    public ArrayList<String> getSelfAndAncestors(String ontology) {
        ArrayList<String> retVal = new ArrayList<>();

        Queue<String> queue = new LinkedList<>();
        queue.add(ontology);

        while(!queue.isEmpty()) {
            String o = queue.poll();

            if(this.ontologies.containsKey(o)) {
                DBOntology dbo = this.ontologies.get(o);
                if(!retVal.contains(o)) {
                    retVal.add(o);
                }

                for(String parent : dbo.getParents()) {
                    queue.add(parent);
                }
            }
        }

        return retVal;
    }
}

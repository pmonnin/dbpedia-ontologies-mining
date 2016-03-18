package dbpediaanalyzer.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import dbpediaanalyzer.dbpediaobject.DBOntologiesManager;

import dbpediaanalyzer.dbpediaobject.DBOntology;

/**
 * Crawler of the DBPedia ontology classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
public class DBOntologiesCrawler {

    private HashMap<String, DBOntology> dbontologies;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws IOException thrown when server is unavailable
     */
    public static void main(String[] args) throws IOException {
        System.out.println("== START MAIN DB ONTOLOGIES CRAWLER ==");
        DBOntologiesCrawler crawler = new DBOntologiesCrawler();
        crawler.computeOntologiesHierarchy();
    }

    /**
     * Getter on the parsed ontology classes
     * @return the parsed ontology classes as a manager object
     */
    public DBOntologiesManager getDBOntologiesManager() {
        return new DBOntologiesManager(this.dbontologies);
    }

    /**
     * Method computing the DBPedia ontology classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void computeOntologiesHierarchy() throws IOException {

    }
}

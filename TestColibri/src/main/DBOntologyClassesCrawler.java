package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBOntologyClass;
import dbpediaobjects.PediaOntologyThread;

/**
 * Crawler of the DBPedia ontology classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBOntologyClassesCrawler {

    private HashMap<String, DBOntologyClass> dbontologies;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        new DBOntologyClassesCrawler().computeParents();
    }

    /**
     * Getter on the parsed ontology classes
     * @return the parsed ontology classes with HashMap (key = URI of the ontology classes and object is the DBOnolotyClass)
     */
    public HashMap<String, DBOntologyClass> getDbontologies() {
        return dbontologies;
    }

    /**
     * Method computing the DBPedia ontology classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
    	// Ask for all the ontology classes
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader.getJSON(URLEncoder.encode(
        		"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
        		+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
        		+ "select distinct ?Ontology ?Label where "
                + "{ ?Ontology rdf:type owl:Class . ?Ontology rdfs:label ?Label . "
                + "FILTER (REGEX(STR(?Ontology), \"http://dbpedia.org/ontology\", \"i\")) "
                + "FILTER(langMatches(lang(?Label), \"EN\")) }", "UTF-8"));

        // Parse them
        JsonParser parser = new JsonParser(jsonResponse);
        dbontologies = parser.getDbPediaOntologyClasses();

	    // Now we construct the hierarchy by asking parents of each ontology class
	    	//The hierarchy asking to the server is divided into nbCores thread -- threading setup here
        Set<String> keys = dbontologies.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaOntologyThread> threadList = new ArrayList<PediaOntologyThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBOntologyClass> threadOntologies = new ArrayList<DBOntologyClass>();

        for (String key : keys) {
            DBOntologyClass ont = dbontologies.get(key);
            threadOntologies.add(ont);

            // We give to each thread an amount of ontology classes to take care
            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaOntologyThread thread = new PediaOntologyThread(parser, urlReader, threadOntologies);
                thread.start();
                threadList.add(thread);
                threadOntologies = new ArrayList<DBOntologyClass>();
            }

            i++;
        }

        // We join the thread and add all the classes in the final hashmap
        System.out.println("STARTING THREADS JOIN...");
        dbontologies.clear();
        int categoriesWithParents = 0;
        for (PediaOntologyThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBOntologyClass ont : thread.getThreadCategories()) {
                    dbontologies.put(ont.getUri(), ont);
                    if (ont.getParentsNumber() != 0) {
                        categoriesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + categoriesWithParents + " ontologies avec parents.");
        System.out.println("Nombre total de classes ontologies : " + dbontologies.size());
    }
}

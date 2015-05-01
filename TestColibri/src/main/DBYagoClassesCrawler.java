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
import dbpediaobjects.DBYagoClass;
import dbpediaobjects.PediaYagoThread;

/**
 * Crawler of the DBPedia Yago classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBYagoClassesCrawler {

    private HashMap<String, DBYagoClass> dbyagoclasses;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        new DBYagoClassesCrawler().computeParents();
    }

    /**
     * Getter on the parsed yago classes
     * @return the parsed yago classes with HashMap (key = URI of the yago classes and object is the DBYagoClass)
     */
    public HashMap<String, DBYagoClass> getDbYagoClasses() {
        return dbyagoclasses;
    }

    /**
     * Method computing the DBPedia yago classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
        // Ask for all the yago classes
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader.getJSON(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?yago where "
                + "{ [] rdf:type ?yago . "
                + "FILTER (REGEX(STR(?yago), \"http://dbpedia.org/class/yago\", \"i\")) }", "UTF-8"));

        // Parse them
        JsonParser parser = new JsonParser(jsonResponse);
        dbyagoclasses = parser.getDbPediaYagoClasses();

    	// Now we construct the hierarchy by asking parents of each yago class
        	//The hierarchy asking to the server is divided into nbCores thread -- threading setup here
        Set<String> keys = dbyagoclasses.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaYagoThread> threadList = new ArrayList<PediaYagoThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBYagoClass> threadYagoClasses = new ArrayList<DBYagoClass>();

        for (String key : keys) {
            DBYagoClass yagoClass = dbyagoclasses.get(key);
            threadYagoClasses.add(yagoClass);

            // We give to each thread an amount of yago classes to take care
            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaYagoThread thread = new PediaYagoThread(parser, urlReader, threadYagoClasses);
                thread.start();
                threadList.add(thread);
                threadYagoClasses = new ArrayList<DBYagoClass>();
            }

            i++;
        }

        // We join the thread and add all the classes in the final hashmap
        System.out.println("STARTING THREADS JOIN...");
        dbyagoclasses.clear();
        int yagoClassesWithParents = 0;
        for (PediaYagoThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBYagoClass yagoClass : thread.getThreadYagoClasses()) {
                    dbyagoclasses.put(yagoClass.getUri(), yagoClass);
                    if (yagoClass.getParentsNumber() != 0) {
                        yagoClassesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + yagoClassesWithParents + " classes yago avec parents.");
        System.out.println("Nombre total de classes yago : " + dbyagoclasses.size());
    }
}

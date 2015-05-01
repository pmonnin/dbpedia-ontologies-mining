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
import dbpediaobjects.DBCategory;
import dbpediaobjects.PediaCategoryThread;

/**
 * Crawler of the DBPedia categories
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBCategoriesCrawler {

    private HashMap<String, DBCategory> dbcategories;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        new DBCategoriesCrawler().computeParents();
    }

    /**
     * Getter on the parsed categories
     * @return the parsed categories with HashMap (key = URI of the categories and object is the DBCategory)
     */
    public HashMap<String, DBCategory> getDbcategories() {
        return dbcategories;
    }

    /**
     * Method computing the DBPedia categories hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
    	// Ask for all the categories
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader.getJSON(URLEncoder.encode(
                "PREFIX dcterms:<http://purl.org/dc/terms/> "
        		+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "select distinct ?Category ?Label where "
                + "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label " + "}", "UTF-8"));

        // Parse them
        JsonParser parser = new JsonParser(jsonResponse);
        dbcategories = parser.getDbPediaCategories();

        // Now we construct the hierarchy by asking parents of each category
    		//The hierarchy asking to the server is divided into nbCores thread -- threading setup here
        Set<String> keys = dbcategories.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaCategoryThread> threadList = new ArrayList<PediaCategoryThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBCategory> threadCategories = new ArrayList<DBCategory>();

        for (String key : keys) {
            DBCategory cat = dbcategories.get(key);
            threadCategories.add(cat);

            // We give to each thread an amount of categories to take care
            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaCategoryThread thread = new PediaCategoryThread(parser, urlReader, threadCategories);
                thread.start();
                threadList.add(thread);
                threadCategories = new ArrayList<DBCategory>();
            }

            i++;
        }

        // We join the thread and add all the classes in the final hashmap
        System.out.println("STARTING THREADS JOIN...");
        dbcategories.clear();
        int categoriesWithParents = 0;
        for (PediaCategoryThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBCategory cat : thread.getThreadCategories()) {
                    dbcategories.put(cat.getUri(), cat);
                    if (cat.getParentsNumber() != 0) {
                        categoriesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("CATEGORIES CRAWLER TERMINE : " + categoriesWithParents + " catégories avec parents.");
        System.out.println("Nombre de catégories total : " + dbcategories.keySet().size());
    }
}

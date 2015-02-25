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
import dbpediaobjects.PediaThread;

public class DBCategoriesCrawler {

    private HashMap<String, DBCategory> dbcategories;

    public static void main(String[] args) throws UnsupportedEncodingException,
            IOException, ParseException {
        System.out.println("START MAIN");
        new DBCategoriesCrawler().computeParents();
    }

    public DBCategoriesCrawler() {

    }

    public void computeParents() throws UnsupportedEncodingException,
            IOException, ParseException {
        // Ask for categories json
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader
                .getJSON(URLEncoder
                        .encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> select distinct ?Category ?Label where "
                                + "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label "
                                + "}", "UTF-8"));

        // Parse the categories
        JsonParser parser = new JsonParser(jsonResponse);
        dbcategories = parser.getDbPediaCategories();

        Set<String> keys = dbcategories.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaThread> threadList = new ArrayList<PediaThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBCategory> threadCategories = new ArrayList<DBCategory>();

        // Add relationship

        for (String key : keys) {
            if (i % 1000 == 0) {
                System.out.println("i " + i);
            }

            DBCategory cat = dbcategories.get(key);
            threadCategories.add(cat);

            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaThread thread = new PediaThread(parser, urlReader,
                        dbcategories, threadCategories);
                thread.start();
                threadList.add(thread);
                threadCategories = new ArrayList<DBCategory>();
            }

            i++;
        }

        System.out.println("STARTING THREADS JOIN...");
        int categoriesWithoutParents = 0;
        for (PediaThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBCategory cat : thread.getThreadCategories()) {
                    if (cat.getParents().isEmpty()) {
//                        System.err.println("La catégorie " + cat.getUri()  + " n'a pas de parents.");
                        categoriesWithoutParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + categoriesWithoutParents + " catégories sans parents.");
    }
}

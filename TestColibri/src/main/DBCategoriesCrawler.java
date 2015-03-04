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
        ArrayList<PediaCategoryThread> threadList = new ArrayList<PediaCategoryThread>();
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
                PediaCategoryThread thread = new PediaCategoryThread(parser, urlReader, threadCategories);
                thread.start();
                threadList.add(thread);
                threadCategories = new ArrayList<DBCategory>();
            }

            i++;
        }

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
//                        System.err.println("La catégorie " + cat.getUri()  + " n'a pas de parents.");
                        categoriesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        int categoriesWithParents2 = 0;
        for (String key : dbcategories.keySet()) {
            DBCategory cat = dbcategories.get(key);
            System.out.println(cat);
            if (cat.getParentsNumber() != 0) {
                categoriesWithParents2++;
            }
        }
        System.out.println("PROGRAMME TERMINE : " + categoriesWithParents + " catégories avec parents.");
        System.out.println("PROGRAMME TERMINE : " + categoriesWithParents2 + " catégories avec parents.");
        System.out.println(dbcategories.keySet().size());
    }
}
